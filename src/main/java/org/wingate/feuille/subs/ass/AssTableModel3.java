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

import org.wingate.feuille.util.DrawColor;
import org.wingate.feuille.util.ISO_3166;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author util2
 */
public class AssTableModel3 extends AbstractTableModel {

    private final NormalRenderer stringNormalRenderer;
    private final JTable table;
    private static ASS ass;

    private AssStatistics stats;

    public AssTableModel3(JTable table) {
        this.table = table;
        ass = new ASS();
        stringNormalRenderer = new NormalRenderer();
        stats = new AssStatistics();
        init();
    }
    
    private void init(){
        table.setModel(this);
        table.setDefaultRenderer(AssStatistics.class, new AssStatistics.Renderer());
        
        table.setDefaultRenderer(String.class, stringNormalRenderer);
        table.setDefaultRenderer(Integer.class, new NormalRenderer());
        table.setDefaultRenderer(AssActor.class, new NormalRenderer());
        table.setDefaultRenderer(AssEffect.class, new NormalRenderer());
        table.setDefaultRenderer(AssStyle.class, new NormalRenderer());
        table.setDefaultRenderer(AssTimeExtra.class, new AssTimeExtra.Renderer());
        table.setDefaultRenderer(AssEvent.class, new AssEvent.Renderer());
        table.setDefaultRenderer(AssEvent.Type.class, new NormalRenderer());
    }

    public void updateColumnSize(){
        final TableColumnModel cm = table.getColumnModel();
        
        for(int i=0; i<table.getColumnCount(); i++){
            switch(i){
                case 0, 1, 7, 8, 9 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 2, 11 -> { cm.getColumn(i).setPreferredWidth(50); }
                case 3, 4 -> { cm.getColumn(i).setPreferredWidth(70); }
                case 5, 6 -> { cm.getColumn(i).setPreferredWidth(130); }
                case 10 -> { cm.getColumn(i).setPreferredWidth(170); }
                case 12 -> { cm.getColumn(i).setPreferredWidth(1000); }
            }            
        }
        
        table.updateUI();
    }

    @Override
    public int getRowCount() {
        return ass.getEvents().size();
    }

    @Override
    public int getColumnCount() {        
        return 13;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0 -> { return Integer.class; } // Line Number
            case 1 -> { return AssEvent.Type.class; }
            case 2, 7, 8, 9 -> { return Integer.class; }
            case 3, 4 -> { return AssTimeExtra.class; }
            case 5 -> { return AssStyle.class; }
            case 6 -> { return AssActor.class; }
            case 10 -> { return AssEffect.class; }
            case 11 -> { return AssStatistics.class; } // Statistics
            case 12 -> { return AssEvent.class; }
        }
        
        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0 -> { return "#"; }
            case 1 -> { return "Type"; }
            case 2 -> { return "Layer"; }
            case 3 -> { return "Start"; }
            case 4 -> { return "End"; }
            case 5 -> { return "Style"; }
            case 6 -> { return "Actor"; }
            case 7 -> { return "ML"; }
            case 8 -> { return "MR"; }
            case 9 -> { return "MV"; }
            case 10 -> { return "FX"; }
            case 11 -> { return "Stats"; } // Statistics
            case 12 -> { return "Text"; }
        }
        
        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object obj = null;
        AssEvent event = ass.getEvents().get(rowIndex);
        stats.setEvent(event);
        
        // Set AssTimeExtra (start)
        AssTimeExtra eStart = new AssTimeExtra(event.getStart(), event.getStart());
        if(rowIndex > 0){
            double msStart = eStart.getTime().getMsTime();
            double lastEnd = ass.getEvents().get(rowIndex - 1).getEnd().getMsTime();
            eStart.setDifference(new AssTime(msStart - lastEnd));
        }
        
        // Set AssTimeExtra (end)
        AssTimeExtra eEnd = new AssTimeExtra(event.getEnd(), event.getEnd());
        if(rowIndex > 0 && rowIndex < ass.getEvents().size() - 1){
            double msEnd = eEnd.getTime().getMsTime();
            double nextStart = ass.getEvents().get(rowIndex + 1).getStart().getMsTime();
            eEnd.setDifference(new AssTime(nextStart - msEnd));
        }
        
        switch(columnIndex){
            case 0 -> { obj = rowIndex + 1; }
            case 1 -> { obj = event.getType(); }
            case 2 -> { obj = event.getLayer(); }
            case 3 -> { obj = eStart; }
            case 4 -> { obj = eEnd; }
            case 5 -> { obj = event.getStyle(); }
            case 6 -> { obj = event.getName(); }
            case 7 -> { obj = event.getMarginL(); }
            case 8 -> { obj = event.getMarginR(); }
            case 9 -> { obj = event.getMarginV(); }
            case 10 -> { obj = event.getEffect(); }
            case 11 -> { obj = stats; } // Stats
            case 12 -> { obj = event; }
        }
        return obj;
    }

    @Override
    public void setValueAt(Object v, int rowIndex, int columnIndex) {
        AssEvent event = ass.getEvents().get(rowIndex);
        switch(columnIndex){
            // 0 Line Number
            case 1 -> { if(v instanceof AssEvent.Type x) event.setType(x); }
            case 2 -> { if(v instanceof Integer x) event.setLayer(x); }
            case 3 -> { if(v instanceof AssTimeExtra x) event.setStart(x.getTime()); }
            case 4 -> { if(v instanceof AssTimeExtra x) event.setEnd(x.getTime()); }
            case 5 -> { if(v instanceof AssStyle x) event.setStyle(x); }
            case 6 -> { if(v instanceof AssActor x) event.setName(x); }
            case 7 -> { if(v instanceof Integer x) event.setMarginL(x); }
            case 8 -> { if(v instanceof Integer x) event.setMarginR(x); }
            case 9 -> { if(v instanceof Integer x) event.setMarginV(x); }
            case 10 -> { if(v instanceof AssEffect x) event.setEffect(x); }
            case 11 -> { if(v instanceof AssStatistics x) stats = x; }
            case 12 -> { if(v instanceof AssEvent x) event = x; }
        }
        ass.getEvents().set(rowIndex, event);
        fireTableCellUpdated(rowIndex, columnIndex);
        table.updateUI();
    }
    
    public void addValue(AssEvent event){
        int rowCount = ass.getEvents().size();
        ass.getEvents().add(event);
        fireTableRowsInserted(rowCount, rowCount);
        table.updateUI();
    }
    
    public void insertValueAt(AssEvent event, int row){
        ass.getEvents().add(row, event);
        fireTableRowsInserted(row, row);
        table.updateUI();
    }
    
    public void removeValueAt(int row){
        ass.getEvents().remove(row);
        fireTableRowsDeleted(row, row);
        table.updateUI();
    }
    
    public void replaceValueAt(AssEvent event, int row){
        ass.getEvents().set(row, event);
        fireTableRowsUpdated(row, row);
        table.updateUI();
    }

    public ASS getAss() {
        return ass;
    }

    public void setAss(ASS ass) {
        this.ass = ass;
        table.updateUI();
    }

    public void setThreshold(float threshold) {
        // CPL threshold (60f by default)
//        statsRenderer.setCplThreshold(threshold);
    }
    
    public void setStripped(NormalRenderer.Stripped stripped){
        stringNormalRenderer.setStripped(stripped);
    }
    
    public void setPartiallyStrippedSymbol(String symbol){
        stringNormalRenderer.setPartiallyStrippedSymbol(symbol);
    }
    
    public static class NormalRenderer extends JLabel implements TableCellRenderer {
    
        public enum Stripped {
            Off, Partially, On; 
        }
        
        private Stripped stripped = Stripped.Partially;
        private String partiallyStrippedSymbol = "◆";
        
        public NormalRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            AssEvent event = ass.getEvents().get(row);
            
            Color bg;
            
            if(event.getType() == AssEvent.Type.Comment){
                bg = DrawColor.violet.getColor();
            }else{
                // Get table background (avoid searching from any other way)
                // FlatLaf properties >> Table.background
                bg = UIManager.getColor("Table.background");
            }
            
            // Get table foreground (avoid searching from any other way)
            // FlatLaf properties >> Table.foreground
            Color fg = UIManager.getColor("Table.foreground");
            
            switch(value){
                case AssEvent.Type x -> { setText(x == AssEvent.Type.Comment ? "#" : "D"); }
                case AssTime x -> { setText(x.toString()); }
                case AssStyle x -> { setText(x.toString()); }
                case AssActor x -> { setText(x.toString()); }
                case AssEffect x -> { setText(x.toString()); }
                case Integer x -> { setText(Integer.toString(x)); }
                case String x -> { setText(applyStrip(x)); }
                default -> {}
            }
            
            if(isSelected){
                bg = UIManager.getColor("Table.selectionBackground");
                fg = UIManager.getColor("Table.selectionForeground");
            }
            
            // Set color to label
            setBackground(bg);
            setForeground(fg);            
            
            return this;
        }
        
        private String applyStrip(String s){
            String str = "";
            switch(stripped){
                case On -> {
                    if(s.contains("{\\")){
                        try {
                            str = s.replaceAll("\\{[^\\}]+\\}", "");
                        } catch (Exception e) {
                            str = s;
                        }
                    }else{
                        str = s;
                    }
                }
                case Partially -> {
                    if(s.contains("{\\")){
                        try {
                            str = s.replaceAll("\\{[^\\}]+\\}", partiallyStrippedSymbol);
                        } catch (Exception e) {
                            str = s;
                        }
                    }else{
                        str = s;
                    }
                }
                case Off -> {
                    str = s;
                }
            }
            return str;
        }

        public void setStripped(Stripped stripped) {
            this.stripped = stripped;
        }

        public void setPartiallyStrippedSymbol(String partiallyStrippedSymbol) {
            this.partiallyStrippedSymbol = partiallyStrippedSymbol;
        }
        
    }
}
