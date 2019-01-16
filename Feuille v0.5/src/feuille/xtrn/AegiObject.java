/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.xtrn;

import feuille.xtrn.XtrnLib.EffectType;
import feuille.xtrn.XtrnLib.ModeType;
import feuille.xtrn.XtrnLib.TreatmentType;

/**
 *
 * @author Yves
 */
public class AegiObject {
    
    private String name = "Effect #0";
    private String authors = "Unknown";
    private String description = "No description";
    private String commands = null;
    
    private EffectType ef = EffectType.Normal;
    private ModeType mode = ModeType.Normal;
    private TreatmentType tr = TreatmentType.Line;
    
    public AegiObject(){
        
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setAuthors(String authors){
        this.authors = authors;
    }
    
    public String getAuthors(){
        return authors;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setCommands(String commands){
        this.commands = commands;
    }
    
    public String getCommands(){
        return commands;
    }
    
    public void setEffectType(EffectType ef){
        this.ef = ef;
    }
    
    public void setEffectType(String effect){
        this.ef = EffectType.valueOf(effect);
    }
    
    public EffectType getEffectType(){
        return ef;
    }
    
    public void setModeType(ModeType mode){
        this.mode = mode;
    }
    
    public void setModeType(String mode){
        this.mode = ModeType.valueOf(mode);
    }
    
    public ModeType getModeType(){
        return mode;
    }
    
    public void setTreatmentType(TreatmentType tr){
        this.tr = tr;
    }
    
    public void setTreatmentType(String tr){
        this.tr = TreatmentType.valueOf(tr);
    }
    
    public TreatmentType getTreatmentType(){
        return tr;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
}
