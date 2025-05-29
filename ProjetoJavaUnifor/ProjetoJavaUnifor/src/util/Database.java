package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {

    private static final String URL      = "jdbc:mysql://localhost:3306/biblioteca_aprender_mais";
    private static final String USER     = "root";
    private static final String PASSWORD = "20250529";

    private Database() { }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver não encontrado no classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}