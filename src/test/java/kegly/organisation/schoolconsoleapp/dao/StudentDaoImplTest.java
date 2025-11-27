package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentDaoImplTest {

    private DBConnection dBConnection;
    private StudentDaoImpl studentDaoImpl;
    private static final String DROP_ALL = "DROP ALL OBJECTS";

    @BeforeEach
    void setup() {
        dBConnection = new DBConnection() {
            @Override
            public Connection getConnection() {
                return super.getTestConnection();
            }
        };
        studentDaoImpl = new StudentDaoImpl(dBConnection);

        final String initialSql = "schema.sql";

        try (Connection conn = dBConnection.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute(DROP_ALL);
            SchemaLoader initializer = new SchemaLoader();
            initializer.runScript(conn, initialSql);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Test
    void findAll_returnStudents_whenRightConnection() {
        List<Student> expected = List.of();

        List<Student> result = studentDaoImpl.findAll();

        assertEquals(expected, result);
    }

    @Test
    void save_addStudentToSql() {
        Integer expectedId = 1;
        String firstName = "John";
        String lastName = "Doe-" + System.currentTimeMillis();

        Student newStudent = new Student(expectedId, null, firstName, lastName);

        List<Student> expected = List.of(newStudent);
        studentDaoImpl.save(newStudent);
        List<Student> result = studentDaoImpl.findAll();

        assertEquals(expected, result);
    }
}