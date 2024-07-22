/*
 * Copyright (C) 2022 util2
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
package org.wingate.feuille.ass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author util2
 */
public class AssTime {
    
    private static long msStart;
    private static long msStop;

    public AssTime() {
        this(0L, 0L);
    }

    public AssTime(long msStart, long msStop) {
        AssTime.msStart = msStart;
        AssTime.msStop = msStop;
    }

    public AssTime(String start, String stop) {        
        AssTime.msStart = parseASS(start);
        AssTime.msStop = parseASS(stop);
    }

    public long getMsStart() {
        return msStart;
    }

    public void setMsStart(long msStart) {
        AssTime.msStart = msStart;
    }

    public long getMsStop() {
        return msStop;
    }

    public void setMsStop(long msStop) {
        AssTime.msStop = msStop;
    }
    
    public boolean isBetween(long msCurrent){
        return msStart <= msCurrent && msCurrent < msStop;
    }
    
    public long getExtendStart(long ms){
        return msStart - ms;
    }
    
    public long getExtendStop(long ms){
        return msStop + ms;
    }
    
    public long getInnerStart(long ms){
        return msStart + ms;
    }
    
    public long getInnerStop(long ms){
        return msStop - ms;
    }
    
    public long getDuration(){
        return Math.max(msStart, msStop) - Math.min(msStart, msStop);
    }
    
    public String getDurationString(){
        return getTimeString(Math.max(msStart, msStop) - Math.min(msStart, msStop));
    }
    
    public long[] getDurations(int part){
        long times = Math.round(Math.floor((double)getDuration() / (double)part));
        long last = getDuration() - (times * (part - 1));
        long s[] = new long[part];
        for(int i=0; i<part-1; i++){
            s[i] = times;
        }
        s[part-1] = last;
        return s;
    }
    
    public long[] getStarts(long[] durations){
        long[] s = new long[durations.length];
        long v = 0;
        for(int i=0; i<s.length; i++){
            s[i] = v;
            v += durations[i];
        }
        return s;
    }
    
    public long[] getEnds(long[] durations){
        long[] s = new long[durations.length];
        long v = 0;
        for(int i=0; i<s.length; i++){
            v += durations[i];
            s[i] = v;            
        }
        return s;
    }
    
    public static String getDiff(String start, String end){
        return getDiff(parseASS(start), parseASS(end));
    }
    
    public static String getDiff(long start, long end){
        long diff = Math.max(start, end) - Math.min(start, end);
        return getTimeString(diff);
    }
    
    public static long parseASS(String t){
        Pattern p = Pattern.compile("(\\d+):(\\d{2}):(\\d{2}).{1}(\\d{2})");
        Matcher m = p.matcher(t);
        
        if(m.find()){
            int hs = Integer.parseInt(m.group(1));
            int ms = Integer.parseInt(m.group(2));
            int ss = Integer.parseInt(m.group(3));
            int cs = Integer.parseInt(m.group(4)) * 10;
            
            return cs + ss * 1_000L + ms * 60 * 1_000L + hs * 60L * 60L * 1000L;
        }
        return 0L;
    }
    
    public static String getTimeString(long ms){
        int hour = (int)(ms / 3600000);
        int min = (int)((ms - 3600000 * hour) / 60000);
        int sec = (int)((ms - 3600000 * hour - 60000 * min) / 1000);
        int cs = (int)(ms - 3600000 * hour - 60000 * min - 1000 * sec) / 10;
        
        return String.format(
                "%s:%s:%s.%s",
                hour,
                min < 10 ? "0" + min : min,
                sec < 10 ? "0" + sec : sec,
                cs < 10 ? "0" + cs : cs
        );
    }    
    
    public String getStartString(){
        return getTimeString(msStart);
    }
    
    public void setStartString(String str){
        msStart = parseASS(str);
    }
    
    public String getEndString(){
        return getTimeString(msStop);
    }
    
    public void setEndString(String str){
        msStop = parseASS(str);
    }
}
