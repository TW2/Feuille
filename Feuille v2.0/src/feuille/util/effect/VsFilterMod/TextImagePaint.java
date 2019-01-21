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

import feuille.util.effect.AFx;

/**
 *
 * @author util2
 */
public class TextImagePaint extends AFx {
    
    String pngPath = "";
    int xOffset = 0;
    int yOffset = 0;
    boolean offsetEnabled = false;

    public TextImagePaint() {
        
    }
    
    @Override
    public String getTag() {
        return "\\1img(" + pngPath + (offsetEnabled == true ? "," + xOffset + "," + yOffset : "") + ")";
    }

    public void setPngPath(String pngPath) {
        this.pngPath = pngPath;
    }

    public String getPngPath() {
        return pngPath;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getXOffset() {
        return xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public void setOffsetEnabled(boolean offsetEnabled) {
        this.offsetEnabled = offsetEnabled;
    }

    public boolean isOffsetEnabled() {
        return offsetEnabled;
    }
}
