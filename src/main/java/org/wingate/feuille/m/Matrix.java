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

/**
 *
 * @author util2
 */
public class Matrix {
    private final int maxWCases = 35;
    private final int maxHCases = 35;
        
    private int wCases = 1;
    private int hCases = 1;

    public Matrix() {
    }

    public int getWCases() {
        return wCases;
    }

    public void setWCases(int wCases) {
        if(wCases < 1) wCases = 1;
        if(wCases > maxWCases) wCases = maxWCases;
        this.wCases = wCases;
    }

    public int getHCases() {
        return hCases;
    }

    public void setHCases(int hCases) {
        if(hCases < 1) hCases = 1;
        if(hCases > maxHCases) hCases = maxHCases;
        this.hCases = hCases;
    }

    public int getMaxWCases() {
        return maxWCases;
    }

    public int getMaxHCases() {
        return maxHCases;
    }
}
