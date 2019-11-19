/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.drawing.lib;

import java.awt.Rectangle;
import java.awt.geom.Point2D;



/**
 * Cette classe définit un élément de type "bézier" comme étant un dérivé de
 * "forme" en implementant les méthodes demandées par "forme". Un "bézier" se
 * compose de la coordonnée 'origine' donnant la point de début et de la
 * coordonnée 'dernier' donnant le bout du "bézier", ainsi que de deux "points
 * de contrôle" afin de créer la courbe.
 * @author The Wingate 2940
 */
public class Bezier implements IShape, Cloneable {

    //Les points de coordonnées 'origine' et 'dernier'.
    java.awt.Point origin, last;
    //marked sert à signalé un changement de position de façon globale.
    boolean marked = false;
    //Les variables suivantes avertissent d'une chagement de position en cours
    //sur le point 'origine' (ochange) ou le point dernier (lchange).
    boolean oChange = false; boolean lChange = false;
    //Enfin voici les deux "points de contrôle" pour créer la courbe.
    ControlPoint c1 = null;
    ControlPoint c2 = null;
    private Thickness thickness = Thickness.Big;
    
    private boolean inSelection = false, firstInSelection = false;

    /** Crée un bézier */
    public Bezier(){
        //nothing
    }

    /** Crée un bézier en paramétrant tout. */
    public Bezier(int originX1, int originY1,
            int controlX2, int controlY2,
            int controlX3, int controlY3,
            int lastX4, int lastY4){
        origin = new java.awt.Point(originX1,originY1);
        c1 = new ControlPoint(controlX2, controlY2);
        c2 = new ControlPoint(controlX3,controlY3);
        last = new java.awt.Point(lastX4,lastY4);
    }

    /** Crée un bézier en paramétrant 'origine' et 'dernier', la méthode se
     * chargeant de configurer les "points de contrôle". */
    public Bezier(int originX1, int originY1,
            int lastX4, int lastY4){
        origin = new java.awt.Point(originX1,originY1);
        last = new java.awt.Point(lastX4,lastY4);

        int xdiff = (int)last.getX() - (int)origin.getX();
        int ydiff = (int)last.getY() - (int)origin.getY();

        int x1_3 = (int)origin.getX() + xdiff/3;
        int x2_3 = (int)origin.getX() + xdiff*2/3;
        int y1_3 = (int)origin.getY() + ydiff/3;
        int y2_3 = (int)origin.getY() + ydiff*2/3;
        
        c1 = new ControlPoint(x1_3, y1_3);
        c2 = new ControlPoint(x2_3,y2_3);
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

    /** Donne la commande ASS spécifique à la "ligne". */
    @Override
    public String getCommand() {
        return "b "+(int)c1.getOriginPoint().getX()+" "+(int)c1.getOriginPoint().getY()+
                " "+(int)c2.getOriginPoint().getX()+" "+(int)c2.getOriginPoint().getY()+
                " "+(int)last.getX()+" "+(int)last.getY();
    }

    /** Définit le point de 'origine' */
    @Override
    public void setOriginPoint(int x, int y) {
        origin = new java.awt.Point(x,y);
    }

    /** Obtient le point de 'origine' */
    @Override
    public java.awt.Point getOriginPoint() {
        return origin;
    }

    /** Définit le point de 'dernier' */
    @Override
    public void setLastPoint(int x, int y) {
        last = new java.awt.Point(x,y);
    }

    /** Obtient le point de 'dernier' */
    @Override
    public java.awt.Point getLastPoint() {
        return last;
    }

    /** Définit le "point de contrôle" n°1 */
    public void setControl1Point(int x1, int y1){
        c1 = new ControlPoint(x1, y1);
    }

    /** Définit le "point de contrôle" n°2 */
    public void setControl2Point(int x2, int y2){
        c2 = new ControlPoint(x2, y2);
    }

    /** Retourne les coordonnées du "point de contrôle" n°1 */
    public Point2D getControl1Point(){
        return c1.getOriginPoint();
    }

    /** Retourne les coordonnées du "point de contrôle" n°2 */
    public Point2D getControl2Point(){
        return c2.getOriginPoint();
    }

    /** Marque l'élément "bézier" comme 'en train de changer. */
    @Override
    public void setMarked(boolean b) {
        marked = b;
    }

    /** Obtient le status de changement de l'élément "bézier". */
    @Override
    public boolean getMarked() {
        return marked;
    }

    /** Définit le "point de contrôle" n°1 */
    public void setControl1(ControlPoint c1){
        this.c1 = c1;
    }

    /** Obtient le "point de contrôle" n°1 */
    public ControlPoint getControl1(){
        return c1;
    }

    /** Définit le "point de contrôle" n°2 */
    public void setControl2(ControlPoint c2){
        this.c2 = c2;
    }

    /** Obtient le "point de contrôle" n°2 */
    public ControlPoint getControl2(){
        return c2;
    }

    /** Renvoie <b>true</b> si une extrémité de la "ligne" est concernée par
     * un changement et définit lequel dans l'élément "ligne". */
    public boolean isPointisNear(java.awt.Point p){
        int xp = (int)p.getX(); int yp = (int)p.getY();
        Rectangle rect = new Rectangle(
                xp-thickness.getThickness()/2,
                yp-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        if(rect.contains(origin)){oChange=true; return true;}
        if(rect.contains(last)){lChange=true; return true;}
        return false;
    }

    /** Met à jour la position du point concerné. */
    public void updatePointPosition(java.awt.Point p){
        if(oChange==true){origin = p;}
        if(lChange==true){last = p;}
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
    
    /**
     * <p><b>Crée une courbe Bézier cubique à partir de points venant d'une
     * quadratique selon la formule :</b><br />
     * <code>CP0 = QP0<br />CP3 = QP2<br />
     * CP1 = QP0 + 2/3 *(QP1-QP0)<br />
     * CP2 = QP2 + 2/3 *(QP1-QP2)</code><br />
     * où CP signifie cubic point et QP signifie quad point.<br />
     * Voir aussi : http://fontforge.org/bezier.html
     * </p>
     */
    public static Bezier createCubicFromQuad(int QP0x, int QP0y,
            int QP1x, int QP1y, int QP2x, int QP2y){
        int CP0x = QP0x;
        int CP0y = QP0y;
        int CP1x = QP0x + (int)(2f/3*(QP1x-QP0x));
        int CP1y = QP0y + (int)(2f/3*(QP1y-QP0y));
        int CP2x = QP2x + (int)(2f/3*(QP1x-QP2x));
        int CP2y = QP2y + (int)(2f/3*(QP1y-QP2y));
        int CP3x = QP2x;
        int CP3y = QP2y;
        return new Bezier(CP0x, CP0y, CP1x, CP1y, CP2x, CP2y, CP3x, CP3y);
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
