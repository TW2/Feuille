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
package org.wingate.feuille.m.ygg.ocr;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.wingate.feuille.m.ContainerInternalFrame;
import org.wingate.feuille.m.ElementAbstract;

/**
 *
 * @author util2
 */
public class TesseractElement extends ElementAbstract<TesseractPanel> {
    
    public TesseractElement() {
        name = "OCR";
        panel = new TesseractPanel();
    }

    @Override
    public void setupMenu(String friendlyName, ContainerInternalFrame cp) {
        ImageIcon iiOpenDoc = new ImageIcon(getClass().getResource("/images/16 folder.png"));
        ImageIcon iiCorner1 = new ImageIcon(getClass().getResource("/images/16 corner 1.png"));
        ImageIcon iiCorner3 = new ImageIcon(getClass().getResource("/images/16 corner 3.png"));
        ImageIcon iiCorner9 = new ImageIcon(getClass().getResource("/images/16 corner 9.png"));
        ImageIcon iiCorner7 = new ImageIcon(getClass().getResource("/images/16 corner 7.png"));
        ImageIcon iiClose = new ImageIcon(getClass().getResource("/images/16 cross-small.png"));
        
        menu = cp.getFileMenu();
        menu.setText(String.format("%s (%s)", name, friendlyName));
        
        JMenuItem miOpenDoc = new JMenuItem("Open video...");
        miOpenDoc.addActionListener((listener)->{
            int z = panel.getOpenVideoChooser().showOpenDialog(panel);
            if(z == JFileChooser.APPROVE_OPTION){
                String path = panel.getOpenVideoChooser().getSelectedFile().getPath();
                panel.setVideoFile(path);                
            }
        });
        miOpenDoc.setIcon(iiOpenDoc);
        menu.add(miOpenDoc);
        
        menu.add(new JSeparator());
        
        JMenuItem miToLeftTop = new JMenuItem("Put to the corner 7");
        miToLeftTop.addActionListener((listener)->{
            cp.getContainersDesktopPane().toCorner7(this);
            cp.getElementAbstract().setCorner(7);
        });
        miToLeftTop.setIcon(iiCorner7);
        menu.add(miToLeftTop);
        
        JMenuItem miToRightTop = new JMenuItem("Put to the corner 9");
        miToRightTop.addActionListener((listener)->{
            cp.getContainersDesktopPane().toCorner9(this);
            cp.getElementAbstract().setCorner(9);
        });
        miToRightTop.setIcon(iiCorner9);
        menu.add(miToRightTop);
        
        JMenuItem miToRightBottom = new JMenuItem("Put to the corner 3");
        miToRightBottom.addActionListener((listener)->{
            cp.getContainersDesktopPane().toCorner3(this);
            cp.getElementAbstract().setCorner(3);
        });
        miToRightBottom.setIcon(iiCorner3);
        menu.add(miToRightBottom);
        
        JMenuItem miToLeftBottom = new JMenuItem("Put to the corner 1");
        miToLeftBottom.addActionListener((listener)->{
            cp.getContainersDesktopPane().toCorner1(this);
            cp.getElementAbstract().setCorner(1);
        });
        miToLeftBottom.setIcon(iiCorner1);
        menu.add(miToLeftBottom);
        
        menu.add(new JSeparator());
        
        JMenuItem miToLeft = new JMenuItem("Left by 1");
        miToLeft.addActionListener((listener)->{
            cp.getContainersDesktopPane().toLeft(this);
        });
        menu.add(miToLeft);
        
        JMenuItem miToRight = new JMenuItem("Right by 1");
        miToRight.addActionListener((listener)->{
            cp.getContainersDesktopPane().toRight(this);
        });
        menu.add(miToRight);
        
        JMenuItem miToTop = new JMenuItem("Top by 1");
        miToTop.addActionListener((listener)->{
            cp.getContainersDesktopPane().toUp(this);
        });
        menu.add(miToTop);
        
        JMenuItem miToBottom = new JMenuItem("Bottom by 1");
        miToBottom.addActionListener((listener)->{
            cp.getContainersDesktopPane().toDown(this);
        });
        menu.add(miToBottom);
        
        menu.add(new JSeparator());
        
        JMenuItem miClose = new JMenuItem("Close element");
        miClose.addActionListener((listener)->{
            cp.getContainersDesktopPane().removeElementAbstract(this);
            try{
                panel.dispose();
            }catch(Exception exc){
                System.err.println("Error: " + exc.getMessage());
            }
        });
        miClose.setIcon(iiClose);
        menu.add(miClose);
    }
    
}
