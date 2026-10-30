package feuille.module.video.base.image;

import feuille.module.video.base.subs.LibassSubtitleRenderer;

import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public final class AssSubtitleController implements AutoCloseable {

    private volatile boolean hasTrack;

    private final LibassSubtitleRenderer renderer;
    private final SubtitleOverlay overlay;
    private final LongSupplier positionSupplier;

    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread thread = new Thread(r, "ASS-subtitle-renderer");
                thread.setDaemon(true);
                return thread;
            });

    private volatile boolean closed;
    private volatile boolean visible = true;
    private volatile long trackGeneration;

    public AssSubtitleController(
            LibassSubtitleRenderer renderer,
            SubtitleOverlay overlay,
            LongSupplier positionSupplier) {

        this.renderer = Objects.requireNonNull(renderer);
        this.overlay = Objects.requireNonNull(overlay);
        this.positionSupplier = Objects.requireNonNull(positionSupplier);

        executor.scheduleWithFixedDelay(
                this::renderSafely,
                0,
                33,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * Remplace la piste ASS complète.
     * La vidéo continue de jouer.
     *
     * L'appel est asynchrone : le chargement est effectué
     * par le thread de rendu, pas par l'EDT.
     */
    public void loadTrack(String assText) {
        Objects.requireNonNull(assText, "assText");

        final byte[] utf8 = assText.getBytes(
                java.nio.charset.StandardCharsets.UTF_8
        );

        final long generation = ++trackGeneration;

        SwingUtilities.invokeLater(overlay::clearSubtitles);

        executor.execute(() -> {
            if (closed || generation != trackGeneration) {
                return;
            }

            try {
                hasTrack = false;
                renderer.loadTrack(utf8);
                hasTrack = true;
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }

            // Le prochain cycle de rendu utilisera la position
            // courante de la vidéo avec la nouvelle piste.
        });
    }

    public void setVisible(boolean visible) {
        this.visible = visible;

        if (!visible) {
            SwingUtilities.invokeLater(overlay::clearSubtitles);
        }
    }

    private void renderSafely() {
        if (closed || !visible || !hasTrack) {
            return;
        }

        final long generation = trackGeneration;

        try {
            long positionMs = positionSupplier.getAsLong();

            if (positionMs < 0 || closed || !visible) {
                return;
            }

            BufferedImage image = renderer.render(positionMs);

            // Ne pas publier une image appartenant à une ancienne piste.
            if (closed || !visible || generation != trackGeneration) {
                return;
            }

            SwingUtilities.invokeLater(() -> {
                if (!closed
                        && visible
                        && generation == trackGeneration) {

                    overlay.setSubtitleImage(image);
                }
            });

        } catch (RuntimeException ex) {
            if (!closed) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        closed = true;
        trackGeneration++;

        executor.shutdownNow();

        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                System.err.println(
                        "Le thread de rendu ASS ne s'est pas arrêté à temps."
                );
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        renderer.close();

        SwingUtilities.invokeLater(overlay::clearSubtitles);
    }
}
