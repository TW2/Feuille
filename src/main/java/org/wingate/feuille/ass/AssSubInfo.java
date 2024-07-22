/*
 * Copyright (C) 2021 util2
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

/**
 *
 * @author util2
 */
public enum AssSubInfo {
    Title("Title"),
    OriginalScript("Original Script"),
    OriginalTranslation("Original Translation"),
    OriginalEditing("Original Editing"),
    OriginalTiming("Original Timing"),
    SynchPoint("Synch Point"),
    ScriptUpdatedBy("Script Updated By"),
    UpdateDetails("Update Details"),
    ScriptType("ScriptType"),
    Collisions("Collisions"),
    PlayResY("PlayResY"),
    PlayResX("PlayResX"),
    PlayDepth("PlayDepth"),
    Timer("Timer"),
    WrapStyle("WrapStyle");
    
    String property;
    
    private AssSubInfo(String property){
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
    
    @Override
    public String toString(){
        return property;
    }
}
