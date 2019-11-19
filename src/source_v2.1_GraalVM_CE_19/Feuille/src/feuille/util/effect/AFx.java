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

import feuille.MainFrame;
import feuille.util.ISO_3166;
import feuille.util.Language;
import java.util.ArrayList;
import java.util.List;

/**
 * Define an effect (which can content others effects if transition)
 * @author util2
 */
public abstract class AFx implements IFx {
    
    protected FxType fxType = FxType.Animation;
    protected List<AFx> sfx = new ArrayList<>();
    protected Language in = MainFrame.getLanguage();
    protected ISO_3166 iso = MainFrame.getISOCountry();
    protected List<Parameter> params = new ArrayList<>();
    protected String name = "Unknown effect";
    protected int uniqueID = -1;

    public AFx() {
        
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setFxType(FxType fxType) {
        this.fxType = fxType;
    }

    public FxType getFxType() {
        return fxType;
    }

    @Override
    public List<AFx> getFxObjects() {
        return sfx;
    }

    @Override
    public void addFxObject(AFx afx) {
        boolean add = true;
        for(AFx search : sfx){
            if(search.getName().equalsIgnoreCase(afx.getName()) == true){
                add = false;
                break;
            }
        }
        if(add == true){
            sfx.add(afx);
        }
    }

    @Override
    public void removeFxObject(AFx afx) {
        int index = -1;
        for(int i=0; i<sfx.size(); i++){
            AFx search = sfx.get(i);
            if(search.getName().equalsIgnoreCase(afx.getName()) == true){
                index = i;
                break;
            }
        }
        if(index != -1){
            sfx.remove(index);
        }
    }

    @Override
    public void clearObjectsList() {
        sfx.clear();
    }

    @Override
    public String toString() {
        return name;
    }
    
}
