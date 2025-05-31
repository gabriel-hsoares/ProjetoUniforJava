package controller;

import service.AlunoService;
import service.ConsultaService;
import service.EmprestimoService;
import service.LivroService;
import service.RelatorioService;

import java.util.Scanner;

public class MenuController {
    private final AlunoService alunoService = new AlunoService();
    private final LivroService livroService = new LivroService();
    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final RelatorioService relatorioService = new RelatorioService();
    private final ConsultaService consultaService = new ConsultaService();
    private final Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        while (true) {
            System.out.println("""
                \n===== Biblioteca =====
                === ALUNOS ===
                1  - Cadastrar aluno
                2  - Listar alunos
                3  - Buscar aluno
                4  - Atualizar aluno
                5  - Deletar aluno
                
                === LIVROS ===
                6  - Cadastrar livro
                7  - Listar livros
                8  - Buscar livro
                9  - Atualizar livro
                10 - Deletar livro
                
                === EMPRÉSTIMOS ===
                11 - Registrar empréstimo
                12 - Registrar devolução
                13 - Buscar empréstimo
                14 - Atualizar empréstimo
                15 - Deletar empréstimo
                16 - Listar empréstimos pendentes
                17 - Listar histórico de empréstimos
                18 - Listar empréstimos por aluno
                19 - Listar empréstimos por livro
                
                === CONSULTAS ===
                20 - Verificar disponibilidade de livro
                21 - Verificar empréstimos em atraso
                22 - Estatísticas gerais
                
                === RELATÓRIOS ===
                23 - Gerar relatório geral
                24 - Relatório por período
                
                0  - Sair
                Escolha: """);

            switch (scanner.nextInt()) {
                case 1 -> alunoService.cadastrar();
                case 2 -> alunoService.listar();
                case 3 -> alunoService.buscar();
                case 4 -> alunoService.atualizar();
                case 5 -> alunoService.deletar();
                case 6 -> livroService.cadastrar();
                case 7 -> livroService.listar();
                case 8 -> livroService.buscar();
                case 9 -> livroService.atualizar();
                case 10 -> livroService.deletar();
                case 11 -> emprestimoService.registrarEmprestimo();
                case 12 -> emprestimoService.registrarDevolucao();
                case 13 -> emprestimoService.buscarEmprestimo();
                case 14 -> emprestimoService.atualizarEmprestimo();
                case 15 -> emprestimoService.deletarEmprestimo();
                case 16 -> emprestimoService.listarPendentes();
                case 17 -> emprestimoService.listarHistorico();
                case 18 -> emprestimoService.listarEmprestimosPorAluno();
                case 19 -> emprestimoService.listarEmprestimosPorLivro();
                case 20 -> consultaService.verificarDisponibilidade();
                case 21 -> consultaService.verificarEmprestimosAtrasados();
                case 22 -> consultaService.estatisticasGerais();
                case 23 -> relatorioService.gerarRelatorioGeral();
                case 24 -> relatorioService.gerarRelatorioPorPeriodo();
                case 0 -> { 
                    System.out.println("Até logo!"); 
                    return; 
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
} 