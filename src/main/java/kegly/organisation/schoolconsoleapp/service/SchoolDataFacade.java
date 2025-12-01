package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.CourseJdbc;
import kegly.organisation.schoolconsoleapp.dao.jdbc.GroupJdbc;
import kegly.organisation.schoolconsoleapp.dao.jdbc.StudentJdbc;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.ServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SchoolDataFacade {

    private final DBConnection DBConnection;
    private final StudentJdbc studentJdbc;
    private final GroupJdbc groupJdbc;
    private final CourseJdbc courseJdbc;
    private static final String initialSql = "schema.sql";
    private static final int coursesAmount = 10;
    private static final int groupsAmount = 10;
    private static final int studentsAmount = 200;
    private static final int MIN_COURSES_PER_STUDENT = 1;
    private static final int MAX_COURSES_PER_STUDENT = 3;

    public SchoolDataFacade() {
        this.DBConnection = new DBConnection();
        this.studentJdbc = new StudentJdbc(DBConnection);
        this.groupJdbc = new GroupJdbc(DBConnection);
        this.courseJdbc = new CourseJdbc(DBConnection);
    }

    public void initializeDatabase() {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Initializing database schema");
            new SchemaLoader().runScript(connection, initialSql);

            System.out.println("Generating data");
            new CoursesSeeder(courseJdbc).generate(coursesAmount);
            new GroupsSeeder(groupJdbc).generate(groupsAmount);
            StudentsSeeder studentsSeeder = new StudentsSeeder(studentJdbc, groupJdbc);
            studentsSeeder.generate(studentsAmount);
            studentsSeeder.assignRandomGroups(10, 30);

            new StudentsToCoursesSeeder(
                studentJdbc,
                courseJdbc,
                MIN_COURSES_PER_STUDENT,
                MAX_COURSES_PER_STUDENT
            ).generate(studentsAmount);

            System.out.println("Data initialization completed.\n");

        } catch (SQLException e) {
            throw new ServiceException("Initialization failed", e);
        }
    }

    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        return groupJdbc.findGroupsWithLessOrEqualStudents(count);
    }

    public List<Student> findStudentsByCourseName(String courseName) {
        return studentJdbc.findStudentsByCourseName(courseName);
    }

    public void addNewStudent(String firstName, String lastName, Integer groupId) {
        studentJdbc.save(new Student(groupId, firstName, lastName));
    }

    public void deleteStudentById(int studentId) {
        studentJdbc.deleteById(studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) {
        studentJdbc.addCourseToStudent(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        studentJdbc.removeStudentFromCourse(studentId, courseId);
    }
}