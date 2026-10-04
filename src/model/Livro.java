package model;

import java.util.ArrayList;
import java.util.List;

public class Livro {

    private String isbn;
    private String nome;
    private String editora;
    private int anoLancamento;
    private String genero;
    private List<Autor> autores;
    private byte[] fotoCapa;   // foto da capa do livro
    private String cpfDono;    // CPF de quem cadastrou

    public Livro() {
        autores = new ArrayList<Autor>();
    }

    public Livro(
            String isbn,
            String nome,
            String editora,
            int anoLancamento,
            String genero,
            List<Autor> autoresSelecionados) {

        this.isbn = isbn;
        this.nome = nome;
        this.editora = editora;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
        this.autores = autoresSelecionados;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public byte[] getFotoCapa() {
        return fotoCapa;
    }

    public void setFotoCapa(byte[] fotoCapa) {
        this.fotoCapa = fotoCapa;
    }

    public String getCpfDono() {
        return cpfDono;
    }

    public void setCpfDono(String cpfDono) {
        this.cpfDono = cpfDono;
    }

    @Override
    public String toString() {
        return nome;
    }
}