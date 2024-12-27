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
package org.wingate.feuille.subs.ass;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author util2
 */
public class AssRenderer {
    
    private final ASS ass;
    private Dimension size;

    public AssRenderer(ASS ass) {
        this.ass = ass;
        init();
    }
    
    private void init(){
        size = new Dimension(ass.getInfos().getPlayResX(), ass.getInfos().getPlayResY());
    }

    public AssRenderer(ASS ass, Dimension size) {
        this.ass = ass;
        this.size = size;
    }
    
    public BufferedImage getSubsAtTime(double msTime){
        BufferedImage image = new BufferedImage(
                size.width,
                size.height,
                BufferedImage.TYPE_INT_ARGB
        );        
        Graphics2D g = image.createGraphics();
        
        for(AssEvent ev : ass.getEvents()){
            if(ev == null || ev.getType() != AssEvent.Type.Dialogue){
                continue;
            }
            if(ev.getStart().getMsTime() <= msTime && msTime < ev.getEnd().getMsTime()){
                // Blend
//                AssFxDefault def = new AssFxDefault(ass, ev, msTime);
//                def.getImage(g, msTime);
            }
        }
        
        g.dispose();        
        return image;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public ASS getAss() {
        return ass;
    }
}
