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
import feuille.util.effect.Parameter;

/**
 *
 * @author util2
 */
public class BorderAlpha extends AFx {
    
    Parameter param = new Parameter(0f, in.getTranslated("FxListValue", iso, "Value"));

    public BorderAlpha() {
        name = in.getTranslated("FxListBorderAlpha", iso, "Border transparency");
        uniqueID = -1;
        params.add(param);
    }

    public void setValue(float value) {
        param.setParam(value);
        params.set(0, param);
    }

    public float getValue() {
        return (float)params.get(0).getParam();
    }

    @Override
    public String getTag() {
        return "\\3a&H" + getHexadecimal() + "&";
    }
    
    public String getHexadecimal(){
        int a = Math.round(getValue() * 100f) * 255;
        return Integer.toHexString(a).toUpperCase();
    }
    
    public void setHexadecimal(String hexa){
        int a = Integer.parseInt(hexa, 16);
        setValue((float)(a / 255f) / 100f);
    }
    
}
