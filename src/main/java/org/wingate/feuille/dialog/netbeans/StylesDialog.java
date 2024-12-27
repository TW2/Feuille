/*
 * Copyright (C) 2024 util2
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
package org.wingate.feuille.dialog.netbeans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import org.wingate.feuille.subs.ass.ASS;
import org.wingate.feuille.subs.ass.AssActor;
import org.wingate.feuille.subs.ass.AssComboBoxRenderer;
import org.wingate.feuille.subs.ass.AssStyle;
import org.wingate.feuille.subs.ass.AssView;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class StylesDialog extends javax.swing.JDialog {

    private List<AssStyle> styles = new ArrayList<>();
    private List<AssActor> actors = new ArrayList<>();
    
    private DialogResult dialogResult = DialogResult.None;
    private final java.awt.Frame parent;
    
    private final DefaultComboBoxModel cbModelStyle;
    private final DefaultComboBoxModel cbModelActor;
    private final DefaultComboBoxModel cbModelFonts;
    
    private final SpinnerNumberModel spModelFontsize;
    private final SpinnerNumberModel spModelMarginL;
    private final SpinnerNumberModel spModelMarginR;
    private final SpinnerNumberModel spModelMarginV;
    private final SpinnerNumberModel spModelOutline;
    private final SpinnerNumberModel spModelShadow;
    private final SpinnerNumberModel spModelScaleX;
    private final SpinnerNumberModel spModelScaleY;
    private final SpinnerNumberModel spModelSpacing;
    private final SpinnerNumberModel spModelAngleZ;
    
    private final ASS ass;
    private final AssView assPreview;
    
    private AssStyle curStyle = null;
    private AssActor curActor = null;
    
    /**
     * Creates new form StylesDialog
     * @param parent
     * @param modal
     */
    public StylesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent = parent;
        
        cbModelStyle = new DefaultComboBoxModel();
        cbStyle.setModel(cbModelStyle);
        cbStyle.setRenderer(new AssComboBoxRenderer());
        
        cbModelActor = new DefaultComboBoxModel();
        cbActor.setModel(cbModelActor);
        cbActor.setRenderer(new AssComboBoxRenderer());
        
        cbModelFonts = new DefaultComboBoxModel();
        cbFonts.setModel(cbModelFonts);
        cbFonts.setRenderer(new AssComboBoxRenderer());
        
        final List<Font> fonts = Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()
        );
        cbModelFonts.addAll(fonts);
        
        spModelFontsize = new SpinnerNumberModel(12, 5, 10000, 1);
        spModelMarginL = new SpinnerNumberModel(0, 0, 1000000, 1);
        spModelMarginR = new SpinnerNumberModel(0, 0, 1000000, 1);
        spModelMarginV = new SpinnerNumberModel(0, 0, 1000000, 1);
        spModelOutline = new SpinnerNumberModel(2d, 0d, 5000d, 0.01d);
        spModelShadow = new SpinnerNumberModel(2d, 0d, 5000d, 0.01d);
        spModelScaleX = new SpinnerNumberModel(100d, 1d, 100000d, 0.01d);
        spModelScaleY = new SpinnerNumberModel(100d, 1d, 100000d, 0.01d);
        spModelSpacing = new SpinnerNumberModel(0d, -10000d, 10000d, 0.01d);
        spModelAngleZ = new SpinnerNumberModel(0d, -50000d, 50000d, 0.01d);
        
        spinnerFontSize.setModel(spModelFontsize);
        spinnerML.setModel(spModelMarginL);
        spinnerMR.setModel(spModelMarginR);
        spinnerMV.setModel(spModelMarginV);
        spinnerOutline.setModel(spModelOutline);
        spinnerShadow.setModel(spModelShadow);
        spinnerScaleX.setModel(spModelScaleX);
        spinnerScaleY.setModel(spModelScaleY);
        spinnerSpacing.setModel(spModelSpacing);
        spinnerAngleZ.setModel(spModelAngleZ);
        
        ass = ASS.inMemory();
        assPreview = new AssView(ass, 852, 200);
        panPreview.add(assPreview, BorderLayout.CENTER);
        assPreview.setBackgroundColor(Color.white);
        assPreview.setSquareColor(DrawColor.dark_olive_green.getColor(.3f));        
    }
    
    public void showDialog(List<AssStyle> styles, List<AssActor> actors){
        this.styles = styles;
        this.actors = actors;
        
        if(styles.isEmpty()){
            curStyle = new AssStyle();
        }else{
            curStyle = styles.get(0);
        }
        
        fillStyles(); // Add styles to model
        
        
        if(actors.isEmpty()){
            curActor = new AssActor();
        }else{
            curActor = actors.get(0);
        }
        
        fillActors(); // Add actors to model
        
        assPreview.setSentence("A white guy painted in red is in the bath.", curStyle);
        panPreview.setSize(852, 200);
        assPreview.generate(250d);
        
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
    public DialogResult getDialogResult(){
        return dialogResult;
    }
    
    public List<AssStyle> getStyles(){
        return styles;
    }
    
    public List<AssActor> getActors(){
        return actors;
    }
    
    private void fillStyles(){
        cbModelStyle.removeAllElements();
        for(AssStyle style : styles){
            cbModelStyle.addElement(style);
        }
        if(cbModelStyle.getSize() != -1){
            cbStyle.setSelectedIndex(0);
        }
    }
    
    private void fillActors(){
        cbModelActor.removeAllElements();
        for(AssActor actor : actors){
            cbModelActor.addElement(actor);
        }
        if(cbModelActor.getSize() != -1){
            cbActor.setSelectedIndex(0);
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

        bgAlignment = new javax.swing.ButtonGroup();
        placeholderTextField1 = new org.wingate.konnpetkoll.swing.PlaceholderTextField();
        cbFonts = new org.wingate.konnpetkoll.swing.PlaceholderComboBox();
        spinnerFontSize = new javax.swing.JSpinner();
        cbStyle = new org.wingate.konnpetkoll.swing.PlaceholderComboBox();
        btnLoadDefaultCollection = new javax.swing.JButton();
        cbCollectionChoice = new org.wingate.konnpetkoll.swing.PlaceholderComboBox();
        btnRemoveCollection = new javax.swing.JButton();
        btnAddCollection = new javax.swing.JButton();
        btnRemoveStyle = new javax.swing.JButton();
        btnAddStyle = new javax.swing.JButton();
        btnCopyStyle = new javax.swing.JButton();
        cbActor = new org.wingate.konnpetkoll.swing.PlaceholderComboBox();
        btnEditActors = new javax.swing.JButton();
        tbStrikeOut = new javax.swing.JToggleButton();
        tbUnderline = new javax.swing.JToggleButton();
        tbItalic = new javax.swing.JToggleButton();
        tbBold = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        cvTextColor = new org.wingate.konnpetkoll.swing.ColorViewer();
        sliderTextColor = new javax.swing.JSlider();
        cvKaraokeColor = new org.wingate.konnpetkoll.swing.ColorViewer();
        sliderKaraokeColor = new javax.swing.JSlider();
        cvOutlineColor = new org.wingate.konnpetkoll.swing.ColorViewer();
        sliderOutlineColor = new javax.swing.JSlider();
        cvShadowColor = new org.wingate.konnpetkoll.swing.ColorViewer();
        sliderShadowColor = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        spinnerML = new javax.swing.JSpinner();
        spinnerMR = new javax.swing.JSpinner();
        spinnerMV = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        rb7 = new javax.swing.JRadioButton();
        rb8 = new javax.swing.JRadioButton();
        rb9 = new javax.swing.JRadioButton();
        rb4 = new javax.swing.JRadioButton();
        rb5 = new javax.swing.JRadioButton();
        rb6 = new javax.swing.JRadioButton();
        rb1 = new javax.swing.JRadioButton();
        rb2 = new javax.swing.JRadioButton();
        rb3 = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        spinnerOutline = new javax.swing.JSpinner();
        spinnerShadow = new javax.swing.JSpinner();
        cbBorderStyle = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        spinnerScaleX = new javax.swing.JSpinner();
        spinnerScaleY = new javax.swing.JSpinner();
        spinnerSpacing = new javax.swing.JSpinner();
        spinnerAngleZ = new javax.swing.JSpinner();
        cbEncoding = new org.wingate.konnpetkoll.swing.PlaceholderComboBox();
        tfSentencePreview = new org.wingate.konnpetkoll.swing.PlaceholderTextField();
        lblColorPreview = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnApplyNow = new javax.swing.JButton();
        panPreview = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Styles (EDITOR)");

        placeholderTextField1.setToolTipText("Collection");
        placeholderTextField1.setPlaceholder("Collection");

        cbFonts.setToolTipText("Font name");
        cbFonts.setPlaceholder("Font");
        cbFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFontsActionPerformed(evt);
            }
        });

        spinnerFontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerFontSizeStateChanged(evt);
            }
        });

        cbStyle.setToolTipText("Style");
        cbStyle.setPlaceholder("Style");
        cbStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStyleActionPerformed(evt);
            }
        });

        btnLoadDefaultCollection.setText("Get Default collection");
        btnLoadDefaultCollection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadDefaultCollectionActionPerformed(evt);
            }
        });

        cbCollectionChoice.setToolTipText("Choice your collection here");
        cbCollectionChoice.setPlaceholder("Collection");
        cbCollectionChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCollectionChoiceActionPerformed(evt);
            }
        });

        btnRemoveCollection.setText("-");
        btnRemoveCollection.setToolTipText("Delete a collection");
        btnRemoveCollection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveCollectionActionPerformed(evt);
            }
        });

        btnAddCollection.setText("+");
        btnAddCollection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCollectionActionPerformed(evt);
            }
        });

        btnRemoveStyle.setText("-");
        btnRemoveStyle.setToolTipText("Remove current style");
        btnRemoveStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveStyleActionPerformed(evt);
            }
        });

        btnAddStyle.setText("+");
        btnAddStyle.setToolTipText("Add a new style");
        btnAddStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStyleActionPerformed(evt);
            }
        });

        btnCopyStyle.setText("C");
        btnCopyStyle.setToolTipText("Copy current style");
        btnCopyStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyStyleActionPerformed(evt);
            }
        });

        cbActor.setToolTipText("Actor by default for this style");
        cbActor.setPlaceholder("Actor");
        cbActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbActorActionPerformed(evt);
            }
        });

        btnEditActors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 losange carre.png"))); // NOI18N
        btnEditActors.setToolTipText("Edit actors");
        btnEditActors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActorsActionPerformed(evt);
            }
        });

        tbStrikeOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 font style strikeout.png"))); // NOI18N
        tbStrikeOut.setToolTipText("StrikeOut");
        tbStrikeOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbStrikeOutActionPerformed(evt);
            }
        });

        tbUnderline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 font style underline.png"))); // NOI18N
        tbUnderline.setToolTipText("Underline");
        tbUnderline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbUnderlineActionPerformed(evt);
            }
        });

        tbItalic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 font style italic.png"))); // NOI18N
        tbItalic.setToolTipText("Italic");
        tbItalic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbItalicActionPerformed(evt);
            }
        });

        tbBold.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/wingate/feuille/16 font style bold.png"))); // NOI18N
        tbBold.setToolTipText("Bold");
        tbBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbBoldActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(1, 8, 2, 2));

        cvTextColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cvTextColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout cvTextColorLayout = new javax.swing.GroupLayout(cvTextColor);
        cvTextColor.setLayout(cvTextColorLayout);
        cvTextColorLayout.setHorizontalGroup(
            cvTextColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 104, Short.MAX_VALUE)
        );
        cvTextColorLayout.setVerticalGroup(
            cvTextColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel1.add(cvTextColor);

        sliderTextColor.setMaximum(255);
        sliderTextColor.setValue(0);
        sliderTextColor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderTextColorStateChanged(evt);
            }
        });
        jPanel1.add(sliderTextColor);

        cvKaraokeColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cvKaraokeColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout cvKaraokeColorLayout = new javax.swing.GroupLayout(cvKaraokeColor);
        cvKaraokeColor.setLayout(cvKaraokeColorLayout);
        cvKaraokeColorLayout.setHorizontalGroup(
            cvKaraokeColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 104, Short.MAX_VALUE)
        );
        cvKaraokeColorLayout.setVerticalGroup(
            cvKaraokeColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel1.add(cvKaraokeColor);

        sliderKaraokeColor.setMaximum(255);
        sliderKaraokeColor.setValue(0);
        sliderKaraokeColor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderKaraokeColorStateChanged(evt);
            }
        });
        jPanel1.add(sliderKaraokeColor);

        cvOutlineColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cvOutlineColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout cvOutlineColorLayout = new javax.swing.GroupLayout(cvOutlineColor);
        cvOutlineColor.setLayout(cvOutlineColorLayout);
        cvOutlineColorLayout.setHorizontalGroup(
            cvOutlineColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 104, Short.MAX_VALUE)
        );
        cvOutlineColorLayout.setVerticalGroup(
            cvOutlineColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel1.add(cvOutlineColor);

        sliderOutlineColor.setMaximum(255);
        sliderOutlineColor.setValue(0);
        sliderOutlineColor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderOutlineColorStateChanged(evt);
            }
        });
        jPanel1.add(sliderOutlineColor);

        cvShadowColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cvShadowColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout cvShadowColorLayout = new javax.swing.GroupLayout(cvShadowColor);
        cvShadowColor.setLayout(cvShadowColorLayout);
        cvShadowColorLayout.setHorizontalGroup(
            cvShadowColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 104, Short.MAX_VALUE)
        );
        cvShadowColorLayout.setVerticalGroup(
            cvShadowColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel1.add(cvShadowColor);

        sliderShadowColor.setMaximum(255);
        sliderShadowColor.setValue(0);
        sliderShadowColor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderShadowColorStateChanged(evt);
            }
        });
        jPanel1.add(sliderShadowColor);

        jPanel2.setLayout(new java.awt.GridLayout(1, 3, 2, 2));

        spinnerML.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerMLStateChanged(evt);
            }
        });
        jPanel2.add(spinnerML);

        spinnerMR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerMRStateChanged(evt);
            }
        });
        jPanel2.add(spinnerMR);

        spinnerMV.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerMVStateChanged(evt);
            }
        });
        jPanel2.add(spinnerMV);

        jPanel3.setLayout(new java.awt.GridLayout(3, 3, 2, 2));

        bgAlignment.add(rb7);
        rb7.setText("7");
        rb7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb7ActionPerformed(evt);
            }
        });
        jPanel3.add(rb7);

        bgAlignment.add(rb8);
        rb8.setText("8");
        rb8.setToolTipText("");
        rb8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb8ActionPerformed(evt);
            }
        });
        jPanel3.add(rb8);

        bgAlignment.add(rb9);
        rb9.setText("9");
        rb9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb9ActionPerformed(evt);
            }
        });
        jPanel3.add(rb9);

        bgAlignment.add(rb4);
        rb4.setText("4");
        rb4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb4ActionPerformed(evt);
            }
        });
        jPanel3.add(rb4);

        bgAlignment.add(rb5);
        rb5.setText("5");
        rb5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb5ActionPerformed(evt);
            }
        });
        jPanel3.add(rb5);

        bgAlignment.add(rb6);
        rb6.setText("6");
        rb6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb6ActionPerformed(evt);
            }
        });
        jPanel3.add(rb6);

        bgAlignment.add(rb1);
        rb1.setText("1");
        rb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb1ActionPerformed(evt);
            }
        });
        jPanel3.add(rb1);

        bgAlignment.add(rb2);
        rb2.setSelected(true);
        rb2.setText("2");
        rb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb2ActionPerformed(evt);
            }
        });
        jPanel3.add(rb2);

        bgAlignment.add(rb3);
        rb3.setText("3");
        rb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb3ActionPerformed(evt);
            }
        });
        jPanel3.add(rb3);

        jPanel4.setLayout(new java.awt.GridLayout(1, 3, 2, 2));

        spinnerOutline.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerOutlineStateChanged(evt);
            }
        });
        jPanel4.add(spinnerOutline);

        spinnerShadow.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerShadowStateChanged(evt);
            }
        });
        jPanel4.add(spinnerShadow);

        cbBorderStyle.setText("Border style");
        cbBorderStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBorderStyleActionPerformed(evt);
            }
        });
        jPanel4.add(cbBorderStyle);

        jPanel5.setLayout(new java.awt.GridLayout(1, 4, 2, 2));

        spinnerScaleX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerScaleXStateChanged(evt);
            }
        });
        jPanel5.add(spinnerScaleX);

        spinnerScaleY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerScaleYStateChanged(evt);
            }
        });
        jPanel5.add(spinnerScaleY);

        spinnerSpacing.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerSpacingStateChanged(evt);
            }
        });
        jPanel5.add(spinnerSpacing);

        spinnerAngleZ.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerAngleZStateChanged(evt);
            }
        });
        jPanel5.add(spinnerAngleZ);

        cbEncoding.setToolTipText("Encoding");
        cbEncoding.setPlaceholder("Encoding");
        cbEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEncodingActionPerformed(evt);
            }
        });

        tfSentencePreview.setToolTipText("Text for your preview");
        tfSentencePreview.setPlaceholder("Text for your preview");
        tfSentencePreview.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfSentencePreviewKeyReleased(evt);
            }
        });

        lblColorPreview.setBackground(new java.awt.Color(255, 255, 255));
        lblColorPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblColorPreview.setText("jLabel10");
        lblColorPreview.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblColorPreview.setOpaque(true);
        lblColorPreview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColorPreviewMouseClicked(evt);
            }
        });

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnApplyNow.setText("Apply current style");
        btnApplyNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyNowActionPerformed(evt);
            }
        });

        panPreview.setBackground(new java.awt.Color(255, 255, 255));
        panPreview.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panPreview.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panPreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLoadDefaultCollection)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbCollectionChoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddCollection)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveCollection))
                    .addComponent(placeholderTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbStyle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCopyStyle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddStyle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveStyle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbActor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditActors))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbFonts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbBold)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbItalic)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbUnderline)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbStrikeOut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
                            .addComponent(cbEncoding, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfSentencePreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblColorPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnApplyNow)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoadDefaultCollection)
                    .addComponent(cbCollectionChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemoveCollection)
                    .addComponent(btnAddCollection))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(placeholderTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemoveStyle)
                    .addComponent(btnAddStyle)
                    .addComponent(btnCopyStyle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbActor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditActors, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbFonts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbStrikeOut, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbUnderline, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbItalic, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbBold, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbEncoding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfSentencePreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblColorPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel)
                    .addComponent(btnApplyNow))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // OK
        dialogResult = DialogResult.OK;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Cancel
        dialogResult = DialogResult.Cancel;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnApplyNowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyNowActionPerformed
        // Apply current style to document
        // TODO
        assPreview.setSentence("A white guy painted in red is in the bath.", curStyle);
        assPreview.generate(250d);
    }//GEN-LAST:event_btnApplyNowActionPerformed

    private void btnLoadDefaultCollectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadDefaultCollectionActionPerformed
        // TODO add your handling code here:
        // SQL and collection in ass
    }//GEN-LAST:event_btnLoadDefaultCollectionActionPerformed

    private void btnAddCollectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCollectionActionPerformed
        // TODO add your handling code here:
        // SQL and collection in ass
    }//GEN-LAST:event_btnAddCollectionActionPerformed

    private void btnRemoveCollectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveCollectionActionPerformed
        // TODO add your handling code here:
        // SQL and collection in ass
    }//GEN-LAST:event_btnRemoveCollectionActionPerformed

    private void btnCopyStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyStyleActionPerformed
        // Copy style and apply to it a new name
        if(cbModelStyle.getSize() == 0) return;
        String name = JOptionPane.showInputDialog("Type a new name");
        if(name == null) return;
        // Check if the style already exists (if so, cancel order)
        boolean exists = false;
        for(AssStyle style : styles){
            if(style.getName().equalsIgnoreCase(name)){
                exists = true;
                break;
            }
        }
        if(exists == true){
            JOptionPane.showMessageDialog(parent, "The name already exists!");
            return;
        }
        if(cbModelStyle.getSelectedItem() instanceof AssStyle t){
            AssStyle newStyle;
            try {
                newStyle = t.clone();
                newStyle.setName(name);
                cbModelStyle.addElement(newStyle);
                styles.add(newStyle);
                cbModelStyle.setSelectedItem(newStyle);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(StylesDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnCopyStyleActionPerformed

    private void btnAddStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStyleActionPerformed
        // Add style
        String name = JOptionPane.showInputDialog("Type a new name");
        if(name == null) return;
        // Check if the style already exists (if so, cancel order)
        boolean exists = false;
        for(AssStyle style : styles){
            if(style.getName().equalsIgnoreCase(name)){
                exists = true;
                break;
            }
        }
        if(exists == true){
            JOptionPane.showMessageDialog(parent, "The name already exists!");
            return;
        }
        AssStyle newStyle = new AssStyle();
        newStyle.setName(name);
        styles.add(newStyle);
        fillStyles();        
    }//GEN-LAST:event_btnAddStyleActionPerformed

    private void btnRemoveStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveStyleActionPerformed
        // Remove selected style
        if(cbStyle.getSelectedIndex() != -1){
            if(cbModelStyle.getSelectedItem() instanceof AssStyle t){
                if(t.getName().equalsIgnoreCase("default")){
                    JOptionPane.showMessageDialog(
                            parent,
                            "The 'Default' style can not be deleted!"
                    );
                    return;
                }
                styles.remove(t);
                fillStyles();
            }
        }
    }//GEN-LAST:event_btnRemoveStyleActionPerformed

    private void btnEditActorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActorsActionPerformed
        // Edit actors
        ActorsDialog dialog = new ActorsDialog(parent, true);
        dialog.showDialog(actors);
        if(dialog.getDialogResult() == DialogResult.OK){            
            try {
                actors = dialog.getActors();
                fillActors();
            } catch (IOException ex) {
                Logger.getLogger(StylesDialog.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }//GEN-LAST:event_btnEditActorsActionPerformed

    private void tbBoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbBoldActionPerformed
        // Change style
        curStyle.getAssFont().setBold(tbBold.isSelected());
    }//GEN-LAST:event_tbBoldActionPerformed

    private void tbItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbItalicActionPerformed
        // Change style
        curStyle.getAssFont().setItalic(tbItalic.isSelected());
    }//GEN-LAST:event_tbItalicActionPerformed

    private void tbUnderlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbUnderlineActionPerformed
        // Change style
        curStyle.getAssFont().setUnderline(tbUnderline.isSelected());
    }//GEN-LAST:event_tbUnderlineActionPerformed

    private void tbStrikeOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbStrikeOutActionPerformed
        // Change style
        curStyle.getAssFont().setStrikeout(tbStrikeOut.isSelected());
    }//GEN-LAST:event_tbStrikeOutActionPerformed

    private void cbCollectionChoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCollectionChoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCollectionChoiceActionPerformed

    private void cbStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStyleActionPerformed
        // Select style
        curStyle = (AssStyle)cbStyle.getSelectedItem();
        
    }//GEN-LAST:event_cbStyleActionPerformed

    private void cbActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActorActionPerformed
        // Change actor
        if(cbActor.getSelectedItem() == null) return;
        if(cbActor.getSelectedItem() instanceof AssActor actor){
            curActor = actor;
        }        
    }//GEN-LAST:event_cbActorActionPerformed

    private void cbFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFontsActionPerformed
        // Change font name
        if(cbFonts.getSelectedItem() == null) return;
        if(cbFonts.getSelectedItem() instanceof Font x){
            curStyle.getAssFont().setFont(x);
        }
    }//GEN-LAST:event_cbFontsActionPerformed

    private void spinnerFontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerFontSizeStateChanged
        // Change font size
        if(spinnerFontSize.getValue() instanceof Double value){
            float v = Float.parseFloat(Double.toString(value));
            curStyle.getAssFont().setSize(v);
        }
    }//GEN-LAST:event_spinnerFontSizeStateChanged

    private void sliderTextColorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderTextColorStateChanged
        // Change color (text)
        Color x = curStyle.getTextColor().getColor();
        int sliderValue = 255 - sliderTextColor.getValue();
        Color c = new Color(x.getRed(), x.getGreen(), x.getBlue(), sliderValue);
        curStyle.getTextColor().setColor(c);
        cvTextColor.setShowColor(c);
    }//GEN-LAST:event_sliderTextColorStateChanged

    private void sliderKaraokeColorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderKaraokeColorStateChanged
        // Change color (karaoke)
        Color x = curStyle.getKaraokeColor().getColor();
        int sliderValue = 255 - sliderKaraokeColor.getValue();
        Color c = new Color(x.getRed(), x.getGreen(), x.getBlue(), sliderValue);
        curStyle.getKaraokeColor().setColor(c);
        cvKaraokeColor.setShowColor(c);
    }//GEN-LAST:event_sliderKaraokeColorStateChanged

    private void sliderOutlineColorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderOutlineColorStateChanged
        // Change color (outline)
        Color x = curStyle.getOutlineColor().getColor();
        int sliderValue = 255 - sliderOutlineColor.getValue();
        Color c = new Color(x.getRed(), x.getGreen(), x.getBlue(), sliderValue);
        curStyle.getOutlineColor().setColor(c);
        cvOutlineColor.setShowColor(c);
    }//GEN-LAST:event_sliderOutlineColorStateChanged

    private void sliderShadowColorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderShadowColorStateChanged
        // Change color (shadow)
        Color x = curStyle.getShadowColor().getColor();
        int sliderValue = 255 - sliderShadowColor.getValue();
        Color c = new Color(x.getRed(), x.getGreen(), x.getBlue(), sliderValue);
        curStyle.getShadowColor().setColor(c);
        cvShadowColor.setShowColor(c);
    }//GEN-LAST:event_sliderShadowColorStateChanged

    private void spinnerMLStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerMLStateChanged
        // Change margin (left)
        if(spinnerML.getValue() instanceof Integer value){
            curStyle.setMarginL(value);
        }
    }//GEN-LAST:event_spinnerMLStateChanged

    private void spinnerMRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerMRStateChanged
        // Change margin (right)
        if(spinnerMR.getValue() instanceof Integer value){
            curStyle.setMarginR(value);
        }
    }//GEN-LAST:event_spinnerMRStateChanged

    private void spinnerMVStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerMVStateChanged
        // Change margin (vertical)
        if(spinnerMV.getValue() instanceof Integer value){
            curStyle.setMarginV(value);
        }
    }//GEN-LAST:event_spinnerMVStateChanged

    private void rb7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb7ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(7);
    }//GEN-LAST:event_rb7ActionPerformed

    private void rb8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb8ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(8);
    }//GEN-LAST:event_rb8ActionPerformed

    private void rb9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb9ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(9);
    }//GEN-LAST:event_rb9ActionPerformed

    private void rb4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb4ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(4);
    }//GEN-LAST:event_rb4ActionPerformed

    private void rb5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb5ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(5);
    }//GEN-LAST:event_rb5ActionPerformed

    private void rb6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb6ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(6);
    }//GEN-LAST:event_rb6ActionPerformed

    private void rb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb1ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(1);
    }//GEN-LAST:event_rb1ActionPerformed

    private void rb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb2ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(2);
    }//GEN-LAST:event_rb2ActionPerformed

    private void rb3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb3ActionPerformed
        // Change alignment
        curStyle.getAlignment().setNumber(3);
    }//GEN-LAST:event_rb3ActionPerformed

    private void spinnerOutlineStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerOutlineStateChanged
        // Change outline thickness
        if(spinnerOutline.getValue() instanceof Double value){
            float v = Float.parseFloat(Double.toString(value));
            curStyle.setOutline(v);
        }
    }//GEN-LAST:event_spinnerOutlineStateChanged

    private void spinnerShadowStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerShadowStateChanged
        // Change shadow thickness
        if(spinnerShadow.getValue() instanceof Double value){
            float v = Float.parseFloat(Double.toString(value));
            curStyle.setShadow(v);
        }
    }//GEN-LAST:event_spinnerShadowStateChanged

    private void cbBorderStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBorderStyleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBorderStyleActionPerformed

    private void spinnerScaleXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerScaleXStateChanged
        // Change scale x
        if(spinnerScaleX.getValue() instanceof Double value){
            float v = Float.parseFloat(Double.toString(value));
            curStyle.setScaleX(v);
        }
    }//GEN-LAST:event_spinnerScaleXStateChanged

    private void spinnerScaleYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerScaleYStateChanged
        // Change scale y
        if(spinnerScaleY.getValue() instanceof Double value){
            float v = Float.parseFloat(Double.toString(value));
            curStyle.setScaleY(v);
        }
    }//GEN-LAST:event_spinnerScaleYStateChanged

    private void spinnerSpacingStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSpacingStateChanged
        // Change spacing
        if(spinnerSpacing.getValue() instanceof Double value){
            float v = Float.parseFloat(Double.toString(value));
            curStyle.setSpacing(v);
        }
    }//GEN-LAST:event_spinnerSpacingStateChanged

    private void spinnerAngleZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerAngleZStateChanged
        // Change angle z
        if(spinnerAngleZ.getValue() instanceof Double value){
            float v = Float.parseFloat(Double.toString(value));
            curStyle.setAngleZ(v);
        }
    }//GEN-LAST:event_spinnerAngleZStateChanged

    private void cbEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEncodingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEncodingActionPerformed

    private void tfSentencePreviewKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSentencePreviewKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tfSentencePreviewKeyReleased

    private void lblColorPreviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColorPreviewMouseClicked
        // Change color (preview)
        Color c = JColorChooser.showDialog(parent, "Choose a color", lblColorPreview.getBackground());
        if(c == null) return;
        lblColorPreview.setBackground(c);
        // TODO carreaux en couleur avec c dans panel preview
    }//GEN-LAST:event_lblColorPreviewMouseClicked

    private void cvTextColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cvTextColorMouseClicked
        // Change color (text)
        Color value = curStyle.getTextColor().getColor();
        Color c = JColorChooser.showDialog(parent, "Choose a color", value);
        if(c == null) return;
        int sliderValue = 255 - sliderTextColor.getValue();
        Color x = new Color(c.getRed(), c.getGreen(), c.getBlue(), sliderValue);
        curStyle.getTextColor().setColor(x);
        cvTextColor.setShowColor(x);
    }//GEN-LAST:event_cvTextColorMouseClicked

    private void cvKaraokeColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cvKaraokeColorMouseClicked
        // Change color (karaoke)
        Color value = curStyle.getKaraokeColor().getColor();
        Color c = JColorChooser.showDialog(parent, "Choose a color", value);
        if(c == null) return;
        int sliderValue = 255 - sliderKaraokeColor.getValue();
        Color x = new Color(c.getRed(), c.getGreen(), c.getBlue(), sliderValue);
        curStyle.getKaraokeColor().setColor(x);
        cvKaraokeColor.setShowColor(x);
    }//GEN-LAST:event_cvKaraokeColorMouseClicked

    private void cvOutlineColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cvOutlineColorMouseClicked
        // Change color (outline)
        Color value = curStyle.getOutlineColor().getColor();
        Color c = JColorChooser.showDialog(parent, "Choose a color", value);
        if(c == null) return;
        int sliderValue = 255 - sliderOutlineColor.getValue();
        Color x = new Color(c.getRed(), c.getGreen(), c.getBlue(), sliderValue);
        curStyle.getOutlineColor().setColor(x);
        cvOutlineColor.setShowColor(x);
    }//GEN-LAST:event_cvOutlineColorMouseClicked

    private void cvShadowColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cvShadowColorMouseClicked
        // Change color (shadow)
        Color value = curStyle.getShadowColor().getColor();
        Color c = JColorChooser.showDialog(parent, "Choose a color", value);
        if(c == null) return;
        int sliderValue = 255 - sliderShadowColor.getValue();
        Color x = new Color(c.getRed(), c.getGreen(), c.getBlue(), sliderValue);
        curStyle.getShadowColor().setColor(x);
        cvShadowColor.setShowColor(x);
    }//GEN-LAST:event_cvShadowColorMouseClicked

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
            java.util.logging.Logger.getLogger(StylesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            StylesDialog dialog = new StylesDialog(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgAlignment;
    private javax.swing.JButton btnAddCollection;
    private javax.swing.JButton btnAddStyle;
    private javax.swing.JButton btnApplyNow;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCopyStyle;
    private javax.swing.JButton btnEditActors;
    private javax.swing.JButton btnLoadDefaultCollection;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnRemoveCollection;
    private javax.swing.JButton btnRemoveStyle;
    private org.wingate.konnpetkoll.swing.PlaceholderComboBox cbActor;
    private javax.swing.JCheckBox cbBorderStyle;
    private org.wingate.konnpetkoll.swing.PlaceholderComboBox cbCollectionChoice;
    private org.wingate.konnpetkoll.swing.PlaceholderComboBox cbEncoding;
    private org.wingate.konnpetkoll.swing.PlaceholderComboBox cbFonts;
    private org.wingate.konnpetkoll.swing.PlaceholderComboBox cbStyle;
    private org.wingate.konnpetkoll.swing.ColorViewer cvKaraokeColor;
    private org.wingate.konnpetkoll.swing.ColorViewer cvOutlineColor;
    private org.wingate.konnpetkoll.swing.ColorViewer cvShadowColor;
    private org.wingate.konnpetkoll.swing.ColorViewer cvTextColor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblColorPreview;
    private javax.swing.JPanel panPreview;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField placeholderTextField1;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JRadioButton rb3;
    private javax.swing.JRadioButton rb4;
    private javax.swing.JRadioButton rb5;
    private javax.swing.JRadioButton rb6;
    private javax.swing.JRadioButton rb7;
    private javax.swing.JRadioButton rb8;
    private javax.swing.JRadioButton rb9;
    private javax.swing.JSlider sliderKaraokeColor;
    private javax.swing.JSlider sliderOutlineColor;
    private javax.swing.JSlider sliderShadowColor;
    private javax.swing.JSlider sliderTextColor;
    private javax.swing.JSpinner spinnerAngleZ;
    private javax.swing.JSpinner spinnerFontSize;
    private javax.swing.JSpinner spinnerML;
    private javax.swing.JSpinner spinnerMR;
    private javax.swing.JSpinner spinnerMV;
    private javax.swing.JSpinner spinnerOutline;
    private javax.swing.JSpinner spinnerScaleX;
    private javax.swing.JSpinner spinnerScaleY;
    private javax.swing.JSpinner spinnerShadow;
    private javax.swing.JSpinner spinnerSpacing;
    private javax.swing.JToggleButton tbBold;
    private javax.swing.JToggleButton tbItalic;
    private javax.swing.JToggleButton tbStrikeOut;
    private javax.swing.JToggleButton tbUnderline;
    private org.wingate.konnpetkoll.swing.PlaceholderTextField tfSentencePreview;
    // End of variables declaration//GEN-END:variables
}
