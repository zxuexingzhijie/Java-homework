package petgame.command.impl;

import petgame.command.PetCommand;
import petgame.model.Pet;
import petgame.record.PetSnapshot;

public class SleepCommand implements PetCommand {
    private PetSnapshot previousState;

    @Override
    public void execute(Pet pet) {
        previousState = pet.snapshot();
        pet.sleep();
    }

    @Override
    public void undo(Pet pet) {
        if (previousState != null) {
            pet.restoreFromSnapshot(previousState);
        }
    }

    @Override
    public String getDescription() {
        return "睡觉";
    }
}
