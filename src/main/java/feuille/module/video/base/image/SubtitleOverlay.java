package feuille.module.video.base.image;

import feuille.util.DrawColor;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class SubtitleOverlay extends JComponent {

    private volatile BufferedImage subtitleImage;

    public SubtitleOverlay() {
        setOpaque(false);
        setFocusable(false);
        setDoubleBuffered(true);
    }

    public void setSubtitleImage(BufferedImage image) {
        subtitleImage = image;
        repaint();
    }

    public void clearSubtitles() {
        subtitleImage = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage image = subtitleImage;
        if (image == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setComposite(AlphaComposite.SrcOver);

            // À activer si le composant ne correspond pas
            // exactement à la résolution de rendu libass.
            g2.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );

            double ratioX = getWidth() / (double) image.getWidth();
            double ratioY = getHeight() / (double) image.getHeight();
            double ratio = Math.min(ratioX, ratioY);
            int w = (int) (ratio * image.getWidth());
            int h = (int) (ratio * image.getHeight());
            int x = (getWidth() - w) / 2;
            int y = (getHeight() - h) / 2;

            g2.drawImage(image, x, y, w, h, null);
        } finally {
            g2.dispose();
        }
    }
}
