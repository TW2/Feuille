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
public class VectorClip extends AFx {
    
    Parameter p_drawing = new Parameter("", in.getTranslated("FxListDrawing", iso, "Drawing path"));
    Parameter p_scale = new Parameter(0, in.getTranslated("FxListDrawingScale", iso, "Scale"));

    public VectorClip() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListVectorClip", iso, "Clip (vector)");
        uniqueID = -1;
        params.add(p_drawing);
        params.add(p_scale);
    }

    public void setDrawing(String drawing) {
        p_drawing.setParam(drawing);
        params.set(0, p_drawing);
    }

    public String getDrawing() {
        return (String)params.get(0).getParam();
    }

    public void setScale(int scale) {
        p_scale.setParam(scale);
        params.set(1, p_scale);
    }

    public int getScale() {
        return (int)params.get(1).getParam();
    }

    @Override
    public String getTag() {
        if(getDrawing().isEmpty() == false){
            return "\\clip(" + (getScale() != 0 ? Integer.toString(getScale()) + "," : "") + getDrawing() + ")";
        }
        return "";
    }
    
    
}
