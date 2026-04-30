package feuille.module.audio;

import java.util.EventListener;

public interface FFAudioListener extends EventListener {
    void getSignal(FFAudioEvent event);
}
