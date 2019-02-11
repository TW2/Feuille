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
public class FadingComplex extends AFx {
    
    Parameter p_msStart = new Parameter(0, in.getTranslated("FxListMsStart", iso, "Start time"));
    Parameter p_msAfterStart = new Parameter(0, in.getTranslated("FxListMsAfterStart", iso, "Time after start"));
    Parameter p_msBeforeStop = new Parameter(0, in.getTranslated("FxListMsBeforeStop", iso, "Time before end"));
    Parameter p_msStop = new Parameter(0, in.getTranslated("FxListMsStop", iso, "End time"));
    
     // Values in integer from 0 to 255
    Parameter p_alphaStartToAfterStart = new Parameter(0, in.getTranslated("FxListAlphaFromStartTo", iso, "Transparency limit from start time"));
    Parameter p_alphaAfterStartToBeforeStop = new Parameter(0, in.getTranslated("FxListAlphaMeantime", iso, "Transparency limit in meantime"));
    Parameter p_alphaBeforeStopToStop = new Parameter(0, in.getTranslated("FxListAlphaFromMeantimeTo", iso, "Transparency limit to end time"));

    public FadingComplex() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListFadingComplex", iso, "Fading (complex)");
        uniqueID = -1;
        params.add(p_msStart);
        params.add(p_msAfterStart);
        params.add(p_msBeforeStop);
        params.add(p_msStop);
        params.add(p_alphaStartToAfterStart);
        params.add(p_alphaAfterStartToBeforeStop);
        params.add(p_alphaBeforeStopToStop);
    }

    public void setMsStart(int msStart) {
        p_msStart.setParam(msStart);
        params.set(0, p_msStart);
    }

    public int getMsStart() {
        return (int)params.get(0).getParam();
    }

    public void setMsAfterStart(int msAfterStart) {
        p_msAfterStart.setParam(msAfterStart);
        params.set(1, p_msAfterStart);
    }

    public int getMsAfterStart() {
        return (int)params.get(1).getParam();
    }

    public void setMsBeforeStop(int msBeforeStop) {
        p_msBeforeStop.setParam(msBeforeStop);
        params.set(2, p_msBeforeStop);
    }

    public int getMsBeforeStop() {
        return (int)params.get(2).getParam();
    }

    public void setMsStop(int msStop) {
        p_msStop.setParam(msStop);
        params.set(3, p_msStop);
    }

    public int getMsStop() {
        return (int)params.get(3).getParam();
    }

    public void setAlphaStartToAfterStart(int alphaStartToAfterStart) {
        p_alphaStartToAfterStart.setParam(alphaStartToAfterStart);
        params.set(4, p_alphaStartToAfterStart);
    }

    public int getAlphaStartToAfterStart() {
        return (int)params.get(4).getParam();
    }

    public void setAlphaAfterStartToBeforeStop(int alphaAfterStartToBeforeStop) {
        p_alphaAfterStartToBeforeStop.setParam(alphaAfterStartToBeforeStop);
        params.set(5, p_alphaAfterStartToBeforeStop);
    }

    public int getAlphaAfterStartToBeforeStop() {
        return (int)params.get(5).getParam();
    }

    public void setAlphaBeforeStopToStop(int alphaBeforeStopToStop) {
        p_alphaBeforeStopToStop.setParam(alphaBeforeStopToStop);
        params.set(6, p_alphaBeforeStopToStop);
    }

    public int getAlphaBeforeStopToStop() {
        return (int)params.get(6).getParam();
    }

    @Override
    public String getTag() {
        return "\\fade(" 
                + getAlphaStartToAfterStart() + ","
                + getAlphaAfterStartToBeforeStop() + ","
                + getAlphaBeforeStopToStop() + ","
                + getMsStart() + ","
                + getMsAfterStart() + ","
                + getMsBeforeStop() + ","
                + getMsStop() + ")";
    }
    
    
}
