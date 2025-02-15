/*
 * Copyright (C) 2024 util2
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
package org.wingate.feuille.subs.ass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author util2
 */
public class AssTime implements Cloneable {
    private double msTime;

    public AssTime(double msTime) {
        this.msTime = msTime;
    }

    public AssTime() {
        msTime = 0d;
    }

    public double getMsTime() {
        return msTime;
    }

    public void setMsTime(double msTime) {
        this.msTime = msTime;
    }
    
    public static AssTime create(String rawtime){
        if(rawtime.contains(":") == false && rawtime.split("\\.").length <= 2){
            return new AssTime(Double.parseDouble(rawtime));
        }else if(rawtime.matches("\\d+.{1}\\d{2}.{1}\\d{2}.{1}\\d{2}")){
            Pattern p = Pattern.compile("(\\d+).{1}(\\d+).{1}(\\d+).{1}(\\d+)");
            Matcher m = p.matcher(rawtime);
            if(m.find()){
                int hh = Integer.parseInt(m.group(1));
                int mm = Integer.parseInt(m.group(2));
                int ss = Integer.parseInt(m.group(3));
                int cs = Integer.parseInt(m.group(4));
                
                int ms = 10 * cs;
                
                double time = 
                        (double)ms +
                        (double)ss * 1000d +
                        (double)mm * 1000d * 60d +
                        (double)hh * 1000d * 60d * 60d;
                
                return new AssTime(time);
            }
        }else if(rawtime.matches("\\d+.{1}\\d{2}.{1}\\d{2}.{1}\\d{3}")){
            Pattern p = Pattern.compile("(\\d+).{1}(\\d+).{1}(\\d+).{1}(\\d+)");
            Matcher m = p.matcher(rawtime);
            if(m.find()){
                int hh = Integer.parseInt(m.group(1));
                int mm = Integer.parseInt(m.group(2));
                int ss = Integer.parseInt(m.group(3));
                int ms = Integer.parseInt(m.group(4));
                
                double time = 
                        (double)ms +
                        (double)ss * 1000d +
                        (double)mm * 1000d * 60d +
                        (double)hh * 1000d * 60d * 60d;
                
                return new AssTime(time);
            }
        }
        // Default
        return new AssTime();
    }
    
    public String toAss(){
        int hh = (int)(msTime / 3600000d);
        int mm = (int)((msTime - 3600000d * hh) / 60000d);
        int ss = (int)((msTime - 3600000d * hh - 60000d * mm) / 1000d);
        int cs = (int)((msTime - 3600000d * hh - 60000d * mm - 1000d * ss) / 10d);
        
        return String.format("%s:%s:%s.%s",
                hh,
                mm < 10 ? "0" + mm : mm,
                ss < 10 ? "0" + ss : ss,
                cs < 10 ? "0" + cs : cs
        );
    }
    
    public static String getAssDuration(AssTime start, AssTime end){
        double dur = 
                Math.max(start.getMsTime(), end.getMsTime()) -
                Math.min(start.getMsTime(), end.getMsTime());
        return new AssTime(dur).toAss();
    }
    
    public static AssTime getMsDuration(AssTime start, AssTime end){
        double dur = 
                Math.max(start.getMsTime(), end.getMsTime()) -
                Math.min(start.getMsTime(), end.getMsTime());
        return new AssTime(dur);
    }
    
    public void addToTime(AssTime t){
        msTime += t.getMsTime();
    }
    
    public void substractToTime(AssTime t){
        msTime -= t.getMsTime();
    }
    
    public void multiplyToTime(double value){
        msTime *= value;
    }
    
    public void divideToTime(double value){
        msTime = value == 0d ? msTime : msTime / value;
    }

    @Override
    public String toString() {
        return toAss();
    }

    @Override
    public AssTime clone() {
        try {
            return (AssTime) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
