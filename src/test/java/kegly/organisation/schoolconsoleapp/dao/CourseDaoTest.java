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

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDaoTest {

    private ConnectionClass connectionClass;
    CourseDao courseDao;

    @BeforeEach
    void setup() {
        connectionClass = new ConnectionClass();
        courseDao = new CourseDao(connectionClass);

        try (Connection conn = connectionClass.getConnection()) {
            DatabaseInitializer initializer = new DatabaseInitializer();
            initializer.runScript(conn);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Test
    void findAll_returnCourses_whenRightConnection() {
        List<Course> expected = List.of();

        List<Course> result = courseDao.findAll();

        assertEquals(expected, result);

    }

    @Test
    void save_addCourseToSql() {
        String testCourseName = "TEST-" + System.currentTimeMillis();
        String testCourseDescription = "TEST-" + System.currentTimeMillis() + 22;

        Course newCourse = new Course(testCourseName,testCourseDescription);

        List<Course> expected = List.of(new Course(testCourseName, testCourseDescription));

        courseDao.save(newCourse);

        List<Course> result = courseDao.findAll();

        assertEquals(expected,result);


    }

}





