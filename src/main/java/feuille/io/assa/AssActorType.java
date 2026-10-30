package feuille.io.assa;

import feuille.util.Loader;

import javax.swing.*;

public enum AssActorType {
    Man(Loader.language("actor.man", "Man"),
            Loader.fromResource("/images/16-k-man.png", 16, 16)),
    Woman(Loader.language("actor.woman", "Woman"),
            Loader.fromResource("/images/16-k-woman.png", 16, 16)),
    Boy(Loader.language("actor.boy", "Boy"),
            Loader.fromResource("/images/16-k-boy.png", 16, 16)),
    Girl(Loader.language("actor.girl", "Girl"),
            Loader.fromResource("/images/16-k-girl.png", 16, 16)),
    Robot(Loader.language("actor.robot", "Robot"),
            Loader.fromResource("/images/16-k-robot.png", 16, 16)),
    Narrator(Loader.language("actor.narrator", "Narrator"),
            Loader.fromResource("/images/16-k-narrator.png", 16, 16)),
    Unknown(Loader.language("actor.default", "Unknown"),
            Loader.fromResource("/images/16-k-unknown.png", 16, 16));

    final String name;
    final ImageIcon icon;

    AssActorType(String name, ImageIcon icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return name;
    }

    public static AssActorType get(String s){
        AssActorType kind = AssActorType.Unknown;

        for(AssActorType k : AssActorType.values()){
            if(s.equalsIgnoreCase(k.getName())){
                kind = k;
                break;
            }
        }

        return kind;
    }
}
