/*
 * Copyright (C) 2019 util2
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
package feuille.panel;

import feuille.MainFrame;
import feuille.exception.AudioVideoErrorException;
import feuille.io.Event;
import feuille.listener.FrameAdapter;
import feuille.listener.FrameEvent;
import feuille.util.Time;
import feuille.util.VideoBag;
import feuille.video.Reader;
import feuille.video.SubRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author util2
 */
public class VideoPane extends JPanel {
    
    private final Reader reader = new Reader();
    private boolean ready = false; // Status of reader
    private VideoBag vb = new VideoBag();

    public VideoPane() {
        init();
    }
    
    private void init(){
        reader.addFrameListener(new FrameAdapter() {
            @Override
            public void FrameChanged(FrameEvent e) {
                vb = e.getBag();
                repaint();
            }
        });
    }
    
    public void defineSize(int width, int height){
        setSize(width, height);
    }
    
    public void setPath(String path){
        File file = new File(path);
        if(file.exists() == true){
            reader.setFile(file, Reader.PlayerOption.Video);
            ready = true;
        }        
    }
    
    public void freeReader(){
        reader.freeAllResources();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        if(ready == true && vb.getWidth() > 0 && vb.getHeight() > 0){
            // Get the width (to adapt to ratio and max panel size)
            int width = getHeight() * vb.getWidth() / vb.getHeight();
            
            // Create a new image to render and resize
            BufferedImage bi = new BufferedImage(width, getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bi.createGraphics();
            
            // Get a new frame
            BufferedImage img = vb.getImage();
            
            // Adapt it
            int dx1 = (getWidth() - width) / 2;
            int dy1 = 0;
            int dx2 = dx1 + width;
            int dy2 = getHeight();
            int sx1 = 0;
            int sy1 = 0;
            int sx2 = vb.getWidth();
            int sy2 = vb.getHeight();
            g2d.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
            
            // Get subtitle
            BufferedImage sub = SubRenderer.getSubLayer(vb);
            
            // Draw sutitle
            g2d.drawImage(sub, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
            
            // Draw infos
            g2d.setFont(new Font("Ubuntu", Font.BOLD, 16));
            g2d.setColor(Color.yellow);
            g2d.drawString(vb.getWidth() + " x " + vb.getHeight(), 10, 20);
            g2d.drawString("NbFrames : " + vb.getNbFrames() + " ; Current : " + vb.getFrameNumber(), 10, 40);
            g2d.drawString("Duration : " + vb.getDuration() + " ; Current : " + vb.getTime(), 10, 60);
            g2d.drawString("FPS : " + vb.getFps() + " ; PTS : " + vb.getPts(), 10, 80);
            
            // Draw all            
            g.drawImage(bi, 0, 0, null);
            
            // Dispose image graphics2D
            g2d.dispose();
        }
    }
    
    private List<Event> getSubs(int frame, double fps){
        List<Event> evts = new ArrayList<>();
        
        // With the frame and the fps, let's get a time
        // frame / fps => seconds but we want milliseconds (*1000)
        Time current = Time.create(Math.round(frame/fps*1000));
        
        System.out.println("curtime: "+current.toASSTime()+"; frame: "+frame+"; fps: "+fps);
        
        // Now search for time that include current
        // If any add them to the list
        for(Event evt : MainFrame.getScriptEvents()){
            if(Time.toMillisecondsTime(evt.getStartTime()) <= Time.toMillisecondsTime(current)
                    && Time.toMillisecondsTime(evt.getEndTime()) > Time.toMillisecondsTime(current)){
                evts.add(evt);
            }
        }
        
        return evts;
    }
    
    public void play(){
        try {
            reader.startMedia();
        } catch (AudioVideoErrorException ex) {
            Logger.getLogger(VideoPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pause(){
        reader.pauseMedia();
    }
    
    public void stop(){
        reader.stopMedia();
    }
}
