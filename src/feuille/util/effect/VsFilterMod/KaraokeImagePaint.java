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
import feuille.util.effect.Parameter;

/**
 *
 * @author util2
 */
public class KaraokeImagePaint extends AFx {
    
    Parameter p_pngPath = new Parameter("", in.getTranslated("FxListPNGPath", iso, "PNG path"));
    Parameter p_xOffset = new Parameter(0, in.getTranslated("FxListXOffset", iso, "Offset on X"));
    Parameter p_yOffset = new Parameter(0, in.getTranslated("FxListYOffset", iso, "Offset on Y"));
    Parameter p_offsetEnabled = new Parameter(false, in.getTranslated("FxListOffsetEnabled", iso, "Offset enabled"));

    public KaraokeImagePaint() {
        name = in.getTranslated("FxListKaraokeImagePaint", iso, "Fill karaoke with image");
        uniqueID = -1;
        params.add(p_pngPath);
        params.add(p_xOffset);
        params.add(p_yOffset);
        params.add(p_offsetEnabled);
    }
    
    @Override
    public String getTag() {
        return "\\2img(" + getPngPath() + (isOffsetEnabled() == true ? "," + getXOffset() + "," + getYOffset() : "") + ")";
    }

    public void setPngPath(String pngPath) {
        p_pngPath.setParam(pngPath);
        params.set(0, p_pngPath);
    }

    public String getPngPath() {
        return (String)params.get(0).getParam();
    }

    public void setXOffset(int xOffset) {
        p_xOffset.setParam(xOffset);
        params.set(1, p_xOffset);
    }

    public int getXOffset() {
        return (int)params.get(1).getParam();
    }

    public void setYOffset(int yOffset) {
        p_yOffset.setParam(yOffset);
        params.set(2, p_yOffset);
    }

    public int getYOffset() {
        return (int)params.get(2).getParam();
    }

    public void setOffsetEnabled(boolean offsetEnabled) {
        p_offsetEnabled.setParam(offsetEnabled);
        params.set(3, p_offsetEnabled);
    }

    public boolean isOffsetEnabled() {
        return (boolean)params.get(3).getParam();
    }
}
