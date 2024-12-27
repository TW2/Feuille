/*
 * Copyright (C) 2024 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wingate.feuille.subs.ass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class AssView extends JPanel {
    
    private final AssRenderer renderer;
    private BufferedImage image;
    
    private Color backgroundColor;
    private Color squareColor;

    public AssView(ASS ass, int width, int height) {
        setDoubleBuffered(true);
        this.renderer = new AssRenderer(ass, new Dimension(width, height));
        image = null;
        
        backgroundColor = DrawColor.light_yellow.getColor();
        squareColor = DrawColor.yellow.getColor(.5f);
    }
    
    public void generate(double ms){
        image = renderer.getSubsAtTime(ms);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(squareColor);
        boolean pair = true;
        for(int y=0; y<getHeight(); y+=10){
            for(int x=0; x<getWidth(); x+=20){
                int xa = pair ? x : x + 10;
                g2d.fillRect(xa, y, 10, 10);
            }
            pair = !pair;
        }
        
        if(image != null){
            g2d.drawImage(image, 0, 0, null);
        }
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setSquareColor(Color squareColor) {
        this.squareColor = squareColor;
    }
    
    public void setSentence(String sentence, AssStyle style){
        renderer.getAss().getEvents().clear();
        renderer.getAss().getStyles().clear();
        style.getAssFont().setSize(42f);
        renderer.getAss().getStyles().add(style);
        AssEvent event = new AssEvent();
        event.setType(AssEvent.Type.Dialogue);
        event.getTranslations().getVersions().getFirst().setText(sentence);
        event.setStart(new AssTime(0d));
        event.setEnd(new AssTime(500d));
        event.setStyle(style);
        renderer.getAss().getEvents().add(event);
        repaint();
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            JFrame frm = new JFrame("Test");
            frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frm.setSize(1900, 600);
            frm.getContentPane().setLayout(new BorderLayout());

            ASS ass = new ASS();
            AssStyle style = new AssStyle();
            style.getAssFont().setSize(80f);
            style.getAssFont().setName("Essai");
            style.setOutline(30);
            ass.getStyles().add(style);
            AssEvent event = new AssEvent();
            event.setStart(new AssTime(0d));
            event.setEnd(new AssTime(500d));
            event.setType(AssEvent.Type.Dialogue);
            event.setStyle(style);
            event.getTranslations().getVersions().getFirst().setText("Un p{\\fsp60}ays{\\fsp0}age VERDOYANT et brumeux !");
            ass.getEvents().add(event);

            AssView view = new AssView(ass, 1900, 600);
            frm.getContentPane().add(view, BorderLayout.CENTER);
            frm.setLocationRelativeTo(null);
            frm.setVisible(true);
            
            view.generate(250d);
        });        
    }
}
