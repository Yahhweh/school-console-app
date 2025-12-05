package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CourseDaoTest extends AbstractDaoTest {

    private static final String SQL_DATA = "datasets/course_dao.sql";
    private CourseDao courseDao;

    @BeforeEach
    void setUp() {
        this.courseDao = new JdbcCourseDao(super.connectionProvider);
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoCoursesExist() {
        List<Course> result = courseDao.findAll();
        assertEquals(0, result.size());
    }

    @Test
    void save_PersistsCourse_WhenCourseIsNew() {
        String courseName = "Physics";
        String description = "Physics Description";

        Course newCourse = new Course(courseName, description);

        courseDao.save(newCourse);

        assertNotNull(newCourse.getId());

        List<Course> courses = courseDao.findAll();
        assertEquals(1, courses.size());

        Course savedCourse = courses.get(0);
        assertEquals(courseName, savedCourse.getName());
        assertEquals(description, savedCourse.getDescription());
        assertEquals(newCourse.getId(), savedCourse.getId());
    }

    @Test
    void findByStudentId_ReturnsCourses_WhenStudentHasEnrollments() throws SQLException {
        SchemaLoader schemaLoader = new SchemaLoader();
        schemaLoader.runScript(connectionProvider.getConnection(), SQL_DATA);

        List<Course> result = courseDao.findByStudentId(100);

        assertEquals(2, result.size());


        result.sort(Comparator.comparing(Course::getName));

        assertEquals("Biology", result.get(0).getName());
        assertEquals(11, result.get(0).getId());

        assertEquals("Math", result.get(1).getName());
        assertEquals(10, result.get(1).getId());
    }

    
}