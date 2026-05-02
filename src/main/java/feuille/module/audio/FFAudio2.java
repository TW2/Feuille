package feuille.module.audio;

import feuille.util.Loader;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;

import java.awt.image.BufferedImage;
import java.io.File;

public class FFAudio2 {

    private FFAudio2(){

    }

    public static long getMsDuration(File media){
        long dur = -1L;

        try(FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(media)) {

            grabber.start();

            dur = grabber.getLengthInTime() / 1000L;

            grabber.stop();
            grabber.release();

        } catch (FrameGrabber.Exception ex) {
            Loader.consoleErr(ex.getMessage());
        }

        return dur;
    }

    public static BufferedImage getImage(long ms, boolean isSpectrum){
        File app = new File(new File("").getAbsolutePath());
        File folder = new File(app + "/settings");

        WaveDB db;

        if(isSpectrum){
            db = new WaveDB((new File(folder, "audio.db").toPath()), "spectrum");
        }else{
            db = new WaveDB((new File(folder, "audio.db").toPath()), "audio");
        }

        return db.get(ms);
    }
}
