package org.wingate.feuille.lib.video;

import org.wingate.feuille.engine.FFMpeg;

import java.awt.*;
import java.awt.image.BufferedImage;

public class VideoPlayer extends javax.swing.JPanel {

    private final FFMpeg ffEngine;
    private BufferedImage image;
    private long currentMicros;
    private double fps;
    private int frame;

    public VideoPlayer(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        ffEngine = new FFMpeg();
        image = null;
        currentMicros = 0L;
        fps = 24000d / 1001d;
        frame = 0;

        ffEngine.addMediaListener(new FFMpeg.FFMessageListener() {
            @Override
            public void updatedMessage(FFMpeg.FFMessage e) {
                image = e.getImage();
                currentMicros = e.getCurrentMicro();
                fps = e.getFps();
                frame = e.getFrame();
                repaint();
            }
        });
    }

    public void setMediaPath(String mediaPath) {
        ffEngine.setMedia(mediaPath);
    }

    public void play(){
        ffEngine.play();
    }

    public void pause(){
        ffEngine.pause();
    }

    public void stop(){
        ffEngine.stop();
    }

    public void setStartTime(long micros){
        ffEngine.setStartTime(micros);
    }

    public void setEndTime(long micros){
        ffEngine.setEndTime(micros);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0,0, getWidth(), getHeight());

        if(image != null){
            double ratioX = (double) getWidth() / image.getWidth();
            double ratioY = (double) getHeight() / image.getHeight();

            double reducCoeff;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            int w = (int) (image.getWidth() * reducCoeff);
            int h = (int) (image.getHeight() * reducCoeff);

            int x = (getWidth() - w) / 2;
            int y = (getHeight() - h) / 2;

            g.drawImage(image, x, y, w, h, null);
        }
    }

    public FFMpeg getFfEngine() {
        return ffEngine;
    }

    public BufferedImage getImage() {
        return image;
    }

    public long getCurrentMicros() {
        return currentMicros;
    }

    public double getFps() {
        return fps;
    }

    public int getFrame() {
        return frame;
    }
}
