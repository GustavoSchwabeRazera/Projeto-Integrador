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

public class tela_inicial extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JButton btnPesquisar;
    private JButton btnMeusLivros;
    private JButton btnSolicitacoes;
    private JButton btnPerfil;

    /**
     * Launch the application.
     */
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

    /**
     * Create the frame.
     */
    public tela_inicial() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        setBounds(100, 100, 1920, 1080);

        contentPane = new JPanel();

        contentPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPane.setBackground(new Color(175, 244, 198));

        // Margem da tela
        contentPane.setBorder(new EmptyBorder(5, 0, 5, 5));

        setContentPane(contentPane);

        contentPane.setLayout(new MigLayout("", "[][][197.00][172.00,grow][480][480,grow][480,grow][261.00,grow][153.00,grow]", "[113.00,grow][][][grow][grow][grow][grow][grow][71.00,grow][54.00,grow][grow]"));

        ImageIcon logoOriginal = new ImageIcon(
            tela_inicial.class.getResource("/imagens/Logo.png")
        );

        // Tamanho da logo
        Image logoRedimensionada = logoOriginal.getImage().getScaledInstance(
            300,  // largura
            150,  // altura
            Image.SCALE_SMOOTH
        );

        // =========================================================
        // PESQUISAR
        // =========================================================

        btnPesquisar = new JButton();

        ImageIcon pesquisar = new ImageIcon(
            tela_inicial.class.getResource("/imagens/pesquisar.png")
        );

        Image imgPesquisar = pesquisar.getImage().getScaledInstance(
            295,
            115,
            Image.SCALE_SMOOTH
        );
        
                // =========================================================
                // LOGO
                // =========================================================
        
                JButton btnNewButton = new JButton("");
                btnNewButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                	}
                });
                
                        btnNewButton.setContentAreaFilled(false);
                        btnNewButton.setBorderPainted(false);
                        btnNewButton.setFocusPainted(false);
                        
                                btnNewButton.setIcon(
                                    new ImageIcon(tela_inicial.class.getResource("/imagens/LogoPequena.png"))
                                );
                                
                                        contentPane.add(
                                            btnNewButton,
                                            "cell 0 0 3 1,alignx left,aligny center"
                                        );

        JLabel lblNewLabel_3_2 = new JLabel("");

        contentPane.add(
            lblNewLabel_3_2,
            "flowx,cell 7 0"
        );

        // =========================================================
        // PERFIL
        // =========================================================

        btnPerfil = new JButton("");

        btnPerfil.setFont(
            new Font("Tahoma", Font.BOLD, 11)
        );

        btnPerfil.setIcon(
            new ImageIcon(
                tela_inicial.class.getResource("/imagens/FotoPerfil.png")
            )
        );

        btnPerfil.setBorderPainted(false);
        btnPerfil.setContentAreaFilled(false);
        btnPerfil.setFocusPainted(false);

        contentPane.add(
            btnPerfil,
            "flowy,cell 8 0,growy"
        );

        // =========================================================
        // BEM-VINDO
        // =========================================================

        JLabel lblNewLabel = new JLabel("Bem-Vindo!");

        lblNewLabel.setForeground(
            new Color(10, 86, 27)
        );

        lblNewLabel.setFont(
            new Font("Tahoma", Font.BOLD, 51)
        );

        contentPane.add(
            lblNewLabel,
            "cell 5 3,alignx center"
        );

        // =========================================================
        // BOTÃO PESQUISAR
        // =========================================================

        btnPesquisar.setIcon(
            new ImageIcon(imgPesquisar)
        );

        btnPesquisar.setBorderPainted(false);
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setFocusPainted(false);

        contentPane.add(
            btnPesquisar,
            "cell 4 6,alignx center"
        );

        // =========================================================
        // MEUS LIVROS
        // =========================================================

        btnMeusLivros = new JButton();

        btnMeusLivros.setAlignmentX(
            Component.RIGHT_ALIGNMENT
        );

        ImageIcon livros = new ImageIcon(
            tela_inicial.class.getResource("/imagens/meus livros.png")
        );

        Image imgLivros = livros.getImage().getScaledInstance(
            295,
            115,
            Image.SCALE_SMOOTH
        );

        btnMeusLivros.setIcon(
            new ImageIcon(imgLivros)
        );

        btnMeusLivros.setBorderPainted(false);
        btnMeusLivros.setContentAreaFilled(false);
        btnMeusLivros.setFocusPainted(false);

        contentPane.add(
            btnMeusLivros,
            "cell 5 6,alignx center"
        );

        // =========================================================
        // SOLICITAÇÕES
        // =========================================================

        btnSolicitacoes = new JButton();

        ImageIcon solicitacoes = new ImageIcon(
            tela_inicial.class.getResource("/imagens/solicitacoes.png")
        );

        Image imgSolicitacoes = solicitacoes.getImage().getScaledInstance(
            295,
            115,
            Image.SCALE_SMOOTH
        );

        btnSolicitacoes.setIcon(
            new ImageIcon(imgSolicitacoes)
        );

        btnSolicitacoes.setBorderPainted(false);
        btnSolicitacoes.setContentAreaFilled(false);
        btnSolicitacoes.setFocusPainted(false);

        contentPane.add(
            btnSolicitacoes,
            "cell 6 6,alignx center"
        );

        // =========================================================
        // LABELS
        // =========================================================

        JLabel lblNewLabel_3_1 = new JLabel("");

        contentPane.add(
            lblNewLabel_3_1,
            "flowx,cell 3 0"
        );

        ImageIcon perfil = new ImageIcon(
            tela_inicial.class.getResource("/imagens/perfil3.png")
        );

        JLabel lblNewLabel_1 = new JLabel("");

        contentPane.add(
            lblNewLabel_1,
            "cell 7 0"
        );

        ImageIcon historico = new ImageIcon(
            tela_inicial.class.getResource("/imagens/historico.png")
        );

        Image imgHistorico = historico.getImage().getScaledInstance(
            170,
            50,
            Image.SCALE_SMOOTH
        );

        // =========================================================
        // CALENDÁRIO
        // =========================================================

        ImageIcon calendario = new ImageIcon(
            tela_inicial.class.getResource("/imagens/calendario.png")
        );

        Image imgCalendario = calendario.getImage().getScaledInstance(
            75,
            75,
            Image.SCALE_SMOOTH
        );

        JButton btnCalendario = new JButton("");

        btnCalendario.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            }
        );

        btnCalendario.setContentAreaFilled(false);
        btnCalendario.setBorderPainted(false);
        btnCalendario.setFocusPainted(false);

        btnCalendario.setIcon(
            new ImageIcon(imgCalendario)
        );

        contentPane.add(
            btnCalendario,
            "cell 8 8,alignx center"
        );

        // =========================================================
        // NOTIFICAÇÃO
        // =========================================================

        ImageIcon notificacao = new ImageIcon(
            tela_inicial.class.getResource("/imagens/notificacao.png")
        );

        Image imgNotificacao = notificacao.getImage().getScaledInstance(
            75,
            75,
            Image.SCALE_SMOOTH
        );

        JButton btnNotificacao = new JButton("");

        btnNotificacao.setContentAreaFilled(false);
        btnNotificacao.setBorderPainted(false);
        btnNotificacao.setFocusPainted(false);

        btnNotificacao.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            }
        );
        
                // =========================================================
                // HISTÓRICO
                // =========================================================
        
                JButton btnHistorico = new JButton("");
                
                        btnHistorico.setBorderPainted(false);
                        btnHistorico.setContentAreaFilled(false);
                        btnHistorico.setFocusPainted(false);
                        
                                btnHistorico.addActionListener(
                                    new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                        }
                                    }
                                );
                                
                                        btnHistorico.setIcon(
                                            new ImageIcon(imgHistorico)
                                        );
                                        
                                                contentPane.add(
                                                    btnHistorico,
                                                    "cell 0 9 3 1,alignx right,aligny bottom"
                                                );

        btnNotificacao.setIcon(
            new ImageIcon(imgNotificacao)
        );

        contentPane.add(
            btnNotificacao,
            "cell 8 9 1 2,alignx center"
        );
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }

    public JButton getBtnMeusLivros() {
        return btnMeusLivros;
    }

    public JButton getBtnSolicitacoes() {
        return btnSolicitacoes;
    }

    public JButton getBtnPerfil() {
        return btnPerfil;
    }
}