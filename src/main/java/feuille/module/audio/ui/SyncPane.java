package feuille.module.audio.ui;

import feuille.module.audio.Wave;
import feuille.util.Exchange;
import feuille.util.Loader;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import javax.swing.*;
import java.awt.*;

public class SyncPane extends JPanel {

    private final Exchange exchange;

    private final Wave waveform;

    public SyncPane(Exchange exchange){
        this.exchange = exchange;
        exchange.setSyncPane(this);
        waveform = new Wave();

        setLayout(new BorderLayout());
        add(waveform, BorderLayout.CENTER);
    }

    public void openMedia(String path) {
        try{
            if(!waveform.setMedia(path)){
                Loader.dialogErr("Error parsing media file!");
                return;
            }
            showPart(0L, 15000L, waveform.getWidth(), waveform.getHeight());
        } catch (FFmpegFrameGrabber.Exception e) {
            Loader.dialogErr(e.getMessage());
        }
    }

    public void showPart(long msStart, long msEnd, int width, int height){
        waveform.execute(msStart, msEnd, width, height);
    }

    public Wave getWaveform() {
        return waveform;
    }
}
