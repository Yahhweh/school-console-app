package kegly.organisation.schoolconsoleapp.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseInitializerTest {

    @Test
    void runScript_shouldCreateTables_whenFileExists() {
        DBConnection DBConnection = new DBConnection();

        try(Connection connection = DBConnection.getConnection()) {
            DatabaseInitializer databaseInitializer = new DatabaseInitializer();
            databaseInitializer.runScript(connection);

            boolean tableExists = checkTableExists(connection, "students");
            assertTrue(tableExists);
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