package controller;

import java.awt.Color;
import java.sql.SQLException;

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

import model.LivroTableModel;

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

    private final PesquisarController pesquisarController;
    private final LoginController loginController;
    private final SolicitacaoController solicitacaoController;
    private final PerfilController perfilController;

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

        this.pesquisarController =
                new PesquisarController(livroDAO, pesquisarLivro);

        this.loginController =
                new LoginController(usuarioDAO, telaLogin, criarConta);

        this.solicitacaoController =
                new SolicitacaoController(emprestimoDAO, telaSolicitacoes);

        this.perfilController =
                new PerfilController(usuarioDAO, perfil, alterarCadastro, telaInicial);

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
                .addActionListener(e -> {

                    // Se cadastrou, volta para o login
                    if (loginController.cadastrarUsuario()) {
                        iniciar();
                    }
                });

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


        // --- MEUS LIVROS ---

        telaMeusLivros.getBtnHome()
                .addActionListener(e -> abrirHome());

        telaMeusLivros.getBtnNewButton()
                .addActionListener(e -> abrirCadastroLivro());


        // --- SOLICITAÇÕES ---

        telaSolicitacoes.getBtnHome()
                .addActionListener(e -> abrirHome());


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
                .addActionListener(e ->
                        perfilController.alterarFoto(cpfUsuarioLogado));

        perfil.getBtnExcluirConta()
                .addActionListener(e -> {

                    // Se excluiu, volta para o login
                    if (perfilController.excluirConta(cpfUsuarioLogado)) {
                        logoff();
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
        pesquisarController.pesquisar();
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

        solicitacaoController.carregarSolicitacoes(cpfUsuarioLogado);

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

        perfilController.carregarPerfil(
                emailUsuarioLogado,
                cpfUsuarioLogado
        );

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

        if (!perfilController.carregarDadosAlteracao(cpfUsuarioLogado)) {
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
    // LOGIN
    // =========================================================

    private void validarLogin() {

        if (loginController.validarLogin()) {

            emailUsuarioLogado = loginController.getEmailLogado();
            cpfUsuarioLogado = loginController.getCpfLogado();

            telaLogin.setVisible(false);

            abrirHome();
        }
    }


    // =========================================================
    // ATUALIZAR CADASTRO
    // =========================================================

    private void atualizarCadastro() {

        if (!perfilController.atualizarCadastro(cpfUsuarioLogado)) {
            return;
        }

        // ==============================
        // ATUALIZA SESSÃO
        // ==============================

        cpfUsuarioLogado = perfilController.getCpfAtualizado();
        emailUsuarioLogado = perfilController.getEmailAtualizado();


        // ==============================
        // VOLTA PARA O PERFIL
        // ==============================

        esconderTodas();

        perfil.setVisible(true);


        mostrarMensagem(
                "Cadastro atualizado com sucesso!"
        );
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