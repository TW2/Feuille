package org.wingate.feuille.engine;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.event.EventListenerList;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FFMpeg implements Runnable {

    private volatile Thread playThread;
    private volatile boolean running;
    private volatile String media;
    private volatile long startTime;
    private volatile long endTime;
    private final List<Long> keyFrames;
    private long mediaLength;
    private int mediaFrameCount;

    private volatile FFmpegFrameGrabber grabber = null;
    private volatile SourceDataLine soundLine = null;
    private volatile ExecutorService audioExecutor = null;
    private volatile ExecutorService imageExecutor = null;
    private final Java2DFrameConverter converter;

    public FFMpeg() {
        playThread = null;
        running = false;
        media = null;
        startTime = -1L;
        endTime = -1L;
        keyFrames = new ArrayList<>();
        mediaLength = -1L;
        mediaFrameCount = -1;

        converter = new Java2DFrameConverter();
    }

    public void setMedia(String path){
        media = path;
        if(grabber != null){
            free();
            grabber = null;
        }
        if(path != null){
            grabber = new FFmpegFrameGrabber(path);
            try{
                String ffprobe = Loader.load(org.bytedeco.ffmpeg.ffprobe.class);
                ProcessBuilder pb = new ProcessBuilder(
                        ffprobe,
                        "-i", String.format("\"%s\"", path),
                        "-loglevel", "error",
                        "-select_streams", "v:0",
                        "-show_entries", "packet=pts_time,flags",
                        "-of", "csv=print_section=0");
                Process p = pb.redirectErrorStream(true).start();

                try(BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                    String line;
                    while((line = br.readLine()) != null){
                        if(line.contains("K")){
                            double value = Double.parseDouble(line.substring(0, line.indexOf(",")));
                            keyFrames.add(Math.round(value * 1_000_000d));
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void free(){
        if(grabber != null){
            try {
                grabber.stop();
                grabber.release();
                if (soundLine != null) {
                    soundLine.stop();
                }
                audioExecutor.shutdownNow();
                audioExecutor.awaitTermination(10L, TimeUnit.SECONDS);
                imageExecutor.shutdownNow();
                imageExecutor.awaitTermination(10L, TimeUnit.SECONDS);
            } catch (FrameGrabber.Exception | InterruptedException e) {
                System.err.println("Grabber has tried to be free! (free() at FFmpeg.java)");
            }
        }
    }

    public void play(){
        if(media == null) return;
        try{
            startThread();
        }catch(Exception _){
            System.err.println("Something goes wrong when you are tried to play a media!");
        }
    }

    public void pause(){
        if(media == null) return;
        if (playThread == null) return;
        if (running) {
            running = false;
        } else {
            running = true;
        }
    }

    public void stop(){
        if(media == null) return;
        if (playThread == null) return;
        running = false;
    }

    public void setStartTime(long micros){
        startTime = micros;
    }

    public void setEndTime(long micros){
        endTime = micros;
    }

    public boolean isRunning() {
        return running;
    }

    public List<Long> getKeyFrames() {
        return keyFrames;
    }

    public long getMediaLength() {
        return mediaLength;
    }

    public int getMediaFrameCount() {
        return mediaFrameCount;
    }

    @Override
    public void run() {
        while(true){
            if(running){
                // It is bad the media loops.
                // If you don't want to loop then consider modify.
                doProcess();
            }
        }
    }

    private static class PlaybackTimer {
        private Long startTime = -1L;
        private final DataLine soundLine;

        public PlaybackTimer(DataLine soundLine) {
            this.soundLine = soundLine;
        }

        public PlaybackTimer() {
            this.soundLine = null;
        }

        public void start() {
            if (soundLine == null) {
                startTime = System.nanoTime();
            }
        }

        public long elapsedMicros() {
            if (soundLine == null) {
                if (startTime < 0) {
                    throw new IllegalStateException("PlaybackTimer not initialized.");
                }
                return (System.nanoTime() - startTime) / 1000;
            } else {
                return soundLine.getMicrosecondPosition();
            }
        }
    }

    public void startThread() {
        stopThread();
        if(playThread == null || (playThread != null &&
                (playThread.isInterrupted() || !running))){
            if(playThread == null){
                playThread = new Thread(this);
                playThread.start();
            }
            running = true;
        }
    }

    public void stopThread() {
        if(playThread != null && !playThread.isInterrupted()){
            running = false;
            playThread.interrupt();
            playThread = null;
        }
    }

    private void doProcess(){
        try {
            grabber.start();

            mediaLength = grabber.getLengthInTime();
            mediaFrameCount = grabber.getLengthInVideoFrames();

            final PlaybackTimer playbackTimer;
            if (grabber.getAudioChannels() > 0) {
                final AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);

                final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                soundLine = (SourceDataLine) AudioSystem.getLine(info);
                soundLine.open(audioFormat);
                soundLine.start();
                playbackTimer = new PlaybackTimer(soundLine);
            } else {
                soundLine = null;
                playbackTimer = new PlaybackTimer();
            }
            audioExecutor = Executors.newSingleThreadExecutor();
            imageExecutor = Executors.newSingleThreadExecutor();

            final long maxReadAheadBufferMicros = 1000 * 1000L;

            long lastTimeStamp = -1L;
            while (!Thread.interrupted()) {
                if(running){
                    //==============================================
                    // DESIRED START TRIGGER - AREA START
                    //==============================================
                    if(startTime != -1L){
                        grabber.setTimestamp(startTime);
                        startTime = -1L;
                    }
                    //==============================================
                    // DESIRED START TRIGGER - AREA END
                    //==============================================
                    final Frame frame = grabber.grab();
                    if (frame == null) {
                        break;
                    }
                    if (lastTimeStamp < 0) {
                        playbackTimer.start();
                    }
                    lastTimeStamp = frame.timestamp;
                    //==============================================
                    // DESIRED END TRIGGER - AREA START
                    //==============================================
                    if((frame.image != null || frame.samples != null)
                            && !playThread.isInterrupted() && endTime != -1L){
                        if(frame.timestamp >= endTime){
                            endTime = -1L;
                            running = false;
                            continue;
                        }
                    }
                    //==============================================
                    // DESIRED END TRIGGER - AREA END
                    //==============================================
                    if (frame.image != null) {
                        final Frame imageFrame = frame.clone();

                        imageExecutor.submit(() -> {
                            final BufferedImage image = converter.convert(imageFrame);
                            final long timeStampDeltaMicros = imageFrame.timestamp - playbackTimer.elapsedMicros();
                            imageFrame.close();
                            if (timeStampDeltaMicros > 0) {
                                final long delayMillis = timeStampDeltaMicros / 1000L;
                                try {
                                    Thread.sleep(delayMillis);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }

                            //==============================================
                            // EVENT TRIGGER - AREA START
                            //==============================================

                            FFMessage m = new FFMessage(
                                    imageFrame.timestamp,
                                    grabber.getFrameRate(),
                                    image
                            );

                            fireMediaEvent(m);

                            //==============================================
                            // EVENT TRIGGER - AREA END
                            //==============================================
                        });
                    } else if (frame.samples != null) {
                        if (soundLine == null) {
                            throw new IllegalStateException("Internal error: sound playback not initialized");
                        }
                        final ShortBuffer channelSamplesShortBuffer = (ShortBuffer) frame.samples[0];
                        channelSamplesShortBuffer.rewind();

                        final ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesShortBuffer.capacity() * 2);

                        for (int i = 0; i < channelSamplesShortBuffer.capacity(); i++) {
                            short val = channelSamplesShortBuffer.get(i);
                            outBuffer.putShort(val);
                        }

                        audioExecutor.submit(new Runnable() {
                            public void run() {
                                soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
                                outBuffer.clear();
                            }
                        });
                    }
                    final long timeStampDeltaMicros = frame.timestamp - playbackTimer.elapsedMicros();
                    if (timeStampDeltaMicros > maxReadAheadBufferMicros) {
                        Thread.sleep((timeStampDeltaMicros - maxReadAheadBufferMicros) / 1000);
                    }
                }

            }

            if (!Thread.interrupted()) {
                long delay = (lastTimeStamp - playbackTimer.elapsedMicros()) / 1000 +
                        Math.round(1 / grabber.getFrameRate() * 1000);
                Thread.sleep(Math.max(0, delay));
            }

            free();

        } catch (Exception _) {

        }
    }

    //=======================================================
    //=======================================================
    //-------------------------------------------------------
    // EVENTS
    //=======================================================

    private final EventListenerList listeners = new EventListenerList();

    public Object[] getListeners(){
        return listeners.getListenerList();
    }

    public void addMediaListener(IFFMessageListener listener){
        listeners.add(FFMessageListener.class, (FFMessageListener) listener);
    }

    public void removeMediaListener(IFFMessageListener listener){
        listeners.remove(FFMessageListener.class, (FFMessageListener) listener);
    }

    protected void fireMediaEvent(FFMessage message){
        for(Object o : getListeners()){
            if(o instanceof FFMessageListener listen){
                listen.updatedMessage(message);
                break;
            }
        }
    }

    public static class FFMessage {
        private final long currentMicro;
        private final BufferedImage image;
        private final double fps;
        private final int frame;

        public FFMessage(long currentMicro, double fps, BufferedImage image) {
            this.currentMicro = currentMicro;
            this.fps = fps;
            this.image = image;
            frame = (int) (currentMicro <= 0L ? 0 : fps * (double)currentMicro / 1_000_000d);
        }

        public long getCurrentMicro() {
            return currentMicro;
        }

        public BufferedImage getImage() {
            return image;
        }

        public double getFps() {
            return fps;
        }

        public int getFrame() {
            return frame;
        }
    }

    public interface IFFMessageListener {
        void updatedMessage(FFMessage ffm);
    }

    public static abstract class FFMessageListener implements IFFMessageListener, EventListener {
        // Nothing to do
    }
}
