package stockTrading;

import javax.swing.*;
import java.awt.*;

public class MenuButtons extends JButton {
    private Color bgColor;
    private int cornerRadius;

    public MenuButtons(String text, Color bgColor, int cornerRadius) {
        super(text);
        this.bgColor = bgColor;
        this.cornerRadius = cornerRadius;
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(bgColor.darker());
        } else {
            g.setColor(bgColor);
        }
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 50);
    }
}