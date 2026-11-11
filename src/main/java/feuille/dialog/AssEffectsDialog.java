package feuille.dialog;

import feuille.component.PlaceholderTextField;
import feuille.io.Exchange;
import feuille.io.assa.*;
import feuille.util.DialogResult;
import feuille.util.Loader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Objects;

public class AssEffectsDialog extends JDialog {

    private final Exchange exchange;
    private final AssEvent currentEvent;

    private DialogResult dialogResult;
    private final JTabbedPane tabbedPane;

    //=================================================== Styles collector
    private final DefaultListModel<AssEffect> storageModel;
    private final JList<AssEffect> storageList;
    private final PlaceholderTextField tfCol;
    private final DefaultComboBoxModel<AssEffectsCollection> modelColEffects;
    private final JComboBox<AssEffectsCollection> cbColEffect;
    private final DefaultListModel<AssEffect> effectStorageModel;
    private final JList<AssEffect> effectStorageList;

    public AssEffectsDialog(Exchange exchange){
        super(exchange.getMainFrame());
        this.exchange = exchange;

        currentEvent = exchange.getCurrentEvent();

        setTitle("Effects");
        setModal(true);
        setResizable(false);
        setSize(900, 700);

        dialogResult = DialogResult.None;
        JPopupMenu storagePopupMenu = getStoragePopupMenu();
        JPopupMenu effectStoragePopupMenu = getScriptStoragePopupMenu();

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // ### NORTH ######################################
        tabbedPane = new JTabbedPane();
        // ################################################

        // ### SOUTH ######################################
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JButton btnOK = new JButton(Loader.language("dialog.ok", "OK"));
        btnOK.addActionListener(this::btnOKActionPerformed);
        JButton btnCancel = new JButton(Loader.language("dialog.cancel", "Cancel"));
        btnCancel.addActionListener(this::btnCancelActionPerformed);
        resultPanel.add(btnOK);
        resultPanel.add(btnCancel);
        getRootPane().setDefaultButton(btnOK);

        JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        helpPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JButton btnHelp = new JButton(Loader.language("dialog.help", "Help"));
        btnHelp.addActionListener(this::btnHelpActionPerformed);
        helpPanel.add(btnHelp);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(helpPanel, BorderLayout.WEST);
        southPanel.add(resultPanel, BorderLayout.CENTER);
        // ################################################

        contentPane.add(tabbedPane, BorderLayout.CENTER);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        Border bdTab = BorderFactory.createLineBorder(
                UIManager.getColor("TabbedPane.underlineColor"));

        //=================================================== Effects collector
        JPanel collectorPanel = new JPanel(new BorderLayout());
        JPanel collectorColPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel collectorMainPanel = new JPanel(new GridLayout(1, 2));
        collectorPanel.add(collectorColPanel, BorderLayout.NORTH);
        collectorPanel.add(collectorMainPanel, BorderLayout.CENTER);
        collectorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblCol = new JLabel(Loader.language("dialog.lbl.col", "Collection: "));
        modelColEffects = new DefaultComboBoxModel<>();
        cbColEffect = new JComboBox<>(modelColEffects);
        cbColEffect.setPreferredSize(new Dimension(300, 26));
        cbColEffect.addActionListener(this::cbColEffectActionPerformed);
        tfCol = new PlaceholderTextField();
        tfCol.setPlaceholder(Loader.language("dialog.new.col.name", "New collection name"));
        tfCol.setPreferredSize(new Dimension(300, 26));
        JButton btnAddCol = new JButton(Loader.language("dialog.add", "Add"));
        btnAddCol.addActionListener(this::btnAddColActionPerformed);
        JButton btnRemoveCol = new JButton(Loader.language("dialog.remove", "Remove"));
        btnRemoveCol.addActionListener(this::btnRemoveColActionPerformed);
        collectorColPanel.add(lblCol);
        collectorColPanel.add(cbColEffect);
        collectorColPanel.add(tfCol);
        collectorColPanel.add(btnAddCol);
        collectorColPanel.add(btnRemoveCol);

        JPanel storagePanel = new JPanel(new BorderLayout());
        storageModel = new DefaultListModel<>();
        storageList = new JList<>(storageModel);
        storageList.setComponentPopupMenu(storagePopupMenu);
        storageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        storagePanel.add(new JScrollPane(storageList), BorderLayout.CENTER);
        storagePanel.setBorder(BorderFactory.createTitledBorder(bdTab, Loader.language("dialog.main.fx",  "Main effects")));
        collectorMainPanel.add(storagePanel);

        JPanel scriptStoragePanel = new JPanel(new BorderLayout());
        effectStorageModel = new DefaultListModel<>();
        effectStorageList = new JList<>(effectStorageModel);
        effectStorageList.setComponentPopupMenu(effectStoragePopupMenu);
        effectStorageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scriptStoragePanel.add(new JScrollPane(effectStorageList), BorderLayout.CENTER);
        scriptStoragePanel.setBorder(BorderFactory.createTitledBorder(bdTab, Loader.language("dialog.script.fx", "Script effects")));
        effectStorageList.addListSelectionListener(this::effectStorageListSelectionChanged);
        collectorMainPanel.add(scriptStoragePanel);

        tabbedPane.add(Loader.language("dialog.title.fx.col", "Effects Collector"), collectorPanel);

        //=============================================================
        JPanel effectsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        effectsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblMethod = new JLabel("Method: ");
        effectsPanel.add(lblMethod);

        DefaultComboBoxModel<String> methodsModel = new DefaultComboBoxModel<>();
        JComboBox<String> cbMethods = new JComboBox<>(methodsModel);
        cbMethods.setPreferredSize(new Dimension(500, 22));
        effectsPanel.add(cbMethods);

        tabbedPane.add(Loader.language("dialog.title.fx.edit", "Effects Editor"), effectsPanel);
    }

    public void showDialog() {
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

    public void btnHelpActionPerformed(ActionEvent e) {
        HTMLHelpDialog help = new HTMLHelpDialog(exchange);
        if(tabbedPane.getSelectedIndex() == 0){
            help.showDialog("EffectsCollection");
        }else if(tabbedPane.getSelectedIndex() == 1){
            help.showDialog("EffectsEditor");
        }
    }

    //-------------------------------------------------------------------------

    public JPopupMenu getStoragePopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem mScCopy = new JMenuItem(Loader.language("storage.copy.from.ext.script", "Copy from external script"));
        mScCopy.addActionListener(this::btnScCopyActionPerformed);
        mScCopy.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEffectsDialog.class.getResource("/images/16-LucideCopy.png"))));
        popupMenu.add(mScCopy);
        JMenuItem mScRemove = new JMenuItem(Loader.language("storage.remove", "Remove"));
        mScRemove.addActionListener(this::btnScRemoveActionPerformed);
        mScRemove.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEffectsDialog.class.getResource("/images/16-LucideCircleX.png"))));
        popupMenu.add(mScRemove);
        JMenuItem mScToScript = new JMenuItem(Loader.language("storage.copy.to.script", "Copy to script"));
        mScToScript.addActionListener(this::btnScToScriptActionPerformed);
        mScToScript.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEffectsDialog.class.getResource("/images/16-LucideDownload.png"))));
        popupMenu.add(mScToScript);

        return popupMenu;
    }

    public JPopupMenu getScriptStoragePopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem mScSNew = new JMenuItem(Loader.language("storage.new.style", "New"));
        mScSNew.addActionListener(this::btnScSNewActionPerformed);
        mScSNew.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEffectsDialog.class.getResource("/images/16-LucideCirclePlus.png"))));
        popupMenu.add(mScSNew);
        JMenuItem mScSDuplicate = new JMenuItem(Loader.language("storage.duplicate", "Duplicate"));
        mScSDuplicate.addActionListener(this::btnScSDuplicateActionPerformed);
        mScSDuplicate.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEffectsDialog.class.getResource("/images/16-LucideBookCopy.png"))));
        popupMenu.add(mScSDuplicate);
        JMenuItem mScSCopy = new JMenuItem(Loader.language("storage.copy.from.ext.script", "Copy from external script"));
        mScSCopy.addActionListener(this::btnScSCopyActionPerformed);
        mScSCopy.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEffectsDialog.class.getResource("/images/16-LucideCopy.png"))));
        popupMenu.add(mScSCopy);
        JMenuItem mScSRemove = new JMenuItem(Loader.language("storage.remove", "Remove"));
        mScSRemove.addActionListener(this::btnScSRemoveActionPerformed);
        mScSRemove.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEffectsDialog.class.getResource("/images/16-LucideCircleX.png"))));
        popupMenu.add(mScSRemove);
        JMenuItem mScSToStorage = new JMenuItem(Loader.language("storage.copy.to.storage", "Copy to storage"));
        mScSToStorage.addActionListener(this::btnScSToStorageActionPerformed);
        mScSToStorage.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEffectsDialog.class.getResource("/images/16-LucideDownload.png"))));
        popupMenu.add(mScSToStorage);

        return popupMenu;
    }

    public void cbColEffectActionPerformed(ActionEvent e) {
        if(cbColEffect.getSelectedItem() instanceof AssEffectsCollection c){
            storageModel.removeAllElements();
            for(AssEffect fx : c.getEffects()){
                storageModel.addElement(fx);
            }
        }
    }

    public void btnAddColActionPerformed(ActionEvent e) {
        AssEffect newEffect = new AssEffect();
        AssEffectsCollection newCol = new AssEffectsCollection(tfCol.getText());
        newCol.getEffects().add(newEffect);
        modelColEffects.addElement(newCol);
    }

    public void btnRemoveColActionPerformed(ActionEvent e) {
        if(cbColEffect.getSelectedIndex() != -1) {
            AssEffectsCollection c = cbColEffect.getItemAt((cbColEffect.getSelectedIndex()));
            int z = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to remove this collection?\n" + c.getName(),
                    "Confirm action",
                    JOptionPane.YES_NO_OPTION
            );
            if(z == JOptionPane.YES_OPTION) {
                storageModel.removeAllElements();
                modelColEffects.removeElement(c);
            }
        }
    }

    public void btnScCopyActionPerformed(ActionEvent e) {
        // External file copy
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Select file");
        chooser.setApproveButtonText("Select");
        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) return true;
                return f.getName().toLowerCase().endsWith(".ass");
            }

            @Override
            public String getDescription() {
                return "Advanced SubStation Alpha scripts | ASSA (*.ass)";
            }
        });
        int z = chooser.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION) {
            ASS ass = ASS.read(chooser.getSelectedFile().getAbsolutePath());
            ChooseEffectsDialog ce = new ChooseEffectsDialog(exchange.getMainFrame());
            ce.showDialog(ass.getEffects());
            if(ce.getDialogResult() == DialogResult.OK) {
                storageModel.addAll(ce.getEffects());
            }
        }
    }

    public void btnScRemoveActionPerformed(ActionEvent e) {
        if(storageList.getSelectedIndex() != -1) {
            int z = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to remove these storage effects?",
                    "Confirm action",
                    JOptionPane.YES_NO_OPTION
            );
            if(z == JOptionPane.YES_OPTION) {
                for(int i=storageList.getSelectedIndices().length-1; i>=0; i--) {
                    storageModel.remove(storageList.getSelectedIndices()[i]);
                }
            }
        }
    }

    public void btnScToScriptActionPerformed(ActionEvent e) {
        if(storageList.getSelectedIndex() != -1) {
            for(int i=0; i<storageList.getSelectedIndices().length; i++) {
                effectStorageModel.addElement(storageModel.get(i));
            }
        }
    }

    public void btnScSNewActionPerformed(ActionEvent e) {
        AssEffect assEffect = new AssEffect();
        effectStorageModel.addElement(assEffect);
    }

    public void btnScSDuplicateActionPerformed(ActionEvent e) {
        if(effectStorageList.getSelectedIndex() != -1) {
            AssEffect selected = effectStorageModel.getElementAt(effectStorageList.getSelectedIndex());
            AssEffect newEffect = new AssEffect();
            newEffect.setName(selected.getName() + " copy");
            newEffect.setEffect(selected.getEffect());
            effectStorageModel.addElement(newEffect);
        }
    }

    public void btnScSCopyActionPerformed(ActionEvent e) {
        // External file copy
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Select file");
        chooser.setApproveButtonText("Select");
        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) return true;
                return f.getName().toLowerCase().endsWith(".ass");
            }

            @Override
            public String getDescription() {
                return "Advanced SubStation Alpha scripts | ASSA (*.ass)";
            }
        });
        int z = chooser.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION) {
            ASS ass = ASS.read(chooser.getSelectedFile().getAbsolutePath());
            ChooseEffectsDialog ce = new ChooseEffectsDialog(exchange.getMainFrame());
            ce.showDialog(ass.getEffects());
            if(ce.getDialogResult() == DialogResult.OK) {
                effectStorageModel.addAll(ce.getEffects());
            }
        }
    }

    public void btnScSRemoveActionPerformed(ActionEvent e) {
        if(effectStorageList.getSelectedIndex() != -1) {
            int z = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to remove these storage effects?",
                    "Confirm action",
                    JOptionPane.YES_NO_OPTION
            );
            if(z == JOptionPane.YES_OPTION) {
                for(int i=effectStorageList.getSelectedIndices().length-1; i>=0; i--) {
                    effectStorageModel.remove(effectStorageList.getSelectedIndices()[i]);
                }
            }
        }
    }

    public void btnScSToStorageActionPerformed(ActionEvent e) {
        if(effectStorageList.getSelectedIndex() != -1) {
            for(int i=0; i<effectStorageList.getSelectedIndices().length; i++) {
                storageModel.addElement(effectStorageModel.get(i));
            }
        }
    }

    public void effectStorageListSelectionChanged(ListSelectionEvent e) {
        int index = effectStorageList.getSelectedIndex();
        if(index == -1) return;
        // TODO
    }
}
