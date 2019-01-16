/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.xtrn;

import feuille.xtrn.XtrnLib.EffectType;
import feuille.xtrn.XtrnLib.ModeType;
import feuille.xtrn.XtrnLib.TreatmentType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Yves
 */
public class XKCFrame extends javax.swing.JFrame {
    
    XKCStep0 xkc0 = new XKCStep0();
    XKCStep1 xkc1 = new XKCStep1();
    XKCStep2A xkc2A = new XKCStep2A();
    XKCStep2B xkc2B = new XKCStep2B();
    XKCStep2C xkc2C = new XKCStep2C();
    XKCStep2D xkc2D = new XKCStep2D();
    XKCStep3 xkc3 = new XKCStep3();
    XKCStep4 xkc4 = new XKCStep4();
    XKCStep5 xkc5 = new XKCStep5();
    ChooseFX cfx = new ChooseFX();
    
    EffectType selectedEF = EffectType.Normal;
    String commands = "";
    ModeType selectedMode = ModeType.Normal;
    TreatmentType selectedTreatmant = TreatmentType.Line;
    String name = "", authors = "", description = "";
    List<String> outList = new ArrayList<>();
    String folder = "";

    /**
     * Creates new form XKCFrame
     */
    public XKCFrame() {
        initComponents();
        init();        
    }
    
    private void init(){
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        JOptionPane.showMessageDialog(null, "1a");
        
        getContentPane().add(xkc0);
        pack();
        setLocationRelativeTo(null);
        
        xkc0.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFromZero();
            }
        });
        
        xkc1.getCreateFXButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextCreateFX();
            }
        });
        
        xkc1.getSelectFXButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextSelectFX();
            }
        });
        
        xkc2A.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFrom2A();
            }
        });
        
        xkc2B.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFrom2B();
            }
        });
        
        xkc2C.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFrom2C();
            }
        });
        
        xkc2D.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFrom2D();
            }
        });
        
        xkc3.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFrom3();
            }
        });
        
        xkc4.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFrom4();
            }
        });
        
        cfx.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFromCFX();
            }
        });
    }
    
    private void nextFromZero(){
        selectedEF = xkc0.getSelectedEffectType();
        if(selectedEF == EffectType.Normal | selectedEF == EffectType.Periodic
                | selectedEF == EffectType.Random | selectedEF == EffectType.Symmetric){
            getContentPane().removeAll();            
            getContentPane().add(xkc3);
            pack();
            setLocationRelativeTo(null);
        }
    }
    
    private void nextCreateFX(){
        getContentPane().removeAll();
        if(selectedTreatmant == TreatmentType.Line){
            if(selectedEF == EffectType.Normal){
                getContentPane().add(xkc2B);
            }else{
                getContentPane().add(xkc2A);
            }
        }else{
            if(selectedMode == ModeType.Character){
                if(selectedEF == EffectType.Normal){
                    getContentPane().add(xkc2B);
                }else{
                    getContentPane().add(xkc2A);
                }
            }else{
                if(selectedEF == EffectType.Normal){
                    getContentPane().add(xkc2D);
                }else{
                    getContentPane().add(xkc2C);
                }
            }            
        }        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void nextSelectFX(){
        List<AegiObject> aegilist = new ArrayList<>();
        try{
            XmlAegiHandler hd = new XmlAegiHandler(folder+File.separator+"saveDataBase.xml");
            aegilist = hd.getAegiObjectList();
        }catch(IOException | ParserConfigurationException | SAXException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
        List<AegiObject> sublist = new ArrayList<>();
        for(AegiObject ao : aegilist){
            if(ao.getEffectType() == selectedEF 
                    && ao.getModeType() == selectedMode 
                    && ao.getTreatmentType() == selectedTreatmant){
                sublist.add(ao);
            }
        }
        
        if(sublist.isEmpty()){
            JOptionPane.showMessageDialog(this, "There is no effects.");
        }else{    
            getContentPane().removeAll();
            getContentPane().add(cfx);
            cfx.setData(sublist);
            pack();
            setLocationRelativeTo(null);
        }
    }
    
    private void nextFrom2A(){
        commands = xkc2A.getCommands();
        getContentPane().removeAll();
        getContentPane().add(xkc4);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void nextFrom2B(){
        commands = xkc2B.getCommands();
        getContentPane().removeAll();
        getContentPane().add(xkc4);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void nextFrom2C(){
        commands = xkc2C.getCommands();
        getContentPane().removeAll();
        getContentPane().add(xkc4);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void nextFrom2D(){
        commands = xkc2D.getCommands();
        getContentPane().removeAll();
        getContentPane().add(xkc4);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void nextFrom3(){
        selectedMode = xkc3.getSelectedModeType();
        selectedTreatmant = xkc3.getSelectedTreatmentType();
        getContentPane().removeAll();
        getContentPane().add(xkc1);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void nextFrom4(){
        name = xkc4.getEffectName();
        authors = xkc4.getEffectAuthors();
        description = xkc4.getEffectDescription();
        
        if(name.isEmpty()==false 
                && authors.isEmpty()==false 
                && description.isEmpty()==false){
            
            List<AegiObject> aegilist = new ArrayList<>();
            
            try {
                XmlAegiHandler hd = new XmlAegiHandler(folder+File.separator+"saveDataBase.xml");
                aegilist = hd.getAegiObjectList();
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(XKCFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            AegiObject ao = new AegiObject();
            ao.setAuthors(authors);
            ao.setCommands(commands);
            ao.setDescription(description);
            ao.setEffectType(selectedEF);
            ao.setModeType(selectedMode);
            ao.setName(name);
            ao.setTreatmentType(selectedTreatmant);            
            aegilist.add(ao);
            
            XmlAegiWriter wr = new XmlAegiWriter();
            wr.setAegiObjectList(aegilist);
            wr.createAegiBase(folder+File.separator+"saveDataBase.xml");
            
            doFx();
        }else{
            JOptionPane.showMessageDialog(this, "Please fill the form.");
        }
    }
    
    private void nextFromCFX(){
        commands = cfx.getCommmands();
        
        if(commands.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please choose an effect.");
        }else{
            cfx.rewriteXmlDataBase(folder);
            doFx();
        }
    }
    
    private void doFx(){
        getContentPane().removeAll();
        getContentPane().add(xkc5);
        pack();
        setLocationRelativeTo(null);
        
        outList.clear();
        
        try {
            List<String> inList = readFile(folder+File.separator+"in.txt");
            List<String> keyzList = readFile(folder+File.separator+"line_keywords.txt");
            List<String> skWholeList = readFile(folder+File.separator+"syllable_keywords.txt");
            List<String> ckWholeList = readFile(folder+File.separator+"char_keywords.txt");
            
            int count = 0; 
            for(String s : inList){
                
                String keyzOfLine = keyzList.get(count);
                List<String> keyzOfSyllables = getSylKeywords(skWholeList, count);
                List<String> keyzOfChars = getCharKeywords(ckWholeList, count);
                
                count += 1;
                List<String> newlineList = new ArrayList<>();
                
                xkc5.setNumber(count);
                
                if(selectedEF == EffectType.Normal && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Line){
                    newlineList = XtrnOps.getForNormalNormalLine(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Periodic && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Line){
                    newlineList = XtrnOps.getForPeriodicNormalLine(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Random && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Line){
                    newlineList = XtrnOps.getForRandomNormalLine(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Symmetric && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Line){
                    newlineList = XtrnOps.getForSymmetricNormalLine(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Normal && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Line){
                    newlineList = XtrnOps.getForNormalCharacterLine(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Periodic && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Line){
                    newlineList = XtrnOps.getForPeriodicCharacterLine(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Random && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Line){
                    newlineList = XtrnOps.getForRandomCharacterLine(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Symmetric && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Line){
                    newlineList = XtrnOps.getForSymmetricCharacterLine(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Normal && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Syllable){
                    newlineList = XtrnOps.getForNormalNormalSyllable(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Periodic && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Syllable){
                    newlineList = XtrnOps.getForPeriodicNormalSyllable(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Random && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Syllable){
                    newlineList = XtrnOps.getForRandomNormalSyllable(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Symmetric && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Syllable){
                    newlineList = XtrnOps.getForSymmetricNormalSyllable(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Normal && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Syllable){
                    newlineList = XtrnOps.getForNormalCharacterSyllable(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Periodic && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Syllable){
                    newlineList = XtrnOps.getForPeriodicCharacterSyllable(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Random && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Syllable){
                    newlineList = XtrnOps.getForRandomCharacterSyllable(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Symmetric && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Syllable){
                    newlineList = XtrnOps.getForSymmetricCharacterSyllable(s, commands, keyzOfLine, keyzOfSyllables);
                }else if(selectedEF == EffectType.Normal && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Character){
                    newlineList = XtrnOps.getForNormalNormalCharacter(s, commands, keyzOfLine, keyzOfSyllables, keyzOfChars);
                }else if(selectedEF == EffectType.Periodic && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Character){
                    newlineList = XtrnOps.getForPeriodicNormalCharacter(s, commands, keyzOfLine, keyzOfSyllables, keyzOfChars);
                }else if(selectedEF == EffectType.Random && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Character){
                    newlineList = XtrnOps.getForRandomNormalCharacter(s, commands, keyzOfLine, keyzOfSyllables, keyzOfChars);
                }else if(selectedEF == EffectType.Symmetric && selectedMode == ModeType.Normal && selectedTreatmant == TreatmentType.Character){
                    newlineList = XtrnOps.getForSymmetricNormalCharacter(s, commands, keyzOfLine, keyzOfSyllables, keyzOfChars);
                }else if(selectedEF == EffectType.Normal && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Character){
                    newlineList = XtrnOps.getForNormalNormalCharacter(s, commands, keyzOfLine, keyzOfSyllables, keyzOfChars);
                }else if(selectedEF == EffectType.Periodic && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Character){
                    newlineList = XtrnOps.getForPeriodicNormalCharacter(s, commands, keyzOfLine, keyzOfSyllables, keyzOfChars);
                }else if(selectedEF == EffectType.Random && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Character){
                    newlineList = XtrnOps.getForRandomNormalCharacter(s, commands, keyzOfLine, keyzOfSyllables, keyzOfChars);
                }else if(selectedEF == EffectType.Symmetric && selectedMode == ModeType.Character && selectedTreatmant == TreatmentType.Character){
                    newlineList = XtrnOps.getForSymmetricNormalCharacter(s, commands, keyzOfLine, keyzOfSyllables, keyzOfChars);
                }
                
                for(String output : newlineList){
                    addOutputLine(output);
                }
            }
            writeFile(folder+File.separator+"out.txt", outList);
            
            xkc5.setNumber(6000);
            
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(XKCFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addOutputLine(String outputLine){
        outList.add(outputLine);
    }
    
    private void writeFile(String outFile, List<String> list) throws IOException{
        try (FileWriter fw = new FileWriter(outFile); PrintWriter pw = new PrintWriter(fw)) {
            for(String s : list){
                pw.println(s); 
            }                               
        }
    }
    
    private List<String> readFile(String inFile) throws FileNotFoundException, IOException{
        List<String> inList = new ArrayList<>();
        
        FileReader fr = new FileReader(inFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while((line = br.readLine()) != null){
            inList.add(line);
        }
        
        return inList;
    }
    
    public void setFolder(String folder){
        this.folder = folder;
    }
    
    private List<String> getSylKeywords(List<String> lineKeywordsForSyl, int index){
        List<String> sk = new ArrayList<>();
        
        String inline = lineKeywordsForSyl.get(index);
        String[] table = inline.split("_");
        
        sk.addAll(Arrays.asList(table));
        
        return sk;
    }
    
    private List<String> getCharKeywords(List<String> lineKeywordsForChar, int index){
        List<String> sk = new ArrayList<>();
        
        String inline = lineKeywordsForChar.get(index);
        inline = inline.replaceAll("¤SPACE_CHAR", "");
        String[] table = inline.split("¤");
        
        sk.addAll(Arrays.asList(table));
        
        return sk;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(XKCFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new XKCFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
