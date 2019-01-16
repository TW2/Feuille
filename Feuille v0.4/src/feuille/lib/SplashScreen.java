/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SplashScreen.java
 *
 * Created on 16 janv. 2011, 03:18:08
 */

package feuille.lib;

import javax.swing.ImageIcon;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * <p>This class is a splahscreen.<br />
 * Cette classe est une splahscreen.</p>
 * @author The Wingate 2940
 */
public class SplashScreen extends javax.swing.JFrame {
    
    ImageIcon splash;

    /** <p>Creates new form SplashScreen.<br />
     * Crée un nouveau formulaire SplashScreen.</p> */
    public SplashScreen() {
        initComponents();
        setLocationRelativeTo(null);
        prbLoading.setMinimum(0);
        prbLoading.setMaximum(100);
        long choice = Math.round(Math.random()*5);        
        if(choice==0){
            splash = new ImageIcon(getClass().getResource("AboutSBFF01.png"));
        }else if(choice==1){
            splash = new ImageIcon(getClass().getResource("FeuilleGen3.png"));
        }else if(choice==2){
            splash = new ImageIcon(getClass().getResource("AboutSBFF01.png"));
        }else if(choice==3){
            splash = new ImageIcon(getClass().getResource("FeuilleGen3.png"));
        }else if(choice==4){
            splash = new ImageIcon(getClass().getResource("AboutSBFF01.png"));
        }else if(choice==5){
            splash = new ImageIcon(getClass().getResource("FeuilleGen3.png"));
        }else{
            splash = new ImageIcon(getClass().getResource("AboutSBFF01.png"));
        }
        lblImage.setIcon(splash);
        
        // X. Essaie de changer le look & feel pour Nimbus
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblImage = new javax.swing.JLabel();
        prbLoading = new javax.swing.JProgressBar();
        lblLoading = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);

        lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/lib/FeuilleGen3.png"))); // NOI18N

        lblLoading.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoading.setText("Loading : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLoading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prbLoading, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblLoading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prbLoading, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** <p>Just to have a progress (from 0 to 100) and informations.<br />
     * Montre la progression (de 0 à 100) et les informations.</p> */
    public void loadingInfo(String subject, Integer progress){
        lblLoading.setText("Loading : "+subject+".");
        System.out.println("Loading : "+subject+".");
        prbLoading.setValue(progress);
    }

    /** <p>Just to have a progress (from 0 to 100) and informations.<br />
     * Montre la progression (de 0 à 100) et les informations.</p> */
    public void loadedInfo(String subject, Integer progress){
        lblLoading.setText(subject+" -> loaded.");
        System.out.println("          "+subject+" -> loaded.");
        prbLoading.setValue(progress);
    }

    /** <p>Just to have informations.<br />
     * Juste pour avoir des informations.</p> */
    public void nowPrint(String printout){
        System.out.println(printout);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SplashScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblLoading;
    private javax.swing.JProgressBar prbLoading;
    // End of variables declaration//GEN-END:variables

}
