package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
import kegly.organisation.schoolconsoleapp.db.DatabaseInitializer;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.mockito.Mock;

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