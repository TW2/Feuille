/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.listener;

import feuille.grafx.AShape;
import feuille.grafx.GCubic;
import feuille.grafx.GLine;
import feuille.grafx.GMoveM;
import feuille.grafx.GMoveN;
import feuille.grafx.GQuadratic;
import feuille.grafx.GSpline;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author util2
 */
public class EditionCursorEvent {
    
    Point cursor = null;
    List<Integer> uniqueIDList = new ArrayList<>();
    List<Change> changedList = new ArrayList<>();
    List<AShape> shapes = new ArrayList<>(); 
    
    public enum Change {
        None, Start, End, Cp, Cp1, Cp2, CPx;
    }

    public EditionCursorEvent(Point cursor, List<AShape> shapes) {
        init(cursor, shapes);
    }
    
    private void init(Point cursor, List<AShape> shapes){
        this.cursor = cursor;
        this.shapes = shapes;
        
        for(int i=0; i<shapes.size(); i++){
            AShape sh = shapes.get(i);
            
            if(sh instanceof GMoveM){
                GMoveM gMoveM = (GMoveM)sh;
                
                if(isClosePoint(cursor, gMoveM.getStartPoint())) {
                    uniqueIDList.add(gMoveM.getUniqueID());
                    changedList.add(Change.Start);
                }else if(isClosePoint(cursor, gMoveM.getEndPoint())){
                    uniqueIDList.add(gMoveM.getUniqueID());
                    changedList.add(Change.End);
                }
            }
            
            if(sh instanceof GMoveN){
                GMoveN gMoveN = (GMoveN)sh;
                
                if(isClosePoint(cursor, gMoveN.getStartPoint())) {
                    uniqueIDList.add(gMoveN.getUniqueID());
                    changedList.add(Change.Start);
                }else if(isClosePoint(cursor, gMoveN.getEndPoint())){
                    uniqueIDList.add(gMoveN.getUniqueID());
                    changedList.add(Change.End);
                }
            }
            
            if(sh instanceof GLine){
                GLine gLine = (GLine)sh;
                
                if(isClosePoint(cursor, gLine.getStartPoint())) {
                    uniqueIDList.add(gLine.getUniqueID());
                    changedList.add(Change.Start);
                }else if(isClosePoint(cursor, gLine.getEndPoint())){
                    uniqueIDList.add(gLine.getUniqueID());
                    changedList.add(Change.End);
                }
            }
            
            if(sh instanceof GCubic){
                GCubic gCubic = (GCubic)sh;
                
                if(isClosePoint(cursor, gCubic.getStartPoint())) {
                    uniqueIDList.add(gCubic.getUniqueID());
                    changedList.add(Change.Start);
                }else if(isClosePoint(cursor, gCubic.getEndPoint())){
                    uniqueIDList.add(gCubic.getUniqueID());
                    changedList.add(Change.End);
                }else if(isClosePoint(cursor, gCubic.getControlPoints().get(0))){
                    uniqueIDList.add(gCubic.getUniqueID());
                    changedList.add(Change.Cp1);
                }else if(isClosePoint(cursor, gCubic.getControlPoints().get(1))){
                    uniqueIDList.add(gCubic.getUniqueID());
                    changedList.add(Change.Cp2);
                }
            }
            
            if(sh instanceof GQuadratic){
                GQuadratic gQuad = (GQuadratic)sh;
                
                if(isClosePoint(cursor, gQuad.getStartPoint())) {
                    uniqueIDList.add(gQuad.getUniqueID());
                    changedList.add(Change.Start);
                }else if(isClosePoint(cursor, gQuad.getEndPoint())){
                    uniqueIDList.add(gQuad.getUniqueID());
                    changedList.add(Change.End);
                }else if(isClosePoint(cursor, gQuad.getControlPoints().get(0))){
                    uniqueIDList.add(gQuad.getUniqueID());
                    changedList.add(Change.Cp);
                }
            }
            
            if(sh instanceof GSpline){
                GSpline gSpline = (GSpline)sh;
                
                
            }
        }
    }

    public List<Integer> getUniqueIDList() {
        return uniqueIDList;
    }

    public Point getCursor() {
        return cursor;
    }

    public void setCursor(Point cursor) {
        this.cursor = cursor;
    }
    
    private boolean isClosePoint(Point2D a, Point2D b){
        return a.distance(b) < 10d;
    }

    public List<Change> getWhatHasChanged() {
        return changedList;
    }
    
    public List<AShape> getNow(boolean clone){
        List<AShape> clones = new ArrayList<>();
        
        for(AShape sh : shapes){
            if(getUniqueIDList().contains(sh.getUniqueID()) == true){
                                
                if(sh instanceof GMoveM){
                    GMoveM gMoveM = clone == true ? ((GMoveM)sh).getClone() : (GMoveM)sh;

                    if(getUniqueIDList().size() == 2 && 
                            getWhatHasChanged().get(0) == Change.End &&
                            getWhatHasChanged().get(1) == Change.Start){
                        if(getUniqueIDList().get(0) == sh.getUniqueID()){
                            gMoveM.setEndPoint(cursor);
                            clones.add(gMoveM);
                        }else{
                            gMoveM.setStartPoint(cursor);
                            clones.add(gMoveM);
                        }
                    }else if(getWhatHasChanged().contains(Change.Start)) {
                        gMoveM.setStartPoint(cursor);
                        clones.add(gMoveM);
                    }else if(getWhatHasChanged().contains(Change.End)){
                        gMoveM.setEndPoint(cursor);
                        clones.add(gMoveM);
                    }
                }

                if(sh instanceof GMoveN){
                    GMoveN gMoveN = clone == true ? ((GMoveN)sh).getClone() : (GMoveN)sh;

                    if(getUniqueIDList().size() == 2 && 
                            getWhatHasChanged().get(0) == Change.End &&
                            getWhatHasChanged().get(1) == Change.Start){
                        if(getUniqueIDList().get(0) == sh.getUniqueID()){
                            gMoveN.setEndPoint(cursor);
                            clones.add(gMoveN);
                        }else{
                            gMoveN.setStartPoint(cursor);
                            clones.add(gMoveN);
                        }
                    }else if(getWhatHasChanged().contains(Change.Start)) {
                        gMoveN.setStartPoint(cursor);
                        clones.add(gMoveN);
                    }else if(getWhatHasChanged().contains(Change.End)){
                        gMoveN.setEndPoint(cursor);
                        clones.add(gMoveN);
                    }
                }

                if(sh instanceof GLine){
                    GLine gLine = clone == true ? ((GLine)sh).getClone() : (GLine)sh;

                    if(getUniqueIDList().size() == 2 && 
                            getWhatHasChanged().get(0) == Change.End &&
                            getWhatHasChanged().get(1) == Change.Start){
                        if(getUniqueIDList().get(0) == sh.getUniqueID()){
                            gLine.setEndPoint(cursor);
                            clones.add(gLine);
                        }else{
                            gLine.setStartPoint(cursor);
                            clones.add(gLine);
                        }
                    }else if(getWhatHasChanged().contains(Change.Start)) {
                        gLine.setStartPoint(cursor);
                        clones.add(gLine);
                    }else if(getWhatHasChanged().contains(Change.End)){
                        gLine.setEndPoint(cursor);
                        clones.add(gLine);
                    }
                }

                if(sh instanceof GCubic){
                    GCubic gCubic = clone == true ? ((GCubic)sh).getClone() : (GCubic)sh;

                    if(getUniqueIDList().size() == 2 && 
                            getWhatHasChanged().get(0) == Change.End &&
                            getWhatHasChanged().get(1) == Change.Start){
                        if(getUniqueIDList().get(0) == sh.getUniqueID()){
                            gCubic.setEndPoint(cursor);
                            clones.add(gCubic);
                        }else{
                            gCubic.setStartPoint(cursor);
                            clones.add(gCubic);
                        }
                    }else if(getWhatHasChanged().contains(Change.Start)) {
                        gCubic.setStartPoint(cursor);
                        clones.add(gCubic);
                    }else if(getWhatHasChanged().contains(Change.End)){
                        gCubic.setEndPoint(cursor);
                        clones.add(gCubic);
                    }else if(getWhatHasChanged().contains(Change.Cp1)){
                        gCubic.getControlPoints().get(0).setLocation(cursor);
                        clones.add(gCubic);
                    }else if(getWhatHasChanged().contains(Change.Cp2)){
                        gCubic.getControlPoints().get(1).setLocation(cursor);
                        clones.add(gCubic);
                    }
                }

                if(sh instanceof GQuadratic){
                    GQuadratic gQuad = clone == true ? ((GQuadratic)sh).getClone() : (GQuadratic)sh;

                    if(getUniqueIDList().size() == 2 && 
                            getWhatHasChanged().get(0) == Change.End &&
                            getWhatHasChanged().get(1) == Change.Start){
                        if(getUniqueIDList().get(0) == sh.getUniqueID()){
                            gQuad.setEndPoint(cursor);
                            clones.add(gQuad);
                        }else{
                            gQuad.setStartPoint(cursor);
                            clones.add(gQuad);
                        }
                    }else if(getWhatHasChanged().contains(Change.Start)) {
                        gQuad.setStartPoint(cursor);
                        clones.add(gQuad);
                    }else if(getWhatHasChanged().contains(Change.End)){
                        gQuad.setEndPoint(cursor);
                        clones.add(gQuad);
                    }else if(getWhatHasChanged().contains(Change.Cp)){
                        gQuad.getControlPoints().get(0).setLocation(cursor);
                        clones.add(gQuad);
                    }
                }

                if(sh instanceof GSpline){
                    GSpline gSpline = clone == true ? ((GSpline)sh).getClone() : (GSpline)sh;


                }
            }
        }
        
        return clones;
    }
    
}
