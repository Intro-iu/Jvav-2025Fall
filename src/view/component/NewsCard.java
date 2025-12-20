package view.component;

import model.News;
import view.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewsCard extends JPanel {
    private static final long serialVersionUID = 1L;
    private News news;

    public NewsCard(News news, ActionListener onEdit, ActionListener onDelete) {
        this.news = news;

        setLayout(new BorderLayout());
        setBackground(Theme.SIDEBAR_COLOR);
        setBorder(new EmptyBorder(0, 0, 10, 0));

        // Main Card Container
        JPanel cardInfo = new JPanel(new BorderLayout());
        cardInfo.setBackground(Theme.SIDEBAR_COLOR);
        cardInfo.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Accent Bar (Left)
        JPanel accentBar = new JPanel();
        accentBar.setBackground(Theme.ACCENT_COLOR);
        accentBar.setPreferredSize(new Dimension(4, 0));
        add(accentBar, BorderLayout.WEST);

        // Header: Title + Time
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.SIDEBAR_COLOR);

        JLabel lblTitle = new JLabel(news.getTitle());
        lblTitle.setFont(Theme.FONT_TITLE.deriveFont(20f));
        lblTitle.setForeground(Theme.TEXT_COLOR);

        JLabel lblTime = new JLabel(news.getPublishTime() != null ? news.getPublishTime().toString() : "");
        lblTime.setFont(Theme.FONT_EN_TECH);
        lblTime.setForeground(Theme.TEXT_DIM_COLOR);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(lblTime, BorderLayout.EAST);

        // Metadata Line (Mixed Fonts)
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        metaPanel.setBackground(Theme.SIDEBAR_COLOR);
        metaPanel.setBorder(new EmptyBorder(5, 0, 10, 0));

        JLabel lblCatKey = new JLabel("CATEGORY: ");
        lblCatKey.setFont(Theme.FONT_EN_TECH.deriveFont(10f));
        lblCatKey.setForeground(Theme.TECH_BLUE);

        JLabel lblCatVal = new JLabel(news.getCategoryName());
        lblCatVal.setFont(Theme.FONT_REGULAR.deriveFont(11f)); // Use Regular for potential Chinese
        lblCatVal.setForeground(Theme.TECH_BLUE);

        JLabel lblSep = new JLabel("  /  AUTHOR: ");
        lblSep.setFont(Theme.FONT_EN_TECH.deriveFont(10f));
        lblSep.setForeground(Theme.TECH_BLUE);

        JLabel lblAuthVal = new JLabel(news.getAuthorName());
        lblAuthVal.setFont(Theme.FONT_REGULAR.deriveFont(11f));
        lblAuthVal.setForeground(Theme.TECH_BLUE);

        metaPanel.add(lblCatKey);
        metaPanel.add(lblCatVal);
        metaPanel.add(lblSep);
        metaPanel.add(lblAuthVal);

        // Abstract / Content Snippet
        String content = news.getContent();
        if (content != null && content.length() > 150) {
            content = content.substring(0, 150) + "...";
        }
        JTextArea txtAbstract = new JTextArea(content);
        txtAbstract.setWrapStyleWord(true);
        txtAbstract.setLineWrap(true);
        txtAbstract.setOpaque(false);
        txtAbstract.setEditable(false);
        txtAbstract.setFocusable(false);
        txtAbstract.setFont(Theme.FONT_REGULAR);
        txtAbstract.setForeground(Theme.TEXT_DIM_COLOR);
        txtAbstract.setBorder(null);

        // Assemble Center
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Theme.SIDEBAR_COLOR);
        centerPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(Theme.SIDEBAR_COLOR);
        bodyPanel.add(metaPanel, BorderLayout.NORTH); // Changed from lblMeta to metaPanel
        bodyPanel.add(txtAbstract, BorderLayout.CENTER);

        centerPanel.add(bodyPanel, BorderLayout.CENTER);

        // Actions Panel (Right Side)
        JPanel pnlActions = new JPanel();
        pnlActions.setLayout(new BoxLayout(pnlActions, BoxLayout.Y_AXIS));
        pnlActions.setBackground(Theme.SIDEBAR_COLOR);
        pnlActions.setBorder(new EmptyBorder(0, 15, 0, 0));

        ModernButton btnEdit = new ModernButton("EDIT");
        btnEdit.setPreferredSize(new Dimension(80, 30));
        btnEdit.addActionListener(onEdit);

        ModernButton btnDelete = new ModernButton("DEL");
        btnDelete.setPreferredSize(new Dimension(80, 30));
        // Use a slightly different color for delete? keeping consistent for now
        btnDelete.addActionListener(onDelete);

        pnlActions.add(btnEdit);
        pnlActions.add(Box.createVerticalStrut(10));
        pnlActions.add(btnDelete);

        cardInfo.add(centerPanel, BorderLayout.CENTER);
        cardInfo.add(pnlActions, BorderLayout.EAST);

        add(cardInfo, BorderLayout.CENTER);

        // Wrapper for external margin
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)), // Separator
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // Interactions
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                repaint();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                Color hoverColor = new Color(50, 50, 50);
                cardInfo.setBackground(hoverColor);
                headerPanel.setBackground(hoverColor);
                centerPanel.setBackground(hoverColor);
                bodyPanel.setBackground(hoverColor);
                metaPanel.setBackground(hoverColor); // Add this
                pnlActions.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                repaint();
                setCursor(Cursor.getDefaultCursor());
                cardInfo.setBackground(Theme.SIDEBAR_COLOR);
                headerPanel.setBackground(Theme.SIDEBAR_COLOR);
                centerPanel.setBackground(Theme.SIDEBAR_COLOR);
                bodyPanel.setBackground(Theme.SIDEBAR_COLOR);
                metaPanel.setBackground(Theme.SIDEBAR_COLOR); // Add this
                pnlActions.setBackground(Theme.SIDEBAR_COLOR);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Only show detail if not clicked on buttons (Buttons consume events usually,
                // but just in case)
                showDetail();
            }
        };

        // Add listener to container, but buttons will handle their own clicks
        cardInfo.addMouseListener(ma);
        txtAbstract.addMouseListener(ma);
    }

    private void showDetail() {
        JDialog dialog = DialogFactory.createDialog(SwingUtilities.getWindowAncestor(this), "NEWS DETAIL", 600, 500);

        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(Theme.BG_COLOR);
        pnlContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Title Area
        JPanel pnlHead = new JPanel(new BorderLayout());
        pnlHead.setBackground(Theme.BG_COLOR);
        pnlHead.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblT = new JLabel("<html>" + news.getTitle() + "</html>");
        lblT.setFont(Theme.FONT_TITLE.deriveFont(28f));
        lblT.setForeground(Theme.ACCENT_COLOR);

        JLabel lblM = new JLabel("Category: " + news.getCategoryName() + " | Author: " + news.getAuthorName() + " | "
                + news.getPublishTime());
        lblM.setFont(Theme.FONT_EN_TECH);
        lblM.setForeground(Theme.TEXT_DIM_COLOR);

        pnlHead.add(lblT, BorderLayout.CENTER);
        pnlHead.add(lblM, BorderLayout.SOUTH);

        // Body
        JTextArea txtFull = new JTextArea(news.getContent());
        txtFull.setLineWrap(true);
        txtFull.setWrapStyleWord(true);
        txtFull.setEditable(false);
        txtFull.setBackground(Theme.BG_COLOR);
        txtFull.setForeground(Theme.TEXT_COLOR);
        txtFull.setFont(Theme.FONT_REGULAR.deriveFont(16f));
        txtFull.setBorder(null);

        JScrollPane scroll = new JScrollPane(txtFull);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_COLOR);

        // Custom scrollbar for detail view too
        JScrollBar vsb = scroll.getVerticalScrollBar();
        vsb.setBackground(Theme.BG_COLOR);
        vsb.setUI(new ModernScrollBarUI());

        pnlContent.add(pnlHead, BorderLayout.NORTH);
        pnlContent.add(scroll, BorderLayout.CENTER);

        dialog.add(pnlContent);
        dialog.setVisible(true);
    }
}
