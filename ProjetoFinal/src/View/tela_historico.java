package View;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class tela_historico extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JButton btnVoltar;
    private JTable tabelaHistorico;
    private DefaultTableModel modeloTabela;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    tela_historico frame = new tela_historico();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public tela_historico() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        setBounds(100, 100, 1920, 1080);

        contentPane = new JPanel();

        contentPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPane.setBackground(new Color(175, 244, 198));

        // Margem da tela
        contentPane.setBorder(new EmptyBorder(5, 0, 5, 5));

        setContentPane(contentPane);

        contentPane.setLayout(new MigLayout("", "[][][197.00][172.00,grow][449.00][480,grow][480,grow][261.00,grow][153.00,grow]", "[113.00,grow][][][grow][grow][grow][grow][grow][71.00,grow][54.00,grow][grow]"));

        // LOGO

        JButton btnLogo = new JButton("");
        btnLogo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        btnLogo.setContentAreaFilled(false);
        btnLogo.setBorderPainted(false);
        btnLogo.setFocusPainted(false);

        btnLogo.setIcon(
            new ImageIcon(tela_historico.class.getResource("/imagens/LogoPequena.png"))
        );

        contentPane.add(
            btnLogo,
            "cell 0 0 9 1,alignx center,aligny center"
        );

        // BOTÃO VOLTAR
        // Botão customizado com cantos arredondados e efeito hover,
        // sem depender de nenhuma imagem externa.

        btnVoltar = new BotaoArredondado(
            "\u2190  Voltar",
            new Color(10, 86, 27),
            new Color(15, 112, 38)
        );

        btnVoltar.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tela_inicial telaInicial = new tela_inicial();
                    telaInicial.setVisible(true);
                    dispose();
                }
            }
        );

        contentPane.add(
            btnVoltar,
            "cell 0 2,alignx left"
        );

        // TÍTULO

        JLabel lblTitulo = new JLabel("Histórico de Empréstimos");

        lblTitulo.setForeground(
            new Color(10, 86, 27)
        );

        lblTitulo.setFont(
            new Font("Tahoma", Font.BOLD, 40)
        );

        contentPane.add(
            lblTitulo,
            "cell 4 3 3 1,alignx center"
        );

        // TABELA DE HISTÓRICO

        String[] colunas = {"Título", "Autor", "Data Empréstimo", "Data Devolução", "Status"};

        modeloTabela = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaHistorico = new JTable(modeloTabela);
        tabelaHistorico.setRowHeight(32);
        tabelaHistorico.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tabelaHistorico.setShowGrid(false);
        tabelaHistorico.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tabelaHistorico.setSelectionBackground(new Color(175, 244, 198));

        tabelaHistorico.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        tabelaHistorico.getTableHeader().setBackground(new Color(10, 86, 27));
        tabelaHistorico.getTableHeader().setForeground(Color.WHITE);
        tabelaHistorico.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabelaHistorico);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        contentPane.add(
            scrollPane,
            "cell 1 4 7 5,grow"
        );

        // Dados de exemplo — remover ao integrar com o banco de dados real
        carregarHistoricoExemplo();
    }

    // CARREGAR / MANIPULAR DADOS DA TABELA

    private void carregarHistoricoExemplo() {
        adicionarLinha("Dom Casmurro", "Machado de Assis", "12/03/2026", "26/03/2026", "Devolvido");
        adicionarLinha("O Cortiço", "Aluísio Azevedo", "05/05/2026", "19/05/2026", "Devolvido");
        adicionarLinha("Memórias Póstumas de Brás Cubas", "Machado de Assis", "01/08/2026", "-", "Em andamento");
        adicionarLinha("Iracema", "José de Alencar", "10/01/2026", "24/01/2026", "Devolvido");
    }

    /**
     * Adiciona uma linha na tabela de histórico.
     * Chame este método a partir do Controller, passando os dados vindos do banco.
     */
    public void adicionarLinha(String titulo, String autor, String dataEmprestimo, String dataDevolucao, String status) {
        modeloTabela.addRow(new Object[]{titulo, autor, dataEmprestimo, dataDevolucao, status});
    }

    public void limparTabela() {
        modeloTabela.setRowCount(0);
    }

    // GETTERS

    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    public JTable getTabelaHistorico() {
        return tabelaHistorico;
    }

    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }

    
    // BOTÃO ARREDONDADO (uso interno, sem depender de imagem)
    
    private class BotaoArredondado extends JButton {
        private static final long serialVersionUID = 1L;
        private final Color corFundo;
        private final Color corFundoHover;

        public BotaoArredondado(String texto, Color corFundo, Color corFundoHover) {
            super(texto);
            this.corFundo = corFundo;
            this.corFundoHover = corFundoHover;

            setForeground(Color.WHITE);
            setFont(new Font("Tahoma", Font.BOLD, 14));
            setBorder(new EmptyBorder(10, 22, 10, 22));

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setBackground(corFundo);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(corFundoHover);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(corFundo);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}