package feuille.module.video;

import feuille.util.Exchange;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;

public class ViewVLC extends JPanel {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private String mrl;

    public ViewVLC(Exchange exchange){
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        mrl = null;

        setLayout(new BorderLayout());
        add(mediaPlayerComponent, BorderLayout.CENTER);
        Timer timer = new Timer(50, e -> {
            if(mrl == null) return;
            if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
                exchange.getSyncPane().getWaveform().setMsCurrent(
                        mediaPlayerComponent.mediaPlayer().status().time()
                );
                exchange.getSyncPane().getWaveform().repaint();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    public void setMrl(String mrl){
        this.mrl = mrl;
    }

    public void play(double secondsStart, double secondsEnd){
        if(mrl == null) return;
        mediaPlayerComponent.mediaPlayer().media().play(mrl,
                String.format(":start-time=%.01f", secondsStart),
                String.format(":run-time=%.01f", secondsEnd - secondsStart));
    }

    public void play(double secondsStart){
        if(mrl == null) return;
        mediaPlayerComponent.mediaPlayer().media().play(mrl,
                String.format(":start-time=%.01f", secondsStart));
    }

    public void play(){
        if(mrl == null) return;
        mediaPlayerComponent.mediaPlayer().media().play(mrl);
    }

    public void pause(){
        if(mrl == null) return;
        if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
            mediaPlayerComponent.mediaPlayer().controls().pause();
        }else{
            mediaPlayerComponent.mediaPlayer().controls().play();
        }
    }

    public void stop(){
        if(mrl == null) return;
        if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
            mediaPlayerComponent.mediaPlayer().controls().stop();
        }
    }

    public void close(){
        if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
            mediaPlayerComponent.mediaPlayer().controls().stop();
        }
    }

    public float getPosition(){
        if(mrl == null) return -1f;
        if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
            return mediaPlayerComponent.mediaPlayer().status().position();
        }
        return -1f;
    }

    public long getDuration(){
        if(mrl == null) return -1L;
        if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
            return mediaPlayerComponent.mediaPlayer().status().length();
        }
        return -1L;
    }

    public long getCurrentTime(){
        if(mrl == null) return -1L;
        if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
            return mediaPlayerComponent.mediaPlayer().status().time();
        }
        return -1L;
    }

    public int getVolume(){
        if(mrl == null) return -1;
        if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
            return mediaPlayerComponent.mediaPlayer().audio().volume();
        }
        return -1;
    }

    public void setVolume(int volume){
        if(mrl == null) return;
        if(mediaPlayerComponent.mediaPlayer().status().isPlaying()){
            mediaPlayerComponent.mediaPlayer().audio().setVolume(volume);
        }
    }

}
