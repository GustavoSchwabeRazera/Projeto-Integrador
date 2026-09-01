package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Historico extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Historico frame = new Historico();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static class RoundedPanel extends JPanel {
	    private static final long serialVersionUID = 1L;
	    private final int radius;

	    public RoundedPanel(LayoutManager layout, int radius) {
	        super(layout);
	        this.radius = radius;
	        setOpaque(false); // essencial: deixa o fundo "transparente" pro Swing
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

	/**
	 * Create the frame.
	 */
	public Historico() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1920, 1080);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(10, 86, 27));
		contentPane.setBackground(new Color(175, 244, 198));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[307.00,grow][300.00,grow][grow][338.00,grow][322.00,grow]", "[grow][grow][grow][grow][grow]"));
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setDefaultCapable(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setIcon(new ImageIcon(Historico.class.getResource("/imagens/casa 1.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(btnNewButton, "cell 0 0,alignx left");
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Historico.class.getResource("/imagens/LogoPequena.png")));
		contentPane.add(lblNewLabel, "cell 1 0 3 1,alignx center");
		
		JLabel lblNewLabel_1 = new JLabel("   Histórico de Emprésimos");
		lblNewLabel_1.setForeground(new Color(10, 86, 27));
		lblNewLabel_1.setBackground(new Color(10, 86, 27));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 32));
		contentPane.add(lblNewLabel_1, "cell 1 1 3 1,alignx center,aligny center");
		
		RoundedPanel panel = new RoundedPanel(new MigLayout(), 30); // 30 = raio das bordas
		panel.setBackground(new Color(10, 86, 27));
		contentPane.add(panel, "cell 1 2 3 3,grow");

		
	}

}
