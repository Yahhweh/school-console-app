package kegly.organisation.schoolconsoleapp.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testH2 {

    Connection connection;

    @BeforeEach
    void setup() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void connectionTest() throws Exception {

        PreparedStatement ps = connection.prepareStatement("CREATE TABLE table1(col1 varchar(20));" +
            "INSERT INTO table1(col1) VALUES ('NIGGER')");

        ps.executeUpdate();

        PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM table1");

        ResultSet rs = ps1.executeQuery();

        String string = new String();
        while (rs.next()) {
            string = rs.getString("col1");
        }

        assertEquals("NIGGER", string);


    }
}
