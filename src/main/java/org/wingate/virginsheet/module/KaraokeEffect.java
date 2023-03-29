/*
 * Copyright (C) 2023 util2
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
package org.wingate.virginsheet.module;

import java.awt.Color;

/**
 *
 * @author util2
 */
public abstract class KaraokeEffect implements KaraokeEffectInterface {
    
    protected String name = "Unknown effect";
    protected String author = "";
    protected Color color = Color.red;
    protected KaraokeEffectType karaokeEffectType = KaraokeEffectType.Unknown;
    protected int layer = 0;
    protected int startingLayer = 0;
    protected boolean incrementsLayer = false;

    public KaraokeEffect() {
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void assignColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getAssignedColor() {
        return color;
    }

    @Override
    public void setKaraokeEffectType(KaraokeEffectType fx) {
        karaokeEffectType = fx;
    }

    @Override
    public KaraokeEffectType getKaraokeEffectType() {
        return karaokeEffectType;
    }

    @Override
    public void setStartingLayer(int layer) {
        startingLayer = layer;
    }

    @Override
    public int getStartingLayer() {
        return startingLayer;
    }

    @Override
    public void incrementsLayer(boolean value) {
        incrementsLayer = value;
    }

    @Override
    public boolean mustIncrementLayer() {
        return incrementsLayer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public int getLayer() {
        return layer;
    }
    
}
