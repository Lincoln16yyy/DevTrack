# DevTrack — diário de projetos e estudo para desenvolvedores

Projeto de portfólio full-stack desenvolvido com apoio do OpenCode, seguindo o contrato de aprendizagem descrito em [`AGENTS.md`](AGENTS.md).

> **Nome provisório.** “DevTrack” foi mantido apenas porque já está no repositório e no remote; o escopo do produto ainda pode mudar.

## Objetivo

Construir uma aplicação realista que exercite, de forma incremental, competências relevantes para vagas de estágio e desenvolvedor júnior: React, TypeScript, Java, Spring Boot, PostgreSQL, APIs REST, Git e testes automatizados.

O recorte proposto: registrar projetos de desenvolvimento, suas tarefas e sessões de estudo (tempo e anotações), com relatórios simples de tempo por projeto.

## Regra de trabalho

O agente ensina os conceitos necessários antes de implementar, faz perguntas e espera respostas, propõe um plano e só prossegue após autorização explícita. Em conceitos recém-estudados, o estudante escreve a primeira versão.

## Estado atual

- [x] Pacote inicial copiado para o repositório
- [ ] Ideia e escopo inicial aprovados
- [ ] Primeira issue aprovada
- [ ] Ambiente de desenvolvimento verificado
- [ ] Primeira funcionalidade implementada e testada
- [ ] Aprendizado registrado

Não marque itens como concluídos sem evidência.

## Estrutura

```
.
├── AGENTS.md                 # contrato de aprendizagem
├── PROMPT_INICIAL_OPENCODE.md
├── docs/
│   ├── architecture/         # decisões técnicas (ADR simplificado)
│   ├── learning/             # diário de aprendizagem e níveis de domínio
│   └── roadmap.md
├── backend/                  # API Java + Spring Boot (esqueleto criado)
└── frontend/                 # React + TypeScript (a criar)
```
