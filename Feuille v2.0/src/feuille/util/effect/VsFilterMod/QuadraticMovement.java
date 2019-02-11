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
 *
 * @author util2
 */
public class QuadraticMovement extends AFx {
    
    Parameter p_msStart = new Parameter(0, in.getTranslated("FxListMsStart", iso, "Start time"));
    Parameter p_msStop = new Parameter(0, in.getTranslated("FxListMsStop", iso, "End time"));
    
    Parameter p_startPointX = new Parameter(0, in.getTranslated("FxListStartPointX", iso, "X at start"));
    Parameter p_startPointY = new Parameter(0, in.getTranslated("FxListStartPointY", iso, "Y at start"));
    
    Parameter p_controlPointX = new Parameter(0, in.getTranslated("FxListControlPointX", iso, "X for control point"));
    Parameter p_controlPointY = new Parameter(0, in.getTranslated("FxListControlPointY", iso, "Y for control point"));
    
    Parameter p_endPointX = new Parameter(0, in.getTranslated("FxListEndPointX", iso, "X at stop"));
    Parameter p_endPointY = new Parameter(0, in.getTranslated("FxListEndPointY", iso, "Y at stop"));

    public QuadraticMovement() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListQuadraticMovement", iso, "Quadratic movement");
        uniqueID = -1;
        params.add(p_msStart);
        params.add(p_msStop);
        params.add(p_startPointX);
        params.add(p_startPointY);
        params.add(p_controlPointX);
        params.add(p_controlPointY);
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

    public void setControlPointX(int controlPointX) {
        p_controlPointX.setParam(controlPointX);
        params.set(4, p_controlPointX);
    }

    public int getControlPointX() {
        return (int)params.get(4).getParam();
    }

    public void setControlPointY(int controlPointY) {
        p_controlPointY.setParam(controlPointY);
        params.set(5, p_controlPointY);
    }

    public int getControlPointY() {
        return (int)params.get(5).getParam();
    }

    public void setEndPointX(int endPointX) {
        p_endPointX.setParam(endPointX);
        params.set(6, p_endPointX);
    }

    public int getEndPointX() {
        return (int)params.get(6).getParam();
    }

    public void setEndPointY(int endPointY) {
        p_endPointY.setParam(endPointY);
        params.set(7, p_endPointY);
    }

    public int getEndPointY() {
        return (int)params.get(7).getParam();
    }

    @Override
    public String getTag() {
        return "\\moves3("
                + getStartPointX() + "," + getStartPointY() + ","
                + getControlPointX() + "," + getControlPointY() + ","
                + getEndPointX() + "," + getEndPointY()
                + ((getMsStart() != getMsStop()) ? "," + getMsStart() + "," + getMsStop() : "") + ")";
    }
}
