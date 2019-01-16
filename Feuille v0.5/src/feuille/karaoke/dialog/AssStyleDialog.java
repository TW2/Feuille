/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AssStyleDialog.java
 *
 * Created on 30 mars 2011, 13:03:52
 */

package feuille.karaoke.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import feuille.karaoke.lib.AssStyle;
import feuille.karaoke.lib.AssStylePreview;
import feuille.lib.Language;

/**
 * <p>This is a dialog for the choice of style.<br />
 * C'est une boîte de dialogue pour lme choix du style.</p>
 * @author The Wingate 2940
 */
public class AssStyleDialog extends javax.swing.JDialog {

    private ButtonPressed bp;
    private Frame frame;
    private SpinnerNumberModel snmColor1; // Embedded Styles
    private SpinnerNumberModel snmColor2; // Embedded Styles
    private SpinnerNumberModel snmColor3; // Embedded Styles
    private SpinnerNumberModel snmColor4; // Embedded Styles
    private SpinnerNumberModel snmBorder; // Embedded Styles
    private SpinnerNumberModel snmShadow; // Embedded Styles
    private SpinnerNumberModel snmFontsize; // Embedded Styles
    private DefaultComboBoxModel cbmFont; // Embedded Styles
    private DefaultComboBoxModel cbmEnco; // Embedded Styles
    private boolean refreshInfos = true; // Embedded Styles
    private Language localeLanguage = feuille.MainFrame.getLanguage();
    private AssStyle assStyle;
    private boolean isOkPressed = false;
    AssStylePreview asp = new AssStylePreview();

    /** <p>A choice of state.<br />Un choix d'état.</p> */
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }

    /** <p>A choice of encoding.<br />Un choix d'encodage.</p> */
    public enum Encoding{
        ANSI(0,"ANSI"),DEFAULT(1,"Default"),SYMBOL(2,"Symbol"),MAC(77,"Mac"),
        SHIFT_JIS(128,"Shift-JIS"),HANGUL(129,"Hangeul"),JOHAB(130,"Johab"),
        GB2312(134,"GB2312"),BIG5(136,"Chinese BIG5"),GREEK(161,"Greek"),
        TURKISH(162,"Turkish"),VIETNAMESE(163,"Vietnamese"),
        HEBREW(177,"Hebrew"),ARABIC(178,"Arabic"),BALTIC(186,"Baltic"),
        RUSSIAN(204,"Russian"),THAI(222,"Thai"),EAST_EURO(238,"East european"),
        OEM(255,"OEM");

        private int number;
        private String sEnco;

        /** <p>Create a new Encoding.<br />Crée un nouveau Encoding.</p> */
        Encoding(int number, String sEnco){
            this.number = number;
            this.sEnco = sEnco;
        }

        /** <p>Return the code of the encoding.<br />
         * Retourne le code de l'encodage.</p> */
        public int getNumber(){
            return number;
        }

        /** <p>Return the name of the encoding.<br />
         * Retourne le nom de l'encodage (en anglais).</p> */
        public String getEncoding(){
            return sEnco;
        }

        /** <p>Return the string "'code' - 'encoding'".<br />
         * Retourne la chaine "'code' - 'encodage'".</p> */
        @Override
        public String toString(){
            return number+" - "+sEnco;
        }

        /** <p>Return the encoding with the given code.<br />
         * Retourne l'encodage avec le code donné.</p> */
        public Encoding getEncodingFrom(int number){
            Encoding e;
            switch(number){
                case 0: e=Encoding.ANSI; break;
                case 1: e=Encoding.DEFAULT; break;
                case 2: e=Encoding.SYMBOL; break;
                case 77: e=Encoding.MAC; break;
                case 128: e=Encoding.SHIFT_JIS; break;
                case 129: e=Encoding.HANGUL; break;
                case 130: e=Encoding.JOHAB; break;
                case 134: e=Encoding.GB2312; break;
                case 136: e=Encoding.BIG5; break;
                case 161: e=Encoding.GREEK; break;
                case 162: e=Encoding.TURKISH; break;
                case 163: e=Encoding.VIETNAMESE; break;
                case 177: e=Encoding.HEBREW; break;
                case 178: e=Encoding.ARABIC; break;
                case 186: e=Encoding.BALTIC; break;
                case 204: e=Encoding.RUSSIAN; break;
                case 222: e=Encoding.THAI; break;
                case 238: e=Encoding.EAST_EURO; break;
                case 255: e=Encoding.OEM; break;
                default: e=Encoding.DEFAULT; break;
            }
            return e;
        }
    }

    /** <p>Creates new form AssStyleDialog.<br />
     * Crée un nouveau formaulaire AssStyleDialog.</p> */
    public AssStyleDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(520,390);
        frame = parent;
        bp = ButtonPressed.NONE;

        snmColor1 = new SpinnerNumberModel(0, 0, 255, 1);
        spiText.setModel(snmColor1);
        snmColor2 = new SpinnerNumberModel(0, 0, 255, 1);
        spiKaraoke.setModel(snmColor2);
        snmColor3 = new SpinnerNumberModel(0, 0, 255, 1);
        spiBorder.setModel(snmColor3);
        snmColor4 = new SpinnerNumberModel(0, 0, 255, 1);
        spiShadow.setModel(snmColor4);
        snmBorder = new SpinnerNumberModel(0, 0, 4, 1);
        spiBorderS.setModel(snmBorder);
        snmShadow = new SpinnerNumberModel(0, 0, 4, 1);
        spiShadowS.setModel(snmShadow);
        snmFontsize = new SpinnerNumberModel(0, 0, 1000, 1);
        spiFontsize.setModel(snmFontsize);

        GraphicsEnvironment geLocal = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final Font[] envFonts = geLocal.getAllFonts();
        cbmFont = new DefaultComboBoxModel();
        cbFontname.setModel(cbmFont);
        for(Font f : envFonts){
            if(cbmFont.getIndexOf(f.getFamily())==-1){
                cbmFont.addElement(f.getFamily());
            }
        }

        cbmEnco = new DefaultComboBoxModel();
        cbEncoding.setModel(cbmEnco);
        cbmEnco.addElement(Encoding.ANSI); cbmEnco.addElement(Encoding.DEFAULT);
        cbmEnco.addElement(Encoding.SYMBOL); cbmEnco.addElement(Encoding.MAC);
        cbmEnco.addElement(Encoding.SHIFT_JIS); cbmEnco.addElement(Encoding.HANGUL);
        cbmEnco.addElement(Encoding.JOHAB); cbmEnco.addElement(Encoding.GB2312);
        cbmEnco.addElement(Encoding.BIG5); cbmEnco.addElement(Encoding.GREEK);
        cbmEnco.addElement(Encoding.TURKISH); cbmEnco.addElement(Encoding.VIETNAMESE);
        cbmEnco.addElement(Encoding.HEBREW); cbmEnco.addElement(Encoding.ARABIC);
        cbmEnco.addElement(Encoding.BALTIC); cbmEnco.addElement(Encoding.RUSSIAN);
        cbmEnco.addElement(Encoding.THAI); cbmEnco.addElement(Encoding.EAST_EURO);
        cbmEnco.addElement(Encoding.OEM);
        cbmEnco.setSelectedItem(Encoding.DEFAULT);

        spiFontsize.setValue(40);
        spiBorderS.setValue(2); spiShadowS.setValue(2);
        spiMarginL.setValue(10); spiMarginR.setValue(10);
        spiMarginVT.setValue(10); spiMarginB.setValue(10);
        
        jPanel1.add(asp);
        
        //Force visibility
        setAlwaysOnTop(true);
        
        if(localeLanguage.getValueOf("titleASD1")!=null){setTitle(localeLanguage.getValueOf("titleASD1"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("labelName")!=null){jLabel17.setText(localeLanguage.getValueOf("labelName"));}
        if(localeLanguage.getValueOf("labelText")!=null){lblColorText4.setText(localeLanguage.getValueOf("labelText"));}
        if(localeLanguage.getValueOf("labelKaraoke")!=null){lblColorKaraoke.setText(localeLanguage.getValueOf("labelKaraoke"));}
        if(localeLanguage.getValueOf("labelBorder")!=null){lblColorBorder.setText(localeLanguage.getValueOf("labelBorder"));}
        if(localeLanguage.getValueOf("labelShadow")!=null){lblColorShadow.setText(localeLanguage.getValueOf("labelShadow"));}
        if(localeLanguage.getValueOf("labelFontName")!=null){jLabel18.setText(localeLanguage.getValueOf("labelFontName"));}
        if(localeLanguage.getValueOf("labelFontSize")!=null){jLabel16.setText(localeLanguage.getValueOf("labelFontSize"));}
        if(localeLanguage.getValueOf("labelAlignment")!=null){jLabel23.setText(localeLanguage.getValueOf("labelAlignment"));}
        if(localeLanguage.getValueOf("labelScaleX")!=null){jLabel26.setText(localeLanguage.getValueOf("labelScaleX"));}
        if(localeLanguage.getValueOf("labelScaleY")!=null){jLabel27.setText(localeLanguage.getValueOf("labelScaleY"));}
        if(localeLanguage.getValueOf("labelRotation")!=null){jLabel28.setText(localeLanguage.getValueOf("labelRotation"));}
        if(localeLanguage.getValueOf("labelSpacing")!=null){jLabel29.setText(localeLanguage.getValueOf("labelSpacing"));}
        if(localeLanguage.getValueOf("labelBorder")!=null){jLabel24.setText(localeLanguage.getValueOf("labelBorder"));}
        if(localeLanguage.getValueOf("labelShadow")!=null){jLabel25.setText(localeLanguage.getValueOf("labelShadow"));}
        if(localeLanguage.getValueOf("labelMarginL")!=null){jLabel20.setText(localeLanguage.getValueOf("labelMarginL"));}
        if(localeLanguage.getValueOf("labelMarginR")!=null){jLabel21.setText(localeLanguage.getValueOf("labelMarginR"));}
        if(localeLanguage.getValueOf("labelMarginTV")!=null){jLabel19.setText(localeLanguage.getValueOf("labelMarginTV"));}
        if(localeLanguage.getValueOf("labelMarginB")!=null){jLabel22.setText(localeLanguage.getValueOf("labelMarginB"));}
        if(localeLanguage.getValueOf("labelEncoding")!=null){jLabel30.setText(localeLanguage.getValueOf("labelEncoding"));}
        if(localeLanguage.getValueOf("checkboxBold")!=null){cboBold.setText(localeLanguage.getValueOf("checkboxBold"));}
        if(localeLanguage.getValueOf("checkboxItalic")!=null){cboItalic.setText(localeLanguage.getValueOf("checkboxItalic"));}
        if(localeLanguage.getValueOf("checkboxUnderline")!=null){cboUnderline.setText(localeLanguage.getValueOf("checkboxUnderline"));}
        if(localeLanguage.getValueOf("checkboxStrikeOut")!=null){cboStrikeOut.setText(localeLanguage.getValueOf("checkboxStrikeOut"));}
        if(localeLanguage.getValueOf("checkboxOpaqueBox")!=null){cboOpaqueBox.setText(localeLanguage.getValueOf("checkboxOpaqueBox"));}
//        setCommonFont(assfxmaker.AssFxMaker.getDefaultFont(), getContentPane().getComponents());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgAlignment = new javax.swing.ButtonGroup();
        jLabel17 = new javax.swing.JLabel();
        tfStyleName = new javax.swing.JTextField();
        lblColorText4 = new javax.swing.JLabel();
        lblColor1 = new javax.swing.JLabel();
        lblColorKaraoke = new javax.swing.JLabel();
        lblColor2 = new javax.swing.JLabel();
        spiText = new javax.swing.JSpinner();
        spiKaraoke = new javax.swing.JSpinner();
        lblColorBorder = new javax.swing.JLabel();
        lblColorShadow = new javax.swing.JLabel();
        spiBorder = new javax.swing.JSpinner();
        lblColor3 = new javax.swing.JLabel();
        lblColor4 = new javax.swing.JLabel();
        spiShadow = new javax.swing.JSpinner();
        spiFontsize = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        cbFontname = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        cboBold = new javax.swing.JCheckBox();
        cboItalic = new javax.swing.JCheckBox();
        cboUnderline = new javax.swing.JCheckBox();
        cboStrikeOut = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        spiMarginL = new javax.swing.JSpinner();
        jLabel21 = new javax.swing.JLabel();
        spiMarginR = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        spiMarginVT = new javax.swing.JSpinner();
        jLabel22 = new javax.swing.JLabel();
        spiMarginB = new javax.swing.JSpinner();
        jLabel23 = new javax.swing.JLabel();
        rb7 = new javax.swing.JRadioButton();
        rb8 = new javax.swing.JRadioButton();
        rb9 = new javax.swing.JRadioButton();
        rb4 = new javax.swing.JRadioButton();
        rb5 = new javax.swing.JRadioButton();
        rb6 = new javax.swing.JRadioButton();
        rb1 = new javax.swing.JRadioButton();
        rb2 = new javax.swing.JRadioButton();
        rb3 = new javax.swing.JRadioButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        tfScaleY = new javax.swing.JTextField();
        tfScaleX = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        tfSpacing = new javax.swing.JTextField();
        tfRotation = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        spiShadowS = new javax.swing.JSpinner();
        spiBorderS = new javax.swing.JSpinner();
        cboOpaqueBox = new javax.swing.JCheckBox();
        cbEncoding = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Cancel_Button = new javax.swing.JButton();
        OK_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel17.setText("Name :");
        getContentPane().add(jLabel17);
        jLabel17.setBounds(10, 10, 60, 30);
        getContentPane().add(tfStyleName);
        tfStyleName.setBounds(70, 10, 170, 30);

        lblColorText4.setText("Text :");
        getContentPane().add(lblColorText4);
        lblColorText4.setBounds(250, 10, 60, 20);

        lblColor1.setBackground(new java.awt.Color(255, 255, 255));
        lblColor1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblColor1.setOpaque(true);
        lblColor1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColor1MouseClicked(evt);
            }
        });
        getContentPane().add(lblColor1);
        lblColor1.setBounds(250, 30, 60, 20);

        lblColorKaraoke.setText("Karaoke :");
        getContentPane().add(lblColorKaraoke);
        lblColorKaraoke.setBounds(320, 10, 60, 20);

        lblColor2.setBackground(new java.awt.Color(255, 0, 0));
        lblColor2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblColor2.setOpaque(true);
        lblColor2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColor2MouseClicked(evt);
            }
        });
        getContentPane().add(lblColor2);
        lblColor2.setBounds(320, 30, 60, 20);
        getContentPane().add(spiText);
        spiText.setBounds(250, 50, 60, 30);
        getContentPane().add(spiKaraoke);
        spiKaraoke.setBounds(320, 50, 60, 30);

        lblColorBorder.setText("Border :");
        getContentPane().add(lblColorBorder);
        lblColorBorder.setBounds(250, 80, 60, 20);

        lblColorShadow.setText("Shadow :");
        getContentPane().add(lblColorShadow);
        lblColorShadow.setBounds(320, 80, 60, 20);
        getContentPane().add(spiBorder);
        spiBorder.setBounds(250, 120, 60, 30);

        lblColor3.setBackground(new java.awt.Color(0, 0, 102));
        lblColor3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblColor3.setOpaque(true);
        lblColor3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColor3MouseClicked(evt);
            }
        });
        getContentPane().add(lblColor3);
        lblColor3.setBounds(250, 100, 60, 20);

        lblColor4.setBackground(new java.awt.Color(0, 0, 0));
        lblColor4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblColor4.setOpaque(true);
        lblColor4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColor4MouseClicked(evt);
            }
        });
        getContentPane().add(lblColor4);
        lblColor4.setBounds(320, 100, 60, 20);
        getContentPane().add(spiShadow);
        spiShadow.setBounds(320, 120, 60, 30);
        getContentPane().add(spiFontsize);
        spiFontsize.setBounds(320, 150, 60, 30);

        jLabel16.setText("Font size :");
        getContentPane().add(jLabel16);
        jLabel16.setBounds(250, 150, 60, 30);

        cbFontname.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbFontname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFontnameActionPerformed(evt);
            }
        });
        getContentPane().add(cbFontname);
        cbFontname.setBounds(96, 150, 140, 30);

        jLabel18.setText("Font name :");
        getContentPane().add(jLabel18);
        jLabel18.setBounds(10, 150, 80, 30);

        cboBold.setText("Bold");
        getContentPane().add(cboBold);
        cboBold.setBounds(10, 180, 80, 20);

        cboItalic.setText("Italic");
        getContentPane().add(cboItalic);
        cboItalic.setBounds(100, 180, 80, 20);

        cboUnderline.setText("Underline");
        getContentPane().add(cboUnderline);
        cboUnderline.setBounds(190, 180, 90, 20);

        cboStrikeOut.setText("Strike out");
        getContentPane().add(cboStrikeOut);
        cboStrikeOut.setBounds(290, 180, 90, 20);

        jLabel20.setText("L :");
        getContentPane().add(jLabel20);
        jLabel20.setBounds(390, 30, 40, 30);
        getContentPane().add(spiMarginL);
        spiMarginL.setBounds(430, 30, 60, 30);

        jLabel21.setText("R :");
        getContentPane().add(jLabel21);
        jLabel21.setBounds(390, 70, 40, 30);
        getContentPane().add(spiMarginR);
        spiMarginR.setBounds(430, 70, 60, 30);

        jLabel19.setText("V/T :");
        getContentPane().add(jLabel19);
        jLabel19.setBounds(390, 110, 40, 30);
        getContentPane().add(spiMarginVT);
        spiMarginVT.setBounds(430, 110, 60, 30);

        jLabel22.setText("B :");
        getContentPane().add(jLabel22);
        jLabel22.setBounds(390, 150, 40, 30);
        getContentPane().add(spiMarginB);
        spiMarginB.setBounds(430, 150, 60, 30);

        jLabel23.setText("Alignment :");
        getContentPane().add(jLabel23);
        jLabel23.setBounds(10, 210, 90, 20);

        bgAlignment.add(rb7);
        rb7.setText("7");
        getContentPane().add(rb7);
        rb7.setBounds(10, 230, 40, 23);

        bgAlignment.add(rb8);
        rb8.setText("8");
        getContentPane().add(rb8);
        rb8.setBounds(50, 230, 40, 23);

        bgAlignment.add(rb9);
        rb9.setText("9");
        getContentPane().add(rb9);
        rb9.setBounds(90, 230, 40, 23);

        bgAlignment.add(rb4);
        rb4.setText("4");
        getContentPane().add(rb4);
        rb4.setBounds(10, 250, 40, 23);

        bgAlignment.add(rb5);
        rb5.setText("5");
        getContentPane().add(rb5);
        rb5.setBounds(50, 250, 40, 23);

        bgAlignment.add(rb6);
        rb6.setText("6");
        getContentPane().add(rb6);
        rb6.setBounds(90, 250, 40, 23);

        bgAlignment.add(rb1);
        rb1.setText("1");
        getContentPane().add(rb1);
        rb1.setBounds(10, 270, 40, 23);

        bgAlignment.add(rb2);
        rb2.setSelected(true);
        rb2.setText("2");
        getContentPane().add(rb2);
        rb2.setBounds(50, 270, 40, 23);

        bgAlignment.add(rb3);
        rb3.setText("3");
        getContentPane().add(rb3);
        rb3.setBounds(90, 270, 40, 23);

        jLabel26.setText("Sc. X :");
        getContentPane().add(jLabel26);
        jLabel26.setBounds(130, 200, 50, 30);

        jLabel27.setText("Sc. Y :");
        getContentPane().add(jLabel27);
        jLabel27.setBounds(130, 230, 50, 30);

        tfScaleY.setText("100");
        getContentPane().add(tfScaleY);
        tfScaleY.setBounds(180, 230, 60, 30);

        tfScaleX.setText("100");
        getContentPane().add(tfScaleX);
        tfScaleX.setBounds(180, 200, 60, 30);

        jLabel28.setText("Rot. :");
        getContentPane().add(jLabel28);
        jLabel28.setBounds(250, 200, 50, 30);

        jLabel29.setText("Spac. : ");
        getContentPane().add(jLabel29);
        jLabel29.setBounds(250, 230, 50, 30);

        tfSpacing.setText("0");
        getContentPane().add(tfSpacing);
        tfSpacing.setBounds(300, 230, 60, 30);

        tfRotation.setText("0");
        getContentPane().add(tfRotation);
        tfRotation.setBounds(300, 200, 60, 30);

        jLabel24.setText("Border :");
        getContentPane().add(jLabel24);
        jLabel24.setBounds(370, 200, 60, 30);

        jLabel25.setText("Shadow :");
        getContentPane().add(jLabel25);
        jLabel25.setBounds(370, 230, 60, 30);
        getContentPane().add(spiShadowS);
        spiShadowS.setBounds(430, 230, 60, 30);
        getContentPane().add(spiBorderS);
        spiBorderS.setBounds(430, 200, 60, 30);

        cboOpaqueBox.setText("Opaque box");
        getContentPane().add(cboOpaqueBox);
        cboOpaqueBox.setBounds(350, 260, 130, 20);

        cbEncoding.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cbEncoding);
        cbEncoding.setBounds(250, 280, 240, 30);

        jLabel30.setText("Encoding :");
        getContentPane().add(jLabel30);
        jLabel30.setBounds(150, 280, 90, 30);

        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 47, 230, 100);

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(Cancel_Button);
        Cancel_Button.setBounds(370, 320, 118, 23);

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(OK_Button);
        OK_Button.setBounds(250, 320, 116, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblColor1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColor1MouseClicked
        Color c = lblColor1.getBackground();
        String red = Integer.toString(c.getRed(), 16);
        if(red.length()==1){red="0"+red;}
        String green = Integer.toString(c.getGreen(), 16);
        if(green.length()==1){green="0"+green;}
        String blue = Integer.toString(c.getBlue(), 16);
        if(blue.length()==1){blue="0"+blue;}
        AssColorDialog acd = new AssColorDialog(frame, true);
        String s = acd.showDialog(blue+green+red);
        int iblue = Integer.parseInt(s.substring(0, 2), 16);
        int igreen = Integer.parseInt(s.substring(2, 4), 16);
        int ired =  Integer.parseInt(s.substring(4), 16);
        lblColor1.setBackground(new Color(ired,igreen,iblue));
        //Update drawing
        int num = Integer.parseInt(spiText.getValue().toString());
        asp.setTextColor(lblColor1.getBackground(), num);
}//GEN-LAST:event_lblColor1MouseClicked

    private void lblColor2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColor2MouseClicked
        Color c = lblColor2.getBackground();
        String red = Integer.toString(c.getRed(), 16);
        if(red.length()==1){red="0"+red;}
        String green = Integer.toString(c.getGreen(), 16);
        if(green.length()==1){green="0"+green;}
        String blue = Integer.toString(c.getBlue(), 16);
        if(blue.length()==1){blue="0"+blue;}
        AssColorDialog acd = new AssColorDialog(frame, true);
        String s = acd.showDialog(blue+green+red);
        int iblue = Integer.parseInt(s.substring(0, 2), 16);
        int igreen = Integer.parseInt(s.substring(2, 4), 16);
        int ired =  Integer.parseInt(s.substring(4), 16);
        lblColor2.setBackground(new Color(ired,igreen,iblue));
}//GEN-LAST:event_lblColor2MouseClicked

    private void lblColor3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColor3MouseClicked
        Color c = lblColor3.getBackground();
        String red = Integer.toString(c.getRed(), 16);
        if(red.length()==1){red="0"+red;}
        String green = Integer.toString(c.getGreen(), 16);
        if(green.length()==1){green="0"+green;}
        String blue = Integer.toString(c.getBlue(), 16);
        if(blue.length()==1){blue="0"+blue;}
        AssColorDialog acd = new AssColorDialog(frame, true);
        String s = acd.showDialog(blue+green+red);
        int iblue = Integer.parseInt(s.substring(0, 2), 16);
        int igreen = Integer.parseInt(s.substring(2, 4), 16);
        int ired =  Integer.parseInt(s.substring(4), 16);
        lblColor3.setBackground(new Color(ired,igreen,iblue));
        //Update drawing
        int num = Integer.parseInt(spiBorder.getValue().toString());
        asp.setOutlineColor(lblColor3.getBackground(), num);
}//GEN-LAST:event_lblColor3MouseClicked

    private void lblColor4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblColor4MouseClicked
        Color c = lblColor4.getBackground();
        String red = Integer.toString(c.getRed(), 16);
        if(red.length()==1){red="0"+red;}
        String green = Integer.toString(c.getGreen(), 16);
        if(green.length()==1){green="0"+green;}
        String blue = Integer.toString(c.getBlue(), 16);
        if(blue.length()==1){blue="0"+blue;}
        AssColorDialog acd = new AssColorDialog(frame, true);
        String s = acd.showDialog(blue+green+red);
        int iblue = Integer.parseInt(s.substring(0, 2), 16);
        int igreen = Integer.parseInt(s.substring(2, 4), 16);
        int ired =  Integer.parseInt(s.substring(4), 16);
        lblColor4.setBackground(new Color(ired,igreen,iblue));
        //Update drawing
        int num = Integer.parseInt(spiShadow.getValue().toString());
        asp.setShadowColor(lblColor4.getBackground(), num);
}//GEN-LAST:event_lblColor4MouseClicked

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON; isOkPressed = true;
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        bp = ButtonPressed.CANCEL_BUTTON; isOkPressed = false;
        dispose();
}//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void cbFontnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFontnameActionPerformed
        //Update drawing
        asp.setFont(cbmFont.getSelectedItem().toString(), 20);
    }//GEN-LAST:event_cbFontnameActionPerformed

    /** <p>Show the dialog. Request and return an AssStyle.<br />
     * Montre la boîte de dialogue. Requiert et retourne un AssStyle.</p> */
    public AssStyle showDialog(AssStyle as){
        setAssStyle(as);
        
        try{asp.setAssStyle(assStyle.getClone());
        }catch(Exception exc){/*Just nothing*/}
        //Update drawing (Text color)
        int num = Integer.parseInt(spiText.getValue().toString());
        asp.setTextColor(lblColor1.getBackground(), num);
        //Update drawing (Outline color)
        num = Integer.parseInt(spiBorder.getValue().toString());
        asp.setOutlineColor(lblColor3.getBackground(), num);
        //Update drawing (Shadow color)
        num = Integer.parseInt(spiShadow.getValue().toString());
        asp.setShadowColor(lblColor4.getBackground(), num);
        
        setVisible(true);

        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return getAssStyle();
        }else{
            return as;
        }
    }

    /** <p>Set the ASS style.<br />Définit le style ASS.</p> */
    public void setAssStyle(AssStyle as){
        assStyle = as;
        updateControls();
    }

    /** <p>Get the ASS style.<br />Obtient la style ASS.</p> */
    public AssStyle getAssStyle(){
        updateStyle();
        return assStyle;
    }
    
    /** <p>Update the style.<br />Met à jour le style.</p> */
    private void updateStyle(){
        assStyle.setName(tfStyleName.getText());
        assStyle.setFontname(cbFontname.getSelectedItem().toString());
        assStyle.setFontsize(Double.parseDouble(spiFontsize.getValue().toString()));
        int iText = Integer.parseInt(spiText.getValue().toString());
        assStyle.setTextColor(lblColor1.getBackground(), Integer.toString(iText, 16));
        int iKara = Integer.parseInt(spiKaraoke.getValue().toString());
        assStyle.setKaraColor(lblColor2.getBackground(), Integer.toString(iKara, 16));
        int iOut = Integer.parseInt(spiBorder.getValue().toString());
        assStyle.setOutlineColor(lblColor3.getBackground(), Integer.toString(iOut, 16));
        int iShad = Integer.parseInt(spiShadow.getValue().toString());
        assStyle.setBackColor(lblColor4.getBackground(), Integer.toString(iShad, 16));
        assStyle.setBold(cboBold.isSelected());
        assStyle.setItalic(cboItalic.isSelected());
        assStyle.setUnderline(cboUnderline.isSelected());
        assStyle.setStrikeout(cboStrikeOut.isSelected());
        assStyle.setScaleX(Double.parseDouble(tfScaleX.getText()));
        assStyle.setScaleY(Double.parseDouble(tfScaleY.getText()));
        assStyle.setAngle(Double.parseDouble(tfRotation.getText()));
        assStyle.setSpacing(Double.parseDouble(tfSpacing.getText()));
        assStyle.setBorderStyle(cboOpaqueBox.isSelected());
        assStyle.setOutline(Integer.parseInt(spiBorderS.getValue().toString()));
        assStyle.setShadow(Integer.parseInt(spiShadowS.getValue().toString()));
        if (rb1.isSelected()){assStyle.setAlignment(1);}
        if (rb2.isSelected()){assStyle.setAlignment(2);}
        if (rb3.isSelected()){assStyle.setAlignment(3);}
        if (rb4.isSelected()){assStyle.setAlignment(4);}
        if (rb5.isSelected()){assStyle.setAlignment(5);}
        if (rb6.isSelected()){assStyle.setAlignment(6);}
        if (rb7.isSelected()){assStyle.setAlignment(7);}
        if (rb8.isSelected()){assStyle.setAlignment(8);}
        if (rb9.isSelected()){assStyle.setAlignment(9);}
        assStyle.setMarginL(Integer.parseInt(spiMarginL.getValue().toString()));
        assStyle.setMarginR(Integer.parseInt(spiMarginR.getValue().toString()));
        assStyle.setMarginV(Integer.parseInt(spiMarginVT.getValue().toString()));
        assStyle.setMarginB(Integer.parseInt(spiMarginB.getValue().toString()));
        assStyle.setMarginT(Integer.parseInt(spiMarginVT.getValue().toString()));
        Encoding enc = (Encoding)cbEncoding.getSelectedItem();
        assStyle.setEncoding(enc.getNumber());
    }

    /** <p>Update controls.<br />Met à jour les contrôles.</p> */
    private void updateControls(){
        tfStyleName.setText(assStyle.getName());
        cbFontname.setSelectedItem(assStyle.getFontname());
        spiFontsize.setValue(assStyle.getFontsize());

        spiText.setValue(Integer.parseInt(assStyle.getTextAlpha(), 16));
        spiKaraoke.setValue(Integer.parseInt(assStyle.getKaraAlpha(), 16));
        spiBorder.setValue(Integer.parseInt(assStyle.getOutlineAlpha(), 16));
        spiShadow.setValue(Integer.parseInt(assStyle.getBackAlpha(), 16));
        lblColor1.setBackground(assStyle.getTextCColor());
        lblColor2.setBackground(assStyle.getKaraCColor());
        lblColor3.setBackground(assStyle.getOutlineCColor());
        lblColor4.setBackground(assStyle.getBackCColor());

        cboBold.setSelected(assStyle.getBold());
        cboItalic.setSelected(assStyle.getItalic());
        cboUnderline.setSelected(assStyle.getUnderline());
        cboStrikeOut.setSelected(assStyle.getStrikeout());
        tfScaleX.setText(Double.toString(assStyle.getScaleX()));
        tfScaleY.setText(Double.toString(assStyle.getScaleY()));
        tfRotation.setText(Double.toString(assStyle.getAngle()));
        tfSpacing.setText(Double.toString(assStyle.getSpacing()));
        cboOpaqueBox.setSelected(assStyle.getBorderSStyle());
        spiBorderS.setValue(assStyle.getOutline());
        spiShadowS.setValue(assStyle.getShadow());
        if (assStyle.getAlignment()==1){rb1.setSelected(true);}
        if (assStyle.getAlignment()==2){rb2.setSelected(true);}
        if (assStyle.getAlignment()==3){rb3.setSelected(true);}
        if (assStyle.getAlignment()==4){rb4.setSelected(true);}
        if (assStyle.getAlignment()==5){rb5.setSelected(true);}
        if (assStyle.getAlignment()==6){rb6.setSelected(true);}
        if (assStyle.getAlignment()==7){rb7.setSelected(true);}
        if (assStyle.getAlignment()==8){rb8.setSelected(true);}
        if (assStyle.getAlignment()==9){rb9.setSelected(true);}
        spiMarginL.setValue(assStyle.getMarginL());
        spiMarginR.setValue(assStyle.getMarginR());
        spiMarginVT.setValue(assStyle.getMarginV());
        spiMarginB.setValue(assStyle.getMarginB());
        Encoding enc = Encoding.DEFAULT;
        cbEncoding.setSelectedItem(enc.getEncodingFrom(assStyle.getEncoding()));
    }
    
    /** <p>Return if the button OK has been pressed or not.<br />
     * Retourne si le bouton OK a été pressé ou non.</p> */
    public boolean isOkPressed(){
        return isOkPressed;
    }
    
    private void setCommonFont(Font f, java.awt.Component[] comps){
        for(java.awt.Component c : comps){
            if (c instanceof javax.swing.JComponent){
                javax.swing.JComponent jcomp = (javax.swing.JComponent)c;
                Font origin = jcomp.getFont();
                if(origin.isPlain()){
                    jcomp.setFont(f);
                }else{
                    jcomp.setFont(f.deriveFont(origin.getStyle(), origin.getSize2D()));
                }
                setCommonFont(f, jcomp.getComponents());
            }
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AssStyleDialog dialog = new AssStyleDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel_Button;
    private javax.swing.JButton OK_Button;
    private javax.swing.ButtonGroup bgAlignment;
    private javax.swing.JComboBox cbEncoding;
    private javax.swing.JComboBox cbFontname;
    private javax.swing.JCheckBox cboBold;
    private javax.swing.JCheckBox cboItalic;
    private javax.swing.JCheckBox cboOpaqueBox;
    private javax.swing.JCheckBox cboStrikeOut;
    private javax.swing.JCheckBox cboUnderline;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblColor1;
    private javax.swing.JLabel lblColor2;
    private javax.swing.JLabel lblColor3;
    private javax.swing.JLabel lblColor4;
    private javax.swing.JLabel lblColorBorder;
    private javax.swing.JLabel lblColorKaraoke;
    private javax.swing.JLabel lblColorShadow;
    private javax.swing.JLabel lblColorText4;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JRadioButton rb3;
    private javax.swing.JRadioButton rb4;
    private javax.swing.JRadioButton rb5;
    private javax.swing.JRadioButton rb6;
    private javax.swing.JRadioButton rb7;
    private javax.swing.JRadioButton rb8;
    private javax.swing.JRadioButton rb9;
    private javax.swing.JSpinner spiBorder;
    private javax.swing.JSpinner spiBorderS;
    private javax.swing.JSpinner spiFontsize;
    private javax.swing.JSpinner spiKaraoke;
    private javax.swing.JSpinner spiMarginB;
    private javax.swing.JSpinner spiMarginL;
    private javax.swing.JSpinner spiMarginR;
    private javax.swing.JSpinner spiMarginVT;
    private javax.swing.JSpinner spiShadow;
    private javax.swing.JSpinner spiShadowS;
    private javax.swing.JSpinner spiText;
    private javax.swing.JTextField tfRotation;
    private javax.swing.JTextField tfScaleX;
    private javax.swing.JTextField tfScaleY;
    private javax.swing.JTextField tfSpacing;
    private javax.swing.JTextField tfStyleName;
    // End of variables declaration//GEN-END:variables

}
