package model;

public class Livro {

	private String nome;
	private String editora;
	private int anoLancamento;
	private String autor;
	private String genero;
	public Livro(String nome, String editora, int anoLancamento, String autor, String genero) {
		super();
		this.nome = nome;
		this.editora = editora;
		this.anoLancamento = anoLancamento;
		this.autor = autor;
		this.genero = genero;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEditora() {
		return editora;
	}
	public void setEditora(String editora) {
		this.editora = editora;
	}
	public int getAnoLancamento() {
		return anoLancamento;
	}
	public void setAnoLancamento(int anoLancamento) {
		this.anoLancamento = anoLancamento;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	
	
	
	
	
	
	
	
	
	
}
