/*
 * Copyright (C) 2024 util2
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
package org.wingate.feuille.util;

import java.awt.geom.Point2D;

/**
 *
 * @author util2
 */
public class Geometry {

    public Geometry() {
        
    }
    
    /**
     * Get the percent between to point (which can be calculated with x or y).
     * If the origin is zero then the calculation is calibrate with origin equals to one to avoid division by zero.
     * @param distance a distance point (x or y)
     * @param origin an origin point (respectively x or y)
     * @return a percent of the distance between this two coordinates
     */
    public static double getPercent(double distance, double origin){
        return 100d * distance / (origin == 0d ? 1d : origin);
    }
    
    /**
     * Get angle from center
     * @param C center
     * @param P point
     * @return an angle in radians
     */
    public static double getAngle(Point2D C, Point2D P){
        double tanC = (P.getY() - C.getY()) / (P.getX() - C.getX());
        double angleC = Math.atan(tanC); // radians
        
        if(P.getX() - C.getX() > 0 && P.getY() - C.getY() < 0){
            angleC = Math.atan(tanC) + 2 * Math.PI;
        }
        if(P.getX() - C.getX() < 0){
            angleC = Math.atan(tanC) + Math.PI;
        }
        
        return angleC;
    }
    
    /**
     * Resize with a distance in percent
     * @param C center point or the last point in a path
     * @param P point to transform
     * @param percent transformation in percent
     * @return A new point P'
     */
    public static Point2D resize(Point2D C, Point2D P, double percent){
        // If P and C is the same point, there is nothing to calculate
        if(P.equals(C)) return P;
        
        double CP = Point2D.distance(C.getX(), C.getY(), P.getX(), P.getY());
        double CPprime = CP * percent / 100d;
        
        double angleC = getAngle(C, P);
        
        double xPprime = CPprime * Math.cos(angleC) + C.getX();
        double yPprime = CPprime * Math.sin(angleC) + C.getY();
        
        return new Point2D.Double(xPprime, yPprime);
    }
    
    /**
     * Rotate a point
     * @param C center or current ref point
     * @param P point
     * @param angle angle to apply
     * @return a new point
     */
    public static Point2D rotate(Point2D C, Point2D P, double angle){
        double CP = Math.sqrt(
                Math.pow(P.getX() - C.getX(), 2d) + Math.pow(P.getY() - C.getY(), 2d)
        );
        
        double angleC = getAngle(C, P);
        
        double xPprime = CP * Math.cos(angleC + angle) + C.getX();
        double yPprime = CP * Math.sin(angleC + angle) + C.getY();
        
        return new Point2D.Double(xPprime, yPprime);
    }
    
    /**
     * Tanslate a point from another one
     * @param C source point
     * @param D distant point
     * @return a new point by applying distance of the comparison of this two points
     */
    public static Point2D translate(Point2D C, Point2D D){
        double xPprime = C.getX() + D.getX();
        double yPprime = C.getY() + D.getY();
        return new Point2D.Double(xPprime, yPprime);
    }
}
