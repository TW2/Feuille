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
package feuille.util.effect.Extended;

import feuille.util.effect.AFx;
import feuille.util.effect.Parameter;

/**
 * Rectangle clip which is invisible
 * @author util2
 */
public class InvisibleNormalClip extends AFx {
    
    //int x1 = 0, y1 = 0;
    Parameter p_x1 = new Parameter(0, in.getTranslated("FxListX1", iso, "X1"));
    Parameter p_y1 = new Parameter(0, in.getTranslated("FxListY1", iso, "Y1"));
    
    //int x2 = 0, y2 = 0;
    Parameter p_x2 = new Parameter(10, in.getTranslated("FxListX2", iso, "X2"));
    Parameter p_y2 = new Parameter(10, in.getTranslated("FxListY2", iso, "Y2"));

    public InvisibleNormalClip() {
        name = in.getTranslated("FxListInvisibleNormalClip", iso, "Invisible Clip (rectangle)");
        uniqueID = -1;
        params.add(p_x1);
        params.add(p_y1);
        params.add(p_x2);
        params.add(p_y2);
    }

    public void setX1(int x1) {
        p_x1.setParam(x1);
        params.set(0, p_x1);
    }

    public int getX1() {
        return (int)params.get(0).getParam();
    }

    public void setY1(int y1) {
        p_y1.setParam(y1);
        params.set(1, p_y1);
    }

    public int getY1() {
        return (int)params.get(1).getParam();
    }

    public void setX2(int x2) {
        p_x2.setParam(x2);
        params.set(2, p_x2);
    }

    public int getX2() {
        return (int)params.get(2).getParam();
    }

    public void setY2(int y2) {
        p_y2.setParam(y2);
        params.set(3, p_y2);
    }

    public int getY2() {
        return (int)params.get(3).getParam();
    }

    @Override
    public String getTag() {
        int x1 = (int)params.get(0).getParam(), y1 = (int)params.get(1).getParam();
        int x2 = (int)params.get(2).getParam(), y2 = (int)params.get(3).getParam();
        return "\\iclip(" + x1 + "," + y1 + "," + x2 + "," + y2 + ")";
    }
}
