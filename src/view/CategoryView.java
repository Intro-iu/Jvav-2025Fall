package view;

import model.Category;
import service.CategoryService;
import view.component.*;
import view.theme.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryView extends JPanel {
    private IndustrialTable table;
    private DefaultTableModel tableModel;
    private CategoryService categoryService = new CategoryService();

    public CategoryView() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Center Panel: Table
        String[] columnNames = { "ID", "名称" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new IndustrialTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Theme.BG_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.SIDEBAR_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Actions
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(Theme.BG_COLOR);
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        ModernButton btnAdd = new ModernButton("ADD");
        ModernButton btnEdit = new ModernButton("EDIT");
        ModernButton btnDelete = new ModernButton("DELETE");
        ModernButton btnRefresh = new ModernButton("REFRESH");

        btnAdd.addActionListener(e -> openEditDialog(null));
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 1);
                openEditDialog(new Category(id, name));
            } else {
                DialogFactory.showMessage(this, "Please select a category.", "INFO");
            }
        });
        btnDelete.addActionListener(e -> deleteCategory());
        btnRefresh.addActionListener(e -> loadCategories());

        pnlBottom.add(btnAdd);
        pnlBottom.add(btnEdit);
        pnlBottom.add(btnDelete);
        pnlBottom.add(btnRefresh);

        add(pnlBottom, BorderLayout.SOUTH);

        // Initial Load
        loadCategories();
    }

    private void loadCategories() {
        tableModel.setRowCount(0);
        List<Category> list = categoryService.findAll();
        for (Category c : list) {
            tableModel.addRow(new Object[] { c.getId(), c.getName() });
        }
    }

    private void deleteCategory() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            DialogFactory.showMessage(this, "Please select a category to delete.", "INFO");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        boolean confirm = DialogFactory.showConfirm(this,
                "Delete category '" + name + "'?\nWarning: This might fail if news exists in this category.",
                "CONFIRM DELETE");

        if (confirm) {
            if (categoryService.delete(id)) {
                DialogFactory.showMessage(this, "Deleted Successfully", "SUCCESS");
                loadCategories();
            } else {
                DialogFactory.showMessage(this, "Delete Failed. Category might be in use.", "ERROR");
            }
        }
    }

    private void openEditDialog(Category category) {
        JDialog dialog = DialogFactory.createDialog(SwingUtilities.getWindowAncestor(this),
                category == null ? "ADD CATEGORY" : "EDIT CATEGORY", 300, 200);

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
