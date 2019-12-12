/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.lib;

import feuille.grafx.AShape;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author The Wingate 2940
 */
public class Shear {
    
    //Un point pour désigner ce qui est le plus petit dans le dessin
    private java.awt.Point LESS = null;
    //Un point pour désigner ce qui est le plus grand dans le dessin
    private java.awt.Point MORE = null;
    //Un point pour désigner l'échelle de x et de y dans le dessin
    private java.awt.Point P = null;
    //Un point pour désigner le point référent
    private java.awt.Point M = null;
    //Un point pour désigner l'origine (à l'opposé du point référent)
    private java.awt.Point O = null;
    
    //Pourcentage en cours
    private double percentX = 100d;
    private double percentY = 100d;
    
    private boolean set = false;
    private boolean define = false;
    
    private List<AShape> mShapes = new ArrayList<>();
    
    public Shear(){
        
    }
    
    public void clear(){
        LESS = null;
        MORE = null;
        P = null; M = null; O = null;
        set = false;
        percentX = 100d;
        percentY = 100d;
        mShapes.clear();
    }
    
//    public void setup(List<IShape> shapes){
//        int lessX = shapes.get(0).getOriginPoint().x;
//        int lessY = shapes.get(0).getOriginPoint().y;
//        int moreX = shapes.get(0).getLastPoint().x;
//        int moreY = shapes.get(0).getLastPoint().y;
//        for(IShape s : shapes){
//            lessX = s.getOriginPoint().x<lessX ? s.getOriginPoint().x : lessX;
//            lessX = s.getLastPoint().x<lessX ? s.getLastPoint().x : lessX;
//            
//            lessY = s.getOriginPoint().y<lessY ? s.getOriginPoint().y : lessY;
//            lessY = s.getLastPoint().y<lessY ? s.getLastPoint().y : lessY;
//            
//            moreX = s.getOriginPoint().x>moreX ? s.getOriginPoint().x : moreX;
//            moreX = s.getLastPoint().x>moreX ? s.getLastPoint().x : moreX;
//            
//            moreY = s.getOriginPoint().y>moreY ? s.getOriginPoint().y : moreY;
//            moreY = s.getLastPoint().y>moreY ? s.getLastPoint().y : moreY;
//        }
//        LESS = new java.awt.Point(lessX, lessY);
//        MORE = new java.awt.Point(moreX, moreY);
//    }
//    
//    public void setShearPoint(int x, int y){
//        P = new java.awt.Point(x, y);
//        if(LESS!=null && MORE!=null){
//            //100% = moreX-lessX
//            //?% = P.x-lessX
//            percentX = (P.x-LESS.x)*100/(MORE.x-LESS.x);
//            percentY = (P.y-LESS.y)*100/(MORE.y-LESS.y);
//            set = true;
//        }
//    }
//    
//    public void setMovePoint(int x, int y){
//        M = new java.awt.Point(x, y);
//        checkDefine();
//    }
//    
//    public boolean isSet(){
//        return set;
//    }
//    
//    public double getPercentX(){
//        return percentX;
//    }
//    
//    public double getPercentY(){
//        return percentY;
//    }
//    
//    public void drawLimits(Graphics2D g2d){
//        g2d.setColor(Color.magenta);
//        g2d.drawLine(LESS.x, LESS.y, MORE.x, LESS.y); //Horizontale
//        g2d.drawLine(MORE.x, LESS.y, MORE.x, MORE.y); //Verticale
//        g2d.drawLine(MORE.x, MORE.y, LESS.x, MORE.y); //Horizontale
//        g2d.drawLine(LESS.x, MORE.y, LESS.x, LESS.y); //Verticale
//        if(define==true){
//            g2d.setColor(Color.red);
//            g2d.fillOval(P.x-10, P.y-10, 20, 20);
//        }else{
//            g2d.setColor(Color.red);
//            g2d.fillOval(P.x-5, P.y-5, 10, 10);
//        }
//        if(O!=null && M!=null){
//            g2d.setColor(Color.pink);
//            g2d.fillOval(M.x-10, M.y-10, 20, 20);
//            g2d.setColor(Color.orange);
//            g2d.fillOval(O.x-10, O.y-10, 20, 20);
//        }
//    }
//    
//    private boolean checkDefine(){
//        Rectangle rect = new Rectangle(M.x-50, M.y-50, 100, 100);
//        if(rect.contains(LESS.x, LESS.y)){
//            M.setLocation(LESS.x, LESS.y);
//            O = new java.awt.Point(MORE.x, MORE.y);
//        }else if(rect.contains(MORE.x, LESS.y)){
//            M.setLocation(MORE.x, LESS.y);
//            O = new java.awt.Point(LESS.x, MORE.y);
//        }else if(rect.contains(MORE.x, MORE.y)){
//            M.setLocation(MORE.x, MORE.y);
//            O = new java.awt.Point(LESS.x, LESS.y);
//        }else if(rect.contains(LESS.x, MORE.y)){
//            M.setLocation(LESS.x, MORE.y);
//            O = new java.awt.Point(MORE.x, LESS.y);
//        }
//        if(rect.contains(M)){            
//            return true;
//        }
//        return false;
//    }
//    
//    public void setShearPreview(List<IShape> pshapes){
//        mShapes.clear();
//        java.awt.Point Q, L; int xa, ya;
//        for(IShape s : pshapes){
//            if(s instanceof ReStart){
//                ReStart m = (ReStart)s;
//                xa = m.getOriginPoint().x; ya = m.getOriginPoint().y;
//                Q = shearWithPoint(xa, ya);
//                xa = m.getLastPoint().x; ya = m.getLastPoint().y;
//                L = shearWithPoint(xa, ya);
//                mShapes.add(new ReStart(Q.x, Q.y, L.x, L.y));
//            }else if(s instanceof Move){
//                Move n = (Move)s;
//                xa = n.getOriginPoint().x; ya = n.getOriginPoint().y;
//                Q = shearWithPoint(xa, ya);
//                xa = n.getLastPoint().x; ya = n.getLastPoint().y;
//                L = shearWithPoint(xa, ya);
//                mShapes.add(new Move(Q.x, Q.y, L.x, L.y));
//            }else if(s instanceof Point){
//                Point p = (Point)s;
//                xa = p.getOriginPoint().x; ya = p.getOriginPoint().y;
//                Q = shearWithPoint(xa, ya);
//                mShapes.add(new Point(Q.x, Q.y));
//            }else if(s instanceof Line){
//                Line l = (Line)s;
//                xa = l.getOriginPoint().x; ya = l.getOriginPoint().y;
//                Q = shearWithPoint(xa, ya);
//                xa = l.getLastPoint().x; ya = l.getLastPoint().y;
//                L = shearWithPoint(xa, ya);
//                mShapes.add(new Line(Q.x, Q.y, L.x, L.y));
//            }else if(s instanceof Bezier){
//                Bezier b = (Bezier)s;
//                xa = b.getOriginPoint().x; ya = b.getOriginPoint().y;
//                Q = shearWithPoint(xa, ya);
//                xa = b.getLastPoint().x; ya = b.getLastPoint().y;
//                L = shearWithPoint(xa, ya);
//                xa = b.getControl1().getOriginPoint().x; ya = b.getControl1().getOriginPoint().y;
//                java.awt.Point CP1 = shearWithPoint(xa, ya);
//                xa = b.getControl2().getOriginPoint().x; ya = b.getControl2().getOriginPoint().y;
//                java.awt.Point CP2 = shearWithPoint(xa, ya);
//                mShapes.add(new Bezier(Q.x, Q.y, CP1.x, CP1.y, CP2.x, CP2.y, L.x, L.y));
//            }else if(s instanceof ControlPoint){
//                ControlPoint cp = (ControlPoint)s;
//                xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
//                java.awt.Point CP1 = shearWithPoint(xa, ya);
//                mShapes.add(new ControlPoint(CP1.x, CP1.y));
//            }else if(s instanceof BSpline){
//                BSpline bs = (BSpline)s;                
//                xa = (int)bs.getOriginPoint().getX();
//                ya = (int)bs.getOriginPoint().getY();
//                java.awt.Point QP = shearWithPoint(xa, ya);
//                BSpline nbs = new BSpline(QP.x, QP.y);
//                for(ControlPoint cp : bs.getControlPoints()){
//                    xa = (int)cp.getOriginPoint().getX();
//                    ya = (int)cp.getOriginPoint().getY();
//                    java.awt.Point CP1 = shearWithPoint(xa, ya);
//                    nbs.addPoint(CP1.x, CP1.y);
//                }
//                if(bs.isNextExist()){
//                    xa = (int)bs.getNextPoint().getX();
//                    ya = (int)bs.getNextPoint().getY();
//                    java.awt.Point NX = shearWithPoint(xa, ya);
//                    nbs.setNextPoint(NX.x, NX.y);
//                }
//                mShapes.add(nbs);
//            }
//        }
//    }
//    
//    public List<IShape> getPreviewShapes(){
//        return mShapes;
//    }
//    
//    private java.awt.Point shearWithPoint(int xa, int ya){
//        //Le point de référence (celui qui ne bouge pas est le point O.
//        //Le point pour déterminant la longueur est le point M.
//        //On a donc la longueur OMx et OMy qui représente  100% de la logueur.
//        //On calcule donc ces longueurs
//        System.out.println(O+"+"+M);
//        double OMx = java.awt.geom.Point2D.distance(O.getX(), O.getY(), M.getX(), O.getY());
//        double OMy = java.awt.geom.Point2D.distance(O.getX(), O.getY(), O.getX(), M.getY());
//        //100% = OM (référent)
//        //?% = OP (shear)
//        double OPx = java.awt.geom.Point2D.distance(O.getX(), O.getY(), P.getX(), O.getY());
//        double OPy = java.awt.geom.Point2D.distance(O.getX(), O.getY(), O.getX(), P.getY());
//        double percentX_MAX = OPx*100/OMx;
//        double percentY_MAX = OPy*100/OMy;
//        //Le point X est le point de l'objet en cours; pour lui 100% représente que ?%
//        //Le point X' est le point que l'on cherche
//        java.awt.Point X = new java.awt.Point(xa, ya);
//        java.awt.Point Xprime = X;
//        if(X.equals(O)){ return O;}
//        //On a :
//        //100% = OP
//        //Percent_MAX = OX
//        double OXx = java.awt.geom.Point2D.distance(O.getX(), O.getY(), X.getX(), O.getY());
//        double OXy = java.awt.geom.Point2D.distance(O.getX(), O.getY(), O.getX(), X.getY());
//        double OXxprime = OPx*percentX_MAX/100;
//        double OXyprime = OPy*percentY_MAX/100;
//        //On calcule l'angle pour reinsérer le point dans le plan
//        double tan_XON = OXyprime / OXxprime;
//        double angle_XON = Math.toDegrees(Math.atan(tan_XON));
//        if(OXxprime > 0 && OXyprime >= 0){
//            angle_XON = Math.toDegrees(Math.atan(tan_XON));
//        }
//        if(OXxprime > 0 && OXyprime < 0){
//            angle_XON = Math.toDegrees(Math.atan(tan_XON)+2*Math.PI);
//        }
//        if(OXxprime < 0){
//            angle_XON = Math.toDegrees(Math.atan(tan_XON)+Math.PI);
//        }
//        //On réinsère le point dans le plan
//        double OX = Math.sqrt(Math.pow(OXxprime, 2)+Math.pow(OXyprime, 2));
//        double xPprime = OX * Math.cos(Math.toRadians(angle_XON)) + O.getX();
//        double yPprime = OX * Math.sin(Math.toRadians(angle_XON)) + O.getY();
//        Xprime = new java.awt.Point((int)xPprime, (int)yPprime);
//        return Xprime;
//    }
    
    
}
