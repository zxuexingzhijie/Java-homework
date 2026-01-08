package petgame.model.state;

import petgame.model.PetAttributes;
import petgame.model.PetStatus;

public interface PetState {
    PetStatus evaluate(PetAttributes attributes, PetStatus currentStatus);
    String getStatusMessage(PetAttributes attributes);
}
