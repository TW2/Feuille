package feuille.module.editor.assa.render;

import feuille.module.editor.assa.ASS;
import feuille.module.editor.assa.AssEvent;
import feuille.module.editor.assa.AssStyle;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

    private final List<AGraphicElement> elements;

    private Converter(AssEvent event){
        elements = new ArrayList<>();
    }

    public List<AGraphicElement> getElements() {
        return elements;
    }

    public static Converter createShapes(AssEvent event, ASS ass){
        Converter converter = new Converter(event);
        GraphicType type = converter.strContains("\\{\\\\p\\d+\\}", event.getText()) ?
                GraphicType.Drawing : GraphicType.Letter;

        BufferedImage image = new BufferedImage(10, 10,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();

        switch(type){
            case Letter -> {
                converter.elements.addAll(converter.getChars(g, event, ass));
                // To do directly while drawing :
                // outline, shadow, rotation, blur, shear, color + alpha,
                // fading, position, karaoke, origin, clip,
                // transform (others than already treated in boxing)
            }
            case Drawing -> {
                converter.elements.add(converter.getDrawing(event, ass));
            }
        }

        g.dispose();

        return converter;
    }

    private List<Char> getChars(Graphics2D g, AssEvent event, ASS ass){
        // Group all characters with their tags
        List<Char> chars = doDiv(event);

        // Original font and for all Chars -- treat b i u s fn fs r
        return doFontForEach(g, chars, event, ass);
    }

    private Drawing getDrawing(AssEvent event, ASS ass){
        GeneralPath gp = new GeneralPath();
        Drawing dr = null;
        Pattern p = Pattern.compile("\\{p(?<scale>\\d+)\\}(?<cm>[^\\{]+)");
        Matcher m = p.matcher(event.getText());

        if(m.find()){
            dr = new Drawing();

            float scale = 1f / Math.max(1, Integer.parseInt(m.group("scale")));

            String commands = m.group("cm");

            Pattern pp = Pattern.compile("(?<tag>[mnlbspc]*)\\s(?<x>\\d+.*\\d*)\\s(?<y>\\d+.*\\d*)");
            Matcher mm = pp.matcher(commands);

            String rem = "";
            final List<Point2D> points = new ArrayList<>();
            while(mm.find()){
                String tag = mm.group("tag");
                String com = tag.isEmpty() ? rem : tag;
                rem = com;
                switch(com){
                    case "m", "n" -> {
                        double x = Double.parseDouble(mm.group("x"));
                        double y = Double.parseDouble(mm.group("y"));
                        gp.moveTo(x, y);
                    }
                    case "l" -> {
                        double x = Double.parseDouble(mm.group("x"));
                        double y = Double.parseDouble(mm.group("y"));
                        gp.lineTo(x, y);
                    }
                    case "b" -> {
                        double x = Double.parseDouble(mm.group("x"));
                        double y = Double.parseDouble(mm.group("y"));
                        points.add(new Point2D.Double(x, y));
                        if(points.size() == 3){
                            gp.curveTo(
                                    points.getFirst().getX(), points.getFirst().getY(),
                                    points.get(1).getX(), points.get(1).getY(),
                                    points.getLast().getX(), points.getLast().getY()
                            );
                            points.clear();
                        }
                    }
                    case "s", "p" -> {
                        double x = Double.parseDouble(mm.group("x"));
                        double y = Double.parseDouble(mm.group("y"));
                        points.add(new Point2D.Double(x, y));
                    }
                    case "c" -> {
                        if(!points.isEmpty()){
                            BSplineCurve spline = new BSplineCurve(points.getFirst());
                            for(int i=1; i<points.size(); i++){
                                spline.addControlPoint(points.get(i));
                            }
                            for(BezierCurve bc : spline.extractAllBezierCurves()){
                                List<Point2D> pts = bc.getControlPoints();
                                // Point A
                                gp.moveTo(pts.getFirst().getX(), pts.getFirst().getY());
                                // ControlPoint CP1, CP2, Point B is basically bezier
                                gp.curveTo(
                                        pts.get(1).getX(), pts.get(1).getY(),
                                        pts.get(2).getX(), pts.get(2).getY(),
                                        pts.getLast().getX(), pts.getLast().getY()
                                );
                            }
                            points.clear();
                        }
                    }
                }
            }

            gp.closePath();

            AffineTransform transform = new AffineTransform();
            transform.scale(scale, scale);
            gp.transform(transform);

            dr.setShape(gp);
        }

        return dr;
    }

    // ----------------------------------------------------------------------------------

    private List<Char> doDiv(AssEvent event){
        final List<Char> chars = new ArrayList<>();

        StringBuilder remember = new StringBuilder();
        String str = event.getText().replace("}{", "");
        if(str.contains("{")){
            Pattern p = Pattern.compile("\\{([^\\}]+)\\}([^\\{]*)");
            Matcher m = p.matcher(str);
            while(m.find()){
                String[] letters = new String[0];
                if(!m.group(2).isEmpty()){
                    letters = new String[m.group(2).length()];
                    for(int i=0; i<m.group(2).length(); i++){
                        letters[i] = Character.toString(m.group(2).toCharArray()[i]);
                    }
                }
                if(!m.group(1).isEmpty() || (!remember.isEmpty())){
                    remember.append(m.group(1));
                }
                if(remember.toString().contains("\\r")){
                    int resetIndex = remember.indexOf("\\r");
                    remember = new StringBuilder(remember.substring(resetIndex));
                }
                for(Map<Tag, String> tags : Tag.getTagsFrom(remember.toString())){
                    if(Arrays.stream(letters).toList().isEmpty()){
                        Char c = new Char();
                        c.addTag(tags);
                        chars.add(c);
                    }else{
                        for(String s : letters){
                            Char c = new Char();
                            c.addTag(tags);
                            c.setCharacter(s);
                            chars.add(c);
                        }
                    }

                }
            }
        }else{
            char[] ca = str.toCharArray();
            for(char c : ca){
                Char cc = new Char();
                cc.setCharacter(Character.toString(c));
                chars.add(cc);
            }
        }

        return chars;
    }

    /**
     * Calculation of points in pixel and font size for ASS
     * @param chars a list of Char
     * @param event the event of the line
     * @return a list of char with true size for ASS pixel font size
     */
    private List<Char> doFontForEach(Graphics2D g, List<Char> chars, AssEvent event, ASS ass){
        // Main line -- getFont features : b i u s fn fs
        Font fontWithAttrs = getFont(g, event, ass);
        // Populate all chars with default font
        for(Char c : chars){
            c.setFont(fontWithAttrs);
        }

        // All chars treatment -- getFont features : b i u s fn fs r
        for (Char c : chars){
            Font fontWithAttrsTags = getFont(g, event, ass, c);
            c.setFont(fontWithAttrsTags);
        }

        // Rectangle boxing
        // Calculate real boxing -- treat fsc[_/x/y] fsp t(only fsc[_/x/y] fsp)
        // TODO t (animation)
        for (Char c : chars){
            TextLayout layout = new TextLayout(
                    c.getCharacter(), c.getFont(), g.getFontRenderContext()
            );
            c.setAdvance(layout.getAdvance());
            AffineTransform transform = new AffineTransform();
            for(Map<Tag, String> map : c.getTags()){
                for(Map.Entry<Tag, String> entry : map.entrySet()){
                    String s = entry.getValue();
                    switch(entry.getKey()){
                        case Tag.Scale -> {
                            if(!s.isEmpty()){
                                double sc = Double.parseDouble(s) / 100d;
                                transform.scale(sc, sc);
                            }
                        }
                        case Tag.ScaleX -> {
                            if(!s.isEmpty()){
                                double sc = Double.parseDouble(s) / 100d;
                                transform.scale(sc, 1d);
                            }
                        }
                        case Tag.ScaleY -> {
                            if(!s.isEmpty()){
                                double sc = Double.parseDouble(s) / 100d;
                                transform.scale(1d, sc);
                            }
                        }
                        case Tag.Spacing -> {
                            if(!s.isEmpty()){
                                c.setExtraSpacing(Double.parseDouble(s));
                            }
                        }
                    }
                }
            }
            c.setShape((GeneralPath) layout.getOutline(transform));
        }

        return chars;
    }

    //-------------------------------------------------------------------------------------

    /**
     * Get font with pixel ASS size, and parse b i u s fn fs r
     * @param g a graphics 2D object to calculate size in pixel
     * @param event the main event
     * @param ass the main ASS
     * @return a Font to apply
     */
    private Font getFont(Graphics2D g, AssEvent event, ASS ass){
        // =========
        // Size in pixel
        // =========
        Font notEval = event.getStyle().getAssFont().getFont();
        notEval = notEval.deriveFont(event.getStyle().getAssFont().getSize());

        Rectangle2D evaluation = new Rectangle2D.Double(
                0d,
                0d,
                ass.getInfos().getPlayResX() * 2.5,
                event.getStyle().getAssFont().getFont().getSize()
        );

        TextLayout layout = new TextLayout(
                event.getText(),
                notEval,
                g.getFontRenderContext()
        );

        float fontSizePoints = notEval.getSize2D();
        double evaluate = evaluation.getBounds2D().getHeight();
        double forced = layout.getAscent() + layout.getLeading() + layout.getDescent();
        while(evaluate < forced && fontSizePoints > 0f){
            fontSizePoints--;
            layout = new TextLayout(
                    event.getText(),
                    notEval.deriveFont(fontSizePoints),
                    g.getFontRenderContext()
            );
            forced = layout.getAscent() + layout.getLeading() + layout.getDescent();
        }

        // =========
        // Font
        // =========
        Map<TextAttribute, Object> attributes = new HashMap<>();
        // Font
        attributes.put(TextAttribute.FONT, event.getStyle().getAssFont().getFont());
        attributes.put(TextAttribute.FAMILY, notEval.getFamily());
        // Size Points to pixels
        attributes.put(TextAttribute.SIZE, fontSizePoints);
        // Bold / Plain
        attributes.put(TextAttribute.WEIGHT, event.getStyle().getAssFont().isBold() ?
                TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
        // Italic / Plain
        attributes.put(TextAttribute.POSTURE, event.getStyle().getAssFont().isItalic() ?
                TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        // Underline / Plain
        attributes.put(TextAttribute.UNDERLINE, event.getStyle().getAssFont().isUnderline() ?
                TextAttribute.UNDERLINE_ON : -1);
        // StrikeOut / Plain
        attributes.put(TextAttribute.STRIKETHROUGH, event.getStyle().getAssFont().isStrikeout() ?
                TextAttribute.STRIKETHROUGH_ON : false);

        // Kerning
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        // Ligatures
        attributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);

        return new Font(attributes);
    }

    /**
     * Get font with pixel ASS size, and parse b i u s fn fs r
     * @param g a graphics 2D object to calculate size in pixel
     * @param event the main event
     * @param ass the main ASS
     * @param c the char
     * @return a Font to apply
     */
    private Font getFont(Graphics2D g, AssEvent event, ASS ass, Char c){
        // =========
        // Char tags -- can change style : b i u s fn fs r
        // =========
        String strStyle = null;
        boolean bold = event.getStyle().getAssFont().isBold();
        boolean italic = event.getStyle().getAssFont().isItalic();
        boolean underline = event.getStyle().getAssFont().isUnderline();
        boolean strikeOut = event.getStyle().getAssFont().isStrikeout();
        String fontName = event.getStyle().getAssFont().getFont().getFontName();
        float fontPoints = event.getStyle().getAssFont().getFont().getSize2D();
        // Treat reset and others
        for(Map<Tag, String> map : c.getTags()){
            for(Map.Entry<Tag, String> entry : map.entrySet()){
                switch(entry.getKey()){
                    case Tag.Reset -> {
                        if(!entry.getValue().isEmpty()){
                            strStyle = entry.getValue();
                        }
                    }
                    case Tag.Bold -> {
                        if(Tag.valuesOf(entry) instanceof Boolean value){
                            bold = value;
                        }
                    }
                    case Tag.Italic -> {
                        if(Tag.valuesOf(entry) instanceof Boolean value){
                            italic = value;
                        }
                    }
                    case Tag.Underline -> {
                        if(Tag.valuesOf(entry) instanceof Boolean value){
                            underline = value;
                        }
                    }
                    case Tag.StrikeOut -> {
                        if(Tag.valuesOf(entry) instanceof Boolean value){
                            strikeOut = value;
                        }
                    }
                    case Tag.FontName -> {
                        if(!entry.getValue().isEmpty()){
                            fontName = entry.getValue();
                        }
                    }
                    case Tag.FontSize -> {
                        if(Tag.valuesOf(entry) instanceof Float value){
                            fontPoints = value;
                        }
                    }
                }
            }
        }

        // =========
        // Style
        // =========
        AssStyle style = event.getStyle();
        if(strStyle != null){
            for(AssStyle s : ass.getStyles()){
                if(s.getName().equals(strStyle)){
                    style = s;
                    break;
                }
            }
        }

        // =========
        // Size in pixel
        // =========
        int iFontStyle = Font.PLAIN;
        if(bold) iFontStyle += Font.BOLD;
        if(italic) iFontStyle += Font.ITALIC;
        Font notEval = new Font(fontName, iFontStyle, style.getAssFont().getFont().getSize());
        notEval = notEval.deriveFont(fontPoints);

        Rectangle2D evaluation = new Rectangle2D.Double(
                0d,
                0d,
                ass.getInfos().getPlayResX() * 2.5,
                fontPoints // pixels
        );

        TextLayout layout = new TextLayout(
                event.getText(),
                notEval,
                g.getFontRenderContext()
        );

        float fontSizePoints = notEval.getSize2D();
        double evaluate = evaluation.getBounds2D().getHeight();
        double forced = layout.getAscent() + layout.getLeading() + layout.getDescent();
        while(evaluate < forced && fontSizePoints > 0f){
            fontSizePoints--;
            layout = new TextLayout(
                    event.getText(),
                    notEval.deriveFont(fontSizePoints),
                    g.getFontRenderContext()
            );
            forced = layout.getAscent() + layout.getLeading() + layout.getDescent();
        }

        // =========
        // Font
        // =========
        Map<TextAttribute, Object> attributes = new HashMap<>();
        // Font
        attributes.put(TextAttribute.FONT, style.getAssFont().getFont());
        attributes.put(TextAttribute.FAMILY, notEval.getFamily());
        // Size Points to pixels
        attributes.put(TextAttribute.SIZE, fontSizePoints);
        // Bold / Plain
        attributes.put(TextAttribute.WEIGHT, bold ?
                TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
        // Italic / Plain
        attributes.put(TextAttribute.POSTURE, italic ?
                TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        // Underline / Plain
        attributes.put(TextAttribute.UNDERLINE, underline ?
                TextAttribute.UNDERLINE_ON : -1);
        // StrikeOut / Plain
        attributes.put(TextAttribute.STRIKETHROUGH, strikeOut ?
                TextAttribute.STRIKETHROUGH_ON : false);

        // Kerning
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        // Ligatures
        attributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);

        return new Font(attributes);
    }

    private boolean strContains(String regex, String sample){
        return Pattern.compile(regex).matcher(sample).find();
    }

}
