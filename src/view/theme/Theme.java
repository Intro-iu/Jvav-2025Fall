package view.theme;

import java.awt.*;
import java.io.File;

public class Theme {
    // Colors
    // Dark Gray Background
    public static final Color BG_COLOR = new Color(45, 45, 45);
    // Darker Sidebar
    public static final Color SIDEBAR_COLOR = new Color(30, 30, 30);
    // Industrial Yellow/Orange for Accent (High Contrast)
    public static final Color ACCENT_COLOR = new Color(242, 201, 76); // F2C94C
    // Cyan for tech accent
    public static final Color TECH_BLUE = new Color(0, 240, 255); // 00F0FF
    // Text Colors
    public static final Color TEXT_COLOR = new Color(240, 240, 240);
    public static final Color TEXT_DIM_COLOR = new Color(160, 160, 160);

    // Fonts
    public static Font FONT_TITLE; // FZYaSong
    public static Font FONT_REGULAR; // SourceHanSans
    public static Font FONT_EN_TITLE; // Novecento
    public static Font FONT_EN_TECH; // Bender

    static {
        loadFonts();
    }

    private static void loadFonts() {
        try {
            // Load custom fonts from local fonts directory
            // Note: In a real jar release, we'd use getResourceAsStream
            FONT_TITLE = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/方正准雅宋简体.TTF")).deriveFont(24f);
            FONT_REGULAR = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/SourceHanSansSC-Normal.otf"))
                    .deriveFont(14f);
            FONT_EN_TITLE = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/NOVECENTO-WIDE-NORMAL-2.OTF"))
                    .deriveFont(Font.BOLD, 20f);
            FONT_EN_TECH = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/JOVANNY LEMONAD - BENDER.OTF"))
                    .deriveFont(12f);

            // Register to GraphicsEnv just in case
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(FONT_TITLE);
            ge.registerFont(FONT_REGULAR);
            ge.registerFont(FONT_EN_TITLE);
            ge.registerFont(FONT_EN_TECH);

        } catch (Exception e) {
            System.err.println("Failed to load fonts, using defaults.");
            e.printStackTrace();
            // Fallbacks
            FONT_TITLE = new Font("SimHei", Font.BOLD, 24);
            FONT_REGULAR = new Font("Microsoft YaHei", Font.PLAIN, 14);
            FONT_EN_TITLE = new Font("Impact", Font.PLAIN, 20);
            FONT_EN_TECH = new Font("Consolas", Font.PLAIN, 12);
        }
    }
}
