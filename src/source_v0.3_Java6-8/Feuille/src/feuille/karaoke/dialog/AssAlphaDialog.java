/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AssAlphaDialog.java
 *
 * Created on 8 août 2010, 00:17:52
 */

package feuille.karaoke.dialog;

import java.awt.BorderLayout;
import feuille.lib.Language;
import feuille.karaoke.lib.TransparencyPreview;

/**
 * <p>This is a dialog for the choice of alpha.<br />
 * C'est une boîte de dialogue pour le choix de l'alpha.</p>
 * @author The Wingate 2940
 */
public class AssAlphaDialog extends javax.swing.JDialog {

    private ButtonPressed bp;
    private int alpha;
    private String hexa;
    private Language localeLanguage = feuille.MainFrame.getLanguage();
    private String Transparency = "Transparency : ";
    private TransparencyPreview tp = new TransparencyPreview();

    /** <p>A choice of state.<br />Un choix d'état.</p> */
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }

    /** <p>Creates new form AssAlphaDialog.<br />
     * Crée un nouveau formulaire AssAlphaDialog.</p> */
    public AssAlphaDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        pAlpha.add(tp, BorderLayout.CENTER);
        
        //Force visibility
        setAlwaysOnTop(true);
        
        //Set the language
        if(localeLanguage.getValueOf("titleAAD")!=null){setTitle(localeLanguage.getValueOf("titleAAD"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnChange.setText(localeLanguage.getValueOf("buttonChange"));}
        if(localeLanguage.getValueOf("labelTransparency")!=null){Transparency=localeLanguage.getValueOf("labelTransparency");}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sldAlpha = new javax.swing.JSlider();
        pAlpha = new javax.swing.JPanel();
        lblAlpha = new javax.swing.JLabel();
        OK_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();
        tfChange = new javax.swing.JTextField();
        btnChange = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Choose an alpha...");

        sldAlpha.setMaximum(255);
        sldAlpha.setValue(127);
        sldAlpha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldAlphaStateChanged(evt);
            }
        });

        pAlpha.setLayout(new java.awt.BorderLayout());

        lblAlpha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlpha.setText("Transparency : 127 - 7F / 50%");

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

        tfChange.setText("h7F");
        tfChange.setToolTipText("<html>Transparency may be changed by using hexadecimals values, decimals values or percents values.<br>\nHexadecimals values samples : h33 or h7F.<br>\nDecimals values samples : 67 or 255.<br>\nPercents values samples : 33% or 100%.<br>\nPush the 'Change' button to apply the requested value.");

        btnChange.setText("Change");
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblAlpha, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnChange, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfChange, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(OK_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(sldAlpha, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sldAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Cancel_Button)
                            .addComponent(OK_Button)
                            .addComponent(tfChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnChange)))
                    .addComponent(pAlpha, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sldAlphaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldAlphaStateChanged
        int percent = sldAlpha.getValue()*100/255;
        hexa = Integer.toString(sldAlpha.getValue(), 16);
        lblAlpha.setText(Transparency+sldAlpha.getValue()+
                " - "+setTwoDigits(hexa).toUpperCase()+" / "+percent+"%");
        alpha = sldAlpha.getValue();
        tp.updateAlpha((100-percent)/100f);
        tfChange.setText("h"+setTwoDigits(hexa).toUpperCase());
    }//GEN-LAST:event_sldAlphaStateChanged

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        /* Change an entry of the user into a value
         * Entry starts with the letter 'h' -> hexadecimal mode.
         * Entry has only number -> decimal mode.
         * Entry has a percent -> percent mode. */
        String text = tfChange.getText();
        int result;
        try{
            if(text.startsWith("h") && text.length()==3){
                // Hexadecimal mode
                text = text.replace("h", "");
                result = Integer.parseInt(text, 16);
                sldAlpha.setValue(result);
            }else if(text.matches("[0-9]+")){
                // Decimal mode
                result = Integer.parseInt(text);
                sldAlpha.setValue(result);
            }else if(text.contains("%")){
                // Percent mode
                text = text.replace("%", "");
                result = Integer.parseInt(text);
                sldAlpha.setValue(result*255/100);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_btnChangeActionPerformed

    /** <p>Return a string with two digits.<br />
     * Retourne une chaine avic deux digits.</p> */
    private String setTwoDigits(String hexa){
        if(hexa.length()==1){hexa="0"+hexa;}
        return hexa;
    }

    /** <p>Set the alpha.<br />Définit l'alpha.</p> */
    public void setAssAlpha(int alpha){
        this.alpha = alpha;
    }

    /** <p>Get the alpha.<br />Obtient l'alpha.</p> */
    public int getAssAlpha(){
        return alpha;
    }
    
    /** <p>Set the alpha in hexadecimal.<br />
     * Définit l'alpha en hexadécimale.</p> */
    public void setHexaAlpha(String hexa){
        this.hexa = hexa;
        if (hexa==null){this.hexa = "FF";}
        try{
            if (hexa.isEmpty()){this.hexa = "FF";}
        }catch(Exception exc){}
        try{
            alpha = Integer.parseInt(hexa, 16);
        }catch(Exception exc){}
    }

    /** <p>Get the alpha in hexadecimal.<br />
     * Obtient l'alpha en héxadécimale.</p> */
    public String getHexaAlpha(){
        hexa = Integer.toString(sldAlpha.getValue(), 16);
        if(hexa.length()==1){hexa = "0"+hexa;}
        return hexa.toUpperCase();
    }

    /** <p>Show the dialog. Request and return an alpha in hexadecimal.<br />
     * Montre la boîte de dialogue. Requiert et retourne un alpha en héxadécimale.</p> */
    public String showDialog(String hexa){
        setHexaAlpha(hexa);
        sldAlpha.setValue(alpha);
        setVisible(true);

        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return getHexaAlpha();
        }else{
            return hexa;
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AssAlphaDialog dialog = new AssAlphaDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnChange;
    private javax.swing.JLabel lblAlpha;
    private javax.swing.JPanel pAlpha;
    private javax.swing.JSlider sldAlpha;
    private javax.swing.JTextField tfChange;
    // End of variables declaration//GEN-END:variables

}