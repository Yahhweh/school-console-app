package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.DaoException;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentDaoTest extends AbstractDaoTest {
    private StudentDao studentDao;

    @BeforeEach
    void setUp() {
        this.studentDao = new JdbcStudentDao(super.connectionProvider);
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoStudentsExist() {
        List<Student> expected = List.of();

        List<Student> result = studentDao.findAll();

        assertEquals(expected, result);
    }

    @Test
    void save_PersistsStudent_WhenStudentIsNew() {
        Integer expectedId = 1;
        String firstName = "John";
        String lastName = "Doe-" + System.currentTimeMillis();

        Student newStudent = new Student(expectedId, null, firstName, lastName);

        List<Student> expected = List.of(newStudent);
        studentDao.save(newStudent);
        List<Student> result = studentDao.findAll();

        assertEquals(expected, result);
    }

    @Test
    void update_UpdatesStudentData_WhenStudentExists() {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement st1 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (300, 'Science Group')")) {
            st1.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        Student student = new Student(1, 300, "Tom", "Hanks");
        studentDao.save(student);

        studentDao.update(student);

        List<Student> students = studentDao.findAll();
        Student updatedStudent = students.stream()
            .filter(s -> s.getId() == 1)
            .findFirst()
            .orElseThrow();

        assertEquals(300, updatedStudent.getGroupId());
    }

    @Test
    void deleteById_RemovesStudent_WhenStudentIdExists() {
        Student student = new Student(1, null, "Tom", "Hanks");
        studentDao.save(student);

        studentDao.deleteById(student.getId());

        List<Student> expected = studentDao.findAll();

        List<Student> result = List.of();

        assertEquals(expected, result);
    }

    @Test
    void removeFromCourse_RemovesAssociation_WhenStudentIsEnrolledInCourse() {
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement st1 = connection.prepareStatement("INSERT INTO courses (course_id, course_name, course_description) VALUES (50, 'Biology', 'Basic Biology')");
            st1.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }

        Student student = new Student(1, null, "Tom", "Hanks");
        studentDao.save(student);
        studentDao.addCourseToStudent(1, 50);

        studentDao.removeFromCourse(1, 50);

        List<Student> result = studentDao.findByCourseName("Biology");
        List<Student> expected = List.of();

        assertEquals(expected, result);
    }
}