package View;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URI;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

// Tela "Sobre": aberta ao clicar na logo da tela inicial.
// Mostra o que é o sistema, quem fez e onde está o código.
public class TelaSobre extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String FONTE = "Segoe UI";
    private static final Color VERDE_TEXTO = new Color(40, 90, 50);
    private static final Color TEXTO_CLARO = new Color(225, 245, 230);

    private static final String INSTITUICAO = "IFSC - Campus Gaspar";
    private static final String ANO = "2026";
    private static final String GITHUB = "https://github.com/GustavoSchwabeRazera/Projeto-Integrador";

    private static final int TAMANHO_FOTO = 120;

    // Nome, foto, zoom (1 = foto inteira) e altura do rosto na foto (0 = topo, 1 = base)
    private static final Integrante[] INTEGRANTES = {
            new Integrante("Gustavo Schwabe Razera", "/imagens/integrante_gustavo.jpg", 1.0, 0.5),
            new Integrante("Guilherme de Liz", "/imagens/integrante_guilherme.jpg", 0.65, 0.3),
            new Integrante("Eduardo Tomé da Silva", "/imagens/integrante_eduardo.png", 1.0, 0.4),
            new Integrante("Robson Machado", "/imagens/integrante_robson.jpg", 1.0, 0.38)
    };

    private JButton btnHome;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new TelaSobre().setVisible(true));
    }

    public TelaSobre() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1920, 1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Cores.FUNDO);
        contentPane.setBorder(new EmptyBorder(16, 28, 20, 28));
        setContentPane(contentPane);

        // Botão de voltar para a tela inicial (a ação fica no Controller)
        btnHome = new JButton(new ImageIcon(TelaSobre.class.getResource("/imagens/casa 1.png")));
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setToolTipText("Voltar para o início");

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);
        barra.add(btnHome, BorderLayout.WEST);
        contentPane.add(barra, BorderLayout.NORTH);

        // Conteúdo com rolagem, para caber em telas menores
        JScrollPane scroll = new JScrollPane(criarConteudo());
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        contentPane.add(scroll, BorderLayout.CENTER);
    }

    private JPanel criarConteudo() {

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        // Logo grande
        Image logo = new ImageIcon(TelaSobre.class.getResource("/imagens/Logo.png")).getImage();
        JLabel lblLogo = new JLabel(new ImageIcon(logo.getScaledInstance(254, 120, Image.SCALE_SMOOTH)));
        centro.add(lblLogo, c);

        JLabel lblTitulo = new JLabel("Sobre o Capas Vivas", SwingConstants.CENTER);
        lblTitulo.setForeground(Cores.VERDE);
        lblTitulo.setFont(new Font(FONTE, Font.BOLD, 36));
        c.gridy++;
        c.insets = new Insets(4, 0, 6, 0);
        centro.add(lblTitulo, c);

        JLabel lblDescricao = new JLabel(
                "<html><div style='text-align:center; width:620px'>"
                + "O Capas Vivas é um sistema para cadastrar, pesquisar e compartilhar livros. "
                + "Os usuários podem emprestar seus livros, pedir emprestado os de outras pessoas "
                + "e acompanhar tudo pelo calendário, notificações, histórico e chat."
                + "</div></html>",
                SwingConstants.CENTER);
        lblDescricao.setForeground(VERDE_TEXTO);
        lblDescricao.setFont(new Font(FONTE, Font.PLAIN, 18));
        c.gridy++;
        c.insets = new Insets(0, 0, 20, 0);
        centro.add(lblDescricao, c);

        // Cartão verde com os integrantes
        c.gridy++;
        c.insets = new Insets(0, 0, 20, 0);
        centro.add(criarCartaoIntegrantes(), c);

        JLabel lblInstituicao = new JLabel(INSTITUICAO + "  ·  " + ANO, SwingConstants.CENTER);
        lblInstituicao.setForeground(Cores.VERDE);
        lblInstituicao.setFont(new Font(FONTE, Font.BOLD, 20));
        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        centro.add(lblInstituicao, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        centro.add(criarLinkGithub(), c);

        return centro;
    }

    private JPanel criarCartaoIntegrantes() {

        PainelArredondado cartao = new PainelArredondado(new GridBagLayout(), 28);
        cartao.setBackground(Cores.VERDE);
        cartao.setBorder(new EmptyBorder(18, 30, 22, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = INTEGRANTES.length;

        JLabel lblTitulo = new JLabel("Integrantes do grupo", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font(FONTE, Font.BOLD, 26));
        c.insets = new Insets(0, 0, 16, 0);
        cartao.add(lblTitulo, c);

        // Uma coluna por integrante: foto redonda e o nome embaixo
        c.gridwidth = 1;
        for (Integrante integrante : INTEGRANTES) {

            JLabel lblFoto = new JLabel(fotoRedonda(integrante));
            c.gridy = 1;
            c.insets = new Insets(0, 18, 8, 18);
            cartao.add(lblFoto, c);

            JLabel lblNome = new JLabel(integrante.nome, SwingConstants.CENTER);
            lblNome.setForeground(TEXTO_CLARO);
            lblNome.setFont(new Font(FONTE, Font.PLAIN, 18));
            c.gridy = 2;
            c.insets = new Insets(0, 18, 0, 18);
            cartao.add(lblNome, c);

            c.gridx++;
        }

        return cartao;
    }

    // Recorta um quadrado em volta do rosto e transforma em círculo com borda clara
    private ImageIcon fotoRedonda(Integrante integrante) {

        int tamanho = TAMANHO_FOTO;
        BufferedImage redonda = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);

        try {
            BufferedImage original = ImageIO.read(TelaSobre.class.getResource(integrante.foto));

            int largura = original.getWidth();
            int altura = original.getHeight();
            int lado = (int) (Math.min(largura, altura) * integrante.zoom);

            int x = (largura - lado) / 2;
            int y = (int) (altura * integrante.focoY) - lado / 2;
            y = Math.max(0, Math.min(y, altura - lado));

            Image recorte = original.getSubimage(x, y, lado, lado)
                    .getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH);

            Graphics2D g2 = redonda.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Desenha o círculo e depois a foto só onde o círculo existe (borda lisa)
            g2.fill(new Ellipse2D.Double(0, 0, tamanho, tamanho));
            g2.setComposite(AlphaComposite.SrcIn);
            g2.drawImage(recorte, 0, 0, null);

            g2.setComposite(AlphaComposite.SrcOver);
            g2.setColor(Cores.FUNDO);
            g2.setStroke(new BasicStroke(3f));
            g2.draw(new Ellipse2D.Double(1.5, 1.5, tamanho - 3, tamanho - 3));
            g2.dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ImageIcon(redonda);
    }

    // Link do repositório: abre no navegador padrão
    private JButton criarLinkGithub() {

        JButton link = new JButton("<html><u>" + GITHUB.replace("https://", "") + "</u></html>");
        link.setForeground(Cores.VERDE);
        link.setFont(new Font(FONTE, Font.PLAIN, 17));
        link.setContentAreaFilled(false);
        link.setBorderPainted(false);
        link.setFocusPainted(false);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.setToolTipText("Abrir o repositório no navegador");
        link.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(GITHUB));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return link;
    }

    // Painel com cantos arredondados
    private static class PainelArredondado extends JPanel {

        private static final long serialVersionUID = 1L;
        private final int arco;

        PainelArredondado(LayoutManager layout, int arco) {
            super(layout);
            this.arco = arco;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arco, arco);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class Integrante {

        final String nome;
        final String foto;
        final double zoom;
        final double focoY;

        Integrante(String nome, String foto, double zoom, double focoY) {
            this.nome = nome;
            this.foto = foto;
            this.zoom = zoom;
            this.focoY = focoY;
        }
    }

    public JButton getBtnHome() {
        return btnHome;
    }
}
