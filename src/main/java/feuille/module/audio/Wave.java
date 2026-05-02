package feuille.module.audio;

import feuille.module.editor.assa.AssTime;
import feuille.util.DrawColor;
import feuille.util.Exchange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Wave extends JPanel {

    private final Exchange exchange;

    private static final long MS_PERIOD = 15_000L;

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

    private BufferedImage current;
    private BufferedImage next;

    private long offset; // Milliseconds
    private int offsetX; // Integer (pixels)
    private long msCurrent; // Milliseconds
    private long msCurrentStart; // Milliseconds (current image start)
    private long msCurrentEnd; // Milliseconds (current image end)
    private long msNextStart; // Milliseconds (next image start)
    private long msNextEnd; // Milliseconds (next image end)

    private int startAnchor;
    private int endAnchor;

    public Wave(Exchange exchange) {
        this.exchange = exchange;
        setDoubleBuffered(true);

        backColor = Color.white;
        waveColor = DrawColor.corn_flower_blue.getColor();
        hourColor = Color.black;
        minuteColor = Color.orange;
        secondColor = Color.yellow;
        msColor = DrawColor.beige.getColor();
        miColor = DrawColor.antique_white.getColor();
        selAreaStartColor = Color.green;
        selAreaEndColor = Color.red;
        selAreaColor = DrawColor.green.getColor(.25f);
        cursorColor = Color.white;

        current = null;
        next = null;

        offset = 0L;
        offsetX = 0;
        msCurrent = 0L;
        msCurrentStart = 0L;
        msCurrentEnd = 0L;
        msNextStart = 0L;
        msNextEnd = 0L;

        startAnchor = 0;
        endAnchor = 0;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                double oneSecond = getWidth() / 15d; // pixels

                int globalShift = Math.toIntExact(offset * getWidth() / MS_PERIOD);
                int visibleShift = globalShift - (globalShift / getWidth());

                switch(e.getButton()){
                    case MouseEvent.BUTTON1 -> {
                        startAnchor = e.getX() + visibleShift;
                        double ms = startAnchor / oneSecond * 1000d;
                        exchange.getEditorPanel().setToLockStart(new AssTime(ms));
                        if(endAnchor - startAnchor > 0){
                            double end = endAnchor / oneSecond * 1000d;
                            exchange.getEditorPanel().setToLockDuration(
                                    new AssTime(ms), new AssTime(end)
                            );
                        }
                    }
                    case MouseEvent.BUTTON2 -> {
                        startAnchor = 0;
                        exchange.getEditorPanel().setToLockStart(new AssTime(0));
                        endAnchor = 0;
                        exchange.getEditorPanel().setToLockEnd(new AssTime(0));
                        exchange.getEditorPanel().setToLockDuration(new AssTime(0));
                    }
                    case MouseEvent.BUTTON3 -> {
                        endAnchor = e.getX() + visibleShift;
                        double ms = endAnchor / oneSecond * 1000d;
                        exchange.getEditorPanel().setToLockEnd(new AssTime(ms));
                        if(endAnchor - startAnchor > 0){
                            double start = startAnchor / oneSecond * 1000d;
                            exchange.getEditorPanel().setToLockDuration(
                                    new AssTime(start), new AssTime(ms)
                            );
                        }
                    }
                }

                repaint();
            }
        });

        addMouseWheelListener((e) -> {
            offset = e.getWheelRotation() > 0 ? offset + 250L : offset - 250L;

//            if(offset > msCurrentEnd || offset < msCurrentStart){
//                long start = -1L;
//
//                if(offset > msCurrentEnd){
//                    start = msCurrentEnd;
//                }
//
//                if(offset < msCurrentStart){
//                    start = msCurrentStart;
//                }
//
//                if(start == -1L){
//                    return;
//                }
//
//                current = FFAudio2.getImage(start, false);
//                next = FFAudio2.getImage(start + MS_PERIOD, false);
//                msCurrentStart = start;
//                msCurrentEnd = start + MS_PERIOD;
//                msNextStart = start + MS_PERIOD;
//                msNextEnd = start + MS_PERIOD * 2;
//            }

            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //=================================================

        g2d.setColor(backColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int globalShift = Math.toIntExact(offset * getWidth() / MS_PERIOD);
        int visibleShift = globalShift - (globalShift / getWidth());

        // How many times we have 'visibleShift' in 'width':
        int a = visibleShift / getWidth();
        // What is the rest:
        offsetX = visibleShift - (a * getWidth());

        long start = Math.abs(offset / MS_PERIOD) * MS_PERIOD;
        current = FFAudio2.getImage(start, false);
        next = FFAudio2.getImage(start + MS_PERIOD, false);
        if(current != null){
            g2d.drawImage(current, -offsetX, 0, this);
        }
        if(next != null){
            g2d.drawImage(next, -offsetX + getWidth(), 0, this);
        }

        if(current != null || next != null){
            g2d.setColor(waveColor);
            g2d.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);

            g2d.setColor(secondColor);
            for(int i=-visibleShift; i<getWidth(); i+=getWidth()/15){
                g2d.drawLine(i, 0, i, getHeight());
            }

            g2d.setColor(Color.black);
            for(int i=-visibleShift, j=0; i<getWidth(); i+=getWidth()/15, j++){
                g2d.drawString(String.format("%d s", j), i-20, 16);
            }

            if(endAnchor - startAnchor > 0){
                g2d.setColor(selAreaColor);
                g2d.fillRect(-visibleShift+startAnchor, 0, endAnchor - startAnchor, getHeight());
            }

            g2d.setColor(selAreaStartColor);
            g2d.drawLine(-visibleShift+startAnchor, 0, -visibleShift+startAnchor, getHeight());

            g2d.setColor(selAreaEndColor);
            g2d.drawLine(-visibleShift+endAnchor, 0, -visibleShift+endAnchor, getHeight());
        }

        //=================================================
        g2d.dispose();
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

    public void setCurrent(BufferedImage current) {
        this.current = current;
    }

    public void setNext(BufferedImage next) {
        this.next = next;
    }
}
