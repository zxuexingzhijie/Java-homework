package petgame.model.behavior;

import petgame.model.PetAttributes;

public class DogBehaviorStrategy implements PetBehaviorStrategy {
    @Override
    public PetAttributes feed(PetAttributes attributes) {
        return attributes
                .withHunger(attributes.hunger() + 19)
                .withMood(attributes.mood() + 6);
    }

    @Override
    public PetAttributes play(PetAttributes attributes) {
        return attributes
                .withMood(attributes.mood() + 20)
                .withEnergy(attributes.energy() - 14)
                .withHunger(attributes.hunger() - 6);
    }

    @Override
    public PetAttributes sleep(PetAttributes attributes) {
        return attributes
                .withEnergy(attributes.energy() + 24)
                .withMood(attributes.mood() - 5)
                .withHunger(attributes.hunger() - 7);
    }

    @Override
    public PetAttributes tick(PetAttributes attributes) {
        return attributes
                .withHunger(attributes.hunger() - 3)
                .withMood(attributes.mood() - 2)
                .withEnergy(attributes.energy() - 2);
    }
}
