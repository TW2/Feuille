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
package feuille.drawing;

import feuille.drawing.shape.AShape;
import feuille.panel.Draw;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author util2
 */
public class History {
    
    private final Draw draw;
    private final Sketchpad sketchpad;
    
    private final List<HistoryItem> objects = new ArrayList<>();
    private int current = -1;

    public History(Draw draw, Sketchpad sketchpad) {
        this.draw = draw;
        this.sketchpad = sketchpad;
    }

    public List<HistoryItem> getObjects() {
        return objects;
    }
        
    public void addItem(HistoryItemType type, Object tag){
        if(current != objects.size()-1){
            removeItems();
        }
        
        HistoryItem hi = new HistoryItem(type.getIcon(), type.toString(), tag);
        objects.add(hi);
        draw.getHistoryListModel().addElement(hi);
        current++;
        
        sketchpad.repaint();
    }
    
    public void removeItems(int from){
        if(from >= 0 && from < objects.size()){
            for(int i=objects.size()-1; i>=from; i--){
                objects.remove(i);
                draw.getHistoryListModel().remove(i);
            }
            current = objects.size() - 1;
            
            sketchpad.repaint();
        }        
    }
    
    public void removeItems(){
        removeItems(current+1);
    }
    
    public void undo(){
        if(current >= 0){
            current--;
        }
    }
    
    public void redo(){
        if(current < objects.size() - 1){
            current++;
        }
    }
    
    public List<AShape> getListOfShapes(){
        List<AShape> shapes = new ArrayList<>();
        for(int i=0; i<=current; i++){
            HistoryItem hi = objects.get(i);
            if(hi.getObject() instanceof AShape){
                shapes.add((AShape)hi.getObject());
            }
        }
        return shapes;
    }
    
    public int getCurrentIndex(){
        return current;
    }
}
