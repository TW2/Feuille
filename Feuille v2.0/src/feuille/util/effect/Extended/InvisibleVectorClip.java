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
import feuille.util.effect.FxType;

/**
 * \\iclip(scale,drawing commands)
 * @author util2
 */
public class InvisibleVectorClip extends AFx {
    
    String drawing = "";
    int scale = 0;

    public InvisibleVectorClip() {
        fxType = FxType.Override;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    @Override
    public String getTag() {
        if(drawing.isEmpty() == false){
            return "\\iclip(" + (scale != 0 ? Integer.toString(scale) + "," : "") + drawing + ")";
        }
        return "";
    }
}
