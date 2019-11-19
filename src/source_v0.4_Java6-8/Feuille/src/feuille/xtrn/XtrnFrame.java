/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.xtrn;

import feuille.analysis.AnalysisPanel;
import feuille.codeeditor.CodeEditorPanel;
import feuille.drawing.DrawingPanel;
import feuille.karaoke.KaraokePanel;
import feuille.karaoke.lib.AssIO;
import feuille.karaoke.lib.AssInfos;
import feuille.karaoke.lib.AssNameCollection;
import feuille.karaoke.lib.AssStyleCollection;
import feuille.karaoke.lib.StylesPack;
import feuille.lib.Configuration;
import feuille.lib.Language;
import feuille.scripting.ScriptPlugin;
import feuille.theme.ThemeCollection;
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
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author Yves
 */
public class XtrnFrame extends javax.swing.JFrame {

    private JDesktopPane desk;
    private BufferedImage backgroundimage;
    private String DOCSPATH = "E:\\Dev\\Projets\\Java\\SmallBoxForFansub\\docs\\";
    private String CODEPATH = "";
    private Configuration cfg = new Configuration();
    private ThemeCollection themecollection = new ThemeCollection();
    private String theme = "";
    private String force_ISO = "---";
    private Font fontToUse = getFont();
    private KaraokePanel kp;
    private CodeEditorPanel cep;
    private DrawingPanel dp;
    private AnalysisPanel ap;
    
    private JInternalFrame kpOriginal, kpResult, kpSound, kpTree, cepCode,
            dpFile, dpDraw, dpImage, dpShape, dpMode, dpOps, dpScripts,
            dpHistoric, dpLayers, dpOrnament, dpSheet, dpAssComs,
            apFirstTable, apFirstReport, apSecondTable, apSecondReport,
            wpWelcome;
    
    private static AssInfos ai = new AssInfos();
    private static AssNameCollection anc = new AssNameCollection("");
    private static AssStyleCollection ascScript = new AssStyleCollection();
    private static ScriptPlugin splug;
    private static Language localeLanguage;
    private static Frame frame;
    private static List<StylesPack> listStylesPack;
    
    
    public enum UseXtrn{
        Karaoke, Drawing, Analysis;
    }
    
    /**
     * Creates new form XtrnFrame
     */
    public XtrnFrame() {
        initComponents();
        init();
    }
    
    private void init(){        
        if((new File(DOCSPATH)).exists()==false | (new File(getApplicationDirectory()+"\\docs\\")).exists()==false){
            DOCSPATH = getApplicationDirectory()+"\\docs\\"; //Défaut
            File f = new File(DOCSPATH);
            f.mkdir();
        }else{
            DOCSPATH = getApplicationDirectory()+"\\docs\\"; //Défaut
        }
        
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
        
        AssIO aio = new AssIO();
        if(!aio.HasConfigFile()){//If there is no file then create a new file
            aio.createConfigFile();
        }
        cfg = aio.ReadConfig();
        DOCSPATH = cfg.get(Configuration.Type.DOCS_PATH);
        CODEPATH =  cfg.get(Configuration.Type.CODE_EDITOR);
        try{
            fontToUse = Font.createFont(Font.TRUETYPE_FONT, new File(cfg.get(Configuration.Type.FONT))).deriveFont(12f);
        }catch(  FontFormatException | IOException ffe){
            fontToUse = getFont();
        }
        try {
            backgroundimage = ImageIO.read(new File(cfg.get(Configuration.Type.BACKGD_IMAGE)));
        } catch (IOException ex) {
            backgroundimage = null;
        }
        theme = cfg.get(Configuration.Type.THEME);
        force_ISO = cfg.get(Configuration.Type.FORCE_ISO);
        
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
        
        
        
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension dim = toolkit.getScreenSize();
        java.awt.GraphicsConfiguration gconf = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        java.awt.Insets insets = toolkit.getScreenInsets(gconf);
        setSize(dim.width - insets.left - insets.right,
                dim.height - insets.top - insets.bottom);
        
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        localeLanguage = new Language(Locale.getDefault(), force_ISO, DOCSPATH);
        
        splug = new ScriptPlugin(this);
        splug.setPaths(DOCSPATH, DOCSPATH, DOCSPATH);
        splug.setModelsForManagement(
                feuille.karaoke.KaraokePanel.getOriginalTableModel(),
                feuille.karaoke.KaraokePanel.getResultTableModel());
        splug.setVideoSize(1280, 720);
        splug.setAssStyleCollection(ascScript);
//        dcbmSplug = new DefaultComboBoxModel();
//        cbbr = new ComboBoxButtonRenderer();
//        cbButtonScript.setModel(dcbmSplug);
//        cbButtonScript.setRenderer(cbbr);
        splug.searchForScript(DOCSPATH);
        List<Object> sobjList = new ArrayList<>(splug.getSObjectList());        
//        for(Object o : sobjList){
//            if(o instanceof SButton){
//                SButton sb = (SButton)o;
//                boolean found = false;
//                for(int i=0;i<dcbmSplug.getSize();i++){
//                    if(sb.equals(dcbmSplug.getElementAt(i))){found = true;}
//                }
//                if(found==false){
//                    dcbmSplug.addElement(sb);
//                    System.out.println("Ruby ou Python plugin : \""+sb.getDisplayName()+"\" by "+sb.getAuthors());
//                }                
//            }
//        }
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
    
    public void setXtrn(UseXtrn xtr){
        switch(xtr){
            case Karaoke:
                kp = new KaraokePanel(DOCSPATH, fontToUse, localeLanguage, this);
                cep = new CodeEditorPanel(DOCSPATH, this, localeLanguage);
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
                break;
            case Drawing:
                dp = new DrawingPanel(DOCSPATH, this, localeLanguage);
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
                break;
            case Analysis:
                ap = new AnalysisPanel(this);
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
                break;
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

        mainPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(XtrnFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(XtrnFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(XtrnFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(XtrnFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new XtrnFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
