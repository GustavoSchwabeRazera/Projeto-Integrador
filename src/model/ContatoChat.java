package model;

// Pessoa com quem o usuário logado pode conversar
public class ContatoChat {

    private String cpf;
    private String nome;
    private byte[] foto;
    private int naoLidas;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getNaoLidas() {
        return naoLidas;
    }

    public void setNaoLidas(int naoLidas) {
        this.naoLidas = naoLidas;
    }
}
