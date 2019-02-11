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

/**
 *
 * @author util2
 */
public class Wave extends javax.swing.JPanel {

    /**
     * Creates new form Wave
     */
    public Wave() {
        initComponents();
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
        jPanel2 = new javax.swing.JPanel();
        btnPlay = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        btnPlayBeforeStart = new javax.swing.JButton();
        btnPlayAfterEnd = new javax.swing.JButton();
        btnPlayAfterStart = new javax.swing.JButton();
        btnPlayBeforeEnd = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1177, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setLayout(null);

        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/play-32.png"))); // NOI18N
        btnPlay.setToolTipText("");
        btnPlay.setFocusable(false);
        btnPlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(btnPlay);
        btnPlay.setBounds(0, 0, 40, 40);

        btnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/pause-32.png"))); // NOI18N
        btnPause.setFocusable(false);
        btnPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(btnPause);
        btnPause.setBounds(40, 0, 40, 40);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/stop-32.png"))); // NOI18N
        btnStop.setFocusable(false);
        btnStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(btnStop);
        btnStop.setBounds(80, 0, 40, 40);

        btnPlayBeforeStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playbeforebegin-32.png"))); // NOI18N
        btnPlayBeforeStart.setFocusable(false);
        btnPlayBeforeStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayBeforeStart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(btnPlayBeforeStart);
        btnPlayBeforeStart.setBounds(0, 40, 40, 40);

        btnPlayAfterEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playafterend-32.png"))); // NOI18N
        btnPlayAfterEnd.setFocusable(false);
        btnPlayAfterEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayAfterEnd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(btnPlayAfterEnd);
        btnPlayAfterEnd.setBounds(40, 40, 40, 40);

        btnPlayAfterStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playafterbegin-32.png"))); // NOI18N
        btnPlayAfterStart.setFocusable(false);
        btnPlayAfterStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayAfterStart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(btnPlayAfterStart);
        btnPlayAfterStart.setBounds(0, 80, 40, 40);

        btnPlayBeforeEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/playbeforeend-32.png"))); // NOI18N
        btnPlayBeforeEnd.setFocusable(false);
        btnPlayBeforeEnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPlayBeforeEnd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(btnPlayBeforeEnd);
        btnPlayBeforeEnd.setBounds(40, 80, 40, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPlayAfterEnd;
    private javax.swing.JButton btnPlayAfterStart;
    private javax.swing.JButton btnPlayBeforeEnd;
    private javax.swing.JButton btnPlayBeforeStart;
    private javax.swing.JButton btnStop;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}