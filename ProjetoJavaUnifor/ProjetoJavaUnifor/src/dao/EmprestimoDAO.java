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
                INSERT INTO Emprestimos (id_aluno, id_livro, data_emprestimo, data_devolucao)
                VALUES (?, ?, CURDATE(), NULL)
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
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        }
    }

    public boolean registrarDevolucao(int idEmprestimo) throws SQLException {
        final String sqlSelect  = "SELECT id_livro FROM Emprestimos WHERE id_emprestimo = ? AND data_devolucao IS NOT NULL";
        final String sqlUpdate  = "UPDATE Emprestimos SET data_devolucao = CURDATE() WHERE id_emprestimo = ? AND data_devolucao IS NULL";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, idEmprestimo);
                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            int idLivro;
            try (PreparedStatement ps2 = conn.prepareStatement("SELECT id_livro FROM Emprestimos WHERE id_emprestimo = ?")) {
                ps2.setInt(1, idEmprestimo);
                try (ResultSet rs = ps2.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    idLivro = rs.getInt("id_livro");
                }
            }

            if (!livroDAO.alterarEstoque(idLivro, +1)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
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

    private Emprestimo mapEmprestimo(ResultSet rs) throws SQLException {
        Date devolucao = rs.getDate("data_devolucao");
        return new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_aluno"),
                rs.getInt("id_livro"),
                rs.getDate("data_emprestimo").toLocalDate(),
                devolucao == null ? null : devolucao.toLocalDate()
        );
    }
}
