/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.filter;

import java.io.File;

/**
 * Cette classe sert de filtre pour le JFileChooser demandant les images.
 * @author The Wingate 2940
 */
public class ImageFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File pathname) {
        //Voir les dossiers dans le FileChooser
        if (pathname.isDirectory()) {
            return true;
        }

        //Voir les fichiers images dans le FileChooser
        if(pathname.getName().endsWith(".tiff")
                | pathname.getName().endsWith(".tif")
                | pathname.getName().endsWith(".gif")
                | pathname.getName().endsWith(".jpeg")
                | pathname.getName().endsWith(".jpg")
                | pathname.getName().endsWith(".png")){
            return true;
        }

        //Ne rien voir d'autres
        return false;
    }

    @Override
    public String getDescription() {
        //Montrer "Images" dans le sélecteur
        return "Images";
    }

}
