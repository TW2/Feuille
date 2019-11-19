/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.operation;

import java.awt.geom.Point2D;
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
public class Resize {
    
    private int xo = -1, yo = -1;
    private boolean set = false;
    private double dx = 0d, dy = 0d;
    private List<IShape> mShapes = new ArrayList<IShape>();
    
    public Resize(){
        
    }
    
    public Resize(int xo, int yo){
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
        dx = 0d;
        dy = 0d;
        mShapes.clear();
    }
    
    public void setResize(int xo, int yo){
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
    
    public void setDistance(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    public double getRX(){
        return dx;
    }
    
    public double getRY(){
        return dy;
    }
    
    public void setResizePreview(List<IShape> pshapes, double percent){
        if(percent==0d){
            percent = getPercent();
        }
        mShapes.clear();
        java.awt.Point O, L, M = getReStartPoint(pshapes); int xa, ya;
        for(IShape s : pshapes){
            if(s instanceof ReStart){
                ReStart m = (ReStart)s;
                xa = m.getOriginPoint().x; ya = m.getOriginPoint().y;
                O = resizeWithPoint(M.x, M.y, xa, ya, percent);
                xa = m.getLastPoint().x; ya = m.getLastPoint().y;
                L = resizeWithPoint(M.x, M.y, xa, ya, percent);
                mShapes.add(new ReStart(O.x, O.y, L.x, L.y));
            }else if(s instanceof Move){
                Move n = (Move)s;
                xa = n.getOriginPoint().x; ya = n.getOriginPoint().y;
                O = resizeWithPoint(M.x, M.y, xa, ya, percent);
                xa = n.getLastPoint().x; ya = n.getLastPoint().y;
                L = resizeWithPoint(M.x, M.y, xa, ya, percent);
                mShapes.add(new Move(O.x, O.y, L.x, L.y));
            }else if(s instanceof Point){
                Point p = (Point)s;
                xa = p.getOriginPoint().x; ya = p.getOriginPoint().y;
                O = resizeWithPoint(M.x, M.y, xa, ya, percent);
                mShapes.add(new Point(O.x, O.y));
            }else if(s instanceof Line){
                Line l = (Line)s;
                xa = l.getOriginPoint().x; ya = l.getOriginPoint().y;
                O = resizeWithPoint(M.x, M.y, xa, ya, percent);
                xa = l.getLastPoint().x; ya = l.getLastPoint().y;
                L = resizeWithPoint(M.x, M.y, xa, ya, percent);
                mShapes.add(new Line(O.x, O.y, L.x, L.y));
            }else if(s instanceof Bezier){
                Bezier b = (Bezier)s;
                xa = b.getOriginPoint().x; ya = b.getOriginPoint().y;
                O = resizeWithPoint(M.x, M.y, xa, ya, percent);
                xa = b.getLastPoint().x; ya = b.getLastPoint().y;
                L = resizeWithPoint(M.x, M.y, xa, ya, percent);
                xa = b.getControl1().getOriginPoint().x; ya = b.getControl1().getOriginPoint().y;
                java.awt.Point CP1 = resizeWithPoint(M.x, M.y, xa, ya, percent);
                xa = b.getControl2().getOriginPoint().x; ya = b.getControl2().getOriginPoint().y;
                java.awt.Point CP2 = resizeWithPoint(M.x, M.y, xa, ya, percent);
                mShapes.add(new Bezier(O.x, O.y, CP1.x, CP1.y, CP2.x, CP2.y, L.x, L.y));
            }else if(s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                java.awt.Point CP1 = resizeWithPoint(M.x, M.y, xa, ya, percent);
                mShapes.add(new ControlPoint(CP1.x, CP1.y));
            }else if(s instanceof BSpline){
                BSpline bs = (BSpline)s;                
                xa = (int)bs.getOriginPoint().getX();
                ya = (int)bs.getOriginPoint().getY();
                java.awt.Point P = resizeWithPoint(M.x, M.y, xa, ya, percent);
                BSpline nbs = new BSpline(P.x, P.y);
                for(ControlPoint cp : bs.getControlPoints()){
                    xa = (int)cp.getOriginPoint().getX();
                    ya = (int)cp.getOriginPoint().getY();
                    java.awt.Point CP1 = resizeWithPoint(M.x, M.y, xa, ya, percent);
                    nbs.addPoint(CP1.x, CP1.y);
                }
                if(bs.isNextExist()){
                    xa = (int)bs.getNextPoint().getX();
                    ya = (int)bs.getNextPoint().getY();
                    java.awt.Point NX = resizeWithPoint(M.x, M.y, xa, ya, percent);
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
    private java.awt.Point resizeWithPoint(int x, int y, int xa, int ya, double percent){
        //S est le point issu d'un ReStart, c'est donc le point de référence
        //P est le point d'insertion (pour lequel on veut calculer P')
        java.awt.Point S = new java.awt.Point(x, y);
        java.awt.Point P = new java.awt.Point(xa, ya);
        java.awt.Point Pprime = P;
        //Si P est égal à S alors, on n'a pas besoin de faire le calcule on retourne le même point.
        if(P.equals(S)){ return P; }
        //On veut que le point S soit toujours l'origine
        //On donc calcule la distance de S à P pour en sortir une distance en fonction du pourcentage
        double SP = Point2D.distance(S.getX(), S.getY(), P.getX(), P.getY());
        double SPprime = SP*percent/100;
        //On calcule l'angle S afin de savoir où resituer le point P'
        double tanS = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double angleS = Math.toDegrees(Math.atan(tanS));
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            angleS = Math.toDegrees(Math.atan(tanS));
        }
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            angleS = Math.toDegrees(Math.atan(tanS)+2*Math.PI);
        }
        if(P.getX() - S.getX() < 0){
            angleS = Math.toDegrees(Math.atan(tanS)+Math.PI);
        }
        //La distance en fonction du pourcentage vient s'ajouter aux coordonnées de S avec l'angle S.
        double xPprime = SPprime * Math.cos(Math.toRadians(angleS)) + S.getX();
        double yPprime = SPprime * Math.sin(Math.toRadians(angleS)) + S.getY();
        //S.x-(S.x-xo+dx-xo)+(P.getX()-dx)*percent/100, S.y-(S.y-yo+dy-yo)+(P.getY()-dy)*percent/100
        //S.x-(S.x-dx)+(P.getX()-dx)*percent/100, S.y-(S.y-dy)+(P.getY()-dy)*percent/100
        Pprime.setLocation(xPprime,yPprime);
        return Pprime;
    }
    
    public double getPercent(){
        //100% = xo
        //?% = dx
        //?% = 100*dx/xo
        double percent = 100d*dx/xo;
        return percent;
    }
    
    public java.awt.Point getReStartPoint(List<IShape> shapes){
        for(IShape s : shapes){
            if(s instanceof ReStart){
                ReStart m = (ReStart)s;
                return m.getLastPoint();
            }
        }
        return new java.awt.Point(0, 0);
    }
}
