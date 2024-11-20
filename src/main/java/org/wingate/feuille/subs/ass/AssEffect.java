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
package org.wingate.feuille.subs.ass;

/**
 *
 * @author util2
 */
public class AssEffect {
    private String name;
    private String effect;

    public AssEffect(String name, String effect) {
        this.name = name;
        this.effect = effect;
    }

    public AssEffect(String effect) {
        this("Default", effect);
    }

    public AssEffect() {
        this("Default", "");
    }
    
    public static AssEffect fromLine(String rawline){
        // [FXs]
        // Format: Name, Effect
        // FX: Banner,{\\move(100,200,300,120)}
        AssEffect fx = new AssEffect();
        String[] t = rawline.split(",");
        
        fx.name = t[0].substring(t[0].indexOf(" ") + 1);
        fx.effect = t[1];
        
        return fx;
    }
    
    public String toLine(){
        // [FXs]
        // Format: Name, Effect
        // FX: Banner,{\\move(100,200,300,120)}
        StringBuilder line = new StringBuilder("FX: ");
        line.append(name).append(",");
        line.append(effect);
        return line.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return getName();
    }
    
}
