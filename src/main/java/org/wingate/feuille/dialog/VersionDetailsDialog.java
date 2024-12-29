package org.wingate.feuille.dialog;

import org.wingate.feuille.subs.ass.AssTranslateTo;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VersionDetailsDialog extends JDialog {

    private final java.awt.Frame parent;
    private DialogResult dialogResult;

    private AssTranslateTo translations, lastTranslations;
    private boolean noError = true;

    private final DefaultComboBoxModel<ISO_3166> cbModelSrc;
    private final DefaultComboBoxModel<ISO_3166> cbModelDst;
    private final JComboBox<ISO_3166> cbSrc;
    private final JComboBox<ISO_3166> cbDst;
    private final JTextField tfSrc;
    private final JTextField tfDst;
    private final JButton btnDstChangeUpdate;
    private final JTextPane paneUpdates;

    public VersionDetailsDialog(java.awt.Frame parent, boolean modal, ISO_3166 iso) {
        super(parent, modal);
        this.parent = parent;
        dialogResult = DialogResult.None;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle(Load.language("title_VersionDetailsDialog", "Versions manager", iso));

        JButton btnOK = new JButton(Load.language("msg_OK", "OK", iso));
        JButton btnCancel = new JButton(Load.language("msg_Cancel", "Cancel", iso));
        JLabel lblSrc = new JLabel(Load.language("lbl_SrcLanguage", "Source language:", iso));
        JLabel lblDst = new JLabel(Load.language("lbl_DstLanguage", "To language:", iso));
        cbModelSrc = new DefaultComboBoxModel<>();
        cbModelDst = new DefaultComboBoxModel<>();
        cbSrc = new JComboBox<>(cbModelSrc);
        cbDst = new JComboBox<>(cbModelDst);
        cbSrc.setRenderer(new Renderer());
        cbDst.setRenderer(new Renderer());
        tfSrc = new JTextField("");
        tfDst = new JTextField("");
        btnDstChangeUpdate = new JButton(Load.language("btn_dst-change-update", "Change", iso));

        // SRC
        lblSrc.setLocation(6, 2);
        lblSrc.setSize(130, 22);
        cbSrc.setLocation(136, 2);
        cbSrc.setSize(200, 22);
        tfSrc.setLocation(338, 2);
        tfSrc.setSize(300, 22);

        // DST
        lblDst.setLocation(6, 26);
        lblDst.setSize(130, 22);
        cbDst.setLocation(136, 26);
        cbDst.setSize(200, 22);
        tfDst.setLocation(338, 26);
        tfDst.setSize(300, 22);
        btnDstChangeUpdate.setLocation(640, 26);
        btnDstChangeUpdate.setSize(160, 22);

        // Update
        JLabel lblUpdates = new JLabel(Load.language("lbl_version-updates", "Updates:", iso));
        lblUpdates.setLocation(6, 50);
        lblUpdates.setSize(130, 22);
        paneUpdates = new JTextPane();
        JScrollPane scrollUpdates = new JScrollPane(paneUpdates);
        scrollUpdates.setLocation(136, 50);
        scrollUpdates.setSize(502, 140);
        paneUpdates.setEditable(false);
        scrollUpdates.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollUpdates.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Cancel
        btnCancel.setLocation(640, 144);
        btnCancel.setSize(160, 22);

        // OK
        btnOK.setLocation(640, 168);
        btnOK.setSize(160, 22);

        getContentPane().setLayout(null);
        getContentPane().add(lblSrc);
        getContentPane().add(cbSrc);
        getContentPane().add(tfSrc);
        getContentPane().add(lblDst);
        getContentPane().add(cbDst);
        getContentPane().add(tfDst);
        getContentPane().add(btnDstChangeUpdate);
        getContentPane().add(lblUpdates);
        getContentPane().add(scrollUpdates);
        getContentPane().add(btnOK);
        getContentPane().add(btnCancel);

        for(ISO_3166 x : ISO_3166.values()){
            if(x == ISO_3166.Unknown || x == null) continue;
            try{
                Load.fromResource("/org/wingate/feuille/util/" + x.getAlpha2() + ".gif");
                cbModelSrc.addElement(x);
            }catch(Exception _){ }
        }

        for(ISO_3166 x : ISO_3166.values()){
            if(x == ISO_3166.Unknown || x == null) continue;
            try{
                Load.fromResource("/org/wingate/feuille/util/" + x.getAlpha2() + ".gif");
                cbModelDst.addElement(x);
            }catch(Exception _){ }
        }

        btnOK.addActionListener(e -> {
            try{
                dialogResult = DialogResult.OK;
                setVisible(false);
                dispose();
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(VersionDetailsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnCancel.addActionListener(e -> {
            try{
                dialogResult = DialogResult.Cancel;
                setVisible(false);
                dispose();
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(VersionDetailsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        cbSrc.addActionListener(e -> {
            try{
                AssTranslateTo.Version x = translations.getVersion(cbSrc.getItemAt(cbSrc.getSelectedIndex()));
                if(x == null){
                    translations = AssTranslateTo.createFirst(cbSrc.getItemAt(cbSrc.getSelectedIndex()));
                    AssTranslateTo.Version v = AssTranslateTo.Version.increment(translations.getVersions().getFirst(), cbDst.getItemAt(cbDst.getSelectedIndex()));
                    translations.getVersions().add(v);
                    tfSrc.setText(translations.getVersion(cbSrc.getItemAt(cbSrc.getSelectedIndex())).getText());
                }else{
                    tfSrc.setText(translations.getVersion(cbSrc.getItemAt(cbSrc.getSelectedIndex())).getText());
                }
                refillPane(translations);
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(VersionDetailsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnDstChangeUpdate.addActionListener(e -> {
            try{
                AssTranslateTo.Version v;
                if(tfDst.getText().isEmpty()){
                    v = AssTranslateTo.Version.increment(translations.getVersions().getLast(), cbDst.getItemAt(cbDst.getSelectedIndex()));
                }else{
                    v = AssTranslateTo.Version.increment(translations.getVersions().getLast(), tfDst.getText(), cbDst.getItemAt(cbDst.getSelectedIndex()));
                }
                translations.getVersions().add(v);
                refillPane(translations);
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(VersionDetailsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public DialogResult getDialogResult(){
        return dialogResult;
    }

    public void showDialog(ISO_3166 src, ISO_3166 dst, AssTranslateTo translations){
        this.translations = translations;
        lastTranslations = translations;
        noError = true;
        cbModelSrc.setSelectedItem(src);
        cbModelDst.setSelectedItem(dst);
        refillPane(translations);
        setSize(820,233);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public boolean noError() {
        return noError;
    }

    public ISO_3166 getSrc() {
        return cbSrc.getItemAt(cbSrc.getSelectedIndex());
    }

    public ISO_3166 getDst() {
        return cbDst.getItemAt(cbDst.getSelectedIndex());
    }

    public void lockSrc(){
        cbSrc.setEnabled(false);
        tfSrc.setEnabled(false);
    }

    public void lockDst(){
        cbDst.setEnabled(false);
        tfDst.setEnabled(false);
        btnDstChangeUpdate.setEnabled(false);
    }

    public AssTranslateTo getTranslations() {
        try{
            translations.getVersion(cbSrc.getItemAt(cbSrc.getSelectedIndex())).setText(tfSrc.getText());
            translations.getLastVersion(cbDst.getItemAt(cbDst.getSelectedIndex())).setText(tfDst.getText());
        }catch(Exception _){
            noError = false;
            JOptionPane.showMessageDialog(
                    this,
                    Load.language("msg_Error_target_language", "You cannot change the target language without make a new version!", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country())),
                    Load.language("msg_Error", "Error", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country())),
                    JOptionPane.ERROR_MESSAGE
            );
            translations = lastTranslations;
        }
        return translations;
    }

    private String getLineOFOneUpdate(AssTranslateTo.Version v){
        // Get the language
        ISO_3166 lng = v.getIso();
        ISO_3166 iso = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        // Get the date
        String day;
        switch(v.getTag().getDayOfWeek()){
            case 2 -> { day = Load.language("date_Monday", "Monday", iso); }
            case 3 -> { day = Load.language("date_Tuesday", "Tuesday", iso); }
            case 4 -> { day = Load.language("date_Wednesday", "Wednesday", iso); }
            case 5 -> { day = Load.language("date_Thursday", "Thursday", iso); }
            case 6 -> { day = Load.language("date_Friday", "Friday", iso); }
            case 7 -> { day = Load.language("date_Saturday", "Saturday", iso); }
            case 1 -> { day = Load.language("date_Sunday", "Sunday", iso); }
            default -> { day = "Unknown day"; }
        }
        String month;
        switch(v.getTag().getMonth()){
            case 0 -> { month = Load.language("date_January", "January", iso); }
            case 1 -> { month = Load.language("date_February", "February", iso); }
            case 2 -> { month = Load.language("date_March", "March", iso); }
            case 3 -> { month = Load.language("date_April", "April", iso); }
            case 4 -> { month = Load.language("date_May", "May", iso); }
            case 5 -> { month = Load.language("date_June", "June", iso); }
            case 6 -> { month = Load.language("date_July", "July", iso); }
            case 7 -> { month = Load.language("date_August", "August", iso); }
            case 8 -> { month = Load.language("date_September", "September", iso); }
            case 9 -> { month = Load.language("date_October", "October", iso); }
            case 10 -> { month = Load.language("date_November", "November", iso); }
            case 11 -> { month = Load.language("date_December", "December", iso); }
            default -> { month = "Unknown month"; }
        }
        // Let's format the date
        int year = v.getTag().getYear();
        int i_day = v.getTag().getDayInMonth();
        int i_month = v.getTag().getMonth();
        int h = v.getTag().getHourOfDay();
        int m = v.getTag().getMinutes();
        int s = v.getTag().getSeconds();
        int ms = v.getTag().getMs();
        String date = String.format("%s, %s %d/%02d/%02d (%02d:%02d:%02d.%03d)", month, day, year, i_month, i_day, h, m, s, ms);
        // Get the message
        String message = String.format("%s %d, %s\n", Load.language("msg_Version", "Version", iso), v.getTag().getNumber(), v.getTag().getTagText());
        return String.format("%s %s %s", message, date, lng.getCountry());
    }

    private void refillPane(AssTranslateTo tr){
        StringBuilder s = new StringBuilder();
        for(AssTranslateTo.Version v : tr.getVersions()){
            s.append(getLineOFOneUpdate(v));
            s.append("\n");
        }
        paneUpdates.setText(s.toString());
    }

    public static class Renderer extends JPanel implements ListCellRenderer<ISO_3166> {

        private final JLabel lblFlag;
        private final JLabel lblText;

        public Renderer(){
            lblFlag = new JLabel();
            lblText = new JLabel();

            setLayout(new BorderLayout());
            add(lblFlag, BorderLayout.WEST);
            add(lblText, BorderLayout.CENTER);
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
                lblText.setForeground(UIManager.getColor("List.selectionForeground"));
            }else{
                setBackground(UIManager.getColor("List.background"));
                setForeground(UIManager.getColor("List.foreground"));
                lblText.setForeground(UIManager.getColor("List.foreground"));
            }

            lblFlag.setIcon(Load.fromResource("/org/wingate/feuille/util/" + value.getAlpha2() + ".gif"));
            lblText.setText(value.getCountry());

            return this;
        }
    }
}
