package feuille.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

public class Loader {

    public static String language(String key, String value, ISO_3166 iso){
        if(iso == null){
            Locale locale = Locale.getDefault();
            try{
                iso = ISO_3166.getISO_3166(locale.getISO3Country());
            }catch(Exception exc){
                return value;
            }
        }
        String resource = "/translate/L_" + iso.getAlpha2().toLowerCase() + ".properties";
        try (InputStream in = Loader.class.getResourceAsStream(resource)) {
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

    public static String language(String key, String value){
        return language(key, value, ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()));
    }

    public static ImageIcon fromResource(String resource, int w, int h){
        try (InputStream in = Loader.class.getResourceAsStream(resource)) {
            if (in != null) {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
                    out.write(in.readAllBytes());
                    return new ImageIcon(out.toByteArray());
                }
            }
        } catch (IOException _) { }
        BufferedImage img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.black);
        g.fillRect(0,0, w,h);
        g.dispose();
        return new ImageIcon(img);
    }

    public static void consoleErr(String source){
        String startTranslation = filter(language("errMsg", "Error"));
        System.err.printf("%s:\n%s\n", startTranslation, source);
    }

    public static void dialogErr(String source){
        consoleErr(source);
        String startTranslation = filter(language("errMsg", "Error"));
        String title = language("errMsgTitle", "Error");

        JOptionPane.showMessageDialog(
                new javax.swing.JFrame(),
                String.format("%s:\n%s\n", startTranslation, source),
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static ImageIcon locations(String imageName){
        return new ImageIcon(Objects.requireNonNull(
                Loader.class.getResource("/locations/" + imageName.toLowerCase())
        ));
    }

    public static ImageIcon locations(Object locale){
        try{
            switch(locale){
                case ISO_639 x -> {
                    String search = String.format("%s.gif", x.getSet1().toLowerCase());
                    return new ImageIcon(Objects.requireNonNull(
                            Loader.class.getResource("/locations/" + search)
                    ));
                }
                case ISO_3166 x -> {
                    String flag = String.format("%s.gif", x.getAlpha2().toLowerCase());
                    return new ImageIcon(Objects.requireNonNull(
                            Loader.class.getResource("/locations/" + flag)
                    ));
                }
                default -> {
                    // Create a false flag
                    String flag = "gb.gif";
                    return new ImageIcon(Objects.requireNonNull(
                            Loader.class.getResource("/locations/" + flag)
                    ));
                }
            }
        }catch(Exception _){
            // Found exceptions
            String flag = "gb.gif";
            return new ImageIcon(Objects.requireNonNull(
                    Loader.class.getResource("/locations/" + flag)
            ));
        }
    }

    private static String filter(String s){
        return s.replace("[SPACE]", " ");
    }

}
