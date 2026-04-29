package feuille.util.assa;

import feuille.util.Loader;

public enum AssEncoding {
    ANSI(0, Loader.language("encoding.ansi", "ANSI")),
    Default(1, Loader.language("encoding.default", "Default")),
    Symbol(2, Loader.language("encoding.symbol", "Symbol")),
    ShiftJIS(128, Loader.language("encoding.shiftJIS", "Shift-JIS")),
    Hangeul(129, Loader.language("encoding.hangeul", "Hangul")),
    Johab(130, Loader.language("encoding.johab", "Johab")),
    SChinese(134, Loader.language("encoding.s_chinese", "Simplified Chinese")),
    TChinese(136, Loader.language("encoding.t_chinese", "Traditional Chinese")),
    Turkish(162, Loader.language("encoding.turkish", "Turkish")),
    Vietnamese(163, Loader.language("encoding.vietnamese", "Vietnamese")),
    Hebrew(177, Loader.language("encoding.hebrew", "Hebrew")),
    Arabic(178, Loader.language("encoding.arabic", "Arabic"));

    final int codepage;
    final String name;

    AssEncoding(int codepage, String name){
        this.codepage = codepage;
        this.name = name;
    }

    public int getCodepage() {
        return codepage;
    }

    public String getName() {
        return name;
    }

    public static AssEncoding search(String s) {
        AssEncoding e = AssEncoding.Default;
        for(AssEncoding l : AssEncoding.values()){
            if(l.getName().equalsIgnoreCase(s)){
                e = l;
                break;
            }
        }
        return e;
    }

    public static AssEncoding search(int v) {
        AssEncoding e = AssEncoding.Default;
        for(AssEncoding l : AssEncoding.values()){
            if(l.getCodepage() == v){
                e = l;
                break;
            }
        }
        return e;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", getCodepage(), getName());
    }
}
