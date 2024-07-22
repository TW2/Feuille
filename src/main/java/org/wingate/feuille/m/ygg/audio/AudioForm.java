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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.wingate.feuille.ass.AssTime;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class AudioForm extends JPanel {
    
    public enum Display {
        None, Waveform, Spectrogram, Both;
    }
    
    public enum Style {
        Both, Normal, Karaoke; 
    }
    
    private Display display = Display.Waveform;
    private Style style = Style.Both;
    
    private double offset = 0d;
    private long mediaMsDuration = -1L;
    private long partDuration = -1L;
    private int partWidth = 0;
    private SignalData data = null;
    private String path = null;
    private AssTime time = new AssTime(0L, 0L);
    
    private int startX = -1, endX = -1;
    private int mouseMoveX = -1;
    
    private Waveform waveformGenerator = null;
    private Spectrum spectrumGenerator = null;
    private Map<Long, Integer> keyframes = null;

    public AudioForm(Display display, Style style) {
        if(display != null) this.display = display;
        if(style != null) this.style = style;
        keyframes = new HashMap<>();
        
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                
                switch(e.getButton()){
                    case MouseEvent.BUTTON1 -> {
                        // Button 1 is the left one
                        startX = e.getX();
                        repaint();
                    }
                    case MouseEvent.BUTTON3 -> {
                        // Button 3 is the right one
                        endX = e.getX();
                        repaint();
                    }
                }
            } 
        });
    }
    
    public void openFile(String path, long partDuration, int partWidth, int partHeight){
        this.path = path;
        this.partDuration = partDuration;
        this.partWidth = partWidth;
        
        data = new SignalData(new File(path), partWidth, partHeight);
        mediaMsDuration = data.getMsDuration();
        
        waveformGenerator = new Waveform(partDuration, mediaMsDuration);
        spectrumGenerator = new Spectrum(partDuration, mediaMsDuration);
        waveformGenerator.startThread();
        spectrumGenerator.startThread();
        
        try{
            keyframes = data.getKeyFrames();
        }catch(Exception exc){
            // No video stream
        }
        
        repaint();
    }
    
    public void displayTime(double percent){
        offset = percent;
        repaint();
    }
    
    public void changeScreen(){
        Display d = Display.None;
        switch(display){
            case Waveform -> { d = Display.Spectrogram; }
            case Spectrogram -> { d = Display.Both; }
            case Both -> { d = Display.Waveform; }
        }
        display = d;
        repaint();
    }
    
    public void changeStyle(Style style){
        this.style = style;
    }
    
    /**
     * Set start time and end time and call to repaint
     * @param xs X position for start time or -1
     * @param xe X position for end time or -1
     */
    public void setStartEnd(int xs, int xe){
        try{
            if(xs != -1) startX = xs;
            if(xe != -1) endX = xe;
            repaint();
        }catch(Exception exc){
            // Not ready!
        }
    }
    
    /**
     * Set start time and end time and call to repaint
     * @param start msStart
     * @param end msEnd
     */
    public void setStartEnd(long start, long end){
        if(start < 0 || end < 0) return;
        time.setMsStart(start);
        time.setMsStop(end);
        repaint();
    }
    
    public void setMousePosition(int x){
        mouseMoveX = x;
        repaint();
    }
    
    public long getMsDuration(){
        return data.getMsDuration();
    }

    public void setTime(AssTime time) {
        this.time = time;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }
    
    public AssTime getTime(){
        // START
        // 100% <> mediaMsDuration
        // offset <> curMs
        // when offset is 0 to 100% as 0d to 1d
        double curMs = offset * mediaMsDuration;
        int x = -(int)Math.round(curMs / partDuration * partWidth);
        
        // nb times partWidth in x
        int n = Math.abs(x) / partWidth;
        
        // images real offset
        int x1 = x + partWidth * n;
        
        double stickyStart = startX + x1;
        double stickyEnd = endX + x1;
        
        long start = Math.round(stickyStart / partWidth * partDuration) + partDuration * n;
        long stop = Math.round(stickyEnd / partWidth * partDuration) + partDuration * n;
        
        return new AssTime(start, stop);
    }

    //==========================================================================
    //88888888888888888888888888888888888888888888888888888888888888888888888888
    //==========================================================================
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if(path != null && partDuration != -1L && mediaMsDuration != -1L && partWidth != 0){
            // 100% <> mediaMsDuration
            // offset <> curMs
            // when offset is 0 to 100% as 0d to 1d
            double curMs = offset * mediaMsDuration;
            int x = -(int)Math.round(curMs / partDuration * partWidth);
            
            // nb times partWidth in x
            int n = Math.abs(x) / partWidth;
            
            // images real offset
            int x1 = x + partWidth * n;
            int x2 = x + partWidth * (n + 1);
            
            long ms = Math.round(curMs);
            ImagesBasket bWave = waveformGenerator.getAt(ms);
            ImagesBasket bSpec = spectrumGenerator.getAt(ms);
            
            if(bWave != null && bSpec != null){
                Graphics2D g2d = (Graphics2D)g;
                
                // On dessine les données / Draw data
                switch(display){
                    case Waveform -> {
                        g2d.drawImage(bWave.getImgOne(), x1, 0, partWidth, getHeight(), null);
                        g2d.drawImage(bWave.getImgTwo(), x2, 0, partWidth, getHeight(), null);
                        // Dessin de ligne continue / Draw the base line
                        g2d.setColor(Color.blue);
                        g2d.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
                    }
                    case Spectrogram -> {
                        g2d.drawImage(bSpec.getImgOne(), x1, 0, partWidth, getHeight(), null);
                        g2d.drawImage(bSpec.getImgTwo(), x2, 0, partWidth, getHeight(), null);
                    }
                    case Both -> {
                        g2d.drawImage(bWave.getImgOne(), x1, 0, partWidth, getHeight()/2, null);
                        g2d.drawImage(bWave.getImgTwo(), x2, 0, partWidth, getHeight()/2, null);
                        g2d.drawImage(bSpec.getImgOne(), x1, getHeight()/2, partWidth, getHeight()/2, null);
                        g2d.drawImage(bSpec.getImgTwo(), x2, getHeight()/2, partWidth, getHeight()/2, null);
                        // Dessin de ligne continue pour la forme d'onde / Draw the base line for the waveform
                        g2d.setColor(Color.blue);
                        g2d.drawLine(0, getHeight()/4, getWidth(), getHeight()/4);
                    }
                }
                
                // On dessine les secondes et les groupes / Draw seconds and groups
                g2d.setColor(Color.pink);
                double seconds = partDuration / 1000d * n;
                double period = partDuration / 1000d;
                if(period > 0){
                    for(double a=x1, ss = seconds; a<x1+partWidth; a+=(partWidth / period), ss++){
                        g2d.draw(new Line2D.Double(a, 0, a, getHeight()));
                        g2d.draw(new Line2D.Double(a + partWidth, 0, a + partWidth, getHeight()));
                        drawSeconds(g2d, a - 2, 2, ss);
                        drawSeconds(g2d, a - 2 + partWidth, 2, ss + period);
                    }
                }
                
                
                // Lignes d'image clé
                drawKeyFrames(g2d, x1, getHeight(), n);

                // Lignes de la sélection + aire / Selection + area
                drawStartEndArea(g2d, startX, endX, x1, getHeight(), n);

                // Lignes de karaoké
                drawKaraoke(g2d);

                // Ligne du pointeur
                drawTime(g2d, new double[]{ mouseMoveX }, new Color[]{ Color.orange }, x1, getHeight(), n);
                
            }
            
        }
    }
    
    private void drawSeconds(Graphics2D g, double x, double y, double seconds){
        Font oldFont = g.getFont();
        
        String s = AssTime.getTimeString((long)seconds * 1000L);
        int measure = g.getFontMetrics().stringWidth(s);
        
        g.translate(x, y + measure);
        g.rotate(Math.toRadians(-90));
        g.drawString(s, 0, 0);
        g.rotate(Math.toRadians(90));
        g.translate(-x, -(y + measure));
        
        g.setFont(oldFont);
    }
    
    private void drawTime(Graphics2D g, double[] tx, Color[] cx, double h){
        Font oldFont = g.getFont();
        
        for(int dx=0; dx<tx.length; dx++){
            String s = AssTime.getTimeString(Math.round(tx[dx] / partWidth * partDuration));
            double y = 2;
            
            int measureText = g.getFontMetrics().stringWidth(s);

            g.setColor(cx[dx]);
            g.draw(new Line2D.Double(tx[dx], 0, tx[dx], h));
            g.translate(tx[dx] - 2, y + measureText);
            g.rotate(Math.toRadians(-90));
            g.drawString(s, 0, 0);
            g.rotate(Math.toRadians(90));
            g.translate(-(tx[dx] - 2), -(y + measureText));
        }
        
        
        g.setFont(oldFont);
    }
    
    private void drawTime(Graphics2D g, double[] tx, Color[] cx, double offsetX, double h, int n){
        Font oldFont = g.getFont();
        
        for(int dx=0; dx<tx.length; dx++){
            String s = AssTime.getTimeString(
                    Math.round((tx[dx] + Math.abs(offsetX)) / partWidth * partDuration) + partDuration * n);

            double y = 2;

            int measureText = g.getFontMetrics().stringWidth(s);

            g.setColor(cx[dx]);
            g.draw(new Line2D.Double(tx[dx], 0, tx[dx], h));
            g.translate(tx[dx] - 2, y + measureText);
            g.rotate(Math.toRadians(-90));
            g.drawString(s, 0, 0);
            g.rotate(Math.toRadians(90));
            g.translate(-(tx[dx] - 2), -(y + measureText));
        }
        
        
        g.setFont(oldFont);
    }
    
    private void drawStartEndArea(Graphics2D g, double startX, double endX, double offsetX, double h, int n){
        Font oldFont = g.getFont();
        
        double stickyStart = startX + offsetX;
        double stickyEnd = endX + offsetX;
        
        g.setColor(DrawColor.blue.getColor(.35f));
        g.fill(new Rectangle2D.Double(stickyStart, 0, endX - startX, h));
        
        String s = AssTime.getDiff(
                Math.round(stickyStart / partWidth * partDuration) + partDuration * n,
                Math.round(stickyEnd / partWidth * partDuration) + partDuration * n
        );
        g.setFont(oldFont.deriveFont(Font.ITALIC));
        int measureText = g.getFontMetrics().stringWidth(s);
        
        double y = 2;
        
        // Aire / Area
        g.setColor(Color.white);
        g.draw(new Line2D.Double(stickyEnd, 0, stickyEnd, h));
        g.translate(stickyEnd - 2, y + 12 + measureText * 2);
        g.rotate(Math.toRadians(-90));
        g.drawString(s, 0, 0);
        g.rotate(Math.toRadians(90));
        g.translate(-(stickyEnd - 2), -(y + 12 + measureText * 2));
        
        g.setFont(oldFont);
        
        
        s = AssTime.getTimeString(Math.round(startX / partWidth * partDuration) + partDuration * n);
        measureText = g.getFontMetrics().stringWidth(s);
        
        // Début / Start
        g.setColor(Color.green);
        g.draw(new Line2D.Double(stickyStart, 0, stickyStart, h));
        g.translate(stickyStart - 2, y + measureText);
        g.rotate(Math.toRadians(-90));
        g.drawString(s, 0, 0);
        g.rotate(Math.toRadians(90));
        g.translate(-(stickyStart - 2), -(y + measureText));
        
        s = AssTime.getTimeString(Math.round(endX / partWidth * partDuration) + partDuration * n);
        measureText = g.getFontMetrics().stringWidth(s);

        // Fin / End
        g.setColor(Color.red);
        g.draw(new Line2D.Double(stickyEnd, 0, stickyEnd, h));
        g.translate(stickyEnd - 2, y + measureText);
        g.rotate(Math.toRadians(-90));
        g.drawString(s, 0, 0);
        g.rotate(Math.toRadians(90));
        g.translate(-(stickyEnd - 2), -(y + measureText));
        
        g.setFont(oldFont);
    }
    
    private void drawKeyFrames(Graphics2D g, double offsetX, double h, int n){
        g.setColor(Color.magenta);
        double y = 2;
        for(Map.Entry<Long, Integer> entry : keyframes.entrySet()){
            // partDuration 5000L ms, partWidth 2000 pixels, entry ms, x pixels
            // x <> partWidth (partWidth as pw)
            // ms <> partDuration (partDuration as pd)
            // x = ms * partWidth / partDuration
            // - partWidth * n for shifting of value
            double ms = (double)entry.getKey();
            double pw = (double)partWidth;
            double pd = partDuration > 0 ? (double)partDuration : 1d;
            double xt = ms * pw / pd - partWidth * n;
            g.draw(new Line2D.Double(offsetX+xt, 0, offsetX+xt, h));
            
            // Display understanding text
            String t = AssTime.getTimeString((long)entry.getKey());
            String frame = Integer.toString(entry.getValue());
            String s = String.format("Keyframe: %s, Time: %s", frame, t);
            int measureText = g.getFontMetrics().stringWidth(s);

            g.translate(offsetX+xt - 2, y + measureText);
            g.rotate(Math.toRadians(-90));
            g.drawString(s, 0, 0);
            g.rotate(Math.toRadians(90));
            g.translate(-(offsetX+xt - 2), -(y + measureText));
        }
    }
    
    private void drawKaraoke(Graphics2D g){
        Font oldFont = g.getFont();
        Stroke oldStroke = g.getStroke();
        
        // On dessine la zone de karaoké
//        if(karaSyllables.isEmpty() == false){
//            AssTime sSyl, eSyl;
//            if(sylIndex == 0){
//                sSyl = start;
//                eSyl = karaSyllables.get(sylIndex).getTime();
//            }else{
//                sSyl = karaSyllables.get(sylIndex - 1).getTime();
//                eSyl = karaSyllables.get(sylIndex).getTime();
//            }
//
//            g.setColor(DrawColor.white_smoke.getColor(.25f));
//            AssTime diff = AssTime.substract(sSyl, eSyl);
//            double diffWidth = AssTime.toMillisecondsTime(diff) * width / partDuration;
//            double xSyl = AssTime.toMillisecondsTime(sSyl) * width / partDuration;
//            g.fill(new Rectangle2D.Double(offsetX+xSyl, 0, diffWidth, height));
//        }
        
//        for(KaraokeSyllable ks : karaSyllables){
//            double x = AssTime.toMillisecondsTime(ks.getTime()) * width / partDuration;
//            double y = 2;
//
//            String s = String.format("%s %dms -> %s",
//                    ks.getSyllable(), ks.getMs(), ks.getTime().toASSTime()
//            );
//            if(f != null){
//                g.setFont(f);
//            }else{
//                g.setFont(g.getFont().deriveFont(Font.BOLD));
//            }
//            int measureText = g.getFontMetrics().stringWidth(s);
//
//            g.setStroke(new BasicStroke(
//                    1f,
//                    BasicStroke.CAP_ROUND,
//                    BasicStroke.JOIN_ROUND,
//                    20f,
//                    new float[]{2f, 5f},
//                    3f)
//            );
//            
//            g.setColor(sylIndex == ks.getIndex() ? DrawColor.orchid.getColor() : Color.white);
//            g.draw(new Line2D.Double(offsetX+x, 0, offsetX+x, height));
//            g.translate(offsetX+x-2, y+measureText);
//            g.rotate(Math.toRadians(-90));
//            g.drawString(s, 0, 0);
//            g.rotate(Math.toRadians(90));
//            g.translate(-(offsetX+x-2), -(y+measureText));
//        }
        
        g.setStroke(oldStroke);
        g.setFont(oldFont);
    }
    
    //==========================================================================
    //88888888888888888888888888888888888888888888888888888888888888888888888888
    //==========================================================================
    
    protected class Waveform implements Runnable {
        
        private final File folder;
        private final String project;
        private final File projectFolder;
        
        private long partDuration = 10000L;
        private long mediaMsDuration = 10000L;
        
        private Thread th = null;
        private volatile boolean active = false;
        
        private int countImages = 0;

        public Waveform(long partDuration, long mediaMsDuration) {
            this.partDuration = partDuration;
            this.mediaMsDuration = mediaMsDuration;
            folder = new File(System.getProperty("user.home"), "yggdrasil projects");
            project = "Untitled project";
            projectFolder = new File(folder.getPath() + File.separator + project);
            if(projectFolder.exists() == false){
                projectFolder.mkdirs();
            }
        }
        
        public void startThread(){
            stopThread();
            active = true;
            th = new Thread(this);
            th.start();
        }
        
        public void stopThread(){
            if(th != null && th.isAlive()) th.interrupt();
            if(th != null && !th.isInterrupted()) th.interrupt();
            if(active == false) return;
            active = false;            
            th = null;
        }
        
        public ImagesBasket getAt(long ms){
            int index = 1;
            while(partDuration * index < ms){
                index++;
            }
            
            File imgFile1 = new File(projectFolder, "w" + Integer.toString(index + 0) + ".png");
            File imgFile2 = new File(projectFolder, "w" + Integer.toString(index + 1) + ".png");
            
            BufferedImage img1 = null, img2 = null;
            
            try{
                img1 = imgFile1.exists() ? ImageIO.read(imgFile1) : null;
                img2 = imgFile2.exists() ? ImageIO.read(imgFile2) : null;
            }catch(IOException exc){
                
            }
            
            return new ImagesBasket(img1, img2);
        }
        
        private void doTask() throws IOException{
            if(partDuration * (countImages + 1) < mediaMsDuration){
                BufferedImage img = data.getWaveForm(
                        partDuration * countImages,
                        partDuration * (countImages + 1)
                );
                if(img == null) return;
                File imgFile = new File(projectFolder, "w" + Integer.toString(countImages + 1) + ".png");
                ImageIO.write(img, "png", imgFile);
                countImages++;
            }else{
                BufferedImage img = data.getWaveForm(
                        partDuration * countImages,
                        mediaMsDuration
                );
                if(img == null) return;
                File imgFile = new File(projectFolder, "w" + Integer.toString(countImages + 1) + ".png");
                ImageIO.write(img, "png", imgFile);
                stopThread();
            }            
        }
        
        @Override
        public void run() {
            while(true){
                if(active){
                    try{
                        doTask();
                    }catch(IOException exc){
                        stopThread();
                    }
                }
            }
        }
    }
    
    protected class Spectrum implements Runnable {
        
        private final File folder;
        private final String project;
        private final File projectFolder;
        
        private Thread th = null;
        private volatile boolean active = false;
        
        private long partDuration = 10000L;
        private long mediaMsDuration = 10000L;
        
        private int countImages = 0;

        public Spectrum(long partDuration, long mediaMsDuration) {
            this.partDuration = partDuration;
            this.mediaMsDuration = mediaMsDuration;
            folder = new File(System.getProperty("user.home"), "yggdrasil projects");
            project = "Untitled project";
            projectFolder = new File(folder.getPath() + File.separator + project);
            if(projectFolder.exists() == false){
                projectFolder.mkdirs();
            }
        }
        
        public void startThread(){
            stopThread();
            active = true;
            th = new Thread(this);
            th.start();
        }
        
        public void stopThread(){
            if(th != null && th.isAlive()) th.interrupt();
            if(th != null && !th.isInterrupted()) th.interrupt();
            if(active == false) return;
            active = false;            
            th = null;
        }
        
        public ImagesBasket getAt(long ms){
            int index = 1;
            while(partDuration * index < ms){
                index++;
            }
            
            File imgFile1 = new File(projectFolder, "s" + Integer.toString(index + 0) + ".png");
            File imgFile2 = new File(projectFolder, "s" + Integer.toString(index + 1) + ".png");
            
            BufferedImage img1 = null, img2 = null;
            
            try{
                img1 = imgFile1.exists() ? ImageIO.read(imgFile1) : null;
                img2 = imgFile2.exists() ? ImageIO.read(imgFile2) : null;
            }catch(IOException exc){
                
            }
            
            return new ImagesBasket(img1, img2);
        }
        
        private void doTask() throws IOException{
            if(partDuration * (countImages + 1) < mediaMsDuration){
                BufferedImage img = data.getSpectrogram(
                        partDuration * countImages,
                        partDuration * (countImages + 1)
                );
                if(img == null) return;
                File imgFile = new File(projectFolder, "s" + Integer.toString(countImages + 1) + ".png");
                ImageIO.write(img, "png", imgFile);
                countImages++;
            }else{
                BufferedImage img = data.getSpectrogram(
                        partDuration * countImages,
                        mediaMsDuration
                );
                if(img == null) return;
                File imgFile = new File(projectFolder, "s" + Integer.toString(countImages + 1) + ".png");
                ImageIO.write(img, "png", imgFile);
                stopThread();
            }
        }
        
        @Override
        public void run() {
            while(true){
                if(active){
                    try{
                        doTask();
                    }catch(IOException exc){
                        stopThread();
                    }
                }
            }
        }
    }
    
    protected class ImagesBasket {
        
        private final BufferedImage imgOne;
        private final BufferedImage imgTwo;

        public ImagesBasket(BufferedImage imgOne, BufferedImage imgTwo) {
            this.imgOne = imgOne;
            this.imgTwo = imgTwo;
        }

        public BufferedImage getImgOne() {
            return imgOne;
        }

        public BufferedImage getImgTwo() {
            return imgTwo;
        }
        
    }
    
}
