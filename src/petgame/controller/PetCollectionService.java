package petgame.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import petgame.enums.PetType;
import petgame.model.Pet;
import petgame.observer.EventPublisher;
import petgame.observer.GameEvent;
import petgame.persistence.PersistenceManager;
import petgame.record.PetCollectionSnapshot;

public class PetCollectionService {
    private final List<Pet> pets = new ArrayList<>();
    private int activeIndex;
    private final PersistenceManager persistenceManager;
    private final EventPublisher eventPublisher;

    public PetCollectionService(PersistenceManager persistenceManager, EventPublisher eventPublisher) {
        this.persistenceManager = persistenceManager;
        this.eventPublisher = eventPublisher;
        loadPets();
    }

    private void loadPets() {
        PetCollectionSnapshot snapshot = persistenceManager.loadCollection();
        List<Pet> loaded = petgame.model.factory.PetFactory.fromSnapshots(snapshot.pets());
        if (loaded.isEmpty()) {
            pets.add(petgame.model.factory.PetFactory.create(PetType.DRAGON, PetType.DRAGON.getDefaultName()));
            activeIndex = 0;
        } else {
            pets.addAll(loaded);
            activeIndex = Math.max(0, Math.min(snapshot.activeIndex(), pets.size() - 1));
            eventPublisher.publish(GameEvent.SYSTEM_MESSAGE, 
                    "加载了上次的宠物们，当前是：" + pets.get(activeIndex).getName());
        }
    }

    public Pet getActivePet() {
        return pets.get(activeIndex);
    }

    public List<Pet> getPets() {
        return Collections.unmodifiableList(pets);
    }

    public int getActivePetIndex() {
        return activeIndex;
    }

    public void adoptPet(PetType type, String name) {
        Pet newPet = petgame.model.factory.PetFactory.create(type, name);
        pets.add(newPet);
        activeIndex = pets.size() - 1;
        eventPublisher.publish(GameEvent.PET_ADOPTED, newPet);
        eventPublisher.publish(GameEvent.SYSTEM_MESSAGE, 
                "新宠物加入：" + newPet.getName() + " (" + type.getDisplayName() + ")");
        persist();
    }

    public void selectPet(int index) {
        if (index < 0 || index >= pets.size() || index == activeIndex) {
            return;
        }
        activeIndex = index;
        Pet selectedPet = pets.get(index);
        eventPublisher.publish(GameEvent.PET_SELECTED, selectedPet);
        eventPublisher.publish(GameEvent.SYSTEM_MESSAGE, "切换到 " + selectedPet.getName());
        persist();
    }

    public void persist() {
        persistenceManager.saveAll(pets, activeIndex);
    }
}
