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

import feuille.util.AlphaBox;
import feuille.util.effect.AFx;
import feuille.util.effect.Parameter;

/**
 *
 * @author util2
 */
public class TextAlphaGradient extends AFx {

    Parameter p_topLeftAlpha = new Parameter(0, in.getTranslated("FxListTopLeft", iso, "Top left value"));
    Parameter p_topRightAlpha = new Parameter(0, in.getTranslated("FxListTopRight", iso, "Top right value"));
    Parameter p_bottomLeftAlpha = new Parameter(0, in.getTranslated("FxListBottomLeft", iso, "Bottom left value"));    
    Parameter p_bottomRightAlpha = new Parameter(0, in.getTranslated("FxListBottomRight", iso, "Bottom right value"));

    public TextAlphaGradient() {
        name = in.getTranslated("FxListTextAlphaGradient", iso, "Text transparency with gradient");
        uniqueID = -1;
        params.add(p_topLeftAlpha);
        params.add(p_topRightAlpha);
        params.add(p_bottomLeftAlpha);
        params.add(p_bottomRightAlpha);
    }
    
    @Override
    public String getTag() {
        return "\\1va(" 
                + "&H" + AlphaBox.integerToHexa(getTopLeftAlpha()) + "&,"
                + "&H" + AlphaBox.integerToHexa(getTopRightAlpha()) + "&,"
                + "&H" + AlphaBox.integerToHexa(getBottomLeftAlpha()) + "&,"
                + "&H" + AlphaBox.integerToHexa(getBottomRightAlpha()) +"&)";
    }

    public void setTopLeftAlpha(int topLeftAlpha) {
        p_topLeftAlpha.setParam(topLeftAlpha);
        params.set(0, p_topLeftAlpha);
    }

    public int getTopLeftAlpha() {
        return (int)params.get(0).getParam();
    }

    public void setTopRightAlpha(int topRightAlpha) {
        p_topRightAlpha.setParam(topRightAlpha);
        params.set(1, p_topRightAlpha);
    }

    public int getTopRightAlpha() {
        return (int)params.get(1).getParam();
    }

    public void setBottomLeftAlpha(int bottomLeftAlpha) {
        p_bottomLeftAlpha.setParam(bottomLeftAlpha);
        params.set(2, p_bottomLeftAlpha);
    }

    public int getBottomLeftAlpha() {
        return (int)params.get(2).getParam();
    }

    public void setBottomRightAlpha(int bottomRightAlpha) {
        p_bottomRightAlpha.setParam(bottomRightAlpha);
        params.set(3, p_bottomRightAlpha);
    }

    public int getBottomRightAlpha() {
        return (int)params.get(3).getParam();
    }
}
