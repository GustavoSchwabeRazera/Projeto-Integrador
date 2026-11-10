package View;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import model.EmprestimoAtivo;
import model.Livro;

public class TelaLivrosComigo extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JPanel contentPane;
    private JButton btnHome;
    private JPanel listaLivros;

    private Consumer<EmprestimoAtivo> aoDevolver;

    /** Painel com cantos arredondados. */
    private static class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final int radius;

        public RoundedPanel(LayoutManager layout, int radius) {
            super(layout);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaLivrosComigo frame = new TelaLivrosComigo();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaLivrosComigo() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);

        contentPane = new JPanel();
        contentPane.setBackground(Cores.FUNDO);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        contentPane.setLayout(
            new MigLayout(
                "",
                "[200,grow][200,grow][200,grow][200,grow][200,grow]",
                "[][][grow][grow][grow][grow][grow][grow][grow][grow]"
            )
        );

        // BOTÃO HOME
        btnHome = new JButton("");
        btnHome.setIcon(new ImageIcon(TelaLivrosComigo.class.getResource("/imagens/casa 1.png")));
        btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));
        btnHome.setForeground(new Color(10, 86, 27));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

        // LOGO
        JLabel lblLogo = new JLabel("");
        lblLogo.setIcon(new ImageIcon(TelaLivrosComigo.class.getResource("/imagens/Logo.png")));
        contentPane.add(lblLogo, "cell 1 0 3 1,alignx center,aligny top");

        // TÍTULO
        JLabel lblTitulo = new JLabel("Livros Comigo");
        lblTitulo.setForeground(new Color(10, 86, 27));
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 34));
        contentPane.add(lblTitulo, "cell 0 1 5 1,alignx center");

        // PAINEL DOS LIVROS
        RoundedPanel painelLivros = new RoundedPanel(
            new MigLayout("insets 0", "[grow]", "[grow]"), 40
        );
        painelLivros.setBackground(new Color(36, 107, 45));
        painelLivros.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        contentPane.add(painelLivros, "cell 0 2 5 8,grow");

        // Lista dos cartões (com barra de rolagem quando tiver muitos)
        listaLivros = new JPanel(
            new MigLayout("insets 0, wrap 1", "[grow]", "[]15[]")
        );
        listaLivros.setOpaque(false);

        JScrollPane scroll = new JScrollPane(listaLivros);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        painelLivros.add(scroll, "cell 0 0,grow");

        mostrarLivros(new ArrayList<EmprestimoAtivo>());
    }


    // =====================================================
    // MOSTRA OS LIVROS QUE ESTÃO COM O USUÁRIO
    // =====================================================

    public void mostrarLivros(List<EmprestimoAtivo> emprestimos) {

        listaLivros.removeAll();

        if (emprestimos.isEmpty()) {

            JLabel lblVazio = new JLabel("Você não está com nenhum livro emprestado.");
            lblVazio.setForeground(Color.WHITE);
            lblVazio.setFont(new Font("Tahoma", Font.BOLD, 22));
            listaLivros.add(lblVazio, "alignx center,gaptop 40");

        } else {

            for (EmprestimoAtivo emprestimo : emprestimos) {
                listaLivros.add(criarCartao(emprestimo), "growx");
            }
        }

        listaLivros.revalidate();
        listaLivros.repaint();
    }


    // =====================================================
    // CARTÃO DE UM LIVRO
    // =====================================================

    private JPanel criarCartao(EmprestimoAtivo emprestimo) {

        RoundedPanel cartao = new RoundedPanel(
            new MigLayout("insets 14 25 14 25", "[]20[grow]20[]", "[center]"), 35
        );
        cartao.setBackground(Cores.FUNDO);

        // Capa do livro
        Livro livro = new Livro();
        livro.setFotoCapa(emprestimo.getFotoCapa());

        JLabel lblCapa = new JLabel(new ImageIcon(TelaMeusLivros.pegarImagem(livro, 80, 105)));
        cartao.add(lblCapa, "cell 0 0,alignx center,aligny center");

        // Título + dono + prazo
        JPanel informacoes = new JPanel(new MigLayout("insets 0", "[grow]", "[]4[]4[]"));
        informacoes.setOpaque(false);
        cartao.add(informacoes, "cell 1 0,growx,aligny center");

        JLabel lblTitulo = new JLabel(emprestimo.getTituloLivro());
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 21));
        informacoes.add(lblTitulo, "cell 0 0");

        JLabel lblDono = new JLabel(
            "Livro de " + emprestimo.getNomeDono()
            + "   •   " + emprestimo.getEmailDono()
            + "   •   " + emprestimo.getTelefoneDono()
        );
        lblDono.setForeground(new Color(60, 60, 60));
        lblDono.setFont(new Font("Tahoma", Font.PLAIN, 15));
        informacoes.add(lblDono, "cell 0 1");

        JLabel lblPrazo = new JLabel(textoPrazo(emprestimo));
        lblPrazo.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblPrazo.setForeground(
            !emprestimo.isDevolucaoSolicitada() && emprestimo.getDiasRestantes() < 0
                ? new Color(140, 20, 20)
                : new Color(10, 86, 27)
        );
        informacoes.add(lblPrazo, "cell 0 2");

        // Já devolveu: espera o dono em vez de mostrar o botão
        if (emprestimo.isDevolucaoSolicitada()) {

            JLabel lblAguardando = new JLabel("Aguardando confirmação");
            lblAguardando.setForeground(new Color(120, 60, 0));
            lblAguardando.setFont(new Font("Tahoma", Font.BOLD, 16));
            cartao.add(lblAguardando, "cell 2 0,alignx center,aligny center");

        } else {

            JButton btnDevolver = new TelaMeusLivros.BotaoArredondado("Devolver", 40);
            btnDevolver.setBackground(new Color(114, 219, 145));
            btnDevolver.setForeground(Color.BLACK);
            btnDevolver.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnDevolver.setMargin(new Insets(8, 24, 8, 24));
            btnDevolver.addActionListener(e -> {
                if (aoDevolver != null) {
                    aoDevolver.accept(emprestimo);
                }
            });
            cartao.add(btnDevolver, "cell 2 0,alignx center,aligny center");
        }

        return cartao;
    }

    private String textoPrazo(EmprestimoAtivo emprestimo) {

        if (emprestimo.isDevolucaoSolicitada()) {
            return "Devolvido em " + emprestimo.getDevolucaoSolicitada().format(FORMATO_DATA)
                    + "   •   " + emprestimo.getNomeDono() + " precisa confirmar";
        }

        String data = "Devolver até " + emprestimo.getDataEntrega().format(FORMATO_DATA);
        long dias = emprestimo.getDiasRestantes();

        if (dias < 0) {
            return data + "   •   atrasado há " + (-dias) + (dias == -1 ? " dia" : " dias");
        }
        if (dias == 0) {
            return data + "   •   termina hoje";
        }
        return data + "   •   " + (dias == 1 ? "falta 1 dia" : "faltam " + dias + " dias");
    }

    public JButton getBtnHome() {
        return btnHome;
    }

    // O controller diz o que fazer quando clicar em devolver
    public void setAoDevolver(Consumer<EmprestimoAtivo> aoDevolver) {
        this.aoDevolver = aoDevolver;
    }
}
