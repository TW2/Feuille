/*
 * Copyright (C) 2020 util2
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
package org.wingate.feuille.ass;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author util2
 */
public class AssStylesCollection {
    
    private String collectionName = "Default collection name";
    private Map<String, AssStyle> styles = new HashMap<>();
        
    private Map<AssStyle, Boolean> enableStyles = new HashMap<>();

    public AssStylesCollection() {
    }
    
    public static AssStylesCollection create(String collectionName, Map<String, AssStyle> styles){
        AssStylesCollection sc = new AssStylesCollection();
        
        sc.collectionName = collectionName;
        sc.styles = styles;
        
        for(Map.Entry<String, AssStyle> entry : styles.entrySet()){
            sc.enableStyles.put(entry.getValue(), Boolean.TRUE);
        }
        
        return sc;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public Map<String, AssStyle> getStyles() {
        return styles;
    }

    public void setStyles(Map<String, AssStyle> styles) {
        this.styles = styles;
    }

    public Map<AssStyle, Boolean> getEnableStyles() {
        return enableStyles;
    }

    public void setEnableStyles(Map<AssStyle, Boolean> enableStyles) {
        this.enableStyles = enableStyles;
    }

    @Override
    public String toString() {
        return collectionName;
    }
    
}
