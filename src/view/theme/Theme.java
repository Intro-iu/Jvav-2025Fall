package view.theme;

import java.awt.*;

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
        // 1. Initialize with defaults first (Fail-safe)
        useDefaults();

        try {
            // 2. Try load custom fonts from classpath
            Font title = loadFont("/fonts/fz-yasong.ttf", 24f);
            if (title != null)
                FONT_TITLE = title;

            Font regular = loadFont("/fonts/source-han-sans.otf", 14f);
            if (regular != null)
                FONT_REGULAR = regular;

            Font enTitle = loadFont("/fonts/novecento.otf", 20f);
            if (enTitle != null)
                FONT_EN_TITLE = enTitle.deriveFont(Font.BOLD, 20f);

            Font enTech = loadFont("/fonts/bender.otf", 12f);
            if (enTech != null)
                FONT_EN_TECH = enTech;

            // Register to GraphicsEnv to ensure Swing can use them if needed
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            if (FONT_TITLE != null)
                ge.registerFont(FONT_TITLE);
            if (FONT_REGULAR != null)
                ge.registerFont(FONT_REGULAR);
            if (FONT_EN_TITLE != null)
                ge.registerFont(FONT_EN_TITLE);
            if (FONT_EN_TECH != null)
                ge.registerFont(FONT_EN_TECH);

        } catch (Exception e) {
            System.err.println("Failed to load custom fonts, keeping defaults.");
            e.printStackTrace();
        }
    }

    private static Font loadFont(String path, float size) {
        try (java.io.InputStream is = Theme.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Font not found: " + path);
                return null;
            }
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void useDefaults() {
        FONT_TITLE = new Font("SimHei", Font.BOLD, 24);
        FONT_REGULAR = new Font("Microsoft YaHei", Font.PLAIN, 14);
        FONT_EN_TITLE = new Font("Impact", Font.PLAIN, 20);
        FONT_EN_TECH = new Font("Consolas", Font.PLAIN, 12);
    }
}
