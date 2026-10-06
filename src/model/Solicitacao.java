package model;

import java.time.LocalDate;

// Pedido de empréstimo feito por um usuário para um livro de outro usuário
public class Solicitacao {

    private int idEmprestimo;
    private String isbn;
    private String tituloLivro;
    private String nomeSolicitante;
    private String emailSolicitante;
    private String telefoneSolicitante;
    private byte[] fotoSolicitante;
    private LocalDate dataPedido;

    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(int idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
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

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }
}
