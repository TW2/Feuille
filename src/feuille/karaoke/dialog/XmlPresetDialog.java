/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * XmlPresetDialog.java
 *
 * Created on 16 déc. 2008, 00:00:03
 */

package feuille.karaoke.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import feuille.filter.PngFilter;
import feuille.filter.PngJpgGifFilter;
import feuille.filter.SubtitleFilter;
import feuille.karaoke.highlighter.HexadecimalHighlighterPainter;
import feuille.karaoke.highlighter.KeyHighlighterPainter;
import feuille.karaoke.highlighter.NormalHighlighterPainter;
import feuille.karaoke.highlighter.NumberHighlighterPainter;
import feuille.karaoke.highlighter.SymbolHighlighterPainter;
import feuille.karaoke.highlighter.UserVariableHighlighterPainter;
import feuille.karaoke.highlighter.VariableHighlighterPainter;
import feuille.karaoke.lib.AssIO;
import feuille.karaoke.lib.AssStyle;
import feuille.karaoke.lib.Clipboard;
import feuille.karaoke.lib.FxObject;
import feuille.karaoke.lib.FxObject.FxObjectType;
import feuille.karaoke.lib.ImagePreview;
import feuille.lib.Language;
import feuille.karaoke.plugins.FunctionsCollection;
import feuille.karaoke.renderer.listStyleRenderer;
import feuille.karaoke.renderer.tablePresetRenderer;

/**
 * <p>This is a dialog for the edition of XFX.<br />
 * C'est une boîte de dialogue pour l'édition des XFX.</p>
 * @author The Wingate 2940
 */
public class XmlPresetDialog extends javax.swing.JDialog {

    private ButtonPressed bp;
    private FxObject xmlfxo;
    private DefaultTableModel dtmodel;
    private SaveState saveState = SaveState.DISABLE;
    private DefaultComboBoxModel dcmFunctions;
    private FunctionsCollection funcc;
    private JTextField tfFocused = null;
    private tablePresetRenderer tpr = null;
    private DefaultListModel dlm; // Embedded Styles
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
    private String drawingPath = "";
    private String drawingsPath = "";
    private Language localeLanguage = feuille.MainFrame.getLanguage();
    private String PleaseHelpMe = "Please help me !?";
    private Highlighter high01, high02, high03, high04, high05;
    private Highlighter.HighlightPainter keywordPainter = new KeyHighlighterPainter();
    private Highlighter.HighlightPainter numberPainter = new NumberHighlighterPainter();
    private Highlighter.HighlightPainter normalPainter = new NormalHighlighterPainter();
    private Highlighter.HighlightPainter symbolPainter = new SymbolHighlighterPainter();
    private Highlighter.HighlightPainter hexaPainter = new HexadecimalHighlighterPainter();
    private Highlighter.HighlightPainter uvarPainter = new UserVariableHighlighterPainter();
    private Highlighter.HighlightPainter lvarPainter = new VariableHighlighterPainter();

    
    //With this variable we'll can open others dialogs into this one.
    //First, we have to set it in the main public method. (frame==parent)
    private Frame frame;

    private String EXAMPLE_K = "<example = {\\k~%dK/10~} >";
    private String EXAMPLE_NONE = "<example = [none] >";

    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }

    public enum SaveState{
        DISABLE, ENABLE;
    }

    public enum Column{
        LAYER(0), COMMANDS(1);

        private int id;

        Column(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }

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

        Encoding(int number, String sEnco){
            this.number = number;
            this.sEnco = sEnco;
        }

        public int getNumber(){
            return number;
        }

        public String getEncoding(){
            return sEnco;
        }

        @Override
        public String toString(){
            return number+" - "+sEnco;
        }
        
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

    /** <p>Creates new form XmlPresetDialog.<br />
     * Crée un nouveau formulaire XmlPresetDialog.</p> */
    public XmlPresetDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        bp = ButtonPressed.NONE;

        String[] fxHead = new String[]{"Layer", "Effects"};

        dtmodel = new DefaultTableModel(null,fxHead){
            Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                    false, true};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        layersTable.setModel(dtmodel);
        TableColumn column;
        for (int i = 0; i < 2; i++) {
            column = layersTable.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(20);
                    column.setIdentifier(Column.LAYER.getId());
                    break; //Layer
                case 1:
                    column.setPreferredWidth(600);
                    column.setIdentifier(Column.COMMANDS.getId());
                    break; //Commands
            }
        }

        //****************************************************
        // Set the renderer
        //****************************************************

        tpr = new tablePresetRenderer();
        tpr.setFont("Arial Unicode MS",java.awt.Font.PLAIN,11);
        layersTable.setDefaultRenderer(String.class, tpr);

        // Utility for functions (or effects type)
        dcmFunctions = new DefaultComboBoxModel();
        cbFunctions.setModel(dcmFunctions);
        cbFunctions.setRenderer(new MyCellRenderer());

        //****************************************************
        // Set the Embedded Styles area
        //****************************************************
        dlm = new DefaultListModel();
        lstStyles.setModel(dlm);
        lstStyles.setCellRenderer(new listStyleRenderer());
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



        //Setting up the frame variable to parent, referencing top level parent
        // as real parent for all. Useful to open new JDialog into this one.
        frame = parent;
        
        // Setting up the scripting object (epScripting) to work with
        // the opensource project JSyntaxPane - see web site :
        // http://code.google.com/p/jsyntaxpane/
        jsyntaxpane.DefaultSyntaxKit.initKit();
        epVariables.setContentType("text/ruby");
        epVariables.setComponentPopupMenu(popOverrides1);
        
        //****************************************************
        // Localization
        //****************************************************
        javax.swing.border.TitledBorder tb;
        if(localeLanguage.getValueOf("titleXPD")!=null){setTitle(localeLanguage.getValueOf("titleXPD"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){Ok_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("buttonAdd")!=null){btnAddLayer.setText(localeLanguage.getValueOf("buttonAdd"));}
        if(localeLanguage.getValueOf("buttonAdd")!=null){btnAdd.setText(localeLanguage.getValueOf("buttonAdd"));}
        if(localeLanguage.getValueOf("buttonGet")!=null){btnEditLayer.setText(localeLanguage.getValueOf("buttonGet"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnChangeLayer.setText(localeLanguage.getValueOf("buttonChange"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnPreview.setText(localeLanguage.getValueOf("buttonChange"));}
        if(localeLanguage.getValueOf("buttonDelete")!=null){btnDeleteLayer.setText(localeLanguage.getValueOf("buttonDelete"));}
        if(localeLanguage.getValueOf("buttonDelete")!=null){btnDelete.setText(localeLanguage.getValueOf("buttonDelete"));}
        if(localeLanguage.getValueOf("buttonModify")!=null){btnModify.setText(localeLanguage.getValueOf("buttonModify"));}
        if(localeLanguage.getValueOf("labelFxType")!=null){jLabel11.setText(localeLanguage.getValueOf("labelFxType"));}
        if(localeLanguage.getValueOf("labelName")!=null){jLabel1.setText(localeLanguage.getValueOf("labelName"));}
        if(localeLanguage.getValueOf("labelMoment")!=null){jLabel9.setText(localeLanguage.getValueOf("labelMoment"));}
        if(localeLanguage.getValueOf("labelFirstLayer")!=null){jLabel8.setText(localeLanguage.getValueOf("labelFirstLayer"));}
        if(localeLanguage.getValueOf("labelTime")!=null){jLabel10.setText(localeLanguage.getValueOf("labelTime"));}
        //if(localeLanguage.getValueOf("labelPleaseHelpMe")!=null){lblHelpMe.setText(localeLanguage.getValueOf("labelPleaseHelpMe"));}
        if(localeLanguage.getValueOf("labelPleaseHelpMe")!=null){PleaseHelpMe=localeLanguage.getValueOf("labelPleaseHelpMe");}
        if(localeLanguage.getValueOf("labelLayersDetails")!=null){jLabel7.setText(localeLanguage.getValueOf("labelLayersDetails"));}
        if(localeLanguage.getValueOf("labelOverrides")!=null){jLabel2.setText(localeLanguage.getValueOf("labelOverrides"));}
        if(localeLanguage.getValueOf("labelInnerOver")!=null){jLabel3.setText(localeLanguage.getValueOf("labelInnerOver"));}
        if(localeLanguage.getValueOf("labelLastOver")!=null){jLabel4.setText(localeLanguage.getValueOf("labelLastOver"));}
        if(localeLanguage.getValueOf("labelBeforeSyl")!=null){jLabel5.setText(localeLanguage.getValueOf("labelBeforeSyl"));}
        if(localeLanguage.getValueOf("labelAfterSyl")!=null){jLabel6.setText(localeLanguage.getValueOf("labelAfterSyl"));}
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
        if(localeLanguage.getValueOf("labelAuthor")!=null){jLabel12.setText(localeLanguage.getValueOf("labelAuthor"));}
        if(localeLanguage.getValueOf("labelComments")!=null){jLabel13.setText(localeLanguage.getValueOf("labelComments"));}
        if(localeLanguage.getValueOf("labelPreview")!=null){jLabel14.setText(localeLanguage.getValueOf("labelPreview"));}
        if(localeLanguage.getValueOf("labelCollection")!=null){jLabel15.setText(localeLanguage.getValueOf("labelCollection"));}
        if(localeLanguage.getValueOf("rbuttonBefore")!=null){rbMomentBefore.setText(localeLanguage.getValueOf("rbuttonBefore"));}
        if(localeLanguage.getValueOf("rbuttonMeantime")!=null){rbMomentMeantime.setText(localeLanguage.getValueOf("rbuttonMeantime"));}
        if(localeLanguage.getValueOf("rbuttonAfter")!=null){rbMomentAfter.setText(localeLanguage.getValueOf("rbuttonAfter"));}
        if(localeLanguage.getValueOf("checkboxSaveFx")!=null){cbSaveEffect.setText(localeLanguage.getValueOf("checkboxSaveFx"));}
        if(localeLanguage.getValueOf("checkboxBold")!=null){cboBold.setText(localeLanguage.getValueOf("checkboxBold"));}
        if(localeLanguage.getValueOf("checkboxItalic")!=null){cboItalic.setText(localeLanguage.getValueOf("checkboxItalic"));}
        if(localeLanguage.getValueOf("checkboxUnderline")!=null){cboUnderline.setText(localeLanguage.getValueOf("checkboxUnderline"));}
        if(localeLanguage.getValueOf("checkboxStrikeOut")!=null){cboStrikeOut.setText(localeLanguage.getValueOf("checkboxStrikeOut"));}
        if(localeLanguage.getValueOf("checkboxOpaqueBox")!=null){cboOpaqueBox.setText(localeLanguage.getValueOf("checkboxOpaqueBox"));}
        if(localeLanguage.getValueOf("tabEffects")!=null){jTabbedPane1.setTitleAt(0, localeLanguage.getValueOf("tabEffects"));}
        if(localeLanguage.getValueOf("tabVariables")!=null){jTabbedPane1.setTitleAt(1, localeLanguage.getValueOf("tabVariables"));}
        if(localeLanguage.getValueOf("tabEmbedStyles")!=null){jTabbedPane1.setTitleAt(2, localeLanguage.getValueOf("tabEmbedStyles"));}
        tb = (javax.swing.border.TitledBorder)panPreview.getBorder();
        if(localeLanguage.getValueOf("tbdPreview")!=null){tb.setTitle(localeLanguage.getValueOf("tbdPreview"));}
        //if(localeLanguage.getValueOf("toolHelpMe")!=null){lblHelpMe.setToolTipText(localeLanguage.getValueOf("toolHelpMe"));}
        if(localeLanguage.getValueOf("popmCut")!=null){popmCut.setText(localeLanguage.getValueOf("popmCut"));}
        if(localeLanguage.getValueOf("popmCut")!=null){popmCut1.setText(localeLanguage.getValueOf("popmCut"));}
        if(localeLanguage.getValueOf("popmCut")!=null){popmCut2.setText(localeLanguage.getValueOf("popmCut"));}
        if(localeLanguage.getValueOf("popmCopy")!=null){popmCopy.setText(localeLanguage.getValueOf("popmCopy"));}
        if(localeLanguage.getValueOf("popmCopy")!=null){popmCopy1.setText(localeLanguage.getValueOf("popmCopy"));}
        if(localeLanguage.getValueOf("popmCopy")!=null){popmCopy2.setText(localeLanguage.getValueOf("popmCopy"));}
        if(localeLanguage.getValueOf("popmPaste")!=null){popmPaste.setText(localeLanguage.getValueOf("popmPaste"));}
        if(localeLanguage.getValueOf("popmPaste")!=null){popmPaste1.setText(localeLanguage.getValueOf("popmPaste"));}
        if(localeLanguage.getValueOf("popmPaste")!=null){popmPaste2.setText(localeLanguage.getValueOf("popmPaste"));}
        if(localeLanguage.getValueOf("popmDelete")!=null){popmDelete.setText(localeLanguage.getValueOf("popmDelete"));}
        if(localeLanguage.getValueOf("popmDelete")!=null){popmDelete1.setText(localeLanguage.getValueOf("popmDelete"));}
        if(localeLanguage.getValueOf("popmDelete")!=null){popmDelete2.setText(localeLanguage.getValueOf("popmDelete"));}
        if(localeLanguage.getValueOf("popmClear")!=null){popmClear.setText(localeLanguage.getValueOf("popmClear"));}
        if(localeLanguage.getValueOf("popmClear")!=null){popmClear1.setText(localeLanguage.getValueOf("popmClear"));}
        if(localeLanguage.getValueOf("popmClear")!=null){popmClearAll2.setText(localeLanguage.getValueOf("popmClear"));}
        if(localeLanguage.getValueOf("popmSelectAll")!=null){popmSelAll.setText(localeLanguage.getValueOf("popmSelectAll"));}
        if(localeLanguage.getValueOf("popmSelectAll")!=null){popmSelAll1.setText(localeLanguage.getValueOf("popmSelectAll"));}
        if(localeLanguage.getValueOf("popmSelectAll")!=null){popmSelAll2.setText(localeLanguage.getValueOf("popmSelectAll"));}
        if(localeLanguage.getValueOf("popmColor")!=null){popmColor.setText(localeLanguage.getValueOf("popmColor"));}
        if(localeLanguage.getValueOf("popmAlpha")!=null){popmAlpha.setText(localeLanguage.getValueOf("popmAlpha"));}
        if(localeLanguage.getValueOf("popmInsOver")!=null){popmOverrides.setText(localeLanguage.getValueOf("popmInsOver"));}
        if(localeLanguage.getValueOf("popmInsCalc")!=null){popmIntCalc.setText(localeLanguage.getValueOf("popmInsCalc"));}
        if(localeLanguage.getValueOf("popmInsFCalc")!=null){popmFloCalc.setText(localeLanguage.getValueOf("popmInsFCalc"));}
        if(localeLanguage.getValueOf("popmInsDraw")!=null){popmDrawing.setText(localeLanguage.getValueOf("popmInsDraw"));}
        if(localeLanguage.getValueOf("popmForAni")!=null){popmKaraOK.setText(localeLanguage.getValueOf("popmForAni"));}
        if(localeLanguage.getValueOf("popmForConf")!=null){popmKaraNOK.setText(localeLanguage.getValueOf("popmForConf"));}
        if(localeLanguage.getValueOf("popmSurround")!=null){popmSurround.setText(localeLanguage.getValueOf("popmSurround"));}
        if(localeLanguage.getValueOf("popmDelSur")!=null){popmDelSurround.setText(localeLanguage.getValueOf("popmDelSur"));}
        if(localeLanguage.getValueOf("popm_b")!=null){popm_b.setText(localeLanguage.getValueOf("popm_b"));}
        if(localeLanguage.getValueOf("popm_i")!=null){popm_i.setText(localeLanguage.getValueOf("popm_i"));}
        if(localeLanguage.getValueOf("popm_u")!=null){popm_u.setText(localeLanguage.getValueOf("popm_u"));}
        if(localeLanguage.getValueOf("popm_s")!=null){popm_s.setText(localeLanguage.getValueOf("popm_s"));}
        if(localeLanguage.getValueOf("popm_bord")!=null){popm_bord.setText(localeLanguage.getValueOf("popm_bord"));}
        if(localeLanguage.getValueOf("popm_shad")!=null){popm_shad.setText(localeLanguage.getValueOf("popm_shad"));}
        if(localeLanguage.getValueOf("popm_be")!=null){popm_be.setText(localeLanguage.getValueOf("popm_be"));}
        if(localeLanguage.getValueOf("popm_blur")!=null){popm_blur.setText(localeLanguage.getValueOf("popm_blur"));}
        if(localeLanguage.getValueOf("popm_fs")!=null){popm_fs.setText(localeLanguage.getValueOf("popm_fs"));}
        if(localeLanguage.getValueOf("popm_fscx")!=null){popm_fscx.setText(localeLanguage.getValueOf("popm_fscx"));}
        if(localeLanguage.getValueOf("popm_fscy")!=null){popm_fscy.setText(localeLanguage.getValueOf("popm_fscy"));}
        if(localeLanguage.getValueOf("popm_fsp")!=null){popm_fsp.setText(localeLanguage.getValueOf("popm_fsp"));}
        if(localeLanguage.getValueOf("popm_frx")!=null){popm_frx.setText(localeLanguage.getValueOf("popm_frx"));}
        if(localeLanguage.getValueOf("popm_fry")!=null){popm_fry.setText(localeLanguage.getValueOf("popm_fry"));}
        if(localeLanguage.getValueOf("popm_frz")!=null){popm_frz.setText(localeLanguage.getValueOf("popm_frz"));}
        if(localeLanguage.getValueOf("popm_1c")!=null){popm_1c.setText(localeLanguage.getValueOf("popm_1c"));}
        if(localeLanguage.getValueOf("popm_2c")!=null){popm_2c.setText(localeLanguage.getValueOf("popm_2c"));}
        if(localeLanguage.getValueOf("popm_3c")!=null){popm_3c.setText(localeLanguage.getValueOf("popm_3c"));}
        if(localeLanguage.getValueOf("popm_4c")!=null){popm_4c.setText(localeLanguage.getValueOf("popm_4c"));}
        if(localeLanguage.getValueOf("popm_alpha")!=null){popm_alpha.setText(localeLanguage.getValueOf("popm_alpha"));}
        if(localeLanguage.getValueOf("popm_1a")!=null){popm_1a.setText(localeLanguage.getValueOf("popm_1a"));}
        if(localeLanguage.getValueOf("popm_2a")!=null){popm_2a.setText(localeLanguage.getValueOf("popm_2a"));}
        if(localeLanguage.getValueOf("popm_3a")!=null){popm_3a.setText(localeLanguage.getValueOf("popm_3a"));}
        if(localeLanguage.getValueOf("popm_4a")!=null){popm_4a.setText(localeLanguage.getValueOf("popm_4a"));}
        if(localeLanguage.getValueOf("popm_k")!=null){popm_k.setText(localeLanguage.getValueOf("popm_k"));}
        if(localeLanguage.getValueOf("popm_kf")!=null){popm_kf.setText(localeLanguage.getValueOf("popm_kf"));}
        if(localeLanguage.getValueOf("popm_ko")!=null){popm_ko.setText(localeLanguage.getValueOf("popm_ko"));}
        if(localeLanguage.getValueOf("popm_t")!=null){popm_t.setText(localeLanguage.getValueOf("popm_t"));}
        if(localeLanguage.getValueOf("popm_r")!=null){popm_reset.setText(localeLanguage.getValueOf("popm_r"));}
        if(localeLanguage.getValueOf("popm_fn")!=null){popm_fn.setText(localeLanguage.getValueOf("popm_fn"));}
        if(localeLanguage.getValueOf("popm_fe")!=null){popm_fe.setText(localeLanguage.getValueOf("popm_fe"));}
        if(localeLanguage.getValueOf("popm_q")!=null){popm_q.setText(localeLanguage.getValueOf("popm_q"));}
        if(localeLanguage.getValueOf("popm_a")!=null){popm_a.setText(localeLanguage.getValueOf("popm_a"));}
        if(localeLanguage.getValueOf("popm_an")!=null){popm_an.setText(localeLanguage.getValueOf("popm_an"));}
        if(localeLanguage.getValueOf("popm_pos")!=null){popm_pos.setText(localeLanguage.getValueOf("popm_pos"));}
        if(localeLanguage.getValueOf("popm_move")!=null){popm_move.setText(localeLanguage.getValueOf("popm_move"));}
        if(localeLanguage.getValueOf("popm_org")!=null){popm_org.setText(localeLanguage.getValueOf("popm_org"));}
        if(localeLanguage.getValueOf("popm_fad")!=null){popm_fad.setText(localeLanguage.getValueOf("popm_fad"));}
        if(localeLanguage.getValueOf("popm_fade")!=null){popm_fade.setText(localeLanguage.getValueOf("popm_fade"));}
        if(localeLanguage.getValueOf("popm_clip")!=null){popm_clip.setText(localeLanguage.getValueOf("popm_clip"));}
        if(localeLanguage.getValueOf("popm_clip2")!=null){popm_clip2.setText(localeLanguage.getValueOf("popm_clip2"));}
        if(localeLanguage.getValueOf("popmForAni")!=null){popmKaraOK2.setText(localeLanguage.getValueOf("popmForAni"));}
        if(localeLanguage.getValueOf("popmForAni")!=null){popmKaraOK3.setText(localeLanguage.getValueOf("popmForAni"));}
        if(localeLanguage.getValueOf("popm_xbord")!=null){popm_xbord.setText(localeLanguage.getValueOf("popm_xbord"));}
        if(localeLanguage.getValueOf("popm_ybord")!=null){popm_ybord.setText(localeLanguage.getValueOf("popm_ybord"));}
        if(localeLanguage.getValueOf("popm_xshad")!=null){popm_xshad.setText(localeLanguage.getValueOf("popm_xshad"));}
        if(localeLanguage.getValueOf("popm_yshad")!=null){popm_yshad.setText(localeLanguage.getValueOf("popm_yshad"));}
        if(localeLanguage.getValueOf("popm_fax")!=null){popm_fax.setText(localeLanguage.getValueOf("popm_fax"));}
        if(localeLanguage.getValueOf("popm_fay")!=null){popm_fay.setText(localeLanguage.getValueOf("popm_fay"));}
        if(localeLanguage.getValueOf("popm_iclip")!=null){popm_iclip.setText(localeLanguage.getValueOf("popm_iclip"));}
        if(localeLanguage.getValueOf("popm_fsc")!=null){popm_fsc.setText(localeLanguage.getValueOf("popm_fsc"));}
        if(localeLanguage.getValueOf("popm_fsvp")!=null){popm_fsvp.setText(localeLanguage.getValueOf("popm_fsvp"));}
        if(localeLanguage.getValueOf("popm_frs")!=null){popm_frs.setText(localeLanguage.getValueOf("popm_frs"));}
        if(localeLanguage.getValueOf("popm_z")!=null){popm_z.setText(localeLanguage.getValueOf("popm_z"));}
        if(localeLanguage.getValueOf("popm_distort")!=null){popm_distort.setText(localeLanguage.getValueOf("popm_distort"));}
        if(localeLanguage.getValueOf("popm_md")!=null){popm_md.setText(localeLanguage.getValueOf("popm_md"));}
        if(localeLanguage.getValueOf("popm_mdx")!=null){popm_mdx.setText(localeLanguage.getValueOf("popm_mdx"));}
        if(localeLanguage.getValueOf("popm_mdy")!=null){popm_mdy.setText(localeLanguage.getValueOf("popm_mdy"));}
        if(localeLanguage.getValueOf("popm_mdz")!=null){popm_mdz.setText(localeLanguage.getValueOf("popm_mdz"));}
        if(localeLanguage.getValueOf("popm_1vc")!=null){popm_1vc.setText(localeLanguage.getValueOf("popm_1vc"));}
        if(localeLanguage.getValueOf("popm_2vc")!=null){popm_2vc.setText(localeLanguage.getValueOf("popm_2vc"));}
        if(localeLanguage.getValueOf("popm_3vc")!=null){popm_3vc.setText(localeLanguage.getValueOf("popm_3vc"));}
        if(localeLanguage.getValueOf("popm_4vc")!=null){popm_4vc.setText(localeLanguage.getValueOf("popm_4vc"));}
        if(localeLanguage.getValueOf("popm_1va")!=null){popm_1va.setText(localeLanguage.getValueOf("popm_1va"));}
        if(localeLanguage.getValueOf("popm_2va")!=null){popm_2va.setText(localeLanguage.getValueOf("popm_2va"));}
        if(localeLanguage.getValueOf("popm_3va")!=null){popm_3va.setText(localeLanguage.getValueOf("popm_3va"));}
        if(localeLanguage.getValueOf("popm_4va")!=null){popm_4va.setText(localeLanguage.getValueOf("popm_4va"));}
        if(localeLanguage.getValueOf("popm_1img")!=null){popm_1img.setText(localeLanguage.getValueOf("popm_1img"));}
        if(localeLanguage.getValueOf("popm_2img")!=null){popm_2img.setText(localeLanguage.getValueOf("popm_2img"));}
        if(localeLanguage.getValueOf("popm_3img")!=null){popm_3img.setText(localeLanguage.getValueOf("popm_3img"));}
        if(localeLanguage.getValueOf("popm_4img")!=null){popm_4img.setText(localeLanguage.getValueOf("popm_4img"));}
        if(localeLanguage.getValueOf("popm_jitter")!=null){popm_jitter.setText(localeLanguage.getValueOf("popm_jitter"));}
        if(localeLanguage.getValueOf("popm_iclip")!=null){popm_iclip2.setText(localeLanguage.getValueOf("popm_iclip"));}
        if(localeLanguage.getValueOf("popm_mover")!=null){popm_mover.setText(localeLanguage.getValueOf("popm_mover"));}
        if(localeLanguage.getValueOf("popm_moves3")!=null){popm_moves3.setText(localeLanguage.getValueOf("popm_moves3"));}
        if(localeLanguage.getValueOf("popm_moves4")!=null){popm_moves4.setText(localeLanguage.getValueOf("popm_moves4"));}
        if(localeLanguage.getValueOf("popm_movevc")!=null){popm_movevc.setText(localeLanguage.getValueOf("popm_movevc"));}
        if(localeLanguage.getValueOf("popm_movevc")!=null){popm_movevc2.setText(localeLanguage.getValueOf("popm_movevc"));}
        if(localeLanguage.getValueOf("popmStyImp")!=null){popmImport.setText(localeLanguage.getValueOf("popmStyImp"));}
        if(localeLanguage.getValueOf("popmStyExp")!=null){popmExport.setText(localeLanguage.getValueOf("popmStyExp"));}
        if(localeLanguage.getValueOf("popCodePNG")!=null){popmPNG.setText(localeLanguage.getValueOf("popCodePNG"));}
        if(localeLanguage.getValueOf("popmImportFrom")!=null){popmImportFrom.setText(localeLanguage.getValueOf("popmImportFrom"));}        
        if(localeLanguage.getValueOf("popmColor")!=null){popmColor1.setText(localeLanguage.getValueOf("popmColor"));}
        if(localeLanguage.getValueOf("popmAlpha")!=null){popmAlpha1.setText(localeLanguage.getValueOf("popmAlpha"));}
        if(localeLanguage.getValueOf("popmInsOver")!=null){popmOverrides1.setText(localeLanguage.getValueOf("popmInsOver"));}
        if(localeLanguage.getValueOf("popmInsDraw")!=null){popmDrawing1.setText(localeLanguage.getValueOf("popmInsDraw"));}
        if(localeLanguage.getValueOf("popmForAni")!=null){popmKaraOK1.setText(localeLanguage.getValueOf("popmForAni"));}
        if(localeLanguage.getValueOf("popmForConf")!=null){popmKaraNOK1.setText(localeLanguage.getValueOf("popmForConf"));}
        if(localeLanguage.getValueOf("popm_b")!=null){popm_b1.setText(localeLanguage.getValueOf("popm_b"));}
        if(localeLanguage.getValueOf("popm_i")!=null){popm_i1.setText(localeLanguage.getValueOf("popm_i"));}
        if(localeLanguage.getValueOf("popm_u")!=null){popm_u1.setText(localeLanguage.getValueOf("popm_u"));}
        if(localeLanguage.getValueOf("popm_s")!=null){popm_s1.setText(localeLanguage.getValueOf("popm_s"));}
        if(localeLanguage.getValueOf("popm_bord")!=null){popm_bord1.setText(localeLanguage.getValueOf("popm_bord"));}
        if(localeLanguage.getValueOf("popm_shad")!=null){popm_shad1.setText(localeLanguage.getValueOf("popm_shad"));}
        if(localeLanguage.getValueOf("popm_be")!=null){popm_be1.setText(localeLanguage.getValueOf("popm_be"));}
        if(localeLanguage.getValueOf("popm_blur")!=null){popm_blur1.setText(localeLanguage.getValueOf("popm_blur"));}
        if(localeLanguage.getValueOf("popm_fs")!=null){popm_fs1.setText(localeLanguage.getValueOf("popm_fs"));}
        if(localeLanguage.getValueOf("popm_fscx")!=null){popm_fscx1.setText(localeLanguage.getValueOf("popm_fscx"));}
        if(localeLanguage.getValueOf("popm_fscy")!=null){popm_fscy1.setText(localeLanguage.getValueOf("popm_fscy"));}
        if(localeLanguage.getValueOf("popm_fsp")!=null){popm_fsp1.setText(localeLanguage.getValueOf("popm_fsp"));}
        if(localeLanguage.getValueOf("popm_frx")!=null){popm_frx1.setText(localeLanguage.getValueOf("popm_frx"));}
        if(localeLanguage.getValueOf("popm_fry")!=null){popm_fry1.setText(localeLanguage.getValueOf("popm_fry"));}
        if(localeLanguage.getValueOf("popm_frz")!=null){popm_frz1.setText(localeLanguage.getValueOf("popm_frz"));}
        if(localeLanguage.getValueOf("popm_1c")!=null){popm_1c1.setText(localeLanguage.getValueOf("popm_1c"));}
        if(localeLanguage.getValueOf("popm_2c")!=null){popm_2c1.setText(localeLanguage.getValueOf("popm_2c"));}
        if(localeLanguage.getValueOf("popm_3c")!=null){popm_3c1.setText(localeLanguage.getValueOf("popm_3c"));}
        if(localeLanguage.getValueOf("popm_4c")!=null){popm_4c1.setText(localeLanguage.getValueOf("popm_4c"));}
        if(localeLanguage.getValueOf("popm_alpha")!=null){popm_alpha1.setText(localeLanguage.getValueOf("popm_alpha"));}
        if(localeLanguage.getValueOf("popm_1a")!=null){popm_1a1.setText(localeLanguage.getValueOf("popm_1a"));}
        if(localeLanguage.getValueOf("popm_2a")!=null){popm_2a1.setText(localeLanguage.getValueOf("popm_2a"));}
        if(localeLanguage.getValueOf("popm_3a")!=null){popm_3a1.setText(localeLanguage.getValueOf("popm_3a"));}
        if(localeLanguage.getValueOf("popm_4a")!=null){popm_4a1.setText(localeLanguage.getValueOf("popm_4a"));}
        if(localeLanguage.getValueOf("popm_k")!=null){popm_k1.setText(localeLanguage.getValueOf("popm_k"));}
        if(localeLanguage.getValueOf("popm_kf")!=null){popm_kf1.setText(localeLanguage.getValueOf("popm_kf"));}
        if(localeLanguage.getValueOf("popm_ko")!=null){popm_ko1.setText(localeLanguage.getValueOf("popm_ko"));}
        if(localeLanguage.getValueOf("popm_t")!=null){popm_t1.setText(localeLanguage.getValueOf("popm_t"));}
        if(localeLanguage.getValueOf("popm_r")!=null){popm_reset1.setText(localeLanguage.getValueOf("popm_r"));}
        if(localeLanguage.getValueOf("popm_fn")!=null){popm_fn1.setText(localeLanguage.getValueOf("popm_fn"));}
        if(localeLanguage.getValueOf("popm_fe")!=null){popm_fe1.setText(localeLanguage.getValueOf("popm_fe"));}
        if(localeLanguage.getValueOf("popm_q")!=null){popm_q1.setText(localeLanguage.getValueOf("popm_q"));}
        if(localeLanguage.getValueOf("popm_a")!=null){popm_a1.setText(localeLanguage.getValueOf("popm_a"));}
        if(localeLanguage.getValueOf("popm_an")!=null){popm_an1.setText(localeLanguage.getValueOf("popm_an"));}
        if(localeLanguage.getValueOf("popm_pos")!=null){popm_pos1.setText(localeLanguage.getValueOf("popm_pos"));}
        if(localeLanguage.getValueOf("popm_move")!=null){popm_move1.setText(localeLanguage.getValueOf("popm_move"));}
        if(localeLanguage.getValueOf("popm_org")!=null){popm_org1.setText(localeLanguage.getValueOf("popm_org"));}
        if(localeLanguage.getValueOf("popm_fad")!=null){popm_fad1.setText(localeLanguage.getValueOf("popm_fad"));}
        if(localeLanguage.getValueOf("popm_fade")!=null){popm_fade1.setText(localeLanguage.getValueOf("popm_fade"));}
        if(localeLanguage.getValueOf("popm_clip")!=null){popm_clip1.setText(localeLanguage.getValueOf("popm_clip"));}
        if(localeLanguage.getValueOf("popm_clip2")!=null){popm_clip3.setText(localeLanguage.getValueOf("popm_clip2"));}
        if(localeLanguage.getValueOf("popmForAni")!=null){popmKaraOK4.setText(localeLanguage.getValueOf("popmForAni"));}
        if(localeLanguage.getValueOf("popmForAni")!=null){popmKaraOK5.setText(localeLanguage.getValueOf("popmForAni"));}
        if(localeLanguage.getValueOf("popm_xbord")!=null){popm_xbord1.setText(localeLanguage.getValueOf("popm_xbord"));}
        if(localeLanguage.getValueOf("popm_ybord")!=null){popm_ybord1.setText(localeLanguage.getValueOf("popm_ybord"));}
        if(localeLanguage.getValueOf("popm_xshad")!=null){popm_xshad1.setText(localeLanguage.getValueOf("popm_xshad"));}
        if(localeLanguage.getValueOf("popm_yshad")!=null){popm_yshad1.setText(localeLanguage.getValueOf("popm_yshad"));}
        if(localeLanguage.getValueOf("popm_fax")!=null){popm_fax1.setText(localeLanguage.getValueOf("popm_fax"));}
        if(localeLanguage.getValueOf("popm_fay")!=null){popm_fay1.setText(localeLanguage.getValueOf("popm_fay"));}
        if(localeLanguage.getValueOf("popm_iclip")!=null){popm_iclip1.setText(localeLanguage.getValueOf("popm_iclip"));}
        if(localeLanguage.getValueOf("popm_fsc")!=null){popm_fsc1.setText(localeLanguage.getValueOf("popm_fsc"));}
        if(localeLanguage.getValueOf("popm_fsvp")!=null){popm_fsvp1.setText(localeLanguage.getValueOf("popm_fsvp"));}
        if(localeLanguage.getValueOf("popm_frs")!=null){popm_frs1.setText(localeLanguage.getValueOf("popm_frs"));}
        if(localeLanguage.getValueOf("popm_z")!=null){popm_z1.setText(localeLanguage.getValueOf("popm_z"));}
        if(localeLanguage.getValueOf("popm_distort")!=null){popm_distort1.setText(localeLanguage.getValueOf("popm_distort"));}
        if(localeLanguage.getValueOf("popm_md")!=null){popm_md1.setText(localeLanguage.getValueOf("popm_md"));}
        if(localeLanguage.getValueOf("popm_mdx")!=null){popm_mdx1.setText(localeLanguage.getValueOf("popm_mdx"));}
        if(localeLanguage.getValueOf("popm_mdy")!=null){popm_mdy1.setText(localeLanguage.getValueOf("popm_mdy"));}
        if(localeLanguage.getValueOf("popm_mdz")!=null){popm_mdz1.setText(localeLanguage.getValueOf("popm_mdz"));}
        if(localeLanguage.getValueOf("popm_1vc")!=null){popm_1vc1.setText(localeLanguage.getValueOf("popm_1vc"));}
        if(localeLanguage.getValueOf("popm_2vc")!=null){popm_2vc1.setText(localeLanguage.getValueOf("popm_2vc"));}
        if(localeLanguage.getValueOf("popm_3vc")!=null){popm_3vc1.setText(localeLanguage.getValueOf("popm_3vc"));}
        if(localeLanguage.getValueOf("popm_4vc")!=null){popm_4vc1.setText(localeLanguage.getValueOf("popm_4vc"));}
        if(localeLanguage.getValueOf("popm_1va")!=null){popm_1va1.setText(localeLanguage.getValueOf("popm_1va"));}
        if(localeLanguage.getValueOf("popm_2va")!=null){popm_2va1.setText(localeLanguage.getValueOf("popm_2va"));}
        if(localeLanguage.getValueOf("popm_3va")!=null){popm_3va1.setText(localeLanguage.getValueOf("popm_3va"));}
        if(localeLanguage.getValueOf("popm_4va")!=null){popm_4va1.setText(localeLanguage.getValueOf("popm_4va"));}
        if(localeLanguage.getValueOf("popm_1img")!=null){popm_1img1.setText(localeLanguage.getValueOf("popm_1img"));}
        if(localeLanguage.getValueOf("popm_2img")!=null){popm_2img1.setText(localeLanguage.getValueOf("popm_2img"));}
        if(localeLanguage.getValueOf("popm_3img")!=null){popm_3img1.setText(localeLanguage.getValueOf("popm_3img"));}
        if(localeLanguage.getValueOf("popm_4img")!=null){popm_4img1.setText(localeLanguage.getValueOf("popm_4img"));}
        if(localeLanguage.getValueOf("popm_jitter")!=null){popm_jitter1.setText(localeLanguage.getValueOf("popm_jitter"));}
        if(localeLanguage.getValueOf("popm_iclip")!=null){popm_iclip3.setText(localeLanguage.getValueOf("popm_iclip"));}
        if(localeLanguage.getValueOf("popm_mover")!=null){popm_mover1.setText(localeLanguage.getValueOf("popm_mover"));}
        if(localeLanguage.getValueOf("popm_moves3")!=null){popm_moves5.setText(localeLanguage.getValueOf("popm_moves3"));}
        if(localeLanguage.getValueOf("popm_moves4")!=null){popm_moves6.setText(localeLanguage.getValueOf("popm_moves4"));}
        if(localeLanguage.getValueOf("popm_movevc")!=null){popm_movevc1.setText(localeLanguage.getValueOf("popm_movevc"));}
        if(localeLanguage.getValueOf("popm_movevc")!=null){popm_movevc3.setText(localeLanguage.getValueOf("popm_movevc"));}
        if(localeLanguage.getValueOf("popCodePNG")!=null){popmPNG1.setText(localeLanguage.getValueOf("popCodePNG"));}
        if(localeLanguage.getValueOf("popmInsScript")!=null){popmInsScript.setText(localeLanguage.getValueOf("popmInsScript"));}
        if(localeLanguage.getValueOf("popmCodeInit")!=null){popmCodeInit.setText(localeLanguage.getValueOf("popmCodeInit"));}
        if(localeLanguage.getValueOf("popmCodeDef")!=null){popmCodeDef.setText(localeLanguage.getValueOf("popmCodeDef"));}
        if(localeLanguage.getValueOf("toolCodeDef")!=null){popmCodeDef.setToolTipText(localeLanguage.getValueOf("toolCodeDef"));}
        
        if(localeLanguage.getValueOf("taHelpPleaseXFX")!=null){taHelpPlease.setText(localeLanguage.getValueOf("taHelpPleaseXFX"));}
        if(localeLanguage.getValueOf("tabHelpPlease")!=null){jTabbedPane1.setTitleAt(4, localeLanguage.getValueOf("tabHelpPlease"));}
        
        for (int i = 0; i < 2; i++) {
            column = layersTable.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    if(localeLanguage.getValueOf("tableLayer")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableLayer"));
                    }
                    break;
                case 1:
                    if(localeLanguage.getValueOf("tableEffects")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableEffects"));
                    }
                    break;
            }
        }
        
        //****************************************************
        // Syntax highlight in textfield
        //****************************************************
        // The text is transparent thanks to this color.
        // We want to paint the text using a highlighterpainter.
        tfOverrides.setForeground(new Color(0, 0, 0, 0));
        // We want to view the caret in black.
        tfOverrides.setCaretColor(Color.black);
        // BOLD for the best visibility
        tfOverrides.setFont(tfOverrides.getFont().deriveFont(Font.BOLD));
        // Get highlighter for this text component.
        high01 = tfOverrides.getHighlighter();
        // Add the caret listener
        tfOverrides.addCaretListener(new CaretListener(){
            @Override
            public void caretUpdate(CaretEvent e) {
                updateASSTextField(tfOverrides, high01);
            }
        });
        tfOverrides.setCaretPosition(0);
        tfOverrides.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                updateASSTextField(tfOverrides, high01);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateASSTextField(tfOverrides, high01);
            }
        });
        
        // The text is transparent thanks to this color.
        // We want to paint the text using a highlighterpainter.
        tfInnerOverrides.setForeground(new Color(0, 0, 0, 0));
        // We want to view the caret in black.
        tfInnerOverrides.setCaretColor(Color.black);
        // BOLD for the best visibility
        tfInnerOverrides.setFont(tfInnerOverrides.getFont().deriveFont(Font.BOLD));
        // Get highlighter for this text component.
        high02 = tfInnerOverrides.getHighlighter();
        // Add the caret listener
        tfInnerOverrides.addCaretListener(new CaretListener(){
            @Override
            public void caretUpdate(CaretEvent e) {
                updateASSTextField(tfInnerOverrides, high02);
            }
        });
        tfInnerOverrides.setCaretPosition(0);
        tfInnerOverrides.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                updateASSTextField(tfInnerOverrides, high02);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateASSTextField(tfInnerOverrides, high02);
            }
        });
        
        // The text is transparent thanks to this color.
        // We want to paint the text using a highlighterpainter.
        tfLastOverrides.setForeground(new Color(0, 0, 0, 0));
        // We want to view the caret in black.
        tfLastOverrides.setCaretColor(Color.black);
        // BOLD for the best visibility
        tfLastOverrides.setFont(tfLastOverrides.getFont().deriveFont(Font.BOLD));
        // Get highlighter for this text component.
        high03 = tfLastOverrides.getHighlighter();
        // Add the caret listener
        tfLastOverrides.addCaretListener(new CaretListener(){
            @Override
            public void caretUpdate(CaretEvent e) {
                updateASSTextField(tfLastOverrides, high03);
            }
        });
        tfLastOverrides.setCaretPosition(0);
        tfLastOverrides.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                updateASSTextField(tfLastOverrides, high03);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateASSTextField(tfLastOverrides, high03);
            }
        });
        
        // The text is transparent thanks to this color.
        // We want to paint the text using a highlighterpainter.
        tfBefore.setForeground(new Color(0, 0, 0, 0));
        // We want to view the caret in black.
        tfBefore.setCaretColor(Color.black);
        // BOLD for the best visibility
        tfBefore.setFont(tfBefore.getFont().deriveFont(Font.BOLD));
        // Get highlighter for this text component.
        high04 = tfBefore.getHighlighter();
        // Add the caret listener
        tfBefore.addCaretListener(new CaretListener(){
            @Override
            public void caretUpdate(CaretEvent e) {
                updateASSTextField(tfBefore, high04);
            }
        });
        tfBefore.setCaretPosition(0);
        tfBefore.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                updateASSTextField(tfBefore, high04);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateASSTextField(tfBefore, high04);
            }
        });
        
        // The text is transparent thanks to this color.
        // We want to paint the text using a highlighterpainter.
        tfAfter.setForeground(new Color(0, 0, 0, 0));
        // We want to view the caret in black.
        tfAfter.setCaretColor(Color.black);
        // BOLD for the best visibility
        tfAfter.setFont(tfAfter.getFont().deriveFont(Font.BOLD));
        // Get highlighter for this text component.
        high05 = tfAfter.getHighlighter();
        // Add the caret listener
        tfAfter.addCaretListener(new CaretListener(){
            @Override
            public void caretUpdate(CaretEvent e) {
                updateASSTextField(tfAfter, high05);
            }
        });
        tfAfter.setCaretPosition(0);
        tfAfter.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                updateASSTextField(tfAfter, high05);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateASSTextField(tfAfter, high05);
            }
        });
        
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

        bgMoment = new javax.swing.ButtonGroup();
        bgLineMode = new javax.swing.ButtonGroup();
        bgSentenceMode = new javax.swing.ButtonGroup();
        bgTypeMode = new javax.swing.ButtonGroup();
        popOverrides = new javax.swing.JPopupMenu();
        popmCut = new javax.swing.JMenuItem();
        popmCopy = new javax.swing.JMenuItem();
        popmPaste = new javax.swing.JMenuItem();
        popmDelete = new javax.swing.JMenuItem();
        popmOverSep1 = new javax.swing.JPopupMenu.Separator();
        popmSelAll = new javax.swing.JMenuItem();
        popmClear = new javax.swing.JMenuItem();
        popmOverSep2 = new javax.swing.JSeparator();
        popmColor = new javax.swing.JMenuItem();
        popmAlpha = new javax.swing.JMenuItem();
        popmPNG = new javax.swing.JMenuItem();
        popmOverSep3 = new javax.swing.JSeparator();
        popmOverrides = new javax.swing.JMenu();
        popmKaraOK = new javax.swing.JMenu();
        popm_b = new javax.swing.JMenuItem();
        popm_i = new javax.swing.JMenuItem();
        popm_u = new javax.swing.JMenuItem();
        popm_s = new javax.swing.JMenuItem();
        popm_bord = new javax.swing.JMenuItem();
        popm_shad = new javax.swing.JMenuItem();
        popm_be = new javax.swing.JMenuItem();
        popm_fs = new javax.swing.JMenuItem();
        popm_fscx = new javax.swing.JMenuItem();
        popm_fscy = new javax.swing.JMenuItem();
        popm_fsp = new javax.swing.JMenuItem();
        popm_frx = new javax.swing.JMenuItem();
        popm_fry = new javax.swing.JMenuItem();
        popm_frz = new javax.swing.JMenuItem();
        popm_1c = new javax.swing.JMenuItem();
        popm_2c = new javax.swing.JMenuItem();
        popm_3c = new javax.swing.JMenuItem();
        popm_4c = new javax.swing.JMenuItem();
        popm_alpha = new javax.swing.JMenuItem();
        popm_1a = new javax.swing.JMenuItem();
        popm_2a = new javax.swing.JMenuItem();
        popm_3a = new javax.swing.JMenuItem();
        popm_4a = new javax.swing.JMenuItem();
        popm_clip = new javax.swing.JMenuItem();
        popmKaraOK2 = new javax.swing.JMenu();
        popm_xbord = new javax.swing.JMenuItem();
        popm_ybord = new javax.swing.JMenuItem();
        popm_xshad = new javax.swing.JMenuItem();
        popm_yshad = new javax.swing.JMenuItem();
        popm_blur = new javax.swing.JMenuItem();
        popm_fax = new javax.swing.JMenuItem();
        popm_fay = new javax.swing.JMenuItem();
        popm_iclip = new javax.swing.JMenuItem();
        popmKaraOK3 = new javax.swing.JMenu();
        popm_fsc = new javax.swing.JMenuItem();
        popm_fsvp = new javax.swing.JMenuItem();
        popm_frs = new javax.swing.JMenuItem();
        popm_z = new javax.swing.JMenuItem();
        popm_distort = new javax.swing.JMenuItem();
        popm_md = new javax.swing.JMenuItem();
        popm_mdx = new javax.swing.JMenuItem();
        popm_mdy = new javax.swing.JMenuItem();
        popm_mdz = new javax.swing.JMenuItem();
        popm_1vc = new javax.swing.JMenuItem();
        popm_2vc = new javax.swing.JMenuItem();
        popm_3vc = new javax.swing.JMenuItem();
        popm_4vc = new javax.swing.JMenuItem();
        popm_1va = new javax.swing.JMenuItem();
        popm_2va = new javax.swing.JMenuItem();
        popm_3va = new javax.swing.JMenuItem();
        popm_4va = new javax.swing.JMenuItem();
        popm_1img = new javax.swing.JMenuItem();
        popm_2img = new javax.swing.JMenuItem();
        popm_3img = new javax.swing.JMenuItem();
        popm_4img = new javax.swing.JMenuItem();
        popm_jitter = new javax.swing.JMenuItem();
        popmKaraNOK = new javax.swing.JMenu();
        popm_fn = new javax.swing.JMenuItem();
        popm_fe = new javax.swing.JMenuItem();
        popm_q = new javax.swing.JMenuItem();
        popm_a = new javax.swing.JMenuItem();
        popm_an = new javax.swing.JMenuItem();
        popm_pos = new javax.swing.JMenuItem();
        popm_move = new javax.swing.JMenuItem();
        popm_org = new javax.swing.JMenuItem();
        popm_fad = new javax.swing.JMenuItem();
        popm_fade = new javax.swing.JMenuItem();
        popm_clip2 = new javax.swing.JMenuItem();
        popm_iclip2 = new javax.swing.JMenuItem();
        popm_mover = new javax.swing.JMenuItem();
        popm_moves3 = new javax.swing.JMenuItem();
        popm_moves4 = new javax.swing.JMenuItem();
        popm_movevc = new javax.swing.JMenuItem();
        popm_movevc2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        popmSurround = new javax.swing.JMenuItem();
        popmDelSurround = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        popm_k = new javax.swing.JMenuItem();
        popm_kf = new javax.swing.JMenuItem();
        popm_ko = new javax.swing.JMenuItem();
        popm_t = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        popm_reset = new javax.swing.JMenuItem();
        popmOverSep4 = new javax.swing.JPopupMenu.Separator();
        popmIntCalc = new javax.swing.JMenuItem();
        popmFloCalc = new javax.swing.JMenuItem();
        popmDrawing = new javax.swing.JMenuItem();
        fcPreview = new javax.swing.JFileChooser();
        bgAlignment = new javax.swing.ButtonGroup();
        popStyleList = new javax.swing.JPopupMenu();
        popmImport = new javax.swing.JMenuItem();
        popmExport = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        popmImportFrom = new javax.swing.JMenuItem();
        popAbout = new javax.swing.JPopupMenu();
        popmCut2 = new javax.swing.JMenuItem();
        popmCopy2 = new javax.swing.JMenuItem();
        popmPaste2 = new javax.swing.JMenuItem();
        popmDelete2 = new javax.swing.JMenuItem();
        popmAboutSep1 = new javax.swing.JPopupMenu.Separator();
        popmSelAll2 = new javax.swing.JMenuItem();
        popmClearAll2 = new javax.swing.JMenuItem();
        popOverrides1 = new javax.swing.JPopupMenu();
        popmInsScript = new javax.swing.JMenu();
        popmCodeInit = new javax.swing.JMenuItem();
        popmCodeDef = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        popmCut1 = new javax.swing.JMenuItem();
        popmCopy1 = new javax.swing.JMenuItem();
        popmPaste1 = new javax.swing.JMenuItem();
        popmDelete1 = new javax.swing.JMenuItem();
        popmOverSep5 = new javax.swing.JPopupMenu.Separator();
        popmSelAll1 = new javax.swing.JMenuItem();
        popmClear1 = new javax.swing.JMenuItem();
        popmOverSep6 = new javax.swing.JSeparator();
        popmColor1 = new javax.swing.JMenuItem();
        popmAlpha1 = new javax.swing.JMenuItem();
        popmPNG1 = new javax.swing.JMenuItem();
        popmOverSep7 = new javax.swing.JSeparator();
        popmOverrides1 = new javax.swing.JMenu();
        popmKaraOK1 = new javax.swing.JMenu();
        popm_b1 = new javax.swing.JMenuItem();
        popm_i1 = new javax.swing.JMenuItem();
        popm_u1 = new javax.swing.JMenuItem();
        popm_s1 = new javax.swing.JMenuItem();
        popm_bord1 = new javax.swing.JMenuItem();
        popm_shad1 = new javax.swing.JMenuItem();
        popm_be1 = new javax.swing.JMenuItem();
        popm_fs1 = new javax.swing.JMenuItem();
        popm_fscx1 = new javax.swing.JMenuItem();
        popm_fscy1 = new javax.swing.JMenuItem();
        popm_fsp1 = new javax.swing.JMenuItem();
        popm_frx1 = new javax.swing.JMenuItem();
        popm_fry1 = new javax.swing.JMenuItem();
        popm_frz1 = new javax.swing.JMenuItem();
        popm_1c1 = new javax.swing.JMenuItem();
        popm_2c1 = new javax.swing.JMenuItem();
        popm_3c1 = new javax.swing.JMenuItem();
        popm_4c1 = new javax.swing.JMenuItem();
        popm_alpha1 = new javax.swing.JMenuItem();
        popm_1a1 = new javax.swing.JMenuItem();
        popm_2a1 = new javax.swing.JMenuItem();
        popm_3a1 = new javax.swing.JMenuItem();
        popm_4a1 = new javax.swing.JMenuItem();
        popm_clip1 = new javax.swing.JMenuItem();
        popmKaraOK4 = new javax.swing.JMenu();
        popm_xbord1 = new javax.swing.JMenuItem();
        popm_ybord1 = new javax.swing.JMenuItem();
        popm_xshad1 = new javax.swing.JMenuItem();
        popm_yshad1 = new javax.swing.JMenuItem();
        popm_blur1 = new javax.swing.JMenuItem();
        popm_fax1 = new javax.swing.JMenuItem();
        popm_fay1 = new javax.swing.JMenuItem();
        popm_iclip1 = new javax.swing.JMenuItem();
        popmKaraOK5 = new javax.swing.JMenu();
        popm_fsc1 = new javax.swing.JMenuItem();
        popm_fsvp1 = new javax.swing.JMenuItem();
        popm_frs1 = new javax.swing.JMenuItem();
        popm_z1 = new javax.swing.JMenuItem();
        popm_distort1 = new javax.swing.JMenuItem();
        popm_md1 = new javax.swing.JMenuItem();
        popm_mdx1 = new javax.swing.JMenuItem();
        popm_mdy1 = new javax.swing.JMenuItem();
        popm_mdz1 = new javax.swing.JMenuItem();
        popm_1vc1 = new javax.swing.JMenuItem();
        popm_2vc1 = new javax.swing.JMenuItem();
        popm_3vc1 = new javax.swing.JMenuItem();
        popm_4vc1 = new javax.swing.JMenuItem();
        popm_1va1 = new javax.swing.JMenuItem();
        popm_2va1 = new javax.swing.JMenuItem();
        popm_3va1 = new javax.swing.JMenuItem();
        popm_4va1 = new javax.swing.JMenuItem();
        popm_1img1 = new javax.swing.JMenuItem();
        popm_2img1 = new javax.swing.JMenuItem();
        popm_3img1 = new javax.swing.JMenuItem();
        popm_4img1 = new javax.swing.JMenuItem();
        popm_jitter1 = new javax.swing.JMenuItem();
        popmKaraNOK1 = new javax.swing.JMenu();
        popm_fn1 = new javax.swing.JMenuItem();
        popm_fe1 = new javax.swing.JMenuItem();
        popm_q1 = new javax.swing.JMenuItem();
        popm_a1 = new javax.swing.JMenuItem();
        popm_an1 = new javax.swing.JMenuItem();
        popm_pos1 = new javax.swing.JMenuItem();
        popm_move1 = new javax.swing.JMenuItem();
        popm_org1 = new javax.swing.JMenuItem();
        popm_fad1 = new javax.swing.JMenuItem();
        popm_fade1 = new javax.swing.JMenuItem();
        popm_clip3 = new javax.swing.JMenuItem();
        popm_iclip3 = new javax.swing.JMenuItem();
        popm_mover1 = new javax.swing.JMenuItem();
        popm_moves5 = new javax.swing.JMenuItem();
        popm_moves6 = new javax.swing.JMenuItem();
        popm_movevc1 = new javax.swing.JMenuItem();
        popm_movevc3 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        popm_k1 = new javax.swing.JMenuItem();
        popm_kf1 = new javax.swing.JMenuItem();
        popm_ko1 = new javax.swing.JMenuItem();
        popm_t1 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        popm_reset1 = new javax.swing.JMenuItem();
        popmOverSep8 = new javax.swing.JPopupMenu.Separator();
        popmDrawing1 = new javax.swing.JMenuItem();
        Ok_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfOverrides = new javax.swing.JTextField();
        tfInnerOverrides = new javax.swing.JTextField();
        tfLastOverrides = new javax.swing.JTextField();
        tfBefore = new javax.swing.JTextField();
        tfAfter = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnAddLayer = new javax.swing.JButton();
        btnEditLayer = new javax.swing.JButton();
        btnDeleteLayer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        layersTable = new javax.swing.JTable();
        btnChangeLayer = new javax.swing.JButton();
        btnAfterSyl = new javax.swing.JButton();
        btnOverrides = new javax.swing.JButton();
        btnInner = new javax.swing.JButton();
        btnLastOver = new javax.swing.JButton();
        btnBeforeSyl = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        spVariables = new javax.swing.JScrollPane();
        epVariables = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblColor1 = new javax.swing.JLabel();
        lblColorKaraoke = new javax.swing.JLabel();
        lblColor2 = new javax.swing.JLabel();
        lblColorBorder = new javax.swing.JLabel();
        lblColor3 = new javax.swing.JLabel();
        lblColorShadow = new javax.swing.JLabel();
        lblColor4 = new javax.swing.JLabel();
        lblColorText4 = new javax.swing.JLabel();
        spiShadow = new javax.swing.JSpinner();
        spiBorder = new javax.swing.JSpinner();
        spiBorderS = new javax.swing.JSpinner();
        spiText = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        spiKaraoke = new javax.swing.JSpinner();
        spiMarginVT = new javax.swing.JSpinner();
        spiMarginR = new javax.swing.JSpinner();
        spiMarginL = new javax.swing.JSpinner();
        rb9 = new javax.swing.JRadioButton();
        rb6 = new javax.swing.JRadioButton();
        rb5 = new javax.swing.JRadioButton();
        rb4 = new javax.swing.JRadioButton();
        rb2 = new javax.swing.JRadioButton();
        rb7 = new javax.swing.JRadioButton();
        rb3 = new javax.swing.JRadioButton();
        rb8 = new javax.swing.JRadioButton();
        rb1 = new javax.swing.JRadioButton();
        jLabel23 = new javax.swing.JLabel();
        cboOpaqueBox = new javax.swing.JCheckBox();
        cboBold = new javax.swing.JCheckBox();
        cboItalic = new javax.swing.JCheckBox();
        cboUnderline = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        spiMarginB = new javax.swing.JSpinner();
        jLabel25 = new javax.swing.JLabel();
        spiShadowS = new javax.swing.JSpinner();
        cboStrikeOut = new javax.swing.JCheckBox();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        cbEncoding = new javax.swing.JComboBox();
        cbFontname = new javax.swing.JComboBox();
        tfStyleName = new javax.swing.JTextField();
        spiFontsize = new javax.swing.JSpinner();
        tfSpacing = new javax.swing.JTextField();
        tfScaleX = new javax.swing.JTextField();
        tfScaleY = new javax.swing.JTextField();
        tfRotation = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstStyles = new javax.swing.JList();
        btnAdd = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        tfAuthor = new javax.swing.JTextField();
        tfComments = new javax.swing.JTextField();
        tfPreview = new javax.swing.JTextField();
        tfCollection = new javax.swing.JTextField();
        btnPreview = new javax.swing.JButton();
        panPreview = new javax.swing.JPanel();
        lblPreview = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taHelpPlease = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        tfFirstLayer = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        rbMomentBefore = new javax.swing.JRadioButton();
        rbMomentMeantime = new javax.swing.JRadioButton();
        rbMomentAfter = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        tfMomentTime = new javax.swing.JTextField();
        cbSaveEffect = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        cbFunctions = new javax.swing.JComboBox();

        popmCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editcut.png"))); // NOI18N
        popmCut.setText("Cut");
        popmCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCutActionPerformed(evt);
            }
        });
        popOverrides.add(popmCut);

        popmCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        popmCopy.setText("Copy");
        popmCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCopyActionPerformed(evt);
            }
        });
        popOverrides.add(popmCopy);

        popmPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editpaste.png"))); // NOI18N
        popmPaste.setText("Paste");
        popmPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPasteActionPerformed(evt);
            }
        });
        popOverrides.add(popmPaste);

        popmDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_button_cancel.png"))); // NOI18N
        popmDelete.setText("Delete");
        popmDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDeleteActionPerformed(evt);
            }
        });
        popOverrides.add(popmDelete);
        popOverrides.add(popmOverSep1);

        popmSelAll.setText("Select all");
        popmSelAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelAllActionPerformed(evt);
            }
        });
        popOverrides.add(popmSelAll);

        popmClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_filesystem_trashcan_empty.png"))); // NOI18N
        popmClear.setText("Clear");
        popmClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmClearActionPerformed(evt);
            }
        });
        popOverrides.add(popmClear);
        popOverrides.add(popmOverSep2);

        popmColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_device_blockdevice.png"))); // NOI18N
        popmColor.setText("Choose a color...");
        popmColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmColorActionPerformed(evt);
            }
        });
        popOverrides.add(popmColor);

        popmAlpha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_miscellaneous.png"))); // NOI18N
        popmAlpha.setText("Choose an alpha...");
        popmAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmAlphaActionPerformed(evt);
            }
        });
        popOverrides.add(popmAlpha);

        popmPNG.setText("Choose a PNG image...");
        popmPNG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPNGActionPerformed(evt);
            }
        });
        popOverrides.add(popmPNG);
        popOverrides.add(popmOverSep3);

        popmOverrides.setText("Insert overrides...");

        popmKaraOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame5.png"))); // NOI18N
        popmKaraOK.setText("For animation...");

        popm_b.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_b.setText("\\b - Bold");
        popm_b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_bActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_b);

        popm_i.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_i.setText("\\i - Italic");
        popm_i.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_iActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_i);

        popm_u.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_u.setText("\\u - Underline");
        popm_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_uActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_u);

        popm_s.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_s.setText("\\s - Strike out");
        popm_s.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_sActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_s);

        popm_bord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_bord.setText("\\bord - Thickness of border");
        popm_bord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_bordActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_bord);

        popm_shad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_shad.setText("\\shad - Depth of shader");
        popm_shad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_shadActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_shad);

        popm_be.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_be.setText("\\be - Blur edge");
        popm_be.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_beActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_be);

        popm_fs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_fs.setText("\\fs - Font size");
        popm_fs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fsActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_fs);

        popm_fscx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fscx.setText("\\fscx - Font scale of X");
        popm_fscx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fscxActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_fscx);

        popm_fscy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fscy.setText("\\fscy - Font scale of Y");
        popm_fscy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fscyActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_fscy);

        popm_fsp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fsp.setText("\\fsp - Font spacing");
        popm_fsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fspActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_fsp);

        popm_frx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_frx.setText("\\frx - Font rotation on X");
        popm_frx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_frxActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_frx);

        popm_fry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fry.setText("\\fry - Font rotation on Y");
        popm_fry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fryActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_fry);

        popm_frz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_frz.setText("\\frz - Font rotation on Z");
        popm_frz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_frzActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_frz);

        popm_1c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_1c.setText("\\1c&H<hexa>& - Color of text");
        popm_1c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1cActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_1c);

        popm_2c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_2c.setText("\\2c&H<hexa>& - Color of karaoke");
        popm_2c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2cActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_2c);

        popm_3c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_3c.setText("\\3c&H<hexa>& - Color of border");
        popm_3c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3cActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_3c);

        popm_4c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_4c.setText("\\4c&H<hexa>& - Color of shader");
        popm_4c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4cActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_4c);

        popm_alpha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_alpha.setText("\\alpha&H<hexa>& - Tranparency");
        popm_alpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_alphaActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_alpha);

        popm_1a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_1a.setText("\\1a&H<hexa>& - Tranparency of text");
        popm_1a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1aActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_1a);

        popm_2a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_2a.setText("\\2a&H<hexa>& - Tranparency of karaoke");
        popm_2a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2aActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_2a);

        popm_3a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_3a.setText("\\3a&H<hexa>& - Tranparency of border");
        popm_3a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3aActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_3a);

        popm_4a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_4a.setText("\\4a&H<hexa>& - Tranparency of shader");
        popm_4a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4aActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_4a);

        popm_clip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_clip.setText("\\clip - Region of visibility");
        popm_clip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_clipActionPerformed(evt);
            }
        });
        popmKaraOK.add(popm_clip);

        popmOverrides.add(popmKaraOK);

        popmKaraOK2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popmKaraOK2.setText("For animation...");

        popm_xbord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_xbord.setText("\\xbord - Thickness of border on X");
        popm_xbord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_xbordActionPerformed(evt);
            }
        });
        popmKaraOK2.add(popm_xbord);

        popm_ybord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_ybord.setText("\\ybord - Thickness of border on Y");
        popm_ybord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_ybordActionPerformed(evt);
            }
        });
        popmKaraOK2.add(popm_ybord);

        popm_xshad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_xshad.setText("\\xshad - Depth of shader on X");
        popm_xshad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_xshadActionPerformed(evt);
            }
        });
        popmKaraOK2.add(popm_xshad);

        popm_yshad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_yshad.setText("\\yshad - Depth of shader on Y");
        popm_yshad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_yshadActionPerformed(evt);
            }
        });
        popmKaraOK2.add(popm_yshad);

        popm_blur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_blur.setText("\\blur - Blur");
        popm_blur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_blurActionPerformed(evt);
            }
        });
        popmKaraOK2.add(popm_blur);

        popm_fax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_fax.setText("\\fax - Text shearing on X");
        popm_fax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_faxActionPerformed(evt);
            }
        });
        popmKaraOK2.add(popm_fax);

        popm_fay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_fay.setText("\\fay - Text shearing on Y");
        popm_fay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fayActionPerformed(evt);
            }
        });
        popmKaraOK2.add(popm_fay);

        popm_iclip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_iclip.setText("\\iclip - Region of  invisibility");
        popm_iclip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_iclipActionPerformed(evt);
            }
        });
        popmKaraOK2.add(popm_iclip);

        popmOverrides.add(popmKaraOK2);

        popmKaraOK3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popmKaraOK3.setText("For animation...");

        popm_fsc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_fsc.setText("\\fsc - Font scale");
        popm_fsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fscActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_fsc);

        popm_fsvp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_fsvp.setText("\\fsvp - Leading");
        popm_fsvp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fsvpActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_fsvp);

        popm_frs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_frs.setText("\\frs - Baseline obliquity");
        popm_frs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_frsActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_frs);

        popm_z.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_z.setText("\\z - Z coordinate");
        popm_z.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_zActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_z);

        popm_distort.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_distort.setText("\\distort - Distortion");
        popm_distort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_distortActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_distort);

        popm_md.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_md.setText("\\md - Boundaries deforming");
        popm_md.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_mdActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_md);

        popm_mdx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_mdx.setText("\\mdx - Boundaries deforming on X");
        popm_mdx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_mdxActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_mdx);

        popm_mdy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_mdy.setText("\\mdy - Boundaries deforming on Y");
        popm_mdy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_mdyActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_mdy);

        popm_mdz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_mdz.setText("\\mdz - Boundaries deforming on Z");
        popm_mdz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_mdzActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_mdz);

        popm_1vc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_1vc.setText("\\1vc - Gradients on text (color)");
        popm_1vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1vcActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_1vc);

        popm_2vc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_2vc.setText("\\2vc - Gradients on karaoke (color)");
        popm_2vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2vcActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_2vc);

        popm_3vc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_3vc.setText("\\3vc - Gradients on border (color)");
        popm_3vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3vcActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_3vc);

        popm_4vc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_4vc.setText("\\4vc - Gradients on shader (color)");
        popm_4vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4vcActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_4vc);

        popm_1va.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_1va.setText("\\1va - Gradients on text (transparency)");
        popm_1va.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1vaActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_1va);

        popm_2va.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_2va.setText("\\2va - Gradients on karaoke (transparency)");
        popm_2va.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2vaActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_2va);

        popm_3va.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_3va.setText("\\3va - Gradients on border (transparency)");
        popm_3va.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3vaActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_3va);

        popm_4va.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_4va.setText("\\4va - Gradients on shader (transparency)");
        popm_4va.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4vaActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_4va);

        popm_1img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_1img.setText("\\1img - Image fill on text");
        popm_1img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1imgActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_1img);

        popm_2img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_2img.setText("\\2img - Image fill on karaoke");
        popm_2img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2imgActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_2img);

        popm_3img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_3img.setText("\\3img - Image fill on border");
        popm_3img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3imgActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_3img);

        popm_4img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_4img.setText("\\4img - Image fill on shader");
        popm_4img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4imgActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_4img);

        popm_jitter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_jitter.setText("\\jitter - Shaking");
        popm_jitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_jitterActionPerformed(evt);
            }
        });
        popmKaraOK3.add(popm_jitter);

        popmOverrides.add(popmKaraOK3);

        popmKaraNOK.setText("For configuration...");

        popm_fn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_fn.setText("\\fn - Font name");
        popm_fn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fnActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_fn);

        popm_fe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_fe.setText("\\fe - Font encoding");
        popm_fe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_feActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_fe);

        popm_q.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_q.setText("\\q - Wrapping style");
        popm_q.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_qActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_q);

        popm_a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_a.setText("\\a -Alignment (old)");
        popm_a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_aActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_a);

        popm_an.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_an.setText("\\an - Alignment");
        popm_an.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_anActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_an);

        popm_pos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_pos.setText("\\pos - Position");
        popm_pos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_posActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_pos);

        popm_move.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_move.setText("\\move - Position in real time");
        popm_move.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_moveActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_move);

        popm_org.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_org.setText("\\org - Origin");
        popm_org.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_orgActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_org);

        popm_fad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fad.setText("\\fad - Fading");
        popm_fad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fadActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_fad);

        popm_fade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fade.setText("\\fade - Fading");
        popm_fade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fadeActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_fade);

        popm_clip2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_clip2.setText("\\clip - Region of visibility");
        popm_clip2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_clip2ActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_clip2);

        popm_iclip2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_iclip2.setText("\\iclip - Region of invisibility");
        popm_iclip2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_iclip2ActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_iclip2);

        popm_mover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_mover.setText("\\mover - Polar move");
        popm_mover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_moverActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_mover);

        popm_moves3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_moves3.setText("\\moves3 - Spline move");
        popm_moves3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_moves3ActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_moves3);

        popm_moves4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_moves4.setText("\\moves4 - Spline move");
        popm_moves4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_moves4ActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_moves4);

        popm_movevc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_movevc.setText("\\movevc - Moveable vector clip");
        popm_movevc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_movevcActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_movevc);

        popm_movevc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_movevc2.setText("\\movevc - Moveable vector clip");
        popm_movevc2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_movevc2ActionPerformed(evt);
            }
        });
        popmKaraNOK.add(popm_movevc2);

        popmOverrides.add(popmKaraNOK);
        popmOverrides.add(jSeparator1);

        popmSurround.setText("Surround by braces");
        popmSurround.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSurroundActionPerformed(evt);
            }
        });
        popmOverrides.add(popmSurround);

        popmDelSurround.setText("Delete all braces");
        popmDelSurround.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDelSurroundActionPerformed(evt);
            }
        });
        popmOverrides.add(popmDelSurround);
        popmOverrides.add(jSeparator2);

        popm_k.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_k.setText("\\k - Simple karaoke");
        popm_k.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_kActionPerformed(evt);
            }
        });
        popmOverrides.add(popm_k);

        popm_kf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_kf.setText("\\kf - Karaoke with fill");
        popm_kf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_kfActionPerformed(evt);
            }
        });
        popmOverrides.add(popm_kf);

        popm_ko.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_ko.setText("\\ko - Karaoke with outline");
        popm_ko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_koActionPerformed(evt);
            }
        });
        popmOverrides.add(popm_ko);

        popm_t.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_t.setText("\\t - Animation");
        popm_t.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_tActionPerformed(evt);
            }
        });
        popmOverrides.add(popm_t);
        popmOverrides.add(jSeparator3);

        popm_reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_reset.setText("\\r - Reset");
        popm_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_resetActionPerformed(evt);
            }
        });
        popmOverrides.add(popm_reset);

        popOverrides.add(popmOverrides);
        popOverrides.add(popmOverSep4);

        popmIntCalc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_calc.png"))); // NOI18N
        popmIntCalc.setText("Insert a calc...");
        popmIntCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmIntCalcActionPerformed(evt);
            }
        });
        popOverrides.add(popmIntCalc);

        popmFloCalc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_calc.png"))); // NOI18N
        popmFloCalc.setText("Insert a float calc...");
        popmFloCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFloCalcActionPerformed(evt);
            }
        });
        popOverrides.add(popmFloCalc);

        popmDrawing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_kcoloredit.png"))); // NOI18N
        popmDrawing.setText("Insert a drawing...");
        popmDrawing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawingActionPerformed(evt);
            }
        });
        popOverrides.add(popmDrawing);

        popmImport.setText("Import from clipboard");
        popmImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmImportActionPerformed(evt);
            }
        });
        popStyleList.add(popmImport);

        popmExport.setText("Export to clipboard");
        popmExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmExportActionPerformed(evt);
            }
        });
        popStyleList.add(popmExport);
        popStyleList.add(jSeparator4);

        popmImportFrom.setText("Import from a script...");
        popmImportFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmImportFromActionPerformed(evt);
            }
        });
        popStyleList.add(popmImportFrom);

        popmCut2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editcut.png"))); // NOI18N
        popmCut2.setText("Cut");
        popmCut2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCut2ActionPerformed(evt);
            }
        });
        popAbout.add(popmCut2);

        popmCopy2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        popmCopy2.setText("Copy");
        popmCopy2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCopy2ActionPerformed(evt);
            }
        });
        popAbout.add(popmCopy2);

        popmPaste2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editpaste.png"))); // NOI18N
        popmPaste2.setText("Paste");
        popmPaste2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPaste2ActionPerformed(evt);
            }
        });
        popAbout.add(popmPaste2);

        popmDelete2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_button_cancel.png"))); // NOI18N
        popmDelete2.setText("Delete");
        popmDelete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDelete2ActionPerformed(evt);
            }
        });
        popAbout.add(popmDelete2);
        popAbout.add(popmAboutSep1);

        popmSelAll2.setText("Select all");
        popmSelAll2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelAll2ActionPerformed(evt);
            }
        });
        popAbout.add(popmSelAll2);

        popmClearAll2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_filesystem_trashcan_empty.png"))); // NOI18N
        popmClearAll2.setText("Clear all");
        popmClearAll2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmClearAll2ActionPerformed(evt);
            }
        });
        popAbout.add(popmClearAll2);

        popmInsScript.setText("Script");

        popmCodeInit.setText("Initialize the script");
        popmCodeInit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeInitActionPerformed(evt);
            }
        });
        popmInsScript.add(popmCodeInit);

        popmCodeDef.setText("Insert def");
        popmCodeDef.setToolTipText("Insert a function for a variable.");
        popmCodeDef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeDefActionPerformed(evt);
            }
        });
        popmInsScript.add(popmCodeDef);

        popOverrides1.add(popmInsScript);
        popOverrides1.add(jSeparator5);

        popmCut1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editcut.png"))); // NOI18N
        popmCut1.setText("Cut");
        popmCut1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCut1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmCut1);

        popmCopy1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        popmCopy1.setText("Copy");
        popmCopy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCopy1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmCopy1);

        popmPaste1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_editpaste.png"))); // NOI18N
        popmPaste1.setText("Paste");
        popmPaste1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPaste1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmPaste1);

        popmDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_action_button_cancel.png"))); // NOI18N
        popmDelete1.setText("Delete");
        popmDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDelete1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmDelete1);
        popOverrides1.add(popmOverSep5);

        popmSelAll1.setText("Select all");
        popmSelAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelAll1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmSelAll1);

        popmClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_filesystem_trashcan_empty.png"))); // NOI18N
        popmClear1.setText("Clear");
        popmClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmClear1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmClear1);
        popOverrides1.add(popmOverSep6);

        popmColor1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_device_blockdevice.png"))); // NOI18N
        popmColor1.setText("Choose a color...");
        popmColor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmColor1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmColor1);

        popmAlpha1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_miscellaneous.png"))); // NOI18N
        popmAlpha1.setText("Choose an alpha...");
        popmAlpha1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmAlpha1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmAlpha1);

        popmPNG1.setText("Choose a PNG image...");
        popmPNG1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPNG1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmPNG1);
        popOverrides1.add(popmOverSep7);

        popmOverrides1.setText("Insert overrides...");

        popmKaraOK1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame5.png"))); // NOI18N
        popmKaraOK1.setText("For animation...");

        popm_b1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_b1.setText("\\b - Bold");
        popm_b1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_b1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_b1);

        popm_i1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_i1.setText("\\i - Italic");
        popm_i1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_i1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_i1);

        popm_u1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_u1.setText("\\u - Underline");
        popm_u1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_u1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_u1);

        popm_s1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_s1.setText("\\s - Strike out");
        popm_s1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_s1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_s1);

        popm_bord1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_bord1.setText("\\bord - Thickness of border");
        popm_bord1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_bord1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_bord1);

        popm_shad1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_shad1.setText("\\shad - Depth of shader");
        popm_shad1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_shad1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_shad1);

        popm_be1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_be1.setText("\\be - Blur edge");
        popm_be1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_be1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_be1);

        popm_fs1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_fs1.setText("\\fs - Font size");
        popm_fs1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fs1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_fs1);

        popm_fscx1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fscx1.setText("\\fscx - Font scale of X");
        popm_fscx1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fscx1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_fscx1);

        popm_fscy1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fscy1.setText("\\fscy - Font scale of Y");
        popm_fscy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fscy1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_fscy1);

        popm_fsp1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fsp1.setText("\\fsp - Font spacing");
        popm_fsp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fsp1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_fsp1);

        popm_frx1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_frx1.setText("\\frx - Font rotation on X");
        popm_frx1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_frx1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_frx1);

        popm_fry1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fry1.setText("\\fry - Font rotation on Y");
        popm_fry1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fry1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_fry1);

        popm_frz1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_frz1.setText("\\frz - Font rotation on Z");
        popm_frz1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_frz1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_frz1);

        popm_1c1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_1c1.setText("\\1c&H<hexa>& - Color of text");
        popm_1c1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1c1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_1c1);

        popm_2c1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_2c1.setText("\\2c&H<hexa>& - Color of karaoke");
        popm_2c1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2c1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_2c1);

        popm_3c1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_3c1.setText("\\3c&H<hexa>& - Color of border");
        popm_3c1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3c1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_3c1);

        popm_4c1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_4c1.setText("\\4c&H<hexa>& - Color of shader");
        popm_4c1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4c1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_4c1);

        popm_alpha1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_alpha1.setText("\\alpha&H<hexa>& - Tranparency");
        popm_alpha1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_alpha1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_alpha1);

        popm_1a1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_1a1.setText("\\1a&H<hexa>& - Tranparency of text");
        popm_1a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1a1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_1a1);

        popm_2a1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_2a1.setText("\\2a&H<hexa>& - Tranparency of karaoke");
        popm_2a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2a1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_2a1);

        popm_3a1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_3a1.setText("\\3a&H<hexa>& - Tranparency of border");
        popm_3a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3a1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_3a1);

        popm_4a1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_4a1.setText("\\4a&H<hexa>& - Tranparency of shader");
        popm_4a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4a1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_4a1);

        popm_clip1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_clip1.setText("\\clip - Region of visibility");
        popm_clip1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_clip1ActionPerformed(evt);
            }
        });
        popmKaraOK1.add(popm_clip1);

        popmOverrides1.add(popmKaraOK1);

        popmKaraOK4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popmKaraOK4.setText("For animation...");

        popm_xbord1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_xbord1.setText("\\xbord - Thickness of border on X");
        popm_xbord1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_xbord1ActionPerformed(evt);
            }
        });
        popmKaraOK4.add(popm_xbord1);

        popm_ybord1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_ybord1.setText("\\ybord - Thickness of border on Y");
        popm_ybord1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_ybord1ActionPerformed(evt);
            }
        });
        popmKaraOK4.add(popm_ybord1);

        popm_xshad1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_xshad1.setText("\\xshad - Depth of shader on X");
        popm_xshad1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_xshad1ActionPerformed(evt);
            }
        });
        popmKaraOK4.add(popm_xshad1);

        popm_yshad1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_yshad1.setText("\\yshad - Depth of shader on Y");
        popm_yshad1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_yshad1ActionPerformed(evt);
            }
        });
        popmKaraOK4.add(popm_yshad1);

        popm_blur1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_blur1.setText("\\blur - Blur");
        popm_blur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_blur1ActionPerformed(evt);
            }
        });
        popmKaraOK4.add(popm_blur1);

        popm_fax1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_fax1.setText("\\fax - Text shearing on X");
        popm_fax1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fax1ActionPerformed(evt);
            }
        });
        popmKaraOK4.add(popm_fax1);

        popm_fay1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_fay1.setText("\\fay - Text shearing on Y");
        popm_fay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fay1ActionPerformed(evt);
            }
        });
        popmKaraOK4.add(popm_fay1);

        popm_iclip1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_iclip1.setText("\\iclip - Region of  invisibility");
        popm_iclip1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_iclip1ActionPerformed(evt);
            }
        });
        popmKaraOK4.add(popm_iclip1);

        popmOverrides1.add(popmKaraOK4);

        popmKaraOK5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popmKaraOK5.setText("For animation...");

        popm_fsc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_fsc1.setText("\\fsc - Font scale");
        popm_fsc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fsc1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_fsc1);

        popm_fsvp1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_fsvp1.setText("\\fsvp - Leading");
        popm_fsvp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fsvp1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_fsvp1);

        popm_frs1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_frs1.setText("\\frs - Baseline obliquity");
        popm_frs1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_frs1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_frs1);

        popm_z1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_z1.setText("\\z - Z coordinate");
        popm_z1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_z1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_z1);

        popm_distort1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_distort1.setText("\\distort - Distortion");
        popm_distort1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_distort1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_distort1);

        popm_md1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_md1.setText("\\md - Boundaries deforming");
        popm_md1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_md1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_md1);

        popm_mdx1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_mdx1.setText("\\mdx - Boundaries deforming on X");
        popm_mdx1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_mdx1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_mdx1);

        popm_mdy1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_mdy1.setText("\\mdy - Boundaries deforming on Y");
        popm_mdy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_mdy1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_mdy1);

        popm_mdz1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_mdz1.setText("\\mdz - Boundaries deforming on Z");
        popm_mdz1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_mdz1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_mdz1);

        popm_1vc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_1vc1.setText("\\1vc - Gradients on text (color)");
        popm_1vc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1vc1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_1vc1);

        popm_2vc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_2vc1.setText("\\2vc - Gradients on karaoke (color)");
        popm_2vc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2vc1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_2vc1);

        popm_3vc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_3vc1.setText("\\3vc - Gradients on border (color)");
        popm_3vc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3vc1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_3vc1);

        popm_4vc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_4vc1.setText("\\4vc - Gradients on shader (color)");
        popm_4vc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4vc1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_4vc1);

        popm_1va1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_1va1.setText("\\1va - Gradients on text (transparency)");
        popm_1va1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1va1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_1va1);

        popm_2va1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_2va1.setText("\\2va - Gradients on karaoke (transparency)");
        popm_2va1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2va1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_2va1);

        popm_3va1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_3va1.setText("\\3va - Gradients on border (transparency)");
        popm_3va1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3va1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_3va1);

        popm_4va1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_4va1.setText("\\4va - Gradients on shader (transparency)");
        popm_4va1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4va1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_4va1);

        popm_1img1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_1img1.setText("\\1img - Image fill on text");
        popm_1img1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_1img1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_1img1);

        popm_2img1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_2img1.setText("\\2img - Image fill on karaoke");
        popm_2img1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_2img1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_2img1);

        popm_3img1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_3img1.setText("\\3img - Image fill on border");
        popm_3img1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_3img1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_3img1);

        popm_4img1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_4img1.setText("\\4img - Image fill on shader");
        popm_4img1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_4img1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_4img1);

        popm_jitter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_jitter1.setText("\\jitter - Shaking");
        popm_jitter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_jitter1ActionPerformed(evt);
            }
        });
        popmKaraOK5.add(popm_jitter1);

        popmOverrides1.add(popmKaraOK5);

        popmKaraNOK1.setText("For configuration...");

        popm_fn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_fn1.setText("\\fn - Font name");
        popm_fn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fn1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_fn1);

        popm_fe1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_fe1.setText("\\fe - Font encoding");
        popm_fe1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fe1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_fe1);

        popm_q1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_q1.setText("\\q - Wrapping style");
        popm_q1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_q1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_q1);

        popm_a1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_a1.setText("\\a -Alignment (old)");
        popm_a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_a1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_a1);

        popm_an1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_an1.setText("\\an - Alignment");
        popm_an1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_an1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_an1);

        popm_pos1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_pos1.setText("\\pos - Position");
        popm_pos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_pos1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_pos1);

        popm_move1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_move1.setText("\\move - Position in real time");
        popm_move1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_move1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_move1);

        popm_org1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_org1.setText("\\org - Origin");
        popm_org1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_org1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_org1);

        popm_fad1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fad1.setText("\\fad - Fading");
        popm_fad1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fad1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_fad1);

        popm_fade1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_fade1.setText("\\fade - Fading");
        popm_fade1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_fade1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_fade1);

        popm_clip3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_clip3.setText("\\clip - Region of visibility");
        popm_clip3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_clip3ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_clip3);

        popm_iclip3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popm_iclip3.setText("\\iclip - Region of invisibility");
        popm_iclip3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_iclip3ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_iclip3);

        popm_mover1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_mover1.setText("\\mover - Polar move");
        popm_mover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_mover1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_mover1);

        popm_moves5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_moves5.setText("\\moves3 - Spline move");
        popm_moves5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_moves5ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_moves5);

        popm_moves6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_moves6.setText("\\moves4 - Spline move");
        popm_moves6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_moves6ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_moves6);

        popm_movevc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_movevc1.setText("\\movevc - Moveable vector clip");
        popm_movevc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_movevc1ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_movevc1);

        popm_movevc3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popm_movevc3.setText("\\movevc - Moveable vector clip");
        popm_movevc3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_movevc3ActionPerformed(evt);
            }
        });
        popmKaraNOK1.add(popm_movevc3);

        popmOverrides1.add(popmKaraNOK1);
        popmOverrides1.add(jSeparator6);

        popm_k1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_k1.setText("\\k - Simple karaoke");
        popm_k1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_k1ActionPerformed(evt);
            }
        });
        popmOverrides1.add(popm_k1);

        popm_kf1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_kf1.setText("\\kf - Karaoke with fill");
        popm_kf1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_kf1ActionPerformed(evt);
            }
        });
        popmOverrides1.add(popm_kf1);

        popm_ko1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_ko1.setText("\\ko - Karaoke with outline");
        popm_ko1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_ko1ActionPerformed(evt);
            }
        });
        popmOverrides1.add(popm_ko1);

        popm_t1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popm_t1.setText("\\t - Animation");
        popm_t1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_t1ActionPerformed(evt);
            }
        });
        popmOverrides1.add(popm_t1);
        popmOverrides1.add(jSeparator7);

        popm_reset1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popm_reset1.setText("\\r - Reset");
        popm_reset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popm_reset1ActionPerformed(evt);
            }
        });
        popmOverrides1.add(popm_reset1);

        popOverrides1.add(popmOverrides1);
        popOverrides1.add(popmOverSep8);

        popmDrawing1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/20px-Crystal_Clear_app_kcoloredit.png"))); // NOI18N
        popmDrawing1.setText("Insert a drawing...");
        popmDrawing1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawing1ActionPerformed(evt);
            }
        });
        popOverrides1.add(popmDrawing1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Create or edit an effect...");

        Ok_Button.setText("OK");
        Ok_Button.setPreferredSize(new java.awt.Dimension(80, 23));
        Ok_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ok_ButtonActionPerformed(evt);
            }
        });

        Cancel_Button.setText("Cancel");
        Cancel_Button.setPreferredSize(new java.awt.Dimension(80, 23));
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Name :");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 20));

        tfName.setText("DefaultEffect");
        tfName.setComponentPopupMenu(popAbout);
        tfName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfNameFocusGained(evt);
            }
        });
        tfName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfNameKeyReleased(evt);
            }
        });

        jPanel1.setLayout(null);

        jLabel2.setText("Overrides :");
        jLabel2.setToolTipText("First syllable only");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 160, 120, 30);

        jLabel3.setText("Inner overrides :");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 190, 120, 30);

        jLabel4.setText("Last overrides :");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 220, 120, 30);

        jLabel5.setText("Before syllable :");
        jLabel5.setToolTipText("All syllables but the first");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 250, 120, 30);

        jLabel6.setText("After syllable :");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(10, 280, 120, 30);

        tfOverrides.setText("<example = {\\k~%dK/10~} >");
        tfOverrides.setComponentPopupMenu(popOverrides);
        tfOverrides.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfOverridesFocusGained(evt);
            }
        });
        jPanel1.add(tfOverrides);
        tfOverrides.setBounds(140, 160, 330, 30);

        tfInnerOverrides.setText("<example = [none] >");
        tfInnerOverrides.setComponentPopupMenu(popOverrides);
        tfInnerOverrides.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfInnerOverridesFocusGained(evt);
            }
        });
        jPanel1.add(tfInnerOverrides);
        tfInnerOverrides.setBounds(140, 190, 330, 30);

        tfLastOverrides.setText("<example = [none] >");
        tfLastOverrides.setComponentPopupMenu(popOverrides);
        tfLastOverrides.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfLastOverridesFocusGained(evt);
            }
        });
        jPanel1.add(tfLastOverrides);
        tfLastOverrides.setBounds(140, 220, 330, 30);

        tfBefore.setText("<example = {\\k~%dK/10~} >");
        tfBefore.setComponentPopupMenu(popOverrides);
        tfBefore.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfBeforeFocusGained(evt);
            }
        });
        jPanel1.add(tfBefore);
        tfBefore.setBounds(140, 250, 330, 30);

        tfAfter.setText("<example = [none] >");
        tfAfter.setComponentPopupMenu(popOverrides);
        tfAfter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfAfterFocusGained(evt);
            }
        });
        jPanel1.add(tfAfter);
        tfAfter.setBounds(140, 280, 330, 30);

        jLabel7.setText("Layers details :");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(10, 10, 120, 20);

        btnAddLayer.setText("Add");
        btnAddLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLayerActionPerformed(evt);
            }
        });
        jPanel1.add(btnAddLayer);
        btnAddLayer.setBounds(140, 10, 80, 23);

        btnEditLayer.setText("Get");
        btnEditLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditLayerActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditLayer);
        btnEditLayer.setBounds(230, 10, 80, 23);

        btnDeleteLayer.setText("Delete");
        btnDeleteLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteLayerActionPerformed(evt);
            }
        });
        jPanel1.add(btnDeleteLayer);
        btnDeleteLayer.setBounds(410, 10, 80, 23);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        layersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Layer", "Effects"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        layersTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jScrollPane1.setViewportView(layersTable);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 40, 480, 110);

        btnChangeLayer.setText("Change");
        btnChangeLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeLayerActionPerformed(evt);
            }
        });
        jPanel1.add(btnChangeLayer);
        btnChangeLayer.setBounds(320, 10, 80, 23);

        btnAfterSyl.setBackground(new java.awt.Color(51, 0, 204));
        btnAfterSyl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAfterSylActionPerformed(evt);
            }
        });
        jPanel1.add(btnAfterSyl);
        btnAfterSyl.setBounds(470, 280, 20, 30);

        btnOverrides.setBackground(new java.awt.Color(0, 204, 51));
        btnOverrides.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverridesActionPerformed(evt);
            }
        });
        jPanel1.add(btnOverrides);
        btnOverrides.setBounds(470, 160, 20, 30);

        btnInner.setBackground(new java.awt.Color(51, 0, 204));
        btnInner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInnerActionPerformed(evt);
            }
        });
        jPanel1.add(btnInner);
        btnInner.setBounds(470, 190, 20, 30);

        btnLastOver.setBackground(new java.awt.Color(51, 0, 204));
        btnLastOver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastOverActionPerformed(evt);
            }
        });
        jPanel1.add(btnLastOver);
        btnLastOver.setBounds(470, 220, 20, 30);

        btnBeforeSyl.setBackground(new java.awt.Color(0, 204, 51));
        btnBeforeSyl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeforeSylActionPerformed(evt);
            }
        });
        jPanel1.add(btnBeforeSyl);
        btnBeforeSyl.setBounds(470, 250, 20, 30);

        jTabbedPane1.addTab("Effects", jPanel1);

        spVariables.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        spVariables.setViewportView(epVariables);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spVariables)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spVariables, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Variables", jPanel4);

        jPanel2.setLayout(null);

        jLabel16.setText("Font size :");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(250, 150, 60, 30);

        jLabel17.setText("Name :");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(10, 0, 60, 30);

        jLabel18.setText("Font name :");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(10, 150, 80, 30);

        lblColor1.setBackground(new java.awt.Color(255, 255, 255));
        lblColor1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblColor1.setOpaque(true);
        lblColor1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColor1MouseClicked(evt);
            }
        });
        jPanel2.add(lblColor1);
        lblColor1.setBounds(250, 30, 60, 20);

        lblColorKaraoke.setText("Karaoke :");
        jPanel2.add(lblColorKaraoke);
        lblColorKaraoke.setBounds(320, 10, 60, 20);

        lblColor2.setBackground(new java.awt.Color(255, 0, 0));
        lblColor2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblColor2.setOpaque(true);
        lblColor2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColor2MouseClicked(evt);
            }
        });
        jPanel2.add(lblColor2);
        lblColor2.setBounds(320, 30, 60, 20);

        lblColorBorder.setText("Border :");
        jPanel2.add(lblColorBorder);
        lblColorBorder.setBounds(250, 80, 60, 20);

        lblColor3.setBackground(new java.awt.Color(0, 0, 102));
        lblColor3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblColor3.setOpaque(true);
        lblColor3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColor3MouseClicked(evt);
            }
        });
        jPanel2.add(lblColor3);
        lblColor3.setBounds(250, 100, 60, 20);

        lblColorShadow.setText("Shadow :");
        jPanel2.add(lblColorShadow);
        lblColorShadow.setBounds(320, 80, 60, 20);

        lblColor4.setBackground(new java.awt.Color(0, 0, 0));
        lblColor4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblColor4.setOpaque(true);
        lblColor4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblColor4MouseClicked(evt);
            }
        });
        jPanel2.add(lblColor4);
        lblColor4.setBounds(320, 100, 60, 20);

        lblColorText4.setText("Text :");
        jPanel2.add(lblColorText4);
        lblColorText4.setBounds(250, 10, 60, 20);
        jPanel2.add(spiShadow);
        spiShadow.setBounds(320, 120, 60, 30);
        jPanel2.add(spiBorder);
        spiBorder.setBounds(250, 120, 60, 30);
        jPanel2.add(spiBorderS);
        spiBorderS.setBounds(430, 200, 60, 30);
        jPanel2.add(spiText);
        spiText.setBounds(250, 50, 60, 30);

        jLabel19.setText("V/T :");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(390, 110, 40, 30);

        jLabel20.setText("L :");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(390, 30, 40, 30);

        jLabel21.setText("R :");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(390, 70, 40, 30);

        jLabel22.setText("B :");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(390, 150, 40, 30);
        jPanel2.add(spiKaraoke);
        spiKaraoke.setBounds(320, 50, 60, 30);
        jPanel2.add(spiMarginVT);
        spiMarginVT.setBounds(430, 110, 60, 30);
        jPanel2.add(spiMarginR);
        spiMarginR.setBounds(430, 70, 60, 30);
        jPanel2.add(spiMarginL);
        spiMarginL.setBounds(430, 30, 60, 30);

        bgAlignment.add(rb9);
        rb9.setText("9");
        jPanel2.add(rb9);
        rb9.setBounds(90, 230, 40, 23);

        bgAlignment.add(rb6);
        rb6.setText("6");
        jPanel2.add(rb6);
        rb6.setBounds(90, 250, 40, 23);

        bgAlignment.add(rb5);
        rb5.setText("5");
        jPanel2.add(rb5);
        rb5.setBounds(50, 250, 40, 23);

        bgAlignment.add(rb4);
        rb4.setText("4");
        jPanel2.add(rb4);
        rb4.setBounds(10, 250, 40, 23);

        bgAlignment.add(rb2);
        rb2.setSelected(true);
        rb2.setText("2");
        jPanel2.add(rb2);
        rb2.setBounds(50, 270, 40, 23);

        bgAlignment.add(rb7);
        rb7.setText("7");
        jPanel2.add(rb7);
        rb7.setBounds(10, 230, 40, 23);

        bgAlignment.add(rb3);
        rb3.setText("3");
        jPanel2.add(rb3);
        rb3.setBounds(90, 270, 40, 23);

        bgAlignment.add(rb8);
        rb8.setText("8");
        jPanel2.add(rb8);
        rb8.setBounds(50, 230, 40, 23);

        bgAlignment.add(rb1);
        rb1.setText("1");
        jPanel2.add(rb1);
        rb1.setBounds(10, 270, 40, 23);

        jLabel23.setText("Alignment :");
        jPanel2.add(jLabel23);
        jLabel23.setBounds(10, 210, 90, 20);

        cboOpaqueBox.setText("Opaque box");
        jPanel2.add(cboOpaqueBox);
        cboOpaqueBox.setBounds(350, 260, 130, 20);

        cboBold.setText("Bold");
        jPanel2.add(cboBold);
        cboBold.setBounds(10, 180, 80, 20);

        cboItalic.setText("Italic");
        jPanel2.add(cboItalic);
        cboItalic.setBounds(100, 180, 80, 20);

        cboUnderline.setText("Underline");
        jPanel2.add(cboUnderline);
        cboUnderline.setBounds(190, 180, 90, 20);

        jLabel24.setText("Border :");
        jPanel2.add(jLabel24);
        jLabel24.setBounds(370, 200, 60, 30);
        jPanel2.add(spiMarginB);
        spiMarginB.setBounds(430, 150, 60, 30);

        jLabel25.setText("Shadow :");
        jPanel2.add(jLabel25);
        jLabel25.setBounds(370, 230, 60, 30);
        jPanel2.add(spiShadowS);
        spiShadowS.setBounds(430, 230, 60, 30);

        cboStrikeOut.setText("Strike out");
        jPanel2.add(cboStrikeOut);
        cboStrikeOut.setBounds(290, 180, 90, 20);

        jLabel26.setText("Sc. X :");
        jPanel2.add(jLabel26);
        jLabel26.setBounds(130, 200, 50, 30);

        jLabel27.setText("Sc. Y :");
        jPanel2.add(jLabel27);
        jLabel27.setBounds(130, 230, 50, 30);

        jLabel28.setText("Rot. :");
        jPanel2.add(jLabel28);
        jLabel28.setBounds(250, 200, 50, 30);

        jLabel29.setText("Spac. : ");
        jPanel2.add(jLabel29);
        jLabel29.setBounds(250, 230, 50, 30);

        jLabel30.setText("Encoding :");
        jPanel2.add(jLabel30);
        jLabel30.setBounds(150, 280, 90, 30);

        cbEncoding.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(cbEncoding);
        cbEncoding.setBounds(250, 280, 240, 30);

        cbFontname.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(cbFontname);
        cbFontname.setBounds(96, 150, 140, 30);

        tfStyleName.setComponentPopupMenu(popAbout);
        jPanel2.add(tfStyleName);
        tfStyleName.setBounds(69, 0, 170, 30);
        jPanel2.add(spiFontsize);
        spiFontsize.setBounds(320, 150, 60, 30);

        tfSpacing.setText("0");
        jPanel2.add(tfSpacing);
        tfSpacing.setBounds(300, 230, 60, 30);

        tfScaleX.setText("100");
        jPanel2.add(tfScaleX);
        tfScaleX.setBounds(180, 200, 60, 30);

        tfScaleY.setText("100");
        jPanel2.add(tfScaleY);
        tfScaleY.setBounds(180, 230, 60, 30);

        tfRotation.setText("0");
        jPanel2.add(tfRotation);
        tfRotation.setBounds(300, 200, 60, 30);

        lstStyles.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstStyles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        lstStyles.setComponentPopupMenu(popStyleList);
        lstStyles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstStylesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstStyles);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(10, 30, 140, 120);

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdd);
        btnAdd.setBounds(150, 30, 90, 40);

        btnModify.setText("Modify");
        btnModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyActionPerformed(evt);
            }
        });
        jPanel2.add(btnModify);
        btnModify.setBounds(150, 70, 90, 40);

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete);
        btnDelete.setBounds(150, 110, 90, 40);

        jTabbedPane1.addTab("Embedded Styles", jPanel2);

        jLabel12.setText("Author(s) :");

        jLabel13.setText("Comments :");

        jLabel14.setText("Preview :");

        jLabel15.setText("Collection :");

        tfAuthor.setText("Users of AssFxMaker/Funsub Project @ 2006.");
        tfAuthor.setComponentPopupMenu(popAbout);
        tfAuthor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfAuthorFocusGained(evt);
            }
        });

        tfComments.setText("Created with AssFxMaker/Funsub Project @ 2006.");
        tfComments.setComponentPopupMenu(popAbout);
        tfComments.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfCommentsFocusGained(evt);
            }
        });

        tfPreview.setText("<none>");
        tfPreview.setComponentPopupMenu(popAbout);
        tfPreview.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfPreviewFocusGained(evt);
            }
        });

        tfCollection.setText("<none>");
        tfCollection.setComponentPopupMenu(popAbout);
        tfCollection.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfCollectionFocusGained(evt);
            }
        });

        btnPreview.setText("Change");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });

        panPreview.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));
        panPreview.setToolTipText("<html>Don't do image larger than <b>460</b>x<b>150</b> !");
        panPreview.setLayout(null);

        lblPreview.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panPreview.add(lblPreview);
        lblPreview.setBounds(180, 70, 130, 60);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfCollection, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(tfPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tfComments, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                            .addComponent(tfAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(tfAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(tfComments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(tfPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPreview))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(tfCollection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("?", jPanel3);

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        taHelpPlease.setBackground(new java.awt.Color(237, 255, 237));
        taHelpPlease.setColumns(20);
        taHelpPlease.setEditable(false);
        taHelpPlease.setRows(5);
        taHelpPlease.setText("%sK - Time of start of syllable   or   Time of start of letter\n%eK - Time of end of syllable   or   Time of end of letter\n%dK - Time of duration of syllable   or   Time of duration of letter\n%osK - Time of start of syllable\n%oeK - Time of end of syllable\n%odK - Time of duration of syllable\n\n%dP - Time of duration of sentence\n%posAF[x1,y1,x2,y2] - Give a random location for x and y\n%num[a1,a2] - Give a random number\n\n$wordinlowercase - Return the result of a function which is in the « Variables » tab.");
        jScrollPane3.setViewportView(taHelpPlease);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Help please", jPanel5);

        jLabel8.setText("First layer :");

        tfFirstLayer.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfFirstLayer.setText("0");
        tfFirstLayer.setComponentPopupMenu(popAbout);

        jLabel9.setText("Moment :");

        bgMoment.add(rbMomentBefore);
        rbMomentBefore.setText("Before");
        rbMomentBefore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMomentBeforeActionPerformed(evt);
            }
        });

        bgMoment.add(rbMomentMeantime);
        rbMomentMeantime.setSelected(true);
        rbMomentMeantime.setText("Meantime");
        rbMomentMeantime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMomentMeantimeActionPerformed(evt);
            }
        });

        bgMoment.add(rbMomentAfter);
        rbMomentAfter.setText("After");
        rbMomentAfter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMomentAfterActionPerformed(evt);
            }
        });

        jLabel10.setText("Time (ms) :");

        tfMomentTime.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfMomentTime.setText("0");
        tfMomentTime.setComponentPopupMenu(popAbout);

        cbSaveEffect.setText("Save effects");
        cbSaveEffect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSaveEffectActionPerformed(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Effects type : ");

        cbFunctions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<no method>" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(cbSaveEffect, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Ok_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(rbMomentBefore)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbMomentMeantime)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbMomentAfter)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tfFirstLayer)
                                    .addComponent(tfMomentTime, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(cbFunctions, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbFunctions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(tfFirstLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(rbMomentBefore)
                    .addComponent(rbMomentMeantime)
                    .addComponent(rbMomentAfter)
                    .addComponent(jLabel10)
                    .addComponent(tfMomentTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ok_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbSaveEffect)
                    .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Ok_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ok_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_Ok_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void rbMomentBeforeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMomentBeforeActionPerformed
        timeMoment();
    }//GEN-LAST:event_rbMomentBeforeActionPerformed

    private void rbMomentMeantimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMomentMeantimeActionPerformed
        timeMoment();
    }//GEN-LAST:event_rbMomentMeantimeActionPerformed

    private void rbMomentAfterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMomentAfterActionPerformed
        timeMoment();
    }//GEN-LAST:event_rbMomentAfterActionPerformed

    private void btnAddLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLayerActionPerformed
        String s = tfOverrides.getText() + ";"
                + tfInnerOverrides.getText() + ";"
                + tfLastOverrides.getText() + ";"
                + tfBefore.getText() + ";"
                + tfAfter.getText();
        if(s.contains(EXAMPLE_K)==false & s.contains(EXAMPLE_NONE)==false){
            dtmodel.addRow(new Object[]{dtmodel.getRowCount(),s});
            getOKButtonState();
        }        
    }//GEN-LAST:event_btnAddLayerActionPerformed

    private void btnEditLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditLayerActionPerformed
        try{
            String s = (String)dtmodel.getValueAt(layersTable.getSelectedRow(), 1);
            String table[] = s.split(";");
            tfOverrides.setText(table[0]);
            tfInnerOverrides.setText(table[1]);
            tfLastOverrides.setText(table[2]);
            tfBefore.setText(table[3]);
            tfAfter.setText(table.length==5?table[4]:"");
        }catch(Exception exc){
            
        }
    }//GEN-LAST:event_btnEditLayerActionPerformed

    private void btnDeleteLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteLayerActionPerformed
        try{
            int tabtemp[] = layersTable.getSelectedRows();
            for (int i=tabtemp.length-1;i>=0;i--){
                dtmodel.removeRow(tabtemp[i]);
            }
            getOKButtonState();
        }catch(Exception exc){
            
        }
    }//GEN-LAST:event_btnDeleteLayerActionPerformed

    private void cbSaveEffectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSaveEffectActionPerformed
        if(cbSaveEffect.isSelected()){
            saveState = SaveState.ENABLE;
        }else{
            saveState = SaveState.DISABLE;
        }
    }//GEN-LAST:event_cbSaveEffectActionPerformed

    private void btnChangeLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeLayerActionPerformed
        String s = tfOverrides.getText() + ";"
                + tfInnerOverrides.getText() + ";"
                + tfLastOverrides.getText() + ";"
                + tfBefore.getText() + ";"
                + tfAfter.getText();
        if(s.contains(EXAMPLE_K)==false & s.contains(EXAMPLE_NONE)==false){
            dtmodel.setValueAt(s, layersTable.getSelectedRow(), 1);
        }        
    }//GEN-LAST:event_btnChangeLayerActionPerformed

    private void tfNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNameKeyReleased
        getOKButtonState();
    }//GEN-LAST:event_tfNameKeyReleased

    private void tfOverridesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfOverridesFocusGained
        tfFocused = tfOverrides;
    }//GEN-LAST:event_tfOverridesFocusGained

    private void tfInnerOverridesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfInnerOverridesFocusGained
        tfFocused = tfInnerOverrides;
    }//GEN-LAST:event_tfInnerOverridesFocusGained

    private void tfLastOverridesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfLastOverridesFocusGained
        tfFocused = tfLastOverrides;
    }//GEN-LAST:event_tfLastOverridesFocusGained

    private void tfBeforeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfBeforeFocusGained
        tfFocused = tfBefore;
    }//GEN-LAST:event_tfBeforeFocusGained

    private void tfAfterFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfAfterFocusGained
        tfFocused = tfAfter;
    }//GEN-LAST:event_tfAfterFocusGained

    private void popmCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCutActionPerformed
        cut();
    }//GEN-LAST:event_popmCutActionPerformed

    private void popmCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCopyActionPerformed
        copy();
    }//GEN-LAST:event_popmCopyActionPerformed

    private void popmPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPasteActionPerformed
        paste();
    }//GEN-LAST:event_popmPasteActionPerformed

    private void popmDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDeleteActionPerformed
        delete();
    }//GEN-LAST:event_popmDeleteActionPerformed

    private void popmClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmClearActionPerformed
        clearAll();
    }//GEN-LAST:event_popmClearActionPerformed

    private void popmColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmColorActionPerformed
        AssColorDialog acd = new AssColorDialog(frame,true);
        acd.setLocationRelativeTo(null);
        String color = acd.showDialog(tfFocused.getSelectedText());
        if(color!=null){
            String s = tfFocused.getText();
            int sStart = tfFocused.getSelectionStart();
            int sEnd = tfFocused.getSelectionEnd();
            tfFocused.setText(s.substring(0, sStart)+color+s.substring(sEnd));
        }
    }//GEN-LAST:event_popmColorActionPerformed

    private void popmAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmAlphaActionPerformed
        AssAlphaDialog aad = new AssAlphaDialog(frame, true);
        aad.setLocationRelativeTo(null);
        String hexa = "FF";
        try{
            hexa = tfFocused.getSelectedText();
        }catch(Exception exc){}
        hexa = aad.showDialog(hexa);
        if(hexa==null){hexa="";}
        hexa=hexa.toUpperCase();
        String s = tfFocused.getText();
        int sStart = tfFocused.getSelectionStart();
        int sEnd = tfFocused.getSelectionEnd();
        tfFocused.setText(s.substring(0, sStart)+hexa+s.substring(sEnd));                
    }//GEN-LAST:event_popmAlphaActionPerformed

    private void popmDrawingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawingActionPerformed
        if(drawingPath.isEmpty()==false){
            try {// Open an external software
                Runtime.getRuntime().exec(drawingPath);
            } catch (java.io.IOException ex) {}
        }else{
            DrawingChooserDialog dcd = new DrawingChooserDialog(frame, true);
            dcd.setLocationRelativeTo(null);
            dcd.setPath(drawingsPath);
            String draw = dcd.showDialog();
            if(draw!=null){
                String s = tfFocused.getText();
                int sStart = tfFocused.getSelectionStart();
                int sEnd = tfFocused.getSelectionEnd();
                tfFocused.setText(s.substring(0, sStart)+draw+s.substring(sEnd));
            }
        }
    }//GEN-LAST:event_popmDrawingActionPerformed

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        // Clear the list of file filters.
        for (javax.swing.filechooser.FileFilter f : fcPreview.getChoosableFileFilters()){
            fcPreview.removeChoosableFileFilter(f);
        }

        // Add good file filters.
        fcPreview.addChoosableFileFilter(new PngJpgGifFilter());
        fcPreview.setAccessory(new ImagePreview(fcPreview));
        // Action
        int z = fcPreview.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            tfPreview.setText(fcPreview.getSelectedFile().getPath());
            //TODO panPreview
            ImageIcon ii0 = new ImageIcon(fcPreview.getSelectedFile().getPath());
            lblPreview.setIcon(ii0);
            lblPreview.setSize(ii0.getIconWidth(), ii0.getIconHeight());
            lblPreview.setLocation(panPreview.getWidth()/2-lblPreview.getWidth()/2,
                    panPreview.getHeight()/2-lblPreview.getHeight()/2);
        }
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void popmIntCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmIntCalcActionPerformed
        putTextToFocused("~your calculation here~");
    }//GEN-LAST:event_popmIntCalcActionPerformed

    private void popmFloCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFloCalcActionPerformed
        putTextToFocused("`your calculation here`");
    }//GEN-LAST:event_popmFloCalcActionPerformed

    private void popm_fnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fnActionPerformed
        putTextToFocused("\\fnDialog");
    }//GEN-LAST:event_popm_fnActionPerformed

    private void popm_feActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_feActionPerformed
        putTextToFocused("\\fe1");
    }//GEN-LAST:event_popm_feActionPerformed

    private void popm_qActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_qActionPerformed
        putTextToFocused("\\q1");
    }//GEN-LAST:event_popm_qActionPerformed

    private void popm_aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_aActionPerformed
        putTextToFocused("\\a2");
    }//GEN-LAST:event_popm_aActionPerformed

    private void popm_anActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_anActionPerformed
        putTextToFocused("\\an2");
    }//GEN-LAST:event_popm_anActionPerformed

    private void popm_posActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_posActionPerformed
        putTextToFocused("\\pos(x,y)");
    }//GEN-LAST:event_popm_posActionPerformed

    private void popm_moveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moveActionPerformed
        putTextToFocused("\\move(x1,y1,x2,y2[,t1,t2])");
    }//GEN-LAST:event_popm_moveActionPerformed

    private void popm_orgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_orgActionPerformed
        putTextToFocused("\\org(x,y)");
    }//GEN-LAST:event_popm_orgActionPerformed

    private void popm_fadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fadActionPerformed
        putTextToFocused("\\fad(t1,t2)");
    }//GEN-LAST:event_popm_fadActionPerformed

    private void popm_fadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fadeActionPerformed
        putTextToFocused("\\fade(a1,a2,a3,t1,t2,t3,t4)");
    }//GEN-LAST:event_popm_fadeActionPerformed

    private void popm_clipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_clipActionPerformed
        putTextToFocused("\\clip(x1,y1,x2,y2)");
    }//GEN-LAST:event_popm_clipActionPerformed

    private void popm_clip2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_clip2ActionPerformed
        putTextToFocused("\\clip([scale,]some drawings)");
    }//GEN-LAST:event_popm_clip2ActionPerformed

    private void popm_bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_bActionPerformed
        putTextToFocused("\\b1");
    }//GEN-LAST:event_popm_bActionPerformed

    private void popm_iActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_iActionPerformed
        putTextToFocused("\\i1");
    }//GEN-LAST:event_popm_iActionPerformed

    private void popm_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_uActionPerformed
        putTextToFocused("\\u1");
    }//GEN-LAST:event_popm_uActionPerformed

    private void popm_sActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_sActionPerformed
        putTextToFocused("\\s1");
    }//GEN-LAST:event_popm_sActionPerformed

    private void popm_bordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_bordActionPerformed
        putTextToFocused("\\bord2");
    }//GEN-LAST:event_popm_bordActionPerformed

    private void popm_shadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_shadActionPerformed
        putTextToFocused("\\shad2");
    }//GEN-LAST:event_popm_shadActionPerformed

    private void popm_beActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_beActionPerformed
        putTextToFocused("\\be0");
    }//GEN-LAST:event_popm_beActionPerformed

    private void popm_blurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_blurActionPerformed
        putTextToFocused("\\blur0");
    }//GEN-LAST:event_popm_blurActionPerformed

    private void popm_fsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fsActionPerformed
        putTextToFocused("\\fs50");
    }//GEN-LAST:event_popm_fsActionPerformed

    private void popm_fscxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fscxActionPerformed
        putTextToFocused("\\fscx100");
    }//GEN-LAST:event_popm_fscxActionPerformed

    private void popm_fscyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fscyActionPerformed
        putTextToFocused("\\fscy100");
    }//GEN-LAST:event_popm_fscyActionPerformed

    private void popm_fspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fspActionPerformed
        putTextToFocused("\\fsp0");
    }//GEN-LAST:event_popm_fspActionPerformed

    private void popm_frxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frxActionPerformed
        putTextToFocused("\\frx0");
    }//GEN-LAST:event_popm_frxActionPerformed

    private void popm_fryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fryActionPerformed
        putTextToFocused("\\fry0");
    }//GEN-LAST:event_popm_fryActionPerformed

    private void popm_frzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frzActionPerformed
        putTextToFocused("\\frz0");
    }//GEN-LAST:event_popm_frzActionPerformed

    private void popm_1cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1cActionPerformed
        putTextToFocused("\\1c&H000000&");
    }//GEN-LAST:event_popm_1cActionPerformed

    private void popm_2cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2cActionPerformed
        putTextToFocused("\\2c&H000000&");
    }//GEN-LAST:event_popm_2cActionPerformed

    private void popm_3cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3cActionPerformed
        putTextToFocused("\\3c&H000000&");
    }//GEN-LAST:event_popm_3cActionPerformed

    private void popm_4cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4cActionPerformed
        putTextToFocused("\\4c&H000000&");
    }//GEN-LAST:event_popm_4cActionPerformed

    private void popm_alphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_alphaActionPerformed
        putTextToFocused("\\alpha&H00&");
    }//GEN-LAST:event_popm_alphaActionPerformed

    private void popm_1aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1aActionPerformed
        putTextToFocused("\\1a&H00&");
    }//GEN-LAST:event_popm_1aActionPerformed

    private void popm_2aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2aActionPerformed
        putTextToFocused("\\2a&H00&");
    }//GEN-LAST:event_popm_2aActionPerformed

    private void popm_3aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3aActionPerformed
        putTextToFocused("\\3a&H00&");
    }//GEN-LAST:event_popm_3aActionPerformed

    private void popm_4aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4aActionPerformed
        putTextToFocused("\\4a&H00&");
    }//GEN-LAST:event_popm_4aActionPerformed

    private void popm_kActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_kActionPerformed
        putTextToFocused("\\k~%dK/10~");
    }//GEN-LAST:event_popm_kActionPerformed

    private void popm_kfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_kfActionPerformed
        putTextToFocused("\\kf~%dK/10~");
    }//GEN-LAST:event_popm_kfActionPerformed

    private void popm_koActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_koActionPerformed
        putTextToFocused("\\ko~%dK/10~");
    }//GEN-LAST:event_popm_koActionPerformed

    private void popm_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_resetActionPerformed
        putTextToFocused("\\r");
    }//GEN-LAST:event_popm_resetActionPerformed

    private void popm_tActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_tActionPerformed
        putTextToFocused("\\t([t1,t2,][accel,]style modifiers)");
    }//GEN-LAST:event_popm_tActionPerformed

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
    }//GEN-LAST:event_lblColor4MouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add AssStyle
        if(!tfStyleName.getText().isEmpty()){
            AssStyle as = new AssStyle();
            as.setName(tfStyleName.getText());
            as.setFontname(cbFontname.getSelectedItem().toString());
            as.setFontsize(Double.parseDouble(spiFontsize.getValue().toString()));
            int iText = Integer.parseInt(spiText.getValue().toString());
            as.setTextColor(lblColor1.getBackground(), Integer.toString(iText, 16));
            int iKara = Integer.parseInt(spiKaraoke.getValue().toString());
            as.setKaraColor(lblColor2.getBackground(), Integer.toString(iKara, 16));
            int iOut = Integer.parseInt(spiBorder.getValue().toString());
            as.setOutlineColor(lblColor3.getBackground(), Integer.toString(iOut, 16));
            int iShad = Integer.parseInt(spiShadow.getValue().toString());
            as.setBackColor(lblColor4.getBackground(), Integer.toString(iShad, 16));
            as.setBold(cboBold.isSelected());
            as.setItalic(cboItalic.isSelected());
            as.setUnderline(cboUnderline.isSelected());
            as.setStrikeout(cboStrikeOut.isSelected());
            as.setScaleX(Double.parseDouble(tfScaleX.getText()));
            as.setScaleY(Double.parseDouble(tfScaleY.getText()));
            as.setAngle(Double.parseDouble(tfRotation.getText()));
            as.setSpacing(Double.parseDouble(tfSpacing.getText()));
            as.setBorderStyle(cboOpaqueBox.isSelected());
            as.setOutline(Integer.parseInt(spiBorderS.getValue().toString()));
            as.setShadow(Integer.parseInt(spiShadowS.getValue().toString()));
            if (rb1.isSelected()){as.setAlignment(1);}
            if (rb2.isSelected()){as.setAlignment(2);}
            if (rb3.isSelected()){as.setAlignment(3);}
            if (rb4.isSelected()){as.setAlignment(4);}
            if (rb5.isSelected()){as.setAlignment(5);}
            if (rb6.isSelected()){as.setAlignment(6);}
            if (rb7.isSelected()){as.setAlignment(7);}
            if (rb8.isSelected()){as.setAlignment(8);}
            if (rb9.isSelected()){as.setAlignment(9);}
            as.setMarginL(Integer.parseInt(spiMarginL.getValue().toString()));
            as.setMarginR(Integer.parseInt(spiMarginR.getValue().toString()));
            as.setMarginV(Integer.parseInt(spiMarginVT.getValue().toString()));
            as.setMarginB(Integer.parseInt(spiMarginB.getValue().toString()));
            as.setMarginT(Integer.parseInt(spiMarginVT.getValue().toString()));
            Encoding enc = (Encoding)cbEncoding.getSelectedItem();
            as.setEncoding(enc.getNumber());
            dlm.addElement(as);
        }        
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifyActionPerformed
        // Modify an AssStyle
        int index = lstStyles.getSelectedIndex();
        AssStyle as = (AssStyle)dlm.getElementAt(index);
        as.setName(tfStyleName.getText());
        as.setFontname(cbFontname.getSelectedItem().toString());
        as.setFontsize(Double.parseDouble(spiFontsize.getValue().toString()));
        int iText = Integer.parseInt(spiText.getValue().toString());
        as.setTextColor(lblColor1.getBackground(), Integer.toString(iText, 16));
        int iKara = Integer.parseInt(spiKaraoke.getValue().toString());
        as.setKaraColor(lblColor2.getBackground(), Integer.toString(iKara, 16));
        int iOut = Integer.parseInt(spiBorder.getValue().toString());
        as.setOutlineColor(lblColor3.getBackground(), Integer.toString(iOut, 16));
        int iShad = Integer.parseInt(spiShadow.getValue().toString());
        as.setBackColor(lblColor4.getBackground(), Integer.toString(iShad, 16));
        as.setBold(cboBold.isSelected());
        as.setItalic(cboItalic.isSelected());
        as.setUnderline(cboUnderline.isSelected());
        as.setStrikeout(cboStrikeOut.isSelected());
        as.setScaleX(Double.parseDouble(tfScaleX.getText()));
        as.setScaleY(Double.parseDouble(tfScaleY.getText()));
        as.setAngle(Double.parseDouble(tfRotation.getText()));
        as.setSpacing(Double.parseDouble(tfSpacing.getText()));
        as.setBorderStyle(cboOpaqueBox.isSelected());
        as.setOutline(Integer.parseInt(spiBorderS.getValue().toString()));
        as.setShadow(Integer.parseInt(spiShadowS.getValue().toString()));
        if (rb1.isSelected()){as.setAlignment(1);}
        if (rb2.isSelected()){as.setAlignment(2);}
        if (rb3.isSelected()){as.setAlignment(3);}
        if (rb4.isSelected()){as.setAlignment(4);}
        if (rb5.isSelected()){as.setAlignment(5);}
        if (rb6.isSelected()){as.setAlignment(6);}
        if (rb7.isSelected()){as.setAlignment(7);}
        if (rb8.isSelected()){as.setAlignment(8);}
        if (rb9.isSelected()){as.setAlignment(9);}
        as.setMarginL(Integer.parseInt(spiMarginL.getValue().toString()));
        as.setMarginR(Integer.parseInt(spiMarginR.getValue().toString()));
        as.setMarginV(Integer.parseInt(spiMarginVT.getValue().toString()));
        as.setMarginB(Integer.parseInt(spiMarginB.getValue().toString()));
        as.setMarginT(Integer.parseInt(spiMarginVT.getValue().toString()));
        Encoding enc = (Encoding)cbEncoding.getSelectedItem();
        as.setEncoding(enc.getNumber());
    }//GEN-LAST:event_btnModifyActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Delete AssStyles
        refreshInfos = false; // TODO Do a better method without the error without refreshInfos wheeze
        dlm.remove(lstStyles.getSelectedIndex());
        lstStyles.repaint();
        refreshInfos = true;
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void lstStylesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstStylesValueChanged
        // Change all setting of form object to show the selected AssStyle
        // of the AssStyles list.
        if(refreshInfos==true){ // TODO Do a better method without error
            AssStyle as = (AssStyle)dlm.getElementAt(lstStyles.getSelectedIndex());
            tfStyleName.setText(as.getName());
            cbFontname.setSelectedItem(as.getFontname());
            spiFontsize.setValue(as.getFontsize());

            spiText.setValue(Integer.parseInt(as.getTextAlpha(), 16));
            spiKaraoke.setValue(Integer.parseInt(as.getKaraAlpha(), 16));
            spiBorder.setValue(Integer.parseInt(as.getOutlineAlpha(), 16));
            spiShadow.setValue(Integer.parseInt(as.getBackAlpha(), 16));
            lblColor1.setBackground(as.getTextCColor());
            lblColor2.setBackground(as.getKaraCColor());
            lblColor3.setBackground(as.getOutlineCColor());
            lblColor4.setBackground(as.getBackCColor());

            cboBold.setSelected(as.getBold());
            cboItalic.setSelected(as.getItalic());
            cboUnderline.setSelected(as.getUnderline());
            cboStrikeOut.setSelected(as.getStrikeout());
            tfScaleX.setText(Double.toString(as.getScaleX()));
            tfScaleY.setText(Double.toString(as.getScaleY()));
            tfRotation.setText(Double.toString(as.getAngle()));
            tfSpacing.setText(Double.toString(as.getSpacing()));
            cboOpaqueBox.setSelected(as.getBorderSStyle());
            spiBorderS.setValue(as.getOutline());
            spiShadowS.setValue(as.getShadow());
            if (as.getAlignment()==1){rb1.setSelected(true);}
            if (as.getAlignment()==2){rb2.setSelected(true);}
            if (as.getAlignment()==3){rb3.setSelected(true);}
            if (as.getAlignment()==4){rb4.setSelected(true);}
            if (as.getAlignment()==5){rb5.setSelected(true);}
            if (as.getAlignment()==6){rb6.setSelected(true);}
            if (as.getAlignment()==7){rb7.setSelected(true);}
            if (as.getAlignment()==8){rb8.setSelected(true);}
            if (as.getAlignment()==9){rb9.setSelected(true);}
            spiMarginL.setValue(as.getMarginL());
            spiMarginR.setValue(as.getMarginR());
            spiMarginVT.setValue(as.getMarginV());
            spiMarginB.setValue(as.getMarginB());
            Encoding enc = Encoding.DEFAULT;
            cbEncoding.setSelectedItem(enc.getEncodingFrom(as.getEncoding()));
        }
    }//GEN-LAST:event_lstStylesValueChanged

    private void popmImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmImportActionPerformed
        // Import a style
        Clipboard cb = new Clipboard();
        String str = cb.CPaste();
        if (str.isEmpty()==false){
            AssStyle as = new AssStyle();
            as.fromAssStyleString(str);
            dlm.addElement(as);
        }
    }//GEN-LAST:event_popmImportActionPerformed

    private void popmExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmExportActionPerformed
        // Export a style
        try{
            // Find the AssStyle object :
            AssStyle as = (AssStyle)lstStyles.getSelectedValue();
            // Get a string of this AssStyle
            String str = as.toAssStyleString();
            // Copy to clipboard
            Clipboard cb = new Clipboard();
            cb.CCopy(str);
        }catch(Exception exc){
            // Do nothing
        }        
    }//GEN-LAST:event_popmExportActionPerformed

    private void popmSurroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSurroundActionPerformed
        // popup menu - Surround the override expression by braces
        String s = tfFocused.getText();
        tfFocused.setText("{"+s+"}");
    }//GEN-LAST:event_popmSurroundActionPerformed

    private void popmDelSurroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDelSurroundActionPerformed
        // popup menu - Delete all braces of the override expression
        String s = tfFocused.getText();
        s = s.replaceAll("\\{", "");
        s = s.replaceAll("\\}", "");
        tfFocused.setText(s);
    }//GEN-LAST:event_popmDelSurroundActionPerformed

    private void popmCut2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCut2ActionPerformed
        cut();
    }//GEN-LAST:event_popmCut2ActionPerformed

    private void popmCopy2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCopy2ActionPerformed
        copy();
    }//GEN-LAST:event_popmCopy2ActionPerformed

    private void popmPaste2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPaste2ActionPerformed
        paste();
    }//GEN-LAST:event_popmPaste2ActionPerformed

    private void popmDelete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDelete2ActionPerformed
        delete();
    }//GEN-LAST:event_popmDelete2ActionPerformed

    private void popmSelAll2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelAll2ActionPerformed
        selectAll();
    }//GEN-LAST:event_popmSelAll2ActionPerformed

    private void tfAuthorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfAuthorFocusGained
        tfFocused = tfAuthor;
    }//GEN-LAST:event_tfAuthorFocusGained

    private void tfCommentsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCommentsFocusGained
        tfFocused = tfComments;
    }//GEN-LAST:event_tfCommentsFocusGained

    private void tfPreviewFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPreviewFocusGained
        tfFocused = tfPreview;
    }//GEN-LAST:event_tfPreviewFocusGained

    private void tfCollectionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCollectionFocusGained
        tfFocused = tfCollection;
    }//GEN-LAST:event_tfCollectionFocusGained

    private void popmSelAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelAllActionPerformed
        selectAll();
    }//GEN-LAST:event_popmSelAllActionPerformed

    private void popmClearAll2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmClearAll2ActionPerformed
        clearAll();
    }//GEN-LAST:event_popmClearAll2ActionPerformed

    private void tfNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfNameFocusGained
        tfFocused = tfName;
    }//GEN-LAST:event_tfNameFocusGained

    private void popm_xbordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_xbordActionPerformed
        putTextToFocused("\\xbord2");
    }//GEN-LAST:event_popm_xbordActionPerformed

    private void popm_ybordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_ybordActionPerformed
        putTextToFocused("\\ybord2");
    }//GEN-LAST:event_popm_ybordActionPerformed

    private void popm_xshadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_xshadActionPerformed
        putTextToFocused("\\xshad2");
    }//GEN-LAST:event_popm_xshadActionPerformed

    private void popm_yshadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_yshadActionPerformed
        putTextToFocused("\\yshad2");
    }//GEN-LAST:event_popm_yshadActionPerformed

    private void popm_faxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_faxActionPerformed
        putTextToFocused("\\fax0");
    }//GEN-LAST:event_popm_faxActionPerformed

    private void popm_fayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fayActionPerformed
        putTextToFocused("\\fay0");
    }//GEN-LAST:event_popm_fayActionPerformed

    private void popm_iclipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_iclipActionPerformed
        putTextToFocused("\\iclip(x1,y1,x2,y2)");
    }//GEN-LAST:event_popm_iclipActionPerformed

    private void popm_fscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fscActionPerformed
        putTextToFocused("\\fsc100");
    }//GEN-LAST:event_popm_fscActionPerformed

    private void popm_fsvpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fsvpActionPerformed
        putTextToFocused("\\fsvp0");
    }//GEN-LAST:event_popm_fsvpActionPerformed

    private void popm_frsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frsActionPerformed
        putTextToFocused("\\frs0");
    }//GEN-LAST:event_popm_frsActionPerformed

    private void popm_zActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_zActionPerformed
        putTextToFocused("\\z0");
    }//GEN-LAST:event_popm_zActionPerformed

    private void popm_distortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_distortActionPerformed
        putTextToFocused("\\distort(u1,v1,u2,v2,u3,v3)");
    }//GEN-LAST:event_popm_distortActionPerformed

    private void popm_mdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdActionPerformed
        putTextToFocused("\\md0");
    }//GEN-LAST:event_popm_mdActionPerformed

    private void popm_mdxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdxActionPerformed
        putTextToFocused("\\mdx0");
    }//GEN-LAST:event_popm_mdxActionPerformed

    private void popm_mdyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdyActionPerformed
        putTextToFocused("\\mdy0");
    }//GEN-LAST:event_popm_mdyActionPerformed

    private void popm_mdzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdzActionPerformed
        putTextToFocused("\\mdz0");
    }//GEN-LAST:event_popm_mdzActionPerformed

    private void popm_1vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1vcActionPerformed
        putTextToFocused("\\1vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_1vcActionPerformed

    private void popm_2vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2vcActionPerformed
        putTextToFocused("\\2vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_2vcActionPerformed

    private void popm_3vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3vcActionPerformed
        putTextToFocused("\\3vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_3vcActionPerformed

    private void popm_4vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4vcActionPerformed
        putTextToFocused("\\4vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_4vcActionPerformed

    private void popm_1vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1vaActionPerformed
        putTextToFocused("\\1va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_1vaActionPerformed

    private void popm_2vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2vaActionPerformed
        putTextToFocused("\\2va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_2vaActionPerformed

    private void popm_3vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3vaActionPerformed
        putTextToFocused("\\3va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_3vaActionPerformed

    private void popm_4vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4vaActionPerformed
        putTextToFocused("\\4va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_4vaActionPerformed

    private void popm_1imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1imgActionPerformed
        putTextToFocused("\\1img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_1imgActionPerformed

    private void popm_2imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2imgActionPerformed
        putTextToFocused("\\2img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_2imgActionPerformed

    private void popm_3imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3imgActionPerformed
        putTextToFocused("\\3img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_3imgActionPerformed

    private void popm_4imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4imgActionPerformed
        putTextToFocused("\\4img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_4imgActionPerformed

    private void popm_jitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_jitterActionPerformed
        putTextToFocused("\\jitter(left,right,up,down,period[,seed])");
    }//GEN-LAST:event_popm_jitterActionPerformed

    private void popm_moverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moverActionPerformed
        putTextToFocused("\\mover(x1,y1,x2,y2,angle1,angle2,radius1,radius2[,t1,t2])");
    }//GEN-LAST:event_popm_moverActionPerformed

    private void popm_iclip2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_iclip2ActionPerformed
        putTextToFocused("\\iclip(scale,drawing commands)");
    }//GEN-LAST:event_popm_iclip2ActionPerformed

    private void popm_moves3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moves3ActionPerformed
        putTextToFocused("\\moves3(x1,x2,x2,y2,x3,y3[,t1,t2])");
    }//GEN-LAST:event_popm_moves3ActionPerformed

    private void popm_moves4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moves4ActionPerformed
        putTextToFocused("\\moves4(x1,x2,x2,y2,x3,y3,x4,y4[,t1,t2])");
    }//GEN-LAST:event_popm_moves4ActionPerformed

    private void popm_movevcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_movevcActionPerformed
        putTextToFocused("\\movevc(x1,y1)");
    }//GEN-LAST:event_popm_movevcActionPerformed

    private void popm_movevc2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_movevc2ActionPerformed
        putTextToFocused("\\movevc(x1,y1,x2,y2[,t1,t2])");
    }//GEN-LAST:event_popm_movevc2ActionPerformed

    private void popmPNGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPNGActionPerformed
        // Clear the list of file filters.
        for (javax.swing.filechooser.FileFilter f : fcPreview.getChoosableFileFilters()){
            fcPreview.removeChoosableFileFilter(f);
        }
        // Add good file filters.
        fcPreview.addChoosableFileFilter(new PngFilter());
        fcPreview.setAccessory(new ImagePreview(fcPreview));
        int z = fcPreview.showOpenDialog(this);
        if (z == JFileChooser.APPROVE_OPTION){
            String png = fcPreview.getSelectedFile().getAbsolutePath();
            String s = tfFocused.getText();
            int sStart = tfFocused.getSelectionStart();
            int sEnd = tfFocused.getSelectionEnd();
            tfFocused.setText(s.substring(0, sStart)+"\""+png+"\""+s.substring(sEnd));
        }
    }//GEN-LAST:event_popmPNGActionPerformed

    private void popmImportFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmImportFromActionPerformed
        // Import styles from script
        // lAssStyle : Get list of all styles from files
        List<AssStyle> lAssStyle = new ArrayList<>();
        // las : Get list of all styles choosen by the user
        List<AssStyle> las;
        // Clear the list of file filters.
        for (javax.swing.filechooser.FileFilter f : fcPreview.getChoosableFileFilters()){
            fcPreview.removeChoosableFileFilter(f);
        }
        // Add good file filters.
        fcPreview.addChoosableFileFilter(new SubtitleFilter());
        fcPreview.setAccessory(null);
        // Action
        int z = this.fcPreview.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            // Search for styles in a SSA
            if(fcPreview.getSelectedFile().getName().endsWith("ssa")){
                AssIO aio = new AssIO();
                lAssStyle = aio.ExtractSSAStyles(fcPreview.getSelectedFile().getPath());
            }
            // Search for styles in an ASS
            if(fcPreview.getSelectedFile().getName().endsWith("ass")){
                AssIO aio = new AssIO();
                lAssStyle = aio.ExtractASSStyles(fcPreview.getSelectedFile().getPath());
            }

            // A new dialog for the choice of styles
            ImportStylesDialog isd = new ImportStylesDialog(frame, true);
            isd.setFilename(fcPreview.getSelectedFile().getName());
            isd.setLocationRelativeTo(null);
            las = isd.showDialog(lAssStyle);
            if(las!=null){
                for(AssStyle as : las){
                    boolean exist = false;
                    for(int i=0;i<dlm.getSize();i++){
                        AssStyle asDlm = (AssStyle)dlm.getElementAt(i);
                        if(asDlm.getName().equalsIgnoreCase(as.getName())){
                            exist = true;
                        }
                    }
                    if(exist==false){
                        dlm.addElement(as);
                    }
                }
            }
        }
    }//GEN-LAST:event_popmImportFromActionPerformed

    private void btnOverridesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverridesActionPerformed
    // Open a frame to edit the ass code.
        XFXIntegrationDialog xid = new XFXIntegrationDialog(frame, true);
        xid.setLocationRelativeTo(null);
        xid.setTitle(jLabel2.getText());
        String text = xid.showDialog(tfOverrides.getText());
        tfOverrides.setText(text);
        updateASSTextField(tfOverrides, high01);
    }//GEN-LAST:event_btnOverridesActionPerformed

    private void btnInnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInnerActionPerformed
    // Open a frame to edit the ass code.
        XFXIntegrationDialog xid = new XFXIntegrationDialog(frame, true);
        xid.setLocationRelativeTo(null);
        xid.setTitle(jLabel3.getText());
        String text = xid.showDialog(tfInnerOverrides.getText());
        tfInnerOverrides.setText(text);
        updateASSTextField(tfInnerOverrides, high02);
    }//GEN-LAST:event_btnInnerActionPerformed

    private void btnLastOverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastOverActionPerformed
    // Open a frame to edit the ass code.
        XFXIntegrationDialog xid = new XFXIntegrationDialog(frame, true);
        xid.setLocationRelativeTo(null);
        xid.setTitle(jLabel4.getText());
        String text = xid.showDialog(tfLastOverrides.getText());
        tfLastOverrides.setText(text);
        updateASSTextField(tfLastOverrides, high03);
    }//GEN-LAST:event_btnLastOverActionPerformed

    private void btnBeforeSylActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeforeSylActionPerformed
    // Open a frame to edit the ass code.
        XFXIntegrationDialog xid = new XFXIntegrationDialog(frame, true);
        xid.setLocationRelativeTo(null);
        xid.setTitle(jLabel5.getText());
        String text = xid.showDialog(tfBefore.getText());
        tfBefore.setText(text);
        updateASSTextField(tfBefore, high04);
    }//GEN-LAST:event_btnBeforeSylActionPerformed

    private void btnAfterSylActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAfterSylActionPerformed
    // Open a frame to edit the ass code.
        XFXIntegrationDialog xid = new XFXIntegrationDialog(frame, true);
        xid.setLocationRelativeTo(null);
        xid.setTitle(jLabel6.getText());
        String text = xid.showDialog(tfAfter.getText());
        tfAfter.setText(text);
        updateASSTextField(tfAfter, high05);
    }//GEN-LAST:event_btnAfterSylActionPerformed

    private void popmCodeInitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeInitActionPerformed
        String code = "require 'java'\n"
                + "require Java::assfxmaker.AssFxMaker.getRubyScriptsPath()+\"tools.rb\"\n";
        String s = epVariables.getText();
        int sStart = epVariables.getSelectionStart();
        int sEnd = epVariables.getSelectionEnd();
        epVariables.setText(s.substring(0, sStart) + code + s.substring(sEnd));
    }//GEN-LAST:event_popmCodeInitActionPerformed

    private void popmCodeDefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeDefActionPerformed
        String code = "def variable\n"
                + "  sk = Java::assfxmaker.AssFxMaker.getKaraokeStart().to_i()\n"
                + "  ek = Java::assfxmaker.AssFxMaker.getKaraokeEnd().to_i()\n"
                + "  dk = Java::assfxmaker.AssFxMaker.getKaraokeDuration().to_i()\n"
                + "  dp = Java::assfxmaker.AssFxMaker.getKaraokeSDuration().to_i()\n"
                + "  osk = Java::assfxmaker.AssFxMaker.getKaraokeOStart().to_i() # For letter mode only\n"
                + "  oek = Java::assfxmaker.AssFxMaker.getKaraokeOEnd().to_i() # For letter mode only\n"
                + "  odk = Java::assfxmaker.AssFxMaker.getKaraokeODuration().to_i() # For letter mode only\n"
                + "  value = \"\"\n\n"
                + "  return value\n"
                + "end\n";
        String s = epVariables.getText();
        int sStart = epVariables.getSelectionStart();
        int sEnd = epVariables.getSelectionEnd();
        epVariables.setText(s.substring(0, sStart) + code + s.substring(sEnd));
    }//GEN-LAST:event_popmCodeDefActionPerformed

    private void popmCut1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCut1ActionPerformed
        try {
            Clipboard cb = new Clipboard();
            cb.CCopy(epVariables.getSelectedText());
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart) + s.substring(sEnd));
        } catch (Exception exc) {/*
             * no selected text or another thing
             */

        }
    }//GEN-LAST:event_popmCut1ActionPerformed

    private void popmCopy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCopy1ActionPerformed
        try {
            Clipboard cb = new Clipboard();
            cb.CCopy(epVariables.getSelectedText());
        } catch (Exception exc) {/*
             * no selected text or another thing
             */

        }
    }//GEN-LAST:event_popmCopy1ActionPerformed

    private void popmPaste1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPaste1ActionPerformed
        try {
            Clipboard cb = new Clipboard();
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart) + cb.CPaste() + s.substring(sEnd));
        } catch (Exception exc) {/*
             * no selected text or another thing
             */

        }
    }//GEN-LAST:event_popmPaste1ActionPerformed

    private void popmDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDelete1ActionPerformed
        try {
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart) + s.substring(sEnd));
        } catch (Exception exc) {/*
             * no selected text or another thing
             */

        }
    }//GEN-LAST:event_popmDelete1ActionPerformed

    private void popmSelAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelAll1ActionPerformed
        epVariables.setSelectionStart(0);
        epVariables.setSelectionEnd(epVariables.getText().length());
    }//GEN-LAST:event_popmSelAll1ActionPerformed

    private void popmClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmClear1ActionPerformed
        epVariables.setText("");
    }//GEN-LAST:event_popmClear1ActionPerformed

    private void popmColor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmColor1ActionPerformed
        AssColorDialog acd = new AssColorDialog(frame, true);
        acd.setLocationRelativeTo(null);
        String color = acd.showDialog(epVariables.getSelectedText());
        if (color != null) {
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart) + color + s.substring(sEnd));
        }
    }//GEN-LAST:event_popmColor1ActionPerformed

    private void popmAlpha1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmAlpha1ActionPerformed
        AssAlphaDialog aad = new AssAlphaDialog(frame, true);
        aad.setLocationRelativeTo(null);
        String hexa = "FF";
        try {
            hexa = epVariables.getSelectedText();
        } catch (Exception exc) {
        }
        hexa = aad.showDialog(hexa);
        if (hexa == null) {
            hexa = "";
        }
        hexa = hexa.toUpperCase();
        String s = epVariables.getText();
        int sStart = epVariables.getSelectionStart();
        int sEnd = epVariables.getSelectionEnd();
        epVariables.setText(s.substring(0, sStart) + hexa + s.substring(sEnd));
    }//GEN-LAST:event_popmAlpha1ActionPerformed

    private void popmPNG1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPNG1ActionPerformed
        // Clear the list of file filters.
        for (javax.swing.filechooser.FileFilter f : fcPreview.getChoosableFileFilters()) {
            fcPreview.removeChoosableFileFilter(f);
        }
        // Add good file filters.
        fcPreview.addChoosableFileFilter(new PngFilter());
        fcPreview.setAccessory(new ImagePreview(fcPreview));
        int z = fcPreview.showOpenDialog(this);
        if (z == JFileChooser.APPROVE_OPTION) {
            String png = fcPreview.getSelectedFile().getAbsolutePath();
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart) + "\"" + png + "\"" + s.substring(sEnd));
        }
    }//GEN-LAST:event_popmPNG1ActionPerformed

    private void popm_b1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_b1ActionPerformed
        putTextToRubyEditor("\\\\b1");
    }//GEN-LAST:event_popm_b1ActionPerformed

    private void popm_i1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_i1ActionPerformed
        putTextToRubyEditor("\\\\i1");
    }//GEN-LAST:event_popm_i1ActionPerformed

    private void popm_u1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_u1ActionPerformed
        putTextToRubyEditor("\\\\u1");
    }//GEN-LAST:event_popm_u1ActionPerformed

    private void popm_s1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_s1ActionPerformed
        putTextToRubyEditor("\\\\s1");
    }//GEN-LAST:event_popm_s1ActionPerformed

    private void popm_bord1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_bord1ActionPerformed
        putTextToRubyEditor("\\\\bord2");
    }//GEN-LAST:event_popm_bord1ActionPerformed

    private void popm_shad1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_shad1ActionPerformed
        putTextToRubyEditor("\\\\shad2");
    }//GEN-LAST:event_popm_shad1ActionPerformed

    private void popm_be1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_be1ActionPerformed
        putTextToRubyEditor("\\\\be0");
    }//GEN-LAST:event_popm_be1ActionPerformed

    private void popm_fs1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fs1ActionPerformed
        putTextToRubyEditor("\\\\fs50");
    }//GEN-LAST:event_popm_fs1ActionPerformed

    private void popm_fscx1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fscx1ActionPerformed
        putTextToRubyEditor("\\\\fscx100");
    }//GEN-LAST:event_popm_fscx1ActionPerformed

    private void popm_fscy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fscy1ActionPerformed
        putTextToRubyEditor("\\\\fscy100");
    }//GEN-LAST:event_popm_fscy1ActionPerformed

    private void popm_fsp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fsp1ActionPerformed
        putTextToRubyEditor("\\\\fsp0");
    }//GEN-LAST:event_popm_fsp1ActionPerformed

    private void popm_frx1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frx1ActionPerformed
        putTextToRubyEditor("\\\\frx0");
    }//GEN-LAST:event_popm_frx1ActionPerformed

    private void popm_fry1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fry1ActionPerformed
        putTextToRubyEditor("\\\\fry0");
    }//GEN-LAST:event_popm_fry1ActionPerformed

    private void popm_frz1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frz1ActionPerformed
        putTextToRubyEditor("\\\\frz0");
    }//GEN-LAST:event_popm_frz1ActionPerformed

    private void popm_1c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1c1ActionPerformed
        putTextToRubyEditor("\\\\1c&H000000&");
    }//GEN-LAST:event_popm_1c1ActionPerformed

    private void popm_2c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2c1ActionPerformed
        putTextToRubyEditor("\\\\2c&H000000&");
    }//GEN-LAST:event_popm_2c1ActionPerformed

    private void popm_3c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3c1ActionPerformed
        putTextToRubyEditor("\\\\3c&H000000&");
    }//GEN-LAST:event_popm_3c1ActionPerformed

    private void popm_4c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4c1ActionPerformed
        putTextToRubyEditor("\\\\4c&H000000&");
    }//GEN-LAST:event_popm_4c1ActionPerformed

    private void popm_alpha1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_alpha1ActionPerformed
        putTextToRubyEditor("\\\\alpha&H00&");
    }//GEN-LAST:event_popm_alpha1ActionPerformed

    private void popm_1a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1a1ActionPerformed
        putTextToRubyEditor("\\\\1a&H00&");
    }//GEN-LAST:event_popm_1a1ActionPerformed

    private void popm_2a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2a1ActionPerformed
        putTextToRubyEditor("\\\\2a&H00&");
    }//GEN-LAST:event_popm_2a1ActionPerformed

    private void popm_3a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3a1ActionPerformed
        putTextToRubyEditor("\\\\3a&H00&");
    }//GEN-LAST:event_popm_3a1ActionPerformed

    private void popm_4a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4a1ActionPerformed
        putTextToRubyEditor("\\\\4a&H00&");
    }//GEN-LAST:event_popm_4a1ActionPerformed

    private void popm_clip1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_clip1ActionPerformed
        putTextToRubyEditor("\\\\clip(x1,y1,x2,y2)");
    }//GEN-LAST:event_popm_clip1ActionPerformed

    private void popm_xbord1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_xbord1ActionPerformed
        putTextToRubyEditor("\\\\xbord2");
    }//GEN-LAST:event_popm_xbord1ActionPerformed

    private void popm_ybord1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_ybord1ActionPerformed
        putTextToRubyEditor("\\\\ybord2");
    }//GEN-LAST:event_popm_ybord1ActionPerformed

    private void popm_xshad1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_xshad1ActionPerformed
        putTextToRubyEditor("\\\\xshad2");
    }//GEN-LAST:event_popm_xshad1ActionPerformed

    private void popm_yshad1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_yshad1ActionPerformed
        putTextToRubyEditor("\\\\yshad2");
    }//GEN-LAST:event_popm_yshad1ActionPerformed

    private void popm_blur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_blur1ActionPerformed
        putTextToRubyEditor("\\\\blur0");
    }//GEN-LAST:event_popm_blur1ActionPerformed

    private void popm_fax1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fax1ActionPerformed
        putTextToRubyEditor("\\\\fax0");
    }//GEN-LAST:event_popm_fax1ActionPerformed

    private void popm_fay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fay1ActionPerformed
        putTextToRubyEditor("\\\\fay0");
    }//GEN-LAST:event_popm_fay1ActionPerformed

    private void popm_iclip1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_iclip1ActionPerformed
        putTextToRubyEditor("\\\\iclip(x1,y1,x2,y2)");
    }//GEN-LAST:event_popm_iclip1ActionPerformed

    private void popm_fsc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fsc1ActionPerformed
        putTextToRubyEditor("\\\\fsc100");
    }//GEN-LAST:event_popm_fsc1ActionPerformed

    private void popm_fsvp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fsvp1ActionPerformed
        putTextToRubyEditor("\\\\fsvp0");
    }//GEN-LAST:event_popm_fsvp1ActionPerformed

    private void popm_frs1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frs1ActionPerformed
        putTextToRubyEditor("\\\\frs0");
    }//GEN-LAST:event_popm_frs1ActionPerformed

    private void popm_z1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_z1ActionPerformed
        putTextToRubyEditor("\\\\z0");
    }//GEN-LAST:event_popm_z1ActionPerformed

    private void popm_distort1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_distort1ActionPerformed
        putTextToRubyEditor("\\\\distort(u1,v1,u2,v2,u3,v3)");
    }//GEN-LAST:event_popm_distort1ActionPerformed

    private void popm_md1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_md1ActionPerformed
        putTextToRubyEditor("\\\\md0");
    }//GEN-LAST:event_popm_md1ActionPerformed

    private void popm_mdx1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdx1ActionPerformed
        putTextToRubyEditor("\\\\mdx0");
    }//GEN-LAST:event_popm_mdx1ActionPerformed

    private void popm_mdy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdy1ActionPerformed
        putTextToRubyEditor("\\\\mdy0");
    }//GEN-LAST:event_popm_mdy1ActionPerformed

    private void popm_mdz1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdz1ActionPerformed
        putTextToRubyEditor("\\\\mdz0");
    }//GEN-LAST:event_popm_mdz1ActionPerformed

    private void popm_1vc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1vc1ActionPerformed
        putTextToRubyEditor("\\\\1vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_1vc1ActionPerformed

    private void popm_2vc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2vc1ActionPerformed
        putTextToRubyEditor("\\\\2vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_2vc1ActionPerformed

    private void popm_3vc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3vc1ActionPerformed
        putTextToRubyEditor("\\\\3vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_3vc1ActionPerformed

    private void popm_4vc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4vc1ActionPerformed
        putTextToRubyEditor("\\\\4vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_4vc1ActionPerformed

    private void popm_1va1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1va1ActionPerformed
        putTextToRubyEditor("\\\\1va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_1va1ActionPerformed

    private void popm_2va1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2va1ActionPerformed
        putTextToRubyEditor("\\\\2va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_2va1ActionPerformed

    private void popm_3va1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3va1ActionPerformed
        putTextToRubyEditor("\\\\3va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_3va1ActionPerformed

    private void popm_4va1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4va1ActionPerformed
        putTextToRubyEditor("\\\\4va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_4va1ActionPerformed

    private void popm_1img1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1img1ActionPerformed
        putTextToRubyEditor("\\\\1img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_1img1ActionPerformed

    private void popm_2img1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2img1ActionPerformed
        putTextToRubyEditor("\\\\2img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_2img1ActionPerformed

    private void popm_3img1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3img1ActionPerformed
        putTextToRubyEditor("\\\\3img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_3img1ActionPerformed

    private void popm_4img1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4img1ActionPerformed
        putTextToRubyEditor("\\\\4img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_4img1ActionPerformed

    private void popm_jitter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_jitter1ActionPerformed
        putTextToRubyEditor("\\\\jitter(left,right,up,down,period[,seed])");
    }//GEN-LAST:event_popm_jitter1ActionPerformed

    private void popm_fn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fn1ActionPerformed
        putTextToRubyEditor("\\\\fnDialog");
    }//GEN-LAST:event_popm_fn1ActionPerformed

    private void popm_fe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fe1ActionPerformed
        putTextToRubyEditor("\\\\fe1");
    }//GEN-LAST:event_popm_fe1ActionPerformed

    private void popm_q1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_q1ActionPerformed
        putTextToRubyEditor("\\\\q1");
    }//GEN-LAST:event_popm_q1ActionPerformed

    private void popm_a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_a1ActionPerformed
        putTextToRubyEditor("\\\\a2");
    }//GEN-LAST:event_popm_a1ActionPerformed

    private void popm_an1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_an1ActionPerformed
        putTextToRubyEditor("\\\\an2");
    }//GEN-LAST:event_popm_an1ActionPerformed

    private void popm_pos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_pos1ActionPerformed
        putTextToRubyEditor("\\\\pos(x,y)");
    }//GEN-LAST:event_popm_pos1ActionPerformed

    private void popm_move1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_move1ActionPerformed
        putTextToRubyEditor("\\\\move(x1,y1,x2,y2[,t1,t2])");
    }//GEN-LAST:event_popm_move1ActionPerformed

    private void popm_org1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_org1ActionPerformed
        putTextToRubyEditor("\\\\org(x,y)");
    }//GEN-LAST:event_popm_org1ActionPerformed

    private void popm_fad1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fad1ActionPerformed
        putTextToRubyEditor("\\\\fad(t1,t2)");
    }//GEN-LAST:event_popm_fad1ActionPerformed

    private void popm_fade1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fade1ActionPerformed
        putTextToRubyEditor("\\\\fade(a1,a2,a3,t1,t2,t3,t4)");
    }//GEN-LAST:event_popm_fade1ActionPerformed

    private void popm_clip3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_clip3ActionPerformed
        putTextToRubyEditor("\\\\clip([scale,]some drawings)");
    }//GEN-LAST:event_popm_clip3ActionPerformed

    private void popm_iclip3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_iclip3ActionPerformed
        putTextToRubyEditor("\\\\iclip(scale,drawing commands)");
    }//GEN-LAST:event_popm_iclip3ActionPerformed

    private void popm_mover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mover1ActionPerformed
        putTextToRubyEditor("\\\\mover(x1,y1,x2,y2,angle1,angle2,radius1,radius2[,t1,t2])");
    }//GEN-LAST:event_popm_mover1ActionPerformed

    private void popm_moves5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moves5ActionPerformed
        putTextToRubyEditor("\\\\moves3(x1,x2,x2,y2,x3,y3[,t1,t2])");
    }//GEN-LAST:event_popm_moves5ActionPerformed

    private void popm_moves6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moves6ActionPerformed
        putTextToRubyEditor("\\\\moves4(x1,x2,x2,y2,x3,y3,x4,y4[,t1,t2])");
    }//GEN-LAST:event_popm_moves6ActionPerformed

    private void popm_movevc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_movevc1ActionPerformed
        putTextToRubyEditor("\\\\movevc(x1,y1)");
    }//GEN-LAST:event_popm_movevc1ActionPerformed

    private void popm_movevc3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_movevc3ActionPerformed
        putTextToRubyEditor("\\\\movevc(x1,y1,x2,y2[,t1,t2])");
    }//GEN-LAST:event_popm_movevc3ActionPerformed

    private void popm_k1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_k1ActionPerformed
        putTextToRubyEditor("\\\\k~%dK/10~");
    }//GEN-LAST:event_popm_k1ActionPerformed

    private void popm_kf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_kf1ActionPerformed
        putTextToRubyEditor("\\\\kf~%dK/10~");
    }//GEN-LAST:event_popm_kf1ActionPerformed

    private void popm_ko1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_ko1ActionPerformed
        putTextToRubyEditor("\\\\ko~%dK/10~");
    }//GEN-LAST:event_popm_ko1ActionPerformed

    private void popm_t1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_t1ActionPerformed
        putTextToRubyEditor("\\\\t([t1,t2,][accel,]style modifiers)");
    }//GEN-LAST:event_popm_t1ActionPerformed

    private void popm_reset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_reset1ActionPerformed
        putTextToRubyEditor("\\\\r");
    }//GEN-LAST:event_popm_reset1ActionPerformed

    private void popmDrawing1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawing1ActionPerformed
        if (drawingPath.isEmpty() == false) {
            try {// Open an external software
                Runtime.getRuntime().exec(drawingPath);
            } catch (java.io.IOException ex) {
            }
        } else {
            DrawingChooserDialog dcd = new DrawingChooserDialog(frame, true);
            dcd.setLocationRelativeTo(null);
            dcd.setPath(drawingsPath);
            String draw = dcd.showDialog();
            if (draw != null) {
                String s = epVariables.getText();
                int sStart = epVariables.getSelectionStart();
                int sEnd = epVariables.getSelectionEnd();
                epVariables.setText(s.substring(0, sStart) + draw + s.substring(sEnd));
            }
        }
    }//GEN-LAST:event_popmDrawing1ActionPerformed

    /** <p>Show the dialog and wait for XFX.<br />
     * Montre la dialogue et attent un XFX.</p> */
    public FxObject showDialog(FxObject fxo){
        setFxObject(fxo);
        getOKButtonState();
        setVisible(true);
        
        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return getFxObject();
        }else{
            return fxo;
        }
    }

    /** <p>Enable or disable the textbox of time for the Moment.<br />
     * Rend disponible ou indisponible le textbox du Moment.</p> */
    private void timeMoment(){
        if(rbMomentBefore.isSelected() | rbMomentAfter.isSelected()){
            tfMomentTime.setEnabled(true);
        }else{
            tfMomentTime.setEnabled(false);
        }
    }

    /** <p>Return true if OK is pressed otherwise false.<br />
     * Retourne true si OK est pressé sinon false.</p> */
    public boolean isOKButtonPressed(){
        if(bp==ButtonPressed.OK_BUTTON){return true;}
        return false;
    }
    
    /** <p>Return true if save is selected otherwise false.<br />
     * Retourne true si la sauvegarde est sélectionnée sinon false.</p> */
    public boolean isSaveSelected(){
        if(saveState==SaveState.ENABLE){return true;}
        return false;
    }

    /** <p>Set the FxObject.<br />Définit le FxObject.</p> */
    public void setFxObject(FxObject fxo){
        xmlfxo = fxo;
        tfAuthor.setText(fxo.getAuthor());
        tfCollection.setText(fxo.getCollect());
        setCommands(fxo.getCommands(),true);
        tfComments.setText(fxo.getDescription());
        setFunction(fxo.getFunction());
        tfPreview.setText(fxo.getImage());
        setMoment(fxo.getMoment());
        tfName.setText(fxo.getName());
        setFirstLayer(fxo.getFirstLayer());
        setNbLayers(fxo.getNbLayers());
        setStyles(fxo.getStyle());
        setTime(fxo.getTime());
        setVersion(fxo.getVersion());
        epVariables.setText(fxo.getRubyCode());
        
        java.io.File fImage = new java.io.File(fxo.getImage());
        if(fImage.exists()){
            ImageIcon ii0 = new ImageIcon(fImage.getPath());
            lblPreview.setIcon(ii0);
            lblPreview.setSize(ii0.getIconWidth(), ii0.getIconHeight());
            lblPreview.setLocation(panPreview.getWidth()/2-lblPreview.getWidth()/2,
                    panPreview.getHeight()/2-lblPreview.getHeight()/2);
        }
    }

    /** <p>Get the FxObject.<br />Obtient le FxObject.</p> */
    public FxObject getFxObject(){
        xmlfxo.setAuthor(tfAuthor.getText());
        xmlfxo.setCollect(tfCollection.getText());
        xmlfxo.setCommands(getCommands());
        xmlfxo.setDescription(tfComments.getText());
        xmlfxo.setFunction(getFunction());
        xmlfxo.setFxObjectType(FxObjectType.XMLPreset);
        xmlfxo.setImage(tfPreview.getText());
        xmlfxo.setMoment(getMoment());
        xmlfxo.setName(tfName.getText());
        xmlfxo.setFirstLayer(getFirstLayer());
        xmlfxo.setNbLayers(getNbLayers());
        xmlfxo.setStyle(getStyles());
        xmlfxo.setTime(getTime());
        xmlfxo.setVersion(getVersion());
        xmlfxo.setRubyCode(epVariables.getText());
        return xmlfxo;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Methods ">
    
    /* 
     * Format of a command in a line :
     * <Line=   'overrides;innerOverrides;lastOverrides;before;after;
     *          #Style=int; #Prototype=int; #Particle=int;'>
     * 
     * Others :
     * <@Style='assStyle'>; <@Prototype='prototype'>; <@Particle='particle'>;
     */

    /** <p>Extracts each line of commands from a xml string content.<br />
     * Extrait chaque ligne de commandes à partir du contenu du xml.</p> */
    private void setCommands(String commands, boolean refresh){
        if(commands.contains("Â§")){
            commands = commands.replaceAll("Â§", "§");
        }
        String line[] = commands.split("§");
        for (int i=0; i<line.length; i++){
            if(line[i].isEmpty()==false){
                dtmodel.addRow(new Object[]{dtmodel.getRowCount(),line[i]});
            }            
        }
        if(refresh==true && dtmodel.getRowCount()>0){//Shows 1st command line.
            try{
                String s = (String)dtmodel.getValueAt(0, 1);
                String table[] = s.split(";");
                tfOverrides.setText(table[0]);
                tfInnerOverrides.setText(table.length>=2?table[1]:"");
                tfLastOverrides.setText(table.length>=3?table[2]:"");
                tfBefore.setText(table.length>=4?table[3]:"");
                tfAfter.setText(table.length==5?table[4]:"");
            }catch(Exception exc){
                exc.printStackTrace();
            }
        }
    }

    /** <p>Return each line of commands from a xml preset.<br />
     * Retourne chaque ligne de commands d'un XFX</p> */
    private String getCommands(){
        String commands = "";
        for(int i=0;i<dtmodel.getRowCount();i++){
            if(i+1<dtmodel.getRowCount()){
                commands += (String)dtmodel.getValueAt(i, 1) + "§";
            }else{
                commands += (String)dtmodel.getValueAt(i, 1);
            }
        }
        if(commands.isEmpty()){
            return commands;
        }else{
            return "§"+commands;
        }
        
    }

    /** <p>Set the function.<br />Définit la fonction.</p> */
    private void setFunction(String function){
//        if(function.startsWith("Line-")){LineXX.setSelected(true);}
//        if(function.startsWith("Syllabe-")){SyllabeXX.setSelected(true);}
//        if(function.startsWith("Letter-")){LetterXX.setSelected(true);}
//        if(function.indexOf("-Word-")>0){XWordX.setSelected(true);}
//        if(function.indexOf("-Syllabe-")>0){XSyllabeX.setSelected(true);}
//        if(function.indexOf("-Letter-")>0){XLetterX.setSelected(true);}
//        if(function.endsWith("-Basic")){XXBasic.setSelected(true);}
//        if(function.endsWith("-Period")){XXPeriod.setSelected(true);}
//        if(function.endsWith("-Random")){XXRandom.setSelected(true);}
//        if(function.endsWith("-Symmetric")){XXSymmetric.setSelected(true);}
//        if(function.endsWith("-Fixed")){XXFixed.setSelected(true);}

        boolean functionExist = false;
        for(int i=0;i<dcmFunctions.getSize();i++){
            if(dcmFunctions.getElementAt(i) instanceof myFunction){
                myFunction mfA = (myFunction)dcmFunctions.getElementAt(i);
                if(mfA.getFunction().equals(function)){
                    functionExist = true;
                    dcmFunctions.setSelectedItem(mfA);
                }
            }
        }
        
        if(functionExist==false){
            myFunction mfB = new myFunction(function, "Unknown : "+function, false);
            dcmFunctions.addElement(mfB);
            dcmFunctions.setSelectedItem(mfB);
        }
    }

    /** <p>Get the function.<br />Obtient la fonction.</p> */
    private String getFunction(){
        String function = "";
//        if(LineXX.isSelected() & XWordX.isSelected() & XXBasic.isSelected()){
//            function = FxObjectXmlFunc.LineWordBasic.getXmlFunc();
//        }else if(LineXX.isSelected() & XSyllabeX.isSelected() & XXBasic.isSelected()){
//            function = FxObjectXmlFunc.LineSyllabeBasic.getXmlFunc();
//        }else if(LineXX.isSelected() & XSyllabeX.isSelected() & XXPeriod.isSelected()){
//            function = FxObjectXmlFunc.LineSyllabePeriod.getXmlFunc();
//        }else if(LineXX.isSelected() & XSyllabeX.isSelected() & XXRandom.isSelected()){
//            function = FxObjectXmlFunc.LineSyllabeRandom.getXmlFunc();
//        }else if(LineXX.isSelected() & XSyllabeX.isSelected() & XXSymmetric.isSelected()){
//            function = FxObjectXmlFunc.LineSyllabeSymmetric.getXmlFunc();
//        }else if(LineXX.isSelected() & XLetterX.isSelected() & XXBasic.isSelected()){
//            function = FxObjectXmlFunc.LineLetterBasic.getXmlFunc();
//        }else if(SyllabeXX.isSelected() & XSyllabeX.isSelected() & XXBasic.isSelected()){
//            function = FxObjectXmlFunc.SyllabeSyllabeBasic.getXmlFunc();
//        }else if(SyllabeXX.isSelected() & XSyllabeX.isSelected() & XXFixed.isSelected()){
//            function = FxObjectXmlFunc.SyllabeSyllabeFixed.getXmlFunc();
//        }else if(SyllabeXX.isSelected() & XLetterX.isSelected() & XXBasic.isSelected()){
//            function = FxObjectXmlFunc.SyllabeLetterBasic.getXmlFunc();
//        }else if(SyllabeXX.isSelected() & XLetterX.isSelected() & XXFixed.isSelected()){
//            function = FxObjectXmlFunc.SyllabeLetterFixed.getXmlFunc();
//        }else if(LetterXX.isSelected() & XLetterX.isSelected() & XXBasic.isSelected()){
//            function = FxObjectXmlFunc.LetterLetterBasic.getXmlFunc();
//        }else if(LetterXX.isSelected() & XLetterX.isSelected() & XXFixed.isSelected()){
//            function = FxObjectXmlFunc.LetterLetterFixed.getXmlFunc();
//        }
//        return function;
        myFunction mf = (myFunction)dcmFunctions.getSelectedItem();
        function = mf.getFunction();
        return function;
    }

    /** <p>Set the moment.<br />Définit le moment.</p> */
    private void setMoment(String moment){
        if(moment.equalsIgnoreCase("Before")){
            rbMomentBefore.setSelected(true);
        }else if(moment.equalsIgnoreCase("Meantime")){
            rbMomentMeantime.setSelected(true);
        }else if(moment.equalsIgnoreCase("After")){
            rbMomentAfter.setSelected(true);
        }
    }

    /** <p>Get the moment.<br />Obtient le moment.</p> */
    private String getMoment(){
        if(rbMomentBefore.isSelected()==true){
            return "Before";
        }else if(rbMomentAfter.isSelected()==true){
            return "After";
        }else{
            return "Meantime";
        }
    }

    /** <p>Set the layers.<br />Définit les couches.</p> */
    private void setNbLayers(String nbLayers){
        //TODO
    }

    /** <p>Get the layers.<br />Obtient les couches.</p> */
    private String getNbLayers(){
        return layersTable.getRowCount()+"";
    }
    
    /** <p>Set the first layer.<br />Définit la première couche.</p> */
    private void setFirstLayer(String firstLayer){
            tfFirstLayer.setText(firstLayer);
    }

    /** <p>Get the first layer.<br />Obtient la première couche.</p> */
    private String getFirstLayer(){
        return tfFirstLayer.getText();
    }

    /** <p>Set the style.<br />Définit le style.</p> */
    private void setStyles(String styles){
        if(styles.isEmpty()==false){
            java.util.List<AssStyle> ast = AssStyle.unlinkAssStyles(styles);
            for (AssStyle as : ast){
                dlm.addElement(as);
            }
        }
    }

    /** <p>Get the style.<br />Obtient le style.</p> */
    private String getStyles(){
        String styles = "";
        System.out.print(dlm.toArray());
        java.util.List<AssStyle> list  = new java.util.ArrayList<AssStyle>();
        for (Object o : dlm.toArray()){
            if (o instanceof AssStyle){
                list.add((AssStyle)o);
            }
        }
        styles = AssStyle.linkAssStyles(list);
        return styles;
    }

    /** <p>Set the time.<br />Définit le temps.</p> */
    private void setTime(String mstime){
        tfMomentTime.setText(mstime);
    }

    /** <p>Get the time.<br />Obtient le temps.</p> */
    private String getTime(){
        return tfMomentTime.getText();
    }

    /** <p>Set the version.<br />Définit la version.</p> */
    private void setVersion(String version){

    }

    /** <p>Get the version.<br />Obtient la version.</p> */
    private String getVersion(){
        String version = "";

        return version;
    }

    /** <p>Set the functions collection.<br />
     * Définit la collection de fonctions.</p> */
    public void setFunctionsCollection(FunctionsCollection fc){
        funcc = fc;
        for(int i=0;i<funcc.getSize();i++){
            myFunction mf = new myFunction(
                    funcc.getMembers()[i].getPluginName(),
                    funcc.getMembers()[i].getDisplayName(),true);
            dcmFunctions.addElement(mf);
            System.out.println(funcc.getMembers()[i].getPluginName());
        }
    }

    /** <p>Get the functions collection.<br />
     * Obtient la collection de fonctions.</p> */
    public FunctionsCollection getFunctionsCollection(){
        return funcc;
    }
    
    /** <p>Get the OK button state.<br />Obtient l'état du bouton OK.</p> */
    private boolean getOKButtonState(){
        if(dtmodel.getRowCount()>0 && tfName.getText().isEmpty()==false){
            Ok_Button.setEnabled(true);
            return true;
        }else{
            Ok_Button.setEnabled(false);
            return false;
        }        
    }
    
    /** <p>Puts a text to tfFocused.<br />
     * Envoie un texte à tfFocused.</p> */
    private void putTextToRubyEditor(String text){
        try{
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart)+text+s.substring(sEnd));
        }catch(Exception exc){}
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Commons methods ">

    /** <p>Cut.<br />Coupe.</p> */
    private void cut(){
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(tfFocused.getSelectedText());
            String s = tfFocused.getText();
            int sStart = tfFocused.getSelectionStart();
            int sEnd = tfFocused.getSelectionEnd();
            tfFocused.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }

    /** <p>Copy.<br />Copie.</p> */
    private void copy(){
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(tfFocused.getSelectedText());
        }catch(Exception exc){/*no selected text or another thing*/}
    }

    /** <p>Paste.<br />Colle.</p> */
    private void paste(){
        try{
            Clipboard cb = new Clipboard();
            String s = tfFocused.getText();
            int sStart = tfFocused.getSelectionStart();
            int sEnd = tfFocused.getSelectionEnd();
            tfFocused.setText(s.substring(0, sStart)+cb.CPaste()+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }

    /** <p>Delete.<br />Supprime.</p> */
    private void delete(){
        try{
            String s = tfFocused.getText();
            int sStart = tfFocused.getSelectionStart();
            int sEnd = tfFocused.getSelectionEnd();
            tfFocused.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }

    /** <p>Select all.<br />Sélectionne tout.</p> */
    private void selectAll(){
        tfFocused.setSelectionStart(0);
        tfFocused.setSelectionEnd(tfFocused.getText().length());
    }

    /** <p>Clear all.<br />Efface tout.</p> */
    private void clearAll(){
        tfFocused.setText("");
    }

    // </editor-fold>

    /** <p>Puts a text to tfFocused.<br />
     * Envoie un texte à tfFocused.</p> */
    private void putTextToFocused(String text){
        try{
            String s = tfFocused.getText();
            int sStart = tfFocused.getSelectionStart();
            int sEnd = tfFocused.getSelectionEnd();
            tfFocused.setText(s.substring(0, sStart)+text+s.substring(sEnd));
        }catch(Exception exc){
            
        }
    }

    /** <pSet the drawing path.><br />Définit le chemin du dessin.</p> */
    public void setDrawingPath(String path){
        drawingPath = path;
    }
    
    /** <pSet the drawings path.><br />Définit le chemin des dessins.</p> */
    public void setDrawingsPath(String path){
        drawingsPath = path;
    }

    /** <p>Get the ass style list of this effects or
     * return a null value if there isn't any style.<br />
     * Obtient une lise de styles ass ou null.</p> */
    public java.util.List<AssStyle> getAssStyles(){
        if(dlm.isEmpty()==false){
            java.util.List<AssStyle> asList = new java.util.ArrayList<AssStyle>();
            for(Object o : dlm.toArray()){
                if(o instanceof AssStyle){
                    asList.add((AssStyle)o);
                }
            }
        }// Else :
        return null;
    }
    
    private void updateASSTextField(JTextField tf, Highlighter h){
        Pattern p; Matcher m;
        
        //All characters in black (normalPainter)
        p = Pattern.compile(".*");
        m = p.matcher(tf.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), normalPainter);
            } catch (BadLocationException ex) {}
        }

        //All keywords in blue (keywordPainter)
        p = Pattern.compile("\\\\[a-z]+");
        m = p.matcher(tf.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), keywordPainter);
            } catch (BadLocationException ex) {}
        }

        //All numbers in magenta (numberPainter)
        p = Pattern.compile("[0-9]+");
        m = p.matcher(tf.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), numberPainter);
            } catch (BadLocationException ex) {}
        }

        //All brackets in green (symbolPainter)
        p = Pattern.compile("\\{*\\}*");
        m = p.matcher(tf.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), symbolPainter);
            } catch (BadLocationException ex) {}
        }
        
        //All hexadecimals in gray (hexaPainter)
        p = Pattern.compile("&H[A-Fa-f0-9]+");
        m = p.matcher(tf.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start()+2, m.end(), hexaPainter);
            } catch (BadLocationException ex) {}
        }
        
        //All user variables in red (uvarPainter)
        p = Pattern.compile("\\$[a-z]+");
        m = p.matcher(tf.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), uvarPainter);
            } catch (BadLocationException ex) {}
        }
        
        //All local variables highlighted in yellow (lvarPainter)
        p = Pattern.compile("%[A-Za-z]+");
        m = p.matcher(tf.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), lvarPainter);
            } catch (BadLocationException ex) {}
        }
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                XmlPresetDialog dialog = new XmlPresetDialog(new javax.swing.JFrame(), true);
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

    /** <p>This class is the object used by functions combobox and its renderer.<br />
     * Cette classe est l'objet utilisé par les fonctions du combobox et son renderer.</p> */
    public class myFunction {
        private String myfunction;
        private String mydisplay;
        private Color fore;
        private Color back;

        public myFunction(String function, String display, Color fore, Color back){
            this.myfunction=function;
            this.mydisplay=display;
            this.fore=fore;
            this.back=back;
        }

        public myFunction(String function, String display, Color fore){
            this.myfunction=function;
            this.mydisplay=display;
            this.fore=fore;
        }

        public myFunction(String function, String display, boolean exist){
            this.myfunction=function;
            this.mydisplay=display;
            if(exist==true){
                this.fore=Color.BLACK;
                this.back=new Color(204,255,204);
            }else{
                this.fore=Color.BLACK;
                this.back=new Color(255,204,204);
            }
        }

        public Color getFColor(){
            return fore;
        }

         public Color getBColor(){
            return back;
        }

        public String getFunction(){
            return myfunction;
        }

        public String getDisplay(){
            return mydisplay;
        }

        @Override
        public String toString(){
            return myfunction;
        }

    }
    
    /** <p>This is the renderer used by functions combobox.<br />
     * Ceci est le renderer utilisé par les fonctions du combobox.</p> */
    public class MyCellRenderer extends javax.swing.JLabel
            implements javax.swing.ListCellRenderer {
            
        /** Creates a new instance of MyCellRenderer */
        public MyCellRenderer() {
            setOpaque(true);
        }

            @Override
        public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList jList, Object obj, int index, 
                    boolean isSelected, boolean cellHasFocus) {
            if (obj instanceof myFunction){
                myFunction mf = (myFunction)obj;
    //            setBackground(isSelected ? new Color(204,204,255) : mf.getBColor());
    //            setForeground(mf.getFColor());
                setBackground(mf.getBColor());
                setForeground(isSelected ? new Color(204,204,204) : mf.getFColor());
                setText(mf.getDisplay());
            }
            return this;
        }
    
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel_Button;
    private javax.swing.JButton Ok_Button;
    private javax.swing.ButtonGroup bgAlignment;
    private javax.swing.ButtonGroup bgLineMode;
    private javax.swing.ButtonGroup bgMoment;
    private javax.swing.ButtonGroup bgSentenceMode;
    private javax.swing.ButtonGroup bgTypeMode;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddLayer;
    private javax.swing.JButton btnAfterSyl;
    private javax.swing.JButton btnBeforeSyl;
    private javax.swing.JButton btnChangeLayer;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteLayer;
    private javax.swing.JButton btnEditLayer;
    private javax.swing.JButton btnInner;
    private javax.swing.JButton btnLastOver;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnOverrides;
    private javax.swing.JButton btnPreview;
    private javax.swing.JComboBox cbEncoding;
    private javax.swing.JComboBox cbFontname;
    private javax.swing.JComboBox cbFunctions;
    private javax.swing.JCheckBox cbSaveEffect;
    private javax.swing.JCheckBox cboBold;
    private javax.swing.JCheckBox cboItalic;
    private javax.swing.JCheckBox cboOpaqueBox;
    private javax.swing.JCheckBox cboStrikeOut;
    private javax.swing.JCheckBox cboUnderline;
    private javax.swing.JEditorPane epVariables;
    private javax.swing.JFileChooser fcPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable layersTable;
    private javax.swing.JLabel lblColor1;
    private javax.swing.JLabel lblColor2;
    private javax.swing.JLabel lblColor3;
    private javax.swing.JLabel lblColor4;
    private javax.swing.JLabel lblColorBorder;
    private javax.swing.JLabel lblColorKaraoke;
    private javax.swing.JLabel lblColorShadow;
    private javax.swing.JLabel lblColorText4;
    private javax.swing.JLabel lblPreview;
    private javax.swing.JList lstStyles;
    private javax.swing.JPanel panPreview;
    private javax.swing.JPopupMenu popAbout;
    private javax.swing.JPopupMenu popOverrides;
    private javax.swing.JPopupMenu popOverrides1;
    private javax.swing.JPopupMenu popStyleList;
    private javax.swing.JPopupMenu.Separator popmAboutSep1;
    private javax.swing.JMenuItem popmAlpha;
    private javax.swing.JMenuItem popmAlpha1;
    private javax.swing.JMenuItem popmClear;
    private javax.swing.JMenuItem popmClear1;
    private javax.swing.JMenuItem popmClearAll2;
    private javax.swing.JMenuItem popmCodeDef;
    private javax.swing.JMenuItem popmCodeInit;
    private javax.swing.JMenuItem popmColor;
    private javax.swing.JMenuItem popmColor1;
    private javax.swing.JMenuItem popmCopy;
    private javax.swing.JMenuItem popmCopy1;
    private javax.swing.JMenuItem popmCopy2;
    private javax.swing.JMenuItem popmCut;
    private javax.swing.JMenuItem popmCut1;
    private javax.swing.JMenuItem popmCut2;
    private javax.swing.JMenuItem popmDelSurround;
    private javax.swing.JMenuItem popmDelete;
    private javax.swing.JMenuItem popmDelete1;
    private javax.swing.JMenuItem popmDelete2;
    private javax.swing.JMenuItem popmDrawing;
    private javax.swing.JMenuItem popmDrawing1;
    private javax.swing.JMenuItem popmExport;
    private javax.swing.JMenuItem popmFloCalc;
    private javax.swing.JMenuItem popmImport;
    private javax.swing.JMenuItem popmImportFrom;
    private javax.swing.JMenu popmInsScript;
    private javax.swing.JMenuItem popmIntCalc;
    private javax.swing.JMenu popmKaraNOK;
    private javax.swing.JMenu popmKaraNOK1;
    private javax.swing.JMenu popmKaraOK;
    private javax.swing.JMenu popmKaraOK1;
    private javax.swing.JMenu popmKaraOK2;
    private javax.swing.JMenu popmKaraOK3;
    private javax.swing.JMenu popmKaraOK4;
    private javax.swing.JMenu popmKaraOK5;
    private javax.swing.JPopupMenu.Separator popmOverSep1;
    private javax.swing.JSeparator popmOverSep2;
    private javax.swing.JSeparator popmOverSep3;
    private javax.swing.JPopupMenu.Separator popmOverSep4;
    private javax.swing.JPopupMenu.Separator popmOverSep5;
    private javax.swing.JSeparator popmOverSep6;
    private javax.swing.JSeparator popmOverSep7;
    private javax.swing.JPopupMenu.Separator popmOverSep8;
    private javax.swing.JMenu popmOverrides;
    private javax.swing.JMenu popmOverrides1;
    private javax.swing.JMenuItem popmPNG;
    private javax.swing.JMenuItem popmPNG1;
    private javax.swing.JMenuItem popmPaste;
    private javax.swing.JMenuItem popmPaste1;
    private javax.swing.JMenuItem popmPaste2;
    private javax.swing.JMenuItem popmSelAll;
    private javax.swing.JMenuItem popmSelAll1;
    private javax.swing.JMenuItem popmSelAll2;
    private javax.swing.JMenuItem popmSurround;
    private javax.swing.JMenuItem popm_1a;
    private javax.swing.JMenuItem popm_1a1;
    private javax.swing.JMenuItem popm_1c;
    private javax.swing.JMenuItem popm_1c1;
    private javax.swing.JMenuItem popm_1img;
    private javax.swing.JMenuItem popm_1img1;
    private javax.swing.JMenuItem popm_1va;
    private javax.swing.JMenuItem popm_1va1;
    private javax.swing.JMenuItem popm_1vc;
    private javax.swing.JMenuItem popm_1vc1;
    private javax.swing.JMenuItem popm_2a;
    private javax.swing.JMenuItem popm_2a1;
    private javax.swing.JMenuItem popm_2c;
    private javax.swing.JMenuItem popm_2c1;
    private javax.swing.JMenuItem popm_2img;
    private javax.swing.JMenuItem popm_2img1;
    private javax.swing.JMenuItem popm_2va;
    private javax.swing.JMenuItem popm_2va1;
    private javax.swing.JMenuItem popm_2vc;
    private javax.swing.JMenuItem popm_2vc1;
    private javax.swing.JMenuItem popm_3a;
    private javax.swing.JMenuItem popm_3a1;
    private javax.swing.JMenuItem popm_3c;
    private javax.swing.JMenuItem popm_3c1;
    private javax.swing.JMenuItem popm_3img;
    private javax.swing.JMenuItem popm_3img1;
    private javax.swing.JMenuItem popm_3va;
    private javax.swing.JMenuItem popm_3va1;
    private javax.swing.JMenuItem popm_3vc;
    private javax.swing.JMenuItem popm_3vc1;
    private javax.swing.JMenuItem popm_4a;
    private javax.swing.JMenuItem popm_4a1;
    private javax.swing.JMenuItem popm_4c;
    private javax.swing.JMenuItem popm_4c1;
    private javax.swing.JMenuItem popm_4img;
    private javax.swing.JMenuItem popm_4img1;
    private javax.swing.JMenuItem popm_4va;
    private javax.swing.JMenuItem popm_4va1;
    private javax.swing.JMenuItem popm_4vc;
    private javax.swing.JMenuItem popm_4vc1;
    private javax.swing.JMenuItem popm_a;
    private javax.swing.JMenuItem popm_a1;
    private javax.swing.JMenuItem popm_alpha;
    private javax.swing.JMenuItem popm_alpha1;
    private javax.swing.JMenuItem popm_an;
    private javax.swing.JMenuItem popm_an1;
    private javax.swing.JMenuItem popm_b;
    private javax.swing.JMenuItem popm_b1;
    private javax.swing.JMenuItem popm_be;
    private javax.swing.JMenuItem popm_be1;
    private javax.swing.JMenuItem popm_blur;
    private javax.swing.JMenuItem popm_blur1;
    private javax.swing.JMenuItem popm_bord;
    private javax.swing.JMenuItem popm_bord1;
    private javax.swing.JMenuItem popm_clip;
    private javax.swing.JMenuItem popm_clip1;
    private javax.swing.JMenuItem popm_clip2;
    private javax.swing.JMenuItem popm_clip3;
    private javax.swing.JMenuItem popm_distort;
    private javax.swing.JMenuItem popm_distort1;
    private javax.swing.JMenuItem popm_fad;
    private javax.swing.JMenuItem popm_fad1;
    private javax.swing.JMenuItem popm_fade;
    private javax.swing.JMenuItem popm_fade1;
    private javax.swing.JMenuItem popm_fax;
    private javax.swing.JMenuItem popm_fax1;
    private javax.swing.JMenuItem popm_fay;
    private javax.swing.JMenuItem popm_fay1;
    private javax.swing.JMenuItem popm_fe;
    private javax.swing.JMenuItem popm_fe1;
    private javax.swing.JMenuItem popm_fn;
    private javax.swing.JMenuItem popm_fn1;
    private javax.swing.JMenuItem popm_frs;
    private javax.swing.JMenuItem popm_frs1;
    private javax.swing.JMenuItem popm_frx;
    private javax.swing.JMenuItem popm_frx1;
    private javax.swing.JMenuItem popm_fry;
    private javax.swing.JMenuItem popm_fry1;
    private javax.swing.JMenuItem popm_frz;
    private javax.swing.JMenuItem popm_frz1;
    private javax.swing.JMenuItem popm_fs;
    private javax.swing.JMenuItem popm_fs1;
    private javax.swing.JMenuItem popm_fsc;
    private javax.swing.JMenuItem popm_fsc1;
    private javax.swing.JMenuItem popm_fscx;
    private javax.swing.JMenuItem popm_fscx1;
    private javax.swing.JMenuItem popm_fscy;
    private javax.swing.JMenuItem popm_fscy1;
    private javax.swing.JMenuItem popm_fsp;
    private javax.swing.JMenuItem popm_fsp1;
    private javax.swing.JMenuItem popm_fsvp;
    private javax.swing.JMenuItem popm_fsvp1;
    private javax.swing.JMenuItem popm_i;
    private javax.swing.JMenuItem popm_i1;
    private javax.swing.JMenuItem popm_iclip;
    private javax.swing.JMenuItem popm_iclip1;
    private javax.swing.JMenuItem popm_iclip2;
    private javax.swing.JMenuItem popm_iclip3;
    private javax.swing.JMenuItem popm_jitter;
    private javax.swing.JMenuItem popm_jitter1;
    private javax.swing.JMenuItem popm_k;
    private javax.swing.JMenuItem popm_k1;
    private javax.swing.JMenuItem popm_kf;
    private javax.swing.JMenuItem popm_kf1;
    private javax.swing.JMenuItem popm_ko;
    private javax.swing.JMenuItem popm_ko1;
    private javax.swing.JMenuItem popm_md;
    private javax.swing.JMenuItem popm_md1;
    private javax.swing.JMenuItem popm_mdx;
    private javax.swing.JMenuItem popm_mdx1;
    private javax.swing.JMenuItem popm_mdy;
    private javax.swing.JMenuItem popm_mdy1;
    private javax.swing.JMenuItem popm_mdz;
    private javax.swing.JMenuItem popm_mdz1;
    private javax.swing.JMenuItem popm_move;
    private javax.swing.JMenuItem popm_move1;
    private javax.swing.JMenuItem popm_mover;
    private javax.swing.JMenuItem popm_mover1;
    private javax.swing.JMenuItem popm_moves3;
    private javax.swing.JMenuItem popm_moves4;
    private javax.swing.JMenuItem popm_moves5;
    private javax.swing.JMenuItem popm_moves6;
    private javax.swing.JMenuItem popm_movevc;
    private javax.swing.JMenuItem popm_movevc1;
    private javax.swing.JMenuItem popm_movevc2;
    private javax.swing.JMenuItem popm_movevc3;
    private javax.swing.JMenuItem popm_org;
    private javax.swing.JMenuItem popm_org1;
    private javax.swing.JMenuItem popm_pos;
    private javax.swing.JMenuItem popm_pos1;
    private javax.swing.JMenuItem popm_q;
    private javax.swing.JMenuItem popm_q1;
    private javax.swing.JMenuItem popm_reset;
    private javax.swing.JMenuItem popm_reset1;
    private javax.swing.JMenuItem popm_s;
    private javax.swing.JMenuItem popm_s1;
    private javax.swing.JMenuItem popm_shad;
    private javax.swing.JMenuItem popm_shad1;
    private javax.swing.JMenuItem popm_t;
    private javax.swing.JMenuItem popm_t1;
    private javax.swing.JMenuItem popm_u;
    private javax.swing.JMenuItem popm_u1;
    private javax.swing.JMenuItem popm_xbord;
    private javax.swing.JMenuItem popm_xbord1;
    private javax.swing.JMenuItem popm_xshad;
    private javax.swing.JMenuItem popm_xshad1;
    private javax.swing.JMenuItem popm_ybord;
    private javax.swing.JMenuItem popm_ybord1;
    private javax.swing.JMenuItem popm_yshad;
    private javax.swing.JMenuItem popm_yshad1;
    private javax.swing.JMenuItem popm_z;
    private javax.swing.JMenuItem popm_z1;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JRadioButton rb3;
    private javax.swing.JRadioButton rb4;
    private javax.swing.JRadioButton rb5;
    private javax.swing.JRadioButton rb6;
    private javax.swing.JRadioButton rb7;
    private javax.swing.JRadioButton rb8;
    private javax.swing.JRadioButton rb9;
    private javax.swing.JRadioButton rbMomentAfter;
    private javax.swing.JRadioButton rbMomentBefore;
    private javax.swing.JRadioButton rbMomentMeantime;
    private javax.swing.JScrollPane spVariables;
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
    private javax.swing.JTextArea taHelpPlease;
    private javax.swing.JTextField tfAfter;
    private javax.swing.JTextField tfAuthor;
    private javax.swing.JTextField tfBefore;
    private javax.swing.JTextField tfCollection;
    private javax.swing.JTextField tfComments;
    private javax.swing.JTextField tfFirstLayer;
    private javax.swing.JTextField tfInnerOverrides;
    private javax.swing.JTextField tfLastOverrides;
    private javax.swing.JTextField tfMomentTime;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfOverrides;
    private javax.swing.JTextField tfPreview;
    private javax.swing.JTextField tfRotation;
    private javax.swing.JTextField tfScaleX;
    private javax.swing.JTextField tfScaleY;
    private javax.swing.JTextField tfSpacing;
    private javax.swing.JTextField tfStyleName;
    // End of variables declaration//GEN-END:variables

}
