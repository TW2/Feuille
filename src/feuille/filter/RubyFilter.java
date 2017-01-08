/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.filter;

import java.io.File;

/**
 * <p>This filter accept only RB files.<br />
 * Ce filtre accepte seulement les fichiers RB.</p>
 * @author The Wingate 2940
 */
public class RubyFilter extends javax.swing.filechooser.FileFilter {

    /** <p>Accepted files.<br />Fichier accepté.</p> */
    @Override
    public boolean accept(File f) {
        //Show folders in FileChooser
        if (f.isDirectory()) {
            return true;
        }

        //Show image files in FileChooser
        if(f.getName().endsWith(".rb")){
            return true;
        }

        //It's enough
        return false;
    }

    /** <p>Text for the combobox.<br />Le texte pour le combobox.</p> */
    @Override
    public String getDescription() {
        //Show *** in selector
        return "Ruby scripts (*.rb)";
    }
    
    public static String getExtension(){
        return ".rb";
    }

}
