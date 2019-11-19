/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.filter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author The Wingate 2940
 */
public class AnyFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        //Pour voir tous les dossiers :
        if (f.isDirectory()) {
            return true;
        }

        //Montre tous les fichiers
        return true;
    }

    @Override
    public String getDescription() {
        //Montre ceci dans le sélecteur :
        return "All files (*.*)";
    }
}
