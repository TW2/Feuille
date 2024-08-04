/*
 * Copyright (C) 2024 util2
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
package org.wingate.feuille.m.afm.karaoke.sfx;

import java.util.ArrayList;
import java.util.List;
import org.wingate.feuille.ass.AssEvent;

/**
 *
 * @author util2
 */
public class LineSyllableBasicSFX extends SFXAbstract {

    public LineSyllableBasicSFX() {
        name = "Per syllable karaoke (LineSyllableBasic)";
        templates.add("{\\kf%cdK\\t(%sK,%eK,\\c&H00FFFF&)}%syllable");
    }

    @Override
    public List<AssEvent> doJob(AssEvent input) {
        final List<AssEvent> output = new ArrayList<>();        
        
        List<SFXSyllable> syls = getSyllable(input);
        AssEvent ev = input;
        ev.setText(replaceParams(templates.getFirst(), syls));
        output.add(ev);
        
        return output;
    }
    
}
