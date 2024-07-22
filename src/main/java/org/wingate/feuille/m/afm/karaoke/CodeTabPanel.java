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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author util2
 */
public class CodeTabPanel extends JPanel {
    
    private final JLabel lblIcon = new JLabel();
    private final JLabel lblText = new JLabel();
    private final JLabel lblClose = new JLabel();
    
    private int indexOfTab;
    private final JTabbedPane tabp;

    public CodeTabPanel(CodeType codeType, JTabbedPane tabp, int indexOfTab, String title) {
        this.tabp = tabp;
        this.indexOfTab = indexOfTab;
        
        setOpaque(false);
        setLayout(new BorderLayout(4, 0));
        add(lblIcon, BorderLayout.WEST);
        add(lblText, BorderLayout.CENTER);
        add(lblClose, BorderLayout.EAST);
        
        lblClose.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                _mouseClicked(e);
            }
        });
        
        switch(codeType){
            case JavaScript -> {
                ImageIcon ii1 = new ImageIcon(getClass().getResource("/images/js-icon-small.png"));
                ImageIcon ii2 = new ImageIcon(getClass().getResource("/images/cross-small.png"));
                lblIcon.setIcon(ii1);
                lblText.setText(title);
                lblClose.setIcon(ii2);
            }
            case Python -> {
                ImageIcon ii1 = new ImageIcon(getClass().getResource("/images/py-icon-small.png"));
                ImageIcon ii2 = new ImageIcon(getClass().getResource("/images/cross-small.png"));
                lblIcon.setIcon(ii1);
                lblText.setText(title);
                lblClose.setIcon(ii2);
            }
            case Ruby -> {
                ImageIcon ii1 = new ImageIcon(getClass().getResource("/images/rb-icon-small.png"));
                ImageIcon ii2 = new ImageIcon(getClass().getResource("/images/cross-small.png"));
                lblIcon.setIcon(ii1);
                lblText.setText(title);
                lblClose.setIcon(ii2);
            }
            case Lua -> {
                ImageIcon ii1 = new ImageIcon(getClass().getResource("/images/lua-icon-small.png"));
                ImageIcon ii2 = new ImageIcon(getClass().getResource("/images/cross-small.png"));
                lblIcon.setIcon(ii1);
                lblText.setText(title);
                lblClose.setIcon(ii2);
            }
        }
    }
    
    public void _mouseClicked(MouseEvent e) {
        tabp.removeTabAt(indexOfTab);
    }
    
    public void changeIndex(int indexOfTab){
        this.indexOfTab = indexOfTab;
    }
    
    public void changeTitle(String title){
        lblText.setText(title);
    }
    
    public String getTitle(){
        return lblText.getText();
    }
}
