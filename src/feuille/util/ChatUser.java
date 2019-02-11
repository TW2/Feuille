/*
 * Copyright (C) 2019 util2
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
package feuille.util;

import java.util.Calendar;

/**
 *
 * @author util2
 */
public class ChatUser {
    
    private String surname = "Anonymous";
    private ChatUserKind kind = ChatUserKind.None;
    private ChatUserRole role = ChatUserRole.Leecher;
    private final Calendar since = Calendar.getInstance();
    private String remoteAddress = "127.0.0.1";

    public ChatUser() {
        
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setKind(ChatUserKind kind) {
        this.kind = kind;
    }

    public ChatUserKind getKind() {
        return kind;
    }

    public void setRole(ChatUserRole role) {
        this.role = role;
    }

    public ChatUserRole getRole() {
        return role;
    }

    public Calendar getSince() {
        return since;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }
    
    
    
}
