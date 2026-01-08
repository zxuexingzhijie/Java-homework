package petgame.persistence;

import java.util.List;
import petgame.model.Pet;
import petgame.record.PetCollectionSnapshot;

public class PersistenceManager {
    private final PersistenceStrategy strategy;

    public PersistenceManager(PersistenceStrategy strategy) {
        this.strategy = strategy;
    }

    public PetCollectionSnapshot loadCollection() {
        return strategy.loadCollection();
    }

    public void saveAll(List<Pet> pets, int activeIndex) {
        strategy.saveAll(pets, activeIndex);
    }

    public static PersistenceManager createDefault() {
        return new PersistenceManager(new PropertiesPersistenceStrategy());
    }
}
