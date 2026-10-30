package feuille.module.video.base.wave;

import feuille.util.Exchange;

/**
 * Playground is a class for the display of current time in WaveForm/Spectrum
 * and status time bar that appears below of the video.
 * It takes care also of the keyframe and like Aegisub in playing, changes
 * directly the current events highlight by a listener system.
 */
public class Playground implements Runnable {

    public enum State {
        Play,
        Pause,
        Stop
    }

    private State state;
    private State last;

    private final Exchange exchange;
    private volatile boolean running;
    private volatile boolean paused;
    private Thread thread;
    private long currentTime;
    private long startUs;
    private long endUs;
    private double fps;

    public Playground(Exchange exchange) {
        this.exchange = exchange;
        state = State.Stop;

        thread = null;
        running = false;
        currentTime = 0L;
        startUs = 0L;
        endUs = 0L;
        fps = 24000d / 1001d;
    }

    public void play(){
        state = State.Play;
        running = true;
        paused = false;
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    public void pause(){
        state = State.Pause;
        paused = true;
    }

    public void stop(){
        state = State.Stop;
        paused = false;
        running = false; // Stop the drawing
        if(thread != null && (thread.isAlive() || !thread.isInterrupted())){
            thread.interrupt();
            thread = null;
        }
        usReset(); // Reset period values
    }

    public void setFps(double fps){
        this.fps = fps;
    }

    public double getFps(){
        return fps;
    }

    public void usDiscovered(long startUs, long endUs) {
        this.startUs = startUs;
        this.endUs = endUs;
    }

    public void usReset(){
        this.startUs = 0;
        this.endUs = 0;
    }

    @Override
    public void run() {
        long time;
        long startTime = System.nanoTime();
        long to = 0;
        boolean controlSet = false;
        while(running){
            if(!paused){
                if(last == State.Pause){
                    startTime = System.nanoTime() - currentTime;
                }
                if(state == State.Play){
                    last = State.Play;
                    time = System.nanoTime() - startTime;
                    currentTime = time;
                    if(startUs != 0 && endUs != 0){
                        if(!controlSet){
                            to = System.nanoTime() + ((endUs * 1000) - (startUs * 1000));
                            controlSet = true;
                        }
                        currentTime = time + (startUs * 1000L);
                        if(System.nanoTime() >= to){
                            stop();
                        }
                    }
                }
                exchange.getSyncPane().getWaveform().setMsCurrent(currentTime / 1_000_000);
                exchange.getSyncPane().getWaveform().repaint();
            }
        }
    }
}
