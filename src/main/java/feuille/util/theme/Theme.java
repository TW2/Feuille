package feuille.util.theme;

import com.formdev.flatlaf.FlatLaf;

public class Theme<Laf extends FlatLaf> implements ITheme {

    private final Laf laf;
    private final String name;
    private final String author;
    private final ThemeType themeType;

    public Theme(Laf laf, String name, String author, ThemeType themeType) {
        this.laf = laf;
        this.name = name;
        this.author = author;
        this.themeType = themeType;
    }

    public Theme(Laf laf, ThemeType themeType){
        this(laf, "Unknown name", "Unknown author", themeType);
    }

    @Override
    public String getName() {
        if(isCustomTheme()){
            return name;
        }else{
            return laf.getName();
        }
    }

    public String getAuthor() {
        return author;
    }

    public ThemeType getThemeType() {
        return themeType;
    }

    public Laf getLaf() {
        return laf;
    }

    public boolean isCustomTheme() {
        return !getAuthor().contains("Unknown");
    }

    @Override
    public String toString() {
        if(isCustomTheme()){
            return String.format("%s - %s (%s) ",
                    getName(), getThemeType().name(), getAuthor());
        }else{
            return String.format("%s - %s",
                    getName(), getThemeType().name());
        }
    }
}
