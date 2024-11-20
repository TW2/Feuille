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
package org.wingate.feuille.subs.ass;

/**
 *
 * @author util2
 */
public class AssAlignment implements Cloneable {
    private int number;

    public AssAlignment(int number) {
        this.number = number;
    }

    public AssAlignment() {
        number = 2;
    }
    
    @Override
    protected AssAlignment clone() throws CloneNotSupportedException {
        return (AssAlignment) super.clone();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public static AssAlignment fromSSA(int ssa){
        AssAlignment ass;
        switch(ssa){
            case 1 -> { ass = new AssAlignment(1); }
            case 2 -> { ass = new AssAlignment(2); }
            case 3 -> { ass = new AssAlignment(3); }
            case 5 -> { ass = new AssAlignment(7); }
            case 6 -> { ass = new AssAlignment(8); }
            case 7 -> { ass = new AssAlignment(9); }
            case 9 -> { ass = new AssAlignment(4); }
            case 10 -> { ass = new AssAlignment(5); }
            case 11 -> { ass = new AssAlignment(6); }
            default -> { ass = new AssAlignment(2); }
        }
        return ass;
    }
    
    public static int toSSA(AssAlignment ass){
        int ssa;
        switch(ass.getNumber()){
            case 1 -> { ssa = 1; }
            case 2 -> { ssa = 2; }
            case 3 -> { ssa = 3; }
            case 4 -> { ssa = 9; }
            case 5 -> { ssa = 10; }
            case 6 -> { ssa = 11; }
            case 7 -> { ssa = 5; }
            case 8 -> { ssa = 6; }
            case 9 -> { ssa = 7; }
            default -> { ssa = 2; }
        }
        return ssa;
    }
}
