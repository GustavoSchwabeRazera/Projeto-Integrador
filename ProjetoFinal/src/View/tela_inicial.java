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
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
	
	public class tela_inicial extends JFrame {
	
		private static final long serialVersionUID = 1L;
		private JPanel contentPane;
	
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
			setBounds(100, 100, 1330, 721);
			contentPane = 	new JPanel();
			contentPane.setBackground(new Color(175, 244, 198));
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(new MigLayout("", "[200,grow][200,grow][200,grow][200,grow][200,grow]", "[113.00,grow][][grow][grow][grow][grow][grow][grow][grow][grow]"));
			
			//logo
			JLabel lblNewLabel_3 = new JLabel("");

			ImageIcon logo = new ImageIcon(tela_inicial.class.getResource("/imagens/Logo.png"));
			Image imagemRedimensionada = logo.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);

			lblNewLabel_3.setIcon(new ImageIcon(imagemRedimensionada));

			contentPane.add(lblNewLabel_3, "flowy,cell 0 0,alignx center");
			
			//pesquisar
			JButton btnPesquisar = new JButton();
			btnPesquisar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});

			ImageIcon pesquisar = new ImageIcon(tela_inicial.class.getResource("/imagens/pesquisar.png"));
			Image imgPesquisar = pesquisar.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);
			
			JLabel lblNewLabel_3_2 = new JLabel("");
			contentPane.add(lblNewLabel_3_2, "flowx,cell 4 0");
			
			JLabel lblNewLabel = new JLabel("Bem-Vindo!");
			lblNewLabel.setForeground(new Color(10, 86, 27));
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 51));
			contentPane.add(lblNewLabel, "cell 0 2 5 1,alignx center");

			btnPesquisar.setIcon(new ImageIcon(imgPesquisar));
			btnPesquisar.setBorderPainted(false);
			btnPesquisar.setContentAreaFilled(false);
			btnPesquisar.setFocusPainted(false);

			contentPane.add(btnPesquisar, "cell 1 5,alignx center");
			
			//meus livros
			JButton btnMeusLivros = new JButton();

			ImageIcon livros = new ImageIcon(tela_inicial.class.getResource("/imagens/meus livros.png"));
			Image imgLivros = livros.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);

			btnMeusLivros.setIcon(new ImageIcon(imgLivros));
			btnMeusLivros.setBorderPainted(false);
			btnMeusLivros.setContentAreaFilled(false);
			btnMeusLivros.setFocusPainted(false);

			contentPane.add(btnMeusLivros, "cell 2 5,alignx center");			
			
			//solicitacoes
			JButton btnSolicitacoes = new JButton();

			ImageIcon solicitacoes = new ImageIcon(tela_inicial.class.getResource("/imagens/solicitacoes.png"));
			Image imgSolicitacoes = solicitacoes.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);

			btnSolicitacoes.setIcon(new ImageIcon(imgSolicitacoes));
			btnSolicitacoes.setBorderPainted(false);
			btnSolicitacoes.setContentAreaFilled(false);
			btnSolicitacoes.setFocusPainted(false);

			contentPane.add(btnSolicitacoes, "cell 3 5,alignx center");			
			
			JLabel lblNewLabel_3_1 = new JLabel("");
			contentPane.add(lblNewLabel_3_1, "cell 0 0");
			
			//perfil
			JButton btnPerfil = new JButton();
			btnPerfil.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});

			ImageIcon perfil = new ImageIcon(tela_inicial.class.getResource("/imagens/perfil3.png"));
			Image imgPerfil = perfil.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

			btnPerfil.setIcon(new ImageIcon(imgPerfil));
			btnPerfil.setBorderPainted(false);
			btnPerfil.setContentAreaFilled(false);
			btnPerfil.setFocusPainted(false);

			contentPane.add(btnPerfil, "cell 4 0,growx,aligny center");
			
			JLabel lblNewLabel_1 = new JLabel("");
			contentPane.add(lblNewLabel_1, "cell 4 0");
		}
	
	}
