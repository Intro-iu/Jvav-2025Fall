package view.component;

import javax.swing.*;
import java.awt.*;

public class ScrollablePanel extends JPanel implements Scrollable {
    private static final long serialVersionUID = 1L;

    public ScrollablePanel() {
        super();
    }

    public ScrollablePanel(LayoutManager layout) {
        super(layout);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true; // Force width to match viewport
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false; // Allow height to grow (scrollable)
    }
}
