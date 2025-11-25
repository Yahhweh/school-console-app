package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InitialCoursesTest {

    private CourseDao mockCourseDao;
    private InitialCourses initialCourses;

    @BeforeEach
    void setup() {
        mockCourseDao = mock(CourseDao.class);
        initialCourses = new InitialCourses(mockCourseDao);
    }

    @Test
    void generate_shouldCreateExactAmountOfCourses_whenCorrectData() {

        when(mockCourseDao.findAll()).thenReturn(List.of());

        initialCourses.generate();

        verify(mockCourseDao, times(10)).save(any(Course.class));
    }

}