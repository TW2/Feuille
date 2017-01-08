/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.analysis.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;
import feuille.lib.Language;

/**
 *
 * @author The Wingate 2940
 */
public class AnalyzeDialog extends javax.swing.JDialog {

    private ButtonPressed bp = ButtonPressed.NONE;
    private DefaultComboBoxModel dcbmWords = new DefaultComboBoxModel();
    private Language localeLanguage = feuille.MainFrame.getLanguage();
    
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }
    
    /**
     * Creates new form AnalyzeDialog
     */
    public AnalyzeDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        cbWords.setModel(dcbmWords);
        
        if(localeLanguage.getValueOf("titleAnalysis")!=null){setTitle(localeLanguage.getValueOf("titleAnalysis"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("tbdAnaCompare")!=null){jPanel1.setBorder(new TitledBorder(localeLanguage.getValueOf("tbdAnaCompare")));}
        if(localeLanguage.getValueOf("tbdAnaReport")!=null){jPanel2.setBorder(new TitledBorder(localeLanguage.getValueOf("tbdAnaReport")));}
        if(localeLanguage.getValueOf("tbdAnaOthers")!=null){jPanel3.setBorder(new TitledBorder(localeLanguage.getValueOf("tbdAnaOthers")));}
        if(localeLanguage.getValueOf("rbuttonAnaCompSentence")!=null){rbCompareSentence.setText(localeLanguage.getValueOf("rbuttonAnaCompSentence"));}
        if(localeLanguage.getValueOf("rbuttonAnaCompTime")!=null){rbCompareTime.setText(localeLanguage.getValueOf("rbuttonAnaCompTime"));}
        if(localeLanguage.getValueOf("rbuttonAnaCompStyle")!=null){rbCompareStyle.setText(localeLanguage.getValueOf("rbuttonAnaCompStyle"));}
        if(localeLanguage.getValueOf("checkboxAnaBar")!=null){cbBarChart.setText(localeLanguage.getValueOf("checkboxAnaBar"));}
        if(localeLanguage.getValueOf("checkboxAnaPie")!=null){cbPieChart.setText(localeLanguage.getValueOf("checkboxAnaPie"));}
        if(localeLanguage.getValueOf("checkboxAnaWords")!=null){cboxWords.setText(localeLanguage.getValueOf("checkboxAnaWords"));}
        if(localeLanguage.getValueOf("rbuttonAnaNoComp")!=null){jRadioButton1.setText(localeLanguage.getValueOf("rbuttonAnaNoComp"));}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgComparison = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        rbCompareSentence = new javax.swing.JRadioButton();
        rbCompareTime = new javax.swing.JRadioButton();
        rbCompareStyle = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        cbBarChart = new javax.swing.JCheckBox();
        cbPieChart = new javax.swing.JCheckBox();
        OK_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cboxWords = new javax.swing.JCheckBox();
        tfWords = new javax.swing.JTextField();
        bAddWords = new javax.swing.JButton();
        bDelWords = new javax.swing.JButton();
        cbWords = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Compare tables"));

        bgComparison.add(rbCompareSentence);
        rbCompareSentence.setText("Comparison by sentence");

        bgComparison.add(rbCompareTime);
        rbCompareTime.setText("Comparison by time");

        bgComparison.add(rbCompareStyle);
        rbCompareStyle.setText("Comparison by style");
        rbCompareStyle.setEnabled(false);

        bgComparison.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("No comparison");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(rbCompareSentence)
                    .addComponent(rbCompareTime)
                    .addComponent(rbCompareStyle)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbCompareSentence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbCompareTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbCompareStyle))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Make report"));

        cbBarChart.setText("Bar chart");

        cbPieChart.setText("Pie chart");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbBarChart)
                    .addComponent(cbPieChart))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbBarChart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbPieChart)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Others"));

        cboxWords.setText("Words");

        bAddWords.setText("+");
        bAddWords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddWordsActionPerformed(evt);
            }
        });

        bDelWords.setText("-");
        bDelWords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDelWordsActionPerformed(evt);
            }
        });

        cbWords.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboxWords, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfWords, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bAddWords, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bDelWords, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbWords, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboxWords)
                    .addComponent(tfWords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bAddWords)
                    .addComponent(bDelWords)
                    .addComponent(cbWords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(136, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(295, Short.MAX_VALUE)
                .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OK_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OK_Button)
                    .addComponent(Cancel_Button))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        // OK
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        // Annuler
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void bAddWordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddWordsActionPerformed
        // Ajoute un mot
        if(tfWords.getText().isEmpty()==false){
            dcbmWords.addElement(tfWords.getText());
        }
    }//GEN-LAST:event_bAddWordsActionPerformed

    private void bDelWordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDelWordsActionPerformed
        // Supprime un mot
        if(cbWords.getSelectedIndex()!=-1){
            dcbmWords.removeElement(cbWords.getSelectedItem());
        }
    }//GEN-LAST:event_bDelWordsActionPerformed

    /** <p>Show the dialog.<br />
     * Montre la dialogue.</p> */
    public boolean showDialog(){
        setVisible(true);
        
        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean compareBySentence(){
        return rbCompareSentence.isSelected();
    }
    
    public boolean compareByTime(){
        return rbCompareTime.isSelected();
    }
    
    public boolean compareByStyle(){
        return rbCompareStyle.isSelected();
    }
    
    public boolean makeBarChart(){
        return cbBarChart.isSelected();
    }
    
    public boolean makePieChart(){
        return cbPieChart.isSelected();
    }
    
    public boolean makeWords(){
        return cboxWords.isSelected();
    }
    
    public List<String> getWords(){
        List<String> list = new ArrayList<String>();
        for(int i=0;i<cbWords.getItemCount();i++){
            String word = cbWords.getItemAt(i).toString();
            list.add(word);
        }
        return list;
    }
    
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
            java.util.logging.Logger.getLogger(AnalyzeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnalyzeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnalyzeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnalyzeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AnalyzeDialog dialog = new AnalyzeDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel_Button;
    private javax.swing.JButton OK_Button;
    private javax.swing.JButton bAddWords;
    private javax.swing.JButton bDelWords;
    private javax.swing.ButtonGroup bgComparison;
    private javax.swing.JCheckBox cbBarChart;
    private javax.swing.JCheckBox cbPieChart;
    private javax.swing.JComboBox cbWords;
    private javax.swing.JCheckBox cboxWords;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton rbCompareSentence;
    private javax.swing.JRadioButton rbCompareStyle;
    private javax.swing.JRadioButton rbCompareTime;
    private javax.swing.JTextField tfWords;
    // End of variables declaration//GEN-END:variables
}
