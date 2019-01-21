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
package feuille.util.effect.VsFilterMod;

import feuille.util.effect.AFx;

/**
 * Shaking
 * \jitter(left,right,up,down,period[,seed])
 * @author util2
 */
public class Shaking extends AFx {
    
    int left = 0;
    int right = 0;
    int up = 0;
    int down = 0;
    int period = 0;
    int seed = 10;
    boolean seeded = false;    

    public Shaking() {
        
    }

    @Override
    public String getTag() {
        return "\\jitter(" + left + "," + right + "," + up + "," + down + "," + period + (seeded == true ? "," + seed : "") + ")";
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getLeft() {
        return left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getRight() {
        return right;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getUp() {
        return up;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getDown() {
        return down;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeeded(boolean seeded) {
        this.seeded = seeded;
    }

    public boolean isSeeded() {
        return seeded;
    }
    
}
