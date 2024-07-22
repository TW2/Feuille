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
public class VideoFileFilter extends javax.swing.filechooser.FileFilter {
    
    private final List<String> extensions = new ArrayList<>();

    public VideoFileFilter() {
        extensions.add(".avi");
        extensions.add(".mov");
        extensions.add(".rm");
        extensions.add(".mpeg");
        extensions.add(".mpg");
        extensions.add(".vob");
        extensions.add(".ts");
        extensions.add(".m2ts");
        extensions.add(".divx");
        extensions.add(".xvid");
        extensions.add(".mp4");
        extensions.add(".m4v");
        extensions.add(".mkv");
        extensions.add(".ogm");
        extensions.add(".vp8");
        extensions.add(".vp9");        
        extensions.add(".flv");
    }

    @Override
    public boolean accept(File f) {
        if(f.isDirectory()) return true;
        String name = f.getName();
        if(name.contains(".") == false) return true;
        if(name.contains(".") == true){
            String extension = name.substring(name.lastIndexOf("."));
            return extensions.contains(extension);
        }
        return false;
    }

    @Override
    public String getDescription() {
        return  "Video files";
    }
    
}
