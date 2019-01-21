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
public class Origin extends AFx {
     
    Parameter p_x = new Parameter(0, in.getTranslated("FxListX0", iso, "X"));
    Parameter p_y = new Parameter(0, in.getTranslated("FxListY0", iso, "Y"));

    public Origin() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListOrigin", iso, "Origin");
        uniqueID = -1;
        params.add(p_x);
        params.add(p_y);
    }

    public void setX(int x) {
        p_x.setParam(x);
        params.set(0, p_x);
    }

    public int getX() {
        return (int)params.get(0).getParam();
    }

    public void setY(int y) {
        p_y.setParam(y);
        params.set(1, p_y);
    }

    public int getY() {
        return (int)params.get(1).getParam();
    }

    @Override
    public String getTag() {
        int x = (int)params.get(0).getParam(), y = (int)params.get(1).getParam();
        return "\\org(" + x + "," + y + ")";
    }
    
    
}
