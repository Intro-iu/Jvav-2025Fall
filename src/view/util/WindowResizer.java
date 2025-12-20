package view.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowResizer extends MouseAdapter {
    private final JFrame frame;
    private final int borderThickness;
    private int direction;

    // Constants for resizing directions
    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 4;
    private static final int EAST = 8;

    public WindowResizer(JFrame frame, int borderThickness) {
        this.frame = frame;
        this.borderThickness = borderThickness;
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        direction = getDirection(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        direction = 0;
        frame.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int dir = getDirection(e.getPoint());
        Cursor cursor = getCursor(dir);
        frame.setCursor(cursor);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (direction == 0)
            return;

        Point p = e.getLocationOnScreen();
        Rectangle bounds = frame.getBounds();

        // Use frame location on screen, not event point which is relative to component
        // (and component might be moving/resizing)
        // Actually for resizing logic, calculating delta is essential.

        // This simple logic might be buggy if we don't handle coordinates carefully.
        // Let's use absolute coordinates.

        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        // Simple resizing logic
        if ((direction & EAST) != 0) {
            w = Math.max(p.x - x, 100);
        }
        if ((direction & SOUTH) != 0) {
            h = Math.max(p.y - y, 100);
        }

        // Expanding North or West changes origin, which is trickier with simple mouse
        // deltas
        // because the component moves under the mouse.
        // For simplicity and stability in this assignment, we might implemented South,
        // East, and South-East resizing only?
        // Full resizing is nice but error prone in raw Swing without a heavy library.
        // However, let's try standard behavior.

        if ((direction & WEST) != 0) {
            int deltaX = p.x - x;
            int newW = w - deltaX;
            if (newW > 100) {
                x += deltaX;
                w = newW;
            }
        }
        if ((direction & NORTH) != 0) {
            int deltaY = p.y - y;
            int newH = h - deltaY;
            if (newH > 100) {
                y += deltaY;
                h = newH;
            }
        }

        frame.setBounds(x, y, w, h);
        frame.validate();
    }

    private int getDirection(Point p) {
        int dir = 0;
        int w = frame.getWidth();
        int h = frame.getHeight();

        if (p.x < borderThickness)
            dir |= WEST;
        if (p.x > w - borderThickness)
            dir |= EAST;
        if (p.y < borderThickness)
            dir |= NORTH;
        if (p.y > h - borderThickness)
            dir |= SOUTH;

        return dir;
    }

    private Cursor getCursor(int dir) {
        switch (dir) {
            case NORTH:
                return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
            case SOUTH:
                return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
            case WEST:
                return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
            case EAST:
                return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
            case NORTH | WEST:
                return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
            case NORTH | EAST:
                return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
            case SOUTH | WEST:
                return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
            case SOUTH | EAST:
                return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
            default:
                return Cursor.getDefaultCursor();
        }
    }
}
