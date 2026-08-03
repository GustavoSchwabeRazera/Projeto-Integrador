package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Toolkit;

public class JanelaTabela extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JButton btnRemover;
    private JButton btnEditar; 

    public JanelaTabela() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaTabela.class.getResource("/imagens/Logo.png")));
        setTitle("Livros Cadastrados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1046, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new MigLayout("", "[grow]", "[grow][]"));

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, "cell 0 0,grow");

        table = new JTable();
        table.setBackground(Color.WHITE);
        scrollPane.setViewportView(table);

        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(175, 244, 198));
        contentPane.add(painelBotoes, "cell 0 1,alignx center");
        painelBotoes.setLayout(new MigLayout("", "[][]", "[]"));

        btnEditar = new JButton("");
        btnEditar.setBackground(new Color(175, 244, 198));
        btnEditar.setContentAreaFilled(false);
        btnEditar.setBorderPainted(false);
        btnEditar.setIcon(new ImageIcon(JanelaTabela.class.getResource("/imagens/11.png")));
        btnEditar.setFont(new Font("Tahoma", Font.BOLD, 14));
        painelBotoes.add(btnEditar, "cell 0 0");

        btnRemover = new JButton("");
        btnRemover.setBorderPainted(false);
        btnRemover.setContentAreaFilled(false);
        btnRemover.setIcon(new ImageIcon(JanelaTabela.class.getResource("/imagens/Group 10.png")));
        btnRemover.setFont(new Font("Tahoma", Font.BOLD, 14));
        painelBotoes.add(btnRemover, "cell 1 0");
    }

    public JTable getTable() {
        return table;
    }

    public JButton getBtnRemover() {
        return btnRemover;
    }

    public JButton getBtnEditar() { 
        return btnEditar;
    }
}