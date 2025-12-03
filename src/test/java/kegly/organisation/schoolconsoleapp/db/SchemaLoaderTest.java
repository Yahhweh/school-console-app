package kegly.organisation.schoolconsoleapp.db;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SchemaLoaderTest {

    @Test
    void runScript_shouldCreateTables_whenFileExists() throws IOException {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        final String initialSql = "schema.sql";

        try(Connection connection = connectionProvider.getConnection()) {
            SchemaLoader schemaLoader = new SchemaLoader();
            schemaLoader.runScript(connection, initialSql);

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