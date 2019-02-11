/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.drawing.shape;

import feuille.util.DrawColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author util2
 */
public class FeuillePoint {
    
    public enum FeuillePointType{
        Unknown, ControlPoint, ControlPoint1, ControlPoint2, Start, End, Center;
    }
    
    private FeuillePointType type = FeuillePointType.Unknown;
    private Point2D point = new Point2D.Float(0, 0);
    private int uniqueID = 0;

    public FeuillePoint() {
    }
    
    public static FeuillePoint create(FeuillePointType type, int uniqueID){
        FeuillePoint fp = new FeuillePoint();
        fp.type = type;
        fp.uniqueID = uniqueID;
        return fp;
    }
    
    public static FeuillePoint create(FeuillePointType type, int uniqueID, Point2D point){
        FeuillePoint fp = new FeuillePoint();
        fp.type = type;
        fp.uniqueID = uniqueID;
        fp.point = point;
        return fp;
    }
    
    public static FeuillePoint create(FeuillePointType type, int uniqueID, float x, float y){
        FeuillePoint fp = new FeuillePoint();
        fp.type = type;
        fp.uniqueID = uniqueID;
        fp.point = new Point2D.Float(x, y);
        return fp;
    }

    public void setType(FeuillePointType type) {
        this.type = type;
    }

    public FeuillePointType getType() {
        return type;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    public int getUniqueID() {
        return uniqueID;
    }
    
    public void draw(Graphics2D g2d, Dimension render, float scale){
        // Example of something that works : x * scale + getWidth() / 2
        Point2D p = new Point2D.Double(
                point.getX() * scale + render.getWidth() / 2,
                point.getY() * scale + render.getHeight()/ 2);
        
        Rectangle2D r;
        Ellipse2D e;
        switch(type){
            case Unknown:
                break;
            case Start:
                g2d.setColor(Color.blue);
                r = new Rectangle2D.Double(p.getX()-5, p.getY()-5, 10, 10);
                g2d.fill(r);
                break;
            case End:
                g2d.setColor(DrawColor.blue_violet.getColor());
                r = new Rectangle2D.Double(p.getX()-5, p.getY()-5, 10, 10);
                g2d.fill(r);
                break;
            case Center:
                break;
            case ControlPoint:
                g2d.setColor(DrawColor.orange_red.getColor());
                e = new Ellipse2D.Double(p.getX()-5, p.getY()-5, 10, 10);
                g2d.fill(e);
                break;
            case ControlPoint1:
                g2d.setColor(DrawColor.orange.getColor());
                e = new Ellipse2D.Double(p.getX()-5, p.getY()-5, 10, 10);
                g2d.fill(e);
                break;
            case ControlPoint2:
                g2d.setColor(DrawColor.gold.getColor());
                e = new Ellipse2D.Double(p.getX()-5, p.getY()-5, 10, 10);
                g2d.fill(e);
                break;
            default:
                break;
        }
    }
    
}
