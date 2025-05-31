package service;

import dao.LivroDAO;
import model.Livro;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LivroService {
    private final LivroDAO livroDAO = new LivroDAO();
    private final Scanner scanner = new Scanner(System.in);

    public void cadastrar() {
        try {
            System.out.print("Título: ");
            scanner.nextLine();
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Ano publicação: ");
            Integer ano = scanner.nextInt();
            System.out.print("Quantidade estoque: ");
            int est = scanner.nextInt();

            Livro livro = new Livro(null, titulo, autor, ano, est);
            int id = livroDAO.save(livro);
            System.out.println("Livro salvo com id " + id);
        } catch (Exception e) {
            System.out.println("Erro ao salvar livro: " + e.getMessage());
        }
    }

    public void listar() {
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

    public void deletar() {
        try {
            System.out.print("ID do livro a deletar: ");
            int id = scanner.nextInt();
            
            Livro livro = livroDAO.findById(id);
            if (livro == null) {
                System.out.println("Livro não encontrado.");
                return;
            }
            
            System.out.println("Livro encontrado: " + livro);
            System.out.print("Confirma exclusão? (s/N): ");
            String confirmacao = scanner.next();
            
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

    public void atualizar() {
        try {
            System.out.print("ID do livro a atualizar: ");
            int id = scanner.nextInt();
            
            Livro livro = livroDAO.findById(id);
            if (livro == null) {
                System.out.println("Livro não encontrado.");
                return;
            }
            
            System.out.println("Livro encontrado: " + livro);
            System.out.println("Digite os novos dados (Enter para manter atual):");
            
            System.out.print("Título [" + livro.getTitulo() + "]: ");
            scanner.nextLine(); // descarta newline
            String titulo = scanner.nextLine();
            if (!titulo.trim().isEmpty()) {
                livro.setTitulo(titulo);
            }
            
            System.out.print("Autor [" + livro.getAutor() + "]: ");
            String autor = scanner.nextLine();
            if (!autor.trim().isEmpty()) {
                livro.setAutor(autor);
            }
            
            System.out.print("Ano publicação [" + livro.getAnoPublicacao() + "]: ");
            String anoStr = scanner.nextLine();
            if (!anoStr.trim().isEmpty()) {
                livro.setAnoPublicacao(Integer.parseInt(anoStr));
            }
            
            System.out.print("Quantidade estoque [" + livro.getQuantidadeEstoque() + "]: ");
            String estoqueStr = scanner.nextLine();
            if (!estoqueStr.trim().isEmpty()) {
                livro.setQuantidadeEstoque(Integer.parseInt(estoqueStr));
            }
            
            boolean atualizado = livroDAO.update(livro);
            System.out.println(atualizado ? "Livro atualizado com sucesso." : "Erro ao atualizar livro.");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar livro: " + e.getMessage());
        }
    }

    public void buscar() {
        try {
            System.out.println("Buscar por:");
            System.out.println("1 - ID");
            System.out.println("2 - Título");
            System.out.println("3 - Autor");
            System.out.println("4 - Livros disponíveis");
            System.out.print("Escolha: ");
            int opcao = scanner.nextInt();
            
            switch (opcao) {
                case 1 -> {
                    System.out.print("ID: ");
                    int id = scanner.nextInt();
                    Livro livro = livroDAO.findById(id);
                    if (livro != null) {
                        System.out.println("Livro encontrado: " + livro);
                        System.out.println("Estoque atual: " + livroDAO.getEstoque(id));
                    } else {
                        System.out.println("Livro não encontrado.");
                    }
                }
                case 2 -> {
                    System.out.print("Título (ou parte): ");
                    scanner.nextLine(); // descarta newline
                    String titulo = scanner.nextLine();
                    List<Livro> livros = livroDAO.findByTitulo(titulo);
                    if (livros.isEmpty()) {
                        System.out.println("Nenhum livro encontrado com '" + titulo + "' no título.");
                    } else {
                        System.out.println("Livros encontrados:");
                        livros.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    System.out.print("Autor (ou parte): ");
                    scanner.nextLine(); // descarta newline
                    String autor = scanner.nextLine();
                    List<Livro> livros = livroDAO.findByAutor(autor);
                    if (livros.isEmpty()) {
                        System.out.println("Nenhum livro encontrado com '" + autor + "' como autor.");
                    } else {
                        System.out.println("Livros encontrados:");
                        livros.forEach(System.out::println);
                    }
                }
                case 4 -> {
                    List<Livro> disponiveis = livroDAO.findDisponiveis();
                    if (disponiveis.isEmpty()) {
                        System.out.println("Nenhum livro disponível no momento.");
                    } else {
                        System.out.println("Livros disponíveis:");
                        disponiveis.forEach(System.out::println);
                    }
                }
                default -> System.out.println("Opção inválida.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar livro: " + e.getMessage());
        }
    }
} 