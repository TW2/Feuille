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

import org.wingate.feuille.util.DialogResult;

/**
 *
 * @author util2
 */
public class CodeTypeDialog extends javax.swing.JDialog {

    private CodeType codeType = null;
    private DialogResult dialogResult = DialogResult.Unknown;
    
    /**
     * Creates new form CodeTypeDialog
     * @param parent
     * @param modal
     */
    public CodeTypeDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public void showDialog(){
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public DialogResult getDialogResult(){
        return dialogResult;
    }
    
    public CodeType getCodeType(){
        return codeType;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnJS = new javax.swing.JButton();
        btnPy = new javax.swing.JButton();
        btnRb = new javax.swing.JButton();
        btnLua = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridLayout(1, 4));

        btnJS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/js-icon 128.png"))); // NOI18N
        btnJS.setText("JavaScript");
        btnJS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnJS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnJS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJSActionPerformed(evt);
            }
        });
        jPanel1.add(btnJS);

        btnPy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/py-icon 128.png"))); // NOI18N
        btnPy.setText("Python");
        btnPy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPyActionPerformed(evt);
            }
        });
        jPanel1.add(btnPy);

        btnRb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/rb-icon 128.png"))); // NOI18N
        btnRb.setText("Ruby");
        btnRb.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRb.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRbActionPerformed(evt);
            }
        });
        jPanel1.add(btnRb);

        btnLua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lua-icon 128.png"))); // NOI18N
        btnLua.setText("Lua");
        btnLua.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLua.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuaActionPerformed(evt);
            }
        });
        jPanel1.add(btnLua);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnJSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJSActionPerformed
        codeType = CodeType.JavaScript;
        dialogResult = DialogResult.Ok;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnJSActionPerformed

    private void btnPyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPyActionPerformed
        codeType = CodeType.Python;
        dialogResult = DialogResult.Ok;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnPyActionPerformed

    private void btnRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRbActionPerformed
        codeType = CodeType.Ruby;
        dialogResult = DialogResult.Ok;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnRbActionPerformed

    private void btnLuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuaActionPerformed
        codeType = CodeType.Lua;
        dialogResult = DialogResult.Ok;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnLuaActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dialogResult = DialogResult.Cancel;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

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
            java.util.logging.Logger.getLogger(CodeTypeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CodeTypeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CodeTypeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CodeTypeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CodeTypeDialog dialog = new CodeTypeDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnJS;
    private javax.swing.JButton btnLua;
    private javax.swing.JButton btnPy;
    private javax.swing.JButton btnRb;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
