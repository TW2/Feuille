package feuille.util;

import feuille.io.assa.*;
import feuille.module.video.base.subs.GetSubs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class PreviewASS extends JPanel {

    private final JLabel lblView;
    private final SpinnerNumberModel spinnerWidthModel;
    private final SpinnerNumberModel spinnerHeightModel;
    private final JTextField tfSentence;

    private AssStyle currentStyle;
    int w, h, sq;
    Color c1, c2;

    public PreviewASS(int width, int height, int squareSize, Color c1, Color c2) {
        w = width;
        h = height - 26;
        sq = squareSize;
        this.c1 = c1;
        this.c2 = c2;

        lblView = new JLabel();
        spinnerWidthModel = new SpinnerNumberModel(1920, 100, 60000, 1);
        spinnerHeightModel = new SpinnerNumberModel(1080, 100, 60000, 1);
        JSpinner spinnerWidth = new JSpinner(spinnerWidthModel);
        JSpinner spinnerHeight = new JSpinner(spinnerHeightModel);
        tfSentence = new JTextField("It's a beautiful day! 1234567\\Nかわいい女の子あなたは");
        JButton btnSentence = new JButton("Change");

        lblView.setPreferredSize(new Dimension(w, h));

        JPanel panSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        spinnerWidth.setPreferredSize(new Dimension(120, 26));
        spinnerHeight.setPreferredSize(new Dimension(120, 26));
        tfSentence.setPreferredSize(new Dimension(500, 26));
        btnSentence.setPreferredSize(new Dimension(120, 26));
        panSouth.add(spinnerWidth);
        panSouth.add(spinnerHeight);
        panSouth.add(tfSentence);
        panSouth.add(btnSentence);

        currentStyle = new AssStyle();

        btnSentence.addActionListener(this::btnSentenceActionPerformed);

        setLayout(new BorderLayout());
        add(lblView, BorderLayout.CENTER);
        add(panSouth, BorderLayout.SOUTH);

        //changeStyleAndSentence();
    }

    public void changeStyleAndSentence() {
        File app = new File(new File("").getAbsolutePath());
        File folder = new File(app.getPath() +
                File.separator + "settings" +
                File.separator + "preview"
        );
        File script = new File(folder, "editstyle.ass");
        ASS ass = ASS.read(script.getPath());
        ass.getInfos().setPlayResX(spinnerWidthModel.getNumber().intValue());
        ass.getInfos().setPlayResY(spinnerHeightModel.getNumber().intValue());
        ass.getStyles().clear();
        int alignment = currentStyle.getAlignment().getNumber();
        currentStyle.setAlignment(new AssAlignment(5));
        ass.getStyles().add(currentStyle);
        AssEvent newEvent = new AssEvent();
        newEvent.setStyle(currentStyle);
        newEvent.setText(tfSentence.getText());
        newEvent.setName(new AssActor("Line"));
        newEvent.setStart(new AssTime(0));
        newEvent.setEnd(new AssTime(5_000));
        ass.getEvents().clear();
        ass.getEvents().add(newEvent);
        ASS.write(ass, script.getPath());
        BufferedImage img = GetSubs.getScreenshot(
                ass,
                spinnerWidthModel.getNumber().intValue(),
                spinnerHeightModel.getNumber().intValue(),
                1_000L
        );
        int ws = spinnerWidthModel.getNumber().intValue() / 3;
        int hs = spinnerHeightModel.getNumber().intValue() / 3;
        lblView.setIcon(new ImageIcon(img.getSubimage(ws, hs, ws, hs)));
        lblView.setHorizontalAlignment(JLabel.CENTER);
        lblView.setVerticalAlignment(JLabel.CENTER);
        currentStyle.setAlignment(new AssAlignment(alignment));
    }

    public void btnSentenceActionPerformed(ActionEvent e) {
        changeStyleAndSentence();
    }

    public void setCurrentStyle(AssStyle currentStyle) {
        this.currentStyle = currentStyle;
    }
}
