package petgame.model.behavior;

import petgame.model.PetAttributes;

public class DragonBehaviorStrategy implements PetBehaviorStrategy {
    @Override
    public PetAttributes feed(PetAttributes attributes) {
        return attributes
                .withHunger(attributes.hunger() + 18)
                .withMood(attributes.mood() + 4);
    }

    @Override
    public PetAttributes play(PetAttributes attributes) {
        return attributes
                .withMood(attributes.mood() + 15)
                .withEnergy(attributes.energy() - 12)
                .withHunger(attributes.hunger() - 5);
    }

    @Override
    public PetAttributes sleep(PetAttributes attributes) {
        return attributes
                .withEnergy(attributes.energy() + 22)
                .withMood(attributes.mood() - 4)
                .withHunger(attributes.hunger() - 6);
    }

    @Override
    public PetAttributes tick(PetAttributes attributes) {
        return attributes
                .withHunger(attributes.hunger() - 3)
                .withMood(attributes.mood() - 2)
                .withEnergy(attributes.energy() - 2);
    }
}
