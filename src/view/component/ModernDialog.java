package view.component;

import view.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernDialog extends JDialog {
    private Point mouseDownCompCoords;

    public ModernDialog(Window owner, String title, int width, int height) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(width, height);
        setLocationRelativeTo(owner);

        // Root Panel with Border
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_COLOR);
        root.setBorder(BorderFactory.createLineBorder(Theme.ACCENT_COLOR, 2));
        setContentPane(root);

        // Title Bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Theme.SIDEBAR_COLOR);
        titleBar.setPreferredSize(new Dimension(width, 30));
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));

        JLabel lblTitle = new JLabel(" " + title);
        lblTitle.setFont(Theme.FONT_EN_TECH.deriveFont(14f));
        lblTitle.setForeground(Theme.TEXT_COLOR);
        titleBar.add(lblTitle, BorderLayout.WEST);

        // Close Button
        ModernButton btnClose = new ModernButton("X");
        btnClose.setPreferredSize(new Dimension(40, 30));
        btnClose.addActionListener(e -> dispose());
        titleBar.add(btnClose, BorderLayout.EAST);

        // Drag Support
        MouseAdapter dragHandler = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }

            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        };
        titleBar.addMouseListener(dragHandler);
        titleBar.addMouseMotionListener(dragHandler);

        root.add(titleBar, BorderLayout.NORTH);
    }

    // Helper to add content to the center
    public void setContent(JPanel panel) {
        add(panel, BorderLayout.CENTER);
    }
}
