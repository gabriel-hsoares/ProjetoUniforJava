-- cria o banco de dados
CREATE DATABASE IF NOT EXISTS biblioteca_aprender_mais
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE biblioteca_aprender_mais;

-- criacao da tabela alunos
CREATE TABLE Alunos (
    id_aluno INT AUTO_INCREMENT PRIMARY KEY,
    nome_aluno VARCHAR(255) NOT NULL,
    matricula VARCHAR(7) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,

    INDEX idx_matricula (matricula),
    INDEX idx_nome (nome_aluno)
) ENGINE=InnoDB;

-- criacao da tabela livros
CREATE TABLE Livros (
    id_livro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) DEFAULT 'Desconhecido',
    ano_publicacao INT,
    quantidade_estoque INT DEFAULT 0 CHECK (quantidade_estoque >= 0),

    INDEX idx_titulo (titulo),
    INDEX idx_autor (autor),
    INDEX idx_ano (ano_publicacao)
) ENGINE=InnoDB;

-- criacao da tabela de emprestimos
CREATE TABLE Emprestimos (
    id_emprestimo INT AUTO_INCREMENT PRIMARY KEY,
    id_aluno INT NOT NULL,
    id_livro INT NOT NULL,
    data_emprestimo DATE NOT NULL DEFAULT (CURDATE()),
    data_devolucao DATE NULL,
    

    FOREIGN KEY (id_aluno) REFERENCES Alunos(id_aluno) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_livro) REFERENCES Livros(id_livro) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    

    INDEX idx_aluno (id_aluno),
    INDEX idx_livro (id_livro),
    INDEX idx_data_emprestimo (data_emprestimo),
    INDEX idx_pendentes (data_devolucao),
    
    -- o constraint vai garantir que a data de devolucao do livro seja maior ou igual a data de emprestimo
    CONSTRAINT chk_datas CHECK (data_devolucao IS NULL OR data_devolucao >= data_emprestimo)
) ENGINE=InnoDB;


-- para que o banco não fique vazio, criamos alguns alunos e livros de forma padrão
INSERT INTO Alunos (nome_aluno, matricula, data_nascimento) VALUES
('João Silva Santos', '2021001', '2000-05-15'),
('Maria Oliveira Costa', '2021002', '1999-08-22'),
('Pedro Souza Lima', '2021003', '2001-03-10'),
('Ana Paula Ferreira', '2021004', '2000-11-30');

INSERT INTO Livros (titulo, autor, ano_publicacao, quantidade_estoque) VALUES
('Clean Code', 'Robert C. Martin', 2008, 5),
('Design Patterns', 'Gang of Four', 1994, 3),
('Effective Java', 'Joshua Bloch', 2017, 4),
('Java: The Complete Reference', 'Herbert Schildt', 2020, 2),
('Spring in Action', 'Craig Walls', 2018, 3);

-- view para emprestimos ativos
CREATE VIEW vw_emprestimos_ativos AS
SELECT 
    e.id_emprestimo,
    a.nome_aluno,
    a.matricula,
    l.titulo,
    l.autor,
    e.data_emprestimo,
    DATEDIFF(CURDATE(), e.data_emprestimo) AS dias_emprestado
FROM Emprestimos e
JOIN Alunos a ON e.id_aluno = a.id_aluno
JOIN Livros l ON e.id_livro = l.id_livro
WHERE e.data_devolucao IS NULL;

-- view para estatísticas dos livros
CREATE VIEW vw_estatisticas_livros AS
SELECT 
    l.id_livro,
    l.titulo,
    l.autor,
    l.quantidade_estoque,
    COUNT(e.id_emprestimo) AS total_emprestimos,
    COUNT(CASE WHEN e.data_devolucao IS NULL THEN 1 END) AS emprestimos_ativos
FROM Livros l
LEFT JOIN Emprestimos e ON l.id_livro = e.id_livro
GROUP BY l.id_livro, l.titulo, l.autor, l.quantidade_estoque; 