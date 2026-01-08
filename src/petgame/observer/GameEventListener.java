package petgame.observer;

public interface GameEventListener {
    void onEvent(GameEvent event, Object data);
}
