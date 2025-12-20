package view.component;

import util.ConfigUtil;
import util.DBUtil;
import view.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.util.Properties;

public class ConfigDialog extends ModernDialog {
    private static final long serialVersionUID = 1L;

    private ModernTextField txtUrl;
    private ModernTextField txtUser;
    private ModernPasswordField txtPass;

    public ConfigDialog(Window owner) {
        super(owner, "DB SETTINGS / 数据库设置", 500, 400);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_COLOR);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        Properties props = ConfigUtil.loadProps();

        // URL
        addLabel(content, "DATABASE URL:");
        txtUrl = new ModernTextField(
                props.getProperty("db.url", ""));
        txtUrl.setCaretPosition(0);
        content.add(txtUrl);
        content.add(Box.createVerticalStrut(15));

        // User
        addLabel(content, "USERNAME:");
        txtUser = new ModernTextField(props.getProperty("db.username", ""));
        content.add(txtUser);
        content.add(Box.createVerticalStrut(15));

        // Pass
        addLabel(content, "PASSWORD:");
        txtPass = new ModernPasswordField();
        txtPass.setText(props.getProperty("db.password", ""));
        content.add(txtPass);
        content.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);

        ModernButton btnTest = new ModernButton("TEST CONNECTION");
        btnTest.addActionListener(e -> testConnection());

        ModernButton btnSave = new ModernButton("SAVE");
        btnSave.addActionListener(e -> saveConfig());

        btnPanel.add(btnTest);
        btnPanel.add(btnSave);

        content.add(btnPanel);

        add(content);
    }

    private void addLabel(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_EN_TECH);
        l.setForeground(Theme.TEXT_DIM_COLOR);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Wrap in left-aligned panel
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        wrapper.setOpaque(false);
        wrapper.add(l);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        p.add(wrapper);
    }

    private void testConnection() {
        // Temporarily save to memory or just try to connect with simple JDBC call
        String url = txtUrl.getText();
        String u = txtUser.getText();
        String p = new String(txtPass.getPassword());

        try {
            // Attempt raw connection
            Connection conn = java.sql.DriverManager.getConnection(url, u, p);
            if (conn != null) {
                DialogFactory.showMessage(this, "CONNECTION SUCCESSFUL!", "SUCCESS");
                conn.close();
            }
        } catch (Exception e) {
            DialogFactory.showMessage(this, "CONNECTION FAILED:\n" + e.getMessage(), "ERROR");
        }
    }

    private void saveConfig() {
        String url = txtUrl.getText();
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        ConfigUtil.saveProps(url, user, pass);
        DBUtil.reloadConfig(); // Update runtime DBUtil

        DialogFactory.showMessage(this, "CONFIGURATION SAVED!", "SUCCESS");
        dispose();
    }
}
