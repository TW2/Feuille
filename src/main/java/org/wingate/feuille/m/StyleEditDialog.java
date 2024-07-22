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
package org.wingate.feuille.m;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.border.LineBorder;
import org.wingate.feuille.ass.ASS;
import org.wingate.feuille.ass.AssEncoding;
import org.wingate.feuille.ass.AssEvent;
import org.wingate.feuille.ass.AssStyle;
import org.wingate.feuille.ass.AssTime;
import org.wingate.feuille.util.DialogResult;

/**
 *
 * @author util2
 */
public class StyleEditDialog extends javax.swing.JDialog implements Runnable {

    private final PreviewPanel previewPanel;
    private ColorDialog colorDialog;
    
    private DialogResult dialogResult = DialogResult.Unknown;
    private final java.awt.Frame parent;
    
    private final DefaultListModel modelFontNames;
    private final DefaultComboBoxModel modelEncoding;
    
    /**
     * Creates new form StyleEditDialog2
     * @param parent
     * @param modal
     */
    public StyleEditDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.parent = parent;
        
        previewPanel = new PreviewPanel();
        embedPanel.add(previewPanel, BorderLayout.CENTER);
        
        modelFontNames = new DefaultListModel();
        listFontNames.setModel(modelFontNames);
        
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for(String s : fonts){
            modelFontNames.addElement(s);
        }
        
        modelEncoding = new DefaultComboBoxModel();
        cbEncoding.setModel(modelEncoding);
        
        for(AssEncoding enc : AssEncoding.values()){
            modelEncoding.addElement(enc);
        }
        modelEncoding.setSelectedItem(AssEncoding.Default);
    }
    
    public void showDialog(){
        setLocationRelativeTo(parent);
        setVisible(true);
        
        startThread();
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }

    public AssStyle updateStyle(){
        AssStyle style = AssStyle.getDefault();
        
        style.setFontname(listFontNames.getSelectedIndex() == -1 ?
                "Arial" : listFontNames.getSelectedValue());
        style.setFontsize((int)spinFontSize.getValue());
        int[] index = listFontStyles.getSelectedIndices();
        for(int i=0; i<index.length; i++){
            String x = listFontStyles.getModel().getElementAt(index[i]);
            if(x.equalsIgnoreCase("bold")) style.setBold(true);
            if(x.equalsIgnoreCase("italic")) style.setItalic(true);
            if(x.equalsIgnoreCase("underline")) style.setUnderline(true);
            if(x.equalsIgnoreCase("strikeout")) style.setStrikeout(true);
        }
        if(rb1.isSelected()) style.setAlignment(1);
        if(rb2.isSelected()) style.setAlignment(2);
        if(rb3.isSelected()) style.setAlignment(3);
        if(rb4.isSelected()) style.setAlignment(4);
        if(rb5.isSelected()) style.setAlignment(5);
        if(rb6.isSelected()) style.setAlignment(6);
        if(rb7.isSelected()) style.setAlignment(7);
        if(rb8.isSelected()) style.setAlignment(8);
        if(rb9.isSelected()) style.setAlignment(9);
        style.setMarginL((int)spinL.getValue());
        style.setMarginR((int)spinR.getValue());
        style.setMarginV((int)spinV.getValue());
        style.setOutline((int)spinBorder.getValue());
        style.setShadow((int)spinShadow.getValue());
        style.setBorderStyle(chkOpaqueBox.isSelected());
        style.setScaleX((int)spinScaleX.getValue());
        style.setScaleY((int)spinScaleY.getValue());
        style.setAngle((int)spinAngleZ.getValue());
        style.setSpacing((int)spinSpacing.getValue());
        Color c1 = lblColorText.getBackground();
        int a1 = 255 - (int)spinAlphaText.getValue();
        style.setTextColor(new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), a1));
        Color c2 = lblColorKaraoke.getBackground();
        int a2 = 255 - (int)spinAlphaKaraoke.getValue();
        style.setTextColor(new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), a2));
        Color c3 = lblColorOutline.getBackground();
        int a3 = 255 - (int)spinAlphaOutline.getValue();
        style.setTextColor(new Color(c3.getRed(), c3.getGreen(), c3.getBlue(), a3));
        Color c4 = lblColorShadow.getBackground();
        int a4 = 255 - (int)spinAlphaShadow.getValue();
        style.setTextColor(new Color(c4.getRed(), c4.getGreen(), c4.getBlue(), a4));
        
        return style;
    }
    
    private void updatePreview(){
        previewPanel.updatePreview(tfSample.getText(), updateStyle());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFontName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listFontNames = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        listFontStyles = new javax.swing.JList<>();
        lblFontStyle = new javax.swing.JLabel();
        spinFontSize = new javax.swing.JSpinner();
        lblFontSize = new javax.swing.JLabel();
        lblStyleName = new javax.swing.JLabel();
        tfStyleName = new javax.swing.JTextField();
        lblBorder = new javax.swing.JLabel();
        spinBorder = new javax.swing.JSpinner();
        lblShadow = new javax.swing.JLabel();
        spinShadow = new javax.swing.JSpinner();
        chkOpaqueBox = new javax.swing.JCheckBox();
        rb7 = new javax.swing.JRadioButton();
        rb4 = new javax.swing.JRadioButton();
        rb1 = new javax.swing.JRadioButton();
        rb8 = new javax.swing.JRadioButton();
        rb5 = new javax.swing.JRadioButton();
        rb2 = new javax.swing.JRadioButton();
        rb3 = new javax.swing.JRadioButton();
        rb9 = new javax.swing.JRadioButton();
        rb6 = new javax.swing.JRadioButton();
        lblAlign = new javax.swing.JLabel();
        lblMarginL = new javax.swing.JLabel();
        lblMarginR = new javax.swing.JLabel();
        lblMarginV = new javax.swing.JLabel();
        spinL = new javax.swing.JSpinner();
        spinR = new javax.swing.JSpinner();
        spinV = new javax.swing.JSpinner();
        lblScaleX = new javax.swing.JLabel();
        lblScaleY = new javax.swing.JLabel();
        spinScaleX = new javax.swing.JSpinner();
        spinScaleY = new javax.swing.JSpinner();
        lblAngleZ = new javax.swing.JLabel();
        lblSpacing = new javax.swing.JLabel();
        lblEncoding = new javax.swing.JLabel();
        spinAngleZ = new javax.swing.JSpinner();
        spinSpacing = new javax.swing.JSpinner();
        cbEncoding = new javax.swing.JComboBox<>();
        lblSample = new javax.swing.JLabel();
        tfSample = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnCollections = new javax.swing.JButton();
        btnImportStyles = new javax.swing.JButton();
        embedPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblColor1 = new javax.swing.JLabel();
        lblColor2 = new javax.swing.JLabel();
        lblColor3 = new javax.swing.JLabel();
        lblColor4 = new javax.swing.JLabel();
        lblColorText = new javax.swing.JLabel();
        lblColorKaraoke = new javax.swing.JLabel();
        lblColorOutline = new javax.swing.JLabel();
        lblColorShadow = new javax.swing.JLabel();
        spinAlphaText = new javax.swing.JSpinner();
        spinAlphaKaraoke = new javax.swing.JSpinner();
        spinAlphaOutline = new javax.swing.JSpinner();
        spinAlphaShadow = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblFontName.setText("Font name : ");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listFontNames.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listFontNames.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listFontNames.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listFontNamesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listFontNames);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listFontStyles.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Plain", "Bold", "Italic", "Underline", "StrikeOut" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listFontStyles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listFontStylesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listFontStyles);

        lblFontStyle.setText("Font style : ");

        spinFontSize.setModel(new javax.swing.SpinnerNumberModel(54, 2, 1000, 1));
        spinFontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinFontSizeStateChanged(evt);
            }
        });

        lblFontSize.setText("Font size : ");

        lblStyleName.setText("Style name : ");

        tfStyleName.setText("Default");

        lblBorder.setText("Border : ");

        spinBorder.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000, 1));
        spinBorder.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinBorderStateChanged(evt);
            }
        });

        lblShadow.setText("Shadow : ");

        spinShadow.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000, 1));
        spinShadow.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinShadowStateChanged(evt);
            }
        });

        chkOpaqueBox.setText("Opaque box");
        chkOpaqueBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOpaqueBoxActionPerformed(evt);
            }
        });

        rb7.setText("7");
        rb7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb7ActionPerformed(evt);
            }
        });

        rb4.setText("4");
        rb4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb4ActionPerformed(evt);
            }
        });

        rb1.setText("1");
        rb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb1ActionPerformed(evt);
            }
        });

        rb8.setText("8");
        rb8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb8ActionPerformed(evt);
            }
        });

        rb5.setText("5");
        rb5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb5ActionPerformed(evt);
            }
        });

        rb2.setSelected(true);
        rb2.setText("2");
        rb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb2ActionPerformed(evt);
            }
        });

        rb3.setText("3");
        rb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb3ActionPerformed(evt);
            }
        });

        rb9.setText("9");
        rb9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb9ActionPerformed(evt);
            }
        });

        rb6.setText("6");
        rb6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb6ActionPerformed(evt);
            }
        });

        lblAlign.setText("Alignment : ");

        lblMarginL.setText("L : ");
        lblMarginL.setToolTipText("Margin L");

        lblMarginR.setText("R : ");
        lblMarginR.setToolTipText("Margin R");

        lblMarginV.setText("V : ");
        lblMarginV.setToolTipText("Margin V");

        spinL.setModel(new javax.swing.SpinnerNumberModel(0, 0, 9000, 1));
        spinL.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinLStateChanged(evt);
            }
        });

        spinR.setModel(new javax.swing.SpinnerNumberModel(0, 0, 9000, 1));
        spinR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinRStateChanged(evt);
            }
        });

        spinV.setModel(new javax.swing.SpinnerNumberModel(0, 0, 9000, 1));
        spinV.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinVStateChanged(evt);
            }
        });

        lblScaleX.setText("Scale X (%) : ");

        lblScaleY.setText("Scale Y (%) : ");

        spinScaleX.setModel(new javax.swing.SpinnerNumberModel(100, 0, 10000, 1));
        spinScaleX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinScaleXStateChanged(evt);
            }
        });

        spinScaleY.setModel(new javax.swing.SpinnerNumberModel(100, 0, 10000, 1));
        spinScaleY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinScaleYStateChanged(evt);
            }
        });

        lblAngleZ.setText("Angle Z (°) : ");

        lblSpacing.setText("Spacing : ");

        lblEncoding.setText("Encoding : ");

        spinAngleZ.setModel(new javax.swing.SpinnerNumberModel(0, -10000, 10000, 1));
        spinAngleZ.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinAngleZStateChanged(evt);
            }
        });

        spinSpacing.setModel(new javax.swing.SpinnerNumberModel(0, -10000, 10000, 1));
        spinSpacing.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinSpacingStateChanged(evt);
            }
        });

        cbEncoding.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblSample.setText("Sample : ");

        tfSample.setText("yggdrasil is an unknown new comer at the corner of the square 1234567890");
        tfSample.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfSampleActionPerformed(evt);
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

        btnCollections.setText("Collections...");

        btnImportStyles.setText("Import styles...");

        embedPanel.setLayout(new java.awt.BorderLayout());

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(3, 4));

        lblColor1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblColor1.setText("Text / Color & Alpha");
        jPanel2.add(lblColor1);

        lblColor2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblColor2.setText("Karaoke / Color & Alpha");
        jPanel2.add(lblColor2);

        lblColor3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblColor3.setText("Outline / Color & Alpha");
        jPanel2.add(lblColor3);

        lblColor4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblColor4.setText("Shadow / Color & Alpha");
        jPanel2.add(lblColor4);

        lblColorText.setBackground(new java.awt.Color(255, 255, 255));
        lblColorText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblColorText.setOpaque(true);
        lblColorText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColorTextMouseClicked(evt);
            }
        });
        jPanel2.add(lblColorText);

        lblColorKaraoke.setBackground(new java.awt.Color(255, 255, 0));
        lblColorKaraoke.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblColorKaraoke.setOpaque(true);
        lblColorKaraoke.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColorKaraokeMouseClicked(evt);
            }
        });
        jPanel2.add(lblColorKaraoke);

        lblColorOutline.setBackground(new java.awt.Color(0, 0, 0));
        lblColorOutline.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblColorOutline.setOpaque(true);
        lblColorOutline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColorOutlineMouseClicked(evt);
            }
        });
        jPanel2.add(lblColorOutline);

        lblColorShadow.setBackground(new java.awt.Color(0, 0, 0));
        lblColorShadow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblColorShadow.setOpaque(true);
        lblColorShadow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColorShadowMouseClicked(evt);
            }
        });
        jPanel2.add(lblColorShadow);

        spinAlphaText.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spinAlphaText.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinAlphaTextStateChanged(evt);
            }
        });
        jPanel2.add(spinAlphaText);

        spinAlphaKaraoke.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spinAlphaKaraoke.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinAlphaKaraokeStateChanged(evt);
            }
        });
        jPanel2.add(spinAlphaKaraoke);

        spinAlphaOutline.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spinAlphaOutline.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinAlphaOutlineStateChanged(evt);
            }
        });
        jPanel2.add(spinAlphaOutline);

        spinAlphaShadow.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spinAlphaShadow.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinAlphaShadowStateChanged(evt);
            }
        });
        jPanel2.add(spinAlphaShadow);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(embedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnImportStyles)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCollections)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblFontName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblFontSize)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spinFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFontStyle)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rb7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rb8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rb9))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rb4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rb5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rb6))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rb1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rb2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rb3))
                            .addComponent(lblAlign)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblMarginV)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spinV))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMarginL)
                                    .addComponent(lblMarginR))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spinL)
                                    .addComponent(spinR))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAngleZ)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinAngleZ, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblScaleY)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinScaleY, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblScaleX)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinScaleX, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblSpacing)
                                        .addGap(22, 22, 22)
                                        .addComponent(spinSpacing, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblBorder)
                                            .addComponent(lblShadow))
                                        .addGap(22, 22, 22)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(spinShadow)
                                            .addComponent(spinBorder))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(chkOpaqueBox, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStyleName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfStyleName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEncoding)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbEncoding, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSample)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfSample)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFontName)
                    .addComponent(lblFontStyle)
                    .addComponent(spinFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFontSize)
                    .addComponent(lblAlign)
                    .addComponent(lblBorder)
                    .addComponent(spinBorder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rb7)
                            .addComponent(rb8)
                            .addComponent(rb9)
                            .addComponent(spinShadow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblShadow))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rb4)
                            .addComponent(rb5)
                            .addComponent(rb6)
                            .addComponent(chkOpaqueBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rb1)
                            .addComponent(rb2)
                            .addComponent(rb3)
                            .addComponent(spinScaleX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblScaleX))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMarginL)
                            .addComponent(spinL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinScaleY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblScaleY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMarginR)
                            .addComponent(spinR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinAngleZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAngleZ))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMarginV)
                            .addComponent(spinV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSpacing)
                            .addComponent(spinSpacing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEncoding)
                    .addComponent(cbEncoding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStyleName)
                    .addComponent(tfStyleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfSample, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSample))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(embedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel)
                    .addComponent(btnCollections)
                    .addComponent(btnImportStyles))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listFontNamesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listFontNamesValueChanged
        needUpdate = true;
    }//GEN-LAST:event_listFontNamesValueChanged

    private void listFontStylesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listFontStylesValueChanged
        needUpdate = true;
    }//GEN-LAST:event_listFontStylesValueChanged

    private void spinFontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinFontSizeStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinFontSizeStateChanged

    private void spinBorderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinBorderStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinBorderStateChanged

    private void spinShadowStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinShadowStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinShadowStateChanged

    private void chkOpaqueBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOpaqueBoxActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_chkOpaqueBoxActionPerformed

    private void rb7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb7ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb7ActionPerformed

    private void rb4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb4ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb4ActionPerformed

    private void rb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb1ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb1ActionPerformed

    private void rb8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb8ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb8ActionPerformed

    private void rb5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb5ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb5ActionPerformed

    private void rb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb2ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb2ActionPerformed

    private void rb3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb3ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb3ActionPerformed

    private void rb9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb9ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb9ActionPerformed

    private void rb6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb6ActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_rb6ActionPerformed

    private void spinLStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinLStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinLStateChanged

    private void spinRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinRStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinRStateChanged

    private void spinVStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinVStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinVStateChanged

    private void spinScaleXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinScaleXStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinScaleXStateChanged

    private void spinScaleYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinScaleYStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinScaleYStateChanged

    private void spinAngleZStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinAngleZStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinAngleZStateChanged

    private void spinSpacingStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinSpacingStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinSpacingStateChanged

    private void tfSampleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfSampleActionPerformed
        needUpdate = true;
    }//GEN-LAST:event_tfSampleActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // OK
        dialogResult = DialogResult.Ok;
        setVisible(false);
        stopThread();
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Annuler
        dialogResult = DialogResult.Cancel;
        setVisible(false);
        stopThread();
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void lblColorTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColorTextMouseClicked
        // Si on clique dans la zone colorée, invite à changer de couleur
        // Couleur de texte
        colorDialog = new ColorDialog(parent, true);
        colorDialog.showDialog(lblColorText.getBackground(), (int)spinAlphaText.getValue());
        if(colorDialog.getDialogResult() == DialogResult.Ok){
            lblColorText.setBackground(colorDialog.getColor());
            spinAlphaText.setValue(colorDialog.getAlpha());
        }

        needUpdate = true;
    }//GEN-LAST:event_lblColorTextMouseClicked

    private void lblColorKaraokeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColorKaraokeMouseClicked
        // Si on clique dans la zone colorée, invite à changer de couleur
        // Couleur de karaoke
        colorDialog = new ColorDialog(parent, true);
        colorDialog.showDialog(lblColorKaraoke.getBackground(), (int)spinAlphaKaraoke.getValue());
        if(colorDialog.getDialogResult() == DialogResult.Ok){
            lblColorKaraoke.setBackground(colorDialog.getColor());
            spinAlphaKaraoke.setValue(colorDialog.getAlpha());
        }

        needUpdate = true;
    }//GEN-LAST:event_lblColorKaraokeMouseClicked

    private void lblColorOutlineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColorOutlineMouseClicked
        // Si on clique dans la zone colorée, invite à changer de couleur
        // Couleur de bordure
        colorDialog = new ColorDialog(parent, true);
        colorDialog.showDialog(lblColorOutline.getBackground(), (int)spinAlphaOutline.getValue());
        if(colorDialog.getDialogResult() == DialogResult.Ok){
            lblColorOutline.setBackground(colorDialog.getColor());
            spinAlphaOutline.setValue(colorDialog.getAlpha());
        }

        needUpdate = true;
    }//GEN-LAST:event_lblColorOutlineMouseClicked

    private void lblColorShadowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColorShadowMouseClicked
        // Si on clique dans la zone colorée, invite à changer de couleur
        // Couleur d'ombre portée
        colorDialog = new ColorDialog(parent, true);
        colorDialog.showDialog(lblColorShadow.getBackground(), (int)spinAlphaShadow.getValue());
        if(colorDialog.getDialogResult() == DialogResult.Ok){
            lblColorShadow.setBackground(colorDialog.getColor());
            spinAlphaShadow.setValue(colorDialog.getAlpha());
        }

        needUpdate = true;
    }//GEN-LAST:event_lblColorShadowMouseClicked

    private void spinAlphaTextStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinAlphaTextStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinAlphaTextStateChanged

    private void spinAlphaKaraokeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinAlphaKaraokeStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinAlphaKaraokeStateChanged

    private void spinAlphaOutlineStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinAlphaOutlineStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinAlphaOutlineStateChanged

    private void spinAlphaShadowStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinAlphaShadowStateChanged
        needUpdate = true;
    }//GEN-LAST:event_spinAlphaShadowStateChanged

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
            java.util.logging.Logger.getLogger(StyleEditDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StyleEditDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StyleEditDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StyleEditDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                StyleEditDialog dialog = new StyleEditDialog(new javax.swing.JFrame(), true);
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
    
    @Override
    public void run(){
        while(true){
            if(needUpdate){
                updatePreview();
                needUpdate = false;
            }
        }
    }
    
    private volatile boolean needUpdate = false;
    private Thread thPreview = null;
    
    public void startThread(){
        stopThread();
        thPreview = new Thread(this);
        thPreview.start();
    }
    
    public void stopThread(){
        needUpdate = false;
        if(thPreview == null) return;
        if(thPreview.isAlive() || thPreview.isInterrupted() == false){
            thPreview.interrupt();
            thPreview = null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCollections;
    private javax.swing.JButton btnImportStyles;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cbEncoding;
    private javax.swing.JCheckBox chkOpaqueBox;
    private javax.swing.JPanel embedPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAlign;
    private javax.swing.JLabel lblAngleZ;
    private javax.swing.JLabel lblBorder;
    private javax.swing.JLabel lblColor1;
    private javax.swing.JLabel lblColor2;
    private javax.swing.JLabel lblColor3;
    private javax.swing.JLabel lblColor4;
    private javax.swing.JLabel lblColorKaraoke;
    private javax.swing.JLabel lblColorOutline;
    private javax.swing.JLabel lblColorShadow;
    private javax.swing.JLabel lblColorText;
    private javax.swing.JLabel lblEncoding;
    private javax.swing.JLabel lblFontName;
    private javax.swing.JLabel lblFontSize;
    private javax.swing.JLabel lblFontStyle;
    private javax.swing.JLabel lblMarginL;
    private javax.swing.JLabel lblMarginR;
    private javax.swing.JLabel lblMarginV;
    private javax.swing.JLabel lblSample;
    private javax.swing.JLabel lblScaleX;
    private javax.swing.JLabel lblScaleY;
    private javax.swing.JLabel lblShadow;
    private javax.swing.JLabel lblSpacing;
    private javax.swing.JLabel lblStyleName;
    private javax.swing.JList<String> listFontNames;
    private javax.swing.JList<String> listFontStyles;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JRadioButton rb3;
    private javax.swing.JRadioButton rb4;
    private javax.swing.JRadioButton rb5;
    private javax.swing.JRadioButton rb6;
    private javax.swing.JRadioButton rb7;
    private javax.swing.JRadioButton rb8;
    private javax.swing.JRadioButton rb9;
    private javax.swing.JSpinner spinAlphaKaraoke;
    private javax.swing.JSpinner spinAlphaOutline;
    private javax.swing.JSpinner spinAlphaShadow;
    private javax.swing.JSpinner spinAlphaText;
    private javax.swing.JSpinner spinAngleZ;
    private javax.swing.JSpinner spinBorder;
    private javax.swing.JSpinner spinFontSize;
    private javax.swing.JSpinner spinL;
    private javax.swing.JSpinner spinR;
    private javax.swing.JSpinner spinScaleX;
    private javax.swing.JSpinner spinScaleY;
    private javax.swing.JSpinner spinShadow;
    private javax.swing.JSpinner spinSpacing;
    private javax.swing.JSpinner spinV;
    private javax.swing.JTextField tfSample;
    private javax.swing.JTextField tfStyleName;
    // End of variables declaration//GEN-END:variables

    public class PreviewPanel extends javax.swing.JPanel {

        private static final int SQUARE_SIZE = 16;
        private String text = "yggdrasil";
        private AssStyle style = AssStyle.getDefault();

        public PreviewPanel() {
            setBorder(new LineBorder(Color.black, 1, true));
        }
        
        public void updatePreview(String text, AssStyle style){
            this.text = text;
            this.style = style;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.setColor(Color.lightGray);
            boolean isSquare;
            int plus = SQUARE_SIZE;
            
            for(int y=0; y<getHeight(); y+=SQUARE_SIZE){
                isSquare = true;
                for(int x=plus; x<getWidth(); x+=SQUARE_SIZE){
                    if(isSquare == true){
                        g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
                        isSquare = false;
                    }else{
                        isSquare = true;
                    }
                }
                plus = plus == SQUARE_SIZE ? 0 : SQUARE_SIZE;
            }
            
            ASS ass = ASS.NoFileToLoad();
            AssStyle def = style;
            def.setName("Default");
            ass.getStyles().clear();
            ass.getStyles().put("Default", def);
            AssEvent event = new AssEvent();
            event.setTime(new AssTime(0L, 1L));
            event.setStyle(def);
            event.setText(text);
            ass.getEvents().add(event);
//            BufferedImage img = BssRenderer.getAssImage(ass, AssTime.create(0L), getWidth(), getHeight());
//            
//            g.drawImage(img, 0, 0, null);
        }        
    }
    
}
