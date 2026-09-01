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
data_nascimento DATE NOT NULL
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
