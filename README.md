# App Estacionamento Backend

Backend Java com Spring Boot e MySQL para receber sincronizacoes remotas do app de estacionamento.

## O que este backend resolve

- Permite usar o seu computador como servidor local ou remoto.
- Disponibiliza um endpoint de health check para o app verificar se o servidor esta online.
- Recebe snapshots completos do app e salva cada sincronizacao no MySQL.
- Deduplica por `deviceName + snapshotHash`, evitando gravacao dupla de dados ja sincronizados.
- Gera relatorio mensal de sincronizacoes por dispositivo.

## Stack

- Java 17
- Spring Boot 3.3
- Spring Web
- Spring Data JPA
- MySQL 8
- Maven Wrapper

## Executando localmente

### 1. Subir o MySQL com Docker

```bash
docker compose up -d
```

### 2. Rodar o backend

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

O backend sobe por padrao na porta `8080`.

## Variaveis de ambiente

- `SERVER_PORT` default `8080`
- `DB_URL` default `jdbc:mysql://localhost:3306/appestacionamento?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo`
- `DB_USERNAME` default `root`
- `DB_PASSWORD` default `root`
- `SYNC_AUTH_HEADER_NAME` default `X-Sync-Token`
- `SYNC_AUTH_TOKEN` sem default. Se estiver vazio, os endpoints protegidos retornam `503`.

## Endpoints principais

### Health check

```http
GET /api/sync/health
```

Resposta:

```json
{
  "status": "UP",
  "serverTime": "2026-04-07T18:30:00Z"
}
```

### Enviar sincronizacao

```http
POST /api/sync/batches
Content-Type: application/json
X-Sync-Token: seu-token-forte
```

Exemplo de payload:

```json
{
  "deviceName": "Terminal Caixa 01",
  "snapshotHash": "2026-04-07-terminal-caixa-01-001",
  "appVersion": "1.0.0",
  "createdAt": "2026-04-07T18:15:00Z",
  "vagas": [],
  "convenios": [],
  "movimentacoes": [],
  "produtos": [],
  "vendas": [],
  "servicos": [],
  "mensalistas": [],
  "rawState": {
    "movimentacoes": [],
    "servicos_realizados": [],
    "qualquer_outra_chave_local": "valor"
  },
  "metadata": {
    "platform": "flutter"
  }
}
```

### Ultima sincronizacao por dispositivo

```http
GET /api/sync/batches/latest?deviceName=Terminal%20Caixa%2001
X-Sync-Token: seu-token-forte
```

### Relatorio mensal

```http
GET /api/sync/reports/monthly?year=2026&month=4
X-Sync-Token: seu-token-forte
```

## Seguranca da sincronizacao

- `GET /api/sync/health` continua publico para o app apenas testar disponibilidade.
- `POST /api/sync/batches`, `GET /api/sync/batches/latest` e `GET /api/sync/reports/monthly` exigem o header configurado em `SYNC_AUTH_HEADER_NAME`.
- Configure o mesmo token no backend e no Flutter usando `SYNC_AUTH_TOKEN` no servidor e `REMOTE_SYNC_TOKEN` no app.

## Estrategia esperada no Flutter

Entre 18h e 20h, o app deve:

1. Chamar `GET /api/sync/health`.
2. Se o servidor estiver online, montar um snapshot com todos os dados locais.
3. Gerar um `snapshotHash` unico para aquele pacote.
4. Enviar para `POST /api/sync/batches`, incluindo o header de autenticacao e tambem `rawState` com o snapshot completo do `SharedPreferences`.
5. Se a resposta indicar `duplicated = true`, considerar aquele pacote como ja sincronizado.

## Exemplo de inicializacao com token

Windows PowerShell:

```powershell
$env:SYNC_AUTH_TOKEN="troque-por-um-token-forte"
./mvnw.cmd spring-boot:run
```
