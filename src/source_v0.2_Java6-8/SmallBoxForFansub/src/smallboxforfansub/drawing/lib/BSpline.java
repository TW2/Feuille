/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.lib;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import smallboxforfansub.drawing.bspline.BSplineCurve;
import smallboxforfansub.drawing.bspline.BezierCurve;

/**
 *
 * @author The Wingate 2940
 */
public class BSpline implements IShape, Cloneable {
    
    //Les points de coordonnées 'origine' et 'dernier'.
    java.awt.Point origin, last, nc, next;
    //marked sert à signalé un changement de position de façon globale.
    boolean marked = false;
    //Les variables suivantes avertissent d'une chagement de position en cours
    //sur le point 'origine' (ochange) ou le point dernier (lchange).
    boolean oChange = false; boolean lChange = false;
    boolean cpChange = false; boolean nextChange;
    //Enfin voici les "points de contrôle" pour créer la courbe.
    List<ControlPoint> lcp = new ArrayList<ControlPoint>();
    private Thickness thickness = Thickness.Big;
    BSplineCurve bsc;
    ControlPoint uc = null; int ucChanged = 0;
    
    boolean isClosed = false;
    boolean isNextExist = false;
    
    private boolean inSelection = false, firstInSelection = false;
    
    public BSpline(){
        
    }
    
    /** Crée un objet b-spline en paramétrant le premier point. */
    public BSpline(int x0, int y0){
        origin = new java.awt.Point(x0, y0);
        bsc = new BSplineCurve(new java.awt.Point(x0, y0));
        lcp.add(new ControlPoint(x0, y0));
    }
    
    /** Crée une b-spline en paramétrant tout (n=x). */
    public BSpline(List<ControlPoint> list){
        lcp = list;
        origin = new java.awt.Point((int)lcp.get(0).getOriginPoint().getX(), (int)lcp.get(0).getOriginPoint().getY());
        last = new java.awt.Point((int)lcp.get(lcp.size()-1).getOriginPoint().getX(), (int)lcp.get(lcp.size()-1).getOriginPoint().getY());
    }
        
    public void addPoint(int x, int y){
        lcp.add(new ControlPoint(x, y));
        bsc.addControlPoint(new java.awt.Point(x,y));
        origin = new java.awt.Point((int)lcp.get(0).getOriginPoint().getX(), (int)lcp.get(0).getOriginPoint().getY());
        last = new java.awt.Point((int)lcp.get(lcp.size()-1).getOriginPoint().getX(), (int)lcp.get(lcp.size()-1).getOriginPoint().getY());
    }
    
    public void updatePoint(int x, int y){
        lcp.set(ucChanged, new ControlPoint(x, y));
        List<Point2D> lsp = new ArrayList<Point2D>();
        for(ControlPoint cp : lcp){
            Point2D p2d = new Point2D.Double(cp.getOriginPoint().getX(),cp.getOriginPoint().getY());
            lsp.add(p2d);
        }
        bsc.setControlPoints(lsp);
        origin = new java.awt.Point((int)lcp.get(0).getOriginPoint().getX(), (int)lcp.get(0).getOriginPoint().getY());
        last = new java.awt.Point((int)lcp.get(lcp.size()-1).getOriginPoint().getX(), (int)lcp.get(lcp.size()-1).getOriginPoint().getY());
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
    public String getCommand() {
        String line = "s";
        for(int i=0;i<lcp.size();i++){
            ControlPoint c = lcp.get(i);
            line = line+" "+(int)c.getOriginPoint().getX()+" "+(int)c.getOriginPoint().getY();
        }
        return line;
    }

    @Override
    public void setOriginPoint(int x, int y) {
        ControlPoint c1 = new ControlPoint(x, y);
        if(lcp.isEmpty()==false){
            lcp.set(0, c1);
        }else{
            lcp.add(c1);
        }
        
    }

    @Override
    public java.awt.Point getOriginPoint() {
        if(lcp.isEmpty()==false){
            return lcp.get(0).getOriginPoint();
        }else{
            return null;
        }
    }

    @Override
    public void setLastPoint(int x, int y) {
        ControlPoint c1 = new ControlPoint(x, y);
        if(lcp.isEmpty()==false && lcp.size()>3){
            lcp.set(lcp.size()-1, c1);
        }
    }

    @Override
    public java.awt.Point getLastPoint() {
        if(lcp.isEmpty()==false){
            return lcp.get(lcp.size()-1).getLastPoint();
        }else{
            return null;
        }
    }

    @Override
    public void setMarked(boolean b) {
        marked = b;
    }

    @Override
    public boolean getMarked() {
        return marked;
    }
    
    public java.awt.Point getLastUsedControlPoint() {
        return nc;
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
//        if(rect.contains(origin)){oChange=true; return true;}
//        if(rect.contains(last)){lChange=true; return true;}
        try{
            if(rect.contains(next)){nextChange=true;}else{nextChange=false;}
        }catch(NullPointerException npe){}        
        int index = 0;
        for(ControlPoint cp : this.getControlPoints()){
            uc = cp; ucChanged = index;
            if(rect.contains(cp.getOriginPoint())){cpChange=true; return true;}
            index += 1;
        }        
        return false;
    }

    /** Met à jour la position du point concerné. */
    public void updatePointPosition(java.awt.Point p){
        if(nextChange==true){next = p;}
        if(oChange==true){origin = p;}
        if(lChange==true){last = p;}
        if(cpChange==true){nc = p;}
    }

    /** Retourne à la configuration par défaut. */
    public void updatehasEnded(){
        nextChange=false;
        oChange=false;
        lChange=false;
        cpChange=false;
    }
    
    public boolean isNear(java.awt.Point o, java.awt.Point t){
        Rectangle rect = new Rectangle(
                (int)o.getX()-thickness.getThickness()/2,
                (int)o.getY()-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        if(rect.contains(t)){return true;}
        return false;
    }
    
    public void updateLastUsedControlPoint(){        
        for(ControlPoint cp : lcp){
            if(uc.equals(cp)){
                cp.setOriginPoint((int)nc.getX(), (int)nc.getY());
            }
        }
        updatePoint((int)nc.getX(), (int)nc.getY());
    }
    
    public BSplineCurve getBSplineCurve(){
        return bsc;
    }
    
    public List<ControlPoint> getControlPoints(){
        return lcp;
    }
    
    public List<Bezier> getAllBeziers(){
        List<BezierCurve> lbc = bsc.extractAllBezierCurves();
        List<Bezier> lb = new ArrayList<Bezier>();
        for(BezierCurve bc : lbc){
            Bezier b = new Bezier();
            List<Point2D> p = bc.getControlPoints();
            b.setOriginPoint((int)p.get(0).getX(), (int)p.get(0).getY());
            b.setControl1Point((int)p.get(1).getX(), (int)p.get(1).getY());
            b.setControl2Point((int)p.get(2).getX(), (int)p.get(2).getY());
            b.setLastPoint((int)p.get(3).getX(), (int)p.get(3).getY());
            lb.add(b);
        }
        return lb;
    }
    
    public boolean isClosed(){
        return isClosed;
    }
    
    public void setClosed(boolean b){
        isClosed = b;
    }
    
    public void setNextPoint(int x, int y){
        next = new java.awt.Point(x,y);
        setNextExist(true);
    }
    
    public java.awt.Point getNextPoint() {
        return next;
    }
    
    public boolean isNextExist(){
        return isNextExist;
    }
    
    public void setNextExist(boolean b){
        isNextExist = b;
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
    
    public void setupBSplineCurve(int x0, int y0){
        origin = new java.awt.Point(x0, y0);
        bsc = new BSplineCurve(new java.awt.Point(x0, y0));
        lcp.add(new ControlPoint(x0, y0));
        last = new java.awt.Point((int)lcp.get(lcp.size()-1).getOriginPoint().getX(), (int)lcp.get(lcp.size()-1).getOriginPoint().getY());
    }
    
    public boolean isBSplineCurveExists(){
        if(bsc!=null){
            return true;
        }
        return false;
    }
    
    //Fonction crée afin de corriger le bug du point en plus de la rotation
    public void removePointAt(int index){
        ControlPoint cp = lcp.get(index);
        bsc.removeControlPoint(cp.getOriginPoint());
        lcp.remove(index);        
    }
}
