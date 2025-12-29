package view;

import model.Category;
import service.CategoryService;
import view.component.*;
import view.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CategoryView extends JPanel {
    private static final long serialVersionUID = 1L;
    private JPanel listPanel;
    private CategoryService categoryService = new CategoryService();

    public CategoryView() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Title & Add
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Theme.BG_COLOR);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel lblTitle = new JLabel("CATEGORY MANAGEMENT");
        lblTitle.setFont(Theme.FONT_EN_TITLE);
        lblTitle.setForeground(Theme.ACCENT_COLOR);

        ModernButton btnAdd = new ModernButton("+ ADD CATEGORY", true);
        btnAdd.setPreferredSize(new Dimension(150, 30));
        btnAdd.addActionListener(e -> openEditDialog(null));

        pnlTop.add(lblTitle, BorderLayout.WEST);
        pnlTop.add(btnAdd, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);

        // Center Panel: List
        listPanel = new ScrollablePanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.BG_COLOR);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBackground(Theme.BG_COLOR);
        scrollPane.getViewport().setBackground(Theme.BG_COLOR);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar vsb = scrollPane.getVerticalScrollBar();
        vsb.setBackground(Theme.BG_COLOR);
        vsb.setUI(new ModernScrollBarUI());

        add(scrollPane, BorderLayout.CENTER);

        loadCategories();
    }

    private void loadCategories() {
        listPanel.removeAll();
        List<Category> list = categoryService.findAll();

        if (list.isEmpty()) {
            JLabel lblEmpty = new JLabel("NO CATEGORIES FOUND");
            lblEmpty.setFont(Theme.FONT_EN_TECH);
            lblEmpty.setForeground(Theme.TEXT_DIM_COLOR);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblEmpty.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            listPanel.add(lblEmpty);
        } else {
            for (Category c : list) {
                CategoryCard card = new CategoryCard(c,
                        e -> openEditDialog(c),
                        e -> deleteCategory(c));
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(5));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void deleteCategory(Category c) {
        boolean confirm = DialogFactory.showConfirm(this,
                "Delete category '" + c.getName() + "'?\nWarning: This might fail if news exists in this category.",
                "CONFIRM DELETE");

        if (confirm) {
            if (categoryService.delete(c.getId())) {
                DialogFactory.showMessage(this, "Deleted Successfully", "SUCCESS");
                loadCategories();
            } else {
                DialogFactory.showMessage(this, "Delete Failed. Category might be in use.", "ERROR");
            }
        }
    }

    private void openEditDialog(Category category) {
        JDialog dialog = DialogFactory.createDialog(SwingUtilities.getWindowAncestor(this),
                category == null ? "ADD CATEGORY" : "EDIT CATEGORY", 350, 200);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Theme.BG_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ModernTextField txtName = new ModernTextField();
        if (category != null) {
            txtName.setText(category.getName());
        }

        DialogFactory.addFormItem(centerPanel, "NAME / 名称:", txtName);
        dialog.add(centerPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Theme.BG_COLOR);

        ModernButton btnSave = new ModernButton("SAVE", true);
        ModernButton btnCancel = new ModernButton("CANCEL");

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                DialogFactory.showMessage(dialog, "Name cannot be empty", "WARNING");
                return;
            }

            boolean success;
            if (category == null) {
                success = categoryService.add(name);
            } else {
                success = categoryService.update(category.getId(), name);
            }

            if (success) {
                DialogFactory.showMessage(dialog, "Saved Successfully", "SUCCESS");
                dialog.dispose();
                loadCategories();
            } else {
                DialogFactory.showMessage(dialog, "Save Failed", "ERROR");
            }
        });

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
