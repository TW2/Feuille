/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.filter;

import java.io.File;

/**
 * <p>This filter accept only XML and XFX files.<br />
 * Ce filtre accepte seulement les fichiers XML et XFX.</p>
 * @author The Wingate 2940
 */
public class XmlPresetFilter extends javax.swing.filechooser.FileFilter {

    /** <p>Accepted files.<br />Fichier accepté.</p> */
    @Override
    public boolean accept(File f) {
        //Show folders in FileChooser
        if (f.isDirectory()) {
            return true;
        }

        //Show image files in FileChooser
        if(f.getName().endsWith(".xml") | f.getName().endsWith(".xfx")){
            return true;
        }

        //It's enough
        return false;
    }

    /** <p>Text for the combobox.<br />Le texte pour le combobox.</p> */
    @Override
    public String getDescription() {
        //Show *** in selector
        return "Xml Preset (*.xml)";
    }

}
