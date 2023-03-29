/*
 * Copyright (C) 2023 util2
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
package org.wingate.virginsheet.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import org.wingate.assj.ASS;
import org.wingate.assj.AssEvent;
import org.wingate.virginsheet.util.Clipboard;

/**
 *
 * @author util2
 */
public class KaraokeTableModel extends AbstractTableModel {
    
    /*
    Column :
    - Line number
    - Layer
    - Start time
    - End time
    - ML
    - MR
    - MV    
    - Style
    - Name
    - Effect
    - CPS
    - CPL
    - Text    
    */
    
    private KaraokeObject karao = new KaraokeObject();
    private final KaraokePanel karaokePanel;
    private final JTable parentTable;
    
    public KaraokeTableModel(JTable parentTable, KaraokePanel karaokePanel){
        this.parentTable = parentTable;
        this.karaokePanel = karaokePanel;
        init();
    }
    
    private void init(){
        parentTable.setModel(this);
        parentTable.setDoubleBuffered(true);
        
        TableColumn col;
        for(int i=0; i<getColumnCount(); i++){
            col = parentTable.getColumnModel().getColumn(i);
            switch(i){
                case 0 -> { col.setPreferredWidth(20); } // Line number
                case 1 -> { col.setPreferredWidth(20); } // Layer
                case 2 -> { col.setPreferredWidth(60); } // Start time
                case 3 -> { col.setPreferredWidth(60); } // End time
                case 4 -> { col.setPreferredWidth(20); } // ML
                case 5 -> { col.setPreferredWidth(20); } // MR
                case 6 -> { col.setPreferredWidth(20); } // MV
                case 7 -> { col.setPreferredWidth(80); } // Style
                case 8 -> { col.setPreferredWidth(80); } // Name
                case 9 -> { col.setPreferredWidth(80); } // Effect
                case 10 -> { col.setPreferredWidth(20); } // CPS
                case 11 -> { col.setPreferredWidth(20); } // CPL
                case 12 -> { col.setPreferredWidth(1000); } // Text
            }
        }
        
        parentTable.setDefaultRenderer(Integer.class, new KaraokeTableRenderer(this, karaokePanel));
        parentTable.setDefaultRenderer(String.class, new KaraokeTableRenderer(this, karaokePanel));
    }
    
    private void refresh(){
        fireTableDataChanged();
        parentTable.updateUI();
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            // Line number
            case 0 -> { return "Line"; }
            // Layer
            case 1 -> { return "Layer"; }
            // Start time
            case 2 -> { return "Start"; }
            // End time
            case 3 -> { return "End"; }
            // ML
            case 4 -> { return "ML"; }
            // MR
            case 5 -> { return "MR"; }
            // MV
            case 6 -> { return "MV"; }
            // Style
            case 7 -> { return "Style"; }
            // Name
            case 8 -> { return "Name"; }
            // Effect
            case 9 -> { return "Effect"; }
            // CPS
            case 10 -> { return "CPS"; }
            // CPL
            case 11 -> { return "CPL"; }
            // Text
            case 12 -> { return "Text"; }
            // Default case
            default -> { return ""; }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            // Line number
            case 0 -> { return Integer.class; }
            // Layer
            case 1 -> { return Integer.class; }
            // Start time
            case 2 -> { return String.class; }
            // End time
            case 3 -> { return String.class; }
            // ML
            case 4 -> { return Integer.class; }
            // MR
            case 5 -> { return Integer.class; }
            // MV
            case 6 -> { return Integer.class; }
            // Style
            case 7 -> { return String.class; }
            // Name
            case 8 -> { return String.class; }
            // Effect
            case 9 -> { return String.class; }
            // CPS
            case 10 -> { return String.class; }
            // CPL
            case 11 -> { return String.class; }
            // Text
            case 12 -> { return String.class; }
            // Default case
            default -> { return String.class; }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch(columnIndex){
            // Line number
            case 0 -> { return false; }
            // Layer
            case 1 -> { return true; }
            // Start time
            case 2 -> { return true; }
            // End time
            case 3 -> { return true; }
            // ML
            case 4 -> { return true; }
            // MR
            case 5 -> { return true; }
            // MV
            case 6 -> { return true; }
            // Style
            case 7 -> { return true; }
            // Name
            case 8 -> { return true; }
            // Effect
            case 9 -> { return true; }
            // CPS
            case 10 -> { return false; }
            // CPL
            case 11 -> { return false; }
            // Text
            case 12 -> { return true; }
            // Default case
            default -> { return false; }
        }
    }
    
    @Override
    public int getRowCount() {
        return karao.getAss().getEvents().size();
    }

    @Override
    public int getColumnCount() {
        return 13;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AssEvent ev = karao.getAss().getEvents().get(rowIndex);
        switch(columnIndex){
            // Line number
            case 0 -> { return rowIndex + 1; }
            // Layer
            case 1 -> { return ev.getLayer(); }
            // Start time
            case 2 -> { return ev.getStartTime().toASSTime(); }
            // End time
            case 3 -> { return ev.getEndTime().toASSTime(); }
            // ML
            case 4 -> { return ev.getMarginL(); }
            // MR
            case 5 -> { return ev.getMarginR(); }
            // MV
            case 6 -> { return ev.getMarginV(); }
            // Style
            case 7 -> { return ev.getStyle().getName(); }
            // Name
            case 8 -> { return ev.getName() == null ? "" : ev.getName(); }
            // Effect
            case 9 -> { return ev.getEffect(); }
            // CPS
            case 10 -> { return "0"; }
            // CPL
            case 11 -> { return "0"; }
            // Text
            case 12 -> { return ev.getText(); }
            // Default case
            default -> { return ""; }
        }
    }

    public ASS getAss() {
        return karao.getAss();
    }
    
    public ASS getAssCopy() {
        ASS copy = karao.getAss();
        File folder = new File("projects");
        File file = new File(folder, "temp.ass");
        ASS.Save(file.getPath(), copy);
        return copy;
    }

    public void setAss(ASS ass) {
        karao.setAss(ass);
        refresh();
    }

    public JTable getParentTable() {
        return parentTable;
    }
    
    public void addLine(AssEvent ev){
        karao.getAss().getEvents().add(ev);
        refresh();
    }
    
    public void removeLines(int[] indices){
        if(indices == null){
            karao.getAss().getEvents().clear();
        }else{
            for(int i=indices.length-1; i>=0; i--){
                karao.getAss().getEvents().remove(indices[i]);
            }
        }        
        refresh();
    }
    
    public void insertBefore(List<AssEvent> evts, int index){
        karao.getAss().getEvents().addAll(index, evts);
        refresh();
    }
    
    public void insertAfter(List<AssEvent> evts, int index){
        for(int i=0; i<evts.size(); i++){
            karao.getAss().getEvents().add(index + i + 1, evts.get(i));
        }
        refresh();
    }
    
    public void replaceLine(AssEvent ev, int index){
        karao.getAss().getEvents().remove(index);
        karao.getAss().getEvents().add(index, ev);
        refresh();
    }
    
    public void duplicateLines(int from, int to){
        List<AssEvent> list = new ArrayList<>();
        for(int i=0; i<karao.getAss().getEvents().size(); i++){
            if(i>=from && i<=to){
                list.add(karao.getAss().getEvents().get(i));
            }
        }
        insertAfter(list, to);
        // No need for refresh cause insertAfter do it itself
    }
    
    /*--------------------------------------------------------------------------
    Clipboard Cut Copy Paste
    --------------------------------------------------------------------------*/
    
    public void cut(){
        int[] indices = parentTable.getSelectedRows();
        List<AssEvent> list = new ArrayList<>();
        for(int i=0; i<indices.length; i++){
            list.add(karao.getAss().getEvents().get(indices[i]));
        }
        String lines = "";
        for(AssEvent ev : list){
            lines = lines + AssEvent.getAssEventLine(ev) + "\n";
        }
        Clipboard.CCopy(lines);
        removeLines(indices);
    }
    
    public void copy(){
        int[] indices = parentTable.getSelectedRows();
        List<AssEvent> list = new ArrayList<>();
        for(int i=0; i<indices.length; i++){
            list.add(karao.getAss().getEvents().get(indices[i]));
        }
        String lines = "";
        for(AssEvent ev : list){
            lines = lines + AssEvent.getAssEventLine(ev) + "\n";
        }
        Clipboard.CCopy(lines);
    }
    
    public void paste(){
        if(parentTable.getSelectedRowCount() == 0) return;
        List<AssEvent> list = new ArrayList<>();
        String lines = Clipboard.CPaste();
        try(StringReader sr = new StringReader(lines);
                BufferedReader br = new BufferedReader(sr);){
            String line;
            while((line = br.readLine()) != null){
                try{
                    AssEvent ev = AssEvent.createFromASS(line);
                    list.add(ev);
                }catch(Exception exc){
                    // Not an event that is readable
                }
            }
        }catch(Exception exc){
            
        }
        if(list.isEmpty() == true) return;
        insertAfter(list, parentTable.getSelectedRow());
    }
    
    public void duplicate(int times){
        if(parentTable.getSelectedRowCount() == 0) return;
        int[] indices = parentTable.getSelectedRows();
        List<AssEvent> list = new ArrayList<>();
        for(int i=0; i<indices.length; i++){
            list.add(karao.getAss().getEvents().get(indices[i]));
        }
        for(int i=0; i<times; i++){
            insertAfter(list, parentTable.getSelectedRow());
        }
    }
    
    public void remove(){
        int[] indices = parentTable.getSelectedRows();
        removeLines(indices);
    }
    
    public void clear(){
        removeLines(null);
    }

    public KaraokeObject getKaraokeObject() {
        return karao;
    }
    
    public void setKaraokeObject(KaraokeObject karao) {
        this.karao = karao;
    }
}
