package petgame.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import petgame.enums.PetType;
import petgame.model.Pet;
import petgame.record.PetCollectionSnapshot;
import petgame.record.PetSnapshot;


public final class PetPersistence {

    private static final Path SAVE_FILE =
            Path.of(System.getProperty("user.home"), ".flying_pet_state.properties");

    private PetPersistence() {
    }

    public static PetCollectionSnapshot loadCollection() {
        if (!Files.exists(SAVE_FILE)) {
            return new PetCollectionSnapshot(Collections.emptyList(), 0);
        }
        Properties properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(SAVE_FILE)) {
            properties.load(reader);
            int count = readInt(properties, "pet.count", -1);
            if (count <= 0) {
                Optional<PetSnapshot> legacy = loadLegacySnapshot(properties);
                return legacy
                        .map(snapshot -> new PetCollectionSnapshot(List.of(snapshot), 0))
                        .orElseGet(() -> new PetCollectionSnapshot(Collections.emptyList(), 0));
            }
            List<PetSnapshot> snapshots = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                readSnapshot(properties, i).ifPresent(snapshots::add);
            }
            if (snapshots.isEmpty()) {
                return new PetCollectionSnapshot(Collections.emptyList(), 0);
            }
            int activeIndex = readInt(properties, "pet.activeIndex", 0);
            activeIndex = Math.max(0, Math.min(snapshots.size() - 1, activeIndex));
            return new PetCollectionSnapshot(snapshots, activeIndex);
        } catch (IOException | IllegalArgumentException ex) {
            System.err.println("无法读取宠物存档：" + ex.getMessage());
            return new PetCollectionSnapshot(Collections.emptyList(), 0);
        }
    }

    public static void saveAll(List<Pet> pets, int activeIndex) {
        Properties properties = new Properties();
        properties.setProperty("pet.count", String.valueOf(pets.size()));
        properties.setProperty(
                "pet.activeIndex",
                String.valueOf(Math.max(0, Math.min(activeIndex, pets.size() - 1))));
        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            String prefix = "pet." + i + ".";
            properties.setProperty(prefix + "type", pet.getType().name());
            properties.setProperty(prefix + "name", pet.getName());
            properties.setProperty(prefix + "hunger", String.valueOf(pet.getHunger()));
            properties.setProperty(prefix + "mood", String.valueOf(pet.getMood()));
            properties.setProperty(prefix + "energy", String.valueOf(pet.getEnergy()));
            properties.setProperty(prefix + "careProgress", String.valueOf(pet.getCareProgress()));
            properties.setProperty(prefix + "neglectCounter", String.valueOf(pet.getNeglectCounter()));
            properties.setProperty(prefix + "evolved", String.valueOf(pet.isEvolved()));
            properties.setProperty(prefix + "sick", String.valueOf(pet.isSick()));
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                SAVE_FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {
            properties.store(writer, "flying pet state");
        } catch (IOException ex) {
            System.err.println("无法保存宠物存档：" + ex.getMessage());
        }
    }

    public static List<Pet> fromSnapshots(List<PetSnapshot> snapshots) {
        List<Pet> pets = new ArrayList<>();
        for (PetSnapshot snapshot : snapshots) {
            pets.add(new Pet(
                    snapshot.type(),
                    snapshot.name(),
                    snapshot.hunger(),
                    snapshot.mood(),
                    snapshot.energy(),
                    snapshot.careProgress(),
                    snapshot.neglectCounter(),
                    snapshot.evolved(),
                    snapshot.sick()));
        }
        return pets;
    }

    private static int readInt(Properties properties, String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private static boolean readBoolean(Properties properties, String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    private static Optional<PetSnapshot> readSnapshot(Properties properties, int index) {
        String prefix = "pet." + index + ".";
        String typeName = properties.getProperty(prefix + "type");
        if (typeName == null) {
            return Optional.empty();
        }
        PetType type = PetType.valueOf(typeName);
        String name = properties.getProperty(prefix + "name", type.getDefaultName());
        int hunger = readInt(properties, prefix + "hunger", type.getInitialHunger());
        int mood = readInt(properties, prefix + "mood", type.getInitialMood());
        int energy = readInt(properties, prefix + "energy", type.getInitialEnergy());
        int careProgress = readInt(properties, prefix + "careProgress", 0);
        int neglectCounter = readInt(properties, prefix + "neglectCounter", 0);
        boolean evolved = readBoolean(properties, prefix + "evolved", false);
        boolean sick = readBoolean(properties, prefix + "sick", false);
        return Optional.of(new PetSnapshot(
                type,
                name,
                hunger,
                mood,
                energy,
                careProgress,
                neglectCounter,
                evolved,
                sick));
    }

    private static Optional<PetSnapshot> loadLegacySnapshot(Properties properties) {
        String typeName = properties.getProperty("type");
        if (typeName == null) {
            return Optional.empty();
        }
        PetType type = PetType.valueOf(typeName);
        String name = properties.getProperty("name", type.getDefaultName());
        int hunger = readInt(properties, "hunger", type.getInitialHunger());
        int mood = readInt(properties, "mood", type.getInitialMood());
        int energy = readInt(properties, "energy", type.getInitialEnergy());
        int careProgress = readInt(properties, "careProgress", 0);
        int neglectCounter = readInt(properties, "neglectCounter", 0);
        boolean evolved = readBoolean(properties, "evolved", false);
        boolean sick = readBoolean(properties, "sick", false);
        return Optional.of(new PetSnapshot(
                type,
                name,
                hunger,
                mood,
                energy,
                careProgress,
                neglectCounter,
                evolved,
                sick));
    }
}
