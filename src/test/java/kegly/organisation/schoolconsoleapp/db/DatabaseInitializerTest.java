package kegly.organisation.schoolconsoleapp.db;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseInitializerTest {

    @Test
    void runScript_shouldCreateTables_whenFileExists() {
        ConnectionClass connectionClass = new ConnectionClass("ds-connection.properties");

        try(Connection connection = connectionClass.getConnection()) {
            DatabaseInitializer databaseInitializer = new DatabaseInitializer("scheme.sql");
            databaseInitializer.runScript(connection);

            boolean tableExists = checkTableExists(connection, "students");
            assertTrue(tableExists, "Table 'students' should exist after script execution");
        }
        catch (Exception e){
            fail("Initialization failed:: "+  e.getMessage());
        }
    }

    private boolean checkTableExists(Connection connection, String tableName) throws SQLException {
        try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }
}