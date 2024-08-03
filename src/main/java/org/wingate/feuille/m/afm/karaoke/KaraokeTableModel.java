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
    
    private final List<BiEvent> events;
    private List<AssEvent> memoryEvents;

    public KaraokeTableModel() {
        events = new ArrayList<>();
        memoryEvents = new ArrayList<>();
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
            case 0 -> { return events.get(row).isActive(); }
            case 1 -> { return events.get(row).getEvent(); }
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
                events.get(row).setActive(bool);
            }
            case 1 -> {
                AssEvent event;
                if(aValue instanceof AssEvent ev){
                    event = ev;
                }else{
                    event = new AssEvent();
                }
                events.get(row).setOriginalAssEvent(event);
            }
            default -> { }
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch(column){
            case 0 -> { return true; }
            case 1 -> { return false; }
            default -> { return false; }
        }
    }

    public List<BiEvent> getEvents() {
        return events;
    }
    
    public void addEvent(boolean b, AssEvent ev){
        events.add(new BiEvent(b, ev));
    }
    
    public void insertEvent(int index, boolean b, AssEvent ev){
        if(index >= events.size()) return;
        events.remove(index);
        addEvent(b, ev);
    }
    
    public void removeEvent(int index){
        if(index >= events.size()) return;
        events.remove(index);
    }
    
    public void clearEvents(){
        events.clear();
    }

    public List<AssEvent> getMemoryEvents() {
        return memoryEvents;
    }

    public void setMemoryEvents(List<AssEvent> memoryEvents) {
        this.memoryEvents = memoryEvents;
    }
}
