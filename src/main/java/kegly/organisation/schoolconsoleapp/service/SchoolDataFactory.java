package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
import kegly.organisation.schoolconsoleapp.db.DatabaseInitializer;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SchoolDataFactory {

    private final ConnectionClass connectionClass;
    private final StudentDao studentDao;
    private final GroupDao groupDao;
    private final CourseDao courseDao;

    public SchoolDataFactory() {
        this.connectionClass = new ConnectionClass();
        this.studentDao = new StudentDao(connectionClass);
        this.groupDao = new GroupDao();
        this.courseDao = new CourseDao(connectionClass);
    }

    public void initializeDatabase() {
        try (Connection connection = connectionClass.getConnection()) {
            System.out.println("Initializing database schema");
            new DatabaseInitializer().runScript(connection);

            System.out.println("Generating data");
            new InitialCourses(courseDao).generate();
            new InitialGroups(groupDao).generate();
            InitialStudents initialStudents = new InitialStudents(studentDao, groupDao);
            initialStudents.generate();
            initialStudents.assignRandomGroups();
            new InitialStudentsToCourses(studentDao).generate();

            System.out.println("Data initialization completed.\n");

        } catch (SQLException e) {
            throw new RuntimeException("Initialization failed", e);
        }
    }


    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        return studentDao.findGroupsWithLessOrEqualStudents(count);
    }

    public List<Student> findStudentsByCourseName(String courseName) {
        return studentDao.findStudentsByCourseName(courseName);
    }

    public void addNewStudent(String firstName, String lastName, Integer groupId) {
        studentDao.save(new Student(groupId, firstName, lastName));
    }

    public void deleteStudentById(int studentId) {
        studentDao.deleteById(studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) {
        studentDao.addCourseToStudent(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        studentDao.removeStudentFromCourse(studentId, courseId);
    }
}