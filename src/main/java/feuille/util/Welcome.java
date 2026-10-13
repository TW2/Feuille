package feuille.util;

import javax.swing.*;

public class Welcome extends JPanel {

    private final Exchange exchange;

    public Welcome(Exchange exchange, int w, int h) {
        this.exchange = exchange;
        setSize(w, h);

        setBackground(DrawColor.dark_blue.getColor());
    }

    public void forceResize(int w, int h) {
        setSize(w, h);
        updateUI();
    }
}
