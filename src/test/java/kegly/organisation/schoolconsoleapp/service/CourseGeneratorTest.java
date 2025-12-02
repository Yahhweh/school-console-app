package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourse;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class CourseGeneratorTest {

    private JdbcCourse mockJdbcCourse;
    private CourseGenerator courseGenerator;
    private static final int coursesAmount = 10;

    @BeforeEach
    void setup() {
        mockJdbcCourse = mock(JdbcCourse.class);
        courseGenerator = new CourseGenerator(mockJdbcCourse);
    }

    @Test
    void generate_shouldCreateExactAmountOfCourses_whenCorrectData() {

        when(mockJdbcCourse.findAll()).thenReturn(List.of());

        courseGenerator.generate(coursesAmount);

        verify(mockJdbcCourse, times(10)).save(any(Course.class));
    }

}