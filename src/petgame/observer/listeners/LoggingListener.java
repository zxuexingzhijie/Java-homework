package petgame.observer.listeners;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.DefaultListModel;
import petgame.observer.GameEvent;
import petgame.observer.GameEventListener;
import petgame.model.Pet;

public class LoggingListener implements GameEventListener {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    private final DefaultListModel<String> logModel = new DefaultListModel<>();
    private int feedCount;
    private int playCount;
    private int sleepCount;

    public DefaultListModel<String> getModel() {
        return logModel;
    }

    public int getFeedCount() {
        return feedCount;
    }

    public int getPlayCount() {
        return playCount;
    }

    public int getSleepCount() {
        return sleepCount;
    }

    @Override
    public void onEvent(GameEvent event, Object data) {
        switch (event) {
            case PET_FED -> {
                feedCount++;
                if (data instanceof Pet pet) {
                    addEntry("[" + now() + "] 喂食了 " + pet.getName());
                }
            }
            case PET_PLAYED -> {
                playCount++;
                if (data instanceof Pet pet) {
                    addEntry("[" + now() + "] 陪 " + pet.getName() + " 玩耍");
                }
            }
            case PET_SLEPT -> {
                sleepCount++;
                if (data instanceof Pet pet) {
                    addEntry("[" + now() + "] 让 " + pet.getName() + " 睡觉");
                }
            }
            case SYSTEM_MESSAGE -> {
                if (data instanceof String message) {
                    addEntry("[" + now() + "] " + message);
                }
            }
            default -> {
                // 其他事件不需要记录
            }
        }
    }

    private String now() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    private void addEntry(String entry) {
        logModel.addElement(entry);
        if (logModel.size() > 100) {
            logModel.remove(0);
        }
    }
}
