package model;

public class Usuario {
	private String nome, senha, Loc, email, genero;

	public Usuario(String nome, String email, String senha, String Loc, String genero) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.Loc = Loc;
		this.genero = genero;
	}
	
	
	public String getNome() {
		return nome;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLoc() {
		return Loc;
	}

	public void setEndereco(String Loc) {
		this.Loc= Loc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
	
}

