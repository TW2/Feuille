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

import feuille.drawing.shape.vector.FeuilleBSplineCurve;
import feuille.drawing.shape.vector.FeuilleCubicCurve;
import feuille.drawing.shape.vector.FeuilleLine;
import feuille.drawing.shape.vector.FeuilleQuadCurve;
import java.awt.geom.Point2D;

/**
 *
 * @author util2
 */
public class FeuilleShapeInvokator {

    public FeuilleShapeInvokator() {
    }
    
    public static AShape createShape(ShapeType type){
        AShape as = null;
        
        switch(type){
            case Group:
                break;
            case Circle:
                break;
            case Oval:
                break;
            case Rectangle:
                break;
            case RoundedRectangle:
                break;
            case Square:
                break;
            case Line:
                as = new FeuilleLine();
                break;
            case QuadraticCurve:
                as = new FeuilleQuadCurve();
                break;
            case CubicCurve:
                as = new FeuilleCubicCurve();
                break;
            case SplineCurve:
                as = new FeuilleBSplineCurve();
                break;
            default:
                break;
        }
        
        return as;
    }
    
    public static AShape createShape(ShapeType type, Point2D start){
        AShape as = createShape(type);
        
        as.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.Start, 0, start));
        
        return as;
    }
    
    public static AShape createShape(ShapeType type, Point2D start, Point2D end){        
        AShape as = createShape(type, start);
        
        as.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.End, 1, end));
        
        return as;
    }
    
    public static AShape changeShapeType(AShape sh, ShapeType type, Point2D joker){
        if(joker == null && sh.getPoints().isEmpty() == true) { return sh; }
        if(joker == null && sh.getPoints().isEmpty() == false) { joker = sh.getPoints().get(0).getPoint(); }
        AShape as = createShape(
                type, 
                sh.getPoints().size() > 0 ? sh.getPoints().get(0).getPoint() : joker, 
                sh.getPoints().size() > 1 ? sh.getPoints().get(1).getPoint() : joker);
        
        if(type == ShapeType.QuadraticCurve){
            FeuillePoint fpStart = sh.getPoint(FeuillePoint.FeuillePointType.Start);
            FeuillePoint fpEnd = sh.getPoint(FeuillePoint.FeuillePointType.End);
            Point2D start = fpStart != null ? fpStart.getPoint() : joker;
            Point2D end = fpEnd != null ? fpEnd.getPoint() : joker;
            as.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint, 2, FeuilleQuadCurve.retrieveControlPoint(start, end)));
        }
        
        if(type == ShapeType.CubicCurve){
            FeuillePoint fpStart = sh.getPoint(FeuillePoint.FeuillePointType.Start);
            FeuillePoint fpEnd = sh.getPoint(FeuillePoint.FeuillePointType.End);
            Point2D start = fpStart != null ? fpStart.getPoint() : joker;
            Point2D end = fpEnd != null ? fpEnd.getPoint() : joker;
            as.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint1, 2, FeuilleCubicCurve.retrieveControlPoint1(start, end)));
            as.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint2, 3, FeuilleCubicCurve.retrieveControlPoint2(start, end)));
        }
        
        return as;                
    }
}
