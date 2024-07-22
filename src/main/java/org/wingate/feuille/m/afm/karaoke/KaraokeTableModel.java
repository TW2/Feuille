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
package org.wingate.feuille.m.afm.karaoke;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.wingate.feuille.ass.AssEvent;

/**
 *
 * @author util2
 */
public class KaraokeTableModel extends DefaultTableModel {
    
    private List<Boolean> actives = new ArrayList<>();
    private List<AssEvent> events = new ArrayList<>();
    
    private List<Boolean> memoryActives = new ArrayList<>();
    private List<AssEvent> memoryEvents = new ArrayList<>();

    public KaraokeTableModel() {
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0 -> { return Boolean.class; }
            case 1 -> { return AssEvent.class; }
            default -> { return Object.class; }
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0 -> { return "Check"; }
            case 1 -> { return "Karaoke"; }
            default -> { return ""; }
        }
    }

    @Override
    public int getRowCount() {
        return events == null ? 0 : events.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch(column){
            case 0 -> { return actives.get(row); }
            case 1 -> { return events.get(row); }
            default -> { return null; }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        switch(column){
            case 0 -> {
                boolean bool;
                if(aValue instanceof Boolean b){
                    bool = b;
                }else{
                    bool = false;
                }
                actives.remove(row);
                actives.add(row, bool);
            }
            case 1 -> {
                AssEvent event;
                if(aValue instanceof AssEvent ev){
                    event = ev;
                }else{
                    event = new AssEvent();
                }
                events.remove(row);
                events.add(row, event);
            }
            default -> { }
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }
    
    public void addEvent(boolean b, AssEvent ev){
        actives.add(b);
        events.add(ev);
    }
    
    public void insertEvent(int index, boolean b, AssEvent ev){
        if(index >= events.size()) return;
        actives.remove(index);
        events.remove(index);
        actives.add(index, b);
        events.add(index, ev);
    }
    
    public void removeEvent(int index){
        if(index >= events.size()) return;
        actives.remove(index);
        events.remove(index);
    }
    
    public void clearEvents(){
        actives.clear();
        events.clear();
    }
    
    public void showResult(List<AssEvent> evts){
        if(evts.isEmpty() == true || actives.isEmpty() == true || events.isEmpty() == true) return;
        memoryActives = actives;
        memoryEvents = events;
        actives.clear();
        events.clear();
        for(AssEvent ev : evts){
            actives.add(true);
            events.add(ev);
        }
    }
    
    public void showKaraoke(){
        if(memoryActives.isEmpty() == true || memoryEvents.isEmpty() == true) return;
        actives = memoryActives;
        events = memoryEvents;
        memoryActives.clear();
        memoryEvents.clear();
    }
}
