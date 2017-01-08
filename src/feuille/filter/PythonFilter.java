/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.filter;

import java.io.File;

/**
 *
 * @author The Wingate 2940
 */
public class PythonFilter extends javax.swing.filechooser.FileFilter {
    
    /** <p>Accepted files.<br />Fichier accepté.</p> */
    @Override
    public boolean accept(File f) {
        //Show folders in FileChooser
        if (f.isDirectory()) {
            return true;
        }

        //Show image files in FileChooser
        if(f.getName().endsWith(".py")){
            return true;
        }

        //It's enough
        return false;
    }

    /** <p>Text for the combobox.<br />Le texte pour le combobox.</p> */
    @Override
    public String getDescription() {
        //Show *** in selector
        return "Python scripts (*.py)";
    }
    
    public static String getExtension(){
        return ".py";
    }
    
}
