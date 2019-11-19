/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ParticleDialog.java
 *
 * Created on 7 déc. 2011, 14:11:03
 */
package smallboxforfansub.karaoke.dialog;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import smallboxforfansub.filter.PngFilter;
import smallboxforfansub.filter.PngJpgGifFilter;
import smallboxforfansub.filter.SubtitleFilter;
import smallboxforfansub.karaoke.highlighter.*;
import smallboxforfansub.karaoke.lib.AssIO;
import smallboxforfansub.karaoke.lib.AssStyle;
import smallboxforfansub.karaoke.lib.AssStyleCollection;
import smallboxforfansub.karaoke.lib.Clipboard;
import smallboxforfansub.karaoke.lib.ImagePreview;
import smallboxforfansub.lib.Language;
import smallboxforfansub.karaoke.lib.ParticleObject;

/**
 *
 * @author The Wingate 2940
 */
public class ParticleDialog extends javax.swing.JDialog {
    
    private DefaultComboBoxModel dcbm;
    private DefaultTableModel dtm;
    private ButtonPressed bp;
    private ParticleObject particleObject;
    private SaveState saveState = SaveState.DISABLE;
    private AssStyleCollection myASC = null;
    private Frame frame;
    private Language localeLanguage = smallboxforfansub.MainFrame.getLanguage();
    private String drawingPath = "";
    private String drawingsPath = "";
    private Highlighter high01;
    private Highlighter.HighlightPainter keywordPainter = new KeyHighlighterPainter();
    private Highlighter.HighlightPainter numberPainter = new NumberHighlighterPainter();
    private Highlighter.HighlightPainter normalPainter = new NormalHighlighterPainter();
    private Highlighter.HighlightPainter symbolPainter = new SymbolHighlighterPainter();
    private Highlighter.HighlightPainter hexaPainter = new HexadecimalHighlighterPainter();
    private Highlighter.HighlightPainter uvarPainter = new UserVariableHighlighterPainter();
    private Highlighter.HighlightPainter lvarPainter = new VariableHighlighterPainter();

    /** Creates new form ParticleDialog */
    public ParticleDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        frame = parent;
        
        // Setting up the scripting object (epScripting) to work with
        // the opensource project JSyntaxPane - see web site :
        // http://code.google.com/p/jsyntaxpane/
        jsyntaxpane.DefaultSyntaxKit.initKit();
        epVariables.setContentType("text/ruby");
        epVariables.setComponentPopupMenu(popOverrides1);
        
        javax.swing.border.TitledBorder tb;
        if(localeLanguage.getValueOf("titleXPD2")!=null){setTitle(localeLanguage.getValueOf("titleXPD2"));}
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("labelName")!=null){lblName.setText(localeLanguage.getValueOf("labelName"));}
        if(localeLanguage.getValueOf("labelMoment")!=null){lblMoment.setText(localeLanguage.getValueOf("labelMoment"));}
        if(localeLanguage.getValueOf("labelFirstLayer")!=null){lblFirstLayer.setText(localeLanguage.getValueOf("labelFirstLayer"));}
        if(localeLanguage.getValueOf("labelTime")!=null){lblTime.setText(localeLanguage.getValueOf("labelTime"));}
        if(localeLanguage.getValueOf("rbuttonBefore")!=null){rbMomentBefore.setText(localeLanguage.getValueOf("rbuttonBefore"));}
        if(localeLanguage.getValueOf("rbuttonMeantime")!=null){rbMomentMeantime.setText(localeLanguage.getValueOf("rbuttonMeantime"));}
        if(localeLanguage.getValueOf("rbuttonAfter")!=null){rbMomentAfter.setText(localeLanguage.getValueOf("rbuttonAfter"));}
        if(localeLanguage.getValueOf("tabEffects")!=null){tpParticle.setTitleAt(0, localeLanguage.getValueOf("tabEffects"));}
        if(localeLanguage.getValueOf("tabVariables")!=null){tpParticle.setTitleAt(1, localeLanguage.getValueOf("tabVariables"));}
        if(localeLanguage.getValueOf("tabSettings")!=null){tpParticle.setTitleAt(2, localeLanguage.getValueOf("tabSettings"));}
        if(localeLanguage.getValueOf("tabStyle")!=null){tpParticle.setTitleAt(3, localeLanguage.getValueOf("tabStyle"));}
        if(localeLanguage.getValueOf("labelLayersDetails")!=null){lblLayersDetails.setText(localeLanguage.getValueOf("labelLayersDetails"));}
        if(localeLanguage.getValueOf("buttonAdd")!=null){btnAddLayer.setText(localeLanguage.getValueOf("buttonAdd"));}
        if(localeLanguage.getValueOf("buttonGet")!=null){btnGetLayer.setText(localeLanguage.getValueOf("buttonGet"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnChangeLayer.setText(localeLanguage.getValueOf("buttonChange"));}
        if(localeLanguage.getValueOf("buttonDelete")!=null){btnDeleteLayer.setText(localeLanguage.getValueOf("buttonDelete"));}
        if(localeLanguage.getValueOf("labelOverrides")!=null){lblOverrides.setText(localeLanguage.getValueOf("labelOverrides"));}
        if(localeLanguage.getValueOf("checkboxSaveFx")!=null){cbSaveEffects.setText(localeLanguage.getValueOf("checkboxSaveFx"));}
        tb = (javax.swing.border.TitledBorder)jPanel1.getBorder();
        if(localeLanguage.getValueOf("tbdParameters")!=null){tb.setTitle(localeLanguage.getValueOf("tbdParameters"));}
        tb = (javax.swing.border.TitledBorder)jPanel2.getBorder();
        if(localeLanguage.getValueOf("tbdModes")!=null){tb.setTitle(localeLanguage.getValueOf("tbdModes"));}
        if(localeLanguage.getValueOf("labelVideoSize")!=null){lblVideoSize.setText(localeLanguage.getValueOf("labelVideoSize"));}
        if(localeLanguage.getValueOf("labelPosY")!=null){lblPosY.setText(localeLanguage.getValueOf("labelPosY"));}
        if(localeLanguage.getValueOf("rbuttonModeNormal")!=null){rbModeNormal.setText(localeLanguage.getValueOf("rbuttonModeNormal"));}
        if(localeLanguage.getValueOf("rbuttonModePeriodic")!=null){rbModePeriodic.setText(localeLanguage.getValueOf("rbuttonModePeriodic"));}
        if(localeLanguage.getValueOf("rbuttonModeRandom")!=null){rbModeRandom.setText(localeLanguage.getValueOf("rbuttonModeRandom"));}
        if(localeLanguage.getValueOf("rbuttonModeSymmetric")!=null){rbModeSymmetric.setText(localeLanguage.getValueOf("rbuttonModeSymmetric"));}
        if(localeLanguage.getValueOf("labelStyle")!=null){lblStyle.setText(localeLanguage.getValueOf("labelStyle"));}
        if(localeLanguage.getValueOf("buttonImpScr")!=null){btnImportScript.setText(localeLanguage.getValueOf("buttonImpScr"));}
        if(localeLanguage.getValueOf("buttonImpFil")!=null){btnImportFile.setText(localeLanguage.getValueOf("buttonImpFil"));}
        tb = (javax.swing.border.TitledBorder)panPreviewStyle.getBorder();
        if(localeLanguage.getValueOf("tbdPreview")!=null){tb.setTitle(localeLanguage.getValueOf("tbdPreview"));}
        if(localeLanguage.getValueOf("labelAuthor")!=null){lblAuthors.setText(localeLanguage.getValueOf("labelAuthor"));}
        if(localeLanguage.getValueOf("labelComments")!=null){lblComments.setText(localeLanguage.getValueOf("labelComments"));}
        if(localeLanguage.getValueOf("labelPreview")!=null){lblPreview.setText(localeLanguage.getValueOf("labelPreview"));}
        if(localeLanguage.getValueOf("labelCollection")!=null){lblCollection.setText(localeLanguage.getValueOf("labelCollection"));}
        if(localeLanguage.getValueOf("buttonChange")!=null){btnChangePreview.setText(localeLanguage.getValueOf("buttonChange"));}
        tb = (javax.swing.border.TitledBorder)panMiscPreview.getBorder();
        if(localeLanguage.getValueOf("tbdPreview")!=null){tb.setTitle(localeLanguage.getValueOf("tbdPreview"));}
        if(localeLanguage.getValueOf("popmCut")!=null){popmCut.setText(localeLanguage.getValueOf("popmCut"));}
        if(localeLanguage.getValueOf("popmCut")!=null){popmCut1.setText(localeLanguage.getValueOf("popmCut"));}
        if(localeLanguage.getValueOf("popmCopy")!=null){popmCopy.setText(localeLanguage.getValueOf("popmCopy"));}
        if(localeLanguage.getValueOf("popmCopy")!=null){popmCopy1.setText(localeLanguage.getValueOf("popmCopy"));}
        if(localeLanguage.getValueOf("popmPaste")!=null){popmPaste.setText(localeLanguage.getValueOf("popmPaste"));}
        if(localeLanguage.getValueOf("popmPaste")!=null){popmPaste1.setText(localeLanguage.getValueOf("popmPaste"));}
        if(localeLanguage.getValueOf("popmDelete")!=null){popmDelete.setText(localeLanguage.getValueOf("popmDelete"));}
        if(localeLanguage.getValueOf("popmDelete")!=null){popmDelete1.setText(localeLanguage.getValueOf("popmDelete"));}
        if(localeLanguage.getValueOf("popmClear")!=null){popmClear.setText(localeLanguage.getValueOf("popmClear"));}
        if(localeLanguage.getValueOf("popmClear")!=null){popmClear1.setText(localeLanguage.getValueOf("popmClear"));}
        if(localeLanguage.getValueOf("popmSelectAll")!=null){popmSelAll.setText(localeLanguage.getValueOf("popmSelectAll"));}
        if(localeLanguage.getValueOf("popmSelectAll")!=null){popmSelAll1.setText(localeLanguage.getValueOf("popmSelectAll"));}
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
        if(localeLanguage.getValueOf("popCodePNG")!=null){popmPNG.setText(localeLanguage.getValueOf("popCodePNG"));}
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
        
        if(localeLanguage.getValueOf("taHelpPleaseParticle")!=null){taHelpPlease.setText(localeLanguage.getValueOf("taHelpPleaseParticle"));}
        if(localeLanguage.getValueOf("tabHelpPlease")!=null){tpParticle.setTitleAt(5, localeLanguage.getValueOf("tabHelpPlease"));}
        if(localeLanguage.getValueOf("rbuttonSubModeSentence")!=null){rbSubModeSentence.setText(localeLanguage.getValueOf("rbuttonSubModeSentence"));}
        if(localeLanguage.getValueOf("rbuttonSubModeSyllable")!=null){rbSubModeSyllable.setText(localeLanguage.getValueOf("rbuttonSubModeSyllable"));}
        
        TableColumn column;
        for (int i = 0; i < 2; i++) {
            column = tableEffects.getColumnModel().getColumn(i);
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
        
//        //****************************************************
//        // Syntax highlight in textfield
//        //****************************************************
//        // The text is transparent thanks to this color.
//        // We want to paint the text using a highlighterpainter.
//        taOverrides.setForeground(new Color(0, 0, 0, 0));
//        // We want to view the caret in black.
//        taOverrides.setCaretColor(Color.black);
//        // BOLD for the best visibility
//        taOverrides.setFont(taOverrides.getFont().deriveFont(Font.BOLD));
//        // Get highlighter for this text component.
//        high01 = taOverrides.getHighlighter();
//        // Add the caret listener
//        taOverrides.addCaretListener(new CaretListener(){
//            @Override
//            public void caretUpdate(CaretEvent e) {
//                updateASSTextArea(taOverrides, high01);
//            }
//        });
//        taOverrides.setCaretPosition(0);
//        taOverrides.addFocusListener(new FocusListener() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                updateASSTextArea(taOverrides, high01);
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                updateASSTextArea(taOverrides, high01);
//            }
//        });
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
        fcPreview = new javax.swing.JFileChooser();
        bgModes = new javax.swing.ButtonGroup();
        fcStyles = new javax.swing.JFileChooser();
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
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        popmSurround = new javax.swing.JMenuItem();
        popmDelSurround = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        popm_k = new javax.swing.JMenuItem();
        popm_kf = new javax.swing.JMenuItem();
        popm_ko = new javax.swing.JMenuItem();
        popm_t = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        popm_reset = new javax.swing.JMenuItem();
        popmOverSep4 = new javax.swing.JPopupMenu.Separator();
        popmIntCalc = new javax.swing.JMenuItem();
        popmFloCalc = new javax.swing.JMenuItem();
        popmDrawing = new javax.swing.JMenuItem();
        popOverrides1 = new javax.swing.JPopupMenu();
        popmInsScript = new javax.swing.JMenu();
        popmCodeInit = new javax.swing.JMenuItem();
        popmCodeDef = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
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
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        popm_k1 = new javax.swing.JMenuItem();
        popm_kf1 = new javax.swing.JMenuItem();
        popm_ko1 = new javax.swing.JMenuItem();
        popm_t1 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        popm_reset1 = new javax.swing.JMenuItem();
        popmOverSep8 = new javax.swing.JPopupMenu.Separator();
        popmDrawing1 = new javax.swing.JMenuItem();
        bgSubModes = new javax.swing.ButtonGroup();
        lblName = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        lblFirstLayer = new javax.swing.JLabel();
        tfFirstLayer = new javax.swing.JTextField();
        lblMoment = new javax.swing.JLabel();
        rbMomentBefore = new javax.swing.JRadioButton();
        rbMomentMeantime = new javax.swing.JRadioButton();
        rbMomentAfter = new javax.swing.JRadioButton();
        lblTime = new javax.swing.JLabel();
        tfTime = new javax.swing.JTextField();
        tpParticle = new javax.swing.JTabbedPane();
        panEffects = new javax.swing.JPanel();
        spEffects = new javax.swing.JScrollPane();
        tableEffects = new javax.swing.JTable();
        lblLayersDetails = new javax.swing.JLabel();
        btnDeleteLayer = new javax.swing.JButton();
        btnChangeLayer = new javax.swing.JButton();
        btnGetLayer = new javax.swing.JButton();
        btnAddLayer = new javax.swing.JButton();
        lblOverrides = new javax.swing.JLabel();
        spOverrides = new javax.swing.JScrollPane();
        taOverrides = new javax.swing.JTextArea();
        panVariables = new javax.swing.JPanel();
        spVariables = new javax.swing.JScrollPane();
        epVariables = new javax.swing.JEditorPane();
        panSettings = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblVideoSize = new javax.swing.JLabel();
        tfHorizontalVSize = new javax.swing.JTextField();
        lblXVSize = new javax.swing.JLabel();
        tfVerticalVSize = new javax.swing.JTextField();
        lblPosY = new javax.swing.JLabel();
        tfPosY = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        rbModeNormal = new javax.swing.JRadioButton();
        rbModePeriodic = new javax.swing.JRadioButton();
        rbModeRandom = new javax.swing.JRadioButton();
        rbModeSymmetric = new javax.swing.JRadioButton();
        rbSubModeSentence = new javax.swing.JRadioButton();
        rbSubModeSyllable = new javax.swing.JRadioButton();
        panStyle = new javax.swing.JPanel();
        lblStyle = new javax.swing.JLabel();
        cbStyle = new javax.swing.JComboBox();
        panPreviewStyle = new javax.swing.JPanel();
        lblPreviewFont = new javax.swing.JLabel();
        btnImportScript = new javax.swing.JButton();
        btnImportFile = new javax.swing.JButton();
        panMisc = new javax.swing.JPanel();
        lblAuthors = new javax.swing.JLabel();
        tfAuthors = new javax.swing.JTextField();
        lblComments = new javax.swing.JLabel();
        tfComments = new javax.swing.JTextField();
        lblPreview = new javax.swing.JLabel();
        tfPreview = new javax.swing.JTextField();
        btnChangePreview = new javax.swing.JButton();
        lblCollection = new javax.swing.JLabel();
        tfCollection = new javax.swing.JTextField();
        panMiscPreview = new javax.swing.JPanel();
        lblMiscPreview = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taHelpPlease = new javax.swing.JTextArea();
        Cancel_Button = new javax.swing.JButton();
        OK_Button = new javax.swing.JButton();
        cbSaveEffects = new javax.swing.JCheckBox();

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
        popmOverrides.add(jSeparator8);

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
        popmOverrides.add(jSeparator9);

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
        popmOverrides.add(jSeparator10);

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
        popOverrides1.add(jSeparator2);

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
        popmOverrides1.add(jSeparator1);

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
        popmOverrides1.add(jSeparator3);

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
        setTitle("Create or edit a particle...");

        lblName.setText("Name :");

        tfName.setText("DefaultParticle");

        lblFirstLayer.setText("First layer :");

        tfFirstLayer.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfFirstLayer.setText("0");

        lblMoment.setText("Moment :");

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

        lblTime.setText("Time (ms) :");

        tfTime.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfTime.setText("0");
        tfTime.setEnabled(false);

        spEffects.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        spEffects.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableEffects.setModel(new javax.swing.table.DefaultTableModel(
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
        spEffects.setViewportView(tableEffects);

        lblLayersDetails.setText("Layers details :");

        btnDeleteLayer.setText("Delete");
        btnDeleteLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteLayerActionPerformed(evt);
            }
        });

        btnChangeLayer.setText("Change");
        btnChangeLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeLayerActionPerformed(evt);
            }
        });

        btnGetLayer.setText("Get");
        btnGetLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetLayerActionPerformed(evt);
            }
        });

        btnAddLayer.setText("Add");
        btnAddLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLayerActionPerformed(evt);
            }
        });

        lblOverrides.setText("Overrides :");

        spOverrides.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        taOverrides.setColumns(20);
        taOverrides.setLineWrap(true);
        taOverrides.setRows(5);
        taOverrides.setComponentPopupMenu(popOverrides);
        spOverrides.setViewportView(taOverrides);

        javax.swing.GroupLayout panEffectsLayout = new javax.swing.GroupLayout(panEffects);
        panEffects.setLayout(panEffectsLayout);
        panEffectsLayout.setHorizontalGroup(
            panEffectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panEffectsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panEffectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spOverrides, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panEffectsLayout.createSequentialGroup()
                        .addComponent(lblLayersDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddLayer, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGetLayer, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChangeLayer, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteLayer, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spEffects, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panEffectsLayout.createSequentialGroup()
                        .addComponent(lblOverrides)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panEffectsLayout.setVerticalGroup(
            panEffectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panEffectsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panEffectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLayersDetails)
                    .addComponent(btnDeleteLayer)
                    .addComponent(btnChangeLayer)
                    .addComponent(btnGetLayer)
                    .addComponent(btnAddLayer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spEffects, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblOverrides)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spOverrides, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpParticle.addTab("Effects", panEffects);

        spVariables.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        epVariables.setComponentPopupMenu(popOverrides1);
        spVariables.setViewportView(epVariables);

        javax.swing.GroupLayout panVariablesLayout = new javax.swing.GroupLayout(panVariables);
        panVariables.setLayout(panVariablesLayout);
        panVariablesLayout.setHorizontalGroup(
            panVariablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panVariablesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spVariables)
                .addContainerGap())
        );
        panVariablesLayout.setVerticalGroup(
            panVariablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panVariablesLayout.createSequentialGroup()
                .addComponent(spVariables, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpParticle.addTab("Variables", panVariables);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));

        lblVideoSize.setText("Video size :");

        tfHorizontalVSize.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfHorizontalVSize.setText("1280");

        lblXVSize.setText("x");

        tfVerticalVSize.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfVerticalVSize.setText("720");

        lblPosY.setText("Position on Y :");

        tfPosY.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfPosY.setText("50");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblPosY, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(lblVideoSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfPosY)
                    .addComponent(tfHorizontalVSize, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblXVSize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfVerticalVSize, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVideoSize)
                    .addComponent(tfHorizontalVSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblXVSize)
                    .addComponent(tfVerticalVSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPosY)
                    .addComponent(tfPosY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Modes"));

        bgModes.add(rbModeNormal);
        rbModeNormal.setSelected(true);
        rbModeNormal.setText("Normal");
        rbModeNormal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbModeNormalMouseClicked(evt);
            }
        });

        bgModes.add(rbModePeriodic);
        rbModePeriodic.setText("Periodic");
        rbModePeriodic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbModePeriodicMouseClicked(evt);
            }
        });

        bgModes.add(rbModeRandom);
        rbModeRandom.setText("Random");
        rbModeRandom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbModeRandomMouseClicked(evt);
            }
        });

        bgModes.add(rbModeSymmetric);
        rbModeSymmetric.setText("Symmetry ");
        rbModeSymmetric.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbModeSymmetricMouseClicked(evt);
            }
        });

        bgSubModes.add(rbSubModeSentence);
        rbSubModeSentence.setText("Sentence");

        bgSubModes.add(rbSubModeSyllable);
        rbSubModeSyllable.setSelected(true);
        rbSubModeSyllable.setText("Syllable");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbModeNormal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbModePeriodic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbModeRandom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbModeSymmetric, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbSubModeSyllable)
                            .addComponent(rbSubModeSentence))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(rbModeNormal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbSubModeSentence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbSubModeSyllable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbModePeriodic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbModeRandom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbModeSymmetric)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panSettingsLayout = new javax.swing.GroupLayout(panSettings);
        panSettings.setLayout(panSettingsLayout);
        panSettingsLayout.setHorizontalGroup(
            panSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        panSettingsLayout.setVerticalGroup(
            panSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tpParticle.addTab("Settings", panSettings);

        lblStyle.setText("Style :");

        cbStyle.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStyleActionPerformed(evt);
            }
        });

        panPreviewStyle.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));
        panPreviewStyle.setLayout(null);

        lblPreviewFont.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPreviewFont.setText("<html>ABCDEFGHIJKLMNOPQRSTUVWXYZ<br />\nabcdefghijklmnopqrstuvwxyz<br />\n0123456789</html>");
        panPreviewStyle.add(lblPreviewFont);
        lblPreviewFont.setBounds(6, 20, 515, 176);

        btnImportScript.setText("Import styles from script...");
        btnImportScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportScriptActionPerformed(evt);
            }
        });

        btnImportFile.setText("Import styles from file...");
        btnImportFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panStyleLayout = new javax.swing.GroupLayout(panStyle);
        panStyle.setLayout(panStyleLayout);
        panStyleLayout.setHorizontalGroup(
            panStyleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panStyleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panStyleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panPreviewStyle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addGroup(panStyleLayout.createSequentialGroup()
                        .addComponent(lblStyle, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panStyleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panStyleLayout.createSequentialGroup()
                                .addComponent(btnImportScript, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnImportFile, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                            .addComponent(cbStyle, 0, 436, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panStyleLayout.setVerticalGroup(
            panStyleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panStyleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panStyleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStyle)
                    .addComponent(cbStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panStyleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImportScript)
                    .addComponent(btnImportFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panPreviewStyle, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpParticle.addTab("Style", panStyle);

        lblAuthors.setText("Author(s) :");

        tfAuthors.setText("Users of AssFxMaker/Funsub Project @ 2006.");

        lblComments.setText("Comments :");

        tfComments.setText("Created with AssFxMaker/Funsub Project @ 2006.");

        lblPreview.setText("Preview :");

        tfPreview.setText("<none>");

        btnChangePreview.setText("Change");
        btnChangePreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePreviewActionPerformed(evt);
            }
        });

        lblCollection.setText("Collection :");

        tfCollection.setText("<none>");

        panMiscPreview.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));
        panMiscPreview.setLayout(null);

        lblMiscPreview.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panMiscPreview.add(lblMiscPreview);
        lblMiscPreview.setBounds(180, 60, 130, 40);

        javax.swing.GroupLayout panMiscLayout = new javax.swing.GroupLayout(panMisc);
        panMisc.setLayout(panMiscLayout);
        panMiscLayout.setHorizontalGroup(
            panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMiscLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panMiscPreview, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addGroup(panMiscLayout.createSequentialGroup()
                        .addGroup(panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblCollection, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPreview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblComments, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblAuthors, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfComments, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panMiscLayout.createSequentialGroup()
                                .addComponent(tfPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChangePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tfAuthors, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                            .addComponent(tfCollection, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panMiscLayout.setVerticalGroup(
            panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMiscLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAuthors)
                    .addComponent(tfAuthors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblComments)
                    .addComponent(tfComments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPreview)
                    .addComponent(tfPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChangePreview))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panMiscLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCollection)
                    .addComponent(tfCollection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panMiscPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpParticle.addTab("?", panMisc);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        taHelpPlease.setBackground(new java.awt.Color(237, 255, 237));
        taHelpPlease.setColumns(20);
        taHelpPlease.setEditable(false);
        taHelpPlease.setRows(5);
        taHelpPlease.setText("%XL - Position on X at the left of the syllable.\n%XC - Position on X at the center of the syllable.\n%XR - Position on X at the right of the syllable.\n\n%XLF - Position on X at the left of the first syllable.\n%XCF - Position on X at the center of the first syllable.\n%XRF - Position on X at the right of the first syllable.\n\n%XLL - Position on X at the left of the last syllable.\n%XCL - Position on X at the center of the last syllable.\n%XRL - Position on X at the right of the last syllable.\n\n%Y - Position on Y defined in the settings.\n\n%sK - Time of start of syllable   or   Time of start of letter\n%eK - Time of end of syllable   or   Time of end of letter\n%dK - Time of duration of syllable   or   Time of duration of letter\n%osK - Time of start of syllable\n%oeK - Time of end of syllable\n%odK - Time of duration of syllable\n\n%dP - Time of duration of sentence\n%posAF[x1,y1,x2,y2] - Give a random location for x and y\n%num[a1,a2] - Give a random number\n\n$wordinlowercase - Return the result of a function which is in the « Variables » tab.");
        jScrollPane1.setViewportView(taHelpPlease);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpParticle.addTab("Help please", jPanel3);

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });

        cbSaveEffects.setText("Save effects");
        cbSaveEffects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSaveEffectsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblMoment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblFirstLayer, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rbMomentBefore)
                                .addGap(10, 10, 10)
                                .addComponent(rbMomentMeantime)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rbMomentAfter)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfTime, 0, 1, Short.MAX_VALUE)
                            .addComponent(tfFirstLayer, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)))
                    .addComponent(tpParticle)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbSaveEffects, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(OK_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfFirstLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFirstLayer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMoment, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbMomentBefore)
                    .addComponent(rbMomentMeantime)
                    .addComponent(lblTime)
                    .addComponent(tfTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbMomentAfter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tpParticle, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbSaveEffects)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Cancel_Button)
                        .addComponent(OK_Button)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void btnAddLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLayerActionPerformed
        String s = taOverrides.getText();
        dtm.addRow(new Object[]{dtm.getRowCount(),s});
//        getOKButtonState();
    }//GEN-LAST:event_btnAddLayerActionPerformed

    private void btnGetLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetLayerActionPerformed
        try{
            String s = (String)dtm.getValueAt(tableEffects.getSelectedRow(), 1);
            taOverrides.setText(s);
        }catch(Exception exc){
            
        }
    }//GEN-LAST:event_btnGetLayerActionPerformed

    private void btnChangeLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeLayerActionPerformed
        String s = taOverrides.getText();
        dtm.setValueAt(s, tableEffects.getSelectedRow(), 1);
    }//GEN-LAST:event_btnChangeLayerActionPerformed

    private void btnDeleteLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteLayerActionPerformed
        try{
            int tabtemp[] = tableEffects.getSelectedRows();
            for (int i=tabtemp.length-1;i>=0;i--){
                dtm.removeRow(tabtemp[i]);
            }
//            getOKButtonState();
        }catch(Exception exc){
            
        }
    }//GEN-LAST:event_btnDeleteLayerActionPerformed

    private void btnChangePreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePreviewActionPerformed
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
            ImageIcon ii0 = new ImageIcon(fcPreview.getSelectedFile().getPath());
            lblMiscPreview.setIcon(ii0);
            lblMiscPreview.setSize(ii0.getIconWidth(), ii0.getIconHeight());
            lblMiscPreview.setLocation(panMiscPreview.getWidth()/2-lblMiscPreview.getWidth()/2,
                    panMiscPreview.getHeight()/2-lblMiscPreview.getHeight()/2);
        }
    }//GEN-LAST:event_btnChangePreviewActionPerformed

    private void cbSaveEffectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSaveEffectsActionPerformed
        if(cbSaveEffects.isSelected()){
            saveState = SaveState.ENABLE;
        }else{
            saveState = SaveState.DISABLE;
        }
    }//GEN-LAST:event_cbSaveEffectsActionPerformed

    private void rbMomentBeforeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMomentBeforeActionPerformed
        timeMoment();
    }//GEN-LAST:event_rbMomentBeforeActionPerformed

    private void rbMomentMeantimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMomentMeantimeActionPerformed
        timeMoment();
    }//GEN-LAST:event_rbMomentMeantimeActionPerformed

    private void rbMomentAfterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMomentAfterActionPerformed
        timeMoment();
    }//GEN-LAST:event_rbMomentAfterActionPerformed

    private void btnImportScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportScriptActionPerformed
        if(myASC!=null){
            for(AssStyle as : myASC.getMembers()){
                dcbm.addElement(as);
            }
        }
    }//GEN-LAST:event_btnImportScriptActionPerformed

    private void btnImportFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportFileActionPerformed
        // lAssStyle : Get list of all styles from files
        List<AssStyle> lAssStyle = new ArrayList<>();
        // las : Get list of all styles choosen by the user
        List<AssStyle> las;
        // Clear the list of file filters.
        for (javax.swing.filechooser.FileFilter f : fcStyles.getChoosableFileFilters()){
            fcStyles.removeChoosableFileFilter(f);
        }
        // Add good file filters.
        fcStyles.addChoosableFileFilter(new SubtitleFilter());
        fcStyles.setAccessory(null);
        // Action
        int z = this.fcStyles.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            // Search for styles in a SSA
            if(fcStyles.getSelectedFile().getName().endsWith("ssa")){
                AssIO aio = new AssIO();
                lAssStyle = aio.ExtractSSAStyles(fcStyles.getSelectedFile().getPath());
            }
            // Search for styles in an ASS
            if(fcStyles.getSelectedFile().getName().endsWith("ass")){
                AssIO aio = new AssIO();
                lAssStyle = aio.ExtractASSStyles(fcStyles.getSelectedFile().getPath());
            }
            
            // A new dialog for the choice of styles
            ImportStylesDialog isd = new ImportStylesDialog(frame, true);
            isd.setFilename(fcStyles.getSelectedFile().getName());
            isd.setLocationRelativeTo(null);
            las = isd.showDialog(lAssStyle);
            if(las!=null){
                for(AssStyle as : las){
                    boolean exist = false;
                    for(int i=0;i<dcbm.getSize();i++){
                        AssStyle asDcbm = (AssStyle)dcbm.getElementAt(i);
                        if(asDcbm.getName().equalsIgnoreCase(as.getName())){
                            exist = true;
                        }
                    }
                    if(exist==false){
                        dcbm.addElement(as);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnImportFileActionPerformed

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

    private void popmSelAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelAllActionPerformed
        selectAll();
    }//GEN-LAST:event_popmSelAllActionPerformed

    private void popmClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmClearActionPerformed
        clearAll();
    }//GEN-LAST:event_popmClearActionPerformed

    private void popmColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmColorActionPerformed
        AssColorDialog acd = new AssColorDialog(frame, true);
        acd.setLocationRelativeTo(null);
        String color = acd.showDialog(taOverrides.getSelectedText());
        if (color != null) {
            String s = taOverrides.getText();
            int sStart = taOverrides.getSelectionStart();
            int sEnd = taOverrides.getSelectionEnd();
            taOverrides.setText(s.substring(0, sStart) + color + s.substring(sEnd));
        }
    }//GEN-LAST:event_popmColorActionPerformed

    private void popmAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmAlphaActionPerformed
        AssAlphaDialog aad = new AssAlphaDialog(frame, true);
        aad.setLocationRelativeTo(null);
        String hexa = "FF";
        try {
            hexa = taOverrides.getSelectedText();
        } catch (Exception exc) {
        }
        hexa = aad.showDialog(hexa);
        if (hexa == null) {
            hexa = "";
        }
        hexa = hexa.toUpperCase();
        String s = taOverrides.getText();
        int sStart = taOverrides.getSelectionStart();
        int sEnd = taOverrides.getSelectionEnd();
        taOverrides.setText(s.substring(0, sStart) + hexa + s.substring(sEnd));
    }//GEN-LAST:event_popmAlphaActionPerformed

    private void popmPNGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPNGActionPerformed
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
            String s = taOverrides.getText();
            int sStart = taOverrides.getSelectionStart();
            int sEnd = taOverrides.getSelectionEnd();
            taOverrides.setText(s.substring(0, sStart) + "\"" + png + "\"" + s.substring(sEnd));
        }
    }//GEN-LAST:event_popmPNGActionPerformed

    private void popm_bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_bActionPerformed
        putTextToFocused2("\\b1");
    }//GEN-LAST:event_popm_bActionPerformed

    private void popm_iActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_iActionPerformed
        putTextToFocused2("\\i1");
    }//GEN-LAST:event_popm_iActionPerformed

    private void popm_uActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_uActionPerformed
        putTextToFocused2("\\u1");
    }//GEN-LAST:event_popm_uActionPerformed

    private void popm_sActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_sActionPerformed
        putTextToFocused2("\\s1");
    }//GEN-LAST:event_popm_sActionPerformed

    private void popm_bordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_bordActionPerformed
        putTextToFocused2("\\bord2");
    }//GEN-LAST:event_popm_bordActionPerformed

    private void popm_shadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_shadActionPerformed
        putTextToFocused2("\\shad2");
    }//GEN-LAST:event_popm_shadActionPerformed

    private void popm_beActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_beActionPerformed
        putTextToFocused2("\\be0");
    }//GEN-LAST:event_popm_beActionPerformed

    private void popm_fsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fsActionPerformed
        putTextToFocused2("\\fs50");
    }//GEN-LAST:event_popm_fsActionPerformed

    private void popm_fscxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fscxActionPerformed
        putTextToFocused2("\\fscx100");
    }//GEN-LAST:event_popm_fscxActionPerformed

    private void popm_fscyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fscyActionPerformed
        putTextToFocused2("\\fscy100");
    }//GEN-LAST:event_popm_fscyActionPerformed

    private void popm_fspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fspActionPerformed
        putTextToFocused2("\\fsp0");
    }//GEN-LAST:event_popm_fspActionPerformed

    private void popm_frxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frxActionPerformed
        putTextToFocused2("\\frx0");
    }//GEN-LAST:event_popm_frxActionPerformed

    private void popm_fryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fryActionPerformed
        putTextToFocused2("\\fry0");
    }//GEN-LAST:event_popm_fryActionPerformed

    private void popm_frzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frzActionPerformed
        putTextToFocused2("\\frz0");
    }//GEN-LAST:event_popm_frzActionPerformed

    private void popm_1cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1cActionPerformed
        putTextToFocused2("\\1c&H000000&");
    }//GEN-LAST:event_popm_1cActionPerformed

    private void popm_2cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2cActionPerformed
        putTextToFocused2("\\2c&H000000&");
    }//GEN-LAST:event_popm_2cActionPerformed

    private void popm_3cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3cActionPerformed
        putTextToFocused2("\\3c&H000000&");
    }//GEN-LAST:event_popm_3cActionPerformed

    private void popm_4cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4cActionPerformed
        putTextToFocused2("\\4c&H000000&");
    }//GEN-LAST:event_popm_4cActionPerformed

    private void popm_alphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_alphaActionPerformed
        putTextToFocused2("\\alpha&H00&");
    }//GEN-LAST:event_popm_alphaActionPerformed

    private void popm_1aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1aActionPerformed
        putTextToFocused2("\\1a&H00&");
    }//GEN-LAST:event_popm_1aActionPerformed

    private void popm_2aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2aActionPerformed
        putTextToFocused2("\\2a&H00&");
    }//GEN-LAST:event_popm_2aActionPerformed

    private void popm_3aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3aActionPerformed
        putTextToFocused2("\\3a&H00&");
    }//GEN-LAST:event_popm_3aActionPerformed

    private void popm_4aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4aActionPerformed
        putTextToFocused2("\\4a&H00&");
    }//GEN-LAST:event_popm_4aActionPerformed

    private void popm_clipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_clipActionPerformed
        putTextToFocused2("\\clip(x1,y1,x2,y2)");
    }//GEN-LAST:event_popm_clipActionPerformed

    private void popm_xbordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_xbordActionPerformed
        putTextToFocused2("\\xbord2");
    }//GEN-LAST:event_popm_xbordActionPerformed

    private void popm_ybordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_ybordActionPerformed
        putTextToFocused2("\\ybord2");
    }//GEN-LAST:event_popm_ybordActionPerformed

    private void popm_xshadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_xshadActionPerformed
        putTextToFocused2("\\xshad2");
    }//GEN-LAST:event_popm_xshadActionPerformed

    private void popm_yshadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_yshadActionPerformed
        putTextToFocused2("\\yshad2");
    }//GEN-LAST:event_popm_yshadActionPerformed

    private void popm_blurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_blurActionPerformed
        putTextToFocused2("\\blur0");
    }//GEN-LAST:event_popm_blurActionPerformed

    private void popm_faxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_faxActionPerformed
        putTextToFocused2("\\fax0");
    }//GEN-LAST:event_popm_faxActionPerformed

    private void popm_fayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fayActionPerformed
        putTextToFocused2("\\fay0");
    }//GEN-LAST:event_popm_fayActionPerformed

    private void popm_iclipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_iclipActionPerformed
        putTextToFocused2("\\iclip(x1,y1,x2,y2)");
    }//GEN-LAST:event_popm_iclipActionPerformed

    private void popm_fscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fscActionPerformed
        putTextToFocused2("\\fsc100");
    }//GEN-LAST:event_popm_fscActionPerformed

    private void popm_fsvpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fsvpActionPerformed
        putTextToFocused2("\\fsvp0");
    }//GEN-LAST:event_popm_fsvpActionPerformed

    private void popm_frsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_frsActionPerformed
        putTextToFocused2("\\frs0");
    }//GEN-LAST:event_popm_frsActionPerformed

    private void popm_zActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_zActionPerformed
        putTextToFocused2("\\z0");
    }//GEN-LAST:event_popm_zActionPerformed

    private void popm_distortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_distortActionPerformed
        putTextToFocused2("\\distort(u1,v1,u2,v2,u3,v3)");
    }//GEN-LAST:event_popm_distortActionPerformed

    private void popm_mdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdActionPerformed
        putTextToFocused2("\\md0");
    }//GEN-LAST:event_popm_mdActionPerformed

    private void popm_mdxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdxActionPerformed
        putTextToFocused2("\\mdx0");
    }//GEN-LAST:event_popm_mdxActionPerformed

    private void popm_mdyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdyActionPerformed
        putTextToFocused2("\\mdy0");
    }//GEN-LAST:event_popm_mdyActionPerformed

    private void popm_mdzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_mdzActionPerformed
        putTextToFocused2("\\mdz0");
    }//GEN-LAST:event_popm_mdzActionPerformed

    private void popm_1vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1vcActionPerformed
        putTextToFocused2("\\1vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_1vcActionPerformed

    private void popm_2vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2vcActionPerformed
        putTextToFocused2("\\2vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_2vcActionPerformed

    private void popm_3vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3vcActionPerformed
        putTextToFocused2("\\3vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_3vcActionPerformed

    private void popm_4vcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4vcActionPerformed
        putTextToFocused2("\\4vc(left-top-color,right-top-color,left-bottom-color,right-bottom-color)");
    }//GEN-LAST:event_popm_4vcActionPerformed

    private void popm_1vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1vaActionPerformed
        putTextToFocused2("\\1va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_1vaActionPerformed

    private void popm_2vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2vaActionPerformed
        putTextToFocused2("\\2va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_2vaActionPerformed

    private void popm_3vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3vaActionPerformed
        putTextToFocused2("\\3va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_3vaActionPerformed

    private void popm_4vaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4vaActionPerformed
        putTextToFocused2("\\4va(left-top-transparency,right-top-transparency,left-bottom-transparency,right-bottom-transparency)");
    }//GEN-LAST:event_popm_4vaActionPerformed

    private void popm_1imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_1imgActionPerformed
        putTextToFocused2("\\1img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_1imgActionPerformed

    private void popm_2imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_2imgActionPerformed
        putTextToFocused2("\\2img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_2imgActionPerformed

    private void popm_3imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_3imgActionPerformed
        putTextToFocused2("\\3img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_3imgActionPerformed

    private void popm_4imgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_4imgActionPerformed
        putTextToFocused2("\\4img(path_to_png_file[,xoffset,yoffset])");
    }//GEN-LAST:event_popm_4imgActionPerformed

    private void popm_jitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_jitterActionPerformed
        putTextToFocused2("\\jitter(left,right,up,down,period[,seed])");
    }//GEN-LAST:event_popm_jitterActionPerformed

    private void popm_fnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fnActionPerformed
        putTextToFocused2("\\fnDialog");
    }//GEN-LAST:event_popm_fnActionPerformed

    private void popm_feActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_feActionPerformed
        putTextToFocused2("\\fe1");
    }//GEN-LAST:event_popm_feActionPerformed

    private void popm_qActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_qActionPerformed
        putTextToFocused2("\\q1");
    }//GEN-LAST:event_popm_qActionPerformed

    private void popm_aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_aActionPerformed
        putTextToFocused2("\\a2");
    }//GEN-LAST:event_popm_aActionPerformed

    private void popm_anActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_anActionPerformed
        putTextToFocused2("\\an2");
    }//GEN-LAST:event_popm_anActionPerformed

    private void popm_posActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_posActionPerformed
        putTextToFocused2("\\pos(x,y)");
    }//GEN-LAST:event_popm_posActionPerformed

    private void popm_moveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moveActionPerformed
        putTextToFocused2("\\move(x1,y1,x2,y2[,t1,t2])");
    }//GEN-LAST:event_popm_moveActionPerformed

    private void popm_orgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_orgActionPerformed
        putTextToFocused2("\\org(x,y)");
    }//GEN-LAST:event_popm_orgActionPerformed

    private void popm_fadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fadActionPerformed
        putTextToFocused2("\\fad(t1,t2)");
    }//GEN-LAST:event_popm_fadActionPerformed

    private void popm_fadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_fadeActionPerformed
        putTextToFocused2("\\fade(a1,a2,a3,t1,t2,t3,t4)");
    }//GEN-LAST:event_popm_fadeActionPerformed

    private void popm_clip2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_clip2ActionPerformed
        putTextToFocused2("\\clip([scale,]some drawings)");
    }//GEN-LAST:event_popm_clip2ActionPerformed

    private void popm_iclip2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_iclip2ActionPerformed
        putTextToFocused2("\\iclip(scale,drawing commands)");
    }//GEN-LAST:event_popm_iclip2ActionPerformed

    private void popm_moverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moverActionPerformed
        putTextToFocused2("\\mover(x1,y1,x2,y2,angle1,angle2,radius1,radius2[,t1,t2])");
    }//GEN-LAST:event_popm_moverActionPerformed

    private void popm_moves3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moves3ActionPerformed
        putTextToFocused2("\\moves3(x1,x2,x2,y2,x3,y3[,t1,t2])");
    }//GEN-LAST:event_popm_moves3ActionPerformed

    private void popm_moves4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_moves4ActionPerformed
        putTextToFocused2("\\moves4(x1,x2,x2,y2,x3,y3,x4,y4[,t1,t2])");
    }//GEN-LAST:event_popm_moves4ActionPerformed

    private void popm_movevcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_movevcActionPerformed
        putTextToFocused2("\\movevc(x1,y1)");
    }//GEN-LAST:event_popm_movevcActionPerformed

    private void popm_movevc2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_movevc2ActionPerformed
        putTextToFocused2("\\movevc(x1,y1,x2,y2[,t1,t2])");
    }//GEN-LAST:event_popm_movevc2ActionPerformed

    private void popmSurroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSurroundActionPerformed
        // popup menu - Surround the override expression by braces
        String s = taOverrides.getText();
        taOverrides.setText("{" + s + "}");
    }//GEN-LAST:event_popmSurroundActionPerformed

    private void popmDelSurroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDelSurroundActionPerformed
        // popup menu - Delete all braces of the override expression
        String s = taOverrides.getText();
        s = s.replaceAll("\\{", "");
        s = s.replaceAll("\\}", "");
        taOverrides.setText(s);
    }//GEN-LAST:event_popmDelSurroundActionPerformed

    private void popm_kActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_kActionPerformed
        putTextToFocused2("\\k~%dK/10~");
    }//GEN-LAST:event_popm_kActionPerformed

    private void popm_kfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_kfActionPerformed
        putTextToFocused2("\\kf~%dK/10~");
    }//GEN-LAST:event_popm_kfActionPerformed

    private void popm_koActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_koActionPerformed
        putTextToFocused2("\\ko~%dK/10~");
    }//GEN-LAST:event_popm_koActionPerformed

    private void popm_tActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_tActionPerformed
        putTextToFocused2("\\t([t1,t2,][accel,]style modifiers)");
    }//GEN-LAST:event_popm_tActionPerformed

    private void popm_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popm_resetActionPerformed
        putTextToFocused2("\\r");
    }//GEN-LAST:event_popm_resetActionPerformed

    private void popmIntCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmIntCalcActionPerformed
        putTextToFocused2("~your calculation here~");
    }//GEN-LAST:event_popmIntCalcActionPerformed

    private void popmFloCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFloCalcActionPerformed
        putTextToFocused2("`your calculation here`");
    }//GEN-LAST:event_popmFloCalcActionPerformed

    private void popmDrawingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawingActionPerformed
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
                String s = taOverrides.getText();
                int sStart = taOverrides.getSelectionStart();
                int sEnd = taOverrides.getSelectionEnd();
                taOverrides.setText(s.substring(0, sStart) + draw + s.substring(sEnd));
            }
        }
    }//GEN-LAST:event_popmDrawingActionPerformed

    private void popmCut1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCut1ActionPerformed
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(epVariables.getSelectedText());
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_popmCut1ActionPerformed

    private void popmCopy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCopy1ActionPerformed
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(epVariables.getSelectedText());
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_popmCopy1ActionPerformed

    private void popmPaste1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPaste1ActionPerformed
        try{
            Clipboard cb = new Clipboard();
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart)+cb.CPaste()+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }//GEN-LAST:event_popmPaste1ActionPerformed

    private void popmDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDelete1ActionPerformed
        try{
            String s = epVariables.getText();
            int sStart = epVariables.getSelectionStart();
            int sEnd = epVariables.getSelectionEnd();
            epVariables.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
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
                + "  value = \"\"\n\n"
                + "  return value\n"
                + "end\n";
        String s = epVariables.getText();
        int sStart = epVariables.getSelectionStart();
        int sEnd = epVariables.getSelectionEnd();
        epVariables.setText(s.substring(0, sStart) + code + s.substring(sEnd));
    }//GEN-LAST:event_popmCodeDefActionPerformed

    private void cbStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStyleActionPerformed
        AssStyle as = (AssStyle)cbStyle.getSelectedItem();
        java.awt.Font f = as.getFont(); f = f.deriveFont((int)as.getFontsize());
        lblPreviewFont.setFont(f);
    }//GEN-LAST:event_cbStyleActionPerformed

    private void rbModeNormalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbModeNormalMouseClicked
        rbSubModeSentence.setEnabled(true);
        rbSubModeSyllable.setEnabled(true);
    }//GEN-LAST:event_rbModeNormalMouseClicked

    private void rbModePeriodicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbModePeriodicMouseClicked
        rbSubModeSentence.setEnabled(false);
        rbSubModeSyllable.setEnabled(false);
    }//GEN-LAST:event_rbModePeriodicMouseClicked

    private void rbModeRandomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbModeRandomMouseClicked
        rbSubModeSentence.setEnabled(false);
        rbSubModeSyllable.setEnabled(false);
    }//GEN-LAST:event_rbModeRandomMouseClicked

    private void rbModeSymmetricMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbModeSymmetricMouseClicked
        rbSubModeSentence.setEnabled(false);
        rbSubModeSyllable.setEnabled(false);
    }//GEN-LAST:event_rbModeSymmetricMouseClicked

    // <editor-fold defaultstate="collapsed" desc=" Popup methods ">

    /** <p>Cut.<br />Coupe.</p> */
    private void cut(){
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(taOverrides.getSelectedText());
            String s = taOverrides.getText();
            int sStart = taOverrides.getSelectionStart();
            int sEnd = taOverrides.getSelectionEnd();
            taOverrides.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }

    /** <p>Copy.<br />Copie.</p> */
    private void copy(){
        try{
            Clipboard cb = new Clipboard();
            cb.CCopy(taOverrides.getSelectedText());
        }catch(Exception exc){/*no selected text or another thing*/}
    }

    /** <p>Paste.<br />Colle.</p> */
    private void paste(){
        try{
            Clipboard cb = new Clipboard();
            String s = taOverrides.getText();
            int sStart = taOverrides.getSelectionStart();
            int sEnd = taOverrides.getSelectionEnd();
            taOverrides.setText(s.substring(0, sStart)+cb.CPaste()+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }

    /** <p>Delete.<br />Supprime.</p> */
    private void delete(){
        try{
            String s = taOverrides.getText();
            int sStart = taOverrides.getSelectionStart();
            int sEnd = taOverrides.getSelectionEnd();
            taOverrides.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch(Exception exc){/*no selected text or another thing*/}
    }

    /** <p>Select all.<br />Sélectionne tout.</p> */
    private void selectAll(){
        taOverrides.setSelectionStart(0);
        taOverrides.setSelectionEnd(taOverrides.getText().length());
    }

    /** <p>Clear all.<br />Efface tout.</p> */
    private void clearAll(){
        taOverrides.setText("");
    }
    
    /** <p>Puts a text to tfFocused.<br />
     * Envoie un texte à tfFocused.</p> */
    private void putTextToFocused2(String text){
        try{
            String s = taOverrides.getText();
            int sStart = taOverrides.getSelectionStart();
            int sEnd = taOverrides.getSelectionEnd();
            taOverrides.setText(s.substring(0, sStart)+text+s.substring(sEnd));
        }catch(Exception exc){
            
        }
    }
    
    /** <p>Puts a text to tfFocused.<br />
     * Envoie un texte à tfFocused.</p> */
    private void putTextToRubyEditor(String text){
        try{
            String s = taOverrides.getText();
            int sStart = taOverrides.getSelectionStart();
            int sEnd = taOverrides.getSelectionEnd();
            taOverrides.setText(s.substring(0, sStart)+text+s.substring(sEnd));
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

    // </editor-fold>    
    
    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
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
    
    public enum SaveState{
        DISABLE, ENABLE;
    }
    
    private void init(){
        tfName.setText("");
        tfAuthors.setText("");
        tfComments.setText("");
        tfPreview.setText("");
        tfCollection.setText("");
        
        dcbm = new DefaultComboBoxModel();
        cbStyle.setModel(dcbm);
        
        String[] fxHead = new String[]{"Layer", "Effects"};
        
        dtm = new DefaultTableModel(null,fxHead){
            Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                    false, true};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        
        tableEffects.setModel(dtm);
        
        TableColumn column;
        for (int i = 0; i < 2; i++) {
            column = tableEffects.getColumnModel().getColumn(i);
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
        
        bp = ButtonPressed.NONE;
        
    }
    
    public ParticleObject showDialog(ParticleObject po){
        setParticleObject(po);
//        getOKButtonState();
        setVisible(true);
        
        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return getParticleObject();
        }else{
            return po;
        }
    }
    
    private void timeMoment(){
        if(rbMomentBefore.isSelected() | rbMomentAfter.isSelected()){
            tfTime.setEnabled(true);
        }else{
            tfTime.setEnabled(false);
        }
    }
    
    public boolean isOKButtonPressed(){
        if(bp==ButtonPressed.OK_BUTTON){return true;}
        return false;
    }
    
    public boolean isSaveSelected(){
        if(saveState==SaveState.ENABLE){return true;}
        return false;
    }
    
    public void setParticleObject(ParticleObject po){
        particleObject = po;
        tfAuthors.setText(po.getAuthor());
        tfCollection.setText(po.getCollect());
        setCommands(po.getCommands(),true);
        tfComments.setText(po.getDescription());
        tfPreview.setText(po.getImage());
        setMoment(po.getMoment());
        tfName.setText(po.getName());
        setFirstLayer(po.getFirstLayer());
        setSelectedStyle(po.getStyle());
        setTime(po.getTime());
//        setPositionCorrection(po.getPosCorrection());
//        setSpaceCorrection(po.getSpaCorrection());
        setVideoWidth(po.getVideoWidth());
        setVideoHeight(po.getVideoHeight());
        setPosY(po.getPosY());
        setMode(po.getMode());
        epVariables.setText(po.getRubyCode());
        setSType(po.getType());
        
        java.io.File fImage = new java.io.File(po.getImage());
        if(fImage.exists()){
            ImageIcon ii0 = new ImageIcon(fImage.getPath());
            lblMiscPreview.setIcon(ii0);
            lblMiscPreview.setSize(ii0.getIconWidth(), ii0.getIconHeight());
            lblMiscPreview.setLocation(panMiscPreview.getWidth()/2-lblMiscPreview.getWidth()/2,
                    panMiscPreview.getHeight()/2-lblMiscPreview.getHeight()/2);
        }
    }
    
    public ParticleObject getParticleObject(){
        particleObject.setAuthor(tfAuthors.getText());
        particleObject.setCollect(tfCollection.getText());
        particleObject.setCommands(getCommands());
        particleObject.setDescription(tfComments.getText());
        particleObject.setImage(tfPreview.getText());
        particleObject.setMoment(getMoment());
        particleObject.setName(tfName.getText());
        particleObject.setFirstLayer(getFirstLayer());
        particleObject.setStyle(getSelectedStyle());
        particleObject.setTime(getTime());
//        particleObject.setPosCorrection(getPositionCorrection());
//        particleObject.setSpaCorrection(getSpaceCorrection());
        particleObject.setVideoWidth(getVideoWidth());
        particleObject.setVideoHeight(getVideoHeight());
        particleObject.setPosY(getPosY());
        particleObject.setMode(getMode());
        particleObject.setRubyCode(epVariables.getText());
        particleObject.setType(getSType());
        return particleObject;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Méthodes ">
    
    public void setStyles(AssStyleCollection asc){
        myASC = asc;
    }
    
    public void setSelectedStyle(String style){
        if(style.isEmpty()==false){
            AssStyle as = new AssStyle();
            as.fromAssStyleString(style);
            dcbm.addElement(as);
            dcbm.setSelectedItem(as);
        }        
    }
    
    public String getSelectedStyle(){
        Object o = dcbm.getSelectedItem();
        if(o instanceof AssStyle){
            AssStyle as = (AssStyle)o;
            return as.toAssStyleString();
        }else{
            return "";
        }        
    }
    
    public void setParticleName(String name){
        tfName.setText(name);
    }
    
    public String getParticleName(){
        return tfName.getText();
    }
    
    public void setFirstLayer(String firstlayer){
        tfFirstLayer.setText(firstlayer);
    }
    
    public String getFirstLayer(){
        return tfFirstLayer.getText();
    }
    
    public void setMoment(String moment){
        if(moment.equalsIgnoreCase("before")){
            rbMomentBefore.setSelected(true);
        }else if(moment.equalsIgnoreCase("meantime")){
            rbMomentMeantime.setSelected(true);
        }else if(moment.equalsIgnoreCase("after")){
            rbMomentAfter.setSelected(true);
        }
    }
    
    public String getMoment(){
        if(rbMomentBefore.isSelected()){
            return "before";
        }else if(rbMomentMeantime.isSelected()){
            return "meantime";
        }else{//rbMomentAfter.isSelected()
            return "after";
        }
    }
    
    public void setTime(String time){
        tfTime.setText(time);
    }
    
    public String getTime(){
        return tfTime.getText();
    }
    
//    public void setPositionCorrection(String positioncorrection){
//        tfPositionCorrection.setText(positioncorrection);
//    }
//    
//    public String getPositionCorrection(){
//        return tfPositionCorrection.getText();
//    }
//    
//    public void setSpaceCorrection(String spacecorrection){
//        tfSpaceCorrection.setText(spacecorrection);
//    }
//    
//    public String getSpaceCorrection(){
//        return tfSpaceCorrection.getText();
//    }
    
    public void setVideoWidth(String videowidth){
        tfHorizontalVSize.setText(videowidth);
    }
    
    public String getVideoWidth(){
        return tfHorizontalVSize.getText();
    }
    
    public void setVideoHeight(String videoheight){
        tfVerticalVSize.setText(videoheight);
    }
    
    public String getVideoHeight(){
        return tfVerticalVSize.getText();
    }
    
    /** <p>Extracts each line of commands from a xml string content.<br />
     * Extrait chaque ligne de commandes à partir du contenu du xml.</p> */
    public void setCommands(String commands, boolean refresh){
        if(commands.contains("Â§")){
            commands = commands.replaceAll("Â§", "§");
        }
        String line[] = commands.split("§");
        for (int i=0; i<line.length; i++){
            if(line[i].isEmpty()==false){
                dtm.addRow(new Object[]{dtm.getRowCount(),line[i]});
            }            
        }
        if(refresh==true && dtm.getRowCount()>0){//Shows 1st command line.
            try{
                String s = (String)dtm.getValueAt(0, 1);                
                taOverrides.setText(s);
            }catch(Exception exc){
            }
        }
    }

    /** <p>Return each line of commands from a xml preset.<br />
     * Retourne chaque ligne de commands d'un XFX</p> */
    public String getCommands(){
        String commands = "";
        for(int i=0;i<dtm.getRowCount();i++){
            if(i+1<dtm.getRowCount()){
                commands += (String)dtm.getValueAt(i, 1) + "§";
            }else{
                commands += (String)dtm.getValueAt(i, 1);
            }
        }
        if(commands.isEmpty()){
            return commands;
        }else{
            return "§"+commands;
        }
        
    }
    
    public void setAuthor(String author){
        tfAuthors.setText(author);
    }
    
    public String getAuthor(){
        return tfAuthors.getText();
    }
    
    public void setComment(String comment){
        tfComments.setText(comment);
    }
    
    public String getComment(){
        return tfComments.getText();
    }
    
    public void setPreview(String preview){
        tfPreview.setText(preview);
    }
    
    public String getPreview(){
        return tfPreview.getText();
    }
    
    public void setCollection(String collection){
        tfCollection.setText(collection);
    }
    
    public String getCollection(){
        return tfCollection.getText();
    }
    
    public void setPosY(String posY){
        tfPosY.setText(posY);
    }
    
    public String getPosY(){
        return tfPosY.getText();
    }
    
    public void setMode(String mode){
        if(mode.equalsIgnoreCase("Normal")){
            rbModeNormal.setSelected(true);
        }else if(mode.equalsIgnoreCase("Periodic")){
            rbModePeriodic.setSelected(true);
        }else if(mode.equalsIgnoreCase("Random")){
            rbModeRandom.setSelected(true);
        }else if(mode.equalsIgnoreCase("Symmetric")){
            rbModeSymmetric.setSelected(true);
        }else{
            rbModeNormal.setSelected(true);
        }
    }
    
    public String getMode(){
        if(rbModeNormal.isSelected()){
            return "Normal";
        }else if(rbModePeriodic.isSelected()){
            return "Periodic";
        }else if(rbModeRandom.isSelected()){
            return "Random";
        }else{ //if(rbModeSymmetric.isSelected())
            return "Symmetric";
        }
    }
    
    public void setSType(String type){
        if(type.equalsIgnoreCase("Sentence")){
            rbSubModeSentence.setSelected(true);
        }else{
            rbSubModeSyllable.setSelected(true);
        }
    }
    
    public String getSType(){
        if(rbSubModeSentence.isSelected()){
            return "Sentence";
        }else{ //if(rbModeSymmetric.isSelected())
            return "Syllable";
        }
    }
    
    // </editor-fold>
    
    private void updateASSTextArea(JTextArea ta, Highlighter h){
        Pattern p; Matcher m;
        
        //All characters in black (normalPainter)
        p = Pattern.compile(".*");
        m = p.matcher(ta.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), normalPainter);
            } catch (BadLocationException ex) {}
        }

        //All keywords in blue (keywordPainter)
        p = Pattern.compile("\\\\[a-z]+");
        m = p.matcher(ta.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), keywordPainter);
            } catch (BadLocationException ex) {}
        }

        //All numbers in magenta (numberPainter)
        p = Pattern.compile("[0-9]+");
        m = p.matcher(ta.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), numberPainter);
            } catch (BadLocationException ex) {}
        }

        //All brackets in green (symbolPainter)
        p = Pattern.compile("\\{*\\}*");
        m = p.matcher(ta.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), symbolPainter);
            } catch (BadLocationException ex) {}
        }
        
        //All hexadecimals in gray (hexaPainter)
        p = Pattern.compile("&H[A-Fa-f0-9]+");
        m = p.matcher(ta.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start()+2, m.end(), hexaPainter);
            } catch (BadLocationException ex) {}
        }
        
        //All user variables in red (uvarPainter)
        p = Pattern.compile("\\$[a-z]+");
        m = p.matcher(ta.getText());
        while(m.find()){
            try {
                h.addHighlight(m.start(), m.end(), uvarPainter);
            } catch (BadLocationException ex) {}
        }
        
        //All local variables highlighted in yellow (lvarPainter)
        p = Pattern.compile("%[A-Za-z]+");
        m = p.matcher(ta.getText());
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
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParticleDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ParticleDialog dialog = new ParticleDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup bgModes;
    private javax.swing.ButtonGroup bgMoment;
    private javax.swing.ButtonGroup bgSubModes;
    private javax.swing.JButton btnAddLayer;
    private javax.swing.JButton btnChangeLayer;
    private javax.swing.JButton btnChangePreview;
    private javax.swing.JButton btnDeleteLayer;
    private javax.swing.JButton btnGetLayer;
    private javax.swing.JButton btnImportFile;
    private javax.swing.JButton btnImportScript;
    private javax.swing.JCheckBox cbSaveEffects;
    private javax.swing.JComboBox cbStyle;
    private javax.swing.JEditorPane epVariables;
    private javax.swing.JFileChooser fcPreview;
    private javax.swing.JFileChooser fcStyles;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JLabel lblAuthors;
    private javax.swing.JLabel lblCollection;
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblFirstLayer;
    private javax.swing.JLabel lblLayersDetails;
    private javax.swing.JLabel lblMiscPreview;
    private javax.swing.JLabel lblMoment;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblOverrides;
    private javax.swing.JLabel lblPosY;
    private javax.swing.JLabel lblPreview;
    private javax.swing.JLabel lblPreviewFont;
    private javax.swing.JLabel lblStyle;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblVideoSize;
    private javax.swing.JLabel lblXVSize;
    private javax.swing.JPanel panEffects;
    private javax.swing.JPanel panMisc;
    private javax.swing.JPanel panMiscPreview;
    private javax.swing.JPanel panPreviewStyle;
    private javax.swing.JPanel panSettings;
    private javax.swing.JPanel panStyle;
    private javax.swing.JPanel panVariables;
    private javax.swing.JPopupMenu popOverrides;
    private javax.swing.JPopupMenu popOverrides1;
    private javax.swing.JMenuItem popmAlpha;
    private javax.swing.JMenuItem popmAlpha1;
    private javax.swing.JMenuItem popmClear;
    private javax.swing.JMenuItem popmClear1;
    private javax.swing.JMenuItem popmCodeDef;
    private javax.swing.JMenuItem popmCodeInit;
    private javax.swing.JMenuItem popmColor;
    private javax.swing.JMenuItem popmColor1;
    private javax.swing.JMenuItem popmCopy;
    private javax.swing.JMenuItem popmCopy1;
    private javax.swing.JMenuItem popmCut;
    private javax.swing.JMenuItem popmCut1;
    private javax.swing.JMenuItem popmDelSurround;
    private javax.swing.JMenuItem popmDelete;
    private javax.swing.JMenuItem popmDelete1;
    private javax.swing.JMenuItem popmDrawing;
    private javax.swing.JMenuItem popmDrawing1;
    private javax.swing.JMenuItem popmFloCalc;
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
    private javax.swing.JMenuItem popmSelAll;
    private javax.swing.JMenuItem popmSelAll1;
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
    private javax.swing.JRadioButton rbModeNormal;
    private javax.swing.JRadioButton rbModePeriodic;
    private javax.swing.JRadioButton rbModeRandom;
    private javax.swing.JRadioButton rbModeSymmetric;
    private javax.swing.JRadioButton rbMomentAfter;
    private javax.swing.JRadioButton rbMomentBefore;
    private javax.swing.JRadioButton rbMomentMeantime;
    private javax.swing.JRadioButton rbSubModeSentence;
    private javax.swing.JRadioButton rbSubModeSyllable;
    private javax.swing.JScrollPane spEffects;
    private javax.swing.JScrollPane spOverrides;
    private javax.swing.JScrollPane spVariables;
    private javax.swing.JTextArea taHelpPlease;
    private javax.swing.JTextArea taOverrides;
    private javax.swing.JTable tableEffects;
    private javax.swing.JTextField tfAuthors;
    private javax.swing.JTextField tfCollection;
    private javax.swing.JTextField tfComments;
    private javax.swing.JTextField tfFirstLayer;
    private javax.swing.JTextField tfHorizontalVSize;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfPosY;
    private javax.swing.JTextField tfPreview;
    private javax.swing.JTextField tfTime;
    private javax.swing.JTextField tfVerticalVSize;
    private javax.swing.JTabbedPane tpParticle;
    // End of variables declaration//GEN-END:variables
}
