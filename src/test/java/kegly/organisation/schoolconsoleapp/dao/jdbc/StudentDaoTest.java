package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentDaoTest extends AbstractDaoTest {
    private static final String SQL_DATA = "datasets/student_dao.sql";
    private StudentDao studentDao;

    @BeforeEach
    void setUp() {
        this.studentDao = new JdbcStudentDao(super.connectionProvider);
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoStudentsExist() {
        assertEquals(0, studentDao.findAll().size());
    }

    @Test
    void save_PersistsStudent_WhenStudentIsNew() throws SQLException {
        SchemaLoader schemaLoader = new SchemaLoader();
        schemaLoader.runScript(connectionProvider.getConnection(), SQL_DATA);

        List<Student> initialStudents = studentDao.findAll();
        int initialSize = initialStudents.size();

        Student newStudent = new Student(1, "John", "Doe-" + System.currentTimeMillis());

        studentDao.save(newStudent);

        List<Student> result = studentDao.findAll();

        assertEquals(initialSize + 1, result.size());

        String studentLastName = result.stream().filter(s -> s.getLastName().equals(newStudent.getLastName())).findFirst().orElseThrow().getLastName();
        assertEquals(newStudent.getLastName(), studentLastName);
    }

    @Test
    void update_UpdatesStudentData_WhenStudentExists() throws SQLException {
        SchemaLoader schemaLoader = new SchemaLoader();
        schemaLoader.runScript(connectionProvider.getConnection(), SQL_DATA);

        Student student = new Student(1, "Tom", "Hanks");
        studentDao.save(student);

        student.setFirstName("Thomas");

        studentDao.update(student);

        Student updatedStudent = studentDao.findAll().stream()
            .filter(s -> s.getId().equals(student.getId()))
            .findFirst()
            .orElseThrow();

        assertEquals("Thomas", updatedStudent.getFirstName());
        assertEquals(1, updatedStudent.getGroupId());
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
    void removeFromCourse_RemovesAssociation_WhenStudentIsEnrolledInCourse() throws SQLException {
        SchemaLoader schemaLoader = new SchemaLoader();
        schemaLoader.runScript(connectionProvider.getConnection(), SQL_DATA);

        Student student = new Student(1, "Tom", "Hanks");
        studentDao.save(student);

        int studentId = student.getId();

        studentDao.addCourseToStudent(studentId, 50);

        studentDao.removeFromCourse(studentId, 50);

        assertEquals(0, studentDao.findByCourseName("Biology").size());
    }
}