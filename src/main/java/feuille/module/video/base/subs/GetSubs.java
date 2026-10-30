package feuille.module.video.base.subs;

import feuille.module.editor.assa.ASS;

import javax.swing.event.EventListenerList;
import java.awt.image.BufferedImage;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class GetSubs implements Runnable {

    private Thread thread;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    private final long delayMs;
    private long currentMs = -1L;
    private final Map<Long, BufferedImage> images;

    private LibassSubtitleRenderer renderer;

    public GetSubs(long delayMs) {
        this.delayMs = delayMs;
        this.images = new HashMap<>();
        renderer = null;
    }

    public void setup(int width, int height) {
        renderer = new LibassSubtitleRenderer(width, height);
    }

    public void changeInput(ASS ass){
        if(renderer == null) return;
        renderer.loadTrack(ASS.stringify(ass));
    }

    public void clearMap(){
        images.clear();
    }

    public void setCurrentMs(long currentMs) {
        if(currentMs < 0L) return;
        this.currentMs = currentMs;
    }

    public void start(){
        if(currentMs == -1L) return;
        if(renderer == null) return;
        if(thread != null) return;
        running = true;
        paused = false;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){
        paused = true;
    }

    public void stop(){
        if(renderer == null) return;
        if(thread != null && (thread.isAlive() || !thread.isInterrupted())){
            running = false;
            paused = false;
            thread.interrupt();
            thread = null;
        }
    }

    public void free(){
        if(renderer == null) return;
        stop();
        renderer.close();
        renderer = null;
    }

    @Override
    public void run() {
        long startTime;
        long time;
        while(running){
            if(paused) continue;
            time = currentMs + delayMs;
            startTime = System.nanoTime();
            if(images.size() <= (delayMs)){
                if(!images.containsKey(time)){
                    images.put(time, renderer.render(time));
                }
            }
            long passedTime = System.nanoTime() - startTime;
            if(passedTime >= 1_000_000L){
                currentMs += (passedTime / 1_000_000L);
            }
            if(!images.isEmpty() && images.containsKey(currentMs)){
                BufferedImage image = images.get(currentMs);
                fireImageQueued(new ImageEvent(image, time));
                images.remove(currentMs);
            }
        }
    }

    public static BufferedImage getScreenshot(ASS ass, int width, int height, long ms){
        BufferedImage image;
        try(LibassSubtitleRenderer r = new LibassSubtitleRenderer(width, height)){
            r.loadTrack(ASS.stringify(ass));
            image = r.render(ms);
        }
        return image;
    }

    public interface ImageListener extends EventListener {
        void imageQueued(ImageEvent event);
    }

    public static class ImageEvent {
        private final BufferedImage image;
        private final long us;

        public ImageEvent(BufferedImage image, long us) {
            this.image = image;
            this.us = us;
        }

        public BufferedImage getImage() {
            return image;
        }

        public long getUs() {
            return us;
        }
    }

    public final EventListenerList listeners = new EventListenerList();

    public Object[] getListeners(){
        return listeners.getListenerList();
    }

    public void addImageListener(ImageListener listener) {
        listeners.add(ImageListener.class, listener);
    }

    public void removeImageListener(ImageListener listener) {
        listeners.remove(ImageListener.class, listener);
    }

    public void fireImageQueued(ImageEvent event) {
        for(Object o : getListeners()){
            if(o instanceof ImageListener listen){
                listen.imageQueued(event);
                break;
            }
        }
    }
}
