package feuille.io.js;

import feuille.io.Exchange;
import org.graalvm.polyglot.HostAccess;

public class Methods {

    private final Exchange exchange;

    public Methods(Exchange exchange) {
        this.exchange = exchange;
    }

    @HostAccess.Export
    public void hello(String name){
        System.out.println("Hello " + name + " from Java!");
    }

    @HostAccess.Export
    public String getSentence(){
        return exchange.getEditorPanel().getCurrentText();
    }

    @HostAccess.Export
    public void setSentence(String sentence){
        exchange.getEditorPanel().setCurrentText(sentence);
    }
}
