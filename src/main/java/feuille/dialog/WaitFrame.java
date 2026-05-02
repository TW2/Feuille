package feuille.dialog;

import javax.swing.*;
import java.awt.*;

public class WaitFrame extends JFrame implements Runnable {

    private Thread thread;
    private volatile boolean running = false;

    private final JProgressBar progressBar;
    private final JLabel progressLabel;

    private int value;
    private String message;

    public WaitFrame() throws HeadlessException {
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);

        progressLabel = new JLabel();

        getContentPane().add(progressBar, BorderLayout.CENTER);
        getContentPane().add(progressLabel, BorderLayout.SOUTH);

        setTitle("Wait");
        setPreferredSize(new Dimension(500, 50));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }

    public void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        thread.interrupt();
        thread = null;
    }

    public void updateData(int progress, String message) {
        value = progress;
        this.message = message;

        if(progress == 100) {
            running = false;
            stop();
            dispose();
        }
    }

    @Override
    public void run() {
        while(running){
            progressBar.setValue(value);
            progressLabel.setText(message);
        }
    }
}
