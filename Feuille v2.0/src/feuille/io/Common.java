/*
 * Copyright (C) 2018 util2
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
package feuille.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 * @author util2
 */
public class Common {
    
    /**
     * Try to get a correct charset
     * @param pathname A file name
     * @return A charset
     * @throws java.io.FileNotFoundException
     */
    public static Charset detectCharset(String pathname) throws FileNotFoundException{
        return detectCharset(new FileReader(pathname));
    }
    
    /**
     * Try to get a correct charset
     * Byte Order mark (Bytes >> Encoding Form):
     * - 00 00 FE FF >> UTF-32, big-endian
     * - FF FE 00 00 >> UTF-32, little-endian
     * - FE FF >> UTF-16, big-endian
     * - FF FE >> UTF-16, little-endian
     * - EF BB BF >> UTF-8
     * @param fr A stream
     * @return A charset
     */
    public static Charset detectCharset(FileReader fr){
        Charset cs = null;
        String newline;
        
        try(BufferedReader br = new BufferedReader(fr)){            
            // Scan for encoding marks
            while ((newline = br.readLine()) != null) {                
                if(newline.startsWith("[\u0000\u0000") | newline.startsWith("\u00FF\u00FE\u0000\u0000")){
                    cs = Charset.forName("UTF-32LE");
                }else if(newline.startsWith("\u0000\u0000[") | newline.startsWith("\u0000\u0000\u00FE\u00FF")){
                    cs = Charset.forName("UTF-32BE");
                }else if(newline.startsWith("[\u0000") | newline.startsWith("\u00FF\u00FE")){
                    cs = Charset.forName("UTF-16LE");
                }else if(newline.startsWith("\u0000[") | newline.startsWith("\u00FE\u00FF")){
                    cs = Charset.forName("UTF-16BE");
                }else if(newline.startsWith("\u00EF\u00BB\u00BF")){
                    cs = Charset.forName("UTF-8");
                }
                
                // If a charset was found then close the stream
                // and the return charset encoding.
                if (cs != null){
                    break;
                }
            }
            
            // If nothing was found then set the encoding to system default.
            if (cs == null){
                cs = Charset.forName(fr.getEncoding());
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        
        return cs;
    }
    
}
