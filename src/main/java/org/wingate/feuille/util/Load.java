package org.wingate.feuille.util;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

public class Load {

    public static ImageIcon fromResource(String resource){
        String s = Objects.requireNonNull(Load.class.getResource(resource)).getPath();
        if(s.startsWith("file:")){
            try (InputStream in = Load.class.getResourceAsStream(resource)) {
                if (in != null) {
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
                        out.write(in.readAllBytes());
                        return new ImageIcon(out.toByteArray());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ImageIcon(s.replace("%20", " "));
    }

    public static String language(String key, String value, ISO_3166 iso){
        if(iso == null){
            Locale locale = Locale.getDefault();
            try{
                iso = ISO_3166.getISO_3166(locale.getISO3Country());
            }catch(Exception exc){
                return value;
            }
        }
        String resource = "/org/wingate/feuille/Languages_" + iso.getAlpha2().toLowerCase() + ".properties";
        try (InputStream in = Load.class.getResourceAsStream(resource)) {
            if (in != null) {
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))){
                    String line;
                    while((line = reader.readLine()) != null){
                        if(line.startsWith(key + "=")){
                            return line.substring(key.length()+1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return value;
    }
}
