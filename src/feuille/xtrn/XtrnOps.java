/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.xtrn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Yves
 */
public class XtrnOps {
    
    public XtrnOps(){
        
    }
    
    // <editor-fold defaultstate="collapsed" desc="Traitements par ligne">
    
    //Effect type : Normal
    //Mode type : Normal
    //Treatment type : Line
    public static List<String> getForNormalNormalLine(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);

        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);

        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);

        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);

        //Apply effects on sentence
        String newSentence = "";
        for(int j=0;j<myCommands.length;j++){
            for(int i=0;i<osyl.length;i++){

                //Get commands (if i=0 then 1st overrides)
                String c = i==0? myCommands[j][0] : myCommands[j][3];
                
                //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
                String sk = sylkeywords.get(i);
                c = applyLinePreset(c, linekeys);
                c = applySylPreset(c, sk, linekeys);

                //Do calcul with preset and syllabe parameters
                c = feuille.scripting.ScriptPlugin.
                        phReplaceParameters(c, osyl, i, head, null, -1);

                //Add it to the new sentence
                newSentence = newSentence + c + osyl[i][0].toString();
            }
            
            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);

            //Return the modified line
            values.add(line);

            //Reinit
            newSentence = "";
        }
        
        return values;
    }
    
    public static List<String> getForNormalNormalLine(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForNormalNormalLine(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Periodic
    //Mode type : Normal
    //Treatment type : Line
    public static List<String> getForPeriodicNormalLine(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);

        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);

        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);

        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        Integer oneLayer = 0;

        //Apply effects on sentence
        String newSentence = "";
        
        for(int i=0;i<osyl.length;i++){ // For each syllable (1 line)
            if(oneLayer>myCommands.length-1){
                oneLayer = 0;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = i==0? myCommands[oneLayer][0] : myCommands[oneLayer][3];

            //Initialize variables for ruby scripting and execute the script to return a value
//            String new_expression = feuille.karaoke.KaraokePanel.
//                    phRubyCode(c, head, osyl, null, i, -1, myCode);
            String sk = sylkeywords.get(i);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                        phReplaceParameters(c, osyl, i, head, null, -1);

            //Add it to the new sentence
            newSentence = newSentence + c + osyl[i][0].toString();

            oneLayer+=1;
        }
        
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);

        //Return the modified line
        values.add(line);
        
        return values;
    }
    
    public static List<String> getForPeriodicNormalLine(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForPeriodicNormalLine(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Random
    //Mode type : Normal
    //Treatment type : Line
    public static List<String> getForRandomNormalLine(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);

        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);

        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);

        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        Integer randLayer = 0; Integer lastRandom = 0;
        java.util.Random seed = new java.util.Random();

        //Apply effects on sentence
        String newSentence = "";
        
        for(int i=0;i<osyl.length;i++){ // For each syllable (1 line)
            if(myCommands.length>1){ //if more than one layer of effets                
                while(Objects.equals(randLayer, lastRandom)){
                    randLayer = seed.nextInt(myCommands.length);
                }
                lastRandom=randLayer;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = i==0? myCommands[randLayer][0] : myCommands[randLayer][3];

            //Initialize variables for ruby scripting and execute the script to return a value
//            String new_expression = feuille.karaoke.KaraokePanel.
//                    phRubyCode(c, head, osyl, null, i, -1, myCode);
            String sk = sylkeywords.get(i);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                        phReplaceParameters(c, osyl, i, head, null, -1);

            //Add it to the new sentence
            newSentence = newSentence + c + osyl[i][0].toString();
        }
        
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);

        //Return the modified line
        values.add(line);
        
        return values;
    }
    
    public static List<String> getForRandomNormalLine(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForRandomNormalLine(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Symmetric
    //Mode type : Normal
    //Treatment type : Line
    public static List<String> getForSymmetricNormalLine(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);

        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);

        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);

        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        final int countComsLine = myCommands.length;
        int currentComs = 0;

        //Apply effects on sentence
        String newSentence = "";
        
        for(int i=0;i<osyl.length;i++){ // For each syllable (1 line)
            if(osyl.length/2>=i){
                currentComs = i;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }else{
                currentComs = osyl.length - i - 1;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }

            //Get commands (if i=0 then 1st overrides)
            String c = i==0? myCommands[currentComs][0] : myCommands[currentComs][3];

            //Initialize variables for ruby scripting and execute the script to return a value
//            String new_expression = feuille.karaoke.KaraokePanel.
//                    phRubyCode(c, head, osyl, null, i, -1, myCode);
            String sk = sylkeywords.get(i);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                        phReplaceParameters(c, osyl, i, head, null, -1);

            //Add it to the new sentence
            newSentence = newSentence + c + osyl[i][0].toString();
        }
        
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);

        //Return the modified line
        values.add(line);
        
        return values;
    }
    
    public static List<String> getForSymmetricNormalLine(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForSymmetricNormalLine(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Normal
    //Mode type : Character
    //Treatment type : Line
    public static List<String> getForNormalCharacterLine(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
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
        for(int j=0;j<myCommands.length;j++){
            for(int i=0;i<syl.length;i++){

                //Get commands (if i=0 then 1st overrides)
                String c = i==0? myCommands[j][0] : myCommands[j][3];

                //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
                String sk = sylkeywords.get(crossIndex[i]);
                c = applyLinePreset(c, linekeys);
                c = applySylPreset(c, sk, linekeys);
                
                //Do calcul with preset and syllabe parameters
                c = feuille.scripting.ScriptPlugin.
                        phReplaceParameters(c, syl, i, head, osyl, crossIndex[i]);

                //Add it to the new sentence
                newSentence = newSentence + c + syl[i][0].toString();
            }

            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            //Return the modified line
            values.add(line);        
        }
        return values;
    }
    
    public static List<String> getForNormalCharacterLine(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForNormalCharacterLine(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Periodic
    //Mode type : Character
    //Treatment type : Line
    public static List<String> getForPeriodicCharacterLine(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
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
        //Count lines of the fxObject and return to line 0 as needed
        Integer oneLayer = 0;
        
        for(int i=0;i<syl.length;i++){
            if(oneLayer>myCommands.length-1){
                oneLayer = 0;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = i==0? myCommands[oneLayer][0] : myCommands[oneLayer][3];

            //Initialize variables for ruby scripting and execute the script to return a value
//            String new_expression = feuille.karaoke.KaraokePanel.
//                    phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
            String sk = sylkeywords.get(crossIndex[i]);
                c = applyLinePreset(c, linekeys);
                c = applySylPreset(c, sk, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, syl, i, head, osyl, crossIndex[i]);

            //Add it to the new sentence
            newSentence = newSentence + c + syl[i][0].toString();

            oneLayer+=1;
        }
        
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
        
        values.add(line);
        
        return values;
    }
    
    public static List<String> getForPeriodicCharacterLine(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForPeriodicCharacterLine(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Random
    //Mode type : Character
    //Treatment type : Line
    public static List<String> getForRandomCharacterLine(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
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
                while(Objects.equals(randLayer, lastRandom)){
                    randLayer = seed.nextInt(myCommands.length);
                }
                lastRandom=randLayer;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = i==0? myCommands[randLayer][0] : myCommands[randLayer][3];

            //Initialize variables for ruby scripting and execute the script to return a value
//            String new_expression = feuille.karaoke.KaraokePanel.
//                    phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
            String sk = sylkeywords.get(crossIndex[i]);
                c = applyLinePreset(c, linekeys);
                c = applySylPreset(c, sk, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, syl, i, head, osyl, crossIndex[i]);

            //Add it to the new sentence
            newSentence = newSentence + c + syl[i][0].toString();

        }
        
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
        
        values.add(line);
        
        return values;
    }
    
    public static List<String> getForRandomCharacterLine(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForRandomCharacterLine(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Symmetric
    //Mode type : Character
    //Treatment type : Line
    public static List<String> getForSymmetricCharacterLine(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]+);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
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
        final int countComsLine = myCommands.length;
        int currentComs = 0;
        for(int i=0;i<syl.length;i++){
            if(syl.length/2>=i){
                currentComs = i;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }else{
                currentComs = syl.length - i - 1;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }

            //Get commands (if i=0 then 1st overrides)
            String c = i==0? myCommands[currentComs][0] : myCommands[currentComs][3];

            //Initialize variables for ruby scripting and execute the script to return a value
//            String new_expression = feuille.karaoke.KaraokePanel.
//                    phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
            String sk = sylkeywords.get(crossIndex[i]);
                c = applyLinePreset(c, linekeys);
                c = applySylPreset(c, sk, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, syl, i, head, osyl, crossIndex[i]);

            //Add it to the new sentence
            newSentence = newSentence + c + syl[i][0].toString();

        }
        
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
        
        values.add(line);
        
        return values;
    }
    
    public static List<String> getForSymmetricCharacterLine(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForSymmetricCharacterLine(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Traitements par syllabe">
    
    //Effect type : Normal
    //Mode type : Normal
    //Treatment type : Syllable
    public static List<String> getForNormalNormalSyllable(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        for(int i=0;i<osyl.length;i++){
            //Get commands (if i=0 then 1st overrides)
            String c = myCommands[0][0];
                        
            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
            String sk = sylkeywords.get(i);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);

            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);

            //Add it to the new sentence
            String newSentence = c + osyl[i][0].toString();

            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            

            //Return the modified line
            values.add(line);            
        }
        return values;
    }
    
    public static List<String> getForNormalNormalSyllable(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForNormalNormalSyllable(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Periodic
    //Mode type : Normal
    //Treatment type : Syllable
    public static List<String> getForPeriodicNormalSyllable(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        Integer oneLayer = 0;
        
        for(int i=0;i<osyl.length;i++){
            if(oneLayer>myCommands.length-1){
                oneLayer = 0;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = myCommands[oneLayer][0];
                        
            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
            String sk = sylkeywords.get(i);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);

            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);

            //Add it to the new sentence
            String newSentence = c + osyl[i][0].toString();

            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            
            oneLayer+=1;

            //Return the modified line
            values.add(line);            
        }
        return values;
    }
    
    public static List<String> getForPeriodicNormalSyllable(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForPeriodicNormalSyllable(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Random
    //Mode type : Normal
    //Treatment type : Syllable
    public static List<String> getForRandomNormalSyllable(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        //For generation of numbers
        Integer randLayer = 0; Integer lastRandom = 0;
        java.util.Random seed = new java.util.Random();
        
        for(int i=0;i<osyl.length;i++){
            if(myCommands.length>1){ //if more than one layer of effets                
                while(Objects.equals(randLayer, lastRandom)){
                    randLayer = seed.nextInt(myCommands.length);
                }
                lastRandom=randLayer;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = myCommands[randLayer][0];
                        
            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
            String sk = sylkeywords.get(i);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);

            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);

            //Add it to the new sentence
            String newSentence = c + osyl[i][0].toString();

            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            

            //Return the modified line
            values.add(line);            
        }
        return values;
    }
    
    public static List<String> getForRandomNormalSyllable(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForRandomNormalSyllable(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Symmetric
    //Mode type : Normal
    //Treatment type : Syllable
    public static List<String> getForSymmetricNormalSyllable(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        //Get a table of syllabe parameters for the basic karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        final int countComsLine = myCommands.length;
        int currentComs;
        
        for(int i=0;i<osyl.length;i++){
            if(osyl.length/2>=i){
                currentComs = i;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }else{
                currentComs = osyl.length - i - 1;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }

            //Get commands (if i=0 then 1st overrides)
            String c = myCommands[currentComs][0];
            
            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
            String sk = sylkeywords.get(i);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);

            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);

            //Add it to the new sentence
            String newSentence = c + osyl[i][0].toString();

            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);

            //Return the modified line
            values.add(line);            
        }
        return values;
    }
    
    public static List<String> getForSymmetricNormalSyllable(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForSymmetricNormalSyllable(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Normal
    //Mode type : Character
    //Treatment type : Syllable
    public static List<String> getForNormalCharacterSyllable(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        int controlCrossIndex = 0, falseIndex = 0;
        String newSentence = "";
        
        for(int i=0;i<syl.length;i++){
            if(controlCrossIndex != crossIndex[i]){
                falseIndex = crossIndex[i];
            }
            //Get commands (if i=0 then 1st overrides)
            String c = crossIndex[i]==falseIndex ? myCommands[0][0] : myCommands[0][3];
            falseIndex += 1;

            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
            String sk = sylkeywords.get(crossIndex[i]);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);

            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);
            
            if(controlCrossIndex != crossIndex[i]){
                if(newSentence.isEmpty()==false){
                    line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
                    values.add(line); 
                    newSentence = "";
                }
                controlCrossIndex = crossIndex[i];
            }
            
            //Add it to the new sentence
            newSentence = newSentence + c + syl[i][0].toString();            
        }
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
        
        //Return the modified line
        values.add(line);     
        
        return values;
    }
    
    public static List<String> getForNormalCharacterSyllable(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForNormalCharacterSyllable(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Periodic
    //Mode type : Character
    //Treatment type : Syllable
    public static List<String> getForPeriodicCharacterSyllable(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        Integer oneLayer = 0;
        int controlCrossIndex = 0, falseIndex = 0;
        String newSentence = "";
        
        for(int i=0;i<syl.length;i++){
            if(oneLayer>myCommands.length-1){
                oneLayer = 0;
            }
            
            if(controlCrossIndex != crossIndex[i]){
                falseIndex = crossIndex[i];
            }
            //Get commands (if i=0 then 1st overrides)
            String c = crossIndex[i]==falseIndex ? myCommands[oneLayer][0] : myCommands[oneLayer][3];
            falseIndex += 1;

            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
            String sk = sylkeywords.get(crossIndex[i]);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);

            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);
            
            if(controlCrossIndex != crossIndex[i]){
                if(newSentence.isEmpty()==false){
                    line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
                    values.add(line); 
                    newSentence = "";
                }
                controlCrossIndex = crossIndex[i];
            }
            
            //Add it to the new sentence
            newSentence = newSentence + c + syl[i][0].toString();  
            
            oneLayer += 1;
        }
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
        
        //Return the modified line
        values.add(line);     
        
        return values;
    }
    
    public static List<String> getForPeriodicCharacterSyllable(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForPeriodicCharacterSyllable(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Random
    //Mode type : Character
    //Treatment type : Syllable
    public static List<String> getForRandomCharacterSyllable(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        int controlCrossIndex = 0, falseIndex = 0;
        String newSentence = "";
        
        //For generation of numbers
        Integer randLayer = 0; Integer lastRandom = 0;
        java.util.Random seed = new java.util.Random();
        
        for(int i=0;i<syl.length;i++){
            if(myCommands.length>1){ //if more than one layer of effets                
                while(Objects.equals(randLayer, lastRandom)){
                    randLayer = seed.nextInt(myCommands.length);
                }
                lastRandom=randLayer;
            }
            
            if(controlCrossIndex != crossIndex[i]){
                falseIndex = crossIndex[i];
            }
            //Get commands (if i=0 then 1st overrides)
            String c = crossIndex[i]==falseIndex ? myCommands[randLayer][0] : myCommands[randLayer][3];
            falseIndex += 1;

            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
            String sk = sylkeywords.get(crossIndex[i]);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);

            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);
            
            if(controlCrossIndex != crossIndex[i]){
                if(newSentence.isEmpty()==false){
                    line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
                    values.add(line); 
                    newSentence = "";
                }
                controlCrossIndex = crossIndex[i];
            }
            
            //Add it to the new sentence
            newSentence = newSentence + c + syl[i][0].toString();            
        }
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
        
        //Return the modified line
        values.add(line);     
        
        return values;
    }
    
    public static List<String> getForRandomCharacterSyllable(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForRandomCharacterSyllable(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    //Effect type : Symmetric
    //Mode type : Character
    //Treatment type : Syllable
    public static List<String> getForSymmetricCharacterSyllable(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        final int countComsLine = myCommands.length;
        int currentComs;
        int controlCrossIndex = 0, falseIndex = 0;
        String newSentence = "";
        
        for(int i=0;i<syl.length;i++){
            if(syl.length/2>=i){
                currentComs = i;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }else{
                currentComs = syl.length - i - 1;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }
            
            if(controlCrossIndex != crossIndex[i]){
                falseIndex = crossIndex[i];
            }
            //Get commands (if i=0 then 1st overrides)
            String c = crossIndex[i]==falseIndex ? myCommands[currentComs][0] : myCommands[currentComs][3];
            falseIndex += 1;

            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, syl, osyl, i, crossIndex[i], myCode);
            String sk = sylkeywords.get(crossIndex[i]);
            c = applyLinePreset(c, linekeys);
            c = applySylPreset(c, sk, linekeys);

            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);
            
            if(controlCrossIndex != crossIndex[i]){
                if(newSentence.isEmpty()==false){
                    line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
                    values.add(line); 
                    newSentence = "";
                }
                controlCrossIndex = crossIndex[i];
            }
            
            //Add it to the new sentence
            newSentence = newSentence + c + syl[i][0].toString();            
        }
        //Reformat assline
        line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
        
        //Return the modified line
        values.add(line);     
        
        return values;
    }
    
    public static List<String> getForSymmetricCharacterSyllable(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine){
        return getForSymmetricCharacterSyllable(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine);
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Traitements par caractère">
    
    //Effect type : Normal
    //Mode type : Normal, Character
    //Treatment type : Character
    public static List<String> getForNormalNormalCharacter(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords, List<String> charkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        for(int i=0;i<syl.length;i++){            
            
            //Get commands (if i=0 then 1st overrides)
            String c = myCommands[0][0];
            
            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
            String sk = sylkeywords.get(crossIndex[i]);
            String ck = charkeywords.get(i);
            c = applyCharPreset(c, ck, sk, linekeys);
            c = applySylPreset(c, sk, linekeys);
            c = applyLinePreset(c, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);
            
            //Add it to the new sentence
            String newSentence = c + syl[i][0].toString();
            
            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            
            //Return the modified line
            values.add(line);
        }
        
        return values;
    }
    
    public static List<String> getForNormalNormalCharacter(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine, List<String> charKeywordsByLine){
        return getForNormalNormalCharacter(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine, charKeywordsByLine);
    }
    
    //Effect type : Periodic
    //Mode type : Normal, Character
    //Treatment type : Character
    public static List<String> getForPeriodicNormalCharacter(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords, List<String> charkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        Integer oneLayer = 0;
        
        for(int i=0;i<syl.length;i++){            
            if(oneLayer>myCommands.length-1){
                oneLayer = 0;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = myCommands[oneLayer][0];
            
            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
            String sk = sylkeywords.get(crossIndex[i]);
            String ck = charkeywords.get(i);
            c = applyCharPreset(c, ck, sk, linekeys);
            c = applySylPreset(c, sk, linekeys);
            c = applyLinePreset(c, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);
            
            //Add it to the new sentence
            String newSentence = c + syl[i][0].toString();
            
            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            
            //Return the modified line
            values.add(line);
            
            oneLayer += 1;
        }
        
        return values;
    }
    
    public static List<String> getForPeriodicNormalCharacter(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine, List<String> charKeywordsByLine){
        return getForPeriodicNormalCharacter(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine, charKeywordsByLine);
    }
    
    //Effect type : Random
    //Mode type : Normal, Character
    //Treatment type : Character
    public static List<String> getForRandomNormalCharacter(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords, List<String> charkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        //For generation of numbers
        Integer randLayer = 0; Integer lastRandom = 0;
        java.util.Random seed = new java.util.Random();
        
        for(int i=0;i<syl.length;i++){            
            if(myCommands.length>1){ //if more than one layer of effets                
                while(Objects.equals(randLayer, lastRandom)){
                    randLayer = seed.nextInt(myCommands.length);
                }
                lastRandom=randLayer;
            }

            //Get commands (if i=0 then 1st overrides)
            String c = myCommands[randLayer][0];
            
            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
            String sk = sylkeywords.get(crossIndex[i]);
            String ck = charkeywords.get(i);
            c = applyCharPreset(c, ck, sk, linekeys);
            c = applySylPreset(c, sk, linekeys);
            c = applyLinePreset(c, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);
            
            //Add it to the new sentence
            String newSentence = c + syl[i][0].toString();
            
            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            
            //Return the modified line
            values.add(line);
        }
        
        return values;
    }
    
    public static List<String> getForRandomNormalCharacter(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine, List<String> charKeywordsByLine){
        return getForRandomNormalCharacter(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine, charKeywordsByLine);
    }
    
    //Effect type : Symmetric
    //Mode type : Normal, Character
    //Treatment type : Character
    public static List<String> getForSymmetricNormalCharacter(String line, String commands,
            String moment, String time, String layer, String code, 
            String linekeys, List<String> sylkeywords, List<String> charkeywords){
        //Commands :
        //Searching
        java.util.regex.Pattern p = java.util.regex.Pattern.
                compile("§([^;]+);([^;]*);([^;]*);([^;]*);([^§]*)");
        java.util.regex.Matcher m = p.matcher(commands);

        //Reset
        String[][] myCommands = new String[commands.split("§").length-1][5];

        //Storing
        int k = 0;
        while(m.find()){
            myCommands[k][0] = m.group(1); //1st overrides (required)
            myCommands[k][1] = m.group(2); //main overrides (useless)
            myCommands[k][2] = m.group(3); //last overrides (useless)
            myCommands[k][3] = m.group(4); //before syllabe (required)
            myCommands[k][4] = m.group(5); //after syllabe (useless)
            k+=1;
        }
        
        List<String> values = new ArrayList<>();
        
        //Get the sentence for this line
        String sentence = feuille.scripting.ScriptPlugin.getSentence(line);        
        //Get the header for this line
        String head = feuille.scripting.ScriptPlugin.getHead(line);
        //Try to transform the header with 'the moment'
        head = feuille.scripting.ScriptPlugin.phBeforeAfter(head, moment, time);
        //Try to change the number of the first layer
        head = feuille.scripting.ScriptPlugin.phChangeLayer(head, layer);
        // Get a karaoke of letters
        String letters = feuille.scripting.ScriptPlugin.phPerLetterKara(sentence);
        // Table for letters :
        Object[][] syl = feuille.scripting.ScriptPlugin.phKaraoke(letters);
        // Get original index for karaoke by syllable
        int[] crossIndex = feuille.scripting.ScriptPlugin.phPerLetterKaraCrossIndex(sentence);
        // The original karaoke
        Object[][] osyl = feuille.scripting.ScriptPlugin.phKaraoke(sentence);
        
        final int countComsLine = myCommands.length;
        int currentComs;
        
        for(int i=0;i<syl.length;i++){            
            if(syl.length/2>=i){
                currentComs = i;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }else{
                currentComs = syl.length - i - 1;
                if (currentComs >= countComsLine){currentComs=countComsLine-1;}
            }

            //Get commands (if i=0 then 1st overrides)
            String c = myCommands[currentComs][0];
            
            //Initialize variables for ruby scripting and execute the script to return a value
//                String new_expression = feuille.karaoke.KaraokePanel.
//                        phRubyCode(c, head, osyl, null, i, -1, code);
            String sk = sylkeywords.get(crossIndex[i]);
            String ck = charkeywords.get(i);
            c = applyCharPreset(c, ck, sk, linekeys);
            c = applySylPreset(c, sk, linekeys);
            c = applyLinePreset(c, linekeys);
            
            //Do calcul with preset and syllabe parameters
            c = feuille.scripting.ScriptPlugin.
                    phReplaceParameters(c, osyl, i, head, null, -1);
            
            //Add it to the new sentence
            String newSentence = c + syl[i][0].toString();
            
            //Reformat assline
            line = feuille.scripting.ScriptPlugin.getAssLineOf(head, newSentence);
            
            //Return the modified line
            values.add(line);
        }
        
        return values;
    }
    
    public static List<String> getForSymmetricNormalCharacter(String line, String commands, 
            String linekeys, List<String> sylkeywordsbyLine, List<String> charKeywordsByLine){
        return getForSymmetricNormalCharacter(line, commands, "Meantime", "0", "0", "", 
            linekeys, sylkeywordsbyLine, charKeywordsByLine);
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Remplacement des paramètres">
    
    private static String applyLinePreset(String sentence, String keywords){
        String[] table = keywords.split("\\|");
        //00 = keyz:write(line.text_stripped)
        sentence = sentence.replaceAll("\\$ldur", table[1]);//01 = keyz:write("|" .. line.duration)
        //00 = --keyz:write("|" .. line.kara)
        //00 = --keyz:write("|" .. line.furi)
        //00 = --keyz:write("|" .. line.styleref)
        //00 = --keyz:write("|" .. line.furistyle)
        sentence = sentence.replaceAll("\\$lwidth", table[2]);//02 = keyz:write("|" .. line.width)
        sentence = sentence.replaceAll("\\$lheight", table[3]);//03 = keyz:write("|" .. line.height)
        //04 = keyz:write("|" .. line.descent)
        //05 = keyz:write("|" .. line.extlead)
        //06 = keyz:write("|" .. line.margin_v)
        sentence = sentence.replaceAll("\\$margin_l", table[7]);//07 = keyz:write("|" .. line.eff_margin_l)
        sentence = sentence.replaceAll("\\$margin_r", table[8]);//08 = keyz:write("|" .. line.eff_margin_r)
        sentence = sentence.replaceAll("\\$margin_t", table[9]);//09 = keyz:write("|" .. line.eff_margin_t)
        sentence = sentence.replaceAll("\\$margin_b", table[10]);//10 = keyz:write("|" .. line.eff_margin_b)
        sentence = sentence.replaceAll("\\$margin_v", table[11]);//11 = keyz:write("|" .. line.eff_margin_v)
        //12 = keyz:write("|" .. line.halign)
        //13 = keyz:write("|" .. line.valign)
        sentence = sentence.replaceAll("\\$lleft", table[14]);//14 = keyz:write("|" .. line.left)
        sentence = sentence.replaceAll("\\$lcenter", table[15]);//15 = keyz:write("|" .. line.center)
        sentence = sentence.replaceAll("\\$lright", table[16]);//16 = keyz:write("|" .. line.right)
        sentence = sentence.replaceAll("\\$ltop", table[17]);//17 = keyz:write("|" .. line.top)
        sentence = sentence.replaceAll("\\$lmiddle", table[18]);//18 = keyz:write("|" .. line.middle)
        //19 = keyz:write("|" .. line.vcenter)
        sentence = sentence.replaceAll("\\$lbottom", table[20]);//20 = keyz:write("|" .. line.bottom)
        sentence = sentence.replaceAll("\\$lx", table[21]);//21 = keyz:write("|" .. line.x)
        sentence = sentence.replaceAll("\\$ly", table[22]);//22 = keyz:write("|" .. line.y)
        //23 = keyz:write("|" .. line.class)
        //24 = keyz:write("|" .. line.raw)
        //25 = keyz:write("|" .. line.section)
        //00 = --keyz:write("|" .. line.comment)
        sentence = sentence.replaceAll("\\$layer", table[26]);//26 = keyz:write("|" .. line.layer)
        sentence = sentence.replaceAll("\\$lstart", table[27]);//27 = keyz:write("|" .. line.start_time)
        sentence = sentence.replaceAll("\\$lend", table[28]);//28 = keyz:write("|" .. line.end_time)
        sentence = sentence.replaceAll("\\$style", table[29]);//29 = keyz:write("|" .. line.style)
        sentence = sentence.replaceAll("\\$actor", table[30]);//30 = keyz:write("|" .. line.actor)
        //31 = keyz:write("|" .. line.margin_l)
        //32 = keyz:write("|" .. line.margin_r)
        //33 = keyz:write("|" .. line.margin_t)
        //34 = keyz:write("|" .. line.margin_b)
        //35 = keyz:write("|" .. line.effect)
        //00 = --keyz:write("|" .. line.userdata)
        //36 = keyz:write("|" .. line.text)
        int mid = Integer.parseInt(table[27])+Integer.parseInt(table[1])/2;
        sentence = sentence.replaceAll("\\$lmid", Integer.toString(mid));
        return sentence;
    }
    
    private static String applySylPreset(String sentence, String syl_keywords, String line_keywords){
        String[] syl_table = syl_keywords.split("\\|");
        String[] line_table = line_keywords.split("\\|");
        sentence = sentence.replaceAll("\\$sdur", syl_table[0]);//00 = sylkeyz:write(syl.duration)
        sentence = sentence.replaceAll("\\$sstart", syl_table[1]);//01 = sylkeyz:write("|" .. syl.start_time)
        sentence = sentence.replaceAll("\\$send", syl_table[2]);//02 = sylkeyz:write("|" .. syl.end_time)
        //03 = sylkeyz:write("|" .. syl.tag)
        //04 = sylkeyz:write("|" .. syl.text)
        //05 = sylkeyz:write("|" .. syl.text_stripped)
        sentence = sentence.replaceAll("\\$skdur", syl_table[6]);//06 = sylkeyz:write("|" .. syl.kdur)
        //00 = --sylkeyz:write("|" .. syl.line)
        //07 = sylkeyz:write("|" .. syl.inline_fx)
        sentence = sentence.replaceAll("\\$si", syl_table[8]);//08 = sylkeyz:write("|" .. syl.i)
        //09 = sylkeyz:write("|" .. syl.prespace)
        //10 = sylkeyz:write("|" .. syl.postspace)
        //11 = sylkeyz:write("|" .. syl.text_spacestripped)
        //00 = --sylkeyz:write("|" .. syl.style)
        sentence = sentence.replaceAll("\\$swidth", syl_table[12]);//12 = sylkeyz:write("|" .. syl.width)
        sentence = sentence.replaceAll("\\$sheight", syl_table[13]);//13 = sylkeyz:write("|" .. syl.height)
        //14 = sylkeyz:write("|" .. syl.prespacewidth)
        //15 = sylkeyz:write("|" .. syl.postspacewidth)
        sentence = sentence.replaceAll("\\$sleft", syl_table[16]);//16 = sylkeyz:write("|" .. syl.left)
        sentence = sentence.replaceAll("\\$scenter", syl_table[17]);//17 = sylkeyz:write("|" .. syl.center)
        sentence = sentence.replaceAll("\\$sright", syl_table[18]);//18 = sylkeyz:write("|" .. syl.right)
        int mid = Integer.parseInt(syl_table[1])+Integer.parseInt(syl_table[0])/2;
        sentence = sentence.replaceAll("\\$smid", Integer.toString(mid));
        sentence = sentence.replaceAll("\\$sbottom", line_table[20]);
        sentence = sentence.replaceAll("\\$smiddle", line_table[18]);
        sentence = sentence.replaceAll("\\$stop", line_table[17]);
        int alphanumeric = Integer.parseInt(syl_table[19]);
        String x = "0", y = "0";
        if(alphanumeric == 1){
            x = Float.toString(Float.parseFloat(syl_table[16])+Float.parseFloat(line_table[14]));  //syl.left+line.left
            y = line_table[20]; //bottom
        }else if(alphanumeric == 2){
            x = Float.toString(Float.parseFloat(syl_table[17])+Float.parseFloat(line_table[14]));  //syl.center+line.left
            y = line_table[20]; //bottom
        }else if(alphanumeric == 3){
            x = Float.toString(Float.parseFloat(syl_table[18])+Float.parseFloat(line_table[14]));  //syl.right+line.left
            y = line_table[20]; //bottom
        }else if(alphanumeric == 4){
            x = Float.toString(Float.parseFloat(syl_table[16])+Float.parseFloat(line_table[14]));  //syl.left+line.left
            y = line_table[18]; //middle
        }else if(alphanumeric == 5){
            x = Float.toString(Float.parseFloat(syl_table[17])+Float.parseFloat(line_table[14]));  //syl.center+line.left
            y = line_table[18]; //middle
        }else if(alphanumeric == 6){
            x = Float.toString(Float.parseFloat(syl_table[18])+Float.parseFloat(line_table[14]));  //syl.right+line.left
            y = line_table[18]; //middle
        }else if(alphanumeric == 7){
            x = Float.toString(Float.parseFloat(syl_table[16])+Float.parseFloat(line_table[14]));  //syl.left+line.left
            y = line_table[17]; //top
        }else if(alphanumeric == 8){
            x = Float.toString(Float.parseFloat(syl_table[17])+Float.parseFloat(line_table[14]));  //syl.center+line.left
            y = line_table[17]; //top
        }else if(alphanumeric == 9){
            x = Float.toString(Float.parseFloat(syl_table[18])+Float.parseFloat(line_table[14]));  //syl.right+line.left
            y = line_table[17]; //top
        }
        sentence = sentence.replaceAll("\\$sx", x);
        sentence = sentence.replaceAll("\\$sy", y);
        return sentence;
    }
    
    private static String applyCharPreset(String sentence, String char_keywords, String syl_keywords, String line_keywords){
        String[] char_table = char_keywords.split("\\|");
        String[] syl_table = syl_keywords.split("\\|");
        String[] line_table = line_keywords.split("\\|");
        sentence = sentence.replaceAll("\\$sdur", syl_table[0]);//00 = sylkeyz:write(syl.duration)
        sentence = sentence.replaceAll("\\$sstart", syl_table[1]);//01 = sylkeyz:write("|" .. syl.start_time)
        sentence = sentence.replaceAll("\\$send", syl_table[2]);//02 = sylkeyz:write("|" .. syl.end_time)
        //03 = sylkeyz:write("|" .. syl.tag)
        //04 = sylkeyz:write("|" .. syl.text)
        //05 = sylkeyz:write("|" .. syl.text_stripped)
        sentence = sentence.replaceAll("\\$skdur", syl_table[6]);//06 = sylkeyz:write("|" .. syl.kdur)
        //00 = --sylkeyz:write("|" .. syl.line)
        //07 = sylkeyz:write("|" .. syl.inline_fx)
        sentence = sentence.replaceAll("\\$si", syl_table[8]);//08 = sylkeyz:write("|" .. syl.i)
        //09 = sylkeyz:write("|" .. syl.prespace)
        //10 = sylkeyz:write("|" .. syl.postspace)
        //11 = sylkeyz:write("|" .. syl.text_spacestripped)
        //00 = --sylkeyz:write("|" .. syl.style)
        sentence = sentence.replaceAll("\\$swidth", syl_table[12]);//12 = sylkeyz:write("|" .. syl.width)
        sentence = sentence.replaceAll("\\$sheight", syl_table[13]);//13 = sylkeyz:write("|" .. syl.height)
        //14 = sylkeyz:write("|" .. syl.prespacewidth)
        //15 = sylkeyz:write("|" .. syl.postspacewidth)
        sentence = sentence.replaceAll("\\$sleft", syl_table[16]);//16 = sylkeyz:write("|" .. syl.left)
        sentence = sentence.replaceAll("\\$scenter", syl_table[17]);//17 = sylkeyz:write("|" .. syl.center)
        sentence = sentence.replaceAll("\\$sright", syl_table[18]);//18 = sylkeyz:write("|" .. syl.right)
        int mid = Integer.parseInt(syl_table[1])+Integer.parseInt(syl_table[0])/2;
        sentence = sentence.replaceAll("\\$smid", Integer.toString(mid));
        sentence = sentence.replaceAll("\\$sbottom", line_table[20]);
        sentence = sentence.replaceAll("\\$smiddle", line_table[18]);
        sentence = sentence.replaceAll("\\$stop", line_table[17]);
        int alphanumeric = Integer.parseInt(syl_table[19]);
        String x = "0", y = "0";
        if(alphanumeric == 1){
            x = char_table[0];  //syl.left+line.left
            y = line_table[20]; //bottom
        }else if(alphanumeric == 2){
            x = char_table[1];  //syl.center+line.left
            y = line_table[20]; //bottom
        }else if(alphanumeric == 3){
            x = char_table[2];  //syl.right+line.left
            y = line_table[20]; //bottom
        }else if(alphanumeric == 4){
            x = char_table[0];  //syl.left+line.left
            y = line_table[18]; //middle
        }else if(alphanumeric == 5){
            x = char_table[1];  //syl.center+line.left
            y = line_table[18]; //middle
        }else if(alphanumeric == 6){
            x = char_table[2];  //syl.right+line.left
            y = line_table[18]; //middle
        }else if(alphanumeric == 7){
            x = char_table[0];  //syl.left+line.left
            y = line_table[17]; //top
        }else if(alphanumeric == 8){
            x = char_table[1];  //syl.center+line.left
            y = line_table[17]; //top
        }else if(alphanumeric == 9){
            x = char_table[2];  //syl.right+line.left
            y = line_table[17]; //top
        }
        sentence = sentence.replaceAll("\\$sx", x);
        sentence = sentence.replaceAll("\\$sy", y);
        return sentence;
    }
    
    // </editor-fold>
    
}
