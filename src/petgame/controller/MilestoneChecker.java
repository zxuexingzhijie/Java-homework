package petgame.controller;

import petgame.model.Pet;
import petgame.observer.EventPublisher;
import petgame.observer.GameEvent;

public class MilestoneChecker {
    private final EventPublisher eventPublisher;

    public MilestoneChecker(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void checkMilestones(Pet pet, boolean wasEvolved, boolean wasSick) {
        if (!wasEvolved && pet.isEvolved()) {
            eventPublisher.publish(GameEvent.PET_EVOLVED, pet);
            eventPublisher.publish(GameEvent.SYSTEM_MESSAGE, pet.getName() + " 进化成闪耀形态！");
        }
        if (!wasSick && pet.isSick()) {
            eventPublisher.publish(GameEvent.PET_SICK, pet);
            eventPublisher.publish(GameEvent.SYSTEM_MESSAGE, pet.getName() + " 生病了，需要更多关心。");
        } else if (wasSick && !pet.isSick()) {
            eventPublisher.publish(GameEvent.PET_RECOVERED, pet);
            eventPublisher.publish(GameEvent.SYSTEM_MESSAGE, pet.getName() + " 痊愈啦，继续保持！");
        }
    }
}
