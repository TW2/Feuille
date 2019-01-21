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
 *
 * @author util2
 */
public class Distortion extends AFx {
    
    int u1 = 0, v1 = 0;
    int u2 = 0, v2 = 0;
    int u3 = 0, v3 = 0;

    public Distortion() {
        
    }
    
    @Override
    public String getTag() {
        return "\\distort("
                + u1 + "," + v1 + ","
                + u2 + "," + v2 + ","
                + u3 + "," + v3 + ")";
    }

    public void setU1(int u1) {
        this.u1 = u1;
    }

    public int getU1() {
        return u1;
    }

    public void setV1(int v1) {
        this.v1 = v1;
    }

    public int getV1() {
        return v1;
    }

    public void setU2(int u2) {
        this.u2 = u2;
    }

    public int getU2() {
        return u2;
    }

    public void setV2(int v2) {
        this.v2 = v2;
    }

    public int getV2() {
        return v2;
    }

    public void setU3(int u3) {
        this.u3 = u3;
    }

    public int getU3() {
        return u3;
    }

    public void setV3(int v3) {
        this.v3 = v3;
    }

    public int getV3() {
        return v3;
    }
    
}
