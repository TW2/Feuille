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
package feuille.util.effect;

/**
 *
 * @author util2
 */
public enum OldAlignment {
    BottomLeft(1),
    BottomCenter(2),
    BottomRight(3),
    TopLeft(5),
    TopCenter(6),
    TopRight(7),
    MiddleLeft(9),
    MiddleCenter(10),
    MiddleRight(11);
        
    int value;
    
    private OldAlignment(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
    
    public static OldAlignment fromValue(int value){
        OldAlignment old = BottomCenter;
        for(OldAlignment a : values()){
            if(a.getValue() == value){
                old = a;
                break;
            }
        }
        return old;
    }
}
