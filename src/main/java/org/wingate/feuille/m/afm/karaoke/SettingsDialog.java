/*
 * Copyright (C) 2024 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wingate.feuille.m.afm.karaoke;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.wingate.feuille.m.afm.karaoke.io.CodeIO;
import org.wingate.feuille.m.afm.karaoke.io.EffectsIO;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllableBasicSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllableComplexSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllablePeriodSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllableRandomSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllableSymSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXAbstract;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXCode;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.GenericFileFilter;

/**
 *
 * @author util2
 */
public class SettingsDialog extends javax.swing.JDialog {
    
    private DialogResult dialogResult = DialogResult.Unknown;

    private final DefaultComboBoxModel cboxModel = new DefaultComboBoxModel();
    private final DefaultListModel templatesModel = new DefaultListModel();
    
    /**
     * Creates new form SettingsDialog
     * @param parent
     * @param modal
     */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        cboxAFMEffects.setModel(cboxModel);
        cboxModel.addElement(new LineSyllableBasicSFX());
        cboxModel.addElement(new LineSyllableComplexSFX());
        cboxModel.addElement(new LineSyllablePeriodSFX());
        cboxModel.addElement(new LineSyllableRandomSFX());
        cboxModel.addElement(new LineSyllableSymSFX());
        
        listTemplates.setModel(templatesModel);
        listTemplates.setCellRenderer(new TemplateListCellRenderer());
        
        fcSaveEffects.setAcceptAllFileFilterUsed(false);       
        fcOpenEffects.setAcceptAllFileFilterUsed(false);
        fcSaveEffects.addChoosableFileFilter(new GenericFileFilter("sfx", "Feuille effect templates")); 
        fcOpenEffects.addChoosableFileFilter(new GenericFileFilter("sfx", "Feuille effect templates"));
        
        fcSaveCode.setAcceptAllFileFilterUsed(false);
        fcOpenCode.setAcceptAllFileFilterUsed(false);
        fcSaveCode.addChoosableFileFilter(new GenericFileFilter("js", "JavaScript files"));
        fcOpenCode.addChoosableFileFilter(new GenericFileFilter("js", "JavaScript files"));
        fcSaveCode.addChoosableFileFilter(new GenericFileFilter("lua", "LUA files"));
        fcOpenCode.addChoosableFileFilter(new GenericFileFilter("lua", "LUA files"));
        fcSaveCode.addChoosableFileFilter(new GenericFileFilter("py", "Python files"));
        fcOpenCode.addChoosableFileFilter(new GenericFileFilter("py", "Python files"));
        fcSaveCode.addChoosableFileFilter(new GenericFileFilter("rb", "Ruby files"));
        fcOpenCode.addChoosableFileFilter(new GenericFileFilter("rb", "Ruby files"));
    }
    
    public void showDialog(){
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }
    
    public List<SFXAbstract> getTemplates(){
        final List<SFXAbstract> tps = new ArrayList<>();
        
        int[] indices = listTemplates.getSelectedIndices();
        
        for(int i=0; i<indices.length; i++){
            if(templatesModel.get(i) instanceof SFXAbstract sfx){
                tps.add(sfx);
            }
        }
        
        return tps;
    }
    
    private void tryAddTemplate(SFXAbstract sfx, String humanName){        
        boolean add = true;
        
        // On vérifie si on n'a aucune autre entrée identique
        for(Object o : templatesModel.toArray()){
            if(o instanceof SFXAbstract a){
                if(a.getHumanName().equalsIgnoreCase(humanName)){
                    // On a trouvé une correspondance,
                    // on dit de ne pas copier cette entrée
                    add = false;
                    break;
                }
            }            
        }
        
        // Si on a une entrée non identique
        if(add == true){
            // On crée de nouveaux objets pour ne pas avoir de doublons
            // qui se changent tels des jumeaux
            switch(sfx){
                case LineSyllableBasicSFX a -> {
                    LineSyllableBasicSFX x = new LineSyllableBasicSFX();
                    x.setHumanName(humanName);
                    templatesModel.addElement(x);
                }
                case LineSyllableComplexSFX a -> {
                    LineSyllableComplexSFX x = new LineSyllableComplexSFX();
                    x.setHumanName(humanName);
                    templatesModel.addElement(x);
                }
                case LineSyllablePeriodSFX a -> {
                    LineSyllablePeriodSFX x = new LineSyllablePeriodSFX();
                    x.setHumanName(humanName);
                    templatesModel.addElement(x);
                }
                case LineSyllableRandomSFX a -> {
                    LineSyllableRandomSFX x = new LineSyllableRandomSFX();
                    x.setHumanName(humanName);
                    templatesModel.addElement(x);
                }
                case LineSyllableSymSFX a -> {
                    LineSyllableSymSFX x = new LineSyllableSymSFX();
                    x.setHumanName(humanName);
                    templatesModel.addElement(x);
                }
                default -> {}
            }
        }
        
        if(add == false){
            JOptionPane.showMessageDialog(
                    new javax.swing.JFrame(),
                    "Object already exists !",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void addText(String s){
        String text = textPanelCommands.getText();
        if(text.isEmpty()){
            textPanelCommands.setText(s);
        }else{
            int insertIndex = textPanelCommands.getCaretPosition();
            if(insertIndex == -1) insertIndex = text.length() - 1;
            if(insertIndex + 1 < text.length() - 1){
                textPanelCommands.setText(
                        text.substring(0, insertIndex) + s + text.substring(insertIndex)
                );
            }else{
                textPanelCommands.setText(
                        text.substring(0, insertIndex) + s
                );
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popTemplates = new javax.swing.JPopupMenu();
        popmRemove = new javax.swing.JMenuItem();
        popCommands = new javax.swing.JPopupMenu();
        popmSave = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        popmenuTriggers = new javax.swing.JMenu();
        popmenuTriggersMs = new javax.swing.JMenu();
        popmMsLetterStart = new javax.swing.JMenuItem();
        popmMsLetterEnd = new javax.swing.JMenuItem();
        popmMsLetterMiddle = new javax.swing.JMenuItem();
        popmMsLetterDuration = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        popmMsStart = new javax.swing.JMenuItem();
        popmMsEnd = new javax.swing.JMenuItem();
        popmMsMiddle = new javax.swing.JMenuItem();
        popmMsDuration = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        popmMsSentenceStart = new javax.swing.JMenuItem();
        popmMsSentenceEnd = new javax.swing.JMenuItem();
        popmMsSentenceMiddle = new javax.swing.JMenuItem();
        popmMsSentenceDuration = new javax.swing.JMenuItem();
        popmenuTriggersCs = new javax.swing.JMenu();
        popmCsLetterStart = new javax.swing.JMenuItem();
        popmCsLetterEnd = new javax.swing.JMenuItem();
        popmCsLetterMiddle = new javax.swing.JMenuItem();
        popmCsLetterDuration = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        popmCsStart = new javax.swing.JMenuItem();
        popmCsEnd = new javax.swing.JMenuItem();
        popmCsMiddle = new javax.swing.JMenuItem();
        popmCsDuration = new javax.swing.JMenuItem();
        popmTriggerLetter = new javax.swing.JMenuItem();
        popmTriggerSyllable = new javax.swing.JMenuItem();
        popmTriggerSentence = new javax.swing.JMenuItem();
        popmenuList = new javax.swing.JMenu();
        popmFontReset = new javax.swing.JMenuItem();
        popmWrapLine = new javax.swing.JMenuItem();
        popmFontEncoding = new javax.swing.JMenuItem();
        popmenuKaraoke = new javax.swing.JMenu();
        popmKaraokeMinusK = new javax.swing.JMenuItem();
        popmKaraokeBigK = new javax.swing.JMenuItem();
        popmKaraokeFill = new javax.swing.JMenuItem();
        popmKaraokeOutline = new javax.swing.JMenuItem();
        popmenuAnimation = new javax.swing.JMenu();
        popmAnimateSentence = new javax.swing.JMenuItem();
        popmAnimateSentenceWithAcc = new javax.swing.JMenuItem();
        popmAnimateTime = new javax.swing.JMenuItem();
        popmAnimateWithAcc = new javax.swing.JMenuItem();
        popmenuFont = new javax.swing.JMenu();
        popmFontName = new javax.swing.JMenuItem();
        popmFontSize = new javax.swing.JMenuItem();
        popmFontScale = new javax.swing.JMenuItem();
        popmFontScaleX = new javax.swing.JMenuItem();
        popmFontScaleY = new javax.swing.JMenuItem();
        popmFontRX = new javax.swing.JMenuItem();
        popmFontRY = new javax.swing.JMenuItem();
        popmFontRZ = new javax.swing.JMenuItem();
        popmFontRZSimplified = new javax.swing.JMenuItem();
        popmFontSpacing = new javax.swing.JMenuItem();
        popmFontShearX = new javax.swing.JMenuItem();
        popmFontShearY = new javax.swing.JMenuItem();
        popmenuFontAttribute = new javax.swing.JMenu();
        popmFontAttrBold = new javax.swing.JMenuItem();
        popmFontAttrItalic = new javax.swing.JMenuItem();
        popmFontAttrUnderline = new javax.swing.JMenuItem();
        popmFontAttrStrikeOut = new javax.swing.JMenuItem();
        popmFontAttrBord = new javax.swing.JMenuItem();
        popmFontAttrXBord = new javax.swing.JMenuItem();
        popmFontAttrYBord = new javax.swing.JMenuItem();
        popmFontAttrShad = new javax.swing.JMenuItem();
        popmFontAttrXShad = new javax.swing.JMenuItem();
        popmFontAttrYShad = new javax.swing.JMenuItem();
        popmenuPosition = new javax.swing.JMenu();
        popmPosA = new javax.swing.JMenuItem();
        popmPosAn = new javax.swing.JMenuItem();
        popmPosPos = new javax.swing.JMenuItem();
        popmPosMove = new javax.swing.JMenuItem();
        popmPosOrg = new javax.swing.JMenuItem();
        popmenuDisplay = new javax.swing.JMenu();
        popmDisplayBlurEdge = new javax.swing.JMenuItem();
        popmDisplayBlur = new javax.swing.JMenuItem();
        popmDisplayPrimaryColor = new javax.swing.JMenuItem();
        popmDisplayTextColor = new javax.swing.JMenuItem();
        popmDisplayKaraokeColor = new javax.swing.JMenuItem();
        popmDisplayOutlineColor = new javax.swing.JMenuItem();
        popmDisplayShadowColor = new javax.swing.JMenuItem();
        popmDisplayAlpha = new javax.swing.JMenuItem();
        popmDisplayTextAlpha = new javax.swing.JMenuItem();
        popmDisplayKaraokeAlpha = new javax.swing.JMenuItem();
        popmDisplayOutlineAlpha = new javax.swing.JMenuItem();
        popmDisplayShadowAlpha = new javax.swing.JMenuItem();
        popmDisplayFad = new javax.swing.JMenuItem();
        popmDisplayFade = new javax.swing.JMenuItem();
        popmenuVisibility = new javax.swing.JMenu();
        popmVisibilityClip = new javax.swing.JMenuItem();
        popmVisibilityIClip = new javax.swing.JMenuItem();
        popmenuDrawing = new javax.swing.JMenu();
        popmDrawP = new javax.swing.JMenuItem();
        popmDrawBaselineOffset = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        popmDrawMove = new javax.swing.JMenuItem();
        popmDrawGoto = new javax.swing.JMenuItem();
        popmDrawLine = new javax.swing.JMenuItem();
        popmDrawCubic = new javax.swing.JMenuItem();
        popmDrawSpline = new javax.swing.JMenuItem();
        popmDrawCloseSpline = new javax.swing.JMenuItem();
        popmDrawExtendSpline = new javax.swing.JMenuItem();
        fcSaveEffects = new javax.swing.JFileChooser();
        fcOpenEffects = new javax.swing.JFileChooser();
        fcSaveCode = new javax.swing.JFileChooser();
        fcOpenCode = new javax.swing.JFileChooser();
        popCode = new javax.swing.JPopupMenu();
        popmCodeCut = new javax.swing.JMenuItem();
        popmCodeCopy = new javax.swing.JMenuItem();
        popmCodePaste = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        popmCodeScriptName = new javax.swing.JMenuItem();
        popmCodeAuthor = new javax.swing.JMenuItem();
        popmCodeVersion = new javax.swing.JMenuItem();
        popmCodeDescription = new javax.swing.JMenuItem();
        popmCodeUpdateDetails = new javax.swing.JMenuItem();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cboxAFMEffects = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listTemplates = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textPanelCommands = new javax.swing.JTextPane();
        jToolBar1 = new javax.swing.JToolBar();
        btnNewCode = new javax.swing.JButton();
        btnOpenCode = new javax.swing.JButton();
        btnSaveCode = new javax.swing.JButton();
        tabpCode = new javax.swing.JTabbedPane();
        btnSaveEffects = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();

        popmRemove.setText("Remove selected elements");
        popmRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmRemoveActionPerformed(evt);
            }
        });
        popTemplates.add(popmRemove);

        popmSave.setText("Register commands");
        popmSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSaveActionPerformed(evt);
            }
        });
        popCommands.add(popmSave);
        popCommands.add(jSeparator1);

        popmenuTriggers.setText("Variable");

        popmenuTriggersMs.setText("In milliseconds");

        popmMsLetterStart.setText("Letter start (ms) - %lsK");
        popmMsLetterStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsLetterStartActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsLetterStart);

        popmMsLetterEnd.setText("Letter end (ms) - %leK");
        popmMsLetterEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsLetterEndActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsLetterEnd);

        popmMsLetterMiddle.setText("Letter middle (ms) - %lmK");
        popmMsLetterMiddle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsLetterMiddleActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsLetterMiddle);

        popmMsLetterDuration.setText("Letter duration (ms) - %ldK");
        popmMsLetterDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsLetterDurationActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsLetterDuration);
        popmenuTriggersMs.add(jSeparator4);

        popmMsStart.setText("Syllable start (ms) - %sK");
        popmMsStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsStartActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsStart);

        popmMsEnd.setText("Syllable end (ms) - %eK");
        popmMsEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsEndActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsEnd);

        popmMsMiddle.setText("Syllable middle (ms) - %mK");
        popmMsMiddle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsMiddleActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsMiddle);

        popmMsDuration.setText("Syllable duration (ms) - %dK");
        popmMsDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsDurationActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsDuration);
        popmenuTriggersMs.add(jSeparator3);

        popmMsSentenceStart.setText("Sentence start (ms) - %ssK");
        popmMsSentenceStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsSentenceStartActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsSentenceStart);

        popmMsSentenceEnd.setText("Sentence end (ms) - %seK");
        popmMsSentenceEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsSentenceEndActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsSentenceEnd);

        popmMsSentenceMiddle.setText("Sentence middle (ms) - %smK");
        popmMsSentenceMiddle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsSentenceMiddleActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsSentenceMiddle);

        popmMsSentenceDuration.setText("Sentence duration (ms) - %sdK");
        popmMsSentenceDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmMsSentenceDurationActionPerformed(evt);
            }
        });
        popmenuTriggersMs.add(popmMsSentenceDuration);

        popmenuTriggers.add(popmenuTriggersMs);

        popmenuTriggersCs.setText("In centiseconds");

        popmCsLetterStart.setText("Letter start (cs) - %lcsK");
        popmCsLetterStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCsLetterStartActionPerformed(evt);
            }
        });
        popmenuTriggersCs.add(popmCsLetterStart);

        popmCsLetterEnd.setText("Letter end (cs) - %lceK");
        popmCsLetterEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCsLetterEndActionPerformed(evt);
            }
        });
        popmenuTriggersCs.add(popmCsLetterEnd);

        popmCsLetterMiddle.setText("Letter middle (cs) - %lcmK");
        popmCsLetterMiddle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCsLetterMiddleActionPerformed(evt);
            }
        });
        popmenuTriggersCs.add(popmCsLetterMiddle);

        popmCsLetterDuration.setText("Letter duration (cs) - %lcdK");
        popmCsLetterDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCsLetterDurationActionPerformed(evt);
            }
        });
        popmenuTriggersCs.add(popmCsLetterDuration);
        popmenuTriggersCs.add(jSeparator5);

        popmCsStart.setText("Syllable start (cs) - %csK");
        popmCsStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCsStartActionPerformed(evt);
            }
        });
        popmenuTriggersCs.add(popmCsStart);

        popmCsEnd.setText("Syllable end (cs) - %ceK");
        popmCsEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCsEndActionPerformed(evt);
            }
        });
        popmenuTriggersCs.add(popmCsEnd);

        popmCsMiddle.setText("Syllable middle (cs) - %cmK");
        popmCsMiddle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCsMiddleActionPerformed(evt);
            }
        });
        popmenuTriggersCs.add(popmCsMiddle);

        popmCsDuration.setText("Syllable duration (cs) - %cdK");
        popmCsDuration.setToolTipText("");
        popmCsDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCsDurationActionPerformed(evt);
            }
        });
        popmenuTriggersCs.add(popmCsDuration);

        popmenuTriggers.add(popmenuTriggersCs);

        popmTriggerLetter.setText("Letter - %letter");
        popmTriggerLetter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmTriggerLetterActionPerformed(evt);
            }
        });
        popmenuTriggers.add(popmTriggerLetter);

        popmTriggerSyllable.setText("Syllable - %syllable");
        popmTriggerSyllable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmTriggerSyllableActionPerformed(evt);
            }
        });
        popmenuTriggers.add(popmTriggerSyllable);

        popmTriggerSentence.setText("Sentence - %sentence");
        popmTriggerSentence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmTriggerSentenceActionPerformed(evt);
            }
        });
        popmenuTriggers.add(popmTriggerSentence);

        popCommands.add(popmenuTriggers);

        popmenuList.setText("Insert tag");

        popmFontReset.setText("Reset - \\r");
        popmFontReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontResetActionPerformed(evt);
            }
        });
        popmenuList.add(popmFontReset);

        popmWrapLine.setText("Wrap method - \\q");
        popmWrapLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmWrapLineActionPerformed(evt);
            }
        });
        popmenuList.add(popmWrapLine);

        popmFontEncoding.setText("Encoding - \\fe");
        popmFontEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontEncodingActionPerformed(evt);
            }
        });
        popmenuList.add(popmFontEncoding);

        popmenuKaraoke.setText("Karaoke");

        popmKaraokeMinusK.setText("\\k");
        popmKaraokeMinusK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmKaraokeMinusKActionPerformed(evt);
            }
        });
        popmenuKaraoke.add(popmKaraokeMinusK);

        popmKaraokeBigK.setText("\\K");
        popmKaraokeBigK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmKaraokeBigKActionPerformed(evt);
            }
        });
        popmenuKaraoke.add(popmKaraokeBigK);

        popmKaraokeFill.setText("\\kf");
        popmKaraokeFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmKaraokeFillActionPerformed(evt);
            }
        });
        popmenuKaraoke.add(popmKaraokeFill);

        popmKaraokeOutline.setText("\\ko");
        popmKaraokeOutline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmKaraokeOutlineActionPerformed(evt);
            }
        });
        popmenuKaraoke.add(popmKaraokeOutline);

        popmenuList.add(popmenuKaraoke);

        popmenuAnimation.setText("Animation");

        popmAnimateSentence.setText("On sentence - \\t()");
        popmAnimateSentence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmAnimateSentenceActionPerformed(evt);
            }
        });
        popmenuAnimation.add(popmAnimateSentence);

        popmAnimateSentenceWithAcc.setText("On sentence + acc - \\t($a,)");
        popmAnimateSentenceWithAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmAnimateSentenceWithAccActionPerformed(evt);
            }
        });
        popmenuAnimation.add(popmAnimateSentenceWithAcc);

        popmAnimateTime.setText("On time - \\t(%sK,%eK,)");
        popmAnimateTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmAnimateTimeActionPerformed(evt);
            }
        });
        popmenuAnimation.add(popmAnimateTime);

        popmAnimateWithAcc.setText("On time + acc - \\t(%sK,%eK,$a,)");
        popmAnimateWithAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmAnimateWithAccActionPerformed(evt);
            }
        });
        popmenuAnimation.add(popmAnimateWithAcc);

        popmenuList.add(popmenuAnimation);

        popmenuFont.setText("Font");

        popmFontName.setText("Name - \\fn");
        popmFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontNameActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontName);

        popmFontSize.setText("Size - \\fs");
        popmFontSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontSizeActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontSize);

        popmFontScale.setText("Scale X and Y - \\fsc");
        popmFontScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontScaleActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontScale);

        popmFontScaleX.setText("Scale X - \\fscx");
        popmFontScaleX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontScaleXActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontScaleX);

        popmFontScaleY.setText("Scale Y - \\fscy");
        popmFontScaleY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontScaleYActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontScaleY);

        popmFontRX.setText("Rotation on X - \\frx");
        popmFontRX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontRXActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontRX);

        popmFontRY.setText("Rotation on Y - \\fry");
        popmFontRY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontRYActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontRY);

        popmFontRZ.setText("Rotation on Z - \\frz");
        popmFontRZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontRZActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontRZ);

        popmFontRZSimplified.setText("Rotation on Z - \\fr");
        popmFontRZSimplified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontRZSimplifiedActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontRZSimplified);

        popmFontSpacing.setText("Spacing - \\fsp");
        popmFontSpacing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontSpacingActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontSpacing);

        popmFontShearX.setText("Shear on X - \\fax");
        popmFontShearX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontShearXActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontShearX);

        popmFontShearY.setText("Shear on Y - \\fay");
        popmFontShearY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontShearYActionPerformed(evt);
            }
        });
        popmenuFont.add(popmFontShearY);

        popmenuList.add(popmenuFont);

        popmenuFontAttribute.setText("Font attribute");

        popmFontAttrBold.setText("Bold - \\b");
        popmFontAttrBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontAttrBoldActionPerformed(evt);
            }
        });
        popmenuFontAttribute.add(popmFontAttrBold);

        popmFontAttrItalic.setText("Italic - \\i");
        popmFontAttrItalic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontAttrItalicActionPerformed(evt);
            }
        });
        popmenuFontAttribute.add(popmFontAttrItalic);

        popmFontAttrUnderline.setText("Underline - \\u");
        popmFontAttrUnderline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontAttrUnderlineActionPerformed(evt);
            }
        });
        popmenuFontAttribute.add(popmFontAttrUnderline);

        popmFontAttrStrikeOut.setText("StrikeOut - \\s");
        popmFontAttrStrikeOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmFontAttrStrikeOutActionPerformed(evt);
            }
        });
        popmenuFontAttribute.add(popmFontAttrStrikeOut);

        popmFontAttrBord.setText("Border on X and Y - \\bord");
        popmenuFontAttribute.add(popmFontAttrBord);

        popmFontAttrXBord.setText("Border on X - \\xbord");
        popmenuFontAttribute.add(popmFontAttrXBord);

        popmFontAttrYBord.setText("Border on Y - \\ybord");
        popmenuFontAttribute.add(popmFontAttrYBord);

        popmFontAttrShad.setText("Shadow on X and Y - \\shad");
        popmenuFontAttribute.add(popmFontAttrShad);

        popmFontAttrXShad.setText("Shadow on X - \\xshad");
        popmenuFontAttribute.add(popmFontAttrXShad);

        popmFontAttrYShad.setText("Shadow on Y - \\yshad");
        popmenuFontAttribute.add(popmFontAttrYShad);

        popmenuList.add(popmenuFontAttribute);

        popmenuPosition.setText("Position");

        popmPosA.setText("Alignment (Formerly) - \\a");
        popmPosA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPosAActionPerformed(evt);
            }
        });
        popmenuPosition.add(popmPosA);

        popmPosAn.setText("Alignment (NumPad) - \\an");
        popmPosAn.setToolTipText("");
        popmPosAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPosAnActionPerformed(evt);
            }
        });
        popmenuPosition.add(popmPosAn);

        popmPosPos.setText("Position - \\pos");
        popmPosPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPosPosActionPerformed(evt);
            }
        });
        popmenuPosition.add(popmPosPos);

        popmPosMove.setText("Movement - \\move");
        popmPosMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPosMoveActionPerformed(evt);
            }
        });
        popmenuPosition.add(popmPosMove);

        popmPosOrg.setText("Origin - \\org");
        popmPosOrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPosOrgActionPerformed(evt);
            }
        });
        popmenuPosition.add(popmPosOrg);

        popmenuList.add(popmenuPosition);

        popmenuDisplay.setText("Display");

        popmDisplayBlurEdge.setText("Blur edge - \\be");
        popmDisplayBlurEdge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayBlurEdgeActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayBlurEdge);

        popmDisplayBlur.setText("Blur - \\blur");
        popmDisplayBlur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayBlurActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayBlur);

        popmDisplayPrimaryColor.setText("Text color (Formerly) - \\c");
        popmDisplayPrimaryColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayPrimaryColorActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayPrimaryColor);

        popmDisplayTextColor.setText("Text color - \\1c");
        popmDisplayTextColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayTextColorActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayTextColor);

        popmDisplayKaraokeColor.setText("Karaoke color - \\2c");
        popmDisplayKaraokeColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayKaraokeColorActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayKaraokeColor);

        popmDisplayOutlineColor.setText("Outline/Border color - \\3c");
        popmDisplayOutlineColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayOutlineColorActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayOutlineColor);

        popmDisplayShadowColor.setText("Shadow color - \\4c");
        popmDisplayShadowColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayShadowColorActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayShadowColor);

        popmDisplayAlpha.setText("Alpha - \\alpha");
        popmDisplayAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayAlphaActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayAlpha);

        popmDisplayTextAlpha.setText("Text alpha - \\1a");
        popmDisplayTextAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayTextAlphaActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayTextAlpha);

        popmDisplayKaraokeAlpha.setText("Karaoke alpha -\\2a");
        popmDisplayKaraokeAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayKaraokeAlphaActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayKaraokeAlpha);

        popmDisplayOutlineAlpha.setText("Outline/Border alpha - \\3a");
        popmDisplayOutlineAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayOutlineAlphaActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayOutlineAlpha);

        popmDisplayShadowAlpha.setText("Shadow alpha - \\4a");
        popmDisplayShadowAlpha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayShadowAlphaActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayShadowAlpha);

        popmDisplayFad.setText("Fade simple - \\fad");
        popmDisplayFad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayFadActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayFad);

        popmDisplayFade.setText("Fade complex - \\fade");
        popmDisplayFade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDisplayFadeActionPerformed(evt);
            }
        });
        popmenuDisplay.add(popmDisplayFade);

        popmenuList.add(popmenuDisplay);

        popmenuVisibility.setText("Visibility");

        popmVisibilityClip.setText("Visible clip - \\clip");
        popmVisibilityClip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmVisibilityClipActionPerformed(evt);
            }
        });
        popmenuVisibility.add(popmVisibilityClip);

        popmVisibilityIClip.setText("Invisible clip - \\iclip");
        popmVisibilityIClip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmVisibilityIClipActionPerformed(evt);
            }
        });
        popmenuVisibility.add(popmVisibilityIClip);

        popmenuList.add(popmenuVisibility);

        popmenuDrawing.setText("Drawing");

        popmDrawP.setText("Draw with scale - \\p");
        popmDrawP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawPActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawP);

        popmDrawBaselineOffset.setText("Baseline offset - \\pbo");
        popmDrawBaselineOffset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawBaselineOffsetActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawBaselineOffset);
        popmenuDrawing.add(jSeparator2);

        popmDrawMove.setText("Move command - m");
        popmDrawMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawMoveActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawMove);

        popmDrawGoto.setText("Goto command - n");
        popmDrawGoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawGotoActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawGoto);

        popmDrawLine.setText("Line command - l");
        popmDrawLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawLineActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawLine);

        popmDrawCubic.setText("Cubic command - b");
        popmDrawCubic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawCubicActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawCubic);

        popmDrawSpline.setText("Spline command - s");
        popmDrawSpline.setToolTipText("List of cubic splines");
        popmDrawSpline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawSplineActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawSpline);

        popmDrawCloseSpline.setText("Close spline command - c");
        popmDrawCloseSpline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawCloseSplineActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawCloseSpline);

        popmDrawExtendSpline.setText("Expand command - p");
        popmDrawExtendSpline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDrawExtendSplineActionPerformed(evt);
            }
        });
        popmenuDrawing.add(popmDrawExtendSpline);

        popmenuList.add(popmenuDrawing);

        popCommands.add(popmenuList);

        fcOpenEffects.setAccessory(new org.wingate.feuille.m.afm.karaoke.io.CodeAccessory());

        popmCodeCut.setText("Cut");
        popmCodeCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeCutActionPerformed(evt);
            }
        });
        popCode.add(popmCodeCut);

        popmCodeCopy.setText("Copy");
        popmCodeCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeCopyActionPerformed(evt);
            }
        });
        popCode.add(popmCodeCopy);

        popmCodePaste.setText("Paste");
        popmCodePaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodePasteActionPerformed(evt);
            }
        });
        popCode.add(popmCodePaste);
        popCode.add(jSeparator6);

        popmCodeScriptName.setText("Script name...");
        popmCodeScriptName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeScriptNameActionPerformed(evt);
            }
        });
        popCode.add(popmCodeScriptName);

        popmCodeAuthor.setText("Author...");
        popmCodeAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeAuthorActionPerformed(evt);
            }
        });
        popCode.add(popmCodeAuthor);

        popmCodeVersion.setText("Version...");
        popmCodeVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeVersionActionPerformed(evt);
            }
        });
        popCode.add(popmCodeVersion);

        popmCodeDescription.setText("Description...");
        popmCodeDescription.setToolTipText("");
        popmCodeDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeDescriptionActionPerformed(evt);
            }
        });
        popCode.add(popmCodeDescription);

        popmCodeUpdateDetails.setText("Update details...");
        popmCodeUpdateDetails.setToolTipText("");
        popmCodeUpdateDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCodeUpdateDetailsActionPerformed(evt);
            }
        });
        popCode.add(popmCodeUpdateDetails);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        cboxAFMEffects.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboxAFMEffects.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxAFMEffectsItemStateChanged(evt);
            }
        });

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listTemplates.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listTemplates.setComponentPopupMenu(popTemplates);
        listTemplates.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listTemplatesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listTemplates);

        jLabel1.setText("Templates : ");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        textPanelCommands.setComponentPopupMenu(popCommands);
        jScrollPane2.setViewportView(textPanelCommands);

        jToolBar1.setRollover(true);

        btnNewCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_newdocument.png"))); // NOI18N
        btnNewCode.setText("New code");
        btnNewCode.setFocusable(false);
        btnNewCode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewCode.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCodeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNewCode);

        btnOpenCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_folder.png"))); // NOI18N
        btnOpenCode.setText("Open code");
        btnOpenCode.setFocusable(false);
        btnOpenCode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenCode.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenCodeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenCode);

        btnSaveCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_floppydisk.png"))); // NOI18N
        btnSaveCode.setText("Save code");
        btnSaveCode.setFocusable(false);
        btnSaveCode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveCode.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCodeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveCode);

        btnSaveEffects.setText("Save selected effect with codes if any");
        btnSaveEffects.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveEffectsActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cboxAFMEffects, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2)
                            .addComponent(tabpCode)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRefresh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSaveEffects)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboxAFMEffects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnSaveEffects)
                    .addComponent(btnRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabpCode, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnOK)
                            .addComponent(btnCancel))
                        .addContainerGap())
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCodeActionPerformed
        // Charge le sélecteur de type de code
        CodeTypeDialog ctd = new CodeTypeDialog(new javax.swing.JFrame(), true);
        ctd.showDialog();
        if(ctd.getDialogResult() == DialogResult.Ok){
            CodePanel cp = new CodePanel();
            cp.setCodeType(ctd.getCodeType());
            cp.getTextArea().setPopupMenu(popCode);
            tabpCode.add(cp);
            String name = JOptionPane.showInputDialog("Type a name for the new tab.");
            CodeTabPanel ctp = new CodeTabPanel(ctd.getCodeType(), tabpCode, tabpCode.getTabCount() - 1, name);
            tabpCode.setTabComponentAt(tabpCode.getTabCount() - 1, ctp);
        }
    }//GEN-LAST:event_btnNewCodeActionPerformed

    private void btnOpenCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenCodeActionPerformed
        // Open code
        File currentFolder = new File(".");
        File folder = new File(currentFolder.getAbsolutePath() + File.separator + "user");
        fcOpenCode.setCurrentDirectory(folder);
        int z = fcOpenCode.showOpenDialog(new javax.swing.JFrame());
        if(z == javax.swing.JFileChooser.APPROVE_OPTION){
            String path = fcOpenCode.getSelectedFile().getAbsolutePath();
            if(fcOpenCode.getFileFilter() instanceof GenericFileFilter filter){
                if(!path.endsWith(filter.getExtension())) path += filter.getExtension();
                SFXCode code = CodeIO.load(path);
                CodePanel cp = new CodePanel();
                cp.getTextArea().setText(code.getContent());
                cp.setCodeType(code.getCodeType());
                cp.getTextArea().setPopupMenu(popCode);
                tabpCode.add(cp);
                CodeTabPanel ctp = new CodeTabPanel(
                        code.getCodeType(), tabpCode, tabpCode.getTabCount() - 1, code.getScriptName());
                tabpCode.setTabComponentAt(tabpCode.getTabCount() - 1, ctp);
            }
        }
    }//GEN-LAST:event_btnOpenCodeActionPerformed

    private void btnSaveCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCodeActionPerformed
        // Save code
        if(tabpCode.getTabCount() == 0 || tabpCode.getSelectedIndex() == -1) return;
        File currentFolder = new File(".");
        File folder = new File(currentFolder.getAbsolutePath() + File.separator + "user");
        fcSaveCode.setCurrentDirectory(folder);
        int z = fcSaveCode.showSaveDialog(new javax.swing.JFrame());
        if(z == javax.swing.JFileChooser.APPROVE_OPTION){
            String path = fcSaveCode.getSelectedFile().getAbsolutePath();
            int tabIndex = tabpCode.getSelectedIndex();
            CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabIndex);            
            cp.getCode().setContent(cp.getTextArea().getText());
            cp.getCode().setCodeType(cp.getCodeType());
            CodeIO.save(path, cp.getCode());
        }
    }//GEN-LAST:event_btnSaveCodeActionPerformed

    private void cboxAFMEffectsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxAFMEffectsItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboxAFMEffectsItemStateChanged

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add preset to template list
        if(cboxAFMEffects.getSelectedIndex() < 0) return;
        if(cboxAFMEffects.getSelectedItem() instanceof SFXAbstract sfx){
            String humanName = JOptionPane.showInputDialog("Type a name :");
            tryAddTemplate(sfx, humanName);
        }        
    }//GEN-LAST:event_btnAddActionPerformed

    private void popmRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmRemoveActionPerformed
        // Remove templates
        if(listTemplates.getSelectedIndex() < 0) return;
        int z = JOptionPane.showConfirmDialog(
                new javax.swing.JFrame(),
                "Do you really want to delete these templates ?",
                "Confirm",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if(z == JOptionPane.OK_OPTION){
            int[] indices = listTemplates.getSelectedIndices();
            for(int i=indices.length-1; i >= 0; i--){                
                if(templatesModel.getElementAt(i) instanceof String name){
                    SFXAbstract a = null;
                    for(Object o : templatesModel.toArray()){
                        if(o instanceof SFXAbstract sfx){
                            if(sfx.getHumanName().equalsIgnoreCase(name)){
                                a = sfx;
                                break;
                            }
                        }
                    }
                    if(a != null){
                        templatesModel.remove(indices[i]);
                    }                    
                }
            }
        }
    }//GEN-LAST:event_popmRemoveActionPerformed

    private void listTemplatesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listTemplatesValueChanged
        // When selection of a name
        if(listTemplates.getSelectedIndex() < 0) return; // Psychotic unused option
        if(templatesModel.getElementAt(listTemplates.getSelectedIndex()) instanceof SFXAbstract sfx){
            SFXAbstract a = null;
            for(int j=0; j<templatesModel.getSize(); j++){
                if(templatesModel.getElementAt(j) instanceof SFXAbstract o){
                    if(o.getHumanName().equalsIgnoreCase(sfx.getHumanName())){
                        a = o;
                        break;
                    }
                }                
            }
            if(a == null) return;
            StringBuilder sb = new StringBuilder();
            for(String commands : a.getTemplates()){
                sb.append(commands);
                sb.append("\n");
            }
            textPanelCommands.setText(sb.toString());
            textPanelCommands.updateUI();
            
            List<Integer> checked = new ArrayList<>();
            for(SFXCode code : a.getCodes()){
                boolean add = true;                
                for(int i=0; i<tabpCode.getTabCount(); i++){
                    for(int j=0; j<tabpCode.getTabCount(); j++){
                        if(tabpCode.getComponentAt(i) instanceof CodePanel cpi
                                && tabpCode.getComponentAt(j) instanceof CodePanel cpj){
                            if(cpi.getCode().getScriptName().equalsIgnoreCase(cpj.getCode().getScriptName())){
                                if(checked.contains(j) == false){
                                    checked.add(j);
                                    add = false;
                                    break;
                                }
                                
                            }
                        }
                    }
                    if(add == false) break;
                }
                if(add == true){
                    CodePanel cp = new CodePanel();
                    cp.getTextArea().setText(code.getContent());
                    cp.setCodeType(code.getCodeType());
                    cp.getTextArea().setPopupMenu(popCode);
                    tabpCode.add(cp);
                    CodeTabPanel ctp = new CodeTabPanel(
                            code.getCodeType(), tabpCode, tabpCode.getTabCount() - 1, code.getScriptName());
                    tabpCode.setTabComponentAt(tabpCode.getTabCount() - 1, ctp);
                }
            }
        }
    }//GEN-LAST:event_listTemplatesValueChanged

    private void popmSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSaveActionPerformed
        // Save commands internally into an SFXAbstract object
        if(listTemplates.getSelectedIndex() < 0){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No selection !",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            if(templatesModel.getElementAt(listTemplates.getSelectedIndex()) instanceof SFXAbstract sfx){
                
                SFXAbstract a = null;
                for(Object obj : templatesModel.toArray()){
                    if(obj instanceof SFXAbstract o)
                    if(o.getHumanName().equalsIgnoreCase(sfx.getHumanName())){
                        a = o;
                        break;
                    }
                }
                if(a == null) return;                
                
                try(StringReader sr = new StringReader(textPanelCommands.getText());
                        BufferedReader br = new BufferedReader(sr);){
                    String line;
                    int z = JOptionPane.showConfirmDialog(new javax.swing.JFrame(),
                            "Do you want to keep existing commands ?", "Do not clear existing commands",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(z == JOptionPane.NO_OPTION){
                        a.getTemplates().clear();
                    }
                    
                    while((line = br.readLine()) != null){
                        a.getTemplates().add(line);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(tabpCode.getTabCount() > 0){
                    int z = JOptionPane.showConfirmDialog(new javax.swing.JFrame(),
                            "Do you want to put all existing coding script to your selection ?", "Question",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(z == JOptionPane.YES_OPTION){
                        a.getCodes().clear();
                        for(int i=0; i<tabpCode.getTabCount(); i++){
                            CodePanel cp = (CodePanel)tabpCode.getComponentAt(i);            
                            cp.getCode().setContent(cp.getTextArea().getText());
                            cp.getCode().setCodeType(cp.getCodeType());
                            a.getCodes().add(cp.getCode());
                        }
                    }
                }                
            }
        }        
    }//GEN-LAST:event_popmSaveActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // OK
        dialogResult = DialogResult.Ok;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Cancel
        dialogResult = DialogResult.Cancel;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void popmMsStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsStartActionPerformed
        addText("%sK");
    }//GEN-LAST:event_popmMsStartActionPerformed

    private void popmMsEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsEndActionPerformed
        addText("%eK");
    }//GEN-LAST:event_popmMsEndActionPerformed

    private void popmKaraokeMinusKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmKaraokeMinusKActionPerformed
        addText("\\k%cdK");
    }//GEN-LAST:event_popmKaraokeMinusKActionPerformed

    private void popmKaraokeBigKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmKaraokeBigKActionPerformed
        addText("\\K%cdK");
    }//GEN-LAST:event_popmKaraokeBigKActionPerformed

    private void popmKaraokeFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmKaraokeFillActionPerformed
        addText("\\kf%cdK");
    }//GEN-LAST:event_popmKaraokeFillActionPerformed

    private void popmKaraokeOutlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmKaraokeOutlineActionPerformed
        addText("\\ko%cdK");
    }//GEN-LAST:event_popmKaraokeOutlineActionPerformed

    private void popmAnimateSentenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmAnimateSentenceActionPerformed
        addText("\\t()");
    }//GEN-LAST:event_popmAnimateSentenceActionPerformed

    private void popmAnimateTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmAnimateTimeActionPerformed
        addText("\\t(%sK,%eK,)");
    }//GEN-LAST:event_popmAnimateTimeActionPerformed

    private void popmAnimateWithAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmAnimateWithAccActionPerformed
        addText("\\t(%sK,%eK,$a,)");
    }//GEN-LAST:event_popmAnimateWithAccActionPerformed

    private void popmAnimateSentenceWithAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmAnimateSentenceWithAccActionPerformed
        addText("\\t($a,)");
    }//GEN-LAST:event_popmAnimateSentenceWithAccActionPerformed

    private void popmFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontNameActionPerformed
        addText("\\fn");
    }//GEN-LAST:event_popmFontNameActionPerformed

    private void popmFontSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontSizeActionPerformed
        addText("\\fs");
    }//GEN-LAST:event_popmFontSizeActionPerformed

    private void popmFontScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontScaleActionPerformed
        addText("\\fsc");
    }//GEN-LAST:event_popmFontScaleActionPerformed

    private void popmFontScaleXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontScaleXActionPerformed
        addText("\\fscx");
    }//GEN-LAST:event_popmFontScaleXActionPerformed

    private void popmFontScaleYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontScaleYActionPerformed
        addText("\\fscy");
    }//GEN-LAST:event_popmFontScaleYActionPerformed

    private void popmFontRXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontRXActionPerformed
        addText("\\frx");
    }//GEN-LAST:event_popmFontRXActionPerformed

    private void popmFontRYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontRYActionPerformed
        addText("\\fry");
    }//GEN-LAST:event_popmFontRYActionPerformed

    private void popmFontRZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontRZActionPerformed
        addText("\\frz");
    }//GEN-LAST:event_popmFontRZActionPerformed

    private void popmFontRZSimplifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontRZSimplifiedActionPerformed
        addText("\\fr");
    }//GEN-LAST:event_popmFontRZSimplifiedActionPerformed

    private void popmFontSpacingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontSpacingActionPerformed
        addText("\\fsp");
    }//GEN-LAST:event_popmFontSpacingActionPerformed

    private void popmFontShearXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontShearXActionPerformed
        addText("\\fax");
    }//GEN-LAST:event_popmFontShearXActionPerformed

    private void popmFontShearYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontShearYActionPerformed
        addText("\\fay");
    }//GEN-LAST:event_popmFontShearYActionPerformed

    private void popmFontAttrBoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontAttrBoldActionPerformed
        addText("\\b");
    }//GEN-LAST:event_popmFontAttrBoldActionPerformed

    private void popmFontAttrItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontAttrItalicActionPerformed
        addText("\\i");
    }//GEN-LAST:event_popmFontAttrItalicActionPerformed

    private void popmFontAttrUnderlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontAttrUnderlineActionPerformed
        addText("\\u");
    }//GEN-LAST:event_popmFontAttrUnderlineActionPerformed

    private void popmFontAttrStrikeOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontAttrStrikeOutActionPerformed
        addText("\\s");
    }//GEN-LAST:event_popmFontAttrStrikeOutActionPerformed

    private void popmFontResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontResetActionPerformed
        addText("\\r");
    }//GEN-LAST:event_popmFontResetActionPerformed

    private void popmWrapLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmWrapLineActionPerformed
        addText("\\q");
    }//GEN-LAST:event_popmWrapLineActionPerformed

    private void popmPosAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPosAActionPerformed
        addText("\\a");
    }//GEN-LAST:event_popmPosAActionPerformed

    private void popmPosAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPosAnActionPerformed
        addText("\\an");
    }//GEN-LAST:event_popmPosAnActionPerformed

    private void popmPosPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPosPosActionPerformed
        addText("\\pos");
    }//GEN-LAST:event_popmPosPosActionPerformed

    private void popmPosMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPosMoveActionPerformed
        addText("\\move");
    }//GEN-LAST:event_popmPosMoveActionPerformed

    private void popmPosOrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPosOrgActionPerformed
        addText("\\org");
    }//GEN-LAST:event_popmPosOrgActionPerformed

    private void popmDisplayBlurEdgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayBlurEdgeActionPerformed
        addText("\\be");
    }//GEN-LAST:event_popmDisplayBlurEdgeActionPerformed

    private void popmDisplayBlurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayBlurActionPerformed
        addText("\\blur");
    }//GEN-LAST:event_popmDisplayBlurActionPerformed

    private void popmDisplayPrimaryColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayPrimaryColorActionPerformed
        addText("\\c");
    }//GEN-LAST:event_popmDisplayPrimaryColorActionPerformed

    private void popmDisplayTextColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayTextColorActionPerformed
        addText("\\1c");
    }//GEN-LAST:event_popmDisplayTextColorActionPerformed

    private void popmDisplayKaraokeColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayKaraokeColorActionPerformed
        addText("\\2c");
    }//GEN-LAST:event_popmDisplayKaraokeColorActionPerformed

    private void popmDisplayOutlineColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayOutlineColorActionPerformed
        addText("\\3c");
    }//GEN-LAST:event_popmDisplayOutlineColorActionPerformed

    private void popmDisplayShadowColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayShadowColorActionPerformed
        addText("\\4c");
    }//GEN-LAST:event_popmDisplayShadowColorActionPerformed

    private void popmDisplayAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayAlphaActionPerformed
        addText("\\alpha");
    }//GEN-LAST:event_popmDisplayAlphaActionPerformed

    private void popmDisplayTextAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayTextAlphaActionPerformed
        addText("\\1a");
    }//GEN-LAST:event_popmDisplayTextAlphaActionPerformed

    private void popmDisplayKaraokeAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayKaraokeAlphaActionPerformed
        addText("\\2a");
    }//GEN-LAST:event_popmDisplayKaraokeAlphaActionPerformed

    private void popmDisplayOutlineAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayOutlineAlphaActionPerformed
        addText("\\3a");
    }//GEN-LAST:event_popmDisplayOutlineAlphaActionPerformed

    private void popmDisplayShadowAlphaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayShadowAlphaActionPerformed
        addText("\\4a");
    }//GEN-LAST:event_popmDisplayShadowAlphaActionPerformed

    private void popmDisplayFadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayFadActionPerformed
        addText("\\fad");
    }//GEN-LAST:event_popmDisplayFadActionPerformed

    private void popmDisplayFadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDisplayFadeActionPerformed
        addText("\\fade");
    }//GEN-LAST:event_popmDisplayFadeActionPerformed

    private void popmVisibilityClipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmVisibilityClipActionPerformed
        addText("\\clip");
    }//GEN-LAST:event_popmVisibilityClipActionPerformed

    private void popmVisibilityIClipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmVisibilityIClipActionPerformed
        addText("\\iclip");
    }//GEN-LAST:event_popmVisibilityIClipActionPerformed

    private void popmFontEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmFontEncodingActionPerformed
        addText("\\fe");
    }//GEN-LAST:event_popmFontEncodingActionPerformed

    private void popmDrawPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawPActionPerformed
        addText("{\\p1}{\\p0}");
    }//GEN-LAST:event_popmDrawPActionPerformed

    private void popmDrawBaselineOffsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawBaselineOffsetActionPerformed
        addText("\\pbo");
    }//GEN-LAST:event_popmDrawBaselineOffsetActionPerformed

    private void popmDrawMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawMoveActionPerformed
        addText("m");
    }//GEN-LAST:event_popmDrawMoveActionPerformed

    private void popmDrawGotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawGotoActionPerformed
        addText("n");
    }//GEN-LAST:event_popmDrawGotoActionPerformed

    private void popmDrawLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawLineActionPerformed
        addText("l");
    }//GEN-LAST:event_popmDrawLineActionPerformed

    private void popmDrawCubicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawCubicActionPerformed
        addText("b");
    }//GEN-LAST:event_popmDrawCubicActionPerformed

    private void popmDrawSplineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawSplineActionPerformed
        addText("s");
    }//GEN-LAST:event_popmDrawSplineActionPerformed

    private void popmDrawCloseSplineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawCloseSplineActionPerformed
        addText("c");
    }//GEN-LAST:event_popmDrawCloseSplineActionPerformed

    private void popmDrawExtendSplineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDrawExtendSplineActionPerformed
        addText("p");
    }//GEN-LAST:event_popmDrawExtendSplineActionPerformed

    private void popmMsMiddleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsMiddleActionPerformed
        addText("%mK");
    }//GEN-LAST:event_popmMsMiddleActionPerformed

    private void popmMsDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsDurationActionPerformed
        addText("%dK");
    }//GEN-LAST:event_popmMsDurationActionPerformed

    private void popmCsStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCsStartActionPerformed
        addText("%csK");
    }//GEN-LAST:event_popmCsStartActionPerformed

    private void popmCsEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCsEndActionPerformed
        addText("%ceK");
    }//GEN-LAST:event_popmCsEndActionPerformed

    private void popmCsMiddleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCsMiddleActionPerformed
        addText("%cmK");
    }//GEN-LAST:event_popmCsMiddleActionPerformed

    private void popmCsDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCsDurationActionPerformed
        addText("%cdK");
    }//GEN-LAST:event_popmCsDurationActionPerformed

    private void popmMsSentenceStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsSentenceStartActionPerformed
        addText("%ssK");
    }//GEN-LAST:event_popmMsSentenceStartActionPerformed

    private void popmMsSentenceEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsSentenceEndActionPerformed
        addText("%seK");
    }//GEN-LAST:event_popmMsSentenceEndActionPerformed

    private void popmMsSentenceMiddleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsSentenceMiddleActionPerformed
        addText("%smK");
    }//GEN-LAST:event_popmMsSentenceMiddleActionPerformed

    private void popmMsSentenceDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsSentenceDurationActionPerformed
        addText("%sdK");
    }//GEN-LAST:event_popmMsSentenceDurationActionPerformed

    private void popmTriggerSentenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmTriggerSentenceActionPerformed
        addText("%sentence");
    }//GEN-LAST:event_popmTriggerSentenceActionPerformed

    private void popmTriggerSyllableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmTriggerSyllableActionPerformed
        addText("%syllable");
    }//GEN-LAST:event_popmTriggerSyllableActionPerformed

    private void popmMsLetterStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsLetterStartActionPerformed
        addText("%lsK");
    }//GEN-LAST:event_popmMsLetterStartActionPerformed

    private void popmMsLetterEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsLetterEndActionPerformed
        addText("%leK");
    }//GEN-LAST:event_popmMsLetterEndActionPerformed

    private void popmMsLetterMiddleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsLetterMiddleActionPerformed
        addText("%lmK");
    }//GEN-LAST:event_popmMsLetterMiddleActionPerformed

    private void popmMsLetterDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmMsLetterDurationActionPerformed
        addText("%ldK");
    }//GEN-LAST:event_popmMsLetterDurationActionPerformed

    private void popmCsLetterStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCsLetterStartActionPerformed
        addText("%lcsK");
    }//GEN-LAST:event_popmCsLetterStartActionPerformed

    private void popmCsLetterEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCsLetterEndActionPerformed
        addText("%lceK");
    }//GEN-LAST:event_popmCsLetterEndActionPerformed

    private void popmCsLetterMiddleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCsLetterMiddleActionPerformed
        addText("%lcmK");
    }//GEN-LAST:event_popmCsLetterMiddleActionPerformed

    private void popmCsLetterDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCsLetterDurationActionPerformed
        addText("%lcdK");
    }//GEN-LAST:event_popmCsLetterDurationActionPerformed

    private void popmTriggerLetterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmTriggerLetterActionPerformed
        addText("%letter");
    }//GEN-LAST:event_popmTriggerLetterActionPerformed

    private void btnSaveEffectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveEffectsActionPerformed
        // Save selected effect with codes if any
        if(listTemplates.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(
                    this,
                    "No selection on list!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if(listTemplates.getSelectedIndices().length > 1){
            JOptionPane.showMessageDialog(
                    this,
                    "Too many selections on list!\nSelect one and only one!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        File currentFolder = new File(".");
        File folder = new File(currentFolder.getAbsolutePath() + File.separator + "user");
        fcSaveEffects.setCurrentDirectory(folder);
        int z = fcSaveEffects.showSaveDialog(new javax.swing.JFrame());
        if(z == javax.swing.JFileChooser.APPROVE_OPTION){
            String path = fcSaveEffects.getSelectedFile().getAbsolutePath();
            if(!path.endsWith(".sfx")) path += ".sfx";
            SFXAbstract sfx = (SFXAbstract)templatesModel.get(listTemplates.getSelectedIndex());
            EffectsIO.save(path, sfx);
        }
    }//GEN-LAST:event_btnSaveEffectsActionPerformed

    @SuppressWarnings("Convert2Lambda")
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // Update list
        File currentFolder = new File(".");
        File folder = new File(currentFolder.getAbsolutePath() + File.separator + "user");        
        templatesModel.clear();
        for(File file : folder.listFiles(new java.io.FileFilter(){
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".sfx");
            }
        })){
            SFXAbstract sfx = EffectsIO.load(file.getPath());
            templatesModel.addElement(sfx);
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void popmCodeCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeCutActionPerformed
        // Cut text from a SFXCode of the selected tab
        if(tabpCode.getTabCount() == 0) return;
        if(tabpCode.getSelectedIndex() == -1) return;
        CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabpCode.getSelectedIndex());
        if(cp.getTextArea().getSelectedText().isEmpty() == false){
            // Copy selected text
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection sel = new StringSelection(cp.getTextArea().getSelectedText());
            clip.setContents(sel, sel);
            // Delete selected text
            String start = cp.getTextArea().getText().substring(0, cp.getTextArea().getSelectionStart());
            String end = cp.getTextArea().getText().substring(cp.getTextArea().getSelectionEnd());
            cp.getTextArea().setText(start + end);
        }
    }//GEN-LAST:event_popmCodeCutActionPerformed

    private void popmCodeCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeCopyActionPerformed
        // Copy text from a SFXCode of the selected tab
        if(tabpCode.getTabCount() == 0) return;
        if(tabpCode.getSelectedIndex() == -1) return;
        CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabpCode.getSelectedIndex());
        if(cp.getTextArea().getSelectedText().isEmpty() == false){
            // Copy
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection sel = new StringSelection(cp.getTextArea().getSelectedText());
            clip.setContents(sel, sel);
        }
    }//GEN-LAST:event_popmCodeCopyActionPerformed

    private void popmCodePasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodePasteActionPerformed
        // Paste text to a SFXCode of the selected tab
        if(tabpCode.getTabCount() == 0) return;
        if(tabpCode.getSelectedIndex() == -1) return;
        CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabpCode.getSelectedIndex());
        // Paste
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clip.getContents(null);
        if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)){
            try{
                if(contents.getTransferData(DataFlavor.stringFlavor) instanceof String s){
                    String start = "", end = "";
                    if(cp.getTextArea().getSelectedText() != null && cp.getTextArea().getSelectedText().isEmpty() == false){
                        start = cp.getTextArea().getText().substring(0, cp.getTextArea().getSelectionStart());
                        end = cp.getTextArea().getText().substring(cp.getTextArea().getSelectionEnd());
                    }
                    cp.getTextArea().setText(start + s + end);
                }
            }catch(UnsupportedFlavorException | IOException exc){
                System.err.println("Error\n" + exc.getMessage());
            }
        }        
    }//GEN-LAST:event_popmCodePasteActionPerformed

    private void popmCodeScriptNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeScriptNameActionPerformed
        // Script name
        if(tabpCode.getTabCount() == 0) return;
        if(tabpCode.getSelectedIndex() == -1) return;
        CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabpCode.getSelectedIndex());
        String s = JOptionPane.showInputDialog("Type a script name :");
        cp.getCode().setScriptName(s);
    }//GEN-LAST:event_popmCodeScriptNameActionPerformed

    private void popmCodeAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeAuthorActionPerformed
        // Author
        if(tabpCode.getTabCount() == 0) return;
        if(tabpCode.getSelectedIndex() == -1) return;
        CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabpCode.getSelectedIndex());
        String s = JOptionPane.showInputDialog("Type an author name :");
        cp.getCode().setAuthor(s);
    }//GEN-LAST:event_popmCodeAuthorActionPerformed

    private void popmCodeVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeVersionActionPerformed
        // Version
        if(tabpCode.getTabCount() == 0) return;
        if(tabpCode.getSelectedIndex() == -1) return;
        CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabpCode.getSelectedIndex());
        String s = JOptionPane.showInputDialog("Type a release version :");
        cp.getCode().setVersion(s);
    }//GEN-LAST:event_popmCodeVersionActionPerformed

    private void popmCodeDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeDescriptionActionPerformed
        // Description
        if(tabpCode.getTabCount() == 0) return;
        if(tabpCode.getSelectedIndex() == -1) return;
        CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabpCode.getSelectedIndex());
        String s = JOptionPane.showInputDialog("Type a description :");
        cp.getCode().setDescription(s);
    }//GEN-LAST:event_popmCodeDescriptionActionPerformed

    private void popmCodeUpdateDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCodeUpdateDetailsActionPerformed
        // UpdateDetails
        if(tabpCode.getTabCount() == 0) return;
        if(tabpCode.getSelectedIndex() == -1) return;
        CodePanel cp = (CodePanel)tabpCode.getComponentAt(tabpCode.getSelectedIndex());
        String s = JOptionPane.showInputDialog("Type an update details :");
        cp.getCode().setUpdateDetails(s);
    }//GEN-LAST:event_popmCodeUpdateDetailsActionPerformed

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("Convert2Lambda")
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SettingsDialog dialog = new SettingsDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNewCode;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnOpenCode;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSaveCode;
    private javax.swing.JButton btnSaveEffects;
    private javax.swing.JComboBox<String> cboxAFMEffects;
    private javax.swing.JFileChooser fcOpenCode;
    private javax.swing.JFileChooser fcOpenEffects;
    private javax.swing.JFileChooser fcSaveCode;
    private javax.swing.JFileChooser fcSaveEffects;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList<String> listTemplates;
    private javax.swing.JPopupMenu popCode;
    private javax.swing.JPopupMenu popCommands;
    private javax.swing.JPopupMenu popTemplates;
    private javax.swing.JMenuItem popmAnimateSentence;
    private javax.swing.JMenuItem popmAnimateSentenceWithAcc;
    private javax.swing.JMenuItem popmAnimateTime;
    private javax.swing.JMenuItem popmAnimateWithAcc;
    private javax.swing.JMenuItem popmCodeAuthor;
    private javax.swing.JMenuItem popmCodeCopy;
    private javax.swing.JMenuItem popmCodeCut;
    private javax.swing.JMenuItem popmCodeDescription;
    private javax.swing.JMenuItem popmCodePaste;
    private javax.swing.JMenuItem popmCodeScriptName;
    private javax.swing.JMenuItem popmCodeUpdateDetails;
    private javax.swing.JMenuItem popmCodeVersion;
    private javax.swing.JMenuItem popmCsDuration;
    private javax.swing.JMenuItem popmCsEnd;
    private javax.swing.JMenuItem popmCsLetterDuration;
    private javax.swing.JMenuItem popmCsLetterEnd;
    private javax.swing.JMenuItem popmCsLetterMiddle;
    private javax.swing.JMenuItem popmCsLetterStart;
    private javax.swing.JMenuItem popmCsMiddle;
    private javax.swing.JMenuItem popmCsStart;
    private javax.swing.JMenuItem popmDisplayAlpha;
    private javax.swing.JMenuItem popmDisplayBlur;
    private javax.swing.JMenuItem popmDisplayBlurEdge;
    private javax.swing.JMenuItem popmDisplayFad;
    private javax.swing.JMenuItem popmDisplayFade;
    private javax.swing.JMenuItem popmDisplayKaraokeAlpha;
    private javax.swing.JMenuItem popmDisplayKaraokeColor;
    private javax.swing.JMenuItem popmDisplayOutlineAlpha;
    private javax.swing.JMenuItem popmDisplayOutlineColor;
    private javax.swing.JMenuItem popmDisplayPrimaryColor;
    private javax.swing.JMenuItem popmDisplayShadowAlpha;
    private javax.swing.JMenuItem popmDisplayShadowColor;
    private javax.swing.JMenuItem popmDisplayTextAlpha;
    private javax.swing.JMenuItem popmDisplayTextColor;
    private javax.swing.JMenuItem popmDrawBaselineOffset;
    private javax.swing.JMenuItem popmDrawCloseSpline;
    private javax.swing.JMenuItem popmDrawCubic;
    private javax.swing.JMenuItem popmDrawExtendSpline;
    private javax.swing.JMenuItem popmDrawGoto;
    private javax.swing.JMenuItem popmDrawLine;
    private javax.swing.JMenuItem popmDrawMove;
    private javax.swing.JMenuItem popmDrawP;
    private javax.swing.JMenuItem popmDrawSpline;
    private javax.swing.JMenuItem popmFontAttrBold;
    private javax.swing.JMenuItem popmFontAttrBord;
    private javax.swing.JMenuItem popmFontAttrItalic;
    private javax.swing.JMenuItem popmFontAttrShad;
    private javax.swing.JMenuItem popmFontAttrStrikeOut;
    private javax.swing.JMenuItem popmFontAttrUnderline;
    private javax.swing.JMenuItem popmFontAttrXBord;
    private javax.swing.JMenuItem popmFontAttrXShad;
    private javax.swing.JMenuItem popmFontAttrYBord;
    private javax.swing.JMenuItem popmFontAttrYShad;
    private javax.swing.JMenuItem popmFontEncoding;
    private javax.swing.JMenuItem popmFontName;
    private javax.swing.JMenuItem popmFontRX;
    private javax.swing.JMenuItem popmFontRY;
    private javax.swing.JMenuItem popmFontRZ;
    private javax.swing.JMenuItem popmFontRZSimplified;
    private javax.swing.JMenuItem popmFontReset;
    private javax.swing.JMenuItem popmFontScale;
    private javax.swing.JMenuItem popmFontScaleX;
    private javax.swing.JMenuItem popmFontScaleY;
    private javax.swing.JMenuItem popmFontShearX;
    private javax.swing.JMenuItem popmFontShearY;
    private javax.swing.JMenuItem popmFontSize;
    private javax.swing.JMenuItem popmFontSpacing;
    private javax.swing.JMenuItem popmKaraokeBigK;
    private javax.swing.JMenuItem popmKaraokeFill;
    private javax.swing.JMenuItem popmKaraokeMinusK;
    private javax.swing.JMenuItem popmKaraokeOutline;
    private javax.swing.JMenuItem popmMsDuration;
    private javax.swing.JMenuItem popmMsEnd;
    private javax.swing.JMenuItem popmMsLetterDuration;
    private javax.swing.JMenuItem popmMsLetterEnd;
    private javax.swing.JMenuItem popmMsLetterMiddle;
    private javax.swing.JMenuItem popmMsLetterStart;
    private javax.swing.JMenuItem popmMsMiddle;
    private javax.swing.JMenuItem popmMsSentenceDuration;
    private javax.swing.JMenuItem popmMsSentenceEnd;
    private javax.swing.JMenuItem popmMsSentenceMiddle;
    private javax.swing.JMenuItem popmMsSentenceStart;
    private javax.swing.JMenuItem popmMsStart;
    private javax.swing.JMenuItem popmPosA;
    private javax.swing.JMenuItem popmPosAn;
    private javax.swing.JMenuItem popmPosMove;
    private javax.swing.JMenuItem popmPosOrg;
    private javax.swing.JMenuItem popmPosPos;
    private javax.swing.JMenuItem popmRemove;
    private javax.swing.JMenuItem popmSave;
    private javax.swing.JMenuItem popmTriggerLetter;
    private javax.swing.JMenuItem popmTriggerSentence;
    private javax.swing.JMenuItem popmTriggerSyllable;
    private javax.swing.JMenuItem popmVisibilityClip;
    private javax.swing.JMenuItem popmVisibilityIClip;
    private javax.swing.JMenuItem popmWrapLine;
    private javax.swing.JMenu popmenuAnimation;
    private javax.swing.JMenu popmenuDisplay;
    private javax.swing.JMenu popmenuDrawing;
    private javax.swing.JMenu popmenuFont;
    private javax.swing.JMenu popmenuFontAttribute;
    private javax.swing.JMenu popmenuKaraoke;
    private javax.swing.JMenu popmenuList;
    private javax.swing.JMenu popmenuPosition;
    private javax.swing.JMenu popmenuTriggers;
    private javax.swing.JMenu popmenuTriggersCs;
    private javax.swing.JMenu popmenuTriggersMs;
    private javax.swing.JMenu popmenuVisibility;
    private javax.swing.JTabbedPane tabpCode;
    private javax.swing.JTextPane textPanelCommands;
    // End of variables declaration//GEN-END:variables
}
