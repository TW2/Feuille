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
import feuille.util.effect.Parameter;

/**
 *
 * @author util2
 */
public class Distortion extends AFx {
    
    Parameter p_u1 = new Parameter(0, in.getTranslated("FxListDistortionU1", iso, "U1"));
    Parameter p_v1 = new Parameter(0, in.getTranslated("FxListDistortionV1", iso, "V1"));
    
    Parameter p_u2 = new Parameter(0, in.getTranslated("FxListDistortionU2", iso, "U2"));
    Parameter p_v2 = new Parameter(0, in.getTranslated("FxListDistortionV2", iso, "V2"));
    
    Parameter p_u3 = new Parameter(0, in.getTranslated("FxListDistortionU3", iso, "U3"));
    Parameter p_v3 = new Parameter(0, in.getTranslated("FxListDistortionV3", iso, "V3"));

    public Distortion() {
        name = in.getTranslated("FxListDistortion", iso, "Distortion");
        uniqueID = -1;
        params.add(p_u1);
        params.add(p_v1);
        params.add(p_u2);
        params.add(p_v2);
        params.add(p_u3);
        params.add(p_v3);
    }
    
    @Override
    public String getTag() {
        return "\\distort("
                + getU1() + "," + getV1() + ","
                + getU2() + "," + getV2() + ","
                + getU3() + "," + getV3() + ")";
    }

    public void setU1(int u1) {
        p_u1.setParam(u1);
        params.set(0, p_u1);
    }

    public int getU1() {
        return (int)params.get(0).getParam();
    }

    public void setV1(int v1) {
        p_v1.setParam(v1);
        params.set(1, p_v1);
    }

    public int getV1() {
        return (int)params.get(1).getParam();
    }

    public void setU2(int u2) {
        p_u2.setParam(u2);
        params.set(2, p_u2);
    }

    public int getU2() {
        return (int)params.get(2).getParam();
    }

    public void setV2(int v2) {
        p_v2.setParam(v2);
        params.set(3, p_v2);
    }

    public int getV2() {
        return (int)params.get(3).getParam();
    }

    public void setU3(int u3) {
        p_u3.setParam(u3);
        params.set(4, p_u3);
    }

    public int getU3() {
        return (int)params.get(4).getParam();
    }

    public void setV3(int v3) {
        p_v3.setParam(v3);
        params.set(5, p_v3);
    }

    public int getV3() {
        return (int)params.get(5).getParam();
    }
    
}
