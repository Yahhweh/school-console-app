package kegly.organisation.schoolconsoleapp.db;

import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectionClassTest {

    private ConnectionClass connectionClass;

    @BeforeEach
    void setup() {
        connectionClass = new ConnectionClass();
    }

    @Test
    void generate_returnsExactAmount_whenRequestedCountGiven() {

        try (Connection connection = connectionClass.getConnection()) {

            assertNotNull(connection);

            assertTrue(connection.isValid(1));

        } catch (
                SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


}