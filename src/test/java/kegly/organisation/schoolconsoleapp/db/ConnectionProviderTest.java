package kegly.organisation.schoolconsoleapp.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectionProviderTest {

    private ConnectionProvider connectionProvider;

    @BeforeEach
    void setup() throws IOException {
        connectionProvider = new ConnectionProvider();
    }

    @Test
    void getConnection_shouldReturnValidConnection() throws SQLException {

        try (Connection connection = connectionProvider.getConnection()) {
            assertNotNull(connection);
            assertTrue(connection.isValid(1));
        }
    }
}