# Issue 002 — Frontend React consumindo a API

**Data:** 2026-11-06
**Branch:** `feat/frontend-projetos`
**Objetivo:** exibir a lista de projetos na tela, buscando os dados reais de `GET /api/projects`, com estados de carregando e de erro.

## O que o usuário precisa conseguir fazer

Abrir `http://localhost:5173` e ver os projetos que estão no PostgreSQL, sem digitar nada no terminal. Se o backend estiver fora do ar, precisa ver uma mensagem de erro em vez de uma tela vazia ou travada.

## Conceitos ensinados

- **Componente React**: função com inicial maiúscula que devolve JSX.
- **JSX**: sintaxe parecida com HTML; `{}` interpola JavaScript.
- **`map`**: transforma uma lista de dados em uma lista de elementos.
- **`key`**: identifica cada item para o React.
- **Props**: dados que o componente pai passa para o filho; o filho não altera.
- **State (`useState`)**: dado que sobrevive à re-renderização e cuja mudança dispara nova renderização.
- **`useEffect`**: executa código depois da renderização; o array de dependências controla quando.
- **`fetch`**: requisição HTTP no navegador; devolve uma `Promise`.
- **`response.ok`**: `true` para status 2xx.
- **`response.json()`**: converte o corpo JSON em objeto JavaScript (também assíncrono).
- **`try / catch / finally`**: garante que `loading` vire `false` mesmo em erro.
- **Proxy do Vite**: repassa `/api` para o backend em desenvolvimento, evitando CORS.
- **CORS / origem**: protocolo + host + porta; origens diferentes exigem autorização explícita.

## Decisões técnicas

| Decisão | Alternativa descartada | Motivo |
|---|---|---|
| Vite + React + TypeScript | Next.js | Menos conceitos ao mesmo tempo (sem SSR nem rotas). |
| `frontend/` separado de `backend/` | Tudo dentro de `backend/` | São aplicações independentes, com ferramentas e build próprios. |
| `fetch('/api/projects')` relativo | `http://localhost:8080/...` | Funciona em dev (proxy) e em produção (reverse proxy), sem CORS. |
| Estado no `App`, prop para `ProjectList` | `fetch` dentro do `ProjectList` | O componente de lista fica só de apresentação, fácil de reutilizar e testar. |
| Três estados (`projects`, `loading`, `error`) | Só `projects` | Sem `loading` e `error`, a tela não distingue "vazio" de "carregando" ou "falhou". |
| PostgreSQL local em `~/.local/share/devtrack/pgdata` | Cluster em `/tmp` | `/tmp` foi limpo e o banco se perdeu. |

## Erros reais e como foram resolvidos

### 1. Nomes errados no `map`

Primeira versão do `App.tsx`:

```tsx
<li key={Projects}>
  <strong>{DevTrack}</strong>
  <p>{Projetos}</p>
</li>
```

Erro: `Projects`, `DevTrack` e `Projetos` não existem. Dentro do `map`, a variável é `project` (singular), e as propriedades são `project.id`, `project.name`, `project.description`.

Aprendizado: em JSX, `<strong>DevTrack</strong>` mostra o texto; `<strong>{DevTrack}</strong>` tenta ler uma variável.

### 2. `export default Ap`

Faltava a letra final. O `export` precisa do mesmo nome da função.

### 3. `catch (err)` sem usar `err`

O build passou, mas a variável não era usada. Orientação: usar `catch {` ou então usar o erro com `console.error(err)`, separando mensagem de usuário de diagnóstico técnico.

### 4. Ambiente derrubado pelo reinício do servidor

Os processos de PostgreSQL, backend e frontend rodavam em background e foram cancelados. Também surgiu `npm: comando não encontrado`, porque o Node é instalado via **fnm** e o PATH não é carregado em shell não interativo.

Solução: `eval "$(fnm env)"` e reiniciar os três processos.

## Verificações executadas

```
pg_isready -h localhost -p 5433            -> aceitando conexões
curl http://localhost:8080/api/projects    -> 200 + JSON com 1 projeto
curl http://localhost:5173/api/projects    -> 200 + mesmo JSON (proxy funcionando)
npm run build                              -> ✓ built (tsc -b sem erros)
```

**Estado de erro:** confirmado pelo relato do estudante — apareceu "Não foi possivel carregar os projetos" com a rede em modo offline.

**Estado de carregando:** não observado diretamente pelo agente; a mensagem "Carregando..." existe no código, mas é exibida por poucos milissegundos na máquina local. Pode ser vista com a rede em "Slow 3G" no DevTools.

## Perguntas praticadas

1. O que é um componente React? — *acertou: função que devolve JSX.*
2. Qual a diferença entre HTML estático e React? — *acertou a essência: React troca só o que mudou.*
3. O que aparece na tela em três exemplos de JSX? — *acertou os três.*
4. O que é state? — *"memória do componente": essência correta, faltou explicar que variáveis comuns são recriadas a cada render.*
5. Por que `useEffect` com `[]`? — *sabia do risco de loop, mas não explicou os três casos do array de dependências.*
6. Por que `fetch('/api')` e não `http://localhost:8080`? — *sabia que o backend não será local, mas não citou reverse proxy em produção nem CORS em desenvolvimento.*

## O que revisar

- Array de dependências do `useEffect`: sem array, `[]` e `[valor]`.
- Ciclo: render → DOM → efeito → `setState` → novo render.
- `StrictMode` executa efeitos duas vezes em desenvolvimento.
- Proxy é só de desenvolvimento; em produção, reverse proxy (Nginx) ou CORS.
- O tipo TypeScript não valida o JSON em runtime.
- Próximo passo: separar `api.ts` com a função `fetchProjects()`, testar erro e carregando de forma visível, e escrever um formulário de criação de projeto.
