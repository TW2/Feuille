package feuille.module.fmp.ai;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import feuille.util.Exchange;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FMP stands for Free Media Player and has been created by
 * AI (Copilot in VsCodeInsiders under Linux - Ubuntu 26.04).
 * If you want it, see fmp.zip inside the project docs folder.
 * A small side project that integrates Java/JNA can be found
 * with the source and is replicated in FmpSwingPlayer class.
 * It's the same, it's used to test the good working of fmp
 * library. Have fun to continue talk with AI.
 */
public class FreeMediaPlayer {

    /** FMP EVENT TYPE to use in type of FmpEvent */
    public enum Event {
        NONE(0),     // private static final int EVENT_NONE = 0;
        VIDEO(1),    // private static final int EVENT_VIDEO = 1;
        AUDIO(2);    // private static final int EVENT_AUDIO = 2;

        private final int value;
        Event(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /** FMP NEXT RESULT */
    public enum Next {
        ERROR(-1), // private static final int NEXT_ERROR = -1;
        EOF(0),    // private static final int NEXT_EOF = 0;
        EVENT(1),  // private static final int NEXT_EVENT = 1;
        PAUSED(2); // private static final int NEXT_PAUSED = 2;

        private final int value;
        Next(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /** FMP VIDEO PROFILE */
    public enum VideoProfile {
        OTHER(0),       // private static final int VIDEO_PROFILE_OTHER = 0;
        TELECINE(1),    // private static final int VIDEO_PROFILE_NTSC_TELECINE = 1;
        FILM(2),        // private static final int VIDEO_PROFILE_NTSC_FILM = 2;
        PAL25(3);       // private static final int VIDEO_PROFILE_PAL_25 = 3;

        private final int value;
        VideoProfile(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /** FMP SCREENSHOT Background Type */
    public enum BackgroundType {
        VIDEO(0),
        CHECKERBOARD(1),
        TRANSPARENT(2);

        private final int value;
        BackgroundType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /* FMP AUDIO */
    private static final int AUDIO_RATE = 48_000;
    private static final int FALLBACK_FPS = 30;
    private static final int MAX_QUEUED_VIDEO_FRAMES = 5;

    @Structure.FieldOrder({"type", "pts_us", "duration_us", "data", "data_size",
            "width", "height", "stride", "sample_rate", "channels"})
    public static class FmpEvent extends Structure {
        public int type; // 0 None, 1 Video, 2 Audio (enum Event)
        public long pts_us;
        public long duration_us;
        public Pointer data;
        public int data_size;
        public int width;
        public int height;
        public int stride;
        public int sample_rate;
        public int channels;
    }

    @Structure.FieldOrder({"pts_us", "data", "data_size", "width", "height", "stride"})
    public static class FmpScreenshot extends Structure {
        public long pts_us;
        /* Packed RGBA, top row first. */
        public Pointer data;
        public int data_size;
        public int width;
        public int height;
        public int stride;
    }

    @Structure.FieldOrder({"background", "width", "height", "square_size",
            "color1_rgba", "color2_rgba"})
    public static class FmpScreenshotOptions extends Structure {
        public int background; // 0 = video, 1 = checkerboard, 2 = transparent
        public int width;
        public int height;
        public int square_size;
        public byte[] color1_rgba = new byte[4];
        public byte[] color2_rgba = new byte[4];
    }

    public interface FmpNative extends Library {
        int fmp_open(String path, PointerByReference outPlayer);
        int fmp_get_video_frame_rate(Pointer player, IntByReference numerator,
                                     IntByReference denominator);
        int fmp_get_video_profile(Pointer player, IntByReference profile,
                                  IntByReference numerator, IntByReference denominator);
        int fmp_load_subtitles(Pointer player, String subtitlePath);
        int fmp_next(Pointer player, FmpEvent event);
        int fmp_play(Pointer player);
        int fmp_play_range(Pointer player, long startTimeUs, long endTimeUs);
        int fmp_pause(Pointer player);
        int fmp_stop(Pointer player);
        int fmp_seek(Pointer player, long positionUs);
        int fmp_screenshot(String mediaPath, long positionUs, String subtitlePath,
                           FmpScreenshot screenshot);
        int fmp_screenshot_with_options(
                String mediaPath, long positionUs, String subtitlePath,
                FmpScreenshotOptions options, FmpScreenshot screenshot);
        void fmp_screenshot_free(FmpScreenshot screenshot);
        int fmp_get_error(Pointer player, byte[] buffer, int bufferSize);
        void fmp_close(Pointer player);
    }

    public static class Loader {

        // The raw library
        private final FmpNative fmp;

        private Loader() {
            // OS choices
            String osName = System.getProperty("os.name");
            int platform = Platform.getOSType();
            switch(platform) {
                case Platform.LINUX -> osName = "linux";
                case Platform.WINDOWS -> osName = "windows";
                case Platform.MAC -> osName = "mac";
            }

            // External path
            File app = new File(new File("").getAbsolutePath());
            File folder = new File(app.getPath() +
                    File.separator + "settings" +
                    File.separator + "dependencies"
            );

            // The path where the shared library is
            String libPath = new File(folder, osName).getPath();

            // The linkage
            System.setProperty("jna.library.path", libPath);

            // The instance
            fmp = Native.load("fmp", FmpNative.class);
        }

        // A getter for the raw library

        public static FmpNative loadFmp() {
            Loader loader = new Loader();
            return loader.fmp;
        }
    }

    public static byte[] setColorToByte(Color c) {
        byte red = (byte) ((c.getRed() & 0xFF) << 8);
        byte green = (byte) ((c.getGreen() & 0xFF) << 8);
        byte blue = (byte) ((c.getBlue() & 0xFF) << 8);
        byte alpha = (byte) ((c.getAlpha() & 0xFF) << 8);
        return new byte[]{red, green, blue, alpha};
    }

    //--------------------------------------------------------------------------------
    private static FmpNative fmp;

    public FreeMediaPlayer() {
        fmp = Loader.loadFmp();
    }

    public FmpNative getFmp() {
        return fmp;
    }

    private String error(Pointer player) {
        byte[] buffer = new byte[1024];
        int length = fmp.fmp_get_error(player, buffer, buffer.length);
        if (length < 0) return "Native FMP error";
        return new String(buffer, 0, length, java.nio.charset.StandardCharsets.UTF_8);
    }

    public static final class VideoFrame {
        final long ptsUs;
        final BufferedImage image;

        VideoFrame(long ptsUs, BufferedImage image) {
            this.ptsUs = ptsUs;
            this.image = image;
        }
    }

    public static final class VideoPanel extends JPanel {
        @Serial
        private static final long serialVersionUID = 1L;
        private transient BufferedImage frame;

        public VideoPanel() {
            setPreferredSize(new Dimension(960, 540));
            setBackground(Color.BLACK);
        }

        void setFrame(BufferedImage nextFrame) {
            if (SwingUtilities.isEventDispatchThread()) {
                frame = nextFrame;
                repaint();
            } else {
                SwingUtilities.invokeLater(() -> setFrame(nextFrame));
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (frame == null) return;

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            double scale = Math.min(
                    (double) panelWidth / frame.getWidth(),
                    (double) panelHeight / frame.getHeight());
            int width = (int) Math.round(frame.getWidth() * scale);
            int height = (int) Math.round(frame.getHeight() * scale);
            int x = (panelWidth - width) / 2;
            int y = (panelHeight - height) / 2;

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(frame, x, y, width, height, null);
            g.dispose();
        }
    }

    public static final class PlaybackController implements AutoCloseable {
        private enum State { PLAYING, PAUSED, STOPPED }

        private final Object stateLock = new Object();
        private final Object frameLock = new Object();
        private final ArrayDeque<VideoFrame> frames = new ArrayDeque<>();
        private final AtomicBoolean closed = new AtomicBoolean();
        private final Pointer player;
        private final VideoPanel videoPanel;
        private final SourceDataLine audioLine;
        private final Timer videoTimer;
        private final Thread worker;
        private final long presentationIntervalNanos;

        private volatile State requestedState = State.STOPPED;
        private volatile long generation;
        private volatile boolean looping;
        private volatile boolean rangePending;
        private volatile boolean seekPending;
        private long pendingSeekPositionUs;
        private long pendingRangeStartUs;
        private long pendingRangeEndUs;
        private long activeRangeStartUs;
        private long activeRangeEndUs;
        private boolean activeRange;
        private State appliedState;
        private volatile boolean audioClockReady;
        private long audioClockBasePtsUs;
        private long audioClockBaseFrame;
        private long audioFramesWritten;
        private long fallbackClockBasePtsUs;
        private long fallbackClockBaseNanos = System.nanoTime();
        private long nextPresentationNanos;
        private final String sourceVideoProfile;

        public PlaybackController(FmpNative fmp, String mediaPath, String subtitlePath, VideoPanel panel)
                throws Exception {
            videoPanel = panel;
            PointerByReference outPlayer = new PointerByReference();
            if (fmp.fmp_open(mediaPath, outPlayer) != 0) {
                throw new IllegalArgumentException(nativeError(null));
            }
            player = outPlayer.getValue();
            if (player == null) {
                throw new IllegalStateException("Native FMP returned a null player handle");
            }

            IntByReference fpsNumerator = new IntByReference();
            IntByReference fpsDenominator = new IntByReference();
            IntByReference profile = new IntByReference();
            double sourceFps = FALLBACK_FPS;
            if (fmp.fmp_get_video_profile(
                    player, profile, fpsNumerator, fpsDenominator) == 0 &&
                    fpsNumerator.getValue() > 0 && fpsDenominator.getValue() > 0) {
                sourceFps = (double) fpsNumerator.getValue() /
                        fpsDenominator.getValue();
            }
            if (!Double.isFinite(sourceFps) || sourceFps <= 0.0) {
                sourceFps = FALLBACK_FPS;
            }
            sourceVideoProfile = switch (profile.getValue()) {
                case 1 -> "NTSC telecine";
                case 2 -> "NTSC film";
                case 3 -> "PAL 25";
                default -> String.format(
                        java.util.Locale.ROOT, "other %.3f FPS", sourceFps);
            };

            if (subtitlePath != null &&
                    fmp.fmp_load_subtitles(player, subtitlePath) != 0) {
                String message = nativeError(player);
                fmp.fmp_close(player);
                throw new IllegalArgumentException(message);
            }

            AudioFormat format = new AudioFormat(AUDIO_RATE, 16, 2, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine openedAudioLine = null;
            try {
                if (!AudioSystem.isLineSupported(info)) {
                    throw new IllegalStateException(
                            "No Java Sound output supports 48 kHz stereo PCM");
                }
                openedAudioLine = (SourceDataLine) AudioSystem.getLine(info);
                openedAudioLine.open(format, 8192);
            } catch (Exception exception) {
                if (openedAudioLine != null) openedAudioLine.close();
                fmp.fmp_close(player);
                throw exception;
            }
            audioLine = openedAudioLine;

            presentationIntervalNanos = Math.max(1L,
                    Math.round(1_000_000_000.0 / sourceFps));
            videoTimer = new Timer(4, event -> presentDueVideo());
            videoTimer.setCoalesce(true);
            videoTimer.start();
            worker = new Thread(this::decodeLoop, "fmp-decoder");
            worker.start();
        }

        public void play() {
            request(PlaybackController.State.PLAYING);
        }

        public void play(long startTimeUs, long endTimeUs) {
            if (startTimeUs < 0 || endTimeUs <= startTimeUs) {
                throw new IllegalArgumentException(
                        "Playback range must be non-negative and end after start");
            }
            generation++;
            synchronized (stateLock) {
                seekPending = false;
                pendingRangeStartUs = startTimeUs;
                pendingRangeEndUs = endTimeUs;
                rangePending = true;
                requestedState = State.PLAYING;
                stateLock.notifyAll();
            }
            synchronized (frameLock) {
                frames.clear();
                frameLock.notifyAll();
            }
            videoPanel.setFrame(null);
        }

        // Possible stack overflow consuming all your memory
//        void seek(long positionUs) {
//            if (positionUs < 0) {
//                throw new IllegalArgumentException("Seek position must be non-negative");
//            }
//            generation++;
//            synchronized (stateLock) {
//                rangePending = false;
//                pendingSeekPositionUs = positionUs;
//                seekPending = true;
//                stateLock.notifyAll();
//            }
//            synchronized (frameLock) {
//                frames.clear();
//                frameLock.notifyAll();
//            }
//            videoPanel.setFrame(null);
//        }

        public void pause() {
            request(PlaybackController.State.PAUSED);
        }

        public void stop() {
            generation++;
            synchronized (stateLock) {
                rangePending = false;
                seekPending = false;
                requestedState = State.STOPPED;
                stateLock.notifyAll();
            }
            synchronized (frameLock) {
                frames.clear();
                frameLock.notifyAll();
            }
            videoPanel.setFrame(null);
        }

        public void setLooping(boolean enabled) {
            looping = enabled;
        }

        private void request(PlaybackController.State state) {
            synchronized (stateLock) {
                requestedState = state;
                stateLock.notifyAll();
            }
        }

        private void decodeLoop() {
            try {
                while (!closed.get()) {
                    applyRequestedState();
                    if (closed.get()) break;
                    if (appliedState != State.PLAYING) {
                        synchronized (stateLock) {
                            if (!closed.get() && requestedState == appliedState &&
                                    !seekPending) {
                                stateLock.wait();
                            }
                        }
                        continue;
                    }

                    long eventGeneration = generation;
                    FmpEvent event = new FmpEvent();
                    int status = fmp.fmp_next(player, event);
                    if (status == Next.PAUSED.getValue()) continue;
                    if (status == Next.EOF.getValue()) {
                        long eofGeneration = generation;
                        waitForAudioToFinish();
                        if (generation != eofGeneration) continue;
                        if (looping && requestedState == State.PLAYING) {
                            restartForLoop();
                            continue;
                        }
                        if (requestedState != State.STOPPED) presentLastQueuedVideo();
                        if (fmp.fmp_stop(player) != 0) {
                            throw new IllegalStateException(nativeError(player));
                        }
                        appliedState = State.STOPPED;
                        requestedState = State.STOPPED;
                        activeRange = false;
                        audioLine.stop();
                        audioLine.flush();
                        resetAudioClock();
                        continue;
                    }
                    if (status == Next.ERROR.getValue()) {
                        throw new IllegalStateException(nativeError(player));
                    }
                    if (status != Next.EVENT.getValue()) {
                        throw new IllegalStateException("Unknown fmp_next result: " + status);
                    }

                    event.read();
                    if (event.data == null || event.data_size <= 0) continue;
                    byte[] data = event.data.getByteArray(0, event.data_size);
                    if (generation != eventGeneration) continue;
                    if (event.type == Event.VIDEO.getValue()) {
                        presentVideo(data, event.width, event.height, event.stride,
                                event.pts_us, eventGeneration);
                        waitForVideoClockIfNeeded(event.pts_us, eventGeneration);
                    } else if (event.type == Event.AUDIO.getValue()) {
                        queueAudio(data, event.sample_rate, event.channels,
                                event.pts_us, eventGeneration);
                    }
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } catch (Exception exception) {
                closed.set(true);
                SwingUtilities.invokeLater(videoTimer::stop);
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        videoPanel, exception.getMessage(), "FMP playback error",
                        JOptionPane.ERROR_MESSAGE));
            } finally {
                fmp.fmp_close(player);
                audioLine.stop();
                audioLine.close();
            }
        }

        private void restartForLoop() {
            audioLine.stop();
            audioLine.flush();
            if (activeRange) {
                if (fmp.fmp_play_range(
                        player, activeRangeStartUs, activeRangeEndUs) != 0) {
                    throw new IllegalStateException(nativeError(player));
                }
            } else {
                if (fmp.fmp_stop(player) != 0) {
                    throw new IllegalStateException(nativeError(player));
                }
                if (fmp.fmp_play(player) != 0) {
                    throw new IllegalStateException(nativeError(player));
                }
            }
            generation++;
            synchronized (frameLock) {
                frames.clear();
                frameLock.notifyAll();
            }
            videoPanel.setFrame(null);
            resetAudioClock();

            if (requestedState != State.PLAYING) {
                appliedState = State.STOPPED;
                return;
            }
            fallbackClockBaseNanos = System.nanoTime();
            appliedState = State.PLAYING;
            audioLine.start();
        }

        private void applyRequestedState() {
            if (seekPending) {
                long positionUs;
                synchronized (stateLock) {
                    positionUs = pendingSeekPositionUs;
                    seekPending = false;
                }
                audioLine.stop();
                audioLine.flush();
                if (fmp.fmp_seek(player, positionUs) != 0) {
                    throw new IllegalStateException(nativeError(player));
                }
                resetAudioClock();
                fallbackClockBasePtsUs = positionUs;
                fallbackClockBaseNanos = System.nanoTime();
                if (requestedState == State.PLAYING) {
                    audioLine.start();
                }
                return;
            }
            if (rangePending) {
                long startUs;
                long endUs;
                synchronized (stateLock) {
                    startUs = pendingRangeStartUs;
                    endUs = pendingRangeEndUs;
                    rangePending = false;
                }
                audioLine.stop();
                audioLine.flush();
                if (fmp.fmp_play_range(player, startUs, endUs) != 0) {
                    throw new IllegalStateException(nativeError(player));
                }
                activeRangeStartUs = startUs;
                activeRangeEndUs = endUs;
                activeRange = true;
                resetAudioClock();
                fallbackClockBasePtsUs = startUs;
                fallbackClockBaseNanos = System.nanoTime();
                appliedState = State.PLAYING;
                audioLine.start();
                return;
            }
            State requested = requestedState;
            if (requested == appliedState) return;

            if (requested == State.PLAYING) {
                if (fmp.fmp_play(player) != 0) {
                    throw new IllegalStateException(nativeError(player));
                }
                fallbackClockBaseNanos = System.nanoTime();
                audioLine.start();
            } else if (requested == State.PAUSED) {
                fallbackClockBasePtsUs = currentClockUs();
                fallbackClockBaseNanos = System.nanoTime();
                if (fmp.fmp_pause(player) != 0) {
                    throw new IllegalStateException(nativeError(player));
                }
                audioLine.stop();
            } else {
                audioLine.stop();
                audioLine.flush();
                if (fmp.fmp_stop(player) != 0) {
                    throw new IllegalStateException(nativeError(player));
                }
                activeRange = false;
                resetAudioClock();
            }
            appliedState = requested;
        }

        private void presentVideo(byte[] rgba, int width, int height, int stride,
                                  long ptsUs, long eventGeneration) {
            if (width <= 0 || height <= 0 || stride < (long) width * 4 ||
                    rgba.length < (long) stride * height) {
                throw new IllegalArgumentException("Invalid decoded RGBA video frame");
            }
            BufferedImage image = new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_ARGB);
            int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            for (int y = 0; y < height; y++) {
                int offset = y * stride;
                int targetOffset = y * width;
                for (int x = 0; x < width; x++) {
                    int pixel = offset + x * 4;
                    int red = rgba[pixel] & 0xff;
                    int green = rgba[pixel + 1] & 0xff;
                    int blue = rgba[pixel + 2] & 0xff;
                    int alpha = rgba[pixel + 3] & 0xff;
                    pixels[targetOffset + x] =
                            (alpha << 24) | (red << 16) | (green << 8) | blue;
                }
            }

            synchronized (frameLock) {
                if (generation != eventGeneration || requestedState != State.PLAYING) return;
                while (frames.size() >= MAX_QUEUED_VIDEO_FRAMES) {
                    frames.removeFirst();
                }
                frames.addLast(new VideoFrame(ptsUs, image));
                if (frames.size() == 1 && !audioClockReady) {
                    fallbackClockBasePtsUs = ptsUs;
                    fallbackClockBaseNanos = System.nanoTime();
                }
            }
        }

        private void presentDueVideo() {
            long now = System.nanoTime();
            if (now < nextPresentationNanos) return;
            VideoFrame due = null;
            long clockUs = currentClockUs();
            synchronized (frameLock) {
                while (!frames.isEmpty() && frames.peekFirst().ptsUs <= clockUs) {
                    due = frames.removeFirst();
                }
                frameLock.notifyAll();
            }
            if (due != null) {
                videoPanel.setFrame(due.image);
                nextPresentationNanos = now + presentationIntervalNanos;
            }
        }

        private void presentLastQueuedVideo() {
            VideoFrame last = null;
            synchronized (frameLock) {
                while (!frames.isEmpty()) last = frames.removeFirst();
            }
            if (last != null) videoPanel.setFrame(last.image);
        }

        private void waitForVideoClockIfNeeded(long ptsUs, long eventGeneration)
                throws InterruptedException {
            while (!closed.get() && generation == eventGeneration &&
                    !audioClockReady && currentClockUs() < ptsUs) {
                if (requestedState != appliedState) applyRequestedState();
                if (requestedState == State.PLAYING) {
                    Thread.sleep(2);
                } else {
                    waitForStateChange();
                }
            }
        }

        private void queueAudio(byte[] pcm, int sampleRate, int channels,
                                long ptsUs, long eventGeneration) {
            if (sampleRate != AUDIO_RATE || channels != 2 || (pcm.length & 3) != 0) {
                throw new IllegalArgumentException("Unexpected decoded PCM audio format");
            }
            if (!audioClockReady) {
                audioClockBasePtsUs = ptsUs;
                audioClockBaseFrame = audioLine.getLongFramePosition();
                audioFramesWritten = 0;
                audioClockReady = true;
            }
            int offset = 0;
            while (offset < pcm.length) {
                if (generation != eventGeneration) return;
                while (!closed.get() && generation == eventGeneration) {
                    if (requestedState != appliedState) applyRequestedState();
                    if (requestedState == State.PLAYING) break;
                    waitForStateChange();
                }
                if (closed.get() || generation != eventGeneration) return;
                int count = Math.min(4096, pcm.length - offset);
                int written = audioLine.write(pcm, offset, count);
                if (written <= 0) {
                    throw new IllegalStateException("Java Sound did not accept PCM audio data");
                }
                offset += written;
                audioFramesWritten += written / 4;
            }
        }

        private void waitForAudioToFinish() throws InterruptedException {
            while (!closed.get() && audioClockReady &&
                    audioLine.getLongFramePosition() - audioClockBaseFrame <
                            audioFramesWritten) {
                if (requestedState != appliedState || seekPending) {
                    applyRequestedState();
                }
                if (requestedState == State.STOPPED) return;
                if (requestedState == State.PAUSED) {
                    waitForStateChange();
                } else {
                    Thread.sleep(5);
                }
            }
        }

        private void waitForStateChange() {
            synchronized (stateLock) {
                if (!closed.get() && requestedState != State.PLAYING &&
                        !seekPending) {
                    try {
                        stateLock.wait();
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        closed.set(true);
                    }
                }
            }
        }

        private long currentClockUs() {
            if (audioClockReady) {
                long playedFrames = audioLine.getLongFramePosition() - audioClockBaseFrame;
                return audioClockBasePtsUs + playedFrames * 1_000_000L / AUDIO_RATE;
            }
            if (requestedState != State.PLAYING) return fallbackClockBasePtsUs;
            return fallbackClockBasePtsUs +
                    (System.nanoTime() - fallbackClockBaseNanos) / 1_000L;
        }

        private void resetAudioClock() {
            audioClockReady = false;
            audioClockBasePtsUs = 0;
            audioClockBaseFrame = audioLine.getLongFramePosition();
            audioFramesWritten = 0;
            fallbackClockBasePtsUs = 0;
            fallbackClockBaseNanos = System.nanoTime();
        }

        private String nativeError(Pointer handle) {
            byte[] buffer = new byte[1024];
            int length = fmp.fmp_get_error(handle, buffer, buffer.length);
            if (length < 0) return "Unknown native FMP error";
            return new String(buffer, 0, length, StandardCharsets.UTF_8);
        }

        @Override
        public void close() {
            if (!closed.compareAndSet(false, true)) return;
            videoTimer.stop();
            audioLine.stop();
            audioLine.flush();
            synchronized (stateLock) {
                stateLock.notifyAll();
            }
            worker.interrupt();
            try {
                worker.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public BufferedImage screenshot(String mediaPath, long positionUs,
                                           String subtitlePath) {
        FmpScreenshot image = new FmpScreenshot();
        int result = fmp.fmp_screenshot(mediaPath, positionUs, subtitlePath, image);
        if (result != 0) {
            throw new IllegalArgumentException(error(null));
        }
        image.read();
        try {
            byte[] rgba = image.data.getByteArray(0, image.data_size);
            BufferedImage output =
                    new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB);
            int[] row = new int[image.width];
            for (int y = 0; y < image.height; y++) {
                int offset = y * image.stride;
                for (int x = 0; x < image.width; x++) {
                    int p = offset + x * 4;
                    int r = rgba[p] & 0xff;
                    int g = rgba[p + 1] & 0xff;
                    int b = rgba[p + 2] & 0xff;
                    int a = rgba[p + 3] & 0xff;
                    row[x] = (a << 24) | (r << 16) | (g << 8) | b;
                }
                output.setRGB(0, y, image.width, 1, row, 0, image.width);
            }
            return output;
        } finally {
            fmp.fmp_screenshot_free(image);
        }
    }

    public BufferedImage screenshot(String mediaPath, long positionUs,
                                           String subtitlePath, BackgroundType backgroundType,
                                           int width, int height, int squareSize,
                                           Color color1, Color color2) {
        switch(backgroundType) {
            case VIDEO -> {
                return screenshot(mediaPath, positionUs, subtitlePath);
            }
            case CHECKERBOARD -> {
                if(width <= 0 || height <= 0) {
                    width = 640;
                    height = 360;
                }
                if(squareSize <= 0){
                    squareSize = 8;
                }
                if(color1 == null || color2 == null) {
                    color1 = Color.white;
                    color2 = Color.lightGray;
                }
            }
            case TRANSPARENT -> {
                if(width <= 0 || height <= 0) {
                    width = 640;
                    height = 360;                    
                }
                squareSize = 8;
                color1 = Color.white;
                color2 = Color.white;
            }
        }       
        
        FmpScreenshot image = new FmpScreenshot();
        
        FmpScreenshotOptions options = new FmpScreenshotOptions();
        options.background = backgroundType.getValue();
        options.width = width;
        options.height = height;
        options.square_size = squareSize;
        options.color1_rgba = setColorToByte(color1);
        options.color2_rgba = setColorToByte(color2);
        
        int result = fmp.fmp_screenshot_with_options(
                mediaPath, positionUs, subtitlePath, options, image);
        if (result != 0) {
            throw new IllegalArgumentException(error(null));
        }
        image.read();
        try {
            byte[] rgba = image.data.getByteArray(0, image.data_size);
            BufferedImage output =
                    new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB);
            int[] row = new int[image.width];
            for (int y = 0; y < image.height; y++) {
                int offset = y * image.stride;
                for (int x = 0; x < image.width; x++) {
                    int p = offset + x * 4;
                    int r = rgba[p] & 0xff;
                    int g = rgba[p + 1] & 0xff;
                    int b = rgba[p + 2] & 0xff;
                    int a = rgba[p + 3] & 0xff;
                    row[x] = (a << 24) | (r << 16) | (g << 8) | b;
                }
                output.setRGB(0, y, image.width, 1, row, 0, image.width);
            }
            return output;
        } finally {
            fmp.fmp_screenshot_free(image);
        }
    }
}
