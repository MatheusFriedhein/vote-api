# Assembly Voting API

Projeto em **Java 17 + Spring Boot** para gerenciamento de agendas, abertura de sessões, recebimento de votos e apuração final.

A solução foi mantida como **API REST pura**, sem implementar o Anexo 1 de telas dinâmicas, conforme o ajuste solicitado. Os requisitos centrais do desafio e os **bônus 1, 2 e 3** foram contemplados com foco em simplicidade, clareza de código e boa base para evolução.

## Requisitos atendidos

- Cadastro de nova agenda
- Abertura de sessão com duração informada ou **1 minuto por padrão**
- Registro de votos **SIM/NÃO**
- Um associado pode votar **apenas uma vez por agenda**
- Persistência em **PostgreSQL**
- Versionamento de API em `/api/v1`
- Documentação em **Swagger/OpenAPI**
- Endpoints protegidos com **Spring Security (HTTP Basic)**
- Migrations com **Flyway**
- Estrutura em **controller / service / repository / model**

## Implementações

### Integração com sistema externo

A API integra com `https://user-info.herokuapp.com/users/{cpf}` para validar se o associado está **ABLE_TO_VOTE** ou **UNABLE_TO_VOTE**.

O cliente externo foi implementado com:

- sanitização de CPF antes da chamada
- tratamento explícito para `404` (CPF inválido/não encontrado)
- tratamento para indisponibilidade do serviço externo
- timeouts configuráveis
- desligamento via configuração para facilitar execução local

### Performance

Para evitar custo de apuração por `COUNT` em cenários com grande volume de votos, a aplicação mantém **contadores agregados dentro da própria sessão de votação**:

- `total_yes`
- `total_no`
- `total_votes`

Esses contadores são atualizados a cada voto com um `UPDATE` incremental no banco. Assim, o endpoint de resultado não precisa contar novamente todos os registros da tabela `votes`.

Também foram incluídos:

- constraint única em `(agenda_id, associate_id)`
- índices para leitura rápida por agenda
- `@Version` na sessão para melhor controle de concorrência
- tratamento de `DataIntegrityViolationException` para cenários de corrida
- parâmetros Hibernate básicos de batch e ordenação de inserts/updates
- Actuator habilitado para health/metrics

### Versionamento da API

A estratégia principal adotada foi **versionamento por URI**:

- `/api/v1/agendas`

Além disso, a aplicação aceita opcionalmente o header:

- `X-API-Version: 1`
- `X-API-Version: v1`

Essa abordagem combina uma rota explícita, simples para clientes mobile e integração, com um cabeçalho opcional que facilita futura evolução e observabilidade de consumo por versão.

## Stack

- Spring Boot 3.3.x
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- Spring Boot Actuator
- PostgreSQL
- Flyway
- Springdoc OpenAPI

## Como executar

### 1. Subir o PostgreSQL

Exemplo com Docker:

```bash
docker run --name assembly-postgres \
  -e POSTGRES_DB=assembly_voting \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 -d postgres:16
```

### 2. Executar a aplicação

```bash
mvn spring-boot:run
```

### 3. Swagger

- UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### 4. Actuator

- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`

## Credenciais da API

- usuário: `admin`
- senha: `admin123`

## Endpoints principais

### Criar agendas

```http
POST /api/v1/agendas
Authorization: Basic ...
X-API-Version: v1
Content-Type: application/json

{
  "title": "Aprovação do orçamento anual",
  "description": "Deliberação sobre o orçamento de 2026"
}
```

### Abrir sessão

```http
POST /api/v1/agendas/1/sessoes
Authorization: Basic ...
X-API-Version: v1
Content-Type: application/json

{
  "durationInMinutes": 5
}
```

> Se o body vier vazio, a sessão abre por 1 minuto.

### Registrar voto

```http
POST /api/v1/agendas/1/votos
Authorization: Basic ...
X-API-Version: v1
Content-Type: application/json

{
  "associate": {
    "name": "João da Silva",
    "cpf": "12345678901"
  },
  "vote": "SIM"
}
```

### Resultado

```http
GET /api/v1/agendas/1/resultado
Authorization: Basic ...
X-API-Version: v1
```

## Configuração da integração externa

Por padrão a validação externa fica desligada para facilitar execução local.

```properties
integration.user-info.enabled=false
integration.user-info.base-url=https://user-info.herokuapp.com
integration.user-info.connect-timeout-ms=3000
integration.user-info.read-timeout-ms=3000
```

Para ativar:

```properties
integration.user-info.enabled=true
```

## Observações de modelagem

- `Agenda`: agenda da assembleia
- `VotingSession`: sessão da agenda, com janela de abertura/fechamento e contadores agregados
- `Associate`: associado identificado por CPF
- `Vote`: voto do associado para uma agenda

## Testes

O projeto possui testes unitários para validar regras de voto e apuração.

## Executar com Docker

Com Docker e Docker Compose instalados, basta rodar:

```bash
docker compose up --build
```

Isso sobe:
- PostgreSQL em `localhost:5432`
- API em `http://localhost:8080`

Para derrubar os containers:

```bash
docker compose down
```

Para derrubar removendo também o volume do banco:

```bash
docker compose down -v
```

Endpoints úteis após subir:
- Swagger: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`
- Health: `http://localhost:8080/actuator/health`

A aplicação usa um `Dockerfile` multi-stage, compilando com Maven dentro do container e executando o JAR em uma imagem JRE menor.
