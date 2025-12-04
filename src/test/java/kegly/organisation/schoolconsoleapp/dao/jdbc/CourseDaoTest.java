package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDaoTest extends AbstractDaoTest {

    private CourseDao courseDao;

    @BeforeEach
    void setUp(){
        this.courseDao = new JdbcCourseDao(super.connectionProvider);
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoCoursesExist() {
        List<Course> expected = List.of();

        List<Course> result = courseDao.findAll();

        assertEquals(expected, result);
    }

    @Test
    void save_PersistsCourse_WhenCourseIsNew() {
        Integer testId = 1;
        String testCourseName = "TEST-" + System.currentTimeMillis();
        String testCourseDescription = "TEST-" + System.currentTimeMillis() + 22;

        Course newCourse = new Course(testId, testCourseName, testCourseDescription);

        List<Course> expected = List.of(new Course(testId, testCourseName, testCourseDescription));

        courseDao.save(newCourse);

        List<Course> result = courseDao.findAll();

        assertEquals(expected, result);
    }
}