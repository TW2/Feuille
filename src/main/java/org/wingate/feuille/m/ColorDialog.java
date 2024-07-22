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
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class ColorDialog extends javax.swing.JDialog {
    
    private final PreviewPanel previewPanel;
    
    private DialogResult dialogResult = DialogResult.Unknown;
    private final java.awt.Frame parent;
    
    private final DefaultComboBoxModel modelKnownColor;
    
    private Component selectedComponent = null;

    /**
     * Creates new form ColorDialog2
     * @param parent
     * @param modal
     */
    public ColorDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent = parent;
        
        previewPanel = new PreviewPanel();
        embedPanel.add(previewPanel, BorderLayout.CENTER);
        
        modelKnownColor = new DefaultComboBoxModel();
        cbKnownColors.setModel(modelKnownColor);
        cbKnownColors.setRenderer(new KnownColorRenderer());
        
        for(DrawColor d : DrawColor.values()){
            modelKnownColor.addElement(d);
        }
    }
    
    public void showDialog(Color color, int alpha){
        sliderAlpha.setValue(alpha);
        tfBGR.setText(toBGR(color));
        selectedComponent = tfBGR;
        updateBGRColor();
        selectedComponent = null;
        
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }
    
    public Color getColor(){
        return previewPanel.getColor();
    }
    
    public int getAlpha(){
        return previewPanel.getAlpha();
    }
    
    private Color fromBGR(String s){
        Color c = Color.black;
        
        if(s.length() == 6 && s.matches("[A-Fa-f0-9]+")){
            int b = Integer.parseInt(s.substring(0, 2), 16);
            int g = Integer.parseInt(s.substring(2, 4), 16);
            int r = Integer.parseInt(s.substring(4), 16);
            
            c = new Color(r, g, b);
        }       
        
        return c;
    }
    
    private Color fromHTML(String s){
        Color c = Color.black;
        
        if(s.length() == 6 && s.matches("[A-Fa-f0-9]+")){
            int r = Integer.parseInt(s.substring(0, 2), 16);
            int g = Integer.parseInt(s.substring(2, 4), 16);
            int b = Integer.parseInt(s.substring(4), 16);
            
            c = new Color(r, g, b);
        }
        
        return c;
    }
    
    private String toBGR(Color c){
        String r = Integer.toHexString(c.getRed()); if(r.length() == 1) r = "0" + r;
        String g = Integer.toHexString(c.getGreen()); if(g.length() == 1) g = "0" + g;
        String b = Integer.toHexString(c.getBlue()); if(b.length() == 1) b = "0" + b;
        
        return String.format("%s%s%s", b, g, r).toUpperCase();
    }
    
    private String toHTML(Color c){
        String r = Integer.toHexString(c.getRed()); if(r.length() == 1) r = "0" + r;
        String g = Integer.toHexString(c.getGreen()); if(g.length() == 1) g = "0" + g;
        String b = Integer.toHexString(c.getBlue()); if(b.length() == 1) b = "0" + b;
        
        return String.format("%s%s%s", r, g, b).toUpperCase();
    }
    
    private void updateKnownColor(){
        int r = ((DrawColor)cbKnownColors.getSelectedItem()).getRed();
        int g = ((DrawColor)cbKnownColors.getSelectedItem()).getGreen();
        int b = ((DrawColor)cbKnownColors.getSelectedItem()).getBlue();
        
        Color c = new Color(r, g, b);
        
        previewPanel.setColor(c);
        previewPanel.setAlpha(sliderAlpha.getValue());
        
        tfBGR.setText(toBGR(c));
        tfHTML.setText(toHTML(c));
        
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        
        // Sliders
        sliderRed.setValue(c.getRed());
        sliderGreen.setValue(c.getGreen());
        sliderBlue.setValue(c.getBlue());
        sliderAlpha.setValue((int)spinnerAlpha.getValue());
        sliderHue.setValue(Math.round(hsb[0] * 360f));
        sliderSaturation.setValue(Math.round(hsb[1] * 100f));
        sliderBrightness.setValue(Math.round(hsb[2] * 100f));
        
        // Spinners
        spinnerRed.setValue(c.getRed());
        spinnerGreen.setValue(c.getGreen());
        spinnerBlue.setValue(c.getBlue());
        spinnerAlpha.setValue(sliderAlpha.getValue());        
        spinnerHue.setValue(Math.round(hsb[0] * 360f));
        spinnerSaturation.setValue(Math.round(hsb[1] * 100f));
        spinnerBrightness.setValue(Math.round(hsb[2] * 100f));
    }
    
    private void updateBGRColor(){
        Color c = fromBGR(tfBGR.getText());
        
        previewPanel.setColor(c);
        previewPanel.setAlpha(sliderAlpha.getValue());
        
        tfHTML.setText(toHTML(c));
        
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        
        // Sliders
        sliderRed.setValue(c.getRed());
        sliderGreen.setValue(c.getGreen());
        sliderBlue.setValue(c.getBlue());
        sliderAlpha.setValue((int)spinnerAlpha.getValue());
        sliderHue.setValue(Math.round(hsb[0] * 360f));
        sliderSaturation.setValue(Math.round(hsb[1] * 100f));
        sliderBrightness.setValue(Math.round(hsb[2] * 100f));
        
        // Spinners
        spinnerRed.setValue(c.getRed());
        spinnerGreen.setValue(c.getGreen());
        spinnerBlue.setValue(c.getBlue());
        spinnerAlpha.setValue(sliderAlpha.getValue());        
        spinnerHue.setValue(Math.round(hsb[0] * 360f));
        spinnerSaturation.setValue(Math.round(hsb[1] * 100f));
        spinnerBrightness.setValue(Math.round(hsb[2] * 100f));
    }
    
    private void updateHTMLColor(){
        Color c = fromHTML(tfHTML.getText());
        
        previewPanel.setColor(c);
        previewPanel.setAlpha(sliderAlpha.getValue());
        
        tfBGR.setText(toBGR(c));
        
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        
        // Sliders
        sliderRed.setValue(c.getRed());
        sliderGreen.setValue(c.getGreen());
        sliderBlue.setValue(c.getBlue());
        sliderAlpha.setValue((int)spinnerAlpha.getValue());
        sliderHue.setValue(Math.round(hsb[0] * 360f));
        sliderSaturation.setValue(Math.round(hsb[1] * 100f));
        sliderBrightness.setValue(Math.round(hsb[2] * 100f));
        
        // Spinners
        spinnerRed.setValue(c.getRed());
        spinnerGreen.setValue(c.getGreen());
        spinnerBlue.setValue(c.getBlue());
        spinnerAlpha.setValue(sliderAlpha.getValue());        
        spinnerHue.setValue(Math.round(hsb[0] * 360f));
        spinnerSaturation.setValue(Math.round(hsb[1] * 100f));
        spinnerBrightness.setValue(Math.round(hsb[2] * 100f));
    }
    
    private void updateSliderColor(javax.swing.JSlider source){
        Color c = previewPanel.getColor();
        int alpha = previewPanel.getAlpha();
        
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        
        if(source == sliderAlpha){
            alpha = sliderAlpha.getValue();
        }else if(source == sliderRed){
            c = new Color(sliderRed.getValue(), c.getGreen(), c.getBlue());
            hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        }else if(source == sliderGreen){
            c = new Color(c.getRed(), sliderGreen.getValue(), c.getBlue());
            hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        }else if(source == sliderBlue){
            c = new Color(c.getRed(), c.getGreen(), sliderBlue.getValue());
            hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        }else if(source == sliderHue){
            hsb[0] = sliderHue.getValue() / 360f;
            c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }else if(source == sliderSaturation){
            hsb[1] = sliderSaturation.getValue() / 100f;
            c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }else if(source == sliderBrightness){
            hsb[2] = sliderBrightness.getValue() / 100f;
            c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }
        
        previewPanel.setColor(c);
        previewPanel.setAlpha(sliderAlpha.getValue());
        
        tfBGR.setText(toBGR(c));
        tfHTML.setText(toHTML(c));
        
        // Sliders
        sliderRed.setValue(c.getRed());
        sliderGreen.setValue(c.getGreen());
        sliderBlue.setValue(c.getBlue());
        sliderAlpha.setValue(alpha);
        sliderHue.setValue(Math.round(hsb[0] * 360f));
        sliderSaturation.setValue(Math.round(hsb[1] * 100f));
        sliderBrightness.setValue(Math.round(hsb[2] * 100f));
        
        // Spinners
        spinnerRed.setValue(c.getRed());
        spinnerGreen.setValue(c.getGreen());
        spinnerBlue.setValue(c.getBlue());
        spinnerAlpha.setValue(alpha);        
        spinnerHue.setValue(Math.round(hsb[0] * 360f));
        spinnerSaturation.setValue(Math.round(hsb[1] * 100f));
        spinnerBrightness.setValue(Math.round(hsb[2] * 100f));
    }
    
    private void updateSpinnerColor(javax.swing.JSpinner source){
        Color c = previewPanel.getColor();
        int alpha = previewPanel.getAlpha();
        
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        
        if(source == spinnerAlpha){
            alpha = (int)spinnerAlpha.getValue();
        }else if(source == spinnerRed){
            c = new Color((int)spinnerRed.getValue(), c.getGreen(), c.getBlue());
            hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        }else if(source == spinnerGreen){
            c = new Color(c.getRed(), (int)spinnerGreen.getValue(), c.getBlue());
            hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        }else if(source == spinnerBlue){
            c = new Color(c.getRed(), c.getGreen(), (int)spinnerBlue.getValue());
            hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        }else if(source == spinnerHue){
            hsb[0] = (int)spinnerHue.getValue() / 360f;
            c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }else if(source == spinnerSaturation){
            hsb[1] = (int)spinnerSaturation.getValue() / 100f;
            c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }else if(source == spinnerBrightness){
            hsb[2] = (int)spinnerBrightness.getValue() / 100f;
            c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }
        
        previewPanel.setColor(c);
        previewPanel.setAlpha(sliderAlpha.getValue());
        
        tfBGR.setText(toBGR(c));
        tfHTML.setText(toHTML(c));
        
        // Sliders
        sliderRed.setValue(c.getRed());
        sliderGreen.setValue(c.getGreen());
        sliderBlue.setValue(c.getBlue());
        sliderAlpha.setValue(alpha);
        sliderHue.setValue(Math.round(hsb[0] * 360f));
        sliderSaturation.setValue(Math.round(hsb[1] * 100f));
        sliderBrightness.setValue(Math.round(hsb[2] * 100f));
        
        // Spinners
        spinnerRed.setValue(c.getRed());
        spinnerGreen.setValue(c.getGreen());
        spinnerBlue.setValue(c.getBlue());
        spinnerAlpha.setValue(alpha);        
        spinnerHue.setValue(Math.round(hsb[0] * 360f));
        spinnerSaturation.setValue(Math.round(hsb[1] * 100f));
        spinnerBrightness.setValue(Math.round(hsb[2] * 100f));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cbKnownColors = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfBGR = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfHTML = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        sliderAlpha = new javax.swing.JSlider();
        spinnerAlpha = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        sliderRed = new javax.swing.JSlider();
        spinnerRed = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        sliderGreen = new javax.swing.JSlider();
        spinnerGreen = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        sliderBlue = new javax.swing.JSlider();
        spinnerBlue = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        sliderHue = new javax.swing.JSlider();
        spinnerHue = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        sliderSaturation = new javax.swing.JSlider();
        spinnerSaturation = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        sliderBrightness = new javax.swing.JSlider();
        spinnerBrightness = new javax.swing.JSpinner();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        embedPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Known colors"));

        cbKnownColors.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbKnownColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKnownColorsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbKnownColors, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbKnownColors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));

        jPanel4.setLayout(new java.awt.GridLayout(1, 4));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("BGR color : ");
        jPanel4.add(jLabel1);

        tfBGR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfBGR.setText("000000");
        tfBGR.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfBGRCaretUpdate(evt);
            }
        });
        tfBGR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfBGRActionPerformed(evt);
            }
        });
        jPanel4.add(tfBGR);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("HTML color : ");
        jPanel4.add(jLabel2);

        tfHTML.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfHTML.setText("000000");
        tfHTML.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfHTMLCaretUpdate(evt);
            }
        });
        tfHTML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfHTMLActionPerformed(evt);
            }
        });
        jPanel4.add(tfHTML);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Adjustment"));

        jPanel5.setLayout(new java.awt.GridLayout(7, 3));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Alpha (transparency) : ");
        jPanel5.add(jLabel3);

        sliderAlpha.setMaximum(255);
        sliderAlpha.setValue(0);
        sliderAlpha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderAlphaStateChanged(evt);
            }
        });
        jPanel5.add(sliderAlpha);

        spinnerAlpha.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spinnerAlpha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerAlphaStateChanged(evt);
            }
        });
        jPanel5.add(spinnerAlpha);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Red : ");
        jPanel5.add(jLabel4);

        sliderRed.setMaximum(255);
        sliderRed.setValue(0);
        sliderRed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderRedStateChanged(evt);
            }
        });
        jPanel5.add(sliderRed);

        spinnerRed.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spinnerRed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerRedStateChanged(evt);
            }
        });
        jPanel5.add(spinnerRed);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Green : ");
        jPanel5.add(jLabel5);

        sliderGreen.setMaximum(255);
        sliderGreen.setValue(0);
        sliderGreen.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderGreenStateChanged(evt);
            }
        });
        jPanel5.add(sliderGreen);

        spinnerGreen.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spinnerGreen.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerGreenStateChanged(evt);
            }
        });
        jPanel5.add(spinnerGreen);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Blue : ");
        jPanel5.add(jLabel6);

        sliderBlue.setMaximum(255);
        sliderBlue.setValue(0);
        sliderBlue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderBlueStateChanged(evt);
            }
        });
        jPanel5.add(sliderBlue);

        spinnerBlue.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spinnerBlue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerBlueStateChanged(evt);
            }
        });
        jPanel5.add(spinnerBlue);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Hue : ");
        jPanel5.add(jLabel7);

        sliderHue.setMaximum(360);
        sliderHue.setValue(0);
        sliderHue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderHueStateChanged(evt);
            }
        });
        jPanel5.add(sliderHue);

        spinnerHue.setModel(new javax.swing.SpinnerNumberModel(0, 0, 360, 1));
        spinnerHue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerHueStateChanged(evt);
            }
        });
        jPanel5.add(spinnerHue);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("Saturation : ");
        jPanel5.add(jLabel8);

        sliderSaturation.setValue(0);
        sliderSaturation.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderSaturationStateChanged(evt);
            }
        });
        jPanel5.add(sliderSaturation);

        spinnerSaturation.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
        spinnerSaturation.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerSaturationStateChanged(evt);
            }
        });
        jPanel5.add(spinnerSaturation);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Brightness : ");
        jPanel5.add(jLabel9);

        sliderBrightness.setValue(0);
        sliderBrightness.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderBrightnessStateChanged(evt);
            }
        });
        jPanel5.add(sliderBrightness);

        spinnerBrightness.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
        spinnerBrightness.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerBrightnessStateChanged(evt);
            }
        });
        jPanel5.add(spinnerBrightness);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));

        embedPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        embedPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(embedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(embedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOK)
                .addContainerGap())
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Cancel and close
        dialogResult = DialogResult.Cancel;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // Ok and close
        dialogResult = DialogResult.Ok;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void cbKnownColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKnownColorsActionPerformed
        // When we select a color in the list
        if(selectedComponent == null){
            selectedComponent = cbKnownColors;
            updateKnownColor();
            selectedComponent = null;
        }
    }//GEN-LAST:event_cbKnownColorsActionPerformed

    private void tfBGRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfBGRActionPerformed
        // BGR color changes
        if(selectedComponent == null){
            selectedComponent = tfBGR;
            updateBGRColor();
            selectedComponent = null;
        }        
    }//GEN-LAST:event_tfBGRActionPerformed

    private void tfHTMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfHTMLActionPerformed
        // HTML color changes
        if(selectedComponent == null){
            selectedComponent = tfHTML;
            updateHTMLColor();
            selectedComponent = null;
        }
    }//GEN-LAST:event_tfHTMLActionPerformed

    private void sliderAlphaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderAlphaStateChanged
        // Alpha changes (slider)
        if(selectedComponent == null){
            selectedComponent = sliderAlpha;
            updateSliderColor(sliderAlpha);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_sliderAlphaStateChanged

    private void spinnerAlphaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerAlphaStateChanged
        // Alpha changes (spinner)
        if(selectedComponent == null){
            selectedComponent = spinnerAlpha;
            updateSpinnerColor(spinnerAlpha);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_spinnerAlphaStateChanged

    private void sliderRedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderRedStateChanged
        // Red changes (slider)
        if(selectedComponent == null){
            selectedComponent = sliderRed;
            updateSliderColor(sliderRed);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_sliderRedStateChanged

    private void spinnerRedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerRedStateChanged
        // Red changes (spinner)
        if(selectedComponent == null){
            selectedComponent = spinnerRed;
            updateSpinnerColor(spinnerRed);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_spinnerRedStateChanged

    private void sliderGreenStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderGreenStateChanged
        // Green changes (slider)
        if(selectedComponent == null){
            selectedComponent = sliderGreen;
            updateSliderColor(sliderGreen);
            selectedComponent = null;
        }
    }//GEN-LAST:event_sliderGreenStateChanged

    private void spinnerGreenStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerGreenStateChanged
        // Green changes (spinner)
        if(selectedComponent == null){
            selectedComponent = spinnerGreen;
            updateSpinnerColor(spinnerGreen);
            selectedComponent = null;
        }
    }//GEN-LAST:event_spinnerGreenStateChanged

    private void sliderBlueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderBlueStateChanged
        // Blue changes (slider)
        if(selectedComponent == null){
            selectedComponent = sliderBlue;
            updateSliderColor(sliderBlue);
            selectedComponent = null;
        }
    }//GEN-LAST:event_sliderBlueStateChanged

    private void spinnerBlueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerBlueStateChanged
        // Blue changes (spinner)
        if(selectedComponent == null){
            selectedComponent = spinnerBlue;
            updateSpinnerColor(spinnerBlue);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_spinnerBlueStateChanged

    private void sliderHueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderHueStateChanged
        // Hue changes (slider)
        if(selectedComponent == null){
            selectedComponent = sliderHue;
            updateSliderColor(sliderHue);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_sliderHueStateChanged

    private void spinnerHueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerHueStateChanged
        // Hue changes (spinner)
        if(selectedComponent == null){
            selectedComponent = spinnerHue;
            updateSpinnerColor(spinnerHue);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_spinnerHueStateChanged

    private void sliderSaturationStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderSaturationStateChanged
        // Saturation changes (slider)
        if(selectedComponent == null){
            selectedComponent = sliderSaturation;
            updateSliderColor(sliderSaturation);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_sliderSaturationStateChanged

    private void spinnerSaturationStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSaturationStateChanged
        // Saturation changes (spinner)
        if(selectedComponent == null){
            selectedComponent = spinnerSaturation;
            updateSpinnerColor(spinnerSaturation);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_spinnerSaturationStateChanged

    private void sliderBrightnessStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderBrightnessStateChanged
        // Brightness changes (slider)
        if(selectedComponent == null){
            selectedComponent = sliderBrightness;
            updateSliderColor(sliderBrightness);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_sliderBrightnessStateChanged

    private void spinnerBrightnessStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerBrightnessStateChanged
        // Brightness changes (spinner)
        if(selectedComponent == null){
            selectedComponent = spinnerBrightness;
            updateSpinnerColor(spinnerBrightness);
            selectedComponent = null;
        }        
    }//GEN-LAST:event_spinnerBrightnessStateChanged

    private void tfBGRCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_tfBGRCaretUpdate
        // BGR color changes
        if(selectedComponent == null){
            selectedComponent = tfBGR;
            updateBGRColor();
            selectedComponent = null;
        }
    }//GEN-LAST:event_tfBGRCaretUpdate

    private void tfHTMLCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_tfHTMLCaretUpdate
        // HTML color changes
        if(selectedComponent == null){
            selectedComponent = tfHTML;
            updateHTMLColor();
            selectedComponent = null;
        }
    }//GEN-LAST:event_tfHTMLCaretUpdate

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
            java.util.logging.Logger.getLogger(ColorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            ColorDialog dialog = new ColorDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cbKnownColors;
    private javax.swing.JPanel embedPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSlider sliderAlpha;
    private javax.swing.JSlider sliderBlue;
    private javax.swing.JSlider sliderBrightness;
    private javax.swing.JSlider sliderGreen;
    private javax.swing.JSlider sliderHue;
    private javax.swing.JSlider sliderRed;
    private javax.swing.JSlider sliderSaturation;
    private javax.swing.JSpinner spinnerAlpha;
    private javax.swing.JSpinner spinnerBlue;
    private javax.swing.JSpinner spinnerBrightness;
    private javax.swing.JSpinner spinnerGreen;
    private javax.swing.JSpinner spinnerHue;
    private javax.swing.JSpinner spinnerRed;
    private javax.swing.JSpinner spinnerSaturation;
    private javax.swing.JTextField tfBGR;
    private javax.swing.JTextField tfHTML;
    // End of variables declaration//GEN-END:variables

    public class PreviewPanel extends javax.swing.JPanel {

        private Color color;
        private int alpha;
        private static final int SQUARE_SIZE = 16;

        public PreviewPanel() {
            color = Color.white;
            alpha = 255;
            setBorder(new LineBorder(Color.black, 1, true));
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
            repaint();
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
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
            
            g.setColor(color);
            g.fillRect(0, 0, getWidth()/2, getHeight());
            
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - alpha));
            g.fillRect(getWidth()/2, 0, getWidth()/2, getHeight());
        }
        
    }
    
}
