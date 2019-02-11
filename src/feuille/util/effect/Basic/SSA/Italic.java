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
package feuille.util.effect.Basic.SSA;

import feuille.util.effect.AFx;
import feuille.util.effect.Parameter;

/**
 *
 * @author util2
 */
public class Italic extends AFx {
    
    Parameter param = new Parameter(false, in.getTranslated("FxListValue", iso, "Value"));

    public Italic() {
        name = in.getTranslated("FxListItalic", iso, "Italic");
        uniqueID = -1;
        params.add(param);
    }
    
    @Override
    public String getTag() {
        return "\\i" + (isActive() == true ? "1" : "0");
    }

    public void setActive(boolean active) {
        param.setParam(active);
        params.set(0, param);
    }

    public boolean isActive() {
        return (boolean)params.get(0).getParam();
    }
}
