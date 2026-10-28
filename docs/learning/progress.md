# Progresso de aprendizagem

Atualizar somente com base em evidências observadas nas respostas, exercícios e revisões do estudante.

**Escala:** 1 = reconhece quando explicado · 2 = explica com as próprias palavras · 3 = aplica sem copiar · 4 = defende trade-offs.
**Regra desta sessão:** repetir o texto da aula não conta como nível 2. Nível 1 só com identificação própria, sem consultar.

| Conceito | Nível atual (1–4) | Evidência demonstrada | Lacuna / próxima revisão | Última revisão |
|---|---:|---|---|---|
| Git e versionamento | — | Ainda não avaliado | Avaliar na primeira branch/commit | — |
| HTTP e APIs REST | 2 | Explicou com precisão 400 (erro do cliente por validação) vs 500 (erro do servidor se estourasse constraint do banco); entendeu DTO de entrada sem id/createdAt para evitar over-posting; o teste de 201 verifica a existência do header `Location` | Fixar o papel prático do header Location e verificar seu valor, não só a existência | 2026-10-27 |
| Camadas e arquitetura | 2 | Explicou a separação de DTO vs Entidade para não vazar dados do banco; explicou injeção por construtor garantindo imutabilidade e facilidade em testes unitários; implementou fluxo Controller -> Service -> Repository | Exercitar a fronteira de validação entre DTO e Service | 2026-10-26 |
| Java e Spring Boot | 3 | Escreveu DTOs com record (ProjectResponse, ProjectRequest); implementou stream().map() no Service e @Transactional; explicou como Spring Data gera proxy dinâmico para EntityManager | Implementar @Valid e @PostMapping no controller | 2026-10-26 |
| Docker e ambiente local | 2 | Explicou container como objeto, healthcheck como teste de conexão, `.env` como credenciais não versionadas e porta do host; errou o número (`5533`) e não citou a porta interna | Revisar `5433` × `5432`; entender volume e YAML | 2026-10-25 |
| JPA e modelagem | 2 | Escreveu a entidade `Project` com `@Entity`, `@Table`, `@Id`, `@GeneratedValue(IDENTITY)` e `@Column`; o Hibernate iniciou sem erro de validação | Explicar o ciclo de vida, transações e o SQL gerado; criar o repository | 2026-10-25 |
| SQL e modelagem relacional | 2 | Explicou Flyway e o histórico; reconheceu que editar migration aplicada causa erro; confundiu `validate` com `update` | Fixar: `validate` não altera schema; demonstrar na entidade JPA | 2026-10-25 |
| React e TypeScript | — | Repetiu a explicação de `useEffect`; não sabe definir o que é um efeito | Recomeçar por componente, JSX, estado e renderização | 2026-10-22 |
| Testes automatizados | 2 | Explicou por que o teste com `@WebMvcTest` passa sem PostgreSQL; entendeu o papel do stub (`when(...).thenReturn(...)`), do `verify` e do `verifyNoInteractions`; escreveu, com orientação, os testes de GET 200, POST 400 e POST 201. | Fixar a sintaxe `when(mock.metodo(args))`; distinguir MockMvc de Mock; prever o efeito de uma mudança de contrato no `jsonPath`. Próximo: escrever um teste novo sem esqueleto. | 2026-10-27 |

Não preencher níveis por suposição. “Ainda não avaliado” não significa desconhecimento.

## Histórico

- **2026-10-22 — correção de rota.** O estudante informou que não conhece os assuntos e que reproduziu o texto da aula. Níveis de React, camadas e JPA rebaixados para “não demonstrado”. Plano alterado para fundamentos antes da issue 001.
- **2026-10-22 — Issue 001, aula inicial.** Primeira exposição a HTTP, camadas, JPA/Flyway e React; ritmo inadequado, sem base prévia.
- **2026-10-27 — primeiros testes de Controller.** `ProjectControllerTest` com três casos: GET 200, POST inválido 400 e POST válido 201 + `Location` + `verify`. `mvn test` passou com o PostgreSQL desligado.
