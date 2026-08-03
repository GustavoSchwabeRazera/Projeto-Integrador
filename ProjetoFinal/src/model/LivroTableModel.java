package model;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class LivroTableModel extends AbstractTableModel{
	ArrayList<Livro> lista;
	
	String colunas[] = new String[] { "Nome do Livro", "Editora", "Ano do Lançamento", "Autor", "Genero"};
	
	public LivroTableModel() {
		this.lista = new ArrayList();
	}
	
	public LivroTableModel(ArrayList<Livro> lista) {
		this.lista = lista;
	}
	@Override
	public String getColumnName(int indice) {
		return colunas[indice]; 
		
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.lista.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.colunas.length;
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Livro livro = lista.get(rowIndex);
		if(columnIndex == 0) {
			return livro.getNome();
		} 
		if(columnIndex ==1) {
			return livro.getEditora();
		}
		if(columnIndex ==2) {
			return  String.valueOf(livro.getAnoLancamento());
		}
		
		if(columnIndex == 3) {
			return livro.getAutor();
		}
		
		if(columnIndex == 4) {
			return livro.getGenero();
		}
		
		return null;
	}

	public void remover(int linhaSelecionada) {
		lista.remove(linhaSelecionada);
		fireTableDataChanged();
		
	}

	public void adicionarLivro(Livro l) {
		lista.add(l);
		fireTableDataChanged();
		
	}
	
	
}
