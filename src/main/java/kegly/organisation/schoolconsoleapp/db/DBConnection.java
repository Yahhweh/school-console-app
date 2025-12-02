package kegly.organisation.schoolconsoleapp.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";
    private final Properties properties;

    public DBConnection() {
        this(DEFAULT_PROPERTIES_FILE);
    }

    public DBConnection(String propertiesFileName) {
        this.properties = loadProperties(propertiesFileName);
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                properties.getProperty("ds.url"),
                properties.getProperty("ds.username"),
                properties.getProperty("ds.password")
            );
        } catch (SQLException e) {
            throw new DBException("Failed to obtain database connection", e);
        }
    }

    private Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new DBException("Config file not found: " + fileName, null);
            }
            props.load(input);
            return props;
        } catch (IOException e) {
            throw new DBException("Error loading configuration: " + fileName, e);
        }
    }
}