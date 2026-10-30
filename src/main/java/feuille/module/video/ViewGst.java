package feuille.module.video;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import feuille.module.video.base.image.AssSubtitleController;
import feuille.module.video.base.image.GstVideoComponent;
import feuille.module.video.base.image.VideoSurface;
import feuille.module.video.base.subs.LibassSubtitleRenderer;
import feuille.module.video.base.wave.Playground;
import feuille.util.Exchange;
import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.elements.PlayBin;
import org.freedesktop.gstreamer.event.SeekEvent;
import org.freedesktop.gstreamer.event.SeekFlags;
import org.freedesktop.gstreamer.event.SeekType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumSet;
import java.util.stream.Stream;

public class ViewGst extends JPanel {

    private final Exchange exchange;
    private final LibassSubtitleRenderer renderer;
    private final AssSubtitleController subtitles;
    private final Playground playground;
    private final PlayBin playbin;
    private final Element level;

    public ViewGst(Exchange exchange) {
        this.exchange = exchange;
        playground = new Playground(exchange);

        //------------------------------ Init Gst
        configurePaths();
        Gst.init(Version.BASELINE, "Feuille");

        GstVideoComponent vc = new GstVideoComponent();

        playbin = new PlayBin("playbin");
        playbin.setVideoSink(vc.getElement());

        level = ElementFactory.make("level", "level");
        playbin.set("audio-filter", level);

        //------------------------------ UI
        int videoWidth = 1920;
        int videoHeight = 1080;

        renderer = new LibassSubtitleRenderer(videoWidth, videoHeight);
        VideoSurface surface = new VideoSurface(vc);

        subtitles = new AssSubtitleController(
                renderer,
                surface.getSubtitleOverlay(),
                this::getMsPosition
        );

        setLayout(new BorderLayout());
        add(surface, BorderLayout.CENTER);
    }

    public long getMsPosition() {
        long nanos = playbin.queryPosition(Format.TIME);
        return nanos < 0 ? -1L : nanos / 1_000_000L;
    }

    public void free(){
        subtitles.close();
        renderer.close();
    }

    public AssSubtitleController getSubtitles() {
        return subtitles;
    }

    public void openMedia(String mediaPath) {
        playbin.stop();
        playbin.setURI(new File(mediaPath).toURI());
    }

    public void play(long startUs, long endUs) {
        if(startUs > 0 && endUs > 0 && startUs < endUs) {
            playbin.pause();
            // Start a count thread
            playground.usDiscovered(startUs, endUs);
            SeekEvent seekEvent = new SeekEvent(
                    1.0,
                    Format.TIME,
                    EnumSet.of(SeekFlags.FLUSH),
                    SeekType.SET, startUs * 1000L,
                    SeekType.SET, endUs * 1000L
            );
            playbin.play(); // Playback
            playbin.sendEvent(seekEvent);
            playground.play(); // Thread for drawing
        }else{
            play();
        }
    }

    private void play(){
        playbin.play(); // Playback
        playground.play(); // Thread for drawing
    }

    public void pause(){
        playbin.pause(); // Playback
        playground.pause(); // Thread for drawing
    }

    public void stop(){
        playbin.stop(); // Playback
        playground.stop(); // Thread for drawing
    }

    /**
     * Configures paths to the GStreamer libraries. On Windows queries various
     * GStreamer environment variables, and then sets up the PATH environment
     * variable. On macOS, adds the location to jna.library.path (macOS binaries
     * link to each other). On both, the gstreamer.path system property can be
     * used to override. On Linux, assumes GStreamer is in the path already.
     */
    private void configurePaths() {
        if (Platform.isWindows()) {
            String gstPath = System.getProperty("gstreamer.path", findWindowsLocation());
            if (!gstPath.isEmpty()) {
                String systemPath = System.getenv("PATH");
                if (systemPath == null || systemPath.trim().isEmpty()) {
                    Kernel32.INSTANCE.SetEnvironmentVariable("PATH", gstPath);
                } else {
                    Kernel32.INSTANCE.SetEnvironmentVariable("PATH", gstPath
                            + File.pathSeparator + systemPath);
                }
            }
        } else if (Platform.isMac()) {
            String gstPath = System.getProperty("gstreamer.path",
                    "/Library/Frameworks/GStreamer.framework/Libraries/");
            if (!gstPath.isEmpty()) {
                String jnaPath = System.getProperty("jna.library.path", "").trim();
                if (jnaPath.isEmpty()) {
                    System.setProperty("jna.library.path", gstPath);
                } else {
                    System.setProperty("jna.library.path", jnaPath + File.pathSeparator + gstPath);
                }
            }

        }
    }

    /**
     * Query over a stream of possible environment variables for GStreamer
     * location, filtering on the first non-null result, and adding \bin\ to the
     * value.
     *
     * @return location or empty string
     */
    private String findWindowsLocation() {
        if (Platform.is64Bit()) {
            return Stream.of("GSTREAMER_1_0_ROOT_MSVC_X86_64",
                            "GSTREAMER_1_0_ROOT_MINGW_X86_64",
                            "GSTREAMER_1_0_ROOT_X86_64")
                    .map(System::getenv)
                    .filter(p -> p != null)
                    .map(p -> p.endsWith("\\") ? p + "bin\\" : p + "\\bin\\")
                    .findFirst().orElse("");
        } else {
            return "";
        }
    }
}
