package view.component;

import model.Category;
import view.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoryCard extends JPanel {
    private static final long serialVersionUID = 1L;

    public CategoryCard(Category category, ActionListener onEdit, ActionListener onDelete) {
        setLayout(new BorderLayout());
        setBackground(Theme.SIDEBAR_COLOR);
        setPreferredSize(new Dimension(0, 60)); // Fixed height, flexible width
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Left Accent
        JPanel accent = new JPanel();
        accent.setBackground(Theme.TECH_BLUE);
        accent.setPreferredSize(new Dimension(4, 0));
        add(accent, BorderLayout.WEST);

        // Content
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Theme.SIDEBAR_COLOR);
        center.setBorder(new EmptyBorder(0, 15, 0, 15));

        JLabel lblName = new JLabel(category.getName());
        lblName.setFont(Theme.FONT_TITLE.deriveFont(18f));
        lblName.setForeground(Theme.TEXT_COLOR);
        center.add(lblName, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        actions.setBackground(Theme.SIDEBAR_COLOR);

        ModernButton btnEdit = new ModernButton("EDIT");
        btnEdit.setPreferredSize(new Dimension(80, 26)); // Increased width
        btnEdit.addActionListener(onEdit);

        ModernButton btnDel = new ModernButton("DEL");
        btnDel.setPreferredSize(new Dimension(80, 26)); // Increased width
        btnDel.addActionListener(onDelete);

        actions.add(btnEdit);
        actions.add(btnDel);

        add(actions, BorderLayout.EAST);

        // Border
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));

        // Hover Effect
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(50, 50, 50));
                center.setBackground(new Color(50, 50, 50));
                actions.setBackground(new Color(50, 50, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Theme.SIDEBAR_COLOR);
                center.setBackground(Theme.SIDEBAR_COLOR);
                actions.setBackground(Theme.SIDEBAR_COLOR);
            }
        };
        addMouseListener(ma);
        center.addMouseListener(ma);
        actions.addMouseListener(ma);
    }
}
