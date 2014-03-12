/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.audio;

/**
 *
 * @author The Wingate 2940
 */
public class Tag {
    String syllable = "";
    int x = 0;

    public Tag(){
        //Nothing, simple call.
    }

    public Tag(String syllable, int x){
        this.syllable = syllable;
        this.x = x;
    }

    public void setSyllable(String syllable){
        this.syllable = syllable;
    }

    public void setX(int x){
        this.x = x;
    }

    public String getSyllable(){
        return syllable;
    }

    public int getX(){
        return x;
    }

    public boolean mouseOver(int xpos){
        if(xpos==x |
                xpos==x-1 | xpos==x-2 |
                xpos==x+1 | xpos==x+2 ){
            return true;
        }
        return false;
    }
    
    public boolean isSet(){
        if(syllable.isEmpty() && x==0){
            return false;
        }
        return true;
    }
    
    public void reset(){
        syllable = "";
        x = 0;
    }
}
