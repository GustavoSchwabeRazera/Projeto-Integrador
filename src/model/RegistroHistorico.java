package model;

import java.time.LocalDateTime;

// Um empréstimo que aparece na tela de Histórico
public class RegistroHistorico {

    // Tipos de ação guardados na coluna "tipo"
    public static final String EMPRESTIMO_CONCEDIDO = "EMPRESTIMO_CONCEDIDO"; // o usuário emprestou
    public static final String EMPRESTIMO_RECEBIDO = "EMPRESTIMO_RECEBIDO";   // o usuário pegou emprestado

    private int id;
    private String tipo;
    private String isbn;
    private String tituloLivro;
    private String detalhe; // nome da outra pessoa do empréstimo
    private LocalDateTime dataHora;

    // Texto principal do cartão
    public String getDescricao() {

        switch (tipo) {
            case EMPRESTIMO_CONCEDIDO:
                return "Você emprestou o livro \"" + tituloLivro + "\" para " + detalhe;
            case EMPRESTIMO_RECEBIDO:
                return "Você pegou emprestado o livro \"" + tituloLivro + "\" que pertence a " + detalhe;
            default:
                return tituloLivro;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
