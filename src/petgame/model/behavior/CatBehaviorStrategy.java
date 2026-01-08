package petgame.model.behavior;

import petgame.model.PetAttributes;

public class CatBehaviorStrategy implements PetBehaviorStrategy {
    @Override
    public PetAttributes feed(PetAttributes attributes) {
        return attributes
                .withHunger(attributes.hunger() + 20)
                .withMood(attributes.mood() + 5);
    }

    @Override
    public PetAttributes play(PetAttributes attributes) {
        return attributes
                .withMood(attributes.mood() + 18)
                .withEnergy(attributes.energy() - 10)
                .withHunger(attributes.hunger() - 4);
    }

    @Override
    public PetAttributes sleep(PetAttributes attributes) {
        return attributes
                .withEnergy(attributes.energy() + 25)
                .withMood(attributes.mood() - 3)
                .withHunger(attributes.hunger() - 5);
    }

    @Override
    public PetAttributes tick(PetAttributes attributes) {
        return attributes
                .withHunger(attributes.hunger() - 2)
                .withMood(attributes.mood() - 2)
                .withEnergy(attributes.energy() - 2);
    }
}
