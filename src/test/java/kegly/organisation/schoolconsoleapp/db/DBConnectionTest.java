package kegly.organisation.schoolconsoleapp.db;

import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBConnectionTest {

    private DBConnection DBConnection;

    @BeforeEach
    void setup() {
        DBConnection = new DBConnection();
    }

    @Test
    void generate_returnsExactAmount_whenRequestedCountGiven() {

        try (Connection connection = DBConnection.getConnection()) {

            assertNotNull(connection);

            assertTrue(connection.isValid(1));

        } catch (
                SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


}