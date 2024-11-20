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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author util2
 */
public class AssInfos {
    private String title;
    private String originalScript;
    private String originalTranslation;
    private String originalEditing;
    private String originalTiming;
    private String synchPoint;
    private String scriptUpdatedBy;
    private String updateDetails;
    private String scriptType;
    private String collisions;
    private int playResX;
    private int playResY;
    private int playDepth;
    private float timer;
    private int wrapStyle;

    public AssInfos(String title, String originalScript,
            String originalTranslation, String originalEditing,
            String originalTiming, String synchPoint, String scriptUpdatedBy,
            String updateDetails, String scriptType, String collisions,
            int playResX, int playResY, int playDepth, float timer,
            int wrapStyle) {
        this.title = title;
        this.originalScript = originalScript;
        this.originalTranslation = originalTranslation;
        this.originalEditing = originalEditing;
        this.originalTiming = originalTiming;
        this.synchPoint = synchPoint;
        this.scriptUpdatedBy = scriptUpdatedBy;
        this.updateDetails = updateDetails;
        this.scriptType = scriptType;
        this.collisions = collisions;
        this.playResX = playResX;
        this.playResY = playResY;
        this.playDepth = playDepth;
        this.timer = timer;
        this.wrapStyle = wrapStyle;
    }

    public AssInfos() {
        title = "Original Feuille script";
        originalScript = null;
        originalTranslation = null;
        originalEditing = null;
        originalTiming = null;
        synchPoint = null;
        scriptUpdatedBy = null;
        updateDetails = null;
        scriptType = "v4.00+";
        collisions = null;
        playResX = 800;
        playResY = 600;
        playDepth = -1;
        timer = 100f;
        wrapStyle = -1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalScript() {
        return originalScript;
    }

    public void setOriginalScript(String originalScript) {
        this.originalScript = originalScript;
    }

    public String getOriginalTranslation() {
        return originalTranslation;
    }

    public void setOriginalTranslation(String originalTranslation) {
        this.originalTranslation = originalTranslation;
    }

    public String getOriginalEditing() {
        return originalEditing;
    }

    public void setOriginalEditing(String originalEditing) {
        this.originalEditing = originalEditing;
    }

    public String getOriginalTiming() {
        return originalTiming;
    }

    public void setOriginalTiming(String originalTiming) {
        this.originalTiming = originalTiming;
    }

    public String getSynchPoint() {
        return synchPoint;
    }

    public void setSynchPoint(String synchPoint) {
        this.synchPoint = synchPoint;
    }

    public String getScriptUpdatedBy() {
        return scriptUpdatedBy;
    }

    public void setScriptUpdatedBy(String scriptUpdatedBy) {
        this.scriptUpdatedBy = scriptUpdatedBy;
    }

    public String getUpdateDetails() {
        return updateDetails;
    }

    public void setUpdateDetails(String updateDetails) {
        this.updateDetails = updateDetails;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getCollisions() {
        return collisions;
    }

    public void setCollisions(String collisions) {
        this.collisions = collisions;
    }

    public int getPlayResX() {
        return playResX;
    }

    public void setPlayResX(int playResX) {
        this.playResX = playResX;
    }

    public int getPlayResY() {
        return playResY;
    }

    public void setPlayResY(int playResY) {
        this.playResY = playResY;
    }

    public int getPlayDepth() {
        return playDepth;
    }

    public void setPlayDepth(int playDepth) {
        this.playDepth = playDepth;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    public int getWrapStyle() {
        return wrapStyle;
    }

    public void setWrapStyle(int wrapStyle) {
        this.wrapStyle = wrapStyle;
    }
    
    public static void writeInfos(AssInfos inf, PrintWriter pw){
        pw.println("Title: " + inf.getTitle());
        if(inf.getOriginalScript() != null){
            pw.println("Original Script: " + inf.getOriginalScript());
        }
        if(inf.getOriginalTranslation() != null){
            pw.println("Original Translation: " + inf.getOriginalTranslation());
        }
        if(inf.getOriginalEditing() != null){
            pw.println("Original Editing: " + inf.getOriginalEditing());
        }
        if(inf.getOriginalTiming() != null){
            pw.println("Original Timing: " + inf.getOriginalTiming());
        }
        if(inf.getSynchPoint() != null){
            pw.println("Synch Point: " + inf.getSynchPoint());
        }
        if(inf.getScriptUpdatedBy() != null){
            pw.println("Script Updated By: " + inf.getScriptUpdatedBy());
        }
        if(inf.getUpdateDetails() != null){
            pw.println("Update Details: " + inf.getUpdateDetails());
        }
        pw.println("ScriptType: " + inf.getScriptType());
        if(inf.getCollisions() != null){
            pw.println("Collisions: " + inf.getCollisions());
        }
        pw.println("PlayResX: " + inf.getPlayResX());
        pw.println("PlayResY: " + inf.getPlayResY());
        if(inf.getPlayDepth() != -1){
            pw.println("PlayDepth: " + inf.getPlayDepth());
        }
        if(inf.getTimer() != 100f){
            pw.println("Timer: " + inf.getTimer());
        }
        pw.println("WrapStyle: " + inf.getWrapStyle());
    }
    
    public static void read(AssInfos inf, BufferedReader br) throws IOException{
        String line = br.readLine();

        if(line != null){
            if(line.startsWith("Title: ")) inf.setTitle(line.substring("Title: ".length()));
            if(line.startsWith("Original Script: ")) inf.setOriginalScript(line.substring("Original Script: ".length()));
            if(line.startsWith("Original Translation: ")) inf.setOriginalTranslation(line.substring("Original Translation: ".length()));
            if(line.startsWith("Original Editing: ")) inf.setOriginalEditing(line.substring("Original Editing: ".length()));
            if(line.startsWith("Original Timing: ")) inf.setOriginalTiming(line.substring("Original Timing: ".length()));
            if(line.startsWith("Synch Point: ")) inf.setSynchPoint(line.substring("Synch Point: ".length()));
            if(line.startsWith("Script Updated By: ")) inf.setScriptUpdatedBy(line.substring("Script Updated By: ".length()));
            if(line.startsWith("Update Details: ")) inf.setUpdateDetails(line.substring("Update Details: ".length()));
            if(line.startsWith("ScriptType: ")) inf.setScriptType(line.substring("ScriptType: ".length()));
            if(line.startsWith("Collisions: ")) inf.setCollisions(line.substring("Collisions: ".length()));
            if(line.startsWith("PlayResX: ")) inf.setPlayResX(Integer.parseInt(line.substring("PlayResX: ".length())));
            if(line.startsWith("PlayResY: ")) inf.setPlayResY(Integer.parseInt(line.substring("PlayResY: ".length())));
            if(line.startsWith("PlayDepth: ")) inf.setPlayDepth(Integer.parseInt(line.substring("PlayDepth: ".length())));
            if(line.startsWith("Timer: ")) inf.setTimer(Float.parseFloat(line.substring("Timer: ".length())));
            if(line.startsWith("WrapStyle: ")) inf.setWrapStyle(Integer.parseInt(line.substring("WrapStyle: ".length())));
        }
        
    }
}
