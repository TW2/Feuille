package feuille.module.video;

import java.awt.image.BufferedImage;

public class AssaEvent {
    private final double seconds;
    private final BufferedImage image;

    public AssaEvent(double seconds, BufferedImage image) {
        this.seconds = seconds;
        this.image = image;
    }

    public double getSeconds() {
        return seconds;
    }

    public BufferedImage getImage() {
        return image;
    }
}
