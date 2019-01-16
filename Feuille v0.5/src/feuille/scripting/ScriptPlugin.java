/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.scripting;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;
import feuille.drawing.lib.BSpline;
import feuille.drawing.lib.Bezier;
import feuille.drawing.lib.ControlPoint;
import feuille.drawing.lib.IShape;
import feuille.drawing.lib.Layer;
import feuille.drawing.lib.Line;
import feuille.drawing.lib.Move;
import feuille.drawing.lib.Point;
import feuille.drawing.lib.ReStart;
import feuille.drawing.lib.Sheet;
import feuille.karaoke.lib.AssIO;
import feuille.karaoke.lib.AssStyle;
import feuille.karaoke.lib.AssStyle.AssStyleType;
import feuille.karaoke.lib.AssStyleCollection;
import feuille.karaoke.lib.Calcul;
import feuille.karaoke.lib.FxObject;
import feuille.karaoke.lib.ProblemFont;
import feuille.karaoke.lib.ProgramLine;
import feuille.karaoke.lib.Time;
import feuille.theme.Theme;
import feuille.theme.ThemeCollection;

/**
 *
 * @author The Wingate 2940
 */
public class ScriptPlugin {
    
    private static String actualPath = null;
    private static List<Object> sobjectList = new ArrayList<Object>();
    JRubyScript ruby;
    JythonScript python;
    
    static String fxScripts = "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\";
    static String script = "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\sample.rb";
    static String docsPath = "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\";
    static ProblemFont pf = new ProblemFont();
    static int videoHeight = 720, videoWidth = 1280;
    static DefaultTableModel orgModel, resModel;
    static AssStyleCollection ascScript;
    
    Frame frame; //We have to display dialog on program (error message)
    
    public ScriptPlugin(Frame frame){
        this.frame = frame;
        ruby = new JRubyScript(frame);
        python = new JythonScript(frame);
    }
    
    public ScriptPlugin(Frame frame, String fxPath, String scriptPath, String docsPath){
        this.frame = frame;
        fxScripts = fxPath;
        script = scriptPath;
        ScriptPlugin.docsPath = docsPath;
        ruby = new JRubyScript(frame);
        python = new JythonScript(frame);
    }
    
    /** <p>Search for all script of this directory.<br />
     * Recherche tous les scripts du répertoire.</p>
     * @param directory */
    public void searchForScript(String directory){
        sobjectList.clear(); //Security
        File dir = new File(directory);
        for(File f : dir.listFiles()){
            if(f.getPath().endsWith(".rb")){
                actualPath = f.getPath();
                ruby.runRubyScript(actualPath);
            }
            if(f.getPath().endsWith(".py")){
                actualPath = f.getPath();
                python.runPythonScript(actualPath);
            }
            actualPath = "";
        }
    }
    
    public void runScript(Object o){
        if(o instanceof SButton){
            SButton sb = (SButton)o;
            if(sb.getPath().endsWith(".rb")){
                ruby.runRubyScript(sb.getPath());
            }else if(sb.getPath().endsWith(".py")){
                python.runPythonScript(sb.getPath());
            }
        }else if(o instanceof STab){
            STab st = (STab)o;
            if(st.getPath().endsWith(".rb")){
                ruby.runRubyScript(st.getPath());
            }else if(st.getPath().endsWith(".py")){
                python.runPythonScript(st.getPath());
            }
        }else if(o instanceof FxObject){
            FxObject fxo = (FxObject)o;
            if(fxo.getScriptPathname().endsWith(".rb")){
                ruby.runRubyScript(fxo.getScriptPathname());
            }else if(fxo.getScriptPathname().endsWith(".py")){
                python.runPythonScript(fxo.getScriptPathname());
            }
        }
    }
    
    public void runScriptAndDo(Object o){
        if(o instanceof SButton){
            SButton sb = (SButton)o;
            if(sb.getPath().endsWith(".rb")){
                ruby.runRubyScriptAndDo(sb.getPath(), sb.getFunction());
            }else if(sb.getPath().endsWith(".py")){
                python.runPythonScriptAndDo(sb.getPath(), sb.getFunction());
            }
        }else if(o instanceof STab){
            STab st = (STab)o;
            if(st.getPath().endsWith(".rb")){
                ruby.runRubyScriptAndDo(st.getPath(), st.getFunction());
            }else if(st.getPath().endsWith(".py")){
                python.runPythonScriptAndDo(st.getPath(), st.getFunction());
            }
        }else if(o instanceof FxObject){
            FxObject fxo = (FxObject)o;
            if(fxo.getScriptPathname().endsWith(".rb")){
                ruby.runRubyScriptAndDo(fxo.getScriptPathname(), fxo.getFunction());
            }else if(fxo.getScriptPathname().endsWith(".py")){
                python.runPythonScriptAndDo(fxo.getScriptPathname(), fxo.getFunction());
            }
        }else if(o instanceof DrawingScript){
            DrawingScript scr = (DrawingScript)o;
            if(scr.getScriptPathname().endsWith(".rb")){
                ruby.runRubyScriptAndDo(scr.getScriptPathname(), scr.getFunction());
            }else if(scr.getScriptPathname().endsWith(".py")){
                python.runPythonScriptAndDo(scr.getScriptPathname(), scr.getFunction());
            }
        }
    }
    
    public String runCodeAndDo(Object o){
        String value = "";
        if(o instanceof SButton){
            SButton sb = (SButton)o;
            if(sb.getPath().endsWith(".rb")){
                value = ruby.runRubyCodeAndDo(sb.getPath(), sb.getFunction());
            }else if(sb.getPath().endsWith(".py")){
                value = python.runPythonCodeAndDo(sb.getPath(), sb.getFunction());
            }
        }else if(o instanceof STab){
            STab st = (STab)o;
            if(st.getPath().endsWith(".rb")){
                value = ruby.runRubyCodeAndDo(st.getPath(), st.getFunction());
            }else if(st.getPath().endsWith(".py")){
                value = python.runPythonCodeAndDo(st.getPath(), st.getFunction());
            }
        }else if(o instanceof FxObject){
            FxObject fxo = (FxObject)o;
            if(fxo.getScriptPathname().endsWith(".rb")){
                value = ruby.runRubyCodeAndDo(fxo.getScriptPathname(), fxo.getFunction());
            }else if(fxo.getScriptPathname().endsWith(".py")){
                value = python.runPythonCodeAndDo(fxo.getScriptPathname(), fxo.getFunction());
            }
        }
        return value;
    }
    
    public String runFxCodeAndDo(String code, String function){
        String value;
        if(code.contains("):") | code.contains("# python")){
            value = python.runPythonCodeAndDo(code, function);
        }else{
            value = ruby.runRubyCodeAndDo(code, function);
        }
        return value;
    }
    
    /** <p>Register a function of a ruby script.<br />
     * Enregistre une fonction d'un script ruby.</p>
     * @param oname The display name for your effect. (required)
     * @param ofunction The def function's name to use. (required)
     * @param oversion Your revision.
     * @param odescription A small description to explain your function.
     * @param oauthor Your name or team name.
     * @param ofirstlayer The fisrt layer to apply effect.
     * @param onblayers Number of layers that effect produce.
     */
    public static void rubyRegister(Object oname, Object ofunction,
            Object oversion, Object odescription, Object oauthor,
            Object ofirstlayer, Object onblayers){
        if(actualPath!=null && actualPath.equals("")==false){
            FxObject fxo = new FxObject(oname.toString(),
                    odescription.toString(), actualPath, 
                    ofunction.toString());
            fxo.setFxObjectType(actualPath);
            fxo.setVersion(oversion.toString());
            fxo.setAuthor(oauthor.toString());
            fxo.setFirstLayer(ofirstlayer.toString());
            fxo.setNbLayers(onblayers.toString());
            sobjectList.add(fxo);
        }
    }
    
    /** <p>Register a function of a python script.<br />
     * Enregistre une fonction d'un script pythun.</p>
     * @param oname The display name for your effect. (required)
     * @param ofunction The def function's name to use. (required)
     * @param oversion Your revision.
     * @param odescription A small description to explain your function.
     * @param oauthor Your name or team name.
     * @param ofirstlayer The fisrt layer to apply effect.
     * @param onblayers Number of layers that effect produce.
     */
    public static void pythonRegister(Object oname, Object ofunction,
            Object oversion, Object odescription, Object oauthor,
            Object ofirstlayer, Object onblayers){
        if(actualPath!=null && actualPath.equals("")==false){
            FxObject fxo = new FxObject(oname.toString(),
                    odescription.toString(), actualPath, 
                    ofunction.toString());
            fxo.setFxObjectType(actualPath);
            fxo.setVersion(oversion.toString());
            fxo.setAuthor(oauthor.toString());
            fxo.setFirstLayer(ofirstlayer.toString());
            fxo.setNbLayers(onblayers.toString());
            sobjectList.add(fxo);
        }
    }
    
    public static void addButton(Object opluginname, Object odisplayname,
            Object ofunction, Object oversion, Object odescription, Object oauthor,
            Object ohelp, Object otype){
        if(actualPath!=null && actualPath.equals("")==false){
            SButton sb = new SButton(opluginname.toString(), odisplayname.toString(),
                    actualPath, ofunction.toString(), oversion.toString(),
                    odescription.toString(), oauthor.toString(),
                    ohelp.toString(), otype.toString());
            sobjectList.add(sb);
        }
    }
    
    public static void addTab(Object opluginname, Object odisplayname,
            Object ofunction, Object oversion, Object odescription, Object oauthor,
            Object ohelp){
        if(actualPath!=null){
            STab st = new STab(opluginname.toString(), odisplayname.toString(),
                    actualPath, ofunction.toString(), oversion.toString(),
                    odescription.toString(), oauthor.toString(), ohelp.toString());
            sobjectList.add(st);
        }
    }
    
    public static void addTheme(Object onimbusBase, Object ocontrol, Object oname){
        Color c1 = fromHTMLColor(onimbusBase.toString());
        Color c2 = fromHTMLColor(ocontrol.toString());
        ThemeCollection.addExternalTheme(new Theme(c1,c2,oname.toString()));
    }
    
    public static void addTheme(Object onimbusBase, Object ocontrol, Object otext, Object oname){
        Color c1 = fromHTMLColor(onimbusBase.toString());
        Color c2 = fromHTMLColor(ocontrol.toString());
        Color c3 = fromHTMLColor(otext.toString());
        ThemeCollection.addExternalTheme(new Theme(c1,c2,c3,oname.toString()));
    }
    
    public static void addTheme(Object onimbusBase, Object ocontrol, Object otext, Object oinfo, Object oname){
        Color c1 = fromHTMLColor(onimbusBase.toString());
        Color c2 = fromHTMLColor(ocontrol.toString());
        Color c3 = fromHTMLColor(otext.toString());
        Color c4 = fromHTMLColor(oinfo.toString());
        ThemeCollection.addExternalTheme(new Theme(c1,c2,oname.toString()));
    }
    
    public List<Object> getSObjectList(){
        return sobjectList;
    }
    
    public void clearSObjectList(){
        sobjectList.clear();
    }
    
    //==========================================================================
    //****************************************************************IO Methods
    //==========================================================================
    
    public void setPaths(String fxPath, String scriptPath, String docsPath){
        ScriptPlugin.fxScripts = fxPath;
        ScriptPlugin.script = scriptPath;
        ScriptPlugin.docsPath = docsPath;
    }
    
    public void setModelsForManagement(DefaultTableModel org, DefaultTableModel res){
        ScriptPlugin.orgModel = org;
        ScriptPlugin.resModel = res;
    }
    
    public void setVideoSize(int videoWidth, int videoHeight){
        ScriptPlugin.videoWidth = videoWidth;
        ScriptPlugin.videoHeight = videoHeight;
    }
    
    public void setAssStyleCollection(AssStyleCollection asc){
        ScriptPlugin.ascScript = asc;
    }
    
    public void setFrameReference(Frame frame){
        this.frame = frame;
        ruby = new JRubyScript(frame);
        python = new JythonScript(frame);
    }
    
    //==========================================================================
    //****************************************************************** Methods
    //==========================================================================
    
    private static Color fromHTMLColor(String HTMLColor){
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
    
    /** <p>Set the path of actual ruby script.<br />
     * Définit le chemin du script ruby actuel.</p> */
    public static void setScript(Object opath){
        script = opath.toString();
    }
    
    /** <p>Get the path of the last ruby script.<br />
     * Obtient le chemin du dernier script ruby.</p> */
    public static String getScript(){
        return script;
    }

    /** <p>Get the absolute path of all scripts.<br />
     * Obtient le chemin de l'endroit où sont sauvegardés tous les scripts.</p> */
    public static String getScriptsPath(){
        return fxScripts;
    }
    
    /** <p>Get the absolute path of all documents.<br />
     * Obtient le chemin de l'endroit où sont sauvegardés tous les documents.</p> */
    public static String getDocsPath(){
        return docsPath;
    }
    
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
    public static Object[][] phKaraoke(String sentence){
        //{\k31}ra
        //(?<temps>\d+)\}(?<syllabe>[^\{]*) en vb.net
        //(\\d+)\\}([^\\{]*) en java
        Pattern p = Pattern.compile("(\\d+)\\}([^\\{]*)");
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
     * @see getSentence()
     * @see getHead()
     * @see getAssLineOf() */
    public static String phBeforeAfter(String head, String moment, String time){
        if(moment.equalsIgnoreCase("Meantime")==false){
            //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
            //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
            Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
            Matcher m = p.matcher(head);

            Time start = new Time();
            Time end = new Time();
            Time t = new Time();
            Time new_start = new Time();
            Time new_end = new Time();

            t = t.fromMillisecondsTime(Integer.parseInt(time));

            boolean endTime = false;
            while(m.find()){
                if (endTime == false){
                    start.setHours(Integer.parseInt(m.group(1)));
                    start.setMinutes(Integer.parseInt(m.group(2)));
                    start.setSeconds(Integer.parseInt(m.group(3)));
                    start.setMilliseconds(Integer.parseInt(m.group(4))*10);
                }else{
                    end.setHours(Integer.parseInt(m.group(1)));
                    end.setMinutes(Integer.parseInt(m.group(2)));
                    end.setSeconds(Integer.parseInt(m.group(3)));
                    end.setMilliseconds(Integer.parseInt(m.group(4))*10);
                }
                endTime = true;
            }

            if (moment.equalsIgnoreCase("Before")){
                new_start = start.substract(t, start);
                new_end = start;
            }else if (moment.equalsIgnoreCase("After")){
                new_start = end;
                new_end = end.addition(end, t);
            }

            String[] hdata = head.split(",");
            if(hdata.length==9){
                return hdata[0] + "," + new_start.toASSTime() + ","
                    + new_end.toASSTime() + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + "," + hdata[8];
            }else{
                return hdata[0] + "," + new_start.toASSTime() + ","
                    + new_end.toASSTime() + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + ",";
            }
        }else{
            return head;
        }
    }
    
    /** <p>Get a new head with modified time if the moment isn't 'Meantime'.<br />
     * Obtient une nouvelle entête avec des temps modifiés si le moment 
     * n'est pas 'Pendant'.</p>
     * @param head The actual head of the ass line.
     * @param moment The moment requested.
     * @param time The time for the new period (start -> end) of the head.
     * @return The new head of your ass line. You have to regroup the head and the sentence to have your new ass line.
     * @see getSentence()
     * @see getHead()
     * @see getAssLineOf() */
    public static String phShift(String head, String time){        
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
        Matcher m = p.matcher(head);

        Time start = new Time();
        Time end = new Time();
        Time t = new Time();

        t = t.fromMillisecondsTime(Integer.parseInt(time));

        boolean endTime = false;
        while(m.find()){
            if (endTime == false){
                start.setHours(Integer.parseInt(m.group(1)));
                start.setMinutes(Integer.parseInt(m.group(2)));
                start.setSeconds(Integer.parseInt(m.group(3)));
                start.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }else{
                end.setHours(Integer.parseInt(m.group(1)));
                end.setMinutes(Integer.parseInt(m.group(2)));
                end.setSeconds(Integer.parseInt(m.group(3)));
                end.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }
            endTime = true;
        }

        Time new_start = start.addition(t, start);
        Time new_end = end.addition(t, end);

        String[] hdata = head.split(",");
        if(hdata.length==9){
            return hdata[0] + "," + new_start.toASSTime() + ","
                + new_end.toASSTime() + "," + hdata[3] + "," + hdata[4] + ","
                + hdata[5] + "," + hdata[6] + "," + hdata[7] + "," + hdata[8];
        }else{
            return hdata[0] + "," + new_start.toASSTime() + ","
                + new_end.toASSTime() + "," + hdata[3] + "," + hdata[4] + ","
                + hdata[5] + "," + hdata[6] + "," + hdata[7] + ",";
        }
    }
    
    /** <p>Change the layer of the given head.<br />
     * Change la couche de l'entête.</p> */
    public static String phChangeLayer(String head, String firstLayer){
        String[] hdata = head.split(",");
        if(hdata.length==9){
            return "Dialogue: "+ firstLayer + "," + hdata[1] + ","
                    + hdata[2] + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + "," + hdata[8];
        }else{
            return "Dialogue: "+ firstLayer + "," + hdata[1] + ","
                    + hdata[2] + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + ",";
        }
    }

    /** <p>Change the style name of the given head.<br />
     * Change le nom du style de l'entête.</p> */
    public static String phChangeStyle(String head, String styleName){
        String[] hdata = head.split(",");
        if(hdata.length==9){
            return hdata[0] + "," + hdata[1] + "," + hdata[2] +
                    "," + styleName + "," + hdata[4] + "," + hdata[5] +
                    "," + hdata[6] + "," + hdata[7] + "," + hdata[8];
        }else{
            return hdata[0] + "," + hdata[1] + "," + hdata[2] +
                    "," + styleName + "," + hdata[4] + "," + hdata[5] +
                    "," + hdata[6] + "," + hdata[7] + ",";
        }
    }
    
    /** <p>Change the start time of the given head.<br />
     * Change le temps de début de l'entête.</p> */
    public static String phChangeStart(String head, String start){
        String[] hdata = head.split(",");
        try{
            Time t = new Time();
            t = t.fromMillisecondsTime(Integer.parseInt(start));
            start = t.toASSTime();
        }catch(NumberFormatException nbe){            
        }
        if(hdata.length==9){
            return hdata[0] + "," + start + ","
                    + hdata[2] + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + "," + hdata[8];
        }else{
            return hdata[0] + "," + start + ","
                    + hdata[2] + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + ",";
        }
    }
    
    /** <p>Change the end time of the given head.<br />
     * Change le temps de fin de l'entête.</p> */
    public static String phChangeEnd(String head, String end){
        String[] hdata = head.split(",");
        try{
            Time t = new Time();
            t = t.fromMillisecondsTime(Integer.parseInt(end));
            end = t.toASSTime();
        }catch(NumberFormatException nbe){            
        }
        if(hdata.length==9){
            return hdata[0] + "," + hdata[1] + ","
                    + end + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + "," + hdata[8];
        }else{
            return hdata[0] + "," + hdata[1] + ","
                    + end + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + ",";
        }
    }
    
    public static String phGetMillisecondsStart(String head){
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
        Matcher m = p.matcher(head);
        
        Time start = new Time();
        Time end = new Time();
        
        boolean endTime = false;
        while(m.find()){
            if (endTime == false){
                start.setHours(Integer.parseInt(m.group(1)));
                start.setMinutes(Integer.parseInt(m.group(2)));
                start.setSeconds(Integer.parseInt(m.group(3)));
                start.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }else{
                end.setHours(Integer.parseInt(m.group(1)));
                end.setMinutes(Integer.parseInt(m.group(2)));
                end.setSeconds(Integer.parseInt(m.group(3)));
                end.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }
            endTime = true;
        }
        
        return Long.toString(start.toMillisecondsTime(start));
    }
    
    public static String phGetMillisecondsEnd(String head){
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
        Matcher m = p.matcher(head);
        
        Time start = new Time();
        Time end = new Time();
        
        boolean endTime = false;
        while(m.find()){
            if (endTime == false){
                start.setHours(Integer.parseInt(m.group(1)));
                start.setMinutes(Integer.parseInt(m.group(2)));
                start.setSeconds(Integer.parseInt(m.group(3)));
                start.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }else{
                end.setHours(Integer.parseInt(m.group(1)));
                end.setMinutes(Integer.parseInt(m.group(2)));
                end.setSeconds(Integer.parseInt(m.group(3)));
                end.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }
            endTime = true;
        }
        
        return Long.toString(end.toMillisecondsTime(end));
    }

    /** <p>Return a new sentence with karaoke per letter from a given sentence.<br />
     * Retourne une nouvelle phrase avec un karaoke par lettre à partir de la phrase fournie</p>
     * <p>Original version @ KaraModeFunsub - 2006-2008</p> */
    public static String phPerLetterKara(String sentence){
        String new_sentence = "";
        int first, last, countLetters;

        //On récupère le tableau de syllabes afin de recréer la chaine et
        // de pouvoir calculer les durées de syllabes.
        //Calcul d'une syllabe : tps syl / nb lettres =(première lettre) |
        // tps syl - ((nb lettres - 1) * première lettre) =(dernière lettre)

        Object[][] syl = phKaraoke(sentence);
        
        for (int i=0; i<syl.length;i++){
            countLetters = getCountLetters(syl[i][0].toString());
            if (countLetters>1){
                //On traite les syllabes à plusieurs lettres (espace non compté)
                first = Integer.parseInt(syl[i][1].toString()) / countLetters;
                last = Integer.parseInt(syl[i][1].toString()) -
                        ((countLetters - 1) * first);
                
                for (int j=0;j<countLetters;j++){
                    if(j!=countLetters-1){
                        new_sentence += "{\\k" + first + "}" +
                                (syl[i][0].toString()).substring(j,j+1);
                    }else{
                        new_sentence += "{\\k" + last + "}" +
                                (syl[i][0].toString()).substring(j);
                    }
                }
            }else{
                new_sentence += "{\\k" + syl[i][1].toString() +
                        "}" + syl[i][0].toString();
            }
            
        }
        return new_sentence;
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
    public static int[] phPerLetterKaraCrossIndex(String sentence){
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
    public static int getCountLetters(String syllabe){
        Pattern p = Pattern.compile("[^0-9a-zA-Z]*([0-9a-zA-Z]*)");
        Matcher m = p.matcher(syllabe);
        m.find();
        return m.group().length();
    }

    /** <p>Get the header of a ASS/SSA line.<br />
     * Obtient l'entête de cette ligne ASS/SSA.</p> */
    public static String getHead(String assline){
        Pattern p = Pattern.compile("([^:]+:\\s[a-zA-Z=]*\\d+," +
                            "\\d+:\\d+:\\d+.\\d+," +
                            "\\d+:\\d+:\\d+.\\d+," +
                            "[^,]+,[^,]*," +
                            "\\d+,\\d+,\\d+,[^,]*),(.*)");
        Matcher m = p.matcher(assline);
        m.find();
        return m.group(1);
    }

    /** <p>Get the sentence of a ASS/SSA line.<br />
     * Obtient la phrase de cette ligne ASS/SSA.</p> */
    public static String getSentence(String assline){
        Pattern p = Pattern.compile("([^:]+:\\s[a-zA-Z=]*\\d+," +
                            "\\d+:\\d+:\\d+.\\d+," +
                            "\\d+:\\d+:\\d+.\\d+," +
                            "[^,]+,[^,]*," +
                            "\\d+,\\d+,\\d+,[^,]*),(.*)");
        Matcher m = p.matcher(assline);
        m.find();
        return m.group(2);
    }
    
    /** <p>Get a complete ASS line from a head and a sentence.<br />
     * Obrient une ligne complète en regroupant entête et phrase.</p> */
    public static String getAssLineOf(String head, String sentence){
        return head + "," + sentence;
    }
    
    /** <p>Replace all parameters of sentence and do calculation.<br />
     * Remplace tous les paramètres de cette phrase et fait les calculs.</p>
     * @param expression The commands for this line (object of the plugin).
     * @param syl Syllabes or letters table (obtain it with phKaraoke() or
     * phPerLetterKara()).
     * @param currentIndex The actual index of syllabe in line.
     * @param head Head of line (obtain it with getHead()).
     * @param osyl Syllabes table when letters mode is used
     * (obtain it with phKaraoke()).
     * @param currentCrossIndex The index of syllabes when letters mode is used
     * (obtain an integer by using phPerLetterKaraCrossIndex()).
     * @return A new sentence.
     * @see getSentence()
     * @see phKaraoke()
     * @see getHead() */
    public static String phReplaceParameters(String expression,
            Object[][] syl, int currentIndex, String head,
            Object[][] osyl, int currentCrossIndex){
        //First step : change all parameters
        
        /* List of parameters :
         * %sK          start Karaoke       when syllabe must start highlight
         * %eK          end Karaoke         when syllabe must stop highlight
         * %dK          during Karaoke
         * %osK         start Karaoke       as original %sK (if perletter mode)
         * %oeK         end Karaoke         as original %eK (if perletter mode)
         * %odK         during Karaoke      as original %dK (if perletter mode)
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
            
            if(osyl != null && Integer.valueOf(currentCrossIndex) != -1){
                //%osK - start Karaoke
                expression = expression.replaceAll("%osK",
                        osyl[currentCrossIndex][3].toString());

                //%oeK - end Karaoke
                expression = expression.replaceAll("%oeK",
                        osyl[currentCrossIndex][4].toString());

                //%odK - during Karaoke
                expression = expression.replaceAll("%odK",
                        osyl[currentCrossIndex][2].toString());
            }

            if(head != null){
                Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
                Matcher m = p.matcher(head);

                Time start = new Time();
                Time end = new Time();
                Time t = new Time();
                
                boolean endTime = false;
                while(m.find()){
                    if (endTime == false){
                        start.setHours(Integer.parseInt(m.group(1)));
                        start.setMinutes(Integer.parseInt(m.group(2)));
                        start.setSeconds(Integer.parseInt(m.group(3)));
                        start.setMilliseconds(Integer.parseInt(m.group(4))*10);
                    }else{
                        end.setHours(Integer.parseInt(m.group(1)));
                        end.setMinutes(Integer.parseInt(m.group(2)));
                        end.setSeconds(Integer.parseInt(m.group(3)));
                        end.setMilliseconds(Integer.parseInt(m.group(4))*10);
                    }
                    endTime = true;
                }
                
                t = t.substract(start, end);

                //%dP - during sentence
                expression = expression.replaceAll("%dP",
                        Long.toString(t.toMillisecondsTime(t)));
            }

            //%posAF[] - randomized position
            while(expression.contains("%posAF[")){
                Pattern p = Pattern.compile("(\\d+),(\\d+),(\\d+),(\\d+)");
                Matcher m = p.matcher(expression);
                
                m.find();
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

            //%num[] - randomized number
            while(expression.contains("%num[")){
                Pattern p = Pattern.compile("(\\d+),(\\d+)");
                Matcher m = p.matcher(expression);

                m.find();
                int a1 = Integer.parseInt(m.group(1));
                int a2 = Integer.parseInt(m.group(2));

                double a = a1+Math.round((a2-a1)*Math.random());

                int index0 = expression.indexOf("%num[");
                int index1 = expression.indexOf("]",index0);

                expression = expression.substring(0, index0) + a +
                        expression.substring(index1+1);
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
                Pattern p = Pattern.compile("t\\(0,0,([^\\)]+)\\)");
                Matcher m = p.matcher(expression);

                m.find();

                int index0 = expression.indexOf("\\t(0,0");
                int index1 = expression.indexOf(")",index0+1);

                expression = expression.substring(0, index0) + m.group(1) +
                        expression.substring(index1+1);
            }

            
            
            
        }catch(NullPointerException npe){

        }catch(Exception exc){
            
        }
        
        //Second step : calculate
//        Calcul calc = new Calcul();
//        System.out.println("Le résultat de -(-20.001*-2.3)--8 est : " +
//                calc.doCalcul("-(-20.001*-2.3)--8"));
        
        return expression;
    }
    
    /** <p>Return the request line of the 'Original tab' table or a fake line 
     * if there is an error. Use it with scripts and XFX.<br />
     * Retourne la ligne voulue de la table 'Original' ou, si non trouvé, 
     * une fausse ligne. A utiliser avec les scripts et les XFX.</p>
     * @param row A line number.
     * @return A line in ASS format. */
    public static String getOrgLine(int row){
        try{
            String[] s = new String[12];
            for (int i=0; i<12; i++){
                s[i] = (String)orgModel.getValueAt(row, i);
            }
            ProgramLine pl = AssIO.Format(s, AssIO.ModeFormat.ProgramToASS);
            return pl.toAssLine();
        }catch(Exception ex){
            return "Dialogue: 0,0:00:00.00,0:00:00.00,NoStyle,," +
                    "0000,0000,0000,,ERROR: "+ex.getMessage();
        }        
    }
    
    /** <p>Return the next selected line of the 'Original tab' table or a fake 
     * line if there is an error. Use it with scripts and XFX.<br />
     * Retourne la ligne sélectionnée de la table 'Original' ou, si non trouvé, 
     * une fausse ligne. A utiliser avec les scripts et les XFX.</p>
     * @return A line in ASS format. */
    public static String getSelectedOrgLine(){
        if(feuille.karaoke.KaraokePanel.getLastSelectedRow()!=-1){
            String[] s = new String[12];
            for (int i=0; i<12; i++){
                s[i] = (String)orgModel.getValueAt(feuille.karaoke.KaraokePanel.getLastSelectedRow(), i);
            }
            ProgramLine pl = AssIO.Format(s, AssIO.ModeFormat.ProgramToASS);
            return pl.toAssLine();
        }else{
            return "Dialogue: 0,0:00:00.00,0:00:00.00,NoStyle,," +
                    "0000,0000,0000,,ERROR: No selected line to process.";
        }
        
    }
    
    /** <p>Return the selected lines of the 'Original tab' table. 
     * Use it with scripts and XFX.<br />
     * Retourne les lignes sélectionnées de la table 'Original'. 
     * A utiliser avec les scripts et les XFX.</p>
     * @return The lines in ASS format. */
    public static String[] getSelectedOrgLines(){
        String[] table = new String[feuille.karaoke.KaraokePanel.getSelectedRows().length];
        int count = 0;
        for(int index : feuille.karaoke.KaraokePanel.getSelectedRows()){
            String[] s = new String[12];
            for (int i=0; i<12; i++){
                s[i] = (String)orgModel.getValueAt(index, i);
            }
            ProgramLine pl = AssIO.Format(s, AssIO.ModeFormat.ProgramToASS);
            table[count] = pl.toAssLine();
            count += 1;
        }
        return table;
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
    public static String getFxoMoment(String xmlpresetname){
//        for (FxObject fxo : (FxObject[])fxModel.toArray()){
        for (FxObject fxo : feuille.karaoke.KaraokePanel.getFxObjectListFromFxTree()){
            if(fxo.getName().equals(xmlpresetname)){
                return fxo.getMoment();
            }
        }
        return "undefined?";
    }

    /** <p>Return the time of this XMLPreset (XFX) or 'undefined?'.<br />
     * Retourne le temps de ce XFX ou revoit la chaine d'erreur 'undefined?'.</p> */
    public static String getFxoTime(String xmlpresetname){
        for (FxObject fxo : feuille.karaoke.KaraokePanel.getFxObjectListFromFxTree()){
            if(fxo.getName().equals(xmlpresetname)){
                return fxo.getTime();
            }
        }
        return "undefined?";
    }

    /** <p>Return the first layer of this XMLPreset (XFX) or 'undefined?'.<br />
     * Retourne la première couche de ce XFX ou revoit la chaine d'erreur 'undefined?'.</p> */
    public static String getFxoFirstLayer(String xmlpresetname){
        for (FxObject fxo : feuille.karaoke.KaraokePanel.getFxObjectListFromFxTree()){
            if(fxo.getName().equals(xmlpresetname)){
                return fxo.getFirstLayer();
            }
        }
        return "undefined?";
    }

    /** <p>Return the number of layers of this XMLPreset (XFX) or 'undefined?'.<br />
     * Retourne le nombre de couches de ce XFX ou revoit la chaine d'erreur 'undefined?'.</p> */
    public static String getFxoNbLayers(String xmlpresetname){
        for (FxObject fxo : feuille.karaoke.KaraokePanel.getFxObjectListFromFxTree()){
            if(fxo.getName().equals(xmlpresetname)){
                return fxo.getNbLayers();
            }
        }
        return "undefined?";
    }

    /** <p>Return the style of this XMLPreset (XFX) or 'undefined?'.<br />
     * Retourne le style de ce XFX ou revoit la chaine d'erreur 'undefined?'.</p> */
    public static String getFxoStyle(String xmlpresetname){
        for (FxObject fxo : feuille.karaoke.KaraokePanel.getFxObjectListFromFxTree()){
            if(fxo.getName().equals(xmlpresetname)){
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
    public static double[][] phXAbscissas(Font font, String sentence){
        //{\k31}ra
        //(?<temps>\d+)\}(?<syllabe>[^\{]*) en vb.net
        //(\\d+)\\}([^\\{]*) en java
        Pattern p = Pattern.compile("(\\d+)\\}([^\\{]*)");
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
        
        double coef = pf.coefProblemFont(font);

        for(int i=0;i<syl.size();i++){
            int width = (int)(fm.stringWidth(syl.get(i))*coef);
            xAbscisse[i][0] = pX; //x coté gauche
            xAbscisse[i][1] = width+pX;//x coté droit
            xAbscisse[i][2] = width/2+pX;//x milieu
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
    public static double[] phYOrdinates(Font font){

        // fake frame but I need it to work with FontMetrics in static
        java.awt.Frame frm = new java.awt.Frame();
        FontMetrics fm=frm.getFontMetrics(font);
        double[] yOrdinates = new double[3];

        yOrdinates[0] = fm.getHeight();     // bottom
        yOrdinates[1] = 0;                  // top
        yOrdinates[2] = fm.getHeight()/2;   // middle

        return yOrdinates;
    }
    
    /** <p>Get the width of the video (defined in the options).<br />
     * Obtient la largeur de la vidéo (défini dans les options).</p> */
    public static int getVideoWidth(){
        return videoWidth;
    }

    /** <p>Get the height of the video (defined in the options).<br />
     * Obtient la hauteur de la vidéo (défini dans les options).</p> */
    public static int getVideoHeight(){
        return videoHeight;
    }

    /** <p>Get a stripped sentence from a karaoke sentence.<br />
     * Obtient une phrase sans temps de karaoke à partir d'une phrase avec des karaoke.</p> */
    public static String getStrippedSentence(String sentence){
        //{\k31}ra
        //(?<temps>\d+)\}(?<syllabe>[^\{]*) en vb.net
        //(\\d+)\\}([^\\{]*) en java
        Pattern p = Pattern.compile("\\d+\\}([^\\{]*)");
        Matcher m = p.matcher(sentence);

        String newSentence = "";

        while(m.find()){
            newSentence = newSentence + m.group(1);
        }
        return newSentence;
    }

    /** <p>Get the width of the sentence.<br />
     * Obtient la largeur de la phrase.</p> */
    public static int getStringWidth(Font font, String sentence){
        // fake frame but I need it to work with FontMetrics in static
        java.awt.Frame frm = new java.awt.Frame();
        FontMetrics fm=frm.getFontMetrics(font);
        
        double coef = pf.coefProblemFont(font);

        int sWidth = (int)(fm.stringWidth(getStrippedSentence(sentence))*coef);

        return sWidth;
    }

    /** <p>Get the position of x from the style.<br />
     * Obtient la position de x à partir du style.</p> */
    public static int getPosXFromStyle(AssStyle as, String sentence){
        int posX = -1;
        Font font = as.getFont();//Search for font
        int sentenceWidth = getStringWidth(font, sentence);        
        if(as.getAlignment()==1 | as.getAlignment()==4 | as.getAlignment()==7){
            //if text is to the left then posX=marginL
            return as.getMarginL();
        }else if(as.getAlignment()==2 | as.getAlignment()==5 | as.getAlignment()==8){
            //if text is to the center then posX=(videoWidth-sentenceWidth)/2
            return (videoWidth-sentenceWidth)/2;
        }else if(as.getAlignment()==3 | as.getAlignment()==6 | as.getAlignment()==9){
            //if text is to the right then posX=videoWidth-(sentenceWidth+marginR)
            return videoWidth-(sentenceWidth+as.getMarginR());
        }
        return posX; //error
    }

    /** <p>Get the position of y from the style.<br />
     * Obtient la position de y à partir du style.</p> */
    public static int getPosYFromStyle(AssStyle as){
        int posY = -1;
        //Font font = as.getFont();//Search for font
        
        if(as.getAlignment()==1 | as.getAlignment()==2 | as.getAlignment()==3){
            return videoHeight-as.getMarginB();
        }else if(as.getAlignment()==4 | as.getAlignment()==5 | as.getAlignment()==6){
            return videoHeight/2;
        }else if(as.getAlignment()==7 | as.getAlignment()==8 | as.getAlignment()==9){
            return as.getMarginT();
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
     * @see phXAbscissas()
     * @see phYOrdinates()
     * @see getPosXFromStyle()
     * @see getPosYFromStyle()
     * @return A sentence without parameters. */
    public static String phReplacePosParameters(String expression,
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
    public static String[] getColorFromStyle(String style){
        try{
            String[] colors = new String[4];
            for (AssStyle as : ascScript.getMembers()){
                if(as.getElement(AssStyle.AssStyleType.name).equals(style)){
                    String color1, color2, color3, color4;
                    color1 = as.getElement(AssStyle.AssStyleType.primarycolour, "+");
                    color2 = as.getElement(AssStyle.AssStyleType.secondarycolour, "+");
                    color3 = as.getElement(AssStyle.AssStyleType.outlinecolour, "+");
                    color4 = as.getElement(AssStyle.AssStyleType.backcolour, "+");
                    colors[0] = "H"+color1.substring(2);
                    colors[1] = "H"+color2.substring(2);
                    colors[2] = "H"+color3.substring(2);
                    colors[3] = "H"+color4.substring(2);
                }
            }
            return colors;
        }catch(Exception exc){
            return null;
        }
    }
    
    public static int[] phPosXSyllable(Object[][] kara, String assline, float poscor, int spacor, AssStyle astyle){
//        AssStyle astyle = new AssStyle();
//        //Get AssStyle from assline
//        Pattern p = Pattern.compile("[^:]+:\\s[a-zA-Z=]*\\d+," +
//                            "\\d+:\\d+:\\d+.\\d+," +
//                            "\\d+:\\d+:\\d+.\\d+," +
//                            "([^,]+),[^,]*," +
//                            "\\d+,\\d+,\\d+,[^,]*,(.*)");
//        Matcher m = p.matcher(assline); m.find();
//        String style = m.group(1);
//        for (AssStyle as : ascScript.getMembers()){
//            if(as.getElement(AssStyleType.name).equals(style)){
//                astyle = as;
//            }
//        }
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();        
        FontMetrics fm = gra.getFontMetrics(astyle.getFont());
        int[] position = new int[kara.length];        
        float totalSize = 0;
        for(int i=0;i<kara.length;i++){
            String str = (String)kara[i][0];
            int sylSize = Math.round(fm.stringWidth(str)/poscor);
            position[i] = Math.round(totalSize+sylSize/2);
            if(str.contains(" ")){
                sylSize = sylSize + spacor;
            }            
            totalSize += sylSize;
        }        
        return position;
    }
    
    public static int[] phPosXSyllable(Object[][] kara, AssStyle astyle){
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();        
        FontMetrics fm = gra.getFontMetrics(astyle.getFont());
        double coef = pf.coefProblemFont(astyle.getFont());
        int[] position = new int[kara.length];        
        float totalSize = 0;
        for(int i=0;i<kara.length;i++){
            String str = (String)kara[i][0];
            int sylSize = (int)(fm.stringWidth(str)*coef);
            position[i] = Math.round(totalSize+sylSize/2);
            totalSize += sylSize;
        }        
        return position;
    }
    
    public static int phXSentenceWidth(Object[][] kara, String assline, float poscor, int spacor, AssStyle astyle){
//        AssStyle astyle = new AssStyle();
//        //Get AssStyle from assline
//        Pattern p = Pattern.compile("[^:]+:\\s[a-zA-Z=]*\\d+," +
//                            "\\d+:\\d+:\\d+.\\d+," +
//                            "\\d+:\\d+:\\d+.\\d+," +
//                            "([^,]+),[^,]*," +
//                            "\\d+,\\d+,\\d+,[^,]*,(.*)");
//        Matcher m = p.matcher(assline); m.find();
//        String style = m.group(1);
//        for (AssStyle as : ascScript.getMembers()){
//            if(as.getElement(AssStyleType.name).equals(style)){
//                astyle = as;
//            }
//        }
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();        
        FontMetrics fm = gra.getFontMetrics(astyle.getFont());
        float totalSize = 0;
        for(int i=0;i<kara.length;i++){
            String str = (String)kara[i][0];
            int sylSize = Math.round(fm.stringWidth(str)/poscor);
            if(str.contains(" ")){
                sylSize = sylSize + spacor;
            }            
            totalSize += sylSize;
        }
        return Math.round(totalSize);
    }
    
    public static int phXSentenceWidth(Object[][] kara, AssStyle astyle){
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();        
        FontMetrics fm = gra.getFontMetrics(astyle.getFont());
        double coef = pf.coefProblemFont(astyle.getFont());
        float totalSize = 0;
        for(int i=0;i<kara.length;i++){
            String str = (String)kara[i][0];
            int sylSize = (int)(fm.stringWidth(str)*coef);
            totalSize += sylSize;
        }
        return Math.round(totalSize);
    }
    
    public static int phXCharsWidth(String sentence,  AssStyle astyle){
        
        //Create a false image from text measuring
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics gra = bi.getGraphics();
        
        //Get the metrics for the used font
        FontMetrics fm = gra.getFontMetrics(astyle.getFont());
        
        int advance = gra.getFontMetrics().getMaxAdvance();
        int width = getMaxWidth(fm);
        
        //Calculation
        int totalSize = 0; int emuSize = 0;
        System.out.println("La phrase est : "+sentence);
        System.out.println("La police est : "+astyle.getFontname());
        System.out.println("La taile de la police est : "+astyle.getFont().getSize());
        System.out.println("MaxAdvance : "+advance);
        for(int i=0;i<sentence.length();i++){
            char letter = sentence.charAt(i);
            int letterSize = fm.charWidth(letter);
            int letterSize2 = letterSize * advance / width - (advance*20/100);// + (advance*17/100)
            System.out.println("Caractère en cours : '"+letter+"'");
            System.out.println("La largeur de ce caractère est : "+letterSize);
            System.out.println("La largeur émulée : "+letterSize2);
            totalSize += letterSize;
            emuSize += letterSize2;
        }
        System.out.println("Phrase émulée : "+emuSize);
        
        return Math.round(emuSize);
    }
    
    public static int getMaxWidth(FontMetrics fm){
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int max = 0;
        for(int i=0;i<alpha.length();i++){
            char letter = alpha.charAt(i);
            int letterSize = fm.charWidth(letter);
            max = letterSize > max ? letterSize : max;
        }
        return max;
    }
    
    public static String getKaraokeStart(){
        return feuille.karaoke.KaraokePanel.getKaraokeStart();
    }
    
    public static String getKaraokeEnd(){
        return feuille.karaoke.KaraokePanel.getKaraokeEnd();
    }
    
    public static String getKaraokeDuration(){
        return feuille.karaoke.KaraokePanel.getKaraokeDuration();
    }
    
    public static String getKaraokeOStart(){
        return feuille.karaoke.KaraokePanel.getKaraokeOStart();
    }
    
    public static String getKaraokeOEnd(){
        return feuille.karaoke.KaraokePanel.getKaraokeOEnd();
    }
    
    public static String getKaraokeODuration(){
        return feuille.karaoke.KaraokePanel.getKaraokeODuration();
    }
    
    public static String getKaraokeSDuration(){
        return feuille.karaoke.KaraokePanel.getKaraokeSDuration();
    }
    
    /** <p>Add a line at the end of lines of the 'Original tab' table.
     * Use it with scripts and XFX.<br />
     * Ajoute une ligne à la fin de la table 'Original'. 
     * A utiliser avec les scripts et les XFX.</p>
     * @param oline Your line in ASS format. */
    public static void addOrgLine(Object oline){
        String s = (String)oline;
        ProgramLine pl = AssIO.Format(s,AssIO.ModeFormat.ASSToProgram);        
        if(pl.getLineType().getLabel()
                .equals(ProgramLine.LineType.Nothing.getLabel())==false){
            orgModel.addRow(pl.toRow());
        }
    }
    
    /** <p>Add a line at the end of lines of the 'Result tab' table.
     * Use it with scripts and XFX.<br />
     * Ajoute une ligne à la fin de la table 'Résultat'. 
     * A utiliser avec les scripts et les XFX.</p>
     * @param oline Your line in ASS format. */
    public static void addResLine(Object oline){
        String s = (String)oline;
        ProgramLine pl = AssIO.Format(s,AssIO.ModeFormat.ASSToProgram);        
        if(pl.getLineType().getLabel()
                .equals(ProgramLine.LineType.Nothing.getLabel())==false){
            resModel.addRow(pl.toRow());
        }
    }
    
    public static AssStyle getAssStyle(String line){
        //Get AssStyle from assline
        Pattern p = Pattern.compile("[^:]+:\\s[a-zA-Z=]*\\d+," +
                            "\\d+:\\d+:\\d+.\\d+," +
                            "\\d+:\\d+:\\d+.\\d+," +
                            "([^,]+),[^,]*," +
                            "\\d+,\\d+,\\d+,[^,]*,(.*)");
        Matcher m = p.matcher(line); m.find();
        String style = m.group(1);
        for (AssStyle as : ascScript.getMembers()){
            if(as.getElement(AssStyleType.name).equals(style)){
                return as;
            }
        }
        return null;
    }
    
    public static void regenerateAssStyleCollection(){
        ascScript = feuille.MainFrame.getAssStyleCollection();
    }
    
    /** <p>Launch the selected link and open a browser.<br />
     * Lance le lien sélectionné et ou un navigateur.</p> */
    public static void openLink(String link){
        boolean hasResult = true;
        if(java.awt.Desktop.isDesktopSupported()){
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if(desktop.isSupported(java.awt.Desktop.Action.BROWSE)){
                try {
                    try {
                        desktop.browse(new java.net.URI(link));
                    } catch (java.io.IOException ex) {hasResult = false;}
                } catch (java.net.URISyntaxException ex) {hasResult = false;}
            }else{
                hasResult = false;
            }
        }else{
            hasResult = false;
        }
        if(hasResult==false){
            java.util.Properties sys = System.getProperties();
            String os = sys.getProperty("os.name").toLowerCase();
            try {
                if(os.contains("windows")==true){
                    Process proc = Runtime.getRuntime().exec("cmd /c start "+link);
                }else{
                    Process proc = Runtime.getRuntime().exec("start "+link);
                }
            } catch (java.io.IOException e) {
                // unsupported
            }
        }        
    }
    
    /** <p>Launch the selected link and open a browser.<br />
     * Lance le lien sélectionné et ou un navigateur.</p> */
    public static void openFile(String doc){
        boolean hasResult = true;
        if(java.awt.Desktop.isDesktopSupported()){
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if(desktop.isSupported(java.awt.Desktop.Action.OPEN)){
                try {
                    desktop.open(new File(doc));
                } catch (Exception ex) {hasResult = false;}                
            }else{
                hasResult = false;
            }
        }else{
            hasResult = false;
        }
        if(hasResult==false){
            java.util.Properties sys = System.getProperties();
            String os = sys.getProperty("os.name").toLowerCase();
            try {
                if(os.contains("windows")==true){
                    Process proc = Runtime.getRuntime().exec("cmd /c start "+doc);
                }else{
                    Process proc = Runtime.getRuntime().exec("start "+doc);
                }
            } catch (java.io.IOException e) {
                // unsupported
            }
        }        
    }
    
    //============================= DRAWING ====================================
    
    /** <p>Register a function of a script. (JRuby-Jython)<br />
     * Enregistre une fonction d'un script. (JRuby-Jython)</p>
     * @param oname The display name for your effect. (required)
     * @param ofunction The def function's name to use. (required)
     * @param oversion Your revision.
     * @param odescription A small description to explain your function.
     * @param oauthor Your name or team name.
     */
    public static void aspRegister(Object oname, Object ofunction,
            Object oversion, Object odescription, Object oauthor){
        if(actualPath!=null && actualPath.equals("")==false){
            DrawingScript scr = new DrawingScript(
                    oname.toString(),
                    odescription.toString(),
                    oversion.toString(),
                    oauthor.toString(),
                    actualPath, 
                    ofunction.toString());
            sobjectList.add(scr);
        }
    }
    
    public static void add_M_To(int x, int y){
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        ReStart m = new ReStart(x+1000, y+1000, x+1000, y+1000);
        lay.getShapesList().addShape(m); lay.addRemember(m);
        Sheet sh = feuille.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        feuille.drawing.DrawingPanel.updateRemember(lay);
        feuille.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_N_To(int x, int y){
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();        
        Move m = new Move(x+1000, y+1000, x+1000, y+1000);
        lay.getShapesList().addShape(m); lay.addRemember(m);
        Sheet sh = feuille.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        feuille.drawing.DrawingPanel.updateRemember(lay);
        feuille.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_L_To(int x, int y){        
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        Line l = new Line(point.x, point.y, x+1000, y+1000);
        lay.getShapesList().addShape(l); lay.addRemember(l);
        Sheet sh = feuille.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        feuille.drawing.DrawingPanel.updateRemember(lay);
        feuille.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_B_To(int x, int y){        
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        Bezier b = new Bezier(point.x, point.y, x+1000, y+1000);
        lay.getShapesList().addShape(b); lay.addRemember(b);
        ControlPoint cp1 = b.getControl1();
        lay.getShapesList().addShape(cp1); lay.addRemember(cp1);
        ControlPoint cp2 = b.getControl2();
        lay.getShapesList().addShape(cp2); lay.addRemember(cp2);
        Sheet sh = feuille.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        feuille.drawing.DrawingPanel.updateRemember(lay);
        feuille.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_B_To(int x, int y, int cpx1, int cpy1, int cpx2, int cpy2){        
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        Bezier b = new Bezier(point.x, point.y, cpx1+1000, cpy1+1000, cpx2+1000, cpy2+1000, x+1000, y+1000);
        lay.getShapesList().addShape(b); lay.addRemember(b);
        ControlPoint cp1 = b.getControl1();
        lay.getShapesList().addShape(cp1); lay.addRemember(cp1);
        ControlPoint cp2 = b.getControl2();
        lay.getShapesList().addShape(cp2); lay.addRemember(cp2);
        Sheet sh = feuille.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        feuille.drawing.DrawingPanel.updateRemember(lay);
        feuille.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_S_To(int x, int y){
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        BSpline bs = new BSpline(point.x, point.y);
        lay.getShapesList().addShape(bs); lay.addRemember(bs);
        Sheet sh = feuille.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        feuille.drawing.DrawingPanel.updateRemember(lay);
        feuille.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_S_To(int x, int y, int[] cpx, int[] cpy){
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        BSpline bs = new BSpline(point.x, point.y);
        lay.getShapesList().addShape(bs); lay.addRemember(bs);
        for(int i=0; i<cpx.length; i++){
            try{
                bs.addPoint(cpx[i], cpy[i]);
            }catch(Exception e){}
        }
        Sheet sh = feuille.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        feuille.drawing.DrawingPanel.updateRemember(lay);
        feuille.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_P_To(int x, int y){
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        IShape s = lay.getShapesList().getLastShape();
        if(s instanceof BSpline){
            BSpline bs = (BSpline)s;
            bs.setNextPoint(x+1000, y+1000);
            Sheet sh = feuille.drawing.DrawingPanel.getSheet();
            sh.updateGeneralPath(lay.getGeneralPath());
            sh.updateShapesList(lay.getShapesList());
            lay.setFirstPoint(lay.getLastPoint());
            feuille.drawing.DrawingPanel.updateRemember(lay);
            feuille.drawing.DrawingPanel.setAssCommands();
        }        
    }
    
    public static void add_C(){
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        IShape s = lay.getShapesList().getLastShape();
        if(s instanceof BSpline){
            BSpline bs = (BSpline)s;
            if(bs.isNextExist()==false){
                bs.setClosed(true);
            }
            Sheet sh = feuille.drawing.DrawingPanel.getSheet();
            sh.updateGeneralPath(lay.getGeneralPath());
            sh.updateShapesList(lay.getShapesList());
            lay.setFirstPoint(lay.getLastPoint());
            feuille.drawing.DrawingPanel.updateRemember(lay);
            feuille.drawing.DrawingPanel.setAssCommands();
        }
    }
    
    public static int createLayer(String name, int r, int g, int b){
        return feuille.drawing.DrawingPanel.createLayer(name, r, g, b);
    }
    
    public static boolean changeLayer(int id){
        return feuille.drawing.DrawingPanel.changeLayer(id);
    }
    
    public static void setLayerName(String name){
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        lay.setName(name);
        feuille.drawing.DrawingPanel.updateLayerList();
    }
    
    public static void setLayerColor(int r, int g, int b){
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        try{
            lay.setColor(new Color(r,g,b));
        }catch(Exception e){
            lay.setColor(Color.green);
        }
        feuille.drawing.DrawingPanel.updateLayerList();
    }
    
    public static void addASSCommands(String asscommands){
        feuille.drawing.DrawingPanel.shapesFromCommands(asscommands, null, 0, 0, null, 0);
        Layer lay = feuille.drawing.DrawingPanel.getCurrentLayer();
        Sheet sh = feuille.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        feuille.drawing.DrawingPanel.updateRemember(lay);
        feuille.drawing.DrawingPanel.setAssCommands();
    }
    
    public List<Object> getObjectsList(){
        return sobjectList;
    }
    
}
