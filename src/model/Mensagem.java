package model;

import java.time.LocalDateTime;

public class Mensagem {

    private int idMensagem;
    private String cpfRemetente;
    private String cpfDestinatario;
    private String texto;
    private LocalDateTime dataHora;
    private boolean lida;

    public int getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(int idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getCpfRemetente() {
        return cpfRemetente;
    }

    public void setCpfRemetente(String cpfRemetente) {
        this.cpfRemetente = cpfRemetente;
    }

    public String getCpfDestinatario() {
        return cpfDestinatario;
    }

    public void setCpfDestinatario(String cpfDestinatario) {
        this.cpfDestinatario = cpfDestinatario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }
}
