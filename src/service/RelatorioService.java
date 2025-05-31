package service;

import dao.RelatorioDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class RelatorioService {
    private final RelatorioDAO relatorioDAO = new RelatorioDAO();
    private final Scanner scanner = new Scanner(System.in);

    public void gerarRelatorioGeral() {
        try {
            relatorioDAO.gerarRelatorioCompleto();
        } catch (SQLException e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    public void gerarRelatorioPorPeriodo() {
        try {
            System.out.print("Data início (AAAA-MM-DD): ");
            LocalDate dataInicio = LocalDate.parse(scanner.next());
            System.out.print("Data fim (AAAA-MM-DD): ");
            LocalDate dataFim = LocalDate.parse(scanner.next());
            
            relatorioDAO.relatorioEmprestimosPorPeriodo(dataInicio, dataFim);
        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }
} 