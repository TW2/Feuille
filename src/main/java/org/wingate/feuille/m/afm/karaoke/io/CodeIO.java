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
package org.wingate.feuille.m.afm.karaoke.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.wingate.feuille.m.afm.karaoke.CodeType;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXCode;

/**
 *
 * @author util2
 */
public class CodeIO {

    public CodeIO() {
    }
    
    public static void save(String path, SFXCode code){
        try(PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8);){
            pw.print(code.getContent());
        } catch (IOException ex) {
            Logger.getLogger(CodeIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static SFXCode load(String path) {
        SFXCode code = null;
        try(FileReader fr = new FileReader(path);
                BufferedReader br = new BufferedReader(fr);){
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null){
                sb.append(line).append("\n");
            }
            CodeType c = CodeType.JavaScript;
            switch(path.substring(path.lastIndexOf("."))){
                case ".js" -> { c = CodeType.JavaScript; }
                case ".lua" -> { c = CodeType.Lua; }
                case ".py" -> { c = CodeType.Python; }
                case ".rb" -> { c = CodeType.Ruby; }
            }
            code = new SFXCode(c, sb.toString());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CodeIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CodeIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return code;
    }
}
