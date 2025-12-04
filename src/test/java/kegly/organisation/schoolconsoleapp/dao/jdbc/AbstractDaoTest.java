package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.db.ConnectionProvider;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import org.junit.jupiter.api.BeforeEach; // Обязательно!

import java.sql.Connection;
import java.sql.Statement;

public abstract class AbstractDaoTest {

    protected ConnectionProvider connectionProvider;

    @BeforeEach
    public void setup() throws Exception {
        connectionProvider = new ConnectionProvider("testApplication.properties");
        String initialSql = "data.sql";

        try (Connection conn = connectionProvider.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute("DROP ALL OBJECTS");

            SchemaLoader initializer = new SchemaLoader();
            initializer.runScript(conn, initialSql);
        }
    }
}