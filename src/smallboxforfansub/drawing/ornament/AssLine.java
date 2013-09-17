/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.ornament;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import smallboxforfansub.karaoke.lib.Time;

/**
 *
 * @author The Wingate 2940
 */
public class AssLine {
    
    String head = null;
    String body = null;
    
    public AssLine(){
        
    }
    
    public AssLine(String assline){
        Pattern p = Pattern.compile("([^:]+:\\s[a-zA-Z=]*\\d+," +
                            "\\d+:\\d+:\\d+.\\d+," +
                            "\\d+:\\d+:\\d+.\\d+," +
                            "[^,]+,[^,]*," +
                            "\\d+,\\d+,\\d+,[^,]*),(.*)");
        Matcher m = p.matcher(assline);
        m.find();
        head = m.group(1);
        body = m.group(2);
    }
    
    public AssLine(String head, String body){
        this.head = head;
        this.body = body;
    }
    
    @Override
    public String toString(){
        if(head!=null && body!=null){
            return head + "," + body;
        }
        return "";
    }
    
    public String getHead(){
        return head;
    }
    
    public String getBody(){
        return body;
    }
    
    public String try_addToBody(String param){
        if(param.startsWith("{")==false){ param = "{"+param; }
        if(param.endsWith("}")==false){ param = param+"}"; }
        if(body.contains(param)==false){
            return param + body;
        }
        return body;
    }
    
    public String changeLayer(String head, String firstLayer){
        String[] hdata = head.split(",");
        if(hdata.length==9){
            return "Dialogue: "+ firstLayer + "," + hdata[1] + ","
                    + hdata[2] + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + "," + hdata[8];
        }else{
            return "Dialogue: "+ firstLayer + "," + hdata[1] + ","
                    + hdata[2] + "," + hdata[3] + "," + hdata[4] + ","
                    + hdata[5] + "," + hdata[6] + "," + hdata[7] + ",";
        }
    }
    
    /** <p>Change the style name of the given head.<br />
     * Change le nom du style de l'entête.</p> */
    public String changeStyle(String head, String styleName){
        String[] hdata = head.split(",");
        if(hdata.length==9){
            return hdata[0] + "," + hdata[1] + "," + hdata[2] +
                    "," + styleName + "," + hdata[4] + "," + hdata[5] +
                    "," + hdata[6] + "," + hdata[7] + "," + hdata[8];
        }else{
            return hdata[0] + "," + hdata[1] + "," + hdata[2] +
                    "," + styleName + "," + hdata[4] + "," + hdata[5] +
                    "," + hdata[6] + "," + hdata[7] + ",";
        }
    }
    
    public String getMillisecondsStart(){
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
        Matcher m = p.matcher(head);
        
        Time start = new Time();
        Time end = new Time();
        
        boolean endTime = false;
        while(m.find()){
            if (endTime == false){
                start.setHours(Integer.parseInt(m.group(1)));
                start.setMinutes(Integer.parseInt(m.group(2)));
                start.setSeconds(Integer.parseInt(m.group(3)));
                start.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }else{
                end.setHours(Integer.parseInt(m.group(1)));
                end.setMinutes(Integer.parseInt(m.group(2)));
                end.setSeconds(Integer.parseInt(m.group(3)));
                end.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }
            endTime = true;
        }
        
        return Long.toString(start.toMillisecondsTime(start));
    }
    
    public String getMillisecondsEnd(){
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
        Matcher m = p.matcher(head);
        
        Time start = new Time();
        Time end = new Time();
        
        boolean endTime = false;
        while(m.find()){
            if (endTime == false){
                start.setHours(Integer.parseInt(m.group(1)));
                start.setMinutes(Integer.parseInt(m.group(2)));
                start.setSeconds(Integer.parseInt(m.group(3)));
                start.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }else{
                end.setHours(Integer.parseInt(m.group(1)));
                end.setMinutes(Integer.parseInt(m.group(2)));
                end.setSeconds(Integer.parseInt(m.group(3)));
                end.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }
            endTime = true;
        }
        
        return Long.toString(end.toMillisecondsTime(end));
    }
    
    public String getMillisecondsDur(){
        //regex en vb.net "(?<hh>\d+):(?<mm>\d+):(?<ss>\d+).(?<cc>\d+)"
        //regex en java "(\\d+):(\\d+):(\\d+).(\\d+)"
        Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
        Matcher m = p.matcher(head);
        
        Time start = new Time();
        Time end = new Time();
        
        boolean endTime = false;
        while(m.find()){
            if (endTime == false){
                start.setHours(Integer.parseInt(m.group(1)));
                start.setMinutes(Integer.parseInt(m.group(2)));
                start.setSeconds(Integer.parseInt(m.group(3)));
                start.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }else{
                end.setHours(Integer.parseInt(m.group(1)));
                end.setMinutes(Integer.parseInt(m.group(2)));
                end.setSeconds(Integer.parseInt(m.group(3)));
                end.setMilliseconds(Integer.parseInt(m.group(4))*10);
            }
            endTime = true;
        }
        
        return Long.toString(end.toMillisecondsTime(end)-start.toMillisecondsTime(start));
    }
    
}
