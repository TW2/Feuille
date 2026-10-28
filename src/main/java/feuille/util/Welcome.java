package feuille.util;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class Welcome extends JPanel {

    private final Exchange exchange;

    private final JFXPanel fxPanel;
    private WebEngine engine;

    public Welcome(Exchange exchange, int w, int h) {
        this.exchange = exchange;
        setSize(w, h);

        setBackground(DrawColor.dark_blue.getColor());
        setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(800, h - 70));
        add(panel);

        fxPanel = new JFXPanel();
        panel.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> initFX(fxPanel));
    }

    private void initFX(final JFXPanel fxPanel) {
        Group group = new Group();
        Scene scene = new Scene(group);
        fxPanel.setScene(scene);

        WebView view = new WebView();
        view.setPrefSize(800, fxPanel.getHeight());

        engine = view.getEngine();

        group.getChildren().add(view);
    }

    public void forceResize(int w, int h) {
        setSize(w, h);
        updateUI();
    }

    public void setContent(String content) {
        Platform.runLater(() -> engine.loadContent(content));
    }

    public void setURL(String url) { Platform.runLater(() -> engine.load(url)); }
}
