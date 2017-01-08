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
public class LineSyllableComplex implements AssFunctionPlugin {

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

        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);

        //Apply effects on sentence
        String newSentence = "";

        for(int j=0;j<myCommands.length;j++){
            for(int i=0;i<osyl.length;i++){
                if(i==0){ //Beginning
                    String a1 = myCommands[j][0]; //1st overrides
                    String e1 = myCommands[j][4]; //after syllable

                    //Initialize variables for ruby scripting and execute the script to return a value
                    String a2 = feuille.karaoke.KaraokePanel.
                            phRubyCode(a1, head, osyl, null, i, -1, myCode);
                    String e2 = feuille.karaoke.KaraokePanel.
                            phRubyCode(e1, head, osyl, null, i, -1, myCode);
                    
                    //Do calcul with preset and syllabe parameters
                    a2 = feuille.scripting.ScriptPlugin.
                            phReplaceParameters(a2, osyl, i, head, null, -1);
                    e2 = feuille.scripting.ScriptPlugin.
                            phReplaceParameters(e2, osyl, i, head, null, -1);

                    //Add it to the new sentence
                    newSentence = newSentence + a2 + osyl[i][0].toString() + e2;
                    for (int k=1;k<osyl.length;k++){newSentence += osyl[k][0];}

                    line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);//Reformat assline
                    feuille.scripting.ScriptPlugin.addResLine(line);//Return the modified line
                    newSentence = "";

                }else if(i==osyl.length-1){ //End
                    String c1 = myCommands[j][2]; //Last overrides
                    String d1 = myCommands[j][3]; //before syllable
                    
                    //Initialize variables for ruby scripting and execute the script to return a value
                    String c2 = feuille.karaoke.KaraokePanel.
                            phRubyCode(c1, head, osyl, null, i, -1, myCode);
                    String d2 = feuille.karaoke.KaraokePanel.
                            phRubyCode(d1, head, osyl, null, i, -1, myCode);

                    //Do calcul with preset and syllabe parameters
                    c2 = feuille.scripting.ScriptPlugin.
                            phReplaceParameters(c2, osyl, i, head, null, -1);
                    d2 = feuille.scripting.ScriptPlugin.
                            phReplaceParameters(d2, osyl, i, head, null, -1);

                    //Add it to the new sentence
                    newSentence += c2;
                    for (int k=0;k<osyl.length-1;k++){newSentence += osyl[k][0];}
                    newSentence = newSentence + d2 + osyl[i][0].toString();

                    line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);//Reformat assline
                    feuille.scripting.ScriptPlugin.addResLine(line);//Return the modified line
                    newSentence = "";

                }else{ //Body
                    String b1 = myCommands[j][1]; //Inner overrides
                    String d1 = myCommands[j][3]; //before syllable
                    String e1 = myCommands[j][4]; //after syllable

                    //Initialize variables for ruby scripting and execute the script to return a value
                    String b2 = feuille.karaoke.KaraokePanel.
                            phRubyCode(b1, head, osyl, null, i, -1, myCode);
                    String d2 = feuille.karaoke.KaraokePanel.
                            phRubyCode(d1, head, osyl, null, i, -1, myCode);
                    String e2 = feuille.karaoke.KaraokePanel.
                            phRubyCode(e1, head, osyl, null, i, -1, myCode);
                    
                    //Do calcul with preset and syllabe parameters
                    b2 = feuille.scripting.ScriptPlugin.
                            phReplaceParameters(b2, osyl, i, head, null, -1);
                    d2 = feuille.scripting.ScriptPlugin.
                            phReplaceParameters(d2, osyl, i, head, null, -1);
                    e2 = feuille.scripting.ScriptPlugin.
                            phReplaceParameters(e2, osyl, i, head, null, -1);

                    //Add it to the new sentence
                    newSentence += b2;
                    for (int k=0;k<i;k++){newSentence += osyl[k][0];}
                    newSentence = newSentence + d2 + osyl[i][0] + e2;
                    for (int k=i+1;k<osyl.length;k++){newSentence += osyl[k][0];}

                    line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);//Reformat assline
                    feuille.scripting.ScriptPlugin.addResLine(line);//Return the modified line
                    newSentence = "";
                }
            }
        }
        return null;
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

            //Get a table of syllabe parameters for the basic karaoke
            Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);

            //Apply effects on sentence
            String newSentence = "";

            for(int j=0;j<myCommands.length;j++){
                for(int i=0;i<osyl.length;i++){
                    if(i==0){ //Beginning
                        String a1 = myCommands[j][0]; //1st overrides
                        String e1 = myCommands[j][4]; //after syllable

                        //Initialize variables for ruby scripting and execute the script to return a value
                        String a2 = feuille.karaoke.KaraokePanel.
                                phRubyCode(a1, head, osyl, null, i, -1, myCode);
                        String e2 = feuille.karaoke.KaraokePanel.
                                phRubyCode(e1, head, osyl, null, i, -1, myCode);

                        //Do calcul with preset and syllabe parameters
                        a2 = feuille.scripting.ScriptPlugin.
                                phReplaceParameters(a2, osyl, i, head, null, -1);
                        e2 = feuille.scripting.ScriptPlugin.
                                phReplaceParameters(e2, osyl, i, head, null, -1);

                        //Add it to the new sentence
                        newSentence = newSentence + a2 + osyl[i][0].toString() + e2;
                        for (int k=1;k<osyl.length;k++){newSentence += osyl[k][0];}

                        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);//Reformat assline
                        feuille.scripting.ScriptPlugin.addResLine(line);//Return the modified line
                        newSentence = "";

                    }else if(i==osyl.length-1){ //End
                        String c1 = myCommands[j][2]; //Last overrides
                        String d1 = myCommands[j][3]; //before syllable

                        //Initialize variables for ruby scripting and execute the script to return a value
                        String c2 = feuille.karaoke.KaraokePanel.
                                phRubyCode(c1, head, osyl, null, i, -1, myCode);
                        String d2 = feuille.karaoke.KaraokePanel.
                                phRubyCode(d1, head, osyl, null, i, -1, myCode);

                        //Do calcul with preset and syllabe parameters
                        c2 = feuille.scripting.ScriptPlugin.
                                phReplaceParameters(c2, osyl, i, head, null, -1);
                        d2 = feuille.scripting.ScriptPlugin.
                                phReplaceParameters(d2, osyl, i, head, null, -1);

                        //Add it to the new sentence
                        newSentence += c2;
                        for (int k=0;k<osyl.length-1;k++){newSentence += osyl[k][0];}
                        newSentence = newSentence + d2 + osyl[i][0].toString();

                        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);//Reformat assline
                        feuille.scripting.ScriptPlugin.addResLine(line);//Return the modified line
                        newSentence = "";

                    }else{ //Body
                        String b1 = myCommands[j][1]; //Inner overrides
                        String d1 = myCommands[j][3]; //before syllable
                        String e1 = myCommands[j][4]; //after syllable

                        //Initialize variables for ruby scripting and execute the script to return a value
                        String b2 = feuille.karaoke.KaraokePanel.
                                phRubyCode(b1, head, osyl, null, i, -1, myCode);
                        String d2 = feuille.karaoke.KaraokePanel.
                                phRubyCode(d1, head, osyl, null, i, -1, myCode);
                        String e2 = feuille.karaoke.KaraokePanel.
                                phRubyCode(e1, head, osyl, null, i, -1, myCode);

                        //Do calcul with preset and syllabe parameters
                        b2 = feuille.scripting.ScriptPlugin.
                                phReplaceParameters(b2, osyl, i, head, null, -1);
                        d2 = feuille.scripting.ScriptPlugin.
                                phReplaceParameters(d2, osyl, i, head, null, -1);
                        e2 = feuille.scripting.ScriptPlugin.
                                phReplaceParameters(e2, osyl, i, head, null, -1);

                        //Add it to the new sentence
                        newSentence += b2;
                        for (int k=0;k<i;k++){newSentence += osyl[k][0];}
                        newSentence = newSentence + d2 + osyl[i][0] + e2;
                        for (int k=i+1;k<osyl.length;k++){newSentence += osyl[k][0];}

                        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);//Reformat assline
                        feuille.scripting.ScriptPlugin.addResLine(line);//Return the modified line
                        newSentence = "";
                    }
                }
            }
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
            myCommands[i][1] = m.group(2); //main overrides (required)
            myCommands[i][2] = m.group(3); //last overrides (required)
            myCommands[i][3] = m.group(4); //before syllabe (required)
            myCommands[i][4] = m.group(5); //after syllabe (required)
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
        return "LineSyllableComplex";
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
        return "Complex line(s) - syllables";
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
