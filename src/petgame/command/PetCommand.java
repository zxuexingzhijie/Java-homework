package petgame.command;

import petgame.model.Pet;

public interface PetCommand {
    void execute(Pet pet);
    void undo(Pet pet);
    String getDescription();
}
