package feuille.module.editor.assa.ui;

import feuille.module.editor.*;
import feuille.module.editor.assa.*;
import feuille.util.*;
import feuille.util.assa.AssEventType;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Locale;

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

        JPanel panCommandsOne = new JPanel(new FlowLayout());
        JPanel panCommandsTwo = new JPanel(new FlowLayout());
        JSplitPane splitTextPanes = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        flagVersion = new FlagVersion(
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
            Path conf = Path.of(Path.of("").toAbsolutePath() + "\\conf\\conf.txt");
//            SettingsDialog set = new SettingsDialog(new Frame());
//            set.getTabbedPane().setSelectedIndex(1);
//            set.getStyleTabbedPane().setSelectedIndex(0);
//            set.showDialog(Settings.read(conf.toString()));
//
//            if(set.getDialogResult() == DialogResult.OK){
//                Settings.write(conf.toString(), set.getSettings());
//            }
            StylesDialog dialog = new StylesDialog(new Frame());
            dialog.showDialog(exchange.getAss().getStyles());
            if(dialog.getDialogResult() == DialogResult.OK){
                exchange.getAss().setStyles(dialog.getStyles());
            }
        });

//        ecbEditActors.getButton().addActionListener((_)->{
//            Path conf = Path.of(Path.of("").toAbsolutePath() + "\\conf\\conf.txt");
//            SettingsDialog set = new SettingsDialog(new Frame());
//            set.getTabbedPane().setSelectedIndex(2);
//            set.showDialog(Settings.read(conf.toString()));
//
//            if(set.getDialogResult() == DialogResult.OK){
//                Settings.write(conf.toString(), set.getSettings());
//            }
//        });

//        ecbEditEffects.getButton().addActionListener((_)->{
//            Path conf = Path.of(Path.of("").toAbsolutePath() + "\\conf\\conf.txt");
//            SettingsDialog set = new SettingsDialog(new Frame());
//            set.getTabbedPane().setSelectedIndex(3);
//            set.showDialog(Settings.read(conf.toString()));
//
//            if(set.getDialogResult() == DialogResult.OK){
//                Settings.write(conf.toString(), set.getSettings());
//            }
//        });

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
                Loader.language("control.cut", "Cut"),
                Loader.fromResource("/org/wingate/feuille/20px-Crystal_Clear_action_editcut.png", 20, 20));

        JMenuItem mCopy = new JMenuItem(
                Loader.language("control.copy", "Copy"),
                Loader.fromResource("/org/wingate/feuille/20px-Crystal_Clear_action_editcopy.png", 20, 20));

        JMenuItem mPaste = new JMenuItem(
                Loader.language("control.paste", "Paste"),
                Loader.fromResource("/org/wingate/feuille/20px-Crystal_Clear_action_editpaste.png", 20, 20));

        JMenuItem mDelete = new JMenuItem(
                Loader.language("control.delete", "Delete"),
                Loader.fromResource("/images/16KO.png", 16, 16));

        mCut.addActionListener((e)->{
            try{
                int start = paneOrigin.getSelectionStart();
                int end = paneOrigin.getSelectionEnd();
                String text = paneOrigin.getText();
                Clipboard.copyString(paneOrigin.getSelectedText());
                paneOrigin.setText(
                        (start > 0 ? text.substring(0, start) : "") +
                        (end < text.length() ? text.substring(end) : ""));
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        mCopy.addActionListener((e)->{
            try{
                Clipboard.copyString(paneOrigin.getSelectedText());
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        mPaste.addActionListener((e)->{
            try{
                int start = paneOrigin.getSelectionStart();
                int end = paneOrigin.getSelectionEnd();
                String text = paneOrigin.getText();
                paneOrigin.setText(
                        (start > 0 ? text.substring(0, start) : "") +
                        Clipboard.pasteString() +
                        (end < text.length() ? text.substring(end) : ""));
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        mDelete.addActionListener((e)->{
            try{
                int start = paneOrigin.getSelectionStart();
                int end = paneOrigin.getSelectionEnd();
                String text = paneOrigin.getText();
                paneOrigin.setText(
                        (start > 0 ? text.substring(0, start) : "") +
                        (end < text.length() ? text.substring(end) : ""));
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        popupMenu.add(mCut);
        popupMenu.add(mCopy);
        popupMenu.add(mPaste);
        popupMenu.add(mDelete);

        return  popupMenu;
    }

    private JPopupMenu createTranslationPopupMenu(){
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem mCut = new JMenuItem(
                Loader.language("control.cut", "Cut"),
                Loader.fromResource("/org/wingate/feuille/20px-Crystal_Clear_action_editcut.png", 20, 20));

        JMenuItem mCopy = new JMenuItem(
                Loader.language("control.copy", "Copy"),
                Loader.fromResource("/org/wingate/feuille/20px-Crystal_Clear_action_editcopy.png", 20, 20));

        JMenuItem mPaste = new JMenuItem(
                Loader.language("control.paste", "Paste"),
                Loader.fromResource("/org/wingate/feuille/20px-Crystal_Clear_action_editpaste.png", 20, 20));

        JMenuItem mDelete = new JMenuItem(
                Loader.language("control.delete", "Delete"),
                Loader.fromResource("/images/16KO.png", 16, 16));

        mCut.addActionListener((e)->{
            try{
                int start = paneTranslation.getSelectionStart();
                int end = paneTranslation.getSelectionEnd();
                String text = paneTranslation.getText();
                Clipboard.copyString(paneTranslation.getSelectedText());
                paneTranslation.setText(
                        (start > 0 ? text.substring(0, start) : "") +
                        (end < text.length() ? text.substring(end) : ""));
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        mCopy.addActionListener((e)->{
            try{
                Clipboard.copyString(paneTranslation.getSelectedText());
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        mPaste.addActionListener((e)->{
            try{
                int start = paneTranslation.getSelectionStart();
                int end = paneTranslation.getSelectionEnd();
                String text = paneTranslation.getText();
                paneTranslation.setText(
                        (start > 0 ? text.substring(0, start) : "") +
                        Clipboard.pasteString() +
                        (end < text.length() ? text.substring(end) : ""));
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        mDelete.addActionListener((e)->{
            try{
                int start = paneTranslation.getSelectionStart();
                int end = paneTranslation.getSelectionEnd();
                String text = paneTranslation.getText();
                paneTranslation.setText(
                        (start > 0 ? text.substring(0, start) : "") +
                        (end < text.length() ? text.substring(end) : ""));
            }catch(Exception ex){
                Loader.dialogErr(ex.getLocalizedMessage());
            }
        });

        popupMenu.add(mCut);
        popupMenu.add(mCopy);
        popupMenu.add(mPaste);
        popupMenu.add(mDelete);

        return  popupMenu;
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
}
