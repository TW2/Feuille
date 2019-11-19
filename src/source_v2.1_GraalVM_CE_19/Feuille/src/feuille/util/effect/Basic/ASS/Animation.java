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
public class Animation extends AFx {    
    
    // Parameter 0 on the list (int msStart = 0;)
    Parameter p_msStart = new Parameter(0, in.getTranslated("FxListMsStart", iso, "Start time"));
    
    // Parameter 1 on the list (int msStop = 0;)
    Parameter p_msStop = new Parameter(0, in.getTranslated("FxListMsStop", iso, "End time"));
    
    // Parameter 2 on the list (float acceleration = 0;)
    Parameter p_acceleration = new Parameter(0f, in.getTranslated("FxListAcceleration", iso, "Acceleration"));

    public Animation() {
        fxType = FxType.Common;
        name = in.getTranslated("FxListAnimation", iso, "Animation");
        uniqueID = 004;
        params.add(p_msStart);
        params.add(p_msStop);
        params.add(p_acceleration);
    }

    @Override
    public String getTag() {
        int msStart = (int)params.get(0).getParam();
        int msStop = (int)params.get(1).getParam();
        float acceleration = (int)params.get(2).getParam();
        return "\\t(" 
                + (msStart != msStop ? msStart + "," + msStop + "," : "") 
                + (acceleration != 0 ? acceleration + "," : "") 
                + getTags() + ")";
    }
    
    private String getTags(){
        StringBuilder sb = new StringBuilder();
        for(AFx afx : getFxObjects()){
            if(afx.getFxType() == FxType.Animation){
                sb.append(afx.getTag());
            }            
        }
        return sb.toString();
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

    public void setAcceleration(float acceleration) {
        p_acceleration.setParam(acceleration);
        params.set(2, p_acceleration);
    }

    public float getAcceleration() {
        return (float)params.get(2).getParam();
    }
    
    
}
