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
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author util2
 */
public class GCubic extends ACurveShape implements Cloneable {

    public GCubic() {
    }
    
    public GCubic(Point2D start, Point2D end, int uniqueID) {
        this.start = start;
        this.end = end;
        this.uniqueID = uniqueID;
        
        this.clearControlPoints();
        
        // Calculation of control points
        Point2D cp1 = new Point2D.Double(
                (end.getX() - start.getX()) * 1 / 3 + start.getX(),
                (end.getY() - start.getY()) * 1 / 3 + start.getY()
        );
        
        Point2D cp2 = new Point2D.Double(
                (end.getX() - start.getX()) * 2 / 3 + start.getX(),
                (end.getY() - start.getY()) * 2 / 3 + start.getY()
        );
        
        this.addControlPoint(cp1);
        this.addControlPoint(cp2);
    }
    
    public GCubic(Point2D start, Point2D cp1, Point2D cp2, Point2D end, int uniqueID) {
        this.start = start;
        this.end = end;
        this.uniqueID = uniqueID;
        this.clearControlPoints();
        this.addControlPoint(cp1);
        this.addControlPoint(cp2);
    }
    
    public GCubic(double sX, double sY, double eX, double eY, int uniqueID) {
        Point2D s = new Point2D.Double(sX, sY);
        Point2D e = new Point2D.Double(eX, eY);
        
        this.start = s;
        this.end = e;
        this.uniqueID = uniqueID;
        
        this.clearControlPoints();
        
        // Calculation of control points
        Point2D cp1 = new Point2D.Double(
                (end.getX() - start.getX()) * 1 / 3 + start.getX(),
                (end.getY() - start.getY()) * 1 / 3 + start.getY()
        );
        
        Point2D cp2 = new Point2D.Double(
                (end.getX() - start.getX()) * 2 / 3 + start.getX(),
                (end.getY() - start.getY()) * 2 / 3 + start.getY()
        );
        
        this.addControlPoint(cp1);
        this.addControlPoint(cp2);
    }
    
    public GCubic(double sX, double sY, double cp1X, double cp1Y, double cp2X, double cp2Y, double eX, double eY, int uniqueID) {
        Point2D s = new Point2D.Double(sX, sY);
        Point2D e = new Point2D.Double(eX, eY);
        
        this.start = s;
        this.end = e;
        this.uniqueID = uniqueID;
        
        this.clearControlPoints();
        
        // Calculation of control points
        Point2D cp1 = new Point2D.Double(cp1X, cp1Y);        
        Point2D cp2 = new Point2D.Double(cp2X, cp2Y);
        
        this.addControlPoint(cp1);
        this.addControlPoint(cp2);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        Line2D l;
        CubicCurve2D c;
        Rectangle2D r;
        
        Stroke oldStroke = g2d.getStroke();
        
        Point2D cp1 = getControlPoints().get(0);
        Point2D cp2 = getControlPoints().get(1);
        
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
        l = new Line2D.Double(start, cp1); g2d.draw(l);
        l = new Line2D.Double(cp1, cp2); g2d.draw(l);
        l = new Line2D.Double(cp2, end); g2d.draw(l);
        
        // Draw cubic
        g2d.setStroke(oldStroke);
        g2d.setColor(Color.magenta);
        c = new CubicCurve2D.Double(
                start.getX(), start.getY(),
                cp1.getX(), cp1.getY(),
                cp2.getX(), cp2.getY(),
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
        e = new Ellipse2D.Double(cp1.getX() - 3d, cp1.getY() - 3d, 6d, 6d);
        g2d.fill(e);
        e = new Ellipse2D.Double(cp2.getX() - 3d, cp2.getY() - 3d, 6d, 6d);
        g2d.fill(e);
    }

    @Override
    public void drawOperations(Graphics2D g2d) {
        Line2D l;
        CubicCurve2D c;
        Rectangle2D r;
        
        g2d.setColor(Color.magenta);
        Stroke oldStroke = g2d.getStroke();
        
        Point2D cp1 = getControlPoints().get(0);
        Point2D cp2 = getControlPoints().get(1);
        
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
        l = new Line2D.Double(start, cp1); g2d.draw(l);
        l = new Line2D.Double(cp1, cp2); g2d.draw(l);
        l = new Line2D.Double(cp2, end); g2d.draw(l);
        
        // Draw cubic
        g2d.setStroke(oldStroke);
//        g2d.setColor(Color.magenta);
        c = new CubicCurve2D.Double(
                start.getX(), start.getY(),
                cp1.getX(), cp1.getY(),
                cp2.getX(), cp2.getY(),
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
        e = new Ellipse2D.Double(cp1.getX() - 3d, cp1.getY() - 3d, 6d, 6d);
        g2d.fill(e);
        e = new Ellipse2D.Double(cp2.getX() - 3d, cp2.getY() - 3d, 6d, 6d);
        g2d.fill(e);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }    
    
    public GCubic getClone(){
        try {
            return (GCubic)clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
