# Security policy

## Scope

This project runs a rooted Android phone as a personal server. It is published
as a reference implementation for a single-user setup, not as a product.

The security model in one line: **no port is opened to the LAN or the internet;
all access is through the owner's Tailscale tailnet.**

Read [docs/07-security.md](docs/07-security.md) before enabling `FULL_ROOT=1`
or the agent's `full` permission mode.

## Please do not

* Do not open issues containing tailnet IPs, node names, SSH public keys,
  `ttyd.cred` contents or anything from `~/.kabuk-agent`.
* Do not run `kabuk disable` on someone else's phone.
* Do not report "rooting voids the warranty" — that is the premise.

## Reporting a vulnerability

Open a GitHub issue with the `security` label, or contact the maintainer
privately if the issue is exploitable in the default configuration. Please
include:

* the file and line,
* whether `FULL_ROOT` was 0 or 1,
* whether the attacker was inside or outside the tailnet.

Expect an honest answer about whether the default configuration is affected;
this is a hobby project with no bounty and no SLA.
