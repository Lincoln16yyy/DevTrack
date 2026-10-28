# ISSUE-001 — Cadastrar e listar projetos

Status: **API de projetos e testes de Controller concluídos; frontend e teste de integração pendentes**
Data: 2026-10-22
Última atualização: 2026-10-27

## Objetivo da fatia

Permitir criar um projeto pelo navegador, persistir no PostgreSQL e vê-lo na lista após recarregar a página. É a menor fatia que conecta tela → API → banco → teste.

## Conceitos ensinados nesta aula

### 1. O caminho de uma requisição

`React (fetch) → Vite proxy /api → Controller → Service → Repository → PostgreSQL`

- **Controller**: traduz HTTP (verbo, path, query, corpo, headers) para uma chamada Java. Não deve conter regra de negócio nem SQL.
- **Service**: regras de negócio e fronteira transacional. É onde ficam validações que dependem de dados (ex.: nome duplicado) e orquestração.
- **Repository**: acesso a dados. Com Spring Data JPA, a interface é implementada em tempo de execução.
- **Banco**: garante integridade (NOT NULL, UNIQUE, FK) mesmo que a aplicação tenha bug.

Por que separar: teste isolado de cada parte, troca de tecnologia de persistência sem reescrever HTTP, e leitura mais fácil em code review. O custo é mais arquivos e mais indireção — aceitável a partir do momento em que existe mais de uma regra.

### 2. HTTP e REST

Recurso: `projects`. Coleção em `/api/projects`, item em `/api/projects/{id}`.

| Situação | Status | Por quê |
|---|---|---|
| Listou projetos | `200 OK` | A requisição foi entendida e há representação do recurso |
| Criou projeto | `201 Created` + header `Location` | O recurso passou a existir em uma URI própria |
| Nome vazio/curto/longo | `400 Bad Request` | O cliente enviou dados que não satisfazem o contrato |
| JSON malformado | `400 Bad Request` | O corpo não pôde ser interpretado |
| Erro inesperado | `500 Internal Server Error` | Falha do servidor, não do cliente |

`201` + `Location` comunica ao cliente *onde* o recurso recém-criado está, sem ele precisar adivinhar o id. `200` seria aceitável na prática, mas perde essa informação.

`400` vs `422`: `422 Unprocessable Content` é usado por algumas APIs para “sintaxe correta, conteúdo inválido”. É defendível, porém `400` é mais comum em APIs Spring e suficiente aqui. O importante é ser consistente e documentar.

### 3. DTO × entidade JPA

- **Entidade** (`@Entity`): mapeia a tabela, tem identidade e é gerenciada pelo Hibernate dentro de uma transação.
- **DTO** (`record ProjectRequest`, `ProjectResponse`): o contrato da API.

Não devolver a entidade diretamente evita: expor colunas internas, quebrar o JSON por causa de relacionamentos lazy e permitir que o cliente grave campos que não deveria (over-posting). Também desacopla o banco do contrato público.

### 4. JPA, identidade e transação

- `bigint generated always as identity` (ou `BIGSERIAL`): simples, índice menor, bom para chave interna.
- `UUID`: útil quando o id é exposto e não deve ser sequencial (não revela volume, facilita geração no cliente). Custo: 16 bytes, leitura menos amigável.

Escolha inicial: `bigint identity`. O id é detalhe interno; se depois precisarmos expor algo não sequencial, discutimos.

`@Transactional` no service define onde começa e termina a transação. Sem ela, cada operação do repository abre sua própria transação e o `flush` acontece em momento imprevisível.

### 5. Flyway × `ddl-auto=update`

- `ddl-auto=update`: o Hibernate altera o banco a partir das entidades. Rápido para protótipo, mas não versiona, não é revisável e pode gerar alterações destrutivas ou silenciosas.
- **Flyway**: cada mudança é um arquivo SQL versionado (`V1__create_projects.sql`, `V2__...`). O banco de qualquer ambiente é reproduzível a partir do zero, e a mudança aparece no code review.

Custo: um arquivo a mais por mudança e disciplina de nunca editar migration já aplicada. Benefício: previsibilidade e histórico — exatamente o que se espera em produção.

### 6. Validação

Bean Validation (`@NotBlank`, `@Size(max = 120)`) no DTO, com `@Valid` no controller. Falha → `400` com o campo e a mensagem, via `ProblemDetail`/handler.

Validação de formato no DTO; validação que depende do banco (nome único, existência de projeto) no service, porque precisa consultar estado.

### 7. Testcontainers × H2

- **H2 em modo PostgreSQL**: rápido, sem Docker, mas não é o Postgres real — tipos, constraints, funções e planos divergem.
- **Testcontainers**: sobe um `postgres:16` real no teste e derruba no fim. Mais lento (segundos) e exige Docker, mas testa o SQL que realmente roda.

Para esta fatia, o teste de integração usa Testcontainers. Teste unitário puro do service pode usar mock do repository quando houver regra complexa.

### 8. React: carregamento e formulário

- `fetch` dentro de `useEffect` com array de dependências vazio roda depois da primeira renderização; colocar no corpo do componente dispara a cada render (loop).
- Estados explícitos: `loading`, `error`, `data`. A tela precisa tratar os três, não só o caminho felizes.
- Formulário controlado: `value` + `onChange` + estado; o React é a fonte da verdade do input.
- **Proxy do Vite**: em desenvolvimento, `/api` é redirecionado para `localhost:8080`, então o navegador enxerga uma única origem e não há CORS. Em produção, isso vira configuração de proxy/reverse proxy ou CORS explícito.

### 9. Alternativas e trade-offs

| Decisão | Escolha | Alternativa | Custo/benefício |
|---|---|---|---|
| Persistência | Spring Data JPA | JdbcTemplate / Spring Data JDBC | JPA esconde SQL e facilita CRUD; JDBC dá controle e SQL explícito |
| Build backend | Maven | Gradle | Maven já instalado; Gradle é mais rápido e flexível, mas não está no ambiente |
| Frontend | Vite + React + TS | Next.js | Vite é foco em SPA e menos conceitos; Next adiciona SSR/rotas, útil depois |
| Organização | Monorepo `backend/` + `frontend/` | Dois repositórios | Monorepo simplifica issue, CI e deploy inicial |
| Id | `bigint identity` | UUID | Menor e simples vs. não sequencial e gerável no cliente |

## Como um entrevistador poderia perguntar

- “Por que a regra de negócio não fica no controller?”
- “Qual a diferença entre 400, 404 e 500? Dê um exemplo de cada.”
- “Por que usar DTO em vez de serializar a entidade?”
- “O que acontece se você usar `ddl-auto=update` em produção?”
- “Por que o `fetch` não pode ficar no corpo do componente React?”
- “O que é uma transação e onde ela começa no seu código?”
- “Por que testar com Postgres real em vez de H2?”

## Reavaliação e mudança de plano (2026-10-22)

O estudante informou que não conhece os assuntos e que as respostas anteriores reproduziram o texto da aula. A implementação da issue 001 fica **pausada** até concluirmos os fundamentos.

Sequência acordada, um bloco curto por sessão:

1. **HTTP e web** — cliente, servidor, URL, requisição, resposta, métodos, status, headers, body, JSON.
2. **SQL** — tabela, linha, coluna, tipos, chave primária, `CREATE TABLE`, `INSERT`, `SELECT`.
3. **Java e Spring Boot** — classe, objeto, método, anotação, controller, service, repository.
4. **PostgreSQL + Flyway** — migration, `ddl-auto=validate`.
5. **React e TypeScript** — componente, JSX, estado, renderização, `useEffect`, `fetch`.
6. **Teste automatizado** — o que é um teste, JUnit, Testcontainers.
7. **Retomada da issue 001**, com o estudante escrevendo as primeiras versões.

Cada bloco termina com perguntas e um exercício pequeno. Nada de código da aplicação antes de o bloco correspondente ser concluído.

## Exercício do Lincoln (adiado para o bloco de SQL)

Escrever à mão o conteúdo de `V1__create_projects.sql` para a tabela `projects`, com:
- chave primária;
- nome obrigatório e com limite;
- descrição opcional;
- data de criação preenchida pelo banco.

Só será pedido depois de estudarmos tabela, tipos e constraints. Depois eu reviso linha por linha.

## Perguntas da aula (aguardando respostas)

1. A regra “não permitir dois projetos com o mesmo nome” deve ficar no controller, no service ou no banco? Por quê?
2. Por que responder `201` com `Location` em vez de `200` ao criar um projeto?
3. Qual o risco concreto de devolver a entidade JPA direto no JSON?
4. Por que Flyway em vez de deixar o Hibernate criar/alterar as tabelas?
5. Por que `fetch` no corpo do componente React causa problema, e como `useEffect` resolve?

## Avaliação das respostas (2026-10-22)

| # | Resposta do Lincoln | Avaliação |
|---|---|---|
| 1 | Unicidade no controller por causa de `@Valid` | **Incorreta.** `@Valid` valida formato (obrigatório, tamanho); unicidade exige consulta ao banco e pertence ao service, com constraint no banco como defesa final |
| 2 | `201` indica criação; `200` indica `Location` | **Parcial.** Acertou que `201` é criação; errou o papel do `Location`, que é header da resposta `201`, não o significado de `200` |
| 3 | Vazamento de colunas, lazy e over-posting | **Correta em essência**, reproduzindo a explicação da aula |
| 4 | Flyway permite reproduzir; não sabe o que é Hibernate | **Parcial.** Reproducibilidade correta; falta a relação JPA → Hibernate → `ddl-auto` |
| 5 | `useEffect` roda após a primeira renderização; três estados | **Correta em essência.** Faltou explicar o loop causado por `fetch` + `setState` no corpo |

### Correções ensinadas

- `@Valid` no controller + DTO cobre **formato**. Unicidade é regra de negócio e estado do banco → **service**, com `UNIQUE` no banco. Existe corrida entre “consultar e inserir”; a constraint é a autoridade final.
- `201 Created` é o status; `Location` é um **header** que acompanha o `201` e informa a URI do recurso criado. `200` é sucesso genérico.
- **JPA** é a especificação (anotações/interfaces); **Hibernate** é a implementação que gera o SQL. `ddl-auto` é propriedade do Hibernate: `update` altera o schema; com Flyway usamos `validate`, que só confere se o mapeamento bate com o banco.
- No React, `fetch` + `setState` no corpo do componente dispara render → efeito → render. `useEffect` com `[]` roda uma vez após a montagem; com dependências, roda quando elas mudam.

### Decisão de escopo

Unicidade de nome fica **fora da issue 001** e entra na 002 como `V2__...`, justamente para praticar o fluxo de migrations.

## Esqueleto Spring Boot criado e verificado (2026-10-24)

Arquivos: `.gitignore`, `backend/pom.xml`, `backend/src/main/java/com/devtrack/DevTrackApplication.java`, `backend/src/main/java/com/devtrack/health/HealthController.java`, `backend/src/main/resources/application.properties`.

Verificação executada:

```
mvn -q -DskipTests package
java -jar target/devtrack-api-0.0.1-SNAPSHOT.jar
curl http://localhost:8080/api/health   -> ok
```

Log confirmou `Tomcat started on port 8080` e `Started DevTrackApplication`. Endpoint `/api/health` é temporário e será removido quando o endpoint real de projetos existir.

## Banco local verificado (2026-10-24)

`docker-compose.yml` sobe `postgres:16` com volume e healthcheck. A porta do host é **5433**, porque já existe um PostgreSQL do sistema escutando em 5432 (`postgresql.service`); o container continua usando 5432 internamente.

Verificações executadas:

```
docker compose up -d db        -> devtrack-db ... (healthy)
docker compose exec db psql -U devtrack -d devtrack -c "select version();"
  -> PostgreSQL 16.15
docker compose exec db psql -U devtrack -d devtrack -c "\dt"
  -> Did not find any relations  (banco vazio, como esperado)
git check-ignore -v .env       -> .env ignorado pelo .gitignore
```

Credenciais locais ficam em `.env` (não versionado). `.env.example` é versionado.

## Migration aplicada (2026-10-25)

O Docker daemon parou de iniciar por falha de `iptables`/`nftables` no sistema (`chain PREROUTING` ausente). Como alternativa local, foi iniciado um cluster PostgreSQL isolado em `/tmp/opencode/pgdata`, na porta 5433, sem root. O `docker-compose.yml` continua no projeto para uso futuro.

Arquivos criados/alterados:
- `backend/src/main/resources/db/migration/V1__create_projects.sql` (SQL do Lincoln, com `GENERATED ALWAYS` corrigido)
- `backend/pom.xml` (data-jpa, flyway-core, flyway-database-postgresql, driver PostgreSQL)
- `backend/src/main/resources/application.properties` (datasource, Flyway, `ddl-auto=validate`, `open-in-view=false`)

Verificação executada:

```
mvn -q -DskipTests package
java -jar target/devtrack-api-0.0.1-SNAPSHOT.jar   (com variáveis do .env)
curl /api/health -> ok
Flyway: Successfully applied 1 migration ... now at version v1
\dt -> projects, flyway_schema_history
select ... from flyway_schema_history -> 1 | 1 | create projects | t
```

Aviso registrado: o cluster local é PostgreSQL 18.6; a versão do Flyway suporta até 17. A migration foi aplicada com sucesso, mas o ambiente oficial do projeto (compose) usa PostgreSQL 16, que é suportado.

## Entidade JPA verificada (2026-10-25)

`Project.java` criado em `com.devtrack.project` com `@Entity`, `@Table(name = "projects")`, `@Id`, `@GeneratedValue(IDENTITY)`, `@Column`, construtor sem argumentos e `createdAt = Instant.now()`. O IntelliJ compilou e executou; o Hibernate inicializou sem erro de validação, confirmando que o mapeamento bate com a migration.

Observação de ambiente: a execução do IntelliJ usou **Java 27** (`.jdks/openjdk-27`), enquanto o projeto declara Java 21. Funcionou, mas o SDK deve ser ajustado para 21 para bater com o Maven e evitar incompatibilidades.

## API de Projetos verificada (2026-10-26)

Container `devtrack-db` (Postgres 16) iniciado via Docker Compose na porta 5433.
O Spring Boot inicializou, o Flyway aplicou a migration `V1__create_projects.sql` e o Hibernate validou o mapeamento da entidade `Project`.

Arquivos criados/ajustados:
- `backend/src/main/java/com/devtrack/project/ProjectRepository.java` (Spring Data JPA)
- `backend/src/main/java/com/devtrack/project/ProjectResponse.java` (DTO record de saída)
- `backend/src/main/java/com/devtrack/project/ProjectRequest.java` (DTO record de entrada com `@NotBlank` e `@Size`)
- `backend/src/main/java/com/devtrack/project/ProjectService.java` (`findAll` com streams e `create` com `@Transactional`)
- `backend/src/main/java/com/devtrack/project/ProjectController.java` (`GET /api/projects` e `POST /api/projects` com `201 Created` e `Location`)
- `backend/pom.xml` (adicionado `spring-boot-starter-validation`)

Verificações executadas:
```
curl -i http://localhost:8080/api/projects
  -> 200 OK, []
curl -i -X POST http://localhost:8080/api/projects -H "Content-Type: application/json" -d '{"name": ""}'
  -> 400 Bad Request (validação @NotBlank funcionou)
curl -i -X POST http://localhost:8080/api/projects -H "Content-Type: application/json" -d '{"name": "DevTrack", "description": "Diário de projetos e estudos"}'
  -> 201 Created, Location: http://localhost:8080/api/projects/1, body com id e createdAt
curl -i http://localhost:8080/api/projects
  -> 200 OK, [{"id":1,"name":"DevTrack", ...}]
```

## Testes automatizados de Controller (2026-10-27)

Arquivo criado: `backend/src/test/java/com/devtrack/project/ProjectControllerTest.java`.

Testes implementados:

| Teste | O que verifica |
|---|---|
| `deveListarProjetos` | `GET /api/projects` → `200`, JSON com `id`, `name` e `description` vindos do mock. |
| `deveRetornar400QuandoNomeVazio` | `POST` com `name` vazio → `400`, sem chamar o Service (o `@Valid` barra antes; comprovado com `verifyNoInteractions(service)`). |
| `deveCriarProjetoQuandoDadosValidos` | `POST` válido → `201`, header `Location`, corpo com `id`/`name`/`description` e `verify` de que o Controller chamou `service.create(...)`. |

Configuração:

- `@WebMvcTest(ProjectController.class)` — sobe apenas a fatia web; não carrega JPA, Flyway, Hikari nem PostgreSQL.
- `@MockitoBean ProjectService service` — substitui o Service real por um dublê.
- `MockMvc` — simula a requisição HTTP em memória, sem Tomcat na porta 8080.

Verificação executada:

```text
mvn -B test
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

O teste rodou com o PostgreSQL desligado. O log não teve `HikariPool`, `Flyway` nem `Connection refused`, confirmando que a fatia web não depende do banco.

Correções feitas durante a aula:

- O import correto de `@WebMvcTest` é `org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest` (com `.servlet`).
- O import de `@MockitoBean` é `org.springframework.test.context.bean.override.mockito.MockitoBean`.
- A forma correta do stub é `when(service.metodo(any(...))).thenReturn(...)`; o `when` envolve a chamada ao mock, não só o argumento.
- O `verify` precisa do método: `verify(service).create(any(ProjectRequest.class))`.
- `jsonPath("$[0].name")` protege o contrato do JSON: se o campo virar `title`, o teste falha.
- `verifyNoInteractions(service)` comprova que o Service não foi tocado no teste de `400`.
- Import não usado (`jakarta.validation.Valid`) removido do arquivo de teste.

Pendências:

- Verificar o **valor** do header `Location`, não só a existência.
- Teste de unidade do `ProjectService` com mock do `ProjectRepository`.
- Teste de integração com PostgreSQL real (Testcontainers) quando o Docker estiver estável.

## Registro de evidências

| Conceito | Nível | Evidência |
|---|---:|---|
| HTTP e APIs REST | 2 | Explicou com precisão 400 (erro do cliente por validação) vs 500 (erro do servidor se estourasse constraint do banco); entendeu DTO de entrada sem id/createdAt para evitar over-posting |
| Camadas (controller/service/repository) | 2 | Explicou a separação de DTO vs Entidade para não vazar dados do banco; explicou injeção por construtor garantindo imutabilidade e facilidade em testes unitários; implementou fluxo Controller -> Service -> Repository |
| JPA / Hibernate | 2 | Explicou o funcionamento interno do Spring Data gerando dynamic proxy que delega para o EntityManager traduzir em SQL |
| Java e Spring Boot | 3 | Escreveu DTOs com record (ProjectResponse, ProjectRequest); implementou stream().map() no Service e @Transactional; integrou @Valid e @PostMapping com ServletUriComponentsBuilder |
| SQL e Flyway | 2 | Explicou Flyway; migration V1 aplicada com sucesso pelo Flyway no PostgreSQL |
| Docker e ambiente local | 2 | Subiu container devtrack-db via Docker Compose com volume e porta mapeada |
| React e TypeScript | — | Próxima etapa |
| Testes automatizados | 2 | Explicou por que `@WebMvcTest` não precisa do PostgreSQL; entendeu stub e `verify`; escreveu os testes de GET 200, POST 400 e POST 201 com orientação |
