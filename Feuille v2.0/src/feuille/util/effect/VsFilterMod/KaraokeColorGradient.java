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

import feuille.util.ColorBox;
import feuille.util.effect.AFx;
import feuille.util.effect.Parameter;
import java.awt.Color;

/**
 *
 * @author util2
 */
public class KaraokeColorGradient extends AFx {
    
    Parameter p_topLeftColor = new Parameter(Color.black, in.getTranslated("FxListTopLeft", iso, "Top left value"));
    Parameter p_topRightColor = new Parameter(Color.black, in.getTranslated("FxListTopRight", iso, "Top right value"));
    Parameter p_bottomLeftColor = new Parameter(Color.black, in.getTranslated("FxListBottomLeft", iso, "Bottom left value"));    
    Parameter p_bottomRightColor = new Parameter(Color.black, in.getTranslated("FxListBottomRight", iso, "Bottom right value"));

    public KaraokeColorGradient() {
        name = in.getTranslated("FxListKaraokeColorGradient", iso, "Karaoke color with gradient");
        uniqueID = -1;
        params.add(p_topLeftColor);
        params.add(p_topRightColor);
        params.add(p_bottomLeftColor);
        params.add(p_bottomRightColor);
    }

    @Override
    public String getTag() {
        return "\\2vc(" 
                + "&H" + ColorBox.colorToBgr(getTopLeftColor()) + "&,"
                + "&H" + ColorBox.colorToBgr(getTopRightColor()) + "&,"
                + "&H" + ColorBox.colorToBgr(getBottomLeftColor()) + "&,"
                + "&H" + ColorBox.colorToBgr(getBottomRightColor()) + "&)";
    }

    public void setTopLeftColor(Color topLeftColor) {
        p_topLeftColor.setParam(topLeftColor);
        params.set(0, p_topLeftColor);
    }

    public Color getTopLeftColor() {
        return (Color)params.get(0).getParam();
    }

    public void setTopRightColor(Color topRightColor) {
        p_topRightColor.setParam(topRightColor);
        params.set(1, p_topRightColor);
    }

    public Color getTopRightColor() {
        return (Color)params.get(1).getParam();
    }

    public void setBottomLeftColor(Color bottomLeftColor) {
        p_bottomLeftColor.setParam(bottomLeftColor);
        params.set(2, p_bottomLeftColor);
    }

    public Color getBottomLeftColor() {
        return (Color)params.get(2).getParam();
    }

    public void setBottomRightColor(Color bottomRightColor) {
        p_bottomRightColor.setParam(bottomRightColor);
        params.set(3, p_bottomRightColor);
    }

    public Color getBottomRightColor() {
        return (Color)params.get(3).getParam();
    }
}
