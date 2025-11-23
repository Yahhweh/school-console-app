package kegly.organisation.schoolconsoleapp.db;

import kegly.organisation.schoolconsoleapp.exception.DBException;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DatabaseInitializer {

    private final String scriptName;

    public void runScript(Connection connection) {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(scriptName)) {

            if (input == null) {
                throw new RuntimeException("SQL script not found: " + scriptName);
            }

            String sqlScript;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                sqlScript = reader.lines().collect(Collectors.joining("\n"));
            }

            String[] commands = sqlScript.split(";");

            try (Statement statement = connection.createStatement()) {
                for (String command : commands) {
                    if (!command.isBlank()) {
                        statement.execute(command.trim());
                    }
                }
            }

        } catch (IOException | SQLException e) {
            throw new DBException("Error initializing database from script: " + scriptName, e);
        }
    }
}