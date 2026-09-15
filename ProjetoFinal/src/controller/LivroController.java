package controller;

import java.awt.Color;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.Cadastro_Livro;
import View.Perfil;
import View.PesquisarLivro;
import View.TelaAlterarCadastro;
import View.TelaCriarConta;
import View.TelaLogin;
import View.TelaMeusLivros;
import View.TelaSolicitacoes;
import View.tela_inicial;
import dao.LivroDAO;
import View.Calendario;
import model.Livro;
import model.LivroTableModel;
import dao.UsuarioDAO;
import View.Historico;

public class LivroController {

    private final LivroTableModel livroModel;

    private final UsuarioDAO usuarioDAO;
    private final TelaLogin telaLogin;
    private final tela_inicial telaInicial;
    private final PesquisarLivro pesquisarLivro;
    private final TelaSolicitacoes telaSolicitacoes;
    private final TelaMeusLivros telaMeusLivros;
    private final Perfil perfil;
    private final Cadastro_Livro cadastro_livro;
    private final TelaCriarConta CriarConta;
    private final Calendario calendario;
    private final TelaAlterarCadastro alterarCadastro;
    private final Historico historico;

	private LivroDAO livroDAO;

    /*
     * Construtor principal: mantém compatibilidade com o seu Main atual.
     */
    public LivroController(LivroDAO livroDAO, LivroTableModel modelo, Cadastro_Livro view) {
        this.livroModel = modelo;
        this.livroDAO = livroDAO;
        this.cadastro_livro = view;
        this.CriarConta = new TelaCriarConta();
        this.telaLogin = new TelaLogin();
        this.telaInicial = new tela_inicial();
        this.pesquisarLivro = new PesquisarLivro();
        this.telaSolicitacoes = new TelaSolicitacoes();
        this.telaMeusLivros = new TelaMeusLivros();
        this.perfil = new Perfil();
        this.calendario = new Calendario();
        this.alterarCadastro = new TelaAlterarCadastro();
        this.usuarioDAO = new UsuarioDAO();
        this.historico = new Historico();
        
        

        configurarEventos();
    }

    /*
     * Construtor alternativo caso queira iniciar sem criar Cadastro no Main.
     */
    public LivroController(LivroDAO livroDAO, LivroTableModel modelo) {
        this(livroDAO, modelo, new Cadastro_Livro());
    }

    private void configurarEventos() {

        // =========================
        // LOGIN
        // =========================
    
        telaLogin.getBotaoEntrar().addActionListener(e -> validarLogin());
        telaLogin.getLblCadastro().addActionListener(e -> abrirCriarConta());
        
        //==========================
        // Criar Conta
        //==========================
        CriarConta.getBotaoCadastrar().addActionListener(e -> cadastrarUsuario());
        CriarConta.getBotaoEntrar().addActionListener(e -> iniciar());

        // =========================
        // HOME
        // =========================
        telaInicial.getBtnPesquisar().addActionListener(e -> abrirPesquisa());
        telaInicial.getBtnMeusLivros().addActionListener(e -> abrirMeusLivros());
        telaInicial.getBtnSolicitacoes().addActionListener(e -> abrirSolicitacoes());
        telaInicial.getBtnPerfil().addActionListener(e -> abrirPerfil());
        telaInicial.getBtnSair().addActionListener(e -> iniciar());
        telaInicial.getBtnCalendario().addActionListener(e -> abrirCalendario());
        telaInicial.getBtnHistorico().addActionListener(e -> abrirHistorico());

        // =========================
        // PESQUISA
        // =========================
        pesquisarLivro.getBtnHome().addActionListener(e -> abrirHome());
        pesquisarLivro.getBtnPerfil().addActionListener(e -> abrirPerfil());
        pesquisarLivro.getBtnPesquisar().addActionListener(e -> pesquisar());

        // =========================
        // MEUS LIVROS
        // =========================
        telaMeusLivros.getBtnHome().addActionListener(e -> abrirHome());
        telaMeusLivros.getBtnPerfil().addActionListener(e -> abrirPerfil());

        // =========================
        // SOLICITAÇÕES
        // =========================
        telaSolicitacoes.getBtnHome().addActionListener(e -> abrirHome());
        telaSolicitacoes.getBtnPerfil().addActionListener(e -> abrirPerfil());
        telaSolicitacoes.getBtnAceitar().addActionListener(e -> aceitarSolicitacao());
        telaSolicitacoes.getBtnExcluir().addActionListener(e -> excluirSolicitacao());

        // =========================
        // PERFIL
        // =========================
        perfil.getBtnHome().addActionListener(e -> abrirHome());
        perfil.getBtnAlterarCadastro().addActionListener(e -> abrirAlterarCadastro());
        // =========================
        // CADASTRO LIVROS
        // =========================
        cadastro_livro.getBtnAdicionar().addActionListener(e -> adicionarLivro());
        // =========================
        // Calendario
        // =========================
        calendario.getBtnHome().addActionListener(e -> abrirHome());
        // =========================
        // HISTORICO
        // =========================
      historico.getBtnHome().addActionListener(e -> abrirHome());
    
        
    }

    // =========================================================
    // INÍCIO
    // =========================================================

    public void iniciar() {
        esconderTodas();
        telaLogin.setVisible(true);
    }

    // =========================================================
    // NAVEGAÇÃO
    // =========================================================

    private void abrirHome() {
        esconderTodas();
        telaInicial.setVisible(true);
    }

    private void abrirPesquisa() {
        esconderTodas();
        pesquisarLivro.setVisible(true);
    }

    private void abrirMeusLivros() {
        esconderTodas();
        telaMeusLivros.setVisible(true);
    }

    private void abrirSolicitacoes() {
        esconderTodas();
        telaSolicitacoes.setVisible(true);
    }

    private void abrirPerfil() {
        esconderTodas();
        perfil.setVisible(true);
    }

    private void abrirCadastroLivro() {
        esconderTodas();
        cadastro_livro.setVisible(true);
    }
    private void abrirCriarConta() {
    	esconderTodas();
    	CriarConta.setVisible(true);
    }
    private void abrirCalendario() {
    	esconderTodas();
    	calendario.setVisible(true);
    }
    
    private void abrirAlterarCadastro() {
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
        telaMeusLivros.setVisible(false);
        perfil.setVisible(false);
        cadastro_livro.setVisible(false);
        CriarConta.setVisible(false);
        calendario.setVisible(false);
        alterarCadastro.setVisible(false);
    }

    // =========================================================
    // CADASTRO -> MODEL
    // =========================================================

    private void cadastrarUsuario() {

        String nome = CriarConta.getTxtNome().getText().trim();
        String email = CriarConta.getTxtEmail().getText().trim();
        String telefone = CriarConta.getTxtTelefone().getText().trim();
        String cpf = CriarConta.getTxtCpf().getText().trim();
        String dataNascimento = CriarConta.getTxtDataNascimento().getText().trim();

        String senha = new String(CriarConta.getTxtSenha().getPassword());
        String confirmarSenha = new String(CriarConta.getTxtConfirmarSenha().getPassword());

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()
                || cpf.isEmpty() || dataNascimento.contains("_")
                || senha.isEmpty() || confirmarSenha.isEmpty()) {

            mostrarMensagem("Preencha todos os campos.");
            return;
        }

        if (senha.length() > 20) {
            mostrarMensagem("A senha deve ter no máximo 20 caracteres.");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            mostrarMensagem("As senhas não são iguais.");
            return;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            mostrarMensagem("CPF deve possuir 11 números.");
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

            // Só chega aqui se o cadastro deu certo
            mostrarMensagem("Cadastro realizado com sucesso!");

            iniciar();

        } catch (SQLException e) {

            e.printStackTrace();

            if (e.getMessage().contains("Duplicate")) {
                mostrarMensagem("Este CPF já está cadastrado.");
            } else {
                mostrarMensagem("Erro ao cadastrar usuário.");
            }

            // ❌ NÃO coloque iniciar() aqui
        }
    }
    
    private void adicionarLivro() {
        try {
            String nome = cadastro_livro.getTxtNome().getText().trim();
            String editora = cadastro_livro.getTxtEditora().getText().trim();
            String genero = cadastro_livro.getComboBox().getSelectedItem().toString();
            String anoTexto = cadastro_livro.getTxtAno().getText().trim();

            if (nome.isEmpty() || editora.isEmpty() || autor.isEmpty()
                    || genero.isEmpty() || anoTexto.isEmpty()) {

                mostrarMensagem("Preencha todos os campos.");
                return;
            }

            int anoLancamento = Integer.parseInt(anoTexto);

            Livro livro = new Livro(
                    nome,
                    editora,
                    anoLancamento,
                    autor,
                    genero
            );
            
         //   livroDAO.inserir(livro);

            livroModel.adicionarLivro(livro);

            limparCadastro();

            mostrarMensagem("Livro cadastrado com sucesso.");

        } catch (NumberFormatException e) {
            mostrarMensagem("O ano deve ser um número.");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensagem("Erro ao cadastrar o livro.");
        }
    }

    
    private void validarLogin() {

        String email = telaLogin.getTxtNome().getText().trim();

        String senha = new String(
            telaLogin.getTxtSenha().getPassword()
        );

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarMensagem("Preencha o e-mail e a senha.");
            return;
        }

        try {

            boolean loginValido = usuarioDAO.login(email, senha);

            if (loginValido) {

                telaLogin.dispose();
                abrirHome();

            } else {

                mostrarMensagem("E-mail ou senha incorretos.");
            }

        } catch (SQLException e) {

            e.printStackTrace();
            mostrarMensagem("Erro ao acessar o banco de dados.");
        }
    }
    
    private void limparCadastro() {
        cadastro_livro.getTxtNome().setText("");
        cadastro_livro.getTxtEditora().setText("");
        cadastro_livro.getTxtAno().setText("");
    }

    // =========================================================
    // PESQUISA
    // =========================================================

    private void pesquisar() {
        String texto = pesquisarLivro.getTextoPesquisa();

        if (texto.isEmpty()) {
            mostrarMensagem("Digite o nome do livro para pesquisar.");
            return;
        }

        
      

       try {
		List<Livro> listaLivros = livroDAO.buscarLivrosPorNome(texto);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

        /*
         * A busca visual depende do método de pesquisa disponível no seu
         * LivroTableModel. O Controller já recebe o texto corretamente.
         */
        mostrarMensagem("Pesquisa: " + texto);
    }

    // =========================================================
    // SOLICITAÇÕES
    // =========================================================

    private void aceitarSolicitacao() {
        mostrarMensagem("Solicitação aceita.");
    }

    private void excluirSolicitacao() {
        mostrarMensagem("Solicitação excluída.");
    }

    // =========================================================
    // UTILITÁRIO
    // =========================================================

    private void mostrarMensagem(String mensagem) {
        UIManager.put("OptionPane.background", new Color(175, 244, 198));
        UIManager.put("Panel.background", new Color(175, 244, 198));

        JOptionPane.showMessageDialog(null, mensagem);

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
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

    public TelaMeusLivros getTelaMeusLivros() {
        return telaMeusLivros;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public Cadastro_Livro getCadastro() {
        return cadastro_livro;
    }
}
