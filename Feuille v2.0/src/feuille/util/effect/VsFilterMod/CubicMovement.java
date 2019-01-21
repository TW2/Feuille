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
 * \\moves4(x1,y1,x2,y2,x3,y3,x4,y4[,t1,t2])
 * @author util2
 */
public class CubicMovement extends AFx {
    
    int msStart = 0;
    int msStop = 0;
    
    int startPointX = 0, startPointY = 0;
    int controlPointX1 = 0, controlPointY1 = 0;
    int controlPointX2 = 0, controlPointY2 = 0;
    int endPointX = 0, endPointY = 0;

    public CubicMovement() {
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
    
    public void setStartPointX(int startPointX) {
        this.startPointX = startPointX;
    }

    public int getStartPointX() {
        return startPointX;
    }

    public void setStartPointY(int startPointY) {
        this.startPointY = startPointY;
    }

    public int getStartPointY() {
        return startPointY;
    }

    public void setControlPointX1(int controlPointX1) {
        this.controlPointX1 = controlPointX1;
    }

    public int getControlPointX1() {
        return controlPointX1;
    }

    public void setControlPointY1(int controlPointY1) {
        this.controlPointY1 = controlPointY1;
    }

    public int getControlPointY1() {
        return controlPointY1;
    }

    public void setControlPointX2(int controlPointX2) {
        this.controlPointX2 = controlPointX2;
    }

    public int getControlPointX2() {
        return controlPointX2;
    }

    public void setControlPointY2(int controlPointY2) {
        this.controlPointY2 = controlPointY2;
    }

    public int getControlPointY2() {
        return controlPointY2;
    }

    public void setEndPointX(int endPointX) {
        this.endPointX = endPointX;
    }

    public int getEndPointX() {
        return endPointX;
    }

    public void setEndPointY(int endPointY) {
        this.endPointY = endPointY;
    }

    public int getEndPointY() {
        return endPointY;
    }

    @Override
    public String getTag() {
        return "\\moves4("
                + startPointX + "," + startPointY + ","
                + controlPointX1 + "," + controlPointY1 + ","
                + controlPointX2 + "," + controlPointY2 + ","
                + endPointX + "," + endPointY
                + (msStart != msStop ? "," + msStart + "," + msStop : "") + ")";
    }
    
    
}
