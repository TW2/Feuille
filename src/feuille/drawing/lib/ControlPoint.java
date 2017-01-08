/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.drawing.lib;

import java.awt.Rectangle;

/**
 * Cette classe sert à définir les "points de contrôle" qui sont utilisés par
 * la classe Bézier afin de créer une courbe paramétrique de type bézier.
 * @author The Wingate 2940
 */
public class ControlPoint implements IShape, Cloneable {

    //Un "point de contrôle" est défini par un cercle sur le dessin et par un
    //rectangle virtuellement; ce rectangle sert de conteneur de coordonnées.
    //Les coordonnées du centre du rectangle étant les coordonnées réelles.
    private Rectangle rect = new Rectangle();
    //Les points de coordonnées 'origine' et 'dernier'.
    java.awt.Point origin, last;
    //marked sert à signalé un changement de position de façon globale.
    boolean marked = false;
    private Thickness thickness = Thickness.Big;
    
    private boolean inSelection = false, firstInSelection = false;

    /** Création d'un "point de contrôle". */
    public ControlPoint(){
        // nothing
    }

    /** Création d'un "point de contrôle" avec coordonnées. */
    public ControlPoint(int x, int y){
        rect.setRect(
                x-thickness.getThickness()/2,
                y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        origin = new java.awt.Point(x, y);
        last = new java.awt.Point(x, y);
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

    /** Donne la commande ASS spécifique à la "ligne". */
    @Override
    public String getCommand() {
        return "";
    }

    /** Définit le point de 'origine' */
    @Override
    public void setOriginPoint(int x, int y) {
        origin = new java.awt.Point(x, y);
    }

    /** Obtient le point de 'origine' */
    @Override
    public java.awt.Point getOriginPoint() {
        return origin;
    }

    /** Définit le point de 'dernier' */
    @Override
    public void setLastPoint(int x, int y) {
        last = new java.awt.Point(x, y);
    }

    /** Obtient le point de 'dernier' */
    @Override
    public java.awt.Point getLastPoint() {
        return last;
    }

    /** Marque l'élément "point de contrôle" comme 'en train de changer. */
    @Override
    public void setMarked(boolean b) {
        marked = b;
    }

    /** Obtient le status de changement de l'élément "point de contrôle". */
    @Override
    public boolean getMarked() {
        return marked;
    }

    /** Renvoie <b>true</b> si la coordonnée est concernée par un changement. */
    public boolean isPointisinRectangle(java.awt.Point p){
        return rect.contains(p);
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
    
    @Override
    public void setFirstInSelection(boolean b){
        firstInSelection = b;
    }
    
    @Override
    public boolean isFirstInSelection(){
        return firstInSelection;
    }
}
