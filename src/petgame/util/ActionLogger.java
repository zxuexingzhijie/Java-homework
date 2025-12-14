package petgame.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.DefaultListModel;


public class ActionLogger {

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

    public void logFeed(String petName) {
        feedCount++;
        addEntry("[" + now() + "] 喂食了 " + petName);
    }

    public void logPlay(String petName) {
        playCount++;
        addEntry("[" + now() + "] 陪 " + petName + " 玩耍");
    }

    public void logSleep(String petName) {
        sleepCount++;
        addEntry("[" + now() + "] 让 " + petName + " 睡觉");
    }

    public void logSystem(String message) {
        addEntry("[" + now() + "] " + message);
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
