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

import feuille.MainFrame;
import feuille.util.ISO_3166;
import feuille.util.Language;

/**
 *
 * @author util2
 */
public enum ShapeType {
    Unknown("X"),
    Group("Group"),
    Circle("Circle"),
    Oval("Oval"),
    Rectangle("Rectangle"),
    RoundedRectangle("RoundedRectangle"),
    Square("Square"),
    Line("Line"),
    CubicCurve("CubicCurve"),
    QuadraticCurve("QuadraticCurve"),
    SplineCurve("SplineCurve");
    
    String name;
    Language language = MainFrame.getLanguage();
    ISO_3166 iso = MainFrame.getISOCountry();
    
    private ShapeType(String name){
        switch(name){
            case "Group":
                this.name = language.getTranslated("ShapeTypeGroup", iso, "Group");
                break;
            case "Circle":
                this.name = language.getTranslated("ShapeTypeCircle", iso, "Circle");
                break;
            case "Oval":
                this.name = language.getTranslated("ShapeTypeOval", iso, "Oval");
                break;
            case "Rectangle":
                this.name = language.getTranslated("ShapeTypeRectangle", iso, "Rectangle");
                break;
            case "RoundedRectangle":
                this.name = language.getTranslated("ShapeTypeRoundedRectangle", iso, "RoundedRectangle");
                break;
            case "Square":
                this.name = language.getTranslated("ShapeTypeSquare", iso, "Square");
                break;
            case "Line":
                this.name = language.getTranslated("ShapeTypeLine", iso, "Line");
                break;
            case "CubicCurve":
                this.name = language.getTranslated("ShapeTypeCubicCurve", iso, "Cubic curve");
                break;
            case "QuadraticCurve":
                this.name = language.getTranslated("ShapeTypeQuadraticCurve", iso, "Quadratic curve");
                break;
            case "SplineCurve":
                this.name = language.getTranslated("ShapeTypeSplineCurve", iso, "Spline curve");
                break;
            default:
                this.name = name;
                break;
        }        
    }
    
    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
