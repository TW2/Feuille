/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.filter;

import java.io.File;

/**
 * Cette classe sert de filtre pour le JFileChooser demandant le tracé
 * du dessin ASS au format ADF (Ass Drawing File) contenant juste une ligne
 * de commandes au format de dessin ASS.
 * @author The Wingate 2940
 */
public class DrawingFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File f) {
        //Pour voir tous les dossiers :
        if (f.isDirectory()) {
            return true;
        }

        //Montre tous les fichiers au format ADF : Ass Drawing File
        if(f.getName().endsWith(".adf")){
            return true;
        }

        //C'est tout, on n'a pas besoin d'en voir plus.
        return false;
    }

    @Override
    public String getDescription() {
        //Montre ceci dans le sélecteur :
        return "Ass Drawing File (*.adf)";
    }

}
