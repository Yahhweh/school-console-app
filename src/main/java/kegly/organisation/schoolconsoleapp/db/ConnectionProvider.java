package kegly.organisation.schoolconsoleapp.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {

    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";
    private final Properties properties;

    public ConnectionProvider() throws IOException {
        this(DEFAULT_PROPERTIES_FILE);
    }

    public ConnectionProvider(String propertiesFileName) throws IOException {
        this.properties = loadProperties(propertiesFileName);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            properties.getProperty("ds.url"),
            properties.getProperty("ds.username"),
            properties.getProperty("ds.password")
        );
    }

    private Properties loadProperties(String fileName) throws IOException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);) {
            if (input == null) {
                throw new DBException("Config file not found: " + fileName);
            }
            props.load(input);
            return props;
        }
    }
}