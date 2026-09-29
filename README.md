# 📚 Capas Vivas

Sistema desktop desenvolvido em **Java** para gerenciamento e compartilhamento de livros. O projeto foi desenvolvido com o objetivo de aplicar conceitos de programação orientada a objetos, interface gráfica, banco de dados e arquitetura organizada em camadas.

## 🚀 Sobre o projeto

O **Capas Vivas** é uma aplicação que permite aos usuários cadastrar livros, pesquisar obras e gerenciar seus próprios dados.

O sistema possui uma interface gráfica desenvolvida em **Java Swing**, integração com banco de dados **MySQL** e organização do código utilizando uma estrutura baseada em **MVC/DAO**.

## ✨ Funcionalidades

* 🔐 Login de usuários
* 📝 Cadastro de novos usuários
* 👤 Visualização do perfil
* ✏️ Alteração dos dados cadastrais
* 🖼️ Alteração da foto de perfil
* 📚 Cadastro de livros
* 🔎 Pesquisa de livros
* 📖 Visualização dos livros cadastrados
* 🤝 Sistema de solicitações de empréstimo
* 📅 Calendário
* 🔔 Sistema de notificações
* 📋 Histórico de empréstimos
* 🚪 Logout
* 💾 Persistência dos dados em banco de dados

## 🛠️ Tecnologias utilizadas

* ☕ **Java 21**
* 🖥️ **Java Swing**
* 🎨 **MigLayout**
* 🗄️ **MySQL**
* 🔌 **JDBC**
* 🧩 **Eclipse IDE**
* 🏗️ **DAO**
* 🏛️ **MVC / separação em camadas**

## 📂 Estrutura do projeto

```text
src/
├── View/
│   ├── TelaLogin.java
│   ├── TelaCriarConta.java
│   ├── tela_inicial.java
│   ├── Perfil.java
│   ├── Cadastro_Livro.java
│   ├── PesquisarLivro.java
│   ├── TelaMeusLivros.java
│   ├── TelaSolicitacoes.java
│   ├── TelaNotificacoes.java
│   ├── Calendario.java
│   └── Historico.java
│
├── controller/
│   └── LivroController.java
│
├── dao/
│   ├── UsuarioDAO.java
│   ├── LivroDAO.java
│   └── AutorDAO.java
│
└── model/
    └── Classes relacionadas ao sistema
```

## 🗃️ Banco de dados

O sistema utiliza **MySQL** para armazenar as informações dos usuários, livros, autores e empréstimos.

Entre as principais tabelas estão:

* `Usuarios`
* `Livros`
* `Autor`
* `Pertence`
* `Emprestimos`

As informações dos usuários incluem dados como nome, CPF, telefone, e-mail, data de nascimento, senha e foto de perfil.

## 🖥️ Interface

A aplicação possui uma interface gráfica desenvolvida com **Java Swing** e organizada com **MigLayout**, buscando proporcionar uma experiência simples e intuitiva para o usuário.

Entre as telas disponíveis estão:

* Tela de Login
* Tela de Cadastro
* Tela Inicial
* Perfil
* Alteração de Cadastro
* Cadastro de Livros
* Pesquisa de Livros
* Meus Livros
* Solicitações
* Notificações
* Histórico
* Calendário

## ⚙️ Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/GustavoSchwabeRazera/Capas-Vivas.git
```

### 2. Abra o projeto no Eclipse

Importe o projeto no **Eclipse IDE** e verifique se o **Java 21** está configurado.

### 3. Configure o banco de dados

Crie o banco de dados MySQL e execute o script SQL do projeto para criar as tabelas necessárias.

Depois, configure as informações de conexão no projeto:

```text
Host
Porta
Usuário
Senha
Banco de dados
```

### 4. Configure as bibliotecas

Verifique se as dependências utilizadas pelo projeto estão adicionadas ao **Build Path** do Eclipse, principalmente o driver JDBC do MySQL e o MigLayout.

### 5. Execute o projeto

Execute a classe principal da aplicação pelo Eclipse.

## 🎯 Objetivos do projeto

Este projeto foi desenvolvido com o objetivo de praticar e consolidar conhecimentos em:

* Programação Orientada a Objetos
* Java
* Java Swing
* Interfaces gráficas
* Banco de dados
* SQL
* JDBC
* Padrão DAO
* Organização de projetos
* Manipulação de imagens
* Eventos e componentes gráficos
* Desenvolvimento de aplicações desktop

---

⭐ Se este projeto foi útil ou interessante, considere deixar uma estrela no repositório!
