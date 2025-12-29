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
import java.awt.*;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class HomeView extends JPanel {
    private static final long serialVersionUID = 1L;
    private JPanel feedPanel;
    private ModernTextField txtSearchTitle;
    private ModernTextField txtStartDate;
    private ModernTextField txtEndDate;
    private ModernComboBox<Category> cmbSearchCategory;
    private JLabel lblPageInfo;
    private ModernButton btnPrev;
    private ModernButton btnNext;

    private NewsService newsService = new NewsService();
    private CategoryService categoryService = new CategoryService();
    private User currentUser;

    private int currentPage = 1;
    private int pageSize = 5;
    private int totalPage = 0;

    public HomeView(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Theme.BG_COLOR);

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.setBackground(Theme.BG_COLOR);

        JLabel lblTitle = new JLabel("SEARCH:");
        lblTitle.setForeground(Theme.TEXT_COLOR);
        lblTitle.setFont(Theme.FONT_EN_TECH);
        row1.add(lblTitle);

        txtSearchTitle = new ModernTextField();
        txtSearchTitle.setColumns(20);
        row1.add(txtSearchTitle);

        cmbSearchCategory = new ModernComboBox<>();
        cmbSearchCategory.addItem(new Category(0, "ALL CATEGORIES"));
        loadCategories();
        row1.add(cmbSearchCategory);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.setBackground(Theme.BG_COLOR);

        JLabel lblStart = new JLabel("START(Y-M-D):");
        lblStart.setForeground(Theme.TEXT_COLOR);
        lblStart.setFont(Theme.FONT_EN_TECH);
        row2.add(lblStart);

        txtStartDate = new ModernTextField();
        txtStartDate.setColumns(8);
        row2.add(txtStartDate);

        JLabel lblEnd = new JLabel("END:");
        lblEnd.setForeground(Theme.TEXT_COLOR);
        lblEnd.setFont(Theme.FONT_EN_TECH);
        row2.add(lblEnd);

        txtEndDate = new ModernTextField();
        txtEndDate.setColumns(8);
        row2.add(txtEndDate);

        ModernButton btnSearch = new ModernButton("GO", true);
        btnSearch.addActionListener(e -> {
            currentPage = 1;
            loadNews();
        });

        ModernButton btnClear = new ModernButton("CLEAR");
        btnClear.addActionListener(e -> {
            txtSearchTitle.setText("");
            txtStartDate.setText("");
            txtEndDate.setText("");
            if (cmbSearchCategory.getItemCount() > 0) {
                cmbSearchCategory.setSelectedIndex(0);
            }
            currentPage = 1;
            loadNews();
        });

        row2.add(btnSearch);
        row2.add(btnClear);

        pnlTop.add(row1);
        pnlTop.add(row2);

        add(pnlTop, BorderLayout.NORTH);

        feedPanel = new ScrollablePanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setBackground(Theme.BG_COLOR);
        feedPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JScrollPane scrollPane = new JScrollPane(feedPanel);
        scrollPane.setBackground(Theme.BG_COLOR);
        scrollPane.getViewport().setBackground(Theme.BG_COLOR);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar vsb = scrollPane.getVerticalScrollBar();
        vsb.setBackground(Theme.BG_COLOR);
        vsb.setUI(new ModernScrollBarUI());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(Theme.BG_COLOR);

        JPanel pnlPager = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlPager.setBackground(Theme.BG_COLOR);

        btnPrev = new ModernButton("PREV");
        btnNext = new ModernButton("NEXT");
        lblPageInfo = new JLabel("1 / 1");
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


        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlActions.setBackground(Theme.BG_COLOR);
        ModernButton btnAdd = new ModernButton("+ ADD NEWS / 发布新闻", true);
        btnAdd.setPreferredSize(new Dimension(200, 40));
        btnAdd.addActionListener(e -> openEditDialog(null));
        pnlActions.add(btnAdd);

        pnlBottom.add(pnlPager, BorderLayout.WEST);
        pnlBottom.add(pnlActions, BorderLayout.CENTER);

        add(pnlBottom, BorderLayout.SOUTH);

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

    public void refresh() {
        loadCategories();
        currentPage = 1;
        loadNews();
    }

    private void loadNews() {
        feedPanel.removeAll();

        String title = txtSearchTitle.getText().trim();
        Category selectedCat = (Category) cmbSearchCategory.getSelectedItem();
        int catId = selectedCat != null ? selectedCat.getId() : 0;

        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String s = txtStartDate.getText().trim();
            if (!s.isEmpty()) startDate = sdf.parse(s);
        } catch (Exception e) {}

        try {
            String eStr = txtEndDate.getText().trim();
            if (!eStr.isEmpty()) {
                Date d = sdf.parse(eStr);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.set(Calendar.HOUR_OF_DAY, 23);
                c.set(Calendar.MINUTE, 59);
                c.set(Calendar.SECOND, 59);
                endDate = c.getTime();
            }
        } catch (Exception e) {}

        PageResult<News> pageResult = newsService.findPage(title, catId, startDate, endDate, currentPage, pageSize);
        List<News> list = pageResult.getList();
        totalPage = pageResult.getTotalPage();
        if (totalPage == 0)
            totalPage = 1;

        if (list.isEmpty()) {
            JLabel lblEmpty = new JLabel("NO NEWS FOUND");
            lblEmpty.setFont(Theme.FONT_EN_TITLE);
            lblEmpty.setForeground(Theme.TEXT_DIM_COLOR);
            lblEmpty.setBorder(new javax.swing.border.EmptyBorder(50, 0, 0, 0));
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            feedPanel.add(lblEmpty);
        } else {
            for (News n : list) {
                NewsCard card = new NewsCard(n,
                        e -> openEditDialog(n),
                        e -> deleteNews(n)
                );
                feedPanel.add(card);
                feedPanel.add(Box.createVerticalStrut(10));
            }
        }

        lblPageInfo.setText(currentPage + " / " + totalPage);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPage);

        feedPanel.revalidate();
        feedPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollPane scroller = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, feedPanel);
            if (scroller != null)
                scroller.getVerticalScrollBar().setValue(0);
        });
    }

    private void deleteNews(News news) {
        boolean confirm = DialogFactory.showConfirm(this, "Delete '" + news.getTitle() + "'?", "CONFIRM DELETE");
        if (confirm) {
            if (newsService.delete(news.getId())) {
                DialogFactory.showMessage(this, "Deleted Successfully", "SUCCESS");
                loadNews();
            } else {
                DialogFactory.showMessage(this, "Delete Failed", "ERROR");
            }
        }
    }

    private void openEditDialog(News news) {
        String actionTitle = (news == null) ? "PUBLISH" : "EDIT";
        JDialog dialog = DialogFactory.createDialog(SwingUtilities.getWindowAncestor(this), actionTitle, 600, 500);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Theme.BG_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        ModernTextField txtTitle = new ModernTextField();
        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JTextArea txtContent = new JTextArea(8, 20);
        txtContent.setBackground(Theme.SIDEBAR_COLOR);
        txtContent.setForeground(Theme.TEXT_COLOR);
        txtContent.setCaretColor(Theme.ACCENT_COLOR);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        txtContent.setFont(Theme.FONT_REGULAR.deriveFont(16f));

        ModernComboBox<Category> cmbCategory = new ModernComboBox<>();
        List<Category> categories = categoryService.findAll();
        for (Category c : categories)
            cmbCategory.addItem(c);

        DialogFactory.addFormItem(centerPanel, "TITLE / 标题:", txtTitle);
        DialogFactory.addFormItem(centerPanel, "CATEGORY / 分类:", cmbCategory);

        JLabel lblCo = new JLabel("CONTENT / 内容:");
        lblCo.setForeground(Theme.TEXT_COLOR);
        lblCo.setFont(Theme.FONT_REGULAR);
        lblCo.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(lblCo);
        centerPanel.add(Box.createVerticalStrut(5));

        JScrollPane scrollContent = new JScrollPane(txtContent);
        scrollContent.setBorder(null);
        scrollContent.getVerticalScrollBar().setBackground(Theme.BG_COLOR);
        scrollContent.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        centerPanel.add(scrollContent);

        dialog.add(centerPanel, BorderLayout.CENTER);

        if (news != null) {
            txtTitle.setText(news.getTitle());
            txtContent.setText(news.getContent());
            for (int i = 0; i < cmbCategory.getItemCount(); i++) {
                Category c = cmbCategory.getItemAt(i);
                if (c.getId() == news.getCategoryId()) {
                    cmbCategory.setSelectedIndex(i);
                    break;
                }
            }
        }

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Theme.BG_COLOR);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ModernButton btnSave = new ModernButton("SAVE", true);
        ModernButton btnCancel = new ModernButton("CANCEL");

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            String t = txtTitle.getText();
            String c = txtContent.getText();
            Category cat = (Category) cmbCategory.getSelectedItem();

            if (t.isEmpty()) {
                DialogFactory.showMessage(dialog, "Title cannot be empty", "WARNING");
                return;
            }

            boolean success;
            if (news == null) {
                success = newsService.add(t, c, cat.getId(), currentUser.getId());
            } else {
                success = newsService.update(news.getId(), t, c, cat.getId());
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
}
