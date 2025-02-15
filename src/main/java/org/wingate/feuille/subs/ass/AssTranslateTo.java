package org.wingate.feuille.subs.ass;


import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;

import java.util.*;

public class AssTranslateTo {
    private final List<Version> versions;

    public AssTranslateTo(){
        this.versions = new ArrayList<>();
    }

    public AssTranslateTo(List<Version> versions){
        this.versions = versions;
    }

    public static AssTranslateTo createFirst(ISO_3166 src){
        AssTranslateTo tr = new AssTranslateTo(new ArrayList<>());
        tr.versions.add(Version.createFirstVersion(src));
        return tr;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public Version getVersion(ISO_3166 iso) {
        Version version = null;

        for(Version v : getVersions()){
            if(v.getIso().getCountry().equalsIgnoreCase(iso.getCountry())){
                version = v;
                break;
            }
        }

        return version;
    }

    public Version getLastVersion(ISO_3166 iso){
        Version version = null;

        for(Version v : getVersions()){
            if(v.getIso().getCountry().equalsIgnoreCase(iso.getCountry())){
                version = v;
            }
        }

        return version;
    }

    public static class Tag {
        private int number;
        private String tagText;

        private int dayOfWeek;
        private int dayInMonth;
        private int month;
        private int year;
        private int hourOfDay;
        private int minutes;
        private int seconds;
        private int ms;

        public Tag(int lastTagNumber, String tagText) {
            this.tagText = tagText;
            number = lastTagNumber + 1;
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            // Day of week from Monday (1) to Sunday (7)
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            // Day in month
            dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
            // Month
            month = calendar.get(Calendar.MONTH);
            // Year
            year = calendar.get(Calendar.YEAR);
            // Hour (24h format)
            hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            // Minutes
            minutes =  calendar.get(Calendar.MINUTE);
            // Seconds
            seconds = calendar.get(Calendar.SECOND);
            // Milliseconds
            ms = calendar.get((Calendar.MILLISECOND));
        }

        public Tag(String tagText){
            this(0, tagText);
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getTagText() {
            return tagText;
        }

        public void setTagText(String tagText) {
            this.tagText = tagText;
        }

        public int getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public int getDayInMonth() {
            return dayInMonth;
        }

        public void setDayInMonth(int dayInMonth) {
            this.dayInMonth = dayInMonth;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getHourOfDay() {
            return hourOfDay;
        }

        public void setHourOfDay(int hourOfDay) {
            this.hourOfDay = hourOfDay;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public int getMs() {
            return ms;
        }

        public void setMs(int ms) {
            this.ms = ms;
        }
    }

    public static class Version {
        private final Tag tag;
        private final Map<String, String> roles;
        private ISO_3166 iso;
        private String text;

        public Version(Tag tag, ISO_3166 iso){
            this.tag = tag;
            this.iso = iso;
            roles = new HashMap<>();
            text = "";
        }

        public static Version createFirstVersion(ISO_3166 src){
            return new Version(new Tag(Load.language("tag_FirstScript", "First script", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()))), src);
        }

        public static Version increment(Version lastVersion, String updates, ISO_3166 lng){
            Tag lastTag = lastVersion.getTag();
            return new Version(new Tag(lastTag.getNumber(), updates), lng);
        }

        public static Version increment(Version lastVersion, ISO_3166 lng){
            String sUp = Load.language("tag_UpdateScript", "Update script of", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()));
            String sVer = Load.language("msg_Version", "Version", ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()));
            return increment(lastVersion, String.format("%s \"%s %d\"", sUp, sVer, lastVersion.tag.number), lng);
        }

        public Tag getTag() {
            return tag;
        }

        public Map<String, String> getRoles() {
            return roles;
        }

        public ISO_3166 getIso() {
            return iso;
        }

        public void setIso(ISO_3166 iso) {
            this.iso = iso;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
