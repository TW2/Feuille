/*
 * Copyright (C) 2017 Naruto
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

import java.awt.Shape;
import java.awt.geom.Area;

/**
 *
 * @author Naruto
 */
public class BooleanOp {

    public BooleanOp() {
    }
    
    public static Shape getUnion(Shape one, Shape two){
        Area a1 = new Area(one);
        Area a2 = new Area(two);
        a1.add(a2);
        return a1;
    }
    
    public static Shape getSubstract(Shape one, Shape two){
        Area a1 = new Area(one);
        Area a2 = new Area(two);
        a1.subtract(a2);
        return a1;
    }
    
    public static Shape getIntersect(Shape one, Shape two){
        Area a1 = new Area(one);
        Area a2 = new Area(two);
        a1.intersect(a2);
        return a1;
    }
    
    public static Shape getXOR(Shape one, Shape two){
        Area a1 = new Area(one);
        Area a2 = new Area(two);
        a1.exclusiveOr(a2);
        return a1;
    }
    
}
