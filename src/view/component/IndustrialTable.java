package view.component;

import view.theme.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IndustrialTable extends JTable {

    private int hoverRow = -1;

    public IndustrialTable(TableModel dm) {
        super(dm);
        initStyle();
        initHover();
    }

    private void initStyle() {
        setBackground(Theme.SIDEBAR_COLOR);
        setForeground(Theme.TEXT_COLOR);
        setSelectionBackground(Theme.ACCENT_COLOR); // Cyan/Yellow
        setSelectionForeground(Color.BLACK); // Text becomes black on accent

        // Remove borders/spacing for seamless look
        setShowVerticalLines(false);
        setShowHorizontalLines(true); // Keep rows distinct
        setGridColor(new Color(50, 50, 50)); // Subtle divider
        setIntercellSpacing(new Dimension(0, 0)); // Crucial for "modern" feel
        setFocusable(false); // Remove "dashed box" on cell focus

        setFont(Theme.FONT_REGULAR);
        setRowHeight(40); // Comfortable height

        // Header styling
        JTableHeader header = getTableHeader();
        header.setBackground(Theme.BG_COLOR);
        header.setForeground(Theme.TEXT_COLOR);
        header.setFont(Theme.FONT_TITLE.deriveFont(14f));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.ACCENT_COLOR));
        header.setReorderingAllowed(false);

        // Header Alignment
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private void initHover() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != hoverRow) {
                    hoverRow = row;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoverRow = -1;
                repaint();
            }
        });
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);

        // Logic: Selected > Hovered > Alternating/Normal
        if (isRowSelected(row)) {
            c.setBackground(Theme.ACCENT_COLOR);
            c.setForeground(Color.BLACK);
        } else if (row == hoverRow) {
            // Hover color: Slightly lighter than Sidebar, or a tint of Accent
            // Let's use a very dark version of accent or just a lighter grey
            c.setBackground(new Color(60, 60, 60));
            c.setForeground(Theme.TEXT_COLOR);
        } else {
            // Alternating
            if (row % 2 == 0) {
                c.setBackground(Theme.SIDEBAR_COLOR);
            } else {
                c.setBackground(new Color(38, 38, 38)); // Slightly different dark
            }
            c.setForeground(Theme.TEXT_COLOR);
        }

        // Padding
        if (c instanceof JComponent) {
            ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        }

        return c;
    }
}
