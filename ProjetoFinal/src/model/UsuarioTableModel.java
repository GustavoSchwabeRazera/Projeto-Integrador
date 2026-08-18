package model;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class UsuarioTableModel extends AbstractTableModel{
	ArrayList<Usuario> lista;
	
	String colunas[] = new String[] { "Nome", "senha", "email", "localizaçao", "Genero"};
	
	public UsuarioTableModel() {
		this.lista = new ArrayList();
	}
	
	public UsuarioTableModel(ArrayList<Usuario> lista) {
		this.lista = lista;
	}
	/**
	 * Retorna o nome da Coluna consultando a String do vetor "colunas"
	 * no índice "indice"
	 */
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
		Usuario usuario = lista.get(rowIndex);
		if(columnIndex == 0) {
			return usuario.getNome();
		} 
		if(columnIndex ==1) {
			return usuario.getSenha();
		}
		if(columnIndex ==2) {
			return usuario.getEmail();
		}
		if(columnIndex ==3) {
			return usuario.getLoc();
		}
		
		if(columnIndex == 4) {
		    return usuario.getGenero();
		}
		
		return null;
	}
	


	public void adicionarPessoa(Usuario u) {
        lista.add(u);
        fireTableDataChanged();
    }

    public void remover(int linhaSelecionada) {
        lista.remove(linhaSelecionada);
        fireTableDataChanged();
    }

    
    public Usuario getUsuario(int linha) {
        return lista.get(linha);
    }
    
    public void atualizar(int index, Usuario u) {
        lista.set(index, u);
        fireTableDataChanged();
    }
}

