package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import View.Cadastro_Autor;
import View.Cadastro_Livro;
import View.JanelaMensagem;
import View.Lista_Autores;
import dao.AutorDAO;
import dao.ConnectionFactory;
import model.Autor;

public class AutorController {

    private final Cadastro_Livro cadastroLivro;

    // Lista de autores aberta no momento
    private Lista_Autores listaAutores;


    public AutorController(Cadastro_Livro cadastroLivro) {

        this.cadastroLivro = cadastroLivro;
    }


    // Sempre pega uma conexão aberta, porque outras partes do programa
    // podem fechar a antiga
    private AutorDAO autorDAO() throws SQLException {

        return new AutorDAO(ConnectionFactory.getConnection());
    }


    // =========================================================
    // LISTA DE AUTORES
    // =========================================================

    public void abrirLista() {

        listaAutores = new Lista_Autores();

        listaAutores.getBotaoNovoAutor()
                .addActionListener(e -> abrirFormulario(null));

        listaAutores.getBotaoEditar()
                .addActionListener(e -> editarSelecionado());

        listaAutores.getBotaoExcluir()
                .addActionListener(e -> excluirSelecionado());

        listaAutores.getBotaoSelecionar()
                .addActionListener(e -> selecionarAutores());

        carregarAutores();

        listaAutores.setVisible(true);
    }


    private void carregarAutores() {

        try {

            listaAutores.carregarAutores(autorDAO().listarAutores());

        } catch (SQLException e) {

            e.printStackTrace();

            JanelaMensagem.mostrar(
                    "Erro ao carregar os autores:\n" + e.getMessage()
            );
        }
    }


    private void selecionarAutores() {

        List<Autor> selecionados = listaAutores.getAutoresSelecionados();

        if (selecionados.isEmpty()) {

            JanelaMensagem.mostrar("Selecione pelo menos um autor.");

            return;
        }

        cadastroLivro.receberAutoresSelecionados(new ArrayList<>(selecionados));

        listaAutores.dispose();
    }


    // =========================================================
    // CADASTRAR E ALTERAR
    // =========================================================

    private void editarSelecionado() {

        Autor autor = pegarUmSelecionado("alterar");

        if (autor != null) {
            abrirFormulario(autor);
        }
    }


    // Autor null = cadastro de um novo
    private void abrirFormulario(Autor autor) {

        Cadastro_Autor formulario = new Cadastro_Autor();

        if (autor != null) {
            formulario.prepararEdicao(autor);
        }

        formulario.getBtnAdicionar()
                .addActionListener(e -> salvar(formulario, autor));

        formulario.setVisible(true);
    }


    private void salvar(Cadastro_Autor formulario, Autor autorEditado) {

        String nome = formulario.getTxtNome().getText().trim();

        Object nacionalidade = formulario.getComboBox().getSelectedItem();

        if (nome.isEmpty()) {

            JanelaMensagem.mostrar("Digite o nome do autor.");

            formulario.getTxtNome().requestFocus();

            return;
        }

        if (nome.length() > 50) {

            JanelaMensagem.mostrar("O nome do autor deve ter no máximo 50 caracteres.");

            formulario.getTxtNome().requestFocus();

            return;
        }

        if (nacionalidade == null) {

            JanelaMensagem.mostrar("Selecione a nacionalidade.");

            return;
        }

        try {

            if (autorEditado == null) {

                autorDAO().cadastrar(new Autor(0, nome, nacionalidade.toString()));

                JanelaMensagem.mostrar("Autor cadastrado com sucesso!");

            } else {

                autorEditado.setNome(nome);
                autorEditado.setNacionalidade(nacionalidade.toString());

                autorDAO().atualizar(autorEditado);

                JanelaMensagem.mostrar("Autor alterado com sucesso!");
            }

            carregarAutores();

            formulario.dispose();

        } catch (SQLException e) {

            e.printStackTrace();

            JanelaMensagem.mostrar(
                    "Erro ao salvar o autor:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // EXCLUIR
    // =========================================================

    private void excluirSelecionado() {

        Autor autor = pegarUmSelecionado("excluir");

        if (autor == null) {
            return;
        }

        try {

            if (autorDAO().temLivros(autor.getId_autor())) {

                JanelaMensagem.mostrar(
                        "Esse autor tem livros cadastrados e não pode ser excluído."
                );

                return;
            }

            if (!JanelaMensagem.confirmar(
                    "Tem certeza que quer excluir \"" + autor.getNome() + "\"?",
                    "Excluir autor")) {
                return;
            }

            autorDAO().excluir(autor.getId_autor());

            // Tira o autor do livro que está sendo cadastrado
            List<Autor> doLivro = new ArrayList<>(cadastroLivro.getAutoresSelecionados());
            doLivro.removeIf(a -> a.getId_autor() == autor.getId_autor());
            cadastroLivro.receberAutoresSelecionados(doLivro);

            carregarAutores();

            JanelaMensagem.mostrar("Autor excluído com sucesso!");

        } catch (SQLException e) {

            e.printStackTrace();

            JanelaMensagem.mostrar(
                    "Erro ao excluir o autor:\n" + e.getMessage()
            );
        }
    }


    // Alterar e excluir funcionam com um autor de cada vez
    private Autor pegarUmSelecionado(String acao) {

        List<Autor> selecionados = listaAutores.getAutoresSelecionados();

        if (selecionados.size() != 1) {

            JanelaMensagem.mostrar("Selecione um autor na lista para " + acao + ".");

            return null;
        }

        return selecionados.get(0);
    }
}
