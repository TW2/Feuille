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
package org.wingate.feuille.m;

import java.awt.geom.Point2D;

/**
 *
 * @author util2
 */
public class Geometry {

    public Geometry() {
    }
    
    /**
     * Calcul d'un point P' résultant d'un agrandissement ou d'un rétrécissement
     * initié par une distance issue d'un pourcentage.
     * @param S point de référence
     * @param P point avant transformation
     * @param percent pourcentage allant de 0d = 0% à 1d = 100%
     * @return un point P' résultant de l'opération géométrique de la taille
     */
    public static Point2D resize(Point2D S, Point2D P, double percent){
        // Si P et S sont aux mêmes coordonnées
        // alors il n'y a pas de calcul possible
        if(P.equals(S)) return P;
        
        // Distance SP - distance pour calculer le nouveau point
        double SP = Point2D.distance(S.getX(), S.getY(), P.getX(), P.getY());
        
        // Distance SP' - distance avec inclusion du pourcentage de taille
        double SPprime = SP * percent;
        
        // On calcule l'angle S en radians afin de savoir où restituer le point P'
        double tanS = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double angleS = Math.atan(tanS);
        
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            angleS = Math.atan(tanS);
        }
        
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            angleS = Math.atan(tanS) + 2 * Math.PI;
        }
        
        if(P.getX() - S.getX() < 0){
            angleS = Math.atan(tanS) + Math.PI;
        }
        
        // On calcul le nouveau point P' avec la distance et l'angle
        // et on y ajoute le point de référence (qui sert d'origine)
        double xPprime = SPprime * Math.cos(angleS) + S.getX();
        double yPprime = SPprime * Math.sin(angleS) + S.getX();
        
        return new Point2D.Double(xPprime, yPprime);
    }
    
    /**
     * Calcul d'un point P' résultant d'un agrandissement ou d'un rétrécissement
     * initié par une distance en pixels.
     * @param S point de référence
     * @param P point avant transformation
     * @param pixels distance pour la transformation
     * @return un point P' résultant de l'opération géométrique de la taille
     */
    public static Point2D resize(Point2D S, Point2D P, float pixels){
        // Si P et S sont aux mêmes coordonnées
        // alors il n'y a pas de calcul possible
        if(P.equals(S)) return P;
        
        // Distance SP - distance pour calculer le nouveau point
        double SP = Point2D.distance(S.getX(), S.getY(), P.getX(), P.getY());
        
        // Distance SP' - distance voulue pour calculer la transformation
        double SPprime = SP * (1);// TODO Point to pixels
        
        // On calcule l'angle S en radians afin de savoir où restituer le point P'
        double tanS = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double angleS = Math.atan(tanS);
        
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            angleS = Math.atan(tanS);
        }
        
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            angleS = Math.atan(tanS) + 2 * Math.PI;
        }
        
        if(P.getX() - S.getX() < 0){
            angleS = Math.atan(tanS) + Math.PI;
        }
        
        // On calcul le nouveau point P' avec la distance et l'angle
        // et on y ajoute le point de référence (qui sert d'origine)
        double xPprime = SPprime * Math.cos(angleS) + S.getX();
        double yPprime = SPprime * Math.sin(angleS) + S.getX();
        
        return new Point2D.Double(xPprime, yPprime);
    }
    
    /**
     * Calcul d'un point P' résultant d'une rotation selon un angle.
     * @param S point de référence
     * @param P point avant transformation
     * @param angle angle exprimé en degrées
     * @return un point P' résultant de l'opération géométrique de la rotation
     */
    public static Point2D rotate(Point2D S, Point2D P, double angle){
        
        // Calcul de la distance
        double SPprime = Math.sqrt(Math.pow(P.getX() - S.getX(), 2) + Math.pow(P.getY() - S.getY(), 2));
        
        // Calcul de l'angle
        double tanPSN = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double anglePSN = Math.atan(tanPSN);
        
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            anglePSN = Math.atan(tanPSN);
        }
        
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            anglePSN = Math.atan(tanPSN) + 2 * Math.PI;
        }
        
        if(P.getX() - S.getX() < 0){
            anglePSN = Math.atan(tanPSN) + Math.PI;
        }
        
        // On calcul le nouveau point P' avec la distance et l'angle
        // et on y ajoute le point de référence (qui sert d'origine)
        double xPprime = SPprime * Math.cos(anglePSN + Math.toRadians(angle)) + S.getX();
        double yPprime = SPprime * Math.sin(anglePSN + Math.toRadians(angle)) + S.getX();
        
        return new Point2D.Double(xPprime, yPprime);
    }
    
    /**
     * Calcul d'un point P' résultant d'une translation.
     * @param O point à transformer
     * @param Ppixels point de distance voulue
     * @return un point P' résultant de l'opération géométrique de la translation
     */
    public static Point2D translate(Point2D O, Point2D Ppixels){
        return new Point2D.Double(O.getX() + Ppixels.getX(), O.getY() + Ppixels.getY());
    }
    
    // TODO SHEAR (Etirement)
}
