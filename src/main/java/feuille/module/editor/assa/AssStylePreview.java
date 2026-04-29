package feuille.module.editor.assa;

import feuille.module.editor.assa.render.AGraphicElement;
import feuille.module.editor.assa.render.Char;
import feuille.module.editor.assa.render.Converter;
import feuille.util.DrawColor;
import feuille.util.assa.AssEventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class AssStylePreview extends JPanel {

    private static final int UNIT = 10;
    private String sentenceSample;
    private ASS ass;

    public AssStylePreview() {
        setDoubleBuffered(true);
        ass = new ASS();
        sentenceSample = "A beautiful world is a green one.";

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setResolution(
                        e.getComponent().getWidth(),
                        e.getComponent().getHeight()
                );
            }
        });
    }

    private void setResolution(int w, int h){
        ass.getInfos().setPlayResX(w);
        ass.getInfos().setPlayResY(h);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(DrawColor.dark_green.getColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(DrawColor.lime.getColor());
        boolean shift = true;
        for(int y=0; y<getHeight(); y+=UNIT){
            for(int x=0; x<getWidth(); x+=UNIT*2){
                g.fillRect(shift ? x + UNIT : x, y, UNIT, UNIT);
            }
            shift = !shift;
        }

        if(ass.getEvents().isEmpty()) return;

        // Foreground
        BufferedImage image = doImage(
                ass.getInfos().getPlayResX(),
                ass.getInfos().getPlayResY()
        );

        g.drawImage(image, 0, 0, null);
    }

    public String getSentenceSample() {
        return sentenceSample;
    }

    public void setSentenceSample(AssStyle style, String sentenceSample) {
        this.sentenceSample = sentenceSample;
        if(!sentenceSample.isEmpty() && style != null){
            ass = assOf(style, sentenceSample);
            repaint();
        }
    }

    private BufferedImage doImage(int width, int height){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );

        AssEvent event = ass.getEvents().getFirst();

        Converter converter = Converter.createShapes(event, ass);
        // Calculation
        double sentenceWidth = 0d;
        double sentenceHeight = 0d;
        for(AGraphicElement element : converter.getElements()){
            if(element instanceof Char c){
                sentenceWidth += c.getAdvance();
                sentenceWidth += c.getExtraSpacing();
                sentenceHeight = Math.max(sentenceHeight, c.getHeight());
            }
        }

        // Position
        AffineTransform tr = new AffineTransform();
        tr.translate(
                (getWidth() - sentenceWidth) / 2d, // Center X
                ((getHeight() - sentenceHeight) / 2d) + sentenceHeight // Center Y
        );
        g.setTransform(tr);

        // Draw
        for(AGraphicElement element : converter.getElements()){
            if(element instanceof Char c){
                // Shadow
                // Outline
                g.setColor(event.getStyle().getOutlineColor().getColor());
                c.draw(g);
                // Text
                g.setColor(event.getStyle().getTextColor().getColor());
                c.fill(g);
                tr.translate(c.getAdvance() + c.getExtraSpacing(), 0d);
                g.setTransform(tr);
            }
        }

        g.dispose();
        return image;
    }

    private static ASS assOf(AssStyle style, String sentence){
        ASS ass = new ASS();

        AssEvent event = new AssEvent();
        event.setStart(new AssTime(0L));
        event.setEnd(new AssTime(1000L));
        event.setStyle(style);
        event.setType(AssEventType.Dialogue);
        event.setText(sentence);

        ass.getStyles().add(style);
        ass.getEvents().add(event);

        return ass;
    }
}
