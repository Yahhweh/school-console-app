package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.CourseJdbc;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class CoursesSeederTest {

    private CourseJdbc mockCourseJdbc;
    private CoursesSeeder coursesSeeder;
    private static final int coursesAmount = 10;

    @BeforeEach
    void setup() {
        mockCourseJdbc = mock(CourseJdbc.class);
        coursesSeeder = new CoursesSeeder(mockCourseJdbc);
    }

    @Test
    void generate_shouldCreateExactAmountOfCourses_whenCorrectData() {

        when(mockCourseJdbc.findAll()).thenReturn(List.of());

        coursesSeeder.generate(coursesAmount);

        verify(mockCourseJdbc, times(10)).save(any(Course.class));
    }

}