package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Empréstimo que ainda não terminou: livro com o leitor ou devolução esperando o dono confirmar
public class EmprestimoAtivo {

    private int idEmprestimo;
    private String isbn;
    private String tituloLivro;
    private byte[] fotoCapa;
    private String nomeDono;
    private String emailDono;
    private String telefoneDono;
    private String nomeLeitor;
    private byte[] fotoLeitor;
    private LocalDate dataEntrega;
    private LocalDate devolucaoSolicitada; // null enquanto o leitor não devolveu

    // Quantos dias faltam para devolver (negativo = atrasado)
    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataEntrega);
    }

    public boolean isDevolucaoSolicitada() {
        return devolucaoSolicitada != null;
    }

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

    public byte[] getFotoCapa() {
        return fotoCapa;
    }

    public void setFotoCapa(byte[] fotoCapa) {
        this.fotoCapa = fotoCapa;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getEmailDono() {
        return emailDono;
    }

    public void setEmailDono(String emailDono) {
        this.emailDono = emailDono;
    }

    public String getTelefoneDono() {
        return telefoneDono;
    }

    public void setTelefoneDono(String telefoneDono) {
        this.telefoneDono = telefoneDono;
    }

    public String getNomeLeitor() {
        return nomeLeitor;
    }

    public void setNomeLeitor(String nomeLeitor) {
        this.nomeLeitor = nomeLeitor;
    }

    public byte[] getFotoLeitor() {
        return fotoLeitor;
    }

    public void setFotoLeitor(byte[] fotoLeitor) {
        this.fotoLeitor = fotoLeitor;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public LocalDate getDevolucaoSolicitada() {
        return devolucaoSolicitada;
    }

    public void setDevolucaoSolicitada(LocalDate devolucaoSolicitada) {
        this.devolucaoSolicitada = devolucaoSolicitada;
    }
}
