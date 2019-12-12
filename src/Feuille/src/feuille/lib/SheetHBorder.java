/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.lib;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * Règle horizontale pour le composant Sheet
 * @author The Wingate 2940
 */
public class SheetHBorder extends JPanel {
    
    private int scale = 1, width = 0;
    
    public SheetHBorder(int width){
        this.width = width;
        setLayout(null);
        setPreferredSize(new Dimension(width, 40));
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
        g2d.drawLine((getWidth()/scale)/2, 0, (getWidth()/scale)/2, getHeight()); //vertical
        g2d.setColor(new Color(234,216,255));
        g2d.setStroke(new BasicStroke(1f));
        int i = getWidth()/2;
        while(i>=0){ i=i-25; g2d.drawLine(i, 0, i, getHeight()); }
        i = getWidth()/2;
        while(i<=getWidth()){ i=i+25; g2d.drawLine(i, 0, i, getHeight()); }
        
        //Dessine les nombres aux bords ; ils correspondent aux coordonnées.
        g2d.setColor(Color.darkGray);
        i = (getWidth()/scale)/2;
        while(i>=0){ i=i-50; g2d.drawString(Integer.toString(i-(getWidth()/scale)/2), i-10, 10); }
        i = (getWidth()/scale)/2;
        while(i<=getWidth()){ i=i+50; g2d.drawString(Integer.toString(i-(getWidth()/scale)/2), i-10, 10); }
    }
    
    public void setScaleXY(int scale){
        this.scale = scale;
        setPreferredSize(new Dimension(width*scale, 40));
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
