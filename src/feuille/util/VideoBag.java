/*
 * Copyright (C) 2018 util2
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
package feuille.util;

import java.awt.image.BufferedImage;

/**
 *
 * @author util2
 */
public class VideoBag {
    
    private int frameNumber = 0;
    private int width = 0;
    private int height = 0;
    private BufferedImage image = null;
    private double fps = 0d;
    private long time = 0L;
    private long pts = 0L;
    private long duration = 0L;
    private long nbFrames = 0;

    public VideoBag() {
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public double getFps() {
        return fps;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setPts(long pts) {
        this.pts = pts;
    }

    public long getPts() {
        return pts;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setNbFrames(long nbFrames) {
        this.nbFrames = nbFrames;
    }

    public long getNbFrames() {
        return nbFrames;
    }
    
    
    
}
