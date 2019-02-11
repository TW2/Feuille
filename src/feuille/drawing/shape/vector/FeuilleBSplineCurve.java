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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author util2
 */
public class FeuilleBSplineCurve extends AShape implements Cloneable {
    
    private Color pointColor = Color.blue;
    private Color lineColor = DrawColor.dark_green.getColor();
    private Color controlPointColor = DrawColor.chocolate.getColor();
    
    public FeuilleBSplineCurve() {
        shapeType = ShapeType.SplineCurve;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();  	
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
                points.get(points.size()-1).getPoint().getX() * scale + render.getWidth() / 2,
                points.get(points.size()-1).getPoint().getY() * scale + render.getHeight()/ 2) : null;
        
        List<Point2D> controlPoints = new ArrayList<>();
        for(int i=0; i<points.size(); i++){
            if(i != 0 && i != points.size() - 1){
                Point2D cp = new Point2D.Double(
                        points.get(i).getPoint().getX() * scale + render.getWidth() / 2,
                        points.get(i).getPoint().getY() * scale + render.getHeight()/ 2);
                controlPoints.add(cp);
            }
        }
        
        if(points.size() > 1){
            Line2D line = new Line2D.Double(start, end);
            
            g2d.setColor(lineColor);
            g2d.draw(line);
        }
        
        Rectangle2D rStart = new Rectangle2D.Double(
                start.getX() - 3d,
                start.getY() - 3d,
                6d,
                6d);
        
        g2d.setColor(pointColor);
        g2d.fill(rStart);
        
        if(controlPoints.isEmpty() == false){
            g2d.setColor(controlPointColor);
            for(Point2D p2d : controlPoints){
                Ellipse2D eCP = new Ellipse2D.Double(
                    p2d.getX() - 3d,
                    p2d.getY() - 3d,
                    6d,
                    6d);
                
                g2d.fill(eCP);
            }
            
        }
        
        if(points.size() > 1 && end != null){
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
