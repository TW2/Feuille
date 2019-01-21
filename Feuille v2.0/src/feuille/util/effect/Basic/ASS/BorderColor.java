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

import feuille.util.ColorBox;
import feuille.util.effect.AFx;
import feuille.util.effect.Parameter;
import java.awt.Color;

/**
 *
 * @author util2
 */
public class BorderColor extends AFx {
    
    Parameter param = new Parameter(Color.black, in.getTranslated("FxListColor", iso, "Color"));

    public BorderColor() {
        name = in.getTranslated("FxListBorderColor", iso, "Border color");
        uniqueID = -1;
        params.add(param);
    }

    public void setColor(Color color) {
        param.setParam(color);
        params.set(0, param);
    }
    
    public Color getColor(){
        return (Color)params.get(0).getParam();
    }
    
    @Override
    public String getTag() {
        // Return a BGR value (ASS format)
        return "\\3c&H" + ColorBox.colorToBgr(getColor()) + "&";
    }
}
