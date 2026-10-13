package feuille.util.theme;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.*;
import feuille.util.theme.collection.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ThemeCollection extends ArrayList<Theme<? extends FlatLaf>> {

    public ThemeCollection() {
        super();

        add(new Theme<>(new FlatLightLaf(), ThemeType.Light));
        add(new Theme<>(new FlatDarkLaf(), ThemeType.Dark));
        add(new Theme<>(new FlatDarculaLaf(), ThemeType.Dark));
        add(new Theme<>(new AirTheme(), ThemeType.Light));
        add(new Theme<>(new Arctic(), ThemeType.Light));
        add(new Theme<>(new BloodyGolden(), ThemeType.Dark));
        add(new Theme<>(new GirlPower(), ThemeType.Light));
        add(new Theme<>(new Lemon(), ThemeType.Light));
        add(new Theme<>(new Military(), ThemeType.Dark));
        add(new Theme<>(new Mooon(), ThemeType.Dark));
        add(new Theme<>(new Nihon(), ThemeType.Light));
        add(new Theme<>(new Nuclear(), ThemeType.Dark));
        add(new Theme<>(new Patriot(), ThemeType.Light));
        add(new Theme<>(new Ranch(), ThemeType.Dark));
        add(new Theme<>(new Satan(), ThemeType.Dark));

        File folder = new File(
                Objects.requireNonNull(
                        ThemeCollection.class.getResource("/themes")).getPath());
        for(File f : Objects.requireNonNull(folder.listFiles())) {
            if(f.getName().endsWith(".th")) {
                CustomTheme ct = CustomTheme.create(f.getAbsolutePath());
                add(new Theme<>(ct.getFlatLaf(), ct.getName(), ct.getAuthor(), ct.getThemeType()));
            }
        }

        add(new Theme<>(new FlatArcIJTheme(), ThemeType.Light));
        add(new Theme<>(new FlatArcOrangeIJTheme(), ThemeType.Light));
        add(new Theme<>(new FlatArcDarkIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatArcDarkOrangeIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatCarbonIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatCobalt2IJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatCyanLightIJTheme(), ThemeType.Light));
        add(new Theme<>(new FlatDarkFlatIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatDarkPurpleIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatDraculaIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatGradiantoDarkFuchsiaIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatGradiantoDeepOceanIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatGradiantoMidnightBlueIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatGradiantoNatureGreenIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatGrayIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatGruvboxDarkHardIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatHiberbeeDarkIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatHighContrastIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatLightLaf(), ThemeType.Light));
        add(new Theme<>(new FlatMaterialDesignDarkIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatMonocaiIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatMonokaiProIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatNordIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatOneDarkIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatSolarizedDarkIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatSolarizedLightIJTheme(), ThemeType.Light));
        add(new Theme<>(new FlatSpacegrayIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatVuesionIJTheme(), ThemeType.Dark));
        add(new Theme<>(new FlatXcodeDarkIJTheme(), ThemeType.Dark));
    }

}
