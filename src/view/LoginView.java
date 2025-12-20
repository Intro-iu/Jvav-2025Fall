package view;

import view.component.ModernButton;
import view.component.ModernPasswordField;
import view.component.ModernTextField;
import view.component.DialogFactory;
import view.component.PatternPanel;
import view.theme.Theme;
import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();

    public LoginView() {
        setTitle("LOGIN - NEWS SYSTEM");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Main Panel with Dot Pattern
        PatternPanel rootPanel = new PatternPanel(PatternPanel.Pattern.POINTS);
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBorder(BorderFactory.createLineBorder(Theme.ACCENT_COLOR, 2));

        // Drag Handler
        MouseDragHandler dragHandler = new MouseDragHandler(this);
        rootPanel.addMouseListener(dragHandler);
        rootPanel.addMouseMotionListener(dragHandler);

        // Settings Button (Top Right)
        ModernButton btnSettings = new ModernButton("SET");
        btnSettings.setPreferredSize(new Dimension(60, 30));
        btnSettings.setFont(Theme.FONT_EN_TECH);
        btnSettings.addActionListener(e -> new view.component.ConfigDialog(this).setVisible(true));

        GridBagConstraints gbcSet = new GridBagConstraints();
        gbcSet.gridx = 0;
        gbcSet.gridy = 0;
        gbcSet.weightx = 1.0;
        gbcSet.weighty = 1.0;
        gbcSet.anchor = GridBagConstraints.NORTHEAST;
        gbcSet.insets = new Insets(10, 0, 0, 10);
        rootPanel.add(btnSettings, gbcSet);

        add(rootPanel);

        // Center Login Form
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        // Logo / Title
        JLabel lblTitle = new JLabel("SYSTEM LOGIN");
        lblTitle.setFont(Theme.FONT_EN_TITLE.deriveFont(28f));
        lblTitle.setForeground(Theme.ACCENT_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(lblTitle);

        loginPanel.add(Box.createVerticalStrut(40));

        // Username
        JLabel lblUser = new JLabel("USERNAME:");
        lblUser.setForeground(Theme.TEXT_DIM_COLOR);
        lblUser.setFont(Theme.FONT_EN_TECH);
        loginPanel.add(createAlignedPanel(lblUser));

        ModernTextField txtUsername = new ModernTextField();
        // Input cleared
        loginPanel.add(txtUsername);

        loginPanel.add(Box.createVerticalStrut(20));

        // Password
        JLabel lblPass = new JLabel("PASSWORD:");
        lblPass.setForeground(Theme.TEXT_DIM_COLOR);
        lblPass.setFont(Theme.FONT_EN_TECH);
        loginPanel.add(createAlignedPanel(lblPass));

        ModernPasswordField txtPassword = new ModernPasswordField();
        // Input cleared
        loginPanel.add(txtPassword);

        loginPanel.add(Box.createVerticalStrut(40));

        // Buttons
        ModernButton btnLogin = new ModernButton("ENTER SYSTEM", true);
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            User user = userService.login(username, password);
            if (user != null) {
                new MainView(user).setVisible(true);
                this.dispose();
            } else {
                DialogFactory.showMessage(this, "INVALID CREDENTIALS / 用户名或密码错误", "LOGIN FAILED");
            }
        });

        ModernButton btnExit = new ModernButton("EXIT");
        btnExit.addActionListener(e -> System.exit(0));

        loginPanel.add(btnLogin);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(btnExit);

        GridBagConstraints gbcLogin = new GridBagConstraints();
        gbcLogin.gridx = 0;
        gbcLogin.gridy = 0;
        gbcLogin.weightx = 1.0;
        gbcLogin.weighty = 1.0;
        gbcLogin.anchor = GridBagConstraints.CENTER;
        rootPanel.add(loginPanel, gbcLogin);
    }

    private JPanel createAlignedPanel(JComponent c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.add(c);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return p;
    }

    // Internal Drag Handler
    private static class MouseDragHandler extends MouseAdapter {
        private Point mouseDownCompCoords = null;
        private JFrame frame;

        public MouseDragHandler(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
}