/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.analysis.lib;

/**
 *
 * @author The Wingate 2940
 */
public class LineChangeObject {
    
    private SentenceState sentenceChange = SentenceState.Unknown;
    private StyleState styleChange = StyleState.Unchanged;
    private NameState nameChange = NameState.Unchanged;
    private TimeState timeChange = TimeState.Unchanged;
    
    public LineChangeObject(){
        
    }
    
    public enum SentenceState{
        Unknown, Double, Deleted, Added;
    }
    
    public enum StyleState{
        Unchanged, Double, Old, New;
    }
    
    public enum NameState{
        Unchanged, Double, Old, New;
    }
    
    public enum TimeState{
        Unchanged, Double, Old, New, Shift;
    }
    
    public void changeSentenceState(SentenceState ss){
        sentenceChange = ss;
    }
    
    public SentenceState getSentenceState(){
        return sentenceChange;
    }
    
    public void changeStyleState(StyleState ss){
        styleChange = ss;
    }
    
    public StyleState getStyleState(){
        return styleChange;
    }
    
    public void changeNameState(NameState ns){
        nameChange = ns;
    }
    
    public NameState getNameState(){
        return nameChange;
    }
    
    public void changeTimeState(TimeState ts){
        timeChange = ts;
    }
    
    public TimeState getTimeState(){
        return timeChange;
    }
    
}
