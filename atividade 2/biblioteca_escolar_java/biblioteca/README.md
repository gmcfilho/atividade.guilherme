# 📚 Sistema de Biblioteca Escolar — Java

Sistema de controle de empréstimo de livros desenvolvido em Java puro,
executado via terminal/console com menus interativos.

---

## 🗂️ Estrutura do Projeto

```
biblioteca/
├── src/
│   ├── Main.java                        ← Ponto de entrada
│   ├── model/
│   │   ├── Livro.java                   ← Entidade Livro
│   │   ├── Aluno.java                   ← Entidade Aluno
│   │   └── Emprestimo.java              ← Entidade Empréstimo
│   ├── service/
│   │   ├── LivroService.java            ← Regras de negócio de livros
│   │   ├── AlunoService.java            ← Regras de negócio de alunos
│   │   └── EmprestimoService.java       ← Regras de empréstimo/devolução
│   ├── exception/
│   │   ├── LivroIndisponivelException.java
│   │   └── DadosInvalidosException.java
│   └── ui/
│       ├── Console.java                 ← Utilitários de I/O
│       ├── MenuLivros.java
│       ├── MenuAlunos.java
│       ├── MenuEmprestimos.java
│       └── MenuConsultas.java
├── compilar_e_executar.sh               ← Linux / macOS
├── compilar_e_executar.bat              ← Windows
└── README.md
```

---

## ✅ Pré-requisito

- **Java 17 ou superior** instalado.
- Verifique com: `java -version` e `javac -version`
- Download: https://adoptium.net

---

## ▶️ Como Compilar e Executar

### Linux / macOS

```bash
chmod +x compilar_e_executar.sh
./compilar_e_executar.sh
```

### Windows

Dê duplo clique em `compilar_e_executar.bat`  
ou execute no Prompt de Comando:

```
compilar_e_executar.bat
```

### Manualmente (qualquer SO)

```bash
# Na pasta raiz do projeto:
mkdir out
javac -d out -sourcepath src src/Main.java src/model/*.java src/service/*.java src/exception/*.java src/ui/*.java
java -cp out Main
```

---

## 🚀 Funcionalidades

### 📚 Livros
- Cadastrar com título, autor, gênero e quantidade
- Validação: título não pode ser vazio; quantidade mínima de 1
- Listar todos, listar disponíveis, buscar por ID
- Indicação de estoque: disponível / baixo / indisponível

### 👥 Alunos
- Cadastrar com nome, turma e matrícula
- Visualizar pendências de cada aluno
- Histórico completo de empréstimos por aluno

### 📋 Empréstimos e Devoluções
- Registrar empréstimo (valida disponibilidade antes)
- Impede emprestar o mesmo livro duas vezes ao mesmo aluno
- Registrar devolução (atualiza estoque automaticamente)
- Listar todos / listar em aberto

### 🔍 Consultas
- Livros atualmente emprestados (agrupados por título)
- Alunos com livros em aberto (com lista dos livros)
- Painel geral: totais de livros, exemplares, empréstimos

---

## 🏗️ Arquitetura

O projeto segue o padrão em camadas:

```
UI (menus) → Service (regras de negócio) → Model (entidades)
```

- **Model**: entidades puras sem dependências externas
- **Service**: lógica de negócio, validações, gerenciamento de estado
- **UI**: leitura do console, exibição de menus, interação com o usuário
- **Exception**: exceções específicas do domínio

---

## 📝 Regras de Negócio Implementadas

| Regra | Implementação |
|-------|--------------|
| Título vazio proibido | `DadosInvalidosException` em `LivroService` |
| Quantidade negativa/zero proibida | `DadosInvalidosException` em `LivroService` |
| Livro sem estoque não pode ser emprestado | `LivroIndisponivelException` em `EmprestimoService` |
| Mesmo livro não pode ser emprestado 2x ao mesmo aluno | Verificação em `EmprestimoService` |
| Devolução atualiza estoque automaticamente | Método `registrarDevolucao()` em `Emprestimo` |
| Nome de aluno obrigatório | `DadosInvalidosException` em `AlunoService` |
