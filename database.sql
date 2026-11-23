DROP DATABASE IF EXISTS CapasVivas;
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
    senha VARCHAR(20) NOT NULL,
    foto MEDIUMBLOB
);

-- Tabela de livros
CREATE TABLE Livros
(
    ISBN VARCHAR(13) PRIMARY KEY NOT NULL,
    fotoContraCapa MEDIUMBLOB,
    fotoCapa MEDIUMBLOB,
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
    id_autor INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
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
-- data_retirada / data_retorno = dias que o leitor escolheu para pegar
-- e devolver o livro (viram as datas do empréstimo quando o dono aceita).
CREATE TABLE Solicitacoes
(
    id_solicitacao INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    CPF_solicitante VARCHAR(11) NOT NULL,
    ISBN VARCHAR(13) NOT NULL,
    data_pedido DATE NOT NULL,
    data_retirada DATE NULL,
    data_retorno DATE NULL,
    FOREIGN KEY (CPF_solicitante) REFERENCES Usuarios(CPF),
    FOREIGN KEY (ISBN) REFERENCES Livros(ISBN)
);

-- Empréstimos aceitos: liga o dono do livro e quem pegou emprestado.
-- data_devolucao fica NULL enquanto o livro está com o leitor.
-- devolucao_solicitada = dia em que o leitor devolveu, esperando o dono confirmar.
-- ultimo_aviso_prazo = último dia em que o leitor recebeu o aviso diário do prazo.
CREATE TABLE Emprestimo
(
    id_emprestimo INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
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

DELIMITER $$

-- Não deixa emprestar um livro que já está emprestado
CREATE TRIGGER trg_emprestimo_antes_inserir
BEFORE INSERT ON Emprestimo
FOR EACH ROW
BEGIN
    IF (SELECT status FROM Livros WHERE ISBN = NEW.ISBN) = FALSE THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Livro indisponível: já está emprestado.';
    END IF;
END$$

-- Registrou um empréstimo: o livro fica indisponível
CREATE TRIGGER trg_emprestimo_inserido
AFTER INSERT ON Emprestimo
FOR EACH ROW
BEGIN
    IF NEW.data_devolucao IS NULL THEN
        UPDATE Livros SET status = FALSE WHERE ISBN = NEW.ISBN;
    END IF;
END$$

-- Registrou a devolução: o livro fica disponível de novo
CREATE TRIGGER trg_emprestimo_devolvido
AFTER UPDATE ON Emprestimo
FOR EACH ROW
BEGIN
    IF OLD.data_devolucao IS NULL AND NEW.data_devolucao IS NOT NULL THEN
        UPDATE Livros SET status = TRUE WHERE ISBN = NEW.ISBN;
    END IF;
END$$

DELIMITER ;

-- Tabela de eventos/lembretes do calendário
CREATE TABLE Eventos
(
    id_evento INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
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
    id_historico INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    CPF_usuario VARCHAR(11) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    ISBN VARCHAR(13),
    titulo VARCHAR(50),
    detalhe VARCHAR(150),
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Mensagens do chat entre usuários.
-- lida = FALSE até o destinatário abrir a conversa.
CREATE TABLE Mensagens
(
    id_mensagem INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    CPF_remetente VARCHAR(11) NOT NULL,
    CPF_destinatario VARCHAR(11) NOT NULL,
    texto VARCHAR(500) NOT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (CPF_remetente) REFERENCES Usuarios(CPF)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (CPF_destinatario) REFERENCES Usuarios(CPF)
        ON UPDATE CASCADE ON DELETE CASCADE
);


-- =========================================================
-- DADOS DE EXEMPLO
-- As datas são contadas a partir de hoje (CURDATE()), então os pedidos,
-- prazos e lembretes continuam valendo em qualquer dia que o script rodar.
--
-- Contas para testar (e-mail / senha):
--   gui@gmail.com      / 123456    -> tem pedido pra aceitar e devolveu um livro
--   mariana@gmail.com  / senha123  -> precisa confirmar uma devolução; prazo vence amanhã
--   lucas@gmail.com    / lucas123  -> está com "O Hobbit" dentro do prazo
--   pedro@gmail.com    / pedro123  -> está com "Dom Casmurro" ATRASADO
--   ana@gmail.com      / ana123    -> sem empréstimo: pode pedir livros; tem pedidos esperando
--   beatriz@gmail.com  / bia123    -> sem empréstimo; tem pedidos esperando
-- =========================================================

-- USUÁRIOS (sem foto: aparece a imagem padrão)
INSERT INTO Usuarios
(CPF, nome, telefone, email, data_nascimento, senha, foto)
VALUES
('12345678911', 'guilherme', '47999999999', 'gui@gmail.com', '2008-09-12', '123456', NULL),
('98765432100', 'Mariana', '47988888888', 'mariana@gmail.com', '2007-05-23', 'senha123', NULL),
('45678912300', 'Lucas', '47977777777', 'lucas@gmail.com', '2009-11-08', 'lucas123', NULL),
('65498732100', 'Pedro', '47966666666', 'pedro@gmail.com', '2008-02-14', 'pedro123', NULL),
('32165498700', 'Ana', '47955555555', 'ana@gmail.com', '2007-07-30', 'ana123', NULL),
('15975345600', 'Beatriz', '47944444444', 'beatriz@gmail.com', '2009-04-03', 'bia123', NULL);

-- LIVROS (sem foto: aparece a imagem padrão)
-- Todos começam disponíveis; os triggers marcam os emprestados.
-- Gêneros iguais aos da lista do cadastro: Ação, Aventura, Drama, Fantasia, Romance, Terror
INSERT INTO Livros
(ISBN, fotoContraCapa, fotoCapa, status, CPF_dono, titulo, data_lancamento, editora, generos)
VALUES
-- do guilherme
('9788535902777', NULL, NULL, TRUE, '12345678911', 'O Hobbit', '1937-09-21', 'HarperCollins', 'Fantasia'),
('9788595084759', NULL, NULL, TRUE, '12345678911', 'A Sociedade do Anel', '1954-07-29', 'HarperCollins', 'Fantasia'),
('9788535914849', NULL, NULL, TRUE, '12345678911', '1984', '1949-06-08', 'Companhia das Letras', 'Drama'),
-- da Mariana
('9788532530788', NULL, NULL, TRUE, '98765432100', 'Dom Casmurro', '1899-01-01', 'Carambaia', 'Romance'),
('9788594318602', NULL, NULL, TRUE, '98765432100', 'Memórias Póstumas de Brás Cubas', '1881-01-01', 'Principis', 'Romance'),
('9788532508120', NULL, NULL, TRUE, '98765432100', 'A Hora da Estrela', '1977-10-26', 'Rocco', 'Drama'),
-- do Lucas
('9788560280940', NULL, NULL, TRUE, '45678912300', 'It: A Coisa', '1986-09-15', 'Suma', 'Terror'),
('9788535909555', NULL, NULL, TRUE, '45678912300', 'A Revolução dos Bichos', '1945-08-17', 'Companhia das Letras', 'Drama'),
-- do Pedro
('9788579800245', NULL, NULL, TRUE, '65498732100', 'Jogos Vorazes', '2008-09-14', 'Rocco', 'Ação'),
-- da Ana
('9788598078397', NULL, NULL, TRUE, '32165498700', 'Percy Jackson e o Ladrão de Raios', '2005-06-28', 'Intrínseca', 'Aventura'),
('9788525432186', NULL, NULL, TRUE, '32165498700', 'A Ilha do Tesouro', '1883-11-14', 'L&PM', 'Aventura');

-- AUTORES
INSERT INTO Autor
(id_autor, nome, nacionalidade)
VALUES
(1, 'J. R. R. Tolkien', 'Reino Unido'),
(2, 'Machado de Assis', 'Brasileira'),
(3, 'Clarice Lispector', 'Brasileira'),
(4, 'George Orwell', 'Reino Unido'),
(5, 'Stephen King', 'Estados Unidos'),
(6, 'Suzanne Collins', 'Estados Unidos'),
(7, 'Rick Riordan', 'Estados Unidos'),
(8, 'Robert Louis Stevenson', 'Reino Unido'),
(9, 'Monteiro Lobato', 'Brasileira');

-- LIVROS E SEUS AUTORES
-- Monteiro Lobato fica sem livro, para aparecer só na lista de autores
INSERT INTO Pertence
(ISBN, id_autor)
VALUES
('9788535902777', 1),
('9788595084759', 1),
('9788535914849', 4),
('9788532530788', 2),
('9788594318602', 2),
('9788532508120', 3),
('9788560280940', 5),
('9788535909555', 4),
('9788579800245', 6),
('9788598078397', 7),
('9788525432186', 8);

-- EMPRÉSTIMOS JÁ DEVOLVIDOS (aparecem no histórico e no calendário)
-- Vêm antes dos ativos: o trigger não deixa emprestar livro indisponível
INSERT INTO Emprestimo
(ISBN, titulo_livro, CPF_dono, CPF_leitor, data_emprestimo, data_entrega, data_devolucao)
VALUES
('9788535902777', 'O Hobbit', '12345678911', '98765432100',
    CURDATE() - INTERVAL 60 DAY, CURDATE() - INTERVAL 46 DAY, CURDATE() - INTERVAL 47 DAY),
('9788535914849', '1984', '12345678911', '32165498700',
    CURDATE() - INTERVAL 40 DAY, CURDATE() - INTERVAL 26 DAY, CURDATE() - INTERVAL 28 DAY),
('9788560280940', 'It: A Coisa', '45678912300', '12345678911',
    CURDATE() - INTERVAL 35 DAY, CURDATE() - INTERVAL 21 DAY, CURDATE() - INTERVAL 20 DAY),
('9788598078397', 'Percy Jackson e o Ladrão de Raios', '32165498700', '45678912300',
    CURDATE() - INTERVAL 30 DAY, CURDATE() - INTERVAL 16 DAY, CURDATE() - INTERVAL 17 DAY);

-- EMPRÉSTIMOS ATIVOS (o trigger deixa esses livros indisponíveis)
-- Cada leitor só pode estar com um livro por vez
INSERT INTO Emprestimo
(ISBN, titulo_livro, CPF_dono, CPF_leitor, data_emprestimo, data_entrega, devolucao_solicitada)
VALUES
-- Lucas está com "O Hobbit" do guilherme, dentro do prazo
('9788535902777', 'O Hobbit', '12345678911', '45678912300',
    CURDATE() - INTERVAL 7 DAY, CURDATE() + INTERVAL 7 DAY, NULL),
-- Pedro está com "Dom Casmurro" da Mariana e o prazo venceu há 3 dias
('9788532530788', 'Dom Casmurro', '98765432100', '65498732100',
    CURDATE() - INTERVAL 17 DAY, CURDATE() - INTERVAL 3 DAY, NULL),
-- guilherme devolveu "A Hora da Estrela" ontem; falta a Mariana confirmar
('9788532508120', 'A Hora da Estrela', '98765432100', '12345678911',
    CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 4 DAY, CURDATE() - INTERVAL 1 DAY),
-- Mariana está com "Percy Jackson" da Ana e o prazo vence amanhã
('9788598078397', 'Percy Jackson e o Ladrão de Raios', '32165498700', '98765432100',
    CURDATE() - INTERVAL 13 DAY, CURDATE() + INTERVAL 1 DAY, NULL);

-- PEDIDOS DE EMPRÉSTIMO esperando o dono responder
-- Só quem não está com livro pode pedir: por isso são da Ana e da Beatriz
INSERT INTO Solicitacoes
(CPF_solicitante, ISBN, data_pedido, data_retirada, data_retorno)
VALUES
-- Ana pediu "A Sociedade do Anel" do guilherme
('32165498700', '9788595084759', CURDATE() - INTERVAL 2 DAY, CURDATE() + INTERVAL 2 DAY, CURDATE() + INTERVAL 16 DAY),
-- Ana pediu "It: A Coisa" do Lucas
('32165498700', '9788560280940', CURDATE() - INTERVAL 1 DAY, CURDATE() + INTERVAL 3 DAY, CURDATE() + INTERVAL 17 DAY),
-- Beatriz pediu "Jogos Vorazes" do Pedro
('15975345600', '9788579800245', CURDATE() - INTERVAL 4 DAY, CURDATE() + INTERVAL 1 DAY, CURDATE() + INTERVAL 15 DAY),
-- Beatriz pediu "Memórias Póstumas de Brás Cubas" da Mariana
('15975345600', '9788594318602', CURDATE(), CURDATE() + INTERVAL 5 DAY, CURDATE() + INTERVAL 19 DAY);

-- HISTÓRICO: cada empréstimo aparece para o dono e para o leitor
-- detalhe = nome da outra pessoa
INSERT INTO Historico
(CPF_usuario, tipo, ISBN, titulo, detalhe, data_hora)
VALUES
('12345678911', 'EMPRESTIMO_CONCEDIDO', '9788535902777', 'O Hobbit', 'Mariana', NOW() - INTERVAL 60 DAY),
('98765432100', 'EMPRESTIMO_RECEBIDO', '9788535902777', 'O Hobbit', 'guilherme', NOW() - INTERVAL 60 DAY),
('12345678911', 'EMPRESTIMO_CONCEDIDO', '9788535914849', '1984', 'Ana', NOW() - INTERVAL 40 DAY),
('32165498700', 'EMPRESTIMO_RECEBIDO', '9788535914849', '1984', 'guilherme', NOW() - INTERVAL 40 DAY),
('45678912300', 'EMPRESTIMO_CONCEDIDO', '9788560280940', 'It: A Coisa', 'guilherme', NOW() - INTERVAL 35 DAY),
('12345678911', 'EMPRESTIMO_RECEBIDO', '9788560280940', 'It: A Coisa', 'Lucas', NOW() - INTERVAL 35 DAY),
('32165498700', 'EMPRESTIMO_CONCEDIDO', '9788598078397', 'Percy Jackson e o Ladrão de Raios', 'Lucas', NOW() - INTERVAL 30 DAY),
('45678912300', 'EMPRESTIMO_RECEBIDO', '9788598078397', 'Percy Jackson e o Ladrão de Raios', 'Ana', NOW() - INTERVAL 30 DAY),
('98765432100', 'EMPRESTIMO_CONCEDIDO', '9788532530788', 'Dom Casmurro', 'Pedro', NOW() - INTERVAL 17 DAY),
('65498732100', 'EMPRESTIMO_RECEBIDO', '9788532530788', 'Dom Casmurro', 'Mariana', NOW() - INTERVAL 17 DAY),
('32165498700', 'EMPRESTIMO_CONCEDIDO', '9788598078397', 'Percy Jackson e o Ladrão de Raios', 'Mariana', NOW() - INTERVAL 13 DAY),
('98765432100', 'EMPRESTIMO_RECEBIDO', '9788598078397', 'Percy Jackson e o Ladrão de Raios', 'Ana', NOW() - INTERVAL 13 DAY),
('98765432100', 'EMPRESTIMO_CONCEDIDO', '9788532508120', 'A Hora da Estrela', 'guilherme', NOW() - INTERVAL 10 DAY),
('12345678911', 'EMPRESTIMO_RECEBIDO', '9788532508120', 'A Hora da Estrela', 'Mariana', NOW() - INTERVAL 10 DAY),
('12345678911', 'EMPRESTIMO_CONCEDIDO', '9788535902777', 'O Hobbit', 'Lucas', NOW() - INTERVAL 7 DAY),
('45678912300', 'EMPRESTIMO_RECEBIDO', '9788535902777', 'O Hobbit', 'guilherme', NOW() - INTERVAL 7 DAY);

-- LEMBRETES DO CALENDÁRIO
INSERT INTO Eventos
(CPF_usuario, data_evento, descricao)
VALUES
('12345678911', CURDATE(), 'Separar "A Sociedade do Anel" caso aceite o pedido da Ana'),
('12345678911', CURDATE() + INTERVAL 3 DAY, 'Clube do livro na biblioteca às 19h'),
('98765432100', CURDATE() + INTERVAL 1 DAY, 'Devolver "Percy Jackson" para a Ana'),
('98765432100', CURDATE() + INTERVAL 5 DAY, 'Encontrar a Beatriz para entregar o livro'),
('45678912300', CURDATE() + INTERVAL 7 DAY, 'Terminar de ler "O Hobbit"'),
('65498732100', CURDATE(), 'Devolver "Dom Casmurro" (atrasado!)'),
('32165498700', CURDATE() + INTERVAL 2 DAY, 'Buscar "A Sociedade do Anel" com o guilherme'),
('15975345600', CURDATE() + INTERVAL 10 DAY, 'Prova de literatura: Machado de Assis');

-- MENSAGENS DO CHAT
-- lida = FALSE: aparecem como não lidas para quem recebeu
INSERT INTO Mensagens
(CPF_remetente, CPF_destinatario, texto, data_hora, lida)
VALUES
-- guilherme e Lucas (tudo lido)
('45678912300', '12345678911', 'Oi! Peguei O Hobbit hoje, valeu demais!', NOW() - INTERVAL 7 DAY, TRUE),
('12345678911', '45678912300', 'De nada! Cuida bem dele, é meu favorito.', NOW() - INTERVAL 7 DAY + INTERVAL 10 MINUTE, TRUE),
('45678912300', '12345678911', 'Pode deixar. Já estou na metade!', NOW() - INTERVAL 2 DAY, TRUE),
-- Mariana cobrando o Pedro (o Pedro ainda não leu a última)
('98765432100', '65498732100', 'Oi Pedro, o prazo do Dom Casmurro já venceu.', NOW() - INTERVAL 2 DAY, TRUE),
('65498732100', '98765432100', 'Desculpa! Devolvo essa semana.', NOW() - INTERVAL 2 DAY + INTERVAL 1 HOUR, TRUE),
('98765432100', '65498732100', 'Combinado, me avisa quando puder entregar.', NOW() - INTERVAL 3 HOUR, FALSE),
-- Ana e guilherme sobre o pedido (o guilherme ainda não leu)
('32165498700', '12345678911', 'Oi! Pedi A Sociedade do Anel, pode ser?', NOW() - INTERVAL 1 DAY, FALSE),
('32165498700', '12345678911', 'Posso buscar depois da aula.', NOW() - INTERVAL 1 DAY + INTERVAL 2 MINUTE, FALSE),
-- guilherme avisando a Mariana da devolução (a Mariana ainda não leu)
('12345678911', '98765432100', 'Devolvi A Hora da Estrela, confirma lá no app?', NOW() - INTERVAL 20 HOUR, FALSE),
-- Beatriz e Pedro
('15975345600', '65498732100', 'Oi Pedro! Fiz um pedido do Jogos Vorazes.', NOW() - INTERVAL 4 DAY, TRUE);

-- CONSULTA FINAL
SELECT * FROM Usuarios;
SELECT * FROM Livros;
