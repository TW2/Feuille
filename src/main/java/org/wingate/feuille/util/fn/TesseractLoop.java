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
package org.wingate.feuille.util.fn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.LeptonicaFrameConverter;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.leptonica.global.leptonica;
import org.bytedeco.tesseract.TessBaseAPI;

/**
 *
 * @author util2
 */
public class TesseractLoop {
    @SuppressWarnings({"CallToPrintStackTrace", "null"})
    public static void main(String[] args){
        List<BufferedImage> images = new ArrayList<>();
        try{
            images.add(ImageIO.read(new File("C:\\Users\\util2\\Desktop\\Bureau\\feuille ocr tests\\01.png")));
            images.add(ImageIO.read(new File("C:\\Users\\util2\\Desktop\\Bureau\\feuille ocr tests\\02.png")));
        }catch(IOException exc){
            exc.printStackTrace();
        }
        
        Java2DFrameConverter converter1 = new Java2DFrameConverter();
        LeptonicaFrameConverter converter2 = new LeptonicaFrameConverter();
        
        TessBaseAPI api = new TessBaseAPI();
        api.Init(null, "eng");
        BytePointer outText = null;
        PIX pixImage1 = null;
        
        for(int i=0; i<10; i++){
            for(BufferedImage img : images){
                try{
                    // Init
                    Frame frame = converter1.convert(img);
                    pixImage1 = converter2.convert(frame);

                    // Usage
                    api.SetImage(pixImage1);
                    outText = api.GetUTF8Text();
                    String s = outText.getString("UTF-8");
                    System.out.println(s);

                    
                } catch (NullPointerException | UnsupportedEncodingException ex) {
                    Logger.getLogger(TesseractLoop.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        }
        
        // Tesseract + Leptonica
        api.End();
        outText.deallocate();
        leptonica.pixDestroy(pixImage1);
    }
}
