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
            long startUs = Math.round(exchange.getStartTime().getMsTime() * 1000d);
            long endUs = Math.round(exchange.getEndTime().getMsTime() * 1000d);
            if(startUs < endUs){
                exchange.getViewGst().play(startUs, endUs);
            }else{
                exchange.getViewGst().play(0L, 0L);
            }
        });
        toolBar.add(btnPlay);

        JButton btnPause = new JButton(Loader.fromResource(
                "/images/carbon--pause-outline-filled.png", 16, 16));
        btnPause.setToolTipText("Pause");
        btnPause.addActionListener(e -> exchange.getViewGst().pause());
        toolBar.add(btnPause);

        JButton btnStop = new JButton(Loader.fromResource(
                "/images/carbon--stop-filled.png", 16, 16));
        btnStop.setToolTipText("Stop");
        btnStop.addActionListener(e -> exchange.getViewGst().stop());
        toolBar.add(btnStop);

        JButton btnPlayBefore = new JButton(Loader.fromResource(
                "/images/16-play-start-to.png", 16, 16));
        btnPlayBefore.setToolTipText(Loader.language("tooltip.play.before", "Play before"));
        btnPlayBefore.addActionListener(e -> {
            long endUs = Math.round(exchange.getStartTime().getMsTime() * 1000d);
            long startUs = endUs - 500_000L;
            exchange.getViewGst().play(startUs, endUs);
        });
        toolBar.add(btnPlayBefore);

        JButton btnPlayBegin = new JButton(Loader.fromResource(
                "/images/16-play-to-start.png", 16, 16));
        btnPlayBegin.setToolTipText(Loader.language("tooltip.play.begin", "Play beginning"));
        btnPlayBegin.addActionListener(e -> {
            long startUs = Math.round(exchange.getStartTime().getMsTime() * 1000d);
            long endUs = startUs + 500_000L;
            exchange.getViewGst().play(startUs, endUs);
        });
        toolBar.add(btnPlayBegin);

        JButton btnPlayEnd = new JButton(Loader.fromResource(
                "/images/16-play-to-end.png", 16, 16));
        btnPlayEnd.setToolTipText(Loader.language("tooltip.play.ending", "Play ending"));
        btnPlayEnd.addActionListener(e -> {
            long endUs = Math.round(exchange.getEndTime().getMsTime() * 1000d);
            long startUs = endUs - 500_000L;
            exchange.getViewGst().play(startUs, endUs);
        });
        toolBar.add(btnPlayEnd);

        JButton btnPlayAfter = new JButton(Loader.fromResource(
                "/images/16-play-end-to.png", 16, 16));
        btnPlayAfter.setToolTipText(Loader.language("tooltip.play.after", "Play after"));
        btnPlayAfter.addActionListener(e -> {
            long startUs = Math.round(exchange.getEndTime().getMsTime() * 1000d);
            long endUs = startUs + 500_000L;
            exchange.getViewGst().play(startUs, endUs);
        });
        toolBar.add(btnPlayAfter);

        JButton btnKaraokeRules = new JButton(Loader.fromResource(
                "/images/16-characters.png", 16, 16));
        btnKaraokeRules.setToolTipText(Loader.language("tooltip.karaoke.rules", "Karaoke rules"));
        btnKaraokeRules.addActionListener(e -> {

        });
        toolBar.add(btnKaraokeRules);

        JButton btnKaraokeOn = new JButton(Loader.fromResource(
                "/images/16-characters-green.png", 16, 16));
        btnKaraokeOn.setToolTipText(Loader.language("tooltip.karaoke.on", "Create Karaoke"));
        btnKaraokeOn.addActionListener(e -> {
            waveform.setUseSpectrum(true);
            tbSpectrum.setSelected(true);
            waveform.repaint();
        });
        toolBar.add(btnKaraokeOn);

        JButton btnKaraokeOff = new JButton(Loader.fromResource(
                "/images/16-characters-red.png", 16, 16));
        btnKaraokeOff.setToolTipText(Loader.language("tooltip.karaoke.off", "Validate Karaoke"));
        btnKaraokeOff.addActionListener(e -> {
            waveform.setUseSpectrum(false);
            tbSpectrum.setSelected(false);
            waveform.repaint();
        });
        toolBar.add(btnKaraokeOff);

        JButton btnKaraokeSU = new JButton(Loader.fromResource(
                "/images/16-circle-up.png", 16, 16));
        btnKaraokeSU.setToolTipText(Loader.language("tooltip.karaoke.su", "Take the current sentence"));
        btnKaraokeSU.addActionListener(e -> {

        });
        toolBar.add(btnKaraokeSU);

        JButton btnKaraokeSLeft = new JButton(Loader.fromResource(
                "/images/16-circle-left.png", 16, 16));
        btnKaraokeSLeft.setToolTipText(Loader.language("tooltip.karaoke.sl", "Previous syllable"));
        btnKaraokeSLeft.addActionListener(e -> {

        });
        toolBar.add(btnKaraokeSLeft);

        JButton btnKaraokeSR = new JButton(Loader.fromResource(
                "/images/16-circle-right.png", 16, 16));
        btnKaraokeSR.setToolTipText(Loader.language("tooltip.karaoke.sr", "Next syllable"));
        btnKaraokeSR.addActionListener(e -> {

        });
        toolBar.add(btnKaraokeSR);

        JButton btnKaraokeSD = new JButton(Loader.fromResource(
                "/images/16-circle-down.png", 16, 16));
        btnKaraokeSD.setToolTipText(Loader.language("tooltip.karaoke.sd", "Get the karaoke"));
        btnKaraokeSD.addActionListener(e -> {

        });
        toolBar.add(btnKaraokeSD);

        JButton btnKaraokeSA = new JButton(Loader.fromResource(
                "/images/16-tasks.png", 16, 16));
        btnKaraokeSA.setToolTipText(Loader.language("tooltip.karaoke.sa", "Make it!"));
        btnKaraokeSA.addActionListener(e -> {

        });
        toolBar.add(btnKaraokeSA);

        JButton btnKaraokeDone = new JButton(Loader.fromResource(
                "/images/16-done.png", 16, 16));
        btnKaraokeDone.setToolTipText(Loader.language("tooltip.karaoke.done", "Go to next line"));
        btnKaraokeDone.addActionListener(e -> {

        });
        toolBar.add(btnKaraokeDone);

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
