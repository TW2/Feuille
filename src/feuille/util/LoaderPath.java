/*
 * Copyright (C) 2019 util2
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
package feuille.util;

import java.io.File;
import java.net.URL;

/**
 * GraalVM returns user.home for user.dir so just correct it
 * @author util2
 */
public class LoaderPath {
    
    private boolean enabled = false;
    private String folderPath = null;

    public LoaderPath() {
        init();
    }
    
    private void init(){        
        URL location = getClass().getResource("/feuille/MainFrame.class");
        // Format:
        // file:/D:/Dev/Java/Netbeans/FeuillleMulti/Feuillev2GraalVM/Feuille/dist/Feuille.jar!/feuille/MainFrame.class
        String locPath = location.toString();
        
        if(locPath.startsWith("jar:")) { locPath = locPath.substring("jar:".length()); }
        if(locPath.startsWith("file:/")) { locPath = locPath.substring("file:/".length()); }
        
        locPath = locPath.replaceAll("!", "");
        locPath = locPath.substring(0, locPath.lastIndexOf("/feuille/MainFrame.class") - 1);
        if(locPath.contains("build") == false){
            File jar = new File(locPath);
            folderPath = jar.getParent();
            enabled = true;
        }
    }

    public String getFolderPath() {
        return folderPath;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
