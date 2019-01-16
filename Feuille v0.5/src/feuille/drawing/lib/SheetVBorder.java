/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.lib;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * Règle verticale pour le composant Sheet
 * @author The Wingate 2940
 */
public class SheetVBorder extends JPanel {
    
    private int scale = 1, height = 0;
    
    public SheetVBorder(int height){
        this.height = height;
        setLayout(null);
        setPreferredSize(new Dimension(40, height));
        revalidate();
    }
    
    @Override
    public void paint(Graphics g){
        //Charge la classe Graphics2D pour pouvoir avoir accès à ses méthodes.
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.scale(scale, scale);
        
        //Définit et dessine les axes du milieu et le quadrillage.
        g2d.setColor(new Color(216,255,253));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(0, (getHeight()/scale)/2, getWidth(), (getHeight()/scale)/2); //horizontal
        g2d.setColor(new Color(234,216,255));
        g2d.setStroke(new BasicStroke(1f));
        int i = getHeight()/2;
        while(i>=0){ i=i-25; g2d.drawLine(0, i, getWidth(), i); }
        i = getHeight()/2;
        while(i<=getHeight()){ i=i+25; g2d.drawLine(0, i, getWidth(), i); }
        
        //Dessine les nombres aux bords ; ils correspondent aux coordonnées.
        g2d.setColor(Color.darkGray);
        g2d.rotate(Math.toRadians(-90));
        i = (getHeight()/scale)/2;
        while(i>=0){ i=i-50; g2d.drawString(Integer.toString((i-(getHeight()/scale)/2)), -i-10, 10); }
        i = (getHeight()/scale)/2;
        while(i<=getHeight()){ i=i+50; g2d.drawString(Integer.toString((i-(getHeight()/scale)/2)), -i-10, 10); }
        g2d.rotate(Math.toRadians(0));
    }
    
    public void setScaleXY(int scale){
        this.scale = scale;
        setPreferredSize(new Dimension(40, height*scale));
        revalidate();
        repaint();
    }

    public int getScaleXY(){
        return scale;
    }
    
    public void update(){
        repaint();
    }
}
