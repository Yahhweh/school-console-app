package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudent;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcStudentTest {

    private DBConnection dbConnection;
    private JdbcStudent jdbcStudent;
    private static final String DROP_ALL = "DROP ALL OBJECTS";

    @BeforeEach
    void setup() {
        dbConnection = new DBConnection("testApplication.properties");
        jdbcStudent = new JdbcStudent(dbConnection);

        final String initialSql = "schema.sql";

        try (Connection conn = dbConnection.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute("DROP ALL OBJECTS");

            SchemaLoader initializer = new SchemaLoader();
            initializer.runScript(conn, initialSql);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }
        final String initialSql = "schema.sql";

    @Test
    void findAll_returnStudents_whenRightConnection() {
        List<Student> expected = List.of();

        List<Student> result = jdbcStudent.findAll();

        assertEquals(expected, result);
    }

    @Test
    void save_addStudentToSql_whenCorrectData() {
        Integer expectedId = 1;
        String firstName = "John";
        String lastName = "Doe-" + System.currentTimeMillis();

        Student newStudent = new Student(expectedId, null, firstName, lastName);

        List<Student> expected = List.of(newStudent);
        jdbcStudent.save(newStudent);
        List<Student> result = jdbcStudent.findAll();

        assertEquals(expected, result);
    }

    @Test
    void update_updatesStudentGroupId_whenGroupExists() {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement st1 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (300, 'Science Group')")) {
            st1.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        Student student = new Student(1, 300, "Tom", "Hanks");
        jdbcStudent.save(student);

        jdbcStudent.update(student);

        List<Student> students = jdbcStudent.findAll();
        Student updatedStudent = students.stream()
            .filter(s -> s.getId() == 1)
            .findFirst()
            .orElseThrow();

        assertEquals(300, updatedStudent.getGroupId());
    }

    @Test
    void deleteById_deletesStudentsById_WhenOneStudent() {
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement st1 = connection.prepareStatement("DELETE FROM students WHERE student_id = '1'");
            st1.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        Student student = new Student(1, null, "Tom", "Hanks");
        jdbcStudent.save(student);

        jdbcStudent.deleteById(student.getId());

        List<Student> expected = jdbcStudent.findAll();

        List<Student> result = List.of();

        assertEquals(expected, result);
    }

    @Test
    void removeStudentFromCourse_removesStudentFromCourse_WhenLinkExists() {
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement st1 = connection.prepareStatement("INSERT INTO courses (course_id, course_name, course_description) VALUES (50, 'Biology', 'Basic Biology')");
            st1.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }

        Student student = new Student(1, null, "Tom", "Hanks");
        jdbcStudent.save(student);
        jdbcStudent.addCourseToStudent(1, 50);

        jdbcStudent.removeStudentFromCourse(1, 50);

        List<Student> result = jdbcStudent.findByCourseName("Biology");
        List<Student> expected = List.of();

        assertEquals(expected, result);
    }

}