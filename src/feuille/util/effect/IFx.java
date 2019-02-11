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

import feuille.util.ISO_3166;
import feuille.util.Language;
import java.util.List;

/**
 * Interface that defines effects objects
 * @author util2
 */
public interface IFx {
    
    /**
     * Get a list of effects
     * @return An effects list
     */
    public List<AFx> getFxObjects();
    
    /**
     * Add one effect
     * @param afx The effect to add 
     */
    public void addFxObject(AFx afx);
    
    /**
     * Remove one effect
     * @param afx The effect to remove
     */
    public void removeFxObject(AFx afx);
    
    /**
     * Clear the list of effects
     */
    public void clearObjectsList();
    
    /**
     * Get the tag for this effect
     * @return The tag in ASS format (including '\' character)
     */
    public String getTag();
}
