package petgame.model.state;

import petgame.model.PetAttributes;
import petgame.model.PetStatus;

public class HealthyState implements PetState {
    @Override
    public PetStatus evaluate(PetAttributes attributes, PetStatus currentStatus) {
        int sum = attributes.getSum();
        PetStatus newStatus = currentStatus;

        if (sum >= 210) {
            int newCareProgress = Math.min(200, currentStatus.careProgress() + 5);
            int newNeglectCounter = Math.max(0, currentStatus.neglectCounter() - 6);
            boolean newEvolved = !currentStatus.evolved() && newCareProgress >= 120;
            boolean newSick = currentStatus.sick() && newCareProgress >= 80 ? false : currentStatus.sick();
            newStatus = new PetStatus(newCareProgress, newNeglectCounter, 
                    newEvolved || currentStatus.evolved(), newSick);
        } else if (sum <= 120) {
            int newNeglectCounter = Math.min(200, currentStatus.neglectCounter() + 6);
            int newCareProgress = Math.max(0, currentStatus.careProgress() - 4);
            boolean newSick = newNeglectCounter >= 90 || currentStatus.sick();
            newStatus = new PetStatus(newCareProgress, newNeglectCounter, 
                    currentStatus.evolved(), newSick);
        } else {
            int newCareProgress = Math.max(0, currentStatus.careProgress() - 2);
            int newNeglectCounter = Math.max(0, currentStatus.neglectCounter() - 2);
            newStatus = new PetStatus(newCareProgress, newNeglectCounter, 
                    currentStatus.evolved(), currentStatus.sick());
        }

        return newStatus;
    }

    @Override
    public String getStatusMessage(PetAttributes attributes) {
        if (attributes.hunger() < 20) {
            return "我饿了，快喂我吧！";
        }
        if (attributes.mood() < 20) {
            return "好无聊，陪我玩！";
        }
        if (attributes.energy() < 20) {
            return "有点困，需要休息。";
        }
        return "精神饱满，想在天空飞翔！";
    }
}
