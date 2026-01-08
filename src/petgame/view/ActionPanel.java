package petgame.view;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import petgame.controller.GameController;

public class ActionPanel extends JPanel {
    private final GameController controller;
    private final Runnable onActionPerformed;

    public ActionPanel(GameController controller, Runnable onActionPerformed) {
        this.controller = controller;
        this.onActionPerformed = onActionPerformed;
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        createButtons();
    }

    private void createButtons() {
        JButton feedButton = new JButton("喂食");
        feedButton.addActionListener(e -> {
            controller.feedPet();
            onActionPerformed.run();
        });
        
        JButton playButton = new JButton("玩耍");
        playButton.addActionListener(e -> {
            controller.playWithPet();
            onActionPerformed.run();
        });
        
        JButton sleepButton = new JButton("睡觉");
        sleepButton.addActionListener(e -> {
            controller.letPetSleep();
            onActionPerformed.run();
        });

        add(feedButton);
        add(playButton);
        add(sleepButton);
    }
}
