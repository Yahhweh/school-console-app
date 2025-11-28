package kegly.organisation.schoolconsoleapp.db;

import kegly.organisation.schoolconsoleapp.exception.DBException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String PROPERTIES_FILE = "application.properties";
    private static final String TEST_PROPERTIES_FILE = "testApplication.properties";

    public Connection getConnection() {
        return createConnection(PROPERTIES_FILE, "ds.url", "ds.username", "ds.password");
    }

    public Connection getTestConnection() {
        return createConnection(TEST_PROPERTIES_FILE, "db.url", "ds.username", "ds.password");
    }

    private Connection createConnection(String fileName, String urlKey, String userKey, String passKey) {
        Properties properties = loadProperties(fileName);

        try {
            return DriverManager.getConnection(
                properties.getProperty(urlKey),
                properties.getProperty(userKey),
                properties.getProperty(passKey)
            );
        } catch (SQLException e) {
            throw new DBException("Error connecting to the database using config: " + fileName, e);
        }
    }

    private Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new DBException("Config file not found: " + fileName, null);
            }
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new DBException("Error loading configuration: " + fileName, e);
        }
    }
}