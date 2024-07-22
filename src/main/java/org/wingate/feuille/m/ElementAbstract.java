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
package org.wingate.feuille.m;

import javax.swing.JMenu;

/**
 *
 * @author util2
 * @param <P> a transformed JPanel
 */
public abstract class ElementAbstract<P> implements ElementInterface {
    
    protected String name;
    protected String friendlyName;
    protected P panel;
    protected JMenu menu;
    
    // Matrice X
    protected int firstWCase = 0, lastWCase = 1;
    
    // Matrice Y
    protected int firstHCase = 0, lastHCase = 1;
    
    // Position numérique
    private int corner = 7;

    public ElementAbstract() {
        name = "Unknown element";
        panel = null;
        menu = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public P getPanel() {
        return panel;
    }

    public void setPanel(P panel) {
        this.panel = panel;
    }

    public JMenu getMenu() {
        return menu;
    }

    public void setMenu(JMenu menu) {
        this.menu = menu;
    }

    public int getFirstWCase() {
        return firstWCase;
    }

    public void setFirstWCase(int firstWCase) {
        this.firstWCase = firstWCase;
    }

    public int getLastWCase() {
        return lastWCase;
    }

    public void setLastWCase(int lastWCase) {
        this.lastWCase = lastWCase;
    }

    public int getFirstHCase() {
        return firstHCase;
    }

    public void setFirstHCase(int firstHCase) {
        this.firstHCase = firstHCase;
    }

    public int getLastHCase() {
        return lastHCase;
    }

    public void setLastHCase(int lastHCase) {
        this.lastHCase = lastHCase;
    }

    public int getCorner() {
        return corner;
    }

    public void setCorner(int corner) {
        this.corner = corner;
    }
    
}
