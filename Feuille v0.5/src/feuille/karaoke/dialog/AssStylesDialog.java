/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AssStylesDialog.java
 *
 * Created on 27 mars 2011, 22:07:35
 */

package feuille.karaoke.dialog;

import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import feuille.filter.SubtitleFilter;
import feuille.karaoke.lib.AssIO;
import feuille.karaoke.lib.AssStyle;
import feuille.karaoke.lib.AssStyleCollection;
import feuille.karaoke.lib.StylesPack;
import feuille.lib.Language;

/**
 * <p>This is a dialog for the choice of styles.<br />
 * C'est une boîte de dialogue pour lme choix des styles.</p>
 * @author The Wingate 2940
 */
public class AssStylesDialog extends javax.swing.JDialog {

    private ButtonPressed bp;
    private DefaultListModel dlmScript = null;
    private DefaultListModel dlmStored = null;
    private DefaultListModel dlmEmbedded = null;
    private DefaultComboBoxModel dcmStored = null;
    private List<StylesPack> listStylesPack = null;
    private Frame frame;

    private Language localeLanguage = feuille.MainFrame.getLanguage();
    private String sCopyOf = "Copy of ";
    private String sError = "Error";
    private String sConfirm = "Confirm";
    private String sAborted = "Aborted";
    private String sErrorMsg = "You cannot remove the Default package.";
    private String sConfirmMsg = "Do you really want to delete this package ?";
    private String sAbortedMsg = "A package already uses the same name.";

    /** <p>A choice of state.<br />Un choix d'état.</p> */
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }

    /** <p>Creates new form AssStylesDialog.<br />
     * Crée un nouveau formulaire AssStylesDialog.</p> */
    public AssStylesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        frame = parent;
        
        if(localeLanguage.getValueOf("titleASD2")!=null){setTitle(localeLanguage.getValueOf("titleASD2"));}
        if(localeLanguage.getValueOf("buttonClose")!=null){Close_Button.setText(localeLanguage.getValueOf("buttonClose"));}
        if(localeLanguage.getValueOf("labelStylesPack")!=null){lblPack.setText(localeLanguage.getValueOf("labelStylesPack"));}
        if(localeLanguage.getValueOf("labelStylesScript")!=null){lblScript.setText(localeLanguage.getValueOf("labelStylesScript"));}
        if(localeLanguage.getValueOf("labelStylesXFX")!=null){lblXFX.setText(localeLanguage.getValueOf("labelStylesXFX"));}
        if(localeLanguage.getValueOf("toolStylesAddPack")!=null){bNewSylesPack.setToolTipText(localeLanguage.getValueOf("toolStylesAddPack"));}
        if(localeLanguage.getValueOf("toolStylesDelPack")!=null){bRemoveSylesPack.setToolTipText(localeLanguage.getValueOf("toolStylesDelPack"));}
        if(localeLanguage.getValueOf("toolStylesStoScr")!=null){bStoredToScript.setToolTipText(localeLanguage.getValueOf("toolStylesStoScr"));}
        if(localeLanguage.getValueOf("toolStylesScrSto")!=null){bScriptToStored.setToolTipText(localeLanguage.getValueOf("toolStylesScrSto"));}
        if(localeLanguage.getValueOf("toolStylesAddSto")!=null){bAddStoredStyle.setToolTipText(localeLanguage.getValueOf("toolStylesAddSto"));}
        if(localeLanguage.getValueOf("toolStylesAddScr")!=null){bAddScriptStyle.setToolTipText(localeLanguage.getValueOf("toolStylesAddScr"));}
        if(localeLanguage.getValueOf("toolStylesEditSto")!=null){bEditStoredStyle.setToolTipText(localeLanguage.getValueOf("toolStylesEditSto"));}
        if(localeLanguage.getValueOf("toolStylesEditScr")!=null){bEditScriptStyle.setToolTipText(localeLanguage.getValueOf("toolStylesEditScr"));}
        if(localeLanguage.getValueOf("toolStylesCopySto")!=null){bCopyStoredStyle.setToolTipText(localeLanguage.getValueOf("toolStylesCopySto"));}
        if(localeLanguage.getValueOf("toolStylesCopyScr")!=null){bCopyScriptStyle.setToolTipText(localeLanguage.getValueOf("toolStylesCopyScr"));}
        if(localeLanguage.getValueOf("toolStylesRemSto")!=null){bRemoveStoredStyle.setToolTipText(localeLanguage.getValueOf("toolStylesRemSto"));}
        if(localeLanguage.getValueOf("toolStylesRemScr")!=null){bRemoveScriptStyle.setToolTipText(localeLanguage.getValueOf("toolStylesRemScr"));}
        if(localeLanguage.getValueOf("toolStylesEmbSto")!=null){bEmbeddedToStored.setToolTipText(localeLanguage.getValueOf("toolStylesEmbSto"));}
        if(localeLanguage.getValueOf("toolStylesEmbScr")!=null){bEmbeddedToScript.setToolTipText(localeLanguage.getValueOf("toolStylesEmbScr"));}
        if(localeLanguage.getValueOf("optpTitle1")!=null){sError = localeLanguage.getValueOf("optpTitle1");}
        if(localeLanguage.getValueOf("optpTitle2")!=null){sConfirm = localeLanguage.getValueOf("optpTitle2");}
        if(localeLanguage.getValueOf("optpTitle3")!=null){sAborted = localeLanguage.getValueOf("optpTitle3");}
        if(localeLanguage.getValueOf("optpMessage3")!=null){sErrorMsg = localeLanguage.getValueOf("optpMessage3");}
        if(localeLanguage.getValueOf("optpMessage4")!=null){sConfirmMsg = localeLanguage.getValueOf("optpMessage4");}
        if(localeLanguage.getValueOf("optpMessage5")!=null){sAbortedMsg = localeLanguage.getValueOf("optpMessage5");}
        if(localeLanguage.getValueOf("messCopyOf")!=null){sCopyOf = localeLanguage.getValueOf("messCopyOf");}
        if(localeLanguage.getValueOf("popmStyImp")!=null){popmImport.setText(localeLanguage.getValueOf("popmStyImp"));}
        if(localeLanguage.getValueOf("popmStyExp")!=null){popmExport.setText(localeLanguage.getValueOf("popmStyExp"));}
        if(localeLanguage.getValueOf("popmStyExp")!=null){popmExport2.setText(localeLanguage.getValueOf("popmStyExp"));}
        if(localeLanguage.getValueOf("buttonImport")!=null){btnImport.setText(localeLanguage.getValueOf("buttonImport"));}
//        setCommonFont(assfxmaker.AssFxMaker.getDefaultFont(), getContentPane().getComponents());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popStyles1 = new javax.swing.JPopupMenu();
        popmImport = new javax.swing.JMenuItem();
        popmExport = new javax.swing.JMenuItem();
        popStyles2 = new javax.swing.JPopupMenu();
        popmExport2 = new javax.swing.JMenuItem();
        fcStyles = new javax.swing.JFileChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        listScript = new javax.swing.JList();
        bScriptToStored = new javax.swing.JButton();
        lblScript = new javax.swing.JLabel();
        lblPack = new javax.swing.JLabel();
        lblXFX = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listEmbedded = new javax.swing.JList();
        bEmbeddedToScript = new javax.swing.JButton();
        bEmbeddedToStored = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listStored = new javax.swing.JList();
        cbStored = new javax.swing.JComboBox();
        bStoredToScript = new javax.swing.JButton();
        Close_Button = new javax.swing.JButton();
        bRemoveScriptStyle = new javax.swing.JButton();
        bRemoveSylesPack = new javax.swing.JButton();
        bAddScriptStyle = new javax.swing.JButton();
        bEditStoredStyle = new javax.swing.JButton();
        bEditScriptStyle = new javax.swing.JButton();
        bRemoveStoredStyle = new javax.swing.JButton();
        bAddStoredStyle = new javax.swing.JButton();
        bNewSylesPack = new javax.swing.JButton();
        bCopyStoredStyle = new javax.swing.JButton();
        bCopyScriptStyle = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();

        popmImport.setText("Import from clipboard");
        popStyles1.add(popmImport);

        popmExport.setText("Export to clipboard");
        popStyles1.add(popmExport);

        popmExport2.setText("Export to clipboard");
        popStyles2.add(popmExport2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listScript.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listScript);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(290, 50, 200, 370);

        bScriptToStored.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_back.png"))); // NOI18N
        bScriptToStored.setToolTipText("Copy the selected style to package.");
        bScriptToStored.setFocusable(false);
        bScriptToStored.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bScriptToStored.setPreferredSize(new java.awt.Dimension(30, 30));
        bScriptToStored.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bScriptToStored.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bScriptToStoredActionPerformed(evt);
            }
        });
        getContentPane().add(bScriptToStored);
        bScriptToStored.setBounds(250, 70, 30, 30);

        lblScript.setText("Script :");
        getContentPane().add(lblScript);
        lblScript.setBounds(290, 10, 200, 29);

        lblPack.setText("Packages :");
        getContentPane().add(lblPack);
        lblPack.setBounds(10, 12, 190, 26);

        lblXFX.setText("From XFX :");
        getContentPane().add(lblXFX);
        lblXFX.setBounds(10, 230, 190, 26);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listEmbedded.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listEmbedded);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 260, 190, 160);

        bEmbeddedToScript.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_forward.png"))); // NOI18N
        bEmbeddedToScript.setToolTipText("Copy the selected style to script.");
        bEmbeddedToScript.setFocusable(false);
        bEmbeddedToScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bEmbeddedToScript.setPreferredSize(new java.awt.Dimension(30, 30));
        bEmbeddedToScript.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bEmbeddedToScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEmbeddedToScriptActionPerformed(evt);
            }
        });
        getContentPane().add(bEmbeddedToScript);
        bEmbeddedToScript.setBounds(210, 290, 30, 30);

        bEmbeddedToStored.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_up.png"))); // NOI18N
        bEmbeddedToStored.setToolTipText("Copy the selected style to package.");
        bEmbeddedToStored.setFocusable(false);
        bEmbeddedToStored.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bEmbeddedToStored.setPreferredSize(new java.awt.Dimension(30, 30));
        bEmbeddedToStored.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bEmbeddedToStored.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEmbeddedToStoredActionPerformed(evt);
            }
        });
        getContentPane().add(bEmbeddedToStored);
        bEmbeddedToStored.setBounds(210, 260, 30, 30);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listStored.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listStored);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 72, 190, 150);

        cbStored.setEditable(true);
        cbStored.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbStored.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStoredActionPerformed(evt);
            }
        });
        getContentPane().add(cbStored);
        cbStored.setBounds(10, 46, 190, 20);

        bStoredToScript.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_forward.png"))); // NOI18N
        bStoredToScript.setToolTipText("Copy the selected style to script.");
        bStoredToScript.setFocusable(false);
        bStoredToScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bStoredToScript.setPreferredSize(new java.awt.Dimension(30, 30));
        bStoredToScript.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bStoredToScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStoredToScriptActionPerformed(evt);
            }
        });
        getContentPane().add(bStoredToScript);
        bStoredToScript.setBounds(210, 70, 30, 30);

        Close_Button.setText("Close");
        Close_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Close_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(Close_Button);
        Close_Button.setBounds(380, 430, 110, 30);

        bRemoveScriptStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_edit_remove.png"))); // NOI18N
        bRemoveScriptStyle.setToolTipText("Remove all selected styles of the script.");
        bRemoveScriptStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveScriptStyleActionPerformed(evt);
            }
        });
        getContentPane().add(bRemoveScriptStyle);
        bRemoveScriptStyle.setBounds(250, 190, 30, 30);

        bRemoveSylesPack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_edit_remove2.png"))); // NOI18N
        bRemoveSylesPack.setToolTipText("Delete a package.");
        bRemoveSylesPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveSylesPackActionPerformed(evt);
            }
        });
        getContentPane().add(bRemoveSylesPack);
        bRemoveSylesPack.setBounds(250, 40, 30, 30);

        bAddScriptStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_edit_add.png"))); // NOI18N
        bAddScriptStyle.setToolTipText("Add a new style to the script.");
        bAddScriptStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddScriptStyleActionPerformed(evt);
            }
        });
        getContentPane().add(bAddScriptStyle);
        bAddScriptStyle.setBounds(250, 100, 30, 30);

        bEditStoredStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_edit.png"))); // NOI18N
        bEditStoredStyle.setToolTipText("Edit the selected style of the package.");
        bEditStoredStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEditStoredStyleActionPerformed(evt);
            }
        });
        getContentPane().add(bEditStoredStyle);
        bEditStoredStyle.setBounds(210, 130, 30, 30);

        bEditScriptStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_edit.png"))); // NOI18N
        bEditScriptStyle.setToolTipText("Edit the selected style of the script.");
        bEditScriptStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEditScriptStyleActionPerformed(evt);
            }
        });
        getContentPane().add(bEditScriptStyle);
        bEditScriptStyle.setBounds(250, 130, 30, 30);

        bRemoveStoredStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_edit_remove.png"))); // NOI18N
        bRemoveStoredStyle.setToolTipText("Remove all selected styles of the package.");
        bRemoveStoredStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveStoredStyleActionPerformed(evt);
            }
        });
        getContentPane().add(bRemoveStoredStyle);
        bRemoveStoredStyle.setBounds(210, 190, 30, 30);

        bAddStoredStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_edit_add.png"))); // NOI18N
        bAddStoredStyle.setToolTipText("Add a new style to the package.");
        bAddStoredStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddStoredStyleActionPerformed(evt);
            }
        });
        getContentPane().add(bAddStoredStyle);
        bAddStoredStyle.setBounds(210, 100, 30, 30);

        bNewSylesPack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_edit_add2.png"))); // NOI18N
        bNewSylesPack.setToolTipText("Add a new package.");
        bNewSylesPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNewSylesPackActionPerformed(evt);
            }
        });
        getContentPane().add(bNewSylesPack);
        bNewSylesPack.setBounds(210, 40, 30, 30);

        bCopyStoredStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        bCopyStoredStyle.setToolTipText("Copy the selected style of the package.");
        bCopyStoredStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCopyStoredStyleActionPerformed(evt);
            }
        });
        getContentPane().add(bCopyStoredStyle);
        bCopyStoredStyle.setBounds(210, 160, 30, 30);

        bCopyScriptStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        bCopyScriptStyle.setToolTipText("Copy the selected style of the script.");
        bCopyScriptStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCopyScriptStyleActionPerformed(evt);
            }
        });
        getContentPane().add(bCopyScriptStyle);
        bCopyScriptStyle.setBounds(250, 160, 30, 30);

        btnImport.setText("Import...");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });
        getContentPane().add(btnImport);
        btnImport.setBounds(260, 430, 110, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbStoredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStoredActionPerformed
        if(dcmStored.getSize()!=0){
            if(dcmStored.getSelectedItem() instanceof StylesPack){
                StylesPack sp = (StylesPack)dcmStored.getSelectedItem();
                dlmStored.clear();                
                for(AssStyle as : sp.getCollection().getMembers()){
                    dlmStored.addElement(as);
                }
            }
        }
    }//GEN-LAST:event_cbStoredActionPerformed

    private void Close_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Close_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_Close_ButtonActionPerformed

    private void bNewSylesPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNewSylesPackActionPerformed
        StylesPack sp = new StylesPack(cbStored.getSelectedItem().toString(), new AssStyleCollection());
        boolean exist = false;
        for(int i=0; i<dcmStored.getSize();i++){
            if(dcmStored.getElementAt(i) instanceof StylesPack){
                StylesPack sPack = (StylesPack)dcmStored.getElementAt(i);
                if(sPack.getPack().equalsIgnoreCase(sp.getPack())){
                    exist = true;
                }
            }
        }
        if(exist==false){
            dcmStored.addElement(sp);
        }else{
            JOptionPane.showMessageDialog(this,sAbortedMsg,sAborted,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_bNewSylesPackActionPerformed

    private void bRemoveSylesPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveSylesPackActionPerformed
        if(dcmStored.getSelectedItem() instanceof StylesPack){
            StylesPack sp = (StylesPack)dcmStored.getSelectedItem();
            if(sp.getPack().equalsIgnoreCase("Default")){
                JOptionPane.showMessageDialog(this,sErrorMsg,sError,
                        JOptionPane.ERROR_MESSAGE);
            }else{
                int z = JOptionPane.showConfirmDialog(this,sConfirmMsg,sConfirm,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(z==JOptionPane.YES_OPTION){
                    dcmStored.removeElement(dcmStored.getSelectedItem());
                }                
            }
        }        
    }//GEN-LAST:event_bRemoveSylesPackActionPerformed

    private void bAddStoredStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddStoredStyleActionPerformed
        AssStyleDialog asd = new AssStyleDialog(frame, true);
        asd.setLocationRelativeTo(null);
        AssStyle as = asd.showDialog(new AssStyle());
        if(asd.isOkPressed()){
            if(dcmStored.getSelectedItem() instanceof StylesPack){
                StylesPack sp = (StylesPack)dcmStored.getSelectedItem();
                AssStyleCollection asc = sp.getCollection();
                asc.addMember(as.getName(), as);
                sp.setCollection(asc);            
                dcmStored.removeElement(dcmStored.getSelectedItem());
                dcmStored.addElement(sp);
                dcmStored.setSelectedItem(sp);
            }
        }        
    }//GEN-LAST:event_bAddStoredStyleActionPerformed

    private void bEditStoredStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEditStoredStyleActionPerformed
        AssStyleDialog asd = new AssStyleDialog(frame, true);
        asd.setLocationRelativeTo(null);
        AssStyle as = asd.showDialog((AssStyle)listStored.getSelectedValue());
        if(asd.isOkPressed()){
            if(dcmStored.getSelectedItem() instanceof StylesPack){
                StylesPack sp = (StylesPack)dcmStored.getSelectedItem();
                AssStyleCollection asc = sp.getCollection();
                asc.changeMember(as.getName(), as);
                sp.setCollection(asc);
                dcmStored.removeElement(dcmStored.getSelectedItem());
                dcmStored.addElement(sp);
                dcmStored.setSelectedItem(sp);
            }
        }        
    }//GEN-LAST:event_bEditStoredStyleActionPerformed

    private void bCopyStoredStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCopyStoredStyleActionPerformed
        AssStyleDialog asd = new AssStyleDialog(frame, true);
        asd.setLocationRelativeTo(null);
        AssStyle asOrg = (AssStyle)listStored.getSelectedValue();
        AssStyle as = AssStyle.cloneAssStyle(asOrg);
        as.setName(sCopyOf+as.getName());
        as = asd.showDialog(as);
        if(asd.isOkPressed()){
            if(dcmStored.getSelectedItem() instanceof StylesPack){
                StylesPack sp = (StylesPack)dcmStored.getSelectedItem();
                AssStyleCollection asc = sp.getCollection();
                asc.addMember(as.getName(), as);
                sp.setCollection(asc);
                dcmStored.removeElement(dcmStored.getSelectedItem());
                dcmStored.addElement(sp);
                dcmStored.setSelectedItem(sp);
            }
        }
    }//GEN-LAST:event_bCopyStoredStyleActionPerformed

    private void bRemoveStoredStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveStoredStyleActionPerformed
        for(Object o : listStored.getSelectedValues()){
            AssStyle as = (AssStyle)o;
            if(dcmStored.getSelectedItem() instanceof StylesPack){
                StylesPack sp = (StylesPack)dcmStored.getSelectedItem();
                AssStyleCollection asc = sp.getCollection();
                asc.deleteMember(as.getName());
                sp.setCollection(asc);
                dcmStored.removeElement(dcmStored.getSelectedItem());
                dcmStored.addElement(sp);
                dcmStored.setSelectedItem(sp);
            }
        }
    }//GEN-LAST:event_bRemoveStoredStyleActionPerformed

    private void bAddScriptStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddScriptStyleActionPerformed
        AssStyleDialog asd = new AssStyleDialog(frame, true);
        asd.setLocationRelativeTo(null);
        AssStyle as = asd.showDialog(new AssStyle());
        if(asd.isOkPressed()){
            boolean exist = false;
            for(Object o : dlmScript.toArray()){
                AssStyle ase = (AssStyle)o;
                if(as.getName().equals(ase.getName())){
                    exist = true;
                }
            }
            if(exist==false){
                dlmScript.addElement(as);
            }
        }
    }//GEN-LAST:event_bAddScriptStyleActionPerformed

    private void bEditScriptStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEditScriptStyleActionPerformed
        AssStyleDialog asd = new AssStyleDialog(frame, true);
        asd.setLocationRelativeTo(null);
        AssStyle as = (AssStyle)listScript.getSelectedValue();
        String name = as.getName();
        as = asd.showDialog(as);
        if(asd.isOkPressed()){
            //Check if the name has been changed
            boolean exist = false;
            for(Object o : dlmScript.toArray()){
                AssStyle ase = (AssStyle)o;
                if(ase.equals(as)==false){
                    if(as.getName().equals(ase.getName())){
                        exist = true;
                    }
                }
            }
            //The name has been changed and a doubloon has been detected.
            //Then the name must be the initial name.
            if(exist==true){
                as.setName(name);
            }
        }        
    }//GEN-LAST:event_bEditScriptStyleActionPerformed

    private void bCopyScriptStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCopyScriptStyleActionPerformed
        AssStyleDialog asd = new AssStyleDialog(frame, true);
        asd.setLocationRelativeTo(null);
        AssStyle asOrg = (AssStyle)listScript.getSelectedValue();
        AssStyle as = AssStyle.cloneAssStyle(asOrg);
        as.setName(sCopyOf+as.getName());
        as = asd.showDialog(as);
        if(asd.isOkPressed()){
            boolean exist = false;
            for(Object o : dlmScript.toArray()){
                AssStyle ase = (AssStyle)o;
                if(as.getName().equals(ase.getName())){
                    exist = true;
                }
            }
            if(exist==false){
                dlmScript.addElement(as);
            }
        }
    }//GEN-LAST:event_bCopyScriptStyleActionPerformed

    private void bRemoveScriptStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveScriptStyleActionPerformed
        for(Object o : listScript.getSelectedValues()){
            dlmScript.removeElement(o);
        }
    }//GEN-LAST:event_bRemoveScriptStyleActionPerformed

    private void bStoredToScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStoredToScriptActionPerformed
        for(Object object : listStored.getSelectedValues()){
            AssStyle asOrg = (AssStyle)object;
            AssStyle as = AssStyle.cloneAssStyle(asOrg);
            boolean exist = false;
            for(Object o : dlmScript.toArray()){
                AssStyle ase = (AssStyle)o;
                if(as.getName().equals(ase.getName())){
                    exist = true;
                }
            }
            if(exist==false){
                dlmScript.addElement(as);
            }
        }
    }//GEN-LAST:event_bStoredToScriptActionPerformed

    private void bScriptToStoredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bScriptToStoredActionPerformed
        for(Object object : listScript.getSelectedValues()){
            AssStyle asOrg = (AssStyle)object;
            AssStyle as = AssStyle.cloneAssStyle(asOrg);
            if(dcmStored.getSelectedItem() instanceof StylesPack){
                StylesPack sp = (StylesPack)dcmStored.getSelectedItem();
                AssStyleCollection asc = sp.getCollection();
                asc.addMember(as.getName(), as);
                sp.setCollection(asc);
                dcmStored.removeElement(dcmStored.getSelectedItem());
                dcmStored.addElement(sp);
                dcmStored.setSelectedItem(sp);
            }
        }
    }//GEN-LAST:event_bScriptToStoredActionPerformed

    private void bEmbeddedToStoredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEmbeddedToStoredActionPerformed
        for(Object object : listEmbedded.getSelectedValues()){
            AssStyle asOrg = (AssStyle)object;
            AssStyle as = AssStyle.cloneAssStyle(asOrg);
            if(dcmStored.getSelectedItem() instanceof StylesPack){
                StylesPack sp = (StylesPack)dcmStored.getSelectedItem();
                AssStyleCollection asc = sp.getCollection();
                asc.addMember(as.getName(), as);
                sp.setCollection(asc);
                dcmStored.removeElement(dcmStored.getSelectedItem());
                dcmStored.addElement(sp);
                dcmStored.setSelectedItem(sp);
            }
        }
    }//GEN-LAST:event_bEmbeddedToStoredActionPerformed

    private void bEmbeddedToScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEmbeddedToScriptActionPerformed
        for(Object object : listEmbedded.getSelectedValues()){
            AssStyle asOrg = (AssStyle)object;
            AssStyle as = AssStyle.cloneAssStyle(asOrg);
            boolean exist = false;
            for(Object o : dlmScript.toArray()){
                AssStyle ase = (AssStyle)o;
                if(as.getName().equals(ase.getName())){
                    exist = true;
                }
            }
            if(exist==false){
                dlmScript.addElement(as);
            }
        }        
    }//GEN-LAST:event_bEmbeddedToScriptActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        // lAssStyle : Get list of all styles from files
        List<AssStyle> lAssStyle = new ArrayList<AssStyle>();
        // las : Get list of all styles choosen by the user
        List<AssStyle> las = new ArrayList<AssStyle>();
        // Clear the list of file filters.
        for (javax.swing.filechooser.FileFilter f : fcStyles.getChoosableFileFilters()){
            fcStyles.removeChoosableFileFilter(f);
        }
        // Add good file filters.
        fcStyles.addChoosableFileFilter(new SubtitleFilter());
        fcStyles.setAccessory(null);
        // Action
        int z = this.fcStyles.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            // Search for styles in a SSA
            if(fcStyles.getSelectedFile().getName().endsWith("ssa")){
                AssIO aio = new AssIO();
                lAssStyle = aio.ExtractSSAStyles(fcStyles.getSelectedFile().getPath());
            }
            // Search for styles in an ASS
            if(fcStyles.getSelectedFile().getName().endsWith("ass")){
                AssIO aio = new AssIO();
                lAssStyle = aio.ExtractASSStyles(fcStyles.getSelectedFile().getPath());
            }
            
            // A new dialog for the choice of styles
            ImportStylesDialog isd = new ImportStylesDialog(frame, true);
            isd.setFilename(fcStyles.getSelectedFile().getName());
            isd.setLocationRelativeTo(null);
            las = isd.showDialog(lAssStyle);
            if(las!=null){
                for(AssStyle as : las){
                    boolean exist = false;
                    for(int i=0;i<dlmScript.getSize();i++){
                        AssStyle asDlm = (AssStyle)dlmScript.getElementAt(i);
                        if(asDlm.getName().equalsIgnoreCase(as.getName())){
                            exist = true;
                        }
                    }
                    if(exist==false){
                        dlmScript.addElement(as);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnImportActionPerformed

    /** <p>Initialize the dialog.<br />
     * Initialise la boîte de dialogue.</p> */
    private void init(){
        setSize(520,510);
        setTitle("Styles");

        dlmScript = new DefaultListModel();
        dlmStored = new DefaultListModel();
        dlmEmbedded = new DefaultListModel();
        dcmStored = new DefaultComboBoxModel();
        listStylesPack = new ArrayList<StylesPack>();

        listScript.setModel(dlmScript);
        listStored.setModel(dlmStored);
        listEmbedded.setModel(dlmEmbedded);
        cbStored.setModel(dcmStored);
    }

    /** <p>Show the dialog.<br />Ouvre la boîte de dialogue.</p> */
    public void showDialog(){
        setVisible(true);
    }

    /** <p>Set the script list.<br />Définit la liste du script.</p> */
    public void setScriptList(AssStyleCollection asc){
        for(AssStyle as : asc.getMembers()){
            dlmScript.addElement(as);
        }
    }

    /** <p>Add an embedded style.<br />Ajoute un style embarqué.</p> */
    public void addEmbeddedStyle(AssStyle as){
        as.setName(as.getName().trim());
        dlmEmbedded.addElement(as);
    }

    /** <p>Set the stored list.<br />Définit la liste du stockage.</p> */
    public void setStoredList(List<StylesPack> lsp){
        this.listStylesPack = lsp;
        for(StylesPack sp : lsp){
            dcmStored.addElement(sp);
        }
    }
    
    /** <p>Get the script list.<br />Obtient la liste du script.</p> */
    public AssStyleCollection getScriptList(){
        AssStyleCollection newAsc = new AssStyleCollection();
        for(Object o : dlmScript.toArray()){
            if(o instanceof AssStyle){
                AssStyle as = (AssStyle)o;
                newAsc.addMember(as.getName(), as);
            }
        }
        return newAsc;
    }

    /** <p>Get the stored list.<br />Obtient la liste du stockage.</p> */
    public List<StylesPack> getStoredList(){
        for(int i=0;i<dcmStored.getSize();i++){
            Object o = dcmStored.getElementAt(i);
            if(o instanceof StylesPack){
                StylesPack sp = (StylesPack)o;
                if(listStylesPack.contains(sp)==false){
                    listStylesPack.add(sp);
                }
            }
        }
        return listStylesPack;
    }
    
    private void setCommonFont(Font f, java.awt.Component[] comps){
        for(java.awt.Component c : comps){
            if (c instanceof javax.swing.JComponent){
                javax.swing.JComponent jcomp = (javax.swing.JComponent)c;
                Font origin = jcomp.getFont();
                if(origin.isPlain()){
                    jcomp.setFont(f);
                }else{
                    jcomp.setFont(f.deriveFont(origin.getStyle(), origin.getSize2D()));
                }
                setCommonFont(f, jcomp.getComponents());
            }
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AssStylesDialog dialog = new AssStylesDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton Close_Button;
    private javax.swing.JButton bAddScriptStyle;
    private javax.swing.JButton bAddStoredStyle;
    private javax.swing.JButton bCopyScriptStyle;
    private javax.swing.JButton bCopyStoredStyle;
    private javax.swing.JButton bEditScriptStyle;
    private javax.swing.JButton bEditStoredStyle;
    private javax.swing.JButton bEmbeddedToScript;
    private javax.swing.JButton bEmbeddedToStored;
    private javax.swing.JButton bNewSylesPack;
    private javax.swing.JButton bRemoveScriptStyle;
    private javax.swing.JButton bRemoveStoredStyle;
    private javax.swing.JButton bRemoveSylesPack;
    private javax.swing.JButton bScriptToStored;
    private javax.swing.JButton bStoredToScript;
    private javax.swing.JButton btnImport;
    private javax.swing.JComboBox cbStored;
    private javax.swing.JFileChooser fcStyles;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblPack;
    private javax.swing.JLabel lblScript;
    private javax.swing.JLabel lblXFX;
    private javax.swing.JList listEmbedded;
    private javax.swing.JList listScript;
    private javax.swing.JList listStored;
    private javax.swing.JPopupMenu popStyles1;
    private javax.swing.JPopupMenu popStyles2;
    private javax.swing.JMenuItem popmExport;
    private javax.swing.JMenuItem popmExport2;
    private javax.swing.JMenuItem popmImport;
    // End of variables declaration//GEN-END:variables

}
