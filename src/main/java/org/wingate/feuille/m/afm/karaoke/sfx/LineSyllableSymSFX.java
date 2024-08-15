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
public class LineSyllableSymSFX extends SFXAbstract {

    public LineSyllableSymSFX() {
        name = "Symmetric karaoke";
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
        int index;        
        for(int i=0; i<syls.size(); i++){
            index = syls.size() / 2 >= i ? i : syls.size() -1 - i;
            index = index < templates.size() ? index : templates.size() - 1;
            
            sb.append(replaceParams(templates.get(index), syls, i));            
        }
        
        // Define output new event
        AssEvent ev = input.getCopy();
        ev.setText(sb.toString());
        output.add(ev);        
        
        // Our result
        return output;
    }
    
}
