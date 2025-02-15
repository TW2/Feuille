package org.wingate.feuille.lib.table;

import org.wingate.feuille.subs.ass.AssEvent;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Version {

    public enum Type {
        Unknown(Load.language("vs_Unknown", "Unknown", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country())), Load.language("vs_Unknown", "Unknown")),
        Translation(Load.language("vs_Translation", "Translation", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country())), Load.language("vs_Translation", "Translation")),
        Revision(Load.language("vs_Revision", "Revision", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country())), Load.language("vs_Revision", "Revision")),
        Original(Load.language("vs_Original", "Original", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country())), Load.language("vs_Original", "Original")),
        FreeStyle(Load.language("vs_FreeStyle", "Free style", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country())), Load.language("vs_FreeStyle", "Free style"));

        final String text;
        final String universalText;

        Type(String text, String universalText){
            this.text = text;
            this.universalText = universalText;
        }

        public String getText() {
            return text;
        }

        public String getUniversalText() {
            return universalText;
        }

        public static Type get(String universal){
            Type type = Unknown;

            for(Type t : values()){
                if(t.getUniversalText().equalsIgnoreCase(universal)){
                    type = t;
                    break;
                }
            }

            return type;
        }
    }

    private List<AssEvent> events;
    private ISO_3166 language;
    private String name;
    private String date; // 0000-00-00-00h00m00s000ms-Day
    private int number; // vNumber
    private String company;
    private String author;
    private String details;
    private Type type;

    public Version() {
        events = new ArrayList<>();
        language = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        name = "Default";
        date = "NotDefined";
        number = 0;
        company = "UnknownCompany";
        author = "UnknownAuthor";
        details = "None";
        type = Type.Unknown;
    }

    public Version(List<AssEvent> events, ISO_3166 language, String name, String date, int number, String company, String author, String details, Type type) {
        this.events = events;
        this.language = language;
        this.name = name;
        this.date = date;
        this.number = number;
        this.company = company;
        this.author = author;
        this.details = details;
        this.type = type;
    }

    public List<AssEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AssEvent> events) {
        this.events = events;
    }

    public ISO_3166 getLanguage() {
        return language;
    }

    public void setLanguage(ISO_3166 language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
