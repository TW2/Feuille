package org.wingate.feuille.dialog.idea;

import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;

import javax.swing.*;

public class VersionDetailsDialog extends JDialog {

    private final java.awt.Frame parent;
    private DialogResult dialogResult;

    public VersionDetailsDialog(java.awt.Frame parent, boolean modal, ISO_3166 iso) {
        super(parent, modal);
        this.parent = parent;
        dialogResult = DialogResult.None;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JButton btnOK = new JButton(Load.language("msg_OK", "OK", iso));
        JButton btnCancel = new JButton(Load.language("msg_Cancel", "Cancel", iso));
        JLabel lblSrc = new JLabel(Load.language("lbl_SrcLanguage", "Source language:", iso));
        JLabel lblDst = new JLabel(Load.language("lbl_DstLanguage", "To language:", iso));
        DefaultComboBoxModel<ISO_3166> cbModelSrc = new DefaultComboBoxModel<>();
        JComboBox<ISO_3166> cbSrc = new JComboBox<>(cbModelSrc);
        DefaultComboBoxModel<ISO_3166> cbModelDst = new DefaultComboBoxModel<>();
        JComboBox<ISO_3166> cbDst = new JComboBox<>(cbModelDst);

        btnOK.setLocation(300 - 204, 200 - 24);
        btnOK.setSize(100, 22);

        btnCancel.setLocation(300 - 102, 200 - 24);
        btnCancel.setSize(100, 22);

        getContentPane().setLayout(null);
        getContentPane().add(btnOK);
        getContentPane().add(btnCancel);

        btnOK.addActionListener(e -> {
            dialogResult = DialogResult.OK;
            setVisible(false);
            dispose();
        });

        btnCancel.addActionListener(e -> {
            dialogResult = DialogResult.Cancel;
            setVisible(false);
            dispose();
        });
    }

    public DialogResult getDialogResult(){
        return dialogResult;
    }

    public void showDialog(){
        setSize(300,200);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
