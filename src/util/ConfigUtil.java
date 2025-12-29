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

    private static final String SRC_PATH = "src/" + CONFIG_FILE_NAME;
    private static final String BIN_PATH = "bin/" + CONFIG_FILE_NAME;

    public static Properties loadProps() {
        props = new Properties();
        try (InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (in != null) {
                props.load(in);
            } else {
                File f = new File(CONFIG_FILE_NAME);
                if (f.exists()) {
                    try (FileInputStream fis = new FileInputStream(f)) {
                        props.load(fis);
                    }
                } else {
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

        saveToFile(CONFIG_FILE_NAME, "Jvav News System DB Config");

        saveToFile(SRC_PATH, "Jvav News System DB Config (Source)");

        saveToFile(BIN_PATH, "Jvav News System DB Config (Runtime)");
    }

    private static void saveToFile(String path, String comment) {
        File file = new File(path);
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
