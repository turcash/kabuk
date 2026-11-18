'use strict';
// Kabuk Ajan: Claude Code'u sohbet olarak sunan kucuk HTTP sunucusu (bagimliliksiz Node.js).
// - Yalniz tailnet adresinde dinler; her istegi 'tailscale whois' ile dogrular (yalniz tailnet sahibi).
// - Her sohbet = bir proje dizininde bir Claude Code oturumu (claude -p --resume ...).
// - Olaylar ~/.kabuk-agent/chats/<id>.jsonl'a yazilir: baglanti kopsa da is surer, geri gelince
//   kacirilan olaylar aynen aktarilir (SSE ?after=N).
const http = require('http');
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const { spawn, execFile } = require('child_process');

const HOME = process.env.HOME || '/home/dev';
const DATA = path.join(HOME, '.kabuk-agent');
const CHATS = path.join(DATA, 'chats');
const PROJ = path.join(HOME, 'projeler');
const BIND = process.env.BIND || '127.0.0.1';
const PORT = +process.env.PORT || 8787;
const TS = ['--socket=/run/tailscale/tailscaled.sock'];
const MODES = { plan: 'plan', edit: 'acceptEdits', auto: 'auto', full: 'bypassPermissions' };

for (const d of [DATA, CHATS, PROJ]) fs.mkdirSync(d, { recursive: true, mode: 0o700 });
// Servisler OOM'dan korunur (-900); ajan ve baslattigi claude surecleri normal is sureci (0).
try { fs.writeFileSync('/proc/self/oom_score_adj', '0'); } catch (_) {}
const INDEX = path.join(DATA, 'index.json');
let index = { chats: {} };
try { index = JSON.parse(fs.readFileSync(INDEX, 'utf8')); } catch (_) {}
for (const c of Object.values(index.chats)) if (c.status === 'running') c.status = 'interrupted';
function saveIndex() {
  fs.writeFileSync(INDEX + '.tmp', JSON.stringify(index), { mode: 0o600 });
  fs.renameSync(INDEX + '.tmp', INDEX);
}

// ---- olay gunlugu -----------------------------------------------------------
const logs = new Map();      // id -> [entry]
const listeners = new Map(); // id -> Set(res)
const runs = new Map();      // id -> child
function logOf(id) {
  if (!logs.has(id)) {
    let arr = [];
    try {
      arr = fs.readFileSync(path.join(CHATS, id + '.jsonl'), 'utf8').split('\n').filter(Boolean).map(l => JSON.parse(l));
    } catch (_) {}
    logs.set(id, arr);
  }
  return logs.get(id);
}
function push(id, entry) {
  const arr = logOf(id);
  entry.i = arr.length; entry.ts = Date.now();
  arr.push(entry);
  fs.appendFileSync(path.join(CHATS, id + '.jsonl'), JSON.stringify(entry) + '\n', { mode: 0o600 });
  for (const res of listeners.get(id) || []) sse(res, entry);
}
function sse(res, entry) { res.write(`id: ${entry.i}\ndata: ${JSON.stringify(entry)}\n\n`); }

// ---- yetki: yalniz tailnet sahibi -----------------------------------------------
let ownerId = null;
const whoCache = new Map();
function tsJson(args) {
  return new Promise(ok => execFile('tailscale', [...TS, ...args], { timeout: 5000 }, (e, out) => {
    try { ok(e ? null : JSON.parse(out)); } catch (_) { ok(null); }
  }));
}
async function owner() {
  if (ownerId == null) { const s = await tsJson(['status', '--json']); ownerId = s && s.Self ? s.Self.UserID : null; }
  return ownerId;
}
async function allowed(req) {
  const ip = (req.socket.remoteAddress || '').replace(/^::ffff:/, '');
  if (ip === '127.0.0.1' || ip === '::1') return true;
  const hit = whoCache.get(ip);
  if (hit && hit.t > Date.now() - 600000) return hit.ok;
  const [w, o] = await Promise.all([tsJson(['whois', '--json', ip]), owner()]);
  const ok = !!(w && w.Node && o != null && w.Node.User === o);
  whoCache.set(ip, { ok, t: Date.now() });
  return ok;
}

// ---- on ayarlar (eklenti setleri) ve ajanlar ---------------------------------------
// Tum eklentiler kurulu ama global olarak kapali (caveman haric); her sohbet kendi setini
// '--settings {"enabledPlugins":…}' ile acar. Boylece hafif sohbetler ucuz kalir.
let PRESETS = {};
try { PRESETS = JSON.parse(fs.readFileSync(path.join(__dirname, 'presets.json'), 'utf8')); } catch (_) {}
function presetPlugins(id) {
  const p = PRESETS[id]; if (!p) return [];
  if (p.plugins === '*') {
    try { return fs.readFileSync(path.join(HOME, '.kabuk-pluginlist.txt'), 'utf8').split(/\s+/).filter(Boolean); } catch (_) { return []; }
  }
  return p.plugins || [];
}
function presetSettings(id) {
  return JSON.stringify({ enabledPlugins: Object.fromEntries(presetPlugins(id).map(x => [x, true])) });
}
const agentCache = {};
function agentsFor(id) {
  if (agentCache[id]) return agentCache[id];
  let inst = {};
  try { inst = JSON.parse(fs.readFileSync(path.join(HOME, '.claude/plugins/installed_plugins.json'), 'utf8')).plugins || {}; } catch (_) {}
  const out = [];
  for (const full of ['caveman@caveman', ...presetPlugins(id)]) {
    const e = (inst[full] || [])[0]; if (!e || !e.installPath) continue;
    const dir = path.join(e.installPath, 'agents');
    let files = []; try { files = fs.readdirSync(dir).filter(f => f.endsWith('.md')); } catch (_) { continue; }
    for (const f of files) {
      const txt = fs.readFileSync(path.join(dir, f), 'utf8');
      const fm = (txt.match(/^---\n([\s\S]*?)\n---/) || [])[1] || '';
      const name = (fm.match(/^name:\s*["']?([^"'\n]+)/m) || [])[1] || f.replace(/\.md$/, '');
      const desc = ((fm.match(/^description:\s*["']?([^\n]+)/m) || [])[1] || '').replace(/["']$/, '').slice(0, 140);
      out.push({ id: full.split('@')[0] + ':' + name.trim(), plugin: full.split('@')[0], desc });
    }
  }
  return (agentCache[id] = out.sort((a, b) => a.id.localeCompare(b.id)));
}

// ---- claude calistirma --------------------------------------------------------
function startRun(chat, text, mode) {
  const args = ['-p', text, '--output-format', 'stream-json', '--verbose', '--permission-mode', MODES[mode] || 'auto'];
  if (chat.sessionId) args.push('--resume', chat.sessionId);
  if (chat.model) args.push('--model', chat.model);
  args.push('--settings', presetSettings(chat.preset || 'hafif'));
  if (chat.agent) args.push('--agent', chat.agent);
  const cwd = path.join(PROJ, chat.project);
  push(chat.id, { t: 'user', text, mode });
  const child = spawn('claude', args, {
    cwd, detached: true, stdio: ['ignore', 'pipe', 'pipe'],
    env: { ...process.env, KABUK_AGENT: '1', KABUK_CHAT: chat.title || '' },
  });
  runs.set(chat.id, child);
  chat.status = 'running'; chat.mode = mode; chat.updated = Date.now(); saveIndex();
  let buf = '';
  child.stdout.on('data', d => {
    buf += d;
    let n;
    while ((n = buf.indexOf('\n')) >= 0) {
      const line = buf.slice(0, n).trim(); buf = buf.slice(n + 1);
      if (!line) continue;
      let ev; try { ev = JSON.parse(line); } catch (_) { push(chat.id, { t: 'err', text: line }); continue; }
      if (ev.type === 'system' && ev.subtype === 'init' && ev.session_id) { chat.sessionId = ev.session_id; saveIndex(); }
      if (ev.type === 'result') {
        chat.last = { ok: !ev.is_error, cost: ev.total_cost_usd, ms: ev.duration_ms, denials: (ev.permission_denials || []).length };
      }
      push(chat.id, { t: 'ev', ev: slim(ev) });
    }
  });
  let errBuf = '';
  child.stderr.on('data', d => { errBuf += d; });
  child.on('close', code => {
    runs.delete(chat.id);
    if (errBuf.trim()) push(chat.id, { t: 'err', text: errBuf.trim().slice(-2000) });
    push(chat.id, { t: 'end', code });
    chat.status = code === 0 ? 'done' : (chat.stopped ? 'stopped' : 'error');
    chat.stopped = false; chat.updated = Date.now(); saveIndex();
  });
}

// Buyuk alanlari kirp (telefona gereksiz yuk gitmesin)
function slim(ev) {
  if (ev.type === 'system') return { type: 'system', subtype: ev.subtype, session_id: ev.session_id, model: ev.model, cwd: ev.cwd, permissionMode: ev.permissionMode };
  if (ev.type === 'user' && ev.message && Array.isArray(ev.message.content)) {
    ev.message.content = ev.message.content.map(c => {
      if (c.type !== 'tool_result') return c;
      let txt = typeof c.content === 'string' ? c.content
        : Array.isArray(c.content) ? c.content.map(x => x.text || (x.type === 'image' ? '[resim]' : '')).join('\n') : '';
      if (txt.length > 6000) txt = txt.slice(0, 3000) + '\n…\n' + txt.slice(-2500);
      return { type: 'tool_result', tool_use_id: c.tool_use_id, is_error: !!c.is_error, content: txt };
    });
    delete ev.tool_use_result;
  }
  return ev;
}

// ---- HTTP -------------------------------------------------------------------
function json(res, code, obj) {
  res.writeHead(code, { 'content-type': 'application/json; charset=utf-8', 'cache-control': 'no-store' });
  res.end(JSON.stringify(obj));
}
function body(req) {
  return new Promise((ok, no) => {
    let b = ''; req.on('data', d => { b += d; if (b.length > 1e6) no(new Error('cok buyuk')); });
    req.on('end', () => { try { ok(b ? JSON.parse(b) : {}); } catch (e) { no(e); } });
  });
}
const safeName = s => /^[A-Za-z0-9._-]{1,64}$/.test(s) && !s.startsWith('.');
function projects() {
  return fs.readdirSync(PROJ, { withFileTypes: true }).filter(d => d.isDirectory() && !d.name.startsWith('.'))
    .map(d => {
      const p = path.join(PROJ, d.name);
      let mtime = 0; try { mtime = fs.statSync(p).mtimeMs; } catch (_) {}
      return { name: d.name, git: fs.existsSync(path.join(p, '.git')), mtime };
    }).sort((a, b) => b.mtime - a.mtime);
}
function listChats() {
  return Object.values(index.chats).sort((a, b) => b.updated - a.updated).map(c => {
    const arr = logOf(c.id); let preview = '';
    for (let k = arr.length - 1; k >= 0 && !preview; k--) {
      const e = arr[k];
      if (e.t === 'ev' && e.ev.type === 'result') preview = (e.ev.result || '').slice(0, 140);
      else if (e.t === 'user') preview = 'Sen: ' + e.text.slice(0, 120);
    }
    return { ...c, preview, running: runs.has(c.id) };
  });
}

const STATIC = path.join(__dirname, 'index.html');
const server = http.createServer(async (req, res) => {
  try {
    if (!(await allowed(req))) return json(res, 403, { error: 'yalniz tailnet sahibi' });
    const u = new URL(req.url, 'http://x');
    const p = u.pathname.split('/').filter(Boolean);
    if (req.method === 'GET' && (u.pathname === '/' || u.pathname === '/index.html')) {
      res.writeHead(200, { 'content-type': 'text/html; charset=utf-8', 'cache-control': 'no-store' });
      return fs.createReadStream(STATIC).pipe(res);
    }
    if (p[0] !== 'api') return json(res, 404, { error: 'yok' });

    if (p[1] === 'projects' && req.method === 'GET') return json(res, 200, projects());
    if (p[1] === 'meta' && req.method === 'GET') {
      const presets = Object.entries(PRESETS).map(([id, v]) => ({ id, label: v.label, desc: v.desc, tokens: v.tokens, count: presetPlugins(id).length }));
      const agents = Object.fromEntries(Object.keys(PRESETS).map(id => [id, agentsFor(id)]));
      return json(res, 200, { presets, agents });
    }
    if (p[1] === 'projects' && req.method === 'POST') {
      const b = await body(req);
      if (!safeName(b.name || '')) return json(res, 400, { error: 'gecersiz proje adi (harf, rakam, . _ -)' });
      const dir = path.join(PROJ, b.name);
      if (fs.existsSync(dir)) return json(res, 409, { error: 'zaten var' });
      const run = (cmd, args, cwd) => new Promise(ok => execFile(cmd, args, { cwd, timeout: 300000 }, (e, o, er) => ok({ e, er })));
      if (b.clone) {
        const r = await run('git', ['clone', '--', b.clone, dir], PROJ);
        if (r.e) return json(res, 500, { error: 'git clone: ' + (r.er || r.e.message).slice(-400) });
      } else {
        fs.mkdirSync(dir, { mode: 0o755 });
        await run('git', ['init', '-q'], dir);
      }
      return json(res, 200, { ok: true });
    }

    if (p[1] === 'chats' && !p[2] && req.method === 'GET') return json(res, 200, listChats());
    if (p[1] === 'chats' && !p[2] && req.method === 'POST') {
      const b = await body(req);
      if (!b.project || !fs.existsSync(path.join(PROJ, b.project))) return json(res, 400, { error: 'proje yok' });
      if (!b.text || !b.text.trim()) return json(res, 400, { error: 'mesaj bos' });
      const id = crypto.randomBytes(6).toString('hex');
      const chat = { id, project: b.project, title: b.text.trim().split('\n')[0].slice(0, 60), mode: MODES[b.mode] ? b.mode : 'auto',
        model: b.model || '', sessionId: null, status: 'new', created: Date.now(), updated: Date.now(),
        preset: PRESETS[b.preset] ? b.preset : 'hafif',
        agent: b.agent && agentsFor(PRESETS[b.preset] ? b.preset : 'hafif').some(a => a.id === b.agent) ? b.agent : '' };
      index.chats[id] = chat; saveIndex();
      startRun(chat, b.text, chat.mode);
      return json(res, 200, chat);
    }
    const chat = p[2] && index.chats[p[2]];
    if (p[1] === 'chats' && p[2] && !chat) return json(res, 404, { error: 'sohbet yok' });

    if (chat && p[3] === 'stream' && req.method === 'GET') {
      res.writeHead(200, { 'content-type': 'text/event-stream; charset=utf-8', 'cache-control': 'no-store', connection: 'keep-alive' });
      const after = +(u.searchParams.get('after') ?? -1);
      res.write(`event: chat\ndata: ${JSON.stringify({ ...chat, running: runs.has(chat.id) })}\n\n`);
      for (const e of logOf(chat.id)) if (e.i > after) sse(res, e);
      if (!listeners.has(chat.id)) listeners.set(chat.id, new Set());
      listeners.get(chat.id).add(res);
      const ka = setInterval(() => res.write(': ka\n\n'), 25000);
      req.on('close', () => { clearInterval(ka); listeners.get(chat.id).delete(res); });
      return;
    }
    if (chat && p[3] === 'send' && req.method === 'POST') {
      if (runs.has(chat.id)) return json(res, 409, { error: 'ajan hala calisiyor' });
      const b = await body(req);
      if (!b.text || !b.text.trim()) return json(res, 400, { error: 'mesaj bos' });
      startRun(chat, b.text, MODES[b.mode] ? b.mode : chat.mode);
      return json(res, 200, { ok: true });
    }
    if (chat && p[3] === 'stop' && req.method === 'POST') {
      const ch = runs.get(chat.id);
      if (ch) { chat.stopped = true; try { process.kill(-ch.pid, 'SIGINT'); } catch (_) {}
        setTimeout(() => { if (runs.get(chat.id) === ch) try { process.kill(-ch.pid, 'SIGKILL'); } catch (_) {} }, 5000); }
      return json(res, 200, { ok: true });
    }
    if (chat && !p[3] && req.method === 'PATCH') {
      const b = await body(req);
      if (b.title) chat.title = String(b.title).slice(0, 80);
      if (b.mode && MODES[b.mode]) chat.mode = b.mode;
      saveIndex(); return json(res, 200, chat);
    }
    if (chat && !p[3] && req.method === 'DELETE') {
      if (runs.has(chat.id)) return json(res, 409, { error: 'once durdur' });
      delete index.chats[chat.id]; saveIndex(); logs.delete(chat.id);
      fs.rmSync(path.join(CHATS, chat.id + '.jsonl'), { force: true });
      return json(res, 200, { ok: true });
    }
    return json(res, 404, { error: 'yok' });
  } catch (e) {
    json(res, 500, { error: String(e && e.message || e) });
  }
});
server.listen(PORT, BIND, () => console.log(`kabuk-agent http://${BIND}:${PORT}`));
for (const s of ['SIGTERM', 'SIGINT']) process.on(s, () => {
  for (const ch of runs.values()) try { process.kill(-ch.pid, 'SIGTERM'); } catch (_) {}
  process.exit(0);
});
