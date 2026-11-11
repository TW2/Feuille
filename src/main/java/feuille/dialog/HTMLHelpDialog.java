package feuille.dialog;

import feuille.FeuilleFrame;
import feuille.io.Exchange;
import feuille.util.DialogResult;
import feuille.util.ISO_3166;
import feuille.util.Loader;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class HTMLHelpDialog extends JDialog {

    private final Exchange exchange;
    private DialogResult dialogResult;

    private final JFXPanel fxPanel;
    private WebEngine engine;

    public HTMLHelpDialog(Exchange exchange){
        super(exchange.getMainFrame());
        this.exchange = exchange;

        setTitle(Loader.language("dialog.help", "Help"));
        setModal(true);
        setResizable(false);
        setSize(600, 500);

        dialogResult = DialogResult.None;

        JPanel contentPane = new JPanel(new BorderLayout());

        JButton btnClose = new JButton(Loader.language("dialog.close", "Close"));
        btnClose.addActionListener(this::btnCloseActionPerformed);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnClose);

        fxPanel = new JFXPanel();

        Platform.runLater(() -> initFX(fxPanel));

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(btnClose);

        contentPane.add(fxPanel, BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.SOUTH);
    }

    public void showDialog(String htmlFileWithoutLocaleAndExtension) {
        boolean result = displayWelcomeDocument(htmlFileWithoutLocaleAndExtension);
        if(!result) return;
        setLocationRelativeTo(exchange.getMainFrame());
        setVisible(true);
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }

    public void btnCloseActionPerformed(ActionEvent e) {
        dialogResult = DialogResult.OK;
        setVisible(false);
        dispose();
    }

    private void initFX(final JFXPanel fxPanel) {
        Group group = new Group();
        Scene scene = new Scene(group);
        fxPanel.setScene(scene);

        WebView view = new WebView();
        view.setPrefSize(600, 410);

        engine = view.getEngine();

        group.getChildren().add(view);
    }

    private void setContent(String content) {
        Platform.runLater(() -> engine.loadContent(content));
    }

    private boolean displayWelcomeDocument(String nameWithoutLanguage){
        boolean result = false;
        try{
            String html = nameWithoutLanguage;
            switch(ISO_3166.getISO_3166(Locale.getDefault().getISO3Country())){
                case Spain -> html += ".es";
                case France -> html += ".fr";
                case Italy -> html += ".it";
                case Japan -> html += ".jp";
                default -> html += ".en";
            }
            File file = new File(Objects.requireNonNull(
                    FeuilleFrame.class.getResource("/help/" + html + ".html")).getPath());
            if(!file.exists()) return false;
            try(BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                StringBuilder document = new StringBuilder();
                while((line = br.readLine()) != null){
                    document.append(line).append("\n");
                }
                setContent(document.toString());
                result = true;
            } catch (IOException e) {
                Loader.consoleErr(e.getMessage());
            }
        }catch(Exception _){

        }
        return result;
    }
}
