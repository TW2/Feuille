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
import feuille.util.effect.FxType;

/**
 * \\mover(x1,y1,x2,y2,angle1,angle2,radius1,radius2[,t1,t2])
 * @author util2
 */
public class PolarMovement extends AFx {
    
    int msStart = 0;
    int msStop = 0;
    
    int x1 = 0, y1 = 0;
    int x2 = 0, y2 = 0;
    
    int angle1 = 0;
    int angle2 = 0;
    
    int radius1 = 0;
    int radius2 = 0;

    public PolarMovement() {
        fxType = FxType.Override;
    }

    public void setMsStart(int msStart) {
        this.msStart = msStart;
    }

    public int getMsStart() {
        return msStart;
    }

    public void setMsStop(int msStop) {
        this.msStop = msStop;
    }

    public int getMsStop() {
        return msStop;
    }
    
    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX1() {
        return x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY1() {
        return y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getX2() {
        return x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getY2() {
        return y2;
    }

    public void setAngle1(int angle1) {
        this.angle1 = angle1;
    }

    public int getAngle1() {
        return angle1;
    }

    public void setAngle2(int angle2) {
        this.angle2 = angle2;
    }

    public int getAngle2() {
        return angle2;
    }

    public void setRadius1(int radius1) {
        this.radius1 = radius1;
    }

    public int getRadius1() {
        return radius1;
    }

    public void setRadius2(int radius2) {
        this.radius2 = radius2;
    }

    public int getRadius2() {
        return radius2;
    }

    @Override
    public String getTag() {
        return "\\mover("
                + x1 + "," + y1 + ","
                + x2 + "," + y2 + ","
                + angle1 + "," + angle2 + ","
                + radius1 + "," + radius2
                + (msStart != msStop ? "," + msStart + "," + msStop : "") + ")";
    }
        
}
