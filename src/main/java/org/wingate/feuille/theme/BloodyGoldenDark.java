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
package org.wingate.feuille.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author util2
 */
public class BloodyGoldenDark extends Theme {
    
    public BloodyGoldenDark() {
        theme = new FlatDarkLaf();
        type = Type.Dark;
        name = "BloodyGolden";
        author = "TW2";
        
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#FF3200");
        settings.put("@foreground", "#FFCC00");
        theme.setExtraDefaults(settings);
    }
    
}
