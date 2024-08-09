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
package org.wingate.feuille.m.afm.karaoke.io;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXAbstract;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXCode;

/**
 *
 * @author util2
 */
public class CodeAccessory extends JPanel implements PropertyChangeListener {
    
    private final JList list;
    private final JScrollPane scrollPane;
    
    private final DefaultListModel listModel;

    public CodeAccessory() {
        list = new JList();
        scrollPane = new JScrollPane(list);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        listModel = new DefaultListModel();
        list.setModel(listModel);
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(javax.swing.JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)){
            listModel.clear();
            File file = (File)evt.getNewValue();
            if(file != null && file.getName().endsWith(".sfx")){
                SFXAbstract sfx = EffectsIO.load(file.getPath());
                for(SFXCode code : sfx.getCodes()){
                    listModel.addElement(code);
                }
            }
        }
    }
    
}
