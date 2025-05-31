# Sistema de Biblioteca - CRUD Completo

## Funcionalidades Implementadas

### 🧑‍🎓 CRUD de Alunos
- **CREATE**: Cadastrar novo aluno com validação de matrícula única
- **READ**: 
  - Listar todos os alunos
  - Buscar por ID, matrícula ou nome (busca parcial)
- **UPDATE**: Atualizar dados do aluno (nome, matrícula, data nascimento)
- **DELETE**: Remover aluno com confirmação

### 📚 CRUD de Livros  
- **CREATE**: Cadastrar novo livro
- **READ**:
  - Listar todos os livros
  - Buscar por ID, título (busca parcial), autor (busca parcial)
  - Listar apenas livros disponíveis (estoque > 0)
- **UPDATE**: Atualizar dados do livro (título, autor, ano, estoque)
- **DELETE**: Remover livro com confirmação

### 📖 CRUD de Empréstimos
- **CREATE**: Registrar novo empréstimo (com controle de estoque automático)
- **READ**:
  - Buscar empréstimo por ID
  - Listar empréstimos pendentes
  - Listar histórico completo
  - Listar empréstimos por aluno específico
  - Listar empréstimos por livro específico
- **UPDATE**: Atualizar dados do empréstimo
- **DELETE**: Remover empréstimo com confirmação

### 🔍 Funcionalidades Extras
- **Registrar devolução**: Marca empréstimo como devolvido e atualiza estoque
- **Verificar disponibilidade**: Consulta se livro está disponível
- **Empréstimos em atraso**: Lista empréstimos vencidos
- **Estatísticas gerais**: Números do sistema
- **Relatórios**: Geral e por período

## Melhorias Implementadas

### Validações
- Matrícula única para alunos (7 caracteres)
- Controle automático de estoque nos empréstimos
- Verificação de existência antes de operações

### Métodos de Busca Otimizados
- **AlunoDAO**: `findByMatricula()`, `findByNome()`, `existsByMatricula()`
- **LivroDAO**: `findByTitulo()`, `findByAutor()`, `findDisponiveis()`, `getEstoque()`
- **EmprestimoDAO**: `findByAluno()`, `findByLivro()`, `update()`, `delete()`

### Interface Melhorada
- Menu reorganizado com numeração sequencial
- Opções de busca múltiplas para cada entidade
- Confirmações para operações de exclusão
- Mensagens de erro mais detalhadas

## Estrutura do Projeto

```
src/
├── controller/
│   └── MenuController.java     # Interface principal
├── service/
│   ├── AlunoService.java       # Lógica de negócio - Alunos
│   ├── LivroService.java       # Lógica de negócio - Livros
│   ├── EmprestimoService.java  # Lógica de negócio - Empréstimos
│   ├── ConsultaService.java    # Consultas especiais
│   └── RelatorioService.java   # Relatórios
├── dao/
│   ├── AlunoDAO.java           # Acesso a dados - Alunos
│   ├── LivroDAO.java           # Acesso a dados - Livros
│   ├── EmprestimoDAO.java      # Acesso a dados - Empréstimos
│   └── RelatorioDAO.java       # Acesso a dados - Relatórios
├── model/
│   ├── Aluno.java              # Entidade Aluno
│   ├── Livro.java              # Entidade Livro
│   └── Emprestimo.java         # Entidade Empréstimo
├── util/
│   └── Database.java           # Conexão com banco
└── Main.java                   # Ponto de entrada
```

## Como Executar

1. Configure o banco MySQL com o schema em `database_schema.sql`
2. Ajuste as credenciais em `src/util/Database.java` se necessário
3. Compile: `javac -d bin -cp "lib/*" src/**/*.java`
4. Execute: `java -cp "bin:lib/*" Main`

## Operações CRUD Disponíveis

### Menu Principal
- **1-5**: CRUD completo de Alunos
- **6-10**: CRUD completo de Livros  
- **11-19**: CRUD completo de Empréstimos + funcionalidades especiais
- **20-22**: Consultas e verificações
- **23-24**: Relatórios

Todas as operações básicas (Create, Read, Update, Delete) estão implementadas para as três entidades principais, com validações, confirmações e métodos de busca otimizados. 