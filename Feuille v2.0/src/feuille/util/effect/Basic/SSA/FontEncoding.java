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

/**
 *
 * @author util2
 */
public class FontEncoding extends AFx {

    // TODO retrieve list of encoding charsets
    int value = 1;

    public FontEncoding() {
        fxType = FxType.Override;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    @Override
    public String getTag() {
        return "\\fe" + value;
    }
    
}
