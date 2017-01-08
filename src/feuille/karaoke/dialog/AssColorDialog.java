/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AssColorDialog.java
 *
 * Created on 7 déc. 2009, 01:55:23
 */

package feuille.karaoke.dialog;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import feuille.lib.Language;

/**
 * <p>This is a dialog for the choice of color.<br />
 * C'est une boîte de dialogue pour lme choix de la couleur.</p>
 * @author Unknown User
 */
public class AssColorDialog extends javax.swing.JDialog {
    
    private ButtonPressed bp;
    private String assColor = "FFFFFF"; //white
    private Language localeLanguage = feuille.MainFrame.getLanguage();

    /** <p>A choice of state.<br />Un choix d'état.</p> */
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }

    //With this variable we'll can open others dialogs into this one.
    //First, we have to set it in the main public method. (frame==parent)
    private Frame frame;

    /** <p>Creates new form AssColorDialog.<br />
     * Crée un nouveau formulaire AssColorDialog.</p> */
    public AssColorDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(460,480);
        bp = ButtonPressed.NONE;

        //With the following listener, we'll get color.
        //Thanks to : http://java.sun.com/developer/JDCTechTips/2003/tt1021.html
        jColorChooser1.getSelectionModel().addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                assColor = colorToAssColor();
                tfAssColor.setText(assColor);
                tfHtmlColor.setText(colorToHtmlColor());
            }
        });

        //Setting up the frame variable to parent, referencing top level parent
        // as real parent for all. Useful to open new JDialog into this one.
        frame = parent;
        
        //Disable functions that don't work on Linux and Mac OS X
        if(System.getProperty("os.name").equalsIgnoreCase("Linux")
                | System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
            ColorScreen_Button.setEnabled(false);
            ColorAllScreen_Button.setEnabled(false);
        }
        
        //Force visibility
        setAlwaysOnTop(true);
        
        if(localeLanguage.getValueOf("titleACD")!=null){setTitle(localeLanguage.getValueOf("titleACD"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("buttonInMain")!=null){ColorScreen_Button.setText(localeLanguage.getValueOf("buttonInMain"));}
        if(localeLanguage.getValueOf("buttonOfScreen")!=null){ColorAllScreen_Button.setText(localeLanguage.getValueOf("buttonOfScreen"));}
        if(localeLanguage.getValueOf("labelRGBHTML")!=null){lblHtmlColor.setText(localeLanguage.getValueOf("labelRGBHTML"));}
        if(localeLanguage.getValueOf("labelBGR")!=null){lblAssColor.setText(localeLanguage.getValueOf("labelBGR"));}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jColorChooser1 = new javax.swing.JColorChooser();
        lblHtmlColor = new javax.swing.JLabel();
        tfHtmlColor = new javax.swing.JTextField();
        lblAssColor = new javax.swing.JLabel();
        tfAssColor = new javax.swing.JTextField();
        OK_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();
        ColorScreen_Button = new javax.swing.JButton();
        ColorAllScreen_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Choose your color...");
        getContentPane().setLayout(null);
        getContentPane().add(jColorChooser1);
        jColorChooser1.setBounds(0, 0, 440, 371);

        lblHtmlColor.setText("RGB or HTML color :");
        getContentPane().add(lblHtmlColor);
        lblHtmlColor.setBounds(10, 380, 120, 30);

        tfHtmlColor.setText("#FFFFFF");
        tfHtmlColor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfHtmlColorKeyReleased(evt);
            }
        });
        getContentPane().add(tfHtmlColor);
        tfHtmlColor.setBounds(130, 380, 90, 30);

        lblAssColor.setText("BGR color : ");
        getContentPane().add(lblAssColor);
        lblAssColor.setBounds(230, 380, 110, 30);

        tfAssColor.setText("FFFFFF");
        tfAssColor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfAssColorKeyReleased(evt);
            }
        });
        getContentPane().add(tfAssColor);
        tfAssColor.setBounds(342, 380, 90, 30);

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(OK_Button);
        OK_Button.setBounds(250, 420, 90, 23);

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(Cancel_Button);
        Cancel_Button.setBounds(340, 420, 91, 23);

        ColorScreen_Button.setText("In main");
        ColorScreen_Button.setToolTipText("<html>Pick up a color on the screen when the main window is not hidden.<br />\nYou just have to click somewhere in the glass window and the color<br />\nof the element in the background (the main window) will be found.");
        ColorScreen_Button.setEnabled(false);
        ColorScreen_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ColorScreen_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(ColorScreen_Button);
        ColorScreen_Button.setBounds(10, 420, 110, 23);

        ColorAllScreen_Button.setText("Of screen");
        ColorAllScreen_Button.setToolTipText("<html>Pick up a color on the screen when the main window is hidden.<br />\nYou just have to click somewhere in the glass window and the color<br />\nof the element in the background (your desktop) will be found.");
        ColorAllScreen_Button.setEnabled(false);
        ColorAllScreen_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ColorAllScreen_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(ColorAllScreen_Button);
        ColorAllScreen_Button.setBounds(130, 420, 110, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void tfHtmlColorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfHtmlColorKeyReleased
        applyHTMLColor(tfHtmlColor.getText());
        tfAssColor.setText(HTMLToAssColor(tfHtmlColor.getText()));
    }//GEN-LAST:event_tfHtmlColorKeyReleased

    private void tfAssColorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfAssColorKeyReleased
        applyAssColor(tfAssColor.getText());
        tfHtmlColor.setText(AssToHTMLColor(tfAssColor.getText()));
    }//GEN-LAST:event_tfAssColorKeyReleased

    private void ColorScreen_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ColorScreen_ButtonActionPerformed
        setAlwaysOnTop(false);
        NullColorDialog ncd = new NullColorDialog(frame, true);
        ncd.setAlwaysOnTop(true);
        ncd.setOwnerTransparency(false);
        Color colorPi = ncd.showDialog(jColorChooser1.getColor());        
        //System.out.println("Color: "+colorPi);
        jColorChooser1.setColor(colorPi);
        tfHtmlColor.setText(colorToHtmlColor());
        tfAssColor.setText(colorToAssColor());
        setAlwaysOnTop(true);
    }//GEN-LAST:event_ColorScreen_ButtonActionPerformed

    private void ColorAllScreen_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ColorAllScreen_ButtonActionPerformed
        setAlwaysOnTop(false);
        NullColorDialog ncd = new NullColorDialog(frame, true);
        ncd.setAlwaysOnTop(true);
        ncd.setOwnerTransparency(true);
        Color colorPi = ncd.showDialog(jColorChooser1.getColor());        
        //System.out.println("Color: "+colorPi);
        jColorChooser1.setColor(colorPi);
        tfHtmlColor.setText(colorToHtmlColor());
        tfAssColor.setText(colorToAssColor());
        setAlwaysOnTop(true);
    }//GEN-LAST:event_ColorAllScreen_ButtonActionPerformed

    /** <p>Set the color.<br />Définit la couleur.</p> */
    public void setAssColor(String assColor){
        this.assColor = assColor;        
    }

    /** <p>Get the color.<br />Obtient la couleur.</p> */
    public String getAssColor(){
        return assColor;
    }
    
    /** <p>Show the dialog. Request and return a color.<br />
     * Montre la boîte de dialogue. Requiert et retourne une couleur.</p> */
    public String showDialog(String assColor){
        if(assColor!=null && assColor.isEmpty()==false){
            applyAssColor(assColor);
            setAssColor(assColor);  
        }
        setVisible(true);

        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return getAssColor();
        }else{
            return assColor;
        }
    }
    
    /** <p>Show a color in ASS mode into colorchooser.<br />
     * Montre une couleur dans le mode ASS à l'intérieur du colorchooser.</p> */
    private void applyAssColor(String assColor){
        // ASS -> BBGGRR
        String blue = assColor.substring(0, 2);
        String green = assColor.substring(2, 4);
        String red = assColor.substring(4);
        jColorChooser1.setColor(
                Integer.parseInt(red, 16),
                Integer.parseInt(green, 16),
                Integer.parseInt(blue, 16));
    }

    /** <p>Show a color in HTML or normal mode into colorchooser.<br />
     * Montre une couleur dans le mode HTML (ou normal) à l'intérieur du colorchooser.</p> */
    private void applyHTMLColor(String HTMLColor){
        // HTML -> RRGGBB
        if(HTMLColor.startsWith("#")){HTMLColor=HTMLColor.substring(1);}
        String red = HTMLColor.substring(0, 2);
        String green = HTMLColor.substring(2, 4);
        String blue = HTMLColor.substring(4);
        jColorChooser1.setColor(
                Integer.parseInt(red, 16),
                Integer.parseInt(green, 16),
                Integer.parseInt(blue, 16));
    }

    /** <p>Converts a color from RGB format to ASS format.<br />
     * Convertit une couleur du format RGB au format ASS.</p> */
    private String colorToAssColor(){
        Color c = jColorChooser1.getColor();
        String red = Integer.toHexString(c.getRed()).toUpperCase();
        String green = Integer.toHexString(c.getGreen()).toUpperCase();
        String blue = Integer.toHexString(c.getBlue()).toUpperCase();
        if(red.length()<2){red="0"+red;}
        if(green.length()<2){green="0"+green;}
        if(blue.length()<2){blue="0"+blue;}
        return blue+green+red;
    }

    /** <p>Converts a color from RGB format to HTML format.<br />
     * Convertit une couleur du format RGB au format HTML.</p> */
    private String colorToHtmlColor(){
        Color c = jColorChooser1.getColor();
        String red = Integer.toHexString(c.getRed()).toUpperCase();
        String green = Integer.toHexString(c.getGreen()).toUpperCase();
        String blue = Integer.toHexString(c.getBlue()).toUpperCase();
        if(red.length()<2){red="0"+red;}
        if(green.length()<2){green="0"+green;}
        if(blue.length()<2){blue="0"+blue;}
        return "#"+red+green+blue;
    }
    
    /** <p>Converts a color from ASS format to HTML format.<br />
     * Convertit une couleur du format ASS au format HTML.</p> */
    private String AssToHTMLColor(String assColor){
        String blue = assColor.substring(0, 2);
        String green = assColor.substring(2, 4);
        String red = assColor.substring(4);
        return "#"+red+green+blue;
    }
    
    /** <p>Converts a color from HTML format to ASS format.<br />
     * Convertit une couleur du format HTML au format ASS.</p> */
    private String HTMLToAssColor(String HTMLColor){
        if(HTMLColor.startsWith("#")){HTMLColor=HTMLColor.substring(1);}
        String red = HTMLColor.substring(0, 2);
        String green = HTMLColor.substring(2, 4);
        String blue = HTMLColor.substring(4);
        return blue+green+red;
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AssColorDialog dialog = new AssColorDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton ColorAllScreen_Button;
    private javax.swing.JButton ColorScreen_Button;
    private javax.swing.JButton OK_Button;
    private javax.swing.JColorChooser jColorChooser1;
    private javax.swing.JLabel lblAssColor;
    private javax.swing.JLabel lblHtmlColor;
    private javax.swing.JTextField tfAssColor;
    private javax.swing.JTextField tfHtmlColor;
    // End of variables declaration//GEN-END:variables

}
