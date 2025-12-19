package view.component;

import view.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class PatternPanel extends JPanel {
    public enum Pattern {
        NONE, DOTS, SLASHES, RECTANGLES
    }

    private Pattern pattern = Pattern.NONE;
    private Color patternColor = new Color(255, 255, 255, 10); // Subtle transparent white

    public PatternPanel() {
        setOpaque(true);
        setBackground(Theme.BG_COLOR);
    }

    public PatternPanel(Pattern pattern) {
        this();
        this.pattern = pattern;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (pattern == Pattern.NONE)
            return;

        g2.setColor(patternColor);
        int w = getWidth();
        int h = getHeight();

        switch (pattern) {
            case NONE:
                break;
            case DOTS:
                int gap = 20;
                for (int x = 0; x < w; x += gap) {
                    for (int y = 0; y < h; y += gap) {
                        g2.fillOval(x, y, 2, 2);
                    }
                }
                break;
            case SLASHES:
                int space = 30;
                for (int i = -h; i < w; i += space) {
                    g2.setStroke(new BasicStroke(1));
                    g2.drawLine(i, 0, i + h, h);
                }
                break;
            case RECTANGLES:
                // Random tech rects or grid? Let's do a corner decoration grid
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(w - 100, h - 100, 80, 80);
                g2.drawRect(w - 90, h - 90, 60, 60);
                // Some random fills
                g2.setColor(new Color(Theme.ACCENT_COLOR.getRed(), Theme.ACCENT_COLOR.getGreen(),
                        Theme.ACCENT_COLOR.getBlue(), 30));
                g2.fillRect(20, 20, 100, 20);
                g2.fillRect(20, 50, 60, 10);
                break;
        }
    }
}
