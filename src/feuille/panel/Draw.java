/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.panel;

import feuille.MainFrame;
import feuille.drawing.History;
import feuille.drawing.HistoryItem;
import feuille.drawing.HistoryItemType;
import feuille.drawing.Sketchpad;
import feuille.drawing.renderer.HistoryListRenderer;
import feuille.drawing.shape.ShapeType;
import feuille.util.ISO_3166;
import feuille.util.Language;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.DefaultListModel;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;

/**
 *
 * @author util2
 */
public class Draw extends javax.swing.JPanel {
    
    JFrame fullScreenFrame = new JFrame();
    Sketchpad sketchpad;
    History history;
    
    DefaultListModel dlmHistory = new DefaultListModel();

    /**
     * Creates new form Draw
     */
    public Draw() {
        initComponents();
        init();
    }
    
    private void init(){
        sketchpad = new Sketchpad(this);
        history = new History(this, sketchpad);
        
        // Dimension
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        GraphicsConfiguration gconf = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        Insets insets = toolkit.getScreenInsets(gconf);
        fullScreenFrame.setSize(dim.width - insets.left - insets.right,
                dim.height - insets.top - insets.bottom);
        fullScreenFrame.setLocationRelativeTo(null);
        fullScreenFrame.getContentPane().setLayout(new BorderLayout());
        
        drawingPanel.setLayout(new BorderLayout());
        drawingPanel.add(sketchpad, BorderLayout.CENTER);
        
        Language chosen = MainFrame.getLanguage();
        ISO_3166 iso = MainFrame.getISOCountry();
        
        String translated;
        translated = chosen.getTranslated("DrawingToolsImageAlpha", iso, "Image alpha");
        lblImageTransparency.setText(translated+" : "+Integer.toString(sliderImageTransparency.getValue())+"%");              
        translated = chosen.getTranslated("DrawingToolsImageSize", iso, "Image size");
        lblImageSize.setText(translated+" : "+Integer.toString(sliderImageSize.getValue())+"%");                             
        translated = chosen.getTranslated("DrawingToolsShapesAlpha", iso, "Shapes alpha");
        lblShapesAlpha.setText(translated+" : "+Integer.toString(sliderShapesAlpha.getValue())+"%");                               
        translated = chosen.getTranslated("DrawingToolsDrawingScale", iso, "Drawing scale");
        lblDrawingScale.setText(translated+" : "+Float.toString(((float)sliderDrawingScale.getValue())/2f));
        
        listHistory.setModel(dlmHistory);
        listHistory.setCellRenderer(new HistoryListRenderer(this));
        
        commandPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                tfASSCommand.setSize(commandPanel.getWidth()-400, tfASSCommand.getHeight());
                tfLayerName.setSize(commandPanel.getWidth()-800, tfLayerName.getHeight());
            }            
        });
    }
    
    public void setCoordinates(int x, int y){
        lblCoordinates.setText(x+","+y);
    }
    
    public void setScale(float scale){
        sliderDrawingScale.setValue(Math.round(scale*2f));
        String translated = MainFrame.getLanguage().getTranslated("DrawingToolsDrawingScale", MainFrame.getISOCountry(), "Drawing scale");
        lblDrawingScale.setText(translated+" : "+Float.toString(((float)sliderDrawingScale.getValue())/2f));
    }
    
    public JList getHistoryList(){
        return listHistory;
    }
    
    public DefaultListModel getHistoryListModel(){
        return dlmHistory;
    }
    
    public History getHistory(){
        return history;
    }
    
    private void transportView(){
        if(toggleEmbedded.isSelected() == true){
            // Embedded is selected (normal mode)
            fullScreenFrame.setVisible(false);
            fullScreenFrame.getContentPane().remove(embedPanel);            
            add(embedPanel, BorderLayout.CENTER);
            embedPanel.revalidate();
        }else{
            // Embedded isn't selected (full screen mode)
            remove(embedPanel);
            fullScreenFrame.getContentPane().add(embedPanel, BorderLayout.CENTER);
            fullScreenFrame.setVisible(true);
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

        bgFullEmbed = new javax.swing.ButtonGroup();
        bgTools = new javax.swing.ButtonGroup();
        bgMode = new javax.swing.ButtonGroup();
        embedPanel = new javax.swing.JPanel();
        drawingPanel = new javax.swing.JPanel();
        commandPanel = new javax.swing.JPanel();
        toggleLine = new javax.swing.JToggleButton();
        toggleQuad = new javax.swing.JToggleButton();
        toggleCubic = new javax.swing.JToggleButton();
        toggleBSpline = new javax.swing.JToggleButton();
        toggleBSplineClose = new javax.swing.JToggleButton();
        toggleBSplineStop = new javax.swing.JToggleButton();
        togglePointer = new javax.swing.JToggleButton();
        lblCoordinates = new javax.swing.JLabel();
        toggleFullScreen = new javax.swing.JToggleButton();
        toggleEmbedded = new javax.swing.JToggleButton();
        tfASSCommand = new javax.swing.JTextField();
        btnSetCommand = new javax.swing.JButton();
        btnGetCommand = new javax.swing.JButton();
        btnImageUp = new javax.swing.JButton();
        btnImageCenter = new javax.swing.JButton();
        btnImageRight = new javax.swing.JButton();
        btnImageLeft = new javax.swing.JButton();
        btnImageDown = new javax.swing.JButton();
        btnNewDocument = new javax.swing.JButton();
        btnOpenDocument = new javax.swing.JButton();
        btnSaveDocument = new javax.swing.JButton();
        btnLoadCharacters = new javax.swing.JButton();
        btnImageOpen = new javax.swing.JButton();
        btnImageClear = new javax.swing.JButton();
        sliderImageTransparency = new javax.swing.JSlider();
        lblImageTransparency = new javax.swing.JLabel();
        toggleMCommand = new javax.swing.JToggleButton();
        toggleNCommand = new javax.swing.JToggleButton();
        toggleCrayon = new javax.swing.JToggleButton();
        toggleGomme = new javax.swing.JToggleButton();
        toggleMove = new javax.swing.JToggleButton();
        toggleRotate = new javax.swing.JToggleButton();
        toggleResize = new javax.swing.JToggleButton();
        toggleShear = new javax.swing.JToggleButton();
        toggleOperations = new javax.swing.JToggleButton();
        toggleGridState = new javax.swing.JToggleButton();
        sliderShapesAlpha = new javax.swing.JSlider();
        sliderDrawingScale = new javax.swing.JSlider();
        lblShapesAlpha = new javax.swing.JLabel();
        lblDrawingScale = new javax.swing.JLabel();
        sliderImageSize = new javax.swing.JSlider();
        lblImageSize = new javax.swing.JLabel();
        lblLayerColor = new javax.swing.JLabel();
        tfLayerName = new javax.swing.JTextField();
        toggleRose = new javax.swing.JToggleButton();
        toggleTournesol = new javax.swing.JToggleButton();
        toggleImageMagicTool = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listHistory = new javax.swing.JList<>();
        btnHistRewind = new javax.swing.JButton();
        btnHistForward = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        drawingPanel.setBackground(new java.awt.Color(51, 153, 255));

        javax.swing.GroupLayout drawingPanelLayout = new javax.swing.GroupLayout(drawingPanel);
        drawingPanel.setLayout(drawingPanelLayout);
        drawingPanelLayout.setHorizontalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        drawingPanelLayout.setVerticalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        commandPanel.setLayout(null);

        bgTools.add(toggleLine);
        toggleLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingLine.png"))); // NOI18N
        toggleLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleLineActionPerformed(evt);
            }
        });
        commandPanel.add(toggleLine);
        toggleLine.setBounds(400, 80, 40, 40);

        bgTools.add(toggleQuad);
        toggleQuad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/afm splines 03.png"))); // NOI18N
        toggleQuad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleQuadActionPerformed(evt);
            }
        });
        commandPanel.add(toggleQuad);
        toggleQuad.setBounds(440, 80, 40, 40);

        bgTools.add(toggleCubic);
        toggleCubic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBezier.png"))); // NOI18N
        toggleCubic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleCubicActionPerformed(evt);
            }
        });
        commandPanel.add(toggleCubic);
        toggleCubic.setBounds(480, 80, 40, 40);

        bgTools.add(toggleBSpline);
        toggleBSpline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBSpline.png"))); // NOI18N
        toggleBSpline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleBSplineActionPerformed(evt);
            }
        });
        commandPanel.add(toggleBSpline);
        toggleBSpline.setBounds(520, 80, 40, 40);

        bgTools.add(toggleBSplineClose);
        toggleBSplineClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingCloseBSpline.png"))); // NOI18N
        commandPanel.add(toggleBSplineClose);
        toggleBSplineClose.setBounds(560, 80, 40, 40);

        bgTools.add(toggleBSplineStop);
        toggleBSplineStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-NextAfterBSpline.png"))); // NOI18N
        commandPanel.add(toggleBSplineStop);
        toggleBSplineStop.setBounds(600, 80, 40, 40);

        bgTools.add(togglePointer);
        togglePointer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_cur.png"))); // NOI18N
        togglePointer.setSelected(true);
        togglePointer.setToolTipText("");
        togglePointer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePointerActionPerformed(evt);
            }
        });
        commandPanel.add(togglePointer);
        togglePointer.setBounds(360, 80, 40, 40);

        lblCoordinates.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblCoordinates.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCoordinates.setText("-1000,-1000");
        commandPanel.add(lblCoordinates);
        lblCoordinates.setBounds(0, 0, 160, 40);

        bgFullEmbed.add(toggleFullScreen);
        toggleFullScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_enlarge.png"))); // NOI18N
        toggleFullScreen.setToolTipText("Full screen");
        toggleFullScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleFullScreenActionPerformed(evt);
            }
        });
        commandPanel.add(toggleFullScreen);
        toggleFullScreen.setBounds(0, 40, 40, 40);

        bgFullEmbed.add(toggleEmbedded);
        toggleEmbedded.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_reduce.png"))); // NOI18N
        toggleEmbedded.setSelected(true);
        toggleEmbedded.setToolTipText("Embedded");
        toggleEmbedded.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleEmbeddedActionPerformed(evt);
            }
        });
        commandPanel.add(toggleEmbedded);
        toggleEmbedded.setBounds(40, 40, 40, 40);
        commandPanel.add(tfASSCommand);
        tfASSCommand.setBounds(400, 0, 610, 40);

        btnSetCommand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_editpaste.png"))); // NOI18N
        btnSetCommand.setToolTipText("Set");
        commandPanel.add(btnSetCommand);
        btnSetCommand.setBounds(360, 0, 40, 40);

        btnGetCommand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        btnGetCommand.setToolTipText("Get");
        commandPanel.add(btnGetCommand);
        btnGetCommand.setBounds(320, 0, 40, 40);

        btnImageUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f02.gif"))); // NOI18N
        commandPanel.add(btnImageUp);
        btnImageUp.setBounds(160, 0, 40, 40);

        btnImageCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f05.gif"))); // NOI18N
        commandPanel.add(btnImageCenter);
        btnImageCenter.setBounds(160, 40, 40, 40);

        btnImageRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f06.gif"))); // NOI18N
        commandPanel.add(btnImageRight);
        btnImageRight.setBounds(200, 40, 40, 40);

        btnImageLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f04.gif"))); // NOI18N
        commandPanel.add(btnImageLeft);
        btnImageLeft.setBounds(120, 40, 40, 40);

        btnImageDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f08.gif"))); // NOI18N
        commandPanel.add(btnImageDown);
        btnImageDown.setBounds(160, 80, 40, 40);

        btnNewDocument.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_filenew.png"))); // NOI18N
        commandPanel.add(btnNewDocument);
        btnNewDocument.setBounds(0, 80, 40, 40);

        btnOpenDocument.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_filesystem_folder_grey_open.png"))); // NOI18N
        commandPanel.add(btnOpenDocument);
        btnOpenDocument.setBounds(40, 80, 40, 40);

        btnSaveDocument.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_device_floppy_unmount.png"))); // NOI18N
        commandPanel.add(btnSaveDocument);
        btnSaveDocument.setBounds(80, 80, 40, 40);

        btnLoadCharacters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_mimetype_font_type1.png"))); // NOI18N
        commandPanel.add(btnLoadCharacters);
        btnLoadCharacters.setBounds(120, 80, 40, 40);

        btnImageOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_app_kpaint.png"))); // NOI18N
        commandPanel.add(btnImageOpen);
        btnImageOpen.setBounds(240, 40, 40, 40);

        btnImageClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_app_windows_users.png"))); // NOI18N
        commandPanel.add(btnImageClear);
        btnImageClear.setBounds(280, 40, 40, 40);

        sliderImageTransparency.setToolTipText("");
        sliderImageTransparency.setValue(100);
        sliderImageTransparency.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderImageTransparencyStateChanged(evt);
            }
        });
        commandPanel.add(sliderImageTransparency);
        sliderImageTransparency.setBounds(200, 0, 120, 20);

        lblImageTransparency.setText("Image alpha : 100%");
        commandPanel.add(lblImageTransparency);
        lblImageTransparency.setBounds(210, 20, 110, 20);

        bgTools.add(toggleMCommand);
        toggleMCommand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/move m.png"))); // NOI18N
        commandPanel.add(toggleMCommand);
        toggleMCommand.setBounds(640, 80, 40, 40);

        bgTools.add(toggleNCommand);
        toggleNCommand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/move n.png"))); // NOI18N
        commandPanel.add(toggleNCommand);
        toggleNCommand.setBounds(680, 80, 40, 40);

        bgTools.add(toggleCrayon);
        toggleCrayon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32-Crayon.png"))); // NOI18N
        commandPanel.add(toggleCrayon);
        toggleCrayon.setBounds(720, 80, 40, 40);

        bgTools.add(toggleGomme);
        toggleGomme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32-Gomme.png"))); // NOI18N
        commandPanel.add(toggleGomme);
        toggleGomme.setBounds(760, 80, 40, 40);

        bgTools.add(toggleMove);
        toggleMove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-translate.png"))); // NOI18N
        commandPanel.add(toggleMove);
        toggleMove.setBounds(320, 40, 40, 40);

        bgTools.add(toggleRotate);
        toggleRotate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-rotate.png"))); // NOI18N
        commandPanel.add(toggleRotate);
        toggleRotate.setBounds(360, 40, 40, 40);

        bgTools.add(toggleResize);
        toggleResize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-scale.png"))); // NOI18N
        commandPanel.add(toggleResize);
        toggleResize.setBounds(400, 40, 40, 40);

        bgTools.add(toggleShear);
        toggleShear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-shear.png"))); // NOI18N
        commandPanel.add(toggleShear);
        toggleShear.setBounds(440, 40, 40, 40);

        bgTools.add(toggleOperations);
        toggleOperations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Selection.png"))); // NOI18N
        commandPanel.add(toggleOperations);
        toggleOperations.setBounds(480, 40, 40, 40);

        toggleGridState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/gridlocker.png"))); // NOI18N
        commandPanel.add(toggleGridState);
        toggleGridState.setBounds(80, 40, 40, 40);

        sliderShapesAlpha.setValue(20);
        sliderShapesAlpha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderShapesAlphaStateChanged(evt);
            }
        });
        commandPanel.add(sliderShapesAlpha);
        sliderShapesAlpha.setBounds(520, 40, 120, 20);

        sliderDrawingScale.setMaximum(20);
        sliderDrawingScale.setMinimum(2);
        sliderDrawingScale.setValue(2);
        sliderDrawingScale.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderDrawingScaleStateChanged(evt);
            }
        });
        commandPanel.add(sliderDrawingScale);
        sliderDrawingScale.setBounds(640, 40, 120, 20);

        lblShapesAlpha.setText("Shapes alpha : 20%");
        commandPanel.add(lblShapesAlpha);
        lblShapesAlpha.setBounds(530, 60, 110, 20);

        lblDrawingScale.setText("Drawing scale : 1");
        commandPanel.add(lblDrawingScale);
        lblDrawingScale.setBounds(650, 60, 110, 20);

        sliderImageSize.setMaximum(1000);
        sliderImageSize.setMinimum(10);
        sliderImageSize.setValue(100);
        sliderImageSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderImageSizeStateChanged(evt);
            }
        });
        commandPanel.add(sliderImageSize);
        sliderImageSize.setBounds(200, 80, 120, 20);

        lblImageSize.setText("Image size : 100%");
        commandPanel.add(lblImageSize);
        lblImageSize.setBounds(210, 100, 110, 20);

        lblLayerColor.setBackground(new java.awt.Color(0, 255, 0));
        lblLayerColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        lblLayerColor.setOpaque(true);
        lblLayerColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLayerColorMouseClicked(evt);
            }
        });
        commandPanel.add(lblLayerColor);
        lblLayerColor.setBounds(760, 40, 40, 40);

        tfLayerName.setText("Default layer");
        commandPanel.add(tfLayerName);
        tfLayerName.setBounds(800, 40, 210, 40);

        bgMode.add(toggleRose);
        toggleRose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32-Rose.png"))); // NOI18N
        toggleRose.setSelected(true);
        commandPanel.add(toggleRose);
        toggleRose.setBounds(800, 80, 40, 40);

        bgMode.add(toggleTournesol);
        toggleTournesol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32-ornement.png"))); // NOI18N
        commandPanel.add(toggleTournesol);
        toggleTournesol.setBounds(840, 80, 40, 40);

        toggleImageMagicTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/magic-wand_icon-icons.com_48268 - 32.png"))); // NOI18N
        commandPanel.add(toggleImageMagicTool);
        toggleImageMagicTool.setBounds(320, 80, 40, 40);

        jPanel2.setLayout(null);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listHistory.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listHistory);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(0, 40, 290, 230);

        btnHistRewind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/40_rewind.png"))); // NOI18N
        btnHistRewind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistRewindActionPerformed(evt);
            }
        });
        jPanel2.add(btnHistRewind);
        btnHistRewind.setBounds(0, 0, 40, 40);

        btnHistForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/40_forward.png"))); // NOI18N
        btnHistForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistForwardActionPerformed(evt);
            }
        });
        jPanel2.add(btnHistForward);
        btnHistForward.setBounds(40, 0, 40, 40);

        jTabbedPane1.addTab("History", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 272, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Graphical scripting", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 285, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Layers", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 285, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Ornaments", jPanel5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addComponent(jTabbedPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2))
        );

        javax.swing.GroupLayout embedPanelLayout = new javax.swing.GroupLayout(embedPanel);
        embedPanel.setLayout(embedPanelLayout);
        embedPanelLayout.setHorizontalGroup(
            embedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(commandPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1225, Short.MAX_VALUE)
            .addGroup(embedPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        embedPanelLayout.setVerticalGroup(
            embedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(embedPanelLayout.createSequentialGroup()
                .addComponent(commandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(embedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(drawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        add(embedPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void toggleFullScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleFullScreenActionPerformed
        transportView();
    }//GEN-LAST:event_toggleFullScreenActionPerformed

    private void toggleEmbeddedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleEmbeddedActionPerformed
        transportView();
    }//GEN-LAST:event_toggleEmbeddedActionPerformed

    private void togglePointerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togglePointerActionPerformed
        sketchpad.setToolInCourse(ShapeType.Unknown);
        sketchpad.stop();
    }//GEN-LAST:event_togglePointerActionPerformed

    private void toggleLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleLineActionPerformed
        sketchpad.setToolInCourse(ShapeType.Line);
        sketchpad.recall();        
    }//GEN-LAST:event_toggleLineActionPerformed

    private void toggleQuadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleQuadActionPerformed
        sketchpad.setToolInCourse(ShapeType.QuadraticCurve);
        sketchpad.recall();        
    }//GEN-LAST:event_toggleQuadActionPerformed

    private void toggleCubicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleCubicActionPerformed
        sketchpad.setToolInCourse(ShapeType.CubicCurve);
        sketchpad.recall();        
    }//GEN-LAST:event_toggleCubicActionPerformed

    private void toggleBSplineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleBSplineActionPerformed
        sketchpad.setToolInCourse(ShapeType.SplineCurve);
        sketchpad.recall();        
    }//GEN-LAST:event_toggleBSplineActionPerformed

    private void sliderImageTransparencyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderImageTransparencyStateChanged
        String translated = MainFrame.getLanguage().getTranslated("DrawingToolsImageAlpha", MainFrame.getISOCountry(), "Image alpha");
        lblImageTransparency.setText(translated+" : "+Integer.toString(sliderImageTransparency.getValue())+"%");
    }//GEN-LAST:event_sliderImageTransparencyStateChanged

    private void sliderImageSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderImageSizeStateChanged
        String translated = MainFrame.getLanguage().getTranslated("DrawingToolsImageSize", MainFrame.getISOCountry(), "Image size");
        lblImageSize.setText(translated+" : "+Integer.toString(sliderImageSize.getValue())+"%");
    }//GEN-LAST:event_sliderImageSizeStateChanged

    private void sliderShapesAlphaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderShapesAlphaStateChanged
        String translated = MainFrame.getLanguage().getTranslated("DrawingToolsShapesAlpha", MainFrame.getISOCountry(), "Shapes alpha");
        lblShapesAlpha.setText(translated+" : "+Integer.toString(sliderShapesAlpha.getValue())+"%");
    }//GEN-LAST:event_sliderShapesAlphaStateChanged

    private void sliderDrawingScaleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderDrawingScaleStateChanged
        String translated = MainFrame.getLanguage().getTranslated("DrawingToolsDrawingScale", MainFrame.getISOCountry(), "Drawing scale");
        lblDrawingScale.setText(translated+" : "+Float.toString(((float)sliderDrawingScale.getValue())/2f));
        sketchpad.setScale(((float)sliderDrawingScale.getValue())/2f);
    }//GEN-LAST:event_sliderDrawingScaleStateChanged

    private void lblLayerColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLayerColorMouseClicked
        lblLayerColor.setBackground(JColorChooser.showDialog(new JFrame(), "Choose a color for the current layer", lblLayerColor.getBackground()));
    }//GEN-LAST:event_lblLayerColorMouseClicked

    private void btnHistRewindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistRewindActionPerformed
        history.undo();
        listHistory.updateUI();
        sketchpad.repaint();
    }//GEN-LAST:event_btnHistRewindActionPerformed

    private void btnHistForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistForwardActionPerformed
        history.redo();
        listHistory.updateUI();
        sketchpad.repaint();
    }//GEN-LAST:event_btnHistForwardActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgFullEmbed;
    private javax.swing.ButtonGroup bgMode;
    private javax.swing.ButtonGroup bgTools;
    private javax.swing.JButton btnGetCommand;
    private javax.swing.JButton btnHistForward;
    private javax.swing.JButton btnHistRewind;
    private javax.swing.JButton btnImageCenter;
    private javax.swing.JButton btnImageClear;
    private javax.swing.JButton btnImageDown;
    private javax.swing.JButton btnImageLeft;
    private javax.swing.JButton btnImageOpen;
    private javax.swing.JButton btnImageRight;
    private javax.swing.JButton btnImageUp;
    private javax.swing.JButton btnLoadCharacters;
    private javax.swing.JButton btnNewDocument;
    private javax.swing.JButton btnOpenDocument;
    private javax.swing.JButton btnSaveDocument;
    private javax.swing.JButton btnSetCommand;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JPanel drawingPanel;
    private javax.swing.JPanel embedPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblCoordinates;
    private javax.swing.JLabel lblDrawingScale;
    private javax.swing.JLabel lblImageSize;
    private javax.swing.JLabel lblImageTransparency;
    private javax.swing.JLabel lblLayerColor;
    private javax.swing.JLabel lblShapesAlpha;
    private javax.swing.JList<String> listHistory;
    private javax.swing.JSlider sliderDrawingScale;
    private javax.swing.JSlider sliderImageSize;
    private javax.swing.JSlider sliderImageTransparency;
    private javax.swing.JSlider sliderShapesAlpha;
    private javax.swing.JTextField tfASSCommand;
    private javax.swing.JTextField tfLayerName;
    private javax.swing.JToggleButton toggleBSpline;
    private javax.swing.JToggleButton toggleBSplineClose;
    private javax.swing.JToggleButton toggleBSplineStop;
    private javax.swing.JToggleButton toggleCrayon;
    private javax.swing.JToggleButton toggleCubic;
    private javax.swing.JToggleButton toggleEmbedded;
    private javax.swing.JToggleButton toggleFullScreen;
    private javax.swing.JToggleButton toggleGomme;
    private javax.swing.JToggleButton toggleGridState;
    private javax.swing.JToggleButton toggleImageMagicTool;
    private javax.swing.JToggleButton toggleLine;
    private javax.swing.JToggleButton toggleMCommand;
    private javax.swing.JToggleButton toggleMove;
    private javax.swing.JToggleButton toggleNCommand;
    private javax.swing.JToggleButton toggleOperations;
    private javax.swing.JToggleButton togglePointer;
    private javax.swing.JToggleButton toggleQuad;
    private javax.swing.JToggleButton toggleResize;
    private javax.swing.JToggleButton toggleRose;
    private javax.swing.JToggleButton toggleRotate;
    private javax.swing.JToggleButton toggleShear;
    private javax.swing.JToggleButton toggleTournesol;
    // End of variables declaration//GEN-END:variables
}
