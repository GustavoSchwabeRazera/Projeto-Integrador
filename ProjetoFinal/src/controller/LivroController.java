package controller;

import model.Livro;
import model.LivroTableModel;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.Cadastro;
import View.Generos;
import View.JanelaTabela; 
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
    
    private void eventoBotaoVisualizar() {
        JanelaTabela janelaTabela = new JanelaTabela();
        janelaTabela.getTable().setModel(this.livroModel);
        
        
        janelaTabela.getBtnRemover().addActionListener(e -> {
            int linhaSelecionada = janelaTabela.getTable().getSelectedRow();
            if (linhaSelecionada != -1) {
                livroModel.remover(linhaSelecionada);
            }
        });

        
        janelaTabela.getBtnEditar().addActionListener(e -> {
            int linhaSelecionada = janelaTabela.getTable().getSelectedRow();
            
            
            if (linhaSelecionada != -1) {
                
                
                JanelaEditar janelaEditar = new JanelaEditar();
                
                
                janelaEditar.getTxtNome().setText(janelaTabela.getTable().getValueAt(linhaSelecionada, 0).toString());
                janelaEditar.getTxtEditora().setText(janelaTabela.getTable().getValueAt(linhaSelecionada, 1).toString());
                janelaEditar.getTxtAno().setText(janelaTabela.getTable().getValueAt(linhaSelecionada, 2).toString());
                janelaEditar.getTxtAutor().setText(janelaTabela.getTable().getValueAt(linhaSelecionada, 3).toString());
                janelaEditar.getComboBox().setSelectedItem(
                	    Generos.valueOf(janelaTabela.getTable().getValueAt(linhaSelecionada, 4).toString())
                	);
                
                janelaEditar.getBtnSalvar().addActionListener(ev -> {
                    try {
                        
                        String novoNome = janelaEditar.getTxtNome().getText();
                        String novaEditora = janelaEditar.getTxtEditora().getText();
                        int novoAno = Integer.parseInt(janelaEditar.getTxtAno().getText());
                        String novoAutor = janelaEditar.getTxtAutor().getText();
                        String novoGenero = janelaEditar.getComboBox().getSelectedItem().toString();

                        
                        Livro livroAtualizado = new Livro(novoNome, novaEditora, novoAno, novoAutor, novoGenero);
                        
                        
                        livroModel.remover(linhaSelecionada);
                        livroModel.adicionarLivro(livroAtualizado);
                        
                        
                        janelaEditar.dispose();
                        
                    } catch (Exception error) {
                        System.out.println("Erro ao atualizar dados: Verifique o campo Ano.");
                    }
                });

                janelaEditar.setVisible(true);
            }
        });
        
        janelaTabela.setVisible(true);
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