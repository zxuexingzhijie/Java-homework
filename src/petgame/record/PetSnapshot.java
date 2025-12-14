package petgame.record;

import petgame.enums.PetType;


public record PetSnapshot(
        PetType type,
        String name,
        int hunger,
        int mood,
        int energy,
        int careProgress,
        int neglectCounter,
        boolean evolved,
        boolean sick) {
}
