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

/**
 *
 * @author util2
 */
public class BorderAlphaGradient extends AFx {

    int topLeftAlpha = 0;
    int topRightAlpha = 0;
    int bottomLeftAlpha = 0;
    int bottomRightAlpha = 0;

    public BorderAlphaGradient() {
        
    }
    
    @Override
    public String getTag() {
        return "\\3va(" 
                + "&H" + AlphaBox.integerToHexa(topLeftAlpha) + "&,"
                + "&H" + AlphaBox.integerToHexa(topRightAlpha) + "&,"
                + "&H" + AlphaBox.integerToHexa(bottomLeftAlpha) + "&,"
                + "&H" + AlphaBox.integerToHexa(bottomRightAlpha) +"&)";
    }

    public void setTopLeftAlpha(int topLeftAlpha) {
        this.topLeftAlpha = topLeftAlpha;
    }

    public int getTopLeftAlpha() {
        return topLeftAlpha;
    }

    public void setTopRightAlpha(int topRightAlpha) {
        this.topRightAlpha = topRightAlpha;
    }

    public int getTopRightAlpha() {
        return topRightAlpha;
    }

    public void setBottomLeftAlpha(int bottomLeftAlpha) {
        this.bottomLeftAlpha = bottomLeftAlpha;
    }

    public int getBottomLeftAlpha() {
        return bottomLeftAlpha;
    }

    public void setBottomRightAlpha(int bottomRightAlpha) {
        this.bottomRightAlpha = bottomRightAlpha;
    }

    public int getBottomRightAlpha() {
        return bottomRightAlpha;
    }
}
