package feuille.module.editor.assa.ui;

import feuille.module.audio.ui.SyncPane;
import feuille.module.editor.VoyagersTable;
import feuille.util.Exchange;

import javax.swing.*;
import java.awt.*;

public class AssEditor extends JPanel {

    private final Exchange exchange;

    private final SyncPane syncPane;
    private final VoyagersTable voyagersTable;
    private final EditorPanel editorPanel;

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

        exchange.setSyncPane(syncPane);
        exchange.setVoyagersTable(voyagersTable);
        exchange.setEditorPanel(editorPanel);

        setLayout(new BorderLayout());
        add(syncPane, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
        bottomPanel.add(editorPanel, BorderLayout.NORTH);
        bottomPanel.add(innerLeftBottomPanel, BorderLayout.CENTER);
        innerLeftBottomPanel.add(voyagersTable);

        syncPane.setPreferredSize(new Dimension(w, h / 4));
        editorPanel.setPreferredSize(new Dimension(w, (h * 3 / 4) * 2 / 9));
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
}
