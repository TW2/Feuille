package feuille.util.theme;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
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
    }

}
