package org.wingate.feuille;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.wingate.feuille.dialog.ActorsDialog;
import org.wingate.feuille.dialog.StylesDialog;
import org.wingate.feuille.subs.ass.ASS;
import org.wingate.feuille.subs.ass.AssActor;
import org.wingate.feuille.subs.ass.AssComboBoxRenderer;
import org.wingate.feuille.subs.ass.AssEffect;
import org.wingate.feuille.subs.ass.AssEvent;
import org.wingate.feuille.subs.ass.AssStyle;
import org.wingate.feuille.subs.ass.AssTableModel;
import org.wingate.feuille.subs.ass.AssTime;
import org.wingate.feuille.util.Clipboard;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.LanguageAccessoryPanel;

/**
 *
 * @author util2
 */
public class MainFrame extends javax.swing.JFrame {
    
    private final AssTableModel assSubsTableModel;
    private final DefaultComboBoxModel comboBoxModelStyles;
    private final DefaultComboBoxModel comboBoxModelActors;
    private final DefaultComboBoxModel comboBoxModelEffects;
    
    private final LanguageAccessoryPanel languageAccess;
    private float cplThreshold = 60f;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        
        assSubsTableModel = new AssTableModel(tableASS);
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
        tfStart = new javax.swing.JTextField();
        tfEnd = new javax.swing.JTextField();
        tfTotal = new javax.swing.JTextField();
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
            .addGap(0, 907, Short.MAX_VALUE)
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
        jPanel11.add(cbComment);

        jPanel8.setLayout(new java.awt.BorderLayout());

        cbStyles.setToolTipText("Styles");
        cbStyles.setPlaceholder("Style");
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
        jPanel11.add(tfLayer);

        tfStart.setText("0.00.00.00");
        tfStart.setToolTipText("Start");
        jPanel11.add(tfStart);

        tfEnd.setText("0.00.00.00");
        tfEnd.setToolTipText("End");
        jPanel11.add(tfEnd);

        tfTotal.setText("0.00.00.00");
        tfTotal.setToolTipText("Duration");
        jPanel11.add(tfTotal);

        tfML.setToolTipText("Scroll your mouse to change the left margin value");
        tfML.setPlaceholder("0");
        jPanel11.add(tfML);

        tfMR.setToolTipText("Scroll your mouse to change the right margin value");
        tfMR.setPlaceholder("0");
        jPanel11.add(tfMR);

        tfMV.setToolTipText("Scroll your mouse to change the vertical margin value");
        tfMV.setPlaceholder("0");
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
        tpTextForTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tpTextForTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tpTextForTable);

        jSplitPane3.setBottomComponent(jScrollPane4);

        tpSourceFromTable.setToolTipText("<html>Click on the middle button of the mouse to add an event.<br> Click on the right button of the mouse to have more choices.<br> Double left click to paste text.");
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
            .addGap(0, 917, Short.MAX_VALUE)
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
            .addGap(0, 917, Short.MAX_VALUE)
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
            .addGap(0, 917, Short.MAX_VALUE)
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
                    AssTime.create(tfStart.getText()),
                    AssTime.create(tfEnd.getText()),
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
                    AssTime.create(tfStart.getText()),
                    AssTime.create(tfEnd.getText()),
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
        if(toggleCPS.isSelected()){
            assSubsTableModel.setToCPS();
        }else{
            assSubsTableModel.setToCPL();
        }
        tableASS.updateUI();
    }//GEN-LAST:event_toggleCPSActionPerformed

    private void toggleCPLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleCPLActionPerformed
        // Stats CPL
        if(toggleCPL.isSelected()){
            assSubsTableModel.setToCPL();
        }else{
            assSubsTableModel.setToCPS();
        }
        tableASS.updateUI();
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
        assSubsTableModel.setStripped(AssTableModel.NormalRenderer.Stripped.On);
        tableASS.updateUI();
    }//GEN-LAST:event_btnStrippedActionPerformed

    private void btnPartiallyStrippedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartiallyStrippedActionPerformed
        // Partially stripped - text with symbol
        assSubsTableModel.setStripped(AssTableModel.NormalRenderer.Stripped.Partially);
        tableASS.updateUI();
    }//GEN-LAST:event_btnPartiallyStrippedActionPerformed

    private void btnNotStrippedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotStrippedActionPerformed
        // Not stripped - untouched text
        assSubsTableModel.setStripped(AssTableModel.NormalRenderer.Stripped.Off);
        tableASS.updateUI();
    }//GEN-LAST:event_btnNotStrippedActionPerformed

    private void tableASSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableASSMouseClicked
        // Display line for editing
        if(evt.getButton() == java.awt.event.MouseEvent.BUTTON1
                && evt.getClickCount() == 2){
            tpSourceFromTable.setText(assSubsTableModel.getValueAt(
                    tableASS.getSelectedRow(),
                    12
            ).toString());
        }
    }//GEN-LAST:event_tableASSMouseClicked

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgCPSCPL;
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
    private javax.swing.JTable tableASS;
    private javax.swing.JTextField tfEnd;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfLayer;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfML;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfMR;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfMV;
    private javax.swing.JTextField tfStart;
    private javax.swing.JTextField tfTotal;
    private javax.swing.JToggleButton toggleCPL;
    private javax.swing.JToggleButton toggleCPS;
    private javax.swing.JTextPane tpChat;
    private javax.swing.JTextPane tpSourceFromTable;
    private javax.swing.JTextPane tpTextForTable;
    private org.wingate.konnpetkoll.swing.Waveform waveform1;
    // End of variables declaration//GEN-END:variables
}
