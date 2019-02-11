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
package feuille.util.effect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author util2
 */
public class EffectsList {
    
    private List<AFx> effects = new ArrayList<>();

    public EffectsList() {
        init();
    }
    
    private void init(){
        try {
            effects.addAll(FxClassFinder.getEffectsFromPackage("feuille.util.effect.Basic.ASS"));
            effects.addAll(FxClassFinder.getEffectsFromPackage("feuille.util.effect.Basic.SSA"));
            effects.addAll(FxClassFinder.getEffectsFromPackage("feuille.util.effect.Extended"));
            effects.addAll(FxClassFinder.getEffectsFromPackage("feuille.util.effect.VsFilterMod"));
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(EffectsList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setEffects(List<AFx> effects) {
        this.effects = effects;
    }

    public List<AFx> getEffects() {
        return effects;
    }
    
    public void addEffect(AFx afx){
        boolean add = true;
        for(AFx search : effects){
            if(search.getName().equalsIgnoreCase(afx.getName()) == true){
                add = false;
                break;
            }
        }
        if(add == true){
            effects.add(afx);
        }
    }
    
    public void removeEffect(AFx afx){
        int index = -1;
        for(int i=0; i<effects.size(); i++){
            AFx search = effects.get(i);
            if(search.getName().equalsIgnoreCase(afx.getName()) == true){
                index = i;
                break;
            }
        }
        if(index != -1){
            effects.remove(index);
        }
    }
    
    public void clearEffectsList(){
        effects.clear();
    }
}
