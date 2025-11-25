package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.DatabaseInitializer;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.ServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SchoolDataFactory {

    private final DBConnection DBConnection;
    private final StudentDao studentDao;
    private final GroupDao groupDao;
    private final CourseDao courseDao;

    public SchoolDataFactory() {
        this.DBConnection = new DBConnection();
        this.studentDao = new StudentDao(DBConnection);
        this.groupDao = new GroupDao();
        this.courseDao = new CourseDao(DBConnection);
    }

    public void initializeDatabase() {
        try (Connection connection = DBConnection.getConnection()) {
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
            throw new ServiceException("Initialization failed", e);
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