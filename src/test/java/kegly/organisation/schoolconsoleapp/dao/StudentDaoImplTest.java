package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    void save_addStudentToSql_whenCorrectData() {
        Integer expectedId = 1;
        String firstName = "John";
        String lastName = "Doe-" + System.currentTimeMillis();

        Student newStudent = new Student(expectedId, null, firstName, lastName);

        List<Student> expected = List.of(newStudent);
        studentDaoImpl.save(newStudent);
        List<Student> result = studentDaoImpl.findAll();

        assertEquals(expected, result);
    }

    @Test
    void findGroupsWithLessOrEqualStudents_returnsFilteredGroups_whenTwoStudents() {
        try (Connection connection = dBConnection.getConnection();
             PreparedStatement st1 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (100, 'Group A')");
             PreparedStatement st2 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (200, 'Group B')")) {

            st1.executeUpdate();
            st2.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }

        Student s1 = new Student(1, 100, "Student", "One");
        Student s2 = new Student(2, 100, "Student", "Two");
        Student s3 = new Student(3, 200, "Student", "Three");

        studentDaoImpl.save(s1);
        studentDaoImpl.save(s2);
        studentDaoImpl.save(s3);

        List<Group> result = studentDaoImpl.findGroupsWithLessOrEqualStudents(1);

        assertEquals(1, result.size());
        assertEquals("Group B", result.get(0).getGroupName());
    }


    @Test
    void update_updatesStudentGroupId_whenGroupExists() {
        try (Connection connection = dBConnection.getConnection();
             PreparedStatement st1 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (300, 'Science Group')")) {
            st1.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        Student student = new Student(1, 300, "Tom", "Hanks");
        studentDaoImpl.save(student);

        studentDaoImpl.update(student);

        List<Student> students = studentDaoImpl.findAll();
        Student updatedStudent = students.stream()
            .filter(s -> s.getStudentId() == 1)
            .findFirst()
            .orElseThrow();

        assertEquals(300, updatedStudent.getGroupId());
    }

    @Test
    void deleteById_deletesStudentsById_WhenOneStudent() {
        try (Connection connection = dBConnection.getConnection()) {
            PreparedStatement st1 = connection.prepareStatement("DELETE FROM students WHERE student_id = '1'");
            st1.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        Student student = new Student(1, null, "Tom", "Hanks");
        studentDaoImpl.save(student);

        studentDaoImpl.deleteById(student.getStudentId());

        List<Student> expected = studentDaoImpl.findAll();

        List<Student> result = List.of();

        assertEquals(expected, result);
    }

    @Test
    void removeStudentFromCourse_removesStudentFromCourse_WhenLinkExists() {
        try (Connection connection = dBConnection.getConnection()) {
            PreparedStatement st1 = connection.prepareStatement("INSERT INTO courses (course_id, course_name, course_description) VALUES (50, 'Biology', 'Basic Biology')");
            st1.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }

        Student student = new Student(1, null, "Tom", "Hanks");
        studentDaoImpl.save(student);
        studentDaoImpl.addCourseToStudent(1, 50);

        studentDaoImpl.removeStudentFromCourse(1, 50);

        List<Student> result = studentDaoImpl.findStudentsByCourseName("Biology");
        List<Student> expected = List.of();

        assertEquals(expected, result);
    }

}