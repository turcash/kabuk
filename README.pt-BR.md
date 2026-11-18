# Kabuk

**Transforme um celular Android com root em um servidor Claude Code ligado 24 horas por dia — e acesse de qualquer lugar pelo Tailscale.**

Sem nuvem, sem relay, sem portas abertas. O servidor é o próprio celular: dentro de um
chroot Alpine rodam OpenSSH, mosh, tmux, terminal web, notificações e Claude Code; um
watchdog iniciado pelo Magisk cuida de carga, temperatura e rede; um cliente Android
oferece um terminal de verdade sem Termux e sem root.

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## O que é

| Camada | O que roda |
|---|---|
| Celular (Android 12, Magisk) | CLI `kabuk`, gerenciador de mounts, watchdog, firewall da LAN, limite de carga |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| Cliente | app Android de terminal (mosh/ssh, várias abas) + interfaces web |

Aparelho de referência: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700,
4 GB RAM), Android 12 / MIUI 13, Magisk 31.0, usado como servidor de
desenvolvimento diário nessa configuração.

## Estado

| | |
|---|---|
| Aparelho | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, boot original, SELinux enforcing |
| Ambiente | Alpine 3.24.4 aarch64, `chroot` real, usuário `dev` (uid 8000) |
| Supervisor | runit (`runsvdir`), 6 serviços |
| Rede | somente Tailscale — o firewall da LAN descarta novas conexões em `wlan+`/`eth+` |
| Energia | carga mantida entre 75–80 %, throttling a partir de 40 °C |
| Acesso | ssh, mosh, sftp, taildrop, terminal web (7681), interface do agente (8787) |
| Documentação | 12 capítulos, 12 idiomas |

## Recursos

* **chroot de verdade, não emulação.** `git`, `npm`, `tmux`, PTYs e `rm -rf .git` funcionam como em um Linux normal.
* **Sobrevive a reinício, troca de rede e tela bloqueada.** O Magisk inicia o ambiente antes de você desbloquear; o mosh mantém a sessão ao alternar entre Wi-Fi e LTE; o watchdog corrige a rota do Tailscale em até 30 segundos.
* **Uma interface de chat para o Claude Code.** O `kabuk-agent` inicia `claude` por conversa, envia eventos por SSE e grava em disco antes de transmitir — reconectar não perde nada e a próxima mensagem continua com `--resume`.
* **Pensado para a bateria.** Janela de carga, wakelock só quando há trabalho, cpuset + nice acima de 40 °C, reset do Wi-Fi se a rede cair.
* **Nada exposto.** Todos os serviços escutam na interface do tailnet; o firewall da LAN é a segunda linha; o agente valida cada requisição com `tailscale whois`.
* **Parada de emergência.** `kabuk disable` corta todo acesso remoto mantendo o trabalho em andamento.

## Início rápido

```sh
# no celular, uma vez
# 1. instale Kabuk Server, conceda root, toque em "Kur / Onar"
# 2. entre no tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. adicione sua chave pública
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... voce@notebook"'
# 4. verifique
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# do notebook
ssh dev@note10 -t kabuk-session     # sessão tmux persistente + Claude Code
mosh dev@note10 -- kabuk-session    # o mesmo, sem cair ao trocar de rede
open http://note10:8787               # interface de chat do Claude Code
```

Instruções completas: [docs/03-install.md](docs/03-install.md).

## Estrutura do repositório

```text
runtime/         árvore implantada em /data/kabuk
  bin/           CLI kabuk, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        auxiliares do chroot + agente (server.js, index.html, presets.json)
  sv/            definições de serviço runit
  tests/         stage2.sh
apps/
  client/        recovered Java (client)
  server/        recovered Java (server)
  prebuilt/      signed APK + SHA-256
docs/            01 arquitetura … 12 decisões de projeto
```

## Documentação

| # | Capítulo |
|---|---|
| 01 | [Arquitetura](docs/01-architecture.md) |
| 02 | [Requisitos](docs/02-requirements.md) |
| 03 | [Instalação](docs/03-install.md) |
| 04 | [Referência da CLI](docs/04-cli-reference.md) |
| 05 | [Serviços](docs/05-services.md) |
| 06 | [Acesso remoto](docs/06-remote-access.md) |
| 07 | [Segurança](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [Solução de problemas](docs/11-troubleshooting.md) |
| 12 | [Decisões de projeto](docs/12-design-decisions.md) |

## Segurança em um parágrafo

O celular não recebe atualização de segurança desde abril de 2022, então não pode ser
alcançável a partir de redes não confiáveis. E não é: nenhuma porta de entrada em
Wi-Fi, Ethernet ou dados móveis, nenhuma autenticação por senha, nenhum bind em
`0.0.0.0`, nenhum relay na nuvem. Tudo passa pelo seu tailnet Tailscale, e o agente
revalida cada requisição contra o dono do tailnet. O chroot é uma fronteira de
empacotamento, não uma sandbox — leia [docs/07-security.md](docs/07-security.md) antes
de ativar `FULL_ROOT=1` ou o modo `full` do agente.

## Licença

MIT — veja [LICENSE](LICENSE). Componentes de terceiros (Termux terminal-view, musl,
mosh, OpenSSH, pacotes Alpine, Tailscale, ttyd, ntfy) estão em [NOTICE.md](NOTICE.md)
e mantêm suas próprias licenças.

Sem vínculo com Google, Xiaomi, Magisk ou Anthropic. Root anula a garantia; um celular
ligado 24/7 no carregador é responsabilidade sua.