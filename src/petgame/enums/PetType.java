package petgame.enums;

import java.awt.Color;


public enum PetType {
    DRAGON("翡翠小龙", "云宝", new Color(46, 204, 113), 70, 65, 60),
    CAT("调皮小猫", "喵喵", new Color(241, 196, 15), 60, 75, 55),
    DOG("忠诚小狗", "旺财", new Color(52, 152, 219), 65, 60, 70);

    private final String displayName;
    private final String defaultName;
    private final Color baseColor;
    private final int initialHunger;
    private final int initialMood;
    private final int initialEnergy;

    PetType(String displayName,
            String defaultName,
            Color baseColor,
            int initialHunger,
            int initialMood,
            int initialEnergy) {
        this.displayName = displayName;
        this.defaultName = defaultName;
        this.baseColor = baseColor;
        this.initialHunger = initialHunger;
        this.initialMood = initialMood;
        this.initialEnergy = initialEnergy;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public int getInitialHunger() {
        return initialHunger;
    }

    public int getInitialMood() {
        return initialMood;
    }

    public int getInitialEnergy() {
        return initialEnergy;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
