package petgame.observer;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private final List<GameEventListener> listeners = new ArrayList<>();

    public void subscribe(GameEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unsubscribe(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(GameEvent event, Object data) {
        for (GameEventListener listener : listeners) {
            listener.onEvent(event, data);
        }
    }

    public void publish(GameEvent event) {
        publish(event, null);
    }
}
