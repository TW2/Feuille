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
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.wingate.feuille.ass.AssEvent;
import org.wingate.feuille.m.afm.karaoke.BiEvent;

/**
 *
 * @author util2
 */
public abstract class SFXAbstract implements SFXInterface {
    
    protected String name;
    protected String humanName;
    protected String helper;
    protected List<SFXCode> codes = new ArrayList<>();
    protected List<String> templates = new ArrayList<>();

    public SFXAbstract() {
        name = "Unknown template";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHumanName() {
        return humanName;
    }

    @Override
    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    @Override
    public String getHelper() {
        return helper;
    }

    @Override
    public void setHelper(String helper) {
        this.helper = helper;
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
    public List<String> getTemplates() {
        return templates;
    }

    @Override
    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }
    
    @Override
    public List<BiEvent> forOneLine(BiEvent input) {
        final List<BiEvent> bevts = new ArrayList<>();
        
        input.getTransformedAssEvents().clear();
        List<AssEvent> evts = doJob(input.getOriginalAssEvent());
        input.getTransformedAssEvents().addAll(evts);        
        
        for(AssEvent ev : evts){
            BiEvent b = new BiEvent(false, ev);
            bevts.add(b);
        }
        
        return bevts;
    }

    @Override
    public List<BiEvent> forFewLines(List<BiEvent> input) {
        final List<BiEvent> bevts = new ArrayList<>();
        
        for(BiEvent bev : input){
            bev.getTransformedAssEvents().clear();
            List<AssEvent> evts = doJob(bev.getOriginalAssEvent());
            bev.getTransformedAssEvents().addAll(evts);
            
            for(AssEvent ev : evts){
                BiEvent b = new BiEvent(false, ev);
                bevts.add(b);
            }
        }
        
        return bevts;
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
        
        StringBuilder sentence = new StringBuilder();
        for(SFXSyllable syl : syls){
            sentence.append(syl.getSyllable());
        }
        
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
            
            t = t.replace("%ssK", Long.toString(syl.getMsEventStart())); // Phrase
            t = t.replace("%seK", Long.toString(syl.getMsEventEnd())); // Phrase
            t = t.replace("%smK", Long.toString(syl.getMsEventDuration() / 2)); // Phrase
            t = t.replace("%sdK", Long.toString(syl.getMsEventDuration())); // Phrase
            
            t = t.replace("%sentence", sentence.toString());
            t = t.replace("%syllable", syl.getSyllable());
            
            t = replaceFunction(t);
            
            char[] letters = syl.getSyllable().toCharArray();
            long pastLetterDuration = syl.getMsDuration();
            long currentBefore = syl.getMsStart();
            for(int i=0; i<letters.length; i++){
                long dur = letters.length - 1 == i ?
                        pastLetterDuration : syl.getMsDuration() / letters.length;
                pastLetterDuration -= syl.getMsDuration() / letters.length;
                
                // Millisecondes
                t = t.replace("%lsK", Long.toString(currentBefore)); // Lettre
                t = t.replace("%lmK", Long.toString(currentBefore + dur / 2L)); // Lettre
                t = t.replace("%leK", Long.toString(currentBefore + dur)); // Lettre
                t = t.replace("%ldK", Long.toString(dur)); // Lettre
                
                // Centièmes de seconde
                t = t.replace("%lcsK", Long.toString(currentBefore / 10)); // Lettre
                t = t.replace("%lcmK", Long.toString((currentBefore + dur / 2L) / 10)); // Lettre
                t = t.replace("%lceK", Long.toString((currentBefore + dur) / 10)); // Lettre
                t = t.replace("%lcdK", Long.toString(dur / 10)); // Lettre

                t = t.replace("%letter", Character.toString(letters[i]));
                
                currentBefore += dur;
            }
            
            output += t;
        }
        
        return output;
    }
    
    // Remplace phReplaceParameters
    public String replaceParams(String template, List<SFXSyllable> syls, int index){
        String t, output = "";
        
        StringBuilder sentence = new StringBuilder();
        for(SFXSyllable syl : syls){
            sentence.append(syl.getSyllable());
        }
        
        SFXSyllable syl = syls.get(index);
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

        t = t.replace("%ssK", Long.toString(syl.getMsEventStart())); // Phrase
        t = t.replace("%seK", Long.toString(syl.getMsEventEnd())); // Phrase
        t = t.replace("%smK", Long.toString(syl.getMsEventDuration() / 2)); // Phrase
        t = t.replace("%sdK", Long.toString(syl.getMsEventDuration())); // Phrase

        t = t.replace("%sentence", sentence.toString());
        t = t.replace("%syllable", syl.getSyllable());
            
        t = replaceFunction(t);


        char[] letters = syl.getSyllable().toCharArray();
        long pastLetterDuration = syl.getMsDuration();
        long currentBefore = syl.getMsStart();
        for(int i=0; i<letters.length; i++){
            long dur = letters.length - 1 == i ?
                    pastLetterDuration : syl.getMsDuration() / letters.length;
            pastLetterDuration -= syl.getMsDuration() / letters.length;

            // Millisecondes
            t = t.replace("%lsK", Long.toString(currentBefore)); // Lettre
            t = t.replace("%lmK", Long.toString(currentBefore + dur / 2L)); // Lettre
            t = t.replace("%leK", Long.toString(currentBefore + dur)); // Lettre
            t = t.replace("%ldK", Long.toString(dur)); // Lettre

            // Centièmes de seconde
            t = t.replace("%lcsK", Long.toString(currentBefore / 10)); // Lettre
            t = t.replace("%lcmK", Long.toString((currentBefore + dur / 2L) / 10)); // Lettre
            t = t.replace("%lceK", Long.toString((currentBefore + dur) / 10)); // Lettre
            t = t.replace("%lcdK", Long.toString(dur / 10)); // Lettre

            t = t.replace("%letter", Character.toString(letters[i]));

            currentBefore += dur;
        }

        output += t;
        
        return output;
    }
    
    private String replaceFunction(String text){
        // %resolve(scriptName, functionName, args...)
        final String RESOLVE = "%resolve";
        if(text.contains(RESOLVE)){
            int from = text.indexOf(RESOLVE + "(");
            int to = text.indexOf(")", from) + 1;
            
            String tag = text.substring(from, to);
            
            Pattern p = Pattern.compile(RESOLVE+"\\((?<name>\\w+),(?<func>\\w+),*(?<params>[^\\)]*)");
            Matcher m = p.matcher(tag);
            
            String result = "";
            
            if(m.find()){
                String scriptName = m.group("name");
                String func = m.group("func");
                String ps = m.group("params");
                String[] params = null;
                
                if(ps.isEmpty() == false){
                    params = ps.split(",");
                }
                
                
                
                for(SFXCode c : getCodes()){
                    if(c.getScriptName().equalsIgnoreCase(scriptName)){
                        try(Context ctx = Context.newBuilder("js").build();){
                            Value script = ctx.eval("js", "(" + c.getContent() + ")");
                            
                            result = script.execute().asString();
                            
//                            Value function = script.getMember(func);
//
//                            if(params == null){
//                                result = function.execute();
//                            }else{
//                                result = function.execute((Object) params);
//                            }
                        }
                        break;
                    }
                }
            }
            
            text = text.replace(tag, result);
            text = replaceFunction(text);            
        }
        return text;
    } 
}
