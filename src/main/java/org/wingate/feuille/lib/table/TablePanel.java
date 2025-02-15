package org.wingate.feuille.lib.table;

import org.wingate.feuille.ctrl.ElementsComboBox;
import org.wingate.feuille.ctrl.FlagVersion;
import org.wingate.feuille.ctrl.LockFormatTextField;
import org.wingate.feuille.subs.ass.*;
import org.wingate.feuille.util.DrawColor;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;
import org.wingate.feuille.util.Waveform;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class TablePanel extends JPanel {

    private int w;
    private int h;

    private final ISO_3166 iso;

    // The need for translations object is handled by flagButtons object

    // =================================================================================================================
    // == MAIN CONTROLS
    // =================================================================================================================
    private final JSplitPane ver01;
    private final JSplitPane ver02;
    private final JSplitPane ver03;
    private final JSplitPane hor01;

    private final JPanel panVideo;
    private final JPanel panAudio;
    private final JPanel panEdit;
    private final JPanel panTable;
    private final JPanel panPeers;
    // -----------------------------------------------------------------------------------------------------------------

    // =================================================================================================================
    // == EDITING CONTROLS
    // =================================================================================================================
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
    private final JButton btnReplaceSelvent = new JButton();
    private final JButton btnAddEventBefore = new JButton();
    private final JButton btnAddEventAfter = new JButton();
    private FlagVersion flagVersion;
    // -----------------------------------------------------------------------------------------------------------------

    // =================================================================================================================
    // == TABLE CONTROLS
    // =================================================================================================================
    private final JTable tableTable = new JTable();
    private AssTableModel3 tableTableModel = null;
    private final JScrollPane scrollTable = new JScrollPane();
    // -----------------------------------------------------------------------------------------------------------------

    private final Waveform waveform = new Waveform();

    public TablePanel(int w, int h, ISO_3166 iso){
        this.iso = iso;

        this.w = w;
        this.h = h;

        ver01 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        ver02 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        ver03 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        hor01 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        ver01.setOneTouchExpandable(true);
        ver02.setOneTouchExpandable(true);
        ver03.setOneTouchExpandable(true);
        hor01.setOneTouchExpandable(true);

        ver01.setDividerSize(10);
        ver02.setDividerSize(10);
        ver03.setDividerSize(10);
        hor01.setDividerSize(10);

        panVideo = new JPanel(new BorderLayout());
        panAudio = new JPanel(new BorderLayout());
        panEdit = new JPanel(new BorderLayout());
        panTable = new JPanel(new BorderLayout());
        panPeers = new JPanel(new BorderLayout());

        panVideo.setBorder(new LineBorder(DrawColor.black.getColor(.1f)));
        panAudio.setBorder(new LineBorder(DrawColor.black.getColor(.1f)));
        panEdit.setBorder(new LineBorder(DrawColor.black.getColor(.1f)));
        panTable.setBorder(new LineBorder(DrawColor.black.getColor(.1f)));
        panPeers.setBorder(new LineBorder(DrawColor.black.getColor(.1f)));

        // +-------------+---------------+
        // | VIDEO       | AUDIO         |
        // |             +---------------+
        // |             | EDITING       |
        // +-------------+---------------+
        // | TABLE                       |
        // +-----------------------------+
        // | P2P                         |
        // +-----------------------------+

        setLayout(new BorderLayout());
        add(ver01, BorderLayout.CENTER); // Entre (VIDEO, AUDIO, EDITING) et (TABLE, P2P)
        ver01.setTopComponent(hor01); // ver01 >> (VIDEO, AUDIO, EDITING)
        ver01.setBottomComponent(ver02); // hor02 >> (TABLE, P2P)
        hor01.setLeftComponent(panVideo);
        hor01.setRightComponent(ver03); // hor03 divise AUDIO et EDITING
        ver03.setTopComponent(panAudio);
        ver03.setBottomComponent(panEdit);
        ver02.setTopComponent(panTable);
        ver02.setBottomComponent(panPeers);

        tableTableModel = new AssTableModel3(tableTable);

        makeVideo();
        makeAudio();
        makeEditing();
        makeTable();
        makePeers();
    }

    public void refreshDividers(){
        ver01.setDividerLocation(h / 2); // Divide main (VIDEO + AUDIO + EDITING vs TABLE + P2P)
        ver02.setDividerLocation(h / 3); // Divide bottom (TABLE vs P2P)
        ver03.setDividerLocation(h / 4); // Divide top (AUDIO vs EDITING)
        hor01.setDividerLocation(w / 2); // Divide top (VIDEO vs AUDIO + EDITING)
    }

    private void makeVideo(){

    }

    private void makeAudio(){
        JPanel panCommands = new JPanel(new FlowLayout());

        panAudio.add(waveform, BorderLayout.CENTER);
        panAudio.add(panCommands, BorderLayout.SOUTH);

        JButton btnOpenAudio = new JButton(
                Load.fromResource("/org/wingate/feuille/16 folder.png")
        );

        panCommands.add(btnOpenAudio);

        btnOpenAudio.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter(){
                final String[] extensions = new String[]{
                        ".wav", ".mp3", ".m4a", ".aac", ".mp2", ".ogg", ".oga", ".mka", ".wma", ".opus"
                };

                @Override
                public boolean accept(File f) {
                    if(f.isDirectory()) return true;
                    return Arrays.asList(extensions).contains(f.getName().substring(f.getName().lastIndexOf(".")));
                }

                @Override
                public String getDescription() {
                    return "FFMPEG audio files";
                }
            });
            int z = fc.showOpenDialog(new java.awt.Frame());
            if(z == JFileChooser.APPROVE_OPTION){
                waveform.setPath(fc.getSelectedFile().getAbsolutePath());
                waveform.setClassicMode(false);
                waveform.setTime(0d, 3d);
                waveform.repaint();
            }
        });
    }

    private void makeEditing(){
        JPanel panCommandsOne = new JPanel(new FlowLayout());
        JPanel panCommandsTwo = new JPanel(new FlowLayout());
        JSplitPane splitTextPanes = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JTextPane paneOrigin = new JTextPane();
        JTextPane paneTranslation = new JTextPane();
        flagVersion = new FlagVersion(iso, paneOrigin, paneTranslation);

        panEdit.add(panCommandsOne, BorderLayout.NORTH);
        panEdit.add(splitTextPanes, BorderLayout.CENTER);
        panEdit.add(panCommandsTwo, BorderLayout.SOUTH);

        panCommandsOne.setPreferredSize(new java.awt.Dimension(panEdit.getWidth(), 30));
        panCommandsTwo.setPreferredSize(new java.awt.Dimension(panEdit.getWidth(), 30));

        splitTextPanes.setTopComponent(paneOrigin);
        splitTextPanes.setBottomComponent(paneTranslation);

        paneOrigin.setBorder(new LineBorder(DrawColor.black.getColor(.05f)));
        paneTranslation.setBorder(new LineBorder(DrawColor.black.getColor(.05f)));

        paneOrigin.setPreferredSize(new java.awt.Dimension(panEdit.getWidth(), 50));

        spinEditLayer.setModel(spinEditModelLayer);
        spinEditLayer.setPreferredSize(new java.awt.Dimension(80, 22));
        spinEditML.setModel(spinEditModelML);
        spinEditML.setPreferredSize(new java.awt.Dimension(60, 22));
        spinEditMR.setModel(spinEditModelMR);
        spinEditMR.setPreferredSize(new java.awt.Dimension(60, 22));
        spinEditMV.setModel(spinEditModelMV);
        spinEditMV.setPreferredSize(new java.awt.Dimension(60, 22));

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

        btnAddEventQueue.setIcon(Load.fromResource("/org/wingate/feuille/16OK-custom-green.png"));
        btnReplaceSelvent.setIcon(Load.fromResource("/org/wingate/feuille/16OK-custom-orange.png"));
        btnAddEventBefore.setIcon(Load.fromResource("/org/wingate/feuille/16OK-custom-blue.png"));
        btnAddEventAfter.setIcon(Load.fromResource("/org/wingate/feuille/16OK-custom-violet.png"));

        btnAddEventQueue.setToolTipText(Load.language("btn_add-event-queue", "Add event to queue", iso));
        btnReplaceSelvent.setToolTipText(Load.language("btn_replace-event", "Replace the first selected event", iso));
        btnAddEventBefore.setToolTipText(Load.language("btn_add-event-before", "Add event before the first selected event", iso));
        btnAddEventAfter.setToolTipText(Load.language("btn_add-event-after", "Add event after the first selected event", iso));

        panCommandsTwo.add(btnAddEventQueue);
        panCommandsTwo.add(btnReplaceSelvent);
        panCommandsTwo.add(btnAddEventBefore);
        panCommandsTwo.add(btnAddEventAfter);
        panCommandsTwo.add(flagVersion);

        btnAddEventQueue.addActionListener(e -> {
            String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
            tableTableModel.addValue(createTextPaneEvent(text));
        });

        btnReplaceSelvent.addActionListener(e -> {
            if(tableTable.getSelectedRow() != -1){
                String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                tableTableModel.replaceValueAt(createTextPaneEvent(text), tableTable.getSelectedRow());
            }
        });

        btnAddEventBefore.addActionListener(e -> {
            if(tableTable.getSelectedRow() != -1){
                String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                tableTableModel.insertValueAt(createTextPaneEvent(text), tableTable.getSelectedRow());
            }
        });

        btnAddEventAfter.addActionListener(e -> {
            if(tableTable.getSelectedRow() != -1){
                String text = paneTranslation.getText().isEmpty() ? paneOrigin.getText() : paneTranslation.getText();
                if(tableTable.getSelectedRow() == tableTable.getRowCount() - 1){
                    tableTableModel.addValue(createTextPaneEvent(text));
                }else{
                    tableTableModel.insertValueAt(createTextPaneEvent(text), tableTable.getSelectedRow() + 1);
                }
            }
        });
    }

    private void makeTable(){
        tableTable.setModel(tableTableModel);
        tableTable.setRowHeight(30);
        tableTable.setRowMargin(2);
        scrollTable.setViewportView(tableTable);
        scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panTable.add(scrollTable, BorderLayout.CENTER);
        tableTableModel.updateColumnSize();
    }

    private void makePeers(){

    }

    private AssEvent createTextPaneEvent(String text){
        AssEvent event = new AssEvent();

        event.setType(cbEditComment.isSelected() ? AssEvent.Type.Comment : AssEvent.Type.Dialogue);
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
}
