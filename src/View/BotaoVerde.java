package View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

// Botão padrão do sistema: verde-escuro arredondado com texto branco.
// Fica um pouco mais claro quando o mouse passa por cima.
public class BotaoVerde extends JButton {

    private static final long serialVersionUID = 1L;

    private static final int ARCO = 22;

    // Tamanho padrão do botão "Voltar" que fica no canto das telas
    public BotaoVerde(String texto) {
        this(texto, 130, 46);
    }

    public BotaoVerde(String texto, int largura, int altura) {

        super(texto);

        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 20));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        Dimension tamanho = new Dimension(largura, altura);
        setPreferredSize(tamanho);
        setMinimumSize(tamanho);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean emCima = getModel().isRollover() || getModel().isPressed();
        g2.setColor(emCima ? Cores.VERDE_HOVER : Cores.VERDE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARCO, ARCO);

        // Contorno na cor do fundo: some no fundo claro, mas destaca
        // o botão quando ele fica em cima de um painel verde
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Cores.FUNDO);
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, ARCO, ARCO);

        g2.dispose();

        // Desenha o texto por cima do fundo
        super.paintComponent(g);
    }
}
