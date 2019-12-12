/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.lib;

import feuille.grafx.AShape;
import feuille.grafx.GCubic;
import feuille.grafx.GLine;
import feuille.grafx.GMoveM;
import feuille.grafx.GMoveN;
import feuille.grafx.GSpline;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author The Wingate 2940
 */
public class Translation {
    
    private Point2D T = null;
    private boolean set = false;
    private double dx = 0d, dy = 0d;
    private List<AShape> mShapes = new ArrayList<>();
    
    public Translation(){
        
    }
    
    public Translation(Point2D T){
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
    
    public void setTranslation(Point2D T){
        this.T = T;
        if(T != null){
            set = true;
        }
    }
    
    public boolean isSet(){
        return set;
    }
    
    public double getX(){
        return T.getX();
    }
    
    public double getY(){
        return T.getY();
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
    
    public void setTranslatonPreview(List<AShape> pshapes){        
        mShapes.clear();
        Point2D O, L; double xa, ya;
        for(AShape s : pshapes){
            if(s instanceof GMoveM){ // ReStart
                GMoveM m = (GMoveM)s;
                xa = m.getStartPoint().getX(); ya = m.getStartPoint().getY();
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = m.getEndPoint().getX(); ya = m.getEndPoint().getY();
                L = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new GMoveM(O, L, m.getUniqueID()));
            }else if(s instanceof GMoveN){
                GMoveN n = (GMoveN)s;
                xa = n.getStartPoint().getX(); ya = n.getStartPoint().getY();
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = n.getEndPoint().getX(); ya = n.getEndPoint().getY();
                L = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new GMoveN(O, L, n.getUniqueID()));
            }else if(s instanceof GLine){
                GLine l = (GLine)s;
                xa = l.getStartPoint().getX(); ya = l.getStartPoint().getY();
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = l.getEndPoint().getX(); ya = l.getEndPoint().getY();
                L = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new GLine(O, L, l.getUniqueID()));
            }else if(s instanceof GCubic){
                GCubic b = (GCubic)s;
                xa = b.getStartPoint().getX(); ya = b.getStartPoint().getY();
                O = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = b.getEndPoint().getX(); ya = b.getEndPoint().getY();
                L = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = b.getControlPoints().get(0).getX(); ya = b.getControlPoints().get(0).getY();
                Point2D CP1 = translateWithPoint(getDX(), getDY(), xa, ya);
                xa = b.getControlPoints().get(1).getX(); ya = b.getControlPoints().get(1).getY();
                Point2D CP2 = translateWithPoint(getDX(), getDY(), xa, ya);
                mShapes.add(new GCubic(O, CP1, CP2, L, b.getUniqueID()));
            }else if(s instanceof GSpline){
//                BSpline bs = (BSpline)s;                
//                xa = (int)bs.getStartPoint().getX();
//                ya = (int)bs.getStartPoint().getY();
//                Point2D P = translateWithPoint(getDX(), getDY(), xa, ya);
//                BSpline nbs = new BSpline(P.x, P.y);
//                for(ControlPoint cp : bs.getControlPoints()){
//                    xa = (int)cp.getStartPoint().getX();
//                    ya = (int)cp.getStartPoint().getY();
//                    Point2D CP1 = translateWithPoint(getDX(), getDY(), xa, ya);
//                    nbs.addPoint(CP1.x, CP1.y);
//                }
//                if(bs.isNextExist()){
//                    xa = (int)bs.getNextPoint().getX();
//                    ya = (int)bs.getNextPoint().getY();
//                    Point2D NX = translateWithPoint(getDX(), getDY(), xa, ya);
//                    nbs.setNextPoint(NX.x, NX.y);
//                }
//                mShapes.add(nbs);
            }
        }
    }
    
    public List<AShape> getPreviewShapes(){
        return mShapes;
    }
    
    private Point2D translateWithPoint(double rdx, double rdy, double xa, double ya){
        Point2D P = new Point2D.Double(xa, ya);
        Point2D Pprime = P;
        double xPprime = P.getX() + rdx;
        double yPprime = P.getY() + rdy;
        Pprime.setLocation(xPprime, yPprime);
        return Pprime;
    }
}
