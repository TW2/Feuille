/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * <p>This class is a module for the language.<br />
 * Cette classe est un module pour le langage.</p>
 * @author The Wingate 2940
 */
public final class Language {

    static Locale loc = Locale.US;
    private Map<String,String> LangUS = new HashMap<>();
    private Map<String,String> LangFR = new HashMap<>();
    private Map<String,String> LangXX = new HashMap<>();
    private List<File> listFiles = new ArrayList<>();
    private boolean isDefault = true;
    private String searchPath = null;
    private String forceISO = "---";

    /** <p>Create a new Language.<br />Crée un nouveau Langage.</p> 
     * @param loc The Locale of your language or not. */
    public Language(Locale loc, String forceISO, String searchPath){
        this.forceISO = forceISO;
        this.searchPath = searchPath;
        Locale fromFile = null;
        try{
            fromFile = changeFromCode(forceISO);
        }catch(Exception e){
            //Unrecognized code by GUI launcher, try command line.
        }
        System.out.println(fromFile);
        System.out.println(forceISO);
        if(fromFile==null && !forceISO.equalsIgnoreCase("---") && !forceISO.isEmpty()){
            isDefault=false;
        }else if(fromFile!=null){
            isDefault=false;
        }else{
            Language.loc = loc!=null ? loc : Locale.getDefault();
            init(Language.loc);
        }
        if(isDefault==false){
            searchForLangFile(searchPath);
            if(listFiles.isEmpty()==false){
                setLanguageFromFile();
            }else{
                init(Locale.US);
            }            
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc=" ISO 3166 ">
    /** <p>A choice of countries.<br />Un choix de pays.</p> */
    public enum ISO_3166{
        Afghanistan("AF","AFG","Afghanistan"),
        Albania("AL","ALB","Albania"),
        Algeria("DZ","DZA","Algeria"),
        American_Samoa("AS","ASM","American Samoa"),
        Andorra("AD","AND","Andorra"),
        Angola("AO","AGO","Angola"),
        Anguilla("AI","AIA","Anguilla"),
        Antarctica("AQ","ATA","Antarctica"),
        Antigua_and_Barbuda("AG","ATG","Antigua and Barbuda"),
        Argentina("AR","ARG","Argentina"),
        Armenia("AM","ARM","Armenia"),
        Aruba("AW","ABW","Aruba"),
        Australia("AU","AUS","Australia"),
        Austria("AT","AUT","Austria"),
        Azerbaijan("AZ","AZE","Azerbaijan"),
        Bahamas("BS","BHS","Bahamas"),
        Bahrain("BH","BHR","Bahrain"),
        Bangladesh("BD","BGD","Bangladesh"),
        Barbados("BB","BRB","Barbados"),
        Belarus("BY","BLR","Belarus"),
        Belgium("BE","BEL","Belgium"),
        Belize("BZ","BLZ","Belize"),
        Benin("BJ","BEN","Benin"),
        Bermuda("BM","BMU","Bermuda"),
        Bhutan("BT","BTN","Bhutan"),
        Bolivia("BO","BOL","Bolivia"),
        Bosnia_and_Herzegovina("BA","BIH","Bosnia and Herzegovina"),
        Botswana("BW","BWA","Botswana"),
        Bouvet_Island("BV","BVT","Bouvet Island"),
        Brazil("BR","BRA","Brazil"),
        British_Indian_Ocean_Territory("IO","IOT","British Indian Ocean Territory"),
        British_Virgin_Islands("VG","VGB","British Virgin Islands"),
        Brunei_Darussalam("BN","BRN","Brunei Darussalam"),
        Bulgaria("BG","BGR","Bulgaria"),
        Burkina_Faso("BF","BFA","Burkina Faso"),
        Burundi("BI","BDI","Burundi"),
        Cambodia("KH","KHM","Cambodia"),
        Cameroon("CM","CMR","Cameroon"),
        Canada("CA","CAN","Canada"),
        Cape_Verde("CV","CPV","Cape Verde"),
        Cayman_Islands("KY", "CYM", "Cayman Islands"),
        Central_African_Republic("CF", "CAF", "Central African Republic"),
        Chad("TD", "TCD", "Chad"),
        Chile("CL", "CHL", "Chile"),
        China("CN", "CHN", "China"),
        Christmas_Island("CX", "CXR", "Christmas Island"),
        Cocos_Islands("CC", "CCK", "Cocos (Keeling), Islands"),
        Colombia("CO", "COL", "Colombia"),
        Comoros("KM", "COM", "Comoros"),
        Congo1("CD", "COD", "Congo"),
        Congo2("CG", "COG", "Congo"),
        Cook_Islands("CK", "COK", "Cook Islands"),
        Costa_Rica("CR", "CRI", "Costa Rica"),
        Cote_DIvoire("CI", "CIV", "Cote D'Ivoire"),
        Cuba("CU", "CUB", "Cuba"),
        Cyprus("CY", "CYP", "Cyprus"),
        Czech("CZ", "CZE", "Czech"),
        Denmark("DK", "DNK", "Denmark"),
        Djibouti("DJ", "DJI", "Djibouti"),
        Dominica("DM", "DMA", "Dominica"),
        Dominican("DO", "DOM", "Dominican"),
        Ecuador("EC", "ECU", "Ecuador"),
        Egypt("EG", "EGY", "Egypt"),
        El_Salvador("SV", "SLV", "El Salvador"),
        Equatorial_Guinea("GQ", "GNQ", "Equatorial Guinea"),
        Eritrea("ER", "ERI", "Eritrea"),
        Estonia("EE", "EST", "Estonia"),
        Ethiopia("ET", "ETH", "Ethiopia"),
        Faeroe_Islands("FO", "FRO", "Faeroe Islands"),
        Falkland_Islands("FK", "FLK", "Falkland Islands (Malvinas),"),
        Fiji("FJ", "FJI", "Fiji"),
        Finland("FI", "FIN", "Finland"),
        France("FR", "FRA", "France"),
        French_Guiana("GF", "GUF", "French Guiana"),
        French_Polynesia("PF", "PYF", "French Polynesia"),
        French_Southern_Territories("TF", "ATF", "French Southern Territories"),
        Gabon("GA", "GAB", "Gabon"),
        Gambia("GM", "GMB", "Gambia"),
        Georgia("GE", "GEO", "Georgia"),
        Germany("DE", "DEU", "Germany"),
        Ghana("GH", "GHA", "Ghana"),
        Gibraltar("GI", "GIB", "Gibraltar"),
        Greece("GR", "GRC", "Greece"),
        Greenland("GL", "GRL", "Greenland"),
        Grenada("GD", "GRD", "Grenada"),
        Guadaloupe("GP", "GLP", "Guadaloupe"),
        Guam("GU", "GUM", "Guam"),
        Guatemala("GT", "GTM", "Guatemala"),
        Guinea("GN", "GIN", "Guinea"),
        Guinea_Bissau("GW", "GNB", "Guinea-Bissau"),
        Guyana("GY", "GUY", "Guyana"),
        Haiti("HT", "HTI", "Haiti"),
        Heard_and_McDonald_Islands("HM", "HMD", "Heard and McDonald Islands"),
        Holy_See("VA", "VAT", "Holy See (Vatican City State),"),
        Honduras("HN", "HND", "Honduras"),
        Hong_Kong("HK", "HKG", "Hong Kong"),
        Hrvatska("HR", "HRV", "Hrvatska (Croatia),"),
        Hungary("HU", "HUN", "Hungary"),
        Iceland("IS", "ISL", "Iceland"),
        India("IN", "IND", "India"),
        Indonesia("ID", "IDN", "Indonesia"),
        Iran("IR", "IRN", "Iran"),
        Iraq("IQ", "IRQ", "Iraq"),
        Ireland("IE", "IRL", "Ireland"),
        Israel("IL", "ISR", "Israel"),
        Italy("IT", "ITA", "Italy"),
        Jamaica("JM", "JAM", "Jamaica"),
        Japan("JP", "JPN", "Japan"),
        Jordan("JO", "JOR", "Jordan"),
        Kazakhstan("KZ", "KAZ", "Kazakhstan"),
        Kenya("KE", "KEN", "Kenya"),
        Kiribati("KI", "KIR", "Kiribati"),
        Korea1("KP", "PRK", "Korea"),
        Korea2("KR", "KOR", "Korea"),
        Kuwait("KW", "KWT", "Kuwait"),
        Kyrgyz_Republic("KG", "KGZ", "Kyrgyz Republic"),
        Lao_Peoples_Democratic_Republic("LA", "LAO", "Lao People's Democratic Republic"),
        Latvia("LV", "LVA", "Latvia"),
        Lebanon("LB", "LBN", "Lebanon"),
        Lesotho("LS", "LSO", "Lesotho"),
        Liberia("LR", "LBR", "Liberia"),
        Libyan("LY", "LBY", "Libyan"),
        Liechtenstein("LI", "LIE", "Liechtenstein"),
        Lithuania("LT", "LTU", "Lithuania"),
        Luxembourg("LU", "LUX", "Luxembourg"),
        Macao("MO", "MAC", "Macao"),
        Macedonia("MK", "MKD", "Macedonia"),
        Madagascar("MG", "MDG", "Madagascar"),
        Malawi("MW", "MWI", "Malawi"),
        Malaysia("MY", "MYS", "Malaysia"),
        Maldives("MV", "MDV", "Maldives"),
        Mali("ML", "MLI", "Mali"),
        Malta("MT", "MLT", "Malta"),
        Marshall_Islands("MH", "MHL", "Marshall Islands"),
        Martinique("MQ", "MTQ", "Martinique"),
        Mauritania("MR", "MRT", "Mauritania"),
        Mauritius("MU", "MUS", "Mauritius"),
        Mayotte("YT", "MYT", "Mayotte"),
        Mexico("MX", "MEX", "Mexico"),
        Micronesia("FM", "FSM", "Micronesia"),
        Moldova("MD", "MDA", "Moldova"),
        Monaco("MC", "MCO", "Monaco"),
        Mongolia("MN", "MNG", "Mongolia"),
        Montserrat("MS", "MSR", "Montserrat"),
        Morocco("MA", "MAR", "Morocco"),
        Mozambique("MZ", "MOZ", "Mozambique"),
        Myanmar("MM", "MMR", "Myanmar"),
        Namibia("NA", "NAM", "Namibia"),
        Nauru("NR", "NRU", "Nauru"),
        Nepal("NP", "NPL", "Nepal"),
        Netherlands_Antilles("AN", "ANT", "Netherlands Antilles"),
        Netherlands("NL", "NLD", "Netherlands"),
        New_Caledonia("NC", "NCL", "New Caledonia"),
        New_Zealand("NZ", "NZL", "New Zealand"),
        Nicaragua("NI", "NIC", "Nicaragua"),
        Niger("NE", "NER", "Niger"),
        Nigeria("NG", "NGA", "Nigeria"),
        Niue("NU", "NIU", "Niue"),
        Norfolk_Island("NF", "NFK", "Norfolk Island"),
        Northern_Mariana_Islands("MP", "MNP", "Northern Mariana Islands"),
        Norway("NO", "NOR", "Norway"),
        Oman("OM", "OMN", "Oman"),
        Pakistan("PK", "PAK", "Pakistan"),
        Palau("PW", "PLW", "Palau"),
        Palestinian_Territory("PS", "PSE", "Palestinian Territory"),
        Panama("PA", "PAN", "Panama"),
        Papua_New_Guinea("PG", "PNG", "Papua New Guinea"),
        Paraguay("PY", "PRY", "Paraguay"),
        Peru("PE", "PER", "Peru"),
        Philippines("PH", "PHL", "Philippines"),
        Pitcairn_Island("PN", "PCN", "Pitcairn Island"),
        Poland("PL", "POL", "Poland"),
        Portugal("PT", "PRT", "Portugal"),
        Puerto_Rico("PR", "PRI", "Puerto Rico"),
        Qatar("QA", "QAT", "Qatar"),
        Reunion("RE", "REU", "Reunion"),
        Romania("RO", "ROU", "Romania"),
        Russian_Federation("RU", "RUS", "Russian Federation"),
        Rwanda("RW", "RWA", "Rwanda"),
        St__Helena("SH", "SHN", "St. Helena"),
        St__Kitts_and_Nevis("KN", "KNA", "St. Kitts and Nevis"),
        St__Lucia("LC", "LCA", "St. Lucia"),
        St__Pierre_and_Miquelon("PM", "SPM", "St. Pierre and Miquelon"),
        St__Vincent_and_the_Grenadines("VC", "VCT", "St. Vincent and the Grenadines"),
        Samoa("WS", "WSM", "Samoa"),
        San_Marino("SM", "SMR", "San Marino"),
        Sao_Tome_and_Principe("ST", "STP", "Sao Tome and Principe"),
        Saudi_Arabia("SA", "SAU", "Saudi Arabia"),
        Senegal("SN", "SEN", "Senegal"),
        Serbia_and_Montenegro("CS", "SCG", "Serbia and Montenegro"),
        Seychelles("SC", "SYC", "Seychelles"),
        Sierra_Leone("SL", "SLE", "Sierra Leone"),
        Singapore("SG", "SGP", "Singapore"),
        Slovakia("SK", "SVK", "Slovakia (Slovak Republic),"),
        Slovenia("SI", "SVN", "Slovenia"),
        Solomon_Islands("SB", "SLB", "Solomon Islands"),
        Somalia("SO", "SOM", "Somalia"),
        South_Africa("ZA", "ZAF", "South Africa"),
        South_Georgia_and_the_South_Sandwich_Islands("GS", "SGS", "South Georgia and the South Sandwich Islands"),
        Spain("ES", "ESP", "Spain"),
        Sri_Lanka("LK", "LKA", "Sri Lanka"),
        Sudan("SD", "SDN", "Sudan"),
        Suriname("SR", "SUR", "Suriname"),
        Svalbard___Jan_Mayen_Islands("SJ", "SJM", "Svalbard & Jan Mayen Islands"),
        Swaziland("SZ", "SWZ", "Swaziland"),
        Sweden("SE", "SWE", "Sweden"),
        Switzerland("CH", "CHE", "Switzerland"),
        Syrian_Arab_Republic("SY", "SYR", "Syrian Arab Republic"),
        Taiwan("TW", "TWN", "Taiwan"),
        Tajikistan("TJ", "TJK", "Tajikistan"),
        Tanzania("TZ", "TZA", "Tanzania"),
        Thailand("TH", "THA", "Thailand"),
        Timor_Leste("TL", "TLS", "Timor-Leste"),
        Togo("TG", "TGO", "Togo"),
        Tokelau("TK", "TKL", "Tokelau (Tokelau Islands),"),
        Tonga("TO", "TON", "Tonga"),
        Trinidad_and_Tobago("TT", "TTO", "Trinidad and Tobago"),
        Tunisia("TN", "TUN", "Tunisia"),
        Turkey("TR", "TUR", "Turkey"),
        Turkmenistan("TM", "TKM", "Turkmenistan"),
        Turks_and_Caicos_Islands("TC", "TCA", "Turks and Caicos Islands"),
        Tuvalu("TV", "TUV", "Tuvalu"),
        US_Virgin_Islands("VI", "VIR", "US Virgin Islands"),
        Uganda("UG", "UGA", "Uganda"),
        Ukraine("UA", "UKR", "Ukraine"),
        United_Arab_Emirates("AE", "ARE", "United Arab Emirates"),
        United_Kingdom_of_Great_Britain___N__Ireland("GB", "GBR", "United Kingdom of Great Britain & N. Ireland"),
        United_States_Minor_Outlying_Islands("UM", "UMI", "United States Minor Outlying Islands"),
        United_States_of_America("US", "USA", "United States of America"),
        Uruguay("UY", "URY", "Uruguay"),
        Uzbekistan("UZ", "UZB", "Uzbekistan"),
        Vanuatu("VU", "VUT", "Vanuatu"),
        Venezuela("VE", "VEN", "Venezuela"),
        Viet_Nam("VN", "VNM", "Viet Nam"),
        Wallis_and_Futuna_Islands("WF", "WLF", "Wallis and Futuna Islands"),
        Western_Sahara("EH", "ESH", "Western Sahara"),
        Yemen("YE", "YEM", "Yemen"),
        Zambia("ZM", "ZMB", "Zambia"),
        Zimbabwe("ZW", "ZWE", "Zimbabwe"),
        British_Antarctic_Territory("BQ", "ATB", "British Antarctic Territory"),
        Burma("BU", "BUR", "Burma"),
        Byelorussian("BY", "BYS", "Byelorussian"),
        Canton___Enderbury_Islands("CT", "CTE", "Canton & Enderbury Islands"),
        Czechoslovakia("CS", "CSK", "Czechoslovakia"),
        Dahomey("DY", "DHY", "Dahomey"),
        Dronning_Maud_Land("NQ", "ATN", "Dronning Maud Land"),
        East_Timor("TP", "TMP", "East Timor"),
        Ethiopia2("ET", "ETH", "Ethiopia"),
        France2("FX", "FXX", "France"),
        French_fars_and_Issas("AI", "AFI", "French fars and Issas"),
        French_Southern_and_Antarctic_Territories("FQ", "ATF", "French Southern and Antarctic Territories"),
        German_Democratic_Republic("DD", "DDR", "German Democratic Republic"),
        Germany2("DE", "DEU", "Germany"),
        Gilbert___Ellice_Islands("GE", "GEL", "Gilbert & Ellice Islands"),
        Johnston_Island("JT", "JTN", "Johnston Island"),
        Midway_Islands("MI", "MID", "Midway Islands"),
        Netherlands_Antilles2("AN", "ANT", "Netherlands Antilles"),
        Neutral_Zone("NT", "NTZ", "Neutral Zone"),
        New_Hebrides("NH", "NHB", "New Hebrides"),
        Pacific_Islands("PC", "PCI", "Pacific Islands"),
        Panama2("PA", "PAN", "Panama"),
        Panama_Canal_Zone("PZ", "PCZ", "Panama Canal Zone"),
        Romania2("RO", "ROM", "Romania"),
        St__Kitts_Nevis_Anguilla("KN", "KNA", "St. Kitts-Nevis-Anguilla"),
        Sikkim("SK", "SKM", "Sikkim"),
        Southern_Rhodesia("RH", "RHO", "Southern Rhodesia"),
        Spanish_Sahara("EH", "ESH", "Spanish Sahara"),
        US_Miscellaneous_Pacific_Islands("PU", "PUS", "US Miscellaneous Pacific Islands"),
        USSR("SU", "SUN", "USSR"),
        Upper_Volta("HV", "HVO", "Upper Volta"),
        Vatican_City_State("VA", "VAT", "Vatican City State (Holy See)"),
        Viet_Nam2("VD", "VDR", "Viet-Nam"),
        Wake_Island("WK", "WAK", "Wake Island"),
        Yemen1("YD", "YMD", "Yemen"),
        Yemen2("YE", "YEM", "Yemen"),
        Yugoslavia1("YU", "YUG", "Yugoslavia"),
        Yugoslavia2("YU", "YUG", "Yugoslavia"),
        Zaire("ZR", "ZAR", "Zaire"),
        Unknown("XX", "XXX", "Unknown");
        
        private String alpha_2;
        private String alpha_3;
        private String name;

        ISO_3166(String alpha_2, String alpha_3, String name){
            this.alpha_2 = alpha_2;
            this.alpha_3 = alpha_3;
            this.name = name;
        }
        
        public String getAlpha2(){
            return alpha_2;
        }
        
        public String getAlpha3(){
            return alpha_3;
        }
        
        public String getCountry(){
            return name;
        }
        
        /** Find a value of ISO_3166 by searching for the alpha 2 code or 
         * the alpha 3 code or the name of the country. The name of the 
         * countries are in English only. */
        public ISO_3166 getISO_3166(String search){
            ISO_3166 iso = ISO_3166.Unknown;
            for(ISO_3166 x : ISO_3166.values()){
                if(search.equalsIgnoreCase(x.getAlpha2())){
                    iso = x;
                }
                if(search.equalsIgnoreCase(x.getAlpha3())){
                    iso = x;
                }
                if(search.equalsIgnoreCase(x.getCountry())){
                    iso = x;
                }
            }
            return iso;
        }
        
        @Override
        public String toString(){
            return getCountry() + ", " + getAlpha3() + ", " + getAlpha2();
        }
    }
    // </editor-fold>

    /** <p>Set the locale<br />Définit la locale.</p> */
    public static void setLocale(Locale loc){
        Language.loc = loc;
    }

    /** <p>Get the locale.<br />Obtient la locale.</p> */
    public static Locale getLocale(){
        return loc;
    }

    /** <p>Return a translation.<br />Retourne une traduction.</p> */
    public String getValueOf(String key){
        if (LangXX!=null && LangXX.isEmpty()==false){
            return LangXX.get(key);
        }else if (loc==Locale.US | loc==Locale.UK | loc==Locale.ENGLISH | loc==Locale.CANADA){
            return LangUS.get(key);
        }else if (loc==Locale.FRANCE | loc==Locale.FRENCH | loc==Locale.CANADA_FRENCH){
            return LangFR.get(key);
//            return null;
        }else{
            return null;
        }
    }

    /** <p>Initialize the class.<br />Initialise la classe.</p> */
    public void init(Locale loc){
        if (loc==Locale.US | loc==Locale.UK | loc==Locale.ENGLISH | loc==Locale.CANADA){
            LangUS.put("titleABT", "About Feuille");
            LangUS.put("titleAAD", "Choose an alpha...");
            LangUS.put("titleACD", "Choose your color...");
            LangUS.put("titleFDL", "Choose a font...");
            LangUS.put("titleOHD", "Online help...");
            LangUS.put("titleOPD", "Options");
            LangUS.put("titleXPED", "Collection exporter");
            LangUS.put("titleXPD", "Create or edit an effect...");
            LangUS.put("titleSD", "Insert a snippet...");
            LangUS.put("titleASD1", "Configure a style...");
            LangUS.put("titleASD2", "Styles");
            LangUS.put("titleASD3", "Styles of ");
            LangUS.put("titleDCD", "Choose a drawing");
            LangUS.put("titleXPD2", "Create or edit a particle...");            
            LangUS.put("titleGlyph", "Open a glyph");            
            LangUS.put("titleRotation", "Rotation");
            LangUS.put("titleGeo", "Geometric operation");
            LangUS.put("titleAnalysis", "Analysis");

            LangUS.put("buttonOk", "OK");
            LangUS.put("buttonCancel", "Cancel");
            LangUS.put("buttonAdd", "Add");
            LangUS.put("buttonGet", "Get");
            LangUS.put("buttonChange", "Change");
            LangUS.put("buttonDelete", "Delete");
            LangUS.put("buttonModify", "Modify");
            LangUS.put("buttonInMain", "In main");
            LangUS.put("buttonOfScreen", "Of screen");
            LangUS.put("buttonClose", "Close");
            LangUS.put("buttonAppTheme", "Apply");
            LangUS.put("buttonUpdate", "Update");
            LangUS.put("buttonImport", "Import...");
            LangUS.put("buttonDirectory", "Change the directory...");
            LangUS.put("buttonEdit", "Edit");
            LangUS.put("buttonRemove", "Remove");
            LangUS.put("buttonImpScr", "Import styles from script...");
            LangUS.put("buttonImpFil", "Import styles from file...");
            LangUS.put("buttonSave", "Save");
            LangUS.put("buttonCreate", "Create");

            LangUS.put("labelFxType", "Effects type : ");
            LangUS.put("labelName", "Name : ");
            LangUS.put("labelMoment", "Moment : ");
            LangUS.put("labelFirstLayer", "First layer : ");
            LangUS.put("labelTime", "Time (ms) : ");
            LangUS.put("labelPleaseHelpMe", "Please help me !?");
            LangUS.put("labelLayersDetails", "Layers details : ");
            LangUS.put("labelOverrides", "Overrides : ");
            LangUS.put("labelInnerOver", "Inner overrides : ");
            LangUS.put("labelLastOver", "Last overrides : ");
            LangUS.put("labelBeforeSyl", "Before syllable : ");
            LangUS.put("labelAfterSyl", "After syllable : ");
            LangUS.put("labelText", "Text : ");
            LangUS.put("labelKaraoke", "Karaoke : ");
            LangUS.put("labelBorder", "Border : ");
            LangUS.put("labelShadow", "Shadow : ");
            LangUS.put("labelFontName", "Font name : ");
            LangUS.put("labelFontSize", "Font size : ");
            LangUS.put("labelAlignment", "Alignment : ");
            LangUS.put("labelMarginL", "L : ");
            LangUS.put("labelMarginR", "R : ");
            LangUS.put("labelMarginTV", "V/T : ");
            LangUS.put("labelMarginB", "B : ");
            LangUS.put("labelScaleX", "Sc. X : ");
            LangUS.put("labelScaleY", "Sc. Y : ");
            LangUS.put("labelRotation", "Rot. : ");
            LangUS.put("labelSpacing", "Spac. : ");
            LangUS.put("labelEncoding", "Encoding : ");
            LangUS.put("labelAuthor", "Author(s) : ");
            LangUS.put("labelComments", "Comments : ");
            LangUS.put("labelPreview", "Preview : ");
            LangUS.put("labelCollection", "Collection : ");
            LangUS.put("labelTransparency", "Transparency : ");
            LangUS.put("labelRGBHTML", "RGB or HTML color : ");
            LangUS.put("labelBGR", "BGR color : ");
            LangUS.put("labelUniFont", "Unicode font :");
            LangUS.put("labelVideoSize", "Video size :");            
            LangUS.put("labelODTheme", "Theme :");
            LangUS.put("labelODBackImage", "Background image :");
            LangUS.put("labelODActivate", "Activation of modules :");
            LangUS.put("labelODLaunch", "At launch, show me :");
            LangUS.put("labelODExternalEditor", "External editor :");
            LangUS.put("labelODInst1", "In order to apply the translation, please restart the software.");
            LangUS.put("labelODInst2", "The selected modules will be available at launch, please restart the software.");
            LangUS.put("labelODInst3", "In order to open a script in another software, please add %FILE to your command line.");
            LangUS.put("labelSnippet", "<html><h2>Choose a snippet in the list below :");
            LangUS.put("labelStylesPack", "Packages :");
            LangUS.put("labelStylesScript", "Script :");
            LangUS.put("labelStylesXFX", "From XFX :");
            LangUS.put("labelImportStyles", "Choose the styles you want to import in your script :");
            LangUS.put("labelUsedByAFM", "Used by Feuille : ");
            LangUS.put("labelPBFont1", "Font : ");
            LangUS.put("labelPBFont2", "Correction : ");
            LangUS.put("labelVideoSize", "Video size : ");
            LangUS.put("labelPosY", "Position on Y : ");
            LangUS.put("labelStyle", "Style : ");
            LangUS.put("labelRotation2", "Angle of rotation : ");
            LangUS.put("labelGeoText", "<html>Choose the number of sides you want "
                    + "to have<br />(example : for a pentagone, 5 sides) :");
            LangUS.put("labelGeoSide", "sides");
            LangUS.put("labelWelcomeNews", "News :");
            LangUS.put("labelWelcomeDL", "Downloads :");

            LangUS.put("rbuttonBefore", "Before");
            LangUS.put("rbuttonMeantime", "Meantime");
            LangUS.put("rbuttonAfter", "After");
            LangUS.put("rbuttonModeNormal", "Normal");
            LangUS.put("rbuttonModePeriodic", "Periodic");
            LangUS.put("rbuttonModeRandom", "Random");
            LangUS.put("rbuttonModeSymmetric", "Symmetry");
            LangUS.put("rbuttonSubModeSentence", "Sentence");
            LangUS.put("rbuttonSubModeSyllable", "Syllable");
            LangUS.put("rbuttonGeoAntiClk", "Anti-clockwise direction");            
            LangUS.put("rbuttonGeoClk", "Clockwise direction");
            LangUS.put("rbuttonAnaNoComp", "No comparison");
            LangUS.put("rbuttonAnaCompSentence", "Comparison by sentence");
            LangUS.put("rbuttonAnaCompTime", "Comparison by time");
            LangUS.put("rbuttonAnaCompStyle", "Comparison by style");

            LangUS.put("checkboxSaveFx", "Save effects");
            LangUS.put("checkboxBold", "Bold");
            LangUS.put("checkboxItalic", "Italic");
            LangUS.put("checkboxUnderline", "Underline");
            LangUS.put("checkboxStrikeOut", "Strike out");
            LangUS.put("checkboxOpaqueBox", "Opaque box");
            LangUS.put("checkboxP1P0", "Include {\\p1} and {\\p0}");
            LangUS.put("checkboxForceISO", "Force the language to : ");
            LangUS.put("checkboxSave", "Save");
            LangUS.put("checkboxAnaPie", "Pie chart");
            LangUS.put("checkboxAnaBar", "Bar chart");
            LangUS.put("checkboxAnaWords", "Words");
            LangUS.put("checkboxODKaraModule", "Karaoke");
            LangUS.put("checkboxODCodeModule", "Code");
            LangUS.put("checkboxODDrawModule", "Drawing");
            LangUS.put("checkboxODAnalModule", "Analysis");

            LangUS.put("tabEffects", "Effects");
            LangUS.put("tabEmbedStyles", "Embedded styles");
            LangUS.put("tabOriginal", "Original");
            LangUS.put("tabResult", "Result");
            LangUS.put("tabRubyEdi", "Code editor");
            LangUS.put("tabDrawing", "Drawing editor");
            LangUS.put("tabVariables", "Variables");
            LangUS.put("tabSettings", "Settings");
            LangUS.put("tabStyle", "Style");
            LangUS.put("tabHelpPlease", "Help please");
            LangUS.put("tabWelcome", "Welcome");
            LangUS.put("tabKaraoke", "Karaoke");
            LangUS.put("tabAnalysis", "Analysis");
            LangUS.put("tabODMain", "Main");
            LangUS.put("tabODKaraoke", "Karaoke");
            LangUS.put("tabODCodeEditor", "Code editor");
            LangUS.put("tabODTranslation", "Translation");

            LangUS.put("tbdFont", "Font");
            LangUS.put("tbdSize", "Size");
            LangUS.put("tbdStyle", "Style");
            LangUS.put("tbdPreview", "Preview");
            LangUS.put("tbdParameters", "Parameters");
            LangUS.put("tbdModes", "Modes");
            LangUS.put("tbdAnaCompare", "Compare tables");
            LangUS.put("tbdAnaReport", "Make report");
            LangUS.put("tbdAnaOthers", "Others");
            LangUS.put("tbdODFontsPB", "Font with problems");

            LangUS.put("tableLayer", "Layer");
            LangUS.put("tableEffects", "Effects");
            LangUS.put("tableShortNumber", "#");
            LangUS.put("tableShortType", "T");
            LangUS.put("tableShortLayer", "L");
            LangUS.put("tableMargin", "Marg.");
            LangUS.put("tableStart", "Start");
            LangUS.put("tableEnd", "End");
            LangUS.put("tableTotal", "Total");
            LangUS.put("tableStyle", "Style");
            LangUS.put("tableName", "Name");
            LangUS.put("tableEffect", "Effect");
            LangUS.put("tableText", "Text");
            LangUS.put("tableSDType", "Type");
            LangUS.put("tableSDName", "Name");
            LangUS.put("tableSDDescription", "Description");
            LangUS.put("tableSDAuthor", "Author");
            LangUS.put("tableSDSnippet", "Snippet");
            LangUS.put("tableImportStyles1", "-");
            LangUS.put("tableImportStyles2", "Style");
            LangUS.put("tableFont", "Font");
            LangUS.put("tableCorrection", "Correction %");
            LangUS.put("tableTKey", "Key");
            LangUS.put("tableTValue", "Value");
            LangUS.put("tableParameters", "Parameters");
            LangUS.put("tableSettings", "Settings");

            LangUS.put("treeFxList", "Fx list");
            LangUS.put("treeRubyScripts", "Scripts");
            LangUS.put("treeXMLPresets", "XFX Effects");
            LangUS.put("treeParticles", "Particles");
            LangUS.put("treeEffects", "Effects");
            LangUS.put("treeInitialization", "Initialization");
            LangUS.put("treeAnimations", "Animations");
            
            LangUS.put("menuFile", "File");
            LangUS.put("menuGoWel", "Go to Welcome");
            LangUS.put("menuGoKara", "Go to Karaoke");
            LangUS.put("menuGoCode", "Go to Code editor");
            LangUS.put("menuGoDraw", "Go to Drawing editor");
            LangUS.put("menuGoAna", "Go to Analysis");
            LangUS.put("menuConf", "Configuration...");
            LangUS.put("menuQuit", "Quit");
            LangUS.put("menuRess", "Online external contents...");
            LangUS.put("menuAbout", "About...");
            
            LangUS.put("popmCut", "Cut");
            LangUS.put("popmCopy", "Copy");
            LangUS.put("popmPaste", "Paste");
            LangUS.put("popmDelete", "Delete");
            LangUS.put("popmClear", "Clear");
            LangUS.put("popmRemoveFX", "Remove FX");
            LangUS.put("popmRfReset", "Refresh the scripts list");
            LangUS.put("popmSelectAll", "Select all");
            LangUS.put("popmColor", "Choose a color...");
            LangUS.put("popmAlpha", "Choose an alpha...");
            LangUS.put("popmInsOver", "Insert overrides...");
            LangUS.put("popmInsCalc", "Insert a calc...");
            LangUS.put("popmInsFCalc", "Insert a float calc...");
            LangUS.put("popmInsDraw", "Insert a drawing...");
            LangUS.put("popmForAni", "For animation...");
            LangUS.put("popmForConf", "For configuration...");
            LangUS.put("popmSurround", "Surround by braces");
            LangUS.put("popmDelSur", "Delete all braces");
            LangUS.put("popmStyImp", "Import from clipboard");
            LangUS.put("popmStyExp", "Export to clipboard");
            LangUS.put("popm_b", "\\b - Bold");
            LangUS.put("popm_i", "\\i - Italic");
            LangUS.put("popm_u", "\\u - Underline");
            LangUS.put("popm_s", "\\s - Strike out");
            LangUS.put("popm_bord", "\\bord - Thickness of border");
            LangUS.put("popm_shad", "\\shad - Depth of shader");
            LangUS.put("popm_be", "\\be - Blur edge");
            LangUS.put("popm_blur", "\\blur - Blur");
            LangUS.put("popm_fs", "\\fs - Font size");
            LangUS.put("popm_fscx", "\\fscx - Font scale of X");
            LangUS.put("popm_fscy", "\\fscy - Font scale of Y");
            LangUS.put("popm_fsp", "\\fsp - Font spacing");
            LangUS.put("popm_frx", "\\frx - Font rotation on X");
            LangUS.put("popm_fry", "\\fry - Font rotation on Y");
            LangUS.put("popm_frz", "\\frz - Font rotation on Z");
            LangUS.put("popm_1c", "\\1c&H<hexa>& - Color of text");
            LangUS.put("popm_2c", "\\2c&H<hexa>& - Color of karaoke");
            LangUS.put("popm_3c", "\\3c&H<hexa>& - Color of border");
            LangUS.put("popm_4c", "\\4c&H<hexa>& - Color of shader");
            LangUS.put("popm_alpha", "\\alpha&H<hexa>& - Tranparency");
            LangUS.put("popm_1a", "\\1a&H<hexa>& - Tranparency of text");
            LangUS.put("popm_2a", "\\2a&H<hexa>& - Tranparency of karaoke");
            LangUS.put("popm_3a", "\\3a&H<hexa>& - Tranparency of border");
            LangUS.put("popm_4a", "\\4a&H<hexa>& - Tranparency of shader");
            LangUS.put("popm_k", "\\k - Simple karaoke");
            LangUS.put("popm_kf", "\\kf - Karaoke with fill");
            LangUS.put("popm_ko", "\\ko - Karaoke with outline");
            LangUS.put("popm_t", "\\t - Animation");
            LangUS.put("popm_r", "\\r - Reset");
            LangUS.put("popm_fn", "\\fn - Font name");
            LangUS.put("popm_fe", "\\fe - Font encoding");
            LangUS.put("popm_q", "\\q - Wrapping style");
            LangUS.put("popm_a", "\\a -Alignment (old)");
            LangUS.put("popm_an", "\\an - Alignment");
            LangUS.put("popm_pos", "\\pos - Position");
            LangUS.put("popm_move", "\\move - Position in real time");
            LangUS.put("popm_org", "\\org - Origin");
            LangUS.put("popm_fad", "\\fad - Fading");
            LangUS.put("popm_fade", "\\fade - Fading");
            LangUS.put("popm_clip", "\\clip - Region of visibility");
            LangUS.put("popm_clip2", "\\clip - Region of visibility");
            LangUS.put("popm_xbord", "\\xbord - Thickness of border on X");
            LangUS.put("popm_ybord", "\\ybord - Thickness of border on Y");
            LangUS.put("popm_xshad", "\\xshad - Depth of shader on X");
            LangUS.put("popm_yshad", "\\yshad - Depth of shader on Y");
            LangUS.put("popm_fax", "\\fax - Text shearing on X");
            LangUS.put("popm_fay", "\\fay - Text shearing on Y");
            LangUS.put("popm_iclip", "\\iclip - Region of  invisibility");
            LangUS.put("popm_fsc", "\\fsc - Font scale");
            LangUS.put("popm_fsvp", "\\fsvp - Leading");
            LangUS.put("popm_frs", "\\frs - Baseline obliquity");
            LangUS.put("popm_z", "\\z - Z coordinate");
            LangUS.put("popm_distort", "\\distort - Distortion");
            LangUS.put("popm_md", "\\md - Boundaries deforming");
            LangUS.put("popm_mdx", "\\mdx - Boundaries deforming on X");
            LangUS.put("popm_mdy", "\\mdy - Boundaries deforming on Y");
            LangUS.put("popm_mdz", "\\mdz - Boundaries deforming on Z");
            LangUS.put("popm_1vc", "\\1vc - Gradients on text (color)");
            LangUS.put("popm_2vc", "\\2vc - Gradients on karaoke (color)");
            LangUS.put("popm_3vc", "\\3vc - Gradients on border (color)");
            LangUS.put("popm_4vc", "\\4vc - Gradients on shader (color)");
            LangUS.put("popm_1va", "\\1va - Gradients on text (transparency)");
            LangUS.put("popm_2va", "\\2va - Gradients on karaoke (transparency)");
            LangUS.put("popm_3va", "\\3va - Gradients on border (transparency)");
            LangUS.put("popm_4va", "\\4va - Gradients on shader (transparency)");
            LangUS.put("popm_1img", "\\1img - Image fill on text");
            LangUS.put("popm_2img", "\\2img - Image fill on karaoke");
            LangUS.put("popm_3img", "\\3img - Image fill on border");
            LangUS.put("popm_4img", "\\4img - Image fill on shader");
            LangUS.put("popm_jitter", "\\jitter - Shaking");
            LangUS.put("popm_mover", "\\mover - Polar move");
            LangUS.put("popm_moves3", "\\moves3 - Spline move");
            LangUS.put("popm_moves4", "\\moves4 - Spline move");
            LangUS.put("popm_movevc", "\\movevc - Moveable vector clip");
            LangUS.put("popSkeleton", "Insert skeleton");
            LangUS.put("popFromCommands", "Update from...");
            LangUS.put("popCodePNG", "Choose a PNG image...");
            LangUS.put("popCodeSnippet", "Insert snippet...");
            LangUS.put("popmImportFrom", "Import from a script...");
            LangUS.put("popmGetSelLine", "Get the selected line");
            LangUS.put("popmInsScript", "Script...");
            LangUS.put("popmCodeInit", "Initialize the script");
            LangUS.put("popmCodeDef", "Insert def");
            LangUS.put("popmRfInfo", "Get info about this object");
            LangUS.put("popmXFXActive", "Activate the parameter");
            LangUS.put("popmXFXInactive", "Deactivate the parameter");

            LangUS.put("optpTitle1", "Error");
            LangUS.put("optpTitle2", "Confirm");
            LangUS.put("optpTitle3", "Aborted");
            LangUS.put("optpTitle4", "Existing file");
            LangUS.put("optpMessage1", "Sorry but there is no collection in the list of effects.");            
            LangUS.put("optpMessage2", "Would you really want to delete this item ?");
            LangUS.put("optpMessage3", "You cannot remove the Default package.");
            LangUS.put("optpMessage4", "Do you really want to delete this package ?");
            LangUS.put("optpMessage5", "A package already uses the same name.");
            LangUS.put("optpMessage6", "Would you really overwrite the existing file ?");
            LangUS.put("optpMessage7", "The file has been created or updated.");
            LangUS.put("optpMessage8", "Dou you want to execute the script ?");
            LangUS.put("optpGetInfo1", "Name : ");
            LangUS.put("optpGetInfo2", "Version : ");
            LangUS.put("optpGetInfo3", "Author(s) : ");
            LangUS.put("optpGetInfo4", "Path : ");
            LangUS.put("optpGetInfo5", "Function : ");
            LangUS.put("optpGetInfo6", "Description : ");
            LangUS.put("optpXFXInit", "Not in animation tag !");
            LangUS.put("optpSDConfirmRequest", "Do you really want to do this ?");
            LangUS.put("optpSDConfirmChoice", "Select a choice");

            LangUS.put("toolQuit", "Close");
            LangUS.put("toolOpen", "Open");
            LangUS.put("toolNew", "New");
            LangUS.put("toolSave", "Save");
            LangUS.put("toolSave1", "Save original lines to an ASS Script.");
            LangUS.put("toolSave2", "Save result lines to an ASS Script.");
            LangUS.put("toolNormal", "<html>Normal : See the normal karaoke"
                    + " text<br>(e.g. {\\k20}Wa{\\k10}ta{\\k10}shi {\\k20}no"
                    + " {\\k60}{\\k70}chi{\\k50}ka{\\k60}ra...)");
            LangUS.put("toolItems", "<html>Items : See the karaoke text with"
                    + " items<br>(e.g. ◆Wa◆ta◆shi ◆no ◆◆chi◆ka◆ra...)");
            LangUS.put("toolStrip", "<html>Strip : See the karaoke text"
                    + " without any marks<br>(e.g. Watashi no chikara...)");
            LangUS.put("toolForOneLine", "<html>Execute associated XMLPresets"
                    + " for the selected lines by giving each line to the"
                    + " function which make the effects printout in result"
                    + " tab. <br>This is the default mode.<br>Note that your"
                    + " function (defined in filters or scripts) may support"
                    + " this method or not.");
            LangUS.put("toolForFewLines", "<html>Execute associated XMLPresets"
                    + " for the selected lines by giving all lines to the"
                    + " function which make the effects printout in result"
                    + " tab. <br>This is an other vision of 'how to do' your"
                    + " karaoke. Getting all lines can help you create heavy"
                    + " effets. <br>Note that your function (defined in"
                    + " filters or scripts) may support this method or not.");
            LangUS.put("toolOptions", "Options");
            LangUS.put("toolLinks", "Help me with links !");
            LangUS.put("toolAbout", "About");
            LangUS.put("toolAddFxoToLine", "Add selected effect(s) to"
                    + " selected line(s)");
            LangUS.put("toolAddXmlPresetFx", "Add a new XML-fx effect.");
            LangUS.put("toolModXmlPresetFx", "Modify a XML-fx effect.");
            LangUS.put("toolDelXmlPresetFx", "Delete a XML-fx effect.");
            LangUS.put("toolImpXmlPresetFx", "Import news XML-fx effects"
                    + " from a collection.");
            LangUS.put("toolExpXmlPresetFx", "Export XML-fx effects from"
                    + " a collection.");
            LangUS.put("toolModRuby", "Open your ruby editor.");
            LangUS.put("toolHelpMe","<html>Please help me :<br />"
                    + "<table>"
                    + "<tr><td>%sK</td><td>start Karaoke</td><td>when syllabe"
                    + " must start highlight</td></tr>"
                    + "<tr><td>%eK</td><td>end Karaoke</td><td>when syllabe"
                    + " must stop highlight</td></tr>"
                    + "<tr><td>%dK</td><td>duration of Karaoke</td><td>"
                    + "</td></tr><tr><td>%osK</td><td>start Karaoke</td>"
                    + "<td>as original %sK (if perletter mode)</td></tr>"
                    + "<tr><td>%oeK</td><td>end Karaoke</td><td>as original"
                    + " %eK (if perletter mode)</td></tr><tr><td>%odK</td>"
                    + "<td>duration of Karaoke</td><td>as original %dK"
                    + " (if perletter mode)</td></tr><tr><td>%dP</td>"
                    + "<td>duration of sentence</td><td></td></tr>"
                    + "<tr><td>%posAF[]</td><td>randomized position</td><td>"
                    + "</td></tr><tr><td>%num[]</td><td>randomized number</td>"
                    + "<td></td></tr><tr><td>$X</td><td>variable"
                    + "</td><td></td></tr></table>");
            LangUS.put("toolDrawNew", "Clear the drawing area.");
            LangUS.put("toolDrawImage", "Load an image.");
            LangUS.put("toolDrawTrans", "Transparency of the background image (if loaded).");
            LangUS.put("toolDrawOpen", "Open a drawing.");
            LangUS.put("toolDrawSave", "Save a drawing.");
            LangUS.put("toolDrawLine", "Draw a line.");
            LangUS.put("toolDrawBezier", "Draw a bézier.");
            LangUS.put("toolFromCommands", "Update from the selected command.");
            LangUS.put("toolStylesAddPack", "Add a new package.");
            LangUS.put("toolStylesDelPack", "Delete a package.");
            LangUS.put("toolStylesStoScr", "Copy the selected style to script.");
            LangUS.put("toolStylesScrSto", "Copy the selected style to package.");
            LangUS.put("toolStylesAddSto", "Add a new style to the package.");
            LangUS.put("toolStylesAddScr", "Add a new style to the script.");
            LangUS.put("toolStylesEditSto", "Edit the selected style of the package.");
            LangUS.put("toolStylesEditScr", "Edit the selected style of the script.");
            LangUS.put("toolStylesCopySto", "Copy the selected style of the package.");
            LangUS.put("toolStylesCopyScr", "Copy the selected style of the script.");
            LangUS.put("toolStylesRemSto", "Remove all selected styles of the package.");
            LangUS.put("toolStylesRemScr", "Remove all selected styles of the script.");
            LangUS.put("toolStylesEmbSto", "Copy the selected style to package.");
            LangUS.put("toolStylesEmbScr", "Copy the selected style to script.");
            LangUS.put("toolStyles", "Modify styles.");            
            LangUS.put("toolAudioOpen", "Open a new sound...");
            LangUS.put("toolAudioPlay", "Play the sound from the beginning.");
            LangUS.put("toolAudioStop", "Stop the sound.");
            LangUS.put("toolAudioPlayArea", "Play the delimited part of the sound.");
            LangUS.put("toolAudioPlayBB", "Play before the beginning of the delimited area.");            
            LangUS.put("toolAudioPlayAB", "Play after the beginning of the delimited area.");
            LangUS.put("toolAudioPlayBE", "Play before the end of the delimited area.");
            LangUS.put("toolAudioPlayAE", "Play after the end of the delimited area.");
            LangUS.put("toolAudioNewTime", "Get the time of the start and the time of the end.");
            LangUS.put("toolAudioSetKara", "Set the karaoke.");
            LangUS.put("toolAudioGetKara", "Get the karaoke.");
            LangUS.put("toolAudioWavView", "Create or edit the ASS file using a wav file.");
            LangUS.put("toolCodeDef", "Insert a function for a variable.");
            LangUS.put("toolCreateParticle", "Add a new particle.");
            LangUS.put("toolEditParticle", "Modify a particle.");
            LangUS.put("toolDeleteParticle", "Delete a particle.");
            LangUS.put("toolXFXAddInit", "Add an effect in Initialization or an animation in Animations.");
            LangUS.put("toolXFXAddAnim", "Add an effect in Animation.");
            LangUS.put("toolXFXDelete", "Remove.");
            LangUS.put("toolXFXChange", "Switch settings.");
            LangUS.put("toolTbRuby", "Define Ruby (JRuby) as code type.");
            LangUS.put("toolTbPython", "Define Python (Jython) as code type.");
            LangUS.put("toolBScriptOK", "Launch selected plugin.");
            LangUS.put("toolAnalysis", "Analysis");

            LangUS.put("messTools", " Here are tools and resources you can use"
                    + " to have more skills dealing with scripts : ");
            LangUS.put("messExport", "<html><h3>Please select the collection"
                    + " to export.");
            LangUS.put("messExportSave", "<html><h3>Please select where the"
                    + " file must be saved.");
            LangUS.put("messAbout", "<html><p align=\"center\"><b>Feuille</b> "
                    + "(Funsub project 2006-2014).<br />GNU/GPLv3 - Free and open for all."
                    + " <br />Feuille is made to think differently on "
                    + "Windows, Linux and Mac OS X.<br />"
                    + "<i><b>Developed by The Wingate 2940.<br />"
                    + "Contact at assfxmaker@gmail.com</b></i>.</p>");
            LangUS.put("messCopyOf", "Copy of ");
            LangUS.put("messAssSketchpad", "<html>This software can create or"
                    + " edit drawings in ASS format.<br />It has been created"
                    + " to help those who want to use a drawing tool on Linux"
                    + " and Mac OS X.<br /><br />Evolution : ZDrawing -> "
                    + "ZDrawingLite -> AssSketchpad. (Funsub project)<br />"
                    + "This software is under GNU/GPLv3 licence, open and free"
                    + " to use, for all.");
            
            LangUS.put("paneHelpPleaseXFX","%sK - Time of start of syllable   or   Time of start of letter\n"
                    + "%eK - Time of end of syllable   or   Time of end of letter\n"
                    + "%dK - Time of duration of syllable   or   Time of duration of letter\n"
                    + "%osK - Time of start of syllable\n"
                    + "%oeK - Time of end of syllable\n"
                    + "%odK - Time of duration of syllable\n\n"
                    + "%dP - Time of duration of sentence\n"
                    + "%posAF[x1,y1,x2,y2] - Give a random location for x and y\n"
                    + "%num[a1,a2] - Give a random number\n\n"
                    + "$wordinlowercase - Return the result of a function which is in the « Variables » tab.");
            LangUS.put("paneHelpPleaseParticle","%XL - Position on X at the left of the syllable.\n"
                    + "%XC - Position on X at the center of the syllable.\n"
                    + "%XR - Position on X at the right of the syllable.\n\n"
                    + "%XLF - Position on X at the left of the first syllable.\n"
                    + "%XCF - Position on X at the center of the first syllable.\n"
                    + "%XRF - Position on X at the right of the first syllable.\n\n"
                    + "%XLL - Position on X at the left of the last syllable.\n"
                    + "%XCL - Position on X at the center of the last syllable.\n"
                    + "%XRL - Position on X at the right of the last syllable.\n\n"
                    + "%Y - Position on Y defined in the settings.\n\n"
                    + "%sK - Time of start of syllable   or   Time of start of letter\n"
                    + "%eK - Time of end of syllable   or   Time of end of letter\n"
                    + "%dK - Time of duration of syllable   or   Time of duration of letter\n"
                    + "%osK - Time of start of syllable\n"
                    + "%oeK - Time of end of syllable\n"
                    + "%odK - Time of duration of syllable\n\n"
                    + "%dP - Time of duration of sentence\n"
                    + "%posAF[x1,y1,x2,y2] - Give a random location for x and y\n"
                    + "%num[a1,a2] - Give a random number\n\n"
                    + "$wordinlowercase - Return the result of a function which is in the « Variables » tab.");
            
            LangUS.put("textboxName", "Name");
            LangUS.put("textboxAuthor", "Author");
            LangUS.put("textboxDesc", "Description");
            
            LangUS.put("enumODWelc", "Welcome");
            LangUS.put("enumODKara", "Karaoke");
            LangUS.put("enumODCode", "Code editor");
            LangUS.put("enumODDraw", "Drawing editor");
            LangUS.put("enumODAnal", "Analysis");
            
            LangUS.put("xfxparam_01", "Alignment");
            LangUS.put("xfxparam_02", "Start");
            LangUS.put("xfxparam_03", "End");
            LangUS.put("xfxparam_04", "Acceleration");
            LangUS.put("xfxparam_05", "Value");
            LangUS.put("xfxparam_06", "Scale");
            LangUS.put("xfxparam_07", "Drawings");
            LangUS.put("xfxparam_08", "Color");
            LangUS.put("xfxparam_09", "Fade in time");
            LangUS.put("xfxparam_10", "Fade out time");
            LangUS.put("xfxparam_11", "Start time");
            LangUS.put("xfxparam_12", "End time");
            LangUS.put("xfxparam_13", "Alpha 1");
            LangUS.put("xfxparam_14", "Alpha 2");
            LangUS.put("xfxparam_15", "Alpha 3");
            LangUS.put("xfxparam_16", "Font encoding");
            LangUS.put("xfxparam_17", "Font name");
            LangUS.put("xfxparam_18", "Angle");
            LangUS.put("xfxparam_19", "Percent");
            LangUS.put("xfxparam_20", "Size");
            LangUS.put("xfxparam_21", "Transparency");
            LangUS.put("xfxparam_22", "File");
            LangUS.put("xfxparam_23", "Offset on X");
            LangUS.put("xfxparam_24", "Offset on Y");
            LangUS.put("xfxparam_25", "Left");
            LangUS.put("xfxparam_26", "Right");
            LangUS.put("xfxparam_27", "Up");
            LangUS.put("xfxparam_28", "Down");
            LangUS.put("xfxparam_29", "Period");
            LangUS.put("xfxparam_30", "Seed");
            LangUS.put("xfxparam_31", "Duration");
            LangUS.put("xfxparam_32", "Position on X1 (start)");
            LangUS.put("xfxparam_33", "Position on Y1 (start)");
            LangUS.put("xfxparam_34", "Position on X2 (end)");
            LangUS.put("xfxparam_35", "Position on Y2 (end)");
            LangUS.put("xfxparam_36", "Angle at start");
            LangUS.put("xfxparam_37", "Angle at end");
            LangUS.put("xfxparam_38", "Radius at start");
            LangUS.put("xfxparam_39", "Radius at end");
            LangUS.put("xfxparam_40", "Position on X2 (middle)");
            LangUS.put("xfxparam_41", "Position on Y2 (middle)");
            LangUS.put("xfxparam_42", "Position on X3 (end)");
            LangUS.put("xfxparam_43", "Position on Y3 (end)");
            LangUS.put("xfxparam_44", "Position on X2 (2/4)");
            LangUS.put("xfxparam_45", "Position on Y2 (2/4)");
            LangUS.put("xfxparam_46", "Position on X3 (3/4)");
            LangUS.put("xfxparam_47", "Position on Y3 (3/4)");
            LangUS.put("xfxparam_48", "Position on X4 (end)");
            LangUS.put("xfxparam_49", "Position on Y4 (end)");
            LangUS.put("xfxparam_50", "Origin on X");
            LangUS.put("xfxparam_51", "Origin on Y");
            LangUS.put("xfxparam_52", "Position on X");
            LangUS.put("xfxparam_53", "Position on Y");
            LangUS.put("xfxparam_54", "Style");
            LangUS.put("xfxparam_55", "Wrapping style");
            
            LangUS.put("ifrOri", "Original karaoke");
            LangUS.put("ifrRes", "Result karaoke");
            LangUS.put("ifrSound", "Waveform");
            LangUS.put("ifrTree", "Effects list");
            LangUS.put("ifrCodeEditor", "Code editor");            
            LangUS.put("assSketchpadFile", "File");
            LangUS.put("assSketchpadToDraw", "To draw");
            LangUS.put("assSketchpadImage", "Image");
            LangUS.put("assSketchpadShape", "Shape and scale");
            LangUS.put("assSketchpadScale", "Scale");
            LangUS.put("assSketchpadOperations", "Operations");
            LangUS.put("assSketchpadLayers", "Layers");
            LangUS.put("assSketchpadHistoric", "Historic");
            LangUS.put("assSketchpadSketchpad", "Sketchpad");
            LangUS.put("assSketchpadMode", "Mode");
            LangUS.put("assSketchpadScript", "Your scripts");
            LangUS.put("assSketchpadAssComs", "ASS commandes");
            LangUS.put("assSketchpadOrnament", "Ornament");
            LangUS.put("ifrFirstTable", "Old");
            LangUS.put("ifrSecondTable", "New");
            LangUS.put("ifrFirstReport", "Before");
            LangUS.put("ifrSecondReport", "After");
            LangUS.put("ifrWelcome", "Welcome");
            
            LangUS.put("assSketchpadAss_Commands", "Ass commands : ");
            LangUS.put("assSketchpadCut", "Cut");
            LangUS.put("assSketchpadCopy", "Copy");
            LangUS.put("assSketchpadPaste", "Paste");
            LangUS.put("assSketchpadDelete", "Delete");
            LangUS.put("assSketchpadUpdateFromComs", "Update");
            LangUS.put("assSketchpadChoose_Color", "Choose a color...");
            LangUS.put("assSketchpadRename_Layer", "Rename the layer");
            LangUS.put("assSketchpadClear_Sketch", "Clear the sketch");
            LangUS.put("assSketchpadRubber_Size", "Rubber size");
            LangUS.put("assSketchpadPen_Size", "Pen size");
            LangUS.put("assSketchpadBy_Value", "By");
            LangUS.put("assSketchpadNew", "Create a new drawing");
            LangUS.put("assSketchpadOpen", "Open a file");
            LangUS.put("assSketchpadSave", "Save a file");
            LangUS.put("assSketchpadLine", "Draw lines");
            LangUS.put("assSketchpadCurve", "Draw curves");
            LangUS.put("assSketchpadPen", "Draw a sketch");
            LangUS.put("assSketchpadRubber", "Clear a sketch");
            LangUS.put("assSketchpadClear_Image", "Clear image");
            LangUS.put("assSketchpadOpen_Image", "Open image");
            LangUS.put("assSketchpadTop", "Move an image to the top");
            LangUS.put("assSketchpadLeft", "Move an image to the left");
            LangUS.put("assSketchpadCenter", "Center an image");
            LangUS.put("assSketchpadRight", "Move an image to the right");
            LangUS.put("assSketchpadTrans_Image", "Transparency of image");
            LangUS.put("assSketchpadBottom", "Move an image to the bottom");
            LangUS.put("assSketchpadTrans_Shape", "Transparency of shape");
            LangUS.put("assSketchpadScale_Size", "Scale size");
            LangUS.put("assSketchpadTranslate", "Translate the drawing");
            LangUS.put("assSketchpadRotate", "Rotate the drawing");
            LangUS.put("assSketchpadAdd_Layer", "Add a layer");
            LangUS.put("assSketchpadRemove_Layer", "Remove a layer");
            LangUS.put("assSketchpadLayers_List", "Layers list");
            LangUS.put("assSketchpadUndo", "Clear a point of the current layer");
            LangUS.put("assSketchpadPen_Color", "Choose the color of the pen...");
            LangUS.put("assSketchpadOK_Button", "OK");
            LangUS.put("assSketchpadCancel_Button", "Cancel");
            LangUS.put("assSketchpadTransX", "Translation on X");
            LangUS.put("assSketchpadTransY", "Translation on Y");
            LangUS.put("assSketchpadRedRotateMessage", "");
            LangUS.put("assSketchpadRotateMessage", "Angle of rotation");
            LangUS.put("assSketchpadBSpline", "Draw a bspline");
            LangUS.put("assSketchpadBSplineClose", "Close a bspline");
            LangUS.put("assSketchpadBSplineExtend", "Extend a bspline");
            LangUS.put("assSketchpadGoToA", "Go to (n command)");
            LangUS.put("assSketchpadGoToB", "Go to (m command");
            LangUS.put("assSketchpadDuplicate", "Duplicate the layer");
            LangUS.put("assSketchpadImpFonts", "Import shapes from fonts");
            LangUS.put("assSketchpadNormalMode", "Normal mode");
            LangUS.put("assSketchpadOrnMode", "Glue mode");
            LangUS.put("assSketchpadResize", "Resize");
            LangUS.put("assSketchpadShear", "Shear");
            LangUS.put("assSketchpadSelect", "Selection");
            LangUS.put("assSketchpadReady", "Ready !");
            LangUS.put("assSketchpadSteadyGo", "Generated !");
            LangUS.put("assSketchpadGMovement", "Main move");
            LangUS.put("assSketchpadOrnYes", "Yes");
            LangUS.put("assSketchpadOrnNo", "No");
            LangUS.put("assSketchpadFrequency", "Frequency :");
            LangUS.put("assSketchpadOrnShape", "Shape");
            LangUS.put("assSketchpadOrnDuration", "Duration");            
            LangUS.put("assSketchpadSelCopyPaste", "Put one after the other");
            LangUS.put("assSketchpadSelSym", "Do the symmetry");
            LangUS.put("assSketchpadSelGeo1A", "Around a shape : clockwise 'Line'");
            LangUS.put("assSketchpadSelGeo1B", "Around a shape : anti-clockwise 'Line'");
            LangUS.put("assSketchpadSelGeo2A", "Around a shape : clockwise 'Triangle'");
            LangUS.put("assSketchpadSelGeo2B", "Around a shape : anti-clockwise 'Triangle'");
            LangUS.put("assSketchpadSelGeo3A", "Around a shape : clockwise 'Square'");
            LangUS.put("assSketchpadSelGeo3B", "Around a shape : anti-clockwise 'Square'");
            LangUS.put("assSketchpadSelGeoPlus", "Around other shape...");
        }else if (loc==Locale.FRANCE | loc==Locale.FRENCH | loc==Locale.CANADA_FRENCH){
            LangFR.put("titleABT", "À propos de Feuille");
            LangFR.put("titleAAD", "Choisir la transparence...");
            LangFR.put("titleACD", "Choisir la couleur...");
            LangFR.put("titleFDL", "Choisir la police...");
            LangFR.put("titleOHD", "Aide en ligne...");
            LangFR.put("titleOPD", "Options");
            LangFR.put("titleXPED", "Exporteur de collection");
            LangFR.put("titleXPD", "Créer ou éditer un effet...");
            LangFR.put("titleSD", "Inserer un snippet...");
            LangFR.put("titleASD1", "Configurer un style...");
            LangFR.put("titleASD2", "Styles");
            LangFR.put("titleASD3", "Styles de ");
            LangFR.put("titleDCD", "Choisir un dessin");
            LangFR.put("titleXPD2", "Créer ou éditer une particule...");            
            LangFR.put("titleGlyph", "Ouvrir un caractère");            
            LangFR.put("titleRotation", "Rotation");
            LangFR.put("titleGeo", "Copie selon cotés");
            LangFR.put("titleAnalysis", "Analyse");
            
            LangFR.put("buttonOk", "OK");
            LangFR.put("buttonCancel", "Annuler");
            LangFR.put("buttonAdd", "Ajouter");
            LangFR.put("buttonGet", "Obtenir");
            LangFR.put("buttonChange", "Changer");
            LangFR.put("buttonDelete", "Enlever");
            LangFR.put("buttonModify", "Modifier");
            LangFR.put("buttonInMain", "D'ici");
            LangFR.put("buttonOfScreen", "De l'écran");
            LangFR.put("buttonClose", "Fermer");
            LangFR.put("buttonAppTheme", "Appliquer");
            LangFR.put("buttonUpdate", "Mise à jour");
            LangFR.put("buttonImport", "Importer...");
            LangFR.put("buttonDirectory", "Changer le répertoire...");
            LangFR.put("buttonEdit", "Éditer");
            LangFR.put("buttonRemove", "Enlever");
            LangFR.put("buttonImpScr", "Importer des styles d'un script...");
            LangFR.put("buttonImpFil", "Importer des styles d'un fichier...");
            LangFR.put("buttonSave", "Sauver");
            LangFR.put("buttonCreate", "Créer");

            LangFR.put("labelFxType", "Type d'effets : ");
            LangFR.put("labelName", "Nom : ");
            LangFR.put("labelMoment", "Moment : ");
            LangFR.put("labelFirstLayer", "Première couche : ");
            LangFR.put("labelTime", "Temps (ms) : ");
            LangFR.put("labelPleaseHelpMe", "Aidez-moi !?");
            LangFR.put("labelLayersDetails", "Détails par couches : ");
            LangFR.put("labelOverrides", "Surcharges : ");
            LangFR.put("labelInnerOver", "Surch. internes : ");
            LangFR.put("labelLastOver", "Dernières surch. : ");
            LangFR.put("labelBeforeSyl", "Avant la syllabe : ");
            LangFR.put("labelAfterSyl", "Après la syllabe : ");
            LangFR.put("labelText", "Texte : ");
            LangFR.put("labelKaraoke", "Karaoké : ");
            LangFR.put("labelBorder", "Bordure : ");
            LangFR.put("labelShadow", "Ombre : ");
            LangFR.put("labelFontName", "Police : ");
            LangFR.put("labelFontSize", "Taille : ");
            LangFR.put("labelAlignment", "Alignement : ");
            LangFR.put("labelMarginL", "G : ");
            LangFR.put("labelMarginR", "D : ");
            LangFR.put("labelMarginTV", "V/H : ");
            LangFR.put("labelMarginB", "B : ");
            LangFR.put("labelScaleX", "Éch. X : ");
            LangFR.put("labelScaleY", "Éch. Y : ");
            LangFR.put("labelRotation", "Rot. : ");
            LangFR.put("labelSpacing", "Esp. : ");
            LangFR.put("labelEncoding", "Encodage : ");
            LangFR.put("labelAuthor", "Auteur(s) : ");
            LangFR.put("labelComments", "Commentaires : ");
            LangFR.put("labelPreview", "Aperçu : ");
            LangFR.put("labelCollection", "Collection : ");
            LangFR.put("labelTransparency", "Transparence : ");
            LangFR.put("labelRGBHTML", "Couleur RGB/HTML : ");
            LangFR.put("labelBGR", "Couleur BGR : ");
            LangFR.put("labelUniFont", "Police Unicode :");
            LangFR.put("labelVideoSize", "Taille de la vidéo :");            
            LangFR.put("labelODTheme", "Thème :");
            LangFR.put("labelODBackImage", "Image de fond :");
            LangFR.put("labelODActivate", "Activation des modules :");
            LangFR.put("labelODLaunch", "Au lancement, voir :");
            LangFR.put("labelODExternalEditor", "Éditeur externe :");
            LangFR.put("labelODInst1", "Pour appliquer la traduction, relancez le logiciel.");
            LangFR.put("labelODInst2", "Les modules sélectionnés seront disponible au lancement, relancez le logiciel.");
            LangFR.put("labelODInst3", "Pour pouvoir ouvrir un script dans un autre logiciel, rajoutez %FILE à votre ligne de commande.");            
            LangFR.put("labelSnippet", "<html><h2>Choisir un snippet dans la liste ci-dessous :");
            LangFR.put("labelStylesPack", "Paquets :");
            LangFR.put("labelStylesScript", "Script :");
            LangFR.put("labelStylesXFX", "Depuis XFX :");
            LangFR.put("labelImportStyles", "Choisir les styles que vous voulez importer dans votre script :");
            LangFR.put("labelUsedByAFM", "Utilisé par Feuille : ");
            LangFR.put("labelPBFont1", "Police : ");
            LangFR.put("labelPBFont2", "Correction : ");
            LangFR.put("labelVideoSize", "Taille de la vidéo : ");
            LangFR.put("labelPosY", "Position sur Y : ");
            LangFR.put("labelStyle", "Style : ");
            LangFR.put("labelRotation2", "Angle de rotation : ");            
            LangFR.put("labelGeoText", "<html>Choisissez le nombre de coté au total "
                    + "pour faire<br />le tour de votre forme (exemple : hexagone, 6 cotés) :");
            LangFR.put("labelGeoSide", "cotés");
            LangFR.put("labelWelcomeNews", "Nouvelles :");
            LangFR.put("labelWelcomeDL", "Téléchargements :");
            
            LangFR.put("rbuttonBefore", "Avant");
            LangFR.put("rbuttonMeantime", "Pendant");
            LangFR.put("rbuttonAfter", "Après");
            LangFR.put("rbuttonModeNormal", "Normal");
            LangFR.put("rbuttonModePeriodic", "Périodique");
            LangFR.put("rbuttonModeRandom", "Aléatoire");
            LangFR.put("rbuttonModeSymmetric", "Symétrie");
            LangFR.put("rbuttonSubModeSentence", "Phrase");
            LangFR.put("rbuttonSubModeSyllable", "Syllabe");
            LangFR.put("rbuttonGeoAntiClk", "Sens anti-horaire");            
            LangFR.put("rbuttonGeoClk", "Sens horaire");
            LangFR.put("rbuttonAnaNoComp", "Pas de comparaison");
            LangFR.put("rbuttonAnaCompSentence", "Comparer les phrases");
            LangFR.put("rbuttonAnaCompTime", "Comparer les temps");
            LangFR.put("rbuttonAnaCompStyle", "Comparer les styles");

            LangFR.put("checkboxSaveFx", "Sauvegarder les effets");
            LangFR.put("checkboxBold", "Gras");
            LangFR.put("checkboxItalic", "Italique");
            LangFR.put("checkboxUnderline", "Souligné");
            LangFR.put("checkboxStrikeOut", "Barré");
            LangFR.put("checkboxOpaqueBox", "Boîte opaque");
            LangFR.put("checkboxP1P0", "Inclure {\\p1} et {\\p0}");
            LangFR.put("checkboxForceISO", "Forcer le langage à : ");
            LangFR.put("checkboxSave", "Sauvegarder");
            LangFR.put("checkboxAnaPie", "Graphique circulaire");
            LangFR.put("checkboxAnaBar", "Histogramme");
            LangFR.put("checkboxAnaWords", "Mots");
            LangFR.put("checkboxODKaraModule", "Karaoké");
            LangFR.put("checkboxODCodeModule", "Code");
            LangFR.put("checkboxODDrawModule", "Dessin");
            LangFR.put("checkboxODAnalModule", "Analyse");

            LangFR.put("tabEffects", "Effets");
            LangFR.put("tabEmbedStyles", "Styles embarqués");
            LangFR.put("tabOriginal", "Original");
            LangFR.put("tabResult", "Résultat");
            LangFR.put("tabRubyEdi", "Éditeur de code");
            LangFR.put("tabDrawing", "Éditeur de dessin");
            LangFR.put("tabVariables", "Variables");
            LangFR.put("tabSettings", "Configuration");
            LangFR.put("tabStyle", "Style");
            LangFR.put("tabHelpPlease", "À l'aide");
            LangFR.put("tabWelcome", "Bienvenue");
            LangFR.put("tabKaraoke", "Karaoké");
            LangFR.put("tabAnalysis", "Analyse");
            LangFR.put("tabODMain", "Général");
            LangFR.put("tabODKaraoke", "Karaoké");
            LangFR.put("tabODCodeEditor", "Éditeur de code");
            LangFR.put("tabODTranslation", "Traduction");

            LangFR.put("tbdFont", "Police");
            LangFR.put("tbdSize", "Taille");
            LangFR.put("tbdStyle", "Style");
            LangFR.put("tbdPreview", "Aperçu");
            LangFR.put("tbdParameters", "Paramètres");
            LangFR.put("tbdModes", "Modes");
            LangFR.put("tbdAnaCompare", "Comparer les tables");
            LangFR.put("tbdAnaReport", "Faire un rapport");
            LangFR.put("tbdAnaOthers", "Autres");
            LangFR.put("tbdODFontsPB", "Police à problèmes");

            LangFR.put("tableLayer", "Couche");
            LangFR.put("tableEffects", "Effets");
            LangFR.put("tableShortNumber", "N°");
            LangFR.put("tableShortType", "T");
            LangFR.put("tableShortLayer", "C");
            LangFR.put("tableMargin", "Marg.");
            LangFR.put("tableStart", "Début");
            LangFR.put("tableEnd", "Fin");
            LangFR.put("tableTotal", "Total");
            LangFR.put("tableStyle", "Style");
            LangFR.put("tableName", "Nom");
            LangFR.put("tableEffect", "Effet");
            LangFR.put("tableText", "Texte");
            LangFR.put("tableFX", "FX");
            LangFR.put("tableSDType", "Type");
            LangFR.put("tableSDName", "Nom");
            LangFR.put("tableSDDescription", "Description");
            LangFR.put("tableSDAuthor", "Auteur");
            LangFR.put("tableSDSnippet", "Snippet");
            LangFR.put("tableImportStyles1", "-");
            LangFR.put("tableImportStyles2", "Style");
            LangFR.put("tableFont", "Police");
            LangFR.put("tableCorrection", "Correction %");
            LangFR.put("tableTKey", "Clé");
            LangFR.put("tableTValue", "Valeur");
            LangFR.put("tableParameters", "Paramètres");
            LangFR.put("tableSettings", "Configurations");

            LangFR.put("treeFxList", "Liste d'effets");
            LangFR.put("treeRubyScripts", "Scripts");
            LangFR.put("treeXMLPresets", "Effets XFX");
            LangFR.put("treeParticles", "Particules");
            LangFR.put("treeEffects", "Effets");
            LangFR.put("treeInitialization", "Initialisation");
            LangFR.put("treeAnimations", "Animations");

            LangFR.put("menuFile", "Fichier");
            LangFR.put("menuGoWel", "Aller à Bienvenue");
            LangFR.put("menuGoKara", "Aller à Karaoké");
            LangFR.put("menuGoCode", "Aller à Éditeur de code");
            LangFR.put("menuGoDraw", "Aller à Éditeur de dessin");
            LangFR.put("menuGoAna", "Aller à Analyse");
            LangFR.put("menuConf", "Configuration...");
            LangFR.put("menuQuit", "Quitter");
            LangFR.put("menuRess", "Ressources en ligne...");
            LangFR.put("menuAbout", "A propos de...");
            
            LangFR.put("popmCut", "Couper");
            LangFR.put("popmCopy", "Copier");
            LangFR.put("popmPaste", "Coller");
            LangFR.put("popmDelete", "Supprimer");
            LangFR.put("popmClear", "Effacer");
            LangFR.put("popmRemoveFX", "Supprimer les FX");
            LangFR.put("popmRfReset", "Rafraîchir la liste des scripts");
            LangFR.put("popmSelectAll", "Sélectionner tout");
            LangFR.put("popmColor", "Choisir une couleur...");
            LangFR.put("popmAlpha", "Choisir la transparence...");
            LangFR.put("popmInsOver", "Inserer des surcharges...");
            LangFR.put("popmInsCalc", "Inserer un calcul...");
            LangFR.put("popmInsFCalc", "Inserer un calcul à virgule...");
            LangFR.put("popmInsDraw", "Inserer un dessin...");
            LangFR.put("popmForAni", "Pour l'animation...");
            LangFR.put("popmForConf", "Pour la configuration...");
            LangFR.put("popmSurround", "Entourer d'accolades");
            LangFR.put("popmDelSur", "Supprimer les accolades");
            LangFR.put("popmStyImp", "Importer depuis le presse-papier");
            LangFR.put("popmStyExp", "Exporter vers le presse-papier");
            LangFR.put("popm_b", "\\b - Gras");
            LangFR.put("popm_i", "\\i - Italique");
            LangFR.put("popm_u", "\\u - Souligné");
            LangFR.put("popm_s", "\\s - Barré");
            LangFR.put("popm_bord", "\\bord - Épaisseur de la bordure");
            LangFR.put("popm_shad", "\\shad - Profondeur de l'ombre");
            LangFR.put("popm_be", "\\be - Bordure floue");
            LangFR.put("popm_blur", "\\blur - Flou");
            LangFR.put("popm_fs", "\\fs - Taille de la police");
            LangFR.put("popm_fscx", "\\fscx - Échelle de la police sur X");
            LangFR.put("popm_fscy", "\\fscy - Échelle de la police sur Y");
            LangFR.put("popm_fsp", "\\fsp - Espacement de la police");
            LangFR.put("popm_frx", "\\frx - Rotation de la police sur X");
            LangFR.put("popm_fry", "\\fry - Rotation de la police sur Y");
            LangFR.put("popm_frz", "\\frz - Rotation de la police sur Z");
            LangFR.put("popm_1c", "\\1c&H<hexa>& - Couleur du texte");
            LangFR.put("popm_2c", "\\2c&H<hexa>& - Couleur du karaoké");
            LangFR.put("popm_3c", "\\3c&H<hexa>& - Couleur de la bordure");
            LangFR.put("popm_4c", "\\4c&H<hexa>& - Couleur de l'ombre");
            LangFR.put("popm_alpha", "\\alpha&H<hexa>& - Transparence");
            LangFR.put("popm_1a", "\\1a&H<hexa>& - Transparence du texte");
            LangFR.put("popm_2a", "\\2a&H<hexa>& - Transparence du karaoké");
            LangFR.put("popm_3a", "\\3a&H<hexa>& - Transparence de la bordure");
            LangFR.put("popm_4a", "\\4a&H<hexa>& - Transparence de l'ombre");
            LangFR.put("popm_k", "\\k - Karaoké simple");
            LangFR.put("popm_kf", "\\kf - Karaoké avec remplissage");
            LangFR.put("popm_ko", "\\ko - Karaoké avec la bordure");
            LangFR.put("popm_t", "\\t - Animation");
            LangFR.put("popm_r", "\\r - Valeurs par défaut");
            LangFR.put("popm_fn", "\\fn - Nom de la police");
            LangFR.put("popm_fe", "\\fe - Encodage de la police");
            LangFR.put("popm_q", "\\q - Style d'emballage");
            LangFR.put("popm_a", "\\a - Alignement (ancien)");
            LangFR.put("popm_an", "\\an - Alignement");
            LangFR.put("popm_pos", "\\pos - Position");
            LangFR.put("popm_move", "\\move - Position en temps réel");
            LangFR.put("popm_org", "\\org - Origine");
            LangFR.put("popm_fad", "\\fad - Transition de transparence");
            LangFR.put("popm_fade", "\\fade - Transition de transparence");
            LangFR.put("popm_clip", "\\clip - Zone de visibilité");
            LangFR.put("popm_clip2", "\\clip - Zone de visibilité");
            LangFR.put("popm_xbord", "\\xbord - Épaisseur de la bordure sur X");
            LangFR.put("popm_ybord", "\\ybord - Épaisseur de la bordure sur Y");
            LangFR.put("popm_xshad", "\\xshad - Profondeur de l'ombre sur X");
            LangFR.put("popm_yshad", "\\yshad - Profondeur de l'ombre sur Y");
            LangFR.put("popm_fax", "\\fax - Distortion de perspective sur X");
            LangFR.put("popm_fay", "\\fay - Distortion de perspective sur Y");
            LangFR.put("popm_iclip", "\\iclip - Zone d'invisibilité");
            LangFR.put("popm_fsc", "\\fsc - Échelle de la police");
            LangFR.put("popm_fsvp", "\\fsvp - Début");
            LangFR.put("popm_frs", "\\frs - Obliquité de la ligne de base");
            LangFR.put("popm_z", "\\z - Coordonnées sur Z");
            LangFR.put("popm_distort", "\\distort - Distortion");
            LangFR.put("popm_md", "\\md - Déformation des limites");
            LangFR.put("popm_mdx", "\\mdx - Déformation des limites sur X");
            LangFR.put("popm_mdy", "\\mdy - Déformation des limites sur Y");
            LangFR.put("popm_mdz", "\\mdz - Déformation des limites sur Z");
            LangFR.put("popm_1vc", "\\1vc - Gradients sur texte (couleur)");
            LangFR.put("popm_2vc", "\\2vc - Gradients sur karaoké (couleur)");
            LangFR.put("popm_3vc", "\\3vc - Gradients sur bordure (couleur)");
            LangFR.put("popm_4vc", "\\4vc - Gradients sur ombre (couleur)");
            LangFR.put("popm_1va", "\\1va - Gradients sur texte (transparence)");
            LangFR.put("popm_2va", "\\2va - Gradients sur karaoké (transparence)");
            LangFR.put("popm_3va", "\\3va - Gradients sur bordure (transparence)");
            LangFR.put("popm_4va", "\\4va - Gradients sur ombre (transparence)");
            LangFR.put("popm_1img", "\\1img - Remplissage avec image sur texte");
            LangFR.put("popm_2img", "\\2img - Remplissage avec image sur karaoké");
            LangFR.put("popm_3img", "\\3img - Remplissage avec image sur bordure");
            LangFR.put("popm_4img", "\\4img - Remplissage avec image sur ombre");
            LangFR.put("popm_jitter", "\\jitter - Secousse");
            LangFR.put("popm_mover", "\\mover - Mouvement polaire");
            LangFR.put("popm_moves3", "\\moves3 - Mouvement sur spline");
            LangFR.put("popm_moves4", "\\moves4 - Mouvement sur spline");
            LangFR.put("popm_movevc", "\\movevc - Zone mouvante");
            LangFR.put("popSkeleton", "Inserer un squelette");
            LangFR.put("popFromCommands", "Mise à jour...");
            LangFR.put("popCodePNG", "Choisir l'image PNG...");
            LangFR.put("popCodeSnippet", "Inserer un snippet...");
            LangFR.put("popmImportFrom", "Importer à partir d'un script...");
            LangFR.put("popmGetSelLine", "Obtenir la ligne sélectionnée");
            LangFR.put("popmInsScript", "Script...");
            LangFR.put("popmCodeInit", "Initialiser le script");
            LangFR.put("popmCodeDef", "Insérer def");
            LangFR.put("popmRfInfo", "Avoir des informations sur cet objet");
            LangFR.put("popmXFXActive", "Active le paramètre");
            LangFR.put("popmXFXInactive", "Désactive le paramètre");

            LangFR.put("optpTitle1", "Erreur");
            LangFR.put("optpTitle2", "Confirmation");
            LangFR.put("optpTitle3", "Interrompu");
            LangFR.put("optpTitle4", "Fichier existant");
            LangFR.put("optpMessage1", "Désolé mais il n'y a pas de collection"
                    + " dans la liste des effets.");            
            LangFR.put("optpMessage2", "Voulez-vous vraiment supprimer cet objet ?");
            LangFR.put("optpMessage3", "Vous ne pouvez pas enlever le paquet Default.");
            LangFR.put("optpMessage4", "Voulez-vous vraiment supprimer ce paquet ?");
            LangFR.put("optpMessage5", "Un paquet utilise déjà le même nom.");
            LangFR.put("optpMessage6", "Voulez-vous vraiment écraser le fichier existant ?");
            LangFR.put("optpMessage7", "Le fichier a été crée ou mis à jour.");
            LangFR.put("optpMessage8", "Voulez-vous exécuter le script ?");
            LangFR.put("optpGetInfo1", "Nom : ");
            LangFR.put("optpGetInfo2", "Version : ");
            LangFR.put("optpGetInfo3", "Auteur(s) : ");
            LangFR.put("optpGetInfo4", "Chemin : ");
            LangFR.put("optpGetInfo5", "Fonction : ");
            LangFR.put("optpGetInfo6", "Description : ");
            LangFR.put("optpXFXInit", "Pas dans une balise d'animation !");
            LangFR.put("optpSDConfirmRequest", "Voulez-vous vraiment faire ça ?");
            LangFR.put("optpSDConfirmChoice", "Selectionner un choix");

            LangFR.put("toolQuit", "Fermer");
            LangFR.put("toolOpen", "Ouvrir");
            LangFR.put("toolNew", "Nouveau");
            LangFR.put("toolSave", "Sauvegarder");
            LangFR.put("toolSave1", "Sauvegarder l'original dans un fichier ASS.");
            LangFR.put("toolSave2", "Sauvegarder le résultat dans un fichier ASS.");
            LangFR.put("toolNormal", "<html>Normal : Voir le karaoké normal"
                    + " <br>(ex.: {\\k20}Wa{\\k10}ta{\\k10}shi {\\k20}no"
                    + " {\\k60}{\\k70}chi{\\k50}ka{\\k60}ra...)");
            LangFR.put("toolItems", "<html>Items : Voir le karaoké avec les"
                    + " items<br>(ex.: ◆Wa◆ta◆shi ◆no ◆◆chi◆ka◆ra...)");
            LangFR.put("toolStrip", "<html>Strip : Voir le karaoké"
                    + " sans aucun marquage.<br>(ex.: Watashi no chikara...)");
            LangFR.put("toolForOneLine", "<html>Executer les XML prédéfinis"
                    + " pour toutes les lignes sélectionnées en passant chaque"
                    + " ligne à la fonction qui applique les effets dans le"
                    + " panneau de résultat.<br>C'est le mode par défaut.<br>"
                    + "Veuillez noter que les fonctions (définis dans les"
                    + " filtres ou les scripts) peuvent supporter cette"
                    + " méthode ou pas du tout.");
            LangFR.put("toolForFewLines", "<html>Executer les XML prédéfinis"
                    + " pour toutes les lignes sélectionnées en passant toutes"
                    + " les lignes à la fonction qui applique les effets dans"
                    + " le panneau de résultat.<br>C'est une autre manière de"
                    + " faire votre karaoké. Obtenir toutes les lignes peut"
                    + " vous aider à créer des effets lourds.<br>Veuillez noter"
                    + " que les fonctions (définis dans les"
                    + " filtres ou les scripts) peuvent supporter cette"
                    + " méthode ou pas du tout.");
            LangFR.put("toolOptions", "Options");
            LangFR.put("toolLinks", "Aidez-moi avec des liens !");
            LangFR.put("toolAbout", "A propos de...");
            LangFR.put("toolAddFxoToLine", "Ajoute les effets sélectionnés aux"
                    + " lignes sélectionnée(s).");
            LangFR.put("toolAddXmlPresetFx", "Ajoute un nouveau effet XML-fx.");
            LangFR.put("toolModXmlPresetFx", "Modifie un effet XML-fx.");
            LangFR.put("toolDelXmlPresetFx", "Supprime un effet XML-fx.");
            LangFR.put("toolImpXmlPresetFx", "Importe de nouveaux effets XML-fx"
                    + " à partir d'une collection.");
            LangFR.put("toolExpXmlPresetFx", "Exporte des effets XML-fx à"
                    + " partir d'une collection.");
            LangFR.put("toolModRuby", "Ouvre votre éditeur ruby.");
            LangFR.put("toolHelpMe","<html>Aidez-moi :<br /><table>"
                    + "<tr><td>%sK</td><td>début du Karaoke</td><td>quand une"
                    + " syllabe est mise en valeur</td></tr><tr><td>%eK</td>"
                    + "<td>fin du Karaoke</td><td>quand une syllabe n'est"
                    + " plus mise en valeur</td></tr><tr><td>%dK</td>"
                    + "<td>durée du Karaoke</td><td></td></tr>"
                    + "<tr><td>%osK</td><td>début du Karaoke</td><td>comme"
                    + " l'original %sK (si mode par lettre)</td></tr>"
                    + "<tr><td>%oeK</td><td>fin du Karaoke</td><td>comme"
                    + " l'original %eK (si mode par lettre)</td></tr>"
                    + "<tr><td>%odK</td><td>durée du Karaoke</td><td>comme"
                    + " l'original %dK (si mode par lettre)</td></tr>"
                    + "<tr><td>%dP</td><td>durée de la phrase</td><td>"
                    + "</td></tr><tr><td>%posAF[]</td><td>position aléatoire"
                    + "</td><td></td></tr><tr><td>%num[]</td><td>nombre"
                    + " aléatoire</td><td></td></tr><tr><td>$X</td>"
                    + "<td>variable</td><td></td></tr>"
                    + "</table>");
            LangFR.put("toolDrawNew", "Efface la zone de dessin.");
            LangFR.put("toolDrawImage", "Charge une image.");
            LangFR.put("toolDrawTrans", "Transparence de l'image d'arrière-plan (si chargée).");
            LangFR.put("toolDrawOpen", "Ouvre un dessin.");
            LangFR.put("toolDrawSave", "Sauvegarde un dessin.");
            LangFR.put("toolDrawLine", "Dessine une ligne.");
            LangFR.put("toolDrawBezier", "Dessine un bézier.");
            LangFR.put("toolFromCommands", "Mise à jour à partir de la commande sélectionnée.");
            LangFR.put("toolStylesAddPack", "Ajouter un nouveau paquet.");
            LangFR.put("toolStylesDelPack", "Supprimer un paquet.");
            LangFR.put("toolStylesStoScr", "Copie le style sélectionné vers le script.");
            LangFR.put("toolStylesScrSto", "Copie le style sélectionné vers le paquet.");
            LangFR.put("toolStylesAddSto", "Ajoute un nouveau style au paquet.");
            LangFR.put("toolStylesAddScr", "Ajoute un nouveau style au script.");
            LangFR.put("toolStylesEditSto", "Edite le style sélectionné du paquet.");
            LangFR.put("toolStylesEditScr", "Edite le style sélectionné du script.");
            LangFR.put("toolStylesCopySto", "Copie le style sélectionné du paquet.");
            LangFR.put("toolStylesCopyScr", "Copie le style sélectionné du script.");
            LangFR.put("toolStylesRemSto", "Supprime tous les styles sélectionnés du paquet.");
            LangFR.put("toolStylesRemScr", "Supprime tous les styles sélectionnés du script.");
            LangFR.put("toolStylesEmbSto", "Copie le style sélectionné vers le paquet.");
            LangFR.put("toolStylesEmbScr", "Copie le style sélectionné vers le script.");
            LangFR.put("toolStyles", "Modifier les styles.");
            LangFR.put("toolAudioOpen", "Ouvrir un nouveau son...");
            LangFR.put("toolAudioPlay", "Joue le son à partir du début.");
            LangFR.put("toolAudioStop", "Arrête le son.");
            LangFR.put("toolAudioPlayArea", "Joue la partie délimitée.");
            LangFR.put("toolAudioPlayBB", "Joue avant le début de la partie délimitée.");            
            LangFR.put("toolAudioPlayAB", "Joue après le début de la partie délimitée.");
            LangFR.put("toolAudioPlayBE", "Joue avant la fin de la partie délimitée.");
            LangFR.put("toolAudioPlayAE", "Joue après la fin de la partie délimitée.");
            LangFR.put("toolAudioNewTime", "Obtient le temps de début et de fin.");
            LangFR.put("toolAudioSetKara", "Définit le karaoke.");
            LangFR.put("toolAudioGetKara", "Obtient le karaoke.");
            LangFR.put("toolAudioWavView", "Créer ou éditer le fichier ASS en utilisant un fichier wav.");
            LangFR.put("toolCodeDef", "Insérer une fonction pour une variable.");
            LangFR.put("toolCreateParticle", "Ajouter une nouvelle particule.");
            LangFR.put("toolEditParticle", "Modifier une particule.");
            LangFR.put("toolDeleteParticle", "Supprimer une particule.");
            LangFR.put("toolXFXAddInit", "Ajouter un effet dans Initialisation ou une animation dans Animations.");
            LangFR.put("toolXFXAddAnim", "Ajouter un effet dans Animation.");
            LangFR.put("toolXFXDelete", "Enlever.");
            LangFR.put("toolXFXChange", "Bascule de la configuration.");
            LangFR.put("toolTbRuby", "Definir le type de code en Ruby (JRuby).");
            LangFR.put("toolTbPython", "Definir le type de code en Python (Jython).");
            LangFR.put("toolBScriptOK", "Lancer le plugin sélectionné.");
            LangFR.put("toolAnalysis", "Analyse");
            
            LangFR.put("messTools", " Outils et ressources pour"
                    + " avoir plus d'expérience à propos des scripts : ");
            LangFR.put("messExport", "<html><h3>Sélectionnez la collection"
                    + " à exporter.");
            LangFR.put("messExportSave", "<html><h3>Sélectionnez l'endroit où"
                    + " le fichier doit être sauvegardé.");
            LangFR.put("messAbout", "<html><p align=\"center\"><b>Feuille</b> "
                    + "(Projet Funsub 2006-2014).<br />GNU/GPLv3 - Gratuit et ouvert pour tous."
                    + " <br />Feuille est créé pour penser différemment sur "
                    + "Windows, Linux et Mac OS X.<br />"
                    + "<i><b>Développé par The Wingate 2940.<br />"
                    + "Contact : assfxmaker@gmail.com</b></i>.</p>");
            LangFR.put("messCopyOf", "Copie de ");
            LangFR.put("messAssSketchpad", "<html>Ce logiciel sert à créer ou"
                    + " éditer des dessins au format ASS.<br />Il a été crée"
                    + " dans le but d'aider ceux qui veulent utiliser un outil"
                    + " sur Linux et Mac OS X.<br /><br/>Évolution : ZDrawing"
                    + " -> ZDrawingLite -> AssSketchpad. (Projet Funsub)<br />"
                    + " Ce logiciel est sous license GNU/GPLv3, libre et"
                    + " gratuit pour tous.");
            
            LangFR.put("taHelpPleaseXFX","%sK - Temps de début de la syllabe   ou   Temps de début de la lettre\n"
                    + "%eK - Temps de fin de la syllabe   ou   Temps de fin de la lettre\n"
                    + "%dK - Temps de durée de la syllabe   ou   Temps de durée de la lettre\n"
                    + "%osK - Temps de début de la syllabe\n"
                    + "%oeK - Temps de fin de la syllabe\n"
                    + "%odK - Temps de durée de la syllabe\n\n"
                    + "%dP - Temps de durée de la phrase\n"
                    + "%posAF[x1,y1,x2,y2] - Donne une position avec x et y aléatoire\n"
                    + "%num[a1,a2] - Donne un nombre aléatoire\n\n"
                    + "$motenminuscule - Retourne le résultat d’une fonction figurant dans l’onglet « Variables ».");
            LangFR.put("taHelpPleaseParticle","%XL - Position de X à gauche de la syllabe.\n"
                    + "%XC - Position de X au milieu de la syllabe.\n"
                    + "%XR - Position de X à droite de la syllabe.\n\n"
                    + "%XLF - Position de X à gauche de la première syllabe.\n"
                    + "%XCF - Position de X au milieu de la première syllabe.\n"
                    + "%XRF - Position de X à droite de la première syllabe.\n\n"
                    + "%XLL - Position de X à gauche de la dernière syllabe.\n"
                    + "%XCL - Position de X au milieu de la dernière syllabe.\n"
                    + "%XRL - Position de X à droite de la dernière syllabe.\n\n"
                    + "%Y - Position de Y défini dans la configuration.\n\n"
                    + "%sK - Temps de début de la syllabe   ou   Temps de début de la lettre\n"
                    + "%eK - Temps de fin de la syllabe   ou   Temps de fin de la lettre\n"
                    + "%dK - Temps de durée de la syllabe   ou   Temps de durée de la lettre\n"
                    + "%osK - Temps de début de la syllabe\n"
                    + "%oeK - Temps de fin de la syllabe\n"
                    + "%odK - Temps de durée de la syllabe\n\n"
                    + "%dP - Temps de durée de la phrase\n"
                    + "%posAF[x1,y1,x2,y2] - Donne une position avec x et y aléatoire\n"
                    + "%num[a1,a2] - Donne un nombre aléatoire\n\n"
                    + "$motenminuscule - Retourne le résultat d’une fonction figurant dans l’onglet « Variables ».");
            
            LangFR.put("textboxName", "Nom");
            LangFR.put("textboxAuthor", "Auteur");
            LangFR.put("textboxDesc", "Description");
            
            LangFR.put("enumODWelc", "Bienvenue");
            LangFR.put("enumODKara", "Karaoké");
            LangFR.put("enumODCode", "Éditeur de code");
            LangFR.put("enumODDraw", "Éditeur de dessin");
            LangFR.put("enumODAnal", "Analyse");
            
            LangFR.put("xfxparam_01", "Alignement");
            LangFR.put("xfxparam_02", "Début");
            LangFR.put("xfxparam_03", "Fin");
            LangFR.put("xfxparam_04", "Accélération");
            LangFR.put("xfxparam_05", "Valeur");
            LangFR.put("xfxparam_06", "Échelle");
            LangFR.put("xfxparam_07", "Dessins");
            LangFR.put("xfxparam_08", "Couleur");
            LangFR.put("xfxparam_09", "Temps du fondu d'ouverture");
            LangFR.put("xfxparam_10", "Temps du fondu de fermeture");
            LangFR.put("xfxparam_11", "Temps du début");
            LangFR.put("xfxparam_12", "Temps de la fin");
            LangFR.put("xfxparam_13", "Alpha 1");
            LangFR.put("xfxparam_14", "Alpha 2");
            LangFR.put("xfxparam_15", "Alpha 3");
            LangFR.put("xfxparam_16", "Encodage de la police");
            LangFR.put("xfxparam_17", "Nom de la police");
            LangFR.put("xfxparam_18", "Angle");
            LangFR.put("xfxparam_19", "Pourcentage");
            LangFR.put("xfxparam_20", "Taille");
            LangFR.put("xfxparam_21", "Transparence");
            LangFR.put("xfxparam_22", "Fichier");
            LangFR.put("xfxparam_23", "Offset sur X");
            LangFR.put("xfxparam_24", "Offset sur Y");
            LangFR.put("xfxparam_25", "Gauche");
            LangFR.put("xfxparam_26", "Droite");
            LangFR.put("xfxparam_27", "Haut");
            LangFR.put("xfxparam_28", "Bas");
            LangFR.put("xfxparam_29", "Période");
            LangFR.put("xfxparam_30", "Référence");
            LangFR.put("xfxparam_31", "Durée");
            LangFR.put("xfxparam_32", "Position sur X1 (début)");
            LangFR.put("xfxparam_33", "Position sur Y1 (début)");
            LangFR.put("xfxparam_34", "Position sur X2 (fin)");
            LangFR.put("xfxparam_35", "Position sur Y2 (fin)");
            LangFR.put("xfxparam_36", "Angle au début");
            LangFR.put("xfxparam_37", "Angle à la fin");
            LangFR.put("xfxparam_38", "Rayon au début");
            LangFR.put("xfxparam_39", "Rayon à la fin");
            LangFR.put("xfxparam_40", "Position sur X2 (milieu)");
            LangFR.put("xfxparam_41", "Position sur Y2 (milieu)");
            LangFR.put("xfxparam_42", "Position sur X3 (fin)");
            LangFR.put("xfxparam_43", "Position sur Y3 (fin)");
            LangFR.put("xfxparam_44", "Position sur X2 (2/4)");
            LangFR.put("xfxparam_45", "Position sur Y2 (2/4)");
            LangFR.put("xfxparam_46", "Position sur X3 (3/4)");
            LangFR.put("xfxparam_47", "Position sur Y3 (3/4)");
            LangFR.put("xfxparam_48", "Position sur X4 (fin)");
            LangFR.put("xfxparam_49", "Position sur Y4 (fin)");
            LangFR.put("xfxparam_50", "Origine sur X");
            LangFR.put("xfxparam_51", "Origine sur Y");
            LangFR.put("xfxparam_52", "Position sur X");
            LangFR.put("xfxparam_53", "Position sur Y");
            LangFR.put("xfxparam_54", "Style");
            LangFR.put("xfxparam_55", "Style d'emballage");
            
            LangFR.put("ifrOri", "Karaoké original");
            LangFR.put("ifrRes", "Karaoké résultant");
            LangFR.put("ifrSound", "Forme d'onde");
            LangFR.put("ifrTree", "Liste d'effets");
            LangFR.put("ifrCodeEditor", "Éditeur de code");            
            LangFR.put("assSketchpadFile", "Fichier");
            LangFR.put("assSketchpadToDraw", "Pour dessiner");
            LangFR.put("assSketchpadImage", "Image");
            LangFR.put("assSketchpadShape", "Forme et échelle");
            LangFR.put("assSketchpadScale", "Échelle");
            LangFR.put("assSketchpadOperations", "Opérations");
            LangFR.put("assSketchpadLayers", "Couches");
            LangFR.put("assSketchpadHistoric", "Historique");
            LangFR.put("assSketchpadSketchpad", "Bloc à dessin");
            LangFR.put("assSketchpadMode", "Mode");
            LangFR.put("assSketchpadScript", "Vos scripts");
            LangFR.put("assSketchpadAssComs", "Commandes ASS");
            LangFR.put("assSketchpadOrnament", "Ornement");
            LangFR.put("ifrFirstTable", "Ancien");
            LangFR.put("ifrSecondTable", "Nouveau");
            LangFR.put("ifrFirstReport", "Avant");
            LangFR.put("ifrSecondReport", "Après");
            LangFR.put("ifrWelcome", "Bienvenue");
            
            LangFR.put("assSketchpadAss_Commands", "Commandes ASS : ");
            LangFR.put("assSketchpadCut", "Couper");
            LangFR.put("assSketchpadCopy", "Copier");
            LangFR.put("assSketchpadPaste", "Coller");
            LangFR.put("assSketchpadDelete", "Supprimer");
            LangFR.put("assSketchpadUpdateFromComs", "Mettre à jour");
            LangFR.put("assSketchpadChoose_Color", "Choisir un couleur...");
            LangFR.put("assSketchpadRename_Layer", "Renommer la couche");
            LangFR.put("assSketchpadClear_Sketch", "Effacer le croquis");
            LangFR.put("assSketchpadRubber_Size", "Taille de la gomme");
            LangFR.put("assSketchpadPen_Size", "Taille du crayon");
            LangFR.put("assSketchpadBy_Value", "Par");
            LangFR.put("assSketchpadNew", "Crée un nouveau dessin");
            LangFR.put("assSketchpadOpen", "Ouverture d'un fichier");
            LangFR.put("assSketchpadSave", "Sauvegarder un fichier");
            LangFR.put("assSketchpadLine", "Dessiner des lignes droites");
            LangFR.put("assSketchpadCurve", "Dessiner des lignes courbes");
            LangFR.put("assSketchpadPen", "Tracer un croquis");
            LangFR.put("assSketchpadRubber", "Effacer un croquis");
            LangFR.put("assSketchpadClear_Image", "Effacer une image");
            LangFR.put("assSketchpadOpen_Image", "Ouvrir une image");
            LangFR.put("assSketchpadTop", "Déplacer une image vers le haut");
            LangFR.put("assSketchpadLeft", "Déplacer une image vers la gauche");
            LangFR.put("assSketchpadCenter", "Centrer une image");
            LangFR.put("assSketchpadRight", "Déplacer une image vers la droite");
            LangFR.put("assSketchpadTrans_Image", "Transparence de l'image");
            LangFR.put("assSketchpadBottom", "Déplacer une image vers le bas");
            LangFR.put("assSketchpadTrans_Shape", "Transparence de la forme");
            LangFR.put("assSketchpadScale_Size", "Taille de l'échelle");
            LangFR.put("assSketchpadTranslate", "Déplacer le dessin");
            LangFR.put("assSketchpadRotate", "Tourner le dessin");
            LangFR.put("assSketchpadAdd_Layer", "Ajouter une couche");
            LangFR.put("assSketchpadRemove_Layer", "Enlever une couche");
            LangFR.put("assSketchpadLayers_List", "Liste des calques de dessin");
            LangFR.put("assSketchpadUndo", "Efface un point de la couche en cours");
            LangFR.put("assSketchpadPen_Color", "Choisir la couleur du crayon...");
            LangFR.put("assSketchpadOK_Button", "OK");
            LangFR.put("assSketchpadCancel_Button", "Annuler");
            LangFR.put("assSketchpadTransX", "Translation sur X");
            LangFR.put("assSketchpadTransY", "Translation sur Y");
            LangFR.put("assSketchpadRedRotateMessage", "");
            LangFR.put("assSketchpadRotateMessage", "Angle de rotation");
            LangFR.put("assSketchpadBSpline", "Dessiner des BSplines");
            LangFR.put("assSketchpadBSplineClose", "Fermer la BSpline");
            LangFR.put("assSketchpadBSplineExtend", "Etend la BSpline vers le point");
            LangFR.put("assSketchpadGoToA", "Aller à (commande n)");
            LangFR.put("assSketchpadGoToB", "Aller à (commande m)");
            LangFR.put("assSketchpadDuplicate", "Dupliquer la couche");            
            LangFR.put("assSketchpadImpFonts", "Importer des formes venant de police de caractères");
            LangFR.put("assSketchpadNormalMode", "Mode normal");
            LangFR.put("assSketchpadOrnMode", "Mode colle");
            LangFR.put("assSketchpadResize", "Redimensionnement");
            LangFR.put("assSketchpadShear", "Transformation");
            LangFR.put("assSketchpadSelect", "Sélection");
            LangFR.put("assSketchpadReady", "Prêt !");
            LangFR.put("assSketchpadSteadyGo", "Généré !");
            LangFR.put("assSketchpadGMovement", "Mouvement général");
            LangFR.put("assSketchpadOrnYes", "Oui");
            LangFR.put("assSketchpadOrnNo", "Non");
            LangFR.put("assSketchpadFrequency", "Fréquence :");
            LangFR.put("assSketchpadOrnShape", "Forme");
            LangFR.put("assSketchpadOrnDuration", "Durée");            
            LangFR.put("assSketchpadSelCopyPaste", "Copier à la suite une fois");
            LangFR.put("assSketchpadSelSym", "Faire la symétrie");
            LangFR.put("assSketchpadSelGeo1A", "Tour forme 'Ligne' horaire");
            LangFR.put("assSketchpadSelGeo1B", "Tour forme 'Ligne' anti-horaire");
            LangFR.put("assSketchpadSelGeo2A", "Tour forme 'Triangle' horaire");
            LangFR.put("assSketchpadSelGeo2B", "Tour forme 'Triangle' anti-horaire");
            LangFR.put("assSketchpadSelGeo3A", "Tour forme 'Carré' horaire");
            LangFR.put("assSketchpadSelGeo3B", "Tour forme 'Carré' anti-horaire");
            LangFR.put("assSketchpadSelGeoPlus", "Tour autre forme...");
            
            
        }else{
            isDefault = false;
        }
    }
    
    /** <p>Return the first *.lang file found in the application folder.
     * Otherwise return <code>null</code>.<br />
     * Retourne le premier fichier *.lang trouvé dans le dossier de recherche. 
     * Sinon renvoit <code>null</code>.</p> */
    private String getLanguageFile(){
        //Search for application path
        String path = System.getProperty("user.dir");
        //System.out.println("user dir is : "+path);
        if(path.toLowerCase().contains("jre")){
            File f = new File(getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString()
                    .substring(6));
            path = f.getParent();
            System.out.println("by class is : "+path);
        }
        
        //Search for *.lang file in the application folder
        File folder = new File(path+File.separator);
        for(File sf : folder.listFiles()){
            if(sf.getName().endsWith(".lang")){
                return sf.getAbsolutePath();
            }
        }
        
        //If no file has been found then return null
        return null;
    }
    
    /** <p>Search for *.lang files.<br />Recherche les fichiers *.lang.</p> */
    public void searchForLangFile(String path){
        File folder = new File(path);
        for(File sf : folder.listFiles()){
            if(sf.getName().endsWith(".lang")){
                if(listFiles.contains(sf)==false){
                    listFiles.add(sf);
                }
            }
        }
    }
    
    /** <p>Set the language from a file.<br />
     * Définit le langage à partir d'un fichier.<br />
     * See/Voir : http://www.davros.org/misc/iso3166.txt and/et 
     * http://fr.wikipedia.org/wiki/ISO_3166-1 .</p> */
    public void setLanguageFromFile(){
        if(forceISO.equalsIgnoreCase("---")==false){
            ISO_3166 ISOcode = ISO_3166.Unknown;
            for (File sf : listFiles){
                String code = sf.getName().substring(0, sf.getName().indexOf("."));                
                ISOcode = ISOcode.getISO_3166(code);
                if(forceISO.equalsIgnoreCase(ISOcode.getAlpha3())){
                    loadLangXX2(sf);
                }
            }
        }else if(isDefault==false && forceISO.equalsIgnoreCase("---")){
            ISO_3166 ISOcode = ISO_3166.Unknown;
            for (File sf : listFiles){
                String code = sf.getName().substring(0, sf.getName().indexOf("."));                
                ISOcode = ISOcode.getISO_3166(code);
                if(loc.getISO3Country().equalsIgnoreCase(ISOcode.getAlpha3())){
                    loadLangXX2(sf);
                }
            }
        }else if(isDefault==true && forceISO.equalsIgnoreCase("---")){
            //Rsset local and don't load anything because it's already loaded.
            Language.loc = Locale.getDefault();
        }
    }
    
    private void loadLangXX(File f){
        try{
            FileInputStream fis = new FileInputStream(f);
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(fis, "UTF-8"));
            String newline;

            //Reading of file
            while((newline=br.readLine())!=null){
                String value = newline.substring(newline.indexOf(":")+1);
                if(newline.startsWith("titleABT")){LangXX.put("titleABT", value);}
                if(newline.startsWith("titleACD")){LangXX.put("titleACD", value);}
                if(newline.startsWith("titleFDL")){LangXX.put("titleFDL", value);}
                if(newline.startsWith("titleOHD")){LangXX.put("titleOHD", value);}
                if(newline.startsWith("titleOPD")){LangXX.put("titleOPD", value);}
                if(newline.startsWith("titleXPED")){LangXX.put("titleXPED", value);}
                if(newline.startsWith("titleXPD")){LangXX.put("titleXPD", value);}
                if(newline.startsWith("titleSD")){LangXX.put("titleSD", value);}
                if(newline.startsWith("titleASD1")){LangXX.put("titleASD1", value);}
                if(newline.startsWith("titleASD2")){LangXX.put("titleASD2", value);}
                if(newline.startsWith("titleASD3")){LangXX.put("titleASD3", value);}
                if(newline.startsWith("titleDCD")){LangXX.put("titleDCD", value);}
                if(newline.startsWith("titleXPD2")){LangXX.put("titleXPD2", value);}

                if(newline.startsWith("buttonOk")){LangXX.put("buttonOk", value);}
                if(newline.startsWith("buttonCancel")){LangXX.put("buttonCancel", value);}
                if(newline.startsWith("buttonAdd")){LangXX.put("buttonAdd", value);}
                if(newline.startsWith("buttonGet")){LangXX.put("buttonGet", value);}
                if(newline.startsWith("buttonChange")){LangXX.put("buttonChange", value);}
                if(newline.startsWith("buttonDelete")){LangXX.put("buttonDelete", value);}
                if(newline.startsWith("buttonModify")){LangXX.put("buttonModify", value);}
                if(newline.startsWith("buttonInMain")){LangXX.put("buttonInMain", value);}
                if(newline.startsWith("buttonOfScreen")){LangXX.put("buttonOfScreen", value);}
                if(newline.startsWith("buttonClose")){LangXX.put("buttonClose", value);}
                if(newline.startsWith("buttonAppTheme")){LangXX.put("buttonAppTheme", value);}
                if(newline.startsWith("buttonUpdate")){LangXX.put("buttonUpdate", value);}
                if(newline.startsWith("buttonImport")){LangXX.put("buttonImport", value);}
                if(newline.startsWith("buttonDirectory")){LangXX.put("buttonDirectory", value);}
                if(newline.startsWith("buttonEdit")){LangXX.put("buttonEdit", value);}
                if(newline.startsWith("buttonRemove")){LangXX.put("buttonRemove", value);}
                if(newline.startsWith("buttonImpScr")){LangXX.put("buttonImpScr", value);}
                if(newline.startsWith("buttonImpFil")){LangXX.put("buttonImpFil", value);}

                if(newline.startsWith("labelFxType")){LangXX.put("labelFxType", value);}
                if(newline.startsWith("labelName")){LangXX.put("labelName", value);}
                if(newline.startsWith("labelMoment")){LangXX.put("labelMoment", value);}
                if(newline.startsWith("labelFirstLayer")){LangXX.put("labelFirstLayer", value);}
                if(newline.startsWith("labelTime")){LangXX.put("labelTime", value);}
                if(newline.startsWith("labelPleaseHelpMe")){LangXX.put("labelPleaseHelpMe", value);}
                if(newline.startsWith("labelLayersDetails")){LangXX.put("labelLayersDetails", value);}
                if(newline.startsWith("labelOverrides")){LangXX.put("labelOverrides", value);}
                if(newline.startsWith("labelInnerOver")){LangXX.put("labelInnerOver", value);}
                if(newline.startsWith("labelLastOver")){LangXX.put("labelLastOver", value);}
                if(newline.startsWith("labelBeforeSyl")){LangXX.put("labelBeforeSyl", value);}
                if(newline.startsWith("labelAfterSyl")){LangXX.put("labelAfterSyl", value);}
                if(newline.startsWith("labelText")){LangXX.put("labelText", value);}
                if(newline.startsWith("labelKaraoke")){LangXX.put("labelKaraoke", value);}
                if(newline.startsWith("labelBorder")){LangXX.put("labelBorder", value);}
                if(newline.startsWith("labelShadow")){LangXX.put("labelShadow", value);}
                if(newline.startsWith("labelFontName")){LangXX.put("labelFontName", value);}
                if(newline.startsWith("labelFontSize")){LangXX.put("labelFontSize", value);}
                if(newline.startsWith("labelAlignment")){LangXX.put("labelAlignment", value);}
                if(newline.startsWith("labelMarginL")){LangXX.put("labelMarginL", value);}
                if(newline.startsWith("labelMarginR")){LangXX.put("labelMarginR", value);}
                if(newline.startsWith("labelMarginTV")){LangXX.put("labelMarginTV", value);}
                if(newline.startsWith("labelMarginB")){LangXX.put("labelMarginB", value);}
                if(newline.startsWith("labelScaleX")){LangXX.put("labelScaleX", value);}
                if(newline.startsWith("labelScaleY")){LangXX.put("labelScaleY", value);}
                if(newline.startsWith("labelRotation")){LangXX.put("labelRotation", value);}
                if(newline.startsWith("labelSpacing")){LangXX.put("labelSpacing", value);}
                if(newline.startsWith("labelEncoding")){LangXX.put("labelEncoding", value);}
                if(newline.startsWith("labelAuthor")){LangXX.put("labelAuthor", value);}
                if(newline.startsWith("labelComments")){LangXX.put("labelComments", value);}
                if(newline.startsWith("labelPreview")){LangXX.put("labelPreview", value);}
                if(newline.startsWith("labelCollection")){LangXX.put("labelCollection", value);}
                if(newline.startsWith("labelTransparency")){LangXX.put("labelTransparency", value);}
                if(newline.startsWith("labelRGBHTML")){LangXX.put("labelRGBHTML", value);}
                if(newline.startsWith("labelBGR")){LangXX.put("labelBGR", value);}
                if(newline.startsWith("labelUniFont")){LangXX.put("labelUniFont", value);}
                if(newline.startsWith("labelVideoSize")){LangXX.put("labelVideoSize", value);}
                if(newline.startsWith("labelRubyScriptPath")){LangXX.put("labelRubyScriptPath", value);}
                if(newline.startsWith("labelXMLPresetPath")){LangXX.put("labelXMLPresetPath", value);}
                if(newline.startsWith("labelFXPlugPath")){LangXX.put("labelFXPlugPath", value);}
                if(newline.startsWith("labelRubyEditorPath")){LangXX.put("labelRubyEditorPath", value);}
                if(newline.startsWith("labelDrawingEditorPath")){LangXX.put("labelDrawingEditorPath", value);}
                if(newline.startsWith("labelSnippet")){LangXX.put("labelSnippet", value);}
                if(newline.startsWith("labelStylesPack")){LangXX.put("labelStylesPack", value);}
                if(newline.startsWith("labelStylesScript")){LangXX.put("labelStylesScript", value);}
                if(newline.startsWith("labelStylesXFX")){LangXX.put("labelStylesXFX", value);}
                if(newline.startsWith("labelImportStyles")){LangXX.put("labelImportStyles", value);}
                if(newline.startsWith("labelUsedByAFM")){LangXX.put("labelUsedByAFM", value);}
                if(newline.startsWith("labelPBFont1")){LangXX.put("labelPBFont1", value);}
                if(newline.startsWith("labelPBFont2")){LangXX.put("labelPBFont2", value);}
                if(newline.startsWith("labelVideoSize")){LangXX.put("labelVideoSize", value);}
                if(newline.startsWith("labelPosY")){LangXX.put("labelPosY", value);}
                if(newline.startsWith("labelStyle")){LangXX.put("labelStyle", value);}

                if(newline.startsWith("rbuttonBefore")){LangXX.put("rbuttonBefore", value);}
                if(newline.startsWith("rbuttonMeantime")){LangXX.put("rbuttonMeantime", value);}
                if(newline.startsWith("rbuttonAfter")){LangXX.put("rbuttonAfter", value);}
                if(newline.startsWith("rbuttonModeNormal")){LangXX.put("rbuttonModeNormal", value);}
                if(newline.startsWith("rbuttonModePeriodic")){LangXX.put("rbuttonModePeriodic", value);}
                if(newline.startsWith("rbuttonModeRandom")){LangXX.put("rbuttonModeRandom", value);}
                if(newline.startsWith("rbuttonModeSymmetric")){LangXX.put("rbuttonModeSymmetric", value);}

                if(newline.startsWith("checkboxSaveFx")){LangXX.put("checkboxSaveFx", value);}
                if(newline.startsWith("checkboxBold")){LangXX.put("checkboxBold", value);}
                if(newline.startsWith("checkboxItalic")){LangXX.put("checkboxItalic", value);}
                if(newline.startsWith("checkboxUnderline")){LangXX.put("checkboxUnderline", value);}
                if(newline.startsWith("checkboxStrikeOut")){LangXX.put("checkboxStrikeOut", value);}
                if(newline.startsWith("checkboxOpaqueBox")){LangXX.put("checkboxOpaqueBox", value);}
                if(newline.startsWith("checkboxP1P0")){LangXX.put("checkboxP1P0", value);}
                if(newline.startsWith("checkboxChkUpdate")){LangXX.put("checkboxChkUpdate", value);}

                if(newline.startsWith("tabEffects")){LangXX.put("tabEffects", value);}
                if(newline.startsWith("tabEmbedStyles")){LangXX.put("tabEmbedStyles", value);}
                if(newline.startsWith("tabOptions")){LangXX.put("tabOptions", value);}
                if(newline.startsWith("tabPaths")){LangXX.put("tabPaths", value);}
                if(newline.startsWith("tabCustoms")){LangXX.put("tabCustoms", value);}
                if(newline.startsWith("tabOriginal")){LangXX.put("tabOriginal", value);}
                if(newline.startsWith("tabResult")){LangXX.put("tabResult", value);}
                if(newline.startsWith("tabRubyEdi")){LangXX.put("tabRubyEdi", value);}
                if(newline.startsWith("tabDrawing")){LangXX.put("tabDrawing", value);}
                if(newline.startsWith("tabPBFonts")){LangXX.put("tabPBFonts", value);}
                if(newline.startsWith("tabVariables")){LangXX.put("tabVariables", value);}
                if(newline.startsWith("tabSettings")){LangXX.put("tabSettings", value);}
                if(newline.startsWith("tabStyle")){LangXX.put("tabStyle", value);}

                if(newline.startsWith("tbdFxPaths")){LangXX.put("tbdFxPaths", value);}
                if(newline.startsWith("tbdOtherPaths")){LangXX.put("tbdOtherPaths", value);}
                if(newline.startsWith("tbdTheme")){LangXX.put("tbdTheme", value);}
                if(newline.startsWith("tbdFont")){LangXX.put("tbdFont", value);}
                if(newline.startsWith("tbdSize")){LangXX.put("tbdSize", value);}
                if(newline.startsWith("tbdStyle")){LangXX.put("tbdStyle", value);}
                if(newline.startsWith("tbdPreview")){LangXX.put("tbdPreview", value);}
                if(newline.startsWith("tbdParameters")){LangXX.put("tbdParameters", value);}
                if(newline.startsWith("tbdModes")){LangXX.put("tbdModes", value);}

                if(newline.startsWith("tableLayer")){LangXX.put("tableLayer", value);}
                if(newline.startsWith("tableEffects")){LangXX.put("tableEffects", value);}
                if(newline.startsWith("tableShortNumber")){LangXX.put("tableShortNumber", value);}
                if(newline.startsWith("tableShortType")){LangXX.put("tableShortType", value);}
                if(newline.startsWith("tableShortLayer")){LangXX.put("tableShortLayer", value);}
                if(newline.startsWith("tableMargin")){LangXX.put("tableMargin", value);}
                if(newline.startsWith("tableStart")){LangXX.put("tableStart", value);}
                if(newline.startsWith("tableEnd")){LangXX.put("tableEnd", value);}
                if(newline.startsWith("tableTotal")){LangXX.put("tableTotal", value);}
                if(newline.startsWith("tableStyle")){LangXX.put("tableStyle", value);}
                if(newline.startsWith("tableName")){LangXX.put("tableName", value);}
                if(newline.startsWith("tableEffect")){LangXX.put("tableEffect", value);}
                if(newline.startsWith("tableText")){LangXX.put("tableText", value);}
                if(newline.startsWith("tableSDName")){LangXX.put("tableSDName", value);}
                if(newline.startsWith("tableSDDescription")){LangXX.put("tableSDDescription", value);}
                if(newline.startsWith("tableSDAuthor")){LangXX.put("tableSDAuthor", value);}
                if(newline.startsWith("tableSDSnippet")){LangXX.put("tableSDSnippet", value);}
                if(newline.startsWith("tableImportStyles1")){LangXX.put("tableImportStyles1", value);}
                if(newline.startsWith("tableImportStyles2")){LangXX.put("tableImportStyles2", value);}
                if(newline.startsWith("tableFont")){LangXX.put("tableFont", value);}
                if(newline.startsWith("tableCorrection")){LangXX.put("tableCorrection", value);}

                if(newline.startsWith("treeFxList")){LangXX.put("treeFxList", value);}
                if(newline.startsWith("treeRubyScripts")){LangXX.put("treeRubyScripts", value);}
                if(newline.startsWith("treeXMLPresets")){LangXX.put("treeXMLPresets", value);}

                if(newline.startsWith("popmCut")){LangXX.put("popmCut", value);}
                if(newline.startsWith("popmCopy")){LangXX.put("popmCopy", value);}
                if(newline.startsWith("popmPaste")){LangXX.put("popmPaste", value);}
                if(newline.startsWith("popmDelete")){LangXX.put("popmDelete", value);}
                if(newline.startsWith("popmClear")){LangXX.put("popmClear", value);}
                if(newline.startsWith("popmRemoveFX")){LangXX.put("popmRemoveFX", value);}
                if(newline.startsWith("popmRfReset")){LangXX.put("popmRfReset", value);}
                if(newline.startsWith("popmSelectAll")){LangXX.put("popmSelectAll", value);}
                if(newline.startsWith("popmColor")){LangXX.put("popmColor", value);}
                if(newline.startsWith("popmAlpha")){LangXX.put("popmAlpha", value);}
                if(newline.startsWith("popmInsOver")){LangXX.put("popmInsOver", value);}
                if(newline.startsWith("popmInsCalc")){LangXX.put("popmInsCalc", value);}
                if(newline.startsWith("popmInsFCalc")){LangXX.put("popmInsFCalc", value);}
                if(newline.startsWith("popmInsDraw")){LangXX.put("popmInsDraw", value);}
                if(newline.startsWith("popmForAni")){LangXX.put("popmForAni", value);}
                if(newline.startsWith("popmForConf")){LangXX.put("popmForConf", value);}
                if(newline.startsWith("popmSurround")){LangXX.put("popmSurround", value);}
                if(newline.startsWith("popmDelSur")){LangXX.put("popmDelSur", value);}
                if(newline.startsWith("popmStyImp")){LangXX.put("popmStyImp", value);}
                if(newline.startsWith("popmStyExp")){LangXX.put("popmStyExp", value);}
                if(newline.startsWith("popm_b")){LangXX.put("popm_b", value);}
                if(newline.startsWith("popm_i")){LangXX.put("popm_i", value);}
                if(newline.startsWith("popm_u")){LangXX.put("popm_u", value);}
                if(newline.startsWith("popm_s")){LangXX.put("popm_s", value);}
                if(newline.startsWith("popm_bord")){LangXX.put("popm_bord", value);}
                if(newline.startsWith("popm_shad")){LangXX.put("popm_shad", value);}
                if(newline.startsWith("popm_be")){LangXX.put("popm_be", value);}
                if(newline.startsWith("popm_blur")){LangXX.put("popm_blur", value);}
                if(newline.startsWith("popm_fs")){LangXX.put("popm_fs", value);}
                if(newline.startsWith("popm_fscx")){LangXX.put("popm_fscx", value);}
                if(newline.startsWith("popm_fscy")){LangXX.put("popm_fscy", value);}
                if(newline.startsWith("popm_fsp")){LangXX.put("popm_fsp", value);}
                if(newline.startsWith("popm_frx")){LangXX.put("popm_frx", value);}
                if(newline.startsWith("popm_fry")){LangXX.put("popm_fry", value);}
                if(newline.startsWith("popm_frz")){LangXX.put("popm_frz", value);}
                if(newline.startsWith("popm_1c")){LangXX.put("popm_1c", value);}
                if(newline.startsWith("popm_2c")){LangXX.put("popm_2c", value);}
                if(newline.startsWith("popm_3c")){LangXX.put("popm_3c", value);}
                if(newline.startsWith("popm_4c")){LangXX.put("popm_4c", value);}
                if(newline.startsWith("popm_alpha")){LangXX.put("popm_alpha", value);}
                if(newline.startsWith("popm_1a")){LangXX.put("popm_1a", value);}
                if(newline.startsWith("popm_2a")){LangXX.put("popm_2a", value);}
                if(newline.startsWith("popm_3a")){LangXX.put("popm_3a", value);}
                if(newline.startsWith("popm_4a")){LangXX.put("popm_4a", value);}
                if(newline.startsWith("popm_k")){LangXX.put("popm_k", value);}
                if(newline.startsWith("popm_kf")){LangXX.put("popm_kf", value);}
                if(newline.startsWith("popm_ko")){LangXX.put("popm_ko", value);}
                if(newline.startsWith("popm_t")){LangXX.put("popm_t", value);}
                if(newline.startsWith("popm_r")){LangXX.put("popm_r", value);}
                if(newline.startsWith("popm_fn")){LangXX.put("popm_fn", value);}
                if(newline.startsWith("popm_fe")){LangXX.put("popm_fe", value);}
                if(newline.startsWith("popm_q")){LangXX.put("popm_q", value);}
                if(newline.startsWith("popm_a")){LangXX.put("popm_a", value);}
                if(newline.startsWith("popm_an")){LangXX.put("popm_an", value);}
                if(newline.startsWith("popm_pos")){LangXX.put("popm_pos", value);}
                if(newline.startsWith("popm_move")){LangXX.put("popm_move", value);}
                if(newline.startsWith("popm_org")){LangXX.put("popm_org", value);}
                if(newline.startsWith("popm_fad")){LangXX.put("popm_fad", value);}
                if(newline.startsWith("popm_fade")){LangXX.put("popm_fade", value);}
                if(newline.startsWith("popm_clip")){LangXX.put("popm_clip", value);}
                if(newline.startsWith("popm_clip2")){LangXX.put("popm_clip2", value);}
                if(newline.startsWith("popm_xbord")){LangXX.put("popm_xbord", value);}
                if(newline.startsWith("popm_ybord")){LangXX.put("popm_ybord", value);}
                if(newline.startsWith("popm_xshad")){LangXX.put("popm_xshad", value);}
                if(newline.startsWith("popm_yshad")){LangXX.put("popm_yshad", value);}
                if(newline.startsWith("popm_fax")){LangXX.put("popm_fax", value);}
                if(newline.startsWith("popm_fay")){LangXX.put("popm_fay", value);}
                if(newline.startsWith("popm_iclip")){LangXX.put("popm_iclip", value);}
                if(newline.startsWith("popm_fsc")){LangXX.put("popm_fsc", value);}
                if(newline.startsWith("popm_fsvp")){LangXX.put("popm_fsvp", value);}
                if(newline.startsWith("popm_frs")){LangXX.put("popm_frs", value);}
                if(newline.startsWith("popm_z")){LangXX.put("popm_z", value);}
                if(newline.startsWith("popm_distort")){LangXX.put("popm_distort", value);}
                if(newline.startsWith("popm_md")){LangXX.put("popm_md", value);}
                if(newline.startsWith("popm_mdx")){LangXX.put("popm_mdx", value);}
                if(newline.startsWith("popm_mdy")){LangXX.put("popm_mdy", value);}
                if(newline.startsWith("popm_mdz")){LangXX.put("popm_mdz", value);}
                if(newline.startsWith("popm_1vc")){LangXX.put("popm_1vc", value);}
                if(newline.startsWith("popm_2vc")){LangXX.put("popm_2vc", value);}
                if(newline.startsWith("popm_3vc")){LangXX.put("popm_3vc", value);}
                if(newline.startsWith("popm_4vc")){LangXX.put("popm_4vc", value);}
                if(newline.startsWith("popm_1va")){LangXX.put("popm_1va", value);}
                if(newline.startsWith("popm_2va")){LangXX.put("popm_2va", value);}
                if(newline.startsWith("popm_3va")){LangXX.put("popm_3va", value);}
                if(newline.startsWith("popm_4va")){LangXX.put("popm_4va", value);}
                if(newline.startsWith("popm_1img")){LangXX.put("popm_1img", value);}
                if(newline.startsWith("popm_2img")){LangXX.put("popm_2img", value);}
                if(newline.startsWith("popm_3img")){LangXX.put("popm_3img", value);}
                if(newline.startsWith("popm_4img")){LangXX.put("popm_4img", value);}
                if(newline.startsWith("popm_jitter")){LangXX.put("popm_jitter", value);}
                if(newline.startsWith("popm_mover")){LangXX.put("popm_mover", value);}
                if(newline.startsWith("popm_moves3")){LangXX.put("popm_moves3", value);}
                if(newline.startsWith("popm_moves4")){LangXX.put("popm_moves4", value);}
                if(newline.startsWith("popm_movevc")){LangXX.put("popm_movevc", value);}
                if(newline.startsWith("popSkeleton")){LangXX.put("popSkeleton", value);}
                if(newline.startsWith("popFromCommands")){LangXX.put("popFromCommands", value);}
                if(newline.startsWith("popCodePNG")){LangXX.put("popCodePNG", value);}
                if(newline.startsWith("popCodeSnippet")){LangXX.put("popCodeSnippet", value);}
                if(newline.startsWith("popmImportFrom")){LangXX.put("popmImportFrom", value);}
                if(newline.startsWith("popmGetSelLine")){LangXX.put("popmGetSelLine", value);}
                if(newline.startsWith("popmInsScript")){LangXX.put("popmInsScript", value);}
                if(newline.startsWith("popmCodeInit")){LangXX.put("popmCodeInit", value);}
                if(newline.startsWith("popmCodeDef")){LangXX.put("popmCodeDef", value);}

                if(newline.startsWith("optpTitle1")){LangXX.put("optpTitle1", value);}
                if(newline.startsWith("optpTitle2")){LangXX.put("optpTitle2", value);}
                if(newline.startsWith("optpTitle3")){LangXX.put("optpTitle3", value);}
                if(newline.startsWith("optpMessage1")){LangXX.put("optpMessage1", value);}
                if(newline.startsWith("optpMessage2")){LangXX.put("optpMessage2", value);}
                if(newline.startsWith("optpMessage3")){LangXX.put("optpMessage3", value);}
                if(newline.startsWith("optpMessage4")){LangXX.put("optpMessage4", value);}
                if(newline.startsWith("optpMessage5")){LangXX.put("optpMessage5", value);}

                if(newline.startsWith("toolQuit")){LangXX.put("toolQuit", value);}
                if(newline.startsWith("toolOpen")){LangXX.put("toolOpen", value);}
                if(newline.startsWith("toolNew")){LangXX.put("toolNew", value);}
                if(newline.startsWith("toolSave")){LangXX.put("toolSave", value);}
                if(newline.startsWith("toolSave1")){LangXX.put("toolSave1", value);}
                if(newline.startsWith("toolSave2")){LangXX.put("toolSave2", value);}
                if(newline.startsWith("toolNormal")){LangXX.put("toolNormal", value);}
                if(newline.startsWith("toolItems")){LangXX.put("toolItems", value);}
                if(newline.startsWith("toolStrip")){LangXX.put("toolStrip", value);}
                if(newline.startsWith("toolForOneLine")){LangXX.put("toolForOneLine", value);}
                if(newline.startsWith("toolForFewLines")){LangXX.put("toolForFewLines", value);}
                if(newline.startsWith("toolOptions")){LangXX.put("toolOptions", value);}
                if(newline.startsWith("toolLinks")){LangXX.put("toolLinks", value);}
                if(newline.startsWith("toolAbout")){LangXX.put("toolAbout", value);}
                if(newline.startsWith("toolAddFxoToLine")){LangXX.put("toolAddFxoToLine", value);}
                if(newline.startsWith("toolAddXmlPresetFx")){LangXX.put("toolAddXmlPresetFx", value);}
                if(newline.startsWith("toolModXmlPresetFx")){LangXX.put("toolModXmlPresetFx", value);}
                if(newline.startsWith("toolDelXmlPresetFx")){LangXX.put("toolDelXmlPresetFx", value);}
                if(newline.startsWith("toolImpXmlPresetFx")){LangXX.put("toolImpXmlPresetFx", value);}
                if(newline.startsWith("toolExpXmlPresetFx")){LangXX.put("toolExpXmlPresetFx", value);}
                if(newline.startsWith("toolModRuby")){LangXX.put("toolModRuby", value);}
                if(newline.startsWith("toolHelpMe")){LangXX.put("toolHelpMe", value);}
                if(newline.startsWith("toolDrawNew")){LangXX.put("toolDrawNew", value);}
                if(newline.startsWith("toolDrawImage")){LangXX.put("toolDrawImage", value);}
                if(newline.startsWith("toolDrawTrans")){LangXX.put("toolDrawTrans", value);}
                if(newline.startsWith("toolDrawOpen")){LangXX.put("toolDrawOpen", value);}
                if(newline.startsWith("toolDrawSave")){LangXX.put("toolDrawSave", value);}
                if(newline.startsWith("toolDrawLine")){LangXX.put("toolDrawLine", value);}
                if(newline.startsWith("toolDrawBezier")){LangXX.put("toolDrawBezier", value);}
                if(newline.startsWith("toolFromCommands")){LangXX.put("toolFromCommands", value);}
                if(newline.startsWith("toolStylesAddPack")){LangXX.put("toolStylesAddPack", value);}
                if(newline.startsWith("toolStylesDelPack")){LangXX.put("toolStylesDelPack", value);}
                if(newline.startsWith("toolStylesStoScr")){LangXX.put("toolStylesStoScr", value);}
                if(newline.startsWith("toolStylesScrSto")){LangXX.put("toolStylesScrSto", value);}
                if(newline.startsWith("toolStylesAddSto")){LangXX.put("toolStylesAddSto", value);}
                if(newline.startsWith("toolStylesAddScr")){LangXX.put("toolStylesAddScr", value);}
                if(newline.startsWith("toolStylesEditSto")){LangXX.put("toolStylesEditSto", value);}
                if(newline.startsWith("toolStylesEditScr")){LangXX.put("toolStylesEditScr", value);}
                if(newline.startsWith("toolStylesCopySto")){LangXX.put("toolStylesCopySto", value);}
                if(newline.startsWith("toolStylesCopyScr")){LangXX.put("toolStylesCopyScr", value);}
                if(newline.startsWith("toolStylesRemSto")){LangXX.put("toolStylesRemSto", value);}
                if(newline.startsWith("toolStylesRemScr")){LangXX.put("toolStylesRemScr", value);}
                if(newline.startsWith("toolStylesEmbSto")){LangXX.put("toolStylesEmbSto", value);}
                if(newline.startsWith("toolStylesEmbScr")){LangXX.put("toolStylesEmbScr", value);}
                if(newline.startsWith("toolStyles")){LangXX.put("toolStyles", value);}                            
                if(newline.startsWith("toolAudioOpen")){LangXX.put("toolAudioOpen", value);}
                if(newline.startsWith("toolAudioPlay")){LangXX.put("toolAudioPlay", value);}
                if(newline.startsWith("toolAudioStop")){LangXX.put("toolAudioStop", value);}
                if(newline.startsWith("toolAudioPlayArea")){LangXX.put("toolAudioPlayArea", value);}
                if(newline.startsWith("toolAudioPlayBB")){LangXX.put("toolAudioPlayBB", value);}
                if(newline.startsWith("toolAudioPlayAB")){LangXX.put("toolAudioPlayAB", value);}
                if(newline.startsWith("toolAudioPlayBE")){LangXX.put("toolAudioPlayBE", value);}
                if(newline.startsWith("toolAudioPlayAE")){LangXX.put("toolAudioPlayAE", value);}
                if(newline.startsWith("toolAudioNewTime")){LangXX.put("toolAudioNewTime", value);}
                if(newline.startsWith("toolAudioSetKara")){LangXX.put("toolAudioSetKara", value);}
                if(newline.startsWith("toolAudioGetKara")){LangXX.put("toolAudioGetKara", value);}
                if(newline.startsWith("toolAudioWavView")){LangXX.put("toolAudioWavView", value);}
                if(newline.startsWith("toolCodeDef")){LangXX.put("toolCodeDef", value);}

                if(newline.startsWith("messTools")){LangXX.put("messTools", value);}
                if(newline.startsWith("messExport")){LangXX.put("messExport", value);}
                if(newline.startsWith("messExportSave")){LangXX.put("messExportSave", value);}
                if(newline.startsWith("messAbout")){LangXX.put("messAbout", value);}
                if(newline.startsWith("messCopyOf")){LangXX.put("messCopyOf", value);}

                if(newline.startsWith("assSketchpadFile")){LangXX.put("assSketchpadFile", value);}
                if(newline.startsWith("assSketchpadToDraw")){LangXX.put("assSketchpadToDraw", value);}
                if(newline.startsWith("assSketchpadImage")){LangXX.put("assSketchpadImage", value);}
                if(newline.startsWith("assSketchpadShape")){LangXX.put("assSketchpadShape", value);}
                if(newline.startsWith("assSketchpadScale")){LangXX.put("assSketchpadScale", value);}
                if(newline.startsWith("assSketchpadOperations")){LangXX.put("assSketchpadOperations", value);}
                if(newline.startsWith("assSketchpadLayers")){LangXX.put("assSketchpadLayers", value);}
                if(newline.startsWith("assSketchpadHistoric")){LangXX.put("assSketchpadHistoric", value);}
                if(newline.startsWith("assSketchpadSketchpad")){LangXX.put("assSketchpadSketchpad", value);}
                if(newline.startsWith("assSketchpadAss_Commands")){LangXX.put("assSketchpadAss_Commands", value);}
                if(newline.startsWith("assSketchpadCut")){LangXX.put("assSketchpadCut", value);}
                if(newline.startsWith("assSketchpadCopy")){LangXX.put("assSketchpadCopy", value);}
                if(newline.startsWith("assSketchpadPaste")){LangXX.put("assSketchpadPaste", value);}
                if(newline.startsWith("assSketchpadDelete")){LangXX.put("assSketchpadDelete", value);}
                if(newline.startsWith("assSketchpadUpdateFromComs")){LangXX.put("assSketchpadUpdateFromComs", value);}
                if(newline.startsWith("assSketchpadChoose_Color")){LangXX.put("assSketchpadChoose_Color", value);}
                if(newline.startsWith("assSketchpadRename_Layer")){LangXX.put("assSketchpadRename_Layer", value);}
                if(newline.startsWith("assSketchpadClear_Sketch")){LangXX.put("assSketchpadClear_Sketch", value);}
                if(newline.startsWith("assSketchpadRubber_Size")){LangXX.put("assSketchpadRubber_Size", value);}
                if(newline.startsWith("assSketchpadPen_Size")){LangXX.put("assSketchpadPen_Size", value);}
                if(newline.startsWith("assSketchpadBy_Value")){LangXX.put("assSketchpadBy_Value", value);}
                if(newline.startsWith("assSketchpadNew")){LangXX.put("assSketchpadNew", value);}
                if(newline.startsWith("assSketchpadOpen")){LangXX.put("assSketchpadOpen", value);}
                if(newline.startsWith("assSketchpadSave")){LangXX.put("assSketchpadSave", value);}
                if(newline.startsWith("assSketchpadLine")){LangXX.put("assSketchpadLine", value);}
                if(newline.startsWith("assSketchpadCurve")){LangXX.put("assSketchpadCurve", value);}
                if(newline.startsWith("assSketchpadPen")){LangXX.put("assSketchpadPen", value);}
                if(newline.startsWith("assSketchpadRubber")){LangXX.put("assSketchpadRubber", value);}
                if(newline.startsWith("assSketchpadClear_Image")){LangXX.put("assSketchpadClear_Image", value);}
                if(newline.startsWith("assSketchpadOpen_Image")){LangXX.put("assSketchpadOpen_Image", value);}
                if(newline.startsWith("assSketchpadTop")){LangXX.put("assSketchpadTop", value);}
                if(newline.startsWith("assSketchpadLeft")){LangXX.put("assSketchpadLeft", value);}
                if(newline.startsWith("assSketchpadCenter")){LangXX.put("assSketchpadCenter", value);}
                if(newline.startsWith("assSketchpadRight")){LangXX.put("assSketchpadRight", value);}
                if(newline.startsWith("assSketchpadTrans_Image")){LangXX.put("assSketchpadTrans_Image", value);}
                if(newline.startsWith("assSketchpadBottom")){LangXX.put("assSketchpadBottom", value);}
                if(newline.startsWith("assSketchpadTrans_Shape")){LangXX.put("assSketchpadTrans_Shape", value);}
                if(newline.startsWith("assSketchpadScale_Size")){LangXX.put("assSketchpadScale_Size", value);}
                if(newline.startsWith("assSketchpadTranslate")){LangXX.put("assSketchpadTranslate", value);}
                if(newline.startsWith("assSketchpadRotate")){LangXX.put("assSketchpadRotate", value);}
                if(newline.startsWith("assSketchpadAdd_Layer")){LangXX.put("assSketchpadAdd_Layer", value);}
                if(newline.startsWith("assSketchpadRemove_Layer")){LangXX.put("assSketchpadRemove_Layer", value);}
                if(newline.startsWith("assSketchpadLayers_List")){LangXX.put("assSketchpadLayers_List", value);}
                if(newline.startsWith("assSketchpadUndo")){LangXX.put("assSketchpadUndo", value);}
                if(newline.startsWith("assSketchpadPen_Color")){LangXX.put("assSketchpadPen_Color", value);}
                if(newline.startsWith("assSketchpadOK_Button")){LangXX.put("assSketchpadOK_Button", value);}
                if(newline.startsWith("assSketchpadCancel_Button")){LangXX.put("assSketchpadCancel_Button", value);}
                if(newline.startsWith("assSketchpadTransX")){LangXX.put("assSketchpadTransX", value);}
                if(newline.startsWith("assSketchpadTransY")){LangXX.put("assSketchpadTransY", value);}
                if(newline.startsWith("assSketchpadRedRotateMessage")){LangXX.put("assSketchpadRedRotateMessage", value);}
                if(newline.startsWith("assSketchpadRotateMessage")){LangXX.put("assSketchpadRotateMessage", value);}                
                if(newline.startsWith("assSketchpadBSpline")){LangXX.put("assSketchpadBSpline", value);}
                if(newline.startsWith("assSketchpadBSplineClose")){LangXX.put("assSketchpadBSplineClose", value);}
                if(newline.startsWith("assSketchpadBSplineExtend")){LangXX.put("assSketchpadBSplineExtend", value);}
                if(newline.startsWith("assSketchpadGoToA")){LangXX.put("assSketchpadGoToA", value);}
                if(newline.startsWith("assSketchpadGoToB")){LangXX.put("assSketchpadGoToB", value);}

            }
            br.close(); fis.close();
        }catch(Exception exc){
            //Nothing
        }
    }
    
    private void loadLangXX2(File f){
        try {
            XmlLangHandler xlh = new XmlLangHandler(f.getAbsolutePath());
            LangXX = xlh.getLangMap();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            //Nothing
        }
    }
    
    /** <p>Return the language for this locale or english.<br />
     * Retourne le langage pour cette locale ou l'anglais.</p> */
    public Map<String,String> getLocaleMap(){
        if(LangFR.isEmpty()==false){
            return LangFR;
        }else if(LangUS.isEmpty()==false){
            return LangUS;
        }else if(LangXX.isEmpty()==false){
            return LangXX;
        }else{
            init(Locale.US);
            return LangUS;
        }        
    }
    
    /** <p>Return the language in english.<br />
     * Retourne le langage en anglais.</p> */
    public Map<String,String> getLocaleUSMap(){
        if(LangUS.isEmpty()==false){
            return LangUS;
        }else{
            init(Locale.US);
            return LangUS;
        }        
    }
    
    /** <p>Reload the language and try to load the locale again.<br />
     * Recharge la langue et essaie de charger la locale encore une fois.</p> */
    public void reload(String code){
        forceISO = code;
        searchForLangFile(searchPath);
        setLanguageFromFile();
    }
    
    public static ISO_3166 getDefaultISO_3166(){
        ISO_3166 ISOcode = ISO_3166.Unknown;
        String code = loc.getISO3Country();
        ISOcode = ISOcode.getISO_3166(code);
        return ISOcode;
    }
    
    public static ISO_3166 getFromCode(String code){
        ISO_3166 ISOcode = ISO_3166.Unknown;
        ISOcode = ISOcode.getISO_3166(code);
        return ISOcode;
    }
    
    public Locale changeFromCode(String code){        
        if(code.isEmpty() | code.equalsIgnoreCase("---")){
            return null;
        }
        for(Locale l : Locale.getAvailableLocales()){
//            System.out.println(code+" ? -> "+ l.getISO3Country());
            if(l.getISO3Country().equalsIgnoreCase(code)){                
                return  l;
            }
        }
        return null;
    }
}
