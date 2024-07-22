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
package org.wingate.feuille.m.afm.karaoke;

import javax.swing.table.DefaultTableCellRenderer;
import org.wingate.feuille.ass.AssEvent;

/**
 *
 * @author util2
 */
public class KaraokeAssEventRenderer extends DefaultTableCellRenderer {

    private AssEvent event;

    public KaraokeAssEventRenderer() {
        super();
    }
    
    @Override
    protected void setValue(Object value) {
        if(event == null){
            event = new AssEvent();
        }
        if(value == null){
            setText("");
        }else{
            if(value instanceof AssEvent ev){
                setText(ev.getText());
            }else{
                setText("");
            }
        }
    }
    
}
