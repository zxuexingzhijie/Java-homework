package petgame;

import javax.swing.SwingUtilities;
import petgame.controller.GameController;
import petgame.view.PetGameWindow;


public final class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController();
            PetGameWindow window = new PetGameWindow(controller);
            window.setVisible(true);
        });
    }
}
