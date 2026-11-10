package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AvisoPrazo {

    private int idEmprestimo;
    private String isbn;
    private String tituloLivro;
    private String nomeDono;
    private byte[] fotoDono;
    private LocalDate dataTermino;

    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataTermino);
    }

    public boolean isAtrasado() {
        return getDiasRestantes() < 0;
    }

    public String getLembrete() {

        long dias = getDiasRestantes();

        if (dias < 0) {
            return "Você está atrasado para devolver \"" + tituloLivro + "\"";
        }
        if (dias == 0) {
            return "Devolver \"" + tituloLivro + "\" hoje";
        }
        if (dias == 1) {
            return "Devolver \"" + tituloLivro + "\" em 1 dia";
        }
        return "Devolver \"" + tituloLivro + "\" em " + dias + " dias";
    }

    public byte[] getFotoDono() {
        return fotoDono;
    }

    public void setFotoDono(byte[] fotoDono) {
        this.fotoDono = fotoDono;
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
