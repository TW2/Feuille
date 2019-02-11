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
import feuille.util.effect.Parameter;

/**
 * \\moves4(x1,y1,x2,y2,x3,y3,x4,y4[,t1,t2])
 * @author util2
 */
public class CubicMovement extends AFx {
    
    Parameter p_msStart = new Parameter(0, in.getTranslated("FxListMsStart", iso, "Start time"));
    Parameter p_msStop = new Parameter(0, in.getTranslated("FxListMsStop", iso, "End time"));
    
    Parameter p_startPointX = new Parameter(0, in.getTranslated("FxListStartPointX", iso, "X at start"));
    Parameter p_startPointY = new Parameter(0, in.getTranslated("FxListStartPointY", iso, "Y at start"));
    
    Parameter p_controlPointX1 = new Parameter(0, in.getTranslated("FxListControlPointX1", iso, "X for first control point"));
    Parameter p_controlPointY1 = new Parameter(0, in.getTranslated("FxListControlPointY1", iso, "Y for first control point"));
    
    Parameter p_controlPointX2 = new Parameter(0, in.getTranslated("FxListControlPointX2", iso, "X for second control point"));
    Parameter p_controlPointY2 = new Parameter(0, in.getTranslated("FxListControlPointY2", iso, "Y for second control point"));
    
    Parameter p_endPointX = new Parameter(0, in.getTranslated("FxListEndPointX", iso, "X at stop"));
    Parameter p_endPointY = new Parameter(0, in.getTranslated("FxListEndPointY", iso, "Y at stop"));

    public CubicMovement() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListCubicMovement", iso, "Cubic movement");
        uniqueID = -1;
        params.add(p_msStart);
        params.add(p_msStop);
        params.add(p_startPointX);
        params.add(p_startPointY);
        params.add(p_controlPointX1);
        params.add(p_controlPointY1);
        params.add(p_controlPointX2);
        params.add(p_controlPointY2);
        params.add(p_endPointX);
        params.add(p_endPointY);
    }

    public void setMsStart(int msStart) {
        p_msStart.setParam(msStart);
        params.set(0, p_msStart);
    }

    public int getMsStart() {
        return (int)params.get(0).getParam();
    }

    public void setMsStop(int msStop) {
        p_msStop.setParam(msStop);
        params.set(1, p_msStop);
    }

    public int getMsStop() {
        return (int)params.get(1).getParam();
    }
    
    public void setStartPointX(int startPointX) {
        p_startPointX.setParam(startPointX);
        params.set(2, p_startPointX);
    }

    public int getStartPointX() {
        return (int)params.get(2).getParam();
    }

    public void setStartPointY(int startPointY) {
        p_startPointY.setParam(startPointY);
        params.set(3, p_startPointY);
    }

    public int getStartPointY() {
        return (int)params.get(3).getParam();
    }

    public void setControlPointX1(int controlPointX1) {
        p_controlPointX1.setParam(controlPointX1);
        params.set(4, p_controlPointX1);
    }

    public int getControlPointX1() {
        return (int)params.get(4).getParam();
    }

    public void setControlPointY1(int controlPointY1) {
        p_controlPointY1.setParam(controlPointY1);
        params.set(5, p_controlPointY1);
    }

    public int getControlPointY1() {
        return (int)params.get(5).getParam();
    }

    public void setControlPointX2(int controlPointX2) {
        p_controlPointX2.setParam(controlPointX2);
        params.set(6, p_controlPointX2);
    }

    public int getControlPointX2() {
        return (int)params.get(6).getParam();
    }

    public void setControlPointY2(int controlPointY2) {
        p_controlPointY2.setParam(controlPointY2);
        params.set(7, p_controlPointY2);
    }

    public int getControlPointY2() {
        return (int)params.get(7).getParam();
    }

    public void setEndPointX(int endPointX) {
        p_endPointX.setParam(endPointX);
        params.set(8, p_endPointX);
    }

    public int getEndPointX() {
        return (int)params.get(8).getParam();
    }

    public void setEndPointY(int endPointY) {
        p_endPointY.setParam(endPointY);
        params.set(9, p_endPointY);
    }

    public int getEndPointY() {
        return (int)params.get(9).getParam();
    }

    @Override
    public String getTag() {
        return "\\moves4("
                + getStartPointX() + "," + getStartPointY() + ","
                + getControlPointX1() + "," + getControlPointY1() + ","
                + getControlPointX2() + "," + getControlPointY2() + ","
                + getEndPointX() + "," + getEndPointY()
                + ((getMsStart() != getMsStop()) ? "," + getMsStart() + "," + getMsStop() : "") + ")";
    }
    
    
}
