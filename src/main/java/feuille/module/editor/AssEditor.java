package feuille.module.editor;

import feuille.module.audio.SyncPane;
import feuille.module.drawing.AssSketchpad;
import feuille.module.editor.base.voyager.VoyagersTable;
import feuille.module.video.ViewGst;
import feuille.io.Exchange;
import feuille.module.welcome.Welcome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class AssEditor extends JPanel {

    private final Exchange exchange;
    private final JLayeredPane editKaraPane;
    private final List<Component> editKaraPaneList;

    private final SyncPane syncPane;
    private final VoyagersTable voyagersTable;
    private final EditorPanel editorPanel;
    private final ViewGst viewGst;
    private final KaraokeDisplay karaokeDisplay;

    public AssEditor(Exchange exchange, int w, int h){
        this.exchange = exchange;
        setSize(w, h);

        // Bottom panel >> editor + inner
        JPanel bottomPanel = new JPanel(new BorderLayout());
        // Inner panel >> table + video
        JPanel innerLeftBottomPanel = new JPanel(new GridLayout(1, 2, 2, 2));

        syncPane = new SyncPane(exchange);
        voyagersTable = new VoyagersTable(exchange);
        editorPanel = new EditorPanel(exchange);
        viewGst = new ViewGst(exchange);
        karaokeDisplay = new KaraokeDisplay(exchange);

        exchange.setAssEditor(this);
        exchange.setSyncPane(syncPane);
        exchange.setVoyagersTable(voyagersTable);
        exchange.setEditorPanel(editorPanel);
        exchange.setViewGst(viewGst);
        exchange.setKaraokeDisplay(karaokeDisplay);

        // =============================================
        // JLayeredPane >> EditorPane or KaraokeDisplay
        // =============================================
        editKaraPane = new JLayeredPane();
        editKaraPane.setPreferredSize(new Dimension(w / 2, h / 4));
        editKaraPaneList = new ArrayList<>();
        // index 0 : 1
        editKaraPane.add(editorPanel, 0);
        editKaraPaneList.add(editorPanel);
        // index 1 : 2
        editKaraPane.add(karaokeDisplay, 1);
        editKaraPaneList.add(karaokeDisplay);

        setOnTop(editorPanel);
        // ---------------------------------------------

        setLayout(new GridLayout(2, 1, 2, 2));
        // TOP: View + sync pane + editor
        // BOTTOM: table
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 2, 2));
        // in TOP: left >> view ; right >> sync pane + editor
        JPanel innerTopRightPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        topPanel.add(viewGst);
        topPanel.add(innerTopRightPanel);
        innerTopRightPanel.add(syncPane);
        innerTopRightPanel.add(editKaraPane);
        add(topPanel);
        add(voyagersTable);

        editKaraPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                editorPanel.forceResize(e.getComponent().getWidth(), e.getComponent().getHeight());
                karaokeDisplay.forceResize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
    }

    public void forceResize(int w, int h) {
        setSize(w, h);
        updateUI();
    }

    // Used for:
    // - editorPanel
    // - karaokeDisplay
    public void setOnTop(Component c){
        int z = 1;
        for(Component component : editKaraPaneList){
            if(component.hashCode() == c.hashCode()){
                editKaraPane.setPosition(component, 0);
                component.setVisible(true);
            }else{
                editKaraPane.setPosition(component, z);
                component.setVisible(false);
                z++;
            }
        }
    }

    public SyncPane getSyncPane() {
        return syncPane;
    }

    public VoyagersTable getVoyagersTable() {
        return voyagersTable;
    }

    public EditorPanel getEditorPanel() {
        return editorPanel;
    }

    public ViewGst getViewGst() {
        return viewGst;
    }

    public KaraokeDisplay getKaraokeDisplay() {
        return karaokeDisplay;
    }
}
