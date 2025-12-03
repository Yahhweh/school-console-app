package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourseDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionProvider;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcCourseDaoTest {

    private ConnectionProvider connectionProvider;
    private JdbcCourseDao jdbcCourseDao;

    @BeforeEach
    void setup() throws Exception {
        connectionProvider = new ConnectionProvider("testApplication.properties");
        jdbcCourseDao = new JdbcCourseDao(connectionProvider);

        final String initialSql = "schema.sql";

        try (Connection conn = connectionProvider.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute("DROP ALL OBJECTS");

            SchemaLoader initializer = new SchemaLoader();
            initializer.runScript(conn, initialSql);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Test
    void findAll_returnCourses_whenRightConnection() {
        List<Course> expected = List.of();

        List<Course> result = jdbcCourseDao.findAll();

        assertEquals(expected, result);

    }

    @Test
    void save_addCourseToSql() {
        Integer testId = 1;
        String testCourseName = "TEST-" + System.currentTimeMillis();
        String testCourseDescription = "TEST-" + System.currentTimeMillis() + 22;

        Course newCourse = new Course(testId,testCourseName,testCourseDescription);

        List<Course> expected = List.of(new Course(testId,testCourseName, testCourseDescription));

        jdbcCourseDao.save(newCourse);

        List<Course> result = jdbcCourseDao.findAll();

        assertEquals(expected,result);
    }

}





