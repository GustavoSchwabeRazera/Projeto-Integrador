package View;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.LayoutManager;
import java.awt.Dimension;

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
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import java.awt.Cursor;
import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import model.Solicitacao;

public class TelaSolicitacoes extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnHome;
	private JPanel listaSolicitacoes;
	private Consumer<Solicitacao> aoAceitar;
	private Consumer<Solicitacao> aoRecusar;
	private JLabel lblNewLabel;

    /**
     * Painel com cantos arredondados
     */
	/**
     * Painel com cantos arredondados
     */
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

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            // AS LINHAS COM ERRO FORAM REMOVIDAS DAQUI

            g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                radius,
                radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            public void run() {

                try {

                    TelaSolicitacoes frame = new TelaSolicitacoes();
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
    public TelaSolicitacoes() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);

        contentPane.setLayout(
            new MigLayout(
                "",
                "[200,grow][200,grow][200,grow][200,grow][200,grow]",
                "[][][grow][grow][grow][grow][grow][grow][grow][grow]"
            )
        );


        // =====================================================
        // BOTÃO HOME
        // =====================================================

        btnHome = new JButton("");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		btnHome.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/casa 1.png")));

		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));

		btnHome.setForeground(new Color(10, 86, 27));

		btnHome.setBorderPainted(false);
		btnHome.setContentAreaFilled(false);
		btnHome.setFocusPainted(false);

		contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");
        
        lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(TelaSolicitacoes.class.getResource("/imagens/Logo.png")));
        contentPane.add(lblNewLabel, "cell 1 0 3 1,alignx center,aligny top");


        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel lblSolicitacoes = new JLabel("Solicitações");

        lblSolicitacoes.setForeground(
            new Color(10, 86, 27)
        );

        lblSolicitacoes.setFont(
            new Font("Tahoma", Font.BOLD, 34)
        );

        contentPane.add(
            lblSolicitacoes,
            "cell 0 1 5 1,alignx center"
        );


        // =====================================================
        // PAINEL DE SOLICITAÇÕES
        // =====================================================

        RoundedPanel painelSolicitacoes = new RoundedPanel(
            new MigLayout(
                "insets 0",
                "[grow]",
                "[grow]"
            ),
            40
        );
        painelSolicitacoes.setForeground(new Color(10, 86, 27));

        painelSolicitacoes.setBackground(
            new Color(36, 107, 45)
        );

        painelSolicitacoes.setBorder(
            BorderFactory.createEmptyBorder(
                30,
                35,
                30,
                35
            )
        );

        contentPane.add(
            painelSolicitacoes,
            "cell 0 2 5 8,grow"
        );

        // Lista dos cartões (com barra de rolagem quando tiver muitos)
        listaSolicitacoes = new JPanel(
            new MigLayout("insets 0, wrap 1", "[grow]", "[]15[]")
        );
        listaSolicitacoes.setOpaque(false);

        JScrollPane scroll = new JScrollPane(listaSolicitacoes);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        painelSolicitacoes.add(scroll, "cell 0 0,grow");

        mostrarSolicitacoes(new ArrayList<Solicitacao>());
    }


    // =====================================================
    // MOSTRA AS SOLICITAÇÕES NA TELA
    // =====================================================

    public void mostrarSolicitacoes(List<Solicitacao> solicitacoes) {

        listaSolicitacoes.removeAll();

        if (solicitacoes.isEmpty()) {

            JLabel lblVazio = new JLabel("Nenhuma solicitação pendente.");
            lblVazio.setForeground(Color.WHITE);
            lblVazio.setFont(new Font("Tahoma", Font.BOLD, 22));
            listaSolicitacoes.add(lblVazio, "alignx center,gaptop 40");

        } else {

            for (Solicitacao s : solicitacoes) {
                listaSolicitacoes.add(criarCartao(s), "growx");
            }
        }

        listaSolicitacoes.revalidate();
        listaSolicitacoes.repaint();
    }


    // =====================================================
    // CARTÃO DE UMA SOLICITAÇÃO
    // =====================================================

    private JPanel criarCartao(Solicitacao s) {

        RoundedPanel cartao = new RoundedPanel(
            new MigLayout(
                "insets 14 25 14 25",
                "[]20[grow]20[]20[]",
                "[center]"
            ),
            35
        );
        cartao.setForeground(new Color(255, 255, 255));

        // Foto de quem pediu
        JLabel lblFoto = new JLabel(new ImageIcon(pegarFoto(s.getFotoSolicitante(), 45)));
        cartao.add(lblFoto, "cell 0 0,alignx center,aligny center");

        // Nome + livro
        JPanel informacoes = new JPanel(
            new MigLayout("insets 0", "[grow]", "[]2[]")
        );
        informacoes.setOpaque(false);
        cartao.add(informacoes, "cell 1 0,growx,aligny center");

        JLabel lblNome = new JLabel(
            s.getNomeSolicitante() + " quer " + s.getTituloLivro()
        );
        lblNome.setFont(new Font("Tahoma", Font.BOLD, 21));
        informacoes.add(lblNome, "cell 0 0");

        JLabel lblDetalhes = new JLabel(
            "Solicitação de empréstimo em "
            + s.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            + "   •   " + s.getEmailSolicitante()
            + "   •   " + s.getTelefoneSolicitante()
        );
        lblDetalhes.setForeground(new Color(60, 60, 60));
        lblDetalhes.setFont(new Font("Tahoma", Font.PLAIN, 15));
        informacoes.add(lblDetalhes, "cell 0 1");

        // Botão recusar
        JButton btnExcluir = criarBotaoIcone("/imagens/excluirdim.png", "Recusar");
        btnExcluir.addActionListener(e -> {
            if (aoRecusar != null) {
                aoRecusar.accept(s);
            }
        });
        cartao.add(btnExcluir, "cell 2 0,alignx center,aligny center");

        // Botão aceitar
        JButton btnAceitar = criarBotaoIcone("/imagens/verificadim.png", "Aceitar");
        btnAceitar.addActionListener(e -> {
            if (aoAceitar != null) {
                aoAceitar.accept(s);
            }
        });
        cartao.add(btnAceitar, "cell 3 0,alignx center,aligny center");

        return cartao;
    }

    private JButton criarBotaoIcone(String imagem, String dica) {

        JButton botao = new JButton("");
        botao.setIcon(new ImageIcon(TelaSolicitacoes.class.getResource(imagem)));
        botao.setToolTipText(dica);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return botao;
    }

    // Foto do usuário (ou a imagem padrão se ele não tiver)
    private Image pegarFoto(byte[] bytes, int tamanho) {

        Image imagem = null;

        if (bytes != null && bytes.length > 0) {
            try {
                imagem = ImageIO.read(new ByteArrayInputStream(bytes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imagem == null) {
            imagem = new ImageIcon(TelaSolicitacoes.class.getResource("/imagens/perfil3.png")).getImage();
        }

        return imagem.getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH);
    }

    public JButton getBtnHome() {
        return btnHome;
    }

    // O controller diz o que fazer quando clicar em aceitar/recusar
    public void setAoAceitar(Consumer<Solicitacao> aoAceitar) {
        this.aoAceitar = aoAceitar;
    }

    public void setAoRecusar(Consumer<Solicitacao> aoRecusar) {
        this.aoRecusar = aoRecusar;
    }

}
