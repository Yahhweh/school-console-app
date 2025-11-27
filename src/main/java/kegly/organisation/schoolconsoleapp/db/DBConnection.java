package kegly.organisation.schoolconsoleapp.db;

import kegly.organisation.schoolconsoleapp.exception.DBException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String propertiesFileName = "application.properties";
    private static final String testPropertiesFileName = "testApplication.properties";

    public Connection getConnection() {
        Properties properties = new Properties();
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

            if (input == null) {
                throw new DBException("Config file not found: " + propertiesFileName, null);
            }

            properties.load(input);

            return DriverManager.getConnection(
                properties.getProperty("ds.url"),
                properties.getProperty("ds.username"),
                properties.getProperty("ds.password")
            );

        } catch (IOException | SQLException e) {
            throw new DBException("Error connecting to the database", e);
        }
    }

    public Connection getTestConnection(){
        Properties properties = new Properties();
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(testPropertiesFileName);

            if (input == null) {
                throw new DBException("Config file not found: " + testPropertiesFileName, null);
            }

            properties.load(input);

            return DriverManager.getConnection(
                properties.getProperty("db.url"),
                "sa",
                ""
            );

        } catch (IOException | SQLException e) {
            throw new DBException("Error connecting to the database", e);
        }
    }
}