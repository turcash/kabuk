# Prebuilt APKs

The signed builds that were running on the reference phone. They are Android
packages (arm64-v8a, minSdk 31) and need no Google services.

| File | Version | Size |
|---|---|---|
| `kabuk-client-1.1.1.apk` | 1.1.1 | 6.2 MB |
| `kabuk-server-1.0.0.apk` | 1.0.0 | 640 KB |

Both are signed with the original project key. Android only installs an update
over an existing copy when the signing key matches, so keep these files if you
are updating a phone that already runs Kabuk. The manifests keep their original
package ids; the recovered sources in `../client` and `../server` use the
`com.kabuk` namespace.

## Verify

```sh
sha256sum *.apk
```

```text
0b41e8dfb5fdbc9d9cbd091ef48101eac30caeb42302077b8a5a9c34134bb254  kabuk-client-1.1.1.apk
073490008f20f0da1a7756a1c791088d2a8c405345c975f18d3bc005f8913d90  kabuk-server-1.0.0.apk
```

## Install

```sh
adb install -r kabuk-server-1.0.0.apk   # on the phone that becomes the server
adb install -r kabuk-client-1.1.1.apk   # on the phone or tablet you type on
```

The server app asks for root once, then deploys the runtime and offers the same
commands as the CLI. See [08-agent.md](../../docs/08-agent.md),
[09-client-app.md](../../docs/09-client-app.md) and
[10-server-app.md](../../docs/10-server-app.md).
