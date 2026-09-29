package controller;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Color;
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
import dao.LivroDAO;
import dao.UsuarioDAO;
import model.Livro;
import model.LivroTableModel;

public class LivroController {

private final LivroTableModel livroModel;
private final UsuarioDAO usuarioDAO;
private final TelaLogin telaLogin;
private final tela_inicial telaInicial;
private final PesquisarLivro pesquisarLivro;
private final TelaSolicitacoes telaSolicitacoes;
private final TelaNotificacoes telaNotificacoes;
private final TelaMeusLivros telaMeusLivros;
private final Perfil perfil;
private final Cadastro_Livro cadastro_livro;
private final TelaCriarConta CriarConta;
private final Calendario calendario;
private final TelaAlterar alterarCadastro;
private final Historico historico;

private String emailUsuarioLogado;
private String cpfUsuarioLogado;

private LivroDAO livroDAO;

// Formato utilizado pelo campo txtAno
private static final DateTimeFormatter FORMATO_DATA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

/*
 * Construtor principal
 */
public LivroController(
        LivroDAO livroDAO,
        LivroTableModel modelo,
        Cadastro_Livro view) {

    this.livroModel = modelo;
    this.livroDAO = livroDAO;
    this.cadastro_livro = view;

    this.CriarConta = new TelaCriarConta();
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

    this(
        livroDAO,
        modelo,
        new Cadastro_Livro()
    );
}

private void configurarEventos() {

    // LOGIN
    telaLogin.getBotaoEntrar()
            .addActionListener(e -> validarLogin());

    telaLogin.getLblCadastro()
            .addActionListener(e -> abrirCriarConta());

    telaLogin.getTxtSenha()
            .addActionListener(e -> validarLogin());

    // CRIAR CONTA
    CriarConta.getBotaoCadastrar()
            .addActionListener(e -> cadastrarUsuario());

    CriarConta.getBotaoEntrar()
            .addActionListener(e -> iniciar());

    // HOME
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

    // PESQUISA
    pesquisarLivro.getBtnHome()
            .addActionListener(e -> abrirHome());

    pesquisarLivro.getBtnPesquisar()
            .addActionListener(e -> pesquisar());

    // MEUS LIVROS
    telaMeusLivros.getBtnHome()
            .addActionListener(e -> abrirHome());

    // SOLICITAÇÕES
    telaSolicitacoes.getBtnHome()
            .addActionListener(e -> abrirHome());

    telaSolicitacoes.getBtnAceitar()
            .addActionListener(e -> aceitarSolicitacao());

    telaSolicitacoes.getBtnExcluir()
            .addActionListener(e -> excluirSolicitacao());

    // CADASTRO DE LIVROS
    cadastro_livro.getBtnAdicionar()
            .addActionListener(e -> adicionarLivro());

    // CALENDÁRIO
    calendario.getBtnHome()
            .addActionListener(e -> abrirHome());

    // HISTÓRICO
    historico.getBtnHome()
            .addActionListener(e -> abrirHome());

    // PERFIL
    perfil.getBtnHome()
            .addActionListener(e -> abrirHome());
   perfil.getBtnSair().addActionListener(e -> logoff());

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
 // Alterar Cadastro //
    alterarCadastro.getBtnVoltar().addActionListener(e -> VoltarDoCadastro());
    alterarCadastro.getBotaoCadastrar().addActionListener(e -> atualizarCadastro());
}
	
	

public void VoltarDoCadastro() {
	esconderTodas();
	perfil.setVisible(true);
}

public void iniciar() {

    esconderTodas();

    telaLogin.setVisible(true);
}
public void logoff() {

    // Limpa os dados do usuário logado
    emailUsuarioLogado = null;
    cpfUsuarioLogado = null;

    // Volta as fotos para a imagem padrão
    telaInicial.atualizarFotoPerfil(null);
    perfil.carregarFoto(null);

    // Esconde todas as telas
    esconderTodas();

    // Limpa os campos da tela de login
    telaLogin.getTxtNome().setText("");
    telaLogin.getTxtSenha().setText("");

    // Volta para a tela de login
    telaLogin.setVisible(true);
}


private void abrirHome() {

    try {

        byte[] foto =
                usuarioDAO.buscarFoto(
                        cpfUsuarioLogado
                );

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

        perfil.setCpfUsuario(
                cpfUsuarioLogado
        );

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
                usuarioDAO.buscarFoto(
                        cpfUsuarioLogado
                );

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
    cadastro_livro.setVisible(false);
    CriarConta.setVisible(false);
    calendario.setVisible(false);
    alterarCadastro.setVisible(false);
    historico.setVisible(false);
}

private void cadastrarUsuario() {

    String nome =
            CriarConta.getTxtNome()
                    .getText()
                    .trim();

    String email =
            CriarConta.getTxtEmail()
                    .getText()
                    .trim();

    String telefone =
            CriarConta.getTxtTelefone()
                    .getText()
                    .trim();

    String cpf =
            CriarConta.getTxtCpf()
                    .getText()
                    .trim();

    String dataNascimento =
            CriarConta.getTxtDataNascimento()
                    .getText()
                    .trim();

    String senha =
            new String(
                    CriarConta.getTxtSenha()
                            .getPassword()
            );

    String confirmarSenha =
            new String(
                    CriarConta.getTxtConfirmarSenha()
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

/*
 * =========================================================
 * CADASTRAR LIVRO
 * =========================================================
 */
private void adicionarLivro() {

    try {

        String nome =
                cadastro_livro.getTxtNome()
                        .getText()
                        .trim();

        String editora =
                cadastro_livro.getTxtEditora()
                        .getText()
                        .trim();

        String genero =
                cadastro_livro.getComboBox()
                        .getSelectedItem()
                        .toString();

        String dataTexto =
                cadastro_livro.getTxtAno()
                        .getText()
                        .trim();

        // Verifica os campos
        if (nome.isEmpty()
                || editora.isEmpty()
                || genero.isEmpty()
                || dataTexto.isEmpty()
                || dataTexto.contains("_")) {

            mostrarMensagem(
                    "Preencha todos os campos."
            );

            return;
        }

        /*
         * Converte a data digitada:
         *
         * 22/09/2026
         *
         * para:
         *
         * LocalDate
         */
        LocalDate dataLancamento =
                LocalDate.parse(
                        dataTexto,
                        FORMATO_DATA
                );

        /*
         * Como o banco utiliza ano_lancamento
         * como número inteiro, salvamos somente
         * o ano.
         *
         * Exemplo:
         *
         * 22/09/2026 -> 2026
         */
        int anoLancamento =
                dataLancamento.getYear();

        Livro livro =
                new Livro(
                        nome,
                        editora,
                        dataTexto,
                        anoLancamento,
                        genero, null
                        );

        /*
         * Adiciona no modelo da tabela.
         */
        livroModel.adicionarLivro(livro);

        /*
         * Também salva no banco.
         */
        if (livroDAO != null) {

            livroDAO.inserir(livro);
        }

        limparCadastro();

        mostrarMensagem(
                "Livro cadastrado com sucesso."
        );

    } catch (DateTimeParseException e) {

        mostrarMensagem(
                "Digite uma data válida no formato DD/MM/AAAA."
        );

    } catch (SQLException e) {

        e.printStackTrace();

        mostrarMensagem(
                "Erro ao salvar o livro no banco de dados."
        );

    } catch (Exception e) {

        e.printStackTrace();

        mostrarMensagem(
                "Erro ao cadastrar o livro."
        );
    }
}

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
                    usuarioDAO.buscarCpfPorEmail(
                            email
                    );

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

private void limparCadastro() {

    cadastro_livro.getTxtNome()
            .setText("");

    cadastro_livro.getTxtEditora()
            .setText("");

    cadastro_livro.getTxtAno()
            .setText("");
}

private void pesquisar() {

    String texto =
            pesquisarLivro.getTextoPesquisa();

    if (texto.isEmpty()) {

        mostrarMensagem(
                "Digite o nome do livro para pesquisar."
        );

        return;
    }

    try {

        List<Livro> listaLivros = livroDAO.listar();
               /** livroDAO.buscarLivrosPorNome(
                        texto
                );**/

        if (listaLivros == null
                || listaLivros.isEmpty()) {

            mostrarMensagem(
                    "Nenhum livro encontrado para: "
                            + texto
            );

        } else {

            mostrarMensagem(
                    listaLivros.size()
                            + " livro(s) encontrado(s) para: "
                            + texto
            );
        }

    } catch (SQLException e) {

        e.printStackTrace();

        mostrarMensagem(
                "Erro ao pesquisar livros."
        );
    }
}

private void aceitarSolicitacao() {

    mostrarMensagem(
            "Solicitação aceita."
    );
}

private void excluirSolicitacao() {

    mostrarMensagem(
            "Solicitação excluída."
    );
}

// =========================================================
// UTILITÁRIO
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
private void atualizarCadastro() {

    String cpfAtual = cpfUsuarioLogado;

    String cpfNovo = alterarCadastro.getTxtCpf().getText().trim();
    String nome = alterarCadastro.getTxtNome().getText().trim();
    String telefone = alterarCadastro.getTxtTelefone().getText().trim();
    String email = alterarCadastro.getTxtEmail().getText().trim();
    String dataNascimento =
            alterarCadastro.getTxtDataNascimento().getText().trim();

    String senha =
            new String(
                    alterarCadastro.getTxtSenha().getPassword()
            );

    String confirmarSenha =
            new String(
                    alterarCadastro.getTxtConfirmarSenha().getPassword()
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
        // BUSCA NOVAMENTE NO BANCO
        // ==============================

        String[] dadosAtualizados =
                usuarioDAO.buscarUsuarioPorEmail(
                        emailUsuarioLogado
                );

        if (dadosAtualizados != null) {

            perfil.atualizarDados(
                    dadosAtualizados[0], // nome
                    dadosAtualizados[1], // email
                    dadosAtualizados[2], // telefone
                    dadosAtualizados[3]  // data nascimento
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
    return cadastro_livro;
}
}