package view.component;

import view.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class DialogFactory {

    public static ModernDialog createDialog(Window owner, String title, int width, int height) {
        return new ModernDialog(owner, title, width, height);
    }

    // Helper to add label/input pair
    public static void addFormItem(JPanel panel, String labelText, JComponent input) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Theme.TEXT_COLOR);
        label.setFont(Theme.FONT_REGULAR);

        panel.add(label);
        panel.add(input);
        panel.add(Box.createVerticalStrut(15));
    }

    public static void showMessage(Component parent, String message, String title) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        ModernDialog dialog = new ModernDialog(window, title, 400, 250);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Theme.BG_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea txtMsg = new JTextArea(message);
        txtMsg.setFont(Theme.FONT_REGULAR);
        txtMsg.setForeground(Theme.TEXT_COLOR);
        txtMsg.setBackground(Theme.BG_COLOR);
        txtMsg.setLineWrap(true);
        txtMsg.setWrapStyleWord(true);
        txtMsg.setEditable(false);
        txtMsg.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(txtMsg);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG_COLOR);
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        content.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Theme.BG_COLOR);
        ModernButton btnOK = new ModernButton("OK / 确认", true);
        btnOK.addActionListener(e -> dialog.dispose());
        btnPanel.add(btnOK);

        content.add(btnPanel, BorderLayout.SOUTH);
        dialog.setContent(content);
        dialog.setVisible(true);
    }

    public static boolean showConfirm(Component parent, String message, String title) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        ModernDialog dialog = new ModernDialog(window, title, 350, 180);
        final boolean[] result = { false };

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Theme.BG_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblMsg = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        lblMsg.setForeground(Theme.TEXT_COLOR);
        lblMsg.setFont(Theme.FONT_REGULAR);
        content.add(lblMsg, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Theme.BG_COLOR);

        ModernButton btnYes = new ModernButton("YES / 是");
        ModernButton btnNo = new ModernButton("NO / 否", true); // Default safe

        btnYes.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        btnNo.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        btnPanel.add(btnYes);
        btnPanel.add(btnNo);

        content.add(btnPanel, BorderLayout.SOUTH);
        dialog.setContent(content);
        dialog.setVisible(true);

        return result[0];
    }
}
