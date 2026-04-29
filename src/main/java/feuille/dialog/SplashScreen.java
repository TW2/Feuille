package feuille.dialog;

import feuille.util.Loader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SplashScreen extends JFrame {

    public SplashScreen(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if(isAlwaysOnTopSupported()) setAlwaysOnTop(true);
        setUndecorated(true);
        setSize(800, 300);
        setLocationRelativeTo(null);

        ImageIcon leaf = Loader.fromResource("/images/Feuille2026.png", 800, 300);

        BufferedImage img = new BufferedImage(800, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(leaf.getImage(), 0, 0, null);
        g.setColor(Color.black);
        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String curYear = now.format(formatter);
        String text = String.format("%s ~%d-%s", "AssFxMaker-Feuille GNU/GPLv3", 2007, curYear);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(text);
        g.drawString(text, 800 - w - 10, 20);
        g.dispose();

        JLabel lbl = new JLabel(new ImageIcon(img));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(lbl, BorderLayout.CENTER);

        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                dispose();
            }
        });
    }
}
