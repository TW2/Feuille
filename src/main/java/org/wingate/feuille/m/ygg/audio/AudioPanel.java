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
package org.wingate.feuille.m.ygg.audio;

import java.awt.BorderLayout;
import javax.swing.JFileChooser;
import org.wingate.feuille.ass.AssTime;
import org.wingate.feuille.m.ygg.video.VideoPanel;
import org.wingate.feuille.theme.Theme;
import org.wingate.feuille.util.AudioFileFilter;
import org.wingate.feuille.util.VideoFileFilter;

/**
 *
 * @author util2
 */
public class AudioPanel extends javax.swing.JPanel {
    
    private final Theme theme;
    
    private String videoPath = null;
    
    private final AudioForm audioForm;
    
    private long timeToStop = 0L;
    private long fromAudioStart = 0L;
    private long fromAudioEnd = 0L;
    private long previewTime = 500L;
    private long fromAudioStartK = 0L;
    private long fromAudioEndK = 0L;
    private long previewTimeK = 500L;
    
    private VideoPanel videoPanel = null;
    private boolean useVideoPanel = true;

    /**
     * Creates new form AudioPanel
     * @param theme
     */
    public AudioPanel(Theme theme) {
        initComponents();
        
        audioForm = new AudioForm(AudioForm.Display.Both, AudioForm.Style.Both);
        embedPanel.add(audioForm, BorderLayout.CENTER);
        
        for(javax.swing.filechooser.FileFilter ff : fcOpenVideo.getChoosableFileFilters()){
            fcOpenVideo.removeChoosableFileFilter(ff);
        }
        fcOpenVideo.addChoosableFileFilter(new VideoFileFilter());
        fcOpenVideo.addChoosableFileFilter(new AudioFileFilter());
        
        this.theme = theme;
        
        init();
    }
    
    private void init(){
        theme.apply(this);
    }

    public JFileChooser getOpenVideoChooser() {
        return fcOpenVideo;
    }
    
    public void setVideoFile(String s){
        if(s != null && s.isEmpty() == false){
            videoPath = s;
            audioForm.openFile(s, 5000L, embedPanel.getWidth(), embedPanel.getHeight());
        }
    }
    
    public AssTime getStartTime(){
        AssTime t = audioForm.getTime();
        fromAudioStart = t.getMsStart();
        return t;
    }
    
    public AssTime getEndTime(){
        AssTime t = audioForm.getTime();
        fromAudioEnd = t.getMsStop();
        return t;
    }
    
    public AssTime getStartKTime(){
        // TODO faire une méthode différente à la source
        AssTime t = audioForm.getTime();
        fromAudioStartK = t.getMsStart();
        return t;
    }
    
    public AssTime getEndKTime(){
        // TODO faire une méthode différente à la source
        AssTime t = audioForm.getTime();
        fromAudioEndK = t.getMsStop();
        return t;
    }

    /**
     * Play from start to end (from end to start if detected with reordering)
     * @param start Start time in ms
     * @param end End time in ms
     */
    public void playArea(long start, long end){
        // TODO
        if(videoPath == null) return;
        long s = Math.min(start, end);
        long e = Math.max(start, end);
        if(s < e && s >= 0L && e > 0L){
            timeToStop = e;
            videoPanel.playArea(s, e);
        }
    }

    public long getTimeToStop() {
        return timeToStop;
    }

    public void setTimeToStop(long timeToStop) {
        this.timeToStop = timeToStop;
    }

    public long getFromAudioStart() {
        return fromAudioStart;
    }

    public void setFromAudioStart(long fromAudioStart) {
        this.fromAudioStart = fromAudioStart;
    }

    public long getFromAudioEnd() {
        return fromAudioEnd;
    }

    public void setFromAudioEnd(long fromAudioEnd) {
        this.fromAudioEnd = fromAudioEnd;
    }

    public long getPreviewTime() {
        return previewTime;
    }

    public void setPreviewTime(long previewTime) {
        this.previewTime = previewTime;
    }

    public long getFromAudioStartK() {
        return fromAudioStartK;
    }

    public void setFromAudioStartK(long fromAudioStartK) {
        this.fromAudioStartK = fromAudioStartK;
    }

    public long getFromAudioEndK() {
        return fromAudioEndK;
    }

    public void setFromAudioEndK(long fromAudioEndK) {
        this.fromAudioEndK = fromAudioEndK;
    }

    public long getPreviewTimeK() {
        return previewTimeK;
    }

    public void setPreviewTimeK(long previewTimeK) {
        this.previewTimeK = previewTimeK;
    }

    public VideoPanel getVideoPanel() {
        return videoPanel;
    }

    public void setVideoPanel(VideoPanel videoPanel) {
        this.videoPanel = videoPanel;
    }

    public boolean isUseVideoPanel() {
        return useVideoPanel;
    }

    public void setUseVideoPanel(boolean useVideoPanel) {
        this.useVideoPanel = useVideoPanel;
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnPlay = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnPlayBefore = new javax.swing.JButton();
        btnPlayBegin = new javax.swing.JButton();
        btnPlayArea = new javax.swing.JButton();
        btnPlayEnd = new javax.swing.JButton();
        btnPlayAfter = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnKPlayBefore = new javax.swing.JButton();
        btnKPlayBegin = new javax.swing.JButton();
        btnKPlayArea = new javax.swing.JButton();
        btnKPlayEnd = new javax.swing.JButton();
        btnKPlayAfter = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        toggleShowTracks = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jPanel4 = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel5 = new javax.swing.JPanel();
        embedPanel = new javax.swing.JPanel();
        showTracksPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play.png"))); // NOI18N
        btnPlay.setText("Play");
        btnPlay.setFocusable(false);
        btnPlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPlay);

        btnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs pause.png"))); // NOI18N
        btnPause.setText("Pause");
        btnPause.setFocusable(false);
        btnPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPause);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs stop.png"))); // NOI18N
        btnStop.setText("Stop");
        btnStop.setFocusable(false);
        btnStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        jToolBar1.add(btnStop);
        jToolBar1.add(jSeparator1);

        btnPlayBefore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play out 01.png"))); // NOI18N
        btnPlayBefore.setText("Before");
        btnPlayBefore.setFocusable(false);
        btnPlayBefore.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayBefore.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayBefore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayBeforeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPlayBefore);

        btnPlayBegin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play in 01.png"))); // NOI18N
        btnPlayBegin.setText("Begin");
        btnPlayBegin.setFocusable(false);
        btnPlayBegin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayBegin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayBegin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayBeginActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPlayBegin);

        btnPlayArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs in.png"))); // NOI18N
        btnPlayArea.setText("Area");
        btnPlayArea.setFocusable(false);
        btnPlayArea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayArea.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayAreaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPlayArea);

        btnPlayEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play in 02.png"))); // NOI18N
        btnPlayEnd.setText("End");
        btnPlayEnd.setFocusable(false);
        btnPlayEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayEnd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayEndActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPlayEnd);

        btnPlayAfter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play out 02.png"))); // NOI18N
        btnPlayAfter.setText("After");
        btnPlayAfter.setFocusable(false);
        btnPlayAfter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayAfter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPlayAfter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayAfterActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPlayAfter);
        jToolBar1.add(jSeparator2);

        btnKPlayBefore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play out 01.png"))); // NOI18N
        btnKPlayBefore.setText("K Before");
        btnKPlayBefore.setFocusable(false);
        btnKPlayBefore.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKPlayBefore.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKPlayBefore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKPlayBeforeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnKPlayBefore);

        btnKPlayBegin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play out 06.png"))); // NOI18N
        btnKPlayBegin.setText("K Begin");
        btnKPlayBegin.setFocusable(false);
        btnKPlayBegin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKPlayBegin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKPlayBegin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKPlayBeginActionPerformed(evt);
            }
        });
        jToolBar1.add(btnKPlayBegin);

        btnKPlayArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs area yellow.png"))); // NOI18N
        btnKPlayArea.setText("K Area");
        btnKPlayArea.setFocusable(false);
        btnKPlayArea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKPlayArea.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKPlayArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKPlayAreaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnKPlayArea);

        btnKPlayEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play out 05.png"))); // NOI18N
        btnKPlayEnd.setText("K End");
        btnKPlayEnd.setFocusable(false);
        btnKPlayEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKPlayEnd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKPlayEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKPlayEndActionPerformed(evt);
            }
        });
        jToolBar1.add(btnKPlayEnd);

        btnKPlayAfter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_timer_stuffs play out 02.png"))); // NOI18N
        btnKPlayAfter.setText("K After");
        btnKPlayAfter.setFocusable(false);
        btnKPlayAfter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKPlayAfter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKPlayAfter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKPlayAfterActionPerformed(evt);
            }
        });
        jToolBar1.add(btnKPlayAfter);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Audio controls", jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jToolBar2.setRollover(true);

        toggleShowTracks.setText("Tracks");
        toggleShowTracks.setFocusable(false);
        toggleShowTracks.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleShowTracks.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(toggleShowTracks);

        jPanel2.add(jToolBar2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Timing", jPanel2);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jToolBar3.setRollover(true);
        jPanel3.add(jToolBar3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Karaoke", jPanel3);

        add(jTabbedPane1, java.awt.BorderLayout.SOUTH);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollBar1.setMaximum(250);
        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar1.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar1AdjustmentValueChanged(evt);
            }
        });
        jPanel4.add(jScrollBar1, java.awt.BorderLayout.SOUTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        embedPanel.setLayout(new java.awt.BorderLayout());
        jPanel5.add(embedPanel, java.awt.BorderLayout.CENTER);

        showTracksPanel.setLayout(new java.awt.BorderLayout());
        jPanel5.add(showTracksPanel, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        // Play file
        if(videoPath == null) return;
        videoPanel.play();
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
        // Pause
        if(videoPath == null) return;
        videoPanel.pause();
    }//GEN-LAST:event_btnPauseActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        // Stop and reset
        if(videoPath == null) return;
        videoPanel.stop();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnPlayBeforeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayBeforeActionPerformed
        getStartTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioStart - previewTime, fromAudioStart, videoPath);
        }else{
            playArea(fromAudioStart - previewTime, fromAudioStart);
        }
    }//GEN-LAST:event_btnPlayBeforeActionPerformed

    private void btnPlayBeginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayBeginActionPerformed
        getStartTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioStart, fromAudioStart + previewTime, videoPath);
        }else{
            playArea(fromAudioStart, fromAudioStart + previewTime);
        }
    }//GEN-LAST:event_btnPlayBeginActionPerformed

    private void btnPlayAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayAreaActionPerformed
        getStartTime();
        getEndTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioStart, fromAudioEnd, videoPath);
        }else{
            playArea(fromAudioStart, fromAudioEnd);
        }
    }//GEN-LAST:event_btnPlayAreaActionPerformed

    private void btnPlayEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayEndActionPerformed
        getEndTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioEnd - previewTime, fromAudioEnd, videoPath);
        }else{
            playArea(fromAudioEnd - previewTime, fromAudioEnd);
        }
    }//GEN-LAST:event_btnPlayEndActionPerformed

    private void btnPlayAfterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayAfterActionPerformed
        getEndTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioEnd, fromAudioEnd + previewTime, videoPath);
        }else{
            playArea(fromAudioEnd, fromAudioEnd + previewTime);
        }
    }//GEN-LAST:event_btnPlayAfterActionPerformed

    private void btnKPlayBeforeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKPlayBeforeActionPerformed
        getStartKTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioStartK - previewTimeK, fromAudioStartK, videoPath);
        }else{
            playArea(fromAudioStartK - previewTimeK, fromAudioStartK);
        }
    }//GEN-LAST:event_btnKPlayBeforeActionPerformed

    private void btnKPlayBeginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKPlayBeginActionPerformed
        getStartKTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioStartK, fromAudioStartK + previewTimeK, videoPath);
        }else{
            playArea(fromAudioStartK, fromAudioStartK + previewTimeK);
        }
    }//GEN-LAST:event_btnKPlayBeginActionPerformed

    private void btnKPlayAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKPlayAreaActionPerformed
        getStartKTime();
        getEndKTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioStartK, fromAudioEndK, videoPath);
        }else{
            playArea(fromAudioStartK, fromAudioEndK);
        }
    }//GEN-LAST:event_btnKPlayAreaActionPerformed

    private void btnKPlayEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKPlayEndActionPerformed
        getEndKTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioEndK - previewTimeK, fromAudioEndK, videoPath);
        }else{
            playArea(fromAudioEndK - previewTimeK, fromAudioEndK);
        }
    }//GEN-LAST:event_btnKPlayEndActionPerformed

    private void btnKPlayAfterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKPlayAfterActionPerformed
        getEndKTime();
        if(videoPanel != null && useVideoPanel){
            videoPanel.playArea(fromAudioEndK, fromAudioEndK + previewTimeK, videoPath);
        }else{
            playArea(fromAudioEndK, fromAudioEndK + previewTimeK);
        }
    }//GEN-LAST:event_btnKPlayAfterActionPerformed

    private void jScrollBar1AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBar1AdjustmentValueChanged
        // Move waveform, spectrogram and tracks if visible
        // Must call for repaint object embedded
        if(videoPath != null){
            double percent = (double)jScrollBar1.getValue() / (double)jScrollBar1.getMaximum();
            audioForm.displayTime(percent);
        }        
    }//GEN-LAST:event_jScrollBar1AdjustmentValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKPlayAfter;
    private javax.swing.JButton btnKPlayArea;
    private javax.swing.JButton btnKPlayBefore;
    private javax.swing.JButton btnKPlayBegin;
    private javax.swing.JButton btnKPlayEnd;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPlayAfter;
    private javax.swing.JButton btnPlayArea;
    private javax.swing.JButton btnPlayBefore;
    private javax.swing.JButton btnPlayBegin;
    private javax.swing.JButton btnPlayEnd;
    private javax.swing.JButton btnStop;
    private javax.swing.JPanel embedPanel;
    private javax.swing.JFileChooser fcOpenVideo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JPanel showTracksPanel;
    private javax.swing.JToggleButton toggleShowTracks;
    // End of variables declaration//GEN-END:variables
}
