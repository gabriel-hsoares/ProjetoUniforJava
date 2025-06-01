package service;

import dao.AlunoDAO;
import dao.EmprestimoDAO;
import dao.LivroDAO;
import model.Aluno;
import model.Emprestimo;
import model.Livro;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class ConsultaService {
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final LivroDAO livroDAO = new LivroDAO();
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final Scanner scanner = new Scanner(System.in);

    public void verificarDisponibilidade() {
        try {
            System.out.print("ID do livro: ");
            int idLivro = scanner.nextInt();
            
            Livro livro = livroDAO.findById(idLivro);
            if (livro == null) {
                System.out.println("Livro não encontrado.");
                return;
            }
            
            System.out.println("Livro: " + livro.getTitulo());
            System.out.println("Estoque total: " + livro.getQuantidadeEstoque());
            
            List<Emprestimo> pendentes = emprestimoDAO.listarPendentes();
            long emprestados = pendentes.stream()
                    .filter(emp -> emp.getIdLivro() == idLivro)
                    .count();
            
            System.out.println("Exemplares emprestados: " + emprestados);
            System.out.println("Exemplares disponíveis: " + (livro.getQuantidadeEstoque()));
            
            if (livro.getQuantidadeEstoque() > 0) {
                System.out.println("[OK] Livro disponível para empréstimo");
            } else {
                System.out.println("[X] Livro indisponível no momento");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void verificarEmprestimosAtrasados() {
        try {
            List<Emprestimo> pendentes = emprestimoDAO.listarPendentes();
            LocalDate hoje = LocalDate.now();
            
            System.out.println("\n=== EMPRÉSTIMOS EM ATRASO ===");
            boolean temAtraso = false;
            
            for (Emprestimo emp : pendentes) {
                // Usa a dataPrazoDevolucao do empréstimo
                LocalDate prazoVencimento = emp.getDataPrazoDevolucao(); 
                if (emp.getDataDevolucao() == null && hoje.isAfter(prazoVencimento)) {
                    long diasAtraso = ChronoUnit.DAYS.between(prazoVencimento, hoje);
                    System.out.println("[ATRASO] Empréstimo " + emp.getIdEmprestimo() + 
                                     " - Aluno: " + emp.getIdAluno() + 
                                     " - Livro: " + emp.getIdLivro() + 
                                     " - Vencido em: " + prazoVencimento + 
                                     " (Atraso: " + diasAtraso + " dias)");
                    temAtraso = true;
                }
            }
            
            if (!temAtraso) {
                System.out.println("[OK] Nenhum empréstimo em atraso!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar empréstimos atrasados: " + e.getMessage());
        }
    }

    public void estatisticasGerais() {
        try {
            List<Aluno> alunos = alunoDAO.listAll();
            List<Livro> livros = livroDAO.listAll();
            List<Emprestimo> pendentes = emprestimoDAO.listarPendentes();
            List<Emprestimo> historico = emprestimoDAO.listarTodos();
            
            int totalEstoque = livros.stream()
                    .mapToInt(Livro::getQuantidadeEstoque)
                    .sum();
            
            System.out.println("\n=== ESTATÍSTICAS GERAIS ===");
            System.out.println("Total de alunos: " + alunos.size());
            System.out.println("Total de livros: " + livros.size());
            System.out.println("Total exemplares em estoque: " + totalEstoque);
            System.out.println("Empréstimos pendentes: " + pendentes.size());
            System.out.println("Total de empréstimos já realizados: " + historico.size());
            
            // Livro mais emprestado
            if (!historico.isEmpty()) {
                var livroMaisEmprestado = historico.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                                Emprestimo::getIdLivro,
                                java.util.stream.Collectors.counting()))
                        .entrySet().stream()
                        .max(java.util.Map.Entry.comparingByValue());
                
                if (livroMaisEmprestado.isPresent()) {
                    int idLivro = livroMaisEmprestado.get().getKey();
                    Livro livro = livroDAO.findById(idLivro);
                    System.out.println("Livro mais emprestado: " + 
                                     (livro != null ? livro.getTitulo() : "ID " + idLivro) + 
                                     " (" + livroMaisEmprestado.get().getValue() + " empréstimos)");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
} 