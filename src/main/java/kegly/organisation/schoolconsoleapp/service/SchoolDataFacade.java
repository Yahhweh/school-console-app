package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDaoImpl;
import kegly.organisation.schoolconsoleapp.dao.GroupDaoImpl;
import kegly.organisation.schoolconsoleapp.dao.StudentDaoImpl;
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
    private final StudentDaoImpl studentDaoImpl;
    private final GroupDaoImpl groupDaoImpl;
    private final CourseDaoImpl courseDaoImpl;
    private static final String initialSql = "schema.sql";
    private static final int coursesAmount = 10;
    private static final int groupsAmount = 10;
    private static final int studentsAmount = 200;

    public SchoolDataFacade() {
        this.DBConnection = new DBConnection();
        this.studentDaoImpl = new StudentDaoImpl(DBConnection);
        this.groupDaoImpl = new GroupDaoImpl();
        this.courseDaoImpl = new CourseDaoImpl(DBConnection);
    }

    public void initializeDatabase() {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Initializing database schema");
            new SchemaLoader().runScript(connection, initialSql);

            System.out.println("Generating data");
            new CoursesSeeder(courseDaoImpl).generate(coursesAmount);
            new GroupsSeeder(groupDaoImpl).generate(groupsAmount);
            StudentsSeeder studentsSeeder = new StudentsSeeder(studentDaoImpl, groupDaoImpl);
            studentsSeeder.generate(studentsAmount);
            studentsSeeder.assignRandomGroups(10, 30);
            new StudentsToCoursesSeeder(studentDaoImpl).generate(studentsAmount);

            System.out.println("Data initialization completed.\n");

        } catch (SQLException e) {
            throw new ServiceException("Initialization failed", e);
        }
    }


    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        return studentDaoImpl.findGroupsWithLessOrEqualStudents(count);
    }

    public List<Student> findStudentsByCourseName(String courseName) {
        return studentDaoImpl.findStudentsByCourseName(courseName);
    }

    public void addNewStudent(String firstName, String lastName, Integer groupId) {
        studentDaoImpl.save(new Student(groupId, firstName, lastName));
    }

    public void deleteStudentById(int studentId) {
        studentDaoImpl.deleteById(studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) {
        studentDaoImpl.addCourseToStudent(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        studentDaoImpl.removeStudentFromCourse(studentId, courseId);
    }
}