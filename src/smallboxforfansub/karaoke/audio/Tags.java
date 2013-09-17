/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.audio;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of tag that represent the syllables of the ASS karaoke.
 * @author The Wingate 2940
 */
public class Tags {
    
    private String sentence = "";
    private int firstX = 0;
    private int lastX = 0;
    private List<Tag> arlTags = new ArrayList<Tag>();
    
    public Tags(){
        //Nothing, simple call.
    }
    
    public Tags(int firstX, int lastX){
        this.firstX = firstX;
        this.lastX = lastX;
    }
    
    public Tags(String sentence, int firstX, int lastX){
        this.sentence = sentence;               
        this.firstX = firstX;
        this.lastX = lastX;
        
        if(sentence.contains("/")==true){            
            splitSentence();
        }
    }
    
    public Tags(List<Tag> arlTags, String sentence, int firstX, int lastX){
        this.arlTags = arlTags;
        this.sentence = sentence;
        this.firstX = firstX;
        this.lastX = lastX;
    }
    
    public void setSentence(String sentence){
        this.sentence = sentence;
        
        if(sentence.contains("/")==true){            
            splitSentence();
        }
    }
    
    public void setFirstX(int firstX){
        this.firstX = firstX;
    }
    
    public void setLastX(int lastX){
        this.lastX = lastX;
    }
    
    private void splitSentence(){
        String sTable[] = sentence.split("/");
        int diffX = lastX-firstX;
        int unitX = diffX/sTable.length;
        int j;
        for(int i=0;i<sTable.length;i++){
            j = firstX + unitX*(i+1);
            if(j>lastX){ j=lastX; }
            Tag tag = new Tag(sTable[i], j);
            arlTags.add(tag);
        }
    }
    
    private void splitKaraokeSentence(){
        
       
    }
    
    public List<Tag> getTags(){
        return arlTags;
    }
    
    public int getStart(){
        return firstX;
    }
    
    public int getEnd(){
        return lastX;
    }
    
    public boolean isSet(){
        if(sentence.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    public void reset(){
        sentence = "";
        firstX = 0;
        lastX = 0;
        arlTags = new ArrayList<Tag>();
    }
    
    /** Get the closest tag on the left of the <i>value</i>.*/
    public Tag getLeftTag(int value){
        Tag rtag = null;
        for(Tag tag : arlTags){
            if(value-tag.getX()>0){
                rtag=tag;
            }
        }
        return rtag;
    }
    
    /** Get the closest tag on the right of the <i>value</i>.*/
    public Tag getRightTag(int value){
        for(Tag tag : arlTags){
            if(value-tag.getX()<0){
                return tag;
            }
        }
        return null;
    }
    
    public Tag getLastTag(){
        return arlTags.get(arlTags.size()-1);
    }
}
