package petgame.model.behavior;

import petgame.model.PetAttributes;

public interface PetBehaviorStrategy {
    PetAttributes feed(PetAttributes attributes);
    PetAttributes play(PetAttributes attributes);
    PetAttributes sleep(PetAttributes attributes);
    PetAttributes tick(PetAttributes attributes);
}
