package petgame.model;

import petgame.enums.PetType;
import petgame.model.behavior.PetBehaviorStrategy;
import petgame.model.factory.PetFactory;
import petgame.model.state.EvolvedState;
import petgame.model.state.HealthyState;
import petgame.model.state.PetState;
import petgame.model.state.SickState;
import petgame.record.PetSnapshot;

public class Pet {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;

    private final PetType type;
    private String name;
    private PetAttributes attributes;
    private PetStatus status;
    private final PetBehaviorStrategy behaviorStrategy;
    private PetState currentState;

    public Pet(PetType type, String name, PetBehaviorStrategy behaviorStrategy) {
        this(type, name, type.getInitialHunger(), type.getInitialMood(), type.getInitialEnergy(), 
                0, 0, false, false, behaviorStrategy);
    }

    public Pet(PetType type, String name, int hunger, int mood, int energy) {
        this(type, name, hunger, mood, energy, 0, 0, false, false, null);
    }

    public Pet(PetType type,
            String name,
            int hunger,
            int mood,
            int energy,
            int careProgress,
            int neglectCounter,
            boolean evolved,
            boolean sick,
            PetBehaviorStrategy behaviorStrategy) {
        this.type = type;
        this.name = (name == null || name.isBlank()) ? type.getDefaultName() : name;
        this.attributes = new PetAttributes(hunger, mood, energy);
        this.status = new PetStatus(careProgress, neglectCounter, evolved, sick);
        this.behaviorStrategy = behaviorStrategy != null ? behaviorStrategy : 
                createBehaviorStrategyForType(type);
        updateState();
        evaluateWellbeing();
    }

    public PetType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void rename(String newName) {
        if (newName != null && !newName.isBlank()) {
            this.name = newName.trim();
        }
    }

    public int getHunger() {
        return attributes.hunger();
    }

    public int getMood() {
        return attributes.mood();
    }

    public int getEnergy() {
        return attributes.energy();
    }

    public void feed() {
        attributes = behaviorStrategy.feed(attributes);
        evaluateWellbeing();
    }

    public void play() {
        attributes = behaviorStrategy.play(attributes);
        evaluateWellbeing();
    }

    public void sleep() {
        attributes = behaviorStrategy.sleep(attributes);
        evaluateWellbeing();
    }

    public void tick() {
        attributes = behaviorStrategy.tick(attributes);
        evaluateWellbeing();
    }

    public PetSnapshot snapshot() {
        return new PetSnapshot(type, name, attributes.hunger(), attributes.mood(), 
                attributes.energy(), status.careProgress(), status.neglectCounter(), 
                status.evolved(), status.sick());
    }

    public void restoreFromSnapshot(PetSnapshot snapshot) {
        this.name = snapshot.name();
        this.attributes = new PetAttributes(snapshot.hunger(), snapshot.mood(), snapshot.energy());
        this.status = new PetStatus(snapshot.careProgress(), snapshot.neglectCounter(), 
                snapshot.evolved(), snapshot.sick());
        updateState();
    }

    public String statusMessage() {
        return currentState.getStatusMessage(attributes);
    }

    public boolean isHungry() {
        return attributes.hunger() < 30;
    }

    public boolean isSleepy() {
        return attributes.energy() < 30;
    }

    public boolean isBored() {
        return attributes.mood() < 30;
    }

    @Override
    public String toString() {
        return name + " (" + type.getDisplayName() + ")";
    }

    public boolean isEvolved() {
        return status.evolved();
    }

    public boolean isSick() {
        return status.sick();
    }

    public int getCareProgress() {
        return status.careProgress();
    }

    public int getNeglectCounter() {
        return status.neglectCounter();
    }

    private void updateState() {
        if (status.evolved()) {
            currentState = new EvolvedState();
        } else if (status.sick()) {
            currentState = new SickState();
        } else {
            currentState = new HealthyState();
        }
    }

    private void evaluateWellbeing() {
        PetStatus newStatus = currentState.evaluate(attributes, status);
        boolean stateChanged = !newStatus.equals(status);
        status = newStatus;
        
        if (stateChanged) {
            updateState();
        }
    }

    private static PetBehaviorStrategy createBehaviorStrategyForType(petgame.enums.PetType type) {
        return PetFactory.createBehaviorStrategy(type);
    }
}
