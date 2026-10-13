package controller;

import java.awt.Color;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.TelaCriarConta;
import View.TelaLogin;

import dao.UsuarioDAO;

public class LoginController {

    private final UsuarioDAO usuarioDAO;
    private final TelaLogin telaLogin;
    private final TelaCriarConta criarConta;

    // Preenchidos quando o login dá certo
    private String emailLogado;
    private String cpfLogado;


    public LoginController(
            UsuarioDAO usuarioDAO,
            TelaLogin telaLogin,
            TelaCriarConta criarConta) {

        this.usuarioDAO = usuarioDAO;
        this.telaLogin = telaLogin;
        this.criarConta = criarConta;
    }


    // =========================================================
    // LOGIN
    // =========================================================

    // Retorna true se o e-mail e a senha estiverem certos
    public boolean validarLogin() {

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

            return false;
        }


        try {

            boolean loginValido =
                    usuarioDAO.login(
                            email,
                            senha
                    );


            if (loginValido) {

                emailLogado = email;

                cpfLogado =
                        usuarioDAO.buscarCpfPorEmail(email);

                return true;

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

        return false;
    }


    // =========================================================
    // CADASTRAR USUÁRIO
    // =========================================================

    // Retorna true se o usuário foi cadastrado
    public boolean cadastrarUsuario() {

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

            return false;
        }


        if (senha.length() > 20) {

            mostrarMensagem(
                    "A senha deve ter no máximo 20 caracteres."
            );

            return false;
        }


        if (!senha.equals(confirmarSenha)) {

            mostrarMensagem(
                    "As senhas não são iguais."
            );

            return false;
        }


        cpf = cpf.replaceAll("\\D", "");


        if (cpf.length() != 11) {

            mostrarMensagem(
                    "CPF deve possuir 11 números."
            );

            return false;
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

            return true;

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

            return false;
        }
    }


    // =========================================================
    // GETTERS
    // =========================================================

    public String getEmailLogado() {
        return emailLogado;
    }


    public String getCpfLogado() {
        return cpfLogado;
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
}
