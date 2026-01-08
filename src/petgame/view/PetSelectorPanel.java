package petgame.view;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import petgame.controller.GameController;
import petgame.enums.PetType;
import petgame.model.Pet;

public class PetSelectorPanel extends JPanel {
    private final GameController controller;
    private final DefaultComboBoxModel<Pet> petSelectorModel = new DefaultComboBoxModel<>();
    private final JComboBox<Pet> petSelector = new JComboBox<>(petSelectorModel);
    private boolean updatingSelector;
    private final Runnable onSelectionChanged;

    public PetSelectorPanel(GameController controller, Runnable onSelectionChanged) {
        this.controller = controller;
        this.onSelectionChanged = onSelectionChanged;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("宠物选择"));
        createComponents();
    }

    private void createComponents() {
        add(new JLabel("当前宠物："));
        petSelector.addActionListener(e -> {
            if (updatingSelector) {
                return;
            }
            controller.selectPet(petSelector.getSelectedIndex());
            onSelectionChanged.run();
        });
        add(petSelector);
        
        JButton adoptButton = new JButton("新增宠物");
        adoptButton.addActionListener(e -> showAdoptDialog());
        add(adoptButton);
    }

    public void refresh() {
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

    public void syncSelection() {
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
        JPanel panel = new JPanel(new java.awt.GridLayout(2, 2, 8, 6));
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
            refresh();
            onSelectionChanged.run();
        }
    }
}
