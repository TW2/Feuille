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
 *
 * @author util2
 */
public class QuadraticMovement extends AFx {
    
    int msStart = 0;
    int msStop = 0;
    
    int startPointX = 0, startPointY = 0;
    int controlPointX = 0, controlPointY = 0;
    int endPointX = 0, endPointY = 0;

    public QuadraticMovement() {
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

    public void setControlPointX(int controlPointX) {
        this.controlPointX = controlPointX;
    }

    public int getControlPointX() {
        return controlPointX;
    }

    public void setControlPointY(int controlPointY) {
        this.controlPointY = controlPointY;
    }

    public int getControlPointY() {
        return controlPointY;
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
        return "\\moves3("
                + startPointX + "," + startPointY + ","
                + controlPointX + "," + controlPointY + ","
                + endPointX + "," + endPointY
                + (msStart != msStop ? "," + msStart + "," + msStop : "") + ")";
    }
}
