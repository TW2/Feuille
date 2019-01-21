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
package feuille.util.effect.Basic.ASS;

import feuille.util.effect.AFx;
import feuille.util.effect.FxType;
import feuille.util.effect.Parameter;

/**
 *
 * @author util2
 */
public class Movement extends AFx {
    
    Parameter p_msStart = new Parameter(0, in.getTranslated("FxListMsStart", iso, "Start time"));
    Parameter p_msStop = new Parameter(0, in.getTranslated("FxListMsStop", iso, "End time"));
    
    //int x1 = 0, y1 = 0;
    Parameter p_x1 = new Parameter(0, in.getTranslated("FxListX1", iso, "X1"));
    Parameter p_y1 = new Parameter(0, in.getTranslated("FxListY1", iso, "Y1"));
    
    //int x2 = 0, y2 = 0;
    Parameter p_x2 = new Parameter(0, in.getTranslated("FxListX2", iso, "X2"));
    Parameter p_y2 = new Parameter(0, in.getTranslated("FxListY2", iso, "Y1"));

    public Movement() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListMovement", iso, "Movement");
        uniqueID = -1;
        params.add(p_msStart);
        params.add(p_msStop);
        params.add(p_x1);
        params.add(p_y1);
        params.add(p_x2);
        params.add(p_y2);
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

    public void setX1(int x1) {
        p_x1.setParam(x1);
        params.set(2, p_x1);
    }

    public int getX1() {
        return (int)params.get(2).getParam();
    }

    public void setY1(int y1) {
        p_y1.setParam(y1);
        params.set(3, p_y1);
    }

    public int getY1() {
        return (int)params.get(3).getParam();
    }

    public void setX2(int x2) {
        p_x2.setParam(x2);
        params.set(4, p_x2);
    }

    public int getX2() {
        return (int)params.get(4).getParam();
    }

    public void setY2(int y2) {
        p_y2.setParam(y2);
        params.set(5, p_y2);
    }

    public int getY2() {
        return (int)params.get(5).getParam();
    }

    @Override
    public String getTag() {
        int msStart = (int)params.get(0).getParam();
        int msStop = (int)params.get(1).getParam();
        int x1 = (int)params.get(2).getParam(), y1 = (int)params.get(3).getParam();
        int x2 = (int)params.get(4).getParam(), y2 = (int)params.get(5).getParam();
        return "\\move("
                + (msStart != msStop ? msStart + "," + msStop + "," : "")
                + x1 + "," + y1 + ","
                + x2 + "," + y2
                + ")";
    }
    
        
}
