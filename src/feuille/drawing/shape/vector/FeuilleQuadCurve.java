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
package feuille.drawing.shape.vector;

import feuille.drawing.shape.AShape;
import feuille.drawing.shape.FeuillePoint;
import feuille.drawing.shape.ShapeType;
import feuille.util.DrawColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author util2
 */
public class FeuilleQuadCurve extends AShape implements Cloneable {
    
    private Color pointColor = Color.blue;
    private Color lineColor = DrawColor.medium_slate_blue.getColor();
    private Color controlPointColor = DrawColor.lavender.getColor();
    
    public FeuilleQuadCurve() {
        shapeType = ShapeType.QuadraticCurve;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();  	
    }
    
    public static FeuilleQuadCurve create(Point2D start, Point2D end){
        FeuilleQuadCurve s = new FeuilleQuadCurve();
        s.points.add(FeuillePoint.create(FeuillePoint.FeuillePointType.Start, 0, start));
        s.points.add(FeuillePoint.create(FeuillePoint.FeuillePointType.End, 1, end));
        s.points.add(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint, 2, retrieveControlPoint(start, end)));
        return s;
    }
    
    public static Point2D retrieveControlPoint(Point2D start, Point2D end){
        double xdiff = end.getX() - start.getX();
        double ydiff = end.getY() - start.getY();

        double x1_2 = start.getX() + xdiff/2;
        double y1_2 = start.getY() + ydiff/2;
        
        return new Point2D.Double(x1_2, y1_2);
    }

    @Override
    public boolean isAtPoint(FeuillePoint fp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void selectShape(Point2D p2d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw(Graphics2D g2d, Dimension render, float scale) {
        // Example of something that works : x * scale + getWidth() / 2
        Point2D start = new Point2D.Double(
                points.get(0).getPoint().getX() * scale + render.getWidth() / 2,
                points.get(0).getPoint().getY() * scale + render.getHeight()/ 2);
        Point2D end = points.size() > 1 ? new Point2D.Double(
                points.get(1).getPoint().getX() * scale + render.getWidth() / 2,
                points.get(1).getPoint().getY() * scale + render.getHeight()/ 2) : null;
        Point2D cp = points.size() > 2 ? new Point2D.Double(
                points.get(2).getPoint().getX() * scale + render.getWidth() / 2,
                points.get(2).getPoint().getY() * scale + render.getHeight()/ 2) : end != null ? retrieveControlPoint(start, end) : null;
        
        if(end != null && cp != null){
            QuadCurve2D quad = new QuadCurve2D.Double(
                    start.getX(),
                    start.getY(),
                    cp.getX(),
                    cp.getY(),
                    end.getX(),
                    end.getY());
            
            g2d.setColor(lineColor);
            g2d.draw(quad);
        }
        
        Rectangle2D rStart = new Rectangle2D.Double(
                start.getX() - 3d,
                start.getY() - 3d,
                6d,
                6d);
        
        g2d.setColor(pointColor);
        g2d.fill(rStart);
        
        if(cp != null){
             Ellipse2D eCP = new Ellipse2D.Double(
                    cp.getX() - 3d,
                    cp.getY() - 3d,
                    6d,
                    6d);
            
            g2d.setColor(controlPointColor);
            g2d.fill(eCP);
        }
        
        if(end != null){
             Rectangle2D rEnd = new Rectangle2D.Double(
                    end.getX() - 3d,
                    end.getY() - 3d,
                    6d,
                    6d);
            
            g2d.setColor(pointColor);
            g2d.fill(rEnd);
        }
    }
    
}
