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
package feuille.util.effect.Basic.SSA;

import feuille.util.effect.AFx;
import feuille.util.effect.FxType;
import feuille.util.effect.Parameter;

/**
 *
 * @author util2
 */
public class FontName extends AFx {
    
    Parameter p_fontName = new Parameter("Serif", in.getTranslated("FxListFontName", iso, "Font name"));

    public FontName() {
        fxType = FxType.Override;
        name = in.getTranslated("FxListFontName", iso, "Font name");
        uniqueID = -1;
        params.add(p_fontName);
    }

    public void setValue(String value) {
        p_fontName.setParam(value);
        params.set(0, p_fontName);
    }

    public String getValue() {
        return (String)params.get(0).getParam();
    }

    @Override
    public String getTag() {
        return "\\fn" + getValue();
    }
    
    
}
