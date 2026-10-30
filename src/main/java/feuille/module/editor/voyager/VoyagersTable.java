package feuille.module.editor.voyager;

import feuille.io.assa.ASS;
import feuille.io.assa.AssEvent;
import feuille.io.assa.AssTime;
import feuille.util.*;
import feuille.io.assa.AssActorType;
import feuille.io.assa.AssEventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class VoyagersTable extends JPanel {

    public enum Stripped {
        Off, Partially, On;
    }

    private Stripped stripped = Stripped.Partially;

    private Exchange exchange;

    private ASS ass;
    private final List<Voyager> voyagers;
    private final JScrollBar scrollBar;
    private int vBarOffset;
    private int pressedAt = -1;
    private int releasedAt = -1;

    private final ImageIcon iBoy;
    private final ImageIcon iGirl;
    private final ImageIcon iMan;
    private final ImageIcon iWoman;
    private final ImageIcon iNarrator;
    private final ImageIcon iRobot;
    private final ImageIcon iUnknown;

    public VoyagersTable(Exchange exchange) {
        this.exchange = exchange;
        setDoubleBuffered(true);
        ass = new ASS();
        voyagers = new ArrayList<>();
        scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        scrollBar.setMinimum(0);
        scrollBar.setMaximum(10000);
        vBarOffset = 0;

        setComponentPopupMenu(createPopupMenu());

        setLayout( new BorderLayout());
        add(scrollBar, BorderLayout.EAST);

        iBoy = Loader.fromResource("/images/16-k-boy.png", 16, 16);
        iGirl = Loader.fromResource("/images/16-k-girl.png", 16, 16);
        iMan = Loader.fromResource("/images/16-k-man.png", 16, 16);
        iWoman = Loader.fromResource("/images/16-k-woman.png", 16, 16);
        iNarrator = Loader.fromResource("/images/16-k-narrator.png", 16, 16);
        iRobot = Loader.fromResource("/images/16-k-robot.png", 16, 16);
        iUnknown = Loader.fromResource("/images/16-k-unknown.png", 16, 16);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getButton() == MouseEvent.BUTTON1) {
                    pressedAt = -1;
                    releasedAt = -1;
                    int row = getRowAt(e.getY());
                    if(e.getY() > 20 && row < voyagers.size()) {
                        switch(e.getClickCount()){
                            case 1 -> {
                                if(e.getX() < 20){
                                    boolean value = voyagers.get(row).isCollapsed();
                                    voyagers.get(row).setCollapsed(!value);
                                }else{
                                    for(Voyager v : voyagers) {
                                        if(v == null) continue;
                                        v.setSelected(false);
                                    }
                                    voyagers.get(row).setSelected(true);
                                    exchange.setEditorEvent(voyagers.get(row));
                                }
                                repaint();
                            }
                            case 2 -> {

                            }
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                pressedAt = getRowAt(e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                releasedAt = getRowAt(e.getY());
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int hMax = getVoyagerTotalHeight();
                int thickness = 20;
                if(e.getWheelRotation() < 0) {
                    vBarOffset = Math.min(vBarOffset + thickness, 0);
                }else if(e.getWheelRotation() > 0) {
                    vBarOffset = Math.max(vBarOffset - thickness, -(hMax - getHeight() + thickness * 2));
                }
                if(vBarOffset != scrollBar.getValue() && !voyagers.isEmpty()){
                    scrollBar.setValue(-(vBarOffset / thickness));
                }
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                try{
                    if(pressedAt != -1){
                        voyagers.get(getRowAt(e.getY())).setSelected(true);
                        repaint();
                    }
                }catch(Exception _){

                }
            }
        });

        scrollBar.addAdjustmentListener(e -> {
            int thickness = 20;
            vBarOffset = -(e.getValue() * thickness);
            if(vBarOffset != scrollBar.getValue() && !voyagers.isEmpty()){
                scrollBar.setValue(-(vBarOffset / thickness));
            }
            repaint();
        });
    }

    public List<Voyager> getVoyagers() {
        return voyagers;
    }

    private int getRowAt(int yCursor){
        int row = 0, y = vBarOffset + 20;
        for(Voyager v : voyagers) {
            y += v.isCollapsed() ? 20 : 40 + v.getVoyagers().size() * 20;
            if(y < yCursor) {
                row += 1;
            }else{
                break;
            }
        }
        return row;
    }

    private int getVoyagerTotalHeight(){
        int totalHeight = 0;
        for(Voyager voyager : voyagers) {
            if(voyager != null) {
                totalHeight += voyager.isCollapsed() ? 20 : 40 + voyager.getVoyagers().size() * 20;
            }
        }
        return totalHeight;
    }

    public void updateVerticalScrollBar(){
        scrollBar.setMinimum(0);
        scrollBar.setMaximum(Math.max(0, getVoyagerTotalHeight() / 20));
        //System.out.printf("r(%d) -(%d) +(%d)\n", rows.size(), scrollBar.getMinimum(), scrollBar.getMaximum());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //------------------------------------------------------------
        int offset = vBarOffset > 0 ? -vBarOffset : vBarOffset;

        // Reset
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw Voyagers
        int y = 20 + offset;
        int counter = 0;

        // The background
        for (Voyager voyager : voyagers) {
            if(voyager != null) {
                int h = voyager.isCollapsed() ? 20 : 40 + voyager.getVoyagers().size() * 20;
                if(h >= vBarOffset && vBarOffset < h + getHeight()) {
                    g2d.setColor(
                            voyager.isVisible() ?
                                    voyager.isSelected() ?
                                            DrawColor.blue.getColor(.2f) : UIManager.getColor("TextPane.background")
                                    : DrawColor.light_gray.getColor(.2f)
                    );
                    g2d.fillRect(0, y, getWidth(), h);
                }
                y += h;
            }
        }

        y = 20 + offset;

        // The lines
        for (Voyager voyager : voyagers) {
            if(voyager != null) {
                int h = voyager.isCollapsed() ? 20 : 40 + voyager.getVoyagers().size() * 20;
                if(h >= vBarOffset && vBarOffset < h + getHeight()) {
                    g2d.setColor(DrawColor.black.getColor(.5f));
                    g2d.drawLine(0, y + h, getWidth(), y + h);
                }
                y += h;
            }
        }

        y = 20 + offset;

        FontMetrics fontMetrics = g2d.getFontMetrics(g2d.getFont());

        // Header
        String plus_minus = Loader.language("voyager.plus.minus", "+/-");
        String line = Loader.language("voyager.line", "Line");
        String bookmark = Loader.language("voyager.bookmark", "Bookmark");
        String layer = Loader.language("voyager.layer", "Layer");
        String flag = Loader.language("voyager.flag", "Flag");
        String start = Loader.language("voyager.start", "Start");
        String end = Loader.language("voyager.end", "End");
        String style = Loader.language("voyager.style", "Style");
        String actor = Loader.language("voyager.actor", "Actor");
        String effect = Loader.language("voyager.effect", "Effect");
        String text = Loader.language("voyager.text", "Text");

        int sLine = fontMetrics.stringWidth(line);
        int sBookmark = fontMetrics.stringWidth(bookmark);
        int sLayer = fontMetrics.stringWidth(layer);
        int sFlag = fontMetrics.stringWidth(flag);
        int sStart = fontMetrics.stringWidth(start);
        int sEnd = fontMetrics.stringWidth(end);
        int sStyle = fontMetrics.stringWidth(style);
        int sActor = fontMetrics.stringWidth(actor);
        int sEffect = fontMetrics.stringWidth(effect);

        int lineCounter = 0;
        for(Voyager voyager : voyagers){
            if(voyager != null) {
                sLine = Math.max(sLine, fontMetrics.stringWidth(Integer.toString(lineCounter)));
                sBookmark = Math.max(sBookmark, voyager.getBookmarks().size() * 6);
                sLayer = Math.max(sLayer, fontMetrics.stringWidth(Integer.toString(voyager.getEvent().getLayer())));
                sFlag = Math.max(sFlag, Loader.locations(voyager.getLanguage()).getIconWidth());
                sStart = Math.max(sStart, fontMetrics.stringWidth(voyager.getEvent().getStart().toAss()));
                sEnd = Math.max(sEnd, fontMetrics.stringWidth(voyager.getEvent().getEnd().toAss()));
                sStyle = Math.max(sStyle, fontMetrics.stringWidth(voyager.getEvent().getStyle().getName()));
                sActor = Math.max(sActor, fontMetrics.stringWidth(voyager.getEvent().getName().getName()));
                sEffect = Math.max(sEffect, fontMetrics.stringWidth(voyager.getEvent().getEffect().getName()));

                sActor = Math.max(sActor, sEffect);
                sEffect = sActor;

                lineCounter++;
            }
        }

        sLine += 10;
        sBookmark += 10;
        sLayer += 10;
        sFlag += 10;
        sStart += 10;
        sEnd += 10;
        sStyle += 10;
        sActor += 10 + 40; // 20(16) kind + 20(16) color

        int amplify;

        // The data
        for (Voyager voyager : voyagers) {
            if(voyager != null) {
                counter++;
                amplify = 0;
                int h = voyager.isCollapsed() ? 20 : 40 + voyager.getVoyagers().size() * 20;
                if(h >= vBarOffset && vBarOffset < h + getHeight()) {
                    g2d.setColor(getForeground());
                    // +/-
                    Ellipse2D plusMinus = new Ellipse2D.Double(2, y + 2, 16, 16);
                    if(voyager.isCollapsed()) {
                        // Plus
                        if(!voyager.getVoyagers().isEmpty()) {
                            g2d.draw(plusMinus);
                            g2d.drawLine(6, y + h - 10, 14, y + h - 10);
                            g2d.drawLine(10, y + h - 14, 10, y + h - 6);
                        }
                    }else{
                        // Minus
                        g2d.draw(plusMinus);
                        g2d.drawLine(6, y + 10, 14, y + 10);
                    }
                    amplify += 27;
                    // Line number
                    g2d.drawString(String.valueOf(counter), amplify + 4, y + 16);
                    amplify += sLine;
                    // TODO: Bookmarks
                    amplify += sBookmark;
                    // Layer
                    g2d.drawString(String.format("%d", voyager.getEvent().getLayer()), amplify + 4, y + 16);
                    amplify += sLayer;
                    // Flag
                    try{
                        int wi, xi;
                        if(voyager.isCollapsed()) {
                            // Plus
                            ImageIcon i1 = Loader.locations(voyager.getLanguage());
                            wi = 18 * i1.getIconWidth() / i1.getIconHeight();
                            xi = (39 - wi) / 2;
                            g2d.drawImage(i1.getImage(), amplify + xi, y+1, wi, 18,null);
                        }else{
                            // Minus
                            ImageIcon i1 = Loader.locations(voyager.getLanguage());
                            wi = 18 * i1.getIconWidth() / i1.getIconHeight();
                            xi = (39 - wi) / 2;
                            g2d.drawImage(i1.getImage(), amplify + xi, y+1, wi, 18,null);
                            int ya = y + 40;
                            for(Voyager v : voyager.getVoyagers()) {
                                i1 = Loader.locations(v.getLanguage());
                                wi = 18 * i1.getIconWidth() / i1.getIconHeight();
                                xi = (39 - wi) / 2;
                                g2d.drawImage(i1.getImage(), amplify + xi, ya+1, wi, 18,null);
                                ya += 20;
                            }
                        }
                    }catch(Exception _){
                        // Wrong locale
                    }
                    amplify += sFlag;
                    // Start time in 0:00:00.00 format
                    g2d.drawString(voyager.getEvent().getStart().toAss(), amplify + 4, y + 16);
                    amplify += sStart;
                    // End time in 0:00:00.00 format
                    g2d.drawString(voyager.getEvent().getEnd().toAss(), amplify + 4, y + 16);
                    amplify += sEnd;
                    // Style
                    g2d.drawString(voyager.getEvent().getStyle().getName(), amplify + 4, y + 16);
                    amplify += sStyle;
                    // Actor
                    // Replaced value is 487
                    switch(voyager.getEvent().getName().getAssActorType()){
                        case AssActorType.Boy -> g2d.drawImage(iBoy.getImage(), amplify + 2, y + 2, null);
                        case AssActorType.Girl -> g2d.drawImage(iGirl.getImage(), amplify + 2, y + 2, null);
                        case AssActorType.Man -> g2d.drawImage(iMan.getImage(), amplify + 2, y + 2, null);
                        case AssActorType.Woman -> g2d.drawImage(iWoman.getImage(), amplify + 2, y + 2, null);
                        case AssActorType.Narrator -> g2d.drawImage(iNarrator.getImage(), amplify + 2, y + 2, null);
                        case AssActorType.Robot -> g2d.drawImage(iRobot.getImage(), amplify + 2, y + 2, null);
                        default -> g2d.drawImage(iUnknown.getImage(), amplify + 2, y + 2, null);
                    }
                    g2d.setColor(voyager.getEvent().getName().getColor().getColor());
                    g2d.fillRect(amplify + 22, y + 2, 16, 16);
                    g2d.setColor(getForeground());
                    g2d.drawRect(amplify + 22, y + 2, 16, 16);
                    g2d.drawString(voyager.getEvent().getName().getName(), amplify + 44, y + 16);
                    amplify += sActor;
                    // Text
                    // Replaced value is 567
                    if(voyager.isCollapsed()) {
                        // Plus
                        g2d.drawString(applyStrip(voyager.getEvent().getText()), amplify + 4, y + 16);
                    }else{
                        // Minus
                        g2d.drawString(applyStrip(voyager.getEvent().getText()), amplify + 4, y + 16);
                        int ya = y + 40;
                        for(Voyager v : voyager.getVoyagers()) {
                            g2d.drawString(applyStrip(v.getEvent().getText()), amplify + 4, ya + 16);
                            ya += 20;
                        }
                    }

                    amplify = 27 + sLine + sBookmark + sLayer + sFlag;
                    g2d.setColor(Color.gray);
                    if(!voyager.isCollapsed()) {
                        // Duration in 0:00:00.00 format
                        g2d.drawString(Loader.language("voyager.lines.duration", "Duration: "), amplify + 4, y + 20 + 16);
                        amplify += sStart;
                        // Duration
                        AssTime s = voyager.getEvent().getStart();
                        AssTime e = voyager.getEvent().getEnd();
                        String duration = AssTime.getAssDuration(s, e);
                        g2d.drawString(duration, amplify + 4, y + 20 + 16);
                        amplify += sEnd;
                        // Effect
                        // Replaced value is 487
                        g2d.drawString(Loader.language("voyager.lines.effect", "Effect: "), amplify + 4, y + 20 + 16);
                        amplify += sStyle;
                        g2d.drawString(voyager.getEvent().getEffect().getName(), amplify + 4, y + 20 + 16);
                        amplify += sActor;
                        // CPS
                        g2d.drawString(String.format("%.02f %s",
                                voyager.getEvent().getCPS(),
                                Loader.language("voyager.lines.cps", "cps")
                        ), amplify + 4, y + 20 + 16); // 567
                        amplify += 80;
                        // CPL
                        g2d.drawString(String.format("%d %s",
                                Math.round(voyager.getEvent().getCPL()),
                                Loader.language("voyager.lines.cpl", "cpl")
                        ), amplify + 4, y + 20 + 16); // 647 = 567 + 80
                        amplify += 80;
                        // ML, MR, MV
                        g2d.drawString(String.format("%s : %d %d %d",
                                Loader.language("voyager.lines.margins", "Margins"),
                                voyager.getEvent().getMarginL(),
                                voyager.getEvent().getMarginR(),
                                voyager.getEvent().getMarginV()
                        ), amplify + 4, y + 20 + 16); // 727 = 567 + 160
                    }
                }
                y += h;
            }
        }

        amplify = 27;

        // The header
        g2d.setColor(DrawColor.dark_blue.getColor());
        g2d.fillRect(0, 0, getWidth(), 20);
        g2d.setColor(Color.white);
        g2d.drawRect(0, 0, getWidth()-1, 20-1);
        // Collapse(-) or not(+)
        g2d.drawString(plus_minus, 4, 20 - 4);
        g2d.drawLine(25, 0, 25, 20-1);
        // Line
        g2d.drawString(line, amplify + 4, 20 - 4);
        g2d.drawLine(sLine + amplify, 0, sLine + amplify, 20-1);
        amplify += sLine;
        // Bookmark
        g2d.drawString(bookmark, amplify + 4, 20 - 4);
        g2d.drawLine(sBookmark + amplify, 0, sBookmark + amplify, 20-1);
        amplify += sBookmark;
        // Layer
        g2d.drawString(layer, amplify + 4, 20 - 4);
        g2d.drawLine(sLayer + amplify, 0, sLayer + amplify, 20-1);
        amplify += sLayer;
        // Flag
        g2d.drawString(flag, amplify + 4, 20 - 4);
        g2d.drawLine(sFlag + amplify, 0,  sFlag + amplify, 20-1);
        amplify += sFlag;
        // Start
        g2d.drawString(start, amplify + 4, 20 - 4);
        g2d.drawLine(sStart + amplify, 0, sStart + amplify, 20-1);
        amplify += sStart;
        // End
        g2d.drawString(end, amplify + 4, 20 - 4);
        g2d.drawLine(sEnd + amplify, 0, sEnd + amplify, 20-1);
        amplify += sEnd;
        // Style
        g2d.drawString(style, amplify + 4, 20 - 4);
        g2d.drawLine(sStyle + amplify, 0, sStyle + amplify, 20-1);
        amplify += sStyle;
        // Actor
        g2d.drawString(actor, amplify + 4, 20 - 4);
        g2d.drawLine(sActor + amplify, 0, sActor + amplify, 20-1);
        amplify += sActor;
        // Text
        g2d.drawString(text, amplify + 4, 20 - 4);

        //------------------------------------------------------------
        g2d.dispose();
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        // Cut, Copy, Paste, Delete, Duplicate
        JMenuItem mCut = new JMenuItem(Loader.language("io.cut", "Cut"));
        JMenuItem mCopy = new JMenuItem(Loader.language("io.copy", "Copy"));
        JMenuItem mPaste = new JMenuItem(Loader.language("io.paste", "Paste"));
        JMenuItem mDelete = new JMenuItem(Loader.language("io.delete", "Delete"));
        JMenuItem mDuplicate = new JMenuItem(Loader.language("io.duplicate", "Clone lines"));

        mCut.setIcon(Loader.fromResource("/images/16-LucideCopyX.png", 16, 16));
        mCopy.setIcon(Loader.fromResource("/images/16-LucideCopy.png", 16, 16));
        mPaste.setIcon(Loader.fromResource("/images/16-LucideClipboardPaste.png", 16, 16));
        mDelete.setIcon(Loader.fromResource("/images/16-LucideDelete.png", 16, 16));
        mDuplicate.setIcon(Loader.fromResource("/images/16-LucideBookCopy.png", 16, 16));

        mCut.addActionListener((e) -> {
            List<Integer> indices = getSelectedVoyagers();
            indices.sort(Integer::compareTo);
            if(indices.isEmpty()) return;
            // Copy
            copyVoyagers(indices, exchange.getFlag1(), exchange.getFlag2());
            // Delete
            deleteEvent(exchange.getFlag1(), exchange.getFlag2());
        });
        mCopy.addActionListener((e) -> {
            List<Integer> indices = getSelectedVoyagers();
            indices.sort(Integer::compareTo);
            if(indices.isEmpty()) return;
            // Copy
            copyVoyagers(indices, exchange.getFlag1(), exchange.getFlag2());
        });
        mPaste.addActionListener((e) -> {
            List<Integer> indices = getSelectedVoyagers();
            indices.sort(Integer::compareTo);
            if(indices.isEmpty()) return;
            // Paste (Delete and Paste)
            pasteVoyagers(indices.getFirst(), exchange.getFlag1(), exchange.getFlag2());
        });
        mDelete.addActionListener((e) -> {
            // Delete
            deleteEvent(exchange.getFlag1(), exchange.getFlag2());
        });
        mDuplicate.addActionListener((e) -> {
            List<Integer> indices = getSelectedVoyagers();
            indices.sort(Integer::compareTo);
            if(indices.isEmpty()) return;
            for(int i=indices.size()-1;i>=0;i--){
                beforeEvent(voyagers.get(indices.get(i)).getEvent(),
                        exchange.getFlag1(), exchange.getFlag2());
            }
            updateVerticalScrollBar();
            repaint();
        });

        popupMenu.add(mCut);
        popupMenu.add(mCopy);
        popupMenu.add(mPaste);
        popupMenu.add(mDelete);
        popupMenu.add(mDuplicate);


        return popupMenu;
    }

    private List<Integer> getSelectedVoyagers(){
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<voyagers.size();i++) {
            if(voyagers.get(i).isSelected()) {
                list.add(i);
            }
        }
        return list;
    }

    private String applyStrip(String s){
        String str = "";
        switch(stripped){
            case On -> {
                if(s.contains("{\\")){
                    try {
                        str = s.replaceAll("\\{[^}]+}", "");
                    } catch (Exception e) {
                        str = s;
                    }
                }else{
                    str = s;
                }
            }
            case Partially -> {
                if(s.contains("{\\")){
                    try {
                        String partiallyStrippedSymbol = "◆";
                        str = s.replaceAll("\\{[^}]+}", partiallyStrippedSymbol);
                    } catch (Exception e) {
                        str = s;
                    }
                }else{
                    str = s;
                }
            }
            case Off -> {
                str = s;
            }
        }
        return str;
    }

    public void addEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2) {
        if(f1.equals(f2)){
            voyagers.add(new Voyager(f1, event.getType() != AssEventType.Comment, event));
        }else{
            int index = -1;
            for(int i=0;i<voyagers.size();i++){
                Voyager voyager = voyagers.get(i);
                if(voyager.isSelected()){
                    boolean exists = false;
                    for(Voyager v : voyager.getVoyagers()){
                        if(v.getLanguage().getAlpha3().equals(f2.getAlpha3())){
                            exists = true;
                            break;
                        }
                    }
                    if(!exists){
                        index = i;
                        break;
                    }
                }
            }
            if(index != -1 && !voyagers.isEmpty()){
                voyagers.get(index).getVoyagers().add(new Voyager(f2, event.getType() != AssEventType.Comment, event));
            }
        }
        updateVerticalScrollBar();
        repaint();
    }

    public void replaceEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2) {
        int index = -1;
        for(int i=0;i<voyagers.size();i++){
            Voyager voyager = voyagers.get(i);
            if(voyager.isSelected()){
                index = i;
                break;
            }
        }
        if(index != -1){
            if(f1.equals(f2)){
                List<Voyager> doNotRemoveChildren = voyagers.get(index).getVoyagers();
                boolean isCollapsed = voyagers.get(index).isCollapsed();
                boolean isSelected = voyagers.get(index).isSelected();
                voyagers.remove(index);
                Voyager voyager = new Voyager(f1, event.getType() != AssEventType.Comment, event);
                voyager.setCollapsed(isCollapsed);
                voyager.setSelected(isSelected);
                voyager.getVoyagers().addAll(doNotRemoveChildren);
                voyagers.add(index, voyager);
            }else{
                int index2 = -1;
                for (Voyager v : voyagers) {
                    for (int j = 0; j < v.getVoyagers().size(); j++) {
                        ISO_3166 iso = v.getVoyagers().get(j).getLanguage();
                        if (iso.equals(f2) && v.isSelected()) {
                            index2 = j;
                            break;
                        }
                    }
                    if (index2 != -1) {
                        v.getVoyagers().remove(index2);
                        v.getVoyagers().add(index2, new Voyager(f2, event.getType() != AssEventType.Comment, event));
                        break;
                    }
                }
            }
            updateVerticalScrollBar();
            repaint();
        }
    }

    public void beforeEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2) {
        int index = -1;
        for(int i=0;i<voyagers.size();i++){
            Voyager voyager = voyagers.get(i);
            if(voyager.isSelected()){
                index = i;
                break;
            }
        }
        if(index != -1){
            if(f1.equals(f2)){
                voyagers.add(index, new Voyager(f1, event.getType() != AssEventType.Comment, event));
            }
            updateVerticalScrollBar();
            repaint();
        }
    }

    public void afterEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2) {
        try{
            if(f1.equals(f2)){
                int index = -1;
                for(int i=0;i<voyagers.size();i++){
                    Voyager voyager = voyagers.get(i);
                    if(voyager.isSelected()){
                        index = i;
                        break;
                    }
                }
                if(index != -1){
                    voyagers.add(index+1, new Voyager(f1, event.getType() != AssEventType.Comment, event));
                    updateVerticalScrollBar();
                    repaint();
                }
            }
        }catch(Exception _){
            addEvent(event, f1, f2);
        }
    }

    public void setStripped(Stripped stripped) {
        this.stripped = stripped;
        repaint();
    }

    public void deleteEvent(ISO_3166 f1, ISO_3166 f2) {
        List<Integer> indices = new ArrayList<>();
        for(int i=0;i<voyagers.size();i++){
            Voyager voyager = voyagers.get(i);
            if(voyager.isSelected()){
                if(f1.getAlpha3().equals(f2.getAlpha3())) {
                    indices.add(i);
                }else{
                    int index = -1;
                    for(int j=0;j<voyager.getVoyagers().size();j++){
                        if(voyager.getVoyagers().get(j).getLanguage().getAlpha3().equals(f2.getAlpha3())){
                            index = j;
                            break;
                        }
                    }
                    if(index != -1){
                        voyager.getVoyagers().remove(index);
                    }
                }
            }
        }
        if(!indices.isEmpty()){
            for(int i=indices.size()-1;i>=0;i--){
                voyagers.remove(indices.get(i).intValue());
            }
        }
        updateVerticalScrollBar();
        repaint();
    }

    public void copyVoyagers(List<Integer> selected, ISO_3166 f1, ISO_3166 f2) {
        StringBuilder sb = new StringBuilder();
        if(f1.getAlpha3().equals(f2.getAlpha3())){
            for(Voyager voyager : voyagers){
                if(voyager.getLanguage().getAlpha3().equals(f1.getAlpha3())){
                    if(voyager.isSelected()){
                        sb.append(voyager.getEvent().toRawLine());
                        sb.append("\n");
                    }
                }
            }
        }else{
            for(Voyager voyager : voyagers){
                if(voyager.isSelected()){
                    for(Voyager v : voyager.getVoyagers()){
                        if(v.getLanguage().getAlpha3().equals(f2.getAlpha3())){
                            sb.append(v.getEvent().toRawLine());
                            sb.append("\n");
                        }
                    }
                }
            }
        }
        if(!sb.isEmpty()){
            Clipboard.copyString(sb.toString());
        }
    }

    public void pasteVoyagers(int row, ISO_3166 f1, ISO_3166 f2) {
        try(StringReader sr = new StringReader(Clipboard.pasteString());
            BufferedReader br = new BufferedReader(sr)
        ){
            voyagers.get(Math.min(voyagers.size()-1, row)).setSelected(true);
            String line;
            while((line = br.readLine()) != null){
                AssEvent event = AssEvent.createFromRawLine(line, exchange.getAss());
                if(f1.getAlpha3().equals(f2.getAlpha3())){
                    afterEvent(event, f1, f2);
                    deleteEvent(f1, f2);
                }else{
                    addEvent(event, f1, f2);
                    deleteEvent(f1, f2);
                }
            }
        }catch(Exception ex){
            Loader.dialogErr(ex.getLocalizedMessage());
        }
    }
}
