package model;

public class Autor {

    private int id_autor;
    private String nome;
    private String nacionalidade;

    public Autor() {
    }

    public Autor(
            int id_autor,
            String nome,
            String nacionalidade) {

        this.id_autor = id_autor;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
    }

    public int getId_autor() {
        return id_autor;
    }

    public void setId_autor(int id_autor) {
        this.id_autor = id_autor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    @Override
    public String toString() {
        return nome;
    }
}
