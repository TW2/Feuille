/*
 * Copyright (C) 2021 util2
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
package org.wingate.feuille.m.ygg.audio;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author util2
 */
public class SignalData {
    
    private final File media;
    private final int width;
    private final int height;
    private final File tempWaveImageFile;
    private final File tempSpecImageFile;
    
    private final String ffmpeg;
    private final String ffprobe;

    public SignalData(File media, int wPart, int hPart) {
        this.media = media;
        width = wPart;
        height = hPart;
        tempWaveImageFile = new File("tempW.png");
        tempSpecImageFile = new File("tempS.png");
        
        ffmpeg = getClass().getResource("/conf/ffmpeg.exe").getPath();
        ffprobe = getClass().getResource("/conf/ffprobe.exe").getPath();
    }
    
    public BufferedImage getWaveForm(long msAreaStart, long msAreaStop){
        String start = Long.toString(msAreaStart) + "ms";
        String stop = Long.toString(msAreaStop) + "ms";
        
        if(tempWaveImageFile.exists()) tempWaveImageFile.delete();
        
        try{
            ProcessBuilder pb = new ProcessBuilder(
                    ffmpeg,
                    "-ss", start,
                    "-to", stop, 
                    "-i", media.getPath(), 
                    "-filter_complex", "showwavespic=s=" + width + "x" + height + ":colors=Blue|BlueViolet",
                    "-frames:v", "1",
                    tempWaveImageFile.getPath());
            Process p = pb.start();
            p.waitFor();
        }catch(IOException | InterruptedException ex){
            Logger.getLogger(SignalData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BufferedImage image = null;
        
        if(tempWaveImageFile.exists()){
            boolean waitFor = true;
            while(waitFor){
                try {
                    image = ImageIO.read(tempWaveImageFile);
                    waitFor = false;
                } catch (IOException ex) {
                    System.err.println("An image can't be read in write phase!");
                }
            }

            tempWaveImageFile.delete();
        }
        
        return image;
    }
    
    public BufferedImage getSpectrogram(long msAreaStart, long msAreaStop){
        String start = Long.toString(msAreaStart) + "ms";
        String stop = Long.toString(msAreaStop) + "ms";
        
        if(tempSpecImageFile.exists() == true) tempSpecImageFile.delete();
        
        try{
            ProcessBuilder pb = new ProcessBuilder(
                    ffmpeg,
                    "-ss", start,
                    "-to", stop, 
                    "-i", media.getPath(), 
                    "-lavfi",
                    "showspectrumpic=s=" + width + "x" + height + ":legend=0:scale=cbrt",
                    tempSpecImageFile.getPath());
            Process p = pb.start();
            p.waitFor();
        }catch(IOException | InterruptedException ex){
            Logger.getLogger(SignalData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BufferedImage image = null;
        
        if(tempSpecImageFile.exists()){
            boolean waitFor = true;
            while(waitFor){
                try {
                    image = ImageIO.read(tempSpecImageFile);
                    waitFor = false;
                } catch (IOException ex) {
                    System.err.println("An image can't be read in write phase!");
                }
            }

            tempSpecImageFile.delete();
        }
        
        return image;
    }
    
    /**
     * Get frames per second (fps)
     * @return fps found or 24000/1001 if not found
     */
    public double getFps(){
        double fps = 24000 / 1001;
        
        Process p = null;
        
        try{
            ProcessBuilder pb = new ProcessBuilder(
                    ffprobe,
                    "-v", "error",
                    "-select_streams", "v:0",
                    "-show_entries", "stream=avg_frame_rate",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    media.getPath()
            );
            pb.redirectErrorStream(true);
            p = pb.start();
        }catch(IOException ex){
            Logger.getLogger(SignalData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(p != null){
            String line = null;
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                line = reader.readLine();
            } catch (IOException ex) {        
                Logger.getLogger(SignalData.class.getName()).log(Level.SEVERE, null, ex);
            }
            p.destroy();
            
            if(line != null && line.isEmpty() == false){
                if(line.contains("/")){
                    String[] t = line.split("/");
                    double numerator = Double.parseDouble(t[0]);
                    double denominator = Double.parseDouble(t[1]);
                    fps = numerator / (denominator > 0 ? denominator : 1);
                }else{
                    fps = Double.parseDouble(line);
                }
            }
        }
        
        return fps;
    }
    
    /**
     * Get keyframes which key is mstime and value is frame index of video
     * @return key value keyframes
     */
    public Map<Long, Integer> getKeyFrames(){        
        Map<Long, Integer> keyframes = new HashMap<>();
        double fps = getFps();
        
        Process p = null;
        
        try{
            ProcessBuilder pb = new ProcessBuilder(
                    ffprobe,
                    "-i", media.getPath(),
                    "-select_streams", "v", 
                    "-show_frames",
                    "-show_entries", "frame=pict_type",
                    "-loglevel", "error");
            pb.redirectErrorStream(true);
            p = pb.start();
        }catch(IOException ex){
            Logger.getLogger(SignalData.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(p != null){
            int counter = 0;
            String line;
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))){
//                System.out.println("fps: " + fps);
                while ((line = reader.readLine()) != null) {
                    if(line.contains("pict_type=I")){
                        long ms = Math.round(counter / fps * 1000d);
                        keyframes.put(ms, counter);
//                        System.out.println(String.format("%dms - #%d", ms, counter));
                    }
                    if(line.contains("pict_type=") == true){
                        counter++;
                    }
                }
            } catch (IOException ex) {        
                Logger.getLogger(SignalData.class.getName()).log(Level.SEVERE, null, ex);
            }
            p.destroy();
        }
        return keyframes;
    }
    
    public long getMsDuration(){
        long dur = -1L;
        
        Process p = null;
        
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    ffprobe,
                    "-i", media.getPath(),
                    "-show_entries", "format=duration",
                    "-v", "quiet",
                    "-of", "csv=\"p=0\"");
            pb.redirectErrorStream(true);
            p = pb.start();
        } catch (IOException ex) {
            Logger.getLogger(SignalData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(p != null){
            String line = null;
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                line = reader.readLine();
            } catch (IOException ex) {        
                Logger.getLogger(SignalData.class.getName()).log(Level.SEVERE, null, ex);
            }
            p.destroy();
            
            if(line == null) return -1L;
            
            double secs = Double.parseDouble(line);
            dur = Math.round(secs * 1000d);
        }
        
        return dur;
    }
}