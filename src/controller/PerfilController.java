package controller;

import java.sql.SQLException;


import View.JanelaMensagem;
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


    public boolean carregarDadosAlteracao(String cpf) {

        try {

            String[] dados =
                    usuarioDAO.buscarDadosParaAlteracao(
                            cpf
                    );

            if (dados != null) {

                
                alterarCadastro.getTxtCpf()
                        .setText(dados[0]);

                
                alterarCadastro.getTxtNome()
                        .setText(dados[1]);

               
                alterarCadastro.getTxtEmail()
                        .setText(dados[2]);

                
                alterarCadastro.getTxtTelefone()
                        .setText(dados[3]);

            
                alterarCadastro.getTxtDataNascimento()
                        .setText(dados[4]);

               
                alterarCadastro.getTxtSenha()
                        .setText(dados[5]);

                
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


        String erro =
                Validador.validarUsuario(nome, email, telefone, dataNascimento);

        if (erro != null) {

            mostrarMensagem(erro);

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


        String cpfLimpo =
                cpfNovo.replaceAll("\\D", "");


        if (cpfLimpo.length() != 11) {

            mostrarMensagem(
                    "CPF deve possuir 11 números."
            );

            return false;
        }


        try {

            String donoDoEmail =
                    usuarioDAO.buscarCpfPorEmail(email);

            if (donoDoEmail != null && !donoDoEmail.equals(cpfAtual)) {

                mostrarMensagem(
                        "Este e-mail já está cadastrado."
                );

                return false;
            }



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


    public boolean excluirConta(String cpf) {

        boolean confirmou = JanelaMensagem.confirmar(
                "Tem certeza que deseja excluir sua conta?\n"
                        + "Seus livros e empréstimos também serão apagados.",
                "Excluir conta"
        );

        if (!confirmou) {
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


  

    public String getCpfAtualizado() {
        return cpfAtualizado;
    }


    public String getEmailAtualizado() {
        return emailAtualizado;
    }

    private void mostrarMensagem(String mensagem) {

        JanelaMensagem.mostrar(mensagem);
    }
}
