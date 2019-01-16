/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.ornament;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Ornament Main Move Line pour les mouvements généraux
 * @author The Wingate 2940
 */
public class OrnMMLine extends AShape {
    
    public OrnMMLine(){
        
    }
    
    public OrnMMLine(int originX1, int originY1, int lastX2, int lastY2){
        originPoint = new java.awt.Point(originX1,originY1);
        lastPoint = new java.awt.Point(lastX2, lastY2);
    }

    // http://fr.wikipedia.org/wiki/Fonction_affine
    // On cherche à connaitre y en fonction de ax + b
    // Il nous faut d'abord retrouver la relation et définir a et b.
    // a = (y2-y1)/(x2-x1)
    // b = (x2.y1-x1.y2)/(x2-x1)
    
    private double getA(){
        double x1 = originPoint.getX();     double x2 = lastPoint.getX();
        double y1 = originPoint.getY();     double y2 = lastPoint.getY();        
        return (y2-y1)/(x2-x1);
    }
    
    private double getB(){
        double x1 = originPoint.getX();     double x2 = lastPoint.getX();
        double y1 = originPoint.getY();     double y2 = lastPoint.getY();
        return (x2*y1-x1*y2)/(x2-x1);
    }

    @Override
    public double getY(double x) {
        //y = ax + b pour une droite
//        return getA()*x + getB(); //<< mauvais résultats
//        return getA()*x + originPoint.getY(); //<< bons résultats mais dans notre cas on veut que la droite à y=0
        return getA()*x;
    }

    @Override
    public void draw(Graphics2D g2d, Color c) {
        Color oldc = g2d.getColor();
        g2d.setColor(c);
        // On dessine la ligne
        g2d.drawLine(originPoint.x, originPoint.y, lastPoint.x, lastPoint.y);
        g2d.setColor(oldc);
    }
    
    @Override
    public String toString(){
        return "Line";
    }
    
}
