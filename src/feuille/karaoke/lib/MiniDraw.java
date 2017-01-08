/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.lib;

import java.io.File;

/**
 * <p>This class is a container for the choice of drawing in the
 * DrawingChooserDialog and by the drawingChooserRenderer.<br />
 * Cette classe est utilisé comme conteneur pour le choix de dessin dans 
 * DrawingChooserDialog et par le drawingChooserRenderer.</p>
 * @see DrawingChooserDialog
 * @see drawingChooserRenderer
 * @author The Wingate 2940
 */
public class MiniDraw {
    
    private String commands = "";
    private File file;
    
    /** <p>Create a new MiniDraw.<br />Crée un nouveau MiniDraw.</p>
     * @param file A file of drawing element.
     * @param commands ASS commands for the drawing.
     */
    public MiniDraw(File file, String commands){
        this.file = file;
        this.commands = commands;
    }
    
    /** <p>Get a file.<br />Obtient un fichier.</p> */
    public File getFile(){
        return file;
    }
    
    /** <p>Get ASS commands.<br />Obtient des commandes ASS.</p> */
    public String getCommands(){
        return commands;
    }
    
}
