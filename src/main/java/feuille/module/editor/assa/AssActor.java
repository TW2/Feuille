package feuille.module.editor.assa;

import feuille.util.assa.AssActorType;
import feuille.util.assa.AssColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class AssActor {
    private String name;
    private AssColor color;
    private String b64Image;
    private AssActorType kind;

    public AssActor(String name, AssColor color, String b64Image, AssActorType kind) {
        this.name = name;
        this.color = color;
        this.b64Image = b64Image;
        this.kind = kind;
    }

    public AssActor(String name) {
        this(name, new AssColor(Color.black), null, AssActorType.Unknown);
    }

    public AssActor() {
        this("Unknown", new AssColor(Color.black), null, AssActorType.Unknown);
    }

    public static AssActor fromLine(String line){
        // [Actors]
        // Format: Name, Color, Kind, Image
        // Actor: Robin,125364,Male,0e36ghp=
        AssActor actor = new AssActor();
        String[] t = line.split(",");

        actor.name = t[0].substring(t[0].indexOf(" ") + 1);
        actor.color = new AssColor(new Color(Integer.parseInt(t[1])));
        actor.kind = AssActorType.get(t[2]);
        actor.b64Image = t[3].isEmpty() ? null : t[3];

        return actor;
    }

    public String toLine(){
        // [Actors]
        // Format: Name, Color, Kind, Image
        // Actor: Robin,125364,Male,0e36ghp=
        StringBuilder line = new StringBuilder("Actor: ");
        line.append(name).append(",");
        line.append(Integer.toString(color.getColor().getRGB())).append(",");
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

    public AssColor getColor() {
        return color;
    }

    public void setColor(AssColor color) {
        this.color = color;
    }

    public String getB64Image() {
        return b64Image;
    }

    public void setB64Image(String b64Image) {
        this.b64Image = b64Image;
    }

    public AssActorType getAssActorType() {
        return kind;
    }

    public void setAssActorType(AssActorType kind) {
        this.kind = kind;
    }

    public BufferedImage getImage() throws IOException {
        if(b64Image == null) return null;
        byte[] buffer = Base64.getDecoder().decode(b64Image);
        return ImageIO.read(new ByteArrayInputStream(buffer));
    }

    public void setImage(BufferedImage img) throws IOException{
        if(img == null) return;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", out);
            b64Image = Base64.getEncoder().encodeToString(out.toByteArray());
        }
    }

    public ImageIcon getIcon() throws IOException {
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
