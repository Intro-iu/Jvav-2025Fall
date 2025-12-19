package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class ScriptRunner {
    public static void runScript(String path) {
        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                Reader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(reader)) {

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--"))
                    continue;
                sb.append(line + " ");
                if (line.endsWith(";")) {
                    String sql = sb.toString();
                    System.out.println("Executing: " + sql);
                    // Handle USE command or just ignore if URL has DB
                    if (sql.toUpperCase().startsWith("USE ")) {
                        // ignore, already connected
                    } else {
                        stmt.execute(sql);
                    }
                    sb = new StringBuilder();
                }
            }
            System.out.println("Script executed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runScript("sql/init.sql");
    }
}
