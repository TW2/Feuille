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
package org.wingate.feuille.m.afm.karaoke.sfx;

import java.util.List;
import org.wingate.feuille.ass.AssEvent;
import org.wingate.feuille.m.afm.karaoke.BiEvent;

/**
 *
 * @author util2
 */
public interface SFXInterface {
    public String getName();
    
    public String getHumanName();
    public void setHumanName(String humanName);
    
    public List<SFXCode> getCodes();
    public void setCodes(List<SFXCode> codes);
    
    public List<String> getTemplates();
    public void setTemplates(List<String> templates);
    
    public void forOneLine(BiEvent input);
    public void forFewLines(List<BiEvent> input);
    
    public List<AssEvent> doJob(AssEvent input);
}
