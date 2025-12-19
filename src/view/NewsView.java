package view;

import model.Category;
import model.News;
import model.PageResult;
import model.User;
import service.CategoryService;
import service.NewsService;
import view.component.*;
import view.theme.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NewsView extends JPanel {
    private IndustrialTable table;
    private DefaultTableModel tableModel;
    private ModernTextField txtSearchTitle;
    private ModernComboBox<Category> cmbSearchCategory;
    private JLabel lblPageInfo;
    private ModernButton btnPrev;
    private ModernButton btnNext;

    private NewsService newsService = new NewsService();
    private CategoryService categoryService = new CategoryService();
    private User currentUser;

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPage = 0;
    private List<News> currentList;

    public NewsView(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Search
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.setBackground(Theme.BG_COLOR);

        JLabel lblTitle = new JLabel("标题:");
        lblTitle.setForeground(Theme.TEXT_COLOR);
        lblTitle.setFont(Theme.FONT_REGULAR);
        pnlTop.add(lblTitle);

        txtSearchTitle = new ModernTextField();
        txtSearchTitle.setColumns(15);
        pnlTop.add(txtSearchTitle);

        JLabel lblCat = new JLabel("分类:");
        lblCat.setForeground(Theme.TEXT_COLOR);
        lblCat.setFont(Theme.FONT_REGULAR);
        pnlTop.add(lblCat);

        cmbSearchCategory = new ModernComboBox<>();
        cmbSearchCategory.addItem(new Category(0, "所有分类"));
        loadCategories();
        pnlTop.add(cmbSearchCategory);

        ModernButton btnSearch = new ModernButton("SEARCH / 查询", true);
        btnSearch.addActionListener(e -> {
            currentPage = 1;
            loadNews();
        });
        pnlTop.add(btnSearch);

        add(pnlTop, BorderLayout.NORTH);

        // Center Panel: Table
        String[] columnNames = { "ID", "标题", "分类", "作者", "发布时间" };
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

        // Bottom Panel: Actions + Pagination
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(Theme.BG_COLOR);
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Pagination Controls
        JPanel pnlPager = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlPager.setBackground(Theme.BG_COLOR);

        btnPrev = new ModernButton("< PREV");
        btnNext = new ModernButton("NEXT >");
        lblPageInfo = new JLabel("PAGE 1 / 1");
        lblPageInfo.setForeground(Theme.TEXT_DIM_COLOR);
        lblPageInfo.setFont(Theme.FONT_EN_TECH);

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadNews();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentPage < totalPage) {
                currentPage++;
                loadNews();
            }
        });

        pnlPager.add(btnPrev);
        pnlPager.add(lblPageInfo);
        pnlPager.add(btnNext);
        pnlBottom.add(pnlPager, BorderLayout.WEST);

        // Operation Buttons
        JPanel pnlOps = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlOps.setBackground(Theme.BG_COLOR);

        ModernButton btnAdd = new ModernButton("ADD");
        ModernButton btnEdit = new ModernButton("EDIT");
        ModernButton btnDelete = new ModernButton("DELETE");
        ModernButton btnRefresh = new ModernButton("REFRESH");

        btnAdd.addActionListener(e -> openEditDialog(null));
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                openEditDialog(id);
            } else {
                DialogFactory.showMessage(this, "Please select a news item.", "INFO");
            }
        });
        btnDelete.addActionListener(e -> deleteNews());
        btnRefresh.addActionListener(e -> refresh());

        pnlOps.add(btnAdd);
        pnlOps.add(btnEdit);
        pnlOps.add(btnDelete);
        pnlOps.add(btnRefresh);

        pnlBottom.add(pnlOps, BorderLayout.EAST);

        add(pnlBottom, BorderLayout.SOUTH);

        // Initial Load
        loadNews();
    }

    private void loadCategories() {
        Category first = cmbSearchCategory.getItemAt(0);
        cmbSearchCategory.removeAllItems();
        cmbSearchCategory.addItem(first);

        List<Category> list = categoryService.findAll();
        for (Category c : list) {
            cmbSearchCategory.addItem(c);
        }
    }

    private void loadNews() {
        tableModel.setRowCount(0);
        String title = txtSearchTitle.getText().trim();
        Category selectedCat = (Category) cmbSearchCategory.getSelectedItem();
        int catId = selectedCat != null ? selectedCat.getId() : 0;

        PageResult<News> pageResult = newsService.findPage(title, catId, currentPage, pageSize);
        currentList = pageResult.getList();
        totalPage = pageResult.getTotalPage();
        if (totalPage == 0)
            totalPage = 1;

        for (News n : currentList) {
            tableModel.addRow(new Object[] {
                    n.getId(), n.getTitle(), n.getCategoryName(), n.getAuthorName(), n.getPublishTime()
            });
        }

        lblPageInfo.setText("PAGE " + currentPage + " / " + totalPage + " (TOTAL " + pageResult.getTotalCount() + ")");
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPage);
    }

    private void deleteNews() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            DialogFactory.showMessage(this, "Please select an item to delete.", "INFO");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        boolean confirm = DialogFactory.showConfirm(this, "Confirm Delete? / 确认删除？", "CONFIRM");
        if (confirm) {
            if (newsService.delete(id)) {
                DialogFactory.showMessage(this, "Deleted Successfully", "SUCCESS");
                loadNews();
            } else {
                DialogFactory.showMessage(this, "Delete Failed", "ERROR");
            }
        }
    }

    private void openEditDialog(Integer newsId) {
        JDialog dialog = DialogFactory.createDialog(SwingUtilities.getWindowAncestor(this),
                newsId == null ? "PUBLISH" : "EDIT", 500, 450);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Theme.BG_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ModernTextField txtTitle = new ModernTextField();
        JTextArea txtContent = new JTextArea(5, 20);
        // Simple standardization for TextArea
        txtContent.setBackground(Theme.SIDEBAR_COLOR);
        txtContent.setForeground(Theme.TEXT_COLOR);
        txtContent.setCaretColor(Theme.ACCENT_COLOR);
        txtContent.setLineWrap(true);
        txtContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Padding

        ModernComboBox<Category> cmbCategory = new ModernComboBox<>();

        List<Category> categories = categoryService.findAll();
        for (Category c : categories)
            cmbCategory.addItem(c);

        DialogFactory.addFormItem(centerPanel, "TITLE / 标题:", txtTitle);
        DialogFactory.addFormItem(centerPanel, "CATEGORY / 分类:", cmbCategory);

        // Custom wrap for content
        JLabel lblCo = new JLabel("CONTENT / 内容:");
        lblCo.setForeground(Theme.TEXT_COLOR);
        lblCo.setFont(Theme.FONT_REGULAR);
        centerPanel.add(lblCo);
        centerPanel.add(new JScrollPane(txtContent));

        dialog.add(centerPanel, BorderLayout.CENTER);

        if (newsId != null && currentList != null) {
            for (News n : currentList) {
                if (n.getId() == newsId) {
                    txtTitle.setText(n.getTitle());
                    txtContent.setText(n.getContent());
                    for (int i = 0; i < cmbCategory.getItemCount(); i++) {
                        Category c = cmbCategory.getItemAt(i);
                        if (c.getId() == n.getCategoryId()) {
                            cmbCategory.setSelectedIndex(i);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Theme.BG_COLOR);
        ModernButton btnSave = new ModernButton("SAVE", true);
        ModernButton btnCancel = new ModernButton("CANCEL");

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            String title = txtTitle.getText();
            String content = txtContent.getText();
            Category cat = (Category) cmbCategory.getSelectedItem();

            if (title.isEmpty()) {
                DialogFactory.showMessage(dialog, "Title cannot be empty", "WARNING");
                return;
            }

            boolean success;
            if (newsId == null) {
                success = newsService.add(title, content, cat.getId(), currentUser.getId());
            } else {
                success = newsService.update(newsId, title, content, cat.getId());
            }

            if (success) {
                DialogFactory.showMessage(dialog, "Saved Successfully", "SUCCESS");
                dialog.dispose();
                loadNews();
            } else {
                DialogFactory.showMessage(dialog, "Save Failed", "ERROR");
            }
        });

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public void refresh() {
        loadCategories();
        currentPage = 1;
        loadNews();
    }
}
