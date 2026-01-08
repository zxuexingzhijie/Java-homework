package petgame.view;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import petgame.model.Pet;

public class StatusPanel extends JPanel {
    private final JProgressBar hungerBar = progressBar("饱食度");
    private final JProgressBar moodBar = progressBar("心情值");
    private final JProgressBar energyBar = progressBar("体力值");
    private final JLabel statusLabel = new JLabel("准备开始");

    public StatusPanel() {
        setLayout(new GridLayout(4, 1, 6, 4));
        setBorder(BorderFactory.createTitledBorder("状态面板"));
        add(hungerBar);
        add(moodBar);
        add(energyBar);
        
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        add(statusLabel);
    }

    public void updateStatus(Pet pet) {
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
    }

    private static JProgressBar progressBar(String text) {
        JProgressBar bar = new JProgressBar(0, Pet.MAX_VALUE);
        bar.setStringPainted(true);
        bar.setForeground(new Color(52, 152, 219));
        bar.setString(text);
        return bar;
    }
}
