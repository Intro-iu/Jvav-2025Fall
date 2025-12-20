package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigUtil {
    private static Properties props = new Properties();
    private static final String CONFIG_FILE_NAME = "db.properties";

    // Paths to try writing to for persistence
    // 1. src/db.properties (Development source)
    // 2. bin/db.properties (Runtime classpath for immediate effect)
    private static final String SRC_PATH = "src/" + CONFIG_FILE_NAME;
    private static final String BIN_PATH = "bin/" + CONFIG_FILE_NAME;

    public static Properties loadProps() {
        props = new Properties();
        try (InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (in != null) {
                props.load(in);
            } else {
                // Fallback 1: Local file system (root)
                File f = new File(CONFIG_FILE_NAME);
                if (f.exists()) {
                    try (FileInputStream fis = new FileInputStream(f)) {
                        props.load(fis);
                    }
                } else {
                    // Fallback 2: src directory (Dev environment)
                    File fSrc = new File("src/" + CONFIG_FILE_NAME);
                    if (fSrc.exists()) {
                        try (FileInputStream fis = new FileInputStream(fSrc)) {
                            props.load(fis);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    public static void saveProps(String url, String username, String password) {
        props.setProperty("db.url", url);
        props.setProperty("db.username", username);
        props.setProperty("db.password", password);

        // 1. Save to current directory (Critical for JAR/Release usage)
        saveToFile(CONFIG_FILE_NAME, "Jvav News System DB Config");

        // 2. Save to src/db.properties (Dev environment persistence)
        saveToFile(SRC_PATH, "Jvav News System DB Config (Source)");

        // 3. Save to bin/db.properties (Dev runtime persistence)
        saveToFile(BIN_PATH, "Jvav News System DB Config (Runtime)");
    }

    private static void saveToFile(String path, String comment) {
        File file = new File(path);
        // Only write if directory exists (simple check)
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            return;
        }

        try (OutputStream out = new FileOutputStream(file)) {
            props.store(out, comment);
            System.out.println("Config saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save config to " + path + ": " + e.getMessage());
        }
    }
}
