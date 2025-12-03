package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourseDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroupDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudentDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionProvider;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SchoolDataFacade {

    private final ConnectionProvider connectionProvider;
    private final JdbcStudentDao jdbcStudentDao;
    private final JdbcGroupDao jdbcGroupDao;
    private final JdbcCourseDao jdbcCourseDao;
    private static final String initialSql = "schema.sql";
    private static final int coursesAmount = 10;
    private static final int groupsAmount = 10;
    private static final int studentsAmount = 200;
    private static final int MIN_COURSES_PER_STUDENT = 1;
    private static final int MAX_COURSES_PER_STUDENT = 3;

    public SchoolDataFacade() {
        try {
            this.connectionProvider = new ConnectionProvider();
            this.jdbcStudentDao = new JdbcStudentDao(connectionProvider);
            this.jdbcGroupDao = new JdbcGroupDao(connectionProvider);
            this.jdbcCourseDao = new JdbcCourseDao(connectionProvider);
        } catch (IOException e) {
            throw new ServiceException("Failed to initialize database connection", e);
        }
    }

    public void initializeDatabase() {
        try (Connection connection = connectionProvider.getConnection()) {
            System.out.println("Initializing database schema");
            new SchemaLoader().runScript(connection, initialSql);

            System.out.println("Generating data");
            new CourseGenerator(jdbcCourseDao).generate(coursesAmount);
            new GroupsGenerator(jdbcGroupDao).generate(groupsAmount);
            StudentsGenerator studentsGenerator = new StudentsGenerator(jdbcStudentDao, jdbcGroupDao);
            studentsGenerator.generate(studentsAmount);
            studentsGenerator.assignRandomGroups(10, 30);

            new StudentsToCoursesGenerator(
                jdbcStudentDao,
                jdbcCourseDao,
                MIN_COURSES_PER_STUDENT,
                MAX_COURSES_PER_STUDENT
            ).generate(studentsAmount);

            System.out.println("Data initialization completed.\n");

        } catch (SQLException e) {
            throw new ServiceException("Initialization failed", e);
        }
    }

    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        return jdbcGroupDao.findWithLessOrEqualStudents(count);
    }

    public List<Student> findByCourseName(String courseName) {
        return jdbcStudentDao.findByCourseName(courseName);
    }

    public void addNewStudent(String firstName, String lastName, Integer groupId) {
        jdbcStudentDao.save(new Student(groupId, firstName, lastName));
    }

    public void deleteStudentById(int studentId) {
        jdbcStudentDao.deleteById(studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) {
        jdbcStudentDao.addCourseToStudent(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        jdbcStudentDao.removeFromCourse(studentId, courseId);
    }
}