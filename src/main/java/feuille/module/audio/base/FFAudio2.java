package feuille.module.audio.base;

import java.awt.image.BufferedImage;
import java.io.File;

public class FFAudio2 {

    private FFAudio2(){

    }

    public static BufferedImage getImage(long ms, boolean isSpectrum){
        File app = new File(new File("").getAbsolutePath());
        File folder = new File(app + "/settings/audio");

        WaveDB db;

        if(isSpectrum){
            db = new WaveDB((new File(folder, "audio.db").toPath()), "spectrum");
        }else{
            db = new WaveDB((new File(folder, "audio.db").toPath()), "audio");
        }

        return db.get(ms);
    }
}
