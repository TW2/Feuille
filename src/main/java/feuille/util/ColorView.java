package feuille.util;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;

public class ColorView extends JPanel {

    private Color color;
    private final JLabel lblTitle;
    private final JLabel fullColor;
    private final ColorPanel halfColor;

    public ColorView(String title, Color color) {
        this.color = color;
        lblTitle = new JLabel(title);
        fullColor = new JLabel("");
        halfColor = new ColorPanel();
        JPanel pColor = new JPanel(new GridLayout(1, 2));
        fullColor.setBackground(color);
        fullColor.setOpaque(true);
        fullColor.setPreferredSize(new Dimension(35, 26));
        fullColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                fullColorMouseClicked(e);
            }
        });
        pColor.add(fullColor);
        halfColor.setColor(color);
        halfColor.setPreferredSize(new Dimension(35, 26));
        halfColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                halfColorMouseClicked(e);
            }
        });
        pColor.add(halfColor);

        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.NORTH);
        add(pColor, BorderLayout.CENTER);
    }

    public ColorView(){
        this("RGBA Color", Color.red);
    }

    public void setTitle(String title){
        lblTitle.setText(title);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        fullColor.setBackground(color);
        halfColor.setColor(
                new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        color.getAlpha()
                )
        );
    }

    public void fullColorMouseClicked(MouseEvent e) {
        Color c = JColorChooser.showDialog(ColorView.this, "Choose Color", color);
        if (c != null) {
            color = c;
            fullColor.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 255));
            halfColor.setColor(c);
            fireColorViewEvent(new ColorViewEvent(c));
        }
    }

    public void halfColorMouseClicked(MouseEvent e) {
        Color c = JColorChooser.showDialog(ColorView.this, "Choose Color", color);
        if (c != null) {
            color = c;
            fullColor.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 255));
            halfColor.setColor(c);
            fireColorViewEvent(new ColorViewEvent(c));
        }
    }

    public static class ColorPanel extends JPanel {
        private Color color;

        public ColorPanel() {
            color = Color.red;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, color.getAlpha()/255f));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
            repaint();
        }
    }

    public interface ColorViewListener extends EventListener {
        void colorChanged(ColorViewEvent event);
    }

    public static class ColorViewEvent {
        private final Color color;

        public ColorViewEvent(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    private final EventListenerList listeners = new EventListenerList();

    public Object[] getListeners(){
        return listeners.getListenerList();
    }


    public void addColorViewListener(ColorViewListener listener){
        listeners.add(ColorViewListener.class, listener);
    }

    public void removeColorViewListener(ColorViewListener listener){
        listeners.remove(ColorViewListener.class, listener);
    }

    protected void fireColorViewEvent(ColorViewEvent event){
        for(Object o : getListeners()){
            if(o instanceof ColorViewListener listen){
                listen.colorChanged(event);
                break;
            }
        }
    }

}
