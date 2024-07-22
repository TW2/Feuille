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
package org.wingate.feuille.m.ygg.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import org.wingate.feuille.ass.ASS;
import org.wingate.feuille.ass.AssEvent;
import org.wingate.feuille.ass.AssStyle;
import org.wingate.feuille.ass.AssTime;
import org.wingate.feuille.util.ISO_3166;

/**
 *
 * @author util2
 */
public class AssTableModel extends DefaultTableModel {
    
    // ASS
    private ASS ass = ASS.NoFileToLoad();
    
    // Languages
    private List<ISO_3166> languages = new ArrayList<>();

    public AssTableModel() {
        
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 1 -> { return AssEvent.LineType.class; } // 0 Type of line (Dialogue, comment)
            case 0, 2, 8, 9, 10 -> { return Integer.class; } // 1 Layer // 7 Margin L // 8 Margin R // 9 Margin V
            case 3, 4, 5 -> { return AssTime.class; } // 2 Start time // 3 End time // 4 Total time (end - start)
            case 6, 7, 11, 12 -> { return String.class; } // 5 Style // 6 Name // 10 Effect // 11 Text
            default -> { return String.class; }
        }
    }
    
    @Override
    public int getColumnCount() {
        return 13;
    }
    
    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0 -> { return "Line number"; }
            case 1 -> { return "Line type"; }
            case 2 -> { return "Layer"; }
            case 3 -> { return "Start"; }
            case 4 -> { return "End"; }
            case 5 -> { return "Duration"; }
            case 6 -> { return "Style"; }
            case 7 -> { return "Actor"; }
            case 8 -> { return "MarginL"; }
            case 9 -> { return "MarginR"; }
            case 10 -> { return "MarginV"; }
            case 11 -> { return "Effects"; }
            case 12 -> { return "Text"; }
        }
        return "";
    }
    
    @Override
    public int getRowCount() {
        return ass == null ? 0 : ass.getEvents().size();
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        AssEvent ev = ass.getEvents().get(row);
        switch(column){
            case 0 -> { return row + 1; }
            case 1 -> { return ev.getLineType(); }
            case 2 -> { return ev.getLayer(); }
            case 3 -> { return ev.getTime(); } // AssTime
            case 4 -> { return ev.getTime(); } // AssTime
            case 5 -> { return ev.getTime(); } // AssTime
            case 6 -> { return ev.getStyle().getName(); }
            case 7 -> { return ev.getName(); }
            case 8 -> { return ev.getMarginL(); }
            case 9 -> { return ev.getMarginR(); }
            case 10 -> { return ev.getMarginV(); }
            case 11 -> { return ev.getEffect(); }
            case 12 -> { return ev.getText(); }
        }
        return null;
    }
    
    @Override
    public void setValueAt(Object value, int row, int column) {                
        AssEvent ev = ass.getEvents().get(row);
        switch(column){
            case 0 -> { /* NOTHING to Apply */ }
            case 1 -> { ev.setLineType((AssEvent.LineType)value); }
            case 2 -> { ev.setLayer((int)value); }
            case 3 -> { ev.setTime((AssTime)value); } // AssTime
            case 4 -> { ev.setTime((AssTime)value); } // AssTime
            case 5 -> { ev.setTime((AssTime)value); } // AssTime
            case 6 -> { setStyle(ev, (String)value); }
            case 7 -> { ev.setName((String)value); }
            case 8 -> { ev.setMarginL((int)value); }
            case 9 -> { ev.setMarginR((int)value); }
            case 10 -> { ev.setMarginV((int)value); }
            case 11 -> { ev.setEffect((String)value); }
            case 12 -> { ev.setText((String)value); }
        }
    }

    public ASS getAss() {
        return ass;
    }

    public void setAss(ASS ass) {
        this.ass = ass;
    }

    public List<ISO_3166> getLanguages() {
        return languages;
    }

    public void setLanguages(List<ISO_3166> languages) {
        this.languages = languages;
    }
    
    private void setStyle(AssEvent ev, String styleName){
        for(Map.Entry<String, AssStyle> entry : ass.getStyles().entrySet()){
            if(entry.getKey().equals(styleName)){
                ev.setStyle(entry.getValue());
            }
        }
    }
    
    public void add(AssEvent.LineType l, int layer, String start, String end, String style,
            String name, int ml, int mr, int mv, String effect, String text){
        
        final AssEvent ev = new AssEvent();
        
        ev.setLineType(l);                          // Linetype
        ev.setLayer(layer);                         // Layer
        ev.setTime(new AssTime(start, end));        // Start time + End time
        setStyle(ev, style);                        // Style
        ev.setName(name);                           // Name
        ev.setMarginL(ml);                          // ML
        ev.setMarginR(mr);                          // MR
        ev.setMarginV(mv);                          // MV
        ev.setEffect(effect);                       // Effect
        ev.setText(text);                           // Text
        
        ass.getEvents().add(ev);
    }
    
    public void insert(int index, AssEvent.LineType l, int layer, String start, String end, String style,
            String name, int ml, int mr, int mv, String effect, String text){
        
        final AssEvent ev = new AssEvent();
        
        ev.setLineType(l);                          // Linetype
        ev.setLayer(layer);                         // Layer
        ev.setTime(new AssTime(start, end));        // Start time + End time
        setStyle(ev, style);                        // Style
        ev.setName(name);                           // Name
        ev.setMarginL(ml);                          // ML
        ev.setMarginR(mr);                          // MR
        ev.setMarginV(mv);                          // MV
        ev.setEffect(effect);                       // Effect
        ev.setText(text);                           // Text
        
        if(ass.getEvents().isEmpty() == false){
            ass.getEvents().add(index, ev);
        }
    }
    
    public void replace(int index, AssEvent.LineType l, int layer, String start, String end, String style,
            String name, int ml, int mr, int mv, String effect, String text){
        
        final AssEvent ev = new AssEvent();
        
        ev.setLineType(l);                          // Linetype
        ev.setLayer(layer);                         // Layer
        ev.setTime(new AssTime(start, end));        // Start time + End time
        setStyle(ev, style);                        // Style
        ev.setName(name);                           // Name
        ev.setMarginL(ml);                          // ML
        ev.setMarginR(mr);                          // MR
        ev.setMarginV(mv);                          // MV
        ev.setEffect(effect);                       // Effect
        ev.setText(text);                           // Text
        
        if(ass.getEvents().isEmpty() == false){
            ass.getEvents().set(index, ev);
        }
    }
    
    public void remove(int index){
        if(ass.getEvents().isEmpty() == false){
            ass.getEvents().remove(index);
        }
    }
    
    public void clear(){
        ass.getEvents().clear();
    }
    
    public void createNew(){
        ass = ASS.NoFileToLoad();
    }
}
