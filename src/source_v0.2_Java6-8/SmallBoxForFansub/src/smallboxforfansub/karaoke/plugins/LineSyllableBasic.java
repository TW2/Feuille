/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.karaoke.plugins;

/**
 *
 * @author The WinGate 2940 (Funsub Project/AssFxMaker owner)
 */
public class LineSyllableBasic implements AssFunctionPlugin {

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
        String line = smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLine();

        //Get the sentence for this line
        String sentence = smallboxforfansub.scripting.ScriptPlugin.getSentence(line);

        //Get the header for this line
        String head = smallboxforfansub.scripting.ScriptPlugin.getHead(line);

        //Try to transform the header with 'the moment'
        head = smallboxforfansub.scripting.ScriptPlugin.phBeforeAfter(head, myMoment, myMomentTime);

        //Try to change the number of the first layer
        head = smallboxforfansub.scripting.ScriptPlugin.phChangeLayer(head, myFirstLayer);

        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = smallboxforfansub.scripting.ScriptPlugin.phKaraoke(sentence);

        //Apply effects on sentence
        String newSentence = "";
        for(int j=0;j<myCommands.length;j++){
            for(int i=0;i<osyl.length;i++){

                //Get commands (if i=0 then 1st overrides)
                String c = i==0? myCommands[j][0] : myCommands[j][3];
                
                //Initialize variables for ruby scripting and execute the script to return a value
                String new_expression = smallboxforfansub.karaoke.KaraokePanel.
                        phRubyCode(c, head, osyl, null, i, -1, myCode);

                //Do calcul with preset and syllabe parameters
                new_expression = smallboxforfansub.scripting.ScriptPlugin.
                        phReplaceParameters(new_expression, osyl, i, head, null, -1);

                //Add it to the new sentence
                newSentence = newSentence + new_expression +
                        osyl[i][0].toString();
            }
            
            //Reformat assline
            line = smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(head, newSentence);

            //Return the modified line
            smallboxforfansub.scripting.ScriptPlugin.addResLine(line);

            //Reinit
            newSentence = "";
        }
        
        
        return "forOneLine() :: Operation succeeded !";
    }

    @Override
    public String forFewLines() {
        //Get the selected lines
        String[] lines = smallboxforfansub.scripting.ScriptPlugin.getSelectedOrgLines();

        // For each line of lines table.
        for(String line : lines){
            //Get the sentence for this line
            String sentence = smallboxforfansub.scripting.ScriptPlugin.getSentence(line);
            //Get the header for this line
            String head = smallboxforfansub.scripting.ScriptPlugin.getHead(line);
            //Try to transform the header with 'the moment'
            head = smallboxforfansub.scripting.ScriptPlugin.phBeforeAfter(head, myMoment, myMomentTime);
            //Try to change the number of the first layer
            head = smallboxforfansub.scripting.ScriptPlugin.phChangeLayer(head, myFirstLayer);
            //Get a table of syllabe parameters for the basic karaoke
            Object[][] osyl = smallboxforfansub.scripting.ScriptPlugin.phKaraoke(sentence);
            //Apply effects on sentence
            String newSentence = "";
            for(int j=0;j<myCommands.length;j++){
                for(int i=0;i<osyl.length;i++){

                    //Get commands (if i=0 then 1st overrides)
                    String c = i==0? myCommands[j][0] : myCommands[j][3];
                    
                    //Initialize variables for ruby scripting and execute the script to return a value
                    String new_expression = smallboxforfansub.karaoke.KaraokePanel.
                        phRubyCode(c, head, osyl, null, i, -1, myCode);
                    
                    //Do calcul with preset and syllabe parameters
                    new_expression = smallboxforfansub.scripting.ScriptPlugin.
                            phReplaceParameters(new_expression, osyl, i, head, null, -1);
                    //Add it to the new sentence
                    newSentence = newSentence + new_expression +
                            osyl[i][0].toString();
                }

                //Reformat assline
                line = smallboxforfansub.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
                //Return the modified line
                smallboxforfansub.scripting.ScriptPlugin.addResLine(line);
                //Reinit
                newSentence = "";
            }
        }

        return "forFewLines() :: Operation succeeded !";
    }

    @Override
    public String getPluginName() {
        return "LineSyllabeBasic";
    }

    @Override
    public String getDescription() {
        return "The way to do a real karaoke based on syllabe. " +
                "Return a single line.";
    }

    @Override
    public String getHelp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCommands(String commands) {
        //Format of commands :
        //§ = another preset
        //1st value = 1st overrides (fx inline mode and syllabe/line mode);
        //2nd value = main overrides (syllabe/line mode only);
        //3rd value = last overrides (syllabe/line mode only);
        //4th value = before syllabe (fx inline mode and syllabe/line mode);
        //5th value = after syllabe (syllabe/line mode only)
        
        //LineSyllableBasic works in fx inline mode (the normal mode)

        //Why use overrides ? It's more smoothly, you can set up your line's
        //default position or origin for example and save time operation.

        //How works the 1st overrides field ? Simply puts overrides and what you
        //want to do for the first syllabe of your sentence.
        //Otherwise 1st overrides = overrides + before syllabe (for 1st syllabe)
        //The before syllabe field starts at 2nd syllabe

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
    public String getDisplayName() {
        return "Simple line(s) - syllables";
    }

    @Override
    public String getAuthors() {
        return "Funsub Project/AssFxMaker owner";
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
        myStyles = styles;
    }

    @Override
    public void setRubyCode(String code) {
        myCode = code;
    }

}
