/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.codeeditor.lib;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;

/**
 *
 * @author The Wingate 2940
 */
public class AutoCompletion extends DefaultListModel {
    
    private List<String> choices = new ArrayList<>();
    private JEditorPane ep;
    
    
    public AutoCompletion(){
        choices.add("free");
        choices.add("freedom");
        choices.add("france");
        choices.add("french");
    }
    
    public void clearEntries(){
        clear();
    }
    
    public void addEntry(String s){
        addElement(s);
    }
    
    public void setAutoCompletionList(String word){
        if(word.isEmpty()==false){
            clearEntries();
            for(String entry : choices){
                if(entry.startsWith(word)){
                    addEntry(entry);
                }
            }      
        }else{
            clearEntries();
        }
    }
    
}
