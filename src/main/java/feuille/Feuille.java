package feuille;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import feuille.dialog.SplashScreen;

import java.awt.*;
import java.io.File;
import java.util.Objects;

public class Feuille {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            File themeFolder = new File(Objects.requireNonNull(
                    Feuille.class.getResource("/themes")).getPath());
            FlatLaf.registerCustomDefaultsSource(themeFolder);
            FlatLightLaf.setup();
            SplashScreen sp = new SplashScreen();
            sp.setVisible(true);
            FeuilleFrame mf = new FeuilleFrame();
            mf.setLocationRelativeTo(null);
            mf.setVisible(true);
        });
    }
}
