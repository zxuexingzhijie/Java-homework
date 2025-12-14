package petgame.record;

import java.util.List;


public record PetCollectionSnapshot(List<PetSnapshot> pets, int activeIndex) {
}
