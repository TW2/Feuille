/*
 * FontDialog.java
 *
 * Created on 26 juillet 2008, 13:21
 */

package smallboxforfansub.lib;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultListModel;

/**
 * <p>This is a dialog for the choice of font.<br />
 * C'est une boîte de dialogue pour le choix de police.</p>
 * @author The Wingate 2940
 */
public class FontDialog extends javax.swing.JDialog {

    private ButtonPressed bp;
    private Font f;
    private Font[] flist;
    private List<FontX> fxlist = new ArrayList<FontX>();
    
    private DefaultListModel lmFont;
    private DefaultListModel lmStyle;
    private DefaultListModel lmSize;
    
    private String sPlain = "Normal";
    private String sItalic = "Italic";
    private String sBold = "Bold";
    private String sBoldItalic = "Bold Italic";
    
    private Language localeLanguage = smallboxforfansub.MainFrame.getLanguage();
    
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }
    
    public enum FontStyle{
        PLAIN, BOLD, ITALIC, BOLDITALIC;
    }
    
    public enum FontType{
        TRUETYPE, OPENTYPE, POSTSCRIPT_TYPE1, SCREEN;
    }
    
    /** <p>Creates new form FontDialog.<hr />
     * Cr�e une nouvelle dialogue FontDialog.</p> */
    public FontDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(485,360);
        bp = ButtonPressed.NONE;
        
        lmFont = new DefaultListModel();
        lstFont.setModel(lmFont);
        lmStyle = new DefaultListModel();
        lstStyle.setModel(lmStyle);
        lmSize = new DefaultListModel();
        lstSize.setModel(lmSize);

        lmSize.addElement(8); lmSize.addElement(9); lmSize.addElement(10);
        lmSize.addElement(11); lmSize.addElement(12); lmSize.addElement(14);
        lmSize.addElement(16); lmSize.addElement(18); lmSize.addElement(20);
        lmSize.addElement(22); lmSize.addElement(24); lmSize.addElement(26);
        lmSize.addElement(28); lmSize.addElement(36); lmSize.addElement(48);
        lmSize.addElement(52); lmSize.addElement(64); lmSize.addElement(72);
        
        getFontXList();
        
        String lastFx = null;
        for (FontX fx : fxlist){
            if(fx.getFamily().equals(lastFx)==false){
                lmFont.addElement(fx);
            }
            // TODO - Ajouter support police verticale "@"+font.getFamily()
            lastFx = fx.getFamily();
        }
        
//        System.out.println("Taille de la liste : "+lmFont.size());
//        System.out.println("Nombre de polices au total : "+flist.length);
        
        //Force visibility
        setAlwaysOnTop(true);
        
        javax.swing.border.TitledBorder tb;
        if(localeLanguage.getValueOf("titleFDL")!=null){setTitle(localeLanguage.getValueOf("titleFDL"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        tb = (javax.swing.border.TitledBorder)panFont.getBorder();
        if(localeLanguage.getValueOf("tbdFont")!=null){tb.setTitle(localeLanguage.getValueOf("tbdFont"));}
        tb = (javax.swing.border.TitledBorder)panStyle.getBorder();
        if(localeLanguage.getValueOf("tbdStyle")!=null){tb.setTitle(localeLanguage.getValueOf("tbdStyle"));}
        tb = (javax.swing.border.TitledBorder)panSize.getBorder();
        if(localeLanguage.getValueOf("tbdSize")!=null){tb.setTitle(localeLanguage.getValueOf("tbdSize"));}
        tb = (javax.swing.border.TitledBorder)panPreview.getBorder();
        if(localeLanguage.getValueOf("tbdPreview")!=null){tb.setTitle(localeLanguage.getValueOf("tbdPreview"));}
//        setCommonFont(assfxmaker.AssFxMaker.getDefaultFont(), getContentPane().getComponents());
    }
    
    /** <p>Creates new form FontDialog with a fonts list initialization.<hr />
     * Cr�e une nouvelle dialogue FontDialog avec une liste de polices.</p> */
    public FontDialog(java.awt.Frame parent, boolean modal, Font[] flist) {
        super(parent, modal);
        initComponents();
        setSize(410,380);
        bp = ButtonPressed.NONE;

        lmSize.addElement(8); lmSize.addElement(9); lmSize.addElement(10);
        lmSize.addElement(11); lmSize.addElement(12); lmSize.addElement(14);
        lmSize.addElement(16); lmSize.addElement(18); lmSize.addElement(20);
        lmSize.addElement(22); lmSize.addElement(24); lmSize.addElement(26);
        lmSize.addElement(28); lmSize.addElement(36); lmSize.addElement(48);
        lmSize.addElement(52); lmSize.addElement(64); lmSize.addElement(72);
        
        this.flist = flist;
    }
    
    /** <p>Set the font.<br />D&finit la police.</p> */
    @Override
    public void setFont(Font f){
        this.f = f;
        tfFont.setText(f.getFamily());
        switch(f.getStyle()){
            case Font.PLAIN: tfStyle.setText(sPlain); break;
            case Font.BOLD: tfStyle.setText(sBold); break;
            case Font.ITALIC: tfStyle.setText(sItalic); break;
            case Font.BOLD+Font.ITALIC: tfStyle.setText(sBoldItalic); break;
            default : tfStyle.setText(sPlain); break;
        }
        tfSize.setText(f.getSize()+"");


    }
    
    /** <p>Get the font.<br />Obtient la police.</p> */
    @Override
    public Font getFont(){
        try{
            Font newFont = new Font(tfFont.getText(),
                getFontStyleFromText(),Integer.parseInt(tfSize.getText()));
            return newFont;
        }catch(Exception exc){
            return f;
        }
    }
    
    /** <p>Show the dialog and wait for a font choosen by user.<br />
     * Montre la dialogue et attent une police choisit par l'utilisateur.</p> */
    public Font showDialog(Font f){
        setFont(f);
        setVisible(true);
        
        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return getFont();
        }else{
            return f;
        }
    }
    
    /** <p>Search for fonts of the operating system.<br />
     * Recherche les polices pr�sente sur le syst�me.</p> */
    private void searchFonts(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        flist = ge.getAllFonts();
    }
    
    /** <p>Get the font list.<br />
     * Obtient la liste des polices.</p> */
    public Font[] getFonts(){
        return flist;
    }
    
    /** <p>Set the font list.<br />
     * D�finit la liste des polices.</p> */
    public void setFonts(Font[] flist){
        this.flist = flist;
    }
    
    /** <p>Select a font from its name.<br />
     * Sélectionne une police à partir de son nom.</p> */
    public void selectFont(String fontname){
        lmStyle.clear();
//        lmSize.clear();
        
        // Affiche la police dans la zone de texte.
        tfFont.setText(fontname);
        
        // Renseigne les styles disponibles pour cette police.
        for (FontX fx : fxlist){
            FontX fxSel = (FontX)lstFont.getSelectedValue();
            
            if(fxSel.getFamily().equals(fx.getFamily())){
                switch(fx.getFontStyle()){
                    case PLAIN:
                        if(lmStyle.contains(sPlain)==false){
                            lmStyle.addElement(sPlain);
                        }
                        break;
                    case ITALIC:
                        if(lmStyle.contains(sItalic)==false){
                            lmStyle.addElement(sItalic);
                        }
                        break;
                    case BOLD:
                        if(lmStyle.contains(sBold)==false){
                            lmStyle.addElement(sBold);
                        }
                        break;
                    case BOLDITALIC:
                        if(lmStyle.contains(sBoldItalic)==false){
                            lmStyle.addElement(sBoldItalic);
                        }
                        break;
                }
            }
        }

        // S�lectionne le premier style.
        lstStyle.setSelectedIndex(0);
        
        // Renseigne les tailles.
        // On ne met pas de taille pr�d�fini si il y a une taille fixe.
//        lmSize.addElement(8); lmSize.addElement(9); lmSize.addElement(10);
//        lmSize.addElement(11); lmSize.addElement(12); lmSize.addElement(14);
//        lmSize.addElement(16); lmSize.addElement(18); lmSize.addElement(20);
//        lmSize.addElement(22); lmSize.addElement(24); lmSize.addElement(26);
//        lmSize.addElement(28); lmSize.addElement(36); lmSize.addElement(48);
//        lmSize.addElement(52); lmSize.addElement(64); lmSize.addElement(72);

        // S�lectionne le premier style.
        if(lstSize.getSelectedValue()==null){
            lstSize.setSelectedIndex(9);
        }        
        
        // S�lectionne le premier style.
        if(lmSize.getSize()>1){
            lstStyle.setSelectedIndex(4);
        }else{
            lstStyle.setSelectedIndex(0);
        }        
        
        // Rafraichit la police de l'aper�u.
        refreshPreview();
    }
    
    /** <p>Refresh the preview of the font.<br />
     * Rafraichit la police de l'aper�u.</p> */
    public void refreshPreview(){
        FontX fx = (FontX)lstFont.getSelectedValue();
        String style = lstStyle.getSelectedValue().toString();
        float size = 22;
        if(lstSize.getSelectedValue()!=null){
            size = Float.parseFloat(lstSize.getSelectedValue().toString());
        }

        Font fpreview = fx.getFont().deriveFont(Font.PLAIN, size);
        if(style.equals(sPlain)){
            fpreview = fpreview.deriveFont(Font.PLAIN);
        }else if(style.equals(sItalic)){
            fpreview = fpreview.deriveFont(Font.ITALIC);
        }else if(style.equals(sBold)){
            fpreview = fpreview.deriveFont(Font.BOLD);
        }else if(style.equals(sBoldItalic)){
            fpreview = fpreview.deriveFont(Font.BOLD+Font.ITALIC);
        }
        
        lblPreview.setFont(fpreview);
    }
    
    /** <p>Get a fontX list.<br />Obtient une liste de policeX</p> */
    private List<FontX> getFontXList(){
        fxlist.clear();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        flist = ge.getAllFonts();
        
        for (Font font : flist){
            FontX fx = new FontX(font);
            //System.out.println(fx.getFontName() + " - " + fx.getFontStyle());
            if (fxlist.contains(fx)==false){
                fxlist.add(fx);
            }
        }
        
        return fxlist;
    }
    
    /** <p>Get the font style from the text.<br />
     * Obtient un style de police à partir du texte.</p> */
    private int getFontStyleFromText(){
        if(tfStyle.getText().equalsIgnoreCase(sPlain)){return Font.PLAIN;}
        if(tfStyle.getText().equalsIgnoreCase(sBold)){return Font.BOLD;}
        if(tfStyle.getText().equalsIgnoreCase(sItalic)){return Font.ITALIC;}
        if(tfStyle.getText().equalsIgnoreCase(sBoldItalic)){return Font.BOLD+Font.ITALIC;}
        return Font.PLAIN;//Otherwise
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
            if (c instanceof javax.swing.JPanel){
                javax.swing.JPanel pan = (javax.swing.JPanel)c;
                javax.swing.border.Border bord = pan.getBorder();
                if (bord instanceof javax.swing.border.TitledBorder){
                    javax.swing.border.TitledBorder tb = (javax.swing.border.TitledBorder)bord;
                    Font origin = tb.getTitleFont();
                    if(origin.isPlain()){
                        tb.setTitleFont(f);
                    }else{
                        tb.setTitleFont(f.deriveFont(origin.getStyle(), origin.getSize2D()));
                    }                    
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popFont = new javax.swing.JPopupMenu();
        mnuAbcd = new javax.swing.JMenuItem();
        mnuEn = new javax.swing.JMenuItem();
        mnuFr = new javax.swing.JMenuItem();
        mnuSp = new javax.swing.JMenuItem();
        Cancel_Button = new javax.swing.JButton();
        OK_Button = new javax.swing.JButton();
        panFont = new javax.swing.JPanel();
        tfFont = new javax.swing.JTextField();
        spFont = new javax.swing.JScrollPane();
        lstFont = new javax.swing.JList();
        panStyle = new javax.swing.JPanel();
        tfStyle = new javax.swing.JTextField();
        spStyle = new javax.swing.JScrollPane();
        lstStyle = new javax.swing.JList();
        panSize = new javax.swing.JPanel();
        tfSize = new javax.swing.JTextField();
        spSize = new javax.swing.JScrollPane();
        lstSize = new javax.swing.JList();
        panPreview = new javax.swing.JPanel();
        lblPreview = new javax.swing.JLabel();

        mnuAbcd.setText("ABCD abcd");
        mnuAbcd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAbcdActionPerformed(evt);
            }
        });
        popFont.add(mnuAbcd);

        mnuEn.setText("English");
        mnuEn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEnActionPerformed(evt);
            }
        });
        popFont.add(mnuEn);

        mnuFr.setText("Français");
        mnuFr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFrActionPerformed(evt);
            }
        });
        popFont.add(mnuFr);

        mnuSp.setText("Español");
        mnuSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSpActionPerformed(evt);
            }
        });
        popFont.add(mnuSp);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Choose a font...");
        getContentPane().setLayout(null);

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(Cancel_Button);
        Cancel_Button.setBounds(360, 290, 100, 23);

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });
        getContentPane().add(OK_Button);
        OK_Button.setBounds(250, 290, 100, 23);

        panFont.setBorder(javax.swing.BorderFactory.createTitledBorder("Font"));
        panFont.setLayout(null);
        panFont.add(tfFont);
        tfFont.setBounds(10, 20, 260, 30);

        lstFont.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstFont.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstFontValueChanged(evt);
            }
        });
        spFont.setViewportView(lstFont);

        panFont.add(spFont);
        spFont.setBounds(10, 60, 260, 100);

        getContentPane().add(panFont);
        panFont.setBounds(0, 0, 280, 170);

        panStyle.setBorder(javax.swing.BorderFactory.createTitledBorder("Style"));
        panStyle.setLayout(null);
        panStyle.add(tfStyle);
        tfStyle.setBounds(10, 20, 90, 30);

        lstStyle.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstStyle.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstStyleValueChanged(evt);
            }
        });
        spStyle.setViewportView(lstStyle);

        panStyle.add(spStyle);
        spStyle.setBounds(10, 60, 90, 100);

        getContentPane().add(panStyle);
        panStyle.setBounds(280, 0, 110, 170);

        panSize.setBorder(javax.swing.BorderFactory.createTitledBorder("Size"));
        panSize.setLayout(null);
        panSize.add(tfSize);
        tfSize.setBounds(10, 20, 60, 30);

        lstSize.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "10", "12", "18", "24", "36", "102" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstSize.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstSize.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstSizeValueChanged(evt);
            }
        });
        spSize.setViewportView(lstSize);

        panSize.add(spSize);
        spSize.setBounds(10, 60, 60, 100);

        getContentPane().add(panSize);
        panSize.setBounds(390, 0, 80, 170);

        panPreview.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));
        panPreview.setLayout(null);

        lblPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPreview.setText("<html>ABCDEFGHIJKLMNOPQRSTUVWXYZ<br />\nabcdefghijklmnopqrstuvwxyz<br />\n0123456789</html>");
        lblPreview.setComponentPopupMenu(popFont);
        panPreview.add(lblPreview);
        lblPreview.setBounds(10, 20, 450, 80);

        getContentPane().add(panPreview);
        panPreview.setBounds(0, 170, 470, 110);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
    bp = ButtonPressed.OK_BUTTON;
    dispose();
}//GEN-LAST:event_OK_ButtonActionPerformed

private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
    bp = ButtonPressed.CANCEL_BUTTON;
    dispose();
}//GEN-LAST:event_Cancel_ButtonActionPerformed

private void lstFontValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstFontValueChanged
    // Change the font.
    selectFont(lstFont.getSelectedValue().toString());
}//GEN-LAST:event_lstFontValueChanged

private void lstStyleValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstStyleValueChanged
    // Change the style of the font.
    try{
        tfStyle.setText(lmStyle.getElementAt(lstStyle.getSelectedIndex()).toString());
        // Rafraichit la police de l'aper�u.
        refreshPreview();
    }catch (ArrayIndexOutOfBoundsException aioobe){
        
    }
}//GEN-LAST:event_lstStyleValueChanged

private void mnuAbcdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAbcdActionPerformed
    lblPreview.setText("<html>ABCDEFGHIJKLMNOPQRSTUVWXYZ<br />" +
            "abcdefghijklmnopqrstuvwxyz<br />" +
            "0123456789</html>");
}//GEN-LAST:event_mnuAbcdActionPerformed

private void mnuEnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEnActionPerformed
    lblPreview.setText("<html>It doesn't matter...<br />" +
            "Enjoy it!</html>");
}//GEN-LAST:event_mnuEnActionPerformed

private void mnuFrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFrActionPerformed
    lblPreview.setText("<html>À cœur vaillant rien d'impossible.<br />" +
            "Il n'y a que la vérité qui blesse.</html>");
}//GEN-LAST:event_mnuFrActionPerformed

private void mnuSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSpActionPerformed
    lblPreview.setText("<html>Pensar en la mañana.<br />" +
            "¿Verdad que es bonita?</html>");
}//GEN-LAST:event_mnuSpActionPerformed

private void lstSizeValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstSizeValueChanged
    // Change size of the font.
    tfSize.setText(lmSize.getElementAt(lstSize.getSelectedIndex()).toString());
    refreshPreview();
}//GEN-LAST:event_lstSizeValueChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FontDialog dialog = new FontDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel lblPreview;
    private javax.swing.JList lstFont;
    private javax.swing.JList lstSize;
    private javax.swing.JList lstStyle;
    private javax.swing.JMenuItem mnuAbcd;
    private javax.swing.JMenuItem mnuEn;
    private javax.swing.JMenuItem mnuFr;
    private javax.swing.JMenuItem mnuSp;
    private javax.swing.JPanel panFont;
    private javax.swing.JPanel panPreview;
    private javax.swing.JPanel panSize;
    private javax.swing.JPanel panStyle;
    private javax.swing.JPopupMenu popFont;
    private javax.swing.JScrollPane spFont;
    private javax.swing.JScrollPane spSize;
    private javax.swing.JScrollPane spStyle;
    private javax.swing.JTextField tfFont;
    private javax.swing.JTextField tfSize;
    private javax.swing.JTextField tfStyle;
    // End of variables declaration//GEN-END:variables

    /** Cette classe facilite la lecture des fontes. */
    public class FontX extends Object{
        
        Font f = null;
        FontStyle fs = FontStyle.PLAIN;
        FontType ft = FontType.SCREEN;
        
        /** Initialise une police et un style normal. */
        public FontX(Font f){
            this.f = f;
            initFontStyle(f);
            
        }
        
        /** Initialise une police et son style. */
        public FontX(Font f, FontStyle fs){
            this.f = f;
            this.fs = fs;
            
        }
        
        /** D�finit une police. */
        public void setFont(Font f){
            this.f = f;
        }
        
        /** Obtient une police. */
        public Font getFont(){
            return f;
        }
        
        /** Obtient la famille d'une police. */
        public String getFamily(){
            return f.getFamily();
        }
        
        /** Obtient le nom d'une police. */
        public String getFontName(){
            return f.getFontName(Locale.US);
        }
        
        /** Obtient le nom PostScript d'une police. */
        public String getPSName(){
            return f.getPSName();
        }
        
        /** Obtient le nom complet d'une police. */
        public String getName(){
            return f.getName();
        }
        
        /** Obtient le nom complet d'une police. */
        @Override
        public String toString(){
            return getFamily();
        }
        
        /** D�finit un style. */
        public void setFontStyle(FontStyle fs){
            this.fs = fs;
        }
        
        /** D�finit le style. */
        private void initFontStyle(Font font){
            if(font.getFontName(Locale.US)
                    .toLowerCase().endsWith("bold italic")
                    && font.getFontName(Locale.US)
                    .length() > "bold italic".length()){
                fs = FontStyle.BOLDITALIC;
            }else if(font.getFontName(Locale.US)
                    .toLowerCase().endsWith("bold-italic")
                    && font.getFontName(Locale.US)
                    .length() > "bold-italic".length()){
                fs = FontStyle.BOLDITALIC;
            }else if(font.getFontName(Locale.US)
                    .toLowerCase().endsWith("bolditalic")
                    && font.getFontName(Locale.US)
                    .length() > "bolditalic".length()){
                fs = FontStyle.BOLDITALIC;
            }else if(font.getFontName(Locale.US)
                    .toLowerCase().endsWith("italic")
                    && font.getFontName(Locale.US)
                    .length() > "italic".length()){
                fs = FontStyle.ITALIC;
            }else if(font.getFontName(Locale.US)
                    .toLowerCase().endsWith("bold")
                    && font.getFontName(Locale.US)
                    .length() > "bold".length()){
                fs = FontStyle.BOLD;
            }else{
                fs = FontStyle.PLAIN;
            }
        }
        
        /** Obtient un style. */
        public FontStyle getFontStyle(){
            return fs;
        }
        
        /** Obtient un type. */
        public FontType getFontType(){
            return ft;
        }
        
    }
    
}