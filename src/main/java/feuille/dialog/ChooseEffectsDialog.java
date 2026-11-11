package feuille.dialog;

import feuille.io.assa.AssEffect;
import feuille.util.DialogResult;
import feuille.util.Loader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ChooseEffectsDialog extends JDialog {

    private final Frame frame;
    private DialogResult dialogResult;

    private final DefaultListModel<AssEffect> model;
    private final JList<AssEffect> list;

    public ChooseEffectsDialog(Frame parent) {
        super(parent);
        frame = parent;
        setTitle("Choose Effects");
        setModal(true);
        setResizable(false);
        setSize(300, 300);

        dialogResult = DialogResult.None;

        model = new DefaultListModel<>();
        list = new JList<>(model);

        JPanel contentPane = new JPanel(new BorderLayout());

        JButton btnOK = new JButton(Loader.language("dialog.ok", "OK"));
        btnOK.addActionListener(this::btnOKActionPerformed);
        JButton btnCancel = new JButton(Loader.language("dialog.cancel", "Cancel"));
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnOK);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(btnOK);
        resultPanel.add(btnCancel);

        contentPane.add(new JScrollPane(list), BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.SOUTH);

        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public void showDialog(java.util.List<AssEffect> effects) {
        model.clear();
        model.addAll(effects);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }

    public List<AssEffect> getEffects() {
        return new ArrayList<>(list.getSelectedValuesList());
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
