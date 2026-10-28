# Glossário — termos conforme aparecem nas aulas

> Só entram aqui termos já explicados. Linguagem simples primeiro; o termo técnico depois.

## Aula 1 — Cliente, servidor e HTTP (2026-10-22)

**Cliente** — programa que pede algo. O navegador é o cliente mais comum.
**Servidor** — programa que recebe pedidos, processa e devolve respostas. Nossa API será um servidor.
**HTTP** — conjunto de regras que cliente e servidor usam para conversar. Define o formato do pedido e da resposta.
**Requisição (request)** — a mensagem que o cliente envia: método + caminho + cabeçalhos + corpo.
**Resposta (response)** — a mensagem que o servidor devolve: código de status + cabeçalhos + corpo.
**URL** — endereço do recurso. Exemplo: `http://localhost:8080/api/projects/42`
  - `http` = esquema (protocolo)
  - `localhost` = host (máquina)
  - `8080` = porta (qual programa na máquina)
  - `/api/projects/42` = caminho (qual recurso)
**Método HTTP** — verbo que diz a intenção: `GET` (ler), `POST` (criar), `PUT` (substituir), `PATCH` (alterar parte), `DELETE` (apagar).
**Status HTTP** — número de 3 dígitos que resume o resultado. Famílias: `2xx` sucesso, `3xx` redirecionamento, `4xx` erro do cliente, `5xx` erro do servidor.
**Cabeçalho (header)** — informação extra sobre a mensagem. Ex.: `Content-Type: application/json` diz “o corpo está em JSON”; `Location` diz onde um recurso criado ficou.
**Corpo (body)** — o conteúdo em si. Pode estar vazio (caso comum em `GET`).
**JSON** — formato de texto para representar dados: `{ "name": "DevTrack" }`. Não é exclusivo de JavaScript.
**API** — conjunto de endpoints que um servidor expõe para outros programas usarem.
**Endpoint** — combinação de método + caminho. Ex.: `POST /api/projects`.
**REST** — estilo de organizar a API em torno de recursos (substantivos) e usar os métodos HTTP para agir sobre eles.
**CORS** — regra do navegador que bloqueia chamadas entre origens diferentes (ex.: `localhost:5173` chamando `localhost:8080`) sem permissão explícita do servidor.
**Proxy** — intermediário que repassa a requisição. Em desenvolvimento, o Vite pode repassar `/api` para a API Java, evitando CORS.

## Aula 2 — SQL: tabelas, colunas e linhas (2026-10-22)

**Banco de dados** — programa que guarda dados de forma organizada e responde a consultas.
**Banco relacional** — banco que organiza dados em tabelas relacionadas entre si. PostgreSQL é um.
**Tabela** — conjunto de dados sobre um mesmo tipo de coisa (ex.: todos os projetos).
**Coluna** — uma característica da tabela, com nome e tipo (ex.: `name` do tipo texto).
**Linha (registro)** — um item completo da tabela (ex.: um projeto específico).
**Célula** — o cruzamento de uma linha com uma coluna; o valor propriamente dito.
**NULL** — ausência de valor. Não é zero, não é texto vazio; é “não informado”.
**Chave primária (primary key)** — coluna que identifica cada linha de forma única e não pode ser nula.
**Tipo de dado** — o que a coluna aceita: número inteiro, texto, data, verdadeiro/falso etc.
**BIGINT** — número inteiro grande, usado para identificadores.
**VARCHAR(n)** — texto com limite máximo de `n` caracteres.
**TEXT** — texto sem limite definido.
**TIMESTAMPTZ** — data e hora com fuso horário; guarda o instante exato.
**Comando SQL (statement)** — uma instrução terminada em `;`. Ex.: `CREATE TABLE ...;`
**CREATE TABLE** — comando que cria uma tabela, com suas colunas, tipos e restrições.
**Restrição (constraint)** — regra que o banco faz cumprir. Ex.: `NOT NULL`, `PRIMARY KEY`, `DEFAULT`, `UNIQUE`.
**DEFAULT** — valor usado automaticamente quando o `INSERT` não informa a coluna.
**now()** — função do PostgreSQL que devolve o instante atual.
**IDENTITY** — geração automática de números pelo banco, usada no `id`.
**BOOLEAN** — tipo que aceita `true`/`false` (verdadeiro/falso).
**Erro de sintaxe** — o banco não conseguiu nem interpretar o comando. A mensagem mostra a linha e um `^` embaixo do trecho problemático.
**Violação de constraint** — o comando foi entendido, mas desrespeitou uma regra (`NOT NULL`, `UNIQUE`, `PRIMARY KEY`).
**INSERT** — comando que adiciona uma linha à tabela.
**VALUES** — lista de valores que serão inseridos, na ordem das colunas informadas.
**RETURNING** — faz o `INSERT` devolver as colunas da linha criada (inclusive as geradas pelo banco).
**Sequência (IDENTITY)** — contador usado pelo banco para gerar o próximo `id`. Um `INSERT` que falha ainda consome um número.
**`relation does not exist`** — erro do PostgreSQL dizendo que a tabela (ou índice) com aquele nome não existe. Normalmente é erro de digitação.
**SELECT** — comando que lê dados de uma ou mais tabelas.
**`*` no SELECT** — “todas as colunas”. Cômodo para explorar; em código de aplicação é melhor listar as colunas necessárias.
**WHERE** — filtra quais linhas entram no resultado.
**ORDER BY** — define a ordem do resultado; `DESC` inverte para o maior/mais recente primeiro.

## Aula 3 — Java: classes, objetos e métodos (2026-10-23)

**Classe** — molde que descreve quais dados (campos) e quais comportamentos (métodos) um tipo de coisa tem. Não é o carro; é a planta do carro.
**Objeto (instância)** — algo concreto criado a partir da classe, com valores próprios. `new Car("Gol", 2015)` cria um objeto.
**Campo (atributo)** — variável dentro da classe que guarda o estado do objeto. “Rodas” e “portas” são campos, não objetos.
**Método** — comportamento da classe: uma função com nome, parâmetros e tipo de retorno.
**Construtor** — trecho executado ao criar o objeto, usado para preencher os campos iniciais. Tem o mesmo nome da classe e não declara tipo de retorno.
**`this`** — referência ao próprio objeto; usado para distinguir o campo do parâmetro de mesmo nome.
**`new`** — palavra que cria um objeto a partir de uma classe.
**`private`** — o campo só pode ser acessado de dentro da própria classe (encapsulamento).
**`public`** — visível de fora da classe.
**`null`** — ausência de referência a um objeto; um `id` ainda não salvo pode ser `null`.
**Anotação (`@`)** — metadado que dá instruções a ferramentas/frameworks. `@Override` avisa o compilador que o método sobrescreve outro; `@Entity` (depois) dirá ao JPA que a classe vira tabela.
**`Instant`** — classe do Java que representa um instante (data + hora em UTC), equivalente ao `TIMESTAMPTZ`.
**Pacote (package)** — agrupamento de classes relacionadas. `java.time` é o pacote das classes de data/hora.
**`import`** — declara que o arquivo vai usar uma classe de outro pacote, evitando escrever o nome completo toda vez.
**Valor padrão** — o que um campo recebe se o construtor não atribuir nada: `null` para objetos (`String`, `Long`, `Instant`), `0` para `int`, `false` para `boolean`.
**Getter** — método que apenas devolve o valor de um campo privado (ex.: `getName()`), permitindo leitura controlada de fora da classe.
**Tipo primitivo** — tipo básico do Java que guarda valor direto e nunca é `null`: `long`, `int`, `double`, `boolean`, `char`.
**Wrapper** — classe que embrulha um tipo primitivo e permite `null`: `Long`, `Integer`, `Double`, `Boolean`.
**Autoboxing** — conversão automática entre `long` e `Long` (e vice-versa) feita pelo compilador.
**Concatenação com `null`** — em Java, `"texto" + null` resulta na string `"texto - null"`, porque o valor é convertido com `String.valueOf`.
**Operador ternário** — `condição ? valorSeVerdadeiro : valorSeFalso`, usado para escolher entre dois valores em uma linha.

## Aula 4 — Framework, Spring Boot e injeção de dependência (2026-10-24)

**Biblioteca** — código que você chama quando quer. Ex.: uma classe de datas.
**Framework** — código que chama o seu. Ele controla o fluxo principal e invoca seus trechos nos momentos certos (“não nos chame, nós chamamos você”).
**Spring Boot** — framework Java que sobe um servidor HTTP embutido (Tomcat), escaneia as classes anotadas e monta a aplicação.
**Bean** — objeto criado e gerenciado pelo Spring, em vez de você usar `new`.
**Contêiner (ApplicationContext)** — registro onde o Spring guarda os beans e resolve quem depende de quem.
**Injeção de dependência (DI)** — o Spring entrega ao seu objeto as dependências de que ele precisa, em vez de o objeto criá-las com `new`.
**`@SpringBootApplication`** — marca a classe principal; liga a configuração automática e o escaneamento de componentes.
**`@RestController`** — diz ao Spring que a classe responde a requisições HTTP.
**`@Service`** — marca uma classe de regra de negócio como bean.
**`@Repository`** — marca uma classe de acesso a dados como bean.
**`@GetMapping("/caminho")`** — liga um método a requisições `GET` naquele caminho.
**Tomcat** — servidor HTTP embutido que o Spring Boot inicia; é ele que escuta a porta 8080.
**Reflexão (reflection)** — capacidade do Java de inspecionar classes e anotações em tempo de execução; é como o Spring descobre os beans.

## Aula 5 — Docker e banco local (2026-10-24)

**Imagem** — modelo imutável de um programa e suas dependências. `postgres:16` é a imagem do PostgreSQL 16.
**Container** — instância em execução criada a partir de uma imagem. Pode ser criado, parado e removido.
**Volume** — armazenamento fora do container, para os dados sobreviverem à remoção do container.
**Mapeamento de porta** — `5433:5432` significa “porta 5433 da máquina → porta 5432 dentro do container”.
**Healthcheck** — comando periódico que diz se o container está pronto (`pg_isready` no caso do Postgres).
**Docker Compose** — arquivo YAML que descreve serviços, redes e volumes de uma aplicação local.
**`.env`** — arquivo local com variáveis e credenciais; não é versionado.
**`docker compose down`** — para e remove containers e rede, mas **mantém** os volumes.
**`docker compose down -v`** — remove também os volumes, apagando os dados.
**YAML** — formato de texto para configuração baseado em indentação (espaços), não em chaves. É o formato do `docker-compose.yml`.
**Variável de ambiente** — valor que o programa lê do sistema operacional (ou do `.env`), em vez de estar fixo no código.
**`${VARIAVEL:-valor}`** — sintaxe do Compose para “use a variável; se não existir, use este valor padrão”.
**`${VARIAVEL:?mensagem}`** — “exija a variável; se não existir, pare com esta mensagem de erro”.
**Migration** — arquivo SQL versionado que descreve uma mudança no schema.
**`flyway_schema_history`** — tabela criada pelo Flyway que registra versões aplicadas, data e checksum.
**Checksum** — “impressão digital” do conteúdo de uma migration; se o arquivo muda depois de aplicado, o Flyway detecta.
**`ddl-auto=validate`** — o Hibernate não altera o schema; apenas confere se as entidades batem com as tabelas.
**`ddl-auto=update`** — o Hibernate altera o schema sozinho para caber nas entidades; não versiona nem revisa.
**Fail fast** — deixar a aplicação falhar na inicialização quando há inconsistência, em vez de esconder o problema e quebrar depois.

## Aula 6 — Entidade JPA (2026-10-25)

**Entidade** — classe Java anotada com `@Entity` que representa uma tabela; cada objeto representa uma linha.
**`@Entity`** — diz ao JPA/Hibernate que a classe é uma entidade persistente.
**`@Table(name = "...")`** — define explicitamente o nome da tabela.
**`@Id`** — marca o campo que é a chave primária.
**`@GeneratedValue`** — indica que o banco gera o valor da chave.
**`GenerationType.IDENTITY`** — usa a coluna `GENERATED ALWAYS AS IDENTITY` do banco.
**`@Column`** — ajusta o mapeamento do campo: nome da coluna, nulabilidade, tamanho.
**Construtor sem argumentos** — o JPA exige um construtor vazio para criar o objeto ao ler do banco; pode ser `protected`.
**`JpaRepository`** — interface do Spring Data que já traz `save`, `findAll`, `findById`, `delete` etc.
**`@Service`** — marca a classe de regra de negócio como bean.
**`@RequestMapping("/api/projects")`** — prefixo comum de caminho para os métodos do controller.
**HikariCP** — pool de conexões JDBC usado por padrão no Spring Boot; reaproveita conexões abertas.
**EntityManagerFactory** — objeto criado pelo Hibernate que lê o mapeamento das entidades e produz `EntityManager`s para acessar o banco.
**Unidade de persistência (persistence unit)** — conjunto de entidades e configurações que o Hibernate gerencia; no nosso caso, a `default`.
**`spring.datasource.url`** — configuração do JDBC usada pelo Spring para abrir conexões com o banco.

## Aula 7 — Testes automatizados de Controller (2026-10-27)

**Teste automatizado** — programa que executa uma verificação e falha sozinho quando o resultado não é o esperado. Roda com `mvn test`, sem depender de teste manual.
**JUnit 5** — framework de testes do Java. Descobre métodos anotados com `@Test` e executa cada um.
**`@Test`** — marca um método como caso de teste.
**MockMvc** — simulador de requisições HTTP do Spring. Monta a requisição em memória e passa pelo `DispatcherServlet`, sem abrir a porta 8080.
**`@WebMvcTest`** — fatia de teste que carrega apenas o Controller, o MVC, Jackson e Bean Validation. Não carrega JPA, Flyway, DataSource nem Tomcat.
**Fatia de teste (slice)** — teste que sobe só uma parte do contexto Spring, em vez da aplicação inteira.
**Mock (dublê)** — objeto falso que substitui uma dependência real durante o teste.
**`@MockitoBean`** — cria um mock e o registra no contexto do teste, substituindo o bean real.
**Stub** — configuração da resposta de um mock: “quando este método for chamado, devolva este valor”.
**`when(...).thenReturn(...)`** — forma do Mockito de criar um stub.
**`verify(...)`** — confirma que um método do mock foi chamado.
**`verifyNoInteractions(...)`** — confirma que nenhum método do mock foi chamado.
**Matcher `any(...)`** — argumento genérico usado em stubs e verifies: “qualquer objeto deste tipo”.
**`jsonPath(...)`** — asserção que lê o corpo JSON pelo caminho: `$` é a raiz, `[0]` é o primeiro item, `.name` é o campo.
**`status().isOk()` / `isCreated()` / `isBadRequest()`** — asserções de status HTTP 200, 201 e 400.
**`header().exists("Location")`** — verifica a presença de um header na resposta.
**Teste de unidade** — testa uma classe isolada, sem banco nem rede.
**Teste de integração** — testa várias camadas juntas, geralmente com banco real.
**Regressão** — bug que volta a aparecer depois de uma mudança; o teste automatizado serve para impedir.
**Pirâmide de testes** — estratégia de ter muitos testes rápidos de unidade, alguns de integração e poucos ponta a ponta.

## Ainda não ensinado

Componente React, JSX, estado, `useEffect` e `fetch` no frontend; teste de integração com PostgreSQL real (Testcontainers); `ArgumentCaptor` e recursos avançados do Mockito; CI, autenticação e deploy. Entram no glossário quando chegarmos lá.
