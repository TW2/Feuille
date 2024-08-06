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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXAbstract;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXCode;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class TemplateListCellRenderer extends DefaultListCellRenderer {
    
    private final ImageIcon nothingImage;
    private final ImageIcon jsImage;
    private final ImageIcon pyImage;
    private final ImageIcon rbImage;
    private final ImageIcon luaImage;
    private final ImageIcon moreImage;
    
    private final JPanel mainPanel;
    private final JPanel secondPanel;
    private final JLabel lblLogo;
    private final JLabel lblName;
    private final JLabel lblHelper;

    public TemplateListCellRenderer() {
        nothingImage = new ImageIcon(getClass().getResource("/images/16 penta - gris.png"));
        jsImage = new ImageIcon(getClass().getResource("/images/16 penta - jaune.png"));
        pyImage = new ImageIcon(getClass().getResource("/images/16 penta - vert.png"));
        rbImage = new ImageIcon(getClass().getResource("/images/16 penta - rouge.png"));
        luaImage = new ImageIcon(getClass().getResource("/images/16 penta - bleu-nuit.png"));
        moreImage = new ImageIcon(getClass().getResource("/images/16 penta.png"));        
        
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());
        
        secondPanel = new JPanel();
        secondPanel.setBackground(Color.white);
        secondPanel.setLayout(new BorderLayout());
        
        lblLogo = new JLabel(nothingImage);
        lblName = new JLabel();
        lblHelper = new JLabel();
        
        // Add main panel to root element
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        
        // Add logo and second panel to main panel
        mainPanel.add(lblLogo, BorderLayout.WEST);
        mainPanel.add(secondPanel, BorderLayout.CENTER);
        
        // Add name and helper to second panel
        secondPanel.add(lblName, BorderLayout.CENTER);
        secondPanel.add(lblHelper, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        if(value instanceof SFXAbstract sfx){
            // Nom du template
            lblName.setText(String.format("%s (%s)", sfx.getHumanName(), sfx.getName()));
            
            // Helper
            lblHelper.setText(sfx.getHelper());
            
            // Couleur du logo (si scripts couleur sinon gris)
            if(sfx.getCodes().isEmpty() == false){
                boolean isSame = true;
                CodeType type = sfx.getCodes().getFirst().getCodeType();
                for(SFXCode c : sfx.getCodes()){
                    if(c.getCodeType().equals(type) == false){
                        isSame = false;
                        break;
                    }
                }
                
                if(isSame == true){
                    switch(type){
                        case JavaScript -> { lblLogo.setIcon(jsImage); }
                        case Lua -> { lblLogo.setIcon(luaImage); }
                        case Python -> { lblLogo.setIcon(pyImage); }
                        case Ruby -> { lblLogo.setIcon(rbImage); }
                    }
                }else{
                    lblLogo.setIcon(moreImage);
                }                
            }else{
                // Pas de code >> gris
                lblLogo.setIcon(nothingImage);
            }
            
            // Couleur de navigation/contrôle
            if(isSelected){
                mainPanel.setBackground(DrawColor.corn_flower_blue.getColor());
                secondPanel.setBackground(DrawColor.corn_flower_blue.getColor());
                lblName.setForeground(Color.white);
                lblHelper.setForeground(Color.white);
            }else{
                if(cellHasFocus){
                    mainPanel.setBackground(DrawColor.light_gray.getColor(0.5f));
                    secondPanel.setBackground(DrawColor.light_gray.getColor(0.5f));
                    lblName.setForeground(Color.black);
                    lblHelper.setForeground(Color.black);
                }else{
                    mainPanel.setBackground(Color.white);
                    secondPanel.setBackground(Color.white);
                    lblName.setForeground(Color.black);
                    lblHelper.setForeground(DrawColor.dark_green.getColor());
                }                
            }
        }
        
        return mainPanel;
    }
    
}
