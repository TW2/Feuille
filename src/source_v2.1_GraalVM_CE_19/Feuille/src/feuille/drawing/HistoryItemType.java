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
package feuille.drawing;

import feuille.MainFrame;
import feuille.drawing.shape.ShapeType;
import feuille.util.ISO_3166;
import feuille.util.Language;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author util2
 */
public enum HistoryItemType {    
    Line,
    Quad,
    Cubic,
    BSpline;

    private HistoryItemType() {
    }
    
    public ImageIcon getIcon(){
        // Create null icon to avoid null value
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.blue);
        g2d.fillRect(0, 0, 32, 32);
        g2d.dispose();
        ImageIcon icon = new ImageIcon(image);
        
        switch(this){
            case Line:
                icon = new ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingLine.png"));
                break;
            case Quad:
                icon = new ImageIcon(getClass().getResource("/feuille/images/afm splines 03.png"));
                break;
            case Cubic:
                icon = new ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBezier.png"));
                break;
            case BSpline:
                icon = new ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBSpline.png"));
                break;
        }
        
        return icon;
    }

    @Override
    public String toString() {
        String s = "Object";
        Language lang = MainFrame.getLanguage();
        ISO_3166 iso = MainFrame.getISOCountry();
        
        switch(this){
            case Line:
                s = lang.getTranslated("DrawingObjectLine", iso, "Line");
                break;
            case Quad:
                s = lang.getTranslated("DrawingObjectQuadratic", iso, "Quadratic curve");
                break;
            case Cubic:
                s = lang.getTranslated("DrawingObjectCubic", iso, "Cubic curve");
                break;
            case BSpline:
                s = lang.getTranslated("DrawingObjectBSpline", iso, "BSpline curve");
                break;
        }
        
        return s;
    }
    
    public static HistoryItemType getFromVector(ShapeType type){
        HistoryItemType hit = null;
        
        switch(type){
            case Group:             break;
            case Circle:            break;
            case Oval:              break;
            case Rectangle:         break;
            case RoundedRectangle:  break;
            case Square:            break;
            case Line:              hit = Line;                 break;
            case QuadraticCurve:    hit = Quad;                 break;
            case CubicCurve:        hit = Cubic;                break;
            case SplineCurve:       hit = BSpline;              break;
            default:                hit = Line;                 break;
        }
        
        return hit;
    }
    
}
