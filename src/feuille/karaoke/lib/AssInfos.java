/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.lib;

/**
 * <p>This class is a storage for the script informations.<br />
 * Cette classe sert de sauvegarde des informations du script.</p>
 * @author The Wingate 2940
 */
public class AssInfos {
    
    /** <p>Create a basic script.<br />
     * Cr&#233;e un script basique.</p> */
    public AssInfos(){
        
    }
    
    /** <p>Create a basic script by setting the resolution X and Y.<br />
     * Cr&#233;e un script basique en red&#233;finissant
     * la r&#233;solution X et la r&#233;solution Y.</p> */
    public AssInfos(String resX, String resY){
        ScriptPlayResX = resX;
        ScriptPlayResY = resY;
    }
    
    /* Voici les variables */
    
    /*
     * Seront sauvegard&#233;s les infos du fichier original.
     * Seront modifi&#233;s lors de l'enregistrement.
     */
    private String ScriptPathName = "";
    private String ScriptSoftware = "Make with KaraModeFunsub/Funsub Project";
    private String ScriptWeblinks = "";
    private String ScriptTitle = "";
    private String ScriptAuthors = "";
    private String ScriptTranslators = "";
    private String ScriptEditors = "";
    private String ScriptTimers = "";
    private String ScriptCheckers = "";
    private String ScriptSynchPoint = "";
    private String ScriptUpdateBy = "";
    private String ScriptUpdates = "";
    private String ScriptCollisions = "";
    private String ScriptPlayResX = "640";
    private String ScriptPlayResY = "480";
    private String ScriptPlayDepth = "";
    private String ScriptAudios = "";
    private String ScriptVideo = "";
    private String ScriptTimerSpeed = "100.0000";
    private String ScriptWrapStyle = "";
    private String ScriptType = "";
    private String KaraokeMakers = "";
    
    /* Voici l'&#233;numu&#233;ration pour acc&#233;der aux variables */
    
    /** <p>A choice of information type.<br />
     * Un choix de type d'informations.</p> */
    public enum AssInfosType{
        pathname, software, weblinks, title, authors, translators,
        editors, timers, checkers, synchpoint, updateby, updates,
        collisions, playresx, playresy, playdepth, timerspeed,
        wrapstyle, audios, video, scripttype, karaokemakers;
    }
    
    /* Voici les m&#233;thodes */
    
    /** <p>Save the elements.<br />
     * Sauvegarde les éléments.</p> */
    public void setElement(AssInfosType ait, String value){
        switch(ait){
            case pathname: ScriptPathName = value; break;
            case software: ScriptSoftware = value; break;
            case weblinks: ScriptWeblinks = value; break;
            case title: ScriptTitle = value; break;
            case authors: ScriptAuthors = value; break;
            case translators: ScriptTranslators = value; break;
            case editors: ScriptEditors = value; break;
            case timers: ScriptTimers = value; break;
            case checkers: ScriptCheckers = value; break;
            case synchpoint: ScriptSynchPoint = value; break;
            case updateby: ScriptUpdateBy = value; break;
            case updates: ScriptUpdates = value; break;
            case collisions: ScriptCollisions = value; break;
            case playresx: ScriptPlayResX = value; break;
            case playresy: ScriptPlayResY = value; break;
            case playdepth: ScriptPlayDepth = value; break;
            case timerspeed: ScriptTimerSpeed = value; break;
            case wrapstyle: ScriptWrapStyle = value; break;
            case audios: ScriptAudios = value; break;
            case video: ScriptVideo = value; break;
            case scripttype: ScriptType = value; break;
            case karaokemakers: KaraokeMakers = value; break;
        }
    }
    
    /** <p>Return the elements.<br />
     * Retorne les éléments.</p> */
    public String getElement(AssInfosType ait){
        switch(ait){
            case pathname: return ScriptPathName;
            case software: return ScriptSoftware;
            case weblinks: return ScriptWeblinks;
            case title: return ScriptTitle;
            case authors: return ScriptAuthors;
            case translators: return ScriptTranslators;
            case editors: return ScriptEditors;
            case timers: return ScriptTimers;
            case checkers: return ScriptCheckers;
            case synchpoint: return ScriptSynchPoint;
            case updateby: return ScriptUpdateBy;
            case updates: return ScriptUpdates;
            case collisions: return ScriptCollisions;
            case playresx: return ScriptPlayResX;
            case playresy: return ScriptPlayResY;
            case playdepth: return ScriptPlayDepth;
            case timerspeed: return ScriptTimerSpeed;
            case wrapstyle: return ScriptWrapStyle;
            case audios: return ScriptAudios;
            case video: return ScriptVideo;
            case scripttype: return ScriptType;
            case karaokemakers: return KaraokeMakers;
            default: return "";
        }
    }
}
