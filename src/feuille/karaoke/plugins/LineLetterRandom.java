/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.plugins;

import feuille.karaoke.plugins.AssFunctionPlugin;

/**
 *
 * @author The Wingate 2940
 */
public class LineLetterRandom implements AssFunctionPlugin {

    private String[][] myCommands; //generic
    private String myXMLPreset; //generic
    private String myNbLayers; //generic
    private String myFirstLayer; //generic
    private String myMoment; //generic
    private String myMomentTime; //generic
    private String myStyles; //generic
    private String myCode; //generic

    @Override
    public String forOneLine() {
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

        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);

        //Apply effects on sentence
        String newSentence = "";
        //For generation of numbers
        Integer randLayer = 0; Integer lastRandom = 0;
        java.util.Random seed = new java.util.Random();

        for(int i=0;i<syl.length;i++){
            if(myCommands.length>1){ //if more than one layer of effets
                while(randLayer==lastRandom){
                    randLayer = seed.nextInt(myCommands.length);
                }
                lastRandom=randLayer;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = i==0? myCommands[randLayer][0] : myCommands[randLayer][3];

            //Initialize variables for ruby scripting and execute the script to return a value
            String new_expression = feuille.karaoke.KaraokePanel.
                    phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
            
            //Do calcul with preset and syllabe parameters
            new_expression = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(new_expression, syl, i, head, osyl, crossIndex[i]);

            //Add it to the new sentence
            newSentence = newSentence + new_expression +
                    syl[i][0].toString();

        }

        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
        //Return the modified line
        feuille.scripting.ScriptPlugin.addResLine(line);

        return "forOneLine() :: Operation succeeded !";
    }

    @Override
    public String forFewLines() {
        //Get the selected lines
        String[] lines = feuille.scripting.ScriptPlugin.getSelectedOrgLines();

        // For each line of lines table.
        for(String line : lines){
            //Get the sentence for this line
            String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
            //Get the header for this line
            String head = feuille.scripting.ScriptPlugin.getHead(line);
            //Try to transform the header with 'the moment'
            head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, myMoment, myMomentTime);
            //Try to change the number of the first layer
            head = feuille.scripting.ScriptPlugin.phChangeLayer(head, myFirstLayer);

            // Get a karaoke of letters
            String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
            // Table for letters :
            Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
            // Get original index for karaoke by syllable
            int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
            // The original karaoke
            Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);

            //Apply effects on sentence
            String newSentence = "";
            //For generation of numbers
            Integer randLayer = 0; Integer lastRandom = 0;
            java.util.Random seed = new java.util.Random();

            for(int i=0;i<syl.length;i++){
                if(myCommands.length>1){ //if more than one layer of effets
                    while(randLayer==lastRandom){
                        randLayer = seed.nextInt(myCommands.length);
                    }
                    lastRandom=randLayer;
                }

                //Get commands (if i=0 then 1st overrides)
                String c = i==0? myCommands[randLayer][0] : myCommands[randLayer][3];
                //Initialize variables for ruby scripting and execute the script to return a value
                String new_expression = feuille.karaoke.KaraokePanel.
                        phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
                //Do calcul with preset and syllabe parameters
                new_expression = feuille.scripting.ScriptPlugin.
                        phReplaceParameters(new_expression, syl, i, head, osyl, crossIndex[i]);
                //Add it to the new sentence
                newSentence = newSentence + new_expression +
                        syl[i][0].toString();

            }

            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            //Return the modified line
            feuille.scripting.ScriptPlugin.addResLine(line);
        }

        return "forFewLines() :: Operation succeeded !";
    }

    @Override
    public void setCommands(String commands) {
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int i = 0;
        while(m.find()){
            myCommands[i][0] = m.group(1); //1st overrides (required)
            myCommands[i][1] = m.group(2); //main overrides (useless)
            myCommands[i][2] = m.group(3); //last overrides (useless)
            myCommands[i][3] = m.group(4); //before syllabe (required)
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
    public String getPluginName() {
        return "LineLetterRandom";
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
        return "Random line(s) - letters";
    }

    @Override
    public String getAuthors() {
        return "Funsub Project/AssFxMaker owner";
    }

    @Override
    public void setStyle(String styles) {
        myStyles = styles;
    }

    @Override
    public void setRubyCode(String code) {
        myCode = code;
    }

}
