package service;

import dao.AlunoDAO;
import model.Aluno;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AlunoService {
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final Scanner scanner = new Scanner(System.in);

    public void cadastrar() {
        try {
            System.out.print("Nome: ");
            scanner.nextLine(); // descarta newline
            String nome = scanner.nextLine();
            
            System.out.print("Matrícula (7 chars): ");
            String matricula = scanner.next();
            
            // Validação da matrícula
            if (matricula.length() != 7) {
                System.out.println("Erro: Matrícula deve ter exatamente 7 caracteres.");
                return;
            }
            
            if (alunoDAO.existsByMatricula(matricula)) {
                System.out.println("Erro: Já existe um aluno com esta matrícula.");
                return;
            }
            
            System.out.print("Data nascimento (AAAA-MM-DD): ");
            LocalDate nasc = LocalDate.parse(scanner.next());

            Aluno aluno = new Aluno(null, nome, matricula, nasc);
            int id = alunoDAO.save(aluno);
            System.out.println("Aluno salvo com id " + id);
        } catch (Exception e) {
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
        }
    }

    public void listar() {
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

    public void deletar() {
        try {
            System.out.print("ID do aluno a deletar: ");
            int id = scanner.nextInt();
            
            Aluno aluno = alunoDAO.findById(id);
            if (aluno == null) {
                System.out.println("Aluno não encontrado.");
                return;
            }
            
            System.out.println("Aluno encontrado: " + aluno);
            System.out.print("Confirma exclusão? (s/N): ");
            String confirmacao = scanner.next();
            
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

    public void atualizar() {
        try {
            System.out.print("ID do aluno a atualizar: ");
            int id = scanner.nextInt();
            
            Aluno aluno = alunoDAO.findById(id);
            if (aluno == null) {
                System.out.println("Aluno não encontrado.");
                return;
            }
            
            System.out.println("Aluno encontrado: " + aluno);
            System.out.println("Digite os novos dados (Enter para manter atual):");
            
            System.out.print("Nome [" + aluno.getNomeAluno() + "]: ");
            scanner.nextLine(); // descarta newline
            String nome = scanner.nextLine();
            if (!nome.trim().isEmpty()) {
                aluno.setNomeAluno(nome);
            }
            
            System.out.print("Matrícula [" + aluno.getMatricula() + "]: ");
            String matricula = scanner.nextLine();
            if (!matricula.trim().isEmpty()) {
                aluno.setMatricula(matricula);
            }
            
            System.out.print("Data nascimento (AAAA-MM-DD) [" + aluno.getDataNascimento() + "]: ");
            String dataNascStr = scanner.nextLine();
            if (!dataNascStr.trim().isEmpty()) {
                aluno.setDataNascimento(LocalDate.parse(dataNascStr));
            }
            
            boolean atualizado = alunoDAO.update(aluno);
            System.out.println(atualizado ? "Aluno atualizado com sucesso." : "Erro ao atualizar aluno.");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    public void buscar() {
        try {
            System.out.println("Buscar por:");
            System.out.println("1 - ID");
            System.out.println("2 - Matrícula");
            System.out.println("3 - Nome");
            System.out.print("Escolha: ");
            int opcao = scanner.nextInt();
            
            switch (opcao) {
                case 1 -> {
                    System.out.print("ID: ");
                    int id = scanner.nextInt();
                    Aluno aluno = alunoDAO.findById(id);
                    if (aluno != null) {
                        System.out.println("Aluno encontrado: " + aluno);
                    } else {
                        System.out.println("Aluno não encontrado.");
                    }
                }
                case 2 -> {
                    System.out.print("Matrícula: ");
                    String matricula = scanner.next();
                    Aluno aluno = alunoDAO.findByMatricula(matricula);
                    if (aluno != null) {
                        System.out.println("Aluno encontrado: " + aluno);
                    } else {
                        System.out.println("Aluno com matrícula '" + matricula + "' não encontrado.");
                    }
                }
                case 3 -> {
                    System.out.print("Nome (ou parte): ");
                    scanner.nextLine(); // descarta newline
                    String nome = scanner.nextLine();
                    List<Aluno> alunos = alunoDAO.findByNome(nome);
                    if (alunos.isEmpty()) {
                        System.out.println("Nenhum aluno encontrado com '" + nome + "' no nome.");
                    } else {
                        System.out.println("Alunos encontrados:");
                        alunos.forEach(System.out::println);
                    }
                }
                default -> System.out.println("Opção inválida.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar aluno: " + e.getMessage());
        }
    }
} 