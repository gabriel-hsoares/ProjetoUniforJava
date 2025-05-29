package dao;

import util.Database;

import java.sql.*;
import java.time.LocalDate;

public class RelatorioDAO {

    public void gerarRelatorioCompleto() throws SQLException {
        System.out.println("\n========== RELATÓRIO GERAL DA BIBLIOTECA ==========");
        
        relatorioAlunos();
        relatorioLivros();
        relatorioEmprestimos();
        relatorioEstatisticas();
    }

    private void relatorioAlunos() throws SQLException {
        System.out.println("\n--- ALUNOS CADASTRADOS ---");
        final String sql = "SELECT COUNT(*) as total FROM Alunos";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                System.out.println("Total de alunos: " + rs.getInt("total"));
            }
        }
    }

    private void relatorioLivros() throws SQLException {
        System.out.println("\n--- ACERVO DE LIVROS ---");
        final String sql = """
                SELECT 
                    COUNT(*) as total_titulos,
                    SUM(quantidade_estoque) as total_exemplares,
                    SUM(CASE WHEN quantidade_estoque = 0 THEN 1 ELSE 0 END) as sem_estoque
                FROM Livros
                """;
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                System.out.println("Total de títulos: " + rs.getInt("total_titulos"));
                System.out.println("Total de exemplares: " + rs.getInt("total_exemplares"));
                System.out.println("Livros sem estoque: " + rs.getInt("sem_estoque"));
            }
        }
    }

    private void relatorioEmprestimos() throws SQLException {
        System.out.println("\n--- EMPRÉSTIMOS ---");
        final String sql = """
                SELECT 
                    COUNT(*) as total_emprestimos,
                    SUM(CASE WHEN data_devolucao IS NULL THEN 1 ELSE 0 END) as pendentes,
                    SUM(CASE WHEN data_devolucao IS NOT NULL THEN 1 ELSE 0 END) as devolvidos
                FROM Emprestimos
                """;
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                System.out.println("Total de empréstimos: " + rs.getInt("total_emprestimos"));
                System.out.println("Empréstimos pendentes: " + rs.getInt("pendentes"));
                System.out.println("Empréstimos devolvidos: " + rs.getInt("devolvidos"));
            }
        }
    }

    private void relatorioEstatisticas() throws SQLException {
        System.out.println("\n--- ESTATÍSTICAS ---");
        
        livrosMaisEmprestados();
        alunosComMaisEmprestimos();
    }

    private void livrosMaisEmprestados() throws SQLException {
        System.out.println("\nLivros mais emprestados:");
        final String sql = """
                SELECT l.titulo, l.autor, COUNT(e.id_emprestimo) as total_emprestimos
                FROM Livros l
                LEFT JOIN Emprestimos e ON l.id_livro = e.id_livro
                GROUP BY l.id_livro, l.titulo, l.autor
                ORDER BY total_emprestimos DESC
                LIMIT 5
                """;
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("- %s (%s): %d empréstimos%n",
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("total_emprestimos"));
            }
        }
    }

    private void alunosComMaisEmprestimos() throws SQLException {
        System.out.println("\nAlunos com mais empréstimos:");
        final String sql = """
                SELECT a.nome_aluno, a.matricula, COUNT(e.id_emprestimo) as total_emprestimos
                FROM Alunos a
                LEFT JOIN Emprestimos e ON a.id_aluno = e.id_aluno
                GROUP BY a.id_aluno, a.nome_aluno, a.matricula
                ORDER BY total_emprestimos DESC
                LIMIT 5
                """;
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("- %s (%s): %d empréstimos%n",
                    rs.getString("nome_aluno"),
                    rs.getString("matricula"),
                    rs.getInt("total_emprestimos"));
            }
        }
    }

    public void relatorioEmprestimosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        System.out.println("\n--- EMPRÉSTIMOS POR PERÍODO ---");
        System.out.printf("Período: %s a %s%n", dataInicio, dataFim);
        
        final String sql = """
                SELECT 
                    a.nome_aluno, a.matricula,
                    l.titulo, l.autor,
                    e.data_emprestimo, e.data_devolucao
                FROM Emprestimos e
                JOIN Alunos a ON e.id_aluno = a.id_aluno
                JOIN Livros l ON e.id_livro = l.id_livro
                WHERE e.data_emprestimo BETWEEN ? AND ?
                ORDER BY e.data_emprestimo DESC
                """;
        
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(dataInicio));
            ps.setDate(2, Date.valueOf(dataFim));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s - %s (%s) emprestou '%s' em %s%s%n",
                        rs.getDate("data_emprestimo"),
                        rs.getString("nome_aluno"),
                        rs.getString("matricula"),
                        rs.getString("titulo"),
                        rs.getDate("data_emprestimo"),
                        rs.getDate("data_devolucao") != null ? 
                            " (devolvido em " + rs.getDate("data_devolucao") + ")" : " (pendente)"
                    );
                }
            }
        }
    }
}