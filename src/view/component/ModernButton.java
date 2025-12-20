package view.component;

import view.theme.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private static final long serialVersionUID = 1L;
    private boolean isHovered = false;
    private boolean isActive = false;
    private Color hoverColor = Theme.ACCENT_COLOR;
    private Color normalColor = Theme.SIDEBAR_COLOR;

    public ModernButton(String text) {
        super(text);
        setFont(Theme.FONT_EN_TECH.deriveFont(16f)); // Use Blender/Tech font
        setForeground(Theme.TEXT_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    public ModernButton(String text, boolean primary) {
        this(text);
        if (primary) {
            normalColor = Theme.TECH_BLUE.darker().darker();
            hoverColor = Theme.TECH_BLUE;
        }
    }

    public void setActive(boolean active) {
        this.isActive = active;
        if (active) {
            setForeground(Theme.ACCENT_COLOR);
        } else {
            setForeground(Theme.TEXT_COLOR);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        if (isHovered || isActive) {
            // Use alpha for hover/active bg or hoverColor
            if (hoverColor == Theme.TECH_BLUE) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(new Color(Theme.ACCENT_COLOR.getRed(), Theme.ACCENT_COLOR.getGreen(),
                        Theme.ACCENT_COLOR.getBlue(), 40));
            }
        } else {
            g2.setColor(normalColor);
        }

        g2.fillRect(0, 0, getWidth(), getHeight());

        // Decoration: Left Bar
        if (isHovered || isActive) {
            g2.setColor(hoverColor == Theme.TECH_BLUE ? Color.WHITE : Theme.ACCENT_COLOR);
            g2.fillRect(0, 0, 4, getHeight());

            // Text color handling
            if (hoverColor == Theme.TECH_BLUE)
                setForeground(Color.BLACK);
            else if (isActive)
                setForeground(Theme.ACCENT_COLOR);
            else
                setForeground(Theme.TEXT_COLOR); // Hover only

            // Actually, setForeground triggers repaint, might flicker if done in paint.
            // Rely on isActive/isHovered logic for text color in paint if possible, or just
            // set it outside.
            // For simplicity, let's just draw text color manually or assume setForeground
            // was called.
        } else {
            if (!isActive)
                setForeground(Theme.TEXT_COLOR);
        }

        super.paintComponent(g);
    }
}
