package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
import kegly.organisation.schoolconsoleapp.db.DatabaseInitializer;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseDaoTest {

    private ConnectionClass connectionClass;
    private CourseDao courseDao;

    @BeforeEach
    void setup() {
        connectionClass = new ConnectionClass("ds-connection.properties");
        courseDao = new CourseDao(connectionClass);

        try (Connection conn = connectionClass.getConnection()) {
            DatabaseInitializer initializer = new DatabaseInitializer("schema.sql");
            initializer.runScript(conn);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Test
    void getCourse_returnCourses_whenRightConnection() {
        List<Course> expected = List.of();

        List<Course> result = courseDao.findAll();

        assertEquals(expected, result);

    }

    @Test
    void addCourse_addCourseToSql() {
        String testCourseName = "TEST-" + System.currentTimeMillis();
        String testCourseDescription = "TEST-" + System.currentTimeMillis() + 22;

        Course newCourse = new Course(testCourseName,testCourseDescription);

        List<Course> expected = List.of(new Course(testCourseName, testCourseDescription));

        courseDao.save(newCourse);

        List<Course> result = courseDao.findAll();

        assertEquals(expected,result);


    }
//
//    @Test
//    void findGroupsWithLessOrEqualStudents_returnEmptyCourse_whenLimitIsZero() {
//        String uniqueName = "Test-Empty-" + System.currentTimeMillis();
//        Group emptyGroup = new Group(uniqueName);
//
//        List<Group> expected = List.of(emptyGroup);
//
//        groupDao.save(emptyGroup);
//
//        List<Group> result = groupDao.findGroupsWithLessOrEqualStudents(1);
//
//
//        assertEquals(expected, result);
//
//    }

}





