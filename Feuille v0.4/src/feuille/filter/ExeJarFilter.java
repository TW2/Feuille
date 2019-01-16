/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.filter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author The Wingate 2940
 */
public class ExeJarFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        //Show folders in FileChooser
        if (f.isDirectory()) {
            return true;
        }

        //Show image files in FileChooser
        if(f.getName().endsWith(".jar") | f.getName().endsWith(".exe")){
            return true;
        }

        //It's enough
        return false;
    }

    @Override
    public String getDescription() {
        //Show *** in selector
        return "Executable files (*.exe, *.jar)";
    }
}
