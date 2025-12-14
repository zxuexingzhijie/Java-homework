package petgame.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import petgame.controller.GameController;
import petgame.enums.PetType;
import petgame.model.Pet;
import petgame.util.ActionLogger;


public class PetGameWindow extends JFrame {

    private static final int TIMER_INTERVAL_MS = 3500;

    private final GameController controller;
    private final PetPanel petPanel = new PetPanel();
    private final JProgressBar hungerBar = progressBar("饱食度");
    private final JProgressBar moodBar = progressBar("心情值");
    private final JProgressBar energyBar = progressBar("体力值");
    private final JLabel statusLabel = new JLabel("准备开始");
    private final JLabel feedCountLabel = new JLabel();
    private final JLabel playCountLabel = new JLabel();
    private final JLabel sleepCountLabel = new JLabel();
    private final DefaultComboBoxModel<Pet> petSelectorModel = new DefaultComboBoxModel<>();
    private final JComboBox<Pet> petSelector = new JComboBox<>(petSelectorModel);
    private final Timer decayTimer;
    private boolean updatingSelector;

    public PetGameWindow(GameController controller) {
        super("电子宠物 - 会飞的小龙");
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
        refreshPetSelector();
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
        top.add(createAdoptionPanel(), BorderLayout.NORTH);
        top.add(createStatusPanel(), BorderLayout.SOUTH);
        return top;
    }

    private JPanel createAdoptionPanel() {
        JPanel adoptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adoptionPanel.setBorder(BorderFactory.createTitledBorder("宠物选择"));
        adoptionPanel.add(new JLabel("当前宠物："));
        petSelector.addActionListener(e -> {
            if (updatingSelector) {
                return;
            }
            controller.selectPet(petSelector.getSelectedIndex());
            updateStatus();
        });
        adoptionPanel.add(petSelector);
        JButton adoptButton = new JButton("新增宠物");
        adoptButton.addActionListener(e -> showAdoptDialog());
        adoptionPanel.add(adoptButton);
        return adoptionPanel;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new GridLayout(3, 1, 6, 4));
        statusPanel.setBorder(BorderFactory.createTitledBorder("状态面板"));
        statusPanel.add(hungerBar);
        statusPanel.add(moodBar);
        statusPanel.add(energyBar);
        return statusPanel;
    }

    private JPanel createCenterPanel() {
        JPanel root = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, petPanel, createLogPanel());
        splitPane.setResizeWeight(0.65);
        root.add(splitPane, BorderLayout.CENTER);
        return root;
    }

    private JPanel createLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("操作日志"));
        ActionLogger logger = controller.getLogger();
        JList<String> logList = new JList<>(logger.getModel());
        logList.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(logList);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel counterPanel = new JPanel(new GridLayout(3, 1));
        counterPanel.add(feedCountLabel);
        counterPanel.add(playCountLabel);
        counterPanel.add(sleepCountLabel);
        logPanel.add(counterPanel, BorderLayout.SOUTH);
        return logPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton feedButton = new JButton("喂食");
        feedButton.addActionListener(e -> {
            controller.feedPet();
            updateStatus();
        });
        JButton playButton = new JButton("玩耍");
        playButton.addActionListener(e -> {
            controller.playWithPet();
            updateStatus();
        });
        JButton sleepButton = new JButton("睡觉");
        sleepButton.addActionListener(e -> {
            controller.letPetSleep();
            updateStatus();
        });

        buttonPanel.add(feedButton);
        buttonPanel.add(playButton);
        buttonPanel.add(sleepButton);

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 16f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        controlPanel.add(statusLabel, BorderLayout.SOUTH);
        return controlPanel;
    }

    private void showAdoptDialog() {
        JComboBox<PetType> typeBox = new JComboBox<>(PetType.values());
        JTextField nameField = new JTextField(12);
        typeBox.setSelectedItem(controller.getPet().getType());
        PetType selectedType = (PetType) typeBox.getSelectedItem();
        nameField.setText(selectedType != null ? selectedType.getDefaultName() : "");
        typeBox.addActionListener(e -> {
            PetType newType = (PetType) typeBox.getSelectedItem();
            if (newType != null && (nameField.getText() == null || nameField.getText().isBlank())) {
                nameField.setText(newType.getDefaultName());
            }
        });
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 6));
        panel.add(new JLabel("宠物类型："));
        panel.add(typeBox);
        panel.add(new JLabel("宠物名字："));
        panel.add(nameField);
        int result = JOptionPane.showConfirmDialog(this, panel, "领养新宠物", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            PetType type = (PetType) typeBox.getSelectedItem();
            if (type == null) {
                return;
            }
            String inputName = nameField.getText();
            String finalName = (inputName == null || inputName.isBlank()) ? type.getDefaultName() : inputName.trim();
            controller.adoptPet(type, finalName);
            refreshPetSelector();
            updateStatus();
        }
    }

    private void updateStatus() {
        Pet pet = controller.getPet();
        petPanel.setPet(pet);
        hungerBar.setValue(pet.getHunger());
        moodBar.setValue(pet.getMood());
        energyBar.setValue(pet.getEnergy());
        hungerBar.setString(String.format("饱食度：%d", pet.getHunger()));
        moodBar.setString(String.format("心情：%d", pet.getMood()));
        energyBar.setString(String.format("体力：%d", pet.getEnergy()));

        statusLabel.setText(pet.statusMessage());
        if (pet.isSick()) {
            statusLabel.setForeground(new Color(192, 57, 43));
        } else if (pet.isEvolved()) {
            statusLabel.setForeground(new Color(243, 156, 18));
        } else if (pet.isHungry() || pet.isBored() || pet.isSleepy()) {
            statusLabel.setForeground(new Color(192, 57, 43));
        } else {
            statusLabel.setForeground(new Color(39, 174, 96));
        }

        ActionLogger logger = controller.getLogger();
        feedCountLabel.setText("喂食次数：" + logger.getFeedCount());
        playCountLabel.setText("玩耍次数：" + logger.getPlayCount());
        sleepCountLabel.setText("睡觉次数：" + logger.getSleepCount());
        syncPetSelectorSelection();
    }

    private static JProgressBar progressBar(String text) {
        JProgressBar bar = new JProgressBar(0, Pet.MAX_VALUE);
        bar.setStringPainted(true);
        bar.setForeground(new Color(52, 152, 219));
        bar.setString(text);
        return bar;
    }

    private void refreshPetSelector() {
        updatingSelector = true;
        petSelectorModel.removeAllElements();
        for (Pet pet : controller.getPets()) {
            petSelectorModel.addElement(pet);
        }
        int index = controller.getActivePetIndex();
        if (index >= 0 && index < petSelectorModel.getSize()) {
            petSelector.setSelectedIndex(index);
        }
        updatingSelector = false;
    }

    private void syncPetSelectorSelection() {
        if (petSelectorModel.getSize() == 0) {
            return;
        }
        updatingSelector = true;
        int index = controller.getActivePetIndex();
        if (index >= 0 && index < petSelectorModel.getSize()) {
            petSelector.setSelectedIndex(index);
        }
        updatingSelector = false;
    }
}
