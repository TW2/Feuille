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
package org.wingate.feuille.ass;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author util2
 */
public class AssCommon {
    
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
    
    public static String UUEncode(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
            int remain = bytes.length;
            int buffer = remain > 3 ? 3 : remain;
            while(remain > 0){
                baos.write(bytes, 0, buffer);
                sb.append(UUCodec(baos.toByteArray(), UUDirection.Encode));
                remain -= 3;
                buffer = remain > 3 ? 3 : remain;
            }
        } catch (IOException ex) {
            Logger.getLogger(AssCommon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }
    
    public static byte[] UUDecode(String s){
        byte[] bytes = null;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();){
            int pos = 0, available = s.length();
            while(available > 0){
                String fourChars = s.substring(pos, available < 4 ? pos + available : pos + 4);
                baos.write(fourChars.getBytes());
                byte[] result = UUCodec(baos.toByteArray(), UUDirection.Decode);
                output.write(result, 0, 3);
                pos += 4;
                available -= 4;
            }
            bytes = output.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(AssCommon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bytes;
    }
    
    private enum UUDirection{
        Encode, Decode;
    }
    
    private static byte[] UUCodec(byte[] input, UUDirection dir) throws IOException{
        
        byte[] output = null;
        
        switch(dir){
            case Encode -> {
                byte    a, b = 1, c = 1;
                int     c1, c2, c3, c4;
                
                a = input[0];
                if (input.length > 1) {
                    b = input[1];
                }
                if (input.length > 2) {
                    c = input[2];
                }
                
                c1 = (a >>> 2) & 0x3f;
                c2 = ((a << 4) & 0x30) | ((b >>> 4) & 0xf);
                c3 = ((b << 2) & 0x3c) | ((c >>> 6) & 0x3);
                c4 = c & 0x3f;
                
                output = new byte[4];
                
                output[0] = (byte)(c1 + ' ');
                output[1] = (byte)(c2 + ' ');
                output[2] = (byte)(c3 + ' ');
                output[3] = (byte)(c4 + ' ');
            }
            case Decode -> {
                int c1, c2, c3, c4;
                int a, b, c;
                
                byte[] decoderBuffer = new byte[4];
                
                c1 = input[0];
                c2 = input[1];
                c3 = input[2];
                c4 = input[3];
                
                if(c1 == -1 | c2 == -1 | c3 == -1 | c4 == -1){
                    throw new IOException("CEStreamExhausted");
                }
                
                decoderBuffer[0] = (byte) ((c1 - ' ') & 0x3f);
                decoderBuffer[1] = (byte) ((c2 - ' ') & 0x3f);
                decoderBuffer[2] = (byte) ((c3 - ' ') & 0x3f);
                decoderBuffer[3] = (byte) ((c4 - ' ') & 0x3f);
                
                
                a = ((decoderBuffer[0] << 2) & 0xfc) | ((decoderBuffer[1] >>> 4) & 3);
                b = ((decoderBuffer[1] << 4) & 0xf0) | ((decoderBuffer[2] >>> 2) & 0xf);
                c = ((decoderBuffer[2] << 6) & 0xc0) | (decoderBuffer[3] & 0x3f);
                
                output = new byte[3];
                
                output[0] = (byte)(a & 0xff);
                output[1] = (byte)(b & 0xff);
                output[2] = (byte)(c & 0xff);
            }
        }
        
        return output;
    }
    
    /**
     * <p>Aegisub/libaegisub/ass/uuencode.cpp</p>
     * <p>Despite being called uuencoding by ass_specs.doc,
     * the format is actually somewhat different from real uuencoding.<br />
     * Each 3-byte chunk is split into 4 6-bit pieces, then 33 is added to each piece.<br />
     * Lines are wrapped after 80 characters, and files with non-multiple-of-three 
     * lengths are padded with zero.</p>
     * @param input 4 bytes when encoding otherwise 3 bytes if decoding
     * @param dir choice of method Encoding or Decoding
     * @return (encoding) 4 bytes / (decoding) 3 bytes
     * @throws IOException Problems with an algorithm
     * @link https://github.com/Aegisub/Aegisub/blob/master/libaegisub/ass/uuencode.cpp
     */
//    private static byte[] AegiUUCodec(byte[] input, UUDirection dir) throws IOException{
//        
//        byte[] output = null;
//        
//        switch(dir){
//            case Encode -> {
//                byte    a, b = 0, c = 0;
//                int     c1, c2, c3, c4;
//                
//                a = input[0];
//                if (input.length > 1) {
//                    b = input[1];
//                }
//                if (input.length > 2) {
//                    c = input[2];
//                }
//                
//                c1 = a >> 2;
//                c2 = ((a & 0x3) << 4) | ((b & 0xf0) >> 4);
//                c3 = ((b & 0xf) << 2) | ((c & 0xc0) >> 6);
//                c4 = c & 0x3f;
//                
//                output = new byte[4];
//                
//                output[0] = (byte)(c1 + 33);
//                output[1] = (byte)(c2 + 33);
//                output[2] = (byte)(c3 + 33);
//                output[3] = (byte)(c4 + 33);
//            }
//            case Decode -> {
//                byte[] src = new byte[]{0, 0, 0, 0};
//                int counter = 0;
//                
//                for (int i=0; i<input.length; i++) {
//                    char c = (char)input[i];
//                    if (c != '\n' && c != '\r') {
//                        src[i] = (byte)(c - 33);
//                        counter++;
//                    }
//		}
//                
//                output = new byte[]{0, 0, 0};
//
//		if (counter > 1){ output[0] = (byte)(src[0] << 2 | src[1] >> 4); }
//		if (counter > 2){ output[1] = (byte)((src[1] & 0xf) << 4 | src[2] >> 2); }
//		if (counter > 3){ output[2] = (byte)((src[2] & 0x3) << 6 | src[3]); }
//            }
//        }
//        
//        return output;
//    }
    
    public static File getFile(File folder, String name, String content){
        File file = new File(folder, name);
        content = content.replace("\n", "");
        try(FileOutputStream fos = new FileOutputStream(file);){
            fos.write(AssCommon.UUDecode(content));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AssCommon.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AssCommon.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return file;
    }
    
    public static String fromFile(File path){
        StringBuilder sb = new StringBuilder();
        try(FileInputStream fis = new FileInputStream(path);){
            String wholeContent = AssCommon.UUEncode(fis.readAllBytes());
            int pos = 0, available = wholeContent.length();
            while(available > 0){
                String line = wholeContent.substring(pos, available < 80 ? pos + available : pos + 80);
                sb.append(line).append("\n");
                pos += 4;
                available -= 4;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AssCommon.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AssCommon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
}
