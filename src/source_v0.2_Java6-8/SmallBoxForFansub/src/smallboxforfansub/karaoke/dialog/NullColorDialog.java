/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NullColorDialog.java
 *
 * Created on 7 août 2010, 12:50:00
 */

package smallboxforfansub.karaoke.dialog;
import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;

/**
 * <p>This is a dialog to get color of your screen.<br />
 * C'est une boîte de dialogue qui sert à récupérer la couleur de votre écran.</p>
 * @author The Wingate 2940
 */
public class NullColorDialog extends javax.swing.JDialog {

    private PointerInfo mouseInf;
    private Color cPointer;
    private boolean oTrans = false;
    private MousePressed mp;

    public enum MousePressed{
        NO, YES;
    }

    /** <p>Creates new form NullColorDialog.<br />
     * Crée un nouveau formulaire NullColorDialog.</p> */
    public NullColorDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setInvisible(true, oTrans, 0.5f);

        mp = MousePressed.NO;
        
        // Frame size - Taille de la fenêtre
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension dim = toolkit.getScreenSize();
        setSize(dim.width,dim.height);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        mouseInf = MouseInfo.getPointerInfo();
        setInvisible(true, oTrans, 0.0f);
        int cX = (int)mouseInf.getLocation().getX();
        int cY = (int)mouseInf.getLocation().getY();
        Robot robot;
        try {
            robot = new Robot();
            cPointer = robot.getPixelColor(cX,cY);
        } catch (AWTException ex) {
        }
        setInvisible(false, oTrans, 1.0f);
        mp = MousePressed.YES;
        dispose();
    }//GEN-LAST:event_formMouseClicked

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        //Forces the window to use the asked oTrans value.
        setInvisible(true, oTrans, 0.5f);
    }//GEN-LAST:event_formWindowGainedFocus

    /** <p>Gets the color (from mouse informations).<br />
     * Obtient la couleur (à partir des informations de la souris).</p> */
    public Color getColor(){
        return cPointer;
    }

    /** <p>Sets the color.<br />Définit la couleur.</p> */
    public void setColor(Color color){
        cPointer = color;
    }

    /** <p>Shows the dialog and gets a Color at closing.<br />
     * Montre la dialogue et obtient une couleur à la fermeture.</p> */
    public Color showDialog(Color cPointer){
        if(cPointer!=null){
            setColor(cPointer);
        }
        setVisible(true);

        if(mp.equals(MousePressed.YES)){
            return getColor();
        }else{
            return cPointer;
        }
    }

    /** <p>Gets PointerInfo which contains mouse informations.<br />
     * Obtient des informations du pointeur de la souris.</p> */
    public PointerInfo getPointerInfo(){
        return mouseInf;
    }

    /** <p>Sets PointerInfo which contains mouse informations.<br />
     * Définit les informations du pointeur de la souris.</p> */
    public void setPointerInfo(PointerInfo mouseInf){
        this.mouseInf = mouseInf;
    }

    /** <p>Sets invisibility for dialogs. If owner is <code>true</code>, the main
     * window is also set.<br />Définit l'invisilité pour les dialogues. Si
     * owner est vrai alors la fenêtre principal est aussi mise en transparence.</p> */
    private void setInvisible(boolean inv, boolean owner, float trans){
        if(inv==true){// Frames are invisible.
            for (java.awt.Window wdw : getWindows()){
//                if(wdw instanceof XmlPresetDialog |
//                        wdw instanceof AssColorDialog){
//                    com.sun.awt.AWTUtilities.setWindowOpacity(wdw, 0.0f);
//                }
//                if(wdw instanceof NullColorDialog){
//                    com.sun.awt.AWTUtilities.setWindowOpacity(wdw, trans);
//                }
//                if(wdw instanceof smallboxforfansub.MainFrame && owner==true){
//                    com.sun.awt.AWTUtilities.setWindowOpacity(wdw, 0.0f);
//                }
//                //System.out.println("Window = "+wdw.toString());
            }
        }else{//Frames are visible.
            for (java.awt.Window wdw : getWindows()){
//                if(wdw instanceof XmlPresetDialog |
//                        wdw instanceof AssColorDialog |
//                        wdw instanceof NullColorDialog){
//                    com.sun.awt.AWTUtilities.setWindowOpacity(wdw, 1.0f);
//                }
//                if(wdw instanceof smallboxforfansub.MainFrame && owner==true){
//                    com.sun.awt.AWTUtilities.setWindowOpacity(wdw, 1.0f);
//                }
//                //System.out.println("Window = "+wdw.toString());
            }
        }
    }

    /** <p>Sets the Owner Transparency.<br />
     * Définit la transparence de Owner.</p> */
    public void setOwnerTransparency(boolean oTrans){
        this.oTrans = oTrans;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                NullColorDialog dialog = new NullColorDialog(new javax.swing.JFrame(), true);
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
    // End of variables declaration//GEN-END:variables

}