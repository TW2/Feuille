/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.welcome;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author The Wingate 2940
 */
public class AtomRSS {
    
    private final List<Map<InfoType, String>> entries = new ArrayList<>();
    
    public AtomRSS(String URLaddress, String category) throws MalformedURLException, IOException, IllegalArgumentException, FeedException{
        
        URL url = new URL(URLaddress);
        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
        
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List ents = feed.getEntries();
        Iterator itEntries = ents.iterator();
        
        while (itEntries.hasNext()) {
            SyndEntry entry = (SyndEntry)itEntries.next();
            
            if(category!=null){
                boolean addCategory = false;
                List cats = entry.getCategories();
                Iterator itCats = cats.iterator();
                while (itCats.hasNext()) {
                    SyndCategory cat = (SyndCategory)itCats.next();
                    if(cat.getName().equalsIgnoreCase(category)){
                        addCategory = true;
                    }
                }
                if(addCategory==true){
                    Map<InfoType, String> map = new HashMap<>();
                    map.put(InfoType.TITLE, entry.getTitle()==null ? "" : entry.getTitle());
                    map.put(InfoType.DESCRIPTION, entry.getDescription()==null ? "" : entry.getDescription().getValue());
                    map.put(InfoType.LINK, entry.getLink()==null ? "" : entry.getLink());
                    map.put(InfoType.AUTHOR, entry.getAuthor()==null ? "" : entry.getAuthor());
                    map.put(InfoType.DATE, entry.getPublishedDate()==null ? "" : entry.getPublishedDate().toString());

                    entries.add(map);
                }
            }else{
                Map<InfoType, String> map = new HashMap<>();
                map.put(InfoType.TITLE, entry.getTitle()==null ? "" : entry.getTitle());
                map.put(InfoType.DESCRIPTION, entry.getDescription()==null ? "" : entry.getDescription().getValue());
                map.put(InfoType.LINK, entry.getLink()==null ? "" : entry.getLink());
                map.put(InfoType.AUTHOR, entry.getAuthor()==null ? "" : entry.getAuthor());
                map.put(InfoType.DATE, entry.getPublishedDate()==null ? "" : entry.getPublishedDate().toString());

                entries.add(map);
            }
        }
        
    }
    
    public enum InfoType{
        AUTHOR, TITLE, DESCRIPTION, LINK, DATE;
    }
    
    public List<Map<InfoType, String>> getEntries(){
        return entries;
    }
    
}
