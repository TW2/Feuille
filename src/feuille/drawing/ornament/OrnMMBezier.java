/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.ornament;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;


/**
 *
 * @author The Wingate 2940
 */
public class OrnMMBezier extends AShape {
    
    private OrnControlPoint c1=null, c2=null;
    private double Ax, Bx, Cx, Ay, By, Cy;
    
    public OrnMMBezier(){
        
    }
    
    public OrnMMBezier(int originX1, int originY1,
            int controlX2, int controlY2,
            int controlX3, int controlY3,
            int lastX4, int lastY4){
        originPoint = new java.awt.Point(originX1,originY1);
        c1 = new OrnControlPoint(controlX2,controlY2);
        c2 = new OrnControlPoint(controlX3,controlY3);
        lastPoint = new java.awt.Point(lastX4,lastY4);
    }
    
    public OrnMMBezier(int originX1, int originY1,
            int lastX4, int lastY4){
        originPoint = new java.awt.Point(originX1,originY1);
        lastPoint = new java.awt.Point(lastX4,lastY4);

        int xdiff = (int)lastPoint.getX() - (int)originPoint.getX();
        int ydiff = (int)lastPoint.getY() - (int)originPoint.getY();

        int x1_3 = (int)originPoint.getX() + xdiff/3;
        int x2_3 = (int)originPoint.getX() + xdiff*2/3;
        int y1_3 = (int)originPoint.getY() + ydiff/3;
        int y2_3 = (int)originPoint.getY() + ydiff*2/3;
        
        c1 = new OrnControlPoint(x1_3, y1_3);
        c2 = new OrnControlPoint(x2_3,y2_3);
    }
    
    /** Définit le "point de contrôle" n°1 */
    public void setControl1Point(int x1, int y1){
        c1 = new OrnControlPoint(x1, y1);
    }

    /** Définit le "point de contrôle" n°2 */
    public void setControl2Point(int x2, int y2){
        c2 = new OrnControlPoint(x2, y2);
    }

    /** Retourne les coordonnées du "point de contrôle" n°1 */
    public java.awt.Point getControl1Point(){
        return c1.getOriginPoint();
    }

    /** Retourne les coordonnées du "point de contrôle" n°2 */
    public java.awt.Point getControl2Point(){
        return c2.getOriginPoint();
    }
    
    /** Définit le "point de contrôle" n°1 */
    public void setControl1(OrnControlPoint c1){
        this.c1 = c1;
    }

    /** Obtient le "point de contrôle" n°1 */
    public OrnControlPoint getControl1(){
        return c1;
    }

    /** Définit le "point de contrôle" n°2 */
    public void setControl2(OrnControlPoint c2){
        this.c2 = c2;
    }

    /** Obtient le "point de contrôle" n°2 */
    public OrnControlPoint getControl2(){
        return c2;
    }
    
    // http://fr.wikipedia.org/wiki/Courbe_de_B%C3%A9zier
    // Pour retrouver y, nous allons utiliser la relation :
    // P(t)=P0(1-t)^3+3P1*t(1-t)^2+3*P2*t^2(1-t)+P3*t^3
    // Pöur calculer t on a besoin de faire un produit en croix :
    // 1 <> lastPoint.getX()
    // t <> x
    // D'où t = x / lastPoint.getX()

//    @Override
//    public double getY(double x) {
//        double P0 = originPoint.getX();
//        double P1 = c1.getOriginPoint().getX();
//        double P2 = c2.getOriginPoint().getX();
//        double P3 = lastPoint.getX();
//        double t = x / lastPoint.getX();
//        
//        double y = 
//                P0*Math.pow((1-t),3)+
//                3*P1*t*Math.pow((1-t),2)+
//                3*P2*Math.pow(t,2)*(1-t)+
//                P3*Math.pow(t,3);
//        
//        return y;
//    }
    
    //http://www.moshplant.com/direct-or/bezier/math.html
    public double getX(double t){
        //cx = 3 (x1 - x0)
        //bx = 3 (x2 - x1) - cx
        //ax = x3 - x0 - cx - bx
        double x0 = originPoint.getX();
        double x1 = c1.getOriginPoint().getX();
        double x2 = c2.getOriginPoint().getX();
        double x3 = lastPoint.getX();
        
        Cx = 3*(x1 - x0);
        Bx = 3*(x2 - x1) - Cx;
        Ax = x3 - x0 - Cx - Bx;
        
        //x(t) = axt3 + bxt2 + cxt + x0
        //x1 = x0 + cx / 3
        //x2 = x1 + (cx + bx) / 3
        //x3 = x0 + cx + bx + ax
        
        double x = Ax*Math.pow(t, 3)+Bx*Math.pow(t, 2)+Cx*t+x0;
        return x;
    }
    
    //http://www.moshplant.com/direct-or/bezier/math.html
    @Override
    public double getY(double t){
        //cy = 3 (y1 - y0)
        //by = 3 (y2 - y1) - cy
        //ay = y3 - y0 - cy - by
        double y0 = originPoint.getY();
        double y1 = c1.getOriginPoint().getY();
        double y2 = c2.getOriginPoint().getY();
        double y3 = lastPoint.getY();
        
        Cy = 3*(y1 - y0);
        By = 3*(y2 - y1) - Cy;
        Ay = y3 - y0 - Cy - By;
        
        //y(t) = ayt3 + byt2 + cyt + y0
        //y1 = y0 + cy / 3
        //y2 = y1 + (cy + by) / 3
        //y3 = y0 + cy + by + ay
        
        double y = Ay*Math.pow(t, 3)+By*Math.pow(t, 2)+Cy*t+y0;
        return y;
    }

    @Override
    public void draw(Graphics2D g2d, Color c) {
        Color oldc = g2d.getColor();
        g2d.setColor(c);
        // On dessine la courbe
        CubicCurve2D cc = new CubicCurve2D.Double();
        cc.setCurve(originPoint.x, originPoint.y,
                c1.getOriginPoint().x, c1.getOriginPoint().y,
                c2.getOriginPoint().x, c2.getOriginPoint().y,
                lastPoint.x, lastPoint.y);
        g2d.draw(cc);
        // On dessine les lignes entre points et points de contrôle
        g2d.setColor(c.brighter());
        Stroke stroke = g2d.getStroke();
        float[] dash = {5, 5};
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5, dash, 0));
        g2d.drawLine(originPoint.x, originPoint.y, c1.getOriginPoint().x, c1.getOriginPoint().y);
        g2d.drawLine(c1.getOriginPoint().x, c1.getOriginPoint().y, c2.getOriginPoint().x, c2.getOriginPoint().y);
        g2d.drawLine(c2.getOriginPoint().x, c2.getOriginPoint().y, lastPoint.x, lastPoint.y);
        g2d.setStroke(stroke);
        g2d.setColor(oldc);
        // On ne dessine pas les points de contrôle qui sont dessinés indépendamment.
    }
    
    @Override
    public String toString(){
        return "Bézier";
    }
    
}
