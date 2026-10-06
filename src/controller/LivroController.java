package controller;

import java.awt.Color;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.Cadastro_Livro;
import View.Calendario;
import View.Historico;
import View.Perfil;
import View.PesquisarLivro;
import View.TelaAlterar;
import View.TelaCriarConta;
import View.TelaLogin;
import View.TelaMeusLivros;
import View.TelaNotificacoes;
import View.TelaSolicitacoes;
import View.tela_inicial;

import dao.EmprestimoDAO;
import dao.LivroDAO;
import dao.UsuarioDAO;

import model.Livro;
import model.LivroTableModel;
import model.Solicitacao;

public class LivroController {

    private final LivroTableModel livroModel;
    private final UsuarioDAO usuarioDAO;
    private final LivroDAO livroDAO;
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    private final TelaLogin telaLogin;
    private final tela_inicial telaInicial;
    private final PesquisarLivro pesquisarLivro;
    private final TelaSolicitacoes telaSolicitacoes;
    private final TelaNotificacoes telaNotificacoes;
    private final TelaMeusLivros telaMeusLivros;
    private final Perfil perfil;
    private final Cadastro_Livro cadastroLivro;
    private final TelaCriarConta criarConta;
    private final Calendario calendario;
    private final TelaAlterar alterarCadastro;
    private final Historico historico;

    private String emailUsuarioLogado;
    private String cpfUsuarioLogado;



    // =========================================================
    // CONSTRUTORES
    // =========================================================

    public LivroController(
            LivroDAO livroDAO,
            LivroTableModel modelo,
            Cadastro_Livro view) {

        this.livroModel = modelo;
        this.livroDAO = livroDAO;

        this.cadastroLivro =
                (view != null) ? view : new Cadastro_Livro();

        this.criarConta = new TelaCriarConta();
        this.telaLogin = new TelaLogin();
        this.telaInicial = new tela_inicial();
        this.pesquisarLivro = new PesquisarLivro();
        this.telaSolicitacoes = new TelaSolicitacoes();
        this.telaNotificacoes = new TelaNotificacoes();
        this.telaMeusLivros = new TelaMeusLivros();
        this.perfil = new Perfil();
        this.calendario = new Calendario();
        this.alterarCadastro = new TelaAlterar();
        this.usuarioDAO = new UsuarioDAO();
        this.historico = new Historico();

        configurarEventos();
    }


    public LivroController(
            LivroDAO livroDAO,
            LivroTableModel modelo) {

        this(livroDAO, modelo, new Cadastro_Livro());
    }


    // =========================================================
    // CONFIGURAÇÃO DOS EVENTOS
    // =========================================================

    private void configurarEventos() {

        // --- LOGIN ---

        telaLogin.getBotaoEntrar()
                .addActionListener(e -> validarLogin());

        telaLogin.getLblCadastro()
                .addActionListener(e -> abrirCriarConta());

        telaLogin.getTxtSenha()
                .addActionListener(e -> validarLogin());


        // --- CRIAR CONTA ---

        criarConta.getBotaoCadastrar()
                .addActionListener(e -> cadastrarUsuario());

        criarConta.getBotaoEntrar()
                .addActionListener(e -> iniciar());


        // --- TELA INICIAL / HOME ---

        telaInicial.getBtnPesquisar()
                .addActionListener(e -> abrirPesquisa());

        telaInicial.getBtnMeusLivros()
                .addActionListener(e -> abrirMeusLivros());

        telaInicial.getBtnSolicitacoes()
                .addActionListener(e -> abrirSolicitacoes());

        telaInicial.getBtnPerfil()
                .addActionListener(e -> abrirPerfil());

        telaInicial.getBtnCalendario()
                .addActionListener(e -> abrirCalendario());

        telaInicial.getBtnHistorico()
                .addActionListener(e -> abrirHistorico());


        // --- PESQUISA ---

        pesquisarLivro.getBtnHome()
                .addActionListener(e -> abrirHome());

        pesquisarLivro.getBtnPesquisar()
                .addActionListener(e -> pesquisar());


        // --- MEUS LIVROS ---

        telaMeusLivros.getBtnHome()
                .addActionListener(e -> abrirHome());

        telaMeusLivros.getBtnNewButton()
                .addActionListener(e -> abrirCadastroLivro());


        // --- SOLICITAÇÕES ---

        telaSolicitacoes.getBtnHome()
                .addActionListener(e -> abrirHome());

        telaSolicitacoes.setAoAceitar(this::aceitarSolicitacao);

        telaSolicitacoes.setAoRecusar(this::recusarSolicitacao);


        // --- CADASTRO DE LIVROS ---

        cadastroLivro.getBtnVoltar()
                .addActionListener(e -> abrirMeusLivros());

        cadastroLivro.getBtnAdicionar()
                .addActionListener(e -> {

                    // Se salvou, volta para Meus Livros
                    if (cadastroLivro.cadastrarLivro()) {
                        abrirMeusLivros();
                    }
                });


        // --- CALENDÁRIO ---

        calendario.getBtnHome()
                .addActionListener(e -> abrirHome());


        // --- HISTÓRICO ---

        historico.getBtnHome()
                .addActionListener(e -> abrirHome());


        // --- PERFIL ---

        perfil.getBtnHome()
                .addActionListener(e -> abrirHome());

        perfil.getBtnSair()
                .addActionListener(e -> logoff());

        perfil.getBtnAlterarCadastro()
                .addActionListener(e -> abrirAlterarCadastro());


        perfil.getBtnAlterarFoto()
                .addActionListener(e -> {

                    try {

                        perfil.selecionarEAtualizarFoto();

                        byte[] foto = perfil.getFotoSelecionada();

                        if (foto != null && foto.length > 0) {

                            usuarioDAO.atualizarFoto(
                                    cpfUsuarioLogado,
                                    foto
                            );

                            telaInicial.atualizarFotoPerfil(foto);

                            mostrarMensagem(
                                    "Foto atualizada com sucesso!"
                            );
                        }

                    } catch (SQLException ex) {

                        ex.printStackTrace();

                        mostrarMensagem(
                                "Erro ao salvar a foto."
                        );
                    }
                });


        // --- ALTERAR CADASTRO ---

        alterarCadastro.getBtnVoltar()
                .addActionListener(e -> voltarDoCadastro());

        alterarCadastro.getBotaoCadastrar()
                .addActionListener(e -> atualizarCadastro());
    }


    // =========================================================
    // NAVEGAÇÃO
    // =========================================================

    public void voltarDoCadastro() {

        esconderTodas();

        perfil.setVisible(true);
    }


    public void iniciar() {

        esconderTodas();

        telaLogin.setVisible(true);
    }


    public void logoff() {

        emailUsuarioLogado = null;
        cpfUsuarioLogado = null;

        telaInicial.atualizarFotoPerfil(null);
        perfil.carregarFoto(null);

        esconderTodas();

        telaLogin.getTxtNome().setText("");
        telaLogin.getTxtSenha().setText("");

        telaLogin.setVisible(true);
    }


    private void abrirHome() {

        try {

            byte[] foto =
                    usuarioDAO.buscarFoto(cpfUsuarioLogado);

            if (foto != null && foto.length > 0) {

                telaInicial.atualizarFotoPerfil(foto);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        esconderTodas();

        telaInicial.setVisible(true);
    }


    private void abrirPesquisa() {

        pesquisarLivro.setCpfUsuario(cpfUsuarioLogado);

        esconderTodas();

        pesquisarLivro.setVisible(true);
    }


    private void abrirMeusLivros() {

        try {

            telaMeusLivros.mostrarLivros(
                    livroDAO.listarPorDono(cpfUsuarioLogado)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem("Erro ao carregar seus livros:\n" + e.getMessage());
        }

        esconderTodas();

        telaMeusLivros.setVisible(true);
    }


    private void abrirSolicitacoes() {

        carregarSolicitacoes();

        esconderTodas();

        telaSolicitacoes.setVisible(true);

        telaSolicitacoes.toFront();
        telaSolicitacoes.requestFocus();
    }


    public void abrirNotificacoes() {

        esconderTodas();

        telaNotificacoes.setVisible(true);

        telaNotificacoes.toFront();
        telaNotificacoes.requestFocus();
    }


    private void abrirPerfil() {

        try {

            perfil.setCpfUsuario(cpfUsuarioLogado);

            String[] dados =
                    usuarioDAO.buscarUsuarioPorEmail(
                            emailUsuarioLogado
                    );

            if (dados != null) {

                perfil.atualizarDados(
                        dados[0],
                        dados[1],
                        dados[2],
                        dados[3]
                );
            }


            byte[] foto =
                    usuarioDAO.buscarFoto(cpfUsuarioLogado);

            if (foto != null && foto.length > 0) {

                perfil.carregarFoto(foto);
            }

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar os dados do perfil."
            );
        }

        esconderTodas();

        perfil.setVisible(true);
    }


    public void abrirCadastroLivro() {

        cadastroLivro.setCpfUsuario(cpfUsuarioLogado);

        esconderTodas();

        cadastroLivro.setVisible(true);
    }


    private void abrirCriarConta() {

        esconderTodas();

        criarConta.setVisible(true);
    }


    private void abrirCalendario() {

        esconderTodas();

        calendario.setVisible(true);
    }


    private void abrirAlterarCadastro() {

        try {

            String[] dados =
                    usuarioDAO.buscarDadosParaAlteracao(
                            cpfUsuarioLogado
                    );

            if (dados != null) {

                // CPF
                alterarCadastro.getTxtCpf()
                        .setText(dados[0]);

                // Nome
                alterarCadastro.getTxtNome()
                        .setText(dados[1]);

                // E-mail
                alterarCadastro.getTxtEmail()
                        .setText(dados[2]);

                // Telefone
                alterarCadastro.getTxtTelefone()
                        .setText(dados[3]);

                // Data de nascimento
                alterarCadastro.getTxtDataNascimento()
                        .setText(dados[4]);

                // Senha
                alterarCadastro.getTxtSenha()
                        .setText(dados[5]);

                // Confirmar senha
                alterarCadastro.getTxtConfirmarSenha()
                        .setText(dados[5]);

            } else {

                mostrarMensagem(
                        "Não foi possível encontrar os dados do usuário."
                );

                return;
            }

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar os dados do cadastro."
            );

            return;
        }

        esconderTodas();

        alterarCadastro.setVisible(true);
    }


    private void abrirHistorico() {

        esconderTodas();

        historico.setVisible(true);
    }


    private void esconderTodas() {

        telaLogin.setVisible(false);
        telaInicial.setVisible(false);
        pesquisarLivro.setVisible(false);
        telaSolicitacoes.setVisible(false);
        telaNotificacoes.setVisible(false);
        telaMeusLivros.setVisible(false);
        perfil.setVisible(false);
        cadastroLivro.setVisible(false);
        criarConta.setVisible(false);
        calendario.setVisible(false);
        alterarCadastro.setVisible(false);
        historico.setVisible(false);
    }


    // =========================================================
    // CADASTRAR USUÁRIO
    // =========================================================

    private void cadastrarUsuario() {

        String nome =
                criarConta.getTxtNome()
                        .getText()
                        .trim();

        String email =
                criarConta.getTxtEmail()
                        .getText()
                        .trim();

        String telefone =
                criarConta.getTxtTelefone()
                        .getText()
                        .trim();

        String cpf =
                criarConta.getTxtCpf()
                        .getText()
                        .trim();

        String dataNascimento =
                criarConta.getTxtDataNascimento()
                        .getText()
                        .trim();

        String senha =
                new String(
                        criarConta.getTxtSenha()
                                .getPassword()
                );

        String confirmarSenha =
                new String(
                        criarConta.getTxtConfirmarSenha()
                                .getPassword()
                );


        if (nome.isEmpty()
                || email.isEmpty()
                || telefone.isEmpty()
                || cpf.isEmpty()
                || dataNascimento.contains("_")
                || senha.isEmpty()
                || confirmarSenha.isEmpty()) {

            mostrarMensagem(
                    "Preencha todos os campos."
            );

            return;
        }


        if (senha.length() > 20) {

            mostrarMensagem(
                    "A senha deve ter no máximo 20 caracteres."
            );

            return;
        }


        if (!senha.equals(confirmarSenha)) {

            mostrarMensagem(
                    "As senhas não são iguais."
            );

            return;
        }


        cpf = cpf.replaceAll("\\D", "");


        if (cpf.length() != 11) {

            mostrarMensagem(
                    "CPF deve possuir 11 números."
            );

            return;
        }


        try {

            usuarioDAO.cadastrar(
                    cpf,
                    nome,
                    telefone,
                    email,
                    senha,
                    dataNascimento
            );

            mostrarMensagem(
                    "Cadastro realizado com sucesso!"
            );

            iniciar();

        } catch (SQLException e) {

            e.printStackTrace();

            if (e.getMessage() != null
                    && e.getMessage().contains("Duplicate")) {

                mostrarMensagem(
                        "Este CPF já está cadastrado."
                );

            } else {

                mostrarMensagem(
                        "Erro ao cadastrar usuário."
                );
            }
        }
    }


    // =========================================================
    // LOGIN
    // =========================================================

    private void validarLogin() {

        String email =
                telaLogin.getTxtNome()
                        .getText()
                        .trim();

        String senha =
                new String(
                        telaLogin.getTxtSenha()
                                .getPassword()
                );


        if (email.isEmpty() || senha.isEmpty()) {

            mostrarMensagem(
                    "Preencha o e-mail e a senha."
            );

            return;
        }


        try {

            boolean loginValido =
                    usuarioDAO.login(
                            email,
                            senha
                    );


            if (loginValido) {

                emailUsuarioLogado = email;

                cpfUsuarioLogado =
                        usuarioDAO.buscarCpfPorEmail(email);

                telaLogin.setVisible(false);

                abrirHome();

            } else {

                mostrarMensagem(
                        "E-mail ou senha incorretos."
                );
            }


        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao acessar o banco de dados."
            );
        }
    }


    // =========================================================
    // PESQUISAR LIVRO
    // =========================================================

    private void pesquisar() {

        String texto =
                pesquisarLivro.getTextoPesquisa().toLowerCase();

        try {

            // Busca todos os livros do banco
            List<Livro> listaLivros =
                    livroDAO.listar();

            // Se digitou algo, filtra pelo título
            if (!texto.isEmpty()) {
                listaLivros.removeIf(livro ->
                        livro.getNome() == null
                        || !livro.getNome().toLowerCase().contains(texto));
            }

            pesquisarLivro.mostrarLivros(listaLivros);

            if (listaLivros.isEmpty()) {

                mostrarMensagem(
                        "Nenhum livro encontrado."
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao pesquisar livros:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // SOLICITAÇÕES
    // =========================================================

    private void carregarSolicitacoes() {

        try {

            telaSolicitacoes.mostrarSolicitacoes(
                    emprestimoDAO.listarPendentesDoDono(cpfUsuarioLogado)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar solicitações:\n" + e.getMessage()
            );
        }
    }


    private void aceitarSolicitacao(Solicitacao solicitacao) {

        try {

            if (!emprestimoDAO.livroDisponivel(solicitacao.getIsbn())) {

                mostrarMensagem(
                        "\"" + solicitacao.getTituloLivro()
                        + "\" já está emprestado."
                );
                return;
            }

            if (!confirmar("Emprestar \"" + solicitacao.getTituloLivro()
                    + "\" para " + solicitacao.getNomeSolicitante() + "?")) {
                return;
            }

            emprestimoDAO.aceitar(solicitacao);

            mostrarMensagem(
                    "Solicitação aceita!\n"
                    + "Combine a entrega com " + solicitacao.getNomeSolicitante()
                    + ":\n" + solicitacao.getEmailSolicitante()
                    + "  |  " + solicitacao.getTelefoneSolicitante()
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao aceitar solicitação:\n" + e.getMessage()
            );
        }

        carregarSolicitacoes();
    }


    private void recusarSolicitacao(Solicitacao solicitacao) {

        if (!confirmar("Recusar o pedido de " + solicitacao.getNomeSolicitante()
                + " para \"" + solicitacao.getTituloLivro() + "\"?")) {
            return;
        }

        try {

            emprestimoDAO.recusar(solicitacao);

            mostrarMensagem(
                    "Solicitação recusada."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao recusar solicitação:\n" + e.getMessage()
            );
        }

        carregarSolicitacoes();
    }


    // =========================================================
    // ATUALIZAR CADASTRO
    // =========================================================

    private void atualizarCadastro() {

        String cpfAtual =
                cpfUsuarioLogado;


        String cpfNovo =
                alterarCadastro.getTxtCpf()
                        .getText()
                        .trim();

        String nome =
                alterarCadastro.getTxtNome()
                        .getText()
                        .trim();

        String telefone =
                alterarCadastro.getTxtTelefone()
                        .getText()
                        .trim();

        String email =
                alterarCadastro.getTxtEmail()
                        .getText()
                        .trim();

        String dataNascimento =
                alterarCadastro.getTxtDataNascimento()
                        .getText()
                        .trim();

        String senha =
                new String(
                        alterarCadastro.getTxtSenha()
                                .getPassword()
                );

        String confirmarSenha =
                new String(
                        alterarCadastro.getTxtConfirmarSenha()
                                .getPassword()
                );


        // ==============================
        // VALIDA CAMPOS
        // ==============================

        if (cpfNovo.isEmpty()
                || nome.isEmpty()
                || telefone.isEmpty()
                || email.isEmpty()
                || dataNascimento.isEmpty()
                || senha.isEmpty()
                || confirmarSenha.isEmpty()) {

            mostrarMensagem(
                    "Preencha todos os campos."
            );

            return;
        }


        // ==============================
        // VALIDA SENHA
        // ==============================

        if (senha.length() > 20) {

            mostrarMensagem(
                    "A senha deve ter no máximo 20 caracteres."
            );

            return;
        }


        if (!senha.equals(confirmarSenha)) {

            mostrarMensagem(
                    "As senhas não são iguais."
            );

            return;
        }


        // ==============================
        // VALIDA CPF
        // ==============================

        String cpfLimpo =
                cpfNovo.replaceAll("\\D", "");


        if (cpfLimpo.length() != 11) {

            mostrarMensagem(
                    "CPF deve possuir 11 números."
            );

            return;
        }


        try {

            // ==============================
            // ATUALIZA NO BANCO
            // ==============================

            usuarioDAO.atualizarCadastro(
                    cpfAtual,
                    cpfLimpo,
                    nome,
                    telefone,
                    email,
                    senha,
                    dataNascimento
            );


            // ==============================
            // ATUALIZA SESSÃO
            // ==============================

            cpfUsuarioLogado = cpfLimpo;
            emailUsuarioLogado = email;


            // ==============================
            // BUSCA DADOS ATUALIZADOS
            // ==============================

            String[] dadosAtualizados =
                    usuarioDAO.buscarUsuarioPorEmail(
                            emailUsuarioLogado
                    );


            if (dadosAtualizados != null) {

                perfil.atualizarDados(
                        dadosAtualizados[0],
                        dadosAtualizados[1],
                        dadosAtualizados[2],
                        dadosAtualizados[3]
                );
            }


            // ==============================
            // VOLTA PARA O PERFIL
            // ==============================

            esconderTodas();

            perfil.setVisible(true);


            mostrarMensagem(
                    "Cadastro atualizado com sucesso!"
            );


        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao atualizar cadastro:\n"
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        UIManager.put(
                "OptionPane.background",
                new Color(175, 244, 198)
        );

        UIManager.put(
                "Panel.background",
                new Color(175, 244, 198)
        );


        JOptionPane.showMessageDialog(
                null,
                mensagem
        );


        UIManager.put(
                "OptionPane.background",
                null
        );

        UIManager.put(
                "Panel.background",
                null
        );
    }


    // Pergunta Sim/Não com a janela verde
    private boolean confirmar(String pergunta) {

        UIManager.put(
                "OptionPane.background",
                new Color(175, 244, 198)
        );

        UIManager.put(
                "Panel.background",
                new Color(175, 244, 198)
        );

        Object[] opcoes = { "Sim", "Não" };

        int resposta = JOptionPane.showOptionDialog(
                null,
                pergunta,
                "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[1]
        );

        UIManager.put(
                "OptionPane.background",
                null
        );

        UIManager.put(
                "Panel.background",
                null
        );

        return resposta == 0;
    }


    // =========================================================
    // GETTERS
    // =========================================================

    public LivroTableModel getLivroModel() {
        return livroModel;
    }


    public TelaLogin getTelaLogin() {
        return telaLogin;
    }


    public tela_inicial getTelaInicial() {
        return telaInicial;
    }


    public PesquisarLivro getPesquisarLivro() {
        return pesquisarLivro;
    }


    public TelaSolicitacoes getTelaSolicitacoes() {
        return telaSolicitacoes;
    }


    public TelaNotificacoes getTelaNotificacoes() {
        return telaNotificacoes;
    }


    public TelaMeusLivros getTelaMeusLivros() {
        return telaMeusLivros;
    }


    public Perfil getPerfil() {
        return perfil;
    }


    public Cadastro_Livro getCadastro() {
        return cadastroLivro;
    }
}