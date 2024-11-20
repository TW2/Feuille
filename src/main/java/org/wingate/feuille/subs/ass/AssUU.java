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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author util2
 */
public class AssUU {

    public AssUU() {
    }
    
    public static void writeUU(File file, PrintWriter pw){
        
        byte[] three;// = new byte[3]; // 24bits
        byte[] four = new byte[4]; // 32bits (ascii limit)
        char[] outc = new char[4];
        
        int counterChar = 0;
        
        try(FileInputStream fis = new FileInputStream(file);){
            int remaining = fis.available();
            
            while(remaining > 0){
                three = new byte[3];
                fis.read(three, 0, three.length);
                
                // 33 means jump specials characters of ascii
                // 3bytes = 24bits
                // (000000 to 111111, from 0 to 63) = 24bits
                // 4bytes = 32bits
                // (00000000 to 11111111, from 0 to 255) = 32bits (ascii limit)
                four[0] = (byte)(three[0] >> 2);
                four[1] = (byte)(((three[0] & 0x3) << 4) | ((three[1] & 0xF0) >> 4));
                four[2] = (byte)(((three[1] & 0xF) << 2) | ((three[2] & 0xC0) >> 6));
                four[3] = (byte)(three[2] & 0x3F);
                
                // unsigned char in Java:  byte & 0xFF
                // each char must be in 33-96 range
                outc[0] = (char)setInRange((four[0] & 0xFF) + 33);
                outc[1] = (char)setInRange((four[1] & 0xFF) + 33);
                outc[2] = (char)setInRange((four[2] & 0xFF) + 33);
                outc[3] = (char)setInRange((four[3] & 0xFF) + 33);
                
                // Treatment of the very last data
                if(remaining <= 3){
                    // The last bytes (in char[])
                    char[] cs = new char[]{outc[0], outc[1], outc[2], outc[3]};
                    
                    // Let's pass it to String and reverse it
                    StringBuilder sb0 = new StringBuilder(String.valueOf(cs));                                
                    String s0 = sb0.reverse().toString();

                    // We are on the last data, we can not finish by a '!'
                    // Remove them in the reverse string
                    int indices = 4;
                    while(s0.startsWith("!")){
                        s0 = s0.substring(1);
                        indices--;
                    }

                    // Now we want the real order, reverse it again
                    StringBuilder sb = new StringBuilder(s0);
                    String s = sb.reverse().toString();
                    
                    // Remake the data with a reduced array
                    outc = new char[indices];
                    for(int k=0; k<indices; k++){
                        outc[k] = s.charAt(k);
                    }
                }
                
                for(int i=0; i<outc.length; i++){
                    counterChar++;
                    if(counterChar > 80){
                        pw.println();
                        counterChar = 1;
                    }
                    // Print a char
                    pw.print(outc[i]);
                }
                
                remaining -= three.length;
            }
        } catch (IOException ex) {
            Logger.getLogger(AssUU.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static int setInRange(int v){
        return v > 192 ? v - 192 : v;
    }
    
    public static File readUU(BufferedReader br, String path){
        File file = new File(path);
        
        byte[] three;// = new byte[3]; // 24bits
        byte[] four = new byte[4]; // 32bits (ascii limit)
        
        try(FileOutputStream fos = new FileOutputStream(file);){
            String line;
            while(true){
                line = br.readLine();                
                if(line == null || line.isEmpty() || line.startsWith("fontname: ")){
                    break;
                }
                
                int count = 0;
                int remaining = line.toCharArray().length;
                for(char c : line.toCharArray()){                    
                    if(c != '\n' && c != '\r'){
                        if(c - 33 + 192 > 255){
                            four[count] = (byte)(c - 33 + 192);
                        }else{
                            four[count] = (byte)(c - 33);
                        }                        
                    }
                    
                    if(count == 3 || remaining < 4){
                        three = new byte[count];
                        if(count > 1) three[0] = (byte)((four[0] << 2) | (four[1] >> 4));
                        if(count > 2) three[1] = (byte)(((four[1] & 0xF) << 4) | (four[2] >> 2));
                        if(count > 3) three[2] = (byte)(((four[2] & 0x3) << 6) | (four[3]));

                        fos.write(three, 0, three.length);
                        
                        count = 0;
                    }else{
                        count++;
                    }
                    remaining--;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AssUU.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AssUU.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }
    
    public static void main(String[] args) {
        String path1 = "C:\\Users\\util2\\Desktop\\Tests\\subsy\\uu\\uu.txt";
        String font1 = "F:\\Fansubs\\Bleach\\_Fontes\\AveriaSerif-Regular.ttf";
        try(PrintWriter pw = new PrintWriter(path1)){
            AssUU.writeUU(new java.io.File(font1), pw);
        }catch(Exception exc){
            
        }
        String path2 = "C:\\Users\\util2\\Desktop\\Tests\\subsy\\uu\\xx.ttf";
        try(FileReader fr = new FileReader(path1); BufferedReader br = new BufferedReader(fr);){
            AssUU.readUU(br, path2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AssUU.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AssUU.class.getName()).log(Level.SEVERE, null, ex);
        }
        String path3 = "C:\\Users\\util2\\Desktop\\Tests\\subsy\\uu\\zz.txt";
        try(PrintWriter pw = new PrintWriter(path3)){
            AssUU.writeUU(new java.io.File(path2), pw);
        }catch(Exception exc){
            
        }
    }
}
