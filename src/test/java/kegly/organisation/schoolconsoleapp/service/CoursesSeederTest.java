package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class CoursesSeederTest {

    private CourseDaoImpl mockCourseDaoImpl;
    private CoursesSeeder coursesSeeder;
    private static final int coursesAmount = 10;

    @BeforeEach
    void setup() {
        mockCourseDaoImpl = mock(CourseDaoImpl.class);
        coursesSeeder = new CoursesSeeder(mockCourseDaoImpl);
    }

    @Test
    void generate_shouldCreateExactAmountOfCourses_whenCorrectData() {

        when(mockCourseDaoImpl.findAll()).thenReturn(List.of());

        coursesSeeder.generate(coursesAmount);

        verify(mockCourseDaoImpl, times(10)).save(any(Course.class));
    }

}