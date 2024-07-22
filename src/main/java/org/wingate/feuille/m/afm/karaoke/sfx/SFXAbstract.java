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
package org.wingate.feuille.m.afm.karaoke.sfx;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.wingate.feuille.ass.AssEvent;

/**
 *
 * @author util2
 */
public abstract class SFXAbstract implements SFXInterface {
    
    protected String name;
    protected List<SFXCode> codes = new ArrayList<>();

    public SFXAbstract() {
        name = "Unknown template";
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public List<SFXCode> getCodes() {
        return codes;
    }

    @Override
    public void setCodes(List<SFXCode> codes) {
        this.codes = codes;
    }
    
    @Override
    public List<AssEvent> forOneLine(AssEvent input) {
        return doJob(input);
    }

    @Override
    public List<AssEvent> forFewLines(List<AssEvent> input) {
        final List<AssEvent> output = new ArrayList<>();
        
        for(AssEvent ev : input){
            output.addAll(doJob(ev));
        }
        
        return output;
    }
    
    // Remplace phKaraoke
    public List<SFXSyllable> getSyllable(AssEvent event){
        final List<SFXSyllable> syls = new ArrayList<>();
        
        String str = event.getText();
        Pattern p = Pattern.compile("\\\\[kK]?[fo]*(?<trigger>\\d+)[^\\}]*\\}(?<syl>[^\\{]*)");
        Matcher m = p.matcher(str);
        
        long msStart = 0L, msMid, msEnd, msDur;
        long msEvStart = event.getTime().getMsStart();
        long msEvEnd = event.getTime().getMsStop();
        long msEvDur = event.getTime().getDuration();
        
        while(m.find()){
            int csDur = Integer.parseInt(m.group("trigger"));
            msDur = csDur * 10;
            msMid = msStart + msDur / 2;
            msEnd = msStart + msDur;
            
            SFXSyllable s = new SFXSyllable(
                    msStart,        // long msStart
                    msMid,          // long msMid
                    msEnd,          // long msEnd
                    msDur,          // long msDuration
                    msEvStart,      // long msEventStart
                    msEvEnd,        // long msEventEnd
                    msEvDur,        // long msEventDuration
                    m.group("syl")
            );
            
            msStart += msDur;
            
            syls.add(s);
        }
        
        return syls;
    }
    
    // Remplace phReplaceParameters
    public String replaceParams(String template, List<SFXSyllable> syls){
        String t, output = "";
        
        for(SFXSyllable syl : syls){
            t = template;
            
            // Millisecondes
            t = t.replace("%sK", Long.toString(syl.getMsStart())); // Syllabe
            t = t.replace("%mK", Long.toString(syl.getMsMid())); // Syllabe
            t = t.replace("%eK", Long.toString(syl.getMsEnd())); // Syllabe
            t = t.replace("%dK", Long.toString(syl.getMsDuration())); // Syllabe
            
            // Centièmes de seconde
            t = t.replace("%csK", Long.toString(syl.getMsStart() / 10)); // Syllabe
            t = t.replace("%cmK", Long.toString(syl.getMsMid() / 10)); // Syllabe
            t = t.replace("%ceK", Long.toString(syl.getMsEnd() / 10)); // Syllabe
            t = t.replace("%cdK", Long.toString(syl.getMsDuration() / 10)); // Syllabe
            
            // TODO t = t.replace("%lsK", Long.toString(syl.getMsStart())); // Lettre
            // TODO t = t.replace("%lmK", Long.toString(syl.getMsMid())); // Lettre
            // TODO t = t.replace("%leK", Long.toString(syl.getMsEnd())); // Lettre
            // TODO t = t.replace("%ldK", Long.toString(syl.getMsDuration())); // Lettre
            
            t = t.replace("%ssK", Long.toString(syl.getMsEventStart())); // Phrase
            t = t.replace("%seK", Long.toString(syl.getMsEventEnd())); // Phrase
            t = t.replace("%smK", Long.toString(syl.getMsEventDuration())); // Phrase
            
            t = t.replace("%syllable", syl.getSyllable());
            // TODO LOOP t = t.replace("%letter", syl.getLetter());
            
            output += t;
        }
        
        return output;
    }
    
}
