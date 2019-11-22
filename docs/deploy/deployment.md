# How to deploy

## Docker

Die Applikation wir bei jedem Push als latest tag auf dockerhub gepusht.
Momentan ist die Applikation unter https://hub.docker.com/repository/docker/nliechti/tbz_deployer verfügbar.

### Environment zum builden

Um die Applikation auf dockerhub pushen zu können, müssen diese beiden Env variablen während dem Build verfügbar sein

| ENV | Beschreibung |
| --- | --- |
| `DOCKER_USERNAME` | Username auf dockerhub.com |
| `DOCKER_PASSWORD` | Das passwort des Users |

## ENV

### Kubernetes Config

| ENV | Beschreibung |
| --- | --- |
| `KUBERNETES_MASTER_URL` | Die Url unter der die Kubernetes API verfügbar ist |
| `KUBERNETES_USERNAME` | Username mit dem auf die API connected wird |
| `KUBERNETES_CLIENT_CERT_DATA` | Cert data zur authentifizierung. Base64 encoded client.crt file |
| `KUBERNETES_CLIENT_KEY_DATA` | Key data zur authentifizierung. Base64 encoded client.key file|

### Mail Config

Der SMTP server muss TLS beherrschen

| ENV | Beschreibung |
| --- | --- |
| `EMAIL_SMTP_SERVER` | SMTP Server von dem aus die Emails verschickt werden sollen |
| `EMAIL_SMTP_PORT` | Port auf dem die TLS Verbindung angenommen wird |
| `EMAIL_SMTP_USER` | User mit dem connected wird |
| `EMAIL_SMTP_PASSWORD` | Das Passwort für den connection user |
