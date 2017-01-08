/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import feuille.analysis.AnalysisPanel;
import feuille.codeeditor.CodeEditorPanel;
import feuille.drawing.DrawingPanel;
import feuille.karaoke.KaraokePanel;
import feuille.karaoke.dialog.AboutDialog;
import feuille.karaoke.dialog.OnLineHelpDialog;
import feuille.karaoke.lib.AssIO;
import feuille.karaoke.lib.AssInfos;
import feuille.karaoke.lib.AssNameCollection;
import feuille.karaoke.lib.AssStyle;
import feuille.karaoke.lib.AssStyleCollection;
import feuille.karaoke.lib.FontWithCoef;
import feuille.karaoke.lib.ProblemFont;
import feuille.karaoke.lib.StylesPack;
import feuille.karaoke.lib.XmlProblemFontHandler;
import feuille.karaoke.lib.XmlProblemFontWriter;
import feuille.karaoke.lib.XmlStylesPackHandler;
import feuille.lib.Language;
import feuille.karaoke.renderer.ComboBoxButtonRenderer;
import feuille.lib.Configuration;
import feuille.lib.OptionsDialog2;
import feuille.lib.SplashScreen;
import feuille.scripting.SButton;
import feuille.scripting.ScriptPlugin;
import feuille.theme.Theme;
import feuille.theme.ThemeCollection;
import feuille.welcome.WelcomePanel;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author The Wingate 2940
 */
public class MainFrame extends javax.swing.JFrame {
    
    // Première chose à charger
    SplashScreen ssc = new SplashScreen();
    
    // Variables pour la classe principale
    private JDesktopPane desk;
    private BufferedImage backgroundimage;
    private String DOCSPATH = "E:\\Dev\\Projets\\Java\\Feuille\\docs\\";
    private String CODEPATH = "";
    private int videoWidth = 1280;
    private int videoHeight = 720;
    private DefaultComboBoxModel dcbmSplug = null;
    private ComboBoxButtonRenderer cbbr = null;
    private String force_ISO = "---";
    private ProblemFont pf = new ProblemFont();    
    private Configuration cfg = new Configuration();
    private ThemeCollection themecollection = new ThemeCollection();
    private String theme = "";
    private boolean chkUpdate = true;
    private boolean karaModule = true, codeModule = true;
    private boolean drawModule = true, analModule = true;
    private String startWith = "";
    
    // Variables pouvant être partagées dans le logiciel
    // (voir méthodes get-set correspondantes)
    private static AssInfos ai = new AssInfos();
    private static AssNameCollection anc = new AssNameCollection("");
    private static AssStyleCollection ascScript = new AssStyleCollection();
    private static ScriptPlugin splug;
    private static Language localeLanguage;
    private static Frame frame;
    private static List<StylesPack> listStylesPack;
    
    
    // Panels à charger
    KaraokePanel kp;
    CodeEditorPanel cep;
    DrawingPanel dp;
    AnalysisPanel ap;
    WelcomePanel wp;
    JInternalFrame kpOriginal, kpResult, kpSound, kpTree, cepCode,
            dpFile, dpDraw, dpImage, dpShape, dpMode, dpOps, dpScripts,
            dpHistoric, dpLayers, dpOrnament, dpSheet, dpAssComs,
            apFirstTable, apFirstReport, apSecondTable, apSecondReport,
            wpWelcome;
    

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        ssc.nowPrint("<<<Feuille>>>");
        beforeAll();        
        ssc.setVisible(true);
        ssc.loadingInfo("Initialize the components", 0);
        initComponents();
        ssc.loadedInfo("Components", 5);
        init();
        ssc.setVisible(false);
        ssc.nowPrint("<<<Ready>>>");
        ssc.dispose();
    }
    
    private void beforeAll(){
        frame = this;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Initialisation">
    /** Initialise le programme. */
    private void init(){
        
        ssc.loadingInfo("Font to use", 5);
        
        // 1. Variables à utiliser dans cette méthode.
        Font fontToUse = getFont();
        if((new File(DOCSPATH)).exists()==false | (new File(getApplicationDirectory()+"\\docs\\")).exists()==false){
            DOCSPATH = getApplicationDirectory()+"\\docs\\"; //Défaut
            File f = new File(DOCSPATH);
            f.mkdir();
        }else{
            DOCSPATH = getApplicationDirectory()+"\\docs\\"; //Défaut
        }
        
        ssc.loadedInfo("Font to use", 10);
        ssc.loadingInfo("Configuration", 10);
        
        // 2. Configuration
        cfg.put(Configuration.Type.DOCS_PATH, DOCSPATH);
        cfg.put(Configuration.Type.CODE_EDITOR, CODEPATH);
        cfg.put(Configuration.Type.FONT, "");
        cfg.put(Configuration.Type.BACKGD_IMAGE, "");
        cfg.put(Configuration.Type.THEME, "");
        cfg.put(Configuration.Type.ORG_TABLE_DISPLAY, "");
        cfg.put(Configuration.Type.RES_TABLE_DISPLAY, "");
        cfg.put(Configuration.Type.CHK_UPDATE, "");
        cfg.put(Configuration.Type.FORCE_ISO, force_ISO);
        cfg.put(Configuration.Type.KARA_MODULE, "yes");
        cfg.put(Configuration.Type.CODE_MODULE, "yes");
        cfg.put(Configuration.Type.DRAW_MODULE, "yes");
        cfg.put(Configuration.Type.ANAL_MODULE, "yes");
        cfg.put(Configuration.Type.STARTWITH, "welc");
        
        //-- On lit le fichier de configuration (on le crée d'abord s'il n'existe pas) 
        AssIO aio = new AssIO();
        if(!aio.HasConfigFile()){//If there is no file then create a new file
            aio.createConfigFile();
        }
        cfg = aio.ReadConfig();
        //-- Chargement du répertoire des documents
        DOCSPATH = cfg.get(Configuration.Type.DOCS_PATH);
        //-- Chargement du répertoire de l'éditeur de code
        CODEPATH =  cfg.get(Configuration.Type.CODE_EDITOR);
        //-- Chargement de la police pour les tableaux
        try{
            fontToUse = Font.createFont(Font.TRUETYPE_FONT, new File(cfg.get(Configuration.Type.FONT))).deriveFont(12f);
        }catch(  FontFormatException | IOException ffe){
            fontToUse = getFont();
        }
        //-- Chargement de l'image du fond d'écran
        try {
            backgroundimage = ImageIO.read(new File(cfg.get(Configuration.Type.BACKGD_IMAGE)));
        } catch (IOException ex) {
            backgroundimage = null;
        }
        //-- Chargement du thème
        theme = cfg.get(Configuration.Type.THEME);
        //-- On chargera la configuration des tables après leur création
        //-- (donc plus loin dans cette initialisation)
        //-- Chargement de l'indice de la langue
        force_ISO = cfg.get(Configuration.Type.FORCE_ISO);
        //-- Chargement des modules
        if(cfg.get(Configuration.Type.KARA_MODULE).equalsIgnoreCase("yes")){
            karaModule = true;
        }else{
            karaModule = false;
        }
        if(cfg.get(Configuration.Type.CODE_MODULE).equalsIgnoreCase("yes")){
            codeModule = true;
        }else{
            codeModule = false;
        }
        if(cfg.get(Configuration.Type.DRAW_MODULE).equalsIgnoreCase("yes")){
            drawModule = true;
        }else{
            drawModule = false;
        }
        if(cfg.get(Configuration.Type.ANAL_MODULE).equalsIgnoreCase("yes")){
            analModule = true;
        }else{
            analModule = false;
        }
        //-- Chargement démarrage par le module
        startWith = cfg.get(Configuration.Type.STARTWITH);
        
        // X. Ouverture et lecture du fichier de configuration
        //TODO
//        try {
//            backgroundimage = ImageIO.read(new File(DOCSPATH+"wall_01.jpg"));
//        } catch (IOException ex) { }
//        try{
//           fontToUse = Font.createFont(Font.TRUETYPE_FONT, new File(DOCSPATH+"FreeSans.ttf")).deriveFont(12f);
//        }catch(FontFormatException ffe){ } catch (IOException ex) { }
        
        ssc.loadedInfo("Configuration", 20);
        ssc.loadingInfo("Language", 20);
        
        // 3. Récupération de la langue
        localeLanguage = new Language(Locale.getDefault(), force_ISO, DOCSPATH);
        if(localeLanguage.getValueOf("tabWelcome")!=null){
            tbWelcome.setToolTipText(localeLanguage.getValueOf("tabWelcome"));}
        if(localeLanguage.getValueOf("tabKaraoke")!=null){
            tbKaraoke.setToolTipText(localeLanguage.getValueOf("tabKaraoke"));}
        if(localeLanguage.getValueOf("tabRubyEdi")!=null){
            tbCodeEditor.setToolTipText(localeLanguage.getValueOf("tabRubyEdi"));}
        if(localeLanguage.getValueOf("tabDrawing")!=null){
            tbDrawing.setToolTipText(localeLanguage.getValueOf("tabDrawing"));}
        if(localeLanguage.getValueOf("tabAnalysis")!=null){
            tbAnalysis.setToolTipText(localeLanguage.getValueOf("tabAnalysis"));}
        if(localeLanguage.getValueOf("toolQuit")!=null){
            btnQuit.setToolTipText(localeLanguage.getValueOf("toolQuit"));}
        if(localeLanguage.getValueOf("toolBScriptOK")!=null){
            bScriptOK.setToolTipText(localeLanguage.getValueOf("toolBScriptOK"));}        
        if(localeLanguage.getValueOf("menuFile")!=null){
            jMenu1.setText(localeLanguage.getValueOf("menuFile"));}
        if(localeLanguage.getValueOf("menuGoWel")!=null){
            popmFileWelcome.setText(localeLanguage.getValueOf("menuGoWel"));}
        if(localeLanguage.getValueOf("menuGoKara")!=null){
            popmFileKaraoke.setText(localeLanguage.getValueOf("menuGoKara"));}
        if(localeLanguage.getValueOf("menuGoCode")!=null){
            popmFileCodeEditor.setText(localeLanguage.getValueOf("menuGoCode"));}
        if(localeLanguage.getValueOf("menuGoDraw")!=null){
            popmFileDrawing.setText(localeLanguage.getValueOf("menuGoDraw"));}
        if(localeLanguage.getValueOf("menuGoAna")!=null){
            popmFileAnalysis.setText(localeLanguage.getValueOf("menuGoAna"));}
        if(localeLanguage.getValueOf("menuConf")!=null){
            popmFileConfig.setText(localeLanguage.getValueOf("menuConf"));}
        if(localeLanguage.getValueOf("menuQuit")!=null){
            popmFileQuit.setText(localeLanguage.getValueOf("menuQuit"));}
        if(localeLanguage.getValueOf("menuRess")!=null){
            popmHelpOnlineHelp.setText(localeLanguage.getValueOf("menuRess"));}
        if(localeLanguage.getValueOf("menuAbout")!=null){
            popmHelpAbout.setText(localeLanguage.getValueOf("menuAbout"));}
        
        ssc.loadedInfo("Language", 25);
        ssc.loadingInfo("Custom", 25);
        
        // 4. Customisation et configuration du desktop pane
//        if(backgroundimage!=null){
//            desk = new JDesktopPane() {
//                @Override
//                protected void paintComponent(Graphics g) {
//                    super.paintComponent(g);
//                    g.drawImage(backgroundimage, 0, 0, null);
//                }
//            };
//        }else{
//            desk = new JDesktopPane();
//        }
        
        desk = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(backgroundimage!=null){
                    g.drawImage(backgroundimage, 0, 0, null);
                }
            }
        };
        mainPanel.add(desk, BorderLayout.CENTER);
        
        ssc.loadedInfo("Custom", 30);
        ssc.loadingInfo("Look and Feel", 30);
        
        // 5. Essaie de changer le look & feel pour Nimbus
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        ssc.loadedInfo("Look and Feel", 40);
        ssc.loadingInfo("Size and location", 40);
        
        // 6. Met les bonnes dimension (le maximum possible)
        //-- dim >> Obtient la taille de l'écran
        //-- gconf >> Obtient la configuration de l'écran
        //-- insets >> Obtient les 'marges' de l'écran
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension dim = toolkit.getScreenSize();
        java.awt.GraphicsConfiguration gconf = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        java.awt.Insets insets = toolkit.getScreenInsets(gconf);
        setSize(dim.width - insets.left - insets.right,
                dim.height - insets.top - insets.bottom);
        
        ssc.loadedInfo("Size and location", 50);
        ssc.loadingInfo("Internal frames", 50);
        
        // 7. Création des iFrames (JInternalFrame)
        if(karaModule==true | codeModule==true){
            kp = new KaraokePanel(DOCSPATH, fontToUse, localeLanguage, this);
            cep = new CodeEditorPanel(DOCSPATH, this, localeLanguage);
        }
        if(drawModule==true){
            dp = new DrawingPanel(DOCSPATH, this, localeLanguage);
        }
        if(analModule==true){
            ap = new AnalysisPanel(this);
        }        
        wp = new WelcomePanel();
        
        
        ssc.loadedInfo("Internal frames", 60);
        ssc.loadingInfo("Effects loading", 60);
        
        // 8. Chargement de la liste des effets
        splug = new ScriptPlugin(this);
        splug.setPaths(DOCSPATH, DOCSPATH, DOCSPATH);
        splug.setModelsForManagement(
                feuille.karaoke.KaraokePanel.getOriginalTableModel(),
                feuille.karaoke.KaraokePanel.getResultTableModel());
        splug.setVideoSize(videoWidth, videoHeight);
        splug.setAssStyleCollection(ascScript);
        dcbmSplug = new DefaultComboBoxModel();
        cbbr = new ComboBoxButtonRenderer();
        cbButtonScript.setModel(dcbmSplug);
        cbButtonScript.setRenderer(cbbr);
        try{
            splug.searchForScript(DOCSPATH);
        }catch(Exception e){
            
        }        
        List<Object> sobjList = new ArrayList<>(splug.getSObjectList());        
        for(Object o : sobjList){
            if(o instanceof SButton){
                SButton sb = (SButton)o;
                boolean found = false;
                for(int i=0;i<dcbmSplug.getSize();i++){
                    if(sb.equals(dcbmSplug.getElementAt(i))){found = true;}
                }
                if(found==false){
                    dcbmSplug.addElement(sb);
                    System.out.println("Ruby ou Python plugin : \""+sb.getDisplayName()+"\" by "+sb.getAuthors());
                }                
            }
        }
        
        ssc.loadedInfo("Effects loading", 70);
        ssc.loadingInfo("Internal frames size and location", 70);
        
        // 9. Configuration des iFrames
        if(karaModule==true | codeModule==true){
            kp.setScriptPlugin(splug);
            kp.setCodeEditor(CODEPATH);
            kpOriginal = kp.getOriginalTable();
            kpResult = kp.getResultTable();
            kpSound = kp.getWaveform();
            kpTree = kp.getTree();
            kpOriginal.setLocation(10, 10);
            kpOriginal.setSize(desk.getWidth()-210, desk.getHeight()/2-20);
            kpResult.setLocation(10, desk.getHeight()/2);
            kpResult.setSize(desk.getWidth()-210, desk.getHeight()/2-20);
            kpSound.setLocation(10, desk.getHeight()/2);
            kpSound.setSize(desk.getWidth()-210, desk.getHeight()/2-50);
            kpTree.setLocation(desk.getWidth()-190, 10);
            kpTree.setSize(180, desk.getHeight()-30);
            desk.add(kpOriginal);
            desk.add(kpResult);
            desk.add(kpSound);
            desk.add(kpTree);
            cep.setScriptPlugin(splug);
            cepCode = cep.getCode();
            cepCode.setLocation(10, 10);
            cepCode.setSize(desk.getWidth()-210, desk.getHeight()-30);
            desk.add(cepCode);
        }
        if(karaModule==false){
            tbKaraoke.setEnabled(false);
            popmFileKaraoke.setEnabled(false);
        }
        if(codeModule==false){
            tbCodeEditor.setEnabled(false);
            popmFileCodeEditor.setEnabled(false);
        }
        if(drawModule==true){
            dp.setScriptPlugin(splug);
            dpFile = dp.getIfrFile();
            dpDraw = dp.getIfrDraw();
            dpImage = dp.getIfrImage();
            dpShape = dp.getIfrShape();
            dpMode = dp.getIfrMode();
            dpOps = dp.getIfrOperations();
            dpScripts = dp.getIfrScripts();
            dpHistoric = dp.getIfrHistoric();
            dpLayers = dp.getIfrLayers();
            dpOrnament = dp.getIfrOrnament();
            dpSheet = dp.getIfrSketchpad();
            dpAssComs = dp.getIfrAssCommands();
            dpHistoric.setLocation(desk.getWidth()-190, 340);
            dpLayers.setLocation(desk.getWidth()-190, 700);
            dpOrnament.setLocation(desk.getWidth()-190, 90);
            dpSheet.setSize(desk.getWidth()-400, desk.getHeight()-100);
            dpAssComs.setSize(desk.getWidth()-210, dpAssComs.getHeight());
            desk.add(dpFile);
            desk.add(dpDraw);
            desk.add(dpImage);
            desk.add(dpShape);
            desk.add(dpMode);
            desk.add(dpOps);
            desk.add(dpScripts);
            desk.add(dpHistoric);
            desk.add(dpLayers);
            desk.add(dpOrnament);
            desk.add(dpSheet);
            desk.add(dpAssComs);
        }else{
            tbDrawing.setEnabled(false);
            popmFileDrawing.setEnabled(false);
        }
        if(analModule==true){
            apFirstTable = ap.getIfrFirstTable();
            apFirstReport = ap.getIfrFirstReport();
            apSecondTable = ap.getIfrSecondTable();
            apSecondReport = ap.getIfrSecondReport();
            apFirstTable.setLocation(10,10);
            apFirstTable.setSize(desk.getWidth()-580, desk.getHeight()/2-20);
            apSecondTable.setLocation(10,desk.getHeight()/2);
            apSecondTable.setSize(desk.getWidth()-580, desk.getHeight()/2-20);
            apFirstReport.setLocation(desk.getWidth()-560, 10);
            apFirstReport.setSize(550, desk.getHeight()/2-20);
            apSecondReport.setLocation(desk.getWidth()-560, desk.getHeight()/2);
            apSecondReport.setSize(550, desk.getHeight()/2-20);
            desk.add(apFirstTable);
            desk.add(apFirstReport);
            desk.add(apSecondTable);
            desk.add(apSecondReport);
        }else{
            tbAnalysis.setEnabled(false);
            popmFileAnalysis.setEnabled(false);
        }
        wpWelcome = wp.getWelcome();
        wpWelcome.setLocation((desk.getWidth()-wpWelcome.getWidth())/2, (desk.getHeight()-wpWelcome.getHeight())/2);
        desk.add(wpWelcome);
        hideElements();
        if(karaModule==true && startWith.equalsIgnoreCase("kara")){
            showKaraokeElements();
            tbKaraoke.setSelected(true);
        }else if(codeModule==true && startWith.equalsIgnoreCase("code")){
            showCodeEdElements();
            tbCodeEditor.setSelected(true);
        }else if(drawModule==true && startWith.equalsIgnoreCase("draw")){
            showDrawEdElements();
            tbDrawing.setSelected(true);
        }else if(analModule==true && startWith.equalsIgnoreCase("anal")){
            showAnalysisElements();
            tbAnalysis.setSelected(true);
        }else{ //startWith = "welc"
            wpWelcome.setVisible(true);
        }        
        
        ssc.loadedInfo("Internal frames size and location", 80);
        ssc.loadingInfo("Scripts", 80);
        
        // 10. Chargement de scripts
        if(karaModule==true){
            kp.addScriptsToList(sobjList);
        }        
        
        ssc.loadedInfo("Scripts", 85);
        ssc.loadingInfo("Fonts configuration", 85);
        
        // 11. Chargement des configurations de fontes
        try{
            XmlProblemFontHandler xph = new XmlProblemFontHandler(DOCSPATH+"problemfont.pfont");
            List<FontWithCoef> xpo = xph.getFontWithCoefList();
            for(FontWithCoef p : xpo){
                pf.addFont(p);
            }
        }catch(ParserConfigurationException | SAXException | IOException exc){
        }
        
        ssc.loadedInfo("Fonts configuration", 90);
        ssc.loadingInfo("Styles packs", 90);
        
        // 12. Set the list of StylesPack
        listStylesPack = new ArrayList<>();
        // Try to find styles groups recorded as package.
        try {
            listStylesPack = XmlStylesPackHandler.startProcess(DOCSPATH+"packages.styles");
        } catch (Exception ex) {
        }
        // Verify if the Default style exist in the list. Otherwise restore it.
        boolean stylePackExist = false;
        for(StylesPack sp : listStylesPack){
            if(sp.getPack().equalsIgnoreCase("Default")){
                stylePackExist = true;
            }
        }
        //Restore it if needed.
        if(stylePackExist==false){
            AssStyleCollection ascPack = new AssStyleCollection();
            ascPack.addMember("Default", new AssStyle());
            StylesPack sPack = new StylesPack("Default", ascPack);
            listStylesPack.add(sPack);
        }
        
        ssc.loadedInfo("Styles packs", 95);
        ssc.loadingInfo("Theme", 95);
        
        // 13. Thème
        themecollection.setup();
        changeTheme(themecollection.getTheme(theme));
        
        ssc.loadedInfo("Theme", 100);
        
    }
    // </editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgMainAction = new javax.swing.ButtonGroup();
        jToolBar2 = new javax.swing.JToolBar();
        btnQuit = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        tbWelcome = new javax.swing.JToggleButton();
        tbKaraoke = new javax.swing.JToggleButton();
        tbCodeEditor = new javax.swing.JToggleButton();
        tbDrawing = new javax.swing.JToggleButton();
        tbAnalysis = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        cbButtonScript = new javax.swing.JComboBox();
        bScriptOK = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        popmFileWelcome = new javax.swing.JMenuItem();
        popmFileKaraoke = new javax.swing.JMenuItem();
        popmFileCodeEditor = new javax.swing.JMenuItem();
        popmFileDrawing = new javax.swing.JMenuItem();
        popmFileAnalysis = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        popmFileConfig = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        popmFileQuit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        popmHelpOnlineHelp = new javax.swing.JMenuItem();
        popmHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Feuille");

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/48px-Crystal_Clear_action_exit.png"))); // NOI18N
        btnQuit.setToolTipText("Quitter");
        btnQuit.setFocusable(false);
        btnQuit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitActionPerformed(evt);
            }
        });
        jToolBar2.add(btnQuit);
        jToolBar2.add(jSeparator1);

        bgMainAction.add(tbWelcome);
        tbWelcome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/48px-Crystal_Clear_app_kteatime.png"))); // NOI18N
        tbWelcome.setSelected(true);
        tbWelcome.setToolTipText("Bienvenue");
        tbWelcome.setFocusable(false);
        tbWelcome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbWelcome.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbWelcome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbWelcomeActionPerformed(evt);
            }
        });
        jToolBar2.add(tbWelcome);

        bgMainAction.add(tbKaraoke);
        tbKaraoke.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/48px-Crystal_Clear_app_kmid.png"))); // NOI18N
        tbKaraoke.setToolTipText("Karaoké");
        tbKaraoke.setFocusable(false);
        tbKaraoke.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbKaraoke.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbKaraoke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbKaraokeActionPerformed(evt);
            }
        });
        jToolBar2.add(tbKaraoke);

        bgMainAction.add(tbCodeEditor);
        tbCodeEditor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/48px-Crystal_Clear_app_kedit.png"))); // NOI18N
        tbCodeEditor.setToolTipText("Editeur de code");
        tbCodeEditor.setFocusable(false);
        tbCodeEditor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbCodeEditor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbCodeEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbCodeEditorActionPerformed(evt);
            }
        });
        jToolBar2.add(tbCodeEditor);

        bgMainAction.add(tbDrawing);
        tbDrawing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/48px-Crystal_Clear_app_gimp.png"))); // NOI18N
        tbDrawing.setToolTipText("Dessin");
        tbDrawing.setFocusable(false);
        tbDrawing.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbDrawing.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbDrawing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbDrawingActionPerformed(evt);
            }
        });
        jToolBar2.add(tbDrawing);

        bgMainAction.add(tbAnalysis);
        tbAnalysis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/48px-Crystal_Clear_app_kappfinder.png"))); // NOI18N
        tbAnalysis.setToolTipText("Analyse");
        tbAnalysis.setFocusable(false);
        tbAnalysis.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbAnalysis.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbAnalysis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbAnalysisActionPerformed(evt);
            }
        });
        jToolBar2.add(tbAnalysis);
        jToolBar2.add(jSeparator2);

        cbButtonScript.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbButtonScript.setPreferredSize(new java.awt.Dimension(200, 48));
        jToolBar2.add(cbButtonScript);

        bScriptOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/48px-Crystal_Clear_action_forward.png"))); // NOI18N
        bScriptOK.setToolTipText("Lancer");
        bScriptOK.setFocusable(false);
        bScriptOK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bScriptOK.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bScriptOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bScriptOKActionPerformed(evt);
            }
        });
        jToolBar2.add(bScriptOK);

        mainPanel.setLayout(new java.awt.BorderLayout());

        jMenu1.setText("Fichier");

        popmFileWelcome.setText("Aller à Bienvenue");
        popmFileWelcome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFileWelcomeActionPerformed(evt);
            }
        });
        jMenu1.add(popmFileWelcome);

        popmFileKaraoke.setText("Aller à Karaoké");
        popmFileKaraoke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFileKaraokeActionPerformed(evt);
            }
        });
        jMenu1.add(popmFileKaraoke);

        popmFileCodeEditor.setText("Aller à Editeur de code");
        popmFileCodeEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFileCodeEditorActionPerformed(evt);
            }
        });
        jMenu1.add(popmFileCodeEditor);

        popmFileDrawing.setText("Aller à Dessin");
        popmFileDrawing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFileDrawingActionPerformed(evt);
            }
        });
        jMenu1.add(popmFileDrawing);

        popmFileAnalysis.setText("Aller à Analyse");
        popmFileAnalysis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFileAnalysisActionPerformed(evt);
            }
        });
        jMenu1.add(popmFileAnalysis);
        jMenu1.add(jSeparator3);

        popmFileConfig.setText("Configuration...");
        popmFileConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFileConfigActionPerformed(evt);
            }
        });
        jMenu1.add(popmFileConfig);
        jMenu1.add(jSeparator4);

        popmFileQuit.setText("Quitter");
        popmFileQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFileQuitActionPerformed(evt);
            }
        });
        jMenu1.add(popmFileQuit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("?");

        popmHelpOnlineHelp.setText("Ressources en ligne...");
        popmHelpOnlineHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmHelpOnlineHelpActionPerformed(evt);
            }
        });
        jMenu2.add(popmHelpOnlineHelp);

        popmHelpAbout.setText("A propos de...");
        popmHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmHelpAboutActionPerformed(evt);
            }
        });
        jMenu2.add(popmHelpAbout);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 1162, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitActionPerformed
        // Quitter l'application
        //int z = JOptionPane.showConfirmDialog(this, "Confirmez-vous la fermeture du logiciel ?", "Fermeture", JOptionPane.YES_NO_OPTION);
        int z = JOptionPane.showConfirmDialog(this, "Do you really want to quit the program ?", "Closing", JOptionPane.YES_NO_OPTION);
        if(z == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_btnQuitActionPerformed

    private void tbWelcomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbWelcomeActionPerformed
        // Affiche les iFrames pour la pause et cache les autres.
        hideElements();
        wpWelcome.setVisible(true);
    }//GEN-LAST:event_tbWelcomeActionPerformed

    private void tbKaraokeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbKaraokeActionPerformed
        // Affiche les iFrames pour le karaoke et cache les autres.
        hideElements();
        showKaraokeElements();
    }//GEN-LAST:event_tbKaraokeActionPerformed

    private void tbCodeEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbCodeEditorActionPerformed
        // Affiche les iFrames pour l'éditeur de code et cache les autres.
        hideElements();
        showCodeEdElements();
    }//GEN-LAST:event_tbCodeEditorActionPerformed

    private void tbDrawingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbDrawingActionPerformed
        // Affiche les iFrames pour le dessin et cache les autres.
        hideElements();
        showDrawEdElements();
    }//GEN-LAST:event_tbDrawingActionPerformed

    private void tbAnalysisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbAnalysisActionPerformed
        // Affiche les iFrames pour l'analyse et cache les autres.
        hideElements();
        showAnalysisElements();
    }//GEN-LAST:event_tbAnalysisActionPerformed

    private void popmFileWelcomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFileWelcomeActionPerformed
        tbWelcome.setSelected(true);
        hideElements();
        wpWelcome.setVisible(true);
    }//GEN-LAST:event_popmFileWelcomeActionPerformed

    private void popmFileKaraokeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFileKaraokeActionPerformed
        tbKaraoke.setSelected(true);
        hideElements();
        showKaraokeElements();
    }//GEN-LAST:event_popmFileKaraokeActionPerformed

    private void popmFileCodeEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFileCodeEditorActionPerformed
        tbCodeEditor.setSelected(true);
        hideElements();
        showCodeEdElements();
    }//GEN-LAST:event_popmFileCodeEditorActionPerformed

    private void popmFileDrawingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFileDrawingActionPerformed
        tbDrawing.setSelected(true);
        hideElements();
        showDrawEdElements();
    }//GEN-LAST:event_popmFileDrawingActionPerformed

    private void popmFileAnalysisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFileAnalysisActionPerformed
        tbAnalysis.setSelected(true);
        hideElements();
        showAnalysisElements();
    }//GEN-LAST:event_popmFileAnalysisActionPerformed

    private void popmFileConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFileConfigActionPerformed
        // Open options and configure
        OptionsDialog2 od2 = new OptionsDialog2(this, true);
        od2.setLocationRelativeTo(null);
        od2.setSelectedTheme(themecollection.getTheme(theme));
        od2.setVideoWidth(videoWidth);
        od2.setVideoHeight(videoHeight);
        od2.setProblemFont(pf.getProblemFont());
        od2.setForceLanguage(force_ISO);
        od2.setBGImage(cfg.get(Configuration.Type.BACKGD_IMAGE));
        od2.setKaraModule(karaModule);
        od2.setCodeModule(codeModule);
        od2.setDrawModule(drawModule);
        od2.setAnalModule(analModule);
        od2.setStartWith(startWith);
        od2.setUnicodeFont(cfg.get(Configuration.Type.FONT));
        od2.setCodeEditor(CODEPATH);
        od2.setDocsPath(DOCSPATH);
        boolean bool2 = od2.showDialog();
        if (bool2==true){
            //...Configuration...
            theme = od2.getSelectedTheme().getName();
            cfg.put(Configuration.Type.THEME, theme);            
            force_ISO = od2.getForceLanguage();
            cfg.put(Configuration.Type.FORCE_ISO, force_ISO);            
            videoWidth = od2.getVideoWidth();
            videoHeight = od2.getVideoHeight();
            pf.setProblemFont(od2.getProblemFont());
            XmlProblemFontWriter xpfw = new XmlProblemFontWriter();
            xpfw.setProblemFontList(od2.getProblemFont());
            xpfw.createProblemFont(DOCSPATH+"problemfont.pfont");
            cfg.put(Configuration.Type.BACKGD_IMAGE, od2.getBGImage());
            karaModule = od2.getKaraModule();
            cfg.put(Configuration.Type.KARA_MODULE, od2.getKaraModuleString());
            codeModule = od2.getCodeModule();
            cfg.put(Configuration.Type.CODE_MODULE, od2.getCodeModuleString());
            drawModule = od2.getDrawModule();
            cfg.put(Configuration.Type.DRAW_MODULE, od2.getDrawModuleString());
            analModule = od2.getAnalModule();
            cfg.put(Configuration.Type.ANAL_MODULE, od2.getAnalModuleString());
            startWith = od2.getStartWith();
            cfg.put(Configuration.Type.STARTWITH, startWith);
            cfg.put(Configuration.Type.FONT, od2.getUnicodeFont());
            CODEPATH = od2.getCodeEditor();
            cfg.put(Configuration.Type.CODE_EDITOR, CODEPATH);
            
            //...Sauvegarde...
            AssIO aio = new AssIO();
            aio.SaveConfig(cfg);
            
            //...Application...
            try {
                backgroundimage = ImageIO.read(new File(cfg.get(Configuration.Type.BACKGD_IMAGE)));
            } catch (IOException ex) {
                backgroundimage = null;
            }
            desk.repaint();
            try{
                Font fontToUse = Font.createFont(Font.TRUETYPE_FONT, new File(cfg.get(Configuration.Type.FONT))).deriveFont(12f);
                if(kp!=null){
                    kp.setUnicodeFont(fontToUse);
                }                
            }catch( FontFormatException | IOException e){
            }
            if(kp!=null){
                kp.setCodeEditor(CODEPATH);
            }
            
        }
    }//GEN-LAST:event_popmFileConfigActionPerformed

    private void popmFileQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFileQuitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_popmFileQuitActionPerformed

    private void popmHelpOnlineHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmHelpOnlineHelpActionPerformed
        OnLineHelpDialog olhd = new OnLineHelpDialog(this,true);
        olhd.setLocationRelativeTo(null);
        olhd.setVisible(true);
    }//GEN-LAST:event_popmHelpOnlineHelpActionPerformed

    private void popmHelpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmHelpAboutActionPerformed
        AboutDialog ad = new AboutDialog(this,true);
        ad.setLocationRelativeTo(null);
        ad.setVisible(true);
    }//GEN-LAST:event_popmHelpAboutActionPerformed

    private void bScriptOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bScriptOKActionPerformed
        SButton sb = (SButton)dcbmSplug.getSelectedItem();
        String sMessage = localeLanguage.getValueOf("optpMessage8")!=null ?
                localeLanguage.getValueOf("optpMessage8") :
                "Dou you want to execute the script ?";
        int n = JOptionPane.showConfirmDialog(this,sb.getDisplayName()+" - "
                +sb.getAuthors()+"\n("+sb.getPluginName()+") - ["
                +sb.getFunction()+"]\n\n"
                +sb.getDescription()+"\n\n"+sb.getHelp(),
                sMessage,
                JOptionPane.YES_NO_OPTION);
        if(n==JOptionPane.YES_OPTION){
            splug.runScriptAndDo(sb);
        }
    }//GEN-LAST:event_bScriptOKActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Méthodes de la classe principale">
    private void hideElements(){
        if(karaModule==true){
            kpOriginal.setVisible(false);
            kpResult.setVisible(false);
            kpSound.setVisible(false);
            kpTree.setVisible(false);
        }
        if(codeModule==true){
            cepCode.setVisible(false);
            kpTree.setVisible(false);
        }
        if(drawModule==true){
            dpFile.setVisible(false);
            dpDraw.setVisible(false);
            dpImage.setVisible(false);
            dpShape.setVisible(false);
            dpMode.setVisible(false);
            dpOps.setVisible(false);
            dpScripts.setVisible(false);
            dpHistoric.setVisible(false);
            dpLayers.setVisible(false);
            dpOrnament.setVisible(false);
            dpSheet.setVisible(false);
            dpAssComs.setVisible(false);
        }
        if(analModule==true){
            apFirstTable.setVisible(false);
            apFirstReport.setVisible(false);
            apSecondTable.setVisible(false);
            apSecondReport.setVisible(false);
        }
        wpWelcome.setVisible(false);
    }
    
    private void showKaraokeElements(){
        kpOriginal.setVisible(true);
        kpSound.setVisible(true);
        kpResult.setVisible(true);        
        kpTree.setVisible(true);
    }
    
    private void showCodeEdElements(){
        cepCode.setVisible(true);
        kpTree.setVisible(true);
    }
    
    private void showDrawEdElements(){
        dpFile.setVisible(true);
        dpDraw.setVisible(true);
        dpImage.setVisible(true);
        dpShape.setVisible(true);
        dpMode.setVisible(true);
        dpOps.setVisible(true);
        dpScripts.setVisible(true);
        dpHistoric.setVisible(true);
        dpLayers.setVisible(true);
        dpOrnament.setVisible(true);
        dpSheet.setVisible(true);
        dpAssComs.setVisible(true);
    }
    
    private void showAnalysisElements(){
        apFirstTable.setVisible(true);
        apFirstReport.setVisible(true);
        apSecondTable.setVisible(true);
        apSecondReport.setVisible(true);
    }
    
    public String getApplicationDirectory(){
        if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
            java.io.File file = new java.io.File("");
            return file.getAbsolutePath();
        }
        String path = System.getProperty("user.dir");
        if(path.toLowerCase().contains("jre")){
            File f = new File(getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString()
                    .substring(6));
            path = f.getParent();
        }
        return path;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Méthodes pouvant être partagées">
    public static AssInfos getAssInfos(){
        return ai;
    }
    
    public static void setAssInfos(AssInfos ai){
        MainFrame.ai = ai;
    }
    
    public static AssNameCollection getAssNameCollection(){
        return anc;
    }
    
    public static AssNameCollection getAssNameCollectionWithInit(){
        anc = new AssNameCollection();
        return anc;
    }
    
    public static void setAssNameCollection(AssNameCollection anc){
        MainFrame.anc = anc;
    }
    
    public static AssStyleCollection getAssStyleCollection(){
        return ascScript;
    }
    
    public static void setAssStyleCollection(AssStyleCollection ascScript){
        MainFrame.ascScript = ascScript;
    }
    
    public static List<StylesPack> getStylesPack(){
        return listStylesPack;
    }
    
    public static void setStylesPack(List<StylesPack> listStylesPack){
        MainFrame.listStylesPack = listStylesPack;
    }
    
    public static Language getLanguage(){
        return localeLanguage;
    }
    
    /** <p>Change the theme.<br />Change le thème.</p> */
    public static void changeTheme(Theme th){
        th.applyTheme(frame);
    }
    // </editor-fold>
    
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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bScriptOK;
    private javax.swing.ButtonGroup bgMainAction;
    private javax.swing.JButton btnQuit;
    private javax.swing.JComboBox cbButtonScript;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem popmFileAnalysis;
    private javax.swing.JMenuItem popmFileCodeEditor;
    private javax.swing.JMenuItem popmFileConfig;
    private javax.swing.JMenuItem popmFileDrawing;
    private javax.swing.JMenuItem popmFileKaraoke;
    private javax.swing.JMenuItem popmFileQuit;
    private javax.swing.JMenuItem popmFileWelcome;
    private javax.swing.JMenuItem popmHelpAbout;
    private javax.swing.JMenuItem popmHelpOnlineHelp;
    private javax.swing.JToggleButton tbAnalysis;
    private javax.swing.JToggleButton tbCodeEditor;
    private javax.swing.JToggleButton tbDrawing;
    private javax.swing.JToggleButton tbKaraoke;
    private javax.swing.JToggleButton tbWelcome;
    // End of variables declaration//GEN-END:variables
}
