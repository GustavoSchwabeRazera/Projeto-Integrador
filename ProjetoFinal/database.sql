drop database if exists CapasVivas;
CREATE DATABASE CapasVivas;
USE CapasVivas;

-- Tabela de usuários
CREATE TABLE Usuarios
(
CPF VARCHAR(11) PRIMARY KEY NOT NULL,
nome VARCHAR(50) NOT NULL,
telefone VARCHAR(20) NOT NULL,
email VARCHAR(50) NOT NULL,
data_nascimento DATE NOT NULL,
senha VARCHAR(20) NOT NULL
);

-- Tabela de livros
CREATE TABLE Livros
(
ISBN VARCHAR(13) PRIMARY KEY NOT NULL,
fotoContraCapa BLOB,
fotoCapa BLOB,
status BOOLEAN NOT NULL,
CPF_dono VARCHAR(11) NOT NULL,
titulo VARCHAR(50) NOT NULL,
data_lancamento DATE NOT NULL,
FOREIGN KEY (CPF_dono) REFERENCES Usuarios(CPF)
);

-- Tabela de autores
CREATE TABLE Autor
(
id_autor INT PRIMARY KEY NOT NULL,
nome VARCHAR(50) NOT NULL,
nacionalidade VARCHAR(50) NOT NULL
);

-- Tabela de relacionamento Livro x Autor
CREATE TABLE Pertence
(
ISBN VARCHAR(13) NOT NULL,
id_autor INT NOT NULL,
PRIMARY KEY (ISBN, id_autor),
FOREIGN KEY (ISBN) REFERENCES Livros(ISBN),
FOREIGN KEY (id_autor) REFERENCES Autor(id_autor)
);

-- Tabela de empréstimos
CREATE TABLE Emprestimos
(
id_emprestimo INT PRIMARY KEY NOT NULL,
CPF_usuario VARCHAR(11) NOT NULL,
ISBN VARCHAR(13) NOT NULL,
statusEmprestimo BOOLEAN NOT NULL,
data_inicio DATE NOT NULL,
data_termino DATE NOT NULL,
FOREIGN KEY (CPF_usuario) REFERENCES Usuarios(CPF),
FOREIGN KEY (ISBN) REFERENCES Livros(ISBN)
);

INSERT INTO Usuarios 
(CPF, nome, telefone, email, data_nascimento, senha,foto)
VALUES
('12345678911', 'guilherme', '999999', 'gui@gmail', '2008-09-12', '123456',   LOAD_FILE('/ProjetoFinal/src/imagens/FotoPerfil.png')),
('98765432100', 'Mariana', '988888888', 'mariana@gmail.com', '2007-05-23', 'senha123',   LOAD_FILE('C:/ProjetoFinal/src/imagens/FotoPerfil.png')),
('45678912300', 'Lucas', '977777777', 'lucas@gmail.com', '2009-11-08', 'lucas123',   LOAD_FILE('/ProjetoFinal/src/imagens/FotoPerfil.png'));



--dados aleatórios para teste ->
-- LIVROS
INSERT INTO Livros
(ISBN, fotoContraCapa, fotoCapa, status, CPF_dono, titulo, data_lancamento)
VALUES
('9788535902777', NULL, NULL, TRUE, '12345678911', 'O Hobbit', '1937-09-21'),
('9788532530788', NULL, NULL, TRUE, '98765432100', 'Dom Casmurro', '1899-01-01');


-- AUTORES
INSERT INTO Autor
(id_autor, nome, nacionalidade)
VALUES
(1, 'J. R. R. Tolkien', 'Britânica'),
(2, 'Machado de Assis', 'Brasileira'),
(3, 'Clarice Lispector', 'Brasileira'),
(4, 'George Orwell', 'Britânica');


-- LIVROS E SEUS AUTORES
INSERT INTO Pertence
(ISBN, id_autor)
VALUES
('9788535902777', 1),
('9788532530788', 2);


-- EMPRÉSTIMOS
INSERT INTO Emprestimos
(id_emprestimo, CPF_usuario, ISBN, statusEmprestimo, data_inicio, data_termino)
VALUES
(1, '45678912300', '9788535902777', TRUE, '2026-09-15', '2026-09-29'),
(2, '12345678911', '9788532530788', FALSE, '2026-09-01', '2026-09-15');


