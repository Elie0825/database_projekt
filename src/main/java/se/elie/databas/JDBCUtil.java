package se.elie.databas;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = JDBCUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Unable to find application.properties");
            }
            properties.load(input); // Läs in properties-filen
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Failed to load database properties");
        }
    }

    // Hämta en databasanslutning med information från application.properties
    public static Connection getConnection() throws SQLException {
        // Läs in databasuppgifter från properties-filen
        String dbURL = properties.getProperty("db.url");
        String userId = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        // Skapa och returnera anslutningen
        return DriverManager.getConnection(dbURL, userId, password);
    }

    // Stäng Statement
    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Stäng ResultSet
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Commit för anslutningen
    public static void commit(Connection conn) {
        try {
            if (conn != null) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Rollback för anslutningen
    public static void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
