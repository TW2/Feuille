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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author util2
 */
public class AssLanguageAccessory extends JPanel implements PropertyChangeListener {
    
    private final JLabel lblLanguage;
    private final JComboBox cbLanguage;
    private final DefaultComboBoxModel modelLanguage;
    
    private final JLabel lblDetectedLanguages;
    private final JScrollPane spDetectedLanguages;
    private final JList listDetected;
    private final DefaultListModel modelDetectedLanguages;

    public AssLanguageAccessory() {
        ISO_3166 sys = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        
        lblLanguage = new JLabel("Main language :");
        lblDetectedLanguages = new JLabel("Detected languages :");
        
        cbLanguage = new JComboBox();
        modelLanguage = new DefaultComboBoxModel();
        
        cbLanguage.setModel(modelLanguage);
        
        for(ISO_3166 iso : ISO_3166.values()){
            modelLanguage.addElement(iso.getCountry());
        }
        
        cbLanguage.setSelectedItem(sys.getCountry());
        
        listDetected = new JList();
        modelDetectedLanguages = new DefaultListModel();
        listDetected.setModel(modelDetectedLanguages);
        spDetectedLanguages = new JScrollPane(listDetected);
        spDetectedLanguages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spDetectedLanguages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        setLayout(null);
        
        setPreferredSize(new java.awt.Dimension(170, HEIGHT));
        
        lblLanguage.setLocation(10, 0);
        lblLanguage.setSize(160, 20);
        add(lblLanguage);
        
        cbLanguage.setLocation(10, 25);
        cbLanguage.setSize(160, 20);
        add(cbLanguage);
        
        lblDetectedLanguages.setLocation(10, 50);
        lblDetectedLanguages.setSize(160, 20);
        add(lblDetectedLanguages);
        
        spDetectedLanguages.setLocation(10, 75);
        spDetectedLanguages.setSize(160, 115);
        add(spDetectedLanguages);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)){
            modelDetectedLanguages.clear();
            File file = (File)evt.getNewValue();
            if(file != null && file.getName().endsWith(".ass")){
                AssLanguagesDetection ld = new AssLanguagesDetection(file.getPath());
                for(ISO_3166 iso : ld.getLanguages()){
                    modelDetectedLanguages.addElement(iso.getCountry());
                }
                if(ld.getLanguages().isEmpty()){
                    ISO_3166 sys = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
                    modelDetectedLanguages.addElement(sys);
                }
            }
        }
    }
    
}
