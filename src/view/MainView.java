package view;

import view.component.ModernButton;
import view.component.PatternPanel;
import view.theme.Theme;
import view.util.WindowResizer;
import model.User;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel centerPanel;
    private CardLayout cardLayout;
    private List<ModernButton> navButtons = new ArrayList<>();
    private Point dragOffset;

    public MainView(User user) {
        setTitle("新闻信息管理系统");
        setUndecorated(true); // Remove system decorations
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // Enable resizing
        new WindowResizer(this, 8);

        // Root Panel - Dark Background + Border
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Theme.BG_COLOR);
        rootPanel.setBorder(new LineBorder(Theme.ACCENT_COLOR, 1)); // Thin accent border
        setContentPane(rootPanel);

        // Custom Title Bar
        JPanel titleBar = createTitleBar();
        rootPanel.add(titleBar, BorderLayout.NORTH);

        // Main Content Area (Below Title Bar)
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Sidebar Content
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
        ModernButton btnNews = createSideButton("NEWS / 新闻");
        ModernButton btnCat = createSideButton("CATEGORY / 分类");
        // We can keep an Exit button in sidebar or rely on TitleBar close.
        // Let's keep a "LOGOUT" in sidebar maybe? Or just keep it as is.
        // User asked for "Close" in title bar, sidebar Exit is redundant but harmless.

        navButtons.add(btnNews);
        navButtons.add(btnCat);

        sidebar.add(btnNews);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnCat);

        sidebar.add(Box.createVerticalGlue());

        // User Info
        JLabel lblUser = new JLabel("USER: " + user.getUsername());
        lblUser.setFont(Theme.FONT_EN_TECH);
        lblUser.setForeground(Theme.TEXT_DIM_COLOR);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblUser);
        sidebar.add(Box.createVerticalStrut(10));

        mainContainer.add(sidebar, BorderLayout.WEST);

        // Center Content Area
        PatternPanel contentWrapper = new PatternPanel(PatternPanel.Pattern.RECTANGLES);
        contentWrapper.setLayout(new BorderLayout());
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
        centerPanel.setOpaque(false);

        // Home View (Merged)
        HomeView homeView = new HomeView(user);

        // Category View
        CategoryView categoryView = new CategoryView();

        centerPanel.add(homeView, "NEWS");
        centerPanel.add(categoryView, "CATEGORY");

        contentWrapper.add(centerPanel, BorderLayout.CENTER);
        mainContainer.add(contentWrapper, BorderLayout.CENTER);

        rootPanel.add(mainContainer, BorderLayout.CENTER);

        // Actions
        btnNews.addActionListener(e -> {
            homeView.refresh();
            cardLayout.show(centerPanel, "NEWS");
            updateNavState(btnNews);
        });
        btnCat.addActionListener(e -> {
            cardLayout.show(centerPanel, "CATEGORY");
            updateNavState(btnCat);
        });

        // Initial state
        updateNavState(btnNews);
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Theme.SIDEBAR_COLOR);
        titleBar.setPreferredSize(new Dimension(800, 35));
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));

        // Left: Title
        JLabel lblTitle = new JLabel("  News System - 2025 Fall");
        lblTitle.setFont(Theme.FONT_EN_TECH.deriveFont(14f));
        lblTitle.setForeground(Theme.TEXT_DIM_COLOR);
        titleBar.add(lblTitle, BorderLayout.WEST);

        // Right: Window Controls
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlControls.setOpaque(false);

        ModernButton btnMin = new ModernButton("-");
        btnMin.setPreferredSize(new Dimension(50, 35));
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));

        ModernButton btnMax = new ModernButton("[ ]"); // ASCII Square
        btnMax.setPreferredSize(new Dimension(50, 35));
        btnMax.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
                btnMax.setText("[ ]");
                new WindowResizer(this, 8);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                btnMax.setText("]["); // ASCII Restore
            }
        });

        ModernButton btnClose = new ModernButton("X");
        btnClose.setPreferredSize(new Dimension(50, 35));
        // Make Close button red on hover?
        // For now standard style.
        btnClose.addActionListener(e -> System.exit(0));

        pnlControls.add(btnMin);
        pnlControls.add(btnMax);
        pnlControls.add(btnClose);

        titleBar.add(pnlControls, BorderLayout.EAST);

        // Drag Support
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                    dragOffset = e.getPoint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (getExtendedState() != JFrame.MAXIMIZED_BOTH && dragOffset != null) {
                    Point curr = e.getLocationOnScreen();
                    setLocation(curr.x - dragOffset.x, curr.y - dragOffset.y);
                }
            }
        };
        titleBar.addMouseListener(ma);
        titleBar.addMouseMotionListener(ma);

        return titleBar;
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