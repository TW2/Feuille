package feuille.dialog;

import feuille.io.Settings;
import feuille.module.editor.assa.*;
import feuille.module.editor.voyager.PlaceholderTextField;
import feuille.util.*;
import feuille.util.assa.AssColor;
import feuille.util.assa.AssEncoding;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.*;
import java.util.List;

public class AssEditorDialog extends JDialog {

    private final Exchange exchange;

    private Settings settings;
    private AssStyle currentStyle;
    private DialogResult dialogResult;

    //=================================================== Styles collector
    private final DefaultListModel<AssStyle> storageModel;
    private final JList<AssStyle> storageList;
    private final PlaceholderTextField tfCol;
    private final DefaultComboBoxModel<AssStylesCollection> modelColStyles;
    private final JComboBox<AssStylesCollection> cbColStyle;
    private final DefaultListModel<AssStyle> scriptStorageModel;
    private final JList<AssStyle> scriptStorageList;

    //=================================================== Styles
    private final PlaceholderTextField tfStyleName;
    private final JComboBox<String> cbFont;
    private final SpinnerNumberModel spinnerFontSizeModel;
    private final JSpinner spinnerFontSize;
    private final JCheckBox chkBold;
    private final JCheckBox chkItalic;
    private final JCheckBox chkUnderline;
    private final JCheckBox chkStrikeOut;
    private final SpinnerNumberModel spinnerOutlineModel;
    private final JSpinner spinnerOutline;
    private final SpinnerNumberModel spinnerShadowModel;
    private final JSpinner spinnerShadow;
    private final JCheckBox chkOpaqueBox;
    private final SpinnerNumberModel spinnerScaleXModel;
    private final JSpinner spinnerScaleX;
    private final SpinnerNumberModel spinnerScaleYModel;
    private final JSpinner spinnerScaleY;
    private final DefaultComboBoxModel<AssEncoding> encodingModel;
    private final JComboBox<AssEncoding> cbEncoding;
    private final SpinnerNumberModel spinnerAngleZModel;
    private final JSpinner spinnerAngleZ;
    private final SpinnerNumberModel spinnerSpacingModel;
    private final JSpinner spinnerSpacing;
    private final ColorView cvTextColor;
    private final ColorView cvKaraokeColor;
    private final ColorView cvOutlineColor;
    private final ColorView cvShadowColor;
    private final PlaceholderTextField tfML;
    private final PlaceholderTextField tfMR;
    private final PlaceholderTextField tfMV;
    private final JRadioButton rbAlignment1;
    private final JRadioButton rbAlignment2;
    private final JRadioButton rbAlignment3;
    private final JRadioButton rbAlignment4;
    private final JRadioButton rbAlignment5;
    private final JRadioButton rbAlignment6;
    private final JRadioButton rbAlignment7;
    private final JRadioButton rbAlignment8;
    private final JRadioButton rbAlignment9;
    private final PreviewASS previewASS;

    public AssEditorDialog(Exchange exchange) {
        super(exchange.getMainFrame());
        this.exchange = exchange;
        settings = null;
        currentStyle = new AssStyle();
        setTitle("Ass Editor settings");
        setModal(true);
        setResizable(false);
        setSize(1100, 700);

        dialogResult = DialogResult.None;
        JPopupMenu storagePopupMenu = getStoragePopupMenu();
        JPopupMenu scriptStoragePopupMenu = getScriptStoragePopupMenu();

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

        //=================================================== Styles collector
        JPanel collectorPanel = new JPanel(new BorderLayout());
        JPanel collectorColPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel collectorMainPanel = new JPanel(new GridLayout(1, 2));
        collectorPanel.add(collectorColPanel, BorderLayout.NORTH);
        collectorPanel.add(collectorMainPanel, BorderLayout.CENTER);
        collectorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblCol = new JLabel("Collection: ");
        modelColStyles = new DefaultComboBoxModel<>();
        cbColStyle = new JComboBox<>(modelColStyles);
        cbColStyle.setPreferredSize(new Dimension(300, 26));
        cbColStyle.addActionListener(this::cbColStyleActionPerformed);
        tfCol = new PlaceholderTextField();
        tfCol.setPlaceholder("New collection name");
        tfCol.setPreferredSize(new Dimension(300, 26));
        JButton btnAddCol = new JButton(Loader.language("storage.add.col", "Add"));
        btnAddCol.addActionListener(this::btnAddColActionPerformed);
        JButton btnRemoveCol = new JButton(Loader.language("storage.remove.col", "Remove"));
        btnRemoveCol.addActionListener(this::btnRemoveColActionPerformed);
        collectorColPanel.add(lblCol);
        collectorColPanel.add(cbColStyle);
        collectorColPanel.add(tfCol);
        collectorColPanel.add(btnAddCol);
        collectorColPanel.add(btnRemoveCol);

        JPanel storagePanel = new JPanel(new BorderLayout());
        storageModel = new DefaultListModel<>();
        storageList = new JList<>(storageModel);
        storageList.setComponentPopupMenu(storagePopupMenu);
        storageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        storagePanel.add(new JScrollPane(storageList), BorderLayout.CENTER);
        storagePanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Main styles"));
        collectorMainPanel.add(storagePanel);

        JPanel scriptStoragePanel = new JPanel(new BorderLayout());
        scriptStorageModel = new DefaultListModel<>();
        scriptStorageList = new JList<>(scriptStorageModel);
        scriptStorageList.setComponentPopupMenu(scriptStoragePopupMenu);
        scriptStorageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scriptStoragePanel.add(new JScrollPane(scriptStorageList), BorderLayout.CENTER);
        scriptStoragePanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Script styles"));
        scriptStorageList.addListSelectionListener(this::scriptStorageListSelectionChanged);
        collectorMainPanel.add(scriptStoragePanel);

        tabbedPane.add("Styles Collector", collectorPanel);

        //=================================================== Styles
        JPanel stylesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        stylesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel styleNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleNamePanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Style name"));
        tfStyleName = new PlaceholderTextField();
        tfStyleName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                tfStyleNameKeyReleased(e);
            }
        });
        tfStyleName.setPreferredSize(new Dimension(320, 26));
        tfStyleName.setPlaceholder("New style name");
        styleNamePanel.add(tfStyleName);
        stylesPanel.add(styleNamePanel);

        JPanel styleFontPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleFontPanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Font"));
        DefaultComboBoxModel<String> fontModel = new DefaultComboBoxModel<>();
        final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for(String s : localGraphicsEnvironment.getAvailableFontFamilyNames()){
            fontModel.addElement(s);
        }
        cbFont = new JComboBox<>(fontModel);
        cbFont.addActionListener(this::cbFontActionPerformed);
        cbFont.setPreferredSize(new Dimension(334, 26));
        styleFontPanel.add(cbFont);
        spinnerFontSizeModel = new SpinnerNumberModel(48, 1, null, 1);
        spinnerFontSize = new JSpinner(spinnerFontSizeModel);
        spinnerFontSize.addChangeListener(this::spinnerFontSizeChanged);
        spinnerFontSize.setPreferredSize(new Dimension(70, 26));
        styleFontPanel.add(spinnerFontSize);
        chkBold = new JCheckBox("Bold");
        chkBold.addActionListener(this::chkBoldActionPerformed);
        styleFontPanel.add(chkBold);
        chkItalic = new JCheckBox("Italic");
        chkItalic.addActionListener(this::chkItalicActionPerformed);
        styleFontPanel.add(chkItalic);
        chkUnderline = new JCheckBox("Underline");
        chkUnderline.addActionListener(this::chkUnderlineActionPerformed);
        styleFontPanel.add(chkUnderline);
        chkStrikeOut = new JCheckBox("StrikeOut");
        chkStrikeOut.addActionListener(this::chkStrikeOutActionPerformed);
        styleFontPanel.add(chkStrikeOut);
        stylesPanel.add(styleFontPanel);

        JPanel styleOutLinePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleOutLinePanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Outline"));
        JLabel lblOutline = new JLabel("Outline: ");
        styleOutLinePanel.add(lblOutline);
        spinnerOutlineModel = new SpinnerNumberModel(2f, 0f, null, 1f);
        spinnerOutline = new JSpinner(spinnerOutlineModel);
        spinnerOutline.addChangeListener(this::spinnerOutlineChanged);
        spinnerOutline.setPreferredSize(new Dimension(70, 26));
        styleOutLinePanel.add(spinnerOutline);
        JLabel lblShadow = new JLabel("Shadow: ");
        styleOutLinePanel.add(lblShadow);
        spinnerShadowModel = new SpinnerNumberModel(2f, 0f, null, 1f);
        spinnerShadow = new JSpinner(spinnerShadowModel);
        spinnerShadow.addChangeListener(this::spinnerShadowChanged);
        spinnerShadow.setPreferredSize(new Dimension(70, 26));
        styleOutLinePanel.add(spinnerShadow);
        chkOpaqueBox = new JCheckBox("Opaque box");
        chkOpaqueBox.addActionListener(this::chkOpaqueBoxActionPerformed);
        styleOutLinePanel.add(chkOpaqueBox);
        stylesPanel.add(styleOutLinePanel);

        JPanel styleScalePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleScalePanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Scale %"));
        JLabel lblScaleX = new JLabel("X: ");
        styleScalePanel.add(lblScaleX);
        spinnerScaleXModel = new SpinnerNumberModel(100, 0, null, 1);
        spinnerScaleX = new JSpinner(spinnerScaleXModel);
        spinnerScaleX.addChangeListener(this::spinnerScaleXChanged);
        spinnerScaleX.setPreferredSize(new Dimension(70, 26));
        styleScalePanel.add(spinnerScaleX);
        JLabel lblScaleY = new JLabel("Y: ");
        styleScalePanel.add(lblScaleY);
        spinnerScaleYModel = new SpinnerNumberModel(100, 0, null, 1);
        spinnerScaleY = new JSpinner(spinnerScaleYModel);
        spinnerScaleY.addChangeListener(this::spinnerScaleYChanged);
        spinnerScaleY.setPreferredSize(new Dimension(70, 26));
        styleScalePanel.add(spinnerScaleY);
        stylesPanel.add(styleScalePanel);

        JPanel styleAngleZPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleAngleZPanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Angle Z °"));
        spinnerAngleZModel = new SpinnerNumberModel(0, null, null, 1);
        spinnerAngleZ = new JSpinner(spinnerAngleZModel);
        spinnerAngleZ.addChangeListener(this::spinnerAngleZChanged);
        spinnerAngleZ.setPreferredSize(new Dimension(70, 26));
        styleAngleZPanel.add(spinnerAngleZ);
        stylesPanel.add(styleAngleZPanel);

        JPanel styleSpacingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleSpacingPanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Spacing px"));
        spinnerSpacingModel = new SpinnerNumberModel(0.0, 0.0, null, 1);
        spinnerSpacing = new JSpinner(spinnerSpacingModel);
        spinnerSpacing.addChangeListener(this::spinnerSpacingChanged);
        spinnerSpacing.setPreferredSize(new Dimension(70, 26));
        styleSpacingPanel.add(spinnerSpacing);
        stylesPanel.add(styleSpacingPanel);

        JPanel styleEncodingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleEncodingPanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Encoding"));
        encodingModel = new DefaultComboBoxModel<>();
        for(AssEncoding enc : AssEncoding.values()){
            encodingModel.addElement(enc);
        }
        cbEncoding = new JComboBox<>(encodingModel);
        cbEncoding.addActionListener(this::cbEncodingActionPerformed);
        cbEncoding.setPreferredSize(new Dimension(240, 26));
        styleEncodingPanel.add(cbEncoding);
        stylesPanel.add(styleEncodingPanel);

        JPanel styleColorsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleColorsPanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Colors"));
        cvTextColor = new ColorView("Text", Color.white);
        cvTextColor.addColorViewListener(this::cvTextColorViewChanged);
        cvKaraokeColor = new ColorView("Karaoke", Color.red);
        cvKaraokeColor.addColorViewListener(this::cvKaraokeColorViewChanged);
        cvOutlineColor = new ColorView("Outline", Color.black);
        cvOutlineColor.addColorViewListener(this::cvOutlineColorViewChanged);
        cvShadowColor = new ColorView("Shadow", Color.black);
        cvShadowColor.addColorViewListener(this::cvShadowColorViewChanged);
        styleColorsPanel.add(cvTextColor);
        styleColorsPanel.add(cvKaraokeColor);
        styleColorsPanel.add(cvOutlineColor);
        styleColorsPanel.add(cvShadowColor);
        styleColorsPanel.setPreferredSize(new Dimension(500, 102));
        stylesPanel.add(styleColorsPanel);

        JPanel styleMarginsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleMarginsPanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Margins"));
        JPanel panML = new JPanel(new GridLayout(2,1));
        JLabel lblML = new JLabel("Left");
        tfML = new PlaceholderTextField();
        tfML.setPlaceholder("0");
        tfML.setText("30");
        tfML.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                tfMLKeyReleased(e);
            }
        });
        panML.add(lblML);
        panML.add(tfML);
        JPanel panMR = new JPanel(new GridLayout(2,1));
        JLabel lblMR = new JLabel("Right");
        tfMR = new PlaceholderTextField();
        tfMR.setPlaceholder("0");
        tfMR.setText("30");
        tfMR.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                tfMRKeyReleased(e);
            }
        });
        panMR.add(lblMR);
        panMR.add(tfMR);
        JPanel panMV = new JPanel(new GridLayout(2,1));
        JLabel lblMV = new JLabel("Vertical");
        tfMV = new PlaceholderTextField();
        tfMV.setPlaceholder("0");
        tfMV.setText("30");
        tfMV.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                tfMVKeyReleased(e);
            }
        });
        panMV.add(lblMV);
        panMV.add(tfMV);
        styleMarginsPanel.add(panML);
        styleMarginsPanel.add(panMR);
        styleMarginsPanel.add(panMV);
        styleMarginsPanel.setPreferredSize(new Dimension(444, 102));
        stylesPanel.add(styleMarginsPanel);

        JPanel styleAlignmentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        styleAlignmentPanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Alignment"));
        rbAlignment1 = new JRadioButton("1");
        rbAlignment2 = new JRadioButton("2");
        rbAlignment3 = new JRadioButton("3");
        rbAlignment4 = new JRadioButton("4");
        rbAlignment5 = new JRadioButton("5");
        rbAlignment6 = new JRadioButton("6");
        rbAlignment7 = new JRadioButton("7");
        rbAlignment8 = new JRadioButton("8");
        rbAlignment9 = new JRadioButton("9");
        ButtonGroup bgAlignment = new ButtonGroup();
        bgAlignment.add(rbAlignment1);
        bgAlignment.add(rbAlignment2);
        bgAlignment.add(rbAlignment3);
        bgAlignment.add(rbAlignment4);
        bgAlignment.add(rbAlignment5);
        bgAlignment.add(rbAlignment6);
        bgAlignment.add(rbAlignment7);
        bgAlignment.add(rbAlignment8);
        bgAlignment.add(rbAlignment9);
        rbAlignment2.setSelected(true);
        rbAlignment1.addActionListener(this::rbAlignment1ActionPerformed);
        rbAlignment2.addActionListener(this::rbAlignment2ActionPerformed);
        rbAlignment3.addActionListener(this::rbAlignment3ActionPerformed);
        rbAlignment4.addActionListener(this::rbAlignment4ActionPerformed);
        rbAlignment5.addActionListener(this::rbAlignment5ActionPerformed);
        rbAlignment6.addActionListener(this::rbAlignment6ActionPerformed);
        rbAlignment7.addActionListener(this::rbAlignment7ActionPerformed);
        rbAlignment8.addActionListener(this::rbAlignment8ActionPerformed);
        rbAlignment9.addActionListener(this::rbAlignment9ActionPerformed);
        JPanel panAlign = new JPanel(new GridLayout(3,3));
        panAlign.add(rbAlignment7);
        panAlign.add(rbAlignment8);
        panAlign.add(rbAlignment9);
        panAlign.add(rbAlignment4);
        panAlign.add(rbAlignment5);
        panAlign.add(rbAlignment6);
        panAlign.add(rbAlignment1);
        panAlign.add(rbAlignment2);
        panAlign.add(rbAlignment3);
        styleAlignmentPanel.add(panAlign);
        stylesPanel.add(styleAlignmentPanel);

        JPanel stylePreviewPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        stylePreviewPanel.setBorder(BorderFactory.createTitledBorder(bdTab, "Preview"));
        // TODO make a better style preview
        previewASS = new PreviewASS(426, 240, 8,
                DrawColor.black.getColor(), DrawColor.chocolate.getColor());
        stylePreviewPanel.add(previewASS);
        stylesPanel.add(stylePreviewPanel);

        tabbedPane.add("Style Editor", stylesPanel);

        //=================================================== Karaoke effects creator
        //=================================================== Actor dictionary
        //=================================================== Bookmark customizer
    }

    public JPopupMenu getStoragePopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem mScCopy = new JMenuItem(Loader.language("storage.copy.from.ext.script", "Copy from external script"));
        mScCopy.addActionListener(this::btnScCopyActionPerformed);
        mScCopy.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideCopy.png"))));
        popupMenu.add(mScCopy);
        JMenuItem mScRemove = new JMenuItem(Loader.language("storage.remove", "Remove"));
        mScRemove.addActionListener(this::btnScRemoveActionPerformed);
        mScRemove.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideCircleX.png"))));
        popupMenu.add(mScRemove);
        JMenuItem mScToScript = new JMenuItem(Loader.language("storage.copy.to.script", "Copy to script"));
        mScToScript.addActionListener(this::btnScToScriptActionPerformed);
        mScToScript.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideDownload.png"))));
        popupMenu.add(mScToScript);

        return popupMenu;
    }

    public JPopupMenu getScriptStoragePopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem mScSNew = new JMenuItem(Loader.language("storage.new.style", "New"));
        mScSNew.addActionListener(this::btnScSNewActionPerformed);
        mScSNew.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideCirclePlus.png"))));
        popupMenu.add(mScSNew);
        JMenuItem mScSDuplicate = new JMenuItem(Loader.language("storage.duplicate", "Duplicate"));
        mScSDuplicate.addActionListener(this::btnScSDuplicateActionPerformed);
        mScSDuplicate.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideBookCopy.png"))));
        popupMenu.add(mScSDuplicate);
        JMenuItem mScSSkeleton = new JMenuItem(Loader.language("storage.skeleton", "Skeleton"));
        mScSSkeleton.addActionListener(this::btnScSSkeletonActionPerformed);
        mScSSkeleton.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideSparkles.png"))));
        popupMenu.add(mScSSkeleton);
        JMenuItem mScSCopy = new JMenuItem(Loader.language("storage.copy.from.ext.script", "Copy from external script"));
        mScSCopy.addActionListener(this::btnScSCopyActionPerformed);
        mScSCopy.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideCopy.png"))));
        popupMenu.add(mScSCopy);
        JMenuItem mScSRemove = new JMenuItem(Loader.language("storage.remove", "Remove"));
        mScSRemove.addActionListener(this::btnScSRemoveActionPerformed);
        mScSRemove.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideCircleX.png"))));
        popupMenu.add(mScSRemove);
        JMenuItem mScSToStorage = new JMenuItem(Loader.language("storage.copy.to.storage", "Copy to storage"));
        mScSToStorage.addActionListener(this::btnScSToStorageActionPerformed);
        mScSToStorage.setIcon(new ImageIcon(Objects.requireNonNull(
                AssEditorDialog.class.getResource("/images/16-LucideDownload.png"))));
        popupMenu.add(mScSToStorage);

        return popupMenu;
    }

    public void showDialog(Settings settings) {
        if(settings == null) return;
        this.settings = settings;
        setLocationRelativeTo(exchange.getMainFrame());
        setVisible(true);
    }

    /** SETTER */
    public void initAssStylesCollections(){
        modelColStyles.removeAllElements();
        modelColStyles.addAll(exchange.getStylesCollections());
        if(modelColStyles.getSize() > 0){
            boolean found = false;
            for(int i=0; i<modelColStyles.getSize(); i++){
                AssStylesCollection col = modelColStyles.getElementAt(i);
                if(col.hashCode() == exchange.getCurrentStylesCollection().hashCode()){
                    cbColStyle.setSelectedIndex(i);
                    found = true;
                    break;
                }
            }
            if(!found){
                cbColStyle.setSelectedIndex(0);
            }
            storageList.setSelectedIndex(0);
        }
        if(storageList.getSelectedIndex() != -1) {
            for(int i=0; i<storageList.getSelectedIndices().length; i++) {
                scriptStorageModel.addElement(storageModel.get(i));
            }
            boolean found = false;
            for(int i=0; i<scriptStorageModel.size(); i++) {
                AssStyle style = scriptStorageModel.get(i);
                if(style.hashCode() == exchange.getCurrentStyle().hashCode()) {
                    scriptStorageList.setSelectedIndex(i);
                    found = true;
                    break;
                }
            }
            if(!found && !scriptStorageModel.isEmpty()) {
                scriptStorageList.setSelectedIndex(0);
            }
        }
        currentStyle = scriptStorageList.getSelectedIndex() == -1 ?
                exchange.getCurrentStyle() :
                scriptStorageModel.get(storageList.getSelectedIndex());
        updateFont();
        previewASS.setCurrentStyle(currentStyle);
        previewASS.changeStyleAndSentence();
    }

    /** GETTER */
    public void replaceAssStylesCollections(){
        exchange.getStylesCollections().clear();
        for(int i=0; i<modelColStyles.getSize(); i++){
            exchange.getStylesCollections().add(modelColStyles.getElementAt(i));
        }
        if(cbColStyle.getSelectedItem() instanceof AssStylesCollection v){
            exchange.setCurrentStylesCollection(v);
        }
        exchange.setCurrentStyle(currentStyle);
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }

    // To use with ElementsComboBox combobox
    public List<AssStyle> getStyles() {
        List<AssStyle> list = new ArrayList<>();
        for(int i=0; i<scriptStorageModel.getSize(); i++){
            list.add(scriptStorageModel.getElementAt(i));
        }
        return list;
    }

    // To use with ElementsComboBox combobox
    public void setStyles(List<AssStyle> list){
        scriptStorageModel.clear();
        scriptStorageModel.addAll(list);
    }

    public Settings getSettings() {
        return settings;
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

    public void cbColStyleActionPerformed(ActionEvent e) {
        if(cbColStyle.getSelectedItem() instanceof AssStylesCollection c){
            storageModel.removeAllElements();
            for(AssStyle s : c.getStyles()){
                storageModel.addElement(s);
            }
        }
    }

    public void btnAddColActionPerformed(ActionEvent e) {
        AssStyle newStyle = new AssStyle();
        AssStylesCollection newCol = new AssStylesCollection(tfCol.getText());
        newCol.getStyles().add(newStyle);
        modelColStyles.addElement(newCol);
    }

    public void btnRemoveColActionPerformed(ActionEvent e) {
        if(cbColStyle.getSelectedIndex() != -1) {
            AssStylesCollection c = cbColStyle.getItemAt((cbColStyle.getSelectedIndex()));
            int z = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to remove this collection?\n" + c.getName(),
                    "Confirm action",
                    JOptionPane.YES_NO_OPTION
            );
            if(z == JOptionPane.YES_OPTION) {
                storageModel.removeAllElements();
                modelColStyles.removeElement(c);
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
            ChooseStylesDialog cs = new ChooseStylesDialog(exchange.getMainFrame());
            cs.showDialog(ass.getStyles());
            if(cs.getDialogResult() == DialogResult.OK) {
                storageModel.addAll(cs.getStyles());
            }
        }
    }

    public void btnScRemoveActionPerformed(ActionEvent e) {
        if(storageList.getSelectedIndex() != -1) {
            int z = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to remove these storage styles?",
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
                scriptStorageModel.addElement(storageModel.get(i));
            }
        }
    }

    public void btnScSNewActionPerformed(ActionEvent e) {
        AssStyle assStyle = new AssStyle();
        scriptStorageModel.addElement(assStyle);
    }

    public void btnScSDuplicateActionPerformed(ActionEvent e) {
        if(scriptStorageList.getSelectedIndex() != -1) {
            AssStyle selected = scriptStorageModel.getElementAt(scriptStorageList.getSelectedIndex());
            AssStyle newStyle = new AssStyle();
            newStyle.setName(selected.getName() + " copy");
            newStyle.setAssFont(selected.getAssFont());
            newStyle.setTextColor(selected.getTextColor());
            newStyle.setKaraokeColor(selected.getKaraokeColor());
            newStyle.setOutlineColor(selected.getOutlineColor());
            newStyle.setShadowColor(selected.getShadowColor());
            newStyle.setOutline(selected.getOutline());
            newStyle.setShadow(selected.getShadow());
            newStyle.setAlignment(selected.getAlignment());
            newStyle.setScaleX(selected.getScaleX());
            newStyle.setScaleY(selected.getScaleY());
            newStyle.setAngleZ(selected.getAngleZ());
            newStyle.setSpacing(selected.getSpacing());
            newStyle.setBorderStyle(selected.getBorderStyle());
            newStyle.setMarginL(selected.getMarginL());
            newStyle.setMarginR(selected.getMarginR());
            newStyle.setMarginV(selected.getMarginV());
            newStyle.setEncoding(selected.getEncoding());
            scriptStorageModel.addElement(newStyle);
        }
    }

    public void btnScSSkeletonActionPerformed(ActionEvent e) {

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
            ChooseStylesDialog cs = new ChooseStylesDialog(exchange.getMainFrame());
            cs.showDialog(ass.getStyles());
            if(cs.getDialogResult() == DialogResult.OK) {
                scriptStorageModel.addAll(cs.getStyles());
            }
        }
    }

    public void btnScSRemoveActionPerformed(ActionEvent e) {
        if(scriptStorageList.getSelectedIndex() != -1) {
            int z = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to remove these storage styles?",
                    "Confirm action",
                    JOptionPane.YES_NO_OPTION
            );
            if(z == JOptionPane.YES_OPTION) {
                for(int i=scriptStorageList.getSelectedIndices().length-1; i>=0; i--) {
                    scriptStorageModel.remove(scriptStorageList.getSelectedIndices()[i]);
                }
            }
        }
    }

    public void btnScSToStorageActionPerformed(ActionEvent e) {
        if(scriptStorageList.getSelectedIndex() != -1) {
            for(int i=0; i<scriptStorageList.getSelectedIndices().length; i++) {
                storageModel.addElement(scriptStorageModel.get(i));
            }
        }
    }

    public void scriptStorageListSelectionChanged(ListSelectionEvent e) {
        int index = scriptStorageList.getSelectedIndex();
        if(index == -1) return;
        currentStyle = scriptStorageModel.getElementAt(index);
        tfStyleName.setText(currentStyle.getName());
        AssFont assFont = currentStyle.getAssFont();
        try{cbFont.setSelectedItem(assFont.getFont().getFamily());}catch(Exception _){}
        spinnerFontSize.setValue(assFont.getFont().getSize());
        chkBold.setSelected(assFont.isBold());
        chkItalic.setSelected(assFont.isItalic());
        chkUnderline.setSelected(assFont.isUnderline());
        chkStrikeOut.setSelected(assFont.isStrikeout());
        spinnerOutline.setValue(currentStyle.getOutline());
        spinnerShadow.setValue(currentStyle.getShadow());
        chkOpaqueBox.setSelected(currentStyle.getBorderStyle() == 3);
        spinnerScaleX.setValue(currentStyle.getScaleX());
        spinnerScaleY.setValue(currentStyle.getScaleY());
        spinnerAngleZ.setValue(currentStyle.getAngleZ());
        spinnerSpacing.setValue(currentStyle.getSpacing());
        cvTextColor.setColor(currentStyle.getTextColor().getColor());
        cvKaraokeColor.setColor(currentStyle.getKaraokeColor().getColor());
        cvOutlineColor.setColor(currentStyle.getOutlineColor().getColor());
        cvShadowColor.setColor(currentStyle.getShadowColor().getColor());
        tfML.setText(Integer.toString(currentStyle.getMarginL()));
        tfMR.setText(Integer.toString(currentStyle.getMarginR()));
        tfMV.setText(Integer.toString(currentStyle.getMarginV()));
        switch(currentStyle.getAlignment().getNumber()){
            case 1 -> rbAlignment1.setSelected(true);
            case 2 -> rbAlignment2.setSelected(true);
            case 3 -> rbAlignment3.setSelected(true);
            case 4 -> rbAlignment4.setSelected(true);
            case 5 -> rbAlignment5.setSelected(true);
            case 6 -> rbAlignment6.setSelected(true);
            case 7 -> rbAlignment7.setSelected(true);
            case 8 -> rbAlignment8.setSelected(true);
            case 9 -> rbAlignment9.setSelected(true);
        }
        try{
            int codepage = currentStyle.getEncoding();
            cbEncoding.setSelectedItem(AssEncoding.search(codepage));
        }catch(Exception _){}
        updateFont();
    }

    public void tfStyleNameKeyReleased(KeyEvent e){
        String styleName = tfStyleName.getText();
        if(styleName.isEmpty()) return;
        if(styleName.matches("[\\w\\s]*")) {
            currentStyle.setName(styleName);
        }
    }

    private void updateFont(){
        String fontFamily = currentStyle.getAssFont().getFont().getFamily();
        if(cbFont.getSelectedItem() instanceof String v){
            fontFamily = v;
        }
        AssFont assFont = new AssFont(
                fontFamily,
                spinnerFontSizeModel.getNumber().floatValue(),
                chkBold.isSelected(),
                chkItalic.isSelected(),
                chkUnderline.isSelected(),
                chkStrikeOut.isSelected()
        );
        currentStyle.setAssFont(assFont);
    }

    public void cbFontActionPerformed(ActionEvent e) {
        updateFont();
        previewASS.setCurrentStyle(currentStyle);
    }

    public void spinnerFontSizeChanged(ChangeEvent e) {
        updateFont();
        previewASS.setCurrentStyle(currentStyle);
    }

    public void chkBoldActionPerformed(ActionEvent e) {
        updateFont();
        previewASS.setCurrentStyle(currentStyle);
    }

    public void chkItalicActionPerformed(ActionEvent e) {
        updateFont();
        previewASS.setCurrentStyle(currentStyle);
    }

    public void chkUnderlineActionPerformed(ActionEvent e) {
        updateFont();
        previewASS.setCurrentStyle(currentStyle);
    }

    public void chkStrikeOutActionPerformed(ActionEvent e) {
        updateFont();
        previewASS.setCurrentStyle(currentStyle);
    }

    public void spinnerOutlineChanged(ChangeEvent e) {
        currentStyle.setOutline(spinnerOutlineModel.getNumber().floatValue());
    }

    public void spinnerShadowChanged(ChangeEvent e) {
        currentStyle.setShadow(spinnerShadowModel.getNumber().floatValue());
    }

    public void chkOpaqueBoxActionPerformed(ActionEvent e) {
        currentStyle.setBorderStyle(chkOpaqueBox.isSelected() ? 3 : 1);
    }

    public void spinnerScaleXChanged(ChangeEvent e) {
        currentStyle.setScaleX(spinnerScaleXModel.getNumber().intValue());
    }

    public void spinnerScaleYChanged(ChangeEvent e) {
        currentStyle.setScaleY(spinnerScaleYModel.getNumber().intValue());
    }

    public void spinnerAngleZChanged(ChangeEvent e) {
        currentStyle.setAngleZ(spinnerAngleZModel.getNumber().floatValue());
    }

    public void spinnerSpacingChanged(ChangeEvent e) {
        currentStyle.setSpacing(spinnerSpacingModel.getNumber().floatValue());
    }

    public void cvTextColorViewChanged(ColorView.ColorViewEvent e) {
        currentStyle.setTextColor(new AssColor(e.getColor()));
    }

    public void cvKaraokeColorViewChanged(ColorView.ColorViewEvent e) {
        currentStyle.setKaraokeColor(new AssColor(e.getColor()));
    }

    public void cvOutlineColorViewChanged(ColorView.ColorViewEvent e) {
        currentStyle.setOutlineColor(new AssColor(e.getColor()));
    }

    public void cvShadowColorViewChanged(ColorView.ColorViewEvent e) {
        currentStyle.setShadowColor(new AssColor(e.getColor()));
    }

    public void tfMLKeyReleased(KeyEvent e) {
        String v = tfML.getText();
        if(!v.isEmpty() && v.matches("\\d+")){
            currentStyle.setMarginL(Integer.parseInt(v));
        }
    }

    public void tfMRKeyReleased(KeyEvent e) {
        String v = tfMR.getText();
        if(!v.isEmpty() && v.matches("\\d+")){
            currentStyle.setMarginR(Integer.parseInt(v));
        }
    }

    public void tfMVKeyReleased(KeyEvent e) {
        String v = tfMV.getText();
        if(!v.isEmpty() && v.matches("\\d+")){
            currentStyle.setMarginV(Integer.parseInt(v));
        }
    }

    public void rbAlignment1ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(1));
    }

    public void rbAlignment2ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(2));
    }

    public void rbAlignment3ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(3));
    }

    public void rbAlignment4ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(4));
    }

    public void rbAlignment5ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(5));
    }

    public void rbAlignment6ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(6));
    }

    public void rbAlignment7ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(7));
    }

    public void rbAlignment8ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(8));
    }

    public void rbAlignment9ActionPerformed(ActionEvent e) {
        currentStyle.setAlignment(new AssAlignment(9));
    }

    public void cbEncodingActionPerformed(ActionEvent e) {
        if(encodingModel.getSize() == 0) return;
        if(cbEncoding.getSelectedItem() instanceof AssEncoding v){
            currentStyle.setEncoding(v.getCodepage());
        }
    }
}
