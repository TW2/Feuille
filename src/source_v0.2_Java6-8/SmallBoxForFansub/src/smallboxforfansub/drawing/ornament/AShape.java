/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.ornament;

import java.awt.Rectangle;

/**
 *
 * @author The Wingate 2940
 */
public abstract class AShape implements IShape {
    
    //Les points de coordonnées 'origine' et 'dernier'.
    protected java.awt.Point originPoint = null;
    protected java.awt.Point lastPoint = null;
    //Les variables suivantes avertissent d'un changement de position en cours
    //sur le point 'origine' (ochange) ou le point dernier (lchange).
    protected boolean oChange = false; protected boolean lChange = false;
    //marked sert à signalé un changement de position de façon globale.
    protected boolean marked = false;
    //Montre la grosseur en fonction du zoom.
    Thickness thickness = Thickness.Big;
    //Sauvegarde le temps de la phase.
    protected String duration = "0";
        
    public AShape(){
        
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
    }
    
    @Override
    public void setOriginPoint(int x, int y) {
        originPoint = new java.awt.Point(x, y);
    }

    @Override
    public java.awt.Point getOriginPoint() {
        return originPoint;
    }

    @Override
    public void setLastPoint(int x, int y) {
        lastPoint = new java.awt.Point(x, y);
    }

    @Override
    public java.awt.Point getLastPoint() {
        return lastPoint;
    }
    
    /** Renvoie <b>true</b> si une extrémité de l'objet est concernée par
     * un changement et définit lequel dans l'élément objet. */
    public boolean isPointisNear(java.awt.Point p){
        int xp = (int)p.getX(); int yp = (int)p.getY();
        Rectangle rect = new Rectangle(
                xp-thickness.getThickness()/2,
                yp-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        if(rect.contains(originPoint)){oChange=true; return true;}
        if(rect.contains(lastPoint)){lChange=true; return true;}
        return false;
    }
    
    /** Met à jour la position du point concerné. */
    public void updatePointPosition(java.awt.Point p){
        if(oChange==true){originPoint = p;}
        if(lChange==true){lastPoint = p;}
    }

    /** Retourne à la configuration par défaut. */
    public void updatehasEnded(){
        oChange=false;
        lChange=false;
    }

    @Override
    public void setMarked(boolean b) {
        marked = b;
    }

    @Override
    public boolean getMarked() {
        return marked;
    }

    @Override
    public void setDuration(String dur) {
        duration = dur;
    }

    @Override
    public String getDuration() {
        return duration;
    }
}
