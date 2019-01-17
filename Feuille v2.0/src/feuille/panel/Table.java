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

import feuille.util.ISO_3166;
import feuille.util.Language;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author util2
 */
public class Table extends javax.swing.JPanel {

    DefaultTableModel dtmASS;
    
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
        //Fill in the table
        String[] tableHeader = new String[]{
            "#", 
            in.getTranslated(get, "Type"), 
            in.getTranslated(get, "Layer"), 
            in.getTranslated(get, "ML"),
            in.getTranslated(get, "MR"),
            in.getTranslated(get, "MV"),
            in.getTranslated(get, "Start"),
            in.getTranslated(get, "End"),
            in.getTranslated(get, "Total"),
            in.getTranslated(get, "Style"),
            in.getTranslated(get, "Name"),
            in.getTranslated(get, "Effect"),
            in.getTranslated(get, "Text")};
        dtmASS = new DefaultTableModel(
                null,
                tableHeader
        ){
            Class[] types = new Class [] {
                String.class, String.class, String.class,
                String.class, String.class, String.class,
                String.class, String.class, String.class,
                String.class, String.class, String.class,
                String.class};
            boolean[] canEdit = new boolean [] {
                false, false, true,
                true, true, true,
                true, true, true,
                true};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        
        jTable1.setModel(dtmASS);
        
        TableColumn column;
        for (int i = 0; i < 13; i++) {
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
                    column.setPreferredWidth(20);
                    break; //ML
                case 4:
                    column.setPreferredWidth(20);
                    break; //MR
                case 5:
                    column.setPreferredWidth(20);
                    break; //MV
                case 6:
                    column.setPreferredWidth(100);
                    break; //Start
                case 7:
                    column.setPreferredWidth(100);
                    break; //End
                case 8:
                    column.setPreferredWidth(100);
                    break; //Total
                case 9:
                    column.setPreferredWidth(150);
                    break; //Style
                case 10:
                    column.setPreferredWidth(150);
                    break; //Name
                case 11:
                    column.setPreferredWidth(150);
                    break; //Effect
                case 12:
                    column.setPreferredWidth(1000);
                    break; //Text
            }
        }
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
