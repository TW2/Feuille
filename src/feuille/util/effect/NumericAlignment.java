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
public enum NumericAlignment {
    BottomLeft(1),
    BottomCenter(2),
    BottomRight(3),
    MiddleLeft(4),
    MiddleCenter(5),
    MiddleRight(6),
    TopLeft(7),
    TopCenter(8),
    TopRight(9);
        
    int value;
    
    private NumericAlignment(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
    
    public static NumericAlignment fromValue(int value){
        NumericAlignment val = BottomCenter;
        for(NumericAlignment an : values()){
            if(an.getValue() == value){
                val = an;
                break;
            }
        }
        return val;
    }
}
