package petgame.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import petgame.enums.PetType;
import petgame.model.Pet;
import petgame.record.PetCollectionSnapshot;
import petgame.persistence.PetPersistence;
import petgame.util.ActionLogger;


public class GameController {

    private final List<Pet> pets = new ArrayList<>();
    private int activeIndex;
    private Pet pet;
    private final ActionLogger logger = new ActionLogger();

    public GameController() {
        PetCollectionSnapshot snapshot = PetPersistence.loadCollection();
        List<Pet> loaded = PetPersistence.fromSnapshots(snapshot.pets());
        if (loaded.isEmpty()) {
            pets.add(new Pet(PetType.DRAGON, PetType.DRAGON.getDefaultName()));
            activeIndex = 0;
        } else {
            pets.addAll(loaded);
            activeIndex = Math.max(0, Math.min(snapshot.activeIndex(), pets.size() - 1));
            logger.logSystem("加载了上次的宠物们，当前是：" + pets.get(activeIndex).getName());
        }
        pet = pets.get(activeIndex);
    }

    public Pet getPet() {
        return pet;
    }

    public List<Pet> getPets() {
        return Collections.unmodifiableList(pets);
    }

    public int getActivePetIndex() {
        return activeIndex;
    }

    public ActionLogger getLogger() {
        return logger;
    }

    public void adoptPet(PetType type, String name) {
        Pet newPet = new Pet(type, name);
        pets.add(newPet);
        activeIndex = pets.size() - 1;
        pet = newPet;
        logger.logSystem("新宠物加入：" + newPet.getName() + " (" + type.getDisplayName() + ")");
        persist();
    }

    public void selectPet(int index) {
        if (index < 0 || index >= pets.size() || index == activeIndex) {
            return;
        }
        activeIndex = index;
        pet = pets.get(index);
        logger.logSystem("切换到 " + pet.getName());
        persist();
    }

    public void feedPet() {
        executePetAction(() -> {
            pet.feed();
            logger.logFeed(pet.getName());
        });
    }

    public void playWithPet() {
        executePetAction(() -> {
            pet.play();
            logger.logPlay(pet.getName());
        });
    }

    public void letPetSleep() {
        executePetAction(() -> {
            pet.sleep();
            logger.logSleep(pet.getName());
        });
    }

    public void tickPet() {
        executePetAction(pet::tick);
    }

    private void persist() {
        PetPersistence.saveAll(pets, activeIndex);
    }

    private void executePetAction(Runnable action) {
        boolean wasEvolved = pet.isEvolved();
        boolean wasSick = pet.isSick();
        action.run();
        checkMilestones(wasEvolved, wasSick);
        persist();
    }

    private void checkMilestones(boolean wasEvolved, boolean wasSick) {
        if (!wasEvolved && pet.isEvolved()) {
            logger.logSystem(pet.getName() + " 进化成闪耀形态！");
        }
        if (!wasSick && pet.isSick()) {
            logger.logSystem(pet.getName() + " 生病了，需要更多关心。");
        } else if (wasSick && !pet.isSick()) {
            logger.logSystem(pet.getName() + " 痊愈啦，继续保持！");
        }
    }
}
