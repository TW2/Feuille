/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.scripting;

import java.awt.Frame;
import java.io.FileNotFoundException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;

/**
 *
 * @author The Wingate 2940
 */
public class JRubyScript {
    
    private String rubyScript = "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\sample.rb";
    private String rubyPath = "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\";
    private ScriptEngine rubyEngine;
    private Invocable inv;
    
    Frame frame; //We have to display dialog on program (error message)
    
    public JRubyScript(Frame frame){
        this.frame = frame;
        rubyEngine = new ScriptEngineManager().getEngineByName("jruby");
        inv = (Invocable)rubyEngine;
    }
    
    /** <p>Set the path of actual ruby script.<br />
     * Définit le chemin du script ruby actuel.</p> */
    public void setRubyScript(Object opath){
        rubyScript = opath.toString();
    }
    
    /** <p>Get the path of the last ruby script.<br />
     * Obtient le chemin du dernier script ruby.</p> */
    public String getRubyScript(){
        return rubyScript;
    }

    public void setRubyScriptsPath(String rubyPath){
        this.rubyPath = rubyPath;
    }
    
    /** <p>Get the absolute path of all ruby scripts.<br />
     * Obtient le chemin de l'endroit où sont sauvegardés tous les scripts ruby.</p> */
    public String getRubyScriptsPath(){
        return rubyPath;
    }
    
    /** <p>Runs a ruby script from the given file.<br />
     * Execute un script ruby à partir d'un fichier.</p> */
    public void runRubyScript(String path){
        try{
            rubyEngine.eval(new java.io.FileReader(path));
        } catch (java.io.FileNotFoundException fnfe){            
        } catch(ScriptException se){            
        }
    }
    
    /** <p>Execute a function (of a ruby script).<br />
     * Execute une fonction (d'un script ruby).</p> */
    public void runRubyScriptAndDo(String path, String event){
        try {
            rubyEngine.eval(new java.io.FileReader(path));
            inv.invokeFunction(event);
        } catch (FileNotFoundException fnfe){
            JOptionPane.showMessageDialog(frame,
                    fnfe.getMessage(),
                    "FileNotFoundException",
                    JOptionPane.ERROR_MESSAGE);
        } catch(ScriptException se){
            JOptionPane.showMessageDialog(frame,
                    "At line "+se.getLineNumber()+
                    " column "+se.getColumnNumber()+
                    "\n\n"+se.getMessage(),
                    "ScriptException",
                    JOptionPane.ERROR_MESSAGE);
        } catch(NoSuchMethodException nsme){
            JOptionPane.showMessageDialog(frame,
                    nsme.getMessage(),
                    "NoSuchMethodException",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** <p>Search for all script of this directory.<br />
     * Recherche tous les scripts du répertoire.</p> */
    public void searchForRubyScript(String directory){
        java.io.File dir = new java.io.File(directory);
        for(java.io.File f : dir.listFiles()){
            if(f.getPath().endsWith(".rb")){
                setRubyScript(f.getPath());
                runRubyScript(f.getPath());
                setRubyScript("");
            }
        }
    }
    
    /** <p>Execute a function from a ruby code.<br />
     * Execute une fonction d'un code ruby.</p> */
    public String runRubyCodeAndDo(String code, String event){
        String value = "";
        try {
            rubyEngine.eval(new java.io.StringReader(code));
            Object o = inv.invokeFunction(event);
            value = o.toString();
        } catch(ScriptException se){
            JOptionPane.showMessageDialog(frame,
                    "At line "+se.getLineNumber()+
                    " column "+se.getColumnNumber()+
                    "\n\n"+se.getMessage(),
                    "ScriptException",
                    JOptionPane.ERROR_MESSAGE);
        } catch(NoSuchMethodException nsme){
            JOptionPane.showMessageDialog(frame,
                    nsme.getMessage(),
                    "NoSuchMethodException",
                    JOptionPane.ERROR_MESSAGE);
        } catch(Exception e){}
        return value;
    }
    
    public void setFrameReference(Frame frame){
        this.frame = frame;
    }
}
