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
package org.wingate.feuille.m.ygg.ocr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.LeptonicaFrameConverter;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.leptonica.global.leptonica;
import org.bytedeco.tesseract.TessBaseAPI;
import org.wingate.feuille.ass.ASS;
import org.wingate.feuille.ass.AssEvent;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.VideoFileFilter;

/**
 *
 * @author util2
 */
public class TesseractPanel extends javax.swing.JPanel {
    
    private FFmpegFrameGrabber grabber;
    private Frame frame;
    private final Java2DFrameConverter converter1;
    
    private BufferedImage videoImage;
    private final EmbeddedVideoPanel eVideoPanel;
    
    private String videoPath = null;
    private final ASS ass;
    private AssEvent assEvent;
    
    // Init Tesseract
    private final TessBaseAPI api;
    private BytePointer outText;
    private PIX pixImage1;
    private final LeptonicaFrameConverter converter2;
    private TesseractLanguage language;
    
    /**
     * Creates new form TesseractPanel
     */
    public TesseractPanel() {
        initComponents();
        
        converter1 = new Java2DFrameConverter();
        converter2 = new LeptonicaFrameConverter();        
        api = new TessBaseAPI();
        outText = null;
        pixImage1 = null;
        
        eVideoPanel = new EmbeddedVideoPanel();
        embedPanel.setLayout(new BorderLayout());
        embedPanel.add(eVideoPanel, BorderLayout.CENTER);
        
        videoImage = null;
        ass = ASS.NoFileToLoad();
        assEvent = new AssEvent();
        
        for(javax.swing.filechooser.FileFilter ff : fcOpenVideo.getChoosableFileFilters()){
            fcOpenVideo.removeChoosableFileFilter(ff);
        }
        fcOpenVideo.addChoosableFileFilter(new VideoFileFilter());
        
        slideOCR.setMinimum(0);        
        slideOCR.setValue(0);
        
        btnPreviousFrame.setEnabled(false);
        btnNextFrame.setEnabled(false);
        btnStartTime.setEnabled(false);
        btnEndTime.setEnabled(false);
        btnOCR.setEnabled(false);
        btnOK.setEnabled(false);
    }
    
    public JFileChooser getOpenVideoChooser() {
        return fcOpenVideo;
    }
    
    public void setVideoFile(String s){
        if(s != null && s.isEmpty() == false){
            videoPath = s;
        }
    }
    
    public void initOCR(){
        language = getLanguage();
        api.Init(null, language.getLangCode());
    }
    
    public void initFFmpeg(){
        try {
            grabber = new FFmpegFrameGrabber(videoPath);
            grabber.start();
            slideOCR.setMaximum(grabber.getLengthInVideoFrames());
            initOCR();
        } catch (FFmpegFrameGrabber.Exception ex) {
            Logger.getLogger(TesseractPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dispose(){
        try {
            // FFmpeg
            grabber.stop();
            grabber.release();
            
            // Tesseract + Leptonica
            api.End();
            outText.deallocate();
            leptonica.pixDestroy(pixImage1);
            
        } catch (NullPointerException | FFmpegFrameGrabber.Exception ex) {
            Logger.getLogger(TesseractPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private TesseractLanguage getLanguage(){
        TesseractLanguageDialog tld = new TesseractLanguageDialog(new javax.swing.JFrame(), true);
        tld.showDialog();
        if(tld.getDialogResult() == DialogResult.Ok){
            return tld.getLanguage();
        }else{
            return null;
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

        fcOpenVideo = new javax.swing.JFileChooser();
        embedPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        tbTesseract = new javax.swing.JToolBar();
        btnSet = new javax.swing.JButton();
        btnPreviousFrame = new javax.swing.JButton();
        btnNextFrame = new javax.swing.JButton();
        btnStartTime = new javax.swing.JButton();
        btnEndTime = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnOCR = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnOK = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        tfGetter = new javax.swing.JTextField();
        tfSetter = new javax.swing.JTextField();
        slideOCR = new javax.swing.JSlider();

        setLayout(new java.awt.BorderLayout());

        embedPanel.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout embedPanelLayout = new javax.swing.GroupLayout(embedPanel);
        embedPanel.setLayout(embedPanelLayout);
        embedPanelLayout.setHorizontalGroup(
            embedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 586, Short.MAX_VALUE)
        );
        embedPanelLayout.setVerticalGroup(
            embedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 216, Short.MAX_VALUE)
        );

        add(embedPanel, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        tbTesseract.setRollover(true);

        btnSet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32 engrenage.png"))); // NOI18N
        btnSet.setText("Setup");
        btnSet.setFocusable(false);
        btnSet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetActionPerformed(evt);
            }
        });
        tbTesseract.add(btnSet);

        btnPreviousFrame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fnew4_32.png"))); // NOI18N
        btnPreviousFrame.setText("Previous");
        btnPreviousFrame.setToolTipText("Go the the previous frame/image");
        btnPreviousFrame.setFocusable(false);
        btnPreviousFrame.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPreviousFrame.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPreviousFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousFrameActionPerformed(evt);
            }
        });
        tbTesseract.add(btnPreviousFrame);

        btnNextFrame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fnew6_32.png"))); // NOI18N
        btnNextFrame.setText("Next");
        btnNextFrame.setToolTipText("Go the the next frame/image");
        btnNextFrame.setFocusable(false);
        btnNextFrame.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNextFrame.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNextFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextFrameActionPerformed(evt);
            }
        });
        tbTesseract.add(btnNextFrame);

        btnStartTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play in 01.png"))); // NOI18N
        btnStartTime.setText("Start");
        btnStartTime.setToolTipText("Mark as start time");
        btnStartTime.setFocusable(false);
        btnStartTime.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStartTime.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnStartTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartTimeActionPerformed(evt);
            }
        });
        tbTesseract.add(btnStartTime);

        btnEndTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play in 02.png"))); // NOI18N
        btnEndTime.setText("End");
        btnEndTime.setToolTipText("Mark as end time");
        btnEndTime.setFocusable(false);
        btnEndTime.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEndTime.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEndTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndTimeActionPerformed(evt);
            }
        });
        tbTesseract.add(btnEndTime);
        tbTesseract.add(jSeparator2);

        btnOCR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32 losange carré.png"))); // NOI18N
        btnOCR.setText("OCR");
        btnOCR.setToolTipText("Try to get text on screen");
        btnOCR.setFocusable(false);
        btnOCR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOCR.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOCR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOCRActionPerformed(evt);
            }
        });
        tbTesseract.add(btnOCR);
        tbTesseract.add(jSeparator1);

        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32 accept.png"))); // NOI18N
        btnOK.setText("Ok");
        btnOK.setToolTipText("Push the text and time to ASS");
        btnOK.setFocusable(false);
        btnOK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOK.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        tbTesseract.add(btnOK);

        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        tfGetter.setEditable(false);
        jPanel3.add(tfGetter);
        jPanel3.add(tfSetter);

        tbTesseract.add(jPanel3);

        jPanel1.add(tbTesseract, java.awt.BorderLayout.SOUTH);

        slideOCR.setValue(0);
        slideOCR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slideOCRStateChanged(evt);
            }
        });
        jPanel1.add(slideOCR, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPreviousFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousFrameActionPerformed
        try {
            grabber.setTimestamp(frame.timestamp - 1_000_000L);
            frame = grabber.grab();
            
            if (frame.image != null) {
                try (Frame imageFrame = frame.clone()) {
                    videoImage = converter1.convert(imageFrame);
                    eVideoPanel.repaint();
                }
            }
        } catch (FFmpegFrameGrabber.Exception ex) {
            Logger.getLogger(TesseractPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPreviousFrameActionPerformed

    private void btnNextFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextFrameActionPerformed
        try {
            frame = grabber.grab();
            
            if (frame.image != null) {
                try (Frame imageFrame = frame.clone()) {
                    videoImage = converter1.convert(imageFrame);
                    eVideoPanel.repaint();
                }
            }
        } catch (FFmpegFrameGrabber.Exception ex) {
            Logger.getLogger(TesseractPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnNextFrameActionPerformed

    private void btnStartTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartTimeActionPerformed
        assEvent.getTime().setMsStart(frame.timestamp / 1000L);
    }//GEN-LAST:event_btnStartTimeActionPerformed

    private void btnEndTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndTimeActionPerformed
        assEvent.getTime().setMsStop(frame.timestamp / 1000L);
    }//GEN-LAST:event_btnEndTimeActionPerformed

    @SuppressWarnings("UseSpecificCatch")
    private void btnOCRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOCRActionPerformed
        try{
            // Init
            Frame frame = converter1.convert(videoImage);
            pixImage1 = converter2.convert(frame);
            
            // Usage
            api.SetImage(pixImage1);
            outText = api.GetUTF8Text();
            String s = outText.getString("UTF-8");
            tfGetter.setText(s);
            tfSetter.setText(s);
            
        }catch(NullPointerException | UnsupportedEncodingException ex) {
            Logger.getLogger(TesseractPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnOCRActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        assEvent.setText(tfSetter.getText());
        ass.getEvents().add(assEvent);
        assEvent = new AssEvent();
    }//GEN-LAST:event_btnOKActionPerformed

    private void slideOCRStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slideOCRStateChanged
        try {
            int frameNow = slideOCR.getValue();
            grabber.setVideoFrameNumber(frameNow);
            
            frame = grabber.grab();
            
            if (frame.image != null) {
                try (Frame imageFrame = frame.clone()) {
                    videoImage = converter1.convert(imageFrame);
                    eVideoPanel.repaint();
                }
            }
        } catch (FFmpegFrameGrabber.Exception ex) {
            Logger.getLogger(TesseractPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_slideOCRStateChanged

    private void btnSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetActionPerformed
        initFFmpeg();
        btnPreviousFrame.setEnabled(true);
        btnNextFrame.setEnabled(true);
        btnStartTime.setEnabled(true);
        btnEndTime.setEnabled(true);
        btnOCR.setEnabled(true);
        btnOK.setEnabled(true);
        
        btnSet.setEnabled(false);
    }//GEN-LAST:event_btnSetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEndTime;
    private javax.swing.JButton btnNextFrame;
    private javax.swing.JButton btnOCR;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnPreviousFrame;
    private javax.swing.JButton btnSet;
    private javax.swing.JButton btnStartTime;
    private javax.swing.JPanel embedPanel;
    private javax.swing.JFileChooser fcOpenVideo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSlider slideOCR;
    private javax.swing.JToolBar tbTesseract;
    private javax.swing.JTextField tfGetter;
    private javax.swing.JTextField tfSetter;
    // End of variables declaration//GEN-END:variables

    class EmbeddedVideoPanel extends javax.swing.JPanel {

        public EmbeddedVideoPanel() {
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D)g;
            
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            if(videoImage != null){// && playerState == PlayerState.Play
                int x, y, w, h;
                float ratioX = (float)getWidth() / (float)videoImage.getWidth();
                float ratioY = (float)getHeight() / (float)videoImage.getHeight();
                float ratio = Math.min(ratioX, ratioY);
                w = Math.round(videoImage.getWidth() * ratio);
                h = Math.round(videoImage.getHeight() * ratio);
                x = (getWidth() - w) / 2;
                y = (getHeight() - h) / 2;
                g2d.drawImage(videoImage, x, y, w, h, null);
            }
        }
    }
}
