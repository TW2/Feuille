/*
 * Copyright (C) 2019 util2
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
package feuille.panel;

import feuille.util.ChatUser;
import feuille.util.ChatUserKind;
import feuille.util.ChatUserRole;
import feuille.util.DrawColor;
import feuille.util.ISO_3166;
import feuille.util.Language;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Calendar;
import javax.swing.JPanel;

/**
 *
 * @author util2
 */
public class ChatMyStatus extends JPanel {
    
    private ChatUser user = new ChatUser();

    public ChatMyStatus() {
        init();
    }
    
    private void init(){
        setPreferredSize(new Dimension(80, 80));
    }
    
    public void initializeChatPanel(Language in, ISO_3166 get){        
        // Check if there is a requested language (forced)
        // and choose between posibilities
        if(in.isForced() == true){
            get = in.getIso();
        }
        
        ChatUserKind.Boy.setName(in.getTranslated("ChatBoy", get, "Boy"));
        ChatUserKind.Girl.setName(in.getTranslated("ChatGirl", get, "Girl"));
        ChatUserKind.Man.setName(in.getTranslated("ChatMan", get, "Man"));
        ChatUserKind.None.setName(in.getTranslated("ChatNone", get, "None"));
        ChatUserKind.Other.setName(in.getTranslated("ChatOther", get, "Other"));
        ChatUserKind.Woman.setName(in.getTranslated("ChatWoman", get, "Woman"));
        
        ChatUserRole.Adapter.setName(in.getTranslated("ChatAdapter", get, "Adapter"));
        ChatUserRole.Checker.setName(in.getTranslated("ChatChecker", get, "Checker"));
        ChatUserRole.Editer.setName(in.getTranslated("ChatEditer", get, "Editer"));
        ChatUserRole.Encoder.setName(in.getTranslated("ChatEncoder", get, "Encoder"));
        ChatUserRole.Founder.setName(in.getTranslated("ChatFounder", get, "Founder"));
        ChatUserRole.Leecher.setName(in.getTranslated("ChatLeecher", get, "Leecher"));
        ChatUserRole.NativeLanguageAdapter.setName(in.getTranslated("ChatNativeLanguageAdapter", get, "NativeLanguageAdapter"));
        ChatUserRole.Owner.setName(in.getTranslated("ChatOwner", get, "Owner"));
        ChatUserRole.QualityChecker.setName(in.getTranslated("ChatQualityChecker", get, "QualityChecker"));
        ChatUserRole.RawHunter.setName(in.getTranslated("ChatRawHunter", get, "RawHunter"));
        ChatUserRole.RawProvider.setName(in.getTranslated("ChatRawProvider", get, "RawProvider"));
        ChatUserRole.ReleaseProvider.setName(in.getTranslated("ChatReleaseProvider", get, "ReleaseProvider"));
        ChatUserRole.Seeder.setName(in.getTranslated("ChatSeeder", get, "Seeder"));
        ChatUserRole.Timer.setName(in.getTranslated("ChatTimer", get, "Timer"));
        ChatUserRole.Translater.setName(in.getTranslated("ChatTranslater", get, "Translater"));
        ChatUserRole.Typesetter.setName(in.getTranslated("ChatTypesetter", get, "Typesetter"));
        ChatUserRole.VideoChecker.setName(in.getTranslated("ChatVideoChecker", get, "VideoChecker"));
        ChatUserRole.Webmaster.setName(in.getTranslated("ChatWebmaster", get, "Webmaster"));
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        Font oldFont = g2d.getFont();
        
        Color topColor, bottomColor, textColor = Color.white;
        if(user.getKind() == ChatUserKind.Girl | user.getKind() == ChatUserKind.Woman){
            topColor = DrawColor.pink.getColor();
            bottomColor = Color.red;
        }else if(user.getKind() == ChatUserKind.Boy | user.getKind() == ChatUserKind.Man){
            topColor = DrawColor.royal_blue.getColor().brighter();
            bottomColor = Color.blue;
        }else{
            topColor = Color.lightGray;
            bottomColor = Color.darkGray;
        }
        
        // Fill background with gradient
        GradientPaint gradient = new GradientPaint(0f, 0f, topColor, 0f, getHeight(), bottomColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw surname
        g2d.setColor(textColor);
        g2d.setFont(g2d.getFont().deriveFont(30f));
        g2d.drawString(user.getSurname(), 10, 30);
        g2d.setFont(oldFont);
        
        // Draw attributes
        g2d.setFont(g2d.getFont().deriveFont(20f));
        g2d.drawString(
                user.getKind().getName()
                        + ", " 
                        + user.getRole().getName() 
                        + ", " 
                        + user.getSince().get(Calendar.YEAR),
                10, 60);
        g2d.setFont(oldFont);
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }

    public ChatUser getUser() {
        return user;
    }
    
}
