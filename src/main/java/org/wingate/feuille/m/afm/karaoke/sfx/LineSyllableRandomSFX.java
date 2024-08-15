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
public class LineSyllableRandomSFX extends SFXAbstract {

    public LineSyllableRandomSFX() {
        name = "Random karaoke";
        helper = "Require more than one template by using a template by line.";
    }

    @Override
    public List<AssEvent> doJob(AssEvent input) {
        // A new array as result
        final List<AssEvent> output = new ArrayList<>();        
        
        // Get syllables for this event
        List<SFXSyllable> syls = getSyllable(input);
        
        // Replace params from templates by syllables processing
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<syls.size(); i++){
            sb.append(replaceParams(templates.get((int)(Math.random() * templates.size())), syls, i));
        }
        
        // Define output new event
        AssEvent ev = input.getCopy();
        ev.setText(sb.toString());
        output.add(ev);
        
        // Our result
        return output;
    }
    
}
