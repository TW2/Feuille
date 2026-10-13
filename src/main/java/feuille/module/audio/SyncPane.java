package feuille.module.audio;

import feuille.dialog.WaitFrame;
import feuille.module.audio.base.FFAudio2;
import feuille.module.audio.base.SignalResult;
import feuille.module.audio.base.Wave;
import feuille.module.audio.base.WaveDB;
import feuille.util.Exchange;
import feuille.util.FFMpegColor;
import feuille.util.Loader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;

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
        add(createToolBar(), BorderLayout.SOUTH);

        color1 = FFMpegColor.Blue;
        color2 = FFMpegColor.BlueViolet;
        path = null;
        period = -1L;
    }

    private JToolBar createToolBar(){
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JToggleButton tbSpectrum = new JToggleButton(Loader.fromResource(
                "/images/carbon--bring-to-front.png", 16, 16));
        tbSpectrum.setToolTipText("Spectrum");
        tbSpectrum.setSelected(false);
        tbSpectrum.addActionListener(e -> {
            waveform.setUseSpectrum(tbSpectrum.isSelected());
            waveform.repaint();
        });
        toolBar.add(tbSpectrum);

        JButton btnPlay = new JButton(Loader.fromResource(
                "/images/carbon--play-filled.png", 16, 16));
        btnPlay.setToolTipText("Play media");
        btnPlay.addActionListener(e -> {
            exchange.getViewVLC().play();
        });
        toolBar.add(btnPlay);

        JButton btnPause = new JButton(Loader.fromResource(
                "/images/carbon--pause-outline-filled.png", 16, 16));
        btnPause.setToolTipText("Pause");
        btnPause.addActionListener(e -> {
            exchange.getViewVLC().pause();
        });
        toolBar.add(btnPause);

        JButton btnStop = new JButton(Loader.fromResource(
                "/images/carbon--stop-filled.png", 16, 16));
        btnStop.setToolTipText("Stop");
        btnStop.addActionListener(e -> {
            exchange.getViewVLC().stop();
        });
        toolBar.add(btnStop);

        return toolBar;
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
            File folder = new File(app + "/settings/audio");
            if(!folder.exists()){
                boolean created = folder.mkdirs();
            }

            long msDuration = 0L;

            try{
                File outFile = new File( app + "/settings/audio/out.tmp");
                if(!outFile.exists()) outFile.delete();
                ProcessBuilder pb = new ProcessBuilder(
                        "ffprobe",
                        "-v", "error",
                        "-show_entries", "format=duration",
                        "-of", "default=noprint_wrappers=1:nokey=1",
                        path
                );
                Process p = pb
                        .redirectError(outFile)
                        .start();
                p.waitFor();
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));) {
                    String line = br.readLine();
                    double value = Double.parseDouble(line);
                    msDuration = (long) (value * 1000L);
                }
            } catch (IOException | InterruptedException e) {
                Loader.consoleErr(e.getMessage());
            }

            int w = waveform.getWidth();
            int h = waveform.getHeight();
            String strColor = color2 != null ?
                    color1.getName() + "|" + color2.getName() : color1.getName();

            db = new WaveDB((new File(folder, "audio.db").toPath()), "audio");
            db.clear();
            db.create();

            long milliseconds = 0L;
            while(milliseconds < msDuration){

                File imageFile = new File(folder, "temp1.png");
                if(imageFile.exists()){
                    boolean deleted = imageFile.delete();
                    if(!deleted) return null;
                }

                try{
                    ProcessBuilder pb = new ProcessBuilder(
                            "ffmpeg",
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
                        new SignalResult(milliseconds, msDuration, false)
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
            while(milliseconds < msDuration){
                File imageFile = new File(folder, "temp2.png");
                if(imageFile.exists()){
                    boolean deleted = imageFile.delete();
                    if(!deleted) return null;
                }

                try{
                    ProcessBuilder pb = new ProcessBuilder(
                            "ffmpeg",
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
                        new SignalResult(milliseconds, msDuration, true)
                );

                milliseconds += period;
            }

            worker.firePropertyChange(
                    "progress",
                    null,
                    new SignalResult(msDuration, msDuration, true)
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
