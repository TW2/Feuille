/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.grafx;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 *
 * @author util2
 */
public interface IShape {
    
    /**
     * Draw the shape
     * @param g2d Graphic context
     */
    public void draw(Graphics2D g2d);
    
    public void drawOperations(Graphics2D g2d);
    
    public void setStartPoint(Point2D p2d);
    public Point2D getStartPoint();
    
    public void setEndPoint(Point2D p2d);
    public Point2D getEndPoint();
    
    public void setUniqueID(int uid);
    public int getUniqueID();
}
