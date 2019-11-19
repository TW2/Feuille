/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class Params {
    
    private String name = "";
    private Object parameter = null;
    private boolean canBeInactive = false;
    private boolean isInactive = false;
    private State state = State.Normal;
    private SuperString sstring = new SuperString();
    private Object backup = null;
    
    public Params(){
        
    }
    
    public Params(String name, Object parameter, boolean canBeInactive){
        this.name = name;
        this.parameter = parameter;
        this.canBeInactive = canBeInactive;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    /** Storage space of main object - Set */
    public void setParameter(Object parameter){
        this.parameter = parameter;
    }
    
    /** Storage space of main object - Get */
    public Object getParameter(){
        return parameter;
    }
    
    public void setBeInactive(boolean canBeInactive){
        this.canBeInactive = canBeInactive;
    }
    
    public boolean getBeInactive(){
        return canBeInactive;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    public void setInactive(boolean isInactive){
        this.isInactive = isInactive;
    }
    
    public boolean isInactive(){
        return isInactive;
    }
    
    public enum State{
        Normal, SuperString;
    }
    
    public State getState(){
        return state;
    }
    
    public void setState(State state){
        this.state = state;
    }
    
    /** Storage space of readable object - Set */
    public void setSuperString(SuperString ss){
        sstring = ss;
    }
    
    /** Storage space of readable object - Get */
    public SuperString getSuperString(){
        return sstring;
    }
    
    /** Storage space for not in use object - Set */
    public void setBackup(Object backup){
        this.backup = backup;
    }
    
    /** Storage space for not in use object - Get */
    public Object getBackup(){
        return backup;
    }
}
