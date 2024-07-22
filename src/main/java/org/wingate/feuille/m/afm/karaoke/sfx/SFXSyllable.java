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

/**
 *
 * @author util2
 */
public class SFXSyllable {
    private long msStart;
    private long msMid;
    private long msEnd;
    private long msDuration;
    private long msEventStart;
    private long msEventEnd;
    private long msEventDuration;
    private String syllable;

    public SFXSyllable(long msStart, long msMid, long msEnd, long msDuration, long msEventStart, long msEventEnd, long msEventDuration, String syllable) {
        this.msStart = msStart;
        this.msMid = msMid;
        this.msEnd = msEnd;
        this.msDuration = msDuration;
        this.msEventStart = msEventStart;
        this.msEventEnd = msEventEnd;
        this.msEventDuration = msEventDuration;
        this.syllable = syllable;
    }

    public long getMsStart() {
        return msStart;
    }

    public void setMsStart(long msStart) {
        this.msStart = msStart;
    }

    public long getMsMid() {
        return msMid;
    }

    public void setMsMid(long msMid) {
        this.msMid = msMid;
    }

    public long getMsEnd() {
        return msEnd;
    }

    public void setMsEnd(long msEnd) {
        this.msEnd = msEnd;
    }

    public long getMsDuration() {
        return msDuration;
    }

    public void setMsDuration(long msDuration) {
        this.msDuration = msDuration;
    }

    public long getMsEventStart() {
        return msEventStart;
    }

    public void setMsEventStart(long msEventStart) {
        this.msEventStart = msEventStart;
    }

    public long getMsEventEnd() {
        return msEventEnd;
    }

    public void setMsEventEnd(long msEventEnd) {
        this.msEventEnd = msEventEnd;
    }

    public long getMsEventDuration() {
        return msEventDuration;
    }

    public void setMsEventDuration(long msEventDuration) {
        this.msEventDuration = msEventDuration;
    }

    public String getSyllable() {
        return syllable;
    }

    public void setSyllable(String syllable) {
        this.syllable = syllable;
    }
}
