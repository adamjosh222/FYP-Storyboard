package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:derby://localhost:1527/fyp_tracking";
    private static final String USER = "app";
    private static final String PASS = "app";

    public static Connection getConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException("DB connection error: " + e.getMessage(), e);
        }
    }
}
