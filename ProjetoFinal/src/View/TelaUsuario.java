package View;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.UsuarioTableModel;

public class TelaUsuario extends JFrame {

    private JTable tabela;
    private JButton btnAlterar;
    private JButton btnRemover;
    private JButton btnVoltar;

    public TelaUsuario() {

        setTitle("Usuários Cadastrados");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tabela = new JTable();
        
        btnVoltar = new JButton("Voltar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnVoltar);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    public JTable getTabela() {
        return tabela;
    }

    public JButton getBtnAlterar() {
        return btnAlterar;
    }

    public JButton getBtnRemover() {
        return btnRemover;
    }
    public JButton getBtnVoltar() {
        return btnVoltar;
    }
}