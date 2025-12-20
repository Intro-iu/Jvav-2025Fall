package view.component;

import view.theme.Theme;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class ModernComboBox<E> extends JComboBox<E> {
    private static final long serialVersionUID = 1L;

    public ModernComboBox() {
        setUI(new ModernComboBoxUI());
        setRenderer(new ModernListCellRenderer());
        setBackground(Theme.SIDEBAR_COLOR);
        setForeground(Theme.TEXT_COLOR);
        setFont(Theme.FONT_REGULAR);
    }

    private class ModernComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton btn = new JButton() {
                @Override
                public void paint(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.SIDEBAR_COLOR);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    // Arrow
                    g2.setColor(Theme.ACCENT_COLOR);
                    int[] xPoints = { getWidth() / 2 - 4, getWidth() / 2 + 4, getWidth() / 2 };
                    int[] yPoints = { getHeight() / 2 - 2, getHeight() / 2 - 2, getHeight() / 2 + 3 };
                    g2.fillPolygon(xPoints, yPoints, 3);
                }
            };
            btn.setBorder(BorderFactory.createEmptyBorder());
            return btn;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(Theme.SIDEBAR_COLOR);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    private class ModernListCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (isSelected) {
                setBackground(Theme.ACCENT_COLOR);
                setForeground(Color.BLACK);
            } else {
                setBackground(Theme.SIDEBAR_COLOR);
                setForeground(Theme.TEXT_COLOR);
            }
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return this;
        }
    }
}
