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
package feuille;

import feuille.io.ASS;
import feuille.io.Event;
import feuille.panel.*;
import feuille.util.ISO_3166;
import feuille.util.Language;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author util2
 */
public class MainFrame extends javax.swing.JFrame {
    
    // Language (loading from properties of each component)
    static ISO_3166 wantedIso = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
    static Language chosen = new Language();
    
    List<PanelGroup> groups = new ArrayList<>();
    PanelGroup defaultGroup = null;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        init();
    }
    
    private void init(){
        // Language
        ISO_3166 get = chosen.isForced() ? chosen.getIso() : wantedIso;
        
        // Look and Feel
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        // Dimension
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        GraphicsConfiguration gconf = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        Insets insets = toolkit.getScreenInsets(gconf);
        setSize(dim.width - insets.left - insets.right - 300,
                dim.height - insets.top - insets.bottom - 300);
        
        // Center the position
        setLocationRelativeTo(null);
        
        // Set divider location based on a 16/9 video size
        jSplitPane1.setDividerLocation(jSplitPane1.getHeight() * 16 / 9);
        
        // Creating a group
        defaultGroup = PanelGroup.create(chosen, wantedIso);        
        
        // Adding a table to SOUTH
        tabbedSOUTH.add(defaultGroup.getTable());
        tabbedSOUTH.setTitleAt(0, chosen.getTranslated("TableTab", get, "Subtitles table"));
        
        // Adding a chat to WEST
        tabbedWEST.add(defaultGroup.getChat());
        tabbedWEST.setTitleAt(0, chosen.getTranslated("ChatTab", get, "Chat"));
        
        // Adding a video to MIDDLE TOP/LEFT
        tabbedCTOP.add(defaultGroup.getVideo());
        tabbedCTOP.setTitleAt(0, chosen.getTranslated("VideoTab", get, "Viewer"));
        
        // Adding an ASSEditor to MIDDLE BOTTOM/RIGHT
        tabbedCBOTTOM.add(defaultGroup.getAssEditor());
        tabbedCBOTTOM.setTitleAt(0, chosen.getTranslated("AssEditorTab", get, "Advanced Sub Station Editor"));
        tabbedCBOTTOM.add(defaultGroup.getFxEditor());
        tabbedCBOTTOM.setTitleAt(1, chosen.getTranslated("FxEditorTab", get, "Effects editor"));
        
        // Adding a wave to NORTH
        tabbedNORTH.add(defaultGroup.getWave());
        tabbedNORTH.setTitleAt(0, chosen.getTranslated("WaveTab", get, "Waveform"));
        tabbedNORTH.add(defaultGroup.getSpectrogram());
        tabbedNORTH.setTitleAt(1, chosen.getTranslated("SpecTab", get, "Spectrogram"));
        
        // Language for elements of MainFrame        
        mnuFile.setText(chosen.getTranslated("MnuFile", get, "File"));
        mnuEdit.setText(chosen.getTranslated("MnuEdit", get, "Edit"));
        miOpenProject.setText(chosen.getTranslated("MnuFileOpenProj", get, "Open project..."));
        miSaveProject.setText(chosen.getTranslated("MnuFileSaveProj", get, "Save project..."));
        miOpenSubtitleTable.setText(chosen.getTranslated("MnuFileViewSubt", get, "View subtitles..."));
    }
    
    public static Language getLanguage(){
        return chosen;
    }
    
    public static ISO_3166 getISOCountry(){
        return chosen.isForced() ? chosen.getIso() : wantedIso;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedNORTH = new javax.swing.JTabbedPane();
        tabbedSOUTH = new javax.swing.JTabbedPane();
        tabbedWEST = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        tabbedCTOP = new javax.swing.JTabbedPane();
        tabbedCBOTTOM = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        miOpenProject = new javax.swing.JMenuItem();
        miSaveProject = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miOpenSubtitleTable = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Feuille v2.0 - 2019.1 (GraalVM Experimental) :: 'Oh my god!'");

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setLeftComponent(tabbedCTOP);
        jSplitPane1.setRightComponent(tabbedCBOTTOM);

        mnuFile.setText("File");

        miOpenProject.setText("Open project...");
        miOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenProjectActionPerformed(evt);
            }
        });
        mnuFile.add(miOpenProject);

        miSaveProject.setText("Save project...");
        miSaveProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSaveProjectActionPerformed(evt);
            }
        });
        mnuFile.add(miSaveProject);
        mnuFile.add(jSeparator1);

        miOpenSubtitleTable.setText("View subtitle");
        miOpenSubtitleTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenSubtitleTableActionPerformed(evt);
            }
        });
        mnuFile.add(miOpenSubtitleTable);

        jMenuBar1.add(mnuFile);

        mnuEdit.setText("Edit");
        jMenuBar1.add(mnuEdit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedNORTH)
            .addComponent(tabbedSOUTH)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedWEST, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedNORTH, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedWEST)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedSOUTH, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenProjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miOpenProjectActionPerformed

    private void miSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSaveProjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miSaveProjectActionPerformed

    private void miOpenSubtitleTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenSubtitleTableActionPerformed
        JFileChooser fc = new JFileChooser();
        for(javax.swing.filechooser.FileFilter ff : fc.getChoosableFileFilters()){
            fc.removeChoosableFileFilter(ff);
        }
        fc.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.isDirectory()) { return true; }
                return file.getName().toLowerCase().endsWith(".ass");
            }

            @Override
            public String getDescription() {
                // Language
                ISO_3166 get = chosen.isForced() ? chosen.getIso() : wantedIso;
                return chosen.getTranslated("ASSDescription", get, "Advanced Sub Station Files (*.ass)");
            }
        });
        int z = fc.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            String path = fc.getSelectedFile().getPath();
            ASS ass = ASS.Read(path);
            for(Event ev : ass.getEvents()){
                defaultGroup.getTable().addLine(ev);
            }
        }
    }//GEN-LAST:event_miOpenSubtitleTableActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem miOpenProject;
    private javax.swing.JMenuItem miOpenSubtitleTable;
    private javax.swing.JMenuItem miSaveProject;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JTabbedPane tabbedCBOTTOM;
    private javax.swing.JTabbedPane tabbedCTOP;
    private javax.swing.JTabbedPane tabbedNORTH;
    private javax.swing.JTabbedPane tabbedSOUTH;
    private javax.swing.JTabbedPane tabbedWEST;
    // End of variables declaration//GEN-END:variables
    
}
