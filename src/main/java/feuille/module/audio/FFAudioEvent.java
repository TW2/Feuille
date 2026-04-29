package feuille.module.audio;

import java.awt.image.BufferedImage;

public class FFAudioEvent {
    private final long audioMsLength;
    private final long currentMsStart;
    private final long currentMsEnd;
    private final long nextMsStart;
    private final long nextMsEnd;
    private final BufferedImage current;
    private final BufferedImage next;

    public FFAudioEvent(long audioMicrosLength,
                        long currentMsStart, long currentMsEnd,
                        long nextMsStart, long nextMsEnd,
                        BufferedImage current, BufferedImage next) {
        this.audioMsLength = audioMicrosLength;
        this.currentMsStart = currentMsStart;
        this.currentMsEnd = currentMsEnd;
        this.nextMsStart = nextMsStart;
        this.nextMsEnd = nextMsEnd;
        this.current = current;
        this.next = next;
    }

    public long getAudioMsLength() {
        return audioMsLength;
    }

    public long getCurrentMsStart() {
        return currentMsStart;
    }

    public long getCurrentMsEnd() {
        return currentMsEnd;
    }

    public long getNextMsStart() {
        return nextMsStart;
    }

    public long getNextMsEnd() {
        return nextMsEnd;
    }

    public BufferedImage getCurrent() {
        return current;
    }

    public BufferedImage getNext() {
        return next;
    }
}
