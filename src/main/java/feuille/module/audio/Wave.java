package feuille.module.audio;

import feuille.util.DrawColor;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Wave extends JPanel {

    private Color backColor;
    private Color waveColor;
    private Color hourColor;
    private Color minuteColor;
    private Color secondColor;
    private Color msColor; // Milliseconds
    private Color miColor; // Microseconds
    private Color selAreaStartColor;
    private Color selAreaEndColor;
    private Color selAreaColor;
    private Color cursorColor;

    private final FFAudio ffAudio;
    private BufferedImage current;
    private BufferedImage next;

    private long offset; // Milliseconds
    private long msCurrent; // Milliseconds
    private long msCurrentStart; // Milliseconds (current image start)
    private long msCurrentEnd; // Milliseconds (current image end)
    private long msNextStart; // Milliseconds (next image start)
    private long msNextEnd; // Milliseconds (next image end)

    public Wave() {
        setDoubleBuffered(true);

        backColor = DrawColor.corn_flower_blue.getColor(.9f);
        waveColor = DrawColor.corn_flower_blue.getColor(.1f);
        hourColor = Color.red;
        minuteColor = Color.orange;
        secondColor = Color.yellow;
        msColor = DrawColor.beige.getColor();
        miColor = DrawColor.antique_white.getColor();
        selAreaStartColor = Color.green;
        selAreaEndColor = Color.green;
        selAreaColor = DrawColor.green.getColor(.5f);
        cursorColor = Color.white;

        ffAudio = new FFAudio();
        current = null;
        next = null;

        offset = 0L;
        msCurrent = 0L;
        msCurrentStart = 0L;
        msCurrentEnd = 0L;
        msNextStart = 0L;
        msNextEnd = 0L;

        ffAudio.addSignalListener((event -> {
            current = event.getCurrent();
            next = event.getNext();
            msCurrentStart = event.getCurrentMsStart();
            msCurrentEnd = event.getCurrentMsEnd();
            msNextStart = event.getNextMsStart();
            msNextEnd = event.getNextMsEnd();
            repaint();
        }));
    }

    public boolean setMedia(String path) throws FFmpegFrameGrabber.Exception {
        return ffAudio.setMedia(path);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //=================================================

        g2d.setColor(backColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if(current != null){
            long dur = msCurrentEnd - msCurrentStart;
            int globalShift = Math.toIntExact(msCurrent * getWidth() / dur);
            int visibleShift = globalShift - (globalShift / getWidth());
            g2d.drawImage(current, -visibleShift, 0, null);
            if(next != null) g2d.drawImage(next, getWidth() - visibleShift, 0, null);
        }

        //=================================================
        g2d.dispose();
    }

    public FFAudio getFfAudio() {
        return ffAudio;
    }

    public Color getBackColor() {
        return backColor;
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    public Color getWaveColor() {
        return waveColor;
    }

    public void setWaveColor(Color waveColor) {
        this.waveColor = waveColor;
    }

    public Color getHourColor() {
        return hourColor;
    }

    public void setHourColor(Color hourColor) {
        this.hourColor = hourColor;
    }

    public Color getMinuteColor() {
        return minuteColor;
    }

    public void setMinuteColor(Color minuteColor) {
        this.minuteColor = minuteColor;
    }

    public Color getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(Color secondColor) {
        this.secondColor = secondColor;
    }

    public Color getMsColor() {
        return msColor;
    }

    public void setMsColor(Color msColor) {
        this.msColor = msColor;
    }

    public Color getMiColor() {
        return miColor;
    }

    public void setMiColor(Color miColor) {
        this.miColor = miColor;
    }

    public Color getSelAreaStartColor() {
        return selAreaStartColor;
    }

    public void setSelAreaStartColor(Color selAreaStartColor) {
        this.selAreaStartColor = selAreaStartColor;
    }

    public Color getSelAreaEndColor() {
        return selAreaEndColor;
    }

    public void setSelAreaEndColor(Color selAreaEndColor) {
        this.selAreaEndColor = selAreaEndColor;
    }

    public Color getSelAreaColor() {
        return selAreaColor;
    }

    public void setSelAreaColor(Color selAreaColor) {
        this.selAreaColor = selAreaColor;
    }

    public Color getCursorColor() {
        return cursorColor;
    }

    public void setCursorColor(Color cursorColor) {
        this.cursorColor = cursorColor;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getMsCurrent() {
        return msCurrent;
    }

    public void setMsCurrent(long msCurrent) {
        this.msCurrent = msCurrent;
    }

    public long getMsCurrentStart() {
        return msCurrentStart;
    }

    public void setMsCurrentStart(long msCurrentStart) {
        this.msCurrentStart = msCurrentStart;
    }

    public long getMsCurrentEnd() {
        return msCurrentEnd;
    }

    public void setMsCurrentEnd(long msCurrentEnd) {
        this.msCurrentEnd = msCurrentEnd;
    }

    public long getMsNextStart() {
        return msNextStart;
    }

    public void setMsNextStart(long msNextStart) {
        this.msNextStart = msNextStart;
    }

    public long getMsNextEnd() {
        return msNextEnd;
    }

    public void setMsNextEnd(long msNextEnd) {
        this.msNextEnd = msNextEnd;
    }
}
