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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author util2
 */
public class FeuilleLine extends AShape implements Cloneable {
    
    private Color pointColor = Color.blue;
    private Color lineColor = Color.red;
    
    public FeuilleLine() {
        shapeType = ShapeType.Line;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();  	
    }
    
    public static FeuilleLine create(Point2D start, Point2D end){
        FeuilleLine line = new FeuilleLine();
        line.points.add(FeuillePoint.create(FeuillePoint.FeuillePointType.Start, 0, start));
        line.points.add(FeuillePoint.create(FeuillePoint.FeuillePointType.End, 1, end));
        return line;
    }
    
    public boolean isAtPoint(Point2D p2d) {
        // Retrieve start point and check it
        FeuillePoint start = getPoint(FeuillePoint.FeuillePointType.Start);
        if(start.getPoint().equals(p2d)){
            return true;
        }
        
        // Retrieve start point and check it
        FeuillePoint end = getPoint(FeuillePoint.FeuillePointType.End);
        if(end.getPoint().equals(p2d)){
            return true;
        }
        
        // Soit une droite AB.
        // Coordonnées : A (-2 et 0) et B (0 et -2)
        // (y2 - y1) : -2 - 0 = -2 (Numérateur = -2)
        // (x2 - x1) : 0 - (-2) = 2 (Dénominateur = 2)
        // Pente de la droite AB = Numérateur/Dénominateur = -1.
        double num = end.getPoint().getY() - start.getPoint().getY();
        double den = end.getPoint().getX() - start.getPoint().getX();
        // y = ax + b où b = 0
        double a = num / den;
        // y1 = a*x1 + b
        double b = end.getPoint().getY() - (a * end.getPoint().getX());
        
        // Calcul pour la droite demandée pour O = 0 et A = p2d 
        double num_ab = p2d.getY();
        double den_ab = p2d.getX();
        double aSearched = num_ab / den_ab;
        double bSearched = p2d.getY() - (aSearched * p2d.getX());
        
        return a == aSearched && b == bSearched;
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

    @Override
    public boolean isAtPoint(FeuillePoint fp) {
        return isAtPoint(fp.getPoint());
    }

    @Override
    public void selectShape(Point2D p2d) {
        // Rectangle that contains searched point
        Rectangle2D r = new Rectangle2D.Double(p2d.getX()-3d, p2d.getY()-3d, 6d, 6d);
        
        // We want to search for line in this rectangle
        boolean isOn = false; // State of search
        Point2D onP2D = new Point2D.Double(0d, 0d);
        for(double x = r.getMinX(); x<r.getMaxX(); x++){
            for(double y = r.getMinY(); y<r.getMaxY(); y++){
                Point2D np = new Point2D.Double(x, y);
                if(isAtPoint(np) == true){
                    isOn = true;
                    onP2D = new Point2D.Double(x, y);
                    break;
                }
            }
        }
        
        // We have not found a line at this point
        if(isOn == false){
            selected.reset();                   // <-- Unknown GLUE
            return;
        }
        
        // Calculation of distance and assign closest
        // For ease, let's call A the start and B the end and Z the onP2D
        Point2D A = points.get(0).getPoint();   // <-- Start
        Point2D B = points.get(1).getPoint();   // <-- End
        Point2D Z = onP2D;                      // <-- onP2D        
        double AZ = Point2D.distance(A.getX(), A.getY(), Z.getX(), Z.getY());
        double BZ = Point2D.distance(B.getX(), B.getY(), Z.getX(), Z.getY());
        
        if(AZ >= BZ){
            // B is closer
            selected.setGlueAt(points.get(1));
        }else{
            // A is closer
            selected.setGlueAt(points.get(0));
        }
    }
    
}
