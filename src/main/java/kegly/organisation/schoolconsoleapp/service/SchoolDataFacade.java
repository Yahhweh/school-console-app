package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourse;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroup;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudent;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SchoolDataFacade {

    private final DBConnection DBConnection;
    private final JdbcStudent jdbcStudent;
    private final JdbcGroup jdbcGroup;
    private final JdbcCourse jdbcCourse;
    private static final String initialSql = "schema.sql";
    private static final int coursesAmount = 10;
    private static final int groupsAmount = 10;
    private static final int studentsAmount = 200;
    private static final int MIN_COURSES_PER_STUDENT = 1;
    private static final int MAX_COURSES_PER_STUDENT = 3;

    public SchoolDataFacade() {
        this.DBConnection = new DBConnection();
        this.jdbcStudent = new JdbcStudent(DBConnection);
        this.jdbcGroup = new JdbcGroup(DBConnection);
        this.jdbcCourse = new JdbcCourse(DBConnection);
    }

    public void initializeDatabase() {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Initializing database schema");
            new SchemaLoader().runScript(connection, initialSql);

            System.out.println("Generating data");
            new CourseGenerator(jdbcCourse).generate(coursesAmount);
            new GroupsGenerator(jdbcGroup).generate(groupsAmount);
            StudentsGenerator studentsGenerator = new StudentsGenerator(jdbcStudent, jdbcGroup);
            studentsGenerator.generate(studentsAmount);
            studentsGenerator.assignRandomGroups(10, 30);

            new StudentsToCoursesGenerator(
                jdbcStudent,
                jdbcCourse,
                MIN_COURSES_PER_STUDENT,
                MAX_COURSES_PER_STUDENT
            ).generate(studentsAmount);

            System.out.println("Data initialization completed.\n");

        } catch (SQLException e) {
            throw new ServiceException("Initialization failed", e);
        }
    }

    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        return jdbcGroup.findGroupsWithLessOrEqualStudents(count);
    }

    public List<Student> findByCourseName(String courseName) {
        return jdbcStudent.findByCourseName(courseName);
    }

    public void addNewStudent(String firstName, String lastName, Integer groupId) {
        jdbcStudent.save(new Student(groupId, firstName, lastName));
    }

    public void deleteStudentById(int studentId) {
        jdbcStudent.deleteById(studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) {
        jdbcStudent.addCourseToStudent(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        jdbcStudent.removeStudentFromCourse(studentId, courseId);
    }
}