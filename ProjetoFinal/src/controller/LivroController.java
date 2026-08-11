package controller;

import model.Livro;
import model.LivroTableModel;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.Cadastro;
import View.Generos;
import View.JanelaEditar; 

public class LivroController {
    
    private LivroTableModel livroModel;
    private Cadastro viewCadastro;
    
    /**
     * @wbp.parser.entryPoint
     */
    public LivroController(LivroTableModel modelo, Cadastro view) {
        this.livroModel = modelo;
        this.viewCadastro = view;
        
        this.viewCadastro.getBtnAdicionar().addActionListener(e -> eventoBotaoAdicionar());
    
    }
    


    public void eventoBotaoAdicionar() {
        try {
            String nome = this.viewCadastro.getTxtNome().getText();
            String editora = this.viewCadastro.getTxtEditora().getText();
            String autor = this.viewCadastro.getTxtAutor().getText();
            String genero = this.viewCadastro.getComboBox().getSelectedItem().toString();
            String anoTexto = this.viewCadastro.getTxtAno().getText().trim();
            
            if (anoTexto.isEmpty()) return; 
            
            int anoLancamento = Integer.parseInt(anoTexto);
            
            Livro l = new Livro(nome, editora, anoLancamento, autor, genero);
            livroModel.adicionarLivro(l);
            
            this.viewCadastro.getTxtNome().setText("");
            this.viewCadastro.getTxtEditora().setText("");
            this.viewCadastro.getTxtAno().setText("");
            this.viewCadastro.getTxtAutor().setText("");
            
        } catch (Exception e) {
            UIManager.put("OptionPane.background", new Color(175, 244, 198));
            UIManager.put("Panel.background", new Color(175, 244, 198));

            JOptionPane.showMessageDialog(viewCadastro, "Erro ao cadastrar");

            UIManager.put("OptionPane.background", null);
            UIManager.put("Panel.background", null);
        }
    }
}