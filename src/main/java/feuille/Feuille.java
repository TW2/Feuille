package feuille;

import com.formdev.flatlaf.FlatLightLaf;
import feuille.dialog.SplashScreen;

import java.awt.*;

public class Feuille {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            FlatLightLaf.setup();
            SplashScreen sp = new SplashScreen();
            sp.setVisible(true);
            FeuilleFrame mf = new FeuilleFrame();
            mf.setLocationRelativeTo(null);
            mf.setVisible(true);
        });
    }
}
