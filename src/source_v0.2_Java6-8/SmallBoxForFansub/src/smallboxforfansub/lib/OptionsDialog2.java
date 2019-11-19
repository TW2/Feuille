/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.lib;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import smallboxforfansub.filter.AnyFilter;
import smallboxforfansub.filter.ExeJarFilter;
import smallboxforfansub.filter.ImageFilter;
import smallboxforfansub.filter.TTFFilter;
import smallboxforfansub.karaoke.lib.FontWithCoef;
import smallboxforfansub.karaoke.lib.ImagePreview;
import smallboxforfansub.theme.Theme;
import smallboxforfansub.theme.ThemeCollection;

/**
 *
 * @author The Wingate 2940
 */
public class OptionsDialog2 extends javax.swing.JDialog {

    private ButtonPressed bp = ButtonPressed.NONE;
    private DefaultComboBoxModel dcbmTheme;
    private java.awt.Frame frame;
    private Language localeLanguage = smallboxforfansub.MainFrame.getLanguage();
    private DefaultTableModel dtmFonts;
    private DefaultComboBoxModel dcbmFonts;
    private DefaultTableModel dtmTranslate;
    private Map<String,String> translateMap = new HashMap<>();
    private DefaultComboBoxModel dcbmTranslate;
    private DefaultComboBoxModel dcbmChooseForced;
    private String force_ISO = "---";
    private String optTitle4 = "Existing file";
    private String optMessage6 = "Would you really overwrite the existing file ?";
    private String optMessage7 = "The file has been created or updated.";
    private ThemeCollection themecollection;
    private DefaultComboBoxModel dcbmStartWith;
    private String DOCSPATH = "";
    
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }
    
    public enum Column{
        FONT(0), CORRECTION(1);

        private int id;

        Column(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }
    
    public enum ColumnTranslate{
        KEY(0), VALUE(1);

        private int id;

        ColumnTranslate(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }
    
    public enum SWModule{
        WELCOME("welc","Welcome"),
        KARAOKE("kara","Karaoke"),
        CODEEDITOR("code","Code editor"),
        DRAWEDITOR("draw","Drawing editor"),
        ANALYSIS("anal","Analysis");
        
        private String code;
        private String display;

        SWModule(String code, String display){
            this.code = code;
            this.display = display;
        }
        
        public void setDisplay(String display){
            this.display = display;
        }
        
        public String getDisplay(){
            return display;
        }
        
        public String getCode(){
            return code;
        }
        
        @Override
        public String toString(){
            return display;
        }
    }
    
    /**
     * Creates new form OptoionsDialog2
     */
    public OptionsDialog2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        frame = parent;
        TableColumn column;
        String[] fxHead;
        javax.swing.border.TitledBorder tb;
        
        dcbmTheme = new DefaultComboBoxModel();
        cbTheme.setModel(dcbmTheme);        
        themecollection = new ThemeCollection();
        themecollection.setup();
        for(Theme th : themecollection.getInternalThemes()){
            dcbmTheme.addElement(th);
        }
        for(Theme th : themecollection.getExternalThemes()){
            dcbmTheme.addElement(th);
        }
        
        fxHead = new String[]{"Key", "Value"};        
        dtmTranslate = new DefaultTableModel(null,fxHead){
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                    false, true};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };        
        tableTranslate.setModel(dtmTranslate);        
        for (int i = 0; i < 2; i++) {
            column = tableTranslate.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(100);
                    column.setIdentifier(ColumnTranslate.KEY.getId());
                    break; //Font
                case 1:
                    column.setPreferredWidth(500);
                    column.setIdentifier(ColumnTranslate.VALUE.getId());
                    break; //Correction
            }
        }        
        dcbmTranslate = new DefaultComboBoxModel();
        cbTranslate.setModel(dcbmTranslate);
        dcbmChooseForced = new DefaultComboBoxModel();
        cbChooseForced.setModel(dcbmChooseForced);
        for(Language.ISO_3166 lg : Language.ISO_3166.values()){
            dcbmTranslate.addElement(lg);
            dcbmChooseForced.addElement(lg);
        }
        dcbmTranslate.setSelectedItem(Language.getDefaultISO_3166());
        setTranslationTable();
        
        dcbmStartWith = new DefaultComboBoxModel();
        for(SWModule swm : SWModule.values()){
            dcbmStartWith.addElement(swm);
        }
        cbStartWith.setModel(dcbmStartWith);
        
        fxHead = new String[]{"Font", "Correction %"};
        dtmFonts = new DefaultTableModel(null,fxHead){
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class};
            boolean[] canEdit = new boolean [] {
                    false, false};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };        
        tableFonts.setModel(dtmFonts);        
        for (int i = 0; i < 2; i++) {
            column = tableFonts.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(300);
                    column.setIdentifier(Column.FONT.getId());
                    break; //Font
                case 1:
                    column.setPreferredWidth(60);
                    column.setIdentifier(Column.CORRECTION.getId());
                    break; //Correction
            }
        }
        
        dcbmFonts = new DefaultComboBoxModel();
        cbFonts.setModel(dcbmFonts);
        GraphicsEnvironment geLocal = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final Font[] envFonts = geLocal.getAllFonts();
        for(Font f : envFonts){
            if(dcbmFonts.getIndexOf(f.getFamily())==-1){
                dcbmFonts.addElement(f.getFamily());
            }
        }
        
        if(localeLanguage.getValueOf("titleOPD")!=null){setTitle(localeLanguage.getValueOf("titleOPD"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnChangeFont.setText(localeLanguage.getValueOf("buttonChange"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnCodeEditor.setText(localeLanguage.getValueOf("buttonChange"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnChangeBGImage.setText(localeLanguage.getValueOf("buttonChange"));}
        if(localeLanguage.getValueOf("buttonAppTheme")!=null){btnApplyTheme.setText(localeLanguage.getValueOf("buttonAppTheme"));}
        if(localeLanguage.getValueOf("buttonAdd")!=null){btnAddFont.setText(localeLanguage.getValueOf("buttonAdd"));}
        if(localeLanguage.getValueOf("buttonEdit")!=null){btnEditFont.setText(localeLanguage.getValueOf("buttonEdit"));}
        if(localeLanguage.getValueOf("buttonRemove")!=null){btnRemFont.setText(localeLanguage.getValueOf("buttonRemove"));}
        if(localeLanguage.getValueOf("labelUniFont")!=null){lblFont.setText(localeLanguage.getValueOf("labelUniFont"));}
        if(localeLanguage.getValueOf("labelVideoSize")!=null){lblVideoSize.setText(localeLanguage.getValueOf("labelVideoSize"));}        
        if(localeLanguage.getValueOf("labelODTheme")!=null){lblTheme.setText(localeLanguage.getValueOf("labelODTheme"));}
        if(localeLanguage.getValueOf("labelODBackImage")!=null){lblBackgroundImage.setText(localeLanguage.getValueOf("labelODBackImage"));}
        if(localeLanguage.getValueOf("labelODActivate")!=null){lblActivation.setText(localeLanguage.getValueOf("labelODActivate"));}
        if(localeLanguage.getValueOf("labelODLaunch")!=null){lblLaunch.setText(localeLanguage.getValueOf("labelODLaunch"));}
        if(localeLanguage.getValueOf("labelODExternalEditor")!=null){lblExternalEditor.setText(localeLanguage.getValueOf("labelODExternalEditor"));}
        if(localeLanguage.getValueOf("labelODInst1")!=null){lblInstruction1.setText(localeLanguage.getValueOf("labelODInst1"));}
        if(localeLanguage.getValueOf("labelODInst2")!=null){lblInstruction2.setText(localeLanguage.getValueOf("labelODInst2"));}
        if(localeLanguage.getValueOf("labelODInst3")!=null){lblInstruction3.setText(localeLanguage.getValueOf("labelODInst3"));}
        if(localeLanguage.getValueOf("labelPBFont1")!=null){lblPBFont1.setText(localeLanguage.getValueOf("labelPBFont1"));}
        if(localeLanguage.getValueOf("labelPBFont2")!=null){lblPBFont2.setText(localeLanguage.getValueOf("labelPBFont2"));}
        if(localeLanguage.getValueOf("tabODMain")!=null){jTabbedPane1.setTitleAt(0,localeLanguage.getValueOf("tabODMain"));}
        if(localeLanguage.getValueOf("tabODKaraoke")!=null){jTabbedPane1.setTitleAt(1,localeLanguage.getValueOf("tabODKaraoke"));}
        if(localeLanguage.getValueOf("tabODCodeEditor")!=null){jTabbedPane1.setTitleAt(2,localeLanguage.getValueOf("tabODCodeEditor"));}
        if(localeLanguage.getValueOf("tabODTranslation")!=null){jTabbedPane1.setTitleAt(3,localeLanguage.getValueOf("tabODTranslation"));}
        tb = (javax.swing.border.TitledBorder)jPanel7.getBorder();
        if(localeLanguage.getValueOf("tbdODFontsPB")!=null){tb.setTitle(localeLanguage.getValueOf("tbdODFontsPB"));}
        if(localeLanguage.getValueOf("checkboxForceISO")!=null){cbForceLanguage.setText(localeLanguage.getValueOf("checkboxForceISO"));}
        if(localeLanguage.getValueOf("buttonSave")!=null){btnTranslateSave.setText(localeLanguage.getValueOf("buttonSave"));}
        if(localeLanguage.getValueOf("optpTitle4")!=null){optTitle4 = localeLanguage.getValueOf("optpTitle4");}
        if(localeLanguage.getValueOf("optpMessage6")!=null){optMessage6 = localeLanguage.getValueOf("optpMessage6");}
        if(localeLanguage.getValueOf("optpMessage7")!=null){optMessage7 = localeLanguage.getValueOf("optpMessage7");}
        if(localeLanguage.getValueOf("checkboxODKaraModule")!=null){cbKaraModule.setText(localeLanguage.getValueOf("checkboxODKaraModule"));}
        if(localeLanguage.getValueOf("checkboxODCodeModule")!=null){cbCodeModule.setText(localeLanguage.getValueOf("checkboxODCodeModule"));}
        if(localeLanguage.getValueOf("checkboxODDrawModule")!=null){cbDrawModule.setText(localeLanguage.getValueOf("checkboxODDrawModule"));}
        if(localeLanguage.getValueOf("checkboxODAnalModule")!=null){cbAnalModule.setText(localeLanguage.getValueOf("checkboxODAnalModule"));}
        if(localeLanguage.getValueOf("enumODWelc")!=null){SWModule.WELCOME.setDisplay(localeLanguage.getValueOf("enumODWelc"));}
        if(localeLanguage.getValueOf("enumODKara")!=null){SWModule.KARAOKE.setDisplay(localeLanguage.getValueOf("enumODKara"));}
        if(localeLanguage.getValueOf("enumODCode")!=null){SWModule.CODEEDITOR.setDisplay(localeLanguage.getValueOf("enumODCode"));}
        if(localeLanguage.getValueOf("enumODDraw")!=null){SWModule.DRAWEDITOR.setDisplay(localeLanguage.getValueOf("enumODDraw"));}
        if(localeLanguage.getValueOf("enumODAnal")!=null){SWModule.ANALYSIS.setDisplay(localeLanguage.getValueOf("enumODAnal"));}
        for (int i = 0; i < 2; i++) {
            column = tableFonts.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    if(localeLanguage.getValueOf("tableFont")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableFont"));
                    }
                    break;
                case 1:
                    if(localeLanguage.getValueOf("tableCorrection")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableCorrection"));
                    }
                    break;
            }
        }
        for (int i = 0; i < 2; i++) {
            column = tableTranslate.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    if(localeLanguage.getValueOf("tableTKey")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableTKey"));
                    }
                    break;
                case 1:
                    if(localeLanguage.getValueOf("tableTValue")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableTValue"));
                    }
                    break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcOptions = new javax.swing.JFileChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        cbForceLanguage = new javax.swing.JCheckBox();
        cbChooseForced = new javax.swing.JComboBox();
        lblTheme = new javax.swing.JLabel();
        cbTheme = new javax.swing.JComboBox();
        btnApplyTheme = new javax.swing.JButton();
        lblBackgroundImage = new javax.swing.JLabel();
        tfBGImage = new javax.swing.JTextField();
        btnChangeBGImage = new javax.swing.JButton();
        lblInstruction1 = new javax.swing.JLabel();
        lblActivation = new javax.swing.JLabel();
        cbKaraModule = new javax.swing.JCheckBox();
        cbCodeModule = new javax.swing.JCheckBox();
        cbDrawModule = new javax.swing.JCheckBox();
        cbAnalModule = new javax.swing.JCheckBox();
        lblInstruction2 = new javax.swing.JLabel();
        lblLaunch = new javax.swing.JLabel();
        cbStartWith = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        lblFont = new javax.swing.JLabel();
        tfUnicodeFont = new javax.swing.JTextField();
        btnChangeFont = new javax.swing.JButton();
        lblVideoSize = new javax.swing.JLabel();
        tfWidth = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfHeight = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        lblPBFont1 = new javax.swing.JLabel();
        lblPBFont2 = new javax.swing.JLabel();
        lblPercent = new javax.swing.JLabel();
        cbFonts = new javax.swing.JComboBox();
        sldFontCorrection = new javax.swing.JSlider();
        btnAddFont = new javax.swing.JButton();
        btnEditFont = new javax.swing.JButton();
        btnRemFont = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableFonts = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        lblExternalEditor = new javax.swing.JLabel();
        btnCodeEditor = new javax.swing.JButton();
        tfCodeEditor = new javax.swing.JTextField();
        lblInstruction3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTranslate = new javax.swing.JTable();
        cbTranslate = new javax.swing.JComboBox();
        btnTranslateSave = new javax.swing.JButton();
        OK_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cbForceLanguage.setText("Forcer le langage à :");
        cbForceLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbForceLanguageActionPerformed(evt);
            }
        });

        cbChooseForced.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblTheme.setText("Theme :");

        cbTheme.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnApplyTheme.setText("Appliquer");
        btnApplyTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyThemeActionPerformed(evt);
            }
        });

        lblBackgroundImage.setText("Image de fond :");

        btnChangeBGImage.setText("Changer...");
        btnChangeBGImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeBGImageActionPerformed(evt);
            }
        });

        lblInstruction1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblInstruction1.setForeground(new java.awt.Color(255, 0, 51));
        lblInstruction1.setText("Pour appliquer la traduction, relancez le logiciel.");

        lblActivation.setText("Activation des modules :");

        cbKaraModule.setSelected(true);
        cbKaraModule.setText("Karaoké");

        cbCodeModule.setSelected(true);
        cbCodeModule.setText("Code");

        cbDrawModule.setSelected(true);
        cbDrawModule.setText("Dessin");

        cbAnalModule.setSelected(true);
        cbAnalModule.setText("Analyse");

        lblInstruction2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblInstruction2.setForeground(new java.awt.Color(255, 0, 0));
        lblInstruction2.setText("Les modules sélectionnés seront disponible au lancement, relancez le logiciel.");

        lblLaunch.setText("Au lancement, voir :");

        cbStartWith.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTheme, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblBackgroundImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfBGImage)
                                    .addComponent(cbTheme, 0, 277, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnApplyTheme, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnChangeBGImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cbForceLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbChooseForced, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblActivation, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbKaraModule, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(cbCodeModule, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(cbDrawModule, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(cbAnalModule)))
                                .addGap(0, 2, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblLaunch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbStartWith, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblInstruction2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblInstruction1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbForceLanguage)
                    .addComponent(cbChooseForced, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInstruction1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblTheme))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbTheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnApplyTheme))))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblBackgroundImage))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnChangeBGImage)
                        .addComponent(tfBGImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblActivation)
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbKaraModule)
                            .addComponent(cbCodeModule)
                            .addComponent(cbDrawModule)
                            .addComponent(cbAnalModule))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(lblInstruction2)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLaunch, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbStartWith, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(89, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Général", jPanel1);

        lblFont.setText("Police Unicode :");

        btnChangeFont.setText("Changer...");
        btnChangeFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeFontActionPerformed(evt);
            }
        });

        lblVideoSize.setText("Taille de la vidéo :");

        tfWidth.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfWidth.setText("1280");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("x");

        tfHeight.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfHeight.setText("720");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Police à problèmes"));

        lblPBFont1.setText("Police :");

        lblPBFont2.setText("Correction :");

        lblPercent.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPercent.setText("100 %");

        cbFonts.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        sldFontCorrection.setValue(100);
        sldFontCorrection.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldFontCorrectionStateChanged(evt);
            }
        });

        btnAddFont.setText("Ajouter");
        btnAddFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFontActionPerformed(evt);
            }
        });

        btnEditFont.setText("Editer");
        btnEditFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditFontActionPerformed(evt);
            }
        });

        btnRemFont.setText("Supprimer");
        btnRemFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemFontActionPerformed(evt);
            }
        });

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableFonts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Police", "Correction"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableFonts);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbFonts, 0, 134, Short.MAX_VALUE)
                            .addComponent(lblPBFont1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblPBFont2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPercent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(sldFontCorrection, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddFont, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditFont, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemFont)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRemFont, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEditFont, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAddFont, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPBFont1)
                            .addComponent(lblPBFont2)
                            .addComponent(lblPercent))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbFonts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sldFontCorrection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblFont)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfUnicodeFont, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChangeFont, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblVideoSize)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFont)
                    .addComponent(tfUnicodeFont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChangeFont))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVideoSize)
                    .addComponent(tfWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Karaoké", jPanel2);

        lblExternalEditor.setText("Editeur externe :");

        btnCodeEditor.setText("Changer...");
        btnCodeEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodeEditorActionPerformed(evt);
            }
        });

        lblInstruction3.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        lblInstruction3.setForeground(new java.awt.Color(255, 51, 51));
        lblInstruction3.setText("Pour pouvoir ouvrir un script dans un autre logiciel, rajoutez %FILE à votre ligne de commande.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInstruction3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblExternalEditor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfCodeEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCodeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExternalEditor)
                    .addComponent(btnCodeEditor)
                    .addComponent(tfCodeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInstruction3)
                .addContainerGap(224, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Editeur de code", jPanel3);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableTranslate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clé", "Valeur"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableTranslate.setRowHeight(32);
        jScrollPane1.setViewportView(tableTranslate);

        cbTranslate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTranslateSave.setText("Sauver");
        btnTranslateSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTranslateSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cbTranslate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTranslateSave, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTranslate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTranslateSave))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Traduction", jPanel6);

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });

        Cancel_Button.setText("Annuler");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(OK_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        // Cancel
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void cbForceLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbForceLanguageActionPerformed
        if(cbForceLanguage.isSelected()){
            cbChooseForced.setEnabled(true);
        }else{
            cbChooseForced.setEnabled(false);
        }
    }//GEN-LAST:event_cbForceLanguageActionPerformed

    private void btnApplyThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyThemeActionPerformed
        Theme th = (Theme)dcbmTheme.getSelectedItem();
        smallboxforfansub.MainFrame.changeTheme(th);
        javax.swing.SwingUtilities.updateComponentTreeUI(frame);
        javax.swing.SwingUtilities.updateComponentTreeUI(this);
        try {//Force to redraw
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
    }//GEN-LAST:event_btnApplyThemeActionPerformed

    private void btnChangeBGImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeBGImageActionPerformed
        for (FileFilter ff : fcOptions.getChoosableFileFilters()){
            fcOptions.removeChoosableFileFilter(ff);
        }
        fcOptions.addChoosableFileFilter(new ImageFilter());
        fcOptions.setAccessory(new ImagePreview(fcOptions));
        int z = fcOptions.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            tfBGImage.setText(fcOptions.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_btnChangeBGImageActionPerformed

    private void btnChangeFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeFontActionPerformed
        for (FileFilter ff : fcOptions.getChoosableFileFilters()){
            fcOptions.removeChoosableFileFilter(ff);
        }
        fcOptions.addChoosableFileFilter(new TTFFilter());
        fcOptions.setAccessory(null);
        int z = fcOptions.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            tfUnicodeFont.setText(fcOptions.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_btnChangeFontActionPerformed

    private void btnAddFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFontActionPerformed
        String sfont = dcbmFonts.getSelectedItem().toString();
        int percent = sldFontCorrection.getValue();
        dtmFonts.addRow(new Object[]{sfont,percent});
    }//GEN-LAST:event_btnAddFontActionPerformed

    private void btnEditFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditFontActionPerformed
        if(tableFonts.getSelectedRow()!=-1){
            String sfont = dcbmFonts.getSelectedItem().toString();
            int percent = sldFontCorrection.getValue();
            tableFonts.setValueAt(sfont, tableFonts.getSelectedRow(), 0);
            tableFonts.setValueAt(percent, tableFonts.getSelectedRow(), 1);
        }
    }//GEN-LAST:event_btnEditFontActionPerformed

    private void btnRemFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemFontActionPerformed
        try{
            int tabtemp[] = tableFonts.getSelectedRows();
            for (int i=tabtemp.length-1;i>=0;i--){
                dtmFonts.removeRow(tabtemp[i]);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_btnRemFontActionPerformed

    private void sldFontCorrectionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldFontCorrectionStateChanged
        lblPercent.setText(sldFontCorrection.getValue()+" %");
    }//GEN-LAST:event_sldFontCorrectionStateChanged

    private void btnCodeEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodeEditorActionPerformed
        for (FileFilter ff : fcOptions.getChoosableFileFilters()){
            fcOptions.removeChoosableFileFilter(ff);
        }
        fcOptions.addChoosableFileFilter(new ExeJarFilter());
        fcOptions.addChoosableFileFilter(new AnyFilter());
        fcOptions.setAccessory(null);
        int z = fcOptions.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            tfCodeEditor.setText(fcOptions.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_btnCodeEditorActionPerformed

    private void btnTranslateSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTranslateSaveActionPerformed
        for(int i=0;i<tableTranslate.getRowCount();i++){
            String key = tableTranslate.getValueAt(i, 0).toString();
            String value = tableTranslate.getValueAt(i, 1).toString();
            translateMap.put(key, value);
        }
        Language.ISO_3166 iso = (Language.ISO_3166)cbTranslate.getSelectedItem();
        XmlLangWriter xlw = new XmlLangWriter();
        xlw.setLangMap(translateMap);
        File file = new File(DOCSPATH+iso.getAlpha3()+".lang");
        boolean createFile = true;
        if(file.exists()){
            int a = JOptionPane.showConfirmDialog(this, optMessage6, optTitle4, JOptionPane.YES_NO_OPTION);
            if(a==JOptionPane.NO_OPTION){
                createFile = false;
            }
        }
        if(createFile == true){
            xlw.createLang(file.getAbsolutePath());
            JOptionPane.showMessageDialog(this, optMessage7);
        }
    }//GEN-LAST:event_btnTranslateSaveActionPerformed

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
    
    /** <p>Set the selected theme.<br />
     * Définit le thème sélectionné.</p> */
    public void setSelectedTheme(Theme th){
        dcbmTheme.setSelectedItem(th);
    }
    
    public Theme getSelectedTheme(){
        return (Theme)dcbmTheme.getSelectedItem();
    }
    
    /** <p>Set the video width.<br />
     * Définit la largeur de la vidéo.</p> */
    public void setVideoWidth(int vw){
        tfWidth.setText(vw+"");
    }
    
    /** <p>Get the video width.<br />
     * Obtient la largeur de la vidéo.</p> */
    public int getVideoWidth(){
        return Integer.parseInt(tfWidth.getText());
    }

    /** <p>Set the video height.<br />
     * Définit la hauteur de la vidéo.</p> */
    public void setVideoHeight(int vh){
        tfHeight.setText(vh+"");
    }

    /** <p>Get the video height.<br />
     * Obtient la hauteur de la vidéo.</p> */
    public int getVideoHeight(){
        return Integer.parseInt(tfHeight.getText());
    }
    
    public void setForceLanguage(String language){
        force_ISO = language;
        if(force_ISO.equalsIgnoreCase("---")==false){
            cbForceLanguage.setSelected(true);
            cbChooseForced.setEnabled(true);
        }else{
            cbForceLanguage.setSelected(false);
            cbChooseForced.setEnabled(false);
        }
        dcbmChooseForced.setSelectedItem(Language.getFromCode(force_ISO));
    }
    
    public String getForceLanguage(){
        if(cbForceLanguage.isSelected()==true){
            return ((Language.ISO_3166)cbChooseForced.getSelectedItem()).getAlpha3();
        }else{
            return "---";
        }
    }
    
    public void setBGImage(String BGImage){
        tfBGImage.setText(BGImage);
    }
    
    public String getBGImage(){
        return tfBGImage.getText();
    }
    
    public void setKaraModule(boolean b){
        cbKaraModule.setSelected(b);
    }
    
    public boolean getKaraModule(){
        return cbKaraModule.isSelected();
    }
    
    public String getKaraModuleString(){
        if(cbKaraModule.isSelected()==true){
            return "yes";
        }else{
            return "no";
        }
    }
    
    public void setCodeModule(boolean b){
        cbCodeModule.setSelected(b);
    }
    
    public boolean getCodeModule(){
        return cbCodeModule.isSelected();
    }
    
    public String getCodeModuleString(){
        if(cbCodeModule.isSelected()==true){
            return "yes";
        }else{
            return "no";
        }
    }
    
    public void setDrawModule(boolean b){
        cbDrawModule.setSelected(b);
    }
    
    public boolean getDrawModule(){
        return cbDrawModule.isSelected();
    }
    
    public String getDrawModuleString(){
        if(cbDrawModule.isSelected()==true){
            return "yes";
        }else{
            return "no";
        }
    }
    
    public void setAnalModule(boolean b){
        cbAnalModule.setSelected(b);
    }
    
    public boolean getAnalModule(){
        return cbAnalModule.isSelected();
    }
    
    public String getAnalModuleString(){
        if(cbAnalModule.isSelected()==true){
            return "yes";
        }else{
            return "no";
        }
    }
    
    public void setStartWith(String code){
        if(code.equalsIgnoreCase("kara")){
            dcbmStartWith.setSelectedItem(SWModule.KARAOKE);
        }else if(code.equalsIgnoreCase("code")){
            dcbmStartWith.setSelectedItem(SWModule.CODEEDITOR);
        }else if(code.equalsIgnoreCase("draw")){
            dcbmStartWith.setSelectedItem(SWModule.DRAWEDITOR);
        }else if(code.equalsIgnoreCase("anal")){
            dcbmStartWith.setSelectedItem(SWModule.ANALYSIS);
        }else{
            dcbmStartWith.setSelectedItem(SWModule.WELCOME);
        }
    }
    
    public String getStartWith(){
        SWModule swm = (SWModule)dcbmStartWith.getSelectedItem();
        return swm.getCode();
    }
    
    public void setUnicodeFont(String path){
        tfUnicodeFont.setText(path);
    }
    
    public String getUnicodeFont(){
        return tfUnicodeFont.getText();
    }
    
    public void setProblemFont(List<FontWithCoef> list){
        for(FontWithCoef fwc : list){
            int value = Integer.parseInt(fwc.getCoefInPercent().replace(".0", ""));
            dtmFonts.addRow(new Object[]{fwc.getFontName(),value});
        }
    }
    
    public List<FontWithCoef> getProblemFont(){
        List<FontWithCoef> list = new ArrayList<>();
        for(int i=0;i<dtmFonts.getRowCount();i++){
            String sfont = dtmFonts.getValueAt(i, 0).toString();
            int percent = (Integer)dtmFonts.getValueAt(i, 1);
            FontWithCoef fwc = new FontWithCoef(new Font(sfont, Font.PLAIN, 12), percent);
            list.add(fwc);
        }
        return list;
    }
    
    public void setCodeEditor(String path){
        tfCodeEditor.setText(path);
    }
    
    public String getCodeEditor(){
        return tfCodeEditor.getText();
    }
    
    public void setDocsPath(String path){
        DOCSPATH = path;
    }
    
    private Map sortByComparator(Map map) {
        
        List list = new LinkedList(map.entrySet());
 
        //sort list based on comparator
        Collections.sort(list, new Comparator() {
            @Override
             public int compare(Object o1, Object o2) {
	           return ((Comparable) ((Map.Entry) (o1)).getKey())
	           .compareTo(((Map.Entry) (o2)).getKey());
             }
	});
 
        //put sorted list into map again
	Map sortedMap = new LinkedHashMap();
	for (Iterator it = list.iterator(); it.hasNext();) {
	     Map.Entry entry = (Map.Entry)it.next();
	     sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
        
   }
    
    public final void setTranslationTable(){
        translateMap = localeLanguage.getLocaleMap();
        translateMap = sortByComparator(translateMap);
        tableTranslate.removeAll();
        for (String s : translateMap.keySet()){            
            dtmTranslate.addRow(new Object[]{s,translateMap.get(s)});
        }
    }
    
    public void setTranslationTable(Map<String, String> map){
        map = sortByComparator(map);
        tableTranslate.removeAll();
        for (String s : map.keySet()){            
            dtmTranslate.addRow(new Object[]{s,map.get(s)});
        }
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OptionsDialog2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                OptionsDialog2 dialog = new OptionsDialog2(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAddFont;
    private javax.swing.JButton btnApplyTheme;
    private javax.swing.JButton btnChangeBGImage;
    private javax.swing.JButton btnChangeFont;
    private javax.swing.JButton btnCodeEditor;
    private javax.swing.JButton btnEditFont;
    private javax.swing.JButton btnRemFont;
    private javax.swing.JButton btnTranslateSave;
    private javax.swing.JCheckBox cbAnalModule;
    private javax.swing.JComboBox cbChooseForced;
    private javax.swing.JCheckBox cbCodeModule;
    private javax.swing.JCheckBox cbDrawModule;
    private javax.swing.JComboBox cbFonts;
    private javax.swing.JCheckBox cbForceLanguage;
    private javax.swing.JCheckBox cbKaraModule;
    private javax.swing.JComboBox cbStartWith;
    private javax.swing.JComboBox cbTheme;
    private javax.swing.JComboBox cbTranslate;
    private javax.swing.JFileChooser fcOptions;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblActivation;
    private javax.swing.JLabel lblBackgroundImage;
    private javax.swing.JLabel lblExternalEditor;
    private javax.swing.JLabel lblFont;
    private javax.swing.JLabel lblInstruction1;
    private javax.swing.JLabel lblInstruction2;
    private javax.swing.JLabel lblInstruction3;
    private javax.swing.JLabel lblLaunch;
    private javax.swing.JLabel lblPBFont1;
    private javax.swing.JLabel lblPBFont2;
    private javax.swing.JLabel lblPercent;
    private javax.swing.JLabel lblTheme;
    private javax.swing.JLabel lblVideoSize;
    private javax.swing.JSlider sldFontCorrection;
    private javax.swing.JTable tableFonts;
    private javax.swing.JTable tableTranslate;
    private javax.swing.JTextField tfBGImage;
    private javax.swing.JTextField tfCodeEditor;
    private javax.swing.JTextField tfHeight;
    private javax.swing.JTextField tfUnicodeFont;
    private javax.swing.JTextField tfWidth;
    // End of variables declaration//GEN-END:variables
}
