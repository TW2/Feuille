package feuille.util.filefilter;

import feuille.util.Loader;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AudioFileFilter extends FileFilter {

    final List<String> ext = Arrays.asList(
            ".mp4", ".mp3", ".m4a", ".aac", ".mp2",
            ".mp1", "wav", "wma", ".tta", ".snd",
            ".ogg", ".oga", ".opus", ".mka", ".flac");

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
        return Loader.language("file.filter.audio", "AudioFiles");
    }
}
