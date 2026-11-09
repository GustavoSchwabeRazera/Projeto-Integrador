package controller;

import java.sql.SQLException;
import java.time.Year;


import View.JanelaMensagem;
import javax.swing.JFrame;

import View.Cadastro_Livro;
import View.Calendario;
import View.Historico;
import View.Perfil;
import View.PesquisarLivro;
import View.TelaAlterar;
import View.TelaCriarConta;
import View.TelaLivrosComigo;
import View.TelaLogin;
import View.TelaMeusLivros;
import View.TelaNotificacoes;
import View.TelaSolicitacoes;
import View.tela_inicial;

import dao.EmprestimoDAO;
import dao.HistoricoDAO;
import dao.EventoDAO;
import dao.LivroDAO;
import dao.UsuarioDAO;

import model.Livro;
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
    private final TelaLivrosComigo telaLivrosComigo;
    private final Perfil perfil;
    private final Cadastro_Livro cadastroLivro;
    private final TelaCriarConta criarConta;
    private final Calendario calendario;
    private final TelaAlterar alterarCadastro;
    private final Historico historico;

    private final PesquisarController pesquisarController;
    private final LoginController loginController;
    private final SolicitacaoController solicitacaoController;
    private final NotificacaoController notificacaoController;
    private final LivrosComigoController livrosComigoController;
    private final CalendarioController calendarioController;
    private final PerfilController perfilController;
    private final HistoricoController historicoController;
    private final AutorController autorController;

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
        this.telaLivrosComigo = new TelaLivrosComigo();
        this.perfil = new Perfil();
        this.calendario = new Calendario();
        this.alterarCadastro = new TelaAlterar();
        this.usuarioDAO = new UsuarioDAO();
        this.historico = new Historico();

        this.pesquisarController =
                new PesquisarController(livroDAO, emprestimoDAO, usuarioDAO, pesquisarLivro);

        this.loginController =
                new LoginController(usuarioDAO, telaLogin, criarConta);

        this.solicitacaoController =
                new SolicitacaoController(emprestimoDAO, telaSolicitacoes);

        this.calendarioController =
                new CalendarioController(new EventoDAO(), emprestimoDAO, calendario);

        this.notificacaoController =
                new NotificacaoController(emprestimoDAO, telaNotificacoes);

        this.livrosComigoController =
                new LivrosComigoController(emprestimoDAO, telaLivrosComigo);

        this.perfilController =
                new PerfilController(usuarioDAO, perfil, alterarCadastro, telaInicial);

        this.historicoController =
                new HistoricoController(new HistoricoDAO(), historico);

        this.autorController =
                new AutorController(cadastroLivro);

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

        telaInicial.getBtnLivrosComigo()
                .addActionListener(e -> abrirLivrosComigo());

        telaInicial.getBtnPerfil()
                .addActionListener(e -> abrirPerfil());

        telaInicial.getBtnCalendario()
                .addActionListener(e -> abrirCalendario());

        telaInicial.getBtnHistorico()
                .addActionListener(e -> abrirHistorico());

        telaInicial.getBtnNotificacao()
                .addActionListener(e -> abrirNotificacoes());


        // --- PESQUISA ---

        pesquisarLivro.getBtnHome()
                .addActionListener(e -> abrirHome());


        // --- MEUS LIVROS ---

        telaMeusLivros.getBtnHome()
                .addActionListener(e -> abrirHome());

        telaMeusLivros.getBtnNewButton()
                .addActionListener(e -> abrirCadastroLivro());

        telaMeusLivros.setAoAlterar(this::abrirAlterarLivro);

        telaMeusLivros.setAoRemover(this::removerLivro);

        telaMeusLivros.setAoDevolver(this::registrarDevolucao);

        telaMeusLivros.setBuscarComQuem(this::buscarComQuem);


        // --- LIVROS COMIGO ---

        telaLivrosComigo.getBtnHome()
                .addActionListener(e -> abrirHome());


        // --- SOLICITAÇÕES ---

        telaSolicitacoes.getBtnHome()
                .addActionListener(e -> abrirHome());


        // --- NOTIFICAÇÕES ---

        telaNotificacoes.getBtnHome()
                .addActionListener(e -> abrirHome());

        telaNotificacoes.setAoAbrirTela(destino -> {

            switch (destino) {
                case SOLICITACOES -> abrirSolicitacoes();
                case MEUS_LIVROS -> abrirMeusLivros();
                case LIVROS_COMIGO -> abrirLivrosComigo();
            }
        });


        // --- CADASTRO DE LIVROS ---

        cadastroLivro.getBtnVoltar()
                .addActionListener(e -> abrirMeusLivros());

        cadastroLivro.getBtnAdicionar()
                .addActionListener(e -> {

                    // Se salvou, volta para Meus Livros
                    if (salvarLivro()) {
                        abrirMeusLivros();
                    }
                });

        cadastroLivro.getBotaoNovoAutor()
                .addActionListener(e -> autorController.abrirLista());


        // --- CALENDÁRIO ---

        calendario.getBtnHome()
                .addActionListener(e -> abrirHome());

        calendario.getBtnAdicionarEvento()
                .addActionListener(e -> calendarioController.adicionarEvento());

        calendario.getCampoNovoEvento()
                .addActionListener(e -> calendarioController.adicionarEvento());

        calendario.getBtnAlterarEvento()
                .addActionListener(e -> calendarioController.alterarEvento());

        calendario.getBtnRemoverEvento()
                .addActionListener(e -> calendarioController.removerEvento());


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

        trocarPara(perfil);
    }


    public void iniciar() {

        trocarPara(telaLogin);
    }


    public void logoff() {

        emailUsuarioLogado = null;
        cpfUsuarioLogado = null;

        telaInicial.atualizarFotoPerfil(null);
        perfil.carregarFoto(null);

        telaLogin.getTxtNome().setText("");
        telaLogin.getTxtSenha().setText("");

        trocarPara(telaLogin);
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

        trocarPara(telaInicial);

        notificacaoController.avisarPrazosDoDia(cpfUsuarioLogado);
    }


    private void abrirPesquisa() {

        pesquisarLivro.setCpfUsuario(cpfUsuarioLogado);

        pesquisarController.pesquisar();
        trocarPara(pesquisarLivro);
    }


    private void abrirMeusLivros() {

        carregarMeusLivros();

        trocarPara(telaMeusLivros);
    }


    private void carregarMeusLivros() {

        try {

            telaMeusLivros.mostrarLivros(
                    livroDAO.listarPorDono(cpfUsuarioLogado)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem("Erro ao carregar seus livros:\n" + e.getMessage());
        }
    }


    private void abrirLivrosComigo() {

        livrosComigoController.carregarLivros(cpfUsuarioLogado);

        trocarPara(telaLivrosComigo);
    }


    private void abrirSolicitacoes() {

        solicitacaoController.carregarSolicitacoes(cpfUsuarioLogado);

        trocarPara(telaSolicitacoes);

        telaSolicitacoes.toFront();
        telaSolicitacoes.requestFocus();
    }


    public void abrirNotificacoes() {

        notificacaoController.carregarNotificacoes(cpfUsuarioLogado);

        trocarPara(telaNotificacoes);

        telaNotificacoes.toFront();
        telaNotificacoes.requestFocus();
    }


    private void abrirPerfil() {

        perfilController.carregarPerfil(
                emailUsuarioLogado,
                cpfUsuarioLogado
        );

        trocarPara(perfil);
    }


    public void abrirCadastroLivro() {

        cadastroLivro.setCpfUsuario(cpfUsuarioLogado);

        cadastroLivro.prepararCadastro();

        trocarPara(cadastroLivro);
    }


    private void abrirAlterarLivro(Livro livro) {

        cadastroLivro.setCpfUsuario(cpfUsuarioLogado);

        cadastroLivro.prepararEdicao(livro);

        trocarPara(cadastroLivro);
    }


    private void abrirCriarConta() {

        trocarPara(criarConta);
    }


    private void abrirCalendario() {

        calendarioController.carregarCalendario(cpfUsuarioLogado);

        trocarPara(calendario);
    }


    private void abrirAlterarCadastro() {

        if (!perfilController.carregarDadosAlteracao(cpfUsuarioLogado)) {
            return;
        }

        trocarPara(alterarCadastro);
    }


    private void abrirHistorico() {

        historicoController.carregarHistorico(cpfUsuarioLogado);

        trocarPara(historico);
    }


    // Mostra a tela nova antes de esconder as outras: se o sistema ficar um
    // instante sem nenhuma tela aberta, o Windows passa o foco para outro programa
    private void trocarPara(JFrame tela) {

        tela.setVisible(true);

        tela.toFront();

        JFrame[] telas = {
                telaLogin,
                telaInicial,
                pesquisarLivro,
                telaSolicitacoes,
                telaNotificacoes,
                telaMeusLivros,
                telaLivrosComigo,
                perfil,
                cadastroLivro,
                criarConta,
                calendario,
                alterarCadastro,
                historico
        };

        for (JFrame outra : telas) {

            if (outra != tela) {
                outra.setVisible(false);
            }
        }
    }


    // =========================================================
    // LOGIN
    // =========================================================

    private void validarLogin() {

        if (loginController.validarLogin()) {

            emailUsuarioLogado = loginController.getEmailLogado();
            cpfUsuarioLogado = loginController.getCpfLogado();

            abrirHome();
        }
    }


    // =========================================================
    // SALVAR LIVRO (CADASTRO NOVO OU ALTERAÇÃO)
    // =========================================================

    // Retorna true se o livro foi salvo
    private boolean salvarLivro() {

        String titulo = cadastroLivro.getTxtNome().getText().trim();

        String editora = cadastroLivro.getTxtEditora().getText().trim();

        String anoTexto = cadastroLivro.getTxtAno().getText().trim();

        String isbn = cadastroLivro.getTextField().getText().trim();

        Object genero = cadastroLivro.getComboBox().getSelectedItem();

        boolean alterando = cadastroLivro.getLivroEmEdicao() != null;


        if (titulo.isEmpty()) {

            mostrarMensagem("Digite o título do livro.");

            cadastroLivro.getTxtNome().requestFocus();

            return false;
        }


        if (titulo.length() > 50) {

            mostrarMensagem("O título deve ter no máximo 50 caracteres.");

            cadastroLivro.getTxtNome().requestFocus();

            return false;
        }


        if (editora.isEmpty()) {

            mostrarMensagem("Digite a editora.");

            cadastroLivro.getTxtEditora().requestFocus();

            return false;
        }


        if (editora.length() > 50) {

            mostrarMensagem("A editora deve ter no máximo 50 caracteres.");

            cadastroLivro.getTxtEditora().requestFocus();

            return false;
        }


        int ano;

        try {

            ano = Integer.parseInt(anoTexto);

        } catch (NumberFormatException e) {

            mostrarMensagem("Digite um ano de lançamento válido.");

            cadastroLivro.getTxtAno().requestFocus();

            return false;
        }


        if (ano < 1 || ano > Year.now().getValue()) {

            mostrarMensagem("Digite um ano de lançamento válido.");

            cadastroLivro.getTxtAno().requestFocus();

            return false;
        }


        if (!isbn.matches("\\d{13}")) {

            mostrarMensagem("O ISBN deve conter exatamente 13 números.");

            cadastroLivro.getTextField().requestFocus();

            return false;
        }


        if (genero == null) {

            mostrarMensagem("Selecione o gênero.");

            return false;
        }


        if (cadastroLivro.getAutoresSelecionados().isEmpty()) {

            mostrarMensagem("Selecione pelo menos um autor.");

            return false;
        }


        Livro livro =
                new Livro(
                        isbn,
                        titulo,
                        editora,
                        ano,
                        genero.toString(),
                        cadastroLivro.getAutoresSelecionados()
                );

        livro.setFotoCapa(cadastroLivro.getFotoSelecionada());

        livro.setCpfDono(cadastroLivro.getCpfUsuario());


        try {

            if (alterando) {

                livroDAO.atualizar(livro);

                mostrarMensagem("Livro alterado com sucesso!");

            } else {

                if (livroDAO.buscarPorISBN(isbn) != null) {

                    mostrarMensagem("Já existe um livro cadastrado com esse ISBN.");

                    cadastroLivro.getTextField().requestFocus();

                    return false;
                }

                livroDAO.inserir(livro);

                mostrarMensagem("Livro cadastrado com sucesso!");
            }

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem("Erro ao salvar o livro:\n" + e.getMessage());

            return false;
        }
    }


    // =========================================================
    // REMOVER LIVRO E REGISTRAR DEVOLUÇÃO
    // =========================================================

    private void removerLivro(Livro livro) {

        if (!JanelaMensagem.confirmar(
                "Tem certeza que quer remover \"" + livro.getNome() + "\"?",
                "Remover livro")) {
            return;
        }

        try {

            livroDAO.excluir(livro.getIsbn());

            carregarMeusLivros();

            mostrarMensagem("Livro removido com sucesso!");

        } catch (SQLException e) {

            e.printStackTrace();

            // Livro que já foi emprestado não pode ser apagado
            if (e.getMessage() != null && e.getMessage().contains("foreign key")) {

                mostrarMensagem("Esse livro tem empréstimos registrados e não pode ser removido.");

            } else {

                mostrarMensagem("Erro ao remover o livro:\n" + e.getMessage());
            }
        }
    }


    // O trigger do banco deixa o livro disponível de novo
    private void registrarDevolucao(Livro livro) {

        if (!JanelaMensagem.confirmar(
                "Confirmar que \"" + livro.getNome() + "\" foi devolvido?",
                "Registrar devolução")) {
            return;
        }

        try {

            emprestimoDAO.registrarDevolucao(livro.getIsbn());

            carregarMeusLivros();

            mostrarMensagem(
                    "Devolução registrada! \"" + livro.getNome() + "\" está disponível de novo."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem("Erro ao registrar a devolução:\n" + e.getMessage());
        }
    }


    // Quem está com o livro (null se não encontrou)
    private String buscarComQuem(Livro livro) {

        try {

            return emprestimoDAO.descreverEmprestimoAtivo(livro.getIsbn());

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
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

        trocarPara(perfil);


        mostrarMensagem(
                "Cadastro atualizado com sucesso!"
        );
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        JanelaMensagem.mostrar(mensagem);
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