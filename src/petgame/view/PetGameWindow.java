package petgame.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import petgame.controller.GameController;
import petgame.model.Pet;

public class PetGameWindow extends JFrame {

    private static final int TIMER_INTERVAL_MS = 3500;

    private final GameController controller;
    private final PetPanel petPanel = new PetPanel();
    private final StatusPanel statusPanel;
    private final ActionPanel actionPanel;
    private final PetSelectorPanel petSelectorPanel;
    private final LogPanel logPanel;
    private final Timer decayTimer;

    public PetGameWindow(GameController controller) {
        super("电子宠物 - 会飞的小龙");
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        statusPanel = new StatusPanel();
        actionPanel = new ActionPanel(controller, this::updateStatus);
        petSelectorPanel = new PetSelectorPanel(controller, this::updateStatus);
        logPanel = new LogPanel(controller);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        petSelectorPanel.refresh();
        pack();
        setLocationRelativeTo(null);
        updateStatus();

        decayTimer = new Timer(TIMER_INTERVAL_MS, e -> {
            controller.tickPet();
            updateStatus();
        });
        decayTimer.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                decayTimer.stop();
            }
        });
    }

    private JPanel createTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.add(petSelectorPanel, BorderLayout.NORTH);
        top.add(statusPanel, BorderLayout.SOUTH);
        return top;
    }

    private JPanel createCenterPanel() {
        JPanel root = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, petPanel, logPanel);
        splitPane.setResizeWeight(0.65);
        root.add(splitPane, BorderLayout.CENTER);
        return root;
    }

    private void updateStatus() {
        Pet pet = controller.getPet();
        petPanel.setPet(pet);
        statusPanel.updateStatus(pet);
        logPanel.updateCounts(controller);
        petSelectorPanel.syncSelection();
    }
}
