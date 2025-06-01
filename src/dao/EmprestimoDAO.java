package dao;

import model.Emprestimo;
import util.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    private final LivroDAO livroDAO = new LivroDAO();

    public boolean registrarEmprestimo(int idAluno, int idLivro, int prazoDias) throws SQLException {
        final String sqlInsert = """
                INSERT INTO Emprestimos (id_aluno, id_livro, data_emprestimo, data_prazo_devolucao, data_devolucao)
                VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY), NULL)
                """;

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            if (!livroDAO.alterarEstoque(idLivro, -1)) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, idAluno);
                ps.setInt(2, idLivro);
                ps.setInt(3, prazoDias);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        }
    }

    public boolean registrarDevolucao(int idEmprestimo) throws SQLException {
        final String sqlSelectLivro = "SELECT id_livro FROM Emprestimos WHERE id_emprestimo = ? AND data_devolucao IS NULL";
        final String sqlUpdateDevolucao = "UPDATE Emprestimos SET data_devolucao = CURDATE() WHERE id_emprestimo = ? AND data_devolucao IS NULL";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            int idLivro = -1;

            // 1. Verificar se o empréstimo existe e está pendente, e obter o id_livro
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelectLivro)) {
                psSelect.setInt(1, idEmprestimo);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        idLivro = rs.getInt("id_livro");
                    } else {
                        // Empréstimo não encontrado ou já devolvido
                        conn.rollback();
                        return false;
                    }
                }
            }

            // 2. Registrar a devolução (atualizar data_devolucao)
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateDevolucao)) {
                psUpdate.setInt(1, idEmprestimo);
                int rowsAffected = psUpdate.executeUpdate();
                if (rowsAffected == 0) {
                    // Não deveria acontecer se o select acima funcionou, mas é uma segurança
                    conn.rollback();
                    return false;
                }
            }

            // 3. Atualizar o estoque do livro
            if (idLivro != -1) {
                if (!livroDAO.alterarEstoque(idLivro, +1)) {
                    conn.rollback();
                    return false; // Falha ao alterar o estoque
                }
            } else {
                // Não deveria chegar aqui se o primeiro select funcionou
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            // Em caso de qualquer SQLException, faz rollback e relança a exceção
            // ou trata de forma adequada (ex: logar e retornar false)
            try (Connection conn = Database.getConnection()) { // Try-with-resources para garantir o fechamento
                 if (conn != null && !conn.getAutoCommit()){ // conn pode ser null se Database.getConnection() falhou
                    conn.rollback();
                 }
            } catch (SQLException exRollback) {
                 // Logar falha no rollback, se necessário
                 System.err.println("Erro ao fazer rollback: " + exRollback.getMessage());
            }
            throw e; // ou return false; dependendo da política de tratamento de erro
        }
    }

    public Emprestimo findById(int id) throws SQLException {
        final String sql = "SELECT * FROM Emprestimos WHERE id_emprestimo = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapEmprestimo(rs) : null;
            }
        }
    }

    public List<Emprestimo> listarPendentes() throws SQLException {
        final String sql = "SELECT * FROM Emprestimos WHERE data_devolucao IS NULL";
        List<Emprestimo> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapEmprestimo(rs));
        }
        return lista;
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        final String sql = "SELECT * FROM Emprestimos ORDER BY data_emprestimo DESC";
        List<Emprestimo> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapEmprestimo(rs));
        }
        return lista;
    }

    public List<Emprestimo> findByAluno(int idAluno) throws SQLException {
        final String sql = "SELECT * FROM Emprestimos WHERE id_aluno = ? ORDER BY data_emprestimo DESC";
        List<Emprestimo> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAluno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapEmprestimo(rs));
            }
        }
        return lista;
    }

    public List<Emprestimo> findByLivro(int idLivro) throws SQLException {
        final String sql = "SELECT * FROM Emprestimos WHERE id_livro = ? ORDER BY data_emprestimo DESC";
        List<Emprestimo> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLivro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapEmprestimo(rs));
            }
        }
        return lista;
    }

    public boolean update(Emprestimo emprestimo) throws SQLException {
        final String sql = """
                UPDATE Emprestimos 
                   SET id_aluno = ?, 
                       id_livro = ?, 
                       data_emprestimo = ?, 
                       data_prazo_devolucao = ?, 
                       data_devolucao = ?
                 WHERE id_emprestimo = ?
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, emprestimo.getIdAluno());
            ps.setInt(2, emprestimo.getIdLivro());
            ps.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            ps.setDate(4, Date.valueOf(emprestimo.getDataPrazoDevolucao()));
            ps.setDate(5, emprestimo.getDataDevolucao() != null ? Date.valueOf(emprestimo.getDataDevolucao()) : null);
            ps.setInt(6, emprestimo.getIdEmprestimo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        final String sql = "DELETE FROM Emprestimos WHERE id_emprestimo = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Emprestimo mapEmprestimo(ResultSet rs) throws SQLException {
        Date devolucao = rs.getDate("data_devolucao");
        return new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_aluno"),
                rs.getInt("id_livro"),
                rs.getDate("data_emprestimo").toLocalDate(),
                rs.getDate("data_prazo_devolucao").toLocalDate(),
                devolucao == null ? null : devolucao.toLocalDate()
        );
    }
}
