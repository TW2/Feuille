package feuille.module.editor;

import javax.swing.*;
import java.awt.*;

public class ColorButton extends JButton {

    private Color color;
    private int alpha;

    public ColorButton(Color color) {
        this.color = color;
        this.alpha = 255;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.fillRect(2, 2, getWidth()-4, getHeight()-4);

        g.setColor(color);
        g.fillRect(2, 2, getWidth()/2-1, getHeight()-4);

        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        g.fillRect(getWidth()/2+1, 2, getWidth()-4, getHeight()-4);

        g.setColor(Color.black);
        g.drawRect(2, 2, getWidth()-4, getHeight()-4);
    }
}
