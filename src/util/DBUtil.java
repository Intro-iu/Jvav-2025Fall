package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
    private static Properties props;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            reloadConfig();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found! Please add mysql-connector-j.jar to lib/");
        }
    }

    public static void reloadConfig() {
        props = ConfigUtil.loadProps();
    }

    public static Connection getConnection() throws SQLException {
        if (props == null || props.isEmpty()) {
            reloadConfig();
        }
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password"));
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
