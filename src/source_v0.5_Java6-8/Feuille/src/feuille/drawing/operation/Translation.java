/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.operation;

import java.util.ArrayList;
import java.util.List;
import feuille.drawing.lib.BSpline;
import feuille.drawing.lib.Bezier;
import feuille.drawing.lib.ControlPoint;
import feuille.drawing.lib.IShape;
import feuille.drawing.lib.Line;
import feuille.drawing.lib.Move;
import feuille.drawing.lib.Point;
import feuille.drawing.lib.ReStart;

/**
 *
 * @author The Wingate 2940
 */
public class Translation {
    
    private java.awt.Point T = null;
    private boolean set = false;
    private double dx = 0d, dy = 0d;
    private List<IShape> mShapes = new ArrayList<IShape>();
    
    public Translation(){
        
    }
    
    public Translation(java.awt.Point T){
        this.T = T;
        if(T != null){
            set = true;
        }
    }
    
    public void clear(){
        T = null;
        set = false;
        dx = 0d;
        dy = 0d;
        mShapes.clear();
    }
    
    public void setTranslation(java.awt.Point T){
        this.T = T;
        if(T != null){
            set = true;
        }
    }
    
    public boolean isSet(){
        return set;
    }
    
    public int getX(){
        return T.x;
    }
    
    public int getY(){
        return T.y;
    }
    
    public void setDistance(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    public double getTX(){
        return dx;
    }
    
    public double getTY(){
        return dy;
    }
    
    public double getDX(){
        return dx-T.getX();
    }
    
    public double getDY(){
        return dy-T.getY();
    }
    
    public void setTranslatonPreview(List<IShape> pshapes){        
        mShapes.clear();
        java.awt.Point O, L; int xa, ya;
        for(IShape s : pshapes){
            if(s instanceof ReStart){
                ReStart m = (ReStart)s;
                xa = m.getOriginPoint().x; ya = m.getOriginPoint().y;
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = m.getLastPoint().x; ya = m.getLastPoint().y;
                L = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new ReStart(O.x, O.y, L.x, L.y));
            }else if(s instanceof Move){
                Move n = (Move)s;
                xa = n.getOriginPoint().x; ya = n.getOriginPoint().y;
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = n.getLastPoint().x; ya = n.getLastPoint().y;
                L = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new Move(O.x, O.y, L.x, L.y));
            }else if(s instanceof Point){
                Point p = (Point)s;
                xa = p.getOriginPoint().x; ya = p.getOriginPoint().y;
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new Point(O.x, O.y));
            }else if(s instanceof Line){
                Line l = (Line)s;
                xa = l.getOriginPoint().x; ya = l.getOriginPoint().y;
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = l.getLastPoint().x; ya = l.getLastPoint().y;
                L = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new Line(O.x, O.y, L.x, L.y));
            }else if(s instanceof Bezier){
                Bezier b = (Bezier)s;
                xa = b.getOriginPoint().x; ya = b.getOriginPoint().y;
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = b.getLastPoint().x; ya = b.getLastPoint().y;
                L = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = b.getControl1().getOriginPoint().x; ya = b.getControl1().getOriginPoint().y;
                java.awt.Point CP1 = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = b.getControl2().getOriginPoint().x; ya = b.getControl2().getOriginPoint().y;
                java.awt.Point CP2 = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new Bezier(O.x, O.y, CP1.x, CP1.y, CP2.x, CP2.y, L.x, L.y));
            }else if(s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                java.awt.Point CP1 = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new ControlPoint(CP1.x, CP1.y));
            }else if(s instanceof BSpline){
                BSpline bs = (BSpline)s;                
                xa = (int)bs.getOriginPoint().getX();
                ya = (int)bs.getOriginPoint().getY();
                java.awt.Point P = translateWithPoint(getDX(), getDY(), xa, ya);
                BSpline nbs = new BSpline(P.x, P.y);
                for(ControlPoint cp : bs.getControlPoints()){
                    xa = (int)cp.getOriginPoint().getX();
                    ya = (int)cp.getOriginPoint().getY();
                    java.awt.Point CP1 = translateWithPoint(getDX(), getDY(), xa, ya);
                    nbs.addPoint(CP1.x, CP1.y);
                }
                if(bs.isNextExist()){
                    xa = (int)bs.getNextPoint().getX();
                    ya = (int)bs.getNextPoint().getY();
                    java.awt.Point NX = translateWithPoint(getDX(), getDY(), xa, ya);
                    nbs.setNextPoint(NX.x, NX.y);
                }
                mShapes.add(nbs);
            }
        }
    }
    
    public List<IShape> getPreviewShapes(){
        return mShapes;
    }
    
    private java.awt.Point translateWithPoint(double rdx, double rdy, int xa, int ya){
        java.awt.Point P = new java.awt.Point(xa, ya);
        java.awt.Point Pprime = P;
        double xPprime = P.getX() + rdx;
        double yPprime = P.getY() + rdy;
        Pprime.setLocation(xPprime, yPprime);
        return Pprime;
    }
}
