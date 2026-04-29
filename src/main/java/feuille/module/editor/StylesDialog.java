package feuille.module.editor;

import feuille.module.editor.assa.AssStyle;
import feuille.module.editor.assa.AssStylePreview;
import feuille.module.editor.assa.render.FontRenderer;
import feuille.util.DialogResult;
import feuille.util.Loader;
import feuille.util.assa.AssColor;
import feuille.util.assa.AssColorScheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StylesDialog extends JDialog {

    private List<AssStyle> styles;
    private AssStyle selectedStyle;
    private DialogResult dialogResult;

    private final AssStylePreview preview;
    private final JTextField previewText;

    private final DefaultListModel<AssStyle> lListModel;
    private final DefaultListModel<AssStyle> rListModel;

    public StylesDialog(Frame owner) {
        super(owner);
        setModal(true);
        setResizable(false);
        setTitle(Loader.language("dialog.styles", "Styles"));
        setSize(900, 600);
        setLocationRelativeTo(owner);

        selectedStyle = new AssStyle();
        dialogResult = DialogResult.None;

        preview = new AssStylePreview();
        preview.setPreferredSize(new Dimension(900, 180));
        previewText = new JTextField("LoliSub is a smart project!");

        JButton btnOK = new JButton(Loader.language("message.ok", "OK"));
        JButton btnCancel = new JButton(Loader.language("message.cancel", "Cancel"));

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

        setLayout(new BorderLayout());

        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.add(preview, BorderLayout.CENTER);
        previewPanel.add(previewText, BorderLayout.SOUTH);

        JTabbedPane tabs = new JTabbedPane();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel placeholderPanel = new JPanel();
        JPanel commandPanel = new JPanel(new GridLayout(1, 2, 2, 2));

        commandPanel.add(btnCancel);
        commandPanel.add(btnOK);
        bottomPanel.add(placeholderPanel, BorderLayout.CENTER);
        bottomPanel.add(commandPanel, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(previewPanel, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Tab collection
        JPanel tabCollectionPanel = new JPanel(new GridLayout(1,2));
        tabs.add(tabCollectionPanel, 0);
        tabs.setTitleAt(0, "Collections");
        // --- LEFT ---
        lListModel = new DefaultListModel<>();
        JList<AssStyle> lList = new JList<>(lListModel);
        JComboBox<String> cbCollections = new JComboBox<>();
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel lBottomPanel = new JPanel(new BorderLayout());
        JPanel lBottomPlaceholderPanel = new JPanel();
        JButton rightButton = new JButton("<<");
        lBottomPanel.add(rightButton, BorderLayout.EAST);
        lBottomPanel.add(lBottomPlaceholderPanel, BorderLayout.CENTER);
        leftPanel.add(cbCollections, BorderLayout.NORTH);
        leftPanel.add(lList, BorderLayout.CENTER);
        leftPanel.add(lBottomPanel, BorderLayout.SOUTH);
        tabCollectionPanel.add(leftPanel);
        // --- RIGHT ---
        rListModel = new DefaultListModel<>();
        JList<AssStyle> rList = new JList<>(rListModel);
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel rBottomPanel = new JPanel(new BorderLayout());
        JPanel rBottomPlaceholderPanel = new JPanel();
        JPanel rTopPlaceholderPanel = new JPanel();
        rTopPlaceholderPanel.setPreferredSize(new Dimension(900/2, 24));
        JButton leftButton = new JButton(">>");
        rBottomPanel.add(leftButton, BorderLayout.WEST);
        rBottomPanel.add(rBottomPlaceholderPanel, BorderLayout.CENTER);
        rightPanel.add(rTopPlaceholderPanel, BorderLayout.NORTH);
        rightPanel.add(rList, BorderLayout.CENTER);
        rightPanel.add(rBottomPanel, BorderLayout.SOUTH);
        tabCollectionPanel.add(rightPanel);

        rightButton.addActionListener(e -> {
            if(rList.getSelectedIndex() == -1) return;
            lListModel.addElement(rList.getSelectedValue());
        });
        leftButton.addActionListener(e -> {
            if(lList.getSelectedIndex() == -1) return;
            rListModel.addElement(lList.getSelectedValue());
        });

        // Tab font info
        JPanel tabFontPanel = new JPanel(new GridLayout(6, 2, 2, 2));
        tabs.add(tabFontPanel, 1);
        tabs.setTitleAt(1, "Font attributes");
        JLabel lblStyleName = new JLabel("Style name : ");
        JTextField tfStyleName = new JTextField();
        JLabel lblFontName = new JLabel("Font name : ");
        DefaultComboBoxModel<Font> cbFontModel = new DefaultComboBoxModel<>();
        JComboBox<Font> cbFontName = new JComboBox<>(cbFontModel);
        JLabel lblFontSize = new JLabel("Font size : ");
        SpinnerNumberModel spnFontSize = new SpinnerNumberModel(52, 4, 100_000, 1);
        JSpinner spFontSize = new JSpinner(spnFontSize);
        JToggleButton tgBold = new JToggleButton("Bold");
        JToggleButton tgItalic = new JToggleButton("Italic");
        JToggleButton tgUnderline = new JToggleButton("Underline");
        JToggleButton tgStrikeout = new JToggleButton("Strikeout");
        JLabel lblEncoding = new JLabel("Encoding : ");
        JComboBox<String> cbEncoding = new JComboBox<>();
        tabFontPanel.add(lblStyleName);
        tabFontPanel.add(tfStyleName);
        tabFontPanel.add(lblFontName);
        tabFontPanel.add(cbFontName);
        tabFontPanel.add(lblFontSize);
        tabFontPanel.add(spFontSize);
        tabFontPanel.add(tgBold);
        tabFontPanel.add(tgItalic);
        tabFontPanel.add(tgUnderline);
        tabFontPanel.add(tgStrikeout);
        tabFontPanel.add(lblEncoding);
        tabFontPanel.add(cbEncoding);

        cbFontName.setRenderer(new FontRenderer());
        for(Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
            cbFontModel.addElement(f);
        }

        cbFontName.addActionListener((e)->{
            Font f = cbFontModel.getElementAt(cbFontName.getSelectedIndex());
            selectedStyle.getAssFont().setFont(new Font(f.getName(), f.getStyle(), f.getSize()));
            selectedStyle.getAssFont().setSize(spnFontSize.getNumber().floatValue());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        spFontSize.addChangeListener(e -> {
            selectedStyle.getAssFont().setSize(spnFontSize.getNumber().floatValue());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        tgBold.addActionListener(e -> {
            selectedStyle.getAssFont().setBold(tgBold.isSelected());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        tgItalic.addActionListener(e -> {
            selectedStyle.getAssFont().setItalic(tgItalic.isSelected());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        tgUnderline.addActionListener(e -> {
            selectedStyle.getAssFont().setUnderline(tgUnderline.isSelected());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        tgStrikeout.addActionListener(e -> {
            selectedStyle.getAssFont().setStrikeout(tgStrikeout.isSelected());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        // Tab position
        JPanel tabPositionPanel = new JPanel(new GridLayout(4, 6, 2, 2));
        tabs.add(tabPositionPanel, 2);
        tabs.setTitleAt(2, "Position");
        JLabel lblML = new JLabel("Margin L : ");
        SpinnerNumberModel spnML = new SpinnerNumberModel(1, 1, 1_000_000, 1);
        JSpinner spML = new JSpinner(spnML);
        tabPositionPanel.add(lblML);
        tabPositionPanel.add(spML);
        JLabel lblMR = new JLabel("Margin R : ");
        SpinnerNumberModel spnMR = new SpinnerNumberModel(1, 1, 1_000_000, 1);
        JSpinner spMR = new JSpinner(spnMR);
        tabPositionPanel.add(lblMR);
        tabPositionPanel.add(spMR);
        JLabel lblMV = new JLabel("Margin V : ");
        SpinnerNumberModel spnMV = new SpinnerNumberModel(1, 1, 1_000_000, 1);
        JSpinner spMV = new JSpinner(spnMV);
        tabPositionPanel.add(lblMV);
        tabPositionPanel.add(spMV);
        JLabel lblAN7 = new JLabel("Top.Left");
        JToggleButton tbAN7 = new JToggleButton("7");
        tabPositionPanel.add(lblAN7);
        tabPositionPanel.add(tbAN7);
        JLabel lblAN8 = new JLabel("Top.Center");
        JToggleButton tbAN8 = new JToggleButton("8");
        tabPositionPanel.add(lblAN8);
        tabPositionPanel.add(tbAN8);
        JLabel lblAN9 = new JLabel("Top.Right");
        JToggleButton tbAN9 = new JToggleButton("9");
        tabPositionPanel.add(lblAN9);
        tabPositionPanel.add(tbAN9);
        JLabel lblAN4 = new JLabel("Middle.Left");
        JToggleButton tbAN4 = new JToggleButton("4");
        tabPositionPanel.add(lblAN4);
        tabPositionPanel.add(tbAN4);
        JLabel lblAN5 = new JLabel("Middle.Center");
        JToggleButton tbAN5 = new JToggleButton("5");
        tabPositionPanel.add(lblAN5);
        tabPositionPanel.add(tbAN5);
        JLabel lblAN6 = new JLabel("Middle.Right");
        JToggleButton tbAN6 = new JToggleButton("6");
        tabPositionPanel.add(lblAN6);
        tabPositionPanel.add(tbAN6);
        JLabel lblAN1 = new JLabel("Bottom.Left");
        JToggleButton tbAN1 = new JToggleButton("1");
        tabPositionPanel.add(lblAN1);
        tabPositionPanel.add(tbAN1);
        JLabel lblAN2 = new JLabel("Bottom.Center");
        JToggleButton tbAN2 = new JToggleButton("2");
        tabPositionPanel.add(lblAN2);
        tabPositionPanel.add(tbAN2);
        JLabel lblAN3 = new JLabel("Bottom.Right");
        JToggleButton tbAN3 = new JToggleButton("3");
        tabPositionPanel.add(lblAN3);
        tabPositionPanel.add(tbAN3);
        ButtonGroup bgAlignment = new ButtonGroup();
        bgAlignment.add(tbAN1);
        bgAlignment.add(tbAN2);
        bgAlignment.add(tbAN3);
        bgAlignment.add(tbAN4);
        bgAlignment.add(tbAN5);
        bgAlignment.add(tbAN6);
        bgAlignment.add(tbAN7);
        bgAlignment.add(tbAN8);
        bgAlignment.add(tbAN9);
        tbAN2.setSelected(true);

        // Tab parameters
        JPanel tabParametersPanel = new JPanel(new GridLayout(7, 2, 2, 2));
        tabs.add(tabParametersPanel, 3);
        tabs.setTitleAt(3, "Parameters");
        JLabel lblBord = new JLabel("Border : ");
        SpinnerNumberModel spnBord = new SpinnerNumberModel(1, 1, 100_000, 1);
        JSpinner spBord = new JSpinner(spnBord);
        tabParametersPanel.add(lblBord);
        tabParametersPanel.add(spBord);
        spBord.addChangeListener((e)->{
            selectedStyle.setOutline(spnBord.getNumber().intValue());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        JLabel lblShad = new JLabel("Shadow : ");
        SpinnerNumberModel spnShad = new SpinnerNumberModel(1, 1, 100_000, 1);
        JSpinner spShad = new JSpinner(spnShad);
        tabParametersPanel.add(lblShad);
        tabParametersPanel.add(spShad);
        spShad.addChangeListener((e)->{
            selectedStyle.setShadow(spnShad.getNumber().intValue());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        JLabel lblScaleX = new JLabel("Scale X (%) : ");
        SpinnerNumberModel spnScaleX = new SpinnerNumberModel(1, 1, 1_000_000, 1);
        JSpinner spScaleX = new JSpinner(spnScaleX);
        tabParametersPanel.add(lblScaleX);
        tabParametersPanel.add(spScaleX);
        spScaleX.addChangeListener((e)->{
            selectedStyle.setScaleX(spnScaleX.getNumber().intValue());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        JLabel lblScaleY = new JLabel("Scale Y (%) : ");
        SpinnerNumberModel spnScaleY = new SpinnerNumberModel(1, 1, 1_000_000, 1);
        JSpinner spScaleY = new JSpinner(spnScaleY);
        tabParametersPanel.add(lblScaleY);
        tabParametersPanel.add(spScaleY);
        spScaleY.addChangeListener((e)->{
            selectedStyle.setScaleY(spnScaleY.getNumber().intValue());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        JLabel lblAngle = new JLabel("Angle (°) : ");
        SpinnerNumberModel spnAngle = new SpinnerNumberModel(1, 1, 1_000_000, 1);
        JSpinner spAngle = new JSpinner(spnAngle);
        tabParametersPanel.add(lblAngle);
        tabParametersPanel.add(spAngle);
        spAngle.addChangeListener((e)->{
            selectedStyle.setAngleZ(spnAngle.getNumber().intValue());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        JLabel lblSpacing = new JLabel("Spacing (pixels) : ");
        SpinnerNumberModel spnSpacing = new SpinnerNumberModel(1, 1, 100_000, 1);
        JSpinner spSpacing = new JSpinner(spnSpacing);
        tabParametersPanel.add(lblSpacing);
        tabParametersPanel.add(spSpacing);
        spSpacing.addChangeListener((e)->{
            selectedStyle.setSpacing(spnSpacing.getNumber().intValue());
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        JCheckBox chkOpaqueBox= new JCheckBox("Opaque box");
        tabParametersPanel.add(chkOpaqueBox);
        chkOpaqueBox.addChangeListener((e)->{
            selectedStyle.setBorderStyle(chkOpaqueBox.isSelected() ? 3 : 1);
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        // Tab colors
        JPanel tabColorsPanel = new JPanel(new GridLayout(5, 5, 2, 2));
        tabs.add(tabColorsPanel, 4);
        tabs.setTitleAt(4, "Colors");

        JLabel lblColor = new JLabel("Color");
        lblColor.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblBGR = new JLabel("BGR");
        lblBGR.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblHTML = new JLabel("HTML");
        lblHTML.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblDisplay = new JLabel("Display");
        lblDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblAlpha = new JLabel("Alpha");
        lblAlpha.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblAlpha2 = new JLabel("Alpha");
        lblAlpha2.setHorizontalAlignment(SwingConstants.CENTER);
        tabColorsPanel.add(lblColor);
        tabColorsPanel.add(lblBGR);
        tabColorsPanel.add(lblHTML);
        tabColorsPanel.add(lblDisplay);
        tabColorsPanel.add(lblAlpha);

        JLabel lblTextBGR = new JLabel("Text : ");
        JTextField tfTextBGR = new JTextField(AssColor.withScheme(Color.white, AssColorScheme.BGR));
        JTextField tfTextHTML = new JTextField(AssColor.withScheme(Color.white, AssColorScheme.HTML));
        ColorButton btnTextColor = new ColorButton(Color.white);
        JSlider slTextAlpha = new JSlider(0, 255, 255);
        tabColorsPanel.add(lblTextBGR);
        tabColorsPanel.add(tfTextBGR);
        tabColorsPanel.add(tfTextHTML);
        tabColorsPanel.add(btnTextColor);
        tabColorsPanel.add(slTextAlpha);
        tfTextBGR.addActionListener((e)->{
            if(tfTextBGR.getText().matches("&H[A-Fa-f0-9]{6}&")){
                AssColor col = AssColor.fromScheme(tfTextBGR.getText(), AssColorScheme.BGR);
                btnTextColor.setColor(col.getColor());
                float alpha = slTextAlpha.getValue() / 255.0f;
                selectedStyle.setTextColor(new AssColor(col.getColor(), alpha));
                preview.setSentenceSample(selectedStyle, previewText.getText());
            }
        });
        tfTextHTML.addActionListener((e)->{
            if(tfTextHTML.getText().matches("#[A-Fa-f0-9]{6}")){
                AssColor col = AssColor.fromScheme(tfTextHTML.getText(), AssColorScheme.HTML);
                btnTextColor.setColor(col.getColor());
                float alpha = slTextAlpha.getValue() / 255.0f;
                selectedStyle.setTextColor(new AssColor(col.getColor(), alpha));
                preview.setSentenceSample(selectedStyle, previewText.getText());
            }
        });
        btnTextColor.addActionListener((e)->{
            Color c = JColorChooser.showDialog(new Frame(), "Choose a color", btnTextColor.getColor());
            if(c == null) return;
            btnTextColor.setColor(c);
            tfTextBGR.setText(AssColor.withScheme(c, AssColorScheme.BGR));
            tfTextHTML.setText(AssColor.withScheme(c, AssColorScheme.HTML));
            float alpha = slTextAlpha.getValue() / 255.0f;
            selectedStyle.setTextColor(new AssColor(c, alpha));
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        slTextAlpha.addChangeListener((e)->{
            Color c = btnTextColor.getColor();
            float alpha = slTextAlpha.getValue() / 255.0f;
            btnTextColor.setAlpha(slTextAlpha.getValue());
            selectedStyle.setTextColor(new AssColor(c, alpha));
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        JLabel lblKaraBGR = new JLabel("Karaoke : ");
        JTextField tfKaraBGR = new JTextField(AssColor.withScheme(Color.yellow, AssColorScheme.BGR));
        JTextField tfKaraHTML = new JTextField(AssColor.withScheme(Color.yellow, AssColorScheme.HTML));
        ColorButton btnKaraColor = new ColorButton(Color.yellow);
        JSlider slKaraAlpha = new JSlider(0, 255, 255);
        tabColorsPanel.add(lblKaraBGR);
        tabColorsPanel.add(tfKaraBGR);
        tabColorsPanel.add(tfKaraHTML);
        tabColorsPanel.add(btnKaraColor);
        tabColorsPanel.add(slKaraAlpha);
        tfKaraBGR.addActionListener((e)->{
            if(tfKaraBGR.getText().matches("&H[A-Fa-f0-9]{6}&")){
                AssColor col = AssColor.fromScheme(tfKaraBGR.getText(), AssColorScheme.BGR);
                btnKaraColor.setColor(col.getColor());
                float alpha = slKaraAlpha.getValue() / 255.0f;
                selectedStyle.setKaraokeColor(new AssColor(col.getColor(), alpha));
                preview.setSentenceSample(selectedStyle, previewText.getText());
            }
        });
        tfKaraHTML.addActionListener((e)->{
            if(tfKaraHTML.getText().matches("#[A-Fa-f0-9]{6}")){
                AssColor col = AssColor.fromScheme(tfKaraHTML.getText(), AssColorScheme.HTML);
                btnKaraColor.setColor(col.getColor());
                float alpha = slKaraAlpha.getValue() / 255.0f;
                selectedStyle.setKaraokeColor(new AssColor(col.getColor(), alpha));
                preview.setSentenceSample(selectedStyle, previewText.getText());
            }
        });
        btnKaraColor.addActionListener((e)->{
            Color c = JColorChooser.showDialog(new Frame(), "Choose a color", btnKaraColor.getColor());
            if(c == null) return;
            btnKaraColor.setColor(c);
            tfKaraBGR.setText(AssColor.withScheme(c, AssColorScheme.BGR));
            tfKaraHTML.setText(AssColor.withScheme(c, AssColorScheme.HTML));
            float alpha = slKaraAlpha.getValue() / 255.0f;
            selectedStyle.setKaraokeColor(new AssColor(c, alpha));
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        slKaraAlpha.addChangeListener((e)->{
            Color c = btnKaraColor.getColor();
            float alpha = slKaraAlpha.getValue() / 255.0f;
            btnKaraColor.setAlpha(slKaraAlpha.getValue());
            selectedStyle.setKaraokeColor(new AssColor(c, alpha));
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        JLabel lblBordBGR = new JLabel("Border : ");
        JTextField tfBordBGR = new JTextField(AssColor.withScheme(Color.black, AssColorScheme.BGR));
        JTextField tfBordHTML = new JTextField(AssColor.withScheme(Color.black, AssColorScheme.HTML));
        ColorButton btnBordColor = new ColorButton(Color.black);
        JSlider slBordAlpha = new JSlider(0, 255, 255);
        tabColorsPanel.add(lblBordBGR);
        tabColorsPanel.add(tfBordBGR);
        tabColorsPanel.add(tfBordHTML);
        tabColorsPanel.add(btnBordColor);
        tabColorsPanel.add(slBordAlpha);
        tfBordBGR.addActionListener((e)->{
            if(tfBordBGR.getText().matches("&H[A-Fa-f0-9]{6}&")){
                AssColor col = AssColor.fromScheme(tfBordBGR.getText(), AssColorScheme.BGR);
                btnBordColor.setColor(col.getColor());
                float alpha = slBordAlpha.getValue() / 255.0f;
                selectedStyle.setOutlineColor(new AssColor(col.getColor(), alpha));
                preview.setSentenceSample(selectedStyle, previewText.getText());
            }
        });
        tfBordHTML.addActionListener((e)->{
            if(tfBordHTML.getText().matches("#[A-Fa-f0-9]{6}")){
                AssColor col = AssColor.fromScheme(tfBordHTML.getText(), AssColorScheme.HTML);
                btnBordColor.setColor(col.getColor());
                float alpha = slBordAlpha.getValue() / 255.0f;
                selectedStyle.setOutlineColor(new AssColor(col.getColor(), alpha));
                preview.setSentenceSample(selectedStyle, previewText.getText());
            }
        });
        btnBordColor.addActionListener((e)->{
            Color c = JColorChooser.showDialog(new Frame(), "Choose a color", btnBordColor.getColor());
            if(c == null) return;
            btnBordColor.setColor(c);
            tfBordBGR.setText(AssColor.withScheme(c, AssColorScheme.BGR));
            tfBordHTML.setText(AssColor.withScheme(c, AssColorScheme.HTML));
            float alpha = slBordAlpha.getValue() / 255.0f;
            selectedStyle.setOutlineColor(new AssColor(c, alpha));
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        slBordAlpha.addChangeListener((e)->{
            Color c = btnBordColor.getColor();
            float alpha = slBordAlpha.getValue() / 255.0f;
            btnBordColor.setAlpha(slBordAlpha.getValue());
            selectedStyle.setOutlineColor(new AssColor(c, alpha));
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });

        JLabel lblShadBGR = new JLabel("Shadow : ");
        JTextField tfShadBGR = new JTextField(AssColor.withScheme(Color.black, AssColorScheme.BGR));
        JTextField tfShadHTML = new JTextField(AssColor.withScheme(Color.black, AssColorScheme.HTML));
        ColorButton btnShadColor = new ColorButton(Color.black);
        JSlider slShadAlpha = new JSlider(0, 255, 255);
        tabColorsPanel.add(lblShadBGR);
        tabColorsPanel.add(tfShadBGR);
        tabColorsPanel.add(tfShadHTML);
        tabColorsPanel.add(btnShadColor);
        tabColorsPanel.add(slShadAlpha);
        tfShadBGR.addActionListener((e)->{
            if(tfShadBGR.getText().matches("&H[A-Fa-f0-9]{6}&")){
                AssColor col = AssColor.fromScheme(tfShadBGR.getText(), AssColorScheme.BGR);
                btnShadColor.setColor(col.getColor());
                float alpha = slShadAlpha.getValue() / 255.0f;
                selectedStyle.setShadowColor(new AssColor(col.getColor(), alpha));
                preview.setSentenceSample(selectedStyle, previewText.getText());
            }
        });
        tfShadHTML.addActionListener((e)->{
            if(tfShadHTML.getText().matches("#[A-Fa-f0-9]{6}")){
                AssColor col = AssColor.fromScheme(tfShadHTML.getText(), AssColorScheme.HTML);
                btnTextColor.setColor(col.getColor());
                float alpha = slShadAlpha.getValue() / 255.0f;
                selectedStyle.setShadowColor(new AssColor(col.getColor(), alpha));
                preview.setSentenceSample(selectedStyle, previewText.getText());
            }
        });
        btnShadColor.addActionListener((e)->{
            Color c = JColorChooser.showDialog(new Frame(), "Choose a color", btnShadColor.getColor());
            if(c == null) return;
            btnShadColor.setColor(c);
            tfShadBGR.setText(AssColor.withScheme(c, AssColorScheme.BGR));
            tfShadHTML.setText(AssColor.withScheme(c, AssColorScheme.HTML));
            float alpha = slShadAlpha.getValue() / 255.0f;
            selectedStyle.setShadowColor(new AssColor(c, alpha));
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
        slShadAlpha.addChangeListener((e)->{
            Color c = btnShadColor.getColor();
            float alpha = slShadAlpha.getValue() / 255.0f;
            btnShadColor.setAlpha(slShadAlpha.getValue());
            selectedStyle.setShadowColor(new AssColor(c, alpha));
            preview.setSentenceSample(selectedStyle, previewText.getText());
        });
    }

    public void showDialog(List<AssStyle> styles) {
        this.styles = styles;
        if(!styles.isEmpty()){
            lListModel.clear();
            lListModel.addElement(styles.getFirst());
            rListModel.clear();
            rListModel.addAll(styles);
        }
        preview.setSentenceSample(styles.getFirst(), previewText.getText());
        selectedStyle = styles.getFirst();
        setVisible(true);
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }

    public List<AssStyle> getStyles() {
        return styles;
    }

}
