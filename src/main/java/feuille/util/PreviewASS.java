package feuille.util;

import feuille.module.editor.assa.ASS;
import feuille.module.editor.assa.AssEvent;
import feuille.module.editor.assa.AssStyle;
import feuille.util.assa.AssColor;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class PreviewASS extends JPanel {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private final JLabel lblView;
    private final JTextField tfSentence;

    private String mrl;
    private AssStyle currentStyle;
    private Color back;
    private Color grid;


    public PreviewASS() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        lblView = new JLabel();
        lblView.setOpaque(true);
        lblView.setBackground(getBackground());
        tfSentence = new JTextField("It's a beautiful day! 1234567\\Nかわいい女の子あなたは");
        JButton btnSentence = new JButton("Change");

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        layeredPane.setBackground(getBackground());
        layeredPane.add(lblView);
        layeredPane.add(mediaPlayerComponent);
        layeredPane.setPosition(lblView, 1);
        layeredPane.setPosition(mediaPlayerComponent, 2);
        layeredPane.setPreferredSize(new Dimension(426, 240));

        JPanel panSouth = new JPanel(new BorderLayout());
        panSouth.add(tfSentence, BorderLayout.CENTER);
        panSouth.add(btnSentence, BorderLayout.EAST);

        mrl = null;
        currentStyle = new AssStyle();
        back = null;
        grid = null;

        btnSentence.addActionListener(this::btnSentenceActionPerformed);

        setLayout(new BorderLayout());
        add(layeredPane, BorderLayout.CENTER);
        add(panSouth, BorderLayout.SOUTH);

        mediaPlayerComponent.setSize(426, 240);
        lblView.setPreferredSize(new Dimension(426, 240));

        //changeStyleAndSentence();
    }

    private void changeStyleAndSentence() {
        File app = new File(new File("").getAbsolutePath());
        File folder = new File(app.getPath() +
                File.separator + "settings" +
                File.separator + "preview"
        );
        File script = new File(folder, "editstyle.ass");
        mrl = new File(folder, "output.m4v").getAbsolutePath();
        ASS ass = ASS.read(script.getPath());
        for(AssStyle style : ass.getStyles()) {
            if(style.getName().equals("Default")){
                style.setAssFont(currentStyle.getAssFont());
            }
        }
        for(AssStyle style : ass.getStyles()) {
            if(style.getName().equals("Back") && back != null) {
                style.setTextColor(new AssColor(back));
            }
        }
        for(AssStyle style : ass.getStyles()) {
            if(style.getName().equals("Relief") && grid != null) {
                style.setTextColor(new AssColor(grid));
            }
        }
        for(AssEvent event : ass.getEvents()) {
            if(event.getName().getName().equals("Line")) {
                event.setText(tfSentence.getText());
            }
        }
        ASS.write(ass, script.getPath());
        mediaPlayerComponent.mediaPlayer().media().play(mrl,
                ":no-sub-autodetect-file",
                ":sub-file=" + script.getPath()
        );
        mediaPlayerComponent.mediaPlayer().controls().setPosition(0.8f);
        BufferedImage img = mediaPlayerComponent.mediaPlayer().snapshots().get();
        if(img != null) {
            lblView.setIcon(new ImageIcon(img));
        }
    }

    public void btnSentenceActionPerformed(ActionEvent e) {
        changeStyleAndSentence();
    }

    public void setCurrentStyle(AssStyle currentStyle) {
        this.currentStyle = currentStyle;
    }

    public void setAssBackground(Color c) {
        back = c;
    }

    public void setAssGridColor(Color c) {
        grid = c;
    }

    public void updatePreview() {
        changeStyleAndSentence();
    }
}
