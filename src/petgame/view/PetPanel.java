package petgame.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.Timer;
import petgame.model.Pet;


public class PetPanel extends JPanel {

    private static final int BODY_WIDTH = 120;
    private static final int BODY_HEIGHT = 70;

    private final Timer flyTimer;
    private Pet pet;
    private int petX = 60;
    private int petY = 60;
    private int velocity = 3;

    public PetPanel() {
        setPreferredSize(new Dimension(420, 260));
        setBackground(new Color(225, 245, 254));
        flyTimer = new Timer(35, e -> animate());
        flyTimer.start();
    }

    public void setPet(Pet pet) {
        this.pet = pet;
        petX = Math.max(40, getWidth() / 2 - BODY_WIDTH / 2);
        petY = Math.max(40, getHeight() / 2 - BODY_HEIGHT / 2);
        repaint();
    }

    private void animate() {
        if (pet == null) {
            return;
        }
        int max = Math.max(20, getWidth() - BODY_WIDTH - 20);
        petX += velocity;
        if (petX < 20 || petX > max) {
            velocity = -velocity;
            petX += velocity;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawClouds(g2);
        if (pet == null) {
            drawPlaceholder(g2);
        } else {
            drawPet(g2);
        }
        g2.dispose();
    }

    private void drawPet(Graphics2D g2) {
        Color bodyColor = evaluateBodyColor();
        g2.setColor(bodyColor);
        g2.fillOval(petX, petY, BODY_WIDTH, BODY_HEIGHT);

        if (pet.isEvolved()) {
            g2.setColor(new Color(bodyColor.getRed(), bodyColor.getGreen(), bodyColor.getBlue(), 110));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(petX - 8, petY - 6, BODY_WIDTH + 16, BODY_HEIGHT + 12);
        }
        if (pet.isSick()) {
            g2.setColor(new Color(231, 76, 60, 160));
            g2.setStroke(new BasicStroke(4f));
            g2.drawLine(petX + 15, petY + 15, petX + BODY_WIDTH - 15, petY + BODY_HEIGHT - 15);
            g2.drawLine(petX + 15, petY + BODY_HEIGHT - 15, petX + BODY_WIDTH - 15, petY + 15);
        }

        g2.setColor(bodyColor.brighter());
        g2.fillOval(petX - 30, petY + 10, 60, 30);
        g2.fillOval(petX + BODY_WIDTH - 30, petY + 10, 60, 30);

        g2.setColor(Color.WHITE);
        g2.fillOval(petX + 25, petY + 20, 18, 18);
        g2.fillOval(petX + 70, petY + 20, 18, 18);
        g2.setColor(Color.BLACK);
        g2.fillOval(petX + 31, petY + 26, 6, 6);
        g2.fillOval(petX + 76, petY + 26, 6, 6);

        g2.setStroke(new BasicStroke(3));
        g2.setColor(pet.isBored() ? Color.ORANGE.darker() : Color.PINK.darker());
        if (pet.isBored()) {
            g2.drawArc(petX + 40, petY + 40, 40, 18, 15, 150);
        } else {
            g2.drawArc(petX + 40, petY + 35, 40, 25, 200, 140);
        }
    }

    private Color evaluateBodyColor() {
        if (pet == null) {
            return Color.LIGHT_GRAY;
        }
        Color base = pet.getType().getBaseColor();
        if (pet.isSick()) {
            return new Color(149, 165, 166);
        }
        if (pet.isHungry()) {
            return new Color(231, 76, 60);
        }
        if (pet.isSleepy()) {
            return new Color(base.getRed(), base.getGreen(), base.getBlue(), 160);
        }
        if (pet.isBored()) {
            return base.brighter();
        }
        if (pet.isEvolved()) {
            return base.brighter().brighter();
        }
        return base;
    }

    private void drawClouds(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillOval(40, 30, 80, 30);
        g2.fillOval(80, 20, 90, 40);
        g2.fillOval(getWidth() - 140, 60, 100, 35);
        g2.fillOval(getWidth() - 100, 40, 90, 30);
    }

    private void drawPlaceholder(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
        g2.drawString("请选择一个宠物开始游戏", 60, getHeight() / 2);
    }
}
