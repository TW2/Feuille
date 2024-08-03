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
package org.wingate.feuille.m.afm.karaoke;

import java.util.ArrayList;
import java.util.List;
import org.wingate.feuille.ass.AssEvent;

/**
 *
 * @author util2
 */
public class BiEvent {
    private boolean active;
    private AssEvent originalAssEvent;
    private final List<AssEvent> transformedAssEvents;

    public BiEvent() {
        active = false;
        originalAssEvent = new AssEvent();
        transformedAssEvents = new ArrayList<>();
    }

    public BiEvent(boolean active, AssEvent originalAssEvent) {
        this.active = active;
        this.originalAssEvent = originalAssEvent;
        this.transformedAssEvents = new ArrayList<>();
    }

    public List<AssEvent> getTransformedAssEvents() {
        return transformedAssEvents;
    }
    
    public AssEvent getEvent(){
        return originalAssEvent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AssEvent getOriginalAssEvent() {
        return originalAssEvent;
    }

    public void setOriginalAssEvent(AssEvent originalAssEvent) {
        this.originalAssEvent = originalAssEvent;
    }
}
