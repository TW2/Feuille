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
package feuille.panel;

import feuille.io.ASS;
import feuille.io.Event;
import feuille.io.Event.LineType;
import feuille.io.Style;
import feuille.renderer.AssEventTableRenderer;
import feuille.util.ISO_3166;
import feuille.util.Language;
import feuille.util.Time;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author util2
 */
public class Table extends javax.swing.JPanel {
    
    private DefaultTableModel dtmASS;
    private ASS ass = new ASS();
    private final AssEventTableRenderer assEventTableRenderer = new AssEventTableRenderer(ass.getEvents());
    private DefaultComboBoxModel dcbmLineType = new DefaultComboBoxModel();
    private DefaultComboBoxModel dcbmStyle = new DefaultComboBoxModel();
    private DefaultComboBoxModel dcbmName = new DefaultComboBoxModel();
    private SpinnerNumberModel snmLayer = new SpinnerNumberModel(0, 0, 9999, 1);
    private SpinnerNumberModel snmML = new SpinnerNumberModel(0, 0, 9999, 1);
    private SpinnerNumberModel snmMR = new SpinnerNumberModel(0, 0, 9999, 1);
    private SpinnerNumberModel snmMV = new SpinnerNumberModel(0, 0, 9999, 1);
    /**
     * Creates new form Table
     */
    public Table() {
        initComponents();
        init();
    }
    
    private void init(){
        
    }
    
    public void initializeTable(Language in, ISO_3166 get){        
        // Check if there is a requested language (forced)
        // and choose between posibilities
        if(in.isForced() == true){
            get = in.getIso();
        }
        
        // Fill in the table
        String[] tableHeader = new String[]{
            "#", 
            in.getTranslated("Type", get, "Type"), 
            in.getTranslated("Layer", get, "Layer"), 
            in.getTranslated("Start", get, "Start"),
            in.getTranslated("End", get, "End"),
            in.getTranslated("Margin_Left", get, "ML"),
            in.getTranslated("Margin_Right", get, "MR"),
            in.getTranslated("Vertical_Margin", get, "MV"),
            in.getTranslated("Style", get, "Style"),
            in.getTranslated("Name", get, "Name"),
            in.getTranslated("Effect", get, "Effect"),
            in.getTranslated("Text", get, "Text")};
        dtmASS = new DefaultTableModel(
                null,
                tableHeader
        ){
            Class[] types = new Class [] {
                String.class, String.class, String.class,
                String.class, String.class, String.class,
                String.class, String.class, String.class,
                String.class, String.class, String.class};
            boolean[] canEdit = new boolean [] {
                false, false, true,
                true, true, true,
                true, true, true};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        
        jTable1.setModel(dtmASS);
        
        TableColumn column;
        for (int i = 0; i < 12; i++) {
            column = jTable1.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(40);
                    break; //# (line number)
                case 1:
                    column.setPreferredWidth(150);
                    break; //Type
                case 2:
                    column.setPreferredWidth(40);
                    break; //Layer
                case 3:
                    column.setPreferredWidth(100);
                    break; //Start
                case 4:
                    column.setPreferredWidth(100);
                    break; //End
                case 5:
                    column.setPreferredWidth(20);
                    break; //ML
                case 6:
                    column.setPreferredWidth(20);
                    break; //MR
                case 7:
                    column.setPreferredWidth(20);
                    break; //MV
                case 8:
                    column.setPreferredWidth(150);
                    break; //Style
                case 9:
                    column.setPreferredWidth(150);
                    break; //Name
                case 10:
                    column.setPreferredWidth(150);
                    break; //Effect
                case 11:
                    column.setPreferredWidth(1500);
                    break; //Text
            }
        }
        
        jTable1.setDefaultRenderer(String.class, assEventTableRenderer);
        
//        // Populate combobox of linetype
//        cbLineType.setModel(dcbmLineType);
//        for(LineType lt : LineType.values()){
//            dcbmLineType.addElement(lt);
//        }
//        
//        // Layer spinner
//        spinnerLayer.setModel(snmLayer);
//        
//        // Margins LRV
//        spinnerML.setModel(snmML);
//        spinnerMR.setModel(snmMR);
//        spinnerMV.setModel(snmMV);
//        
//        // Style
//        cbStyle.setModel(dcbmStyle);
//        
//        // Name
//        cbNameActor.setModel(dcbmName);
    }
    
    /**
     * Set value
     * @param value A value
     * @param row Its row
     * @param column Its column
     */
    public void changeValueAt(String value, int row, int column){
        jTable1.setValueAt(value, row, column);
        Event ev = ass.getEvents().get(row);
        switch(column){
            case 1: ev.setLineType(LineType.from(value)); break;
            case 2: ev.setLayer(Integer.parseInt(value)); break;
            case 3: ev.setStartTime(Time.create(value)); break;
            case 4: ev.setEndTime(Time.create(value)); break;
            case 5: ev.setMarginL(Integer.parseInt(value)); break;
            case 6: ev.setMarginR(Integer.parseInt(value)); break;
            case 7: ev.setMarginV(Integer.parseInt(value)); break;
            case 8: ev.setStyle(value); break;
            case 9: ev.setName(value); break;
            case 10: ev.setEffect(value); break;
            case 11: ev.setText(value); break;
        }        
        ass.getEvents().set(row, ev);
        assEventTableRenderer.updateEvents(ass.getEvents());
    }
    
    /**
     * Add a line
     * @param ev An ASS event line
     */
    public void addLine(Event ev){
        Object[] row = new Object[]{
            "", // #
            ev.getLineType().toString(), // Type
            Integer.toString(ev.getLayer()), // Layer
            ev.getStartTime().toASSTime(), // Start time
            ev.getEndTime().toASSTime(), // End time
            Integer.toString(ev.getMarginL()), // Margin L
            Integer.toString(ev.getMarginR()), // Margin R
            Integer.toString(ev.getMarginV()), // Margin V
            ev.getStyle(), // Style
            ev.getName(), // Name
            ev.getEffect(), // Effects
            ev.getText() // Text
        };
        dtmASS.addRow(row);
        ass.getEvents().set(dtmASS.getRowCount() - 1, ev);
        assEventTableRenderer.updateEvents(ass.getEvents());
    }
    
    /**
     * Add a line
     * @param line An ASS event line 
     */
    public void addLine(String line){
        Event ev = Event.createFromASS(line);
        Object[] row = new Object[]{
            "", // #
            ev.getLineType().toString(), // Type
            Integer.toString(ev.getLayer()), // Layer
            ev.getStartTime().toASSTime(), // Start time
            ev.getEndTime().toASSTime(), // End time
            Integer.toString(ev.getMarginL()), // Margin L
            Integer.toString(ev.getMarginR()), // Margin R
            Integer.toString(ev.getMarginV()), // Margin V
            ev.getStyle(), // Style
            ev.getName(), // Name
            ev.getEffect(), // Effects
            ev.getText() // Text
        };
        dtmASS.addRow(row);
        ass.getEvents().set(dtmASS.getRowCount() - 1, ev);
        assEventTableRenderer.updateEvents(ass.getEvents());
    }
    
    /**
     * Add a line
     * @param type A line type (LineType)
     * @param layer A layer (Integer)
     * @param start A time (Time)
     * @param end A time (Time)
     * @param ml A margin L (Integer)
     * @param mr A margin R (Integer)
     * @param mv A margin V (Integer)
     * @param style A style (Style)
     * @param name A name/actor (String)
     * @param effects A list of effects (String)
     * @param text A text (String)
     */
    public void addLine(LineType type, int layer, Time start, Time end, 
            int ml, int mr, int mv, Style style, String name, String effects, String text){
        Object[] row = new Object[]{
            "", // #
            type.toString(),
            Integer.toString(layer),
            start.toASSTime(),
            end.toASSTime(),
            Integer.toString(ml),
            Integer.toString(mr),
            Integer.toString(mv),
            style.getName(),
            name,
            effects,
            text
        };
        dtmASS.addRow(row);
        Event ev = new Event();
        ev.setLineType(type);
        ev.setLayer(layer);
        ev.setStartTime(start);
        ev.setEndTime(end);
        ev.setMarginL(ml);
        ev.setMarginR(mr);
        ev.setMarginV(mv);
        ev.setStyle(style.getName());
        ev.setName(name);
        ev.setEffect(effects);
        ev.setText(text);
        ass.getEvents().set(dtmASS.getRowCount() - 1, ev);
        assEventTableRenderer.updateEvents(ass.getEvents());
    }
    
    /**
     * Remove a line
     * @param row A row index
     */
    public void removeLineAt(int row){
        dtmASS.removeRow(row);
        ass.getEvents().remove(row);
        assEventTableRenderer.updateEvents(ass.getEvents());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
