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
package feuille.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author util2
 */
public class Language {
    
    private Map<ISO_3166, String> translation = new HashMap<>();

    public Language() {
        
    }

    public void setTranslation(Map<ISO_3166, String> translation) {
        this.translation = translation;
    }

    public Map<ISO_3166, String> getTranslation() {
        return translation;
    }
    
    public void addTranslation(ISO_3166 iso, String translated) {
        translation.put(iso, translated);
    }
    
    public String getTranslated(ISO_3166 iso, String defaultDisplay){
        return translation.containsKey(iso) ? translation.get(iso) : defaultDisplay;
    }
    
}
