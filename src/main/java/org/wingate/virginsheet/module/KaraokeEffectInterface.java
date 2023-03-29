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

import java.awt.Color;
import java.util.List;
import javax.swing.ImageIcon;
import org.wingate.assj.AssEvent;

/**
 *
 * @author util2
 */
public interface KaraokeEffectInterface {
    
    public void setName(String name);
    public String getName();
    
    public void setAuthor(String author);
    public String getAuthor();
    
    public void assignColor(Color color);
    public Color getAssignedColor();
    
    public void setKaraokeEffectType(KaraokeEffectType fx);
    public KaraokeEffectType getKaraokeEffectType();
    
    public ImageIcon getIcon();
    
    public void setStartingLayer(int layer);
    public int getStartingLayer();
    
    public void incrementsLayer(boolean value);
    public boolean mustIncrementLayer();
    
    public void setLayer(int layer);
    public int getLayer();
    
    public void applyByLine(List<AssEvent> events);
    
    public void applyByBlock(List<AssEvent> events);
    
}
