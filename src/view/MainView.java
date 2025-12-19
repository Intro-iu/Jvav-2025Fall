package view;

import view.component.ModernButton;
import view.component.PatternPanel;
import view.theme.Theme;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainView extends JFrame {
    private JPanel centerPanel;
    private CardLayout cardLayout;
    // private User currentUser; // unused, kept for reference or removed
    private List<ModernButton> navButtons = new ArrayList<>();

    public MainView(User user) {
        setTitle("新闻信息管理系统 - Jvav 2025");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Root Panel - Dark Background
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Theme.BG_COLOR);
        setContentPane(rootPanel);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Logo Area
        JLabel lblLogo = new JLabel("NEWS SYS");
        lblLogo.setFont(Theme.FONT_EN_TITLE);
        lblLogo.setForeground(Theme.ACCENT_COLOR);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);

        JLabel lblSub = new JLabel("VER 1.0");
        lblSub.setFont(Theme.FONT_EN_TECH);
        lblSub.setForeground(Theme.TEXT_DIM_COLOR);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblSub);

        sidebar.add(Box.createVerticalStrut(40));

        // Navigation Buttons
        ModernButton btnHome = createSideButton("HOME / 首页");
        ModernButton btnNews = createSideButton("NEWS / 新闻");
        ModernButton btnCat = createSideButton("CATEGORY / 分类");
        ModernButton btnExit = createSideButton("EXIT / 退出");

        navButtons.add(btnHome);
        navButtons.add(btnNews);
        navButtons.add(btnCat);

        sidebar.add(btnHome);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnNews);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnCat);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnExit);

        rootPanel.add(sidebar, BorderLayout.WEST);

        // Center Content Area
        // Use PatternPanel as background container
        PatternPanel contentWrapper = new PatternPanel(PatternPanel.Pattern.RECTANGLES);
        contentWrapper.setLayout(new BorderLayout());
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margin

        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
        centerPanel.setOpaque(false); // Transparent to show pattern

        // Welcome View
        PatternPanel welcomePanel = new PatternPanel(PatternPanel.Pattern.DOTS);
        welcomePanel.setLayout(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("WELCOME, " + user.getNickname().toUpperCase());
        welcomeLabel.setFont(Theme.FONT_TITLE.deriveFont(32f));
        welcomeLabel.setForeground(Theme.TEXT_COLOR);
        welcomePanel.add(welcomeLabel);

        // News View
        NewsView newsView = new NewsView(user);
        // Category View
        CategoryView categoryView = new CategoryView();

        centerPanel.add(welcomePanel, "WELCOME");
        centerPanel.add(newsView, "NEWS");
        centerPanel.add(categoryView, "CATEGORY");

        contentWrapper.add(centerPanel, BorderLayout.CENTER);
        rootPanel.add(contentWrapper, BorderLayout.CENTER);

        // Actions
        btnHome.addActionListener(e -> {
            cardLayout.show(centerPanel, "WELCOME");
            updateNavState(btnHome);
        });
        btnNews.addActionListener(e -> {
            newsView.refresh();
            cardLayout.show(centerPanel, "NEWS");
            updateNavState(btnNews);
        });
        btnCat.addActionListener(e -> {
            cardLayout.show(centerPanel, "CATEGORY");
            updateNavState(btnCat);
        });
        btnExit.addActionListener(e -> System.exit(0));

        // Initial state
        updateNavState(btnHome);
    }

    private ModernButton createSideButton(String text) {
        ModernButton btn = new ModernButton(text);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void updateNavState(ModernButton activeBtn) {
        for (ModernButton btn : navButtons) {
            btn.setActive(btn == activeBtn);
        }
    }
}