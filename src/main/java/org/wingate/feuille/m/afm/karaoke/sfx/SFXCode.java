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

import org.wingate.feuille.m.afm.karaoke.CodeType;

/**
 *
 * @author util2
 */
public class SFXCode {
    private CodeType codeType;
    private String content;
    
    private String scriptName;
    private String author;
    private String version;
    private String description;
    private String updateDetails;
    
    /*
    # Scripting
    ScriptName: scriptName
    Author: author
    Version: version
    Description: description
    UpdateDetails: update
    CodeType: codeType
    {
    one script
    },
    */
    
    public SFXCode(){
        this(CodeType.JavaScript, "function hello(){print(\"Hello world!\");}");
    }

    public SFXCode(CodeType codeType, String content) {
        this.codeType = codeType;
        this.content = content;
        scriptName = "Unknown script name";
        author = "Unknown author";
        version = "0.0.1";
        description = "None";
        updateDetails = "None";
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateDetails() {
        return updateDetails;
    }

    public void setUpdateDetails(String updateDetails) {
        this.updateDetails = updateDetails;
    }

    @Override
    public String toString() {
        String s = "";
        switch(codeType){
            case JavaScript -> { s = "JavaScript"; }
            case Lua -> { s = "Lua"; }
            case Python -> { s = "Python"; }
            case Ruby -> { s = "Ruby"; }
        }
        return String.format("%s (%s)", scriptName, s);
    }
}
