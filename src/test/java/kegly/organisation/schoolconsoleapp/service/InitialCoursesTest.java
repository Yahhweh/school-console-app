package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InitialCoursesTest {

    private CourseDaoImpl mockCourseDaoImpl;
    private InitialCourses initialCourses;

    @BeforeEach
    void setup() {
        mockCourseDaoImpl = mock(CourseDaoImpl.class);
        initialCourses = new InitialCourses(mockCourseDaoImpl);
    }

    @Test
    void generate_shouldCreateExactAmountOfCourses_whenCorrectData() {

        when(mockCourseDaoImpl.findAll()).thenReturn(List.of());

        initialCourses.generate();

        verify(mockCourseDaoImpl, times(10)).save(any(Course.class));
    }

}