package feuille.module.audio.ui;

import feuille.dialog.WaitFrame;
import feuille.module.audio.FFAudio2;
import feuille.module.audio.SignalResult;
import feuille.module.audio.Wave;
import feuille.module.audio.WaveDB;
import feuille.util.Exchange;
import feuille.util.FFMpegColor;
import feuille.util.Loader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncPane extends JPanel {

    private final Exchange exchange;

    private final Wave waveform;
    private FFMpegColor color1;
    private FFMpegColor color2;
    private String path;
    private long period;

    public SyncPane(Exchange exchange){
        this.exchange = exchange;
        exchange.setSyncPane(this);
        waveform = new Wave(exchange);

        setLayout(new BorderLayout());
        add(waveform, BorderLayout.CENTER);

        color1 = FFMpegColor.Blue;
        color2 = FFMpegColor.BlueViolet;
        path = null;
        period = -1L;
    }

    public void openMedia(String path, long period) {
        this.path = path;
        this.period = period;

        WaitFrame waitFrame = new WaitFrame();

        worker.addPropertyChangeListener((e)->{
            if(e.getPropertyName().equalsIgnoreCase("progress")){
                if(e.getNewValue() instanceof SignalResult x){
                    waitFrame.updateData(x.getProgress(), x.getPhase());
                }
            }
        });
        worker.execute();
    }

    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        public Void doInBackground() {
            WaveDB db;

            //===========================================================
            // WAVEFORM - WAVEFORM - WAVEFORM - WAVEFORM - WAVEFORM - WAV
            //===========================================================
            File app = new File(new File("").getAbsolutePath());
            File folder = new File(app + "/settings");
            if(!folder.exists()){
                boolean created = folder.mkdirs();
            }

            long length = FFAudio2.getMsDuration(new File(path));
            int w = waveform.getWidth();
            int h = waveform.getHeight();
            String strColor = color2 != null ?
                    color1.getName() + "|" + color2.getName() : color1.getName();

            db = new WaveDB((new File(folder, "audio.db").toPath()), "audio");
            db.clear();
            db.create();

            long milliseconds = 0L;
            while(milliseconds < length){

                File imageFile = new File(folder, "temp1.png");
                if(imageFile.exists()){
                    boolean deleted = imageFile.delete();
                    if(!deleted) return null;
                }

                try{
                    String ffmpeg = org.bytedeco.javacpp.Loader.load(
                            org.bytedeco.ffmpeg.ffmpeg.class);
                    ProcessBuilder pb = new ProcessBuilder(
                            ffmpeg,
                            "-ss", String.format("%dms", milliseconds),
                            "-to", String.format("%dms", milliseconds + period),
                            "-i", path,
                            "-filter_complex", String.format(
                            "showwavespic=s=%dx%d:colors=%s", w, h, strColor),
                            "-frames:v", "1",
                            imageFile.getPath()
                    );
                    Process p = pb.start();
                    p.waitFor();

                    if(imageFile.exists()){
                        db.add(milliseconds, ImageIO.read(imageFile.getAbsoluteFile()));
                    }

                }catch(Exception e){
                    Loader.consoleErr(e.getMessage());
                }

                worker.firePropertyChange(
                        "progress",
                        null,
                        new SignalResult(milliseconds, length, false)
                );

                milliseconds += period;
            }

            //===========================================================
            // SPECTROGRAM - SPECTROGRAM - SPECTROGRAM - SPECTROGRAM - SP
            //===========================================================
            db = new WaveDB((new File(folder, "audio.db").toPath()), "spectrum");
            db.clear();
            db.create();

            milliseconds = 0L;
            while(milliseconds < length){
                File imageFile = new File(folder, "temp2.png");
                if(imageFile.exists()){
                    boolean deleted = imageFile.delete();
                    if(!deleted) return null;
                }

                try{
                    String ffmpeg = org.bytedeco.javacpp.Loader.load(
                            org.bytedeco.ffmpeg.ffmpeg.class);
                    ProcessBuilder pb = new ProcessBuilder(
                            ffmpeg,
                            "-ss", String.format("%dms", milliseconds),
                            "-to", String.format("%dms", milliseconds + period),
                            "-i", path,
                            "-lavfi",
                            String.format(
                                    "showspectrumpic=s=%dx%d:legend=0:scale=cbrt", w, h),
                            imageFile.getPath()
                    );
                    Process p = pb.start();
                    p.waitFor();
                    if(imageFile.exists()){
                        db.add(milliseconds, ImageIO.read(imageFile.getAbsoluteFile()));
                    }
                }catch(IOException | InterruptedException e){
                    Loader.consoleErr(e.getMessage());
                }

                worker.firePropertyChange(
                        "progress",
                        null,
                        new SignalResult(milliseconds, length, true)
                );

                milliseconds += period;
            }

            worker.firePropertyChange(
                    "progress",
                    null,
                    new SignalResult(length, length, true)
            );

            return null;
        }

        @Override
        public void done() {
            try{
                waveform.setCurrent(FFAudio2.getImage(0L, false));
                waveform.setNext(FFAudio2.getImage(15_000L, false));
                waveform.setMsCurrentStart(0L);
                waveform.setMsCurrentEnd(15_000L);
                waveform.setMsNextStart(15_000L);
                waveform.setMsNextEnd(30_000L);
                waveform.repaint();
            }catch(Exception e){
                Loader.dialogErr(e.getMessage());
            }
        }
    };

    public Wave getWaveform() {
        return waveform;
    }

    public FFMpegColor getColor1() {
        return color1;
    }

    public void setColor1(FFMpegColor color1) {
        this.color1 = color1;
    }

    public FFMpegColor getColor2() {
        return color2;
    }

    public void setColor2(FFMpegColor color2) {
        this.color2 = color2;
    }
}
