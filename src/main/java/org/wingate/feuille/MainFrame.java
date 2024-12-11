package org.wingate.feuille;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import org.wingate.feuille.dialog.ActorsDialog;
import org.wingate.feuille.dialog.StylesDialog;
import org.wingate.feuille.subs.ass.ASS;
import org.wingate.feuille.subs.ass.AssActor;
import org.wingate.feuille.subs.ass.AssComboBoxRenderer;
import org.wingate.feuille.subs.ass.AssEffect;
import org.wingate.feuille.subs.ass.AssEvent;
import org.wingate.feuille.subs.ass.AssStyle;
import org.wingate.feuille.subs.ass.AssTableModel2;
import org.wingate.feuille.subs.ass.AssTime;
import org.wingate.feuille.subs.ass.AssTimeExtra;
import org.wingate.feuille.util.Clipboard;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.LanguageAccessoryPanel;

/**
 *
 * @author util2
 */
public class MainFrame extends javax.swing.JFrame {
    
    private final AssTableModel2 assSubsTableModel;
    private final DefaultComboBoxModel comboBoxModelStyles;
    private final DefaultComboBoxModel comboBoxModelActors;
    private final DefaultComboBoxModel comboBoxModelEffects;
    
    private final LanguageAccessoryPanel languageAccess;
    private float cplThreshold = 60f;
    
    private ISO_3166 origin = ISO_3166.Japan;
    private ISO_3166 translation = ISO_3166.United_Kingdom_of_Great_Britain___N__Ireland;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        
        assSubsTableModel = new AssTableModel2(tableASS);
        assSubsTableModel.updateColumnSize();
        
        comboBoxModelStyles = new DefaultComboBoxModel();
        comboBoxModelActors = new DefaultComboBoxModel();
        comboBoxModelEffects = new DefaultComboBoxModel();
        
        cbStyles.setModel(comboBoxModelStyles);
        cbActors.setModel(comboBoxModelActors);
        cbEffects.setModel(comboBoxModelEffects);
        
        cbStyles.setRenderer(new AssComboBoxRenderer());
        cbActors.setRenderer(new AssComboBoxRenderer());
        cbEffects.setRenderer(new AssComboBoxRenderer());
        
        fcLinkLanguages.setAcceptAllFileFilterUsed(false);
        fcLinkLanguages.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) return true;
                return f.getName().toLowerCase().endsWith(".ass");
            }

            @Override
            public String getDescription() {
                return "ASS files";
            }
        });
        languageAccess = new LanguageAccessoryPanel();
        fcLinkLanguages.setAccessory(languageAccess);
        
        initMenuOrigin(popmOrigin);
        initMenuTranslation(popmTranslation);
        
        updateComboBox();
    }
    
    public void setTableSplitter(double media, double table, double edit){
        jSplitPane1.setDividerLocation(media);
        jSplitPane2.setDividerLocation(table);
        jSplitPane3.setDividerLocation(edit);
    }
    
    private void updateComboBox(){
        // Styles
        if(assSubsTableModel.getAss().getStyles().isEmpty()){
            assSubsTableModel.getAss().getStyles().add(new AssStyle());
        }
        
        comboBoxModelStyles.removeAllElements();
        for(AssStyle style : assSubsTableModel.getAss().getStyles()){
            comboBoxModelStyles.addElement(style);
        }
        
        // Actors
        if(assSubsTableModel.getAss().getActors().isEmpty()){
            assSubsTableModel.getAss().getActors().add(new AssActor());
        }
        
        comboBoxModelActors.removeAllElements();
        for(AssActor actor : assSubsTableModel.getAss().getActors()){
            comboBoxModelActors.addElement(actor);
        }
        
        // FXs
        if(assSubsTableModel.getAss().getEffects().isEmpty()){
            assSubsTableModel.getAss().getEffects().add(new AssEffect());
        }
        
        comboBoxModelEffects.removeAllElements();
        for(AssEffect fx : assSubsTableModel.getAss().getEffects()){
            comboBoxModelEffects.addElement(fx);
        }
    }
    
    private void autoResolveLinkLanguage(ASS ass, ASS foreign, ISO_3166 link){
        for(AssEvent ev1 : ass.getEvents()){
            // Official script start
            double msStart = ev1.getStart().getMsTime();
            // Official script end
            double msEnd = ev1.getEnd().getMsTime();
            
            for(AssEvent ev2 : foreign.getEvents()){
                // Foreign inner middle
                double msMiddle = (ev2.getEnd().getMsTime() - ev2.getStart().getMsTime())
                        + ev2.getStart().getMsTime();
                
                if(msStart <= msMiddle && msMiddle < msEnd){
                    ev1.setText(link, ev2.getText());
                }
            }
        }
    }
    
    private void setOrigin(String alpha2){
        origin = ISO_3166.getISO_3166(alpha2);
    }
    
    private void setTranslation(String alpha2){
        translation = ISO_3166.getISO_3166(alpha2);
    }
    
    private void initMenuOrigin(JMenu mnu){
        File folder = new File(getClass().getResource("/org/wingate/feuille/iso3166").getPath());
        for(File f : folder.listFiles((File dir, String name1) -> name1.endsWith(".gif"))){
            ISO_3166 language = ISO_3166.getISO_3166(f.getName().substring(0, f.getName().indexOf(".")));
            if(language == ISO_3166.Unknown) continue;
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(language.getCountry());
            bgOrigin.add(item);
            item.addActionListener((e)->{
                setOrigin(language.getAlpha2());
                for(AssEvent event : assSubsTableModel.getAss().getEvents()){
                    event.setOrigin(origin);
                }
            });
            mnu.add(item);
        }
    }
    
    private void initMenuTranslation(JMenu mnu){
        File folder = new File(getClass().getResource("/org/wingate/feuille/iso3166").getPath());
        for(File f : folder.listFiles((File dir, String name1) -> name1.endsWith(".gif"))){
            ISO_3166 language = ISO_3166.getISO_3166(f.getName().substring(0, f.getName().indexOf(".")));
            if(language == ISO_3166.Unknown) continue;
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(language.getCountry());
            bgTranslation.add(item);
            item.addActionListener((e)->{
                setTranslation(language.getAlpha2());
                for(AssEvent event : assSubsTableModel.getAss().getEvents()){
                    event.setCurrentLink(translation);
                }
            });
            mnu.add(item);
        }
    }
    
    private String reformatNumber(String s){
        if(s.matches("-?\\d*") == false){
            Pattern p = Pattern.compile("(-?\\d*)");
            Matcher m = p.matcher(s);
            boolean containsMinus = s.startsWith("-");
            boolean useMinusAllowed = true;
            StringBuilder sb = new StringBuilder();
            while(m.find()){
                if(useMinusAllowed && containsMinus){
                    sb.append("-");
                }
                sb.append(m.group(1).replace("-", ""));
                useMinusAllowed = false;
            }
            return sb.toString();
        }
        return s;
    }
    
    private String upDownWheelRotation(String str, int wheelRotationUnit){
        if( str.matches("-?\\d*")){
            if(str.equalsIgnoreCase("-")) str = "0";
            if(str.isEmpty()) str = "0";
            int value = Integer.parseInt(str);
            if(wheelRotationUnit < 0){
                value++;
            }else if(wheelRotationUnit > 0){
                value--;
            }
            return value == 0 ? "" : Integer.toString(value);
        }
        return str;
    }
    
    private void cutTextPane(javax.swing.JTextPane p){
        if(p.getText().isEmpty()) return;
        if(p.getSelectedText().isEmpty()) return;
        String str = p.getText();
        //System.out.println(p.getSelectedText());
        if(p.getText().equals(p.getSelectedText())){
            Clipboard.copyString(p.getSelectedText());
            p.setText("");
            //System.out.println("a");
        }else{
            Clipboard.copyString(p.getSelectedText());
            if(p.getSelectionStart() == 0){
                p.setText(str.substring(p.getSelectionEnd()));
                //System.out.println("b");
            }else if(p.getSelectionEnd() == p.getText().length()){
                p.setText(str.substring(0, p.getSelectionStart()));
                //System.out.println("c");
            }else{
                p.setText(str.substring(0, p.getSelectionStart())
                        + str.substring(p.getSelectionEnd()));
                //System.out.println("d");
            }
        }
    }
    
    private void pasteTextPane(javax.swing.JTextPane p){
        String str = p.getText();
        if(str.equals(p.getSelectedText())){
            p.setText(Clipboard.pasteString());
            //System.out.println("e");
        }else{
            if(p.getSelectionStart() == 0){
                p.setText(Clipboard.pasteString() + str.substring(p.getSelectionEnd()));
                //System.out.println("f");
            }else if(p.getSelectionEnd() == p.getText().length()){
                p.setText(str.substring(0, p.getSelectionStart()) + Clipboard.pasteString());
                //System.out.println("g");
            }else{
                String sStart = p.getSelectionStart() == 0 ? "" : str.substring(0, p.getSelectionStart());
                String sEnd = p.getSelectionEnd() == str.length() ? "" : str.substring(p.getSelectionEnd());
                p.setText(sStart + Clipboard.pasteString() + sEnd);
                //System.out.println("h");
            }
        }
    }
    
    private void copyTextPane(javax.swing.JTextPane p){
        Clipboard.copyString(p.getSelectedText());
        //System.out.println("i");
    }
    
    private void insertTagAround(javax.swing.JTextPane p, String tag){
        String tStart = String.format("{\\%s1}", tag);
        String tEnd = String.format("{\\%s0}", tag);
        String sStart = p.getSelectionStart() == 0 ? "" : p.getText().substring(0, p.getSelectionStart());
        String sEnd = p.getSelectionEnd() == p.getText().length() ? "" : p.getText().substring(p.getSelectionEnd());
        String sMid = p.getSelectedText() == null || p.getSelectedText().isEmpty() ? tStart + tEnd : tStart + p.getSelectedText() + tEnd;
        p.setText(sStart + sMid + sEnd);
    }
    
    private void insertTag(javax.swing.JTextPane p, String tag){
        String sMid = String.format("{\\%s}", tag);
        String sStart = p.getSelectionStart() == 0 ? "" : p.getText().substring(0, p.getSelectionStart());
        String sEnd = p.getSelectionEnd() == p.getText().length() ? "" : p.getText().substring(p.getSelectionEnd());
        p.setText(sStart + sMid + sEnd);
    }
    
    private String simplify(String text){        
        String s = text.replace("}{", "");
        StringBuilder sb = new StringBuilder();
        int bracket = 0;
        for(char c : s.toCharArray()){
            String sc = Character.toString(c);
            switch (sc) {
                case "{" -> {
                    if(bracket == 0) sb.append(sc);
                    bracket++;
                }
                case "}" -> {
                    bracket--;
                    if(bracket == 0) sb.append(sc);
                }
                default -> sb.append(sc);
            }
        }        
        return sb.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcASS = new javax.swing.JFileChooser();
        fcVideo = new javax.swing.JFileChooser();
        fcAudio = new javax.swing.JFileChooser();
        fcLinkLanguages = new javax.swing.JFileChooser();
        bgCPSCPL = new javax.swing.ButtonGroup();
        popTable = new javax.swing.JPopupMenu();
        popmOrigin = new javax.swing.JMenu();
        popmTranslation = new javax.swing.JMenu();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        popmUnselectLine = new javax.swing.JMenuItem();
        bgOrigin = new javax.swing.ButtonGroup();
        bgTranslation = new javax.swing.ButtonGroup();
        popEditTP = new javax.swing.JPopupMenu();
        popmEdTpCut = new javax.swing.JMenuItem();
        popmEdTpCopy = new javax.swing.JMenuItem();
        popmEdTpPaste = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        popmEdTpClear = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        popmEdTpMirror = new javax.swing.JMenuItem();
        popmEdTags = new javax.swing.JMenu();
        popmEdTagSimplify = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        popmEdTagB = new javax.swing.JMenuItem();
        popmEdTagI = new javax.swing.JMenuItem();
        popmEdTagU = new javax.swing.JMenuItem();
        popmEdTagS = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnNewASS = new javax.swing.JButton();
        btnOpenASS = new javax.swing.JButton();
        btnSaveASS = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnOpenVideo = new javax.swing.JButton();
        btnOpenSound = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnPlay = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        player = new org.wingate.konnpetkoll.swing.FFPlayer();
        jPanel6 = new javax.swing.JPanel();
        panWave = new javax.swing.JPanel();
        waveform1 = new org.wingate.konnpetkoll.swing.Waveform();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        cbComment = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        cbStyles = new org.wingate.konnpetkoll.swing.PlaceholderComboBox();
        btnEditStyles = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        cbActors = new org.wingate.konnpetkoll.swing.PlaceholderComboBox();
        btnEditActors = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        cbEffects = new org.wingate.konnpetkoll.swing.PlaceholderComboBox();
        btnEditEffects = new javax.swing.JButton();
        tfLayer = new org.wingate.konnpetkoll.swing.PlaceholderTextField();
        tfStart = new org.wingate.feuille.util.LockFormatTextField();
        tfEnd = new org.wingate.feuille.util.LockFormatTextField();
        tfDuration = new org.wingate.feuille.util.LockFormatTextField();
        tfML = new org.wingate.konnpetkoll.swing.PlaceholderTextField();
        tfMR = new org.wingate.konnpetkoll.swing.PlaceholderTextField();
        tfMV = new org.wingate.konnpetkoll.swing.PlaceholderTextField();
        jButton5 = new javax.swing.JButton();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        tpTextForTable = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        tpSourceFromTable = new javax.swing.JTextPane();
        jPanel7 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnStripped = new javax.swing.JButton();
        btnPartiallyStripped = new javax.swing.JButton();
        btnNotStripped = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        toggleCPS = new javax.swing.JToggleButton();
        toggleCPL = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableASS = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        panChat = new javax.swing.JPanel();
        panMessageSending = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tpChat = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuFileTable = new javax.swing.JMenu();
        mTableNewLink = new javax.swing.JMenuItem();
        mTableOpenLink = new javax.swing.JMenuItem();
        mTableSaveLink = new javax.swing.JMenuItem();
        mTableCloseLink = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mTableAutoResolve = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mTableLinks = new javax.swing.JMenu();
        mFileQuit = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();

        popmOrigin.setText("Original language");
        popTable.add(popmOrigin);

        popmTranslation.setText("Translation to");
        popTable.add(popmTranslation);
        popTable.add(jSeparator6);

        popmUnselectLine.setText("Unselect line");
        popmUnselectLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmUnselectLineActionPerformed(evt);
            }
        });
        popTable.add(popmUnselectLine);

        popmEdTpCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16-Crystal_Clear_action_editcut.png"))); // NOI18N
        popmEdTpCut.setText("Cut");
        popmEdTpCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTpCutActionPerformed(evt);
            }
        });
        popEditTP.add(popmEdTpCut);

        popmEdTpCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16-Crystal_Clear_action_editcopy.png"))); // NOI18N
        popmEdTpCopy.setText("Copy");
        popmEdTpCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTpCopyActionPerformed(evt);
            }
        });
        popEditTP.add(popmEdTpCopy);

        popmEdTpPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16-Crystal_Clear_action_editpaste.png"))); // NOI18N
        popmEdTpPaste.setText("Paste");
        popmEdTpPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTpPasteActionPerformed(evt);
            }
        });
        popEditTP.add(popmEdTpPaste);
        popEditTP.add(jSeparator7);

        popmEdTpClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16KO.png"))); // NOI18N
        popmEdTpClear.setText("Clear text");
        popmEdTpClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTpClearActionPerformed(evt);
            }
        });
        popEditTP.add(popmEdTpClear);
        popEditTP.add(jSeparator8);

        popmEdTpMirror.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16OK.png"))); // NOI18N
        popmEdTpMirror.setText("Mirror text");
        popmEdTpMirror.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTpMirrorActionPerformed(evt);
            }
        });
        popEditTP.add(popmEdTpMirror);

        popmEdTags.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 font style bold.png"))); // NOI18N
        popmEdTags.setText("Tags");

        popmEdTagSimplify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 caution.png"))); // NOI18N
        popmEdTagSimplify.setText("Symplify");
        popmEdTagSimplify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTagSimplifyActionPerformed(evt);
            }
        });
        popmEdTags.add(popmEdTagSimplify);
        popmEdTags.add(jSeparator9);

        popmEdTagB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16_timer_stuffs_green.png"))); // NOI18N
        popmEdTagB.setText("Bold - b");
        popmEdTagB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTagBActionPerformed(evt);
            }
        });
        popmEdTags.add(popmEdTagB);

        popmEdTagI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16_timer_stuffs_green.png"))); // NOI18N
        popmEdTagI.setText("Italic - i");
        popmEdTagI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTagIActionPerformed(evt);
            }
        });
        popmEdTags.add(popmEdTagI);

        popmEdTagU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16_timer_stuffs_green.png"))); // NOI18N
        popmEdTagU.setText("Underline - u");
        popmEdTagU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTagUActionPerformed(evt);
            }
        });
        popmEdTags.add(popmEdTagU);

        popmEdTagS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16_timer_stuffs_green.png"))); // NOI18N
        popmEdTagS.setText("StrikeOut - s");
        popmEdTagS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmEdTagSActionPerformed(evt);
            }
        });
        popmEdTags.add(popmEdTagS);

        popEditTP.add(popmEdTags);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        btnNewASS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/32_newdocument.png"))); // NOI18N
        btnNewASS.setToolTipText("Clear subtitles");
        btnNewASS.setFocusable(false);
        btnNewASS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewASS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewASS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewASSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewASS);

        btnOpenASS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/32_folder.png"))); // NOI18N
        btnOpenASS.setToolTipText("Open subtitles...");
        btnOpenASS.setFocusable(false);
        btnOpenASS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenASS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenASS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenASSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenASS);

        btnSaveASS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/32_floppydisk.png"))); // NOI18N
        btnSaveASS.setToolTipText("Save subtitles...");
        btnSaveASS.setFocusable(false);
        btnSaveASS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveASS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveASS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveASSActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveASS);
        jToolBar1.add(jSeparator1);

        btnOpenVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/32_folder.png"))); // NOI18N
        btnOpenVideo.setToolTipText("Open video...");
        btnOpenVideo.setFocusable(false);
        btnOpenVideo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenVideo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenVideoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenVideo);

        btnOpenSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/32_folder.png"))); // NOI18N
        btnOpenSound.setToolTipText("Open sound...");
        btnOpenSound.setFocusable(false);
        btnOpenSound.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenSound.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenSound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenSoundActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenSound);
        jToolBar1.add(jSeparator2);

        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/32_timer_stuffs play.png"))); // NOI18N
        btnPlay.setToolTipText("Play");
        btnPlay.setFocusable(false);
        btnPlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPlay);

        btnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/32_timer_stuffs pause.png"))); // NOI18N
        btnPause.setToolTipText("Pause");
        btnPause.setFocusable(false);
        btnPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPause);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/32_timer_stuffs stop.png"))); // NOI18N
        btnStop.setToolTipText("Stop");
        btnStop.setFocusable(false);
        btnStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        jToolBar1.add(btnStop);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jSplitPane2.setDividerLocation(250);
        jSplitPane2.setDividerSize(10);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setOneTouchExpandable(true);

        jSplitPane1.setDividerSize(10);
        jSplitPane1.setOneTouchExpandable(true);

        javax.swing.GroupLayout playerLayout = new javax.swing.GroupLayout(player);
        player.setLayout(playerLayout);
        playerLayout.setHorizontalGroup(
            playerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        playerLayout.setVerticalGroup(
            playerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(player);

        jPanel6.setPreferredSize(new java.awt.Dimension(415, 250));
        jPanel6.setLayout(new java.awt.GridLayout(2, 1));

        panWave.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout waveform1Layout = new javax.swing.GroupLayout(waveform1);
        waveform1.setLayout(waveform1Layout);
        waveform1Layout.setHorizontalGroup(
            waveform1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 998, Short.MAX_VALUE)
        );
        waveform1Layout.setVerticalGroup(
            waveform1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        panWave.add(waveform1, java.awt.BorderLayout.CENTER);

        jPanel6.add(panWave);

        jPanel5.setLayout(new java.awt.BorderLayout());

        cbComment.setText("#");
        cbComment.setToolTipText("Line type");
        cbComment.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                cbCommentMouseWheelMoved(evt);
            }
        });
        cbComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCommentActionPerformed(evt);
            }
        });
        jPanel11.add(cbComment);

        jPanel8.setLayout(new java.awt.BorderLayout());

        cbStyles.setToolTipText("Styles");
        cbStyles.setPlaceholder("Style");
        cbStyles.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                cbStylesMouseWheelMoved(evt);
            }
        });
        cbStyles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStylesActionPerformed(evt);
            }
        });
        jPanel8.add(cbStyles, java.awt.BorderLayout.CENTER);

        btnEditStyles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 losange carré.png"))); // NOI18N
        btnEditStyles.setToolTipText("Edit Styles...");
        btnEditStyles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditStylesActionPerformed(evt);
            }
        });
        jPanel8.add(btnEditStyles, java.awt.BorderLayout.EAST);

        jPanel11.add(jPanel8);

        jPanel9.setLayout(new java.awt.BorderLayout());

        cbActors.setToolTipText("Actors");
        cbActors.setPlaceholder("Actor");
        cbActors.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                cbActorsMouseWheelMoved(evt);
            }
        });
        cbActors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbActorsActionPerformed(evt);
            }
        });
        jPanel9.add(cbActors, java.awt.BorderLayout.CENTER);

        btnEditActors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 losange carré.png"))); // NOI18N
        btnEditActors.setToolTipText("Edit Actors...");
        btnEditActors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActorsActionPerformed(evt);
            }
        });
        jPanel9.add(btnEditActors, java.awt.BorderLayout.LINE_END);

        jPanel11.add(jPanel9);

        jPanel10.setLayout(new java.awt.BorderLayout());

        cbEffects.setToolTipText("Effects");
        cbEffects.setPlaceholder("Effect");
        cbEffects.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                cbEffectsMouseWheelMoved(evt);
            }
        });
        cbEffects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEffectsActionPerformed(evt);
            }
        });
        jPanel10.add(cbEffects, java.awt.BorderLayout.CENTER);

        btnEditEffects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 losange carré.png"))); // NOI18N
        btnEditEffects.setToolTipText("Edit Effects...");
        btnEditEffects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEffectsActionPerformed(evt);
            }
        });
        jPanel10.add(btnEditEffects, java.awt.BorderLayout.LINE_END);

        jPanel11.add(jPanel10);

        tfLayer.setToolTipText("Scroll your mouse to change the layer");
        tfLayer.setPlaceholder("0");
        tfLayer.setPreferredSize(new java.awt.Dimension(40, 22));
        tfLayer.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                tfLayerMouseWheelMoved(evt);
            }
        });
        tfLayer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfLayerKeyReleased(evt);
            }
        });
        jPanel11.add(tfLayer);
        jPanel11.add(tfStart);
        jPanel11.add(tfEnd);
        jPanel11.add(tfDuration);

        tfML.setToolTipText("Scroll your mouse to change the left margin value");
        tfML.setPlaceholder("0");
        tfML.setPreferredSize(new java.awt.Dimension(50, 22));
        tfML.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                tfMLMouseWheelMoved(evt);
            }
        });
        tfML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfMLActionPerformed(evt);
            }
        });
        jPanel11.add(tfML);

        tfMR.setToolTipText("Scroll your mouse to change the right margin value");
        tfMR.setPlaceholder("0");
        tfMR.setPreferredSize(new java.awt.Dimension(50, 22));
        tfMR.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                tfMRMouseWheelMoved(evt);
            }
        });
        tfMR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfMRActionPerformed(evt);
            }
        });
        jPanel11.add(tfMR);

        tfMV.setToolTipText("Scroll your mouse to change the vertical margin value");
        tfMV.setPlaceholder("0");
        tfMV.setPreferredSize(new java.awt.Dimension(50, 22));
        tfMV.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                tfMVMouseWheelMoved(evt);
            }
        });
        tfMV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfMVActionPerformed(evt);
            }
        });
        jPanel11.add(tfMV);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 rond.png"))); // NOI18N
        jButton5.setToolTipText("Show parameters...");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton5);

        jPanel5.add(jPanel11, java.awt.BorderLayout.NORTH);

        jSplitPane3.setDividerSize(10);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane3.setOneTouchExpandable(true);

        tpTextForTable.setToolTipText("<html>Click on the middle button of the mouse to add an event.<br>\nClick on the right button of the mouse to have more choices.<br>\nDouble left click to paste text.");
        tpTextForTable.setComponentPopupMenu(popEditTP);
        tpTextForTable.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tpTextForTableCaretUpdate(evt);
            }
        });
        tpTextForTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tpTextForTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tpTextForTable);

        jSplitPane3.setBottomComponent(jScrollPane4);

        tpSourceFromTable.setToolTipText("<html>Click on the middle button of the mouse to add an event.<br> Click on the right button of the mouse to have more choices.<br> Double left click to paste text.");
        tpSourceFromTable.setComponentPopupMenu(popEditTP);
        tpSourceFromTable.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tpSourceFromTableCaretUpdate(evt);
            }
        });
        tpSourceFromTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tpSourceFromTableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tpSourceFromTable);

        jSplitPane3.setLeftComponent(jScrollPane5);

        jPanel5.add(jSplitPane3, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel5);

        jSplitPane1.setRightComponent(jPanel6);

        jSplitPane2.setLeftComponent(jSplitPane1);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jToolBar2.setRollover(true);

        btnStripped.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 penta - vert.png"))); // NOI18N
        btnStripped.setToolTipText("Completely stripped");
        btnStripped.setFocusable(false);
        btnStripped.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStripped.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnStripped.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStrippedActionPerformed(evt);
            }
        });
        jToolBar2.add(btnStripped);

        btnPartiallyStripped.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 penta - jaune.png"))); // NOI18N
        btnPartiallyStripped.setToolTipText("Partially stripped");
        btnPartiallyStripped.setFocusable(false);
        btnPartiallyStripped.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPartiallyStripped.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPartiallyStripped.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartiallyStrippedActionPerformed(evt);
            }
        });
        jToolBar2.add(btnPartiallyStripped);

        btnNotStripped.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 penta - rouge.png"))); // NOI18N
        btnNotStripped.setToolTipText("Not stripped");
        btnNotStripped.setFocusable(false);
        btnNotStripped.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNotStripped.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNotStripped.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotStrippedActionPerformed(evt);
            }
        });
        jToolBar2.add(btnNotStripped);
        jToolBar2.add(jSeparator5);

        bgCPSCPL.add(toggleCPS);
        toggleCPS.setSelected(true);
        toggleCPS.setText("CPS");
        toggleCPS.setFocusable(false);
        toggleCPS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleCPS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toggleCPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleCPSActionPerformed(evt);
            }
        });
        jToolBar2.add(toggleCPS);

        bgCPSCPL.add(toggleCPL);
        toggleCPL.setText("CPL x60");
        toggleCPL.setFocusable(false);
        toggleCPL.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleCPL.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toggleCPL.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                toggleCPLMouseWheelMoved(evt);
            }
        });
        toggleCPL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleCPLActionPerformed(evt);
            }
        });
        jToolBar2.add(toggleCPL);

        jPanel7.add(jToolBar2, java.awt.BorderLayout.NORTH);

        tableASS.setModel(new javax.swing.table.DefaultTableModel(
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
        tableASS.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableASS.setComponentPopupMenu(popTable);
        tableASS.setRowHeight(40);
        tableASS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableASSMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableASS);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane2.setRightComponent(jPanel7);

        jPanel1.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Table", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1008, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 621, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Karaoke", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1008, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 621, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Drawing", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1008, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 621, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Analysis", jPanel4);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        panChat.setPreferredSize(new java.awt.Dimension(893, 80));
        panChat.setLayout(new java.awt.BorderLayout());

        panMessageSending.setLayout(new java.awt.BorderLayout());

        jButton4.setText("Send");
        panMessageSending.add(jButton4, java.awt.BorderLayout.LINE_START);
        panMessageSending.add(jTextField1, java.awt.BorderLayout.CENTER);

        panChat.add(panMessageSending, java.awt.BorderLayout.NORTH);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setViewportView(tpChat);

        panChat.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        getContentPane().add(panChat, java.awt.BorderLayout.SOUTH);

        mnuFile.setText("File");

        mnuFileTable.setText("Table");

        mTableNewLink.setText("Create a new link language...");
        mTableNewLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mTableNewLinkActionPerformed(evt);
            }
        });
        mnuFileTable.add(mTableNewLink);

        mTableOpenLink.setText("Open a link language file...");
        mTableOpenLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mTableOpenLinkActionPerformed(evt);
            }
        });
        mnuFileTable.add(mTableOpenLink);

        mTableSaveLink.setText("Save a link language file...");
        mTableSaveLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mTableSaveLinkActionPerformed(evt);
            }
        });
        mnuFileTable.add(mTableSaveLink);

        mTableCloseLink.setText("Close a link language file...");
        mTableCloseLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mTableCloseLinkActionPerformed(evt);
            }
        });
        mnuFileTable.add(mTableCloseLink);
        mnuFileTable.add(jSeparator3);

        mTableAutoResolve.setText("Auto Resolve current link");
        mTableAutoResolve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mTableAutoResolveActionPerformed(evt);
            }
        });
        mnuFileTable.add(mTableAutoResolve);
        mnuFileTable.add(jSeparator4);

        mTableLinks.setText("Link languages");
        mnuFileTable.add(mTableLinks);

        mnuFile.add(mnuFileTable);

        mFileQuit.setText("Quit");
        mFileQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFileQuitActionPerformed(evt);
            }
        });
        mnuFile.add(mFileQuit);

        jMenuBar1.add(mnuFile);

        mnuEdit.setText("Edit");
        jMenuBar1.add(mnuEdit);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mFileQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFileQuitActionPerformed
        // Quit
        System.exit(0);
    }//GEN-LAST:event_mFileQuitActionPerformed

    private void btnNewASSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewASSActionPerformed
        // New ASSA document
        int z = JOptionPane.showConfirmDialog(
                this,
                "Do you really want to reset ASSA document?",
                "Confirm?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if(z == JOptionPane.YES_OPTION){
            assSubsTableModel.setAss(new ASS());
            updateComboBox();
        }        
    }//GEN-LAST:event_btnNewASSActionPerformed

    private void btnOpenASSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenASSActionPerformed
        // Open ASSA document
        int z = fcASS.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            assSubsTableModel.setAss(ASS.read(fcASS.getSelectedFile().getAbsolutePath()));
            updateComboBox();
        }
    }//GEN-LAST:event_btnOpenASSActionPerformed

    private void btnSaveASSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveASSActionPerformed
        // Save ASSA document
        int z = fcASS.showSaveDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            ASS.write(assSubsTableModel.getAss(), fcASS.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnSaveASSActionPerformed

    private void btnOpenVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenVideoActionPerformed
        // Open video and wave
        int z = fcVideo.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            String path = fcVideo.getSelectedFile().getPath();
            player.setMediaPath(path);
            waveform1.setPath(path);
            waveform1.setClassicMode(true);
            waveform1.setTime(0d, 1d);
            waveform1.repaint();
        }        
    }//GEN-LAST:event_btnOpenVideoActionPerformed

    private void btnOpenSoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenSoundActionPerformed
        // Open wave
        int z = fcAudio.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            String path = fcAudio.getSelectedFile().getPath();
            waveform1.setPath(path);
            waveform1.setClassicMode(true);
            waveform1.setTime(0d, 1d);
            waveform1.repaint();
        }
    }//GEN-LAST:event_btnOpenSoundActionPerformed

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        // Play
        player.play();
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
        // Pause
        player.pause();
    }//GEN-LAST:event_btnPauseActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        // Stop
        player.stop();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnEditStylesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditStylesActionPerformed
        // Edit styles
        StylesDialog dialog = new StylesDialog(this, true);
        List<AssStyle> styles = assSubsTableModel.getAss().getStyles();
        List<AssActor> actors = assSubsTableModel.getAss().getActors();
        dialog.showDialog(styles, actors);
        if(dialog.getDialogResult() == DialogResult.OK){
            List<AssStyle> newList = dialog.getStyles();
            
            assSubsTableModel.getAss().setStyles(newList);
            updateComboBox();
        }
    }//GEN-LAST:event_btnEditStylesActionPerformed

    private void btnEditActorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActorsActionPerformed
        // Edit actors
        ActorsDialog dialog = new ActorsDialog(this, true);
        List<AssActor> actors = assSubsTableModel.getAss().getActors();
        dialog.showDialog(actors);
        if(dialog.getDialogResult() == DialogResult.OK){
            try {
                List<AssActor> newList = dialog.getActors();
                
                assSubsTableModel.getAss().setActors(newList);
                updateComboBox();
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnEditActorsActionPerformed

    private void btnEditEffectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEffectsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditEffectsActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tpSourceFromTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tpSourceFromTableMouseClicked
        // 
        if(evt.getButton() == java.awt.event.MouseEvent.BUTTON2){
            // Add new line in the ass table
            AssEvent ev = new AssEvent(
                    cbComment.isSelected() ? AssEvent.Type.Comment : AssEvent.Type.Dialogue,
                    tfLayer.getText().matches("\\d+") ? Integer.parseInt(tfLayer.getText()) : 0,
                    tfStart.getAssTime(),
                    tfEnd.getAssTime(),
                    (AssStyle)comboBoxModelStyles.getSelectedItem(),
                    (AssActor)comboBoxModelActors.getSelectedItem(),
                    tfML.getText().matches("\\d+") ? Integer.parseInt(tfML.getText()) : 0,
                    tfMR.getText().matches("\\d+") ? Integer.parseInt(tfMR.getText()) : 0,
                    tfMV.getText().matches("\\d+") ? Integer.parseInt(tfMV.getText()) : 0,
                    (AssEffect)comboBoxModelEffects.getSelectedItem(),
                    tpSourceFromTable.getText()
            );
            assSubsTableModel.addValue(ev);
        }else if(evt.getButton() == java.awt.event.MouseEvent.BUTTON1
                && evt.getClickCount() == 2){
            // Paste
            int x = tpSourceFromTable.getSelectionStart();
            String s = tpSourceFromTable.getText();
            String t = (x != 0 ? s.substring(0, x) : "") +
                    Clipboard.pasteString() +
                    (x != s.length() - 1 ? s.substring(x) : "");
            tpSourceFromTable.setText(t);
        }
    }//GEN-LAST:event_tpSourceFromTableMouseClicked

    private void tpTextForTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tpTextForTableMouseClicked
        // 
        if(evt.getButton() == java.awt.event.MouseEvent.BUTTON2){
            // Add new line in the ass table
            AssEvent ev = new AssEvent(
                    cbComment.isSelected() ? AssEvent.Type.Comment : AssEvent.Type.Dialogue,
                    tfLayer.getText().matches("\\d+") ? Integer.parseInt(tfLayer.getText()) : 0,
                    tfStart.getAssTime(),
                    tfEnd.getAssTime(),
                    (AssStyle)comboBoxModelStyles.getSelectedItem(),
                    (AssActor)comboBoxModelActors.getSelectedItem(),
                    tfML.getText().matches("\\d+") ? Integer.parseInt(tfML.getText()) : 0,
                    tfMR.getText().matches("\\d+") ? Integer.parseInt(tfMR.getText()) : 0,
                    tfMV.getText().matches("\\d+") ? Integer.parseInt(tfMV.getText()) : 0,
                    (AssEffect)comboBoxModelEffects.getSelectedItem(),
                    tpTextForTable.getText()
            );
            assSubsTableModel.addValue(ev);
        }else if(evt.getButton() == java.awt.event.MouseEvent.BUTTON1
                && evt.getClickCount() == 2){
            // Paste
            int x = tpTextForTable.getSelectionStart();
            String s = tpTextForTable.getText();
            String t = (x != 0 ? s.substring(0, x) : "") +
                    Clipboard.pasteString() +
                    (x != s.length() - 1 ? s.substring(x) : "");
            tpTextForTable.setText(t);
        }
    }//GEN-LAST:event_tpTextForTableMouseClicked

    private void mTableNewLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mTableNewLinkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mTableNewLinkActionPerformed

    private void mTableOpenLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mTableOpenLinkActionPerformed
        // Open a new ASS link language file        
        int z = fcLinkLanguages.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            String filepath = fcLinkLanguages.getSelectedFile().getPath();
            ASS ass = assSubsTableModel.getAss(); // current script
            ASS openedNow = ASS.read(filepath);
            ass.getAvailableLinkLanguages().put(
                    languageAccess.getLinkLanguage(),
                    filepath
            );
            // auto resolve
            autoResolveLinkLanguage(ass, openedNow, languageAccess.getLinkLanguage());
            // RESET
            assSubsTableModel.setAss(ass);
        }
    }//GEN-LAST:event_mTableOpenLinkActionPerformed

    private void mTableSaveLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mTableSaveLinkActionPerformed
        // Save all ASS link language files
        int z = fcLinkLanguages.showSaveDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            Map<ISO_3166, String> map = assSubsTableModel.getAss().getAvailableLinkLanguages();
            String filePath = fcLinkLanguages.getSelectedFile().getPath();
            String base = filePath.substring(0, filePath.lastIndexOf("."));
            for(Map.Entry<ISO_3166, String> entry : map.entrySet()){
                ASS ass = assSubsTableModel.getAss();
                String filename = String.format("%s %s.ass", base, entry.getKey().getAlpha3());
                ASS.write(ass, filename, entry.getKey());
            }
        }
    }//GEN-LAST:event_mTableSaveLinkActionPerformed

    private void mTableCloseLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mTableCloseLinkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mTableCloseLinkActionPerformed

    private void mTableAutoResolveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mTableAutoResolveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mTableAutoResolveActionPerformed

    private void toggleCPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleCPSActionPerformed
        // Stats CPS (by default)
//        if(toggleCPS.isSelected()){
//            assSubsTableModel.setToCPS();
//        }else{
//            assSubsTableModel.setToCPL();
//        }
//        tableASS.updateUI();
    }//GEN-LAST:event_toggleCPSActionPerformed

    private void toggleCPLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleCPLActionPerformed
        // Stats CPL
//        if(toggleCPL.isSelected()){
//            assSubsTableModel.setToCPL();
//        }else{
//            assSubsTableModel.setToCPS();
//        }
//        tableASS.updateUI();
    }//GEN-LAST:event_toggleCPLActionPerformed

    private void toggleCPLMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_toggleCPLMouseWheelMoved
        // Stats CPL (threshold changer)
        int scroll = evt.getWheelRotation();
        if(scroll < 0){ // Up
            cplThreshold = cplThreshold + 1 > 5000 ? 5000 : cplThreshold + 1;
            assSubsTableModel.setThreshold(cplThreshold);
            toggleCPL.setText(String.format("%s x%d", "CPL", (int)cplThreshold));
            tableASS.updateUI();
        }
        if(scroll > 0){ // Down
            cplThreshold = cplThreshold - 1 < 3 ? 3 : cplThreshold - 1;
            assSubsTableModel.setThreshold(cplThreshold);
            toggleCPL.setText(String.format("%s x%d", "CPL", (int)cplThreshold));
            tableASS.updateUI();
        }
    }//GEN-LAST:event_toggleCPLMouseWheelMoved

    private void btnStrippedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStrippedActionPerformed
        // Stripped - only text
        assSubsTableModel.setStripped(AssTableModel2.NormalRenderer.Stripped.On);
        tableASS.updateUI();
    }//GEN-LAST:event_btnStrippedActionPerformed

    private void btnPartiallyStrippedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartiallyStrippedActionPerformed
        // Partially stripped - text with symbol
        assSubsTableModel.setStripped(AssTableModel2.NormalRenderer.Stripped.Partially);
        tableASS.updateUI();
    }//GEN-LAST:event_btnPartiallyStrippedActionPerformed

    private void btnNotStrippedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotStrippedActionPerformed
        // Not stripped - untouched text
        assSubsTableModel.setStripped(AssTableModel2.NormalRenderer.Stripped.Off);
        tableASS.updateUI();
    }//GEN-LAST:event_btnNotStrippedActionPerformed

    private void tableASSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableASSMouseClicked
        // Display line for editing
        if(evt.getButton() == java.awt.event.MouseEvent.BUTTON1 && evt.getClickCount() == 1){            
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 1) instanceof AssEvent.Type x){
                // Type
                cbComment.setSelected(x == AssEvent.Type.Comment);
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 2) instanceof Integer x){
                // Layer
                tfLayer.setText(Integer.toString(x));
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 3) instanceof AssTimeExtra x){
                // Start
                tfStart.setAssTime(x.getTime());
                // Sets end if duration is locked
                if(!tfEnd.isLock() && tfDuration.isLock()){
                    double msEnd = x.getTime().getMsTime() + tfDuration.getAssTime().getMsTime();
                    tfEnd.setAssTime(new AssTime(msEnd));
                }
                // Sets duration if end is locked
                if((tfEnd.isLock() && !tfDuration.isLock()) || (!tfEnd.isLock() && !tfDuration.isLock())){
                    double msDuration = tfEnd.getAssTime().getMsTime() - x.getTime().getMsTime();
                    tfDuration.setAssTime(new AssTime(msDuration));
                }
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 4) instanceof AssTimeExtra x){
                // End
                tfEnd.setAssTime(x.getTime());
                // Sets start if duration is locked
                if(!tfStart.isLock() && tfDuration.isLock()){
                    double msStart = x.getTime().getMsTime() - tfDuration.getAssTime().getMsTime();
                    tfStart.setAssTime(new AssTime(msStart));
                }
                // Sets duration if start is locked
                if((tfStart.isLock() && !tfDuration.isLock()) || (!tfStart.isLock() && !tfDuration.isLock())){
                    double msDuration = x.getTime().getMsTime() - tfStart.getAssTime().getMsTime();
                    tfDuration.setAssTime(new AssTime(msDuration));
                }
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 5) instanceof AssStyle x){
                // Style
                int index = -1;
                for(int i=0; i<comboBoxModelStyles.getSize(); i++){
                    if(comboBoxModelStyles.getElementAt(i) instanceof AssStyle style){
                        if(style.getName().equals(x.getName())){
                            index = i;
                            break;
                        }
                    }
                }
                if(index != -1){
                    comboBoxModelStyles.setSelectedItem(comboBoxModelStyles.getElementAt(index));
                }
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 6) instanceof AssActor x){
                // Actor
                int index = -1;
                for(int i=0; i<comboBoxModelActors.getSize(); i++){
                    if(comboBoxModelActors.getElementAt(i) instanceof AssActor actor){
                        if(actor.getName().equals(x.getName())){
                            index = i;
                            break;
                        }
                    }
                }
                if(index != -1){
                    comboBoxModelActors.setSelectedItem(comboBoxModelActors.getElementAt(index));
                }
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 7) instanceof Integer x){
                // ML
                tfML.setText(Integer.toString(x));
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 8) instanceof Integer x){
                // MR
                tfMR.setText(Integer.toString(x));
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 9) instanceof Integer x){
                // MV
                tfMV.setText(Integer.toString(x));
            }
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 10) instanceof AssEffect x){
                // Effect
                int index = -1;
                for(int i=0; i<comboBoxModelEffects.getSize(); i++){
                    if(comboBoxModelEffects.getElementAt(i) instanceof AssEffect effect){
                        if(effect.getName().equals(x.getName())){
                            index = i;
                            break;
                        }
                    }
                }
                if(index != -1){
                    comboBoxModelEffects.setSelectedItem(comboBoxModelEffects.getElementAt(index));
                }
            }
            // 11 => Stats
            if(assSubsTableModel.getValueAt(tableASS.getSelectedRow(), 12) instanceof AssEvent event){
                tpSourceFromTable.setText(event.getText());
                tpTextForTable.setText(event.getLinks().get(event.getCurrentLink()));                
            }
        }
    }//GEN-LAST:event_tableASSMouseClicked

    private void tpSourceFromTableCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_tpSourceFromTableCaretUpdate
        // TODO add your handling code here:
        if(tableASS.getSelectedRow() != -1){
            // Sert de support, pas copier en entier, juste les phrases
            AssEvent event = new AssEvent();
            
            event.setText(tpSourceFromTable.getText());
            event.getLinks().put(event.getCurrentLink(), tpTextForTable.getText());
            assSubsTableModel.setValueAt(event, tableASS.getSelectedRow(), 12);
        }
    }//GEN-LAST:event_tpSourceFromTableCaretUpdate

    private void tpTextForTableCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_tpTextForTableCaretUpdate
        // TODO add your handling code here:
        if(tableASS.getSelectedRow() != -1){
            // Sert de support, pas copier en entier, juste les phrases
            AssEvent event = new AssEvent();
            
            event.setText(tpSourceFromTable.getText());
            event.getLinks().put(event.getCurrentLink(), tpTextForTable.getText());
            assSubsTableModel.setValueAt(event, tableASS.getSelectedRow(), 12);
        }
    }//GEN-LAST:event_tpTextForTableCaretUpdate

    private void cbCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCommentActionPerformed
        // Sélectionne désélectionne le commentaire
        if(tableASS.getSelectedRow() != -1){
            AssEvent.Type x = cbComment.isSelected() ? AssEvent.Type.Comment : AssEvent.Type.Dialogue;
            assSubsTableModel.setValueAt(x, tableASS.getSelectedRow(), 1);
        }
    }//GEN-LAST:event_cbCommentActionPerformed

    private void cbStylesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStylesActionPerformed
        // Change le style si connu
        if(tableASS.getSelectedRow() != -1){
            AssStyle style = (AssStyle)comboBoxModelStyles.getSelectedItem();
            assSubsTableModel.setValueAt(style, tableASS.getSelectedRow(), 5);
        }
    }//GEN-LAST:event_cbStylesActionPerformed

    private void cbCommentMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_cbCommentMouseWheelMoved
        // Défilement - Comment ou Dialogue
        cbComment.setSelected(!cbComment.isSelected());
        if(tableASS.getSelectedRow() != -1){
            AssEvent.Type x = cbComment.isSelected() ? AssEvent.Type.Comment : AssEvent.Type.Dialogue;
            assSubsTableModel.setValueAt(x, tableASS.getSelectedRow(), 1);
        }
    }//GEN-LAST:event_cbCommentMouseWheelMoved

    private void cbStylesMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_cbStylesMouseWheelMoved
        // Choix du style
        if(comboBoxModelStyles.getSize() > 1){
            int index = comboBoxModelStyles.getIndexOf((AssStyle)comboBoxModelStyles.getSelectedItem());
            if(evt.getWheelRotation() < 0){
                index = index + 1 < comboBoxModelStyles.getSize() ? index + 1 : 0;
            }else if(evt.getWheelRotation() > 0){
                index = index - 1 >= 0 ? index - 1 : comboBoxModelStyles.getSize() - 1;
            }
            comboBoxModelStyles.setSelectedItem(comboBoxModelStyles.getElementAt(index));
            if(tableASS.getSelectedRow() != -1){
                assSubsTableModel.setValueAt(
                        (AssStyle)comboBoxModelStyles.getElementAt(index),
                        tableASS.getSelectedRow(),
                        5
                );
            }
        }
    }//GEN-LAST:event_cbStylesMouseWheelMoved

    private void cbActorsMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_cbActorsMouseWheelMoved
        // Choix de l'acteur
        if(comboBoxModelActors.getSize() > 1){
            int index = comboBoxModelActors.getIndexOf((AssActor)comboBoxModelActors.getSelectedItem());
            if(evt.getWheelRotation() < 0){
                index = index + 1 < comboBoxModelActors.getSize() ? index + 1 : 0;
            }else if(evt.getWheelRotation() > 0){
                index = index - 1 >= 0 ? index - 1 : comboBoxModelActors.getSize() - 1;
            }
            comboBoxModelActors.setSelectedItem(comboBoxModelActors.getElementAt(index));
            if(tableASS.getSelectedRow() != -1){
                assSubsTableModel.setValueAt(
                        (AssActor)comboBoxModelActors.getElementAt(index),
                        tableASS.getSelectedRow(),
                        6
                );
            }
        }
    }//GEN-LAST:event_cbActorsMouseWheelMoved

    private void cbEffectsMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_cbEffectsMouseWheelMoved
        // Choix de l'effet
        if(comboBoxModelEffects.getSize() > 1){
            int index = comboBoxModelEffects.getIndexOf((AssEffect)comboBoxModelEffects.getSelectedItem());
            if(evt.getWheelRotation() < 0){
                index = index + 1 < comboBoxModelEffects.getSize() ? index + 1 : 0;
            }else if(evt.getWheelRotation() > 0){
                index = index - 1 >= 0 ? index - 1 : comboBoxModelEffects.getSize() - 1;
            }
            comboBoxModelEffects.setSelectedItem(comboBoxModelEffects.getElementAt(index));
            if(tableASS.getSelectedRow() != -1){
                assSubsTableModel.setValueAt(
                        (AssEffect)comboBoxModelEffects.getElementAt(index),
                        tableASS.getSelectedRow(),
                        10
                );
            }
        }
    }//GEN-LAST:event_cbEffectsMouseWheelMoved

    private void tfLayerMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_tfLayerMouseWheelMoved
        // Choix de la couche
        tfLayer.setText(upDownWheelRotation(tfLayer.getText(), evt.getWheelRotation()));
        // Change la couche
        if(tableASS.getSelectedRow() != -1){            
            assSubsTableModel.setValueAt(Integer.valueOf(tfLayer.getText()), tableASS.getSelectedRow(), 2);
        }
    }//GEN-LAST:event_tfLayerMouseWheelMoved

    private void tfMLMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_tfMLMouseWheelMoved
        // Choix de la valeur
        tfML.setText(upDownWheelRotation(tfML.getText(), evt.getWheelRotation()));
        // Change la valeur
        if(tableASS.getSelectedRow() != -1){            
            assSubsTableModel.setValueAt(Integer.valueOf(tfML.getText()), tableASS.getSelectedRow(), 7);
        }
    }//GEN-LAST:event_tfMLMouseWheelMoved

    private void tfMRMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_tfMRMouseWheelMoved
        // Choix de la valeur
        tfMR.setText(upDownWheelRotation(tfMR.getText(), evt.getWheelRotation()));
        // Change la valeur
        if(tableASS.getSelectedRow() != -1){            
            assSubsTableModel.setValueAt(Integer.valueOf(tfMR.getText()), tableASS.getSelectedRow(), 8);
        }
    }//GEN-LAST:event_tfMRMouseWheelMoved

    private void tfMVMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_tfMVMouseWheelMoved
        // Choix de la valeur
        tfMV.setText(upDownWheelRotation(tfMV.getText(), evt.getWheelRotation()));
        // Change la valeur
        if(tableASS.getSelectedRow() != -1){            
            assSubsTableModel.setValueAt(Integer.valueOf(tfMV.getText()), tableASS.getSelectedRow(), 9);
        }
    }//GEN-LAST:event_tfMVMouseWheelMoved

    private void cbActorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActorsActionPerformed
        // Change l'acteur si connu
        if(tableASS.getSelectedRow() != -1){
            AssActor actor = (AssActor)comboBoxModelActors.getSelectedItem();
            assSubsTableModel.setValueAt(actor, tableASS.getSelectedRow(), 6);
        }
    }//GEN-LAST:event_cbActorsActionPerformed

    private void cbEffectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEffectsActionPerformed
        // Change l'effet si connu
        if(tableASS.getSelectedRow() != -1){
            AssEffect sfx = (AssEffect)comboBoxModelEffects.getSelectedItem();
            assSubsTableModel.setValueAt(sfx, tableASS.getSelectedRow(), 10);
        }
    }//GEN-LAST:event_cbEffectsActionPerformed

    private void tfMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfMLActionPerformed
        // Change la marge
        if(tableASS.getSelectedRow() != -1){
            assSubsTableModel.setValueAt(Integer.valueOf(tfML.getText()), tableASS.getSelectedRow(), 7);
        }
    }//GEN-LAST:event_tfMLActionPerformed

    private void tfMRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfMRActionPerformed
        // Change la marge
        if(tableASS.getSelectedRow() != -1){
            assSubsTableModel.setValueAt(Integer.valueOf(tfMR.getText()), tableASS.getSelectedRow(), 8);
        }
    }//GEN-LAST:event_tfMRActionPerformed

    private void tfMVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfMVActionPerformed
        // Change la marge
        if(tableASS.getSelectedRow() != -1){
            assSubsTableModel.setValueAt(Integer.valueOf(tfMV.getText()), tableASS.getSelectedRow(), 9);
        }
    }//GEN-LAST:event_tfMVActionPerformed

    private void popmUnselectLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmUnselectLineActionPerformed
        // Unselectect line of table if one is selected
        if(tableASS.getSelectedRow() != -1){
            tableASS.clearSelection();
        }        
    }//GEN-LAST:event_popmUnselectLineActionPerformed

    private void tfLayerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfLayerKeyReleased
        // Change l'effet si connu
        tfLayer.setText(reformatNumber(tfLayer.getText()));
        if(tableASS.getSelectedRow() != -1){
            String str = tfLayer.getText();
            if(str.equalsIgnoreCase("-")) str = "0";
            assSubsTableModel.setValueAt((int)Integer.parseInt(str), tableASS.getSelectedRow(), 2);
        }        
    }//GEN-LAST:event_tfLayerKeyReleased

    private void popmEdTpCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTpCutActionPerformed
        // Cut text
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            cutTextPane(p);
        }
    }//GEN-LAST:event_popmEdTpCutActionPerformed

    private void popmEdTpCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTpCopyActionPerformed
        // Cut text
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            copyTextPane(p);
        }
    }//GEN-LAST:event_popmEdTpCopyActionPerformed

    private void popmEdTpPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTpPasteActionPerformed
        // Cut text
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            pasteTextPane(p);
        }
    }//GEN-LAST:event_popmEdTpPasteActionPerformed

    private void popmEdTpClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTpClearActionPerformed
        // Clear text
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            p.setText("");
        }
    }//GEN-LAST:event_popmEdTpClearActionPerformed

    private void popmEdTpMirrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTpMirrorActionPerformed
        // Mirror between tpSource and tpText
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            if(p.hashCode() == tpSourceFromTable.hashCode()){
                tpTextForTable.setText(p.getText());
            }
            if(p.hashCode() == tpTextForTable.hashCode()){
                tpSourceFromTable.setText(p.getText());
            }
        }
    }//GEN-LAST:event_popmEdTpMirrorActionPerformed

    private void popmEdTagBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTagBActionPerformed
        // BOLD
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            insertTagAround(p, "b");
        }
    }//GEN-LAST:event_popmEdTagBActionPerformed

    private void popmEdTagIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTagIActionPerformed
        // ITALIC
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            insertTagAround(p, "i");
        }
    }//GEN-LAST:event_popmEdTagIActionPerformed

    private void popmEdTagUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTagUActionPerformed
        // UNDERLINE
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            insertTagAround(p, "u");
        }
    }//GEN-LAST:event_popmEdTagUActionPerformed

    private void popmEdTagSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTagSActionPerformed
        // STRIKEOUT
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            insertTagAround(p, "s");
        }
    }//GEN-LAST:event_popmEdTagSActionPerformed

    private void popmEdTagSimplifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmEdTagSimplifyActionPerformed
        // Call simplify
        if(popEditTP.getInvoker() instanceof javax.swing.JTextPane p){
            p.setText(simplify(p.getText()));
        }
    }//GEN-LAST:event_popmEdTagSimplifyActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgCPSCPL;
    private javax.swing.ButtonGroup bgOrigin;
    private javax.swing.ButtonGroup bgTranslation;
    private javax.swing.JButton btnEditActors;
    private javax.swing.JButton btnEditEffects;
    private javax.swing.JButton btnEditStyles;
    private javax.swing.JButton btnNewASS;
    private javax.swing.JButton btnNotStripped;
    private javax.swing.JButton btnOpenASS;
    private javax.swing.JButton btnOpenSound;
    private javax.swing.JButton btnOpenVideo;
    private javax.swing.JButton btnPartiallyStripped;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnSaveASS;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnStripped;
    private org.wingate.konnpetkoll.swing.PlaceholderComboBox cbActors;
    private javax.swing.JCheckBox cbComment;
    private org.wingate.konnpetkoll.swing.PlaceholderComboBox cbEffects;
    private org.wingate.konnpetkoll.swing.PlaceholderComboBox cbStyles;
    private javax.swing.JFileChooser fcASS;
    private javax.swing.JFileChooser fcAudio;
    private javax.swing.JFileChooser fcLinkLanguages;
    private javax.swing.JFileChooser fcVideo;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JMenuItem mFileQuit;
    private javax.swing.JMenuItem mTableAutoResolve;
    private javax.swing.JMenuItem mTableCloseLink;
    private javax.swing.JMenu mTableLinks;
    private javax.swing.JMenuItem mTableNewLink;
    private javax.swing.JMenuItem mTableOpenLink;
    private javax.swing.JMenuItem mTableSaveLink;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuFileTable;
    private javax.swing.JPanel panChat;
    private javax.swing.JPanel panMessageSending;
    private javax.swing.JPanel panWave;
    private org.wingate.konnpetkoll.swing.FFPlayer player;
    private javax.swing.JPopupMenu popEditTP;
    private javax.swing.JPopupMenu popTable;
    private javax.swing.JMenuItem popmEdTagB;
    private javax.swing.JMenuItem popmEdTagI;
    private javax.swing.JMenuItem popmEdTagS;
    private javax.swing.JMenuItem popmEdTagSimplify;
    private javax.swing.JMenuItem popmEdTagU;
    private javax.swing.JMenu popmEdTags;
    private javax.swing.JMenuItem popmEdTpClear;
    private javax.swing.JMenuItem popmEdTpCopy;
    private javax.swing.JMenuItem popmEdTpCut;
    private javax.swing.JMenuItem popmEdTpMirror;
    private javax.swing.JMenuItem popmEdTpPaste;
    private javax.swing.JMenu popmOrigin;
    private javax.swing.JMenu popmTranslation;
    private javax.swing.JMenuItem popmUnselectLine;
    private javax.swing.JTable tableASS;
    private org.wingate.feuille.util.LockFormatTextField tfDuration;
    private org.wingate.feuille.util.LockFormatTextField tfEnd;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfLayer;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfML;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfMR;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfMV;
    private org.wingate.feuille.util.LockFormatTextField tfStart;
    private javax.swing.JToggleButton toggleCPL;
    private javax.swing.JToggleButton toggleCPS;
    private javax.swing.JTextPane tpChat;
    private javax.swing.JTextPane tpSourceFromTable;
    private javax.swing.JTextPane tpTextForTable;
    private org.wingate.konnpetkoll.swing.Waveform waveform1;
    // End of variables declaration//GEN-END:variables
}
