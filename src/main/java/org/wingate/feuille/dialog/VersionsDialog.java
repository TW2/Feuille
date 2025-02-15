package org.wingate.feuille.dialog;

import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VersionsDialog extends JDialog {

    private final java.awt.Frame parent;
    private DialogResult dialogResult;

    private final DefaultComboBoxModel<ISO_3166> cbModelIso;
    private final JComboBox<ISO_3166> cbIso;
    private final DefaultListModel<ISO_3166> listModelIso;
    private final JList<ISO_3166> listIso;
    private final JButton btnAddVersion;
    private final JButton btnRemoveVersion;

    public VersionsDialog(java.awt.Frame parent, boolean modal, ISO_3166 iso){
        super(parent, modal);
        this.parent = parent;
        dialogResult = DialogResult.None;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle(Load.language("title_VersionDetailsDialog", "Versions manager", iso));

        JButton btnOK = new JButton(Load.language("msg_OK", "OK", iso));
        JButton btnCancel = new JButton(Load.language("msg_Cancel", "Cancel", iso));
        btnAddVersion = new JButton(Load.language("btn_Dialog-add-version", "Add version", iso));
        btnRemoveVersion = new JButton(Load.language("btn_Dialog-remove-version", "Delete version", iso));
        JPanel pOkCancel = new JPanel(new GridLayout(1, 4, 2, 1));
        pOkCancel.add(btnAddVersion);
        pOkCancel.add(btnRemoveVersion);
        pOkCancel.add(btnCancel);
        pOkCancel.add(btnOK);

        cbModelIso = new DefaultComboBoxModel<>();
        cbIso = new JComboBox<>(cbModelIso);
        cbIso.setRenderer(new VersionsDialog.Renderer(0));
        listModelIso = new DefaultListModel<>();
        listIso = new JList<>(listModelIso);
        JScrollPane scrollList = new JScrollPane(listIso);
        scrollList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        listIso.setCellRenderer(new VersionsDialog.Renderer(6));
        JPanel pVersion = new JPanel(new BorderLayout(1, 2));
        pVersion.add(cbIso, BorderLayout.NORTH);
        pVersion.add(scrollList, BorderLayout.CENTER);

        JPanel pPaneButtons = new JPanel(new BorderLayout(2,1));
        pPaneButtons.add(pVersion, BorderLayout.CENTER);
        pPaneButtons.add(pOkCancel, BorderLayout.SOUTH);

        setLayout(new BorderLayout(1, 2));
        add(pVersion, BorderLayout.CENTER);
        add(pPaneButtons, BorderLayout.SOUTH);

        for(ISO_3166 x : ISO_3166.values()){
            if(x == ISO_3166.Unknown || x == null) continue;
            try{
                Load.fromResource("/org/wingate/feuille/" + x.getAlpha2() + ".gif");
                cbModelIso.addElement(x);
            }catch(Exception _){ }
        }

        btnOK.addActionListener(e -> {
            try{
                dialogResult = DialogResult.OK;
                setVisible(false);
                dispose();
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(VersionsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnCancel.addActionListener(e -> {
            try{
                dialogResult = DialogResult.Cancel;
                setVisible(false);
                dispose();
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(VersionsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnAddVersion.addActionListener(e -> {
            try{
                if(cbIso.getSelectedIndex() != -1){
                    if(!listModelIso.contains(cbIso.getItemAt(cbIso.getSelectedIndex()))){
                        listModelIso.addElement(cbIso.getItemAt(cbIso.getSelectedIndex()));
                    }
                }
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(VersionsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnRemoveVersion.addActionListener(e -> {
            try{
                if(listIso.getSelectedIndex() != -1){
                    listModelIso.remove(listIso.getSelectedIndex());
                }
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(VersionsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public DialogResult getDialogResult(){
        return dialogResult;
    }

    public void showDialog(java.util.List<ISO_3166> selection){
        if(selection != null && !selection.isEmpty()){
            listModelIso.addAll(selection);
        }

        setSize(660,400);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public java.util.List<ISO_3166> getList() {
        final java.util.List<ISO_3166> list = new ArrayList<>();

        for(int i=0; i<listModelIso.getSize(); i++){
            list.add(listModelIso.get(i));
        }

        return list;
    }

    public static class Renderer extends JPanel implements ListCellRenderer<ISO_3166> {

        private final JPanel p;
        private final JLabel lblFlag;
        private final JLabel lblText;

        public Renderer(int westSpace){
            lblFlag = new JLabel();
            lblText = new JLabel();

            p = new JPanel(new BorderLayout(4, 0));
            p.add(lblFlag, BorderLayout.WEST);
            p.add(lblText, BorderLayout.CENTER);

            setLayout(new BorderLayout(4, 0));
            JLabel lblSpacer = new JLabel();
            lblSpacer.setPreferredSize(new java.awt.Dimension(westSpace, 22));
            add(lblSpacer, BorderLayout.WEST);
            add(p, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ISO_3166> list,
                                                      ISO_3166 value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {

            if(isSelected){
                setBackground(UIManager.getColor("List.selectionBackground"));
                setForeground(UIManager.getColor("List.selectionForeground"));
                p.setBackground(UIManager.getColor("List.selectionBackground"));
                p.setForeground(UIManager.getColor("List.selectionForeground"));
                lblText.setForeground(UIManager.getColor("List.selectionForeground"));
            }else{
                setBackground(UIManager.getColor("List.background"));
                setForeground(UIManager.getColor("List.foreground"));
                p.setBackground(UIManager.getColor("List.background"));
                p.setForeground(UIManager.getColor("List.foreground"));
                lblText.setForeground(UIManager.getColor("List.foreground"));
            }

            lblFlag.setIcon(Load.fromResource("/org/wingate/feuille/" + value.getAlpha2() + ".gif"));
            lblText.setText(value.getCountry());

            return this;
        }
    }
}
