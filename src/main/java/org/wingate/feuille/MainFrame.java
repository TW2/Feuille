package org.wingate.feuille;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import org.wingate.feuille.m.ContainerInternalFrame;
import org.wingate.feuille.m.ContainersDesktopPane;
import org.wingate.feuille.m.ElementDialog;
import org.wingate.feuille.theme.Theme;
import org.wingate.feuille.util.DialogResult;

/**
 *
 * @author util2
 */
public class MainFrame extends javax.swing.JFrame {

    private Theme chosenTheme = new Theme();
    private final ContainersDesktopPane desktopPane;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        
        desktopPane = new ContainersDesktopPane();
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(desktopPane, BorderLayout.CENTER);
        
        ImageIcon iiWallpaper = new ImageIcon(getClass().getResource(
                "/images/wallpapers/beach-1920x1080-sea-ocean-pink-sunset-25978.jpeg"
        ));
        
        desktopPane.setWallpaper(iiWallpaper);
        
        for(int i=0; i<35; i++){
            JCheckBoxMenuItem m = configureMatrix(MatrixOrientation.X, i+1);
            mMatrixX.add(m);
            bgMatrixX.add(m);
            m.setSelected(i == 0);
        }
        
        for(int i=0; i<35; i++){
            JCheckBoxMenuItem m = configureMatrix(MatrixOrientation.Y, i+1);
            mMatrixY.add(m);
            bgMatrixY.add(m);
            m.setSelected(i == 0);
        }
    }
    
    public void setTheme(Theme theme){
        chosenTheme = theme;
    }
    
    public Theme getTheme() {
        return chosenTheme;
    }
    
    public enum MatrixOrientation { X, Y; }
    private JCheckBoxMenuItem configureMatrix(MatrixOrientation orientation, int unit){
        JCheckBoxMenuItem m = null;
        
        switch(orientation){
            case X -> {
                m = new JCheckBoxMenuItem("Set x value of matrix to " + unit);
                m.addActionListener((e) -> {
                    desktopPane.getMatrix().setWCases(unit);
                    for(ContainerInternalFrame cp : desktopPane.getContainers()){
                        desktopPane.changeLocation(cp.getElementAbstract());
                        cp.updateUI();
                    }
                });
            }
            case Y -> {
                m = new JCheckBoxMenuItem("Set y value of matrix to " + unit);
                m.addActionListener((e) -> {
                    desktopPane.getMatrix().setHCases(unit);
                    for(ContainerInternalFrame cp : desktopPane.getContainers()){
                        desktopPane.changeLocation(cp.getElementAbstract());
                        cp.updateUI();
                    }
                });
            }
        }
        
        return m;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgMatrixX = new javax.swing.ButtonGroup();
        bgMatrixY = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mFile = new javax.swing.JMenu();
        mFileNewProject = new javax.swing.JMenuItem();
        mFileOpenProject = new javax.swing.JMenuItem();
        mFileSaveProjectAs = new javax.swing.JMenuItem();
        mFileSaveProject = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mFileAddElement = new javax.swing.JMenuItem();
        mEdit = new javax.swing.JMenu();
        mMatrixX = new javax.swing.JMenu();
        mMatrixY = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        mFile.setText("File");

        mFileNewProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mFileNewProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16 newdocument.png"))); // NOI18N
        mFileNewProject.setText("New project");
        mFileNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFileNewProjectActionPerformed(evt);
            }
        });
        mFile.add(mFileNewProject);

        mFileOpenProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mFileOpenProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16 folder.png"))); // NOI18N
        mFileOpenProject.setText("Open project");
        mFileOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFileOpenProjectActionPerformed(evt);
            }
        });
        mFile.add(mFileOpenProject);

        mFileSaveProjectAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mFileSaveProjectAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16 floppydisk.png"))); // NOI18N
        mFileSaveProjectAs.setText("Save project as");
        mFileSaveProjectAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFileSaveProjectAsActionPerformed(evt);
            }
        });
        mFile.add(mFileSaveProjectAs);

        mFileSaveProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mFileSaveProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16 floppydisk.png"))); // NOI18N
        mFileSaveProject.setText("Save project");
        mFileSaveProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFileSaveProjectActionPerformed(evt);
            }
        });
        mFile.add(mFileSaveProject);
        mFile.add(jSeparator3);

        mFileAddElement.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mFileAddElement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16px-Crystal_Clear_action_edit_add.png"))); // NOI18N
        mFileAddElement.setText("Add element");
        mFileAddElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFileAddElementActionPerformed(evt);
            }
        });
        mFile.add(mFileAddElement);

        jMenuBar1.add(mFile);

        mEdit.setText("Edit");

        mMatrixX.setText("Matrix on X");
        mEdit.add(mMatrixX);

        mMatrixY.setText("Matrix on Y");
        mEdit.add(mMatrixY);

        jMenuBar1.add(mEdit);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mFileNewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFileNewProjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mFileNewProjectActionPerformed

    private void mFileOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFileOpenProjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mFileOpenProjectActionPerformed

    private void mFileSaveProjectAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFileSaveProjectAsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mFileSaveProjectAsActionPerformed

    private void mFileSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFileSaveProjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mFileSaveProjectActionPerformed

    private void mFileAddElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFileAddElementActionPerformed
        // Open dialog to choose a new element
        ElementDialog d = new ElementDialog(this, true, this);
        d.showDialog();
        if(d.getDialogResult() == DialogResult.Ok){
            desktopPane.addElementAbstract(d.getElementComponent(), d.getElementName());
        }
    }//GEN-LAST:event_mFileAddElementActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgMatrixX;
    private javax.swing.ButtonGroup bgMatrixY;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenu mEdit;
    private javax.swing.JMenu mFile;
    private javax.swing.JMenuItem mFileAddElement;
    private javax.swing.JMenuItem mFileNewProject;
    private javax.swing.JMenuItem mFileOpenProject;
    private javax.swing.JMenuItem mFileSaveProject;
    private javax.swing.JMenuItem mFileSaveProjectAs;
    private javax.swing.JMenu mMatrixX;
    private javax.swing.JMenu mMatrixY;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}