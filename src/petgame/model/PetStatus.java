package petgame.model;

public record PetStatus(int careProgress, int neglectCounter, boolean evolved, boolean sick) {
    public static final int MIN_PROGRESS = 0;
    public static final int MAX_PROGRESS = 200;

    public PetStatus {
        careProgress = clampProgress(careProgress);
        neglectCounter = clampProgress(neglectCounter);
    }

    public PetStatus withCareProgress(int careProgress) {
        return new PetStatus(careProgress, neglectCounter, evolved, sick);
    }

    public PetStatus withNeglectCounter(int neglectCounter) {
        return new PetStatus(careProgress, neglectCounter, evolved, sick);
    }

    public PetStatus withEvolved(boolean evolved) {
        return new PetStatus(careProgress, neglectCounter, evolved, sick);
    }

    public PetStatus withSick(boolean sick) {
        return new PetStatus(careProgress, neglectCounter, evolved, sick);
    }

    private static int clampProgress(int value) {
        return Math.max(MIN_PROGRESS, Math.min(MAX_PROGRESS, value));
    }
}
