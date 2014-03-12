/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.ornament;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Voici l'interface qui définit ce qu'une forme doit avoir.
 * @author The Wingate 2940
 */
public interface IShape {
    
    /** Définit le point de 'origine' */
    public void setOriginPoint(int x, int y);

    /** Obtient le point de 'origine' */
    public java.awt.Point getOriginPoint();

    /** Définit le point de 'dernier' */
    public void setLastPoint(int x, int y);

    /** Obtient le point de 'dernier' */
    public java.awt.Point getLastPoint();

    /** Marque l'élément comme 'en train de changer'. */
    public void setMarked(boolean b);

    /** Obtient le status de changement de l'élément. */
    public boolean getMarked();
    
    /** Obtient l'ordonné de y à l'instant t */
    public double getY(double x);
    
    /** Se dessine comme ça */
    public void draw(Graphics2D g2d, Color c);
    
    /** Temps de la phase */
    public void setDuration(String dur);
    
    /** Temps de la phase */
    public String getDuration();
    
}
