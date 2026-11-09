package feuille.module.audio.base;

import feuille.io.assa.AssTime;
import feuille.module.editor.base.Syllable;
import feuille.util.DrawColor;
import feuille.io.Exchange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wave extends JPanel {

    private final Exchange exchange;

    private static final long MS_PERIOD = 15_000L;

    private Color waveColor;
    private Color hourColor;
    private Color minuteColor;
    private Color secondColor;
    private Color msColor; // Milliseconds
    private Color miColor; // Microseconds
    private Color selAreaStartColor;
    private Color selAreaEndColor;
    private Color selAreaColor;
    private Color karaokeColor;
    private Color cursorColor;
    private Color currentTimeColor;

    private BufferedImage current;
    private BufferedImage next;
    private boolean useSpectrum;

    private long offset; // Milliseconds
    private int offsetX; // Integer (pixels)
    private long msCurrent; // Milliseconds
    private long msCurrentStart; // Milliseconds (current image start)
    private long msCurrentEnd; // Milliseconds (current image end)
    private long msNextStart; // Milliseconds (next image start)
    private long msNextEnd; // Milliseconds (next image end)

    private int startAnchor;
    private int endAnchor;
    private int karaokeStartAnchor;
    private int karaokeEndAnchor;

    private boolean karaoke;

    public Wave(Exchange exchange) {
        this.exchange = exchange;
        setDoubleBuffered(true);
        waveColor = DrawColor.corn_flower_blue.getColor();
        hourColor = Color.black;
        minuteColor = Color.orange;
        secondColor = Color.yellow;
        msColor = DrawColor.beige.getColor();
        miColor = DrawColor.antique_white.getColor();
        selAreaStartColor = Color.green;
        selAreaEndColor = Color.red;
        selAreaColor = DrawColor.green.getColor(.25f);
        karaokeColor = Color.cyan;
        cursorColor = Color.white;
        currentTimeColor = DrawColor.black.getColor(.8f);

        current = null;
        next = null;
        useSpectrum = false;

        offset = 0L;
        offsetX = 0;
        msCurrent = 0L;
        msCurrentStart = 0L;
        msCurrentEnd = 0L;
        msNextStart = 0L;
        msNextEnd = 0L;

        startAnchor = 0;
        endAnchor = 0;
        karaokeStartAnchor = 0;
        karaokeEndAnchor = 0;

        karaoke = false;

        setBorder(BorderFactory.createLineBorder(Color.gray));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                double oneSecond = getWidth() / 15d; // pixels

                int globalShift = Math.toIntExact(offset * getWidth() / MS_PERIOD);
                int visibleShift = globalShift - (globalShift / getWidth());

                switch(e.getButton()){
                    case MouseEvent.BUTTON1 -> {
                        if(!karaoke){
                            startAnchor = e.getX() + visibleShift;
                            double ms = startAnchor / oneSecond * 1000d;
                            exchange.getEditorPanel().setToLockStart(new AssTime(ms));
                            exchange.setStartTime(new AssTime(ms));
                            if(endAnchor - startAnchor > 0){
                                double end = endAnchor / oneSecond * 1000d;
                                exchange.getEditorPanel().setToLockDuration(
                                        new AssTime(ms), new AssTime(end)
                                );
                            }
                        }else{
                            reKStart(e.getX(), visibleShift, oneSecond);
                        }
                    }
                    case MouseEvent.BUTTON2 -> {
                        if(!karaoke){
                            startAnchor = 0;
                            exchange.getEditorPanel().setToLockStart(new AssTime(0));
                            endAnchor = 0;
                            exchange.getEditorPanel().setToLockEnd(new AssTime(0));
                            exchange.getEditorPanel().setToLockDuration(new AssTime(0));
                            exchange.setStartTime(new AssTime(0));
                            exchange.setEndTime(new AssTime(0));
                        }
                    }
                    case MouseEvent.BUTTON3 -> {
                        if(!karaoke){
                            endAnchor = e.getX() + visibleShift;
                            double ms = endAnchor / oneSecond * 1000d;
                            exchange.getEditorPanel().setToLockEnd(new AssTime(ms));
                            exchange.setEndTime(new AssTime(ms));
                            if(endAnchor - startAnchor > 0){
                                double start = startAnchor / oneSecond * 1000d;
                                exchange.getEditorPanel().setToLockDuration(
                                        new AssTime(start), new AssTime(ms)
                                );
                            }
                        }else{
                            reKEnd(e.getX(), visibleShift, oneSecond);
                        }
                    }
                }

                repaint();
            }
        });

        addMouseWheelListener((e) -> {
            offset = e.getWheelRotation() > 0 ? offset + 250L : offset - 250L;

            repaint();
        });
    }

    private void reKStart(int x, int visibleShift, double oneSecond){
        int sylIndex = exchange.getKaraokeDisplay().getCurrentSyllableIndex();
        if(sylIndex == 0) return;

        // Syllables
        List<Syllable> syllables = exchange.getKaraokeDisplay().getSyllables();

        karaokeStartAnchor = x + visibleShift;

        double msTriggered = karaokeStartAnchor / oneSecond * 1000d;

        long sylMs = (long) (startAnchor / oneSecond * 1000d);

        for(int i=0; i<sylIndex - 1; i++){
            sylMs += syllables.get(i).getMsDuration();
        }

        long currentMs = exchange.getKaraokeDisplay()
                .getSyllables()
                .get(sylIndex)
                .getMsDuration();

        long toChangeMs = (Math.round(msTriggered - sylMs)) - currentMs;

        exchange.getKaraokeDisplay()
                .getSyllables()
                .get(sylIndex - 1)
                .setMsDuration(Math.round(msTriggered - sylMs));

        exchange.getKaraokeDisplay()
                .getSyllables()
                .get(sylIndex)
                .setMsDuration(toChangeMs);

        exchange.getKaraokeDisplay().repaintKaraoke();
    }

    private void reKEnd(int x, int visibleShift, double oneSecond){
        int sylIndex = exchange.getKaraokeDisplay().getCurrentSyllableIndex();
        if(sylIndex == exchange.getKaraokeDisplay().getSyllables().size() - 1) return;

        // Syllables
        List<Syllable> syllables = exchange.getKaraokeDisplay().getSyllables();

        // Trigger
        karaokeEndAnchor = x + visibleShift;
        double msTriggered = karaokeEndAnchor / oneSecond * 1000d;

        // Previously
        double karaokeStartMs = startAnchor / oneSecond * 1000d;
        for(int i=0; i<syllables.size(); i++){
            if(i <= sylIndex - 1){
                karaokeStartMs += syllables.get(i).getMsDuration();
            }
        }

        karaokeStartAnchor = (int)(karaokeStartMs * oneSecond / 1000d);

        exchange.getKaraokeDisplay()
                .getSyllables()
                .get(sylIndex)
                .setMsDuration(Math.round(msTriggered - karaokeStartMs));

        long sylMs = (long) (startAnchor / oneSecond * 1000d);

        for(int i=0; i<syllables.size() - 1; i++){
            sylMs += syllables.get(i).getMsDuration();
        }

        long endSentenceMs = (long) (endAnchor / oneSecond * 1000d);

        exchange.getKaraokeDisplay()
                .getSyllables()
                .getLast()
                .setMsDuration(Math.round(endSentenceMs - sylMs));

        exchange.getKaraokeDisplay().repaintKaraoke();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //=================================================
        double oneSecond = getWidth() / 15d; // pixels

        g2d.setColor(UIManager.getColor("Panel.background"));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int globalShift = Math.toIntExact(offset * getWidth() / MS_PERIOD);
        int visibleShift = globalShift - (globalShift / getWidth());

        // How many times we have 'visibleShift' in 'width':
        int a = visibleShift / getWidth();
        // What is the rest:
        offsetX = visibleShift - (a * getWidth());

        long start = Math.abs(offset / MS_PERIOD) * MS_PERIOD;
        current = FFAudio2.getImage(start, useSpectrum);
        next = FFAudio2.getImage(start + MS_PERIOD, useSpectrum);
        if(current != null){
            g2d.drawImage(current, -offsetX, 0, this);
        }
        if(next != null){
            g2d.drawImage(next, -offsetX + getWidth(), 0, this);
        }

        if(current != null || next != null){
            if(!useSpectrum){
                // Sound line that represents center (zero dB)
                g2d.setColor(waveColor);
                g2d.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
            }

            // Seconds (bars)
            g2d.setColor(secondColor);
            for(double i=-visibleShift; i<getWidth(); i+=oneSecond){
                g2d.draw(new Line2D.Double(i, 0, i, getHeight()));
            }

            // Seconds (texts)
            g2d.setColor(Color.black);
            for(float i=-visibleShift, j=0; i<getWidth(); i+=(float)oneSecond, j++){
                g2d.drawString(String.format("%d s", (int)j), i-20, 16);
            }

            if(endAnchor - startAnchor > 0 && !karaoke){
                // Area color
                drawLine(g2d, selAreaColor, -visibleShift+startAnchor, -visibleShift+endAnchor);
            }

            // Area start
            drawLine(g2d, selAreaStartColor, -visibleShift+startAnchor);
            // Area end
            drawLine(g2d, selAreaEndColor, -visibleShift+endAnchor);

            if(karaokeEndAnchor - karaokeStartAnchor > 0 && karaoke){
                // Area color
                drawLine(g2d, selAreaColor, -visibleShift+karaokeStartAnchor, -visibleShift+karaokeEndAnchor);
            }

            // Karaoke area start
            drawLine(g2d, karaokeColor, -visibleShift+karaokeStartAnchor);
            // Karaoke area end
            drawLine(g2d, karaokeColor, -visibleShift+karaokeEndAnchor);

            // Current playing position
            g2d.setColor(currentTimeColor);
            double currentSecond = oneSecond * (msCurrent / 1000d);
            g2d.draw(new Line2D.Double(-visibleShift+currentSecond, 0, -visibleShift+currentSecond, getHeight()));
        }

        //=================================================
        g2d.dispose();
    }

    private void drawLine(Graphics2D g2d, Color c, double x){
        Graphics2D ctx = (Graphics2D) g2d.create();
        double oneSecond = getWidth() / 15d; // pixels
        double ms = x / oneSecond * 1000d;
        ctx.setColor(c);
        ctx.draw(new Line2D.Double(x, 0, x, getHeight()));
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        ctx.setTransform(at);
        ctx.drawString(new AssTime(ms).toAss(), -90, (int)(x) - 5);
        ctx.dispose();
    }

    private void drawLine(Graphics2D g2d, Color c, double x1, double x2){
        Graphics2D ctx = (Graphics2D) g2d.create();
        double oneSecond = getWidth() / 15d; // pixels
        double ms1 = x1 / oneSecond * 1000d;
        double ms2 = x2 / oneSecond * 1000d;
        double ms = Math.max(ms1, ms2) - Math.min(ms1, ms2);
        ctx.setColor(c);
        ctx.fill(new Rectangle2D.Double(
                x1,
                0,
                Math.max(x1, x2) - Math.min(x1, x2),
                getHeight()
        ));
        ctx.setColor(DrawColor.deep_pink.getColor());
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        ctx.setTransform(at);
        ctx.drawString(new AssTime(ms).toAss(), -90, (int)(x2) + 15);
        ctx.dispose();
    }

    private Map<Integer, Long> getAllKaraTime(){
        Map<Integer, Long> map = new HashMap<>();
        int index = 0;
        for(Syllable syl : exchange.getKaraokeDisplay().getSyllables()){
            map.put(index, syl.getMsDuration());
            index++;
        }
        return map;
    }

    private void replaceKaraTime(int index, long ms){
        exchange.getKaraokeDisplay().getSyllables().get(index).setMsDuration(ms);
        exchange.getKaraokeDisplay().repaint();
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

    public Color getKaraokeColor() {
        return karaokeColor;
    }

    public void setKaraokeColor(Color karaokeColor) {
        this.karaokeColor = karaokeColor;
    }

    public Color getCurrentTimeColor() {
        return currentTimeColor;
    }

    public void setCurrentTimeColor(Color currentTimeColor) {
        this.currentTimeColor = currentTimeColor;
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

    public void setUseSpectrum(boolean useSpectrum) {
        this.useSpectrum = useSpectrum;
    }

    public boolean isKaraoke() {
        return karaoke;
    }

    public void setKaraoke(boolean karaoke) {
        this.karaoke = karaoke;
    }

    public long getMsCurrentKStartTime() {
        double oneSecond = getWidth() / 15d; // pixels
        return Math.round(karaokeStartAnchor / oneSecond * 1000d);
    }

    public void setMsCurrentKStartTime(long msCurrentKStartTime) {
        double oneSecond = getWidth() / 15d; // pixels
        karaokeStartAnchor = (int)(msCurrentKStartTime * oneSecond / 1000d);
    }

    public long getMsCurrentKEndTime() {
        double oneSecond = getWidth() / 15d; // pixels
        return Math.round(karaokeEndAnchor / oneSecond * 1000d);
    }

    public void setMsCurrentKEndTime(long msCurrentKEndTime) {
        double oneSecond = getWidth() / 15d; // pixels
        karaokeEndAnchor = (int)(msCurrentKEndTime * oneSecond / 1000d);
    }
}
