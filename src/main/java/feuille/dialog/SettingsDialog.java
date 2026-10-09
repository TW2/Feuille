package feuille.dialog;

import feuille.module.editor.PlaceholderTextField;
import feuille.util.DialogResult;
import feuille.util.Loader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class SettingsDialog extends JDialog {

    private DialogResult dialogResult;

    // -- Database settings -----------------------------------
    private boolean changeDBName = false;

    private boolean useDefault;
    private final JCheckBox cbxUseDefault;

    private boolean useManyAudioDBName;
    private final JLabel lblUseManyAudioDBName;
    private final JCheckBox cbxUseManyAudioDBName;

    private boolean useManyAssaDBName;
    private final JLabel lblUseManyAssaDBName;
    private final JCheckBox cbxUseManyAssaDBName;

    private final List<String> audioDBNames;
    private final JLabel lblAudioDBName;
    private final JComboBox<String> cbAudioDBName;
    private final DefaultComboBoxModel<String> cbAudioDBModel;
    private final PlaceholderTextField tfAudioDBName;
    private final JButton btnAudioDBNameAdd;
    private final JButton btnAudioDBNameCorrect;
    private final JButton btnAudioDBNameRemove;

    private final List<String> assaDBNames;
    private final JLabel lblAssaDBName;
    private final JComboBox<String> cbAssaDBName;
    private final DefaultComboBoxModel<String> cbAssaDBModel;
    private final PlaceholderTextField tfAssaDBName;
    private final JButton btnAssaDBNameAdd;
    private final JButton btnAssaDBNameCorrect;
    private final JButton btnAssaDBNameRemove;

    private String databaseLocation;
    private String temporaryWaveformName;
    private String temporarySpectrumName;
    private String temporaryAssScriptName;
    private String temporaryRendererName;




    public SettingsDialog() {
        setModal(true);
        setTitle(Loader.language("dialog.settings", "Settings"));

        JPanel contentPane = new JPanel(new BorderLayout());
        JButton buttonOK = new JButton(Loader.language("dialog.ok", "OK"));
        buttonOK.addActionListener(this::btnOKActionPerformed);
        JButton buttonCancel = new JButton(Loader.language("dialog.cancel", "Cancel"));
        buttonCancel.addActionListener(this::btnCancelActionPerformed);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(buttonOK);
        resultPanel.add(buttonCancel);

        contentPane.add(tabbedPane, BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.SOUTH);

        dialogResult = DialogResult.None;

        // -- Database settings -----------------------------------
        useDefault = true;
        useManyAudioDBName = false;
        useManyAssaDBName = false;
        audioDBNames = new ArrayList<>();
        assaDBNames = new ArrayList<>();
        databaseLocation = "/settings";
        temporaryWaveformName = "temp1.png";
        temporarySpectrumName = "temp2.png";
        temporaryAssScriptName = "ass.temp";
        temporaryRendererName = "temp3.png";

        JPanel pDB = new JPanel(new GridLayout(7, 2));
        pDB.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pDB.add(new JLabel(Loader.language("dialog.settings.lbl.default", "Default settings: ")));
        cbxUseDefault = new JCheckBox(Loader.language("dialog.settings.cbx.default", "Use default settings"));
        cbxUseDefault.addActionListener(this::cbxUseDefaultActionPerformed);
        pDB.add(cbxUseDefault);

        lblUseManyAudioDBName = new JLabel(Loader.language("dialog.settings.lbl.audio.db", "Names of SQLite audio databases: "));
        cbxUseManyAudioDBName = new JCheckBox(Loader.language("dialog.settings.cbx.audio.db", "Different for each project"));
        cbxUseManyAudioDBName.addActionListener(this::cbxUseManyAudioDBNameActionPerformed);
        pDB.add(lblUseManyAudioDBName);
        pDB.add(cbxUseManyAudioDBName);

        lblAudioDBName = new JLabel(Loader.language("dialog.settings.lbl.manage.name", "Manage project"));
        cbAudioDBName = new JComboBox<>();
        cbAudioDBName.addActionListener(this::cbAudioDBNameActionPerformed);
        cbAudioDBModel = new DefaultComboBoxModel<>();
        cbAudioDBName.setModel(cbAudioDBModel);
        tfAudioDBName = new PlaceholderTextField();
        tfAudioDBName.setPlaceholder("project-name/database-name");
        btnAudioDBNameAdd = new JButton(Loader.language("dialog.settings.btn.add", "Add"));
        btnAudioDBNameAdd.addActionListener(this::setBtnAudioDBNameAddActionPerformed);
        btnAudioDBNameCorrect = new JButton(Loader.language("dialog.settings.btn.change", "Change"));
        btnAudioDBNameCorrect.addActionListener(this::setBtnAudioDBNameCorrectActionPerformed);
        btnAudioDBNameRemove = new JButton(Loader.language("dialog.settings.btn.remove", "Remove"));
        btnAudioDBNameRemove.addActionListener(this::setBtnAudioDBNameRemoveActionPerformed);
        pDB.add(lblAudioDBName);
        pDB.add(cbAudioDBName);
        JPanel correctRemoveAudioButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        correctRemoveAudioButtons.add(btnAudioDBNameAdd);
        correctRemoveAudioButtons.add(btnAudioDBNameCorrect);
        correctRemoveAudioButtons.add(btnAudioDBNameRemove);
        pDB.add(correctRemoveAudioButtons);
        pDB.add(tfAudioDBName);

        lblUseManyAssaDBName = new JLabel(Loader.language("dialog.settings.lbl.assa.db", "Names of SQLite ASS databases: "));
        cbxUseManyAssaDBName = new JCheckBox(Loader.language("dialog.settings.cbx.assa.db", "Different for each project"));
        cbxUseManyAssaDBName.addActionListener(this::cbxUseManyAssaDBNameActionPerformed);
        pDB.add(lblUseManyAssaDBName);
        pDB.add(cbxUseManyAssaDBName);

        lblAssaDBName = new JLabel(Loader.language("dialog.settings.lbl.manage.name", "Manage project"));
        cbAssaDBName = new JComboBox<>();
        cbAssaDBName.addActionListener(this::cbAssaDBNameActionPerformed);
        cbAssaDBModel = new DefaultComboBoxModel<>();
        cbAssaDBName.setModel(cbAssaDBModel);
        tfAssaDBName = new PlaceholderTextField();
        tfAssaDBName.setPlaceholder("project-name/database-name");
        btnAssaDBNameAdd = new JButton(Loader.language("dialog.settings.btn.add", "Add"));
        btnAssaDBNameAdd.addActionListener(this::setBtnAssaDBNameAddActionPerformed);
        btnAssaDBNameCorrect = new JButton(Loader.language("dialog.settings.btn.change", "Change"));
        btnAssaDBNameCorrect.addActionListener(this::setBtnAssaDBNameCorrectActionPerformed);
        btnAssaDBNameRemove = new JButton(Loader.language("dialog.settings.btn.remove", "Remove"));
        btnAssaDBNameRemove.addActionListener(this::setBtnAssaDBNameRemoveActionPerformed);
        pDB.add(lblAssaDBName);
        pDB.add(cbAssaDBName);
        JPanel correctRemoveAssaButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        correctRemoveAssaButtons.add(btnAssaDBNameAdd);
        correctRemoveAssaButtons.add(btnAssaDBNameCorrect);
        correctRemoveAssaButtons.add(btnAssaDBNameRemove);
        pDB.add(correctRemoveAssaButtons);
        pDB.add(tfAssaDBName);

        JScrollPane spDB = new JScrollPane(pDB);
        spDB.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spDB.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tabbedPane.addTab(Loader.language("dialog.settings.tab.name.db", "Database"), spDB);
    }

    private void loadSettings() {
        cbxUseDefault.setSelected(useDefault);
        cbxUseManyAudioDBName.setSelected(useManyAudioDBName);
        cbxUseManyAssaDBName.setSelected(useManyAssaDBName);
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

    public DialogResult getDialogResult() {
        return dialogResult;
    }

    public void showDialog() {
        loadSettings();
        setDatabaseParamVisibility();

        setSize(650, 320);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // -- Database settings -----------------------------------
    public void cbxUseDefaultActionPerformed(ActionEvent e) {
        setDatabaseParamVisibility();
    }

    public void cbxUseManyAudioDBNameActionPerformed(ActionEvent e) {
        setDatabaseParamVisibility();
    }

    public void cbxUseManyAssaDBNameActionPerformed(ActionEvent e) {
        setDatabaseParamVisibility();
    }

    public void cbAudioDBNameActionPerformed(ActionEvent e) {
        if(cbAudioDBName.getSelectedItem() instanceof String value && !changeDBName) {
            tfAudioDBName.setText(value);
        }
    }

    public void cbAssaDBNameActionPerformed(ActionEvent e) {
        if(cbAssaDBName.getSelectedItem() instanceof String value && !changeDBName) {
            tfAssaDBName.setText(value);
        }
    }

    public void setBtnAudioDBNameAddActionPerformed(ActionEvent e) {
        cbAudioDBModel.addElement(tfAudioDBName.getText());
    }

    public void setBtnAudioDBNameCorrectActionPerformed(ActionEvent e) {
        int n = cbAudioDBName.getSelectedIndex();
        changeDBName = true;
        if(n != -1){
            cbAudioDBModel.removeElementAt(n);
            if(n-1 == cbAudioDBName.getItemCount() - 1){
                cbAudioDBModel.addElement(tfAudioDBName.getText());
            }else{
                cbAudioDBModel.insertElementAt(tfAudioDBName.getText(), n);
            }
            cbAudioDBName.setSelectedIndex(n);
        }
        changeDBName = false;
    }

    public void setBtnAudioDBNameRemoveActionPerformed(ActionEvent e) {
        int n = cbAudioDBName.getSelectedIndex();
        if(n != -1){
            cbAudioDBModel.removeElementAt(n);
        }
    }

    public void setBtnAssaDBNameAddActionPerformed(ActionEvent e) {
        cbAssaDBModel.addElement(tfAssaDBName.getText());
    }

    public void setBtnAssaDBNameCorrectActionPerformed(ActionEvent e) {
        int n = cbAssaDBName.getSelectedIndex();
        changeDBName = true;
        if(n != -1){
            cbAssaDBModel.removeElementAt(n);
            if(n-1 == cbAssaDBName.getItemCount() - 1){
                cbAssaDBModel.addElement(tfAssaDBName.getText());
            }else{
                cbAssaDBModel.insertElementAt(tfAssaDBName.getText(), n);
            }
            cbAssaDBName.setSelectedIndex(n);
        }
        changeDBName = false;
    }

    public void setBtnAssaDBNameRemoveActionPerformed(ActionEvent e) {
        int n = cbAssaDBName.getSelectedIndex();
        if(n != -1){
            cbAssaDBModel.removeElementAt(n);
        }
    }

    private void setDatabaseParamVisibility(){
        boolean useDefault = cbxUseDefault.isSelected();
        boolean useManyAudioDBName = cbxUseManyAudioDBName.isSelected();
        boolean useManyAssaDBName = cbxUseManyAssaDBName.isSelected();
        lblUseManyAudioDBName.setEnabled(!useDefault);
        lblUseManyAssaDBName.setEnabled(!useDefault);
        cbxUseManyAudioDBName.setEnabled(!useDefault);
        lblAudioDBName.setEnabled(useManyAudioDBName);
        cbAudioDBName.setEnabled(useManyAudioDBName);
        tfAudioDBName.setEnabled(useManyAudioDBName);
        btnAudioDBNameAdd.setEnabled(useManyAudioDBName);
        btnAudioDBNameCorrect.setEnabled(useManyAudioDBName);
        btnAudioDBNameRemove.setEnabled(useManyAudioDBName);
        cbxUseManyAssaDBName.setEnabled(!useDefault);
        lblAssaDBName.setEnabled(useManyAssaDBName);
        cbAssaDBName.setEnabled(useManyAssaDBName);
        tfAssaDBName.setEnabled(useManyAssaDBName);
        btnAssaDBNameAdd.setEnabled(useManyAssaDBName);
        btnAssaDBNameCorrect.setEnabled(useManyAssaDBName);
        btnAssaDBNameRemove.setEnabled(useManyAssaDBName);
    }

    public List<String> getAudioDBNames() {
        return audioDBNames;
    }

    public List<String> getAssaDBNames() {
        return assaDBNames;
    }

    public boolean isUseDefault() {
        return useDefault;
    }

    public void setUseDefault(boolean useDefault) {
        this.useDefault = useDefault;
    }

    public boolean isUseManyAudioDBName() {
        return useManyAudioDBName;
    }

    public void setUseManyAudioDBName(boolean useManyAudioDBName) {
        this.useManyAudioDBName = useManyAudioDBName;
    }

    public boolean isUseManyAssaDBName() {
        return useManyAssaDBName;
    }

    public void setUseManyAssaDBName(boolean useManyAssaDBName) {
        this.useManyAssaDBName = useManyAssaDBName;
    }

    public String getDatabaseLocation() {
        return databaseLocation;
    }

    public void setDatabaseLocation(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }

    public String getTemporaryWaveformName() {
        return temporaryWaveformName;
    }

    public void setTemporaryWaveformName(String temporaryWaveformName) {
        this.temporaryWaveformName = temporaryWaveformName;
    }

    public String getTemporarySpectrumName() {
        return temporarySpectrumName;
    }

    public void setTemporarySpectrumName(String temporarySpectrumName) {
        this.temporarySpectrumName = temporarySpectrumName;
    }

    public String getTemporaryAssScriptName() {
        return temporaryAssScriptName;
    }

    public void setTemporaryAssScriptName(String temporaryAssScriptName) {
        this.temporaryAssScriptName = temporaryAssScriptName;
    }

    public String getTemporaryRendererName() {
        return temporaryRendererName;
    }

    public void setTemporaryRendererName(String temporaryRendererName) {
        this.temporaryRendererName = temporaryRendererName;
    }
}
