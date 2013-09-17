/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.filter;

import java.io.File;

/**
 *
 * @author The Wingate 2940
 */
public class VideoFilter extends javax.swing.filechooser.FileFilter {

    /** <p>Accepted files.<br />Fichier accepté.</p> */
    @Override
    public boolean accept(File f) {
        //Show folders in FileChooser
        if (f.isDirectory()) {
            return true;
        }

        //Show image files in FileChooser
        if(f.getName().endsWith(".mp4") | f.getName().endsWith(".mkv")
                 | f.getName().endsWith(".flv") | f.getName().endsWith(".avi")){
            return true;
        }

        //It's enough
        return false;
    }

    /** <p>Text for the combobox.<br />Le texte pour le combobox.</p> */
    @Override
    public String getDescription() {
        //Show *** in selector
        return "Videos File (*.mp4, *.mkv, *.flv, *.avi)";
    }
}
