package petgame.controller;

import petgame.model.Pet;
import petgame.observer.EventPublisher;
import petgame.observer.GameEvent;

public class PetService {
    private final EventPublisher eventPublisher;

    public PetService(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void feedPet(Pet pet) {
        pet.feed();
        eventPublisher.publish(GameEvent.PET_FED, pet);
    }

    public void playWithPet(Pet pet) {
        pet.play();
        eventPublisher.publish(GameEvent.PET_PLAYED, pet);
    }

    public void letPetSleep(Pet pet) {
        pet.sleep();
        eventPublisher.publish(GameEvent.PET_SLEPT, pet);
    }

    public void tickPet(Pet pet) {
        pet.tick();
        eventPublisher.publish(GameEvent.PET_TICKED, pet);
    }
}
