/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.ornament;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author The Wingate 2940
 */
public class OrnPoint extends AShape {
    
    //Un "point" est défini par un rectangle sur le dessin et virtuellement;
    //ce rectangle sert de conteneur de coordonnées.
    //Les coordonnées du centre du rectangle étant les coordonnées réelles.
    private Rectangle rect = new Rectangle();
    
    /** Création d'un "point". */
    public OrnPoint(){
        // nothing
    }

    /** Création d'un "point" avec coordonnées. */
    public OrnPoint(int x, int y){
        rect.setRect(
                x-thickness.getThickness()/2,
                y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        originPoint = new java.awt.Point(x, y);
        lastPoint = new java.awt.Point(x, y);
    }
    
    @Override
    public void updateThickness(Thickness thickness){
        this.thickness = thickness;
        rect.setRect(
                originPoint.x-thickness.getThickness()/2,
                originPoint.y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
    }
    
    /** Renvoie <b>true</b> si la coordonnée est concernée par un changement. */
    public boolean isPointisinRectangle(java.awt.Point p){
        return rect.contains(p);
    }

    /** Met à jour la position du point concerné. */
    @Override
    public void updatePointPosition(java.awt.Point p){
        int x = (int)p.getX();
        int y = (int)p.getY();
        rect.setRect(
                x-thickness.getThickness()/2,
                y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        originPoint = p;
        lastPoint = p;
    }

    @Override
    public double getY(double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw(Graphics2D g2d, Color c) {
        Color oldc = g2d.getColor();
        g2d.setColor(c);
        // On dessine le point
        g2d.fillRect(
                originPoint.x-thickness.getThickness()/2,
                originPoint.y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        g2d.setColor(oldc);
    }
    
}
