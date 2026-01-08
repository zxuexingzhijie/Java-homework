package petgame.command;

import petgame.model.Pet;
import petgame.record.PetSnapshot;

public class PlayCommand implements PetCommand {
    private PetSnapshot previousState;

    @Override
    public void execute(Pet pet) {
        previousState = pet.snapshot();
        pet.play();
    }

    @Override
    public void undo(Pet pet) {
        if (previousState != null) {
            pet.restoreFromSnapshot(previousState);
        }
    }

    @Override
    public String getDescription() {
        return "玩耍";
    }
}
