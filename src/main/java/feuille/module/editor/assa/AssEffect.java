package feuille.module.editor.assa;

public class AssEffect {
    private String name;
    private String effect;

    public AssEffect(String name, String effect) {
        this.name = name;
        this.effect = effect;
    }

    public AssEffect(String effect) {
        this("Default", effect);
    }

    public AssEffect() {
        this("Default", "");
    }

    public static AssEffect fromLine(String line){
        // [FXs]
        // Format: Name, Effect
        // FX: Banner,{\\move(100,200,300,120)}
        AssEffect fx = new AssEffect();
        String[] t = line.split(",");

        fx.name = t[0].substring(t[0].indexOf(" ") + 1);
        fx.effect = t[1];

        return fx;
    }

    public String toLine(){
        // [FXs]
        // Format: Name, Effect
        // FX: Banner,{\\move(100,200,300,120)}
        return "FX: " + name + "," + effect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return getName();
    }
}
