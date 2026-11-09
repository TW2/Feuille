package feuille.module.editor;

import feuille.io.Exchange;
import feuille.module.editor.base.Syllable;
import feuille.util.Loader;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KaraokeDisplay extends JPanel {

    private final Exchange exchange;
    private final Painter painter;

    public KaraokeDisplay(Exchange exchange) {
        this.exchange = exchange;

        painter = new Painter(exchange);

        JButton btnLeft = new JButton("<< " + Loader.language("karaoke.btn.left", "Left"));
        JButton btnSeparate = new JButton(Loader.language("karaoke.btn.separate", "Separate"));
        JTextField tfDuration = new JTextField(0);
        JButton btnTie = new JButton(Loader.language("karaoke.btn.tie", "Tie"));
        JButton btnRight = new JButton(Loader.language("karaoke.btn.right", "Right") + " >>");

        PlainDocument doc = (PlainDocument) tfDuration.getDocument();
        doc.setDocumentFilter(new MyIntFilter());

        btnLeft.addActionListener(e -> {
            int v = painter.getCurrentIndex() - 1;
            int max = 0;
            painter.setCurrentIndex(Math.max(max, v));
            painter.repaint();
        });

        btnRight.addActionListener(e -> {
            int v = painter.getCurrentIndex() + 1;
            int min = painter.getSyllables().size() - 1;
            painter.setCurrentIndex(Math.min(min, v));
            painter.repaint();
        });

        tfDuration.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                Syllable syllable = painter.getSyllables().get(painter.getCurrentIndex());
                syllable.setMsDuration(Long.parseLong(tfDuration.getText()));
                painter.getSyllables().set(painter.getCurrentIndex(), syllable);
                painter.repaint();
            }
        });

        btnSeparate.addActionListener(e -> {
            Syllable syllable = painter.getSyllables().get(painter.getCurrentIndex());
            if(syllable.getText().length() >= 2) {
                String before = syllable.getText().substring(0, 1);
                String after = syllable.getText().substring(1);
                long beforeTime = syllable.getMsDuration() / 2;
                long afterTime = syllable.getMsDuration() - beforeTime;

                Syllable s1 = new Syllable(before, beforeTime);
                Syllable s2 = new Syllable(after, afterTime);

                painter.getSyllables().add(painter.getCurrentIndex(), s2);
                painter.getSyllables().add(painter.getCurrentIndex(), s1);
                painter.getSyllables().remove(painter.getCurrentIndex() + 2);

                painter.repaint();
            }
        });

        btnTie.addActionListener(e -> {
            Syllable s1 = painter.getSyllables().get(painter.getCurrentIndex());
            Syllable s2 = painter.getSyllables().get(painter.getCurrentIndex() + 1);

            String text = s1.getText() + s2.getText();
            long time = s1.getMsDuration() + s2.getMsDuration();

            Syllable s = new Syllable(text, time);

            painter.getSyllables().set(painter.getCurrentIndex(), s);
            painter.getSyllables().remove(painter.getCurrentIndex() + 1);

            painter.repaint();
        });

        JPanel topPanel = new JPanel(new GridLayout(1, 5));
        topPanel.add(btnLeft);
        topPanel.add(btnSeparate);
        topPanel.add(tfDuration);
        topPanel.add(btnTie);
        topPanel.add(btnRight);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(painter, BorderLayout.CENTER);
    }

    public void forceResize(int w, int h) {
        setSize(w, h);
        updateUI();
    }

    public void adjustTimes(double startMs, double endMs, List<Long> times){
        if(times.size() != getSyllables().size()) return;
        long ms = (long) startMs;
        for(int i=0; i<getSyllables().size(); i++){
            Syllable s = getSyllables().get(i);
            if(s.hashCode() == getSyllables().getLast().hashCode()){
                s.setMsDuration((long) (endMs - ms));
            }else{
                s.setMsDuration(times.get(i));
                ms += times.get(i);
            }
        }
        painter.repaint();
    }

    public List<Long> getMsTimes(){
        List<Long> list = new ArrayList<>();
        for(Syllable s : getSyllables()){
            list.add(s.getMsDuration());
        }
        return list;
    }

    public String getCurrentText(){
        return painter.getCurrentText();
    }

    public void setCurrentText(String text){
        painter.setCurrentText(text);
    }

    public int getCurrentSyllableIndex(){
        return painter.getCurrentIndex();
    }

    public void setCurrentSyllableIndex(int index){
        painter.setCurrentIndex(index);
        painter.repaint();
    }

    public List<Syllable> getSyllables(){
        return painter.getSyllables();
    }

    public void setSyllables(List<Syllable> syllables){
        painter.setSyllables(syllables);
        painter.repaint();
    }

    public void repaintKaraoke(){
        painter.repaint();
    }

    public static class Painter extends JPanel{

        private String currentText;
        private int currentIndex;
        private int generalHeight;
        private List<Syllable> syllables;
        private final List<Integer> syllableWidths;

        public Painter(Exchange exchange) {
            setDoubleBuffered(true);
            currentText = null;
            currentIndex = 0;
            generalHeight = 0;
            syllables = new ArrayList<>();
            syllableWidths = new ArrayList<>();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(currentText == null || currentText.isEmpty()) return;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            //----------------------------------------------------
            g2d.setColor(UIManager.getColor("Panel.background"));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(UIManager.getColor("Panel.foreground"));
            g2d.setFont(g2d.getFont().deriveFont(34f));
            drawTextRectangle(g2d, getWidth(), getHeight());
            //----------------------------------------------------
            g2d.dispose();
        }

        private void drawTextRectangle(Graphics2D g2d, int width, int height){
            if(syllables.isEmpty()) makeSyllables();
            if(syllables.isEmpty()) makeSyllablesByChars();
            int generalWidth =  makeSyllableWidths(g2d);
            Color foreground = UIManager.getColor("Panel.foreground");

            int x = (width - generalWidth) / 2;
            int y = (height - (generalHeight*2 + 40)) / 2;

            int xa = 0;

            for(int i=0; i<syllables.size(); i++) {
                Syllable syllable = syllables.get(i);
                int wSyl = syllableWidths.get(i);

                if(currentIndex == i){
                    g2d.setColor(Color.red);
                }else{
                    g2d.setColor(foreground);
                }

                g2d.drawString(
                        syllable.getText(),
                        x + xa,
                        y + generalHeight
                );

                g2d.drawString(
                        Long.toString(syllable.getMsDuration() / 10L),
                        x + xa,
                        y + 40 + generalHeight
                );

                xa += wSyl;
            }
        }

        private void makeSyllables(){
            Pattern p = Pattern.compile("\\{\\\\k(?<kTime>\\d+)}(?<str>[^{]*)");
            Matcher m = p.matcher(currentText);
            syllables.clear();
            while(m.find()){
                syllables.add(new Syllable(
                        m.group("str"),
                        Long.parseLong(m.group("kTime")) * 10L
                ));
            }
        }

        private void makeSyllablesByChars(){
            for(char c : currentText.toCharArray()){
                syllables.add(new Syllable(Character.toString(c), 0L));
            }
        }

        public int makeSyllableWidths(Graphics2D g2d){
            FontMetrics fm = g2d.getFontMetrics();
            generalHeight = 0;
            int generalWidth = 0;
            syllableWidths.clear();
            for(Syllable syllable : syllables){
                int width = 20 + fm.stringWidth(syllable.getText()) + 20;
                syllableWidths.add(width);
                generalWidth += width;
            }
            generalHeight = fm.getHeight();
            return generalWidth;
        }

        public String getCurrentText() {
            if(!syllables.isEmpty()){
                StringBuilder sb = new StringBuilder();
                for(Syllable syllable : syllables){
                    sb.append("{\\k")
                            .append(syllable.getMsDuration()/10L)
                            .append("}")
                            .append(syllable.getText());
                }
                return sb.toString();
            }else{
                return currentText;
            }
        }

        public void setCurrentText(String currentText) {
            syllables.clear();
            generalHeight = 0;
            this.currentText = currentText;
            repaint();
        }

        public int getCurrentIndex() {
            return currentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        public List<Syllable> getSyllables() {
            return syllables;
        }

        public void setSyllables(List<Syllable> syllables) {
            this.syllables = syllables;
        }

        public List<Integer> getSyllableWidths() {
            return syllableWidths;
        }

        public void setGeneralHeight(int generalHeight) {
            this.generalHeight = generalHeight;
        }
    }

    static class MyIntFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string,
                                 AttributeSet attr) throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (test(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            }
        }

        private boolean test(String text) {
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text,
                            AttributeSet attrs) throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (test(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            }

        }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.delete(offset, offset + length);

            if (test(sb.toString())) {
                super.remove(fb, offset, length);
            }

        }
    }


}
