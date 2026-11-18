# Kabuk Agent

The agent is a single Node.js file (`runtime/rootfs/opt/kabuk-agent/server.js`,
~300 lines, zero npm dependencies) that exposes Claude Code as a chat
application on your tailnet. One HTML file is the whole UI.

```
http://note10:8787
```

## Design constraints

* **No dependencies.** Only Node built-ins (`http`, `fs`, `child_process`,
  `crypto`), so `apk add nodejs` is enough and there is nothing to audit but one
  file.
* **No session store.** Authorisation is re-derived from the peer address on
  every request via `tailscale whois`.
* **Runs survive disconnects.** Claude Code is spawned detached; its JSON events
  are appended to disk before being streamed, so closing the laptop does not
  kill the run and reopening the page replays what you missed.

## Authorisation

```js
const ip = req.socket.remoteAddress.replace(/^::ffff:/, '');
if (ip === '127.0.0.1' || ip === '::1') return true;      // local (ttyd, shell)
const who = await tailscale whois --json <ip>;            // cached 10 minutes
return who.Node.User === (await ownerId());               // tailnet owner only
```

Any request from a device that is not owned by the tailnet owner gets
`403 {"error":"yalniz tailnet sahibi"}`. There is no login form, no token and no
cookie. Removing the device from the tailnet revokes access instantly (the cache
expires after 10 minutes; restart the service to drop it immediately).

## Lifecycle of a chat

1. `POST /api/chats` with `{project, text, mode, preset, model, agent}`.
2. The server creates `<id>` (6 random bytes), stores metadata in
   `~/.kabuk-agent/index.json` and spawns:

   ```sh
   claude -p "<text>" --output-format stream-json --verbose \
          --permission-mode <mode> [--resume <sessionId>] [--model <model>] \
          --settings '{"enabledPlugins":{...}}' [--agent <agent>]
   ```

   with `cwd = ~/projeler/<project>`.
3. Every parsed event is appended to `~/.kabuk-agent/chats/<id>.jsonl` with an
   incrementing `i`, then pushed to every attached SSE listener.
4. When the process exits, `sessionId` is stored so the next message resumes the
   same conversation.

Resuming matters: without `--resume` every message would start from zero context.

## Permission modes

| UI label | `--permission-mode` | Behaviour |
|---|---|---|
| plan | `plan` | read-only planning, no edits, no commands |
| edit | `acceptEdits` | file edits applied without asking, commands still ask |
| auto | `auto` | model decides when to ask |
| full | `bypassPermissions` | never asks — **arbitrary command execution** |

The default is `auto`. `full` is only useful when you are watching the stream and
have `FULL_ROOT=0`.

## Presets (plugin sets)

Claude Code plugins are all installed on the phone but globally disabled except
`caveman`. A chat enables only the set its preset names, via
`--settings {"enabledPlugins":{...}}`. That keeps a quick question cheap: the
preset is part of the cacheable prompt, so its token cost is paid on every
message.

| Preset | Plugins | Approx. prompt tokens |
|---|---|---|
| `hafif` (light) | none, just Claude Code + caveman | ~2 k |
| `gelistirme` (dev) | 20: superpowers, feature-dev, code-review, debugging, TDD, git/PR, JVM, JS/TS, Python, systems, shell, security, context7, frontend/mobile, double-shot-latte | ~9.5 k |
| `tasarim` (design) | 11: Apple HIG, frontend-design, ui-design, brand-landingpage, accessibility, document-skills, modern-web | ~10.6 k |
| `tam` (full) | every installed plugin (87 at the time of writing) | ~50 k |

Presets live in `presets.json` and are read on every message, so editing the
file takes effect for the next message without a restart. `tam` expands to the
contents of `~/.kabuk-pluginlist.txt`, which `kabuk-plugins-setup` writes.

Agents shipped by a plugin are discovered from
`~/.claude/plugins/installed_plugins.json` → `<installPath>/agents/*.md`, with the
`name`/`description` read from the YAML front matter. The UI offers them as a
drop-down limited to the chat's preset.

## HTTP API

| Method | Path | Body / query | Result |
|---|---|---|---|
| GET | `/` | — | the single-page UI |
| GET | `/api/projects` | — | directories under `~/projeler` |
| POST | `/api/projects` | `{name, clone?}` | `git init` or `git clone` |
| GET | `/api/meta` | — | presets + their agents |
| GET | `/api/chats` | — | chat list, newest first |
| POST | `/api/chats` | `{project, text, mode, preset, model, agent}` | creates and starts a chat |
| GET | `/api/chats/<id>/stream` | `?after=N` | SSE: `chat` event + replayed events |
| POST | `/api/chats/<id>/send` | `{text, mode?}` | next turn of the conversation |
| POST | `/api/chats/<id>/stop` | — | `SIGINT` to the process group, `SIGKILL` after 5 s |
| PATCH | `/api/chats/<id>` | `{title?, mode?}` | rename / change mode |
| DELETE | `/api/chats/<id>` | — | deletes the chat and its `.jsonl` |

SSE frames look like:

```
id: 42
data: {"i":42,"t":"assistant","text":"…"}

```

`?after=N` replays only events with `i > N`, which is how a reconnecting browser
loses nothing. A `: ka` comment every 25 s keeps proxies from timing the stream
out.

## Files

```
~/.kabuk-agent/
├── index.json           chat metadata (mode 600)
└── chats/<id>.jsonl     event log, one JSON object per line (mode 600)
```

`index.json` marks chats left in `running` state at startup as `interrupted`,
because the process tree cannot survive a reboot.

## Operating it

```sh
su -c '/data/kabuk/bin/kabuk sv restart kabuk-agent'
su -c '/data/kabuk/bin/kabuk sv status kabuk-agent'
tail -f /data/kabuk/rootfs/var/log/kabuk/kabuk-agent/current
```

Port: `AGENT_PORT` in `kabuk.conf` (default 8787). The service waits for the
tailnet address before starting, so it is never briefly exposed.

## Limitations

* One user. There is no multi-tenant model and no per-chat filesystem isolation:
  every chat runs as `dev` and can read every project.
* No streaming of *your* keystrokes; a message is submitted as a whole.
* `stop` kills the process group of `claude`, which cancels the current turn but
  keeps the session resumable.
* If `claude` is not on `PATH` for the service user, the chat fails at spawn
  time with a `500`.
