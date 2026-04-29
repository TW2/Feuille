package feuille.module.audio;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.AudioFormat;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.EventListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FFAudio {

    private FFmpegFrameGrabber grabber;

    public FFAudio() {
        grabber = null;
    }

    public boolean setMedia(String path) throws FFmpegFrameGrabber.Exception {
        if(grabber != null){
            grabber.stop();
            grabber.release();
            grabber = null;
        }

        if(path != null){
            File f = new File(path);
            if(f.exists() && f.isFile()){
                grabber = new FFmpegFrameGrabber(path);
                return true;
            }
        }

        return false;
    }

    public void execute(long msStart, long msEnd, int width, int height, Color c){
        try(ExecutorService service = Executors.newSingleThreadExecutor()){
            service.submit(() -> {
                try{
                    BufferedImage current = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    BufferedImage next = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                    long msDuration = Math.max(msStart, msEnd) - Math.min(msStart, msEnd);
                    long nextEnd = msEnd + msDuration;

                    grabber.start();
                    process(service, msStart, msEnd, current, width, height, c);
                    process(service, msEnd, nextEnd, next, width, height, c);

                    FFAudioEvent event = new FFAudioEvent(
                            grabber.getLengthInTime(),
                            msStart, msEnd,
                            msEnd, nextEnd,
                            current,
                            next
                    );

                    fireSignalImage(event);

                    grabber.stop();
                }catch(Exception _){

                }
            });
            service.shutdownNow();
        }

    }

    private void process(ExecutorService service, long msStart, long msEnd, BufferedImage image,
                         int width, int height, Color c) {
        try {
            AudioFormat format = null;

            if (grabber.getAudioChannels() > 0) {
                format = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);
            }else{
                grabber.stop();
                service.shutdownNow();
            }
            assert format != null;

            if(msStart > 0L){
                grabber.setTimestamp(msStart);
            }

            Graphics2D g = image.createGraphics();

            boolean stop = false;

            try {
                long msBetween = msStart;
                while(!stop){
                    final Frame frame;
                    frame = grabber.grab();

                    if (frame == null) {
                        break; // TODO EOF
                    }
                    if (frame.samples != null) {
                        final ShortBuffer channelSamplesShortBuffer = (ShortBuffer) frame.samples[0];
                        channelSamplesShortBuffer.rewind();

                        final ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesShortBuffer.capacity() * 2);

                        for (int i = 0; i < channelSamplesShortBuffer.capacity(); i++) {
                            short val = channelSamplesShortBuffer.get(i);
                            outBuffer.putShort(val);
                        }

                        drawWaveform(g, msStart, msEnd, width, height, c, format, outBuffer.array(), msBetween);

                        stop = isEnd(format, msEnd, msBetween, outBuffer.array().length);

                        msBetween += outBuffer.array().length;
                    }
                }
            } catch (FFmpegFrameGrabber.Exception ex) {
                Logger.getLogger(FFAudio.class.getName()).log(Level.SEVERE, null, ex);
            }

            g.dispose();
        } catch (FFmpegFrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
    }

    private double getX(long msStart, long msStop, int width, long msCurrent){
        double total = msStop - msStart;
        double difference = msStop - msCurrent;
        double ratio = difference / Math.max(1d, total);
        return width - width * ratio;
    }

    private boolean isEnd(AudioFormat format, long msStop, long bytesRead, long nowSample){
        // Seconds = bytesFromStart / frameSize * framerate
        double positionInSeconds = (bytesRead + nowSample) / Math.max(1L, format.getFrameSize() * format.getFrameRate());
        long ms = Math.round(positionInSeconds * 1000L);
        return ms >= msStop;
    }

    private void drawWaveform(Graphics2D g, long msStart, long msStop, int width, int height, Color c, AudioFormat format, byte[] samples, long elapsed){
        double x;
        double y;

        if(format.getSampleSizeInBits() == 16){
            // en stéréo (16)
            int samplesLength = samples.length / 2;

            if(format.isBigEndian()){
                for(int i=0; i<samplesLength; i++){
                    // Most significant bit, Low significant bit
                    // First MSB (high order), second LSB (low order)
                    int MSB = (int)samples[2*i];
                    int LSB = (int)samples[2*i+1];
                    int value = MSB << 8 | (255 & LSB);
                    byte b = (byte)(128 * value / 32768);

                    // Dessin
                    double positionInSeconds = (elapsed + i+1) / Math.max(1L, format.getFrameSize() * format.getFrameRate());
                    long msCurrent = Math.round(positionInSeconds * 1000L);
                    x = getX(msStart, msStop, width, msCurrent);
                    y = (double) (height * (128 - b)) / 256;
                    Line2D shape = new Line2D.Double(x, (double) height /2, x, y);
                    g.setColor(c);
                    g.draw(shape);

                    // Is the end?
                    if(isEnd(format, msStop, elapsed, i+1)){
                        break;
                    }
                }
            }else{
                for(int i=0; i<samplesLength; i++){
                    // Most significant bit, Low significant bit
                    // First LSB (low order), second MSB (high order)
                    int LSB = (int)samples[2*i];
                    int MSB = (int)samples[2*i+1];
                    int value = MSB << 8 | (255 & LSB);
                    byte b = (byte)(128 * value / 32768);

                    // Dessin
                    double positionInSeconds = (elapsed + i+1) / Math.max(1L, format.getFrameSize() * format.getFrameRate());
                    long msCurrent = Math.round(positionInSeconds * 1000L);
                    x = getX(msStart, msStop, width, msCurrent);
                    y = (double) (height * (128 - b)) / 256;
                    Line2D shape = new Line2D.Double(x, (double) height /2, x, y);
                    g.setColor(c);
                    g.draw(shape);

                    // Is the end?
                    if(isEnd(format, msStop, elapsed, i+1)){
                        break;
                    }
                }
            }
        }
    }

    private final EventListenerList listeners = new EventListenerList();

    public void addSignalListener(FFAudioInterface listener){
        listeners.add(FFAudioListener.class, (FFAudioListener)listener);
    }

    public void removeSignalListener(FFAudioInterface listener){
        listeners.remove(FFAudioListener.class, (FFAudioListener)listener);
    }

    public Object[] getListeners(){
        return listeners.getListenerList();
    }

    protected void fireSignalImage(FFAudioEvent event){
        for(Object o : getListeners()){
            if(o instanceof FFAudioListener listen){
                listen.getSignal(event);
                break;
            }
        }
    }

    public interface FFAudioInterface {
        void getSignal(FFAudioEvent event);
    }

    public static abstract class FFAudioListener implements FFAudioInterface, EventListener {
        // Nothing here
    }
}
