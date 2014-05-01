/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.xtrn;

import feuille.xtrn.XtrnLib.ModeType;
import feuille.xtrn.XtrnLib.TreatmentType;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author Yves
 */
public class XKCStep3 extends javax.swing.JPanel {
    
    DefaultComboBoxModel comboModelMode = new DefaultComboBoxModel();
    DefaultComboBoxModel comboModelTreatment = new DefaultComboBoxModel();

    /**
     * Creates new form XKCStep3
     */
    public XKCStep3() {
        initComponents();
        init();
    }
    
    private void init(){
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        cbMode.setModel(comboModelMode);
        for(ModeType mode : ModeType.values()){
            comboModelMode.addElement(mode);
        }
        cbMode.setSelectedIndex(0); // Force le rendu de lblSummary
        
        cbTreatment.setModel(comboModelTreatment);
        for(TreatmentType tr : TreatmentType.values()){
            comboModelTreatment.addElement(tr);
        }
        cbTreatment.setSelectedIndex(0); // Force le rendu de lblSummary
    }
    
    public JButton getNextButton(){
        return btnNext;
    }
    
    public ModeType getSelectedModeType(){
        return (ModeType)cbMode.getSelectedItem();
    }
    
    public TreatmentType getSelectedTreatmentType(){
        return (TreatmentType)cbTreatment.getSelectedItem();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblMode = new javax.swing.JLabel();
        cbMode = new javax.swing.JComboBox();
        btnNext = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lblTreatment = new javax.swing.JLabel();
        cbTreatment = new javax.swing.JComboBox();

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("<html><h1>Choose a mode :");

        lblMode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMode.setText("Summary");

        cbMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbModeItemStateChanged(evt);
            }
        });

        btnNext.setText("Next >>");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("<html><h1>Choose a treatment :");

        lblTreatment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTreatment.setText("Summary");

        cbTreatment.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbTreatment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTreatmentItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addComponent(lblMode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbMode, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTreatment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbTreatment, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMode, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTreatment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNext)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbModeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbModeItemStateChanged
        ModeType mode = (ModeType)cbMode.getSelectedItem();
        switch(mode){
            case Normal:
                lblMode.setText("<html><p align=\"center\">Normal : effects on each syllable.</p>");
                break;
            case Character:
                lblMode.setText("<html><p align=\"center\">Character : effects on each character of the syllable.</p>");
                break;           
        }
    }//GEN-LAST:event_cbModeItemStateChanged

    private void cbTreatmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTreatmentItemStateChanged
        TreatmentType tr = (TreatmentType)cbTreatment.getSelectedItem();
        switch(tr){
            case Line:
                lblTreatment.setText("<html><p align=\"center\">Line : effect on each line (equivalent to the <b>template line</b>).</p>");
                break;
            case Syllable:
                lblTreatment.setText("<html><p align=\"center\">Syllable : effect on each syllable (equivalent to the <b>template syl</b>).</p>");
                break;
            case Character:
                lblTreatment.setText("<html><p align=\"center\">Character :  effect on each character (equivalent to the <b>template syl char</b>).</p>");
                break;             
        }
    }//GEN-LAST:event_cbTreatmentItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JComboBox cbMode;
    private javax.swing.JComboBox cbTreatment;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblMode;
    private javax.swing.JLabel lblTreatment;
    // End of variables declaration//GEN-END:variables
}
