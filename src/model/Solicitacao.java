package model;

import java.time.LocalDate;

// Pedido de empréstimo feito por um usuário para um livro de outro usuário
public class Solicitacao {

    // Depois desse tempo sem resposta o pedido vence (em dias)
    public static final int DIAS_VALIDADE = 14;

    private int idSolicitacao;
    private String isbn;
    private String tituloLivro;
    private String nomeSolicitante;
    private String emailSolicitante;
    private String telefoneSolicitante;
    private byte[] fotoSolicitante;
    private String nomeDono;
    private byte[] fotoDono;
    private LocalDate dataPedido;

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
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

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
    }

    public String getEmailSolicitante() {
        return emailSolicitante;
    }

    public void setEmailSolicitante(String emailSolicitante) {
        this.emailSolicitante = emailSolicitante;
    }

    public String getTelefoneSolicitante() {
        return telefoneSolicitante;
    }

    public void setTelefoneSolicitante(String telefoneSolicitante) {
        this.telefoneSolicitante = telefoneSolicitante;
    }

    public byte[] getFotoSolicitante() {
        return fotoSolicitante;
    }

    public void setFotoSolicitante(byte[] fotoSolicitante) {
        this.fotoSolicitante = fotoSolicitante;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public byte[] getFotoDono() {
        return fotoDono;
    }

    public void setFotoDono(byte[] fotoDono) {
        this.fotoDono = fotoDono;
    }

    // Último dia em que o pedido ainda pode ser respondido
    public LocalDate getDataValidade() {
        return dataPedido.plusDays(DIAS_VALIDADE);
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }
}
