/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.grafx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author util2
 */
public class GLine extends AShape implements Cloneable {

    public GLine() {
    }
    
    public GLine(Point2D start, Point2D end, int uniqueID) {
        this.start = start;
        this.end = end;
        this.uniqueID = uniqueID;
    }
    
    public GLine(double sX, double sY, double eX, double eY, int uniqueID) {
        this.start = new Point2D.Double(sX, sY);
        this.end = new Point2D.Double(eX, eY);
        this.uniqueID = uniqueID;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        Line2D l;
        Rectangle2D r;
        
        g2d.setColor(Color.red);
        
        // Draw line
        l = new Line2D.Double(start, end);
        g2d.draw(l);
        
        g2d.setColor(Color.blue);
        
        // Draw Point 1
        r = new Rectangle2D.Double(start.getX() - 3d, start.getY() - 3d, 6d, 6d);
        g2d.fill(r);
        
        // Draw Point 2
        r = new Rectangle2D.Double(end.getX() - 3d, end.getY() - 3d, 6d, 6d);
        g2d.fill(r);
    }

    @Override
    public void drawOperations(Graphics2D g2d) {
        Line2D l;
        Rectangle2D r;
        
        g2d.setColor(Color.magenta);
        
        // Draw line
        l = new Line2D.Double(start, end);
        g2d.draw(l);
        
//        g2d.setColor(Color.blue);
        
        // Draw Point 1
        r = new Rectangle2D.Double(start.getX() - 3d, start.getY() - 3d, 6d, 6d);
        g2d.fill(r);
        
        // Draw Point 2
        r = new Rectangle2D.Double(end.getX() - 3d, end.getY() - 3d, 6d, 6d);
        g2d.fill(r);
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public GLine getClone(){
        try {
            return (GLine)clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
