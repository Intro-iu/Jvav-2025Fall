package view.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ModernScrollBarUI extends BasicScrollBarUI {
    private final int THUMB_SIZE = 6;

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(80, 80, 80, 150);
        this.trackColor = new Color(0, 0, 0, 0); // Transparent track
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        btn.setMinimumSize(new Dimension(0, 0));
        btn.setMaximumSize(new Dimension(0, 0));
        return btn;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setComposite(AlphaComposite.SrcOver);

        int x = thumbBounds.x;
        int y = thumbBounds.y;
        int w = thumbBounds.width;
        int h = thumbBounds.height;

        // Center the thumb
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            x += (w - THUMB_SIZE) / 2;
            w = THUMB_SIZE;
        } else {
            y += (h - THUMB_SIZE) / 2;
            h = THUMB_SIZE;
        }

        g2.setColor(thumbColor);
        g2.fillRoundRect(x, y, w, h, THUMB_SIZE, THUMB_SIZE);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // Do nothing for transparent track
    }
}
