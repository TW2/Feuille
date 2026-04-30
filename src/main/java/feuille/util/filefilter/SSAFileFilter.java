package feuille.util.filefilter;

import feuille.util.Loader;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SSAFileFilter extends FileFilter {

    final List<String> ext = List.of(
            ".ssa");

    @Override
    public boolean accept(File f) {
        if(f.isDirectory()) return true;
        for(String e : ext){
            if(f.getName().endsWith(e)) return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return Loader.language("file.filter.ssa", "SubStation Alpha files");
    }
}
