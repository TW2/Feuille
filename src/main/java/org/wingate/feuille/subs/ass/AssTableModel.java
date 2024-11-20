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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.wingate.feuille.util.DrawColor;
import org.wingate.feuille.util.ISO_3166;

/**
 *
 * @author util2
 */
public class AssTableModel extends AbstractTableModel {
    
    public enum StatsValue {
        CPS, CPL;
    }
    
    private final StatsRenderer statsRenderer;
    private final NormalRenderer stringNormalRenderer;
    private final JTable table;
    private ASS ass;
    private StatsValue statsValue;
    private ISO_3166 link;

    public AssTableModel(JTable table) {
        this.table = table;
        ass = new ASS();
        statsRenderer = new StatsRenderer();
        stringNormalRenderer = new NormalRenderer();
        statsValue = StatsValue.CPS;
        link = null;
        
        init();
    }
    
    private void init(){
        table.setModel(this);
        table.setDefaultRenderer(Float.class, statsRenderer);
        
        table.setDefaultRenderer(String.class, stringNormalRenderer);
        table.setDefaultRenderer(Integer.class, new NormalRenderer());
        table.setDefaultRenderer(AssActor.class, new NormalRenderer());
        table.setDefaultRenderer(AssEffect.class, new NormalRenderer());
        table.setDefaultRenderer(AssStyle.class, new NormalRenderer());
        table.setDefaultRenderer(AssTime.class, new NormalRenderer());
        table.setDefaultRenderer(AssEvent.Type.class, new NormalRenderer());
    }
    
    public void updateColumnSize(){
        final TableColumnModel cm = table.getColumnModel();
        
        for(int i=0; i<table.getColumnCount(); i++){
            switch(i){
                case 0 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 1 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 2 -> { cm.getColumn(i).setPreferredWidth(50); }
                case 3 -> { cm.getColumn(i).setPreferredWidth(70); }
                case 4 -> { cm.getColumn(i).setPreferredWidth(70); }
                case 5 -> { cm.getColumn(i).setPreferredWidth(130); }
                case 6 -> { cm.getColumn(i).setPreferredWidth(130); }
                case 7 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 8 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 9 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 10 -> { cm.getColumn(i).setPreferredWidth(170); }
                case 11 -> { cm.getColumn(i).setPreferredWidth(50); }
                case 12 -> { cm.getColumn(i).setPreferredWidth(500); }
                case 13 -> { cm.getColumn(i).setPreferredWidth(500); }
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
        return 14;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0 -> { return Integer.class; } // Line Number
            case 1 -> { return AssEvent.Type.class; }
            case 2 -> { return Integer.class; }
            case 3 -> { return AssTime.class; }
            case 4 -> { return AssTime.class; }
            case 5 -> { return AssStyle.class; }
            case 6 -> { return AssActor.class; }
            case 7 -> { return Integer.class; }
            case 8 -> { return Integer.class; }
            case 9 -> { return Integer.class; }
            case 10 -> { return AssEffect.class; }
            case 11 -> { return Float.class; } // Statistics
            case 12 -> { return String.class; }
            case 13 -> { return String.class; }
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
            case 12 -> { return "Original Text"; }
            case 13 -> { return "Other Text"; }
        }
        
        return super.getColumnName(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object obj = null;
        AssEvent event = ass.getEvents().get(rowIndex);
        switch(columnIndex){
            case 0 -> { obj = rowIndex + 1; }
            case 1 -> { obj = event.getType(); }
            case 2 -> { obj = event.getLayer(); }
            case 3 -> { obj = event.getStart(); }
            case 4 -> { obj = event.getEnd(); }
            case 5 -> { obj = event.getStyle(); }
            case 6 -> { obj = event.getName(); }
            case 7 -> { obj = event.getMarginL(); }
            case 8 -> { obj = event.getMarginR(); }
            case 9 -> { obj = event.getMarginV(); }
            case 10 -> { obj = event.getEffect(); }
            case 12 -> { obj = event.getText(); }
            case 13 -> { obj = link == null ? "" : event.getText(link); }
            
            // 11 Statistics
            case 11 -> {
                switch(statsValue){
                    case CPS -> { obj = event.getCPS(); }
                    case CPL -> { obj = event.getCPL(); }
                }
            }
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
            case 3 -> { if(v instanceof AssTime x) event.setStart(x); }
            case 4 -> { if(v instanceof AssTime x) event.setEnd(x); }
            case 5 -> { if(v instanceof AssStyle x) event.setStyle(x); }
            case 6 -> { if(v instanceof AssActor x) event.setName(x); }
            case 7 -> { if(v instanceof Integer x) event.setMarginL(x); }
            case 8 -> { if(v instanceof Integer x) event.setMarginR(x); }
            case 9 -> { if(v instanceof Integer x) event.setMarginV(x); }
            case 10 -> { if(v instanceof AssEffect x) event.setEffect(x); }
            case 12 -> { if(v instanceof String x) event.setText(x); }
            case 13 -> { if(v instanceof String x && link != null) event.setText(link, x); }
            
            // 11 Statistics
            case 11 -> { 
                if(v instanceof Float){
                    switch(statsValue){
                        case CPS -> { event.getCPS(); }
                        case CPL -> { event.getCPL(); }
                    }                    
                }
            }
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
        statsRenderer.setCplThreshold(threshold);
    }
    
    public void setToCPS(){
        statsRenderer.setCps(true);
        statsValue = StatsValue.CPS;
    }
    
    public void setToCPL(){
        statsRenderer.setCps(false);
        statsValue = StatsValue.CPL;
    }
    
    public void setStripped(NormalRenderer.Stripped stripped){
        stringNormalRenderer.setStripped(stripped);
    }
    
    public void setPartiallyStrippedSymbol(String symbol){
        stringNormalRenderer.setPartiallyStrippedSymbol(symbol);
    }

    public ISO_3166 getLink() {
        return link;
    }

    public void setLink(ISO_3166 link) {
        this.link = link;
    }
    
    public class StatsRenderer extends JPanel implements TableCellRenderer {
        
        private final JLabel lblText = new JLabel("");
        private final JLabel lblColor = new JLabel("");
        private boolean cps = true; // false if CPL
        // 60 characters by line for highest inline
        // Minimum: 3, Maximum: 5000, Threshold: wanted good value
        private float cplThreshold = 60f;
        
        public StatsRenderer(){
            lblColor.setOpaque(true);
            setLayout(new BorderLayout());
            add(lblText, BorderLayout.CENTER);
            add(lblColor, BorderLayout.EAST);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            AssEvent event = ass.getEvents().get(row);
            
            if(value instanceof Float x){
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
                
                if(isSelected){
                    bg = UIManager.getColor("Table.selectionBackground");
                    fg = UIManager.getColor("Table.selectionForeground");
                }
                
                // Set text
                lblText.setHorizontalAlignment(SwingConstants.CENTER);
                String cpl = Integer.toString(Math.round(x));
                lblText.setText(cps ? String.format("%.2f", x) : cpl);
                lblText.setForeground(fg);
                // Get height of cell
                int size = table.getRowHeight(row);
                lblColor.setPreferredSize(new java.awt.Dimension(size, size));                
                // Set background color to border (make fake background)
                lblColor.setBorder(new LineBorder(bg, size / 4));
                // Set stats color to real background
                lblColor.setBackground(cps ? getCpsStats(x) : getCplStats(x));
                // Set background color to panel
                setBackground(bg);
                setForeground(fg);
                setToolTipText(String.format("%s %.2f", cps ? "CPS" : "CPL", x));
            }
            
            return this;
        }
        
        private Color getCpsStats(float cps){
            Color R = Color.red;
            Color Y = Color.yellow;
            Color G = Color.green;
            // Red to Yellow to Green
            // 0   to 0.5    to 1
            if(cps == 0.0f) return R;
            if(cps == 0.5f) return Y;
            if(cps >= 1.0f) return G;

            if(0.0f < cps && cps < 0.5f){
                float v = cps * 2f;
                return new Color(
                        (int)((R.getRed() * v) + (Y.getRed() * (1 - v))),
                        (int)((R.getGreen() * v) + (Y.getGreen() * (1 - v))),
                        (int)((R.getBlue() * v) + (Y.getBlue() * (1 - v)))
                );
            }else{
                float v = (cps - 0.5f) * 2f;
                return new Color(
                        (int)((Y.getRed() * v) + (G.getRed() * (1 - v))),
                        (int)((Y.getGreen() * v) + (G.getGreen() * (1 - v))),
                        (int)((Y.getBlue() * v) + (G.getBlue() * (1 - v)))
                );
            }
        }
        
        private Color getCplStats(float cpl){
            // Minimum      0.0f >> 0 (green)
            // Threshold    0.5f >> threshold (yellow)
            // Maximum      1.0f >> threshold x2 (red)
            float max = cplThreshold * 2f;
            float u = cpl / max;
            
            Color R = Color.red;
            Color Y = Color.yellow;
            Color G = Color.green;
            // Green to Yellow to Red
            // 0     to 0.5    to 1
            if(u <= 0.0f) return G;
            if(u == 0.5f) return Y;
            if(u >= 1.0f) return R;

            if(0.0f < u && u < 0.5f){
                float v = u * 2f;
                return new Color(
                        (int)((G.getRed() * v) + (Y.getRed() * (1 - v))),
                        (int)((G.getGreen() * v) + (Y.getGreen() * (1 - v))),
                        (int)((G.getBlue() * v) + (Y.getBlue() * (1 - v)))
                );
            }else{
                float v = (u - 0.5f) * 2f;
                return new Color(
                        (int)((Y.getRed() * v) + (R.getRed() * (1 - v))),
                        (int)((Y.getGreen() * v) + (R.getGreen() * (1 - v))),
                        (int)((Y.getBlue() * v) + (R.getBlue() * (1 - v)))
                );
            }
        }

        public boolean isCps() {
            return cps;
        }

        public void setCps(boolean cps) {
            this.cps = cps;
        }

        public float getCplThreshold() {
            return cplThreshold;
        }

        public void setCplThreshold(float cplThreshold) {
            this.cplThreshold = cplThreshold < 3 ? 3 : (cplThreshold > 5000 ? 5000 : cplThreshold);
        }
        
    }
    
    public class NormalRenderer extends JLabel implements TableCellRenderer {
    
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
