package feuille.module.video.base.image;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public final class VideoSurface extends JLayeredPane {

    private final JComponent videoComponent;
    private final SubtitleOverlay subtitleOverlay;

    public VideoSurface(JComponent videoComponent) {
        this.videoComponent = videoComponent;
        this.subtitleOverlay = new SubtitleOverlay();

        add(videoComponent, Integer.valueOf(0));
        add(subtitleOverlay, Integer.valueOf(100));

        setLayout(null);
    }

    @Override
    public void doLayout() {
        videoComponent.setBounds(0, 0, getWidth(), getHeight());
        subtitleOverlay.setBounds(0, 0, getWidth(), getHeight());
    }

    public SubtitleOverlay getSubtitleOverlay() {
        return subtitleOverlay;
    }
}

