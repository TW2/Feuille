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
 * \\mover(x1,y1,x2,y2,angle1,angle2,radius1,radius2[,t1,t2])
 * @author util2
 */
public class PolarMovement extends AFx {
    
    Parameter p_msStart = new Parameter(0, in.getTranslated("FxListMsStart", iso, "Start time"));
    Parameter p_msStop = new Parameter(0, in.getTranslated("FxListMsStop", iso, "End time"));
    
    //int x1 = 0, y1 = 0;
    Parameter p_x1 = new Parameter(0, in.getTranslated("FxListX1", iso, "X1"));
    Parameter p_y1 = new Parameter(0, in.getTranslated("FxListY1", iso, "Y1"));
    
    //int x2 = 0, y2 = 0;
    Parameter p_x2 = new Parameter(0, in.getTranslated("FxListX2", iso, "X2"));
    Parameter p_y2 = new Parameter(0, in.getTranslated("FxListY2", iso, "Y2"));
    
    Parameter p_angle1 = new Parameter(0, in.getTranslated("FxListAngle1", iso, "Angle 1"));
    Parameter p_angle2 = new Parameter(0, in.getTranslated("FxListAngle2", iso, "Angle 2"));
    
    Parameter p_radius1 = new Parameter(0, in.getTranslated("FxListRadius1", iso, "Radius 1"));
    Parameter p_radius2 = new Parameter(0, in.getTranslated("FxListRadius2", iso, "Radius 2"));

    public PolarMovement() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListPolarMovement", iso, "Polar movement");
        uniqueID = -1;
        params.add(p_msStart);
        params.add(p_msStop);
        params.add(p_x1);
        params.add(p_y1);
        params.add(p_x2);
        params.add(p_y2);
        params.add(p_angle1);
        params.add(p_angle2);
        params.add(p_radius1);
        params.add(p_radius2);
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

    public void setAngle1(int angle1) {
        p_angle1.setParam(angle1);
        params.set(6, p_angle1);
    }

    public int getAngle1() {
        return (int)params.get(6).getParam();
    }

    public void setAngle2(int angle2) {
        p_angle2.setParam(angle2);
        params.set(7, p_angle2);
    }

    public int getAngle2() {
        return (int)params.get(7).getParam();
    }

    public void setRadius1(int radius1) {
        p_radius1.setParam(radius1);
        params.set(8, p_radius1);
    }

    public int getRadius1() {
        return (int)params.get(8).getParam();
    }

    public void setRadius2(int radius2) {
        p_radius2.setParam(radius2);
        params.set(9, p_radius2);
    }

    public int getRadius2() {
        return (int)params.get(9).getParam();
    }

    @Override
    public String getTag() {
        return "\\mover("
                + getX1() + "," + getY1() + ","
                + getX2() + "," + getY2() + ","
                + getAngle1() + "," + getAngle2() + ","
                + getRadius1() + "," + getRadius2()
                + (getMsStart() != getMsStop() ? "," + getMsStart() + "," + getMsStop() : "") + ")";
    }
        
}
