/*
 * Copyright (C) 2024 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wingate.feuille.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author util2
 */
public class AudioFileFilter extends javax.swing.filechooser.FileFilter {
    
    private final List<String> extensions = new ArrayList<>();

    public AudioFileFilter() {
        extensions.add(".wav");
        extensions.add(".mp3");
        extensions.add(".mka");
        extensions.add(".oga");
        extensions.add(".ogg");
        extensions.add(".opus");
        extensions.add(".tta");
        extensions.add(".wma");
        extensions.add(".aac");
        extensions.add(".m4a");
        extensions.add(".mp4");
        extensions.add(".amr");
    }

    @Override
    public boolean accept(File f) {
        if(f.isDirectory()) return true;
        String name = f.getName();
        String extension = name.substring(name.lastIndexOf("."));
        return extensions.contains(extension);
    }

    @Override
    public String getDescription() {
        return  "Audio files";
    }
    
}
