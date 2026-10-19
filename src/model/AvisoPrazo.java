package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Empréstimo ativo do usuário que está perto de acabar
public class AvisoPrazo {

    private int idEmprestimo;
    private String isbn;
    private String tituloLivro;
    private String nomeDono;
    private LocalDate dataTermino;

    // Quantos dias faltam para devolver (0 = hoje)
    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataTermino);
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

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }
}
