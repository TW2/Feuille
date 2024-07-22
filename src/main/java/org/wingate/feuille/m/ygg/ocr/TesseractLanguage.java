/*
 * Copyright (C) 2024 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wingate.feuille.m.ygg.ocr;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Tesseract 4.0
 * @author util2
 */
public enum TesseractLanguage {
    Afrikaans("afr", "Afrikaans"),
    Amharic("amh", "Amharic"),
    Arabic("ara", "Arabic"),
    Assamese("asm", "Assamese"),
    Azerbaijani("aze", "Azerbaijani"),
    Azerbaijani_Cyrilic("aze_cyrl", "Azerbaijani - Cyrilic"),
    Belarusian("bel", "Belarusian"),
    Bengali("ben", "Bengali"),
    Tibetan("bod", "Tibetan"),
    Bosnian("bos", "Bosnian"),
    Breton("bre", "Breton"),
    Bulgarian("bul", "Bulgarian"),
    Catalan_Valencian("cat", "Catalan/Valencian"),
    Cebuano("ceb", "Cebuano"),
    Czech("ces", "Czech"),
    Chinese_Simplified("chi_sim", "Chinese - Simplified"),
    Chinese_Traditional("chi_tra", "Chinese - Traditional"),
    Cherokee("chr", "Cherokee"),
    Corsican("cos", "Corsican"),
    Welsh("cym", "Welsh"),
    Danish("dan", "Danish"),
    German("deu", "German"),
    German_Latin("deu_latf", "German (Fraktur Latin)"),
    Dzongkha("dzo", "Dzongkha"),
    Greek_Modern("ell", "Greek, Modern (1453-)"),
    English("eng", "English"),
    English_Middle_Age("enm", "English, Middle (1100-1500)"),
    Esperanto("epo", "Esperanto"),
    Estonian("est", "Estonian"),
    Basque("eus", "Basque"),
    Faroese("fao", "Faroese"),
    Persian("fas", "Persian"),
    Filipino("fil", "Filipino (old - Tagalog)"),
    Finnish("fin", "Finnish"),
    French("fra", "French"),
    French_Middle_Age("frm", "French, Middle (ca.1400-1600)"),
    Western_Frisian("fry", "Western Frisian"),
    Scottish_Gaelic("gla", "Scottish Gaelic"),
    Irish("gle", "Irish"),
    Galician("glg", "Galician"),
    Greek_Ancient("grc", "Greek, Ancient (to 1453) (contrib)"),
    Gujarati("guj", "Gujarati"),
    Haitian_Creole("hat", "Haitian; Haitian Creole"),
    Hebrew("heb", "Hebrew"),
    Hindi("hin", "Hindi"),
    Croatian("hrv", "Croatian"),
    Hungarian("hun", "Hungarian"),
    Armenian("hye", "Armenian"),
    Inuktitut("iku", "Inuktitut"),
    Indonesian("ind", "Indonesian"),
    Icelandic("isl", "Icelandic"),
    Italian("ita", "Italian"),
    Italian_Old("ita_old", "Italian - Old"),
    Javanese("jav", "Javanese"),
    Japanese("jpn", "Japanese"),
    Kannada("kan", "Kannada"),
    Georgian("kat", "Georgian"),
    Georgian_Old("kat_old", "Georgian - Old"),
    Kazakh("kaz", "Kazakh"),
    Central_Khmer("khm", "Central Khmer"),
    Kirghiz_Kyrgyz("kir", "Kirghiz; Kyrgyz"),
    Kurmanji_Latin_Script("kmr", "Kurmanji (Kurdish - Latin Script) "),
    Korean("kor", "Korean"),
    Korean_vertical("kor_vert", "Korean (vertical)"),
    Lao("lao", "Lao"),
    Latin("lat", "Latin"),
    Latvian("lav", "Latvian"),
    Lithuanian("lit", "Lithuanian"),
    Luxembourgish("ltz", "Luxembourgish"),
    Malayalam("mal", "Malayalam"),
    Marathi("mar", "Marathi"),
    Macedonian("mkd", "Macedonian"),
    Maltese("mlt", "Maltese"),
    Mongolian("mon", "Mongolian"),
    Maori("mri", "Maori"),
    Malay("msa", "Malay"),
    Burmese("mya", "Burmese"),
    Nepali("nep", "Nepali"),
    Dutch_Flemish("nld", "Dutch; Flemish"),
    Norwegian("nor", "Norwegian"),
    Occitan_Modern("oci", "Occitan (post 1500)"),
    Oriya("ori", "Oriya"),
    Panjabi("pan", "Panjabi; Punjabi"),
    Polish("pol", "Polish"),
    Portuguese("por", "Portuguese"),
    Pashto("pus", "Pushto; Pashto"),
    Quechua("que", "Quechua"),
    Romanian_Moldavian("ron", "Romanian; Moldavian; Moldovan"),
    Russian("rus", "Russian"),
    Sanskrit("san", "Sanskrit"),
    Sinhala_Sinhalese("sin", "Sinhala; Sinhalese"),
    Slovak("slk", "Slovak"),
    Slovenian("slv", "Slovenian"),
    Sindhi("snd", "Sindhi"),
    Spanish("spa", "Spanish; Castilian"),
    Spanish_Old("spa_old", "Spanish; Castilian - Old"),
    Albanian("sqi", "Albanian"),
    Serbian("srp", "Serbian"),
    Serbian_Latin("srp_latn", "Serbian - Latin"),
    Sundanese("sun", "Sundanese"),
    Swahili("swa", "Swahili"),
    Swedish("swe", "Swedish"),
    Syriac("syr", "Syriac"),
    Tamil("tam", "Tamil"),
    Tatar("tat", "Tatar"),
    Telugu("tel", "Telugu"),
    Tajik("tgk", "Tajik"),
    Thai("tha", "Thai"),
    Tigrinya("tir", "Tigrinya"),
    Tonga("ton", "Tonga"),
    Turkish("tur", "Turkish"),
    Uighur("uig", "Uighur; Uyghur"),
    Ukrainian("ukr", "Ukrainian"),
    Urdu("urd", "Urdu"),
    Uzbek("uzb", "Uzbek"),
    Uzbek_Cyrilic("uzb_cyrl", "Uzbek - Cyrilic"),
    Vietnamese("vie", "Vietnamese"),
    Yiddish("yid", "Yiddish"),
    Yoruba("yor", "Yoruba");
    
    String langCode;
    String language;
    
    private TesseractLanguage(String langCode, String language){
        this.langCode = langCode;
        this.language = language;
    }

    public String getLangCode() {
        return langCode;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", language, langCode);
    }
    
    public static List<TesseractLanguage> getLanguages(){
        List<TesseractLanguage> tess = Arrays.asList(values());
        Collections.sort(tess, new LanguageComparator());        
        return tess;
    }
    
    private static class LanguageComparator implements Comparator<TesseractLanguage> {
        @Override
        public int compare(TesseractLanguage o1, TesseractLanguage o2) {
            return o1.getLanguage().compareTo(o2.getLanguage());
        }
        
    }
}
