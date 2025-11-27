package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDaoImplTest {

    private DBConnection dbConnection;
    private CourseDaoImpl courseDaoImpl;
    private static final String dropAll = "DROP ALL OBJECTS";

    @BeforeEach
    void setup() {
        dbConnection = new DBConnection(){
            @Override
            public   Connection getConnection(){
                return super.getTestConnection();
            }
        };
        courseDaoImpl = new CourseDaoImpl(dbConnection);
        final String initialSql = "schema.sql";

        try (Connection conn = dbConnection.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute(dropAll);
            SchemaLoader initializer = new SchemaLoader();
            initializer.runScript(conn, initialSql);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Test
    void findAll_returnCourses_whenRightConnection() {
        List<Course> expected = List.of();

        List<Course> result = courseDaoImpl.findAll();

        assertEquals(expected, result);

    }

    @Test
    void save_addCourseToSql() {
        Integer testId = 1;
        String testCourseName = "TEST-" + System.currentTimeMillis();
        String testCourseDescription = "TEST-" + System.currentTimeMillis() + 22;

        Course newCourse = new Course(testId,testCourseName,testCourseDescription);

        List<Course> expected = List.of(new Course(testId,testCourseName, testCourseDescription));

        courseDaoImpl.save(newCourse);

        List<Course> result = courseDaoImpl.findAll();

        assertEquals(expected,result);
    }

}





