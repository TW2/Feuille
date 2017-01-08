/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SnippetDialog.java
 *
 * Created on 26 févr. 2011, 21:33:48
 */

package feuille.karaoke.dialog;

import java.awt.Frame;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import feuille.karaoke.renderer.snippetTableRenderer;
import feuille.lib.Language;
import feuille.lib.Language.ISO_3166;
import feuille.scripting.ScriptPlugin;
import feuille.scripting.Snippet;
import feuille.scripting.SnippetElement;
import feuille.scripting.XmlSnippetHandler;
import feuille.scripting.XmlSnippetWriter;

/**
 * <p>This is a dialog for the choice of snippet.<br />
 * C'est une boîte de dialogue pour le choix de snippet.</p>
 * @author The Wingate 2940
 */
public class SnippetDialog extends javax.swing.JDialog {

    private ButtonPressed bp;
    DefaultTableModel dtm;
    DefaultComboBoxModel dcbmTranslate;
    List<Snippet> listSnippet = new ArrayList<Snippet>();
    private Language localeLanguage = feuille.MainFrame.getLanguage();
    private Frame frame;
    private String message1="Do you really want to do this ?", message2="Select a choice";

    /** <p>Creates new form SnippetDialog.<br />
     * Crée un nouveau formulaire SnippetDialog.</p> */
    public SnippetDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        frame = parent;
        
        dcbmTranslate.setSelectedItem(Language.getDefaultISO_3166());
        if(localeLanguage.getValueOf("titleSD")!=null){setTitle(localeLanguage.getValueOf("titleSD"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("labelSnippet")!=null){jLabel1.setText(localeLanguage.getValueOf("labelSnippet"));}
        if(localeLanguage.getValueOf("buttonUpdate")!=null){btnUpdate.setText(localeLanguage.getValueOf("buttonUpdate"));}
        if(localeLanguage.getValueOf("buttonCreate")!=null){btnCreate.setText(localeLanguage.getValueOf("buttonCreate"));}
        if(localeLanguage.getValueOf("buttonDelete")!=null){btnDelete.setText(localeLanguage.getValueOf("buttonDelete"));}
        if(localeLanguage.getValueOf("buttonModify")!=null){btnModify.setText(localeLanguage.getValueOf("buttonModify"));}
        if(localeLanguage.getValueOf("checkboxSave")!=null){cbSave.setText(localeLanguage.getValueOf("checkboxSave"));}
        if(localeLanguage.getValueOf("textboxName")!=null){tfName.setText(localeLanguage.getValueOf("textboxName"));}
        if(localeLanguage.getValueOf("textboxAuthor")!=null){tfAuthor.setText(localeLanguage.getValueOf("textboxAuthor"));}
        if(localeLanguage.getValueOf("textboxDesc")!=null){tfDesc.setText(localeLanguage.getValueOf("textboxDesc"));}
        if(localeLanguage.getValueOf("optpSDConfirmRequest")!=null){message1 = localeLanguage.getValueOf("optpSDConfirmRequest");}
        if(localeLanguage.getValueOf("optpSDConfirmChoice")!=null){message2 = localeLanguage.getValueOf("optpSDConfirmChoice");}
        
        TableColumn column;
        for (int i = 0; i < 5; i++) {
            column = jTable1.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    if(localeLanguage.getValueOf("tableSDType")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableSDType"));
                    }
                    break;
                case 1:
                    if(localeLanguage.getValueOf("tableSDName")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableSDName"));
                    }
                    break;
                case 2:
                    if(localeLanguage.getValueOf("tableSDDescription")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableSDDescription"));
                    }
                    break;
                case 3:
                    if(localeLanguage.getValueOf("tableSDAuthor")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableSDAuthor"));
                    }
                    break;
                case 4:
                    if(localeLanguage.getValueOf("tableSDSnippet")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableSDSnippet"));
                    }
                    break;
            }
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

        bgCode = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        Cancel_Button = new javax.swing.JButton();
        OK_Button = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnUpdate = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        epSnippet = new javax.swing.JEditorPane();
        tbRuby = new javax.swing.JToggleButton();
        tbPython = new javax.swing.JToggleButton();
        btnCreate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        cbTranslate = new javax.swing.JComboBox();
        tfName = new javax.swing.JTextField();
        tfDesc = new javax.swing.JTextField();
        tfAuthor = new javax.swing.JTextField();
        cbSave = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("<html><h2>Choose a snippet in the list below :");

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        OK_Button.setText("OK");
        OK_Button.setEnabled(false);
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setViewportView(epSnippet);

        bgCode.add(tbRuby);
        tbRuby.setSelected(true);
        tbRuby.setText("Ruby");
        tbRuby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbRubyActionPerformed(evt);
            }
        });

        bgCode.add(tbPython);
        tbPython.setText("Python");
        tbPython.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbPythonActionPerformed(evt);
            }
        });

        btnCreate.setText("Create");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnModify.setText("Modify");
        btnModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyActionPerformed(evt);
            }
        });

        cbTranslate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tfName.setText("Name");
        tfName.setToolTipText("Insert a name");

        tfDesc.setText("Description");
        tfDesc.setToolTipText("Insert a description");

        tfAuthor.setText("Author");
        tfAuthor.setToolTipText("Insert an author");

        cbSave.setText("Save");
        cbSave.setToolTipText("Save snippet list");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(tfName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCreate, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(tfAuthor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnModify, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tfDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tbRuby, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tbPython, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(OK_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbTranslate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTranslate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cancel_Button)
                    .addComponent(OK_Button)
                    .addComponent(btnUpdate)
                    .addComponent(btnCreate)
                    .addComponent(btnDelete)
                    .addComponent(tbRuby)
                    .addComponent(tbPython)
                    .addComponent(btnModify)
                    .addComponent(cbSave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON;
        if(cbSave.isSelected()){
            saveSnippets(null);
        }
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        bp = ButtonPressed.CANCEL_BUTTON;
        if(cbSave.isSelected()){
            saveSnippets(null);
        }
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        loadSnippets(true);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        OK_Button.setEnabled(true);
        
        if(jTable1.getSelectedRow()!=-1){
            Snippet sni = getSnippet();
            SnippetElement se = getSnippetElement();
            tfName.setText(sni.getTitle());
            tfAuthor.setText(se.getAuthor());
            tfDesc.setText(se.getDescription());
            epSnippet.setText(se.getCode());
            dcbmTranslate.setSelectedItem(se.getLanguage());
            if(sni.getType().equalsIgnoreCase("Ruby")){
                tbRuby.setSelected(true);
            }else{//Python
                tbPython.setSelected(true);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void tbRubyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbRubyActionPerformed
        // Select Ruby scripting
        epSnippet.setContentType("text/ruby");
    }//GEN-LAST:event_tbRubyActionPerformed

    private void tbPythonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbPythonActionPerformed
        // Select Python scripting
        epSnippet.setContentType("text/python");
    }//GEN-LAST:event_tbPythonActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // Create snippet
        boolean isNew = false;
        Snippet sni = getSnippetFromCurrentName();
        if(sni==null){
            sni = new Snippet();
            isNew = true;
        }
        sni.setTitle(tfName.getText());
        sni.setType(tbRuby.isSelected()==true ? "Ruby" : "Python");
        SnippetElement se = new SnippetElement();
        se.setAuthor(tfAuthor.getText());
        se.setCode(epSnippet.getText());
        se.setDescription(tfDesc.getText());
        se.setLanguage((ISO_3166)dcbmTranslate.getSelectedItem());
        sni.addSnippetElement(se);
        if(isNew==true){
            listSnippet.add(sni);
        }        
        refreshSnippets(true);
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifyActionPerformed
        // Modify snippet
        if(jTable1.getSelectedRow()!=-1){
            int n = JOptionPane.showConfirmDialog(frame, message1, message2,JOptionPane.YES_NO_OPTION);
            if(n==JOptionPane.YES_OPTION){
                Snippet sni = getSnippet();
                sni.setTitle(tfName.getText());
                sni.setType(tbRuby.isSelected()==true ? "Ruby" : "Python");
                SnippetElement se = getSnippetElement();
                se.setAuthor(tfAuthor.getText());
                se.setCode(epSnippet.getText());
                se.setDescription(tfDesc.getText());
                se.setLanguage((ISO_3166)dcbmTranslate.getSelectedItem());
                refreshSnippets(true);
            }            
        }
    }//GEN-LAST:event_btnModifyActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Delete snippet
        if(jTable1.getSelectedRow()!=-1){
            int n = JOptionPane.showConfirmDialog(frame, message1, message2,JOptionPane.YES_NO_OPTION);
            if(n==JOptionPane.YES_OPTION){
                Snippet sni = getSnippet();
                SnippetElement se = getSnippetElement();
                sni.deleteSnippetElement(se);
                if(sni.getSnippetElements().isEmpty()){
                    listSnippet.remove(sni);
                }
                refreshSnippets(true);
            }            
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }

    /** <p>Initialize the form.<br />Initialise le formulaire.</p> */
    private void init(){
        String[] head = new String[]{"Type", "Name", "Description", "Author", "Snippet"};

        dtm = new DefaultTableModel(
                null,
                head
        ){
            Class[] types = new Class [] {
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                    java.lang.Object.class, java.lang.Object.class};
            boolean[] canEdit = new boolean [] {false, false, false, false, false};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        jTable1.setModel(dtm);
        TableColumn column;
        for (int i = 0; i < 5; i++) {
            column = jTable1.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(20);
                    break; //Type
                case 1:
                    column.setPreferredWidth(200);
                    break; //Name
                case 2:
                    column.setPreferredWidth(1000);
                    break; //Description
                case 3:
                    column.setPreferredWidth(200);
                    break; //Author
                case 4:
                    column.setPreferredWidth(0);//1000
                    break; //Snippet
            }
        }
        
        jTable1.setDefaultRenderer(Object.class, new snippetTableRenderer());

        dcbmTranslate = new DefaultComboBoxModel();
        cbTranslate.setModel(dcbmTranslate);
        for(ISO_3166 lg : ISO_3166.values()){
            dcbmTranslate.addElement(lg);
        }
        
        // Setting up the scripting object to work with
        // the opensource project JSyntaxPane - see web site :
        // http://code.google.com/p/jsyntaxpane/
        jsyntaxpane.DefaultSyntaxKit.initKit();
        epSnippet.setContentType("text/ruby");
    }
    
    /** <p>Get a snippet.<br />Obtient un snippet.</p> */
    public Snippet getSnippet(){
        return (Snippet)jTable1.getValueAt(jTable1.getSelectedRow(), 1);        
    }
    
    public Snippet getSnippetFromCurrentName(){
        for (Snippet sni : listSnippet){
            if(sni.getTitle().equalsIgnoreCase(tfName.getText())){
                return sni;
            }
        }
        return null;
    }
    
    public SnippetElement getSnippetElement(){
        return (SnippetElement)jTable1.getValueAt(jTable1.getSelectedRow(), 4);        
    }
    
    private void refreshSnippets(boolean clear){
        if (clear == true){
            try{
                for (int i=dtm.getRowCount()-1;i>=0;i--){
                    dtm.removeRow(i);
                }
            }catch(Exception exc){}
        }
        for(Snippet sni : listSnippet){
            URL urlRuby = getClass().getResource("AFM-mRubyScript.png");
            URL urlPython = getClass().getResource("AFM-mPythonScript.png");
            for(SnippetElement se : sni.getSnippetElements()){
                Object[] data = new Object[5];
                if(sni.getType().equalsIgnoreCase("Ruby")){
                    data[0] = urlRuby; //Object
                }else{//Python
                    data[0] = urlPython; //Object
                }
                data[1] = sni; //String
                data[2] = se.getDescription(); //String
                data[3] = se.getAuthor(); //String
                data[4] = se; //Object
                dtm.addRow(data);
            }
        }
    }

    /** <p>Get a list of snippet and return :<br />if OK - a snippet<br />
     * if Cancel - a null value<br />Obtient une liste de snippet et retourne :<br />
     * si OK - un snippet<br />si Annuler - une valeur null.</p> */
    public String showDialog(){
        loadSnippets(false);
        setVisible(true);

        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return epSnippet.getText();
        }else{
            return null;
        }
    }
    
    public void saveSnippets(String path){
        if(path==null){
            path = ScriptPlugin.getScriptsPath()+"default.sni";
        }
        XmlSnippetWriter xsw = new XmlSnippetWriter();
        xsw.setSnippets(listSnippet);
        xsw.createSnippets(path);
    }
    
    public void loadSnippets(boolean refresh){
        listSnippet.clear();
        java.io.File sniFile = new java.io.File(ScriptPlugin.getScriptsPath());
        for(java.io.File sFile : sniFile.listFiles()){
            if(sFile.getAbsolutePath().endsWith(".sni")){
                try {
                    XmlSnippetHandler xsh = new XmlSnippetHandler(sFile.getAbsolutePath());
                    List<Snippet> tempList = xsh.getSnippets();
                    
                    for(Snippet sni : tempList){
                        listSnippet.add(sni);
                    }
                } catch (ParserConfigurationException ex) {
                    
                } catch (SAXException ex) {
                    
                } catch (IOException ex) {
                    
                }
            }
        }
        refreshSnippets(refresh);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SnippetDialog dialog = new SnippetDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup bgCode;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox cbSave;
    private javax.swing.JComboBox cbTranslate;
    private javax.swing.JEditorPane epSnippet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton tbPython;
    private javax.swing.JToggleButton tbRuby;
    private javax.swing.JTextField tfAuthor;
    private javax.swing.JTextField tfDesc;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables

}
