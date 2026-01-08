package petgame.model;

public record PetAttributes(int hunger, int mood, int energy) {
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;

    public PetAttributes {
        hunger = clamp(hunger);
        mood = clamp(mood);
        energy = clamp(energy);
    }

    public PetAttributes withHunger(int hunger) {
        return new PetAttributes(hunger, mood, energy);
    }

    public PetAttributes withMood(int mood) {
        return new PetAttributes(hunger, mood, energy);
    }

    public PetAttributes withEnergy(int energy) {
        return new PetAttributes(hunger, mood, energy);
    }

    public int getSum() {
        return hunger + mood + energy;
    }

    private static int clamp(int value) {
        return Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
    }
}
