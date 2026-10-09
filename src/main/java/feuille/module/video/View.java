package feuille.module.video;

import feuille.util.Exchange;
import feuille.util.FFMpeg;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class View extends JPanel {

    private final AssaDB assaDB;
    private final FFMpeg mpeg;
    private BufferedImage image;
    private long currentMicros;
    private double fps;
    private int frame;

    private final ViewOverlay overlay;

    public View(Exchange exchange) {
        // Get the settings folder
        File app = new File(new File("").getAbsolutePath());
        File folder = new File(app + "/settings/assa");
        // Initialize the DB
        assaDB = new AssaDB(
                (new File(folder, "assa.db").toPath()),
                "assa"
        );

        mpeg = exchange.getMpeg();
        setDoubleBuffered(true);

        overlay = new ViewOverlay();

        image = null;
        currentMicros = 0L;
        fps = 24000d / 1001d;
        frame = 0;

        mpeg.addMediaListener((e) -> {
            currentMicros = e.getCurrentMicro();
            fps = e.getFps();
            frame = e.getFrame();
            image = blend(e.getImage());
            repaint();
        });
    }

    private BufferedImage blend(BufferedImage cur){
        BufferedImage sub = assaDB.get(currentMicros / 1000L);
        if(sub == null) return cur;
        Graphics2D g2d = cur.createGraphics();
        g2d.drawImage(sub, 0, 0, cur.getWidth(), cur.getHeight(), null);
        g2d.dispose();
        return cur;
    }

    public void setMediaPath(String mediaPath) {
        mpeg.setMedia(mediaPath);
    }

    public void play(){
        mpeg.play();
    }

    public void pause(){
        mpeg.pause();
    }

    public void stop(){
        mpeg.stop();
    }

    public void setStartTime(long micros){
        mpeg.setStartTime(micros);
    }

    public void setEndTime(long micros){
        mpeg.setEndTime(micros);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.fillRect(0,0, getWidth(), getHeight());

        if(image != null){
            double ratioX = (double) getWidth() / image.getWidth();
            double ratioY = (double) getHeight() / image.getHeight();

            double ratio = Math.min(ratioX, ratioY);

            int w = (int) (image.getWidth() * ratio);
            int h = (int) (image.getHeight() * ratio);

            int x = (getWidth() - w) / 2;
            int y = (getHeight() - h) / 2;

            g.drawImage(image, x, y, w, h, null);

            overlay.setMode(ViewOverlay.Mode.Line);
            //overlay.drawOverlay((Graphics2D)g, x, y, w, h);
        }
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
