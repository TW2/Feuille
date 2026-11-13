package feuille.io.assa;

import feuille.scripting.EffectsDB;

import java.util.ArrayList;
import java.util.List;

public class AssEffect {

    public enum MethodType {
        None("None"),
        XFX("XFX"),
        Groovy("Groovy"),
        JavaScript("JavaScript"),
        Kotlin("Kotlin"),
        Lua("Lua"),
        Python("Python"),
        Ruby("Ruby");

        final String name;

        MethodType(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }

        public static MethodType of(String name){
            MethodType methodType = MethodType.None;
            for(MethodType type : values()){
                if(type.getName().equalsIgnoreCase(name)){
                    methodType = type;
                    break;
                }
            }
            return methodType;
        }
    }

    private MethodType methodType;
    private String collectionName;
    private String name;
    private List<String> effects;

    public AssEffect(MethodType methodType, String collectionName, String name, List<String> effects) {
        this.methodType = methodType;
        this.collectionName = collectionName;
        this.name = name;
        this.effects = effects;
    }

    public AssEffect(String name, String collectionName, List<String> effects) {
        this(MethodType.None, collectionName, name, effects);
    }

    public AssEffect(String name, List<String> effects){
        this(MethodType.None, "Default collection", name, effects);
    }

    public AssEffect(List<String> effects) {
        this("Default", effects);
    }

    public AssEffect() {
        this("Default", new ArrayList<>());
    }

    public static AssEffect fromLine(String line){
        // [FXs]
        // Format: Method, Name, Effect
        // FX: None,Banner,{\\move(100,200,300,120)}
        AssEffect fx = new AssEffect();
        String[] t = line.substring(3).split(",");

        fx.methodType = MethodType.of(t[0]);
        fx.collectionName = t[1];
        fx.name = t[2];
        fx.effects = EffectsDB.stringToData(t[3]);

        return fx;
    }

    public String toLine(){
        // [FXs]
        // Format: Method, Name, Effects
        // FX: None,Banner,{\\move(100,200,300,120)}
        return String.format("FX: %s,%s,%s,%s",
                methodType.getName(),
                collectionName,
                name,
                EffectsDB.stringifyData(effects)
        );
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEffects() {
        return effects;
    }

    public void setEffects(List<String> effects) {
        this.effects = effects;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
