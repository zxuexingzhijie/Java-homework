package petgame.model;

import petgame.enums.PetType;
import petgame.record.PetSnapshot;


public class Pet {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;

    private final PetType type;
    private String name;
    private int hunger;
    private int mood;
    private int energy;
    private int careProgress;
    private int neglectCounter;
    private boolean evolved;
    private boolean sick;

    public Pet(PetType type, String name) {
        this(type, name, type.getInitialHunger(), type.getInitialMood(), type.getInitialEnergy());
    }

    public Pet(PetType type, String name, int hunger, int mood, int energy) {
        this(type, name, hunger, mood, energy, 0, 0, false, false);
    }

    public Pet(PetType type,
            String name,
            int hunger,
            int mood,
            int energy,
            int careProgress,
            int neglectCounter,
            boolean evolved,
            boolean sick) {
        this.type = type;
        this.name = (name == null || name.isBlank()) ? type.getDefaultName() : name;
        this.hunger = clamp(hunger);
        this.mood = clamp(mood);
        this.energy = clamp(energy);
        this.careProgress = clampProgress(careProgress);
        this.neglectCounter = clampProgress(neglectCounter);
        this.evolved = evolved;
        this.sick = sick;
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
        return hunger;
    }

    public int getMood() {
        return mood;
    }

    public int getEnergy() {
        return energy;
    }

    public void feed() {
        hunger = clamp(hunger + 18);
        mood = clamp(mood + 4);
        evaluateWellbeing();
    }

    public void play() {
        mood = clamp(mood + 15);
        energy = clamp(energy - 12);
        hunger = clamp(hunger - 5);
        evaluateWellbeing();
    }

    public void sleep() {
        energy = clamp(energy + 22);
        mood = clamp(mood - 4);
        hunger = clamp(hunger - 6);
        evaluateWellbeing();
    }

    public void tick() {
        hunger = clamp(hunger - 3);
        mood = clamp(mood - 2);
        energy = clamp(energy - 2);
        evaluateWellbeing();
    }

    public PetSnapshot snapshot() {
        return new PetSnapshot(type, name, hunger, mood, energy, careProgress, neglectCounter, evolved, sick);
    }

    public String statusMessage() {
        if (sick) {
            return "我生病了，需要精心照顾…";
        }
        if (hunger < 20) {
            return "我饿了，快喂我吧！";
        }
        if (mood < 20) {
            return "好无聊，陪我玩！";
        }
        if (energy < 20) {
            return "有点困，需要休息。";
        }
        if (evolved) {
            return "力量觉醒，准备飞向更高的天空！";
        }
        return "精神饱满，想在天空飞翔！";
    }

    public boolean isHungry() {
        return hunger < 30;
    }

    public boolean isSleepy() {
        return energy < 30;
    }

    public boolean isBored() {
        return mood < 30;
    }

    @Override
    public String toString() {
        return name + " (" + type.getDisplayName() + ")";
    }

    public boolean isEvolved() {
        return evolved;
    }

    public boolean isSick() {
        return sick;
    }

    public int getCareProgress() {
        return careProgress;
    }

    public int getNeglectCounter() {
        return neglectCounter;
    }

    private int clamp(int value) {
        return Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
    }

    private int clampProgress(int value) {
        return Math.max(0, Math.min(200, value));
    }

    private void evaluateWellbeing() {
        int sum = hunger + mood + energy;
        if (sum >= 210) {
            careProgress = clampProgress(careProgress + 5);
            neglectCounter = clampProgress(neglectCounter - 6);
            if (!evolved && careProgress >= 120) {
                evolved = true;
            }
            if (sick && careProgress >= 80) {
                sick = false;
            }
        } else if (sum <= 120) {
            neglectCounter = clampProgress(neglectCounter + 6);
            careProgress = clampProgress(careProgress - 4);
            if (neglectCounter >= 90) {
                sick = true;
            }
        } else {
            careProgress = clampProgress(careProgress - 2);
            neglectCounter = clampProgress(neglectCounter - 2);
        }
    }
}
