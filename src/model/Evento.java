package model;

import java.time.LocalDate;

// Lembrete que o usuário anota num dia do calendário
public class Evento {

    private int idEvento;
    private LocalDate data;
    private String descricao;

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Texto que aparece na lista do calendário
    @Override
    public String toString() {
        return descricao;
    }
}
