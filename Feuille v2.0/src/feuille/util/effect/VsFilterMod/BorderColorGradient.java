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
import java.awt.Color;

/**
 *
 * @author util2
 */
public class BorderColorGradient extends AFx {
    
    Color topLeftColor = Color.black;
    Color topRightColor = Color.black;
    Color bottomLeftColor = Color.black;
    Color bottomRightColor = Color.black;

    public BorderColorGradient() {
        
    }

    @Override
    public String getTag() {
        return "\\3vc(" 
                + "&H" + ColorBox.colorToBgr(topLeftColor) + "&,"
                + "&H" + ColorBox.colorToBgr(topRightColor) + "&,"
                + "&H" + ColorBox.colorToBgr(bottomLeftColor) + "&,"
                + "&H" + ColorBox.colorToBgr(bottomRightColor) + "&)";
    }

    public void setTopLeftColor(Color topLeftColor) {
        this.topLeftColor = topLeftColor;
    }

    public Color getTopLeftColor() {
        return topLeftColor;
    }

    public void setTopRightColor(Color topRightColor) {
        this.topRightColor = topRightColor;
    }

    public Color getTopRightColor() {
        return topRightColor;
    }

    public void setBottomLeftColor(Color bottomLeftColor) {
        this.bottomLeftColor = bottomLeftColor;
    }

    public Color getBottomLeftColor() {
        return bottomLeftColor;
    }

    public void setBottomRightColor(Color bottomRightColor) {
        this.bottomRightColor = bottomRightColor;
    }

    public Color getBottomRightColor() {
        return bottomRightColor;
    }
}
