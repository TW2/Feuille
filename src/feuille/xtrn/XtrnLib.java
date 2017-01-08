/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.xtrn;

/**
 *
 * @author Yves
 */
public class XtrnLib {
    
    public XtrnLib(){
        
    }
    
    public enum EffectType{
        Normal("Normal"),
        Periodic("Periodic"),
        Random("Random"),
        Symmetric("Symmetric");
        
        String name;

        EffectType(String name) {
            this.name = name;
        }
        
        @Override
        public String toString(){
            return name;
        }
        
        public EffectType getEffectType(String n){
            if(n.equalsIgnoreCase("Normal")){ 
                return Normal; 
            }else if(n.equalsIgnoreCase("Periodic")){ 
                return Periodic; 
            }else if(n.equalsIgnoreCase("Random")){ 
                return Random; 
            }else if(n.equalsIgnoreCase("Symmetric")){ 
                return Symmetric; 
            }else{ 
                return Normal; 
            }
        }
    }
    
    public enum ModeType{
        Normal("Normal"),
        Character("Character");
        
        String name;

        ModeType(String name) {
            this.name = name;
        }
        
        @Override
        public String toString(){
            return name;
        }
        
        public ModeType getModeType(String n){
            if(n.equalsIgnoreCase("Normal")){ 
                return Normal; 
            }else if(n.equalsIgnoreCase("Character")){ 
                return Character;
            }else{ 
                return Normal; 
            }
        }
    }
    
    public enum TreatmentType{
        Line("Line"),
        Syllable("Syllable"),
        Character("Character");
        
        String name;

        TreatmentType(String name) {
            this.name = name;
        }
        
        @Override
        public String toString(){
            return name;
        }
        
        public TreatmentType getTreatmentType(String n){
            if(n.equalsIgnoreCase("Line")){ 
                return Line; 
            }else if(n.equalsIgnoreCase("Syllable")){ 
                return Syllable;
            }else if(n.equalsIgnoreCase("Character")){ 
                return Character;
            }else{ 
                return Line; 
            }
        }
    }
    
}
