package petgame.model.factory;

import java.util.ArrayList;
import java.util.List;
import petgame.enums.PetType;
import petgame.model.Pet;
import petgame.model.behavior.CatBehaviorStrategy;
import petgame.model.behavior.DogBehaviorStrategy;
import petgame.model.behavior.DragonBehaviorStrategy;
import petgame.model.behavior.PetBehaviorStrategy;
import petgame.record.PetSnapshot;

public class PetFactory {
    public static Pet create(PetType type, String name) {
        PetBehaviorStrategy strategy = createBehaviorStrategy(type);
        return new Pet(type, name, strategy);
    }

    public static Pet createFromSnapshot(PetSnapshot snapshot) {
        PetBehaviorStrategy strategy = createBehaviorStrategy(snapshot.type());
        return new Pet(
                snapshot.type(),
                snapshot.name(),
                snapshot.hunger(),
                snapshot.mood(),
                snapshot.energy(),
                snapshot.careProgress(),
                snapshot.neglectCounter(),
                snapshot.evolved(),
                snapshot.sick(),
                strategy
        );
    }

    public static List<Pet> fromSnapshots(List<PetSnapshot> snapshots) {
        List<Pet> pets = new ArrayList<>();
        for (PetSnapshot snapshot : snapshots) {
            pets.add(createFromSnapshot(snapshot));
        }
        return pets;
    }

    public static PetBehaviorStrategy createBehaviorStrategy(PetType type) {
        return switch (type) {
            case DRAGON -> new DragonBehaviorStrategy();
            case CAT -> new CatBehaviorStrategy();
            case DOG -> new DogBehaviorStrategy();
        };
    }
}
