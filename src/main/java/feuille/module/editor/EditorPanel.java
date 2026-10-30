package feuille.module.editor;

import feuille.dialog.AssActorsDialog;
import feuille.dialog.AssEditorDialog;
import feuille.module.editor.assa.*;
import feuille.module.editor.voyager.*;
import feuille.util.*;
import feuille.util.assa.AssEventType;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorPanel extends JPanel {

    private final Exchange exchange;

    private final JTextPane paneOrigin = new JTextPane();
    private final JTextPane paneTranslation = new JTextPane();
    private final JCheckBox cbEditComment = new JCheckBox("#", false);
    private final ElementsComboBox<AssStyle> ecbEditStyles = new ElementsComboBox<>();
    private final ElementsComboBox<AssActor> ecbEditActors = new ElementsComboBox<>();
    private final ElementsComboBox<AssEffect> ecbEditEffects = new ElementsComboBox<>();
    private final JSpinner spinEditLayer = new JSpinner();
    private final SpinnerNumberModel spinEditModelLayer = new SpinnerNumberModel(0, 0, 1_000_000, 1);
    private final LockFormatTextField lockStart = new LockFormatTextField();
    private final LockFormatTextField lockEnd = new LockFormatTextField();
    private final LockFormatTextField lockDuration = new LockFormatTextField();
    private final JSpinner spinEditML = new JSpinner();
    private final SpinnerNumberModel spinEditModelML = new SpinnerNumberModel(0, 0, 1_000_000, 1);
    private final JSpinner spinEditMR = new JSpinner();
    private final SpinnerNumberModel spinEditModelMR = new SpinnerNumberModel(0, 0, 1_000_000, 1);
    private final JSpinner spinEditMV = new JSpinner();
    private final SpinnerNumberModel spinEditModelMV = new SpinnerNumberModel(0, 0, 1_000_000, 1);
    private final JButton btnAddEventQueue = new JButton();
    private final JButton btnReplaceSelEvent = new JButton();
    private final JButton btnAddEventBefore = new JButton();
    private final JButton btnAddEventAfter = new JButton();
    private final JButton btnDeleteEvent =  new JButton();
    private FlagVersion flagVersion;

    // TODO: ISO-3166 and translations tasks with and without flagVersion
    public EditorPanel(Exchange exchange) {
        this.exchange = exchange;

        exchange.setEditorPanel(this);

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createLineBorder(Color.gray));

        JPanel panCommandsOne = new JPanel(new FlowLayout());
        JPanel panCommandsTwo = new JPanel(new FlowLayout());
        JSplitPane splitTextPanes = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        flagVersion = new FlagVersion(
                exchange,
                ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()),
                paneOrigin,
                paneTranslation
        );

        paneOrigin.setComponentPopupMenu(createOriginPopupMenu());
        paneTranslation.setComponentPopupMenu(createTranslationPopupMenu());

        add(panCommandsOne, BorderLayout.NORTH);
        add(splitTextPanes, BorderLayout.CENTER);
        add(panCommandsTwo, BorderLayout.SOUTH);

        panCommandsOne.setPreferredSize(new Dimension(panCommandsOne.getWidth(), 30));
        panCommandsTwo.setPreferredSize(new Dimension(panCommandsTwo.getWidth(), 30));

        splitTextPanes.setTopComponent(paneOrigin);
        splitTextPanes.setBottomComponent(paneTranslation);

        paneOrigin.setPreferredSize(new Dimension(paneOrigin.getWidth(), 50));

        spinEditLayer.setModel(spinEditModelLayer);
        spinEditLayer.setPreferredSize(new Dimension(80, 22));
        spinEditML.setModel(spinEditModelML);
        spinEditML.setPreferredSize(new Dimension(60, 22));
        spinEditMR.setModel(spinEditModelMR);
        spinEditMR.setPreferredSize(new Dimension(60, 22));
        spinEditMV.setModel(spinEditModelMV);
        spinEditMV.setPreferredSize(new Dimension(60, 22));

        ecbEditStyles.setImage(Loader.fromResource("/images/16-diamond.png", 16, 16));
        ecbEditActors.setImage(Loader.fromResource("/images/16-user-blue.png", 16, 16));
        ecbEditEffects.setImage(Loader.fromResource("/images/16-goldenstar.png", 16, 16));

        // TODO Do that carefully:
        ecbEditStyles.addObject(new AssStyle());
        ecbEditActors.addObject(new AssActor());
        ecbEditEffects.addObject(new AssEffect());

        panCommandsOne.add(cbEditComment);
        panCommandsOne.add(ecbEditStyles);
        panCommandsOne.add(ecbEditActors);
        panCommandsOne.add(ecbEditEffects);
        panCommandsOne.add(spinEditLayer);
        panCommandsOne.add(lockStart);
        panCommandsOne.add(lockEnd);
        panCommandsOne.add(lockDuration);
        panCommandsOne.add(spinEditML);
        panCommandsOne.add(spinEditMR);
        panCommandsOne.add(spinEditMV);

        btnAddEventQueue.setIcon(Loader.fromResource("/images/16OK-custom-green.png", 16, 16));
        btnReplaceSelEvent.setIcon(Loader.fromResource("/images/16OK-custom-orange.png", 16, 16));
        btnAddEventBefore.setIcon(Loader.fromResource("/images/16OK-custom-blue.png", 16, 16));
        btnAddEventAfter.setIcon(Loader.fromResource("/images/16OK-custom-violet.png", 16, 16));
        btnDeleteEvent.setIcon(Loader.fromResource("/images/16KO.png", 16, 16));

        btnAddEventQueue.setToolTipText(Loader.language("btnAddEventQueue", "Add"));
        btnReplaceSelEvent.setToolTipText(Loader.language("btnReplaceEvent", "Replace"));
        btnAddEventBefore.setToolTipText(Loader.language("btnAddEventBefore", "Add before"));
        btnAddEventAfter.setToolTipText(Loader.language("btnAddEventAfter", "Add after"));
        // TODO - Add tooltip for btnDeleteEvent

        panCommandsTwo.add(btnAddEventQueue);
        panCommandsTwo.add(btnReplaceSelEvent);
        panCommandsTwo.add(btnAddEventBefore);
        panCommandsTwo.add(btnAddEventAfter);
        panCommandsTwo.add(btnDeleteEvent);
        panCommandsTwo.add(flagVersion);

        ecbEditStyles.getButton().addActionListener((_)->{
//            StylesDialog dialog = new StylesDialog(new Frame());
//            dialog.showDialog(exchange.getAss().getStyles());
//            if(dialog.getDialogResult() == DialogResult.OK){
//                exchange.getAss().setStyles(dialog.getStyles());
//            }
            AssEditorDialog ae = new AssEditorDialog(exchange);
            ae.initAssStylesCollections();
            ae.setStyles(ecbEditStyles.getObjects());
            ae.showDialog(exchange.getMainFrame().getSettings());
            if(ae.getDialogResult() == DialogResult.OK){
                ae.replaceAssStylesCollections();
                ecbEditStyles.setObjects(ae.getStyles());
                exchange.getAss().setStyles(ae.getStyles());
            }
        });

        ecbEditActors.getButton().addActionListener((_)->{
            AssActorsDialog ac = new AssActorsDialog(exchange);
            ac.setActorsList(ecbEditActors.getObjects());
            ac.selectActor(ecbEditActors.getSelectedItem());
            ac.setCurrentStyle(exchange.getCurrentStyle());
            ac.showDialog(exchange.getMainFrame().getSettings());
            if(ac.getDialogResult() == DialogResult.OK){
                ecbEditActors.setObjects(ac.getActorsList());
                exchange.setCurrentStyle(ac.getCurrentStyle());
                exchange.getActors().addAll(ac.getActorsList());
            }
        });

        ecbEditEffects.getButton().addActionListener((_)->{
            // TODO
        });

        btnAddEventQueue.addActionListener(e -> {
            try{
                String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                exchange.addEvent(createTextPaneEvent(text), flagVersion.getFlag1(), flagVersion.getFlag2());
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        btnReplaceSelEvent.addActionListener(e -> {
            try{
                String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                exchange.replaceEvent(createTextPaneEvent(text), flagVersion.getFlag1(), flagVersion.getFlag2());
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        btnAddEventBefore.addActionListener(e -> {
            try{
                String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                exchange.beforeEvent(createTextPaneEvent(text), flagVersion.getFlag1(), flagVersion.getFlag2());
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        btnAddEventAfter.addActionListener(e -> {
            try{
                String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                exchange.afterEvent(createTextPaneEvent(text), flagVersion.getFlag1(), flagVersion.getFlag2());
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        btnDeleteEvent.addActionListener(e -> {
            try{
                exchange.deleteEvent(getFlag1(), getFlag2());
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });
    }

    private AssEvent createTextPaneEvent(String text){
        AssEvent event = new AssEvent();

        event.setType(cbEditComment.isSelected() ? AssEventType.Comment : AssEventType.Dialogue);
        event.setLayer(spinEditModelLayer.getNumber().intValue()); // layer
        event.setStart(lockStart.getAssTime()); // start
        event.setEnd(lockEnd.getAssTime()); // end
        event.setStyle(ecbEditStyles.getSelectedItem()); // style
        event.setName(ecbEditActors.getSelectedItem()); // name
        event.setMarginL(spinEditModelML.getNumber().intValue()); // marginL
        event.setMarginR(spinEditModelMR.getNumber().intValue()); // marginR
        event.setMarginV(spinEditModelMV.getNumber().intValue()); // marginV
        event.setEffect(ecbEditEffects.getSelectedItem()); // effect
        event.setText(text); // text

        return event;
    }

    public void setToLockStart(AssTime t){
        if(lockStart.isLock()) return;
        lockStart.setAssTime(t);
    }

    public void setToLockEnd(AssTime t){
        if(lockEnd.isLock()) return;
        lockEnd.setAssTime(t);
    }

    public void setToLockDuration(AssTime t){
        if(lockDuration.isLock()) return;
        lockDuration.setAssTime(t);
    }

    public void setToLockDuration(AssTime start, AssTime end){
        setToLockDuration(AssTime.getMsDuration(start, end));
    }

    public void addToPanel(Voyager voyager){
        cbEditComment.setSelected(voyager.getEvent().getType() == AssEventType.Comment);
        spinEditModelLayer.setValue(voyager.getEvent().getLayer());
        lockStart.setAssTime(voyager.getEvent().getStart());
        lockEnd.setAssTime(voyager.getEvent().getEnd());
        lockDuration.setAssTime(AssTime.getMsDuration(voyager.getEvent().getStart(), voyager.getEvent().getEnd()));
        ecbEditStyles.setSelected(voyager.getEvent().getStyle());
        ecbEditActors.setSelected(voyager.getEvent().getName());
        spinEditModelML.setValue(voyager.getEvent().getMarginL());
        spinEditModelMR.setValue(voyager.getEvent().getMarginR());
        spinEditModelMV.setValue(voyager.getEvent().getMarginV());
        ecbEditEffects.setSelected(voyager.getEvent().getEffect());
        paneOrigin.setText(voyager.getEvent().getText());
        if(getFlag1().getAlpha3().equals(getFlag2().getAlpha3())){
            paneTranslation.setText(voyager.getEvent().getText());
        }else{
            Voyager add = voyager;
            for(Voyager v : voyager.getVoyagers()){
                if(v.getLanguage().getAlpha3().equals(getFlag2().getAlpha3())){
                    add = v;
                    break;
                }
            }
            paneTranslation.setText(add.getEvent().getText());
        }
    }

    private JPopupMenu createOriginPopupMenu(){
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem mCut = new JMenuItem(
                Loader.language("io.cut", "Cut"),
                Loader.fromResource("/images/16-LucideCopyX.png", 16, 16));
        mCut.addActionListener((e) -> cut(paneOrigin));
        popupMenu.add(mCut);

        JMenuItem mCopy = new JMenuItem(
                Loader.language("io.copy", "Copy"),
                Loader.fromResource("/images/16-LucideCopy.png", 16, 16));
        mCopy.addActionListener((e)-> copy(paneOrigin));
        popupMenu.add(mCopy);

        JMenuItem mPaste = new JMenuItem(
                Loader.language("io.paste", "Paste"),
                Loader.fromResource("/images/16-LucideClipboardPaste.png", 16, 16));
        mPaste.addActionListener((e)-> paste(paneOrigin));
        popupMenu.add(mPaste);

        JMenuItem mDelete = new JMenuItem(
                Loader.language("io.delete", "Delete"),
                Loader.fromResource("/images/16-LucideDelete.png", 16, 16));
        mDelete.addActionListener((e)-> delete(paneOrigin));
        popupMenu.add(mDelete);

        popupMenu.add(new JPopupMenu.Separator());

        JMenu menuAdd = new JMenu(Loader.language("io.menu.add", "Add"));
        popupMenu.add(menuAdd);

        JMenuItem mBold = new JMenuItem(
                Loader.language("io.bold.add", "Add Bold"),
                Loader.fromResource("/images/16-bold-blue.png", 16, 16));
        mBold.addActionListener((e)-> bold(paneOrigin, true));
        menuAdd.add(mBold);

        JMenuItem mItalic = new JMenuItem(
                Loader.language("io.italic.add", "Add Italic"),
                Loader.fromResource("/images/16-italic-blue.png", 16, 16));
        mItalic.addActionListener((e)-> italic(paneOrigin, true));
        menuAdd.add(mItalic);

        JMenuItem mUnderline = new JMenuItem(
                Loader.language("io.underline.add", "Add Underline"),
                Loader.fromResource("/images/16-underline-blue.png", 16, 16));
        mUnderline.addActionListener((e)-> underline(paneOrigin, true));
        menuAdd.add(mUnderline);

        JMenuItem mStrikeOut = new JMenuItem(
                Loader.language("io.strikeOut.add", "Add StrikeOut"),
                Loader.fromResource("/images/16-strikeOut-blue.png", 16, 16));
        mStrikeOut.addActionListener((e)-> strikeOut(paneOrigin, true));
        menuAdd.add(mStrikeOut);

        JMenu menuRemove = new JMenu(Loader.language("io.menu.rem", "Remove"));
        popupMenu.add(menuRemove);

        JMenuItem mBold_r = new JMenuItem(
                Loader.language("io.bold.rem", "Remove Bold"),
                Loader.fromResource("/images/16-bold-red.png", 16, 16));
        mBold_r.addActionListener((e)-> bold(paneOrigin, false));
        menuRemove.add(mBold_r);

        JMenuItem mItalic_r = new JMenuItem(
                Loader.language("io.italic.rem", "Remove Italic"),
                Loader.fromResource("/images/16-italic-red.png", 16, 16));
        mItalic_r.addActionListener((e)-> italic(paneOrigin, false));
        menuRemove.add(mItalic_r);

        JMenuItem mUnderline_r = new JMenuItem(
                Loader.language("io.underline.rem", "Remove Underline"),
                Loader.fromResource("/images/16-underline-red.png", 16, 16));
        mUnderline_r.addActionListener((e)-> underline(paneOrigin, false));
        menuRemove.add(mUnderline_r);

        JMenuItem mStrikeOut_r = new JMenuItem(
                Loader.language("io.strikeOut.rem", "Remove StrikeOut"),
                Loader.fromResource("/images/16-strikeOut-red.png", 16, 16));
        mStrikeOut_r.addActionListener((e)-> strikeOut(paneOrigin, false));
        menuRemove.add(mStrikeOut_r);

        JMenu menuDefine = new JMenu(Loader.language("io.menu.def", "Define"));
        popupMenu.add(menuDefine);

        JMenuItem mTextColor = new JMenuItem(
                Loader.language("io.text.color", "Color the text"),
                Loader.fromResource("/images/16-colors-text.png", 16, 16));
        mTextColor.addActionListener((e)-> textColor(paneOrigin));
        menuDefine.add(mTextColor);

        JMenuItem mKaraokeColor = new JMenuItem(
                Loader.language("io.karaoke.color", "Color the karaoke"),
                Loader.fromResource("/images/16-colors-karaoke.png", 16, 16));
        mKaraokeColor.addActionListener((e)-> karaokeColor(paneOrigin));
        menuDefine.add(mKaraokeColor);

        JMenuItem mOutlineColor = new JMenuItem(
                Loader.language("io.outline.color", "Color the outline"),
                Loader.fromResource("/images/16-colors-outline.png", 16, 16));
        mOutlineColor.addActionListener((e)-> outlineColor(paneOrigin));
        menuDefine.add(mOutlineColor);

        JMenuItem mShadowColor = new JMenuItem(
                Loader.language("io.shadow.color", "Color the shadow"),
                Loader.fromResource("/images/16-colors-shadow.png", 16, 16));
        mShadowColor.addActionListener((e)-> shadowColor(paneOrigin));
        menuDefine.add(mShadowColor);

        return  popupMenu;
    }

    private JPopupMenu createTranslationPopupMenu(){
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem mCut = new JMenuItem(
                Loader.language("io.cut", "Cut"),
                Loader.fromResource("/images/16-LucideCopyX.png", 16, 16));
        mCut.addActionListener((e) -> cut(paneTranslation));
        popupMenu.add(mCut);

        JMenuItem mCopy = new JMenuItem(
                Loader.language("io.copy", "Copy"),
                Loader.fromResource("/images/16-LucideCopy.png", 16, 16));
        mCopy.addActionListener((e)-> copy(paneTranslation));
        popupMenu.add(mCopy);

        JMenuItem mPaste = new JMenuItem(
                Loader.language("io.paste", "Paste"),
                Loader.fromResource("/images/16-LucideClipboardPaste.png", 16, 16));
        mPaste.addActionListener((e)-> paste(paneTranslation));
        popupMenu.add(mPaste);

        JMenuItem mDelete = new JMenuItem(
                Loader.language("io.delete", "Delete"),
                Loader.fromResource("/images/16-LucideDelete.png", 16, 16));
        mDelete.addActionListener((e)-> delete(paneTranslation));
        popupMenu.add(mDelete);

        popupMenu.add(new JPopupMenu.Separator());

        JMenu menuAdd = new JMenu(Loader.language("io.menu.add", "Add"));
        popupMenu.add(menuAdd);

        JMenuItem mBold = new JMenuItem(
                Loader.language("io.bold.add", "Add Bold"),
                Loader.fromResource("/images/16-bold-blue.png", 16, 16));
        mBold.addActionListener((e)-> bold(paneTranslation, true));
        menuAdd.add(mBold);

        JMenuItem mItalic = new JMenuItem(
                Loader.language("io.italic.add", "Add Italic"),
                Loader.fromResource("/images/16-italic-blue.png", 16, 16));
        mItalic.addActionListener((e)-> italic(paneTranslation, true));
        menuAdd.add(mItalic);

        JMenuItem mUnderline = new JMenuItem(
                Loader.language("io.underline.add", "Add Underline"),
                Loader.fromResource("/images/16-underline-blue.png", 16, 16));
        mUnderline.addActionListener((e)-> underline(paneTranslation, true));
        menuAdd.add(mUnderline);

        JMenuItem mStrikeOut = new JMenuItem(
                Loader.language("io.strikeOut.add", "Add StrikeOut"),
                Loader.fromResource("/images/16-strikeOut-blue.png", 16, 16));
        mStrikeOut.addActionListener((e)-> strikeOut(paneTranslation, true));
        menuAdd.add(mStrikeOut);

        JMenu menuRemove = new JMenu(Loader.language("io.menu.rem", "Remove"));
        popupMenu.add(menuRemove);

        JMenuItem mBold_r = new JMenuItem(
                Loader.language("io.bold.rem", "Remove Bold"),
                Loader.fromResource("/images/16-bold-red.png", 16, 16));
        mBold_r.addActionListener((e)-> bold(paneTranslation, false));
        menuRemove.add(mBold_r);

        JMenuItem mItalic_r = new JMenuItem(
                Loader.language("io.italic.rem", "Remove Italic"),
                Loader.fromResource("/images/16-italic-red.png", 16, 16));
        mItalic_r.addActionListener((e)-> italic(paneTranslation, false));
        menuRemove.add(mItalic_r);

        JMenuItem mUnderline_r = new JMenuItem(
                Loader.language("io.underline.rem", "Remove Underline"),
                Loader.fromResource("/images/16-underline-red.png", 16, 16));
        mUnderline_r.addActionListener((e)-> underline(paneTranslation, false));
        menuRemove.add(mUnderline_r);

        JMenuItem mStrikeOut_r = new JMenuItem(
                Loader.language("io.strikeOut.rem", "Remove StrikeOut"),
                Loader.fromResource("/images/16-strikeOut-red.png", 16, 16));
        mStrikeOut_r.addActionListener((e)-> strikeOut(paneTranslation, false));
        menuRemove.add(mStrikeOut_r);

        JMenu menuDefine = new JMenu(Loader.language("io.menu.def", "Define"));
        popupMenu.add(menuDefine);

        JMenuItem mTextColor = new JMenuItem(
                Loader.language("io.text.color", "Color the text"),
                Loader.fromResource("/images/16-colors-text.png", 16, 16));
        mTextColor.addActionListener((e)-> textColor(paneTranslation));
        menuDefine.add(mTextColor);

        JMenuItem mKaraokeColor = new JMenuItem(
                Loader.language("io.karaoke.color", "Color the karaoke"),
                Loader.fromResource("/images/16-colors-karaoke.png", 16, 16));
        mKaraokeColor.addActionListener((e)-> karaokeColor(paneTranslation));
        menuDefine.add(mKaraokeColor);

        JMenuItem mOutlineColor = new JMenuItem(
                Loader.language("io.outline.color", "Color the outline"),
                Loader.fromResource("/images/16-colors-outline.png", 16, 16));
        mOutlineColor.addActionListener((e)-> outlineColor(paneTranslation));
        menuDefine.add(mOutlineColor);

        JMenuItem mShadowColor = new JMenuItem(
                Loader.language("io.shadow.color", "Color the shadow"),
                Loader.fromResource("/images/16-colors-shadow.png", 16, 16));
        mShadowColor.addActionListener((e)-> shadowColor(paneTranslation));
        menuDefine.add(mShadowColor);

        return  popupMenu;
    }

    private AssStyle styleFromEditor(){
        AssStyle style = new AssStyle();



        return style;
    }

    public ISO_3166 getFlag1(){
        return flagVersion.getFlag1();
    }

    public ISO_3166 getFlag2(){
        return flagVersion.getFlag2();
    }

    public boolean flag2HasMany(){
        return flagVersion.flag2HasMany();
    }

    private void cut(JTextPane textPane){
        try{
            int start = textPane.getSelectionStart();
            int end = textPane.getSelectionEnd();
            String text = textPane.getText();
            Clipboard.copyString(textPane.getSelectedText());
            textPane.setText(
                    (start > 0 ? text.substring(0, start) : "") +
                            (end < text.length() ? text.substring(end) : ""));
        }catch(Exception ex){
            Loader.dialogErr(ex.getLocalizedMessage());
        }
    }

    private void copy(JTextPane textPane){
        try{
            Clipboard.copyString(textPane.getSelectedText());
        }catch(Exception ex){
            Loader.dialogErr(ex.getLocalizedMessage());
        }
    }

    private void paste(JTextPane textPane){
        try{
            int start = textPane.getSelectionStart();
            int end = textPane.getSelectionEnd();
            String text = textPane.getText();
            textPane.setText(
                    (start > 0 ? text.substring(0, start) : "") +
                            Clipboard.pasteString() +
                            (end < text.length() ? text.substring(end) : ""));
        }catch(Exception ex){
            Loader.dialogErr(ex.getLocalizedMessage());
        }
    }

    private void delete(JTextPane textPane){
        try{
            int start = textPane.getSelectionStart();
            int end = textPane.getSelectionEnd();
            String text = textPane.getText();
            textPane.setText(
                    (start > 0 ? text.substring(0, start) : "") +
                            (end < text.length() ? text.substring(end) : ""));
        }catch(Exception ex){
            Loader.dialogErr(ex.getLocalizedMessage());
        }
    }

    private void insertOverrides(JTextPane textPane, String o1, String o2){
        try{
            int start = textPane.getSelectionStart();
            int end = textPane.getSelectionEnd();
            String text = textPane.getText();
            textPane.setText(
                    (start > 0 ? text.substring(0, start) : "") + "{"+o1+"}" +
                            textPane.getSelectedText() + "{"+o2+"}" +
                            (end < text.length() ? text.substring(end) : ""));
            String s = textPane.getText();
            s = s.replace("}{", "");
            s = s.replace("{}", "");
            textPane.setText(s);
        }catch(Exception ex){
            Loader.dialogErr(ex.getLocalizedMessage());
        }
    }

    private void removeOverrides(JTextPane textPane, String o1, String o2){
        try{
            String s = textPane.getText();
            int startSelection = textPane.getSelectionStart();
            StringBuilder sb = new StringBuilder();
            int start = s.lastIndexOf("{", startSelection);
            String before = start == 0 || start == -1 ? "" : s.substring(0, start);
            String after = start == 0 || start == -1 ? s : s.substring(start);
            Pattern p = Pattern.compile("(?<e1>[^{]*)(?<a1>\\{)(?<r1>[\\\\bius0-1]+)(?<a2>})(?<e2>[^{]*)(?<a3>\\{)(?<r2>[\\\\bius0-1]+)(?<a4>})(?<e3>.*)");
            Matcher m = p.matcher(after);
            if(m.find()){
                //System.out.println("e1: " + m.group("e1"));
                sb.append(m.group("e1"));
                sb.append("{");
                //System.out.println("r1: " + m.group("r1"));
                sb.append(m.group("r1").replace(o1, ""));
                sb.append("}");
                //System.out.println("e2: " + m.group("e2"));
                sb.append(m.group("e2"));
                sb.append("{");
                //System.out.println("r2: " + m.group("r2"));
                sb.append(m.group("r2").replace(o2, ""));
                sb.append("}");
                //System.out.println("e3: " + m.group("e3"));
                sb.append(m.group("e3"));
            }
            s = before + (sb.isEmpty() ? after : sb.toString());
            s = removeLoop(s);
            textPane.setText(s);
        }catch(Exception ex){
            Loader.dialogErr(ex.getLocalizedMessage());
        }
    }

    private String removeLoop(String text){
        String[] values = new String[]{"}{", "{}"};
        while(hasValues(text, values)){
            for(String v : values){
                text = text.replace(v, "");
            }
        }
        return text;
    }

    private boolean hasValues(String text, String[] values){
        for(String v : values){
            if(text.contains(v)){
                return true;
            }
        }
        return false;
    }

    private void bold(JTextPane textPane, boolean add){
        if(add){
            insertOverrides(textPane, "\\b1", "\\b0");
        }else{
            removeOverrides(textPane, "\\b1", "\\b0");
        }
    }

    private void italic(JTextPane textPane, boolean add){
        if(add){
            insertOverrides(textPane, "\\i1", "\\i0");
        }else{
            removeOverrides(textPane, "\\i1", "\\i0");
        }
    }

    private void underline(JTextPane textPane, boolean add){
        if(add){
            insertOverrides(textPane, "\\u1", "\\u0");
        }else{
            removeOverrides(textPane, "\\u1", "\\u0");
        }
    }

    private void strikeOut(JTextPane textPane, boolean add){
        if(add){
            insertOverrides(textPane, "\\s1", "\\s0");
        }else{
            removeOverrides(textPane, "\\s1", "\\s0");
        }
    }

    private void color(JTextPane textPane, int type){
        try{
            int start = textPane.getSelectionStart();
            int end = textPane.getSelectionEnd();
            String text = textPane.getText();
            Color color = JColorChooser.showDialog(this, "Choose a color", null);
            if(color != null){
                String bgr = String.format("%02X%02X%02X", color.getBlue(), color.getGreen(), color.getRed());
                textPane.setText(
                        (start > 0 ? text.substring(0, start) : "") + "{\\"+type+"c&H" + bgr + "&}" +
                                textPane.getSelectedText() +
                                (end < text.length() ? text.substring(end) : ""));
                String s = textPane.getText();
                s = s.replace("}{", "");
                s = s.replace("{}", "");
                textPane.setText(s);
            }
        }catch(Exception ex){
            Loader.dialogErr(ex.getLocalizedMessage());
        }
    }

    private void textColor(JTextPane textPane){
        color(textPane, 1);
    }

    private void karaokeColor(JTextPane textPane){
        color(textPane, 2);
    }

    private void outlineColor(JTextPane textPane){
        color(textPane, 3);
    }

    private void shadowColor(JTextPane textPane){
        color(textPane, 4);
    }


}
