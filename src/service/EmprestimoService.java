package service;

import dao.EmprestimoDAO;
import model.Emprestimo;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class EmprestimoService {
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final Scanner scanner = new Scanner(System.in);

    public void registrarEmprestimo() {
        try {
            System.out.print("Id aluno: ");
            int idAluno = scanner.nextInt();
            System.out.print("Id livro: ");
            int idLivro = scanner.nextInt();
            System.out.print("Prazo (dias): ");
            int prazo = scanner.nextInt();

            boolean ok = emprestimoDAO.registrarEmprestimo(idAluno, idLivro, prazo);
            System.out.println(ok ? "Empréstimo registrado." : "Falha ao emprestar (estoque ou IDs inválidos).");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void registrarDevolucao() {
        try {
            System.out.print("Id empréstimo: ");
            int idEmp = scanner.nextInt();
            boolean ok = emprestimoDAO.registrarDevolucao(idEmp);
            System.out.println(ok ? "Devolução registrada." : "Falha (id inválido ou já devolvido).");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void listarPendentes() {
        try {
            List<Emprestimo> pendentes = emprestimoDAO.listarPendentes();
            if (pendentes.isEmpty()) {
                System.out.println("Nenhum empréstimo pendente.");
            } else {
                pendentes.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void listarHistorico() {
        try {
            List<Emprestimo> historico = emprestimoDAO.listarTodos();
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

    public void buscarEmprestimo() {
        try {
            System.out.print("ID do empréstimo: ");
            int id = scanner.nextInt();
            
            Emprestimo emprestimo = emprestimoDAO.findById(id);
            if (emprestimo != null) {
                System.out.println("Empréstimo encontrado: " + emprestimo);
            } else {
                System.out.println("Empréstimo não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar empréstimo: " + e.getMessage());
        }
    }

    public void listarEmprestimosPorAluno() {
        try {
            System.out.print("ID do aluno: ");
            int idAluno = scanner.nextInt();
            
            List<Emprestimo> doAluno = emprestimoDAO.findByAluno(idAluno);
            if (doAluno.isEmpty()) {
                System.out.println("Nenhum empréstimo encontrado para este aluno.");
            } else {
                System.out.println("\n=== EMPRÉSTIMOS DO ALUNO ===");
                doAluno.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
        }
    }

    public void listarEmprestimosPorLivro() {
        try {
            System.out.print("ID do livro: ");
            int idLivro = scanner.nextInt();
            
            List<Emprestimo> doLivro = emprestimoDAO.findByLivro(idLivro);
            if (doLivro.isEmpty()) {
                System.out.println("Nenhum empréstimo encontrado para este livro.");
            } else {
                System.out.println("\n=== EMPRÉSTIMOS DO LIVRO ===");
                doLivro.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
        }
    }

    public void atualizarEmprestimo() {
        try {
            System.out.print("ID do empréstimo a atualizar: ");
            int id = scanner.nextInt();
            
            Emprestimo emprestimo = emprestimoDAO.findById(id);
            if (emprestimo == null) {
                System.out.println("Empréstimo não encontrado.");
                return;
            }
            
            System.out.println("Empréstimo encontrado: " + emprestimo);
            System.out.println("Digite os novos dados (Enter para manter atual):");
            
            System.out.print("ID do aluno [" + emprestimo.getIdAluno() + "]: ");
            scanner.nextLine(); // descarta newline
            String idAlunoStr = scanner.nextLine();
            if (!idAlunoStr.trim().isEmpty()) {
                emprestimo.setIdAluno(Integer.parseInt(idAlunoStr));
            }
            
            System.out.print("ID do livro [" + emprestimo.getIdLivro() + "]: ");
            String idLivroStr = scanner.nextLine();
            if (!idLivroStr.trim().isEmpty()) {
                emprestimo.setIdLivro(Integer.parseInt(idLivroStr));
            }
            
            System.out.print("Data empréstimo (AAAA-MM-DD) [" + emprestimo.getDataEmprestimo() + "]: ");
            String dataEmpStr = scanner.nextLine();
            if (!dataEmpStr.trim().isEmpty()) {
                emprestimo.setDataEmprestimo(LocalDate.parse(dataEmpStr));
            }
            
            System.out.print("Data devolução (AAAA-MM-DD) [" + emprestimo.getDataDevolucao() + "]: ");
            String dataDevStr = scanner.nextLine();
            if (!dataDevStr.trim().isEmpty()) {
                emprestimo.setDataDevolucao(LocalDate.parse(dataDevStr));
            }
            
            boolean atualizado = emprestimoDAO.update(emprestimo);
            System.out.println(atualizado ? "Empréstimo atualizado com sucesso." : "Erro ao atualizar empréstimo.");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar empréstimo: " + e.getMessage());
        }
    }

    public void deletarEmprestimo() {
        try {
            System.out.print("ID do empréstimo a deletar: ");
            int id = scanner.nextInt();
            
            Emprestimo emprestimo = emprestimoDAO.findById(id);
            if (emprestimo == null) {
                System.out.println("Empréstimo não encontrado.");
                return;
            }
            
            System.out.println("Empréstimo encontrado: " + emprestimo);
            System.out.print("Confirma exclusão? (s/N): ");
            String confirmacao = scanner.next();
            
            if (confirmacao.equalsIgnoreCase("s")) {
                boolean deletado = emprestimoDAO.delete(id);
                System.out.println(deletado ? "Empréstimo deletado com sucesso." : "Erro ao deletar empréstimo.");
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar empréstimo: " + e.getMessage());
        }
    }
} 