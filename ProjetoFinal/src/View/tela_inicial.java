package View;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Component;

public class tela_inicial extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JButton btnPesquisar;
    private JButton btnMeusLivros;
    private JButton btnSolicitacoes;
    private JButton btnPerfil;
    private JButton btnSair;
    private JButton btnCalendario;
    private JButton btnHistorico;
    private JButton btnNotificacao;   // <-- agora é ATRIBUTO da classe
    private JButton btnLogo;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    tela_inicial frame = new tela_inicial();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public tela_inicial() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);

        contentPane = new JPanel();
        contentPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(5, 0, 5, 5));
        setContentPane(contentPane);

        contentPane.setLayout(new MigLayout(
            "",
            "[][][197.00][172.00,grow][449.00][480,grow][480,grow][261.00,grow][153.00,grow]",
            "[113.00,grow][][][grow][grow][grow][grow][grow][71.00,grow][54.00,grow][grow]"
        ));

        // =========================================================
        // LOGO
        // =========================================================
        btnLogo = new JButton("");
        btnLogo.setContentAreaFilled(false);
        btnLogo.setBorderPainted(false);
        btnLogo.setFocusPainted(false);
        btnLogo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogo.setIcon(new ImageIcon(tela_inicial.class.getResource("/imagens/LogoPequena.png")));
        contentPane.add(btnLogo, "cell 0 0 3 1,alignx left,aligny center");

        JLabel lblEspaco1 = new JLabel("");
        contentPane.add(lblEspaco1, "flowx,cell 7 0");

        // =========================================================
        // PERFIL
        // =========================================================
        btnPerfil = new JButton("");
        btnPerfil.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnPerfil.setIcon(new ImageIcon(tela_inicial.class.getResource("/imagens/FotoPerfil.png")));
        btnPerfil.setBorderPainted(false);
        btnPerfil.setContentAreaFilled(false);
        btnPerfil.setFocusPainted(false);
        btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnPerfil, "flowy,cell 8 0,growy");

        // =========================================================
        // SAIR
        // =========================================================
        btnSair = new JButton("");
        btnSair.setFocusPainted(false);
        btnSair.setBorderPainted(false);
        btnSair.setContentAreaFilled(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setIcon(new ImageIcon(tela_inicial.class.getResource("/imagens/sairAjustado.png")));
        contentPane.add(btnSair, "cell 8 1");

        // =========================================================
        // BEM-VINDO
        // =========================================================
        JLabel lblBemVindo = new JLabel("Bem-Vindo!");
        lblBemVindo.setForeground(new Color(10, 86, 27));
        lblBemVindo.setFont(new Font("Tahoma", Font.BOLD, 51));
        contentPane.add(lblBemVindo, "cell 5 3,alignx center");

        // =========================================================
        // PESQUISAR
        // =========================================================
        ImageIcon pesquisar = new ImageIcon(tela_inicial.class.getResource("/imagens/pesquisar.png"));
        Image imgPesquisar = pesquisar.getImage().getScaledInstance(295, 115, Image.SCALE_SMOOTH);

        btnPesquisar = new JButton();
        btnPesquisar.setIcon(new ImageIcon(imgPesquisar));
        btnPesquisar.setBorderPainted(false);
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setFocusPainted(false);
        btnPesquisar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnPesquisar, "cell 4 6,alignx center");

        // =========================================================
        // MEUS LIVROS
        // =========================================================
        ImageIcon livros = new ImageIcon(tela_inicial.class.getResource("/imagens/meus livros.png"));
        Image imgLivros = livros.getImage().getScaledInstance(295, 115, Image.SCALE_SMOOTH);

        btnMeusLivros = new JButton();
        btnMeusLivros.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnMeusLivros.setIcon(new ImageIcon(imgLivros));
        btnMeusLivros.setBorderPainted(false);
        btnMeusLivros.setContentAreaFilled(false);
        btnMeusLivros.setFocusPainted(false);
        btnMeusLivros.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnMeusLivros, "cell 5 6,alignx center");

        // =========================================================
        // SOLICITAÇÕES
        // =========================================================
        ImageIcon solicitacoes = new ImageIcon(tela_inicial.class.getResource("/imagens/solicitacoes.png"));
        Image imgSolicitacoes = solicitacoes.getImage().getScaledInstance(295, 115, Image.SCALE_SMOOTH);

        btnSolicitacoes = new JButton();
        btnSolicitacoes.setIcon(new ImageIcon(imgSolicitacoes));
        btnSolicitacoes.setBorderPainted(false);
        btnSolicitacoes.setContentAreaFilled(false);
        btnSolicitacoes.setFocusPainted(false);
        btnSolicitacoes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnSolicitacoes, "cell 6 6,alignx center");

        JLabel lblEspaco2 = new JLabel("");
        contentPane.add(lblEspaco2, "flowx,cell 3 0");

        JLabel lblEspaco3 = new JLabel("");
        contentPane.add(lblEspaco3, "cell 7 0");

        // =========================================================
        // CALENDÁRIO
        // =========================================================
        ImageIcon calendario = new ImageIcon(tela_inicial.class.getResource("/imagens/calendario.png"));
        Image imgCalendario = calendario.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);

        btnCalendario = new JButton("");
        btnCalendario.setContentAreaFilled(false);
        btnCalendario.setBorderPainted(false);
        btnCalendario.setFocusPainted(false);
        btnCalendario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalendario.setIcon(new ImageIcon(imgCalendario));
        contentPane.add(btnCalendario, "cell 8 8,alignx center");

        // =========================================================
        // HISTÓRICO
        // =========================================================
        ImageIcon historico = new ImageIcon(tela_inicial.class.getResource("/imagens/historico.png"));
        Image imgHistorico = historico.getImage().getScaledInstance(170, 50, Image.SCALE_SMOOTH);

        btnHistorico = new JButton("");
        btnHistorico.setBorderPainted(false);
        btnHistorico.setContentAreaFilled(false);
        btnHistorico.setFocusPainted(false);
        btnHistorico.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistorico.setIcon(new ImageIcon(imgHistorico));
        // O dispose() que estava aqui foi REMOVIDO: quem controla a navegação é o Controller.
        contentPane.add(btnHistorico, "cell 0 9 3 1,alignx left,aligny center");

        // =========================================================
        // NOTIFICAÇÃO (sino)
        // =========================================================
        ImageIcon notificacao = new ImageIcon(tela_inicial.class.getResource("/imagens/notificacao.png"));
        Image imgNotificacao = notificacao.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);

        btnNotificacao = new JButton("");
        btnNotificacao.setContentAreaFilled(false);
        btnNotificacao.setBorderPainted(false);
        btnNotificacao.setFocusPainted(false);
        btnNotificacao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNotificacao.setToolTipText("Notificações");
        btnNotificacao.setIcon(new ImageIcon(imgNotificacao));
        // Sem ActionListener aqui: o LivroController é quem registra a ação.
        contentPane.add(btnNotificacao, "cell 8 9 1 2,alignx center");
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public JButton getBtnPesquisar()    { return btnPesquisar; }
    public JButton getBtnMeusLivros()   { return btnMeusLivros; }
    public JButton getBtnSolicitacoes() { return btnSolicitacoes; }
    public JButton getBtnPerfil()       { return btnPerfil; }
    public JButton getBtnSair()         { return btnSair; }
    public JButton getBtnCalendario()   { return btnCalendario; }
    public JButton getBtnHistorico()    { return btnHistorico; }
    public JButton getBtnNotificacao()  { return btnNotificacao; }
    public JButton getBtnLogo()         { return btnLogo; }
}