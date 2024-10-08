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

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import org.wingate.feuille.ass.ASS;
import org.wingate.feuille.ass.AssEvent;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXAbstract;
import org.wingate.feuille.theme.Theme;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.GenericFileFilter;

/**
 *
 * @author util2
 */
public class KaraokePanel extends javax.swing.JPanel {
    
    private final Theme theme;
    
    private ASS ass = null;
    
    private final KaraokeTableModel tableModel = new KaraokeTableModel();
    private final DefaultComboBoxModel cboxModel = new DefaultComboBoxModel();

    /**
     * Creates new form KaraokePanel
     * @param theme
     */
    public KaraokePanel(Theme theme) {
        initComponents();
        
        cboxAFMEffects.setModel(cboxModel);
        cboxAFMEffects.setRenderer(new TemplateListCellRenderer());
        
        jTable1.setModel(tableModel);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(0).setMinWidth(40);
        jTable1.getColumnModel().getColumn(1).setMinWidth(600);
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        jTable1.setDefaultRenderer(AssEvent.class, new HighLightEditedRenderer());
        
        for(javax.swing.filechooser.FileFilter ff : fcOpenScript.getChoosableFileFilters()){
            fcOpenScript.removeChoosableFileFilter(ff);
        }
        fcOpenScript.addChoosableFileFilter(new GenericFileFilter("ass", "Advanced SubStation Alpha files"));
        
        for(javax.swing.filechooser.FileFilter ff : fcSaveScript.getChoosableFileFilters()){
            fcSaveScript.removeChoosableFileFilter(ff);
        }
        fcSaveScript.addChoosableFileFilter(new GenericFileFilter("ass", "Advanced SubStation Alpha files"));
        
        for(javax.swing.filechooser.FileFilter ff : fcOpenCode.getChoosableFileFilters()){
            fcOpenCode.removeChoosableFileFilter(ff);
        }
        fcOpenCode.addChoosableFileFilter(new GenericFileFilter("js", "JavaScript script files"));
        fcOpenCode.addChoosableFileFilter(new GenericFileFilter("py", "Python script files"));
        fcOpenCode.addChoosableFileFilter(new GenericFileFilter("rb", "Ruby script files"));
        fcOpenCode.addChoosableFileFilter(new GenericFileFilter("lua", "Lua script files"));
        
        for(javax.swing.filechooser.FileFilter ff : fcSaveCode.getChoosableFileFilters()){
            fcSaveCode.removeChoosableFileFilter(ff);
        }
        fcSaveCode.addChoosableFileFilter(new GenericFileFilter("js", "JavaScript script files"));
        fcSaveCode.addChoosableFileFilter(new GenericFileFilter("py", "Python script files"));
        fcSaveCode.addChoosableFileFilter(new GenericFileFilter("rb", "Ruby script files"));
        fcSaveCode.addChoosableFileFilter(new GenericFileFilter("lua", "Lua script files"));
        
        this.theme = theme;
        init();
    }

    private void init(){
        theme.apply(this);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcOpenScript = new javax.swing.JFileChooser();
        fcSaveScript = new javax.swing.JFileChooser();
        fcOpenCode = new javax.swing.JFileChooser();
        fcSaveCode = new javax.swing.JFileChooser();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnNewScript = new javax.swing.JButton();
        btnOpenScript = new javax.swing.JButton();
        btnSaveScript = new javax.swing.JButton();
        btnOneLine = new javax.swing.JButton();
        btnFewLines = new javax.swing.JButton();
        btnConfigureSFX = new javax.swing.JButton();
        btnUndo = new javax.swing.JButton();
        btnRedo = new javax.swing.JButton();
        cboxAFMEffects = new javax.swing.JComboBox<>();

        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Check", "Karaoke"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jToolBar2.setRollover(true);

        btnNewScript.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_newdocument.png"))); // NOI18N
        btnNewScript.setText("Add 1 line");
        btnNewScript.setEnabled(false);
        btnNewScript.setFocusable(false);
        btnNewScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewScript.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewScriptActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNewScript);

        btnOpenScript.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_folder.png"))); // NOI18N
        btnOpenScript.setText("Open script");
        btnOpenScript.setFocusable(false);
        btnOpenScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenScript.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenScriptActionPerformed(evt);
            }
        });
        jToolBar2.add(btnOpenScript);

        btnSaveScript.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_floppydisk.png"))); // NOI18N
        btnSaveScript.setText("Save script");
        btnSaveScript.setFocusable(false);
        btnSaveScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveScript.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveScriptActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSaveScript);

        btnOneLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AFM-ExecuteForOneLine.png"))); // NOI18N
        btnOneLine.setText("One line");
        btnOneLine.setFocusable(false);
        btnOneLine.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOneLine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOneLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOneLineActionPerformed(evt);
            }
        });
        jToolBar2.add(btnOneLine);

        btnFewLines.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/AFM-ExecuteForFewLines.png"))); // NOI18N
        btnFewLines.setText("Few lines");
        btnFewLines.setFocusable(false);
        btnFewLines.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFewLines.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFewLines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFewLinesActionPerformed(evt);
            }
        });
        jToolBar2.add(btnFewLines);

        btnConfigureSFX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32 engrenage.png"))); // NOI18N
        btnConfigureSFX.setText("Config");
        btnConfigureSFX.setFocusable(false);
        btnConfigureSFX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConfigureSFX.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConfigureSFX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigureSFXActionPerformed(evt);
            }
        });
        jToolBar2.add(btnConfigureSFX);

        btnUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32 losange carré.png"))); // NOI18N
        btnUndo.setText("Undo");
        btnUndo.setEnabled(false);
        btnUndo.setFocusable(false);
        btnUndo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUndo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });
        jToolBar2.add(btnUndo);

        btnRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32 modify.png"))); // NOI18N
        btnRedo.setText("Redo");
        btnRedo.setEnabled(false);
        btnRedo.setFocusable(false);
        btnRedo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRedo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedoActionPerformed(evt);
            }
        });
        jToolBar2.add(btnRedo);

        jPanel3.add(jToolBar2, java.awt.BorderLayout.NORTH);

        cboxAFMEffects.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboxAFMEffects.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxAFMEffectsItemStateChanged(evt);
            }
        });
        jPanel3.add(cboxAFMEffects, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewScriptActionPerformed
        // New line
        tableModel.addEvent(false, new AssEvent());
        jTable1.updateUI();
    }//GEN-LAST:event_btnNewScriptActionPerformed

    private void btnOpenScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenScriptActionPerformed
        // Clean and Open
        tableModel.clearEvents();
        jTable1.updateUI();
        
        int z = fcOpenScript.showOpenDialog(new javax.swing.JFrame());
        if(z == JFileChooser.APPROVE_OPTION){
            ass = ASS.Read(fcOpenScript.getSelectedFile().getPath());
            for(AssEvent ev : ass.getEvents()){
                tableModel.addEvent(false, ev);
            }
            jTable1.updateUI();
        }
    }//GEN-LAST:event_btnOpenScriptActionPerformed

    private void btnSaveScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveScriptActionPerformed
        // Save as
        int z = fcSaveScript.showSaveDialog(new javax.swing.JFrame());        
        if(z == JFileChooser.APPROVE_OPTION){
            ASS assa = ass;
            for(BiEvent bev : tableModel.getEvents()){
                assa.getEvents().addAll(bev.getTransformedAssEvents());
            }
            String path = fcSaveScript.getSelectedFile().getPath();
            if(path.endsWith(".ass") == false) path += ".ass";
            ASS.Save(path, assa);
        }
    }//GEN-LAST:event_btnSaveScriptActionPerformed

    private void btnFewLinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFewLinesActionPerformed
        // By few lines
        if(cboxAFMEffects.getSelectedItem() instanceof SFXAbstract sfx){
            List<BiEvent> bevts = sfx.forFewLines(tableModel.getActiveEvents());
            for(BiEvent b : bevts){
                // Original is in fact a modified one
                tableModel.addEvent(false, b.getOriginalAssEvent());
            }
            jTable1.updateUI();
        }
    }//GEN-LAST:event_btnFewLinesActionPerformed

    private void btnOneLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOneLineActionPerformed
        // By one line
        if(cboxAFMEffects.getSelectedItem() instanceof SFXAbstract sfx){
            for(BiEvent bev : tableModel.getActiveEvents()){
                List<BiEvent> bevts = sfx.forOneLine(bev);
                for(BiEvent b : bevts){
                    // Original is in fact a modified one
                    tableModel.addEvent(false, b.getOriginalAssEvent());
                }
            }
            jTable1.updateUI();
        }        
    }//GEN-LAST:event_btnOneLineActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRedoActionPerformed

    private void cboxAFMEffectsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxAFMEffectsItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboxAFMEffectsItemStateChanged

    private void btnConfigureSFXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigureSFXActionPerformed
        // Show settings dialog
        SettingsDialog setd = new SettingsDialog(new javax.swing.JFrame(), true);
        setd.showDialog();
        if(setd.getDialogResult() == DialogResult.Ok){
            cboxModel.removeAllElements();
            for(SFXAbstract sfx : setd.getTemplates()){
                cboxModel.addElement(sfx);
            }
        }
    }//GEN-LAST:event_btnConfigureSFXActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfigureSFX;
    private javax.swing.JButton btnFewLines;
    private javax.swing.JButton btnNewScript;
    private javax.swing.JButton btnOneLine;
    private javax.swing.JButton btnOpenScript;
    private javax.swing.JButton btnRedo;
    private javax.swing.JButton btnSaveScript;
    private javax.swing.JButton btnUndo;
    private javax.swing.JComboBox<String> cboxAFMEffects;
    private javax.swing.JFileChooser fcOpenCode;
    private javax.swing.JFileChooser fcOpenScript;
    private javax.swing.JFileChooser fcSaveCode;
    private javax.swing.JFileChooser fcSaveScript;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables
}
