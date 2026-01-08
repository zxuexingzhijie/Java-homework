package petgame.command;

import petgame.model.Pet;
import petgame.record.PetSnapshot;

public class FeedCommand implements PetCommand {
    private PetSnapshot previousState;

    @Override
    public void execute(Pet pet) {
        previousState = pet.snapshot();
        pet.feed();
    }

    @Override
    public void undo(Pet pet) {
        if (previousState != null) {
            pet.restoreFromSnapshot(previousState);
        }
    }

    @Override
    public String getDescription() {
        return "喂食";
    }
}
