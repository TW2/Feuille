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
public class FadingSimple extends AFx {
    
    Parameter p_msStart = new Parameter(0, in.getTranslated("FxListMsStart", iso, "Start time"));
    Parameter p_msStop = new Parameter(0, in.getTranslated("FxListMsStop", iso, "End time"));

    public FadingSimple() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListFadingSimple", iso, "Fading (simple)");
        uniqueID = -1;
        params.add(p_msStart);
        params.add(p_msStop);
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

    @Override
    public String getTag() {
        return "\\fad(" + getMsStart() + "," + getMsStop() + ")";
    }
    
    
}
