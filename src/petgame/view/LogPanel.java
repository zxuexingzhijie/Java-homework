package petgame.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import petgame.controller.GameController;

public class LogPanel extends JPanel {
    private final JLabel feedCountLabel = new JLabel();
    private final JLabel playCountLabel = new JLabel();
    private final JLabel sleepCountLabel = new JLabel();

    public LogPanel(GameController controller) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("操作日志"));
        
        var logger = controller.getLogger();
        JList<String> logList = new JList<>(logger.getModel());
        logList.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(logList);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel counterPanel = new JPanel(new GridLayout(3, 1));
        counterPanel.add(feedCountLabel);
        counterPanel.add(playCountLabel);
        counterPanel.add(sleepCountLabel);
        add(counterPanel, BorderLayout.SOUTH);
    }

    public void updateCounts(GameController controller) {
        var logger = controller.getLogger();
        feedCountLabel.setText("喂食次数：" + logger.getFeedCount());
        playCountLabel.setText("玩耍次数：" + logger.getPlayCount());
        sleepCountLabel.setText("睡觉次数：" + logger.getSleepCount());
    }
}
