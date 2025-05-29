import dao.AlunoDAO;
import dao.EmprestimoDAO;
import dao.LivroDAO;
import dao.RelatorioDAO;
import model.Aluno;
import model.Livro;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final AlunoDAO      alunoDAO      = new AlunoDAO();
    private static final LivroDAO      livroDAO      = new LivroDAO();
    private static final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private static final RelatorioDAO  relatorioDAO  = new RelatorioDAO();
    private static final Scanner       in            = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("""
                \n===== Biblioteca =====
                1  - Cadastrar aluno
                2  - Listar alunos
                3  - Deletar aluno
                4  - Cadastrar livro
                5  - Listar livros
                6  - Deletar livro
                7  - Registrar empréstimo
                8  - Registrar devolução
                9  - Listar empréstimos pendentes
                10 - Listar histórico de empréstimos
                11 - Gerar relatório geral
                12 - Relatório por período
                0  - Sair
                Escolha: """);

            switch (in.nextInt()) {
                case 1 -> cadastrarAluno();
                case 2 -> listarAlunos();
                case 3 -> deletarAluno();
                case 4 -> cadastrarLivro();
                case 5 -> listarLivros();
                case 6 -> deletarLivro();
                case 7 -> registrarEmprestimo();
                case 8 -> registrarDevolucao();
                case 9 -> listarPendentes();
                case 10 -> listarHistoricoEmprestimos();
                case 11 -> gerarRelatorioGeral();
                case 12 -> gerarRelatorioPorPeriodo();
                case 0 -> { System.out.println("Até logo!"); return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarAluno() {
        try {
            System.out.print("Nome: ");
            in.nextLine(); // descarta newline
            String nome = in.nextLine();
            System.out.print("Matrícula (7 chars): ");
            String matricula = in.next();
            System.out.print("Data nascimento (AAAA-MM-DD): ");
            LocalDate nasc = LocalDate.parse(in.next());

            Aluno aluno = new Aluno(null, nome, matricula, nasc);
            int id = alunoDAO.save(aluno);
            System.out.println("Aluno salvo com id " + id);
        } catch (Exception e) {
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
        }
    }

    private static void listarAlunos() {
        try {
            List<Aluno> lista = alunoDAO.listAll();
            if (lista.isEmpty()) {
                System.out.println("Nenhum aluno cadastrado.");
            } else {
                lista.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void deletarAluno() {
        try {
            System.out.print("ID do aluno a deletar: ");
            int id = in.nextInt();
            
            Aluno aluno = alunoDAO.findById(id);
            if (aluno == null) {
                System.out.println("Aluno não encontrado.");
                return;
            }
            
            System.out.println("Aluno encontrado: " + aluno);
            System.out.print("Confirma exclusão? (s/N): ");
            String confirmacao = in.next();
            
            if (confirmacao.equalsIgnoreCase("s")) {
                boolean deletado = alunoDAO.delete(id);
                System.out.println(deletado ? "Aluno deletado com sucesso." : "Erro ao deletar aluno.");
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar aluno: " + e.getMessage());
        }
    }

    private static void cadastrarLivro() {
        try {
            System.out.print("Título: ");
            in.nextLine();
            String titulo = in.nextLine();
            System.out.print("Autor: ");
            String autor = in.nextLine();
            System.out.print("Ano publicação: ");
            Integer ano = in.nextInt();
            System.out.print("Quantidade estoque: ");
            int est = in.nextInt();

            Livro livro = new Livro(null, titulo, autor, ano, est);
            int id = livroDAO.save(livro);
            System.out.println("Livro salvo com id " + id);
        } catch (Exception e) {
            System.out.println("Erro ao salvar livro: " + e.getMessage());
        }
    }

    private static void listarLivros() {
        try {
            List<Livro> lista = livroDAO.listAll();
            if (lista.isEmpty()) {
                System.out.println("Nenhum livro cadastrado.");
            } else {
                lista.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void deletarLivro() {
        try {
            System.out.print("ID do livro a deletar: ");
            int id = in.nextInt();
            
            Livro livro = livroDAO.findById(id);
            if (livro == null) {
                System.out.println("Livro não encontrado.");
                return;
            }
            
            System.out.println("Livro encontrado: " + livro);
            System.out.print("Confirma exclusão? (s/N): ");
            String confirmacao = in.next();
            
            if (confirmacao.equalsIgnoreCase("s")) {
                boolean deletado = livroDAO.delete(id);
                System.out.println(deletado ? "Livro deletado com sucesso." : "Erro ao deletar livro.");
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar livro: " + e.getMessage());
        }
    }

    private static void registrarEmprestimo() {
        try {
            System.out.print("Id aluno: ");
            int idAluno = in.nextInt();
            System.out.print("Id livro: ");
            int idLivro = in.nextInt();
            System.out.print("Prazo (dias): ");
            int prazo = in.nextInt();

            boolean ok = emprestimoDAO.registrarEmprestimo(idAluno, idLivro, prazo);
            System.out.println(ok ? "Empréstimo registrado." : "Falha ao emprestar (estoque ou IDs inválidos).");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void registrarDevolucao() {
        try {
            System.out.print("Id empréstimo: ");
            int idEmp = in.nextInt();
            boolean ok = emprestimoDAO.registrarDevolucao(idEmp);
            System.out.println(ok ? "Devolução registrada." : "Falha (id inválido ou já devolvido).");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarPendentes() {
        try {
            List<model.Emprestimo> pendentes = emprestimoDAO.listarPendentes();
            if (pendentes.isEmpty()) {
                System.out.println("Nenhum empréstimo pendente.");
            } else {
                pendentes.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarHistoricoEmprestimos() {
        try {
            List<model.Emprestimo> historico = emprestimoDAO.listarTodos();
            if (historico.isEmpty()) {
                System.out.println("Nenhum empréstimo registrado.");
            } else {
                System.out.println("\n=== HISTÓRICO DE EMPRÉSTIMOS ===");
                historico.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void gerarRelatorioGeral() {
        try {
            relatorioDAO.gerarRelatorioCompleto();
        } catch (SQLException e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private static void gerarRelatorioPorPeriodo() {
        try {
            System.out.print("Data início (AAAA-MM-DD): ");
            LocalDate dataInicio = LocalDate.parse(in.next());
            System.out.print("Data fim (AAAA-MM-DD): ");
            LocalDate dataFim = LocalDate.parse(in.next());
            
            relatorioDAO.relatorioEmprestimosPorPeriodo(dataInicio, dataFim);
        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
