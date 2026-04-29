package feuille.util.assa;

import feuille.util.Loader;

public enum AssActorType {
    Male(Loader.language("actor.male", "Male")),
    Female(Loader.language("actor.female", "Female")),
    Robot(Loader.language("actor.robot", "Robot")),
    Narrator(Loader.language("actor.narrator", "Narrator")),
    Unknown(Loader.language("actor.default", "Unknown"));

    final String name;

    private AssActorType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
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
