package feuille.module.video;

import feuille.module.fmp.ai.FreeMediaPlayer;
import feuille.util.Exchange;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ViewFMP extends JPanel {

    private final Exchange exchange;

    private final FreeMediaPlayer freeMediaPlayer;
    private final FreeMediaPlayer.VideoPanel videoPanel;
    private FreeMediaPlayer.PlaybackController controller;
    private final JLabel lblOverlay;

    public ViewFMP(Exchange exchange){
        this.exchange = exchange;
        freeMediaPlayer = new FreeMediaPlayer();
        videoPanel = new FreeMediaPlayer.VideoPanel();
        controller = null;
        lblOverlay = new JLabel();
        lblOverlay.setOpaque(false);
        videoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        videoPanel.add(lblOverlay);
        setLayout(new BorderLayout());
        add(videoPanel, BorderLayout.CENTER);
    }

    public void openMedia(String mediaPath){
        try {
            if(controller != null){
                free();
                controller = null;
            }
            File app = new File(new File("").getAbsolutePath());
            File folder = new File(app.getPath() +
                    File.separator + "settings" +
                    File.separator + "preview"
            );
            File script = new File(folder, "editflag2.ass");
            controller = new FreeMediaPlayer.PlaybackController(
                    exchange,
                    freeMediaPlayer.getFmp(),
                    mediaPath,
                    script.getPath(),
                    videoPanel
            );
        } catch (Exception exception) {
            String error = exception.getMessage() + " at " + exception.getStackTrace()[1];
            JOptionPane.showMessageDialog(null, error,
                    "Unable to start FMP", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void play(long startUs, long endUs) {
        if (controller == null) return;
        if(startUs < 0 || endUs <= 0 || startUs > endUs){
            controller.play();
        }else{
            controller.play(startUs, endUs);
        }
    }

    public void pause() {
        if (controller == null) return;
        controller.pause();
    }

    public void stop() {
        if (controller == null) return;
        controller.stop();
    }

    public void free() {
        if (controller == null) return;
        controller.close();
    }

    public void setOverlay(BufferedImage overlay){
        if(overlay == null){
            lblOverlay.setIcon(null);
        }else{
            double ratioX = (double) lblOverlay.getWidth() / overlay.getWidth();
            double ratioY = (double) lblOverlay.getHeight() / overlay.getHeight();
            double ratio = Math.min(ratioX, ratioY);
            int w = (int) (overlay.getWidth() * ratio);
            int h = (int) (overlay.getHeight() * ratio);
            lblOverlay.setIcon(new ImageIcon(overlay.getScaledInstance(w, h, Image.SCALE_SMOOTH)));
        }
    }

}
