package petgame.controller;

import java.util.List;
import petgame.enums.PetType;
import petgame.model.Pet;
import petgame.observer.EventPublisher;
import petgame.persistence.PersistenceManager;
import petgame.util.ActionLogger;

public class GameController {

    private final PetCollectionService collectionService;
    private final PetService petService;
    private final MilestoneChecker milestoneChecker;
    private final ActionLogger logger;
    private final EventPublisher eventPublisher;

    public GameController() {
        eventPublisher = new EventPublisher();
        PersistenceManager persistenceManager = PersistenceManager.createDefault();
        collectionService = new PetCollectionService(persistenceManager, eventPublisher);
        petService = new PetService(eventPublisher);
        milestoneChecker = new MilestoneChecker(eventPublisher);
        logger = new ActionLogger();
        eventPublisher.subscribe(logger);
    }

    public Pet getPet() {
        return collectionService.getActivePet();
    }

    public List<Pet> getPets() {
        return collectionService.getPets();
    }

    public int getActivePetIndex() {
        return collectionService.getActivePetIndex();
    }

    public ActionLogger getLogger() {
        return logger;
    }

    public void adoptPet(PetType type, String name) {
        collectionService.adoptPet(type, name);
    }

    public void selectPet(int index) {
        collectionService.selectPet(index);
    }

    public void feedPet() {
        Pet pet = getPet();
        boolean wasEvolved = pet.isEvolved();
        boolean wasSick = pet.isSick();
        petService.feedPet(pet);
        milestoneChecker.checkMilestones(pet, wasEvolved, wasSick);
        collectionService.persist();
    }

    public void playWithPet() {
        Pet pet = getPet();
        boolean wasEvolved = pet.isEvolved();
        boolean wasSick = pet.isSick();
        petService.playWithPet(pet);
        milestoneChecker.checkMilestones(pet, wasEvolved, wasSick);
        collectionService.persist();
    }

    public void letPetSleep() {
        Pet pet = getPet();
        boolean wasEvolved = pet.isEvolved();
        boolean wasSick = pet.isSick();
        petService.letPetSleep(pet);
        milestoneChecker.checkMilestones(pet, wasEvolved, wasSick);
        collectionService.persist();
    }

    public void tickPet() {
        Pet pet = getPet();
        petService.tickPet(pet);
        collectionService.persist();
    }
}
