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

import java.awt.image.BufferedImage;
import java.util.List;
import org.wingate.feuille.ass.ASS;
import org.wingate.feuille.ass.AssEvent;

/**
 *
 * @author util2
 */
public class SubtitlesProvider implements Runnable {
    
    private ASS ass = null;
    private final int videoWidth;
    private final int videoHeight;    
    
    private AssEvent available = null;
    private double prepareSeconds = -1d;
    private double lastSeconds = 0d;
    private final double unit = 3d;
    
    private Thread th = null;
    private volatile boolean active = false;
    
    private final List<AssEvent> events;

    public SubtitlesProvider(String path, int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        ass = ASS.Read(path);
        events = ass.getEvents();
    }
    
    public BufferedImage require(double seconds){
        BufferedImage img = null;
        
        // On note ce que souhaite obtenir l'application
        lastSeconds = seconds;
        
        for(AssEvent ao : events){
            // Time class use milliseconds only
            if(ao.getTime().isBetween(Math.round(seconds * 1000d))){
                available = ao;
                break;
            }
        }
        
        if(available != null){
            img = available.getImage();
            available = null;
        }
        
        return img;
    }
    
    public void update(double seconds){        
        prepareSeconds = seconds + unit;
    }
    
    public void startThread(){
        stopThread();
        active = true;
        th = new Thread(this);
        th.start();
    }
    
    public void pause(boolean value){
        active = !value;
    }
    
    public void stopThread(){
        if(th != null){
            if(th.isAlive() == true || th.isInterrupted() == false){
                active = false;
                th.interrupt();
                th = null;
            }
        }
    }

    @Override
    public void run() {
        while(true){
            if(active){
                // On augmente la valeur de 3 secondes
                // si la dernière seconde est supérieure
                // à la valeur de recherche.
                while(prepareSeconds < lastSeconds){
                    update(lastSeconds);
                }
                
                int counter = 0;
                
                // On remplit la liste avec des données futures.
                for(AssEvent ao : events){
                    if(ao.getTime().isBetween(Math.round(prepareSeconds * 1000d)) && ao.getImage() == null){
                        ao.setImage(SubtitlesRenderer.get(videoWidth, videoHeight, prepareSeconds, ao));                        
                    }
                    // On en profite pour compter les images
                    if(ao.getImage() != null){
                        counter++;
                        // Si le compteur atteint les 50 images en même temps
                        // on arrête la boucle, sinon on autorise à poursuivre.
                        if(counter < 50){
                            prepareSeconds += .025d;
                        }else{
                            pause(true);
                            break;
                        }
                    }
                }
                
            }else{
                int counter = 0;
                
                // On allège la liste (suppression d'images)
                for(AssEvent ao : events){
                    if(ao.getTime().getMsStop() < Math.round(lastSeconds * 1000d)){
                        ao.clearImage();
                    }
                    // On en profite pour compter les images
                    if(ao.getImage() != null){
                        counter++;
                        // Si le compteur atteint les 50 images en même temps
                        // on arrête la boucle, sinon on autorise à poursuivre.
                        if(counter < 50){
                            pause(false);
                        }else{
                            pause(true);
                        }
                    }
                }
            }
        }
    }

    public double getUnit() {
        return unit;
    }
    
}
