package View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

// Balão de conversa no mesmo estilo dos outros ícones da tela inicial.
// Com mensagens não lidas, aparece o número em vermelho no canto.
final class IconeChat {

    private IconeChat() {
    }

    static ImageIcon criar(int tamanho, int naoLidas) {

        BufferedImage imagem = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = imagem.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        desenharBalao(g2, tamanho);

        if (naoLidas > 0) {
            desenharContador(g2, tamanho, naoLidas);
        }

        g2.dispose();

        return new ImageIcon(imagem);
    }


    private static void desenharBalao(Graphics2D g2, int t) {

        float espessura = t * 0.1f;
        double margem = t * 0.1;
        double altura = t * 0.62;

        Area balao = new Area(new RoundRectangle2D.Double(
                margem, margem, t - 2 * margem, altura, t * 0.3, t * 0.3));

        // Pontinha do balão
        double base = margem + altura;
        balao.add(new Area(new Polygon(
                new int[] { (int) (t * 0.26), (int) (t * 0.26), (int) (t * 0.5) },
                new int[] { (int) (base - t * 0.05), (int) (t * 0.93), (int) (base - t * 0.05) },
                3)));

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(espessura, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(balao);

        // Três pontinhos
        int raio = Math.max(2, Math.round(t * 0.11f));
        int y = (int) (margem + altura / 2 - raio / 2.0);

        for (double centro : new double[] { 0.3, 0.5, 0.7 }) {
            g2.fillOval((int) (t * centro - raio / 2.0), y, raio, raio);
        }
    }


    private static void desenharContador(Graphics2D g2, int t, int naoLidas) {

        String texto = naoLidas > 9 ? "9+" : String.valueOf(naoLidas);
        int diametro = (int) (t * 0.46);
        int x = t - diametro;

        g2.setColor(new Color(190, 30, 30));
        g2.fillOval(x, 0, diametro, diametro);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Tahoma", Font.BOLD, (int) (diametro * 0.62)));

        FontMetrics fm = g2.getFontMetrics();

        g2.drawString(
                texto,
                x + (diametro - fm.stringWidth(texto)) / 2,
                (diametro - fm.getHeight()) / 2 + fm.getAscent());
    }
}
