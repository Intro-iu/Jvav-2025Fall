package view.component;

import view.theme.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ModernTextField extends JTextField {
    private boolean isFocused = false;

    public ModernTextField() {
        this(null);
    }

    public ModernTextField(String text) {
        super(text);
        setOpaque(false); // Transparent background
        setForeground(Theme.TEXT_COLOR);
        setCaretColor(Theme.ACCENT_COLOR);
        setFont(Theme.FONT_REGULAR.deriveFont(16f));
        setBorder(new EmptyBorder(5, 5, 5, 5)); // Padding for text

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background (Optional, if we want it slightly darker than transparent)
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g);

        // Underline
        if (isFocused) {
            g2.setColor(Theme.ACCENT_COLOR);
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(1));
        }
        g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
}
