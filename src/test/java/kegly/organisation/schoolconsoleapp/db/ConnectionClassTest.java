package kegly.organisation.schoolconsoleapp.db;

import kegly.organisation.schoolconsoleapp.exception.DBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionClassTest {

    private ConnectionClass connectionClass;

    @BeforeEach
    void setup() {
        connectionClass = new ConnectionClass("ds-connection.properties");
    }

    @Test
    void getConnection_returnConnection_whenRightData() {

        try (Connection connection = connectionClass.getConnection()) {

            assertNotNull(connection, "Connection object should not be null");

            assertTrue(connection.isValid(1), "Connection should be valid and open");

        } catch (
                SQLException e) {
            fail("SQL Exception during validation: " + e.getMessage());
        }
    }

    @Test
    void getConnection_throwDatabaseOperationException_whenWrongPath(){

        ConnectionClass wrongConnector = new ConnectionClass("wrong.properies");

        assertThrows(DBException.class, () -> {
            wrongConnector.getConnection();
        });
    }


}