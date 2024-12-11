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
package org.wingate.feuille.dialog;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.wingate.feuille.subs.ass.AssActor;
import org.wingate.feuille.util.DialogResult;

/**
 *
 * @author util2
 */
public class ActorsDialog extends javax.swing.JDialog {
    
    private List<AssActor> actors = new ArrayList<>();
    
    private DialogResult dialogResult = DialogResult.None;
    private final java.awt.Frame parent;
    
    private final DefaultComboBoxModel modelActor = new DefaultComboBoxModel();
    private final DefaultComboBoxModel modelKind = new DefaultComboBoxModel();
    
    private AssActor curActor = null;

    /**
     * Creates new form ActorsDialog
     * @param parent
     * @param modal
     */
    public ActorsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent = parent;
        
        cbName.setModel(modelActor);
        cbKind.setModel(modelKind);
        
        for(AssActor.Kind k : AssActor.Kind.values()){
            modelKind.addElement(k);
        }
        
        fcImage.setAcceptAllFileFilterUsed(false);
        fcImage.addChoosableFileFilter(new FileFilter(){
            final List<String> extension = Arrays.asList(new String[]{
                "jpg", "png", "bmp"
            });
            
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) return true;
                return extension.contains(
                        f.getName().substring(f.getName().lastIndexOf(".")+1));
            }

            @Override
            public String getDescription() {
                return "Images";
            }
        });
    }
    
    public void showDialog(List<AssActor> actors){
        if(actors.isEmpty()){
            this.actors.add(new AssActor("Default"));
        }else{
            this.actors = actors;
        }                
        for(AssActor actor : actors){
            modelActor.addElement(actor.getName());
        }
        
        curActor = actors.get(0);
        
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }
    
    public List<AssActor> getActors() throws IOException{
        return actors;
    }

    private void showImageWithRatio(ImageIcon icon){
        if(icon == null){
            lblImageB64.setIcon(null);
            return;
        }
        float wi = icon.getIconWidth();
        float hi = icon.getIconHeight();
        float wl = lblImageB64.getWidth();
        float hl = lblImageB64.getHeight();
        
        float ratioX = wl / wi;
        float ratioY = hl / hi;
        
        float r = ratioX >= ratioY ? ratioY : ratioX;
        
        int w = Math.round(wi * r);
        int h = Math.round(hi * r);
        
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(icon.getImage(), 0, 0, w, h, null);
        g2d.dispose();
        lblImageB64.setIcon(new ImageIcon(img));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcImage = new javax.swing.JFileChooser();
        lblName = new javax.swing.JLabel();
        lblColor = new javax.swing.JLabel();
        lblColorView = new javax.swing.JLabel();
        btnColor = new javax.swing.JButton();
        lblKind = new javax.swing.JLabel();
        cbKind = new javax.swing.JComboBox<>();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblImageB64 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        btnImage = new javax.swing.JButton();
        btnRemoveActor = new javax.swing.JButton();
        btnAddActor = new javax.swing.JButton();
        cbName = new javax.swing.JComboBox<>();
        btnRemoveImage = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Actors (EDITOR)");

        lblName.setText("Name : ");

        lblColor.setText("Color : ");

        lblColorView.setBackground(new java.awt.Color(51, 51, 255));
        lblColorView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblColorView.setOpaque(true);

        btnColor.setText("...");
        btnColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColorActionPerformed(evt);
            }
        });

        lblKind.setText("Kind : ");

        cbKind.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbKind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKindActionPerformed(evt);
            }
        });

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblImageB64.setBackground(new java.awt.Color(255, 255, 255));
        lblImageB64.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblImageB64.setOpaque(true);

        lblImage.setText("Image : ");

        btnImage.setText("...");
        btnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageActionPerformed(evt);
            }
        });

        btnRemoveActor.setText("-");
        btnRemoveActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActorActionPerformed(evt);
            }
        });

        btnAddActor.setText("+");
        btnAddActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActorActionPerformed(evt);
            }
        });

        cbName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNameActionPerformed(evt);
            }
        });

        btnRemoveImage.setText("x");
        btnRemoveImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName)
                            .addComponent(lblColor)
                            .addComponent(lblKind)
                            .addComponent(lblImage))
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(cbName, 0, 284, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddActor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemoveActor))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblImageB64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnImage, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnRemoveImage, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(cbKind, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblColorView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnColor)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(btnRemoveActor)
                    .addComponent(btnAddActor)
                    .addComponent(cbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblColorView, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblColor)
                        .addComponent(btnColor)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKind)
                    .addComponent(cbKind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblImage)
                            .addComponent(btnImage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveImage))
                    .addComponent(lblImageB64, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColorActionPerformed
        // Choose color
        Color c = JColorChooser.showDialog(parent, "Choose a color", lblColorView.getBackground());
        if(c == null) return;
        lblColorView.setBackground(c);
        if(curActor != null){
            curActor.setColor(c);
        }        
    }//GEN-LAST:event_btnColorActionPerformed

    private void btnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageActionPerformed
        // Choose image
        int z = fcImage.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION && curActor != null){
            File f = fcImage.getSelectedFile();           
            try {
                curActor.setImage(curActor.getBufferedImage(new ImageIcon(f.getPath())));
                
                showImageWithRatio(curActor.getIcon());
            } catch (IOException ex) {
                Logger.getLogger(ActorsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnImageActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // OK
        dialogResult = DialogResult.OK;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Cancel
        dialogResult = DialogResult.Cancel;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRemoveActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActorActionPerformed
        // Remove actor
        if(cbName.getSelectedIndex() != -1){
            if(modelActor.getSelectedItem() instanceof String t){
                if(t.equalsIgnoreCase("unknown")){                
                    JOptionPane.showMessageDialog(
                            parent,
                            "The 'Unknown' actor can not be deleted!"
                    );
                    return;                
                }
            }
            int z = JOptionPane.showConfirmDialog(
                    parent,
                    "Do you really want to remove this actor ?",
                    "Delete an actor ?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if(z == JOptionPane.YES_OPTION){
                AssActor actor = null;
                for(AssActor a : actors){                    
                    if(a.getName().equals(modelActor.getElementAt(cbName.getSelectedIndex()))){
                        actor = a;
                        break;
                    }
                }
                if(actor == null) return;
                actors.remove(actor);
                modelActor.removeElementAt(cbName.getSelectedIndex());
                if(modelActor.getSize() == 0){
                    lblColorView.setBackground(Color.blue);
                    modelKind.setSelectedItem(AssActor.Kind.Unknown);
                    lblImageB64.setIcon(null);
                }else{
                    cbName.setSelectedIndex(0);
                }                
            }            
        }        
    }//GEN-LAST:event_btnRemoveActorActionPerformed

    private void btnAddActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActorActionPerformed
        // Add actor
        String name = JOptionPane.showInputDialog("Type an actor name :");
        if(name != null && name.isEmpty() == false){
            for(AssActor a : actors){
                if(a.getName().equals(name)){
                    return;
                }
            }
            // Add a name with fake values
            curActor = new AssActor(name, Color.blue, null, AssActor.Kind.Unknown);
            actors.add(curActor);
            modelActor.addElement(name);
            cbName.setSelectedItem(name);
        }
    }//GEN-LAST:event_btnAddActorActionPerformed

    private void cbNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNameActionPerformed
        // TODO add your handling code here:
        if(cbName.getSelectedIndex() != -1){
            for(AssActor a : actors){
                if(a.getName().equals(modelActor.getSelectedItem())){
                    curActor = a;
                    break;
                }
            }
            if(curActor == null) return;
            lblColorView.setBackground(curActor.getColor());
            modelKind.setSelectedItem(curActor.getKind());
            try {
                showImageWithRatio(curActor.getIcon());
            } catch (IOException ex) {
                lblImageB64.setIcon(null);
            }
        }        
    }//GEN-LAST:event_cbNameActionPerformed

    private void cbKindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKindActionPerformed
        // TODO add your handling code here:
        if(curActor != null){
            curActor.setKind((AssActor.Kind)modelKind.getSelectedItem());
        }        
    }//GEN-LAST:event_cbKindActionPerformed

    private void btnRemoveImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveImageActionPerformed
        // Remove image (if any)
        if(curActor != null){
            lblImageB64.setIcon(null);
            curActor.setB64Image(null);
        }
    }//GEN-LAST:event_btnRemoveImageActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ActorsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            ActorsDialog dialog = new ActorsDialog(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddActor;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnColor;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnRemoveActor;
    private javax.swing.JButton btnRemoveImage;
    private javax.swing.JComboBox<String> cbKind;
    private javax.swing.JComboBox<String> cbName;
    private javax.swing.JFileChooser fcImage;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblColorView;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblImageB64;
    private javax.swing.JLabel lblKind;
    private javax.swing.JLabel lblName;
    // End of variables declaration//GEN-END:variables
}