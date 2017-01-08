/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.plugins;

import feuille.karaoke.plugins.AssFunctionPlugin;
import java.awt.Font;
import java.util.List;
import feuille.karaoke.lib.AssStyle;

/**
 *
 * @author The Wingate 2940
 */
public class SylSyllableBasic implements AssFunctionPlugin {
    
    private String[][] myCommands; //generic
    private String myXMLPreset; //generic
    private String myNbLayers; //generic
    private String myFirstLayer; //generic
    private String myMoment; //generic
    private String myMomentTime; //generic
    private String myCode; //generic
    private AssStyle myAs;
    private Font myFont;

    @Override
    public String forOneLine() {
        if(myFont!=null){
            //Get the selected line
            String line = feuille.scripting.ScriptPlugin.getSelectedOrgLine();
            //Get the sentence for this line
            String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
            //Get the header for this line
            String head = feuille.scripting.ScriptPlugin.getHead(line);
            //Try to transform the header with 'the moment'
            head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, myMoment, myMomentTime);
            //Try to change the number of the first layer
            head = feuille.scripting.ScriptPlugin.phChangeLayer(head, myFirstLayer);
            //Get a table of syllabe parameters for the basic karaoke
            Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);

            //Get a table of position X for all syllables
            double[][] posSylX = feuille.scripting.ScriptPlugin.phXAbscissas(myFont, sentence);
            //Get the position Y for all syllables
            double[] posSylY = feuille.scripting.ScriptPlugin.phYOrdinates(myFont);
            //Get the offset position on X
            int posOffsetX = feuille.scripting.ScriptPlugin.getPosXFromStyle(myAs, sentence);
            //Get the offset position on Y
            int posOffsetY = feuille.scripting.ScriptPlugin.getPosYFromStyle(myAs);
            
            //Apply effects on sentence
            String newSentence;
            
            for(int j=0;j<myCommands.length;j++){
                for(int i=0;i<osyl.length;i++){

                    //Get commands (if i=0 then 1st overrides)
                    String c = myCommands[j][0];
                    
                    //Get positions
                    c = feuille.scripting.ScriptPlugin.phReplacePosParameters(c,
                            posSylX, posSylY, posOffsetX, posOffsetY, i);

                    //Initialize variables for ruby scripting and execute the script to return a value
                    String new_expression = feuille.karaoke.KaraokePanel.
                            phRubyCode(c, head, osyl, null, i, -1, myCode);
                    
                    //Do calcul with preset and syllabe parameters
                    new_expression = feuille.scripting.ScriptPlugin.
                            phReplaceParameters(new_expression, osyl, i, head, null, -1);

                    //Add it to the new sentence
                    newSentence = new_expression + osyl[i][0].toString();
                    
                    //Reformat assline
                    line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);

                    //Return the modified line
                    feuille.scripting.ScriptPlugin.addResLine(line);
                }
            }
            return "forOneLine :: success !";
        }else{
            return "forOneLine :: error !";
        }
    }

    @Override
    public String forFewLines() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCommands(String commands) {
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int i = 0;
        while(m.find()){
            myCommands[i][0] = m.group(1); //1st overrides (required)
            myCommands[i][1] = m.group(2); //main overrides (useless)
            myCommands[i][2] = m.group(3); //last overrides (useless)
            myCommands[i][3] = m.group(4); //before syllabe (useless)
            myCommands[i][4] = m.group(5); //after syllabe (useless)
            i+=1;
        }
    }

    @Override
    public void setXMLPresetName(String name) {
        myXMLPreset = name;
    }

    @Override
    public void setNbLayers(String nbLayers) {
        myNbLayers = nbLayers;
    }

    @Override
    public void setFirstLayer(String firstLayer) {
        myFirstLayer = firstLayer;
    }

    @Override
    public void setMoment(String moment) {
        myMoment = moment;
    }

    @Override
    public void setTime(String momentTime) {
        myMomentTime = momentTime;
    }

    @Override
    public void setStyle(String styles) {
        if(styles!=null){
            try{
            List<AssStyle> lstAS = AssStyle.unlinkAssStyles(styles);
            myAs = lstAS.get(0);
            myFont = myAs.getFont();
            }catch(Exception exc){
                // 'styles' is not defined.
            }
        }
    }

    @Override
    public String getPluginName() {
        return "SylSyllableBasic";
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getHelp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDisplayName() {
        return "One line per syllable - syllables";
    }

    @Override
    public String getAuthors() {
        return "Funsub Project/AssFxMaker owner";
    }

    @Override
    public void setRubyCode(String code) {
        myCode = code;
    }

}
