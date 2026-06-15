package feuille.module.editor.assa.ui;

import feuille.module.audio.ui.SyncPane;
import feuille.module.editor.VoyagersTable;
import feuille.module.video.View;
import feuille.util.Exchange;

import javax.swing.*;
import java.awt.*;

public class AssEditor extends JPanel {

    private final Exchange exchange;

    private final SyncPane syncPane;
    private final VoyagersTable voyagersTable;
    private final EditorPanel editorPanel;
    private final View view;

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
        view = new View(exchange);

        exchange.setSyncPane(syncPane);
        exchange.setVoyagersTable(voyagersTable);
        exchange.setEditorPanel(editorPanel);
        exchange.setView(view);

        setLayout(new GridLayout(2, 1, 2, 2));
        // TOP: View + sync pane + editor
        // BOTTOM: table
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 2, 2));
        // in TOP: left >> view ; right >> sync pane + editor
        JPanel innerTopRightPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        topPanel.add(view);
        topPanel.add(innerTopRightPanel);
        innerTopRightPanel.add(syncPane);
        innerTopRightPanel.add(editorPanel);
        add(topPanel);
        add(voyagersTable);
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

    public View getView() {
        return view;
    }
}
