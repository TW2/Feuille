package feuille.scripting;

import feuille.io.Exchange;
import feuille.io.assa.*;
import org.graalvm.polyglot.HostAccess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Methods {

    private final Exchange exchange;
    private final int videoWidth = 1920;
    private final int videoHeight = 1080;
    private final AssStylesCollection ascScript;

    public Methods(Exchange exchange) {
        this.exchange = exchange;
        ascScript = exchange.getCurrentStylesCollection();
    }

    @HostAccess.Export
    public String getSentence(){
        return exchange.getEditorPanel().getCurrentText();
    }

    @HostAccess.Export
    public void setSentence(String sentence){
        exchange.getEditorPanel().setCurrentText(sentence);
    }

    //==========================================================================
    //****************************************************************** Methods
    //==========================================================================

    @HostAccess.Export
    public Color fromHTMLColor(String HTMLColor){
        // HTML -> RRGGBB
        if(HTMLColor.startsWith("#")){HTMLColor=HTMLColor.substring(1);}
        String red = HTMLColor.substring(0, 2);
        String green = HTMLColor.substring(2, 4);
        String blue = HTMLColor.substring(4);
        return new Color(
                Integer.parseInt(red, 16),
                Integer.parseInt(green, 16),
                Integer.parseInt(blue, 16));
    }

    //==========================================================================
    //**************************************************************** Functions
    //****************************************************** From KaraModeFunsub
    //==========================================================================

    /** <p>Return a multi-dimension array from a sentence<br />
     * Retourne un tableau multi-dimension depuis une phrase.<br /><br />
     * Sentence/Phrase :<br />"{\k31}ra{\k25}shi{\k15}n{\k14}ba{\k25}n {\k25}na{\k15}n
     * {\k140}te {\k41}jyu{\k20}ta{\k15}i {\k35}no {\k38}mo{\k55}to"<br /><br />
     * Array/Tableau :<br />ra  | shi | n   | ba  | n    | na   | n    | te   | jyu  |
     * ta   | i    | no   | mo   | to   | -> syllables/syllabes<br />
     * 31  | 25  | 15  | 14  | 25   | 25   | 15   | 140  | 41   | 20   | 15   |
     * 35   | 38   | 55   | -> hundredths/centièmes<br />
     * 310 | 250 | 150 | 140 | 250  | 250  | 150  | 1400 | 410  | 200  | 150  |
     * 350  | 380  | 550  | -> thousandths/millièmes<br />
     * 0   | 310 | 560 | 710 | 850  | 1100 | 1350 | 1500 | 2900 | 3310 | 3510 |
     * 3660 | 4010 | 4390 | -> thousandths start/millièmes début<br />
     * 310 | 560 | 710 | 850 | 1100 | 1350 | 1500 | 2900 | 3310 | 3510 | 3660 |
     * 4010 | 4390 | 4940 | -> thousandths end/millièmes fin</p>
     * <p>Original version @ KaraModeFunsub - 2006-2008</p>
     * @param sentence The sentence that contains the karaoke.
     * @return An array of useful data for XFX and ruby script. */
    @HostAccess.Export
    public Object[][] phKaraoke(String sentence){
        //{\k31}ra
        //(?<temps>\d+)\}(?<syllabe>[^\{]*) en vb.net
        //(\\d+)\\}([^\\{]*) en java
        Pattern p = Pattern.compile("(\\d+)}([^{]*)");
        Matcher m = p.matcher(sentence);

        List<String> syl = new ArrayList<String>();
        List<Integer> time = new ArrayList<Integer>();

        while(m.find()){
            time.add(Integer.parseInt(m.group(1)));
            syl.add(m.group(2));
        }

        Object[][] data = new Object[syl.size()][5];
        int iEnd = 0;

        for(int i=0;i<syl.size();i++){
            data[i][0] = syl.get(i);            //Syllabe
            data[i][1] = time.get(i);           //Centi. (total time)
            data[i][2] = time.get(i)*10;        //Milli. (total time)
            data[i][3] = iEnd;                  //Milli. (start time)
            iEnd += time.get(i)*10;
            data[i][4] = iEnd;                  //Milli. (end time)
        }

        return data;
    }

    /** <p>Get a new head with modified time if the moment isn't 'Meantime'.<br />
     * Obtient une nouvelle entête avec des temps modifiés si le moment
     * n'est pas 'Pendant'.</p>
     * @param head The actual head of the ass line.
     * @param moment The moment requested.
     * @param time The time for the new period (start -> end) of the head.
     * @return The new head of your ass line. You have to regroup the head and the sentence to have your new ass line.
     * @see getSentence() */
    @HostAccess.Export
    public String phBeforeAfter(String head, String moment, String time){
        if(!moment.equalsIgnoreCase("Meantime")){
            //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
            //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
            Pattern p = Pattern.compile("(\\d+:\\d+:\\d+.\\d+)");
            Matcher m = p.matcher(head);

            AssTime start = new AssTime();
            AssTime end = new AssTime();
            AssTime t = new AssTime(Integer.parseInt(time));
            AssTime new_start = new AssTime();
            AssTime new_end = new AssTime();

            boolean endTime = false;
            while(m.find()){
                if (!endTime){
                    start = AssTime.create(m.group(1));
                }else{
                    end = AssTime.create(m.group(1));
                }
                endTime = true;
            }

            if (moment.equalsIgnoreCase("Before")){
                new_start = AssTime.subtract(t, start);
                new_end = start;
            }else if (moment.equalsIgnoreCase("After")){
                new_start = end;
                new_end = AssTime.add(end, t);
            }

            String[] data = head.split(",");
            if(data.length==9){
                return data[0] + "," + new_start.toAss() + ","
                        + new_end.toAss() + "," + data[3] + "," + data[4] + ","
                        + data[5] + "," + data[6] + "," + data[7] + "," + data[8];
            }else{
                return data[0] + "," + new_start.toAss() + ","
                        + new_end.toAss() + "," + data[3] + "," + data[4] + ","
                        + data[5] + "," + data[6] + "," + data[7] + ",";
            }
        }else{
            return head;
        }
    }

    /** <p>Get a new head with modified time if the moment isn't 'Meantime'.<br />
     * Obtient une nouvelle entête avec des temps modifiés si le moment
     * n'est pas 'Pendant'.</p>
     * @param head The actual head of the ass line.
     * @param time The time for the new period (start -> end) of the head.
     * @return The new head of your ass line. You have to regroup the head and the sentence to have your new ass line.
     * @see getSentence()) */
    @HostAccess.Export
    public String phShift(String head, String time){
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+:\\d+:\\d+.\\d+)");
        Matcher m = p.matcher(head);

        AssTime start = new AssTime();
        AssTime end = new AssTime();
        AssTime t = new AssTime();

        t = new AssTime(Integer.parseInt(time));

        boolean endTime = false;
        while(m.find()){
            if (!endTime){
                start = AssTime.create(m.group(1));
            }else{
                end = AssTime.create(m.group(1));
            }
            endTime = true;
        }

        AssTime new_start = AssTime.add(t, start);
        AssTime new_end = AssTime.add(t, end);

        String[] data = head.split(",");
        if(data.length==9){
            return data[0] + "," + new_start.toAss() + ","
                    + new_end.toAss() + "," + data[3] + "," + data[4] + ","
                    + data[5] + "," + data[6] + "," + data[7] + "," + data[8];
        }else{
            return data[0] + "," + new_start.toAss() + ","
                    + new_end.toAss() + "," + data[3] + "," + data[4] + ","
                    + data[5] + "," + data[6] + "," + data[7] + ",";
        }
    }

    /** <p>Change the layer of the given head.<br />
     * Change la couche de l'entête.</p> */
    @HostAccess.Export
    public String phChangeLayer(String head, String firstLayer){
        String[] data = head.split(",");
        if(data.length==9){
            return "Dialogue: "+ firstLayer + "," + data[1] + ","
                    + data[2] + "," + data[3] + "," + data[4] + ","
                    + data[5] + "," + data[6] + "," + data[7] + "," + data[8];
        }else{
            return "Dialogue: "+ firstLayer + "," + data[1] + ","
                    + data[2] + "," + data[3] + "," + data[4] + ","
                    + data[5] + "," + data[6] + "," + data[7] + ",";
        }
    }

    /** <p>Change the style name of the given head.<br />
     * Change le nom du style de l'entête.</p> */
    @HostAccess.Export
    public String phChangeStyle(String head, String styleName){
        String[] data = head.split(",");
        if(data.length==9){
            return data[0] + "," + data[1] + "," + data[2] +
                    "," + styleName + "," + data[4] + "," + data[5] +
                    "," + data[6] + "," + data[7] + "," + data[8];
        }else{
            return data[0] + "," + data[1] + "," + data[2] +
                    "," + styleName + "," + data[4] + "," + data[5] +
                    "," + data[6] + "," + data[7] + ",";
        }
    }

    /** <p>Change the start time of the given head.<br />
     * Change le temps de début de l'entête.</p> */
    @HostAccess.Export
    public String phChangeStart(String head, String start){
        String[] data = head.split(",");
        try{
            AssTime t = new AssTime(Integer.parseInt(start));
            start = t.toAss();
        }catch(NumberFormatException _){
        }
        if(data.length==9){
            return data[0] + "," + start + ","
                    + data[2] + "," + data[3] + "," + data[4] + ","
                    + data[5] + "," + data[6] + "," + data[7] + "," + data[8];
        }else{
            return data[0] + "," + start + ","
                    + data[2] + "," + data[3] + "," + data[4] + ","
                    + data[5] + "," + data[6] + "," + data[7] + ",";
        }
    }

    /** <p>Change the end time of the given head.<br />
     * Change le temps de fin de l'entête.</p> */
    @HostAccess.Export
    public String phChangeEnd(String head, String end){
        String[] data = head.split(",");
        try{
            AssTime t = new AssTime(Integer.parseInt(end));
            end = t.toAss();
        }catch(NumberFormatException _){
        }
        if(data.length==9){
            return data[0] + "," + data[1] + ","
                    + end + "," + data[3] + "," + data[4] + ","
                    + data[5] + "," + data[6] + "," + data[7] + "," + data[8];
        }else{
            return data[0] + "," + data[1] + ","
                    + end + "," + data[3] + "," + data[4] + ","
                    + data[5] + "," + data[6] + "," + data[7] + ",";
        }
    }

    @HostAccess.Export
    public String phGetMillisecondsStart(String head){
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+:\\d+:\\d+.\\d+)");
        Matcher m = p.matcher(head);

        AssTime start = new AssTime();

        boolean endTime = false;
        while(m.find()){
            if (!endTime){
                start = AssTime.create(m.group(1));
            }
            endTime = true;
        }

        return Long.toString((long) start.getMsTime());
    }

    @HostAccess.Export
    public String phGetMillisecondsEnd(String head){
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+:\\d+:\\d+.\\d+)");
        Matcher m = p.matcher(head);

        AssTime end = new AssTime();

        boolean endTime = false;
        while(m.find()){
            if (endTime){
                end = AssTime.create(m.group(1));
            }
            endTime = true;
        }

        return Long.toString((long) end.getMsTime());
    }

    /** <p>Return a new sentence with karaoke per letter from a given sentence.<br />
     * Retourne une nouvelle phrase avec un karaoke par lettre à partir de la phrase fournie</p>
     * <p>Original version @ KaraModeFunsub - 2006-2008</p> */
    @HostAccess.Export
    public String phPerLetterKara(String sentence){
        StringBuilder new_sentence = new StringBuilder();
        int first, last, countLetters;

        //On récupère le tableau de syllabes afin de recréer la chaine et
        // de pouvoir calculer les durées de syllabes.
        //Calcul d'une syllabe : tps syl / nb lettres =(première lettre) |
        // tps syl - ((nb lettres - 1) * première lettre) =(dernière lettre)

        Object[][] syl = phKaraoke(sentence);

        for (Object[] objects : syl) {
            countLetters = getCountLetters(objects[0].toString());
            if (countLetters > 1) {
                //On traite les syllabes à plusieurs lettres (espace non compté)
                first = Integer.parseInt(objects[1].toString()) / countLetters;
                last = Integer.parseInt(objects[1].toString()) -
                        ((countLetters - 1) * first);

                for (int j = 0; j < countLetters; j++) {
                    if (j != countLetters - 1) {
                        new_sentence.append("{\\k").append(first).append("}").append((objects[0].toString()).charAt(j));
                    } else {
                        new_sentence.append("{\\k").append(last).append("}").append((objects[0].toString()).substring(j));
                    }
                }
            } else {
                new_sentence.append("{\\k").append(objects[1].toString()).append("}").append(objects[0].toString());
            }

        }
        return new_sentence.toString();
    }

    /** <p>Return the index of the origin syllables for each letter (to reuse
     * the output of phKaraoke).<br /> Retourne les index de la syllabe
     * d'origine pour chaque lettre (en vue de pouvoir réutiliser la sortie
     * de phKaraoke)<br /><br />Request/Demande : <br />"{\k31}ra{\k25}shi{\k15}n
     * {\k14}ba{\k25}n {\k25}na{\k15}n{\k140}te {\k41}jyu{\k20}ta{\k15}i
     * {\k35}no {\k38}mo{\k55}to"<br /><br />Return an indexes table, example
     * on rashinban/Retourne une table d'index, exemple sur rashinban :<br />
     * 0(r);0(a);1(s);1(h);1(i);2(n);3(b);3(a);4(n);</p>
     * <p>Original version @ KaraModeFunsub - 2006-2008</p> */
    @HostAccess.Export
    public int[] phPerLetterKaraCrossIndex(String sentence){
        Object[][] syl = phKaraoke(sentence);
        List<Integer> listCrossIndex = new ArrayList<Integer>();
        int countLetters;

        for (int i=0; i<syl.length;i++){
            countLetters = getCountLetters(syl[i][0].toString());
            for (int j=0;j<countLetters;j++){
                listCrossIndex.add(i);
            }
            if(syl[i][0].toString().isEmpty()){
                listCrossIndex.add(i);
            }
        }

        int[] data = new int[listCrossIndex.size()];
        for (int k=0;k<listCrossIndex.size();k++){
            data[k] = listCrossIndex.get(k);
        }
        return data;
    }

    /** <p>Get the count of letters in this syllables.<br />
     * Obtient le nombre de lettres dans cette syllabes.</p> */
    @HostAccess.Export
    public int getCountLetters(String syllabe){
        Pattern p = Pattern.compile("[^0-9a-zA-Z]*([0-9a-zA-Z]*)");
        Matcher m = p.matcher(syllabe);
        int out = 0;
        if(m.find()){
            out = m.group().length();
        }
        return out;
    }

    /** <p>Get the header of a ASS/SSA line.<br />
     * Obtient l'entête de cette ligne ASS/SSA.</p> */
    @HostAccess.Export
    public String getHead(String raw){
        Pattern p = Pattern.compile("([^:]+:\\s[a-zA-Z=]*\\d+," +
                "\\d+:\\d+:\\d+.\\d+," +
                "\\d+:\\d+:\\d+.\\d+," +
                "[^,]+,[^,]*," +
                "\\d+,\\d+,\\d+,[^,]*),(.*)");
        Matcher m = p.matcher(raw);
        String out = "";
        if(m.find()){
            out = m.group(1);
        }
        return out;
    }

    /** <p>Get the sentence of a ASS/SSA line.<br />
     * Obtient la phrase de cette ligne ASS/SSA.</p> */
    @HostAccess.Export
    public String getSentence(String raw){
        Pattern p = Pattern.compile("([^:]+:\\s[a-zA-Z=]*\\d+," +
                "\\d+:\\d+:\\d+.\\d+," +
                "\\d+:\\d+:\\d+.\\d+," +
                "[^,]+,[^,]*," +
                "\\d+,\\d+,\\d+,[^,]*),(.*)");
        Matcher m = p.matcher(raw);
        String out = "";
        if(m.find()){
            out = m.group(2);
        }
        return out;
    }

    /** <p>Get a complete ASS line from a head and a sentence.<br />
     * Obtient une ligne complète en regroupant entête et phrase.</p> */
    @HostAccess.Export
    public String getAssLineOf(String head, String sentence){
        return head + "," + sentence;
    }

    /** <p>Replace all parameters of sentence and do calculation.<br />
     * Remplace tous les paramètres de cette phrase et fait les calculs.</p>
     * @param expression The commands for this line (object of the plugin).
     * @param syl Syllabes or letters table (obtain it with phKaraoke() or
     * phPerLetterKara()).
     * @param currentIndex The actual index of syllabe in line.
     * @param head Head of line (obtain it with getHead()).
     * @param o_syl Syllabes table when letters mode is used
     * (obtain it with phKaraoke()).
     * @param currentCrossIndex The index of syllabes when letters mode is used
     * (obtain an integer by using phPerLetterKaraCrossIndex()).
     * @return A new sentence.
     * @see getSentence()  */
    @HostAccess.Export
    public String phReplaceParameters(String expression,
                                             Object[][] syl, int currentIndex, String head,
                                             Object[][] o_syl, int currentCrossIndex){
        //First step : change all parameters

        /* List of parameters :
         * %sK          start Karaoke       when syllabe must start highlight
         * %eK          end Karaoke         when syllabe must stop highlight
         * %dK          during Karaoke
         * %osK         start Karaoke       as original %sK (if per letter mode)
         * %oeK         end Karaoke         as original %eK (if per letter mode)
         * %odK         during Karaoke      as original %dK (if per letter mode)
         * %dP          during sentence
         * %posAF[]     randomized position
         * %num[]       randomized number
         */


        try{
            //%sK - start Karaoke
            expression = expression.replaceAll("%sK",
                    syl[currentIndex][3].toString());

            //%eK - end Karaoke
            expression = expression.replaceAll("%eK",
                    syl[currentIndex][4].toString());

            //%dK - during Karaoke
            expression = expression.replaceAll("%dK",
                    syl[currentIndex][2].toString());

            if(o_syl != null && currentCrossIndex != -1){
                //%osK - start Karaoke
                expression = expression.replaceAll("%osK",
                        o_syl[currentCrossIndex][3].toString());

                //%oeK - end Karaoke
                expression = expression.replaceAll("%oeK",
                        o_syl[currentCrossIndex][4].toString());

                //%odK - during Karaoke
                expression = expression.replaceAll("%odK",
                        o_syl[currentCrossIndex][2].toString());
            }

            if(head != null){
                Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
                Matcher m = p.matcher(head);

                AssTime start = new AssTime();
                AssTime end = new AssTime();

                boolean endTime = false;
                while(m.find()){
                    if (!endTime){
                        start = AssTime.create(m.group(1));
                    }else{
                        end = AssTime.create(m.group(1));
                    }
                    endTime = true;
                }

                end.substractToTime(start);

                //%dP - during sentence
                expression = expression.replaceAll("%dP",
                        Long.toString((long) end.getMsTime()));
            }

            //%posAF[] - randomized position
            while(expression.contains("%posAF[")){
                Pattern p = Pattern.compile("(\\d+),(\\d+),(\\d+),(\\d+)");
                Matcher m = p.matcher(expression);

                if(m.find()) {
                    int x1 = Integer.parseInt(m.group(1));
                    int y1 = Integer.parseInt(m.group(2));
                    int x2 = Integer.parseInt(m.group(3));
                    int y2 = Integer.parseInt(m.group(4));

                    double x = x1+Math.round((x2-x1)*Math.random());
                    double y = y1+Math.round((y2-y1)*Math.random());

                    int index0 = expression.indexOf("%posAF[");
                    int index1 = expression.indexOf("]",index0);

                    expression = expression.substring(0, index0) + x + "," + y +
                            expression.substring(index1+1);
                }
            }

            //%num[] - randomized number
            while(expression.contains("%num[")){
                Pattern p = Pattern.compile("(\\d+),(\\d+)");
                Matcher m = p.matcher(expression);

                if(m.find()){
                    int a1 = Integer.parseInt(m.group(1));
                    int a2 = Integer.parseInt(m.group(2));

                    double a = a1+Math.round((a2-a1)*Math.random());

                    int index0 = expression.indexOf("%num[");
                    int index1 = expression.indexOf("]",index0);

                    expression = expression.substring(0, index0) + a +
                            expression.substring(index1+1);
                }
            }

            //Second step : calculate everything but calcul with constant
            //`` (result in float)
            while(expression.contains("`")){
                Calcul calc = new Calcul();

                int index0 = expression.indexOf("`");
                int index1 = expression.indexOf("`",index0+1);

                String aCalcul = expression.substring(index0+1, index1);
                String aResult = calc.doCalcul(aCalcul,true);

                expression = expression.substring(0, index0) + aResult +
                        expression.substring(index1+1);
            }

            //~~ (result in integer)
            while(expression.contains("~")){
                Calcul calc = new Calcul();

                int index0 = expression.indexOf("~");
                int index1 = expression.indexOf("~",index0+1);

                String aCalcul = expression.substring(index0+1, index1);
                String aResult = calc.doCalcul(aCalcul,false);

                expression = expression.substring(0, index0) + aResult +
                        expression.substring(index1+1);
            }


            //Treatment of error \t(0,0,<effects>)
            while(expression.contains("t(0,0")){
                Pattern p = Pattern.compile("t\\(0,0,([^)]+)\\)");
                Matcher m = p.matcher(expression);

                if(m.find()){
                    int index0 = expression.indexOf("\\t(0,0");
                    int index1 = expression.indexOf(")",index0+1);

                    expression = expression.substring(0, index0) + m.group(1) +
                            expression.substring(index1+1);
                }
            }




        } catch(Exception _){

        }
        return expression;
    }

    /** <p>Return the moment of this XMLPreset (XFX) or 'undefined?'.
     * <br />Moment can be :<br />
     * <li><code>Before</code></li>
     * <li><code>Meantime</code></li>
     * <li><code>After</code></li><br /></p>
     * <p>Retourne le moment de ce XFX ou la chaine d'erreur 'undefined?'.
     * <br />Le moment peut être :<br />
     * <li><code>Avant</code></li>
     * <li><code>Pendant</code></li>
     * <li><code>Après</code></li></p> */
    @HostAccess.Export
    public String getFxoMoment(String preset){
//        for (FxObject fxo : (FxObject[])fxModel.toArray()){
        for (FxObject fxo : exchange.getFxObjects()){
            if(fxo.getName().equals(preset)){
                return fxo.getMoment();
            }
        }
        return "undefined?";
    }

    /** <p>Return the time of this XMLPreset (XFX) or 'undefined?'.<br />
     * Retourne le temps de ce XFX ou revoit la chaine d'erreur 'undefined?'.</p> */
    @HostAccess.Export
    public String getFxoTime(String preset){
        for (FxObject fxo : exchange.getFxObjects()){
            if(fxo.getName().equals(preset)){
                return fxo.getTime();
            }
        }
        return "undefined?";
    }

    /** <p>Return the first layer of this XMLPreset (XFX) or 'undefined?'.<br />
     * Retourne la première couche de ce XFX ou revoit la chaine d'erreur 'undefined?'.</p> */
    @HostAccess.Export
    public String getFxoFirstLayer(String preset){
        for (FxObject fxo : exchange.getFxObjects()){
            if(fxo.getName().equals(preset)){
                return fxo.getFirstLayer();
            }
        }
        return "undefined?";
    }

    /** <p>Return the number of layers of this XMLPreset (XFX) or 'undefined?'.<br />
     * Retourne le nombre de couches de ce XFX ou revoit la chaine d'erreur 'undefined?'.</p> */
    @HostAccess.Export
    public String getFxoNbLayers(String preset){
        for (FxObject fxo : exchange.getFxObjects()){
            if(fxo.getName().equals(preset)){
                return fxo.getNbLayers();
            }
        }
        return "undefined?";
    }

    /** <p>Return the style of this XMLPreset (XFX) or 'undefined?'.<br />
     * Retourne le style de ce XFX ou revoit la chaine d'erreur 'undefined?'.</p> */
    @HostAccess.Export
    public String getFxoStyle(String preset){
        for (FxObject fxo : exchange.getFxObjects()){
            if(fxo.getName().equals(preset)){
                return fxo.getStyle();
            }
        }
        return "undefined?";
    }

    /** <p>Return a table of abscissa :<br />
     * [i][0] : abscissa of x on the left of the syllable in course.<br />
     * [i][1] : abscissa of x on the right of the syllable in course.<br />
     * [i][2] : abscissa of x on the middle of the syllable in course.<br />
     * Attention : The text is in the left.<br /><br />
     * Retourne un tableau d'abscisse :<br />
     * [i][0] : abscisse de x sur la gauche de la syllabe en cours.<br />
     * [i][1] : abscisse de x sur la droite de la syllabe en cours.<br />
     * [i][2] : abscisse de x sur le milieu de la syllabe en cours.<br />
     * Attention : Le texte est à gauche.</p> */
    @HostAccess.Export
    public double[][] phXAbscissas(Font font, String sentence){
        //{\k31}ra
        //(?<temps>\d+)\}(?<syllabe>[^\{]*) en vb.net
        //(\\d+)\\}([^\\{]*) en java
        Pattern p = Pattern.compile("(\\d+)}([^{]*)");
        Matcher m = p.matcher(sentence);

        List<String> syl = new ArrayList<String>();

        while(m.find()){
            syl.add(m.group(2));
        }

        // fake frame but I need it to work with FontMetrics in static
        java.awt.Frame frm = new java.awt.Frame();
        FontMetrics fm=frm.getFontMetrics(font);
        double[][] xAbscisse = new double[syl.size()][3];
        double pX = 0;

        double coef = 1;

        for(int i=0;i<syl.size();i++){
            int width = (int)(fm.stringWidth(syl.get(i))*coef);
            xAbscisse[i][0] = pX; //x coté gauche
            xAbscisse[i][1] = width+pX;//x coté droit
            xAbscisse[i][2] = (double) width /2+pX;//x milieu
            System.out.println(">>> "+xAbscisse[i][0]+" "
                    +xAbscisse[i][1]+" "+xAbscisse[i][2]+" <<<");
            pX = xAbscisse[i][1];
        }

        return xAbscisse;
    }

    /** <p>Return a table of ordinate :<br />
     * [0] : ordinate of y on the bottom of the line.<br />
     * [1] : ordinate of y on the top of the line.<br />
     * [2] : ordinate of y on the middle of the line.<br />
     * Attention : The text is at top.<br /><br />
     * Retourne un tableau d'ordonné :<br />
     * [0] : ordonné de y sur le bas de la ligne.<br />
     * [1] : ordonné de y sur le haut de la ligne.<br />
     * [2] : ordonné de y sur le milieu de la ligne.<br />
     * Attention : Le texte est en haut.</p> */
    @HostAccess.Export
    public double[] phYOrdinates(Font font){

        // fake frame but I need it to work with FontMetrics in static
        java.awt.Frame frm = new java.awt.Frame();
        FontMetrics fm=frm.getFontMetrics(font);
        double[] yOrdinates = new double[3];

        yOrdinates[0] = fm.getHeight();     // bottom
        yOrdinates[1] = 0;                  // top
        yOrdinates[2] = (double) fm.getHeight() /2;   // middle

        return yOrdinates;
    }

    /** <p>Get the width of the video (defined in the options).<br />
     * Obtient la largeur de la vidéo (défini dans les options).</p> */
    @HostAccess.Export
    public int getVideoWidth(){
        return videoWidth;
    }

    /** <p>Get the height of the video (defined in the options).<br />
     * Obtient la hauteur de la vidéo (défini dans les options).</p> */
    @HostAccess.Export
    public int getVideoHeight(){
        return videoHeight;
    }

    /** <p>Get a stripped sentence from a karaoke sentence.<br />
     * Obtient une phrase sans temps de karaoke à partir d'une phrase avec des karaoke.</p> */
    @HostAccess.Export
    public String getStrippedSentence(String sentence){
        //{\k31}ra
        //(?<temps>\d+)\}(?<syllabe>[^\{]*) en vb.net
        //(\\d+)\\}([^\\{]*) en java
        Pattern p = Pattern.compile("\\d+}([^{]*)");
        Matcher m = p.matcher(sentence);

        StringBuilder newSentence = new StringBuilder();

        while(m.find()){
            newSentence.append(m.group(1));
        }
        return newSentence.toString();
    }

    /** <p>Get the width of the sentence.<br />
     * Obtient la largeur de la phrase.</p> */
    @HostAccess.Export
    public int getStringWidth(Font font, String sentence){
        // fake frame but I need it to work with FontMetrics in static
        java.awt.Frame frm = new java.awt.Frame();
        FontMetrics fm=frm.getFontMetrics(font);

        double coef = 1;

        return (int)(fm.stringWidth(getStrippedSentence(sentence))*coef);
    }

    /** <p>Get the position of x from the style.<br />
     * Obtient la position de x à partir du style.</p> */
    @HostAccess.Export
    public int getPosXFromStyle(AssStyle style, String sentence){
        int posX = -1;
        Font font = style.getAssFont().getFont();//Search for font
        int sentenceWidth = getStringWidth(font, sentence);
        switch(style.getAlignment().getNumber()){
            case 1, 4, 7 -> { return style.getMarginL(); }
            case 2, 5, 8 -> { return (videoWidth - sentenceWidth) / 2; }
            case 3, 6, 9 -> { return videoWidth - (sentenceWidth + style.getMarginR()); }
        }
        return posX; //error
    }

    /** <p>Get the position of y from the style.<br />
     * Obtient la position de y à partir du style.</p> */
    @HostAccess.Export
    public int getPosYFromStyle(AssStyle style){
        int posY = -1;
        switch(style.getAlignment().getNumber()){
            case 1, 2, 3 -> { return videoHeight-style.getMarginV(); }
            case 4, 5, 6 -> { return videoHeight/2; }
            case 7, 8, 9 -> { return style.getMarginV(); }
        }
        return posY; //error
    }

    /** <p>Replace all position parameters of sentence.<br />
     * Remplace tous les paramètres de position de la phrase.</p>
     * @param expression The sentence that contains parameters.
     * @param abscissas A table of abscissas (phXAbscissas).
     * @param ordinates A table of ordinates (phYOrdinates).
     * @param offsetX A distance between sentence and the border on X (getPosXFromStyle).
     * @param offsetY A distance between sentence and the border on Y (getPosYFromStyle).
     * @param currentIndex The index in course.
     * @return A sentence without parameters. */
    @HostAccess.Export
    public String phReplacePosParameters(String expression,
                                                double[][] abscissas, double[] ordinates, int offsetX, int offsetY,
                                                int currentIndex){

        /*
         * %posXn where n = 1,2,3,4,5,6,7,8,9 (like ASS alignment)
         * %posYn where n = 1,2,3,4,5,6,7,8,9 (like ASS alignment)
         */

        //%posX1,4,7 - left
        if(expression.contains("%posX1")
                | expression.contains("%posX4") | expression.contains("%posX7")){
            int posX = (int)abscissas[currentIndex][0]+offsetX;
            expression = expression.replaceAll("%posX1",Integer.toString(posX));
            expression = expression.replaceAll("%posX4",Integer.toString(posX));
            expression = expression.replaceAll("%posX7",Integer.toString(posX));
        }

        //%posX2,5,8 - middle
        if(expression.contains("%posX2")
                | expression.contains("%posX5") | expression.contains("%posX8")){
            int posX = (int)abscissas[currentIndex][2]+offsetX;
            expression = expression.replaceAll("%posX2",Integer.toString(posX));
            expression = expression.replaceAll("%posX5",Integer.toString(posX));
            expression = expression.replaceAll("%posX8",Integer.toString(posX));
        }

        //%posX3,6,9 - right
        if(expression.contains("%posX3")
                | expression.contains("%posX6") | expression.contains("%posX9")){
            int posX = (int)abscissas[currentIndex][1]+offsetX;
            expression = expression.replaceAll("%posX3",Integer.toString(posX));
            expression = expression.replaceAll("%posX6",Integer.toString(posX));
            expression = expression.replaceAll("%posX9",Integer.toString(posX));
        }

        //%posY1,2,3 - bottom
        if(expression.contains("%posY1")
                | expression.contains("%posY2") | expression.contains("%posY3")){
            int posY = videoHeight/2>offsetY? offsetY+(int)ordinates[0]:offsetY-(int)ordinates[0];
            expression = expression.replaceAll("%posY1",Integer.toString(posY));
            expression = expression.replaceAll("%posY2",Integer.toString(posY));
            expression = expression.replaceAll("%posY3",Integer.toString(posY));
        }

        //%posY4,5,6 - middle
        if(expression.contains("%posY4")
                | expression.contains("%posY5") | expression.contains("%posY6")){
            int posY = videoHeight/2>offsetY? offsetY+(int)ordinates[2]:offsetY-(int)ordinates[2];
            expression = expression.replaceAll("%posY4",Integer.toString(posY));
            expression = expression.replaceAll("%posY5",Integer.toString(posY));
            expression = expression.replaceAll("%posY6",Integer.toString(posY));
        }

        //%posY7,8,9 - top
        if(expression.contains("%posY7")
                | expression.contains("%posY8") | expression.contains("%posY9")){
            int posY = (int)ordinates[1]+offsetY;
            expression = expression.replaceAll("%posY7",Integer.toString(posY));
            expression = expression.replaceAll("%posY8",Integer.toString(posY));
            expression = expression.replaceAll("%posY9",Integer.toString(posY));
        }

        return expression;
    }

    /** <p>Get a color from the style.<br />
     * Obtient une couleur à partir du script.</p> */
    @HostAccess.Export
    public String[] getColorFromStyle(String style){
        try{
            String[] colors = new String[4];
            for (AssStyle myStyle : ascScript.getStyles()){
                if(myStyle.getName().equals(style)){
                    colors[0] = AssColor.withScheme(myStyle.getTextColor().getColor(), AssColorScheme.BGR);
                    colors[1] = AssColor.withScheme(myStyle.getKaraokeColor().getColor(), AssColorScheme.BGR);
                    colors[2] = AssColor.withScheme(myStyle.getOutlineColor().getColor(), AssColorScheme.BGR);
                    colors[3] = AssColor.withScheme(myStyle.getShadowColor().getColor(), AssColorScheme.BGR);
                }
            }
            return colors;
        }catch(Exception exc){
            return null;
        }
    }

    @HostAccess.Export
    public int[] phPosXSyllable(Object[][] kara, String ass_line, float pos_cor, int spa_cor, AssStyle astyle){
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();
        FontMetrics fm = gra.getFontMetrics(astyle.getAssFont().getFont());
        int[] position = new int[kara.length];
        float totalSize = 0;
        for(int i=0;i<kara.length;i++){
            String str = (String)kara[i][0];
            int sylSize = Math.round(fm.stringWidth(str)/pos_cor);
            position[i] = Math.round(totalSize+ (float) sylSize /2);
            if(str.contains(" ")){
                sylSize = sylSize + spa_cor;
            }
            totalSize += sylSize;
        }
        return position;
    }

    @HostAccess.Export
    public int[] phPosXSyllable(Object[][] kara, AssStyle astyle){
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();
        FontMetrics fm = gra.getFontMetrics(astyle.getAssFont().getFont());
        double coef = 1;
        int[] position = new int[kara.length];
        float totalSize = 0;
        for(int i=0;i<kara.length;i++){
            String str = (String)kara[i][0];
            int sylSize = (int)(fm.stringWidth(str)*coef);
            position[i] = Math.round(totalSize+ (float) sylSize /2);
            totalSize += sylSize;
        }
        return position;
    }

    @HostAccess.Export
    public int phXSentenceWidth(Object[][] kara, String ass_line, float pos_cor, int spa_cor, AssStyle astyle){
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();
        FontMetrics fm = gra.getFontMetrics(astyle.getAssFont().getFont());
        float totalSize = 0;
        for (Object[] objects : kara) {
            String str = (String) objects[0];
            int sylSize = Math.round(fm.stringWidth(str) / pos_cor);
            if (str.contains(" ")) {
                sylSize = sylSize + spa_cor;
            }
            totalSize += sylSize;
        }
        return Math.round(totalSize);
    }

    @HostAccess.Export
    public int phXSentenceWidth(Object[][] kara, AssStyle astyle){
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();
        FontMetrics fm = gra.getFontMetrics(astyle.getAssFont().getFont());
        double coef = 1;
        float totalSize = 0;
        for (Object[] objects : kara) {
            String str = (String) objects[0];
            int sylSize = (int) (fm.stringWidth(str) * coef);
            totalSize += sylSize;
        }
        return Math.round(totalSize);
    }

    @HostAccess.Export
    public int phXCharsWidth(String sentence,  AssStyle astyle){

        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();

        //Get the metrics for the used font
        FontMetrics fm = gra.getFontMetrics(astyle.getAssFont().getFont());

        int advance = gra.getFontMetrics().getMaxAdvance();
        int width = getMaxWidth(fm);

        //Calculation
        int emuSize = 0;
        for(int i=0;i<sentence.length();i++){
            char letter = sentence.charAt(i);
            int letterSize = fm.charWidth(letter);
            int letterSize2 = letterSize * advance / width - (advance*20/100);// + (advance*17/100)
            emuSize += letterSize2;
        }

        return emuSize;
    }

    @HostAccess.Export
    public int getMaxWidth(FontMetrics fm){
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int max = 0;
        for(int i=0;i<alpha.length();i++){
            char letter = alpha.charAt(i);
            int letterSize = fm.charWidth(letter);
            max = Math.max(letterSize, max);
        }
        return max;
    }

    @HostAccess.Export
    public AssStyle getAssStyle(String line){
        //Get AssStyle from ass line
        Pattern p = Pattern.compile("[^:]+:\\s[a-zA-Z=]*\\d+," +
                "\\d+:\\d+:\\d+.\\d+," +
                "\\d+:\\d+:\\d+.\\d+," +
                "([^,]+),[^,]*," +
                "\\d+,\\d+,\\d+,[^,]*,(.*)");
        Matcher m = p.matcher(line);

        if(m.find()){
            String style = m.group(1);
            for (AssStyle as : ascScript.getStyles()){
                if(as.getName().equals(style)){
                    return as;
                }
            }
        }

        return null;
    }

    private float getSentenceWidth(String sentence){
        return -1f;
    }
}
