/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.operation;

import java.util.ArrayList;
import java.util.List;
import smallboxforfansub.drawing.lib.BSpline;
import smallboxforfansub.drawing.lib.Bezier;
import smallboxforfansub.drawing.lib.ControlPoint;
import smallboxforfansub.drawing.lib.IShape;
import smallboxforfansub.drawing.lib.Line;
import smallboxforfansub.drawing.lib.Move;
import smallboxforfansub.drawing.lib.Point;
import smallboxforfansub.drawing.lib.ReStart;

/**
 *
 * @author The Wingate 2940
 */
public class Center {
    
    private int xo = -1, yo = -1;
    private boolean set = false;
    private double rx = 0d, ry = 0d;
    private List<IShape> mShapes = new ArrayList<IShape>();
    
    public Center(){
        
    }
    
    public Center(int xo, int yo){
        this.xo = xo;
        this.yo = yo;
        if(xo != -1 && yo != -1){
            set = true;
        }
    }
    
    public void clear(){
        xo = -1;
        yo = -1;
        set = false;
        rx = 0d;
        ry = 0d;
        mShapes.clear();
    }
    
    public void setCenter(int xo, int yo){
        this.xo = xo;
        this.yo = yo;
        if(xo != -1 && yo != -1){
            set = true;
        }
    }
    
    public boolean isSet(){
        return set;
    }
    
    public int getX(){
        return xo;
    }
    
    public int getY(){
        return yo;
    }
    
    public void setRotation(int rx, int ry){
        this.rx = rx;
        this.ry = ry;
    }
    
    public double getRX(){
        return rx;
    }
    
    public double getRY(){
        return ry;
    }
    
    public void setRotationPreview(List<IShape> pshapes, double angle){
        if(angle==0d){
            angle = getAngle();
        }
        mShapes.clear();
        java.awt.Point O, L; int xa, ya;
        for(IShape s : pshapes){
            if(s instanceof ReStart){
                ReStart m = (ReStart)s;
                xa = m.getOriginPoint().x; ya = m.getOriginPoint().y;
                O = rotateWithPoint(xo, yo, xa, ya, angle);
                xa = m.getLastPoint().x; ya = m.getLastPoint().y;
                L = rotateWithPoint(xo, yo, xa, ya, angle);
                mShapes.add(new ReStart(O.x, O.y, L.x, L.y));
            }else if(s instanceof Move){
                Move n = (Move)s;
                xa = n.getOriginPoint().x; ya = n.getOriginPoint().y;
                O = rotateWithPoint(xo, yo, xa, ya, angle);
                xa = n.getLastPoint().x; ya = n.getLastPoint().y;
                L = rotateWithPoint(xo, yo, xa, ya, angle);
                mShapes.add(new Move(O.x, O.y, L.x, L.y));
            }else if(s instanceof Point){
                Point p = (Point)s;
                xa = p.getOriginPoint().x; ya = p.getOriginPoint().y;
                O = rotateWithPoint(xo, yo, xa, ya, angle);
                mShapes.add(new Point(O.x, O.y));
            }else if(s instanceof Line){
                Line l = (Line)s;
                xa = l.getOriginPoint().x; ya = l.getOriginPoint().y;
                O = rotateWithPoint(xo, yo, xa, ya, angle);
                xa = l.getLastPoint().x; ya = l.getLastPoint().y;
                L = rotateWithPoint(xo, yo, xa, ya, angle);
                mShapes.add(new Line(O.x, O.y, L.x, L.y));
            }else if(s instanceof Bezier){
                Bezier b = (Bezier)s;
                xa = b.getOriginPoint().x; ya = b.getOriginPoint().y;
                O = rotateWithPoint(xo, yo, xa, ya, angle);
                xa = b.getLastPoint().x; ya = b.getLastPoint().y;
                L = rotateWithPoint(xo, yo, xa, ya, angle);
                xa = b.getControl1().getOriginPoint().x; ya = b.getControl1().getOriginPoint().y;
                java.awt.Point CP1 = rotateWithPoint(xo, yo, xa, ya, angle);
                xa = b.getControl2().getOriginPoint().x; ya = b.getControl2().getOriginPoint().y;
                java.awt.Point CP2 = rotateWithPoint(xo, yo, xa, ya, angle);
                mShapes.add(new Bezier(O.x, O.y, CP1.x, CP1.y, CP2.x, CP2.y, L.x, L.y));
            }else if(s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                java.awt.Point CP1 = rotateWithPoint(xo, yo, xa, ya, angle);
                mShapes.add(new ControlPoint(CP1.x, CP1.y));
            }else if(s instanceof BSpline){
                BSpline bs = (BSpline)s;                
                xa = (int)bs.getOriginPoint().getX();
                ya = (int)bs.getOriginPoint().getY();
                java.awt.Point P = rotateWithPoint(xo, yo, xa, ya, angle);
                BSpline nbs = new BSpline(P.x, P.y);
                for(ControlPoint cp : bs.getControlPoints()){
                    xa = (int)cp.getOriginPoint().getX();
                    ya = (int)cp.getOriginPoint().getY();
                    java.awt.Point CP1 = rotateWithPoint(xo, yo, xa, ya, angle);
                    nbs.addPoint(CP1.x, CP1.y);
                }
                if(bs.isNextExist()){
                    xa = (int)bs.getNextPoint().getX();
                    ya = (int)bs.getNextPoint().getY();
                    java.awt.Point NX = rotateWithPoint(xo, yo, xa, ya, angle);
                    nbs.setNextPoint(NX.x, NX.y);
                }
                mShapes.add(nbs);
            }
        }
    }
    
    public List<IShape> getPreviewShapes(){
        return mShapes;
    }
    
    /** Rotation d'un point par rapport à un autre.
     * @param xo Centre en xo
     * @param yo Centre en yo
     * @param xa Abscisse du point
     * @param ya Ordonnée du point
     * @param angle Angle en degré (positif ou négatif)
     * @return Le point modifié avec la rotation */
    private java.awt.Point rotateWithPoint(int xo, int yo, int xa, int ya, double angle){
        java.awt.Point S = new java.awt.Point(xo, yo);
        java.awt.Point P = new java.awt.Point(xa, ya);
        java.awt.Point Pprime = P;
        double SP = Math.sqrt(Math.pow(P.getX()-S.getX(), 2)+Math.pow(P.getY()-S.getY(), 2));
        double tan_PSN = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double angle_PSN = Math.toDegrees(Math.atan(tan_PSN));
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN));
        }
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN)+2*Math.PI);
        }
        if(P.getX() - S.getX() < 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN)+Math.PI);
        }
        double xPprime = SP * Math.cos(Math.toRadians(angle+angle_PSN)) + S.getX();
        double yPprime = SP * Math.sin(Math.toRadians(angle+angle_PSN)) + S.getY();
        Pprime.setLocation(xPprime, yPprime);
        return Pprime;
    }
    
    public double getAngle(){
        java.awt.Point S = new java.awt.Point(xo, yo);
        java.awt.Point P = new java.awt.Point((int)rx, (int)ry);
        double tan_PSN = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double angle_PSN = Math.toDegrees(Math.atan(tan_PSN));
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN));
        }
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN)+2*Math.PI);
        }
        if(P.getX() - S.getX() < 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN)+Math.PI);
        }
        return angle_PSN;
    }
}
