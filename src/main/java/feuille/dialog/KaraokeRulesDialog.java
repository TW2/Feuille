package feuille.dialog;

import feuille.io.Settings;
import feuille.io.js.Function;
import feuille.util.DialogResult;
import feuille.io.Exchange;
import feuille.util.Loader;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class KaraokeRulesDialog extends JDialog {

    private final Exchange exchange;

    private Settings settings;
    private DialogResult dialogResult;

    //=============================================

    public KaraokeRulesDialog(Exchange exchange) {
        super(exchange.getMainFrame());
        this.exchange = exchange;
        settings = null;
        setTitle("Karaoke Rules");
        setModal(true);
        setResizable(false);
        setSize(700, 600);

        dialogResult = DialogResult.None;

        JPanel contentPane = new JPanel(new BorderLayout());

        JButton btnOK = new JButton(Loader.language("dialog.ok", "OK"));
        btnOK.addActionListener(this::btnOKActionPerformed);
        JButton btnCancel = new JButton(Loader.language("dialog.cancel", "Cancel"));
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnOK);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(btnOK);
        resultPanel.add(btnCancel);

        contentPane.add(tabbedPane, BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.SOUTH);

        Border bdTab = BorderFactory.createLineBorder(
                UIManager.getColor("TabbedPane.underlineColor"));

        //===================================================
        JPanel rulesMainPanel = new JPanel(new BorderLayout());
        JTable funkTable = new JTable();
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void showDialog(Settings settings){
        if(settings == null) return;
        this.settings = settings;
        setLocationRelativeTo(exchange.getMainFrame());
        setVisible(true);
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }

    public void btnOKActionPerformed(ActionEvent e) {
        dialogResult = DialogResult.OK;
        setVisible(false);
        dispose();
    }

    public void btnCancelActionPerformed(ActionEvent e) {
        dialogResult = DialogResult.Cancel;
        setVisible(false);
        dispose();
    }
}
