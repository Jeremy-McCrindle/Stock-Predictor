package stockTrading;

import javax.swing.*;
import java.awt.*;

public class EnterButtons extends JButton {
    private Color bgColor;
    private int cornerRadius;

    public EnterButtons(String text, Color bgColor, int cornerRadius) {
        super(text);
        this.bgColor = bgColor;
        this.cornerRadius = cornerRadius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(bgColor); // Set foreground color to match background color
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Add padding to the button
        setFont(new Font("Arial", Font.PLAIN, 14)); // Set font
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
        // Small border to give a clean look
        g.setColor(getForeground().darker());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150, 30);
    }
}
