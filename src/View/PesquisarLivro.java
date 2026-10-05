package View;
 
import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
 
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
 
import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.util.List;

import model.Livro;
import model.LivroTableModel;
 
public class PesquisarLivro extends JFrame {
 
    private static final long serialVersionUID = 1L;
 
    private JPanel contentPane;
    private JTextField textField;
    private JButton btnHome;
    private JButton btnPesquisar;
    private JScrollPane scrollLivros;
    private LivroTableModel livroTableModel;
 
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PesquisarLivro frame = new PesquisarLivro();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
 
    public PesquisarLivro() {
 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);
 
        // ===== PAINEL PRINCIPAL =====
        contentPane = new JPanel();
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
 
        // ===== LAYOUT (uma coluna só) =====
        contentPane.setLayout(
            new MigLayout("", "[grow]", "[][][][35.00,grow][105.00,grow][104.00,grow][][][grow]")
        );
 
        // ===== BOTÃO HOME =====
        btnHome = new JButton("");
        btnHome.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/casa 1.png")));
        btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));
        btnHome.setForeground(new Color(10, 86, 27));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);
        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");
 
        // ===== TÍTULO (centralizado) =====
        JLabel lblNewLabel = new JLabel("Pesquisar Livros");
        lblNewLabel.setForeground(new Color(10, 86, 27));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 51));
        contentPane.add(lblNewLabel, "cell 0 1,alignx center");
 
        // ===== CAMPO DE PESQUISA (centralizado) =====
        textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setColumns(10);
        contentPane.add(textField, "flowx,cell 0 3,alignx center,width 500!,height 42!");
 
        // ===== BOTÃO PESQUISAR (menor, ao lado da caixa) =====
        ImageIcon pesquisar = new ImageIcon(PesquisarLivro.class.getResource("/imagens/pesquisar.png"));
        Image imagemPesquisar = pesquisar.getImage().getScaledInstance(180, 65, Image.SCALE_SMOOTH);
 
        btnPesquisar = new JButton("");
        btnPesquisar.setBorderPainted(false);
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setFocusPainted(false);
        btnPesquisar.setIcon(new ImageIcon(imagemPesquisar));
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ação do botão pesquisar
            }
        });
        contentPane.add(btnPesquisar, "cell 0 3,width 190!,height 75!");
 
        // Redimensiona a imagem junto com o botão
        btnPesquisar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int largura = btnPesquisar.getWidth();
                int altura = btnPesquisar.getHeight();
                if (largura > 0 && altura > 0) {
                    Image novaImagem = pesquisar.getImage().getScaledInstance(
                        Math.max(1, largura - 10),
                        Math.max(1, altura - 10),
                        Image.SCALE_SMOOTH
                    );
                    btnPesquisar.setIcon(new ImageIcon(novaImagem));
                }
            }
        });
 
        // ===== LOGO CENTRAL =====
        ImageIcon logo = new ImageIcon(PesquisarLivro.class.getResource("/imagens/Logo.png"));
        Image imagemLogo = logo.getImage().getScaledInstance(350, 190, Image.SCALE_SMOOTH);

        // ===== TABELA COM OS LIVROS ENCONTRADOS =====
        livroTableModel = new LivroTableModel();
        JTable tabelaLivros = new JTable(livroTableModel);
        tabelaLivros.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabelaLivros.setRowHeight(28);
        tabelaLivros.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        tabelaLivros.getTableHeader().setForeground(new Color(10, 86, 27));

        scrollLivros = new JScrollPane(tabelaLivros);
        scrollLivros.setVisible(false); // só aparece depois de pesquisar
        contentPane.add(scrollLivros, "cell 0 4 1 3,grow");
 
        // ===== LOCALIZAÇÃO =====
        JButton btnLocalizacao = new JButton("Gaspar");
        btnLocalizacao.setFont(new Font("Tahoma", Font.BOLD, 36));
        btnLocalizacao.setBorderPainted(false);
        btnLocalizacao.setContentAreaFilled(false);
        btnLocalizacao.setIcon(new ImageIcon(PesquisarLivro.class.getResource("/imagens/LogoLocalizacao.png")));
        btnLocalizacao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ação da localização
            }
        });
        contentPane.add(btnLocalizacao, "cell 0 8,alignx left,aligny bottom");
    }
 
    // ===== GETTERS =====
    public JButton getBtnHome() {
        return btnHome;
    }
 
    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }
 
    public String getTextoPesquisa() {
        return textField.getText().trim();
    }
 
    // Mostra os livros na tabela (esconde a logo para dar espaço)
    public void mostrarLivros(List<Livro> livros) {
        livroTableModel.setLista(livros);
        scrollLivros.setVisible(true);
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void limparPesquisa() {
        textField.setText("");
    }
}