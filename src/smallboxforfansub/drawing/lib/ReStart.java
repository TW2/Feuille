/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.lib;

import smallboxforfansub.drawing.lib.IShape;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author The Wingate 2940
 */
public class ReStart implements IShape, Cloneable {
    
    //Les points de coordonnées 'origine' et 'dernier'.
    java.awt.Point origin, last;
    //Les variables suivantes avertissent d'une chagement de position en cours
    //sur le point 'origine' (ochange) ou le point dernier (lchange).
    boolean oChange = false; boolean lChange = false;
    //marked sert à signalé un changement de position de façon globale.
    boolean marked = false;
    private Thickness thickness = Thickness.Big;
    //Un "point" est défini par un rectangle sur le dessin et virtuellement;
    //ce rectangle sert de conteneur de coordonnées.
    //Les coordonnées du centre du rectangle étant les coordonnées réelles.
    private Rectangle rect = new Rectangle();
    
    private boolean inSelection = false, firstInSelection = false;
    
    /** Création d'un déplacement. */
    public ReStart(){
        //nothing
    }

    /** Création d'un déplacement avec coordonnées. */
    public ReStart(int originX1, int originY1, int lastX2, int lastY2){
        rect.setRect(
                lastX2-thickness.getThickness()/2,
                lastY2-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        origin = new java.awt.Point(originX1,originY1);
        last = new java.awt.Point(lastX2, lastY2);
    }

    public enum Thickness{
        Big(10),Large(8),Medium(6),Small(4);

        private int thick;

        Thickness(int thick){
            this.thick = thick;
        }

        public int getThickness(){
            return thick;
        }
    }

    public void updateThickness(Thickness thickness){
        this.thickness = thickness;
        rect.setRect(
                origin.x-thickness.getThickness()/2,
                origin.y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
    }

    @Override
    public String getCommand() {
        return "m "+(int)last.getX()+" "+(int)last.getY();
    }

    @Override
    public void setOriginPoint(int x, int y) {
        origin = new java.awt.Point(x,y);
    }

    @Override
    public Point getOriginPoint() {
        return origin;
    }

    @Override
    public void setLastPoint(int x, int y) {
        last = new java.awt.Point(x, y);
    }

    @Override
    public Point getLastPoint() {
        return last;
    }

    @Override
    public void setMarked(boolean b) {
        marked = b;
    }

    @Override
    public boolean getMarked() {
        return marked;
    }
    
    /** Renvoie <b>true</b> si la coordonnée est concernée par un changement. */
    public boolean isPointisinRectangle(java.awt.Point p){
        return rect.contains(p);
    }
    
    /** Renvoie <b>true</b> si une extrémité du "move" est concernée par
     * un changement et définit lequel dans l'élément "move". */
    public boolean isPointisNear(java.awt.Point p){
        int xp = (int)p.getX(); int yp = (int)p.getY();
        Rectangle rect2 = new Rectangle(
                xp-thickness.getThickness()/2,
                yp-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        if(rect2.contains(origin)){oChange=true; return true;}
        if(rect2.contains(last)){lChange=true; return true;}
        return false;
    }

    /** Met à jour la position du point concerné. */
    public void updatePointPosition(java.awt.Point p){
        int x = (int)p.getX();
        int y = (int)p.getY();
        rect.setRect(
                x-thickness.getThickness()/2,
                y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        origin = p;
        last = p;
//        if(oChange==true){origin = p;}
//        if(lChange==true){last = p;}
    }

    /** Retourne à la configuration par défaut. */
    public void updatehasEnded(){
        oChange=false;
        lChange=false;
    }
    
    @Override
    public Object clone() {
        Object o = null;
    	try {
      		// On récupère l'instance à renvoyer par l'appel de la 
      		// méthode super.clone()
      		o = super.clone();
    	} catch(CloneNotSupportedException cnse) {
      		// Ne devrait jamais arriver car nous implémentons 
      		// l'interface Cloneable
      		cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;  	
    }
    
    @Override
    public void setInSelection(boolean b){
        inSelection = b;
    }
    
    @Override
    public boolean isInSelection(){
        return inSelection;
    }
    
    public boolean hasCoordinates(int x, int y){
        if(x == last.x && y == last.y){
            return true;
        }
        return false;
    }
    
    public boolean hasCoordinates(java.awt.Point p){
        if(p.x == last.x && p.y == last.y){
            return true;
        }
        return false;
    }
    
    @Override
    public void setFirstInSelection(boolean b){
        firstInSelection = b;
    }
    
    @Override
    public boolean isFirstInSelection(){
        return firstInSelection;
    }
}
