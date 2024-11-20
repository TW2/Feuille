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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author util2
 */
public class AssActor {
    public enum Kind {
        Male("Male"),
        Female("Female"),
        Robot("Robot"),
        Narrator("Narrator"),
        Unknown("Unknown");
        
        String name;
        
        private Kind(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
        
        public static Kind get(String s){
            Kind kind = Unknown;
            
            for(Kind k : values()){
                if(s.equalsIgnoreCase(k.getName())){
                    kind = k;
                    break;
                }
            }
            
            return kind;
        }
    }
    
    private String name;
    private Color color;
    private String b64Image;
    private Kind kind;

    public AssActor(String name, Color color, String b64Image, Kind kind) {
        this.name = name;
        this.color = color;
        this.b64Image = b64Image;
        this.kind = kind;
    }

    public AssActor(String name) {
        this(name, Color.black, null, Kind.Unknown);
    }

    public AssActor() {
        this("Unknown", Color.black, null, Kind.Unknown);
    }
    
    public static AssActor fromLine(String rawline){
        // [Actors]
        // Format: Name, Color, Kind, Image
        // Actor: Robin,125364,Male,0e36ghp=
        AssActor actor = new AssActor();
        String[] t = rawline.split(",");
        
        actor.name = t[0].substring(t[0].indexOf(" ") + 1);
        actor.color = new Color(Integer.parseInt(t[1]));
        actor.kind = Kind.get(t[2]);
        actor.b64Image = t[3].isEmpty() ? null : t[3];
        
        return actor;
    }
    
    public String toLine(){
        // [Actors]
        // Format: Name, Color, Kind, Image
        // Actor: Robin,125364,Male,0e36ghp=
        StringBuilder line = new StringBuilder("Actor: ");
        line.append(name).append(",");
        line.append(Integer.toString(color.getRGB())).append(",");
        line.append(kind.getName()).append(",");        
        line.append(b64Image == null ? "" : b64Image);        
        return line.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getB64Image() {
        return b64Image;
    }

    public void setB64Image(String b64Image) {
        this.b64Image = b64Image;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }
    
    public BufferedImage getImage() throws IOException{
        if(b64Image == null) return null;
        byte[] buffer = Base64.getDecoder().decode(b64Image);
        return ImageIO.read(new ByteArrayInputStream(buffer));
    }
    
    public void setImage(BufferedImage img) throws IOException{
        if(img == null) return;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", out);
            if(out == null) return;
            b64Image = Base64.getEncoder().encodeToString(out.toByteArray());
        }
    }
    
    public ImageIcon getIcon() throws IOException{
        BufferedImage img = getImage();
        return img == null ? null : new ImageIcon(img);
    }
    
    public BufferedImage getBufferedImage(ImageIcon ii){
        if(ii == null) return null;
        BufferedImage img = new BufferedImage(
                ii.getIconWidth(),
                ii.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = img.createGraphics();
        ii.paintIcon(null, g, 0, 0);
        g.dispose();
        
        return img;
    }

    @Override
    public String toString() {
        return getName();
    }
}
