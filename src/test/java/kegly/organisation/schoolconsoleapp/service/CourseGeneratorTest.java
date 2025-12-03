package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourseDao;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class CourseGeneratorTest {

    private JdbcCourseDao mockJdbcCourseDao;
    private CourseGenerator courseGenerator;
    private static final int coursesAmount = 10;

    @BeforeEach
    void setup() {
        mockJdbcCourseDao = mock(JdbcCourseDao.class);
        courseGenerator = new CourseGenerator(mockJdbcCourseDao);
    }

    @Test
    void generate_shouldCreateExactAmountOfCourses_whenCorrectData() {

        when(mockJdbcCourseDao.findAll()).thenReturn(List.of());

        courseGenerator.generate(coursesAmount);

        verify(mockJdbcCourseDao, times(10)).save(any(Course.class));
    }

}