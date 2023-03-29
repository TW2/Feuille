/*
 * Copyright (C) 2023 util2
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
package org.wingate.virginsheet.module;

import java.util.List;
import javax.swing.ImageIcon;
import org.wingate.assj.AssEvent;

/**
 *
 * @author util2
 */
public class KaraokeMLFx extends KaraokeEffect {

    private final ImageIcon icon;
    
    public KaraokeMLFx() {
        karaokeEffectType = KaraokeEffectType.ML;
        icon = new ImageIcon(getClass().getResource("images/xml-icon-small.png"));
    }

    @Override
    public void applyByLine(List<AssEvent> events) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void applyByBlock(List<AssEvent> events) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ImageIcon getIcon() {
        return icon;
    }
}
