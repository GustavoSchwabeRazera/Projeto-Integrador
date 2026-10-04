package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class RoundedButton extends JButton {

    private static final long serialVersionUID = 1L;

    public RoundedButton(String texto) {
        super(texto);

        setForeground(Color.WHITE);

        setBackground(new Color(10, 86, 27));

        setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                16
            )
        );

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
            (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(
            new Color(10, 86, 27)
        );

        g2.fillRoundRect(
            0,
            0,
            getWidth(),
            getHeight(),
            20,
            20
        );

        g2.dispose();

        super.paintComponent(g);
    }
}