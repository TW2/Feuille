/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.grafx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author util2
 */
public class GQuadratic extends ACurveShape implements Cloneable {

    public GQuadratic() {
    }
    
    public GQuadratic(Point2D start, Point2D end, int uniqueID) {
        this.start = start;
        this.end = end;
        this.uniqueID = uniqueID;
        
        this.clearControlPoints();
        
        // Calculation of control points
        Point2D cp = new Point2D.Double(
                (end.getX() - start.getX()) * 1 / 2 + start.getX(),
                (end.getY() - start.getY()) * 1 / 2 + start.getY()
        );
        
        this.addControlPoint(cp);
    }
    
    public GQuadratic(Point2D start, Point2D cp, Point2D end, int uniqueID) {
        this.start = start;
        this.end = end;
        this.uniqueID = uniqueID;
        this.clearControlPoints();
        this.addControlPoint(cp);
    }
    
    public GQuadratic(double sX, double sY, double eX, double eY, int uniqueID) {
        Point2D s = new Point2D.Double(sX, sY);
        Point2D e = new Point2D.Double(eX, eY);
        
        this.start = s;
        this.end = e;
        this.uniqueID = uniqueID;
        
        this.clearControlPoints();
        
        // Calculation of control points
        Point2D cp = new Point2D.Double(
                (end.getX() - start.getX()) * 1 / 2 + start.getX(),
                (end.getY() - start.getY()) * 1 / 2 + start.getY()
        );
        
        this.addControlPoint(cp);
    }
    
    public GQuadratic(double sX, double sY, double cpX, double cpY, double eX, double eY, int uniqueID) {
        Point2D s = new Point2D.Double(sX, sY);
        Point2D e = new Point2D.Double(eX, eY);
        
        this.start = s;
        this.end = e;
        this.uniqueID = uniqueID;
        
        this.clearControlPoints();
        
        // Calculation of control points
        Point2D cp = new Point2D.Double(cpX, cpY);
        
        this.addControlPoint(cp);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        Line2D l;
        QuadCurve2D c;
        Rectangle2D r;
        
        Stroke oldStroke = g2d.getStroke();
        
        Point2D cp = getControlPoints().get(0);
        
        // Draw control lines
        g2d.setStroke(new BasicStroke(
                1f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                10f,
                new float[]{2f, 2f},
                2f
        ));
        g2d.setColor(Color.gray);
        l = new Line2D.Double(start, cp); g2d.draw(l);
        l = new Line2D.Double(cp, end); g2d.draw(l);
        
        // Draw cubic
        g2d.setStroke(oldStroke);
        g2d.setColor(Color.green.darker());
        c = new QuadCurve2D.Double(
                start.getX(), start.getY(),
                cp.getX(), cp.getY(),
                end.getX(), end.getY());
        g2d.draw(c);
        
        // Draw start and end
        g2d.setColor(Color.blue);        
        // Draw Point 1
        r = new Rectangle2D.Double(start.getX() - 3d, start.getY() - 3d, 6d, 6d);
        g2d.fill(r);        
        // Draw Point 2
        r = new Rectangle2D.Double(end.getX() - 3d, end.getY() - 3d, 6d, 6d);
        g2d.fill(r);
        
        // Draw control points
        g2d.setColor(Color.orange);        
        Ellipse2D e;
        e = new Ellipse2D.Double(cp.getX() - 3d, cp.getY() - 3d, 6d, 6d);
        g2d.fill(e);        
    }

    @Override
    public void drawOperations(Graphics2D g2d) {
        Line2D l;
        QuadCurve2D c;
        Rectangle2D r;
        
        g2d.setColor(Color.magenta);
        Stroke oldStroke = g2d.getStroke();
        
        Point2D cp = getControlPoints().get(0);
        
        // Draw control lines
        g2d.setStroke(new BasicStroke(
                1f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                10f,
                new float[]{2f, 2f},
                2f
        ));
//        g2d.setColor(Color.gray);
        l = new Line2D.Double(start, cp); g2d.draw(l);
        l = new Line2D.Double(cp, end); g2d.draw(l);
        
        // Draw cubic
        g2d.setStroke(oldStroke);
//        g2d.setColor(Color.green.darker());
        c = new QuadCurve2D.Double(
                start.getX(), start.getY(),
                cp.getX(), cp.getY(),
                end.getX(), end.getY());
        g2d.draw(c);
        
        // Draw start and end
//        g2d.setColor(Color.blue);        
        // Draw Point 1
        r = new Rectangle2D.Double(start.getX() - 3d, start.getY() - 3d, 6d, 6d);
        g2d.fill(r);        
        // Draw Point 2
        r = new Rectangle2D.Double(end.getX() - 3d, end.getY() - 3d, 6d, 6d);
        g2d.fill(r);
        
        // Draw control points
//        g2d.setColor(Color.orange);        
        Ellipse2D e;
        e = new Ellipse2D.Double(cp.getX() - 3d, cp.getY() - 3d, 6d, 6d);
        g2d.fill(e);        
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public GQuadratic getClone(){
        try {
            return (GQuadratic)clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
