-- Banco do Capas Vivas em PostgreSQL (Neon).
-- Rode pela classe Main.CriarBancoOnline.
-- De propósito não apaga nada: se as tabelas já existirem, dá erro e os dados
-- continuam como estavam.

-- Tabela de usuários
CREATE TABLE Usuarios
(
    CPF VARCHAR(11) PRIMARY KEY NOT NULL,
    nome VARCHAR(50) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    data_nascimento DATE NOT NULL,
    senha VARCHAR(20) NOT NULL,
    foto BYTEA
);

-- Tabela de livros
CREATE TABLE Livros
(
    ISBN VARCHAR(13) PRIMARY KEY NOT NULL,
    fotoContraCapa BYTEA,
    fotoCapa BYTEA,
    status BOOLEAN NOT NULL,
    CPF_dono VARCHAR(11) NOT NULL,
    titulo VARCHAR(50) NOT NULL,
    data_lancamento DATE NOT NULL,
    editora VARCHAR(50),
    generos VARCHAR(50),
    FOREIGN KEY (CPF_dono) REFERENCES Usuarios(CPF)
);

-- Tabela de autores
CREATE TABLE Autor
(
    id_autor SERIAL PRIMARY KEY,
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

-- Pedidos de empréstimo esperando o dono aceitar ou recusar.
-- O pedido vence 14 dias depois de feito.
CREATE TABLE Solicitacoes
(
    id_solicitacao SERIAL PRIMARY KEY,
    CPF_solicitante VARCHAR(11) NOT NULL,
    ISBN VARCHAR(13) NOT NULL,
    data_pedido DATE NOT NULL,
    FOREIGN KEY (CPF_solicitante) REFERENCES Usuarios(CPF),
    FOREIGN KEY (ISBN) REFERENCES Livros(ISBN)
);

-- Empréstimos aceitos: liga o dono do livro e quem pegou emprestado.
-- data_devolucao fica NULL enquanto o livro está com o leitor.
-- devolucao_solicitada = dia em que o leitor devolveu, esperando o dono confirmar.
-- ultimo_aviso_prazo = último dia em que o leitor recebeu o aviso diário do prazo.
CREATE TABLE Emprestimo
(
    id_emprestimo SERIAL PRIMARY KEY,
    ISBN VARCHAR(13) NOT NULL,
    titulo_livro VARCHAR(50) NOT NULL,
    CPF_dono VARCHAR(11) NOT NULL,
    CPF_leitor VARCHAR(11) NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_entrega DATE NOT NULL,
    data_devolucao DATE NULL,
    devolucao_solicitada DATE NULL,
    ultimo_aviso_prazo DATE NULL,
    FOREIGN KEY (ISBN) REFERENCES Livros(ISBN),
    FOREIGN KEY (CPF_dono) REFERENCES Usuarios(CPF),
    FOREIGN KEY (CPF_leitor) REFERENCES Usuarios(CPF)
);

-- Não deixa emprestar um livro que já está emprestado
CREATE FUNCTION fn_emprestimo_antes_inserir() RETURNS trigger AS $$
BEGIN
    IF (SELECT status FROM Livros WHERE ISBN = NEW.ISBN) = FALSE THEN
        RAISE EXCEPTION 'Livro indisponível: já está emprestado.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_emprestimo_antes_inserir
BEFORE INSERT ON Emprestimo
FOR EACH ROW
EXECUTE FUNCTION fn_emprestimo_antes_inserir();

-- Registrou um empréstimo: o livro fica indisponível
CREATE FUNCTION fn_emprestimo_inserido() RETURNS trigger AS $$
BEGIN
    IF NEW.data_devolucao IS NULL THEN
        UPDATE Livros SET status = FALSE WHERE ISBN = NEW.ISBN;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_emprestimo_inserido
AFTER INSERT ON Emprestimo
FOR EACH ROW
EXECUTE FUNCTION fn_emprestimo_inserido();

-- Registrou a devolução: o livro fica disponível de novo
CREATE FUNCTION fn_emprestimo_devolvido() RETURNS trigger AS $$
BEGIN
    IF OLD.data_devolucao IS NULL AND NEW.data_devolucao IS NOT NULL THEN
        UPDATE Livros SET status = TRUE WHERE ISBN = NEW.ISBN;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_emprestimo_devolvido
AFTER UPDATE ON Emprestimo
FOR EACH ROW
EXECUTE FUNCTION fn_emprestimo_devolvido();

-- Tabela de eventos/lembretes do calendário
CREATE TABLE Eventos
(
    id_evento SERIAL PRIMARY KEY,
    CPF_usuario VARCHAR(11) NOT NULL,
    data_evento DATE NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    FOREIGN KEY (CPF_usuario) REFERENCES Usuarios(CPF)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- Histórico de empréstimos do usuário (livros que emprestou e que pegou emprestado).
-- detalhe = nome da outra pessoa do empréstimo.
-- Sem chave estrangeira para Livros: o livro removido continua aparecendo aqui.
CREATE TABLE Historico
(
    id_historico SERIAL PRIMARY KEY,
    CPF_usuario VARCHAR(11) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    ISBN VARCHAR(13),
    titulo VARCHAR(50),
    detalhe VARCHAR(150),
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Mensagens do chat entre usuários.
-- lida = FALSE até o destinatário abrir a conversa.
CREATE TABLE Mensagens
(
    id_mensagem SERIAL PRIMARY KEY,
    CPF_remetente VARCHAR(11) NOT NULL,
    CPF_destinatario VARCHAR(11) NOT NULL,
    texto VARCHAR(500) NOT NULL,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (CPF_remetente) REFERENCES Usuarios(CPF)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (CPF_destinatario) REFERENCES Usuarios(CPF)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- INSERÇÃO DE USUÁRIOS (sem foto: aparece a imagem padrão)
INSERT INTO Usuarios
(CPF, nome, telefone, email, data_nascimento, senha)
VALUES
('12345678911', 'guilherme', '47999999999', 'gui@gmail.com', '2008-09-12', '123456'),
('98765432100', 'Mariana', '47988888888', 'mariana@gmail.com', '2007-05-23', 'senha123'),
('45678912300', 'Lucas', '47977777777', 'lucas@gmail.com', '2009-11-08', 'lucas123');

-- INSERÇÃO DE LIVROS (sem foto: aparece a imagem padrão)
INSERT INTO Livros
(ISBN, status, CPF_dono, titulo, data_lancamento, editora, generos)
VALUES
('9788535902777', TRUE, '12345678911', 'O Hobbit', '1937-09-21', 'HarperCollins', 'Fantasia'),
('9788532530788', TRUE, '98765432100', 'Dom Casmurro', '1899-01-01', 'Carambaia', 'Romance');

-- INSERÇÃO DE AUTORES
INSERT INTO Autor
(id_autor, nome, nacionalidade)
VALUES
(1, 'J. R. R. Tolkien', 'Reino Unido'),
(2, 'Machado de Assis', 'Brasileira'),
(3, 'Clarice Lispector', 'Brasileira'),
(4, 'George Orwell', 'Reino Unido');

-- Os IDs acima foram informados à mão: o próximo autor cadastrado continua do 5
SELECT setval(pg_get_serial_sequence('Autor', 'id_autor'), 4);

-- RELACIONAMENTO LIVROS E SEUS AUTORES
INSERT INTO Pertence
(ISBN, id_autor)
VALUES
('9788535902777', 1),
('9788532530788', 2);

-- INSERÇÃO DE EMPRÉSTIMOS
-- Lucas pegou "O Hobbit" do guilherme: o trigger deixa o livro indisponível
INSERT INTO Emprestimo
(ISBN, titulo_livro, CPF_dono, CPF_leitor, data_emprestimo, data_entrega)
VALUES
('9788535902777', 'O Hobbit', '12345678911', '45678912300', '2026-09-15', '2026-09-29');

-- INSERÇÃO DE SOLICITAÇÕES
INSERT INTO Solicitacoes
(CPF_solicitante, ISBN, data_pedido)
VALUES
('12345678911', '9788532530788', '2026-09-01');
