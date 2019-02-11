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

import feuille.io.Event;
import feuille.util.ISO_3166;
import feuille.util.Language;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author util2
 */
public class ASSEditor extends javax.swing.JPanel {
    
    DefaultComboBoxModel dcbmLineTypes = new DefaultComboBoxModel();
    DefaultComboBoxModel dcbmStyles = new DefaultComboBoxModel();
    DefaultComboBoxModel dcbmNames = new DefaultComboBoxModel();

    /**
     * Creates new form ASSEditor
     */
    public ASSEditor() {
        initComponents();
        init();
    }
    
    private void init(){
        cboxType.setModel(dcbmLineTypes);
        cBoxStyle.setModel(dcbmStyles);
        cBoxName.setModel(dcbmNames);
    }
    
    public void initializeASSEditor(Language in, ISO_3166 get){        
        // Check if there is a requested language (forced)
        // and choose between posibilities
        if(in.isForced() == true){
            get = in.getIso();
        }
        
        Event.LineType.Comment.setType(in.getTranslated("AssEditorLTComment", get, "Comment"));
        Event.LineType.Dialogue.setType(in.getTranslated("AssEditorLTDialogue", get, "Dialogue"));
        Event.LineType.Proposal.setType(in.getTranslated("AssEditorLTProposal", get, "Proposal"));
        Event.LineType.Request.setType(in.getTranslated("AssEditorLTRequest", get, "Request"));
        
        for(Event.LineType type : Event.LineType.values()){
            dcbmLineTypes.addElement(type);
        }
        
        lblType.setText(in.getTranslated("AssEditorType", get, "Type :"));
        lblLayer.setText(in.getTranslated("AssEditorLayer", get, "Layer :"));
        lblMarginLeft.setText(in.getTranslated("AssEditorML", get, "Margin Left (ML) :"));
        lblMarginRight.setText(in.getTranslated("AssEditorMR", get, "Margin Right (MR) :"));
        lblMarginVertical.setText(in.getTranslated("AssEditorMV", get, "Margin Vertical (MV) :"));
        lblStart.setText(in.getTranslated("AssEditorStart", get, "Start :"));
        lblEnd.setText(in.getTranslated("AssEditorEnd", get, "End :"));
        lblDuration.setText(in.getTranslated("AssEditorDuration", get, "Duration :"));
        lblStyle.setText(in.getTranslated("AssEditorStyle", get, "Style :"));
        lblName.setText(in.getTranslated("AssEditorName", get, "Name :"));
        lblEffects.setText(in.getTranslated("AssEditorEffects", get, "Effect(s) :"));
        btnApply.setText(in.getTranslated("AssEditorApply", get, "Apply"));
        
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
        btnApply = new javax.swing.JButton();
        lblType = new javax.swing.JLabel();
        lblLayer = new javax.swing.JLabel();
        lblMarginLeft = new javax.swing.JLabel();
        lblMarginRight = new javax.swing.JLabel();
        lblMarginVertical = new javax.swing.JLabel();
        btnPlay = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        btnPlayBeforeStart = new javax.swing.JButton();
        btnPlayAfterStart = new javax.swing.JButton();
        btnPlayBeforeEnd = new javax.swing.JButton();
        btnPlayAfterEnd = new javax.swing.JButton();
        btnPlayArea = new javax.swing.JButton();
        btnKaraoke = new javax.swing.JButton();
        btnSetTime = new javax.swing.JButton();
        lblStart = new javax.swing.JLabel();
        lblEnd = new javax.swing.JLabel();
        lblDuration = new javax.swing.JLabel();
        lblStyle = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblEffects = new javax.swing.JLabel();
        tfStart = new javax.swing.JTextField();
        tfEnd = new javax.swing.JTextField();
        tfDuration = new javax.swing.JTextField();
        tfEffects = new javax.swing.JTextField();
        cboxType = new javax.swing.JComboBox<>();
        cBoxStyle = new javax.swing.JComboBox<>();
        cBoxName = new javax.swing.JComboBox<>();
        spinLayer = new javax.swing.JSpinner();
        spinML = new javax.swing.JSpinner();
        spinMR = new javax.swing.JSpinner();
        spinMV = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        jPanel1.setLayout(null);

        btnApply.setText("Apply");
        jPanel1.add(btnApply);
        btnApply.setBounds(10, 420, 270, 30);

        lblType.setText("Type :");
        jPanel1.add(lblType);
        lblType.setBounds(10, 90, 100, 30);

        lblLayer.setText("Layer :");
        jPanel1.add(lblLayer);
        lblLayer.setBounds(10, 120, 170, 30);

        lblMarginLeft.setText("ML :");
        jPanel1.add(lblMarginLeft);
        lblMarginLeft.setBounds(10, 150, 170, 30);

        lblMarginRight.setText("MR :");
        jPanel1.add(lblMarginRight);
        lblMarginRight.setBounds(10, 180, 170, 30);

        lblMarginVertical.setText("MV :");
        jPanel1.add(lblMarginVertical);
        lblMarginVertical.setBounds(10, 210, 170, 30);

        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/play-32.png"))); // NOI18N
        jPanel1.add(btnPlay);
        btnPlay.setBounds(80, 0, 40, 40);

        btnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/pause-32.png"))); // NOI18N
        jPanel1.add(btnPause);
        btnPause.setBounds(120, 0, 40, 40);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/stop-32.png"))); // NOI18N
        jPanel1.add(btnStop);
        btnStop.setBounds(160, 0, 40, 40);

        btnPlayBeforeStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playbeforebegin-32.png"))); // NOI18N
        jPanel1.add(btnPlayBeforeStart);
        btnPlayBeforeStart.setBounds(0, 0, 40, 40);

        btnPlayAfterStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playafterbegin-32.png"))); // NOI18N
        jPanel1.add(btnPlayAfterStart);
        btnPlayAfterStart.setBounds(0, 40, 40, 40);

        btnPlayBeforeEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playbeforeend-32.png"))); // NOI18N
        jPanel1.add(btnPlayBeforeEnd);
        btnPlayBeforeEnd.setBounds(40, 40, 40, 40);

        btnPlayAfterEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playafterend-32.png"))); // NOI18N
        jPanel1.add(btnPlayAfterEnd);
        btnPlayAfterEnd.setBounds(40, 0, 40, 40);

        btnPlayArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playarea-32.png"))); // NOI18N
        jPanel1.add(btnPlayArea);
        btnPlayArea.setBounds(200, 0, 40, 40);

        btnKaraoke.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/setkara-32.png"))); // NOI18N
        jPanel1.add(btnKaraoke);
        btnKaraoke.setBounds(200, 40, 40, 40);

        btnSetTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/newtime-32.png"))); // NOI18N
        jPanel1.add(btnSetTime);
        btnSetTime.setBounds(240, 20, 40, 40);

        lblStart.setText("Start :");
        jPanel1.add(lblStart);
        lblStart.setBounds(10, 240, 170, 30);

        lblEnd.setText("End :");
        jPanel1.add(lblEnd);
        lblEnd.setBounds(10, 270, 170, 30);

        lblDuration.setText("Duration :");
        jPanel1.add(lblDuration);
        lblDuration.setBounds(10, 300, 170, 30);

        lblStyle.setText("Style :");
        jPanel1.add(lblStyle);
        lblStyle.setBounds(10, 330, 100, 30);

        lblName.setText("Name :");
        jPanel1.add(lblName);
        lblName.setBounds(10, 360, 100, 30);

        lblEffects.setText("Effect(s) :");
        jPanel1.add(lblEffects);
        lblEffects.setBounds(10, 390, 100, 30);

        tfStart.setText("0.00.00.000");
        jPanel1.add(tfStart);
        tfStart.setBounds(190, 240, 90, 30);

        tfEnd.setText("0.00.00.000");
        jPanel1.add(tfEnd);
        tfEnd.setBounds(190, 270, 90, 30);

        tfDuration.setText("0.00.00.000");
        jPanel1.add(tfDuration);
        tfDuration.setBounds(190, 300, 90, 30);
        jPanel1.add(tfEffects);
        tfEffects.setBounds(120, 390, 160, 30);

        cboxType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cboxType);
        cboxType.setBounds(120, 90, 160, 30);

        cBoxStyle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cBoxStyle);
        cBoxStyle.setBounds(120, 330, 160, 30);

        cBoxName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cBoxName);
        cBoxName.setBounds(120, 360, 160, 30);
        jPanel1.add(spinLayer);
        spinLayer.setBounds(190, 120, 90, 30);
        jPanel1.add(spinML);
        spinML.setBounds(190, 150, 90, 30);
        jPanel1.add(spinMR);
        spinMR.setBounds(190, 180, 90, 30);
        jPanel1.add(spinMV);
        spinMV.setBounds(190, 210, 90, 30);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setViewportView(jEditorPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnKaraoke;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPlayAfterEnd;
    private javax.swing.JButton btnPlayAfterStart;
    private javax.swing.JButton btnPlayArea;
    private javax.swing.JButton btnPlayBeforeEnd;
    private javax.swing.JButton btnPlayBeforeStart;
    private javax.swing.JButton btnSetTime;
    private javax.swing.JButton btnStop;
    private javax.swing.JComboBox<String> cBoxName;
    private javax.swing.JComboBox<String> cBoxStyle;
    private javax.swing.JComboBox<String> cboxType;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblEffects;
    private javax.swing.JLabel lblEnd;
    private javax.swing.JLabel lblLayer;
    private javax.swing.JLabel lblMarginLeft;
    private javax.swing.JLabel lblMarginRight;
    private javax.swing.JLabel lblMarginVertical;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStart;
    private javax.swing.JLabel lblStyle;
    private javax.swing.JLabel lblType;
    private javax.swing.JSpinner spinLayer;
    private javax.swing.JSpinner spinML;
    private javax.swing.JSpinner spinMR;
    private javax.swing.JSpinner spinMV;
    private javax.swing.JTextField tfDuration;
    private javax.swing.JTextField tfEffects;
    private javax.swing.JTextField tfEnd;
    private javax.swing.JTextField tfStart;
    // End of variables declaration//GEN-END:variables
}
