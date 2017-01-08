/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.codeeditor;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import feuille.codeeditor.lib.AutoCompletion;
import feuille.karaoke.KaraokePanel;
import feuille.karaoke.dialog.AssAlphaDialog;
import feuille.karaoke.dialog.AssColorDialog;
import feuille.karaoke.dialog.SnippetDialog;
import feuille.filter.PngFilter;
import feuille.filter.PythonFilter;
import feuille.filter.RubyFilter;
import feuille.karaoke.lib.AssIO;
import feuille.karaoke.lib.Clipboard;
import feuille.karaoke.lib.FxObject;
import feuille.karaoke.lib.ImagePreview;
import feuille.lib.Language;
import feuille.scripting.ScriptPlugin;

/**
 *
 * @author The Wingate 2940
 */
public class CodeEditorPanel extends javax.swing.JPanel {
    
    private ScriptPlugin splug;
    private String docs;
    private Frame frame;
    private Language localeLanguage;
    
    private AutoCompletion acp;

    /**
     * Creates new form CodeEditorPanel
     */
    public CodeEditorPanel(String docs, Frame frame, Language lang) {
        initComponents();
        this.docs = docs;
        this.frame = frame;
        localeLanguage = lang;
        setLanguageAndConfigure(lang);
        
        // Setting up the scripting object (epScripting) to work with
        // the opensource project JSyntaxPane - see web site :
        // http://code.google.com/p/jsyntaxpane/
        jsyntaxpane.DefaultSyntaxKit.initKit();
        epScripting.setContentType("text/ruby");
        epScripting.setComponentPopupMenu(popCode);
        acp = new AutoCompletion();
        jList1.setModel(acp);
    }
    
    public void setScriptPlugin(ScriptPlugin splug){
        this.splug = splug;
    }
    
    public static void selectRuby(){
        tbRuby.setSelected(true);
    }
    
    public static void selectPython(){
        tbPython.setSelected(true);
    }
    
    public static JEditorPane getCodeEditor(){
        return epScripting;
    }
    
    public JInternalFrame getCode(){
        return ifrCode;
    }
    
    /** <p>Add a piece of text to the ruby editor.<br />
     * Ajoute un bout de texte dans l'éditeur ruby.</p> */
    private void putTextToCodeEditor(String text){
        try{
            int sStart = epScripting.getSelectionStart();
            int sEnd = epScripting.getSelectionEnd();
            String firstText = epScripting.getDocument().getText(0, sStart);
            String lastText = epScripting.getDocument().getText(sEnd, epScripting.getDocument().getLength()-sEnd);
            epScripting.setText(firstText+text+lastText);
        }catch(BadLocationException ble){
            System.out.println(ble.getMessage());
        }
    }
    
    private void setLanguageAndConfigure(Language lang){
        if(lang!=null){localeLanguage = lang;}
        if(localeLanguage.getValueOf("popmCut")!=null){
            btnCodeCut.setToolTipText(localeLanguage.getValueOf("popmCut"));
            popCodeCut.setText(localeLanguage.getValueOf("popmCut"));}
        if(localeLanguage.getValueOf("popmCopy")!=null){
            btnCodeCopy.setToolTipText(localeLanguage.getValueOf("popmCopy"));
            popCodeCopy.setText(localeLanguage.getValueOf("popmCopy"));}
        if(localeLanguage.getValueOf("popmPaste")!=null){
            btnCodePaste.setToolTipText(localeLanguage.getValueOf("popmPaste"));
            popCodePaste.setText(localeLanguage.getValueOf("popmPaste"));}
        if(localeLanguage.getValueOf("popmDelete")!=null){
            popCodeDelete.setText(localeLanguage.getValueOf("popmDelete"));}
        if(localeLanguage.getValueOf("popmRfReset")!=null){
            btnCodeReload.setToolTipText(localeLanguage.getValueOf("popmRfReset"));}
        if(localeLanguage.getValueOf("toolOpen")!=null){
            btnCodeOpen.setToolTipText(localeLanguage.getValueOf("toolOpen"));}
        if(localeLanguage.getValueOf("toolNew")!=null){
            btnCodeNew.setToolTipText(localeLanguage.getValueOf("toolNew"));}
        if(localeLanguage.getValueOf("toolSave")!=null){
            btnCodeSave.setToolTipText(localeLanguage.getValueOf("toolSave"));}
        if(localeLanguage.getValueOf("popmColor")!=null){
            popCodeColor.setText(localeLanguage.getValueOf("popmColor"));}
        if(localeLanguage.getValueOf("popmAlpha")!=null){
            popCodeAlpha.setText(localeLanguage.getValueOf("popmAlpha"));}
        if(localeLanguage.getValueOf("popmInsOver")!=null){
            popCodeOver.setText(localeLanguage.getValueOf("popmInsOver"));}
        if(localeLanguage.getValueOf("popmForAni")!=null){
            popCodeAni1.setText(localeLanguage.getValueOf("popmForAni"));
            popCodeAni2.setText(localeLanguage.getValueOf("popmForAni"));
            popCodeAni3.setText(localeLanguage.getValueOf("popmForAni"));}
        if(localeLanguage.getValueOf("popmForConf")!=null){
            popCodeConf.setText(localeLanguage.getValueOf("popmForConf"));}
        if(localeLanguage.getValueOf("popm_b")!=null){
            popCode_b.setText(localeLanguage.getValueOf("popm_b"));}
        if(localeLanguage.getValueOf("popm_i")!=null){
            popCode_i.setText(localeLanguage.getValueOf("popm_i"));}
        if(localeLanguage.getValueOf("popm_u")!=null){
            popCode_u.setText(localeLanguage.getValueOf("popm_u"));}
        if(localeLanguage.getValueOf("popm_s")!=null){
            popCode_s.setText(localeLanguage.getValueOf("popm_s"));}
        if(localeLanguage.getValueOf("popm_bord")!=null){
            popCode_bord.setText(localeLanguage.getValueOf("popm_bord"));}
        if(localeLanguage.getValueOf("popm_shad")!=null){
            popCode_shad.setText(localeLanguage.getValueOf("popm_shad"));}
        if(localeLanguage.getValueOf("popm_be")!=null){
            popCode_be.setText(localeLanguage.getValueOf("popm_be"));}
        if(localeLanguage.getValueOf("popm_blur")!=null){
            popCode_blur.setText(localeLanguage.getValueOf("popm_blur"));}
        if(localeLanguage.getValueOf("popm_fs")!=null){
            popCode_fs.setText(localeLanguage.getValueOf("popm_fs"));}
        if(localeLanguage.getValueOf("popm_fscx")!=null){
            popCode_fscx.setText(localeLanguage.getValueOf("popm_fscx"));}
        if(localeLanguage.getValueOf("popm_fscy")!=null){
            popCode_fscy.setText(localeLanguage.getValueOf("popm_fscy"));}
        if(localeLanguage.getValueOf("popm_fsp")!=null){
            popCode_fsp.setText(localeLanguage.getValueOf("popm_fsp"));}
        if(localeLanguage.getValueOf("popm_frx")!=null){
            popCode_frx.setText(localeLanguage.getValueOf("popm_frx"));}
        if(localeLanguage.getValueOf("popm_fry")!=null){
            popCode_fry.setText(localeLanguage.getValueOf("popm_fry"));}
        if(localeLanguage.getValueOf("popm_frz")!=null){
            popCode_frz.setText(localeLanguage.getValueOf("popm_frz"));}
        if(localeLanguage.getValueOf("popm_1c")!=null){
            popCode_1c.setText(localeLanguage.getValueOf("popm_1c"));}
        if(localeLanguage.getValueOf("popm_2c")!=null){
            popCode_2c.setText(localeLanguage.getValueOf("popm_2c"));}
        if(localeLanguage.getValueOf("popm_3c")!=null){
            popCode_3c.setText(localeLanguage.getValueOf("popm_3c"));}
        if(localeLanguage.getValueOf("popm_4c")!=null){
            popCode_4c.setText(localeLanguage.getValueOf("popm_4c"));}
        if(localeLanguage.getValueOf("popm_alpha")!=null){
            popCode_alpha.setText(localeLanguage.getValueOf("popm_alpha"));}
        if(localeLanguage.getValueOf("popm_1a")!=null){
            popCode_1a.setText(localeLanguage.getValueOf("popm_1a"));}
        if(localeLanguage.getValueOf("popm_2a")!=null){
            popCode_2a.setText(localeLanguage.getValueOf("popm_2a"));}
        if(localeLanguage.getValueOf("popm_3a")!=null){
            popCode_3a.setText(localeLanguage.getValueOf("popm_3a"));}
        if(localeLanguage.getValueOf("popm_4a")!=null){
            popCode_4a.setText(localeLanguage.getValueOf("popm_4a"));}
        if(localeLanguage.getValueOf("popm_k")!=null){
            popCode_k.setText(localeLanguage.getValueOf("popm_k"));}
        if(localeLanguage.getValueOf("popm_kf")!=null){
            popCode_kf.setText(localeLanguage.getValueOf("popm_kf"));}
        if(localeLanguage.getValueOf("popm_ko")!=null){
            popCode_ko.setText(localeLanguage.getValueOf("popm_ko"));}
        if(localeLanguage.getValueOf("popm_t")!=null){
            popCode_t.setText(localeLanguage.getValueOf("popm_t"));}
        if(localeLanguage.getValueOf("popm_r")!=null){
            popCode_reset.setText(localeLanguage.getValueOf("popm_r"));}
        if(localeLanguage.getValueOf("popm_fn")!=null){
            popCode_fn.setText(localeLanguage.getValueOf("popm_fn"));}
        if(localeLanguage.getValueOf("popm_fe")!=null){
            popCode_fe.setText(localeLanguage.getValueOf("popm_fe"));}
        if(localeLanguage.getValueOf("popm_q")!=null){
            popCode_q.setText(localeLanguage.getValueOf("popm_q"));}
        if(localeLanguage.getValueOf("popm_a")!=null){
            popCode_a.setText(localeLanguage.getValueOf("popm_a"));}
        if(localeLanguage.getValueOf("popm_an")!=null){
            popCode_an.setText(localeLanguage.getValueOf("popm_an"));}
        if(localeLanguage.getValueOf("popm_pos")!=null){
            popCode_pos.setText(localeLanguage.getValueOf("popm_pos"));}
        if(localeLanguage.getValueOf("popm_move")!=null){
            popCode_move.setText(localeLanguage.getValueOf("popm_move"));}
        if(localeLanguage.getValueOf("popm_org")!=null){
            popCode_org.setText(localeLanguage.getValueOf("popm_org"));}
        if(localeLanguage.getValueOf("popm_fad")!=null){
            popCode_fad.setText(localeLanguage.getValueOf("popm_fad"));}
        if(localeLanguage.getValueOf("popm_fade")!=null){
            popCode_fade.setText(localeLanguage.getValueOf("popm_fade"));}
        if(localeLanguage.getValueOf("popm_clip")!=null){
            popCode_clip.setText(localeLanguage.getValueOf("popm_clip"));}
        if(localeLanguage.getValueOf("popm_clip2")!=null){
            popCode_clip2.setText(localeLanguage.getValueOf("popm_clip2"));}
        if(localeLanguage.getValueOf("popm_xbord")!=null){
            popCode_xbord.setText(localeLanguage.getValueOf("popm_xbord"));}
        if(localeLanguage.getValueOf("popm_ybord")!=null){
            popCode_ybord.setText(localeLanguage.getValueOf("popm_ybord"));}
        if(localeLanguage.getValueOf("popm_xshad")!=null){
            popCode_xshad.setText(localeLanguage.getValueOf("popm_xshad"));}
        if(localeLanguage.getValueOf("popm_yshad")!=null){
            popCode_yshad.setText(localeLanguage.getValueOf("popm_yshad"));}
        if(localeLanguage.getValueOf("popm_fax")!=null){
            popCode_fax.setText(localeLanguage.getValueOf("popm_fax"));}
        if(localeLanguage.getValueOf("popm_fay")!=null){
            popCode_fay.setText(localeLanguage.getValueOf("popm_fay"));}
        if(localeLanguage.getValueOf("popm_iclip")!=null){
            popCode_iclip.setText(localeLanguage.getValueOf("popm_iclip"));}
        if(localeLanguage.getValueOf("popm_fsc")!=null){
            popCode_fsc.setText(localeLanguage.getValueOf("popm_fsc"));}
        if(localeLanguage.getValueOf("popm_fsvp")!=null){
            popCode_fsvp.setText(localeLanguage.getValueOf("popm_fsvp"));}
        if(localeLanguage.getValueOf("popm_frs")!=null){
            popCode_frs.setText(localeLanguage.getValueOf("popm_frs"));}
        if(localeLanguage.getValueOf("popm_z")!=null){
            popCode_z.setText(localeLanguage.getValueOf("popm_z"));}
        if(localeLanguage.getValueOf("popm_distort")!=null){
            popCode_distort.setText(localeLanguage.getValueOf("popm_distort"));}
        if(localeLanguage.getValueOf("popm_md")!=null){
            popCode_md.setText(localeLanguage.getValueOf("popm_md"));}
        if(localeLanguage.getValueOf("popm_mdx")!=null){
            popCode_mdx.setText(localeLanguage.getValueOf("popm_mdx"));}
        if(localeLanguage.getValueOf("popm_mdy")!=null){
            popCode_mdy.setText(localeLanguage.getValueOf("popm_mdy"));}
        if(localeLanguage.getValueOf("popm_mdz")!=null){
            popCode_mdz.setText(localeLanguage.getValueOf("popm_mdz"));}
        if(localeLanguage.getValueOf("popm_1vc")!=null){
            popCode_1vc.setText(localeLanguage.getValueOf("popm_1vc"));}
        if(localeLanguage.getValueOf("popm_2vc")!=null){
            popCode_2vc.setText(localeLanguage.getValueOf("popm_2vc"));}
        if(localeLanguage.getValueOf("popm_3vc")!=null){
            popCode_3vc.setText(localeLanguage.getValueOf("popm_3vc"));}
        if(localeLanguage.getValueOf("popm_4vc")!=null){
            popCode_4vc.setText(localeLanguage.getValueOf("popm_4vc"));}
        if(localeLanguage.getValueOf("popm_1va")!=null){
            popCode_1va.setText(localeLanguage.getValueOf("popm_1va"));}
        if(localeLanguage.getValueOf("popm_2va")!=null){
            popCode_2va.setText(localeLanguage.getValueOf("popm_2va"));}
        if(localeLanguage.getValueOf("popm_3va")!=null){
            popCode_3va.setText(localeLanguage.getValueOf("popm_3va"));}
        if(localeLanguage.getValueOf("popm_4va")!=null){
            popCode_4va.setText(localeLanguage.getValueOf("popm_4va"));}
        if(localeLanguage.getValueOf("popm_1img")!=null){
            popCode_1img.setText(localeLanguage.getValueOf("popm_1img"));}
        if(localeLanguage.getValueOf("popm_2img")!=null){
            popCode_2img.setText(localeLanguage.getValueOf("popm_2img"));}
        if(localeLanguage.getValueOf("popm_3img")!=null){
            popCode_3img.setText(localeLanguage.getValueOf("popm_3img"));}
        if(localeLanguage.getValueOf("popm_4img")!=null){
            popCode_4img.setText(localeLanguage.getValueOf("popm_4img"));}
        if(localeLanguage.getValueOf("popm_jitter")!=null){
            popCode_jitter.setText(localeLanguage.getValueOf("popm_jitter"));}
        if(localeLanguage.getValueOf("popm_iclip")!=null){
            popCode_iclip2.setText(localeLanguage.getValueOf("popm_iclip"));}
        if(localeLanguage.getValueOf("popm_mover")!=null){
            popCode_mover.setText(localeLanguage.getValueOf("popm_mover"));}
        if(localeLanguage.getValueOf("popm_moves3")!=null){
            popCode_moves3.setText(localeLanguage.getValueOf("popm_moves3"));}
        if(localeLanguage.getValueOf("popm_moves4")!=null){
            popCode_moves4.setText(localeLanguage.getValueOf("popm_moves4"));}
        if(localeLanguage.getValueOf("popm_movevc")!=null){
            popCode_movevc.setText(localeLanguage.getValueOf("popm_movevc"));}
        if(localeLanguage.getValueOf("popm_movevc")!=null){
            popCode_movevc2.setText(localeLanguage.getValueOf("popm_movevc"));}
        if(localeLanguage.getValueOf("popSkeleton")!=null){
            popCodeInsSke.setText(localeLanguage.getValueOf("popSkeleton"));}
        if(localeLanguage.getValueOf("popCodePNG")!=null){
            popCodePNG.setText(localeLanguage.getValueOf("popCodePNG"));}
        if(localeLanguage.getValueOf("popCodeSnippet")!=null){
            popCodeInsSni.setText(localeLanguage.getValueOf("popCodeSnippet"));}
        if(localeLanguage.getValueOf("toolTbRuby")!=null){
            tbRuby.setToolTipText(localeLanguage.getValueOf("toolTbRuby"));}
        if(localeLanguage.getValueOf("toolTbPython")!=null){
            tbPython.setToolTipText(localeLanguage.getValueOf("toolTbPython"));} 
        if(localeLanguage.getValueOf("ifrCodeEditor")!=null){
            ifrCode.setTitle(localeLanguage.getValueOf("ifrCodeEditor"));} 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgCode = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        popCode = new javax.swing.JPopupMenu();
        popCodeCut = new javax.swing.JMenuItem();
        popCodeCopy = new javax.swing.JMenuItem();
        popCodePaste = new javax.swing.JMenuItem();
        popCodeDelete = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        popCodeColor = new javax.swing.JMenuItem();
        popCodeAlpha = new javax.swing.JMenuItem();
        popCodePNG = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        popCodeOver = new javax.swing.JMenu();
        popCodeAni1 = new javax.swing.JMenu();
        popCode_b = new javax.swing.JMenuItem();
        popCode_i = new javax.swing.JMenuItem();
        popCode_u = new javax.swing.JMenuItem();
        popCode_s = new javax.swing.JMenuItem();
        popCode_bord = new javax.swing.JMenuItem();
        popCode_shad = new javax.swing.JMenuItem();
        popCode_be = new javax.swing.JMenuItem();
        popCode_fs = new javax.swing.JMenuItem();
        popCode_fscx = new javax.swing.JMenuItem();
        popCode_fscy = new javax.swing.JMenuItem();
        popCode_fsp = new javax.swing.JMenuItem();
        popCode_frx = new javax.swing.JMenuItem();
        popCode_fry = new javax.swing.JMenuItem();
        popCode_frz = new javax.swing.JMenuItem();
        popCode_1c = new javax.swing.JMenuItem();
        popCode_2c = new javax.swing.JMenuItem();
        popCode_3c = new javax.swing.JMenuItem();
        popCode_4c = new javax.swing.JMenuItem();
        popCode_alpha = new javax.swing.JMenuItem();
        popCode_1a = new javax.swing.JMenuItem();
        popCode_2a = new javax.swing.JMenuItem();
        popCode_3a = new javax.swing.JMenuItem();
        popCode_4a = new javax.swing.JMenuItem();
        popCode_clip = new javax.swing.JMenuItem();
        popCodeAni2 = new javax.swing.JMenu();
        popCode_xbord = new javax.swing.JMenuItem();
        popCode_ybord = new javax.swing.JMenuItem();
        popCode_xshad = new javax.swing.JMenuItem();
        popCode_yshad = new javax.swing.JMenuItem();
        popCode_blur = new javax.swing.JMenuItem();
        popCode_fax = new javax.swing.JMenuItem();
        popCode_fay = new javax.swing.JMenuItem();
        popCode_iclip = new javax.swing.JMenuItem();
        popCodeAni3 = new javax.swing.JMenu();
        popCode_fsc = new javax.swing.JMenuItem();
        popCode_fsvp = new javax.swing.JMenuItem();
        popCode_frs = new javax.swing.JMenuItem();
        popCode_z = new javax.swing.JMenuItem();
        popCode_distort = new javax.swing.JMenuItem();
        popCode_md = new javax.swing.JMenuItem();
        popCode_mdx = new javax.swing.JMenuItem();
        popCode_mdy = new javax.swing.JMenuItem();
        popCode_mdz = new javax.swing.JMenuItem();
        popCode_1vc = new javax.swing.JMenuItem();
        popCode_2vc = new javax.swing.JMenuItem();
        popCode_3vc = new javax.swing.JMenuItem();
        popCode_4vc = new javax.swing.JMenuItem();
        popCode_1va = new javax.swing.JMenuItem();
        popCode_2va = new javax.swing.JMenuItem();
        popCode_3va = new javax.swing.JMenuItem();
        popCode_4va = new javax.swing.JMenuItem();
        popCode_1img = new javax.swing.JMenuItem();
        popCode_2img = new javax.swing.JMenuItem();
        popCode_3img = new javax.swing.JMenuItem();
        popCode_4img = new javax.swing.JMenuItem();
        popCode_jitter = new javax.swing.JMenuItem();
        popCodeConf = new javax.swing.JMenu();
        popCode_fn = new javax.swing.JMenuItem();
        popCode_fe = new javax.swing.JMenuItem();
        popCode_q = new javax.swing.JMenuItem();
        popCode_a = new javax.swing.JMenuItem();
        popCode_an = new javax.swing.JMenuItem();
        popCode_pos = new javax.swing.JMenuItem();
        popCode_move = new javax.swing.JMenuItem();
        popCode_org = new javax.swing.JMenuItem();
        popCode_fad = new javax.swing.JMenuItem();
        popCode_fade = new javax.swing.JMenuItem();
        popCode_clip2 = new javax.swing.JMenuItem();
        popCode_iclip2 = new javax.swing.JMenuItem();
        popCode_mover = new javax.swing.JMenuItem();
        popCode_moves3 = new javax.swing.JMenuItem();
        popCode_moves4 = new javax.swing.JMenuItem();
        popCode_movevc = new javax.swing.JMenuItem();
        popCode_movevc2 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        popCode_k = new javax.swing.JMenuItem();
        popCode_kf = new javax.swing.JMenuItem();
        popCode_ko = new javax.swing.JMenuItem();
        popCode_t = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        popCode_reset = new javax.swing.JMenuItem();
        popCodeInsSke = new javax.swing.JMenuItem();
        popCodeInsSni = new javax.swing.JMenuItem();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        ifrCode = new javax.swing.JInternalFrame();
        jToolBar1 = new javax.swing.JToolBar();
        tbRuby = new javax.swing.JToggleButton();
        tbPython = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnCodeNew = new javax.swing.JButton();
        btnCodeOpen = new javax.swing.JButton();
        btnCodeSave = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnCodeReload = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnCodeCut = new javax.swing.JButton();
        btnCodeCopy = new javax.swing.JButton();
        btnCodePaste = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        epScripting = new javax.swing.JEditorPane();

        popCodeCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editcut.png"))); // NOI18N
        popCodeCut.setText("Cut");
        popCodeCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodeCutActionPerformed(evt);
            }
        });
        popCode.add(popCodeCut);

        popCodeCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        popCodeCopy.setText("Copy");
        popCodeCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodeCopyActionPerformed(evt);
            }
        });
        popCode.add(popCodeCopy);

        popCodePaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editpaste.png"))); // NOI18N
        popCodePaste.setText("Paste");
        popCodePaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodePasteActionPerformed(evt);
            }
        });
        popCode.add(popCodePaste);

        popCodeDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_button_cancel.png"))); // NOI18N
        popCodeDelete.setText("Delete");
        popCodeDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodeDeleteActionPerformed(evt);
            }
        });
        popCode.add(popCodeDelete);
        popCode.add(jSeparator5);

        popCodeColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_device_blockdevice.png"))); // NOI18N
        popCodeColor.setText("Choose a color...");
        popCodeColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodeColorActionPerformed(evt);
            }
        });
        popCode.add(popCodeColor);

        popCodeAlpha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_miscellaneous.png"))); // NOI18N
        popCodeAlpha.setText("Choose an alpha...");
        popCodeAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodeAlphaActionPerformed(evt);
            }
        });
        popCode.add(popCodeAlpha);

        popCodePNG.setText("Choose a PNG image...");
        popCodePNG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodePNGActionPerformed(evt);
            }
        });
        popCode.add(popCodePNG);
        popCode.add(jSeparator6);

        popCodeOver.setText("Insert overrides...");

        popCodeAni1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame5.png"))); // NOI18N
        popCodeAni1.setText("For animation...");

        popCode_b.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popCode_b.setText("\\b - Bold");
        popCode_b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_bActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_b);

        popCode_i.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popCode_i.setText("\\i - Italic");
        popCode_i.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_iActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_i);

        popCode_u.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_u.setText("\\u - Underline");
        popCode_u.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_uActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_u);

        popCode_s.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_s.setText("\\s - Strike out");
        popCode_s.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_sActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_s);

        popCode_bord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_bord.setText("\\bord - Thickness of border");
        popCode_bord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_bordActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_bord);

        popCode_shad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_shad.setText("\\shad - Depth of shader");
        popCode_shad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_shadActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_shad);

        popCode_be.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_be.setText("\\be - Blur edge");
        popCode_be.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_beActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_be);

        popCode_fs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popCode_fs.setText("\\fs - Font size");
        popCode_fs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fsActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_fs);

        popCode_fscx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_fscx.setText("\\fscx - Font scale of X");
        popCode_fscx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fscxActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_fscx);

        popCode_fscy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_fscy.setText("\\fscy - Font scale of Y");
        popCode_fscy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fscyActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_fscy);

        popCode_fsp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_fsp.setText("\\fsp - Font spacing");
        popCode_fsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fspActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_fsp);

        popCode_frx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_frx.setText("\\frx - Font rotation of X");
        popCode_frx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_frxActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_frx);

        popCode_fry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_fry.setText("\\fry - Font rotation of Y");
        popCode_fry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fryActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_fry);

        popCode_frz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_frz.setText("\\frz - Font rotation of Z");
        popCode_frz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_frzActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_frz);

        popCode_1c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_1c.setText("\\1c&H<hexa>& - Color of text");
        popCode_1c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_1cActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_1c);

        popCode_2c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_2c.setText("\\2c&H<hexa>& - Color of karaoke");
        popCode_2c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_2cActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_2c);

        popCode_3c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_3c.setText("\\3c&H<hexa>& - Color of border");
        popCode_3c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_3cActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_3c);

        popCode_4c.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_4c.setText("\\4c&H<hexa>& - Color of shader");
        popCode_4c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_4cActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_4c);

        popCode_alpha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_alpha.setText("\\alpha&H<hexa>& - Transparency");
        popCode_alpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_alphaActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_alpha);

        popCode_1a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_1a.setText("\\1a&H<hexa>& - Transparency of text");
        popCode_1a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_1aActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_1a);

        popCode_2a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_2a.setText("\\2a&H<hexa>& - Transparency of karaoke");
        popCode_2a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_2aActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_2a);

        popCode_3a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_3a.setText("\\3a&H<hexa>& - Transparency of border");
        popCode_3a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_3aActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_3a);

        popCode_4a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_4a.setText("\\4a&H<hexa>& - Transparency of shader");
        popCode_4a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_4aActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_4a);

        popCode_clip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_clip.setText("\\clip - Region of visibility");
        popCode_clip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_clipActionPerformed(evt);
            }
        });
        popCodeAni1.add(popCode_clip);

        popCodeOver.add(popCodeAni1);

        popCodeAni2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCodeAni2.setText("For animation...");

        popCode_xbord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_xbord.setText("\\xbord - Thickness of border on X");
        popCode_xbord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_xbordActionPerformed(evt);
            }
        });
        popCodeAni2.add(popCode_xbord);

        popCode_ybord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_ybord.setText("\\ybord - Thickness of border on Y");
        popCode_ybord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_ybordActionPerformed(evt);
            }
        });
        popCodeAni2.add(popCode_ybord);

        popCode_xshad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_xshad.setText("\\xshad - Depth of shader on X");
        popCode_xshad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_xshadActionPerformed(evt);
            }
        });
        popCodeAni2.add(popCode_xshad);

        popCode_yshad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_yshad.setText("\\yshad - Depth of shader on Y");
        popCode_yshad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_yshadActionPerformed(evt);
            }
        });
        popCodeAni2.add(popCode_yshad);

        popCode_blur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_blur.setText("\\blur - Blur");
        popCode_blur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_blurActionPerformed(evt);
            }
        });
        popCodeAni2.add(popCode_blur);

        popCode_fax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_fax.setText("\\fax - Text shearing on X");
        popCode_fax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_faxActionPerformed(evt);
            }
        });
        popCodeAni2.add(popCode_fax);

        popCode_fay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_fay.setText("\\fay - Text shearing on Y");
        popCode_fay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fayActionPerformed(evt);
            }
        });
        popCodeAni2.add(popCode_fay);

        popCode_iclip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_iclip.setText("\\iclip - Region of invisibility");
        popCode_iclip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_iclipActionPerformed(evt);
            }
        });
        popCodeAni2.add(popCode_iclip);

        popCodeOver.add(popCodeAni2);

        popCodeAni3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCodeAni3.setText("For animation...");

        popCode_fsc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_fsc.setText("\\fsc - Font scale");
        popCode_fsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fscActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_fsc);

        popCode_fsvp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_fsvp.setText("\\fsvp - Leading");
        popCode_fsvp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fsvpActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_fsvp);

        popCode_frs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_frs.setText("\\frs - Baseline obliquity");
        popCode_frs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_frsActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_frs);

        popCode_z.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_z.setText("\\z - Z coordinate");
        popCode_z.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_zActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_z);

        popCode_distort.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_distort.setText("\\distort - Distortion");
        popCode_distort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_distortActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_distort);

        popCode_md.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_md.setText("\\md - Boundaries deforming");
        popCode_md.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_mdActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_md);

        popCode_mdx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_mdx.setText("\\mdx - Boundaries deforming on X");
        popCode_mdx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_mdxActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_mdx);

        popCode_mdy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_mdy.setText("\\mdy - Boundaries deforming on Y");
        popCode_mdy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_mdyActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_mdy);

        popCode_mdz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_mdz.setText("\\mdz - Boundaries deforming on Z");
        popCode_mdz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_mdzActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_mdz);

        popCode_1vc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_1vc.setText("\\1vc - Gradients on text (color)");
        popCode_1vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_1vcActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_1vc);

        popCode_2vc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_2vc.setText("\\2vc - Gradients on karaoke (color)");
        popCode_2vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_2vcActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_2vc);

        popCode_3vc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_3vc.setText("\\3vc - Gradients on border (color)");
        popCode_3vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_3vcActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_3vc);

        popCode_4vc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_4vc.setText("\\4vc - Gradients on shader (color)");
        popCode_4vc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_4vcActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_4vc);

        popCode_1va.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_1va.setText("\\1va - Gradients on text (transparency)");
        popCode_1va.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_1vaActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_1va);

        popCode_2va.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_2va.setText("\\2va - Gradients on karaoke (transparency)");
        popCode_2va.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_2vaActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_2va);

        popCode_3va.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_3va.setText("\\3va - Gradients on border (transparency)");
        popCode_3va.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_3vaActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_3va);

        popCode_4va.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_4va.setText("\\4va - Gradients on shader (transparency)");
        popCode_4va.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_4vaActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_4va);

        popCode_1img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_1img.setText("\\1img - Image fill on text");
        popCode_1img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_1imgActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_1img);

        popCode_2img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_2img.setText("\\2img - Image fill on karaoke");
        popCode_2img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_2imgActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_2img);

        popCode_3img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_3img.setText("\\3img - Image fill on border");
        popCode_3img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_3imgActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_3img);

        popCode_4img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_4img.setText("\\4img - Image fill on shader");
        popCode_4img.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_4imgActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_4img);

        popCode_jitter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_jitter.setText("\\jitter - Shaking");
        popCode_jitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_jitterActionPerformed(evt);
            }
        });
        popCodeAni3.add(popCode_jitter);

        popCodeOver.add(popCodeAni3);

        popCodeConf.setText("For configuration...");

        popCode_fn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popCode_fn.setText("\\fn - Font name");
        popCode_fn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fnActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_fn);

        popCode_fe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popCode_fe.setText("\\fe - Font encoding");
        popCode_fe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_feActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_fe);

        popCode_q.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_q.setText("\\q - Wrapping style");
        popCode_q.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_qActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_q);

        popCode_a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popCode_a.setText("\\a - Alignment (old)");
        popCode_a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_aActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_a);

        popCode_an.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_an.setText("\\an - Alignment");
        popCode_an.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_anActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_an);

        popCode_pos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_pos.setText("\\pos - Position");
        popCode_pos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_posActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_pos);

        popCode_move.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_move.setText("\\move - Position in real time");
        popCode_move.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_moveActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_move);

        popCode_org.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_org.setText("\\org - Origin");
        popCode_org.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_orgActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_org);

        popCode_fad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_fad.setText("\\fad - Fading");
        popCode_fad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fadActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_fad);

        popCode_fade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_fade.setText("\\fade - Fading");
        popCode_fade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_fadeActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_fade);

        popCode_clip2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_clip2.setText("\\clip - Region of visibility");
        popCode_clip2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_clip2ActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_clip2);

        popCode_iclip2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame4.png"))); // NOI18N
        popCode_iclip2.setText("\\iclip - Region of invisibility");
        popCode_iclip2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_iclip2ActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_iclip2);

        popCode_mover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_mover.setText("\\mover - Polar move");
        popCode_mover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_moverActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_mover);

        popCode_moves3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_moves3.setText("\\moves3 - Spline move");
        popCode_moves3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_moves3ActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_moves3);

        popCode_moves4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_moves4.setText("\\moves4 - Spline move");
        popCode_moves4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_moves4ActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_moves4);

        popCode_movevc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_movevc.setText("\\movevc - Moveable vector clip");
        popCode_movevc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_movevcActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_movevc);

        popCode_movevc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame.png"))); // NOI18N
        popCode_movevc2.setText("\\movevc - Moveable vector clip");
        popCode_movevc2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_movevc2ActionPerformed(evt);
            }
        });
        popCodeConf.add(popCode_movevc2);

        popCodeOver.add(popCodeConf);
        popCodeOver.add(jSeparator7);

        popCode_k.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popCode_k.setText("\\k - Simple karaoke");
        popCode_k.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_kActionPerformed(evt);
            }
        });
        popCodeOver.add(popCode_k);

        popCode_kf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_kf.setText("\\kf - Karaoke with fill");
        popCode_kf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_kfActionPerformed(evt);
            }
        });
        popCodeOver.add(popCode_kf);

        popCode_ko.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_ko.setText("\\ko - Karaoke with outline");
        popCode_ko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_koActionPerformed(evt);
            }
        });
        popCodeOver.add(popCode_ko);

        popCode_t.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame2.png"))); // NOI18N
        popCode_t.setText("\\t - Animation");
        popCode_t.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_tActionPerformed(evt);
            }
        });
        popCodeOver.add(popCode_t);
        popCodeOver.add(jSeparator8);

        popCode_reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_ksame3.png"))); // NOI18N
        popCode_reset.setText("\\r - Reset");
        popCode_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCode_resetActionPerformed(evt);
            }
        });
        popCodeOver.add(popCode_reset);

        popCode.add(popCodeOver);

        popCodeInsSke.setText("Insert skeleton");
        popCodeInsSke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodeInsSkeActionPerformed(evt);
            }
        });
        popCode.add(popCodeInsSke);

        popCodeInsSni.setText("Insert snippet...");
        popCodeInsSni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popCodeInsSniActionPerformed(evt);
            }
        });
        popCode.add(popCodeInsSni);

        ifrCode.setIconifiable(true);
        ifrCode.setMaximizable(true);
        ifrCode.setResizable(true);
        ifrCode.setTitle("Editeur de code");
        ifrCode.setVisible(true);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        bgCode.add(tbRuby);
        tbRuby.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-RubyScript.png"))); // NOI18N
        tbRuby.setSelected(true);
        tbRuby.setFocusable(false);
        tbRuby.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbRuby.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbRuby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbRubyActionPerformed(evt);
            }
        });
        jToolBar1.add(tbRuby);

        bgCode.add(tbPython);
        tbPython.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-PythonScript.png"))); // NOI18N
        tbPython.setFocusable(false);
        tbPython.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbPython.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbPython.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbPythonActionPerformed(evt);
            }
        });
        jToolBar1.add(tbPython);
        jToolBar1.add(jSeparator1);

        btnCodeNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_filenew.png"))); // NOI18N
        btnCodeNew.setFocusable(false);
        btnCodeNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCodeNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCodeNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodeNewActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCodeNew);

        btnCodeOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_filesystem_folder_grey_open.png"))); // NOI18N
        btnCodeOpen.setFocusable(false);
        btnCodeOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCodeOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCodeOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodeOpenActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCodeOpen);

        btnCodeSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_device_floppy_unmount.png"))); // NOI18N
        btnCodeSave.setFocusable(false);
        btnCodeSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCodeSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCodeSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodeSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCodeSave);
        jToolBar1.add(jSeparator2);

        btnCodeReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_reload.png"))); // NOI18N
        btnCodeReload.setFocusable(false);
        btnCodeReload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCodeReload.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCodeReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodeReloadActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCodeReload);
        jToolBar1.add(jSeparator3);

        btnCodeCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_editcut.png"))); // NOI18N
        btnCodeCut.setFocusable(false);
        btnCodeCut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCodeCut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCodeCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodeCutActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCodeCut);

        btnCodeCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        btnCodeCopy.setFocusable(false);
        btnCodeCopy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCodeCopy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCodeCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodeCopyActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCodeCopy);

        btnCodePaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_editpaste.png"))); // NOI18N
        btnCodePaste.setFocusable(false);
        btnCodePaste.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCodePaste.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCodePaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodePasteActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCodePaste);

        ifrCode.getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane2.setAutoscrolls(true);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jList1.setPreferredSize(new java.awt.Dimension(165, 20));
        jList1.setVisibleRowCount(1);
        jScrollPane2.setViewportView(jList1);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.NORTH);

        epScripting.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                epScriptingCaretUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(epScripting);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        ifrCode.getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jDesktopPane1.add(ifrCode);
        ifrCode.setBounds(10, 10, 830, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbRubyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbRubyActionPerformed
        // Select the ruby code in editor.
        epScripting.setContentType("text/ruby");
        epScripting.setComponentPopupMenu(popCode);
    }//GEN-LAST:event_tbRubyActionPerformed

    private void tbPythonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbPythonActionPerformed
        // Select the python code in editor.
        epScripting.setContentType("text/python");
        epScripting.setComponentPopupMenu(popCode);
    }//GEN-LAST:event_tbPythonActionPerformed

    private void btnCodeNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodeNewActionPerformed
        epScripting.setText("");
    }//GEN-LAST:event_btnCodeNewActionPerformed

    private void btnCodeOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodeOpenActionPerformed
        // Clear the list of file filters.
        for (FileFilter f : jFileChooser1.getChoosableFileFilters()){
            jFileChooser1.removeChoosableFileFilter(f);
        }
        // Add good file filters.
        jFileChooser1.addChoosableFileFilter(new RubyFilter());
        jFileChooser1.addChoosableFileFilter(new PythonFilter());
        jFileChooser1.setAccessory(null);
        // Action
        int z = this.jFileChooser1.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            AssIO aio = new AssIO();
            String text = aio.openRubyFile(jFileChooser1.getSelectedFile().getPath());
            if(jFileChooser1.getSelectedFile().getPath().endsWith(".rb")){
                tbRuby.setSelected(true);
                epScripting.setContentType("text/ruby");
            }else if(jFileChooser1.getSelectedFile().getPath().endsWith(".py")){
                tbPython.setSelected(true);
                epScripting.setContentType("text/python");
            }
            epScripting.setText(text);
        }
    }//GEN-LAST:event_btnCodeOpenActionPerformed

    private void btnCodeSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodeSaveActionPerformed
        // Clear the list of file filters.
        for (FileFilter f : jFileChooser1.getChoosableFileFilters()){
            jFileChooser1.removeChoosableFileFilter(f);
        }
        // Add good file filters.
        jFileChooser1.addChoosableFileFilter(new RubyFilter());
        jFileChooser1.addChoosableFileFilter(new PythonFilter());
        jFileChooser1.setAccessory(null);
        // Action
        int z = this.jFileChooser1.showSaveDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            String file = jFileChooser1.getSelectedFile().getPath();        
            if(file.endsWith(".rb")==false && file.endsWith(".py")==false){
                if(tbRuby.isSelected()){file=file+RubyFilter.getExtension();}
                if(tbPython.isSelected()){file=file+PythonFilter.getExtension();}
            }
            AssIO aio = new AssIO();
            aio.saveRubyFile(file,epScripting.getText());
        }
    }//GEN-LAST:event_btnCodeSaveActionPerformed

    private void btnCodeReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodeReloadActionPerformed
        // Refresh ruby scripts list in the tree
    // Delete all ruby scripts
    KaraokePanel.getRubyTreeNode().removeAllChildren();
//    dcbmSplug.removeAllElements();
//    cbButtonScript.removeAllItems();
    KaraokePanel.updateTree();
    // and search for all ruby scripts :
//    searchForRubyScript(fxScripts);
    splug.searchForScript(docs);
    List<Object> sobjList = new ArrayList<>(splug.getSObjectList());
    for(Object o : sobjList){
        if(o instanceof FxObject){
            FxObject fxo = (FxObject)o;
            boolean found = false;
            for(FxObject nfxo : KaraokePanel.getFxObjectListFromFxTree()){
                if(nfxo.isTheSame(fxo)){found = true;}
            }
            if(found==false){
                KaraokePanel.getRubyTreeNode().add(new DefaultMutableTreeNode(fxo));
            }                
        }
    }
    KaraokePanel.updateTree();
    }//GEN-LAST:event_btnCodeReloadActionPerformed

    private void btnCodeCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodeCutActionPerformed
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(epScripting.getSelectedText());
            String s = epScripting.getText();
            int sStart = epScripting.getSelectionStart();
            int sEnd = epScripting.getSelectionEnd();
            epScripting.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_btnCodeCutActionPerformed

    private void btnCodeCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodeCopyActionPerformed
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(epScripting.getSelectedText());
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_btnCodeCopyActionPerformed

    private void btnCodePasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodePasteActionPerformed
        try{
            Clipboard cb = new Clipboard();
            String s = epScripting.getText();
            int sStart = epScripting.getSelectionStart();
            int sEnd = epScripting.getSelectionEnd();
            epScripting.setText(s.substring(0, sStart)+cb.CPaste()+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_btnCodePasteActionPerformed

    private void popCodeCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodeCutActionPerformed
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(epScripting.getSelectedText());
            String s = epScripting.getText();
            int sStart = epScripting.getSelectionStart();
            int sEnd = epScripting.getSelectionEnd();
            epScripting.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_popCodeCutActionPerformed

    private void popCodeCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodeCopyActionPerformed
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(epScripting.getSelectedText());
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_popCodeCopyActionPerformed

    private void popCodePasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodePasteActionPerformed
        try{
            Clipboard cb = new Clipboard();
            String s = epScripting.getText();
            int sStart = epScripting.getSelectionStart();
            int sEnd = epScripting.getSelectionEnd();
            epScripting.setText(s.substring(0, sStart)+cb.CPaste()+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_popCodePasteActionPerformed

    private void popCodeDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodeDeleteActionPerformed
        String s = epScripting.getText();
        int sStart = epScripting.getSelectionStart();
        int sEnd = epScripting.getSelectionEnd();
        epScripting.setText(s.substring(0, sStart)+s.substring(sEnd));
    }//GEN-LAST:event_popCodeDeleteActionPerformed

    private void popCodeColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodeColorActionPerformed
        try{
            AssColorDialog acd =
            new AssColorDialog(frame,true);
            acd.setLocationRelativeTo(null);
            String color = acd.showDialog(epScripting.getSelectedText());
            if(color!=null){
                putTextToCodeEditor(color);
            }
        }catch(Exception exc){
            // Bad selection area
        }
    }//GEN-LAST:event_popCodeColorActionPerformed

    private void popCodeAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodeAlphaActionPerformed
        try{
            AssAlphaDialog aad =
            new AssAlphaDialog(frame, true);
            aad.setLocationRelativeTo(null);
            String hexa = epScripting.getSelectedText();
            hexa = aad.showDialog(hexa);
            if(hexa==null){hexa="";}
            hexa=hexa.toUpperCase();
            putTextToCodeEditor(hexa);
        }catch(Exception exc){
            // Bad selection area
        }
    }//GEN-LAST:event_popCodeAlphaActionPerformed

    private void popCodePNGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodePNGActionPerformed
        // Clear the list of file filters.
        for (FileFilter f : jFileChooser1.getChoosableFileFilters()){
            jFileChooser1.removeChoosableFileFilter(f);
        }
        // Add good file filters.
        jFileChooser1.addChoosableFileFilter(new PngFilter());
        jFileChooser1.setAccessory(new ImagePreview(jFileChooser1));
        int z = jFileChooser1.showOpenDialog(this);
        if (z == JFileChooser.APPROVE_OPTION){
            String png = jFileChooser1.getSelectedFile().getAbsolutePath();
            putTextToCodeEditor("\""+png+"\"");
        }
    }//GEN-LAST:event_popCodePNGActionPerformed

    private void popCode_bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_bActionPerformed
        putTextToCodeEditor("\\\\b1");
    }//GEN-LAST:event_popCode_bActionPerformed

    private void popCode_iActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_iActionPerformed
        putTextToCodeEditor("\\\\i1");
    }//GEN-LAST:event_popCode_iActionPerformed

    private void popCode_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_uActionPerformed
        putTextToCodeEditor("\\\\u1");
    }//GEN-LAST:event_popCode_uActionPerformed

    private void popCode_sActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_sActionPerformed
        putTextToCodeEditor("\\\\s1");
    }//GEN-LAST:event_popCode_sActionPerformed

    private void popCode_bordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_bordActionPerformed
        putTextToCodeEditor("\\\\bord2");
    }//GEN-LAST:event_popCode_bordActionPerformed

    private void popCode_shadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_shadActionPerformed
        putTextToCodeEditor("\\\\shad2");
    }//GEN-LAST:event_popCode_shadActionPerformed

    private void popCode_beActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_beActionPerformed
        putTextToCodeEditor("\\\\be0");
    }//GEN-LAST:event_popCode_beActionPerformed

    private void popCode_fsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fsActionPerformed
        putTextToCodeEditor("\\\\fs50");
    }//GEN-LAST:event_popCode_fsActionPerformed

    private void popCode_fscxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fscxActionPerformed
        putTextToCodeEditor("\\\\fscx100");
    }//GEN-LAST:event_popCode_fscxActionPerformed

    private void popCode_fscyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fscyActionPerformed
        putTextToCodeEditor("\\\\fscy100");
    }//GEN-LAST:event_popCode_fscyActionPerformed

    private void popCode_fspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fspActionPerformed
        putTextToCodeEditor("\\\\fsp0");
    }//GEN-LAST:event_popCode_fspActionPerformed

    private void popCode_frxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_frxActionPerformed
        putTextToCodeEditor("\\\\frx0");
    }//GEN-LAST:event_popCode_frxActionPerformed

    private void popCode_fryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fryActionPerformed
        putTextToCodeEditor("\\\\fry0");
    }//GEN-LAST:event_popCode_fryActionPerformed

    private void popCode_frzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_frzActionPerformed
        putTextToCodeEditor("\\\\frz0");
    }//GEN-LAST:event_popCode_frzActionPerformed

    private void popCode_1cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_1cActionPerformed
        putTextToCodeEditor("\\\\1c&H000000&");
    }//GEN-LAST:event_popCode_1cActionPerformed

    private void popCode_2cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_2cActionPerformed
        putTextToCodeEditor("\\\\2c&H000000&");
    }//GEN-LAST:event_popCode_2cActionPerformed

    private void popCode_3cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_3cActionPerformed
        putTextToCodeEditor("\\\\3c&H000000&");
    }//GEN-LAST:event_popCode_3cActionPerformed

    private void popCode_4cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_4cActionPerformed
        putTextToCodeEditor("\\\\4c&H000000&");
    }//GEN-LAST:event_popCode_4cActionPerformed

    private void popCode_alphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_alphaActionPerformed
        putTextToCodeEditor("\\\\alpha&H00&");
    }//GEN-LAST:event_popCode_alphaActionPerformed

    private void popCode_1aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_1aActionPerformed
        putTextToCodeEditor("\\\\1a&H00&");
    }//GEN-LAST:event_popCode_1aActionPerformed

    private void popCode_2aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_2aActionPerformed
        putTextToCodeEditor("\\\\2a&H00&");
    }//GEN-LAST:event_popCode_2aActionPerformed

    private void popCode_3aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_3aActionPerformed
        putTextToCodeEditor("\\\\3a&H00&");
    }//GEN-LAST:event_popCode_3aActionPerformed

    private void popCode_4aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_4aActionPerformed
        putTextToCodeEditor("\\\\4a&H00&");
    }//GEN-LAST:event_popCode_4aActionPerformed

    private void popCode_clipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_clipActionPerformed
        putTextToCodeEditor("\\\\clip(x1,y1,x2,y2)");
    }//GEN-LAST:event_popCode_clipActionPerformed

    private void popCode_xbordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_xbordActionPerformed
        putTextToCodeEditor("\\\\xbord2");
    }//GEN-LAST:event_popCode_xbordActionPerformed

    private void popCode_ybordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_ybordActionPerformed
        putTextToCodeEditor("\\\\ybord2");
    }//GEN-LAST:event_popCode_ybordActionPerformed

    private void popCode_xshadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_xshadActionPerformed
        putTextToCodeEditor("\\\\xshad2");
    }//GEN-LAST:event_popCode_xshadActionPerformed

    private void popCode_yshadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_yshadActionPerformed
        putTextToCodeEditor("\\\\yshad2");
    }//GEN-LAST:event_popCode_yshadActionPerformed

    private void popCode_blurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_blurActionPerformed
        putTextToCodeEditor("\\\\blur0");
    }//GEN-LAST:event_popCode_blurActionPerformed

    private void popCode_faxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_faxActionPerformed
        putTextToCodeEditor("\\\\fax0");
    }//GEN-LAST:event_popCode_faxActionPerformed

    private void popCode_fayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fayActionPerformed
        putTextToCodeEditor("\\\\fay0");
    }//GEN-LAST:event_popCode_fayActionPerformed

    private void popCode_iclipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_iclipActionPerformed
        putTextToCodeEditor("\\\\iclip(x1,y1,x2,y2)");
    }//GEN-LAST:event_popCode_iclipActionPerformed

    private void popCode_fscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fscActionPerformed
        putTextToCodeEditor("\\\\fsc100");
    }//GEN-LAST:event_popCode_fscActionPerformed

    private void popCode_fsvpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fsvpActionPerformed
        putTextToCodeEditor("\\\\fsvp0");
    }//GEN-LAST:event_popCode_fsvpActionPerformed

    private void popCode_frsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_frsActionPerformed
        putTextToCodeEditor("\\\\frs0");
    }//GEN-LAST:event_popCode_frsActionPerformed

    private void popCode_zActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_zActionPerformed
        putTextToCodeEditor("\\\\z0");
    }//GEN-LAST:event_popCode_zActionPerformed

    private void popCode_distortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_distortActionPerformed
        putTextToCodeEditor("\\\\distort(u1,v1,u2,v2,u3,v3)");
    }//GEN-LAST:event_popCode_distortActionPerformed

    private void popCode_mdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_mdActionPerformed
        putTextToCodeEditor("\\\\md0");
    }//GEN-LAST:event_popCode_mdActionPerformed

    private void popCode_mdxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_mdxActionPerformed
        putTextToCodeEditor("\\\\mdx0");
    }//GEN-LAST:event_popCode_mdxActionPerformed

    private void popCode_mdyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_mdyActionPerformed
        putTextToCodeEditor("\\\\mdy0");
    }//GEN-LAST:event_popCode_mdyActionPerformed

    private void popCode_mdzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_mdzActionPerformed
        putTextToCodeEditor("\\\\mdz0");
    }//GEN-LAST:event_popCode_mdzActionPerformed

    private void popCode_1vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_1vcActionPerformed
        putTextToCodeEditor("\\\\1vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popCode_1vcActionPerformed

    private void popCode_2vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_2vcActionPerformed
        putTextToCodeEditor("\\\\2vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popCode_2vcActionPerformed

    private void popCode_3vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_3vcActionPerformed
        putTextToCodeEditor("\\\\3vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popCode_3vcActionPerformed

    private void popCode_4vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_4vcActionPerformed
        putTextToCodeEditor("\\\\4vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popCode_4vcActionPerformed

    private void popCode_1vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_1vaActionPerformed
        putTextToCodeEditor("\\\\1va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popCode_1vaActionPerformed

    private void popCode_2vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_2vaActionPerformed
        putTextToCodeEditor("\\\\2va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popCode_2vaActionPerformed

    private void popCode_3vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_3vaActionPerformed
        putTextToCodeEditor("\\\\3va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popCode_3vaActionPerformed

    private void popCode_4vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_4vaActionPerformed
        putTextToCodeEditor("\\\\4va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popCode_4vaActionPerformed

    private void popCode_1imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_1imgActionPerformed
        putTextToCodeEditor("\\\\1img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popCode_1imgActionPerformed

    private void popCode_2imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_2imgActionPerformed
        putTextToCodeEditor("\\\\2img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popCode_2imgActionPerformed

    private void popCode_3imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_3imgActionPerformed
        putTextToCodeEditor("\\\\3img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popCode_3imgActionPerformed

    private void popCode_4imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_4imgActionPerformed
        putTextToCodeEditor("\\\\4img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popCode_4imgActionPerformed

    private void popCode_jitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_jitterActionPerformed
        putTextToCodeEditor("\\\\jitter(left,right,up,down,period[,seed])");
    }//GEN-LAST:event_popCode_jitterActionPerformed

    private void popCode_fnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fnActionPerformed
        putTextToCodeEditor("\\\\fnDialog");
    }//GEN-LAST:event_popCode_fnActionPerformed

    private void popCode_feActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_feActionPerformed
        putTextToCodeEditor("\\\\fe1");
    }//GEN-LAST:event_popCode_feActionPerformed

    private void popCode_qActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_qActionPerformed
        putTextToCodeEditor("\\\\q1");
    }//GEN-LAST:event_popCode_qActionPerformed

    private void popCode_aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_aActionPerformed
        putTextToCodeEditor("\\\\a2");
    }//GEN-LAST:event_popCode_aActionPerformed

    private void popCode_anActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_anActionPerformed
        putTextToCodeEditor("\\\\an2");
    }//GEN-LAST:event_popCode_anActionPerformed

    private void popCode_posActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_posActionPerformed
        putTextToCodeEditor("\\\\pos(x,y)");
    }//GEN-LAST:event_popCode_posActionPerformed

    private void popCode_moveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_moveActionPerformed
        putTextToCodeEditor("\\\\move(x1,y1,x2,y2[,t1,t2])");
    }//GEN-LAST:event_popCode_moveActionPerformed

    private void popCode_orgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_orgActionPerformed
        putTextToCodeEditor("\\\\org(x,y)");
    }//GEN-LAST:event_popCode_orgActionPerformed

    private void popCode_fadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fadActionPerformed
        putTextToCodeEditor("\\\\fad(t1,t2)");
    }//GEN-LAST:event_popCode_fadActionPerformed

    private void popCode_fadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_fadeActionPerformed
        putTextToCodeEditor("\\\\fade(a1,a2,a3,t1,t2,t3,t4)");
    }//GEN-LAST:event_popCode_fadeActionPerformed

    private void popCode_clip2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_clip2ActionPerformed
        putTextToCodeEditor("\\\\clip([scale,]some drawings)");
    }//GEN-LAST:event_popCode_clip2ActionPerformed

    private void popCode_iclip2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_iclip2ActionPerformed
        putTextToCodeEditor("\\\\iclip(scale,drawing commands)");
    }//GEN-LAST:event_popCode_iclip2ActionPerformed

    private void popCode_moverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_moverActionPerformed
        putTextToCodeEditor("\\\\mover(x1,y1,x2,y2,angle1,angle2,radius1,radius2[,t1,t2])");
    }//GEN-LAST:event_popCode_moverActionPerformed

    private void popCode_moves3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_moves3ActionPerformed
        putTextToCodeEditor("\\\\moves3(x1,x2,x2,y2,x3,y3[,t1,t2])");
    }//GEN-LAST:event_popCode_moves3ActionPerformed

    private void popCode_moves4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_moves4ActionPerformed
        putTextToCodeEditor("\\\\moves4(x1,x2,x2,y2,x3,y3,x4,y4[,t1,t2])");
    }//GEN-LAST:event_popCode_moves4ActionPerformed

    private void popCode_movevcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_movevcActionPerformed
        putTextToCodeEditor("\\\\movevc(x1,y1)");
    }//GEN-LAST:event_popCode_movevcActionPerformed

    private void popCode_movevc2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_movevc2ActionPerformed
        putTextToCodeEditor("\\\\movevc(x1,y1,x2,y2[,t1,t2])");
    }//GEN-LAST:event_popCode_movevc2ActionPerformed

    private void popCode_kActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_kActionPerformed
        putTextToCodeEditor("\\\\k");
    }//GEN-LAST:event_popCode_kActionPerformed

    private void popCode_kfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_kfActionPerformed
        putTextToCodeEditor("\\\\kf");
    }//GEN-LAST:event_popCode_kfActionPerformed

    private void popCode_koActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_koActionPerformed
        putTextToCodeEditor("\\\\ko");
    }//GEN-LAST:event_popCode_koActionPerformed

    private void popCode_tActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_tActionPerformed
        putTextToCodeEditor("\\\\t([t1,t2,][accel,]style modifiers)");
    }//GEN-LAST:event_popCode_tActionPerformed

    private void popCode_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCode_resetActionPerformed
        putTextToCodeEditor("\\\\r");
    }//GEN-LAST:event_popCode_resetActionPerformed

    private void popCodeInsSkeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodeInsSkeActionPerformed
        if(tbRuby.isSelected()){
            String text = "# Enable Java\nrequire 'java'\n\n# Register the script into AssFxMaker\n"
            + "Java::scripting.ScriptPlugin.rubyRegister(\"My function\",\"myfunction\",\"1."
            + "0\",\"Description\",\"Your name\",\"\",\"\")\n\ndef myfunction\n\n  # Enable"
            + " tools (see tools.rb)\n  require Java::scripting.ScriptPlugin.getScriptsP"
            + "ath()+\"tools.rb\"\n\n\nend\n";
            putTextToCodeEditor(text);
        }else if(tbPython.isSelected()){
            String text = "#!/usr/bin/env jython\n\n# Uselful imports\nfrom scripting import ScriptPlugin\n\n"
            + "# Register the script into AssFxMaker\n"
            + "ScriptPlugin.pythonRegister(\"My function\",\"myfunction\",\"1."
            + "0\",\"Description\",\"Your name\",\"\",\"\")\n\ndef myfunction():\n\n";
            putTextToCodeEditor(text);
        }
    }//GEN-LAST:event_popCodeInsSkeActionPerformed

    private void popCodeInsSniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popCodeInsSniActionPerformed
        SnippetDialog sd = new SnippetDialog(frame,true);
        sd.setSize(1000, sd.getHeight());
        sd.setLocationRelativeTo(null);
        String snippet = sd.showDialog();
        if (snippet!=null){
            putTextToCodeEditor(snippet);
        }
    }//GEN-LAST:event_popCodeInsSniActionPerformed

    private void epScriptingCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_epScriptingCaretUpdate
        int pos = evt.getDot();
        int firstSpace = epScripting.getText().lastIndexOf(" ", pos); if(firstSpace==-1){firstSpace=0;}
        int lastSpace = epScripting.getText().indexOf(" ", pos); if(lastSpace==-1){lastSpace=epScripting.getText().length();}
        String piece = epScripting.getText().substring(firstSpace, lastSpace).trim();
        System.out.println("Morceau = "+piece);
        acp.setAutoCompletionList(piece);
    }//GEN-LAST:event_epScriptingCaretUpdate

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgCode;
    private javax.swing.JButton btnCodeCopy;
    private javax.swing.JButton btnCodeCut;
    private javax.swing.JButton btnCodeNew;
    private javax.swing.JButton btnCodeOpen;
    private javax.swing.JButton btnCodePaste;
    private javax.swing.JButton btnCodeReload;
    private javax.swing.JButton btnCodeSave;
    private static javax.swing.JEditorPane epScripting;
    private javax.swing.JInternalFrame ifrCode;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPopupMenu popCode;
    private javax.swing.JMenuItem popCodeAlpha;
    private javax.swing.JMenu popCodeAni1;
    private javax.swing.JMenu popCodeAni2;
    private javax.swing.JMenu popCodeAni3;
    private javax.swing.JMenuItem popCodeColor;
    private javax.swing.JMenu popCodeConf;
    private javax.swing.JMenuItem popCodeCopy;
    private javax.swing.JMenuItem popCodeCut;
    private javax.swing.JMenuItem popCodeDelete;
    private javax.swing.JMenuItem popCodeInsSke;
    private javax.swing.JMenuItem popCodeInsSni;
    private javax.swing.JMenu popCodeOver;
    private javax.swing.JMenuItem popCodePNG;
    private javax.swing.JMenuItem popCodePaste;
    private javax.swing.JMenuItem popCode_1a;
    private javax.swing.JMenuItem popCode_1c;
    private javax.swing.JMenuItem popCode_1img;
    private javax.swing.JMenuItem popCode_1va;
    private javax.swing.JMenuItem popCode_1vc;
    private javax.swing.JMenuItem popCode_2a;
    private javax.swing.JMenuItem popCode_2c;
    private javax.swing.JMenuItem popCode_2img;
    private javax.swing.JMenuItem popCode_2va;
    private javax.swing.JMenuItem popCode_2vc;
    private javax.swing.JMenuItem popCode_3a;
    private javax.swing.JMenuItem popCode_3c;
    private javax.swing.JMenuItem popCode_3img;
    private javax.swing.JMenuItem popCode_3va;
    private javax.swing.JMenuItem popCode_3vc;
    private javax.swing.JMenuItem popCode_4a;
    private javax.swing.JMenuItem popCode_4c;
    private javax.swing.JMenuItem popCode_4img;
    private javax.swing.JMenuItem popCode_4va;
    private javax.swing.JMenuItem popCode_4vc;
    private javax.swing.JMenuItem popCode_a;
    private javax.swing.JMenuItem popCode_alpha;
    private javax.swing.JMenuItem popCode_an;
    private javax.swing.JMenuItem popCode_b;
    private javax.swing.JMenuItem popCode_be;
    private javax.swing.JMenuItem popCode_blur;
    private javax.swing.JMenuItem popCode_bord;
    private javax.swing.JMenuItem popCode_clip;
    private javax.swing.JMenuItem popCode_clip2;
    private javax.swing.JMenuItem popCode_distort;
    private javax.swing.JMenuItem popCode_fad;
    private javax.swing.JMenuItem popCode_fade;
    private javax.swing.JMenuItem popCode_fax;
    private javax.swing.JMenuItem popCode_fay;
    private javax.swing.JMenuItem popCode_fe;
    private javax.swing.JMenuItem popCode_fn;
    private javax.swing.JMenuItem popCode_frs;
    private javax.swing.JMenuItem popCode_frx;
    private javax.swing.JMenuItem popCode_fry;
    private javax.swing.JMenuItem popCode_frz;
    private javax.swing.JMenuItem popCode_fs;
    private javax.swing.JMenuItem popCode_fsc;
    private javax.swing.JMenuItem popCode_fscx;
    private javax.swing.JMenuItem popCode_fscy;
    private javax.swing.JMenuItem popCode_fsp;
    private javax.swing.JMenuItem popCode_fsvp;
    private javax.swing.JMenuItem popCode_i;
    private javax.swing.JMenuItem popCode_iclip;
    private javax.swing.JMenuItem popCode_iclip2;
    private javax.swing.JMenuItem popCode_jitter;
    private javax.swing.JMenuItem popCode_k;
    private javax.swing.JMenuItem popCode_kf;
    private javax.swing.JMenuItem popCode_ko;
    private javax.swing.JMenuItem popCode_md;
    private javax.swing.JMenuItem popCode_mdx;
    private javax.swing.JMenuItem popCode_mdy;
    private javax.swing.JMenuItem popCode_mdz;
    private javax.swing.JMenuItem popCode_move;
    private javax.swing.JMenuItem popCode_mover;
    private javax.swing.JMenuItem popCode_moves3;
    private javax.swing.JMenuItem popCode_moves4;
    private javax.swing.JMenuItem popCode_movevc;
    private javax.swing.JMenuItem popCode_movevc2;
    private javax.swing.JMenuItem popCode_org;
    private javax.swing.JMenuItem popCode_pos;
    private javax.swing.JMenuItem popCode_q;
    private javax.swing.JMenuItem popCode_reset;
    private javax.swing.JMenuItem popCode_s;
    private javax.swing.JMenuItem popCode_shad;
    private javax.swing.JMenuItem popCode_t;
    private javax.swing.JMenuItem popCode_u;
    private javax.swing.JMenuItem popCode_xbord;
    private javax.swing.JMenuItem popCode_xshad;
    private javax.swing.JMenuItem popCode_ybord;
    private javax.swing.JMenuItem popCode_yshad;
    private javax.swing.JMenuItem popCode_z;
    private static javax.swing.JToggleButton tbPython;
    private static javax.swing.JToggleButton tbRuby;
    // End of variables declaration//GEN-END:variables
}
