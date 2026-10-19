package controller;

import java.awt.Color;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.Perfil;
import View.TelaAlterar;
import View.tela_inicial;

import dao.UsuarioDAO;

public class PerfilController {

    private final UsuarioDAO usuarioDAO;
    private final Perfil perfil;
    private final TelaAlterar alterarCadastro;
    private final tela_inicial telaInicial;

    // Preenchidos quando o cadastro é atualizado
    private String cpfAtualizado;
    private String emailAtualizado;


    public PerfilController(
            UsuarioDAO usuarioDAO,
            Perfil perfil,
            TelaAlterar alterarCadastro,
            tela_inicial telaInicial) {

        this.usuarioDAO = usuarioDAO;
        this.perfil = perfil;
        this.alterarCadastro = alterarCadastro;
        this.telaInicial = telaInicial;
    }


    // =========================================================
    // PERFIL
    // =========================================================

    public void carregarPerfil(String email, String cpf) {

        try {

            perfil.setCpfUsuario(cpf);

            String[] dados =
                    usuarioDAO.buscarUsuarioPorEmail(
                            email
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
                    usuarioDAO.buscarFoto(cpf);

            if (foto != null && foto.length > 0) {

                perfil.carregarFoto(foto);
            }

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar os dados do perfil."
            );
        }
    }


    public void alterarFoto(String cpf) {

        try {

            perfil.selecionarEAtualizarFoto();

            byte[] foto = perfil.getFotoSelecionada();

            if (foto != null && foto.length > 0) {

                usuarioDAO.atualizarFoto(
                        cpf,
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
    }


    // =========================================================
    // ALTERAR CADASTRO
    // =========================================================

    // Preenche a tela de alteração. Retorna true se achou os dados.
    public boolean carregarDadosAlteracao(String cpf) {

        try {

            String[] dados =
                    usuarioDAO.buscarDadosParaAlteracao(
                            cpf
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

                return true;

            } else {

                mostrarMensagem(
                        "Não foi possível encontrar os dados do usuário."
                );

                return false;
            }

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar os dados do cadastro."
            );

            return false;
        }
    }


    // Salva as alterações. Retorna true se deu certo.
    public boolean atualizarCadastro(String cpfAtual) {

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

            return false;
        }


        // ==============================
        // VALIDA SENHA
        // ==============================

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


        // ==============================
        // VALIDA CPF
        // ==============================

        String cpfLimpo =
                cpfNovo.replaceAll("\\D", "");


        if (cpfLimpo.length() != 11) {

            mostrarMensagem(
                    "CPF deve possuir 11 números."
            );

            return false;
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


            cpfAtualizado = cpfLimpo;
            emailAtualizado = email;


            // ==============================
            // BUSCA DADOS ATUALIZADOS
            // ==============================

            String[] dadosAtualizados =
                    usuarioDAO.buscarUsuarioPorEmail(
                            email
                    );


            if (dadosAtualizados != null) {

                perfil.atualizarDados(
                        dadosAtualizados[0],
                        dadosAtualizados[1],
                        dadosAtualizados[2],
                        dadosAtualizados[3]
                );
            }

            return true;


        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao atualizar cadastro:\n"
                            + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // EXCLUIR CONTA
    // =========================================================

    // Pede confirmação e exclui a conta. Retorna true se excluiu.
    public boolean excluirConta(String cpf) {

        UIManager.put(
                "OptionPane.background",
                new Color(175, 244, 198)
        );

        UIManager.put(
                "Panel.background",
                new Color(175, 244, 198)
        );


        String[] opcoes = { "Sim", "Não" };

        int opcao = JOptionPane.showOptionDialog(
                perfil,
                "Tem certeza que deseja excluir sua conta?\n"
                        + "Seus livros e empréstimos também serão apagados.",
                "Excluir conta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
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


        if (opcao != 0) {
            return false;
        }

        try {

            usuarioDAO.excluirConta(cpf);

            mostrarMensagem(
                    "Conta excluída com sucesso."
            );

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao excluir conta:\n"
                            + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // GETTERS
    // =========================================================

    public String getCpfAtualizado() {
        return cpfAtualizado;
    }


    public String getEmailAtualizado() {
        return emailAtualizado;
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
