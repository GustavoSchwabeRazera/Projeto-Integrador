package View;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanelLogin extends JPanel {

    private Image imagem;

    public ImagePanelLogin() {
        imagem = new ImageIcon(
                ImagePanelLogin.class.getResource("/imagens/Rectangle.png"))
                .getImage();

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);

        g2.dispose();

        super.paintComponent(g);
    }
}