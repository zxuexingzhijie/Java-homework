package petgame.command.impl;

import java.util.ArrayDeque;
import java.util.Deque;

import petgame.command.PetCommand;
import petgame.model.Pet;

public class CommandInvoker {
    private final Deque<PetCommand> history = new ArrayDeque<>();
    private final int maxHistorySize = 10;

    public void execute(PetCommand command, Pet pet) {
        command.execute(pet);
        history.push(command);
        if (history.size() > maxHistorySize) {
            history.removeLast();
        }
    }

    public void undo(Pet pet) {
        if (!history.isEmpty()) {
            PetCommand command = history.pop();
            command.undo(pet);
        }
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public void clearHistory() {
        history.clear();
    }
}
