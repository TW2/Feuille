/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.grafx;

import java.awt.geom.Point2D;

/**
 *
 * @author util2
 */
public abstract class AShape implements IShape {

    protected Point2D start = null;
    protected Point2D end = null;
    protected int uniqueID = -1;
    
    public AShape() {
    }

    @Override
    public Point2D getStartPoint() throws NullPointerException {
        return start;
    }

    @Override
    public void setStartPoint(Point2D p2d) {
        start = p2d;
    }

    @Override
    public Point2D getEndPoint() throws NullPointerException {
        return end;
    }

    @Override
    public void setEndPoint(Point2D p2d) {
        end = p2d;
    }

    @Override
    public int getUniqueID() {
        return uniqueID;
    }

    @Override
    public void setUniqueID(int uid) {
        this.uniqueID = uid;
    }
    
}
