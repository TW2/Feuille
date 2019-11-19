/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import feuille.codeeditor.CodeEditorPanel;
import feuille.filter.AssFilter;
import feuille.filter.SubtitleFilter;
import feuille.karaoke.audio.WavePanel;
import feuille.karaoke.dialog.AssStylesDialog;
import feuille.karaoke.dialog.XmlPresetDialog;
import feuille.karaoke.dialog.ParticleDialog;
import feuille.karaoke.dialog.XmlPExportDialog;
import feuille.filter.WavFilter;
import feuille.filter.XmlPresetFilter;
import feuille.karaoke.renderer.resultTableRenderer;
import feuille.karaoke.renderer.tableTypeRenderer;
import feuille.karaoke.renderer.treeFxRenderer;
import feuille.karaoke.lib.AssIO;
import feuille.karaoke.lib.AssNameCollection;
import feuille.karaoke.lib.AssStyle;
import feuille.karaoke.lib.EncodingPanel;
import feuille.karaoke.lib.FxObject;
import feuille.karaoke.lib.FxoLines;
import feuille.lib.Language;
import feuille.karaoke.lib.ParticleObject;
import feuille.karaoke.lib.ProgramLine;
import feuille.karaoke.lib.Time;
import feuille.karaoke.lib.XmlStylesPackWriter;
import feuille.karaoke.plugins.FunctionsCollection;
import feuille.scripting.ScriptPlugin;

/**
 *
 * @author The Wingate 2940
 */
public class KaraokePanel extends javax.swing.JPanel {
    
    static DefaultTableModel orgModel;
    tableTypeRenderer ttr;
    static DefaultTableModel resModel;
    resultTableRenderer rtr;
    Font fontToUse = null;
    // lastSelRow notice the current row to treat with "ForOneLine"
    // function of XML FxObjects - (Do not delete this variable)
    static int lastSelRow = -1;
    // getSelRows notice the rows to treat with "ForFewLine" function
    // of XML FxObjects - (Do not delete this variable)
    static int[] getSelRows = null;
    static ScriptPlugin splug;
    FunctionsCollection funcc = new FunctionsCollection();
    static DefaultMutableTreeNode tnRoot, tnRuby, tnXML, tnPart;
    static Language localeLanguage;
    String docs;
    Frame frame;
    AssNameCollection anc;
    String drawingEditor = "", rubyEditor = "";
    
    //Variables for ruby scripting (in xfx or particle)
    static String karaokeStart, karaokeEnd, karaokeDuration,
            karaokeOriStart, karaokeOriEnd, karaokeOriDuration,
            karaokeSentenceDuration;
    
    // Variables for the audio
    WavePanel wp = new WavePanel();
    boolean hasWaveForm = false;
    int xSidebar = 0;
    java.io.File waveformImageDirectory = null;

    /**
     * Creates new form KaraokePanel
     */
    public KaraokePanel(String docs, Font fontToUse, Language lang, Frame frame) {
        initComponents();        
        init(docs, fontToUse, lang);
        this.docs = docs;
        this.frame = frame;
    }

    private void init(String docs, Font fontToUse, Language lang){
        
        // 1. Langue
        localeLanguage = lang;
        setLanguageAndConfigure(lang);
        
        // 2. Tableau
        String[] orgHead = new String[]{"N°", "T", "C", "Marg.", "Début", "Fin",
                "Total", "Style", "Nom", "Effet", "Texte", "FX"};
        
        if(localeLanguage.getValueOf("tableShortNumber")!=null){
            orgHead[0] = localeLanguage.getValueOf("tableShortNumber");}
        if(localeLanguage.getValueOf("tableShortType")!=null){
            orgHead[1] = localeLanguage.getValueOf("tableShortType");}
        if(localeLanguage.getValueOf("tableShortLayer")!=null){
            orgHead[2] = localeLanguage.getValueOf("tableShortLayer");}
        if(localeLanguage.getValueOf("tableMargin")!=null){
            orgHead[3] = localeLanguage.getValueOf("tableMargin");}
        if(localeLanguage.getValueOf("tableStart")!=null){
            orgHead[4] = localeLanguage.getValueOf("tableStart");}
        if(localeLanguage.getValueOf("tableEnd")!=null){
            orgHead[5] = localeLanguage.getValueOf("tableEnd");}
        if(localeLanguage.getValueOf("tableTotal")!=null){
            orgHead[6] = localeLanguage.getValueOf("tableTotal");}
        if(localeLanguage.getValueOf("tableStyle")!=null){
            orgHead[7] = localeLanguage.getValueOf("tableStyle");}
        if(localeLanguage.getValueOf("tableName")!=null){
            orgHead[8] = localeLanguage.getValueOf("tableName");}
        if(localeLanguage.getValueOf("tableEffect")!=null){
            orgHead[9] = localeLanguage.getValueOf("tableEffect");}
        if(localeLanguage.getValueOf("tableText")!=null){
            orgHead[10] = localeLanguage.getValueOf("tableText");}
        if(localeLanguage.getValueOf("tableFX")!=null){
            orgHead[11] = localeLanguage.getValueOf("tableFX");}
        
        orgModel = new DefaultTableModel(
                null,
                orgHead
        ){
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                    false, true, true,
                    true, true, true,
                    true, true, true,
                    true, true, false};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        
        orgTable.setModel(orgModel);
        
        TableColumn column;
        for (int i = 0; i < 12; i++) {
            column = orgTable.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(30);
                    column.setIdentifier(Column.ID.getId());
                    break; //ID
                case 1:
                    column.setPreferredWidth(30);
//                    column.setCellEditor(new DefaultCellEditor(timeType));
                    column.setIdentifier(Column.TYPE.getId());
                    break; //Type
                case 2:
                    column.setPreferredWidth(30);
                    column.setIdentifier(Column.LAYER.getId());
                    break; //Layer
                case 3:
                    column.setPreferredWidth(60);
                    column.setIdentifier(Column.MARGINS.getId());
                    break; //Margins
                case 4:
                    column.setPreferredWidth(90);
                    column.setIdentifier(Column.START.getId());
                    break; //Start
                case 5:
                    column.setPreferredWidth(90);
                    column.setIdentifier(Column.END.getId());
                    break; //End
                case 6:
                    column.setPreferredWidth(90);
                    column.setIdentifier(Column.TOTAL.getId());
                    break; //Total time
                case 7:
                    column.setPreferredWidth(80);
//                    column.setCellEditor(new DefaultCellEditor(timeStyle));
                    column.setIdentifier(Column.STYLE.getId());
                    break; //Style
                case 8:
                    column.setPreferredWidth(80);
//                    column.setCellEditor(new DefaultCellEditor(timeName));
                    column.setIdentifier(Column.NAME.getId());
                    break; //Name
                case 9:
                    column.setPreferredWidth(20); 
                    column.setIdentifier(Column.EFFECTS.getId());
                    break; //Effects
                case 10:
                    column.setPreferredWidth(700); 
                    column.setIdentifier(Column.TEXT.getId());
                    break; //Text
                case 11:
                    column.setPreferredWidth(30); 
                    column.setIdentifier(Column.FX.getId());
                    break; //FX
            }
        }
        
        ttr = new tableTypeRenderer(Color.white, Color.black);
        ttr.setTextColor(1,Color.black); //Texte Karaoke
        ttr.setBackColor(1,new Color(244,247,194,255)); //Fond Karaoke
        ttr.setTextColor(2,Color.black); //Texte Comment
        ttr.setBackColor(2,new Color(215,244,191,255)); //Fond Comment
        ttr.setFont(fontToUse);
        ttr.setTextType(feuille.karaoke.renderer.tableTypeRenderer.TextType.Normal);
        orgTable.setDefaultRenderer(String.class, ttr);
        
        String[] resHead = new String[]{"N°", "T", "C", "Marg.", "Début", "Fin",
                "Total", "Style", "Nom", "Effet", "Texte", "FX"};
        
         if(localeLanguage.getValueOf("tableShortNumber")!=null){
            resHead[0] = localeLanguage.getValueOf("tableShortNumber");}
        if(localeLanguage.getValueOf("tableShortType")!=null){
            resHead[1] = localeLanguage.getValueOf("tableShortType");}
        if(localeLanguage.getValueOf("tableShortLayer")!=null){
            resHead[2] = localeLanguage.getValueOf("tableShortLayer");}
        if(localeLanguage.getValueOf("tableMargin")!=null){
            resHead[3] = localeLanguage.getValueOf("tableMargin");}
        if(localeLanguage.getValueOf("tableStart")!=null){
            resHead[4] = localeLanguage.getValueOf("tableStart");}
        if(localeLanguage.getValueOf("tableEnd")!=null){
            resHead[5] = localeLanguage.getValueOf("tableEnd");}
        if(localeLanguage.getValueOf("tableTotal")!=null){
            resHead[6] = localeLanguage.getValueOf("tableTotal");}
        if(localeLanguage.getValueOf("tableStyle")!=null){
            resHead[7] = localeLanguage.getValueOf("tableStyle");}
        if(localeLanguage.getValueOf("tableName")!=null){
            resHead[8] = localeLanguage.getValueOf("tableName");}
        if(localeLanguage.getValueOf("tableEffect")!=null){
            resHead[9] = localeLanguage.getValueOf("tableEffect");}
        if(localeLanguage.getValueOf("tableText")!=null){
            resHead[10] = localeLanguage.getValueOf("tableText");}
        if(localeLanguage.getValueOf("tableFX")!=null){
            resHead[11] = localeLanguage.getValueOf("tableFX");}
        
        resModel = new DefaultTableModel(
                null,
                resHead
        ){
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                    false, true, true,
                    true, true, true,
                    true, true, true,
                    true, true, false};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        
        resTable.setModel(resModel);
        
        for (int i = 0; i < 12; i++) {
            column = resTable.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(30);
                    column.setIdentifier(Column.ID.getId());
                    break; //ID
                case 1:
                    column.setPreferredWidth(30);
//                    column.setCellEditor(new DefaultCellEditor(timeType));
                    column.setIdentifier(Column.TYPE.getId());
                    break; //Type
                case 2:
                    column.setPreferredWidth(30);
                    column.setIdentifier(Column.LAYER.getId());
                    break; //Layer
                case 3:
                    column.setPreferredWidth(60);
                    column.setIdentifier(Column.MARGINS.getId());
                    break; //Margins
                case 4:
                    column.setPreferredWidth(90);
                    column.setIdentifier(Column.START.getId());
                    break; //Start
                case 5:
                    column.setPreferredWidth(90);
                    column.setIdentifier(Column.END.getId());
                    break; //End
                case 6:
                    column.setPreferredWidth(90);
                    column.setIdentifier(Column.TOTAL.getId());
                    break; //Total time
                case 7:
                    column.setPreferredWidth(80);
//                    column.setCellEditor(new DefaultCellEditor(timeStyle));
                    column.setIdentifier(Column.STYLE.getId());
                    break; //Style
                case 8:
                    column.setPreferredWidth(80);
//                    column.setCellEditor(new DefaultCellEditor(timeName));
                    column.setIdentifier(Column.NAME.getId());
                    break; //Name
                case 9:
                    column.setPreferredWidth(20);
                    column.setIdentifier(Column.EFFECTS.getId());
                    break; //Effects
                case 10:
                    column.setPreferredWidth(700);
                    column.setIdentifier(Column.TEXT.getId());
                    break; //Text
                case 11:
                    column.setPreferredWidth(30);
                    column.setIdentifier(Column.FX.getId());
                    break; //FX
            }
        }
        
        rtr = new resultTableRenderer(true);
        rtr.setFont(fontToUse);
        rtr.setTextType(feuille.karaoke.renderer.resultTableRenderer.TextType.Normal);
        resTable.setDefaultRenderer(String.class, rtr);
        
        String strFxList = localeLanguage.getValueOf("treeFxList")!=null ?
                    localeLanguage.getValueOf("treeFxList") :
                    "Fx list";
        String strScripts = localeLanguage.getValueOf("treeRubyScripts")!=null ?
                    localeLanguage.getValueOf("treeRubyScripts") :
                    "Scripts";
        String strXMLPr = localeLanguage.getValueOf("treeXMLPresets")!=null ?
                    localeLanguage.getValueOf("treeXMLPresets") :
                    "XML Presets";
        String strPart = localeLanguage.getValueOf("treeParticles")!=null ?
                    localeLanguage.getValueOf("treeParticles") :
                    "Particles";
        
        tnRoot = new DefaultMutableTreeNode(strFxList);
        tnRuby = new DefaultMutableTreeNode(strScripts);
        tnXML = new DefaultMutableTreeNode(strXMLPr);
        tnPart = new DefaultMutableTreeNode(strPart);
        DefaultTreeModel dtreem = new DefaultTreeModel(tnRoot);
        jTree1.setModel(dtreem);
        treeFxRenderer tfr = new treeFxRenderer();
        ImageIcon iiRuby = new ImageIcon(getClass().getResource("AFM-RubyScript.png"));
        tfr.setRubyIcon(iiRuby);
        ImageIcon iiXML = new ImageIcon(getClass().getResource("AFM-XFX.png"));
        tfr.setXmlIcon(iiXML);
        ImageIcon iiPart = new ImageIcon(getClass().getResource("AFM-SmallParticle.png"));
        tfr.setParticleIcon(iiPart);
        ImageIcon iiPyth = new ImageIcon(getClass().getResource("AFM-PythonScript.png"));
        tfr.setPythonIcon(iiPyth);
        jTree1.setCellRenderer(tfr);

        tnRoot.add(tnRuby);
        tnRoot.add(tnXML);
        tnRoot.add(tnPart);
        
        try {
            XmlPresetHandler xph = new XmlPresetHandler(docs+"afm-effects.xml");
            List<FxObject> xmlfxo = xph.getFxObjectList();
            for(FxObject f : xmlfxo){
                tnXML.add(new DefaultMutableTreeNode(f));
            }
        } catch (Exception ex) {
        }
        
        try{
            XmlParticleHandler xph = new XmlParticleHandler(docs+"particle.pxfx");
            List<ParticleObject> xpo = xph.getParticleObjectList();
            for(ParticleObject p : xpo){
                tnPart.add(new DefaultMutableTreeNode(p));
            }
        }catch(Exception exc){
        }
        
        expandAllFxTree(true);
        
        //Then create new links between fxo and images (for the preview)
        List<FxObject> listfxo = getFxObjectListFromFxTree();
        boolean imageExists = false;
        for(FxObject fxo : listfxo){
            if(fxo.getImage().isEmpty()==false){
                java.io.File ffxo = new java.io.File(fxo.getImage());
                if(ffxo.exists()){
                    imageExists = true;
                }                
            }
        }
        if(imageExists==false){ //First run
            for(FxObject fxo : listfxo){                
                if(fxo.getFxObjectType()==FxObject.FxObjectType.XMLPreset){
                    String strImage = docs+fxo.getName().toLowerCase()+".gif";
                    java.io.File ffxo = new java.io.File(strImage);
                    if(ffxo.exists()){
                        fxo.setImage(strImage); //Change image
                        modifyXmlPreset(new FxObject(), fxo, false); //Save
                    }                    
                }
            }
        }
        
        // X. Essaie de changer le look & feel pour Nimbus
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        // X. Chargements des méthodes pour les filtres
        funcc.setJavaPluginsDirPath(docs); // Pour l'ajout avec fichier 'TODO à refaire'
        funcc.addMember(new feuille.karaoke.plugins.LineLetterBasic());
        funcc.addMember(new feuille.karaoke.plugins.LineLetterPeriod());
        funcc.addMember(new feuille.karaoke.plugins.LineLetterRandom());
        funcc.addMember(new feuille.karaoke.plugins.LineLetterSym());
        funcc.addMember(new feuille.karaoke.plugins.LineSyllableBasic());
        funcc.addMember(new feuille.karaoke.plugins.LineSyllableComplex());
        funcc.addMember(new feuille.karaoke.plugins.LineSyllablePeriod());
        funcc.addMember(new feuille.karaoke.plugins.LineSyllableRandom());
        funcc.addMember(new feuille.karaoke.plugins.LineSyllableSym());
        funcc.addMember(new feuille.karaoke.plugins.SylSyllableBasic());
        
        // X. Chargement divers
        anc = new AssNameCollection("");
        realWavePanel.add(wp); wp.setSize(realWavePanel.getWidth(), 150);
        java.io.File file = new java.io.File(docs+File.separator+"WavPNG");
        if(file.exists()==false){
            file.mkdir();
        }
        waveformImageDirectory = file;
        
    }
    
    public void setCodeEditor(String codeEditor){
        rubyEditor = codeEditor;
    }
    
    /** Column est l'énumeration des noms de colonne de la table Original */
    public enum Column{
        ID(0), TYPE(1), LAYER(2), MARGINS(3), START(4),
        END(5), TOTAL(6), STYLE(7), NAME(8), EFFECTS(9),
        TEXT(10), FX(11);
        
        private int id;
        
        Column(int id){
            this.id = id;
        }
        
        /** <p>Return the id of the column.<br />
         * Retourne l'identifiant de la colonne.</p> */
        public int getId(){
            return id;
        }
    }
    
    public void setScriptPlugin(ScriptPlugin splug){
        KaraokePanel.splug = splug;
    }
    
    public JInternalFrame getOriginalTable(){
        return ifrOriginal;
    }
    
    public JInternalFrame getResultTable(){
        return ifrResult;
    }
    
    public JInternalFrame getTree(){
        return ifrTree;
    }
    
    public JInternalFrame getWaveform(){
        return ifrSound;
    }
    
    public static int getLastSelectedRow(){
        return lastSelRow;
    }
    
    public static int[] getSelectedRows(){
        return getSelRows;
    }
    
    public static String getKaraokeStart(){
        return karaokeStart;
    }
    
    public static String getKaraokeEnd(){
        return karaokeEnd;
    }
    
    public static String getKaraokeDuration(){
        return karaokeDuration;
    }
    
    public static String getKaraokeOStart(){
        return karaokeOriStart;
    }
    
    public static String getKaraokeOEnd(){
        return karaokeOriEnd;
    }
    
    public static String getKaraokeODuration(){
        return karaokeOriDuration;
    }
    
    public static String getKaraokeSDuration(){
        return karaokeSentenceDuration;
    }
    
    public static DefaultTableModel getOriginalTableModel(){
        return orgModel;
    }
    
    public static DefaultTableModel getResultTableModel(){
        return resModel;
    }
    
    public static DefaultMutableTreeNode getRubyTreeNode(){
        return tnRuby;
    }
    
    public static void updateTree(){
        jTree1.updateUI();
    }
    
    /** <p>Execute a function of an effect for one line. To complete the 
     * karaoke, for each line, this function will be recall.<br />
     * Execute la fonction d'un effet pour une ligne. Pour compléter le
     * karaoke, pour chaque ligne, la fonction sera rappellée.</p> */
    public void runFunctionAndDo(FxObject fxo){
        for(int i=0;i<funcc.getSize();i++){
            if(funcc.getMembers()[i].getPluginName().equals(fxo.getFunction())){
                funcc.getMembers()[i].setXMLPresetName(fxo.getName());
                funcc.getMembers()[i].setCommands(fxo.getCommands());
                funcc.getMembers()[i].setFirstLayer(fxo.getFirstLayer());
                funcc.getMembers()[i].setNbLayers(fxo.getNbLayers());
                funcc.getMembers()[i].setMoment(fxo.getMoment());
                funcc.getMembers()[i].setTime(fxo.getTime());
                funcc.getMembers()[i].setStyle(fxo.getStyle());
                funcc.getMembers()[i].setRubyCode(fxo.getRubyCode());
                funcc.getMembers()[i].forOneLine();
//                System.out.println("Result : "+funcc.getMembers()[i].forOneLine());
            }
        }
    }
    
    /** <p>Execute a function of an effect for few lines.<br />
     * Execute la fonction d'un effet pour quelques lignes.</p> */
    public void runFunctionAndDo2(FxObject fxo){
        for(int i=0;i<funcc.getSize();i++){
            if(funcc.getMembers()[i].getPluginName().equals(fxo.getFunction())){
                funcc.getMembers()[i].setXMLPresetName(fxo.getName());
                funcc.getMembers()[i].setCommands(fxo.getCommands());
                funcc.getMembers()[i].setFirstLayer(fxo.getFirstLayer());
                funcc.getMembers()[i].setNbLayers(fxo.getNbLayers());
                funcc.getMembers()[i].setMoment(fxo.getMoment());
                funcc.getMembers()[i].setTime(fxo.getTime());
                funcc.getMembers()[i].setStyle(fxo.getStyle());
                funcc.getMembers()[i].setRubyCode(fxo.getRubyCode());
                funcc.getMembers()[i].forFewLines();
            }
        }
    }
    
    public static String phRubyCode(String expression, String head, Object[][] syl,
            Object[][] osyl, int index, int crossIndex, String code){
        //Initialize variables for ruby scripting
        karaokeStart = syl[index][3].toString();
        karaokeEnd = syl[index][4].toString();
        karaokeDuration = syl[index][2].toString();
        if(osyl != null && Integer.valueOf(crossIndex) != -1){
            karaokeOriStart = osyl[crossIndex][3].toString();
            karaokeOriEnd = osyl[crossIndex][4].toString();
            karaokeOriDuration = osyl[crossIndex][2].toString();
        }
        karaokeSentenceDuration = getKaraokeSentenceDuration(head);

        //Ruby functions
        Pattern p = Pattern.compile("(\\$[a-z]+)");
        Matcher m = p.matcher(expression);
        List<String> var = new ArrayList<String>();
        while(m.find()){
            var.add(m.group(1));
        }
        for(String v : var){
            String function = v.substring(1);
            String value = splug.runFxCodeAndDo(code, function);
            expression = expression.replace(v, value);
        }
        return expression;
    }
    
    private static String getKaraokeSentenceDuration(String head){
        Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
        Matcher m = p.matcher(head);

        Time start = new Time();
        Time end = new Time();
        Time t = new Time();

        boolean endTime = false;
        while(m.find()){
            if (endTime == false){
                start.setHours(Integer.parseInt(m.group(1)));
                start.setMinutes(Integer.parseInt(m.group(2)));
                start.setSeconds(Integer.parseInt(m.group(3)));
                start.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }else{
                end.setHours(Integer.parseInt(m.group(1)));
                end.setMinutes(Integer.parseInt(m.group(2)));
                end.setSeconds(Integer.parseInt(m.group(3)));
                end.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }
            endTime = true;
        }

        t = t.substract(start, end);

        //%dP - during sentence
        return Long.toString(t.toMillisecondsTime(t));
    }
    
    private void runParticleAndDo(ParticleObject po){
        String line = ScriptPlugin.getSelectedOrgLine(); //Get the selected line

        String sentence = ScriptPlugin.getSentence(line); //Get the sentence for this line
        String head = ScriptPlugin.getHead(line); //Get the header for this line

        
        head = ScriptPlugin.phBeforeAfter(head, po.getMoment(), po.getTime()); //Try to transform the header with 'the moment'
        head = ScriptPlugin.phChangeLayer(head, po.getFirstLayer()); //Try to change the number of the first layer
        head = ScriptPlugin.phChangeStyle(head, po.getStyleName());
        
        Object[][] osyl = ScriptPlugin.phKaraoke(sentence); //Get a table of syllabe parameters for the basic karaoke
        
        AssStyle as = new AssStyle();
        as.fromAssStyleString(po.getStyle());
        
//        float posCor = Float.parseFloat(po.getPosCorrection());
        int vWidth = Integer.parseInt(po.getVideoWidth());
//        int spaCor = Integer.parseInt(po.getSpaCorrection());
        int posy = Integer.parseInt(po.getPosY());
//        int[] px = phPosXSyllable(osyl, line, posCor, spaCor, as); // Get the x position of syllable.
        int[] px = ScriptPlugin.phPosXSyllable(osyl, as); // Get the x position of syllable.
        int videowidth = vWidth; // Get video width
//        int linelength = phXSentenceWidth(osyl, line, posCor, spaCor, as); // Get the length of the line
        int linelength = ScriptPlugin.phXSentenceWidth(osyl, as); // Get the length of the line
        
        String[] commands = po.getCommands().split("§");        
        
        if(po.getMode().equals(ParticleObject.Mode.Normal.toString())){
            if(po.getType().equals(ParticleObject.Type.Syllable.toString())){
                //Apply effects on sentence
                String newSentence;
                for(int j=1;j<commands.length;j++){
                    for(int i=0;i<osyl.length;i++){

                        int srsxCENTER = ((videowidth-linelength)/2)+px[i];
                        int srsxLEFT = 0+px[i];
                        int srsxRIGHT = (videowidth-linelength)+px[i];
                    
                        int srsxLastCENTER = ((videowidth-linelength)/2)+px[osyl.length-1];
                        int srsxLastLEFT = 0+px[osyl.length-1];
                        int srsxLastRIGHT = (videowidth-linelength)+px[osyl.length-1];
                    
                        int srsxFirstCENTER = ((videowidth-linelength)/2)+px[0];
                        int srsxFirstLEFT = 0+px[0];
                        int srsxFirstRIGHT = (videowidth-linelength)+px[0];

                        //Get commands (if i=0 then 1st overrides)
                        String c = commands[j];

                        //Replacement of the variables
                        String new_expression = c.replace("%XCL", srsxLastCENTER+"");
                        new_expression = new_expression.replace("%XLL", srsxLastLEFT+"");
                        new_expression = new_expression.replace("%XRL", srsxLastRIGHT+"");
                        new_expression = new_expression.replace("%XCF", srsxFirstCENTER+"");
                        new_expression = new_expression.replace("%XLF", srsxFirstLEFT+"");
                        new_expression = new_expression.replace("%XRF", srsxFirstRIGHT+"");
                        new_expression = new_expression.replace("%XC", srsxCENTER+"");
                        new_expression = new_expression.replace("%XL", srsxLEFT+"");
                        new_expression = new_expression.replace("%XR", srsxRIGHT+"");
                        new_expression = new_expression.replace("%Y", posy+"");

                        //Initialize variables for ruby scripting
                        karaokeStart = osyl[i][3].toString();
                        karaokeEnd = osyl[i][4].toString();
                        karaokeDuration = osyl[i][2].toString();
                        karaokeOriStart = osyl[i][3].toString();
                        karaokeOriEnd = osyl[i][4].toString();
                        karaokeOriDuration = osyl[i][2].toString();
                        karaokeSentenceDuration = getKaraokeSentenceDuration(head);

                        //Ruby functions
                        Pattern p = Pattern.compile("(\\$[a-z]+)");
                        Matcher m = p.matcher(new_expression);
                        List<String> var = new ArrayList<String>();
                        while(m.find()){
                            var.add(m.group(1));
                        }
                        for(String v : var){
                            String function = v.substring(1);
                            String value = splug.runFxCodeAndDo(po.getRubyCode(), function);
                            new_expression = new_expression.replace(v, value);
                        }
        //                String car = runRubyCodeAndDo("def carbone\nreturn \"AAA\"\nend", "carbone");
        //                String car = runRubyCodeAndDo(po.getRubyCode(), "carbone");
        //                System.out.println(car);

                        //Do calcul with preset and syllabe parameters
                        new_expression = ScriptPlugin.phReplaceParameters(new_expression, osyl, i, head, null, -1);

                        //Add it to the new sentence
        //                newSentence = "{\\an5\\pos(" + srsx + "," + posy + ")}" + new_expression + osyl[i][0].toString();
                        newSentence = new_expression;

                        //Reformat assline
                        line = ScriptPlugin.getAssLineOf(head, newSentence);

                        //Return the modified line
                        ScriptPlugin.addResLine(line);
                    }

                    //Reinit
        //            newSentence = "";
                }
            }else{// Type is Sentence
                //Apply effects on sentence
                String newSentence;
                for(int j=1;j<commands.length;j++){
                    
                    int srsxCENTER = ((videowidth-linelength)/2)+px[0];                    
                    int srsxLEFT = 0+px[0];
                    int srsxRIGHT = (videowidth-linelength)+px[0];
                    
                    int srsxLastCENTER = ((videowidth-linelength)/2)+px[osyl.length-1];
                    int srsxLastLEFT = 0+px[osyl.length-1];
                    int srsxLastRIGHT = (videowidth-linelength)+px[osyl.length-1];
                    
                    int srsxFirstCENTER = ((videowidth-linelength)/2)+px[0];
                    int srsxFirstLEFT = 0+px[0];
                    int srsxFirstRIGHT = (videowidth-linelength)+px[0];

                    //Get commands (if i=0 then 1st overrides)
                    String c = commands[j];

                    //Replacement of the variables
                    String new_expression = c.replace("%XCL", srsxLastCENTER+"");
                    new_expression = new_expression.replace("%XLL", srsxLastLEFT+"");
                    new_expression = new_expression.replace("%XRL", srsxLastRIGHT+"");
                    new_expression = new_expression.replace("%XCF", srsxFirstCENTER+"");
                    new_expression = new_expression.replace("%XLF", srsxFirstLEFT+"");
                    new_expression = new_expression.replace("%XRF", srsxFirstRIGHT+"");
                    new_expression = new_expression.replace("%XC", srsxCENTER+"");
                    new_expression = new_expression.replace("%XL", srsxLEFT+"");
                    new_expression = new_expression.replace("%XR", srsxRIGHT+"");
                    new_expression = new_expression.replace("%Y", posy+"");
                    

                    //Initialize variables for ruby scripting
                    karaokeStart = osyl[0][3].toString();
                    karaokeEnd = osyl[0][4].toString();
                    karaokeDuration = osyl[0][2].toString();
                    karaokeOriStart = osyl[0][3].toString();
                    karaokeOriEnd = osyl[0][4].toString();
                    karaokeOriDuration = osyl[0][2].toString();
                    karaokeSentenceDuration = getKaraokeSentenceDuration(head);

                    //Ruby functions
                    Pattern p = Pattern.compile("(\\$[a-z]+)");
                    Matcher m = p.matcher(new_expression);
                    List<String> var = new ArrayList<String>();
                    while(m.find()){
                        var.add(m.group(1));
                    }
                    for(String v : var){
                        String function = v.substring(1);
                        String value = splug.runFxCodeAndDo(po.getRubyCode(), function);
                        new_expression = new_expression.replace(v, value);
                    }
    //                String car = runRubyCodeAndDo("def carbone\nreturn \"AAA\"\nend", "carbone");
    //                String car = runRubyCodeAndDo(po.getRubyCode(), "carbone");
    //                System.out.println(car);

                    //Do calcul with preset and syllabe parameters
                    new_expression = ScriptPlugin.phReplaceParameters(new_expression, osyl, 0, head, null, -1);

                    //Add it to the new sentence
    //                newSentence = "{\\an5\\pos(" + srsx + "," + posy + ")}" + new_expression + osyl[i][0].toString();
                    newSentence = new_expression;

                    //Reformat assline
                    line = ScriptPlugin.getAssLineOf(head, newSentence);

                    //Return the modified line
                    ScriptPlugin.addResLine(line);
                }
            }
            
        }else if(po.getMode().equals(ParticleObject.Mode.Periodic.toString())){
            Integer oneLayer = 1;//Init source layer (first commands are on layer 1)
            for(int i=0;i<osyl.length;i++){
                //Reset source layer
                if(oneLayer>commands.length-1){                    
                    oneLayer = 1;//Init source layer (first commands are on layer 1)
                }
                //Init the posX variables
                int srsxCENTER = ((videowidth-linelength)/2)+px[i];
                int srsxLEFT = 0+px[i];
                int srsxRIGHT = (videowidth-linelength)+px[i];

                int srsxLastCENTER = ((videowidth-linelength)/2)+px[osyl.length-1];
                int srsxLastLEFT = 0+px[osyl.length-1];
                int srsxLastRIGHT = (videowidth-linelength)+px[osyl.length-1];
                    
                int srsxFirstCENTER = ((videowidth-linelength)/2)+px[0];
                int srsxFirstLEFT = 0+px[0];
                int srsxFirstRIGHT = (videowidth-linelength)+px[0];
                //Get commands from source layer
                String c = commands[oneLayer];
                //Replacement of the variables
                String new_expression = c.replace("%XCL", srsxLastCENTER+"");
                new_expression = new_expression.replace("%XLL", srsxLastLEFT+"");
                new_expression = new_expression.replace("%XRL", srsxLastRIGHT+"");
                new_expression = new_expression.replace("%XCF", srsxFirstCENTER+"");
                new_expression = new_expression.replace("%XLF", srsxFirstLEFT+"");
                new_expression = new_expression.replace("%XRF", srsxFirstRIGHT+"");
                new_expression = new_expression.replace("%XC", srsxCENTER+"");
                new_expression = new_expression.replace("%XL", srsxLEFT+"");
                new_expression = new_expression.replace("%XR", srsxRIGHT+"");
                new_expression = new_expression.replace("%Y", posy+"");
                //Ruby functions
                Pattern p = Pattern.compile("(\\$[a-z]+)");
                Matcher m = p.matcher(new_expression);
                List<String> var = new ArrayList<String>();
                while(m.find()){
                    var.add(m.group(1));
                }
                for(String v : var){
                    String function = v.substring(1);
                    String value = splug.runFxCodeAndDo(po.getRubyCode(), function);
                    new_expression = new_expression.replace(v, value);
                }
                //Do calcul with preset and syllabe parameters
                new_expression = ScriptPlugin.phReplaceParameters(new_expression, osyl, i, head, null, -1);
                //Reformat assline
                line = ScriptPlugin.getAssLineOf(head, new_expression);
                //Return the modified line
                ScriptPlugin.addResLine(line);
                //Change layer
                oneLayer+=1;
            }            
        }else if(po.getMode().equals(ParticleObject.Mode.Random.toString())){
            //Init source layer (first commands are on layer 1)
            Integer randLayer = 1; Integer lastRandom = 1;
            java.util.Random seed = new java.util.Random();
            for(int i=0;i<osyl.length;i++){
                //Reset source layer
                if(commands.length>2){ //if more than one layer of effets (first commands are on layer 1)
                    while(randLayer==lastRandom | randLayer==0){
                        randLayer = seed.nextInt(commands.length);
                    }
                    lastRandom=randLayer;
                }
                //Init the posX variables
                int srsxCENTER = ((videowidth-linelength)/2)+px[i];
                int srsxLEFT = 0+px[i];
                int srsxRIGHT = (videowidth-linelength)+px[i];

                int srsxLastCENTER = ((videowidth-linelength)/2)+px[osyl.length-1];
                int srsxLastLEFT = 0+px[osyl.length-1];
                int srsxLastRIGHT = (videowidth-linelength)+px[osyl.length-1];
                    
                int srsxFirstCENTER = ((videowidth-linelength)/2)+px[0];
                int srsxFirstLEFT = 0+px[0];
                int srsxFirstRIGHT = (videowidth-linelength)+px[0];
                //Get commands from source layer
                String c = commands[randLayer];
                //Replacement of the variables
                String new_expression = c.replace("%XCL", srsxLastCENTER+"");
                new_expression = new_expression.replace("%XLL", srsxLastLEFT+"");
                new_expression = new_expression.replace("%XRL", srsxLastRIGHT+"");
                new_expression = new_expression.replace("%XCF", srsxFirstCENTER+"");
                new_expression = new_expression.replace("%XLF", srsxFirstLEFT+"");
                new_expression = new_expression.replace("%XRF", srsxFirstRIGHT+"");
                new_expression = new_expression.replace("%XC", srsxCENTER+"");
                new_expression = new_expression.replace("%XL", srsxLEFT+"");
                new_expression = new_expression.replace("%XR", srsxRIGHT+"");
                new_expression = new_expression.replace("%Y", posy+"");
                //Ruby functions
                Pattern p = Pattern.compile("(\\$[a-z]+)");
                Matcher m = p.matcher(new_expression);
                List<String> var = new ArrayList<String>();
                while(m.find()){
                    var.add(m.group(1));
                }
                for(String v : var){
                    String function = v.substring(1);
                    String value = splug.runFxCodeAndDo(po.getRubyCode(), function);
                    new_expression = new_expression.replace(v, value);
                }
                //Do calcul with preset and syllabe parameters
                new_expression = ScriptPlugin.phReplaceParameters(new_expression, osyl, i, head, null, -1);
                //Reformat assline
                line = ScriptPlugin.getAssLineOf(head, new_expression);
                //Return the modified line
                ScriptPlugin.addResLine(line);
            }
        }else if(po.getMode().equals(ParticleObject.Mode.Symmetric.toString())){
            //Init source layer (first commands are on layer 1)
            final int countComsLine = commands.length;
            int currentComs;
            for(int i=0;i<osyl.length;i++){
                //Reset source layer
                if(osyl.length/2>=i){
                    currentComs = i+1;
                    if (currentComs >= countComsLine){currentComs=countComsLine-1;}
                }else{
                    currentComs = osyl.length - i;
                    if (currentComs >= countComsLine){currentComs=countComsLine-1;}
                }
                //Init the posX variables
                int srsxCENTER = ((videowidth-linelength)/2)+px[i];
                int srsxLEFT = 0+px[i];
                int srsxRIGHT = (videowidth-linelength)+px[i];

                int srsxLastCENTER = ((videowidth-linelength)/2)+px[osyl.length-1];
                int srsxLastLEFT = 0+px[osyl.length-1];
                int srsxLastRIGHT = (videowidth-linelength)+px[osyl.length-1];
                    
                int srsxFirstCENTER = ((videowidth-linelength)/2)+px[0];
                int srsxFirstLEFT = 0+px[0];
                int srsxFirstRIGHT = (videowidth-linelength)+px[0];
                //Get commands from source layer
                String c = commands[currentComs];
                //Replacement of the variables
                String new_expression = c.replace("%XCL", srsxLastCENTER+"");
                new_expression = new_expression.replace("%XLL", srsxLastLEFT+"");
                new_expression = new_expression.replace("%XRL", srsxLastRIGHT+"");
                new_expression = new_expression.replace("%XCF", srsxFirstCENTER+"");
                new_expression = new_expression.replace("%XLF", srsxFirstLEFT+"");
                new_expression = new_expression.replace("%XRF", srsxFirstRIGHT+"");
                new_expression = new_expression.replace("%XC", srsxCENTER+"");
                new_expression = new_expression.replace("%XL", srsxLEFT+"");
                new_expression = new_expression.replace("%XR", srsxRIGHT+"");
                new_expression = new_expression.replace("%Y", posy+"");
                //Ruby functions
                Pattern p = Pattern.compile("(\\$[a-z]+)");
                Matcher m = p.matcher(new_expression);
                List<String> var = new ArrayList<String>();
                while(m.find()){
                    var.add(m.group(1));
                }
                for(String v : var){
                    String function = v.substring(1);
                    String value = splug.runFxCodeAndDo(po.getRubyCode(), function);
                    new_expression = new_expression.replace(v, value);
                }
                //Do calcul with preset and syllabe parameters
                new_expression = ScriptPlugin.phReplaceParameters(new_expression, osyl, i, head, null, -1);
                //Reformat assline
                line = ScriptPlugin.getAssLineOf(head, new_expression);
                //Return the modified line
                ScriptPlugin.addResLine(line);
            }
        }        
        
    }
    
    public void addScriptsToList(List<Object> lo){
        for(Object o : lo){
            if(o instanceof FxObject){
                FxObject fxo = (FxObject)o;
                boolean found = false;
                for(FxObject nfxo : getFxObjectListFromFxTree()){
                    if(nfxo.isTheSame(fxo)){found = true;}
                }
                if(found==false){
                    tnRuby.add(new DefaultMutableTreeNode(fxo));
                    System.out.println("Ruby or Python script : \""+fxo.getName()+"\" by "+fxo.getAuthor());
                }                
            }
        }
        expandAllFxTree(true);
    }
    
    /** <p>Expand the tree of the effects.<br />
     * Etend l'arbre des effets.</p> */
    private void expandAllFxTree(boolean expandCollection){

        for(int i=0;i<jTree1.getRowCount();i++){
            if(expandCollection==false){
                DefaultMutableTreeNode tni = (DefaultMutableTreeNode)jTree1
                        .getPathForRow(i).getLastPathComponent();
                if(tni.getUserObject() instanceof String){
                    if(tni.isRoot() || tni.isNodeChild(tni.getRoot())){
                        jTree1.expandRow(i);
                    }
                }
            }else{
                jTree1.expandRow(i);
            }
        }
    }
    
    public static DefaultMutableTreeNode getComponentOf(int j){
        return (DefaultMutableTreeNode)jTree1.getPathForRow(j).getLastPathComponent();
    }
    
    /** <p>Get all FxObjects from tree in a list.<br />
     * Obtient tous les effets de l'arbre des effets dans une liste.</p> */
    public static List<FxObject> getFxObjectListFromFxTree(){
        List<FxObject> l = new ArrayList<FxObject>();
        
        //Scan the RubyScript node and add any FxObject
        for(int i=0;i<tnRuby.getChildCount();i++){
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tnRuby
                    .getChildAt(i);
//            System.out.println("getFxObjectListFromFxTree() > "+dmtn.getUserObject());
            if(dmtn.getUserObject() instanceof FxObject){
                l.add((FxObject)dmtn.getUserObject());
            }else{
                //We have a String. We search members of the collection.
                for(int j=0;j<dmtn.getChildCount();j++){
                    DefaultMutableTreeNode d2 = (DefaultMutableTreeNode)dmtn
                            .getChildAt(i);
                    if(d2.getUserObject() instanceof FxObject){
                        l.add((FxObject)d2.getUserObject());
                    }
                }
            }
        }
        
        //Scan the XMLPresets node and add any FxObject
        for(int i=0;i<tnXML.getChildCount();i++){
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tnXML
                    .getChildAt(i);
//            System.out.println("getFxObjectListFromFxTree() > "+dmtn.getUserObject());
            if(dmtn.getUserObject() instanceof FxObject){
                l.add((FxObject)dmtn.getUserObject());
            }else{
                //We have a String. We search members of the collection.
                for(int j=0;j<dmtn.getChildCount();j++){
                    DefaultMutableTreeNode d2 = (DefaultMutableTreeNode)dmtn
                            .getChildAt(i);
                    if(d2.getUserObject() instanceof FxObject){
                        l.add((FxObject)d2.getUserObject());
                    }
                }
            }
        }

        return l;
    }
    
    /** <p>Modify a list of FxObjects in a xml file using a path.<br />
     * Modifie le fichier XML en y ajoutant/retirant des effets.</p> */
    public void modifyXmlPreset(FxObject fxo, FxObject modfxo, boolean file){
        List<FxObject> fxoFromList = getXmlPreset(null);
        List<FxObject> newList = new ArrayList<FxObject>();
        for (FxObject myfxo : fxoFromList){
            if (file==true && myfxo.getScriptPathname()
                    .equals(fxo.getScriptPathname())){
                if(myfxo.isTheSame(fxo)){
                    newList.add(modfxo);
                }else{
                    newList.add(myfxo);
                }
            }else{
                if(myfxo.isTheSame(fxo)){
                    newList.add(modfxo);
                }else{
                    System.out.println(myfxo.getFxObjectType());
                    newList.add(myfxo);
                }
            }
        }

        if(file==true){
            saveXmlPreset(newList, fxo.getScriptPathname());
        }else{
            saveXmlPreset(newList, docs+"afm-effects.xml");
        }
    }
    
    /** <p>Get elements from the fxTree.<br />
     * Obtient de l'arbre des effets les éléments dans une liste.</p> */
    public List<FxObject> getXmlPreset(String pathFilter){
        java.util.List<FxObject> mylfxo = new java.util.ArrayList<FxObject>();
        for(FxObject fxo : getFxObjectListFromFxTree()){
            if(pathFilter!=null){
                if (fxo.getFxObjectType()==FxObject.FxObjectType.XMLPreset
                        & fxo.getScriptPathname().equalsIgnoreCase(pathFilter)){
                    mylfxo.add(fxo);
                }
            }else{
                if (fxo.getFxObjectType()==FxObject.FxObjectType.XMLPreset){
                    mylfxo.add(fxo);
                }
            }
        }

        return mylfxo;
    }

    /** <p>Save a list of FxObjects in a xml file using a path.<br />
     * Sauvegarde une liste d'objet dans un fichier XML en utilisant le chemin.</p> */
    public void saveXmlPreset(List<FxObject> mylfxo, String path){
        XmlPresetWriter xpw = new XmlPresetWriter();
        xpw.setFxObjectList(mylfxo);
        xpw.createXmlPreset(path);
    }
    
    /** <p>Save all FxObject anywhere.<br />
     * Sauvegarde tous les effets XML.</p> */
    public void saveAllXmlPreset(){
        List<String> pathnames = getXmlPresetScriptPathname();
        for(String s : pathnames){
            List<FxObject> lfxo = getXmlPreset(s);
            saveXmlPreset(lfxo, s);
        }
    }
    
    /** <p>Get script pathnames of FxObjects from the fxTree.<br />
     * Récupère les chemins des scripts à partir de l'arbre des effets et de 
     * l'objet contenant les effets 'FxObject'.</p> */
    public List<String> getXmlPresetScriptPathname(){
        java.util.List<String> list = new java.util.ArrayList<String>();
        for(FxObject fxo : getFxObjectListFromFxTree()){
            if (fxo.getFxObjectType()==FxObject.FxObjectType.XMLPreset){
                if(!list.contains(fxo.getScriptPathname())){
                    list.add(fxo.getScriptPathname());
                }
            }
        }
        return list;
    }
    
    private void saveParticleToXML(List<ParticleObject> mylpo, String path){
        XmlParticleWriter xpw = new XmlParticleWriter();
        xpw.setParticleObjectList(mylpo);
        xpw.createParticle(path);
    }
    
    /** <p>Modify a list of FxObjects in a xml file using a path.<br />
     * Modifie le fichier XML en y ajoutant/retirant des effets.</p> */
    public void modifyXmlParticles(ParticleObject po, ParticleObject modpo, boolean file){

        List<ParticleObject> poFromList = getXmlParticles(null);
        List<ParticleObject> newList = new ArrayList<ParticleObject>();
        for (ParticleObject mypo : poFromList){
            if (file==true && mypo.getScriptPathname()
                    .equals(po.getScriptPathname())){
                if(mypo.isTheSame(po)){
                    newList.add(modpo);
                }else{
                    newList.add(mypo);
                }
            }else{
                if(mypo.isTheSame(po)){
                    newList.add(modpo);
                }else{
                    newList.add(mypo);
                }
            }
        }
        
        saveParticleToXML(newList, docs+"particle.pxfx");
    }
    
    /** <p>Get elements from the fxTree.<br />
     * Obtient de l'arbre des effets les éléments dans une liste.</p> */
    public List<ParticleObject> getXmlParticles(String pathFilter){
        List<ParticleObject> mylpo = new ArrayList<ParticleObject>();
        for(ParticleObject po : getParticleObjectListFromFxTree()){
            if(pathFilter!=null){
                if (po.getScriptPathname().equalsIgnoreCase(pathFilter)){
                    mylpo.add(po);
                }
            }else{
                mylpo.add(po);
            }
        }
        return mylpo;
    }
    
    /** <p>Get all FxObjects from tree in a list.<br />
     * Obtient tous les effets de l'arbre des effets dans une liste.</p> */
    private static List<ParticleObject> getParticleObjectListFromFxTree(){
        List<ParticleObject> l = new ArrayList<ParticleObject>();
        
        //Scan the XMLPresets node and add any FxObject
        for(int i=0;i<tnPart.getChildCount();i++){
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tnPart
                    .getChildAt(i);
            if(dmtn.getUserObject() instanceof ParticleObject){
                l.add((ParticleObject)dmtn.getUserObject());
            }else{
                //We have a String. We search members of the collection.
                for(int j=0;j<dmtn.getChildCount();j++){
                    DefaultMutableTreeNode d2 = (DefaultMutableTreeNode)dmtn
                            .getChildAt(i);
                    if(d2.getUserObject() instanceof ParticleObject){
                        l.add((ParticleObject)d2.getUserObject());
                    }
                }
            }
        }

        return l;
    }
    
    private void clearPngOfWavFiles(){
        for(java.io.File f : waveformImageDirectory.listFiles()){
            if(f.getPath().endsWith(".png")==true){
                f.delete();
            }
        }
    }
    
    public void setLanguageAndConfigure(Language lang){
        if(lang!=null){localeLanguage = lang;}
        if(localeLanguage.getValueOf("popmCut")!=null){
            popOrgCut.setText(localeLanguage.getValueOf("popmCut"));
            popResCut.setText(localeLanguage.getValueOf("popmCut"));}
        if(localeLanguage.getValueOf("popmCopy")!=null){
            popOrgCopy.setText(localeLanguage.getValueOf("popmCopy"));
            popResCopy.setText(localeLanguage.getValueOf("popmCopy"));}
        if(localeLanguage.getValueOf("popmPaste")!=null){
            popOrgPaste.setText(localeLanguage.getValueOf("popmPaste"));
            popResPaste.setText(localeLanguage.getValueOf("popmPaste"));}
        if(localeLanguage.getValueOf("popmDelete")!=null){
            popOrgDelete.setText(localeLanguage.getValueOf("popmDelete"));
            popResDelete.setText(localeLanguage.getValueOf("popmDelete"));}
        if(localeLanguage.getValueOf("popmClear")!=null){
            popOrgClear.setText(localeLanguage.getValueOf("popmClear"));
            popResClear.setText(localeLanguage.getValueOf("popmClear"));}
        if(localeLanguage.getValueOf("popmRemoveFX")!=null){
            popOrgRemove.setText(localeLanguage.getValueOf("popmRemoveFX"));}
        if(localeLanguage.getValueOf("popmRfReset")!=null){
            mnuPopRfReset.setText(localeLanguage.getValueOf("popmRfReset"));}
        if(localeLanguage.getValueOf("toolOpen")!=null){
            btnOpen.setToolTipText(localeLanguage.getValueOf("toolOpen"));}
        if(localeLanguage.getValueOf("toolSave1")!=null){
            btnSaveOri.setToolTipText(localeLanguage.getValueOf("toolSave1"));}
        if(localeLanguage.getValueOf("toolSave2")!=null){
            btnSaveRes.setToolTipText(localeLanguage.getValueOf("toolSave2"));}
        if(localeLanguage.getValueOf("toolNormal")!=null){
            tbNormalOri.setToolTipText(localeLanguage.getValueOf("toolNormal"));
            tbNormalRes.setToolTipText(localeLanguage.getValueOf("toolNormal"));}
        if(localeLanguage.getValueOf("toolItems")!=null){
            tbItemsOri.setToolTipText(localeLanguage.getValueOf("toolItems"));
            tbItemsRes.setToolTipText(localeLanguage.getValueOf("toolItems"));}
        if(localeLanguage.getValueOf("toolStrip")!=null){
            tbStripOri.setToolTipText(localeLanguage.getValueOf("toolStrip"));
            tbStripRes.setToolTipText(localeLanguage.getValueOf("toolStrip"));}
        if(localeLanguage.getValueOf("toolForOneLine")!=null){
            btnOneLine.setToolTipText(localeLanguage.getValueOf("toolForOneLine"));}
        if(localeLanguage.getValueOf("toolForFewLines")!=null){
            btnBlock.setToolTipText(localeLanguage.getValueOf("toolForFewLines"));}
        if(localeLanguage.getValueOf("toolAddFxoToLine")!=null){
            btnAddFxoToLine.setToolTipText(localeLanguage.getValueOf("toolAddFxoToLine"));}
        if(localeLanguage.getValueOf("toolAddXmlPresetFx")!=null){
            btnAddXmlPresetFx.setToolTipText(localeLanguage.getValueOf("toolAddXmlPresetFx"));}
        if(localeLanguage.getValueOf("toolModXmlPresetFx")!=null){
            btnModXmlPresetFx.setToolTipText(localeLanguage.getValueOf("toolModXmlPresetFx"));}
        if(localeLanguage.getValueOf("toolDelXmlPresetFx")!=null){
            btnDelXmlPresetFx.setToolTipText(localeLanguage.getValueOf("toolDelXmlPresetFx"));}
        if(localeLanguage.getValueOf("toolImpXmlPresetFx")!=null){
            btnImpXmlPresetFx.setToolTipText(localeLanguage.getValueOf("toolImpXmlPresetFx"));}
        if(localeLanguage.getValueOf("toolExpXmlPresetFx")!=null){
            btnExpXmlPresetFx.setToolTipText(localeLanguage.getValueOf("toolExpXmlPresetFx"));}
        if(localeLanguage.getValueOf("toolModRuby")!=null){
            btnModRuby.setToolTipText(localeLanguage.getValueOf("toolModRuby"));}
        if(localeLanguage.getValueOf("toolStyles")!=null){
            bStyles.setToolTipText(localeLanguage.getValueOf("toolStyles"));}
        if(localeLanguage.getValueOf("toolAudioPlay")!=null){
            btnPlay.setToolTipText(localeLanguage.getValueOf("toolAudioPlay"));}
        if(localeLanguage.getValueOf("toolAudioStop")!=null){
            btnStop.setToolTipText(localeLanguage.getValueOf("toolAudioStop"));}
        if(localeLanguage.getValueOf("toolAudioPlayArea")!=null){
            btnPlayArea.setToolTipText(localeLanguage.getValueOf("toolAudioPlayArea"));}
        if(localeLanguage.getValueOf("toolAudioPlayBB")!=null){
            btnPlayBeforeBegin.setToolTipText(localeLanguage.getValueOf("toolAudioPlayBB"));}
        if(localeLanguage.getValueOf("toolAudioPlayAB")!=null){
            btnPlayAfterBegin.setToolTipText(localeLanguage.getValueOf("toolAudioPlayAB"));}
        if(localeLanguage.getValueOf("toolAudioPlayBE")!=null){
            btnPlayBeforeEnd.setToolTipText(localeLanguage.getValueOf("toolAudioPlayBE"));}
        if(localeLanguage.getValueOf("toolAudioPlayAE")!=null){
            btnPlayAfterEnd.setToolTipText(localeLanguage.getValueOf("toolAudioPlayAE"));}
        if(localeLanguage.getValueOf("toolAudioNewTime")!=null){
            btnNewtime.setToolTipText(localeLanguage.getValueOf("toolAudioNewTime"));}
        if(localeLanguage.getValueOf("toolAudioSetKara")!=null){
            btnSetKara.setToolTipText(localeLanguage.getValueOf("toolAudioSetKara"));}
        if(localeLanguage.getValueOf("toolAudioGetKara")!=null){
            btnGetKara.setToolTipText(localeLanguage.getValueOf("toolAudioGetKara"));}
        if(localeLanguage.getValueOf("popmGetSelLine")!=null){
            popmGetSelLine.setText(localeLanguage.getValueOf("popmGetSelLine"));}
        if(localeLanguage.getValueOf("toolCreateParticle")!=null){
            btnCreateParticle.setToolTipText(localeLanguage.getValueOf("toolCreateParticle"));}
        if(localeLanguage.getValueOf("toolEditParticle")!=null){
            btnEditParticle.setToolTipText(localeLanguage.getValueOf("toolEditParticle"));}
        if(localeLanguage.getValueOf("toolDeleteParticle")!=null){
            btnDeleteParticle.setToolTipText(localeLanguage.getValueOf("toolDeleteParticle"));}
        if(localeLanguage.getValueOf("popmRfInfo")!=null){
            mnuPopRfInfo.setText(localeLanguage.getValueOf("popmRfInfo"));}
        if(localeLanguage.getValueOf("ifrOri")!=null){
            ifrOriginal.setTitle(localeLanguage.getValueOf("ifrOri"));}
        if(localeLanguage.getValueOf("ifrRes")!=null){
            ifrResult.setTitle(localeLanguage.getValueOf("ifrRes"));}
        if(localeLanguage.getValueOf("ifrSound")!=null){
            ifrSound.setTitle(localeLanguage.getValueOf("ifrSound"));}
        if(localeLanguage.getValueOf("ifrTree")!=null){
            ifrTree.setTitle(localeLanguage.getValueOf("ifrTree"));}
    }
    
    public void setUnicodeFont(Font fontToUse){
        ttr.setFont(fontToUse); ttr.repaint();
        rtr.setFont(fontToUse); rtr.repaint();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgOriginal = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        bgResult = new javax.swing.ButtonGroup();
        popOrg = new javax.swing.JPopupMenu();
        popOrgCut = new javax.swing.JMenuItem();
        popOrgCopy = new javax.swing.JMenuItem();
        popOrgPaste = new javax.swing.JMenuItem();
        popOrgSep1 = new javax.swing.JPopupMenu.Separator();
        popOrgDelete = new javax.swing.JMenuItem();
        popOrgClear = new javax.swing.JMenuItem();
        popOrgSep2 = new javax.swing.JPopupMenu.Separator();
        popOrgRemove = new javax.swing.JMenuItem();
        popRes = new javax.swing.JPopupMenu();
        popResCut = new javax.swing.JMenuItem();
        popResCopy = new javax.swing.JMenuItem();
        popResPaste = new javax.swing.JMenuItem();
        popResSep = new javax.swing.JPopupMenu.Separator();
        popResDelete = new javax.swing.JMenuItem();
        popResClear = new javax.swing.JMenuItem();
        popResetFx = new javax.swing.JPopupMenu();
        mnuPopRfReset = new javax.swing.JMenuItem();
        mnuPopRfInfo = new javax.swing.JMenuItem();
        popTimeKara = new javax.swing.JPopupMenu();
        popmToSelected = new javax.swing.JRadioButtonMenuItem();
        popmDirectly = new javax.swing.JRadioButtonMenuItem();
        popSetGetKaraoke = new javax.swing.JPopupMenu();
        popmGetSelLine = new javax.swing.JMenuItem();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        ifrOriginal = new javax.swing.JInternalFrame();
        jToolBar1 = new javax.swing.JToolBar();
        btnOpen = new javax.swing.JButton();
        btnSaveOri = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        tbNormalOri = new javax.swing.JToggleButton();
        tbItemsOri = new javax.swing.JToggleButton();
        tbStripOri = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnOneLine = new javax.swing.JButton();
        btnBlock = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        bStyles = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        orgTable = new javax.swing.JTable();
        ifrResult = new javax.swing.JInternalFrame();
        jToolBar2 = new javax.swing.JToolBar();
        btnSaveRes = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        tbNormalRes = new javax.swing.JToggleButton();
        tbItemsRes = new javax.swing.JToggleButton();
        tbStripRes = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        resTable = new javax.swing.JTable();
        ifrTree = new javax.swing.JInternalFrame();
        toolbarPanel = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnAddFxoToLine = new javax.swing.JButton();
        btnAddXmlPresetFx = new javax.swing.JButton();
        btnModXmlPresetFx = new javax.swing.JButton();
        btnDelXmlPresetFx = new javax.swing.JButton();
        btnImpXmlPresetFx = new javax.swing.JButton();
        btnExpXmlPresetFx = new javax.swing.JButton();
        btnModRuby = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        btnCreateParticle = new javax.swing.JButton();
        btnEditParticle = new javax.swing.JButton();
        btnDeleteParticle = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        ifrSound = new javax.swing.JInternalFrame();
        jToolBar5 = new javax.swing.JToolBar();
        btnOpen1 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        btnPlay = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        btnPlayArea = new javax.swing.JButton();
        btnPlayBeforeBegin = new javax.swing.JButton();
        btnPlayAfterBegin = new javax.swing.JButton();
        btnPlayBeforeEnd = new javax.swing.JButton();
        btnPlayAfterEnd = new javax.swing.JButton();
        btnNewtime = new javax.swing.JButton();
        btnSetKara = new javax.swing.JButton();
        btnGetKara = new javax.swing.JButton();
        tfTimeKara = new javax.swing.JTextField();
        realWavePanel = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();

        popOrgCut.setText("Cut");
        popOrgCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popOrgCutActionPerformed(evt);
            }
        });
        popOrg.add(popOrgCut);

        popOrgCopy.setText("Copy");
        popOrgCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popOrgCopyActionPerformed(evt);
            }
        });
        popOrg.add(popOrgCopy);

        popOrgPaste.setText("Paste");
        popOrgPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popOrgPasteActionPerformed(evt);
            }
        });
        popOrg.add(popOrgPaste);
        popOrg.add(popOrgSep1);

        popOrgDelete.setText("Delete");
        popOrgDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popOrgDeleteActionPerformed(evt);
            }
        });
        popOrg.add(popOrgDelete);

        popOrgClear.setText("Clear");
        popOrgClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popOrgClearActionPerformed(evt);
            }
        });
        popOrg.add(popOrgClear);
        popOrg.add(popOrgSep2);

        popOrgRemove.setText("Remove FX");
        popOrgRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popOrgRemoveActionPerformed(evt);
            }
        });
        popOrg.add(popOrgRemove);

        popResCut.setText("Cut");
        popResCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popResCutActionPerformed(evt);
            }
        });
        popRes.add(popResCut);

        popResCopy.setText("Copy");
        popResCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popResCopyActionPerformed(evt);
            }
        });
        popRes.add(popResCopy);

        popResPaste.setText("Paste");
        popResPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popResPasteActionPerformed(evt);
            }
        });
        popRes.add(popResPaste);
        popRes.add(popResSep);

        popResDelete.setText("Delete");
        popResDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popResDeleteActionPerformed(evt);
            }
        });
        popRes.add(popResDelete);

        popResClear.setText("Clear");
        popResClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popResClearActionPerformed(evt);
            }
        });
        popRes.add(popResClear);

        mnuPopRfReset.setText("Refresh the ruby scripts list");
        mnuPopRfReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPopRfResetActionPerformed(evt);
            }
        });
        popResetFx.add(mnuPopRfReset);

        mnuPopRfInfo.setText("Get info about this object");
        mnuPopRfInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPopRfInfoActionPerformed(evt);
            }
        });
        popResetFx.add(mnuPopRfInfo);

        popmToSelected.setText("To the selected line");
        popTimeKara.add(popmToSelected);

        popmDirectly.setSelected(true);
        popmDirectly.setText("Add a new line");
        popTimeKara.add(popmDirectly);

        popmGetSelLine.setText("Get the selected line");
        popmGetSelLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmGetSelLineActionPerformed(evt);
            }
        });
        popSetGetKaraoke.add(popmGetSelLine);

        ifrOriginal.setIconifiable(true);
        ifrOriginal.setMaximizable(true);
        ifrOriginal.setResizable(true);
        ifrOriginal.setTitle("Karaoké original");
        ifrOriginal.setVisible(true);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/funsub_open.png"))); // NOI18N
        btnOpen.setToolTipText("Ouvriur un fichier SSA ou ASS");
        btnOpen.setFocusable(false);
        btnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpen);

        btnSaveOri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/funsub_save.png"))); // NOI18N
        btnSaveOri.setToolTipText("Sauvegarder le karaoké original");
        btnSaveOri.setFocusable(false);
        btnSaveOri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveOri.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveOri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveOriActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveOri);
        jToolBar1.add(jSeparator1);

        bgOriginal.add(tbNormalOri);
        tbNormalOri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-NormalMode.png"))); // NOI18N
        tbNormalOri.setSelected(true);
        tbNormalOri.setToolTipText("Montrer toutes les balises");
        tbNormalOri.setFocusable(false);
        tbNormalOri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbNormalOri.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbNormalOri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbNormalOriActionPerformed(evt);
            }
        });
        jToolBar1.add(tbNormalOri);

        bgOriginal.add(tbItemsOri);
        tbItemsOri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ItemsMode.png"))); // NOI18N
        tbItemsOri.setToolTipText("Montrer des items");
        tbItemsOri.setFocusable(false);
        tbItemsOri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbItemsOri.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbItemsOri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbItemsOriActionPerformed(evt);
            }
        });
        jToolBar1.add(tbItemsOri);

        bgOriginal.add(tbStripOri);
        tbStripOri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-StripMode.png"))); // NOI18N
        tbStripOri.setToolTipText("Montrer le texte seul");
        tbStripOri.setFocusable(false);
        tbStripOri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbStripOri.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbStripOri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbStripOriActionPerformed(evt);
            }
        });
        jToolBar1.add(tbStripOri);
        jToolBar1.add(jSeparator2);

        btnOneLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ExecuteForOneLine.png"))); // NOI18N
        btnOneLine.setToolTipText("Exécuter ligne par ligne");
        btnOneLine.setFocusable(false);
        btnOneLine.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOneLine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOneLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOneLineActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOneLine);

        btnBlock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ExecuteForFewLines.png"))); // NOI18N
        btnBlock.setToolTipText("Exécuter par bloc");
        btnBlock.setFocusable(false);
        btnBlock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBlock.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlockActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBlock);
        jToolBar1.add(jSeparator4);

        bStyles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_fonts.png"))); // NOI18N
        bStyles.setFocusable(false);
        bStyles.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bStyles.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bStyles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStylesActionPerformed(evt);
            }
        });
        jToolBar1.add(bStyles);

        ifrOriginal.getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        orgTable.setModel(new javax.swing.table.DefaultTableModel(
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
        orgTable.setComponentPopupMenu(popOrg);
        jScrollPane1.setViewportView(orgTable);

        ifrOriginal.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jDesktopPane1.add(ifrOriginal);
        ifrOriginal.setBounds(10, 10, 430, 270);

        ifrResult.setIconifiable(true);
        ifrResult.setMaximizable(true);
        ifrResult.setResizable(true);
        ifrResult.setTitle("Karaoké résultant");
        ifrResult.setVisible(true);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnSaveRes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/funsub_save2.png"))); // NOI18N
        btnSaveRes.setToolTipText("Sauvegarder le karaoké résultant");
        btnSaveRes.setFocusable(false);
        btnSaveRes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveRes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveResActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSaveRes);
        jToolBar2.add(jSeparator3);

        bgResult.add(tbNormalRes);
        tbNormalRes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-NormalMode.png"))); // NOI18N
        tbNormalRes.setSelected(true);
        tbNormalRes.setToolTipText("Montrer toutes les balises");
        tbNormalRes.setFocusable(false);
        tbNormalRes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbNormalRes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbNormalRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbNormalResActionPerformed(evt);
            }
        });
        jToolBar2.add(tbNormalRes);

        bgResult.add(tbItemsRes);
        tbItemsRes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ItemsMode.png"))); // NOI18N
        tbItemsRes.setToolTipText("Montrer des items");
        tbItemsRes.setFocusable(false);
        tbItemsRes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbItemsRes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbItemsRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbItemsResActionPerformed(evt);
            }
        });
        jToolBar2.add(tbItemsRes);

        bgResult.add(tbStripRes);
        tbStripRes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-StripMode.png"))); // NOI18N
        tbStripRes.setToolTipText("Montrer le texte seul");
        tbStripRes.setFocusable(false);
        tbStripRes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbStripRes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbStripRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbStripResActionPerformed(evt);
            }
        });
        jToolBar2.add(tbStripRes);

        ifrResult.getContentPane().add(jToolBar2, java.awt.BorderLayout.NORTH);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        resTable.setModel(new javax.swing.table.DefaultTableModel(
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
        resTable.setComponentPopupMenu(popRes);
        jScrollPane2.setViewportView(resTable);

        ifrResult.getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jDesktopPane1.add(ifrResult);
        ifrResult.setBounds(10, 300, 430, 270);

        ifrTree.setIconifiable(true);
        ifrTree.setResizable(true);
        ifrTree.setTitle("Liste d'effets");
        ifrTree.setVisible(true);

        toolbarPanel.setLayout(new java.awt.BorderLayout());

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnAddFxoToLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-AddFxoToList.png"))); // NOI18N
        btnAddFxoToLine.setFocusable(false);
        btnAddFxoToLine.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddFxoToLine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddFxoToLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFxoToLineActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAddFxoToLine);

        btnAddXmlPresetFx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-NewFxo.png"))); // NOI18N
        btnAddXmlPresetFx.setFocusable(false);
        btnAddXmlPresetFx.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddXmlPresetFx.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddXmlPresetFx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddXmlPresetFxActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAddXmlPresetFx);

        btnModXmlPresetFx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ModFxo.png"))); // NOI18N
        btnModXmlPresetFx.setFocusable(false);
        btnModXmlPresetFx.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModXmlPresetFx.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModXmlPresetFx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModXmlPresetFxActionPerformed(evt);
            }
        });
        jToolBar3.add(btnModXmlPresetFx);

        btnDelXmlPresetFx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DelFxo.png"))); // NOI18N
        btnDelXmlPresetFx.setFocusable(false);
        btnDelXmlPresetFx.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelXmlPresetFx.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDelXmlPresetFx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelXmlPresetFxActionPerformed(evt);
            }
        });
        jToolBar3.add(btnDelXmlPresetFx);

        btnImpXmlPresetFx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ImportFxo.png"))); // NOI18N
        btnImpXmlPresetFx.setFocusable(false);
        btnImpXmlPresetFx.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImpXmlPresetFx.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImpXmlPresetFx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpXmlPresetFxActionPerformed(evt);
            }
        });
        jToolBar3.add(btnImpXmlPresetFx);

        btnExpXmlPresetFx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ExportFxo.png"))); // NOI18N
        btnExpXmlPresetFx.setFocusable(false);
        btnExpXmlPresetFx.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExpXmlPresetFx.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExpXmlPresetFx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpXmlPresetFxActionPerformed(evt);
            }
        });
        jToolBar3.add(btnExpXmlPresetFx);

        btnModRuby.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ModRuby.png"))); // NOI18N
        btnModRuby.setFocusable(false);
        btnModRuby.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModRuby.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModRuby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModRubyActionPerformed(evt);
            }
        });
        jToolBar3.add(btnModRuby);

        toolbarPanel.add(jToolBar3, java.awt.BorderLayout.NORTH);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        btnCreateParticle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-NewPart.png"))); // NOI18N
        btnCreateParticle.setFocusable(false);
        btnCreateParticle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCreateParticle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCreateParticle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateParticleActionPerformed(evt);
            }
        });
        jToolBar4.add(btnCreateParticle);

        btnEditParticle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ModPart.png"))); // NOI18N
        btnEditParticle.setFocusable(false);
        btnEditParticle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditParticle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditParticle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditParticleActionPerformed(evt);
            }
        });
        jToolBar4.add(btnEditParticle);

        btnDeleteParticle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DelPart.png"))); // NOI18N
        btnDeleteParticle.setFocusable(false);
        btnDeleteParticle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeleteParticle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDeleteParticle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteParticleActionPerformed(evt);
            }
        });
        jToolBar4.add(btnDeleteParticle);

        toolbarPanel.add(jToolBar4, java.awt.BorderLayout.SOUTH);

        ifrTree.getContentPane().add(toolbarPanel, java.awt.BorderLayout.NORTH);

        jTree1.setComponentPopupMenu(popResetFx);
        jScrollPane3.setViewportView(jTree1);

        ifrTree.getContentPane().add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jDesktopPane1.add(ifrTree);
        ifrTree.setBounds(450, 10, 200, 560);

        ifrSound.setIconifiable(true);
        ifrSound.setResizable(true);
        ifrSound.setTitle("Forme d'onde");
        ifrSound.setVisible(true);

        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);

        btnOpen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_filesystem_folder_grey_open.png"))); // NOI18N
        btnOpen1.setToolTipText("Open a new sound...");
        btnOpen1.setFocusable(false);
        btnOpen1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpen1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpen1ActionPerformed(evt);
            }
        });
        jToolBar5.add(btnOpen1);
        jToolBar5.add(jSeparator10);

        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/play-32.png"))); // NOI18N
        btnPlay.setToolTipText("Play the sound from the beginning");
        btnPlay.setFocusable(false);
        btnPlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        jToolBar5.add(btnPlay);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/stop-32.png"))); // NOI18N
        btnStop.setToolTipText("Stop the sound");
        btnStop.setFocusable(false);
        btnStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        jToolBar5.add(btnStop);
        jToolBar5.add(jSeparator11);

        btnPlayArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playarea-32.png"))); // NOI18N
        btnPlayArea.setToolTipText("Play the delimited part of the sound");
        btnPlayArea.setFocusable(false);
        btnPlayArea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayArea.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayAreaActionPerformed(evt);
            }
        });
        jToolBar5.add(btnPlayArea);

        btnPlayBeforeBegin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playbeforebegin-32.png"))); // NOI18N
        btnPlayBeforeBegin.setToolTipText("Play before the beginning of the delimited area");
        btnPlayBeforeBegin.setFocusable(false);
        btnPlayBeforeBegin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayBeforeBegin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayBeforeBegin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayBeforeBeginActionPerformed(evt);
            }
        });
        jToolBar5.add(btnPlayBeforeBegin);

        btnPlayAfterBegin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playafterbegin-32.png"))); // NOI18N
        btnPlayAfterBegin.setToolTipText("Play after the beginning of the delimited area");
        btnPlayAfterBegin.setFocusable(false);
        btnPlayAfterBegin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayAfterBegin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayAfterBegin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayAfterBeginActionPerformed(evt);
            }
        });
        jToolBar5.add(btnPlayAfterBegin);

        btnPlayBeforeEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playbeforeend-32.png"))); // NOI18N
        btnPlayBeforeEnd.setToolTipText("Play before the end of the delimited area");
        btnPlayBeforeEnd.setFocusable(false);
        btnPlayBeforeEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayBeforeEnd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayBeforeEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayBeforeEndActionPerformed(evt);
            }
        });
        jToolBar5.add(btnPlayBeforeEnd);

        btnPlayAfterEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playafterend-32.png"))); // NOI18N
        btnPlayAfterEnd.setToolTipText("Play after the end of the delimited area");
        btnPlayAfterEnd.setFocusable(false);
        btnPlayAfterEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayAfterEnd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayAfterEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayAfterEndActionPerformed(evt);
            }
        });
        jToolBar5.add(btnPlayAfterEnd);

        btnNewtime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/newtime-32.png"))); // NOI18N
        btnNewtime.setToolTipText("Get the time of the start and the time of the end");
        btnNewtime.setComponentPopupMenu(popTimeKara);
        btnNewtime.setFocusable(false);
        btnNewtime.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewtime.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewtime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewtimeActionPerformed(evt);
            }
        });
        jToolBar5.add(btnNewtime);

        btnSetKara.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/setkara-32.png"))); // NOI18N
        btnSetKara.setToolTipText("Set the karaoke");
        btnSetKara.setFocusable(false);
        btnSetKara.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSetKara.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSetKara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetKaraActionPerformed(evt);
            }
        });
        jToolBar5.add(btnSetKara);

        btnGetKara.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/changetime-32.png"))); // NOI18N
        btnGetKara.setToolTipText("Get the karaoke");
        btnGetKara.setFocusable(false);
        btnGetKara.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGetKara.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGetKara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetKaraActionPerformed(evt);
            }
        });
        jToolBar5.add(btnGetKara);

        tfTimeKara.setComponentPopupMenu(popSetGetKaraoke);
        tfTimeKara.setPreferredSize(new java.awt.Dimension(500, 30));
        jToolBar5.add(tfTimeKara);

        ifrSound.getContentPane().add(jToolBar5, java.awt.BorderLayout.NORTH);

        realWavePanel.setLayout(null);
        ifrSound.getContentPane().add(realWavePanel, java.awt.BorderLayout.CENTER);

        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar1.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar1AdjustmentValueChanged(evt);
            }
        });
        ifrSound.getContentPane().add(jScrollBar1, java.awt.BorderLayout.SOUTH);

        jDesktopPane1.add(ifrSound);
        ifrSound.setBounds(670, 180, 520, 220);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        for (FileFilter f : jFileChooser1.getChoosableFileFilters()){
            jFileChooser1.removeChoosableFileFilter(f);
        }

        jFileChooser1.addChoosableFileFilter(new SubtitleFilter());
        jFileChooser1.setAccessory(null);

        int z = this.jFileChooser1.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){

            if(jFileChooser1.getSelectedFile().getName().endsWith("ssa")){
                AssIO aio = new AssIO();
                aio.LireFichierSSAi2(
                        jFileChooser1.getSelectedFile().getPath(),
                        orgModel,
                        feuille.MainFrame.getAssInfos(),
                        feuille.MainFrame.getAssStyleCollection(),
                        feuille.MainFrame.getAssNameCollectionWithInit(),
                        false);
            }
            if(jFileChooser1.getSelectedFile().getName().endsWith("ass")){
                AssIO aio = new AssIO();
                aio.LireFichierASSi2(
                        jFileChooser1.getSelectedFile().getPath(),
                        orgModel,
                        feuille.MainFrame.getAssInfos(),
                        feuille.MainFrame.getAssStyleCollection(),
                        feuille.MainFrame.getAssNameCollectionWithInit(),
                        false);
            }
        }
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnSaveOriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveOriActionPerformed
        for (FileFilter f : jFileChooser1.getChoosableFileFilters()){
            jFileChooser1.removeChoosableFileFilter(f);
        }

        // Add good file filters.
        jFileChooser1.addChoosableFileFilter(new AssFilter());
        EncodingPanel ep = new EncodingPanel();
        jFileChooser1.setAccessory(ep);
        // Action
        int z = this.jFileChooser1.showSaveDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            AssIO aio = new AssIO();
            aio.EcrireFichierASS2(
                    jFileChooser1.getSelectedFile().getPath(),
                    orgModel,
                    feuille.MainFrame.getAssInfos(),
                    feuille.MainFrame.getAssStyleCollection(),
                    ep.getEncoding());
        }
    }//GEN-LAST:event_btnSaveOriActionPerformed

    private void tbNormalOriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbNormalOriActionPerformed
        ttr.setTextType(feuille.karaoke.renderer.tableTypeRenderer.TextType.Normal); orgTable.repaint();
//        cfg.put(assfxmaker.lib.Configuration.Type.TABLE_DISPLAY, "Normal");
//        AssIO aio = new AssIO(); aio.SaveConfig(cfg);
    }//GEN-LAST:event_tbNormalOriActionPerformed

    private void tbItemsOriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbItemsOriActionPerformed
        ttr.setTextType(feuille.karaoke.renderer.tableTypeRenderer.TextType.WithItems); orgTable.repaint();
//        cfg.put(assfxmaker.lib.Configuration.Type.TABLE_DISPLAY, "WithItems");
//        AssIO aio = new AssIO(); aio.SaveConfig(cfg);
    }//GEN-LAST:event_tbItemsOriActionPerformed

    private void tbStripOriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbStripOriActionPerformed
        ttr.setTextType(feuille.karaoke.renderer.tableTypeRenderer.TextType.StripAll); orgTable.repaint();
//        cfg.put(assfxmaker.lib.Configuration.Type.TABLE_DISPLAY, "StripAll");
//        AssIO aio = new AssIO(); aio.SaveConfig(cfg);
    }//GEN-LAST:event_tbStripOriActionPerformed

    private void btnOneLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOneLineActionPerformed
        try{
        if(orgTable.getSelectedRowCount()!=0){
            for(int i : orgTable.getSelectedRows()){
                lastSelRow = i;
                String[] list = ((String)orgModel.getValueAt(i, 11)).split(",");
                for(String s : list){
                    int j = Integer.valueOf(s);
                    DefaultMutableTreeNode tn = getComponentOf(j);
                    if(tn.getUserObject() instanceof FxObject){
                        FxObject fxo = (FxObject)tn.getUserObject();
                        if(fxo.getFxObjectType()==FxObject.FxObjectType.Ruby){
                            //runRubyScriptAndDo(fxo.getScriptPathname(), fxo.getFunction());
                            splug.runScriptAndDo(fxo);
                        }
                        if(fxo.getFxObjectType()==FxObject.FxObjectType.Python){
                            splug.runScriptAndDo(fxo);
                        }
                        if(fxo.getFxObjectType()==FxObject.FxObjectType.XMLPreset){
                            runFunctionAndDo(fxo);
                        }                        
                    }
                    if(tn.getUserObject() instanceof ParticleObject){
                        ParticleObject po = (ParticleObject)tn.getUserObject();
                        runParticleAndDo(po);
                    }
                }
            }
            lastSelRow = -1; //Reset
        }
    }catch(Exception exc){
    }
    }//GEN-LAST:event_btnOneLineActionPerformed

    private void btnBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlockActionPerformed
        // Execute associated XMLPresets for the selected lines by giving each line
        // to the function which make the effects printout in result tab.
        try{
            if(orgTable.getSelectedRowCount()!=0){

                List<FxoLines> fxlist = new ArrayList<FxoLines>();

                //Getting the effects
                for(int line : orgTable.getSelectedRows()){
                    String[] list = ((String)orgModel.getValueAt(line, 11)).split(",");
                    for(String s : list){
                        int j = Integer.valueOf(s);
                        DefaultMutableTreeNode tn = (DefaultMutableTreeNode)jTree1
                            .getPathForRow(j).getLastPathComponent();
                        FxObject fxo = (FxObject)tn.getUserObject();

                        boolean FxoFound = false;
                        for(FxoLines fls : fxlist){
                            if(fls.getFxObject().equals(fxo)){
                                //Deux fxo identiques trouves - on rajoute que la ligne
                                fls.addLine(line);
                                FxoFound = true;
                            }
                        }
                        if(FxoFound==false){
                            FxoLines fls = new FxoLines(fxo);
                            fls.addLine(line);
                            fxlist.add(fls);
                        }
                    }
                }

                //Treatment of effects
                for(FxoLines fls : fxlist){
                    getSelRows = fls.getLines();
                    FxObject fxo = fls.getFxObject();
                    if(fxo.getFxObjectType()==FxObject.FxObjectType.XMLPreset){
                        runFunctionAndDo2(fxo);
                    }
                    if(fxo.getFxObjectType()==FxObject.FxObjectType.Ruby){
                        //runRubyScriptAndDo(fxo.getScriptPathname(), fxo.getFunction());
                        splug.runScriptAndDo(fxo);
                    }
                    if(fxo.getFxObjectType()==FxObject.FxObjectType.Python){
                        splug.runScriptAndDo(fxo);
                    }
                }

            }
        }catch(Exception exc){
        }
    }//GEN-LAST:event_btnBlockActionPerformed

    private void btnSaveResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveResActionPerformed
        for (FileFilter f : jFileChooser1.getChoosableFileFilters()){
            jFileChooser1.removeChoosableFileFilter(f);
        }

        jFileChooser1.addChoosableFileFilter(new AssFilter());
        EncodingPanel ep = new EncodingPanel();
        jFileChooser1.setAccessory(ep);
        // Action
        int z = this.jFileChooser1.showSaveDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            AssIO aio = new AssIO();
            aio.EcrireFichierASS2(
                    jFileChooser1.getSelectedFile().getPath(),
                    resModel,
                    feuille.MainFrame.getAssInfos(),
                    feuille.MainFrame.getAssStyleCollection(),
                    ep.getEncoding());
        }
    }//GEN-LAST:event_btnSaveResActionPerformed

    private void tbNormalResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbNormalResActionPerformed
        rtr.setTextType(feuille.karaoke.renderer.resultTableRenderer.TextType.Normal); resTable.repaint();
//        cfg.put(assfxmaker.lib.Configuration.Type.TABLE_DISPLAY, "Normal");
//        AssIO aio = new AssIO(); aio.SaveConfig(cfg);
    }//GEN-LAST:event_tbNormalResActionPerformed

    private void tbItemsResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbItemsResActionPerformed
        rtr.setTextType(feuille.karaoke.renderer.resultTableRenderer.TextType.WithItems); resTable.repaint();
//        cfg.put(assfxmaker.lib.Configuration.Type.TABLE_DISPLAY, "WithItems");
//        AssIO aio = new AssIO(); aio.SaveConfig(cfg);
    }//GEN-LAST:event_tbItemsResActionPerformed

    private void tbStripResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbStripResActionPerformed
        rtr.setTextType(feuille.karaoke.renderer.resultTableRenderer.TextType.StripAll); resTable.repaint();
//        cfg.put(assfxmaker.lib.Configuration.Type.TABLE_DISPLAY, "StripAll");
//        AssIO aio = new AssIO(); aio.SaveConfig(cfg);
    }//GEN-LAST:event_tbStripResActionPerformed

    private void btnAddFxoToLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFxoToLineActionPerformed
        if(orgTable.getSelectedRowCount()!=0 && jTree1.getMinSelectionRow()!=-1){
            String str = "";
            for (int i : jTree1.getSelectionRows()){
                DefaultMutableTreeNode tn = (DefaultMutableTreeNode)jTree1
                        .getPathForRow(i).getLastPathComponent();
                if(tn.getUserObject() instanceof FxObject){
                    str += String.valueOf(i)+",";
                }else if(tn.getUserObject() instanceof ParticleObject){
                    ParticleObject po = (ParticleObject)tn.getUserObject();
                    if(po.getStyle().isEmpty()==false){
                        str += String.valueOf(i)+",";                
                    }else{
                        JOptionPane.showMessageDialog(this,
                                "A style is required, please check the particle and add a style.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }                
                }
            }
            for (int row : orgTable.getSelectedRows()){
                orgModel.setValueAt(str, row, 11);
            }
        }
    }//GEN-LAST:event_btnAddFxoToLineActionPerformed

    private void btnAddXmlPresetFxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddXmlPresetFxActionPerformed
        XmlPresetDialog xpd = new XmlPresetDialog(frame, true);
        xpd.setFunctionsCollection(funcc);
        xpd.setDrawingPath(drawingEditor);
        xpd.setDrawingsPath(docs);
        xpd.setLocationRelativeTo(null); //set the dialog at the center
        FxObject fxo = xpd.showDialog(new FxObject());
        if(xpd.isOKButtonPressed()){
    //        fxModel.addElement(fxo);
            tnXML.add(new DefaultMutableTreeNode(fxo));
            jTree1.updateUI();
        }
        if(xpd.isSaveSelected()){
            modifyXmlPreset(new FxObject(), fxo, false);
        }
    }//GEN-LAST:event_btnAddXmlPresetFxActionPerformed

    private void btnModXmlPresetFxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModXmlPresetFxActionPerformed
        try{
            // Get FxObject
            //FxObject fxo = (FxObject)fxModel.getElementAt(jList1.getSelectedIndex());
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            if(dmtn.getUserObject() instanceof FxObject){
                FxObject fxo = (FxObject)dmtn.getUserObject();
                // Do
                switch(fxo.getFxObjectType()){
                    case XMLPreset:
                        XmlPresetDialog xpd = new XmlPresetDialog(frame, true);
                        xpd.setFunctionsCollection(funcc);
                        xpd.setDrawingPath(drawingEditor);
                        xpd.setDrawingsPath(docs);
                        xpd.setLocationRelativeTo(null); //set the dialog at the center
                        fxo = xpd.showDialog(fxo);
                        if(xpd.isOKButtonPressed()){

        //                    fxModel.insertElementAt(fxo,jList1.getSelectedIndex());
        //                    fxModel.removeElementAt(jList1.getSelectedIndex()+1);
                        }
                        if(xpd.isSaveSelected()){
                            modifyXmlPreset(new FxObject(), fxo, false);
                        }
                        break;
                    case Ruby: break;
                    case JavaScript: break;
                    case Unknown: break;
                }
            }        
        }catch(Exception exc){

        }
    }//GEN-LAST:event_btnModXmlPresetFxActionPerformed

    private void btnDelXmlPresetFxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelXmlPresetFxActionPerformed
        DefaultMutableTreeNode tn = (DefaultMutableTreeNode)jTree1
                    .getSelectionPath().getLastPathComponent();
        if(tn.getUserObject() instanceof FxObject){
    //        FxObject fxo = (FxObject)fxModel.getElementAt(jList1.getSelectedIndex());
            FxObject fxo = (FxObject)tn.getUserObject();
            // Do
            switch(fxo.getFxObjectType()){
                case XMLPreset:
                    String message = localeLanguage.getValueOf("optpMessage2")!=null ?
                        localeLanguage.getValueOf("optpMessage2") :
                        "Would you really want to delete this item ?";
                    String title = localeLanguage.getValueOf("optpTitle2")!=null ?
                        localeLanguage.getValueOf("optpTitle2") :
                        "Confirm";
                    int n = JOptionPane.showConfirmDialog(this,message,title,
                            JOptionPane.YES_NO_OPTION);
                    if (n==JOptionPane.YES_OPTION){
    //                    fxModel.removeElementAt(jList1.getSelectedIndex());
                        tn.setUserObject(null);
                        tn.removeFromParent();
                        jTree1.updateUI();
                        modifyXmlPreset(new FxObject(), fxo, false);
                    }            
                    break;
                case Ruby: break;
                case JavaScript: break;
                case Unknown: break;
            }
        }else if(tn.getLevel()>1){
            //TODO - Delete the collection ?
        }    
    }//GEN-LAST:event_btnDelXmlPresetFxActionPerformed

    private void btnImpXmlPresetFxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpXmlPresetFxActionPerformed
        // Import the collection of XML Preset    
        for (FileFilter f : jFileChooser1.getChoosableFileFilters()){               // Clear the list of file filters.
            jFileChooser1.removeChoosableFileFilter(f);
        }
        jFileChooser1.addChoosableFileFilter(new XmlPresetFilter());
        jFileChooser1.setAccessory(null);
        int z = this.jFileChooser1.showOpenDialog(this);                            // Action
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            try {
    //            FxObject[] xmlfxo = assfxmaker.lib.XmlPresetHandler.startProcess(
    //                    jFileChooser1.getSelectedFile().getPath());

                XmlPresetHandler xph = new XmlPresetHandler(
                        jFileChooser1.getSelectedFile().getPath());
                List<FxObject> xmlfxo = xph.getFxObjectList();

                for(FxObject f : xmlfxo){
                    tnXML.add(new DefaultMutableTreeNode(f));
                    jTree1.updateUI();
                }
                // TODO Save directly
    //            int n = JOptionPane.showConfirmDialog(this,
    //            "Do you want to save the list of effects ?.",
    //            "Saving process...",
    //            JOptionPane.YES_NO_OPTION);
    //            if(n == JOptionPane.YES_OPTION){
    //
    //            }
            } catch (Exception ex) {
            }
        }
    }//GEN-LAST:event_btnImpXmlPresetFxActionPerformed

    private void btnExpXmlPresetFxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpXmlPresetFxActionPerformed
        // Export the collection of XML Preset
        //Get a list from the fxo JTree
        List<FxObject> lfxo = getFxObjectListFromFxTree();
        //List object for the storing of 'collection'
        List<String> lstr = new ArrayList<String>();
        // Make a list of choice (Storing of fxo which have 'collection')
        for(FxObject fxo : lfxo){
            if(!fxo.getCollect().isEmpty()){
                lstr.add(fxo.getCollect());
            }
        }
        // If there is a collection then continue
        if(!lstr.isEmpty()){
            //Let the user choose a collection and path to save it (in xmlfxo)
            XmlPExportDialog xped = new XmlPExportDialog(frame, true);
            xped.setLocationRelativeTo(null);
            String[] sChoice = xped.showDialog(lstr);
            //Make a list of FxCbject which belong to the same 'collection'
            if(sChoice[0]!=null && sChoice[1]!=null){
                List<FxObject> selectedFxo = new ArrayList<FxObject>();
                for(FxObject fxo : lfxo){
                    if(fxo.getCollect().equalsIgnoreCase(sChoice[0])){
                        selectedFxo.add(fxo);
                    }
                }// Then save it
                saveXmlPreset(selectedFxo, sChoice[1]+
                        java.io.File.separator+sChoice[0]+" afm-effects.xml");
            }  
        }else{
            String message = localeLanguage.getValueOf("optpMessage1")!=null ?
                localeLanguage.getValueOf("optpMessage1") :
                "Sorry but there is no collection in the list of effects.";
            String title = localeLanguage.getValueOf("optpTitle1")!=null ?
                localeLanguage.getValueOf("optpTitle1") :
                "Error";
            JOptionPane.showMessageDialog(this,message,title,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExpXmlPresetFxActionPerformed

    private void btnModRubyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModRubyActionPerformed
        if(rubyEditor.isEmpty()){
        // Open the selected fx (as ruby script) to the internal editor.
        try{
            // Get FxObject
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            if(dmtn.getUserObject() instanceof FxObject){
                FxObject fxo = (FxObject)dmtn.getUserObject();
                // Do
                switch(fxo.getFxObjectType()){
                    case XMLPreset: break;
                    case Ruby:
                        AssIO aio = new AssIO();
                        String text = aio.openRubyFile(fxo.getScriptPathname());
                        CodeEditorPanel.selectRuby();
                        CodeEditorPanel.getCodeEditor().setContentType("text/ruby");
//                        CodeEditorPanel.getCodeEditor().setComponentPopupMenu(popCode);
                        CodeEditorPanel.getCodeEditor().setText(text);
                        break;
                    case Python:
                        AssIO aio2 = new AssIO();
                        String text2 = aio2.openRubyFile(fxo.getScriptPathname());
                        CodeEditorPanel.selectPython();
                        CodeEditorPanel.getCodeEditor().setContentType("text/python");
//                        CodeEditorPanel.getCodeEditor().setComponentPopupMenu(popCode);
                        CodeEditorPanel.getCodeEditor().setText(text2);
                        break;
                    case JavaScript: break;
                    case Unknown: break;
                }
            }            
        }catch(Exception exc){
        }
    }else{
        try {
            // Get FxObject
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            if(dmtn.getUserObject() instanceof FxObject){
                FxObject fxo = (FxObject)dmtn.getUserObject();
                String command = rubyEditor.replace("%FILE", "\""+fxo.getScriptPathname()+"\"");
                // Open an external software to make ruby file
                Runtime.getRuntime().exec(command);
            }
        } catch (IOException ex) {
        }
    }
    }//GEN-LAST:event_btnModRubyActionPerformed

    private void btnCreateParticleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateParticleActionPerformed
        ParticleDialog pd = new ParticleDialog(frame, true);
        pd.setLocationRelativeTo(null);
        pd.setDrawingPath(drawingEditor);
        pd.setDrawingsPath(docs);
        pd.setStyles(feuille.MainFrame.getAssStyleCollection());
        ParticleObject po = pd.showDialog(new ParticleObject());
        if(pd.isOKButtonPressed()){
            tnPart.add(new DefaultMutableTreeNode(po));
            jTree1.updateUI();
        }
        if(pd.isSaveSelected()){
            modifyXmlParticles(new ParticleObject(), po, false);
        }
    }//GEN-LAST:event_btnCreateParticleActionPerformed

    private void btnEditParticleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditParticleActionPerformed
        try{
            DefaultMutableTreeNode dmtn = 
                    (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            if(dmtn.getUserObject() instanceof ParticleObject){
                ParticleObject po = (ParticleObject)dmtn.getUserObject();
                ParticleDialog pd = new ParticleDialog(frame, true);
                pd.setLocationRelativeTo(null);
                pd.setDrawingPath(drawingEditor);
                pd.setDrawingsPath(docs);
                pd.setStyles(feuille.MainFrame.getAssStyleCollection());
                po = pd.showDialog(po);
                if(pd.isOKButtonPressed()){
                    // Rien car l'appel de "po = pd.showDialog(po);" change
                    // déjà l'objet dans la liste si on appuie sur le bouton OK.
                }
                if(pd.isSaveSelected()){
                    modifyXmlParticles(new ParticleObject(), po, false);
                }
            }
        }catch(Exception exc){
            // Le try catch ne sert pratiquement à rien. Mais permet quand même
            // d'éviter le cas d'erreur quand on ne sélectionne aucun item.
            // Autrement dit, il prévient l'erreur de dmtn sans sélection d'item.
        }
    }//GEN-LAST:event_btnEditParticleActionPerformed

    private void btnDeleteParticleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteParticleActionPerformed
        try{
            DefaultMutableTreeNode dmtn = 
                    (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            if(dmtn.getUserObject() instanceof ParticleObject){
                ParticleObject po = (ParticleObject)dmtn.getUserObject();
                String message = localeLanguage.getValueOf("optpMessage2")!=null ?
                        localeLanguage.getValueOf("optpMessage2") :
                        "Would you really want to delete this item ?";
                String title = localeLanguage.getValueOf("optpTitle2")!=null ?
                        localeLanguage.getValueOf("optpTitle2") :
                        "Confirm";
                int n = JOptionPane.showConfirmDialog(this,message,title,
                        JOptionPane.YES_NO_OPTION);
                if (n==JOptionPane.YES_OPTION){
                    dmtn.setUserObject(null);
                    dmtn.removeFromParent();
                    jTree1.updateUI();
//                    modifyXmlPreset(new FxObject(), fxo, false);
                }
            }
        }catch(Exception exc){
            // Le try catch ne sert pratiquement à rien. Mais permet quand même
            // d'éviter le cas d'erreur quand on ne sélectionne aucun item.
            // Autrement dit, il prévient l'erreur de dmtn sans sélection d'item.
        }
    }//GEN-LAST:event_btnDeleteParticleActionPerformed

    private void popOrgCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popOrgCutActionPerformed
        // Cut lines to orgModel
        // Copy lines to orgModel
        AssIO aio = new AssIO();
        aio.copySelectedLines(orgModel, orgTable);
        // Delete selected lines from OrgTable
        try{
            int tabtemp[] = orgTable.getSelectedRows();
            for (int i=tabtemp.length-1;i>=0;i--){
                orgModel.removeRow(tabtemp[i]);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_popOrgCutActionPerformed

    private void popOrgCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popOrgCopyActionPerformed
        // Copy lines to orgModel
        AssIO aio = new AssIO();
        aio.copySelectedLines(orgModel, orgTable);
    }//GEN-LAST:event_popOrgCopyActionPerformed

    private void popOrgPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popOrgPasteActionPerformed
        // Paste lines to orgModel
        try{
            AssIO aio = new AssIO();
            aio.pasteInsert(orgModel, anc);
        }catch(java.io.IOException exc){}
    }//GEN-LAST:event_popOrgPasteActionPerformed

    private void popOrgDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popOrgDeleteActionPerformed
        // Delete selected lines from OrgTable
        try{
            int tabtemp[] = orgTable.getSelectedRows();
            for (int i=tabtemp.length-1;i>=0;i--){
                orgModel.removeRow(tabtemp[i]);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_popOrgDeleteActionPerformed

    private void popOrgClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popOrgClearActionPerformed
        // Clear all lines from OrgTable
        try{
            for (int i=orgModel.getRowCount()-1;i>=0;i--){
                orgModel.removeRow(i);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_popOrgClearActionPerformed

    private void popOrgRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popOrgRemoveActionPerformed
        // Remove effects in FX list.
        for(int i : orgTable.getSelectedRows()){
            orgModel.setValueAt("", i, 11);
        }
    }//GEN-LAST:event_popOrgRemoveActionPerformed

    private void popResCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popResCutActionPerformed
        // Cut lines to resModel
        // 1.Copy lines to resModel
        AssIO aio = new AssIO();
        aio.copySelectedLines(resModel, resTable);
        // 2.Delete selected lines from ResTable
        try{
            int tabtemp[] = resTable.getSelectedRows();
            for (int i=tabtemp.length-1;i>=0;i--){
                resModel.removeRow(tabtemp[i]);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_popResCutActionPerformed

    private void popResCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popResCopyActionPerformed
        // Copy lines to resModel
        AssIO aio = new AssIO();
        aio.copySelectedLines(resModel, resTable);
    }//GEN-LAST:event_popResCopyActionPerformed

    private void popResPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popResPasteActionPerformed
        // Paste lines to resModel
        try{
            AssIO aio = new AssIO();
            aio.pasteInsert(resModel, anc);
        }catch(java.io.IOException exc){}
    }//GEN-LAST:event_popResPasteActionPerformed

    private void popResDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popResDeleteActionPerformed
        // Delete selected lines from ResTable
        try{
            int tabtemp[] = resTable.getSelectedRows();
            for (int i=tabtemp.length-1;i>=0;i--){
                resModel.removeRow(tabtemp[i]);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_popResDeleteActionPerformed

    private void popResClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popResClearActionPerformed
        // Clear all lines from ResTable
        try{
            for (int i=resModel.getRowCount()-1;i>=0;i--){
                resModel.removeRow(i);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_popResClearActionPerformed

    private void mnuPopRfResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPopRfResetActionPerformed
        // Refresh ruby scripts list in the tree
        // Delete all ruby scripts
        tnRuby.removeAllChildren();
        //    cbButtonScript.removeAllItems();
        //    dcbmSplug.removeAllElements();
        jTree1.updateUI();
        // and search for all ruby scripts :
        //    searchForRubyScript(fxScripts);
        splug.searchForScript(docs);
        List<Object> sobjList = new ArrayList<Object>(splug.getSObjectList());
        for(Object o : sobjList){
            //        if(o instanceof SButton){
                //            SButton sb = (SButton)o;
                //            boolean found = false;
                //            for(int i=0;i<dcbmSplug.getSize();i++){
                    //                if(sb.equals(dcbmSplug.getElementAt(i))){found = true;}
                    //            }
                //            if(found==false){
                    //                dcbmSplug.addElement(sb);
                    //            }
                //        }
            if(o instanceof FxObject){
                FxObject fxo = (FxObject)o;
                boolean found = false;
                for(FxObject nfxo : getFxObjectListFromFxTree()){
                    if(nfxo.isTheSame(fxo)){found = true;}
                }
                if(found==false){
                    tnRuby.add(new DefaultMutableTreeNode(fxo));
                }
            }
        }
        jTree1.updateUI();
    }//GEN-LAST:event_mnuPopRfResetActionPerformed

    private void mnuPopRfInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPopRfInfoActionPerformed
        // Info about FxObject in the tree.
        try{
            DefaultMutableTreeNode dmtn =
            (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            if(dmtn.getUserObject() instanceof FxObject){
                FxObject fxo = (FxObject)dmtn.getUserObject();
                String sName = localeLanguage.getValueOf("optpGetInfo1")!=null ?
                localeLanguage.getValueOf("optpGetInfo1") : "Name : ";
                String sVersion = localeLanguage.getValueOf("optpGetInfo2")!=null ?
                localeLanguage.getValueOf("optpGetInfo2") : "Version : ";
                String sAuthor = localeLanguage.getValueOf("optpGetInfo3")!=null ?
                localeLanguage.getValueOf("optpGetInfo3") : "Author(s) : ";
                String sPath = localeLanguage.getValueOf("optpGetInfo4")!=null ?
                localeLanguage.getValueOf("optpGetInfo4") : "Path : ";
                String sFunction = localeLanguage.getValueOf("optpGetInfo5")!=null ?
                localeLanguage.getValueOf("optpGetInfo5") : "Function : ";
                String sDesc = localeLanguage.getValueOf("optpGetInfo6")!=null ?
                localeLanguage.getValueOf("optpGetInfo6") : "Description : ";
                JOptionPane.showMessageDialog(this,
                    sName+fxo.getName()+"\n"+
                    sVersion+fxo.getVersion()+"\n"+
                    sAuthor+fxo.getAuthor()+"\n"+
                    sPath+fxo.getScriptPathname()+"\n"+
                    sFunction+fxo.getFunction()+"\n\n"+
                    sDesc+fxo.getDescription());
            }else if(dmtn.getUserObject() instanceof ParticleObject){
                ParticleObject po = (ParticleObject)dmtn.getUserObject();
                String sName = localeLanguage.getValueOf("optpGetInfo1")!=null ?
                localeLanguage.getValueOf("optpGetInfo1") : "Name : ";
                String sVersion = localeLanguage.getValueOf("optpGetInfo2")!=null ?
                localeLanguage.getValueOf("optpGetInfo2") : "Version : ";
                String sAuthor = localeLanguage.getValueOf("optpGetInfo3")!=null ?
                localeLanguage.getValueOf("optpGetInfo3") : "Author(s) : ";
                String sPath = localeLanguage.getValueOf("optpGetInfo4")!=null ?
                localeLanguage.getValueOf("optpGetInfo4") : "Path : ";
                String sFunction = localeLanguage.getValueOf("optpGetInfo5")!=null ?
                localeLanguage.getValueOf("optpGetInfo5") : "Function : ";
                String sDesc = localeLanguage.getValueOf("optpGetInfo6")!=null ?
                localeLanguage.getValueOf("optpGetInfo6") : "Description : ";
                JOptionPane.showMessageDialog(this,
                    sName+po.getName()+"\n"+
                    sVersion+po.getVersion()+"\n"+
                    sAuthor+po.getAuthor()+"\n"+
                    sPath+po.getScriptPathname()+"\n"+
                    sFunction+po.getFunction()+"\n\n"+
                    sDesc+po.getDescription());
            }
        }catch(Exception e){

        }
    }//GEN-LAST:event_mnuPopRfInfoActionPerformed

    private void btnOpen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpen1ActionPerformed
        // Clear the list of file filters.
        for (FileFilter f : jFileChooser1.getChoosableFileFilters()){
            jFileChooser1.removeChoosableFileFilter(f);
        }

        // Add good file filters.
        jFileChooser1.addChoosableFileFilter(new WavFilter());
        jFileChooser1.setAccessory(null);
        // Action
        int z = this.jFileChooser1.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            //Clear the last images
            clearPngOfWavFiles();
            wp.init();
            //Then...
            String source = jFileChooser1.getSelectedFile().getAbsolutePath();
            //String image = source.substring(0, source.lastIndexOf("."))+".png";
            String image = waveformImageDirectory.getAbsolutePath()
            +File.separator+jFileChooser1.getSelectedFile().getName()+".png";
            wp.setImageFilePath(image);
            wp.createAudioInputStream(new java.io.File(source));
            hasWaveForm = true;
            wp.setSize(wp.getTotalPixels(), wp.getHeight());
            wp.setPreferredSize(new java.awt.Dimension(wp.getTotalPixels(), wp.getHeight()));
            jScrollBar1.setMaximum(wp.getTotalPixels()-jToolBar1.getWidth());
        }
    }//GEN-LAST:event_btnOpen1ActionPerformed

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        if(hasWaveForm==true){
            wp.playAllSound();
        }
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        if(hasWaveForm==true){
            wp.stopSound();
        }
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnPlayAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayAreaActionPerformed
        if(hasWaveForm==true){
            wp.playSoundFrom();
        }
    }//GEN-LAST:event_btnPlayAreaActionPerformed

    private void btnPlayBeforeBeginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayBeforeBeginActionPerformed
        if(hasWaveForm==true){
            wp.playSoundBeforeBegin();
        }
    }//GEN-LAST:event_btnPlayBeforeBeginActionPerformed

    private void btnPlayAfterBeginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayAfterBeginActionPerformed
        if(hasWaveForm==true){
            wp.playSoundAfterBegin();
        }
    }//GEN-LAST:event_btnPlayAfterBeginActionPerformed

    private void btnPlayBeforeEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayBeforeEndActionPerformed
        if(hasWaveForm==true){
            wp.playSoundBeforeEnd();
        }
    }//GEN-LAST:event_btnPlayBeforeEndActionPerformed

    private void btnPlayAfterEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayAfterEndActionPerformed
        if(hasWaveForm==true){
            wp.playSoundAfterEnd();
        }
    }//GEN-LAST:event_btnPlayAfterEndActionPerformed

    private void btnNewtimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewtimeActionPerformed
        if(hasWaveForm==true){
            ProgramLine pl = new ProgramLine();

            if(orgTable.getSelectedRow()!=-1){
                String line = ScriptPlugin.getOrgLine(orgTable.getSelectedRow());
                pl = AssIO.Format(line, AssIO.ModeFormat.ASSToProgram);
            }else{
                pl.setLineType(ProgramLine.LineType.Dialogue);
                pl.setText(tfTimeKara.getText());
            }

            System.out.println(wp.getStart());
            String sStart[] = wp.getStart().split("\\.");
            pl.setStart(sStart[0], sStart[1], sStart[2], sStart[3]);
            String sEnd[] = wp.getEnd().split("\\.");
            pl.setEnd(sEnd[0], sEnd[1], sEnd[2], sEnd[3]);
            pl.setTotaltime(pl.getStart(), pl.getEnd());
            if(popmToSelected.isSelected()){
                int srow = orgTable.getSelectedRow();
                orgModel.removeRow(srow);
                orgModel.insertRow(srow, pl.toRow());
            }else{//popmDirectly.isSelected();
                orgModel.addRow(pl.toRow());
            }
        }
    }//GEN-LAST:event_btnNewtimeActionPerformed

    private void btnSetKaraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetKaraActionPerformed
        //wp.modifyTags("Wa/ta/shi/ no/ ku/ru/ma/ wa/ a/ka/i/ de/su", 250, 750);
        if(tfTimeKara.getText().contains("/")){
            wp.modifyTags(tfTimeKara.getText());
        }else if (tfTimeKara.getText().contains("\\k")){
            wp.modifyKaraokeTags(tfTimeKara.getText());
        }
    }//GEN-LAST:event_btnSetKaraActionPerformed

    private void btnGetKaraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetKaraActionPerformed
        if(hasWaveForm==true){
            if(orgTable.getSelectedRow()!=-1){
                String line = ScriptPlugin.getOrgLine(orgTable.getSelectedRow());
                ProgramLine pl = AssIO.Format(line, AssIO.ModeFormat.ASSToProgram);
                pl.setText(wp.getKaraoke());
                pl.setLineType(ProgramLine.LineType.Karaoke);
                int srow = orgTable.getSelectedRow();
                orgModel.removeRow(srow);
                orgModel.insertRow(srow, pl.toRow());
                wp.resetTags();
            }
        }
        //String sentence = wp.getKaraoke();
        //System.out.println("Sentence >> "+sentence);
    }//GEN-LAST:event_btnGetKaraActionPerformed

    private void jScrollBar1AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBar1AdjustmentValueChanged
        //        wp.setLocation(-evt.getValue(), jToolBar1.getHeight());
        wp.setLocation(-evt.getValue(), 0);
        wp.setDisplayZone(Math.abs(evt.getValue()));
        xSidebar = evt.getValue();
    }//GEN-LAST:event_jScrollBar1AdjustmentValueChanged

    private void popmGetSelLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmGetSelLineActionPerformed
        String line = ScriptPlugin.getOrgLine(orgTable.getSelectedRow());
        tfTimeKara.setText(ScriptPlugin.getSentence(line));
    }//GEN-LAST:event_popmGetSelLineActionPerformed

    private void bStylesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStylesActionPerformed
        AssStylesDialog asd = new AssStylesDialog(frame,true);
        asd.setLocationRelativeTo(null);
        //Add the script list
        asd.setScriptList(feuille.MainFrame.getAssStyleCollection());
        //Add the embedded list
        List<FxObject> lfxo = getFxObjectListFromFxTree();
        List<AssStyle> las;
        for(FxObject fxo : lfxo){
            if(fxo.getStyle().isEmpty()==false){
                las = AssStyle.unlinkAssStyles(fxo.getStyle());
                for(AssStyle as : las){
                    asd.addEmbeddedStyle(as);
                }
            }
        }
        //Add the stored list
        asd.setStoredList(feuille.MainFrame.getStylesPack());
        asd.showDialog();
        feuille.MainFrame.setAssStyleCollection(asd.getScriptList());
        feuille.MainFrame.setStylesPack(asd.getStoredList());
        XmlStylesPackWriter xspw = new XmlStylesPackWriter();
        xspw.setStylesPackList(feuille.MainFrame.getStylesPack());
        xspw.createStylesPack(docs+"packages.styles"); 
    }//GEN-LAST:event_bStylesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bStyles;
    private javax.swing.ButtonGroup bgOriginal;
    private javax.swing.ButtonGroup bgResult;
    private javax.swing.JButton btnAddFxoToLine;
    private javax.swing.JButton btnAddXmlPresetFx;
    private javax.swing.JButton btnBlock;
    private javax.swing.JButton btnCreateParticle;
    private javax.swing.JButton btnDelXmlPresetFx;
    private javax.swing.JButton btnDeleteParticle;
    private javax.swing.JButton btnEditParticle;
    private javax.swing.JButton btnExpXmlPresetFx;
    private javax.swing.JButton btnGetKara;
    private javax.swing.JButton btnImpXmlPresetFx;
    private javax.swing.JButton btnModRuby;
    private javax.swing.JButton btnModXmlPresetFx;
    private javax.swing.JButton btnNewtime;
    private javax.swing.JButton btnOneLine;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnOpen1;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPlayAfterBegin;
    private javax.swing.JButton btnPlayAfterEnd;
    private javax.swing.JButton btnPlayArea;
    private javax.swing.JButton btnPlayBeforeBegin;
    private javax.swing.JButton btnPlayBeforeEnd;
    private javax.swing.JButton btnSaveOri;
    private javax.swing.JButton btnSaveRes;
    private javax.swing.JButton btnSetKara;
    private javax.swing.JButton btnStop;
    private javax.swing.JInternalFrame ifrOriginal;
    private javax.swing.JInternalFrame ifrResult;
    private javax.swing.JInternalFrame ifrSound;
    private javax.swing.JInternalFrame ifrTree;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private static javax.swing.JTree jTree1;
    private javax.swing.JMenuItem mnuPopRfInfo;
    private javax.swing.JMenuItem mnuPopRfReset;
    private javax.swing.JTable orgTable;
    private javax.swing.JPopupMenu popOrg;
    private javax.swing.JMenuItem popOrgClear;
    private javax.swing.JMenuItem popOrgCopy;
    private javax.swing.JMenuItem popOrgCut;
    private javax.swing.JMenuItem popOrgDelete;
    private javax.swing.JMenuItem popOrgPaste;
    private javax.swing.JMenuItem popOrgRemove;
    private javax.swing.JPopupMenu.Separator popOrgSep1;
    private javax.swing.JPopupMenu.Separator popOrgSep2;
    private javax.swing.JPopupMenu popRes;
    private javax.swing.JMenuItem popResClear;
    private javax.swing.JMenuItem popResCopy;
    private javax.swing.JMenuItem popResCut;
    private javax.swing.JMenuItem popResDelete;
    private javax.swing.JMenuItem popResPaste;
    private javax.swing.JPopupMenu.Separator popResSep;
    private javax.swing.JPopupMenu popResetFx;
    private javax.swing.JPopupMenu popSetGetKaraoke;
    private javax.swing.JPopupMenu popTimeKara;
    private javax.swing.JRadioButtonMenuItem popmDirectly;
    private javax.swing.JMenuItem popmGetSelLine;
    private javax.swing.JRadioButtonMenuItem popmToSelected;
    private javax.swing.JPanel realWavePanel;
    private javax.swing.JTable resTable;
    private javax.swing.JToggleButton tbItemsOri;
    private javax.swing.JToggleButton tbItemsRes;
    private javax.swing.JToggleButton tbNormalOri;
    private javax.swing.JToggleButton tbNormalRes;
    private javax.swing.JToggleButton tbStripOri;
    private javax.swing.JToggleButton tbStripRes;
    private javax.swing.JTextField tfTimeKara;
    private javax.swing.JPanel toolbarPanel;
    // End of variables declaration//GEN-END:variables
}
