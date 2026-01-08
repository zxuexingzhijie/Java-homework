package petgame.persistence;

import java.util.List;
import petgame.model.Pet;
import petgame.record.PetCollectionSnapshot;

public interface PersistenceStrategy {
    PetCollectionSnapshot loadCollection();
    void saveAll(List<Pet> pets, int activeIndex);
}
