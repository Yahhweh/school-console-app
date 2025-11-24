package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class InitialStudentsToCoursesTest {

    StudentDao mockStudentDao;
    InitialStudentsToCourses studentsToCourses;

    @BeforeEach
    void setup() {
        mockStudentDao = mock(StudentDao.class);
        studentsToCourses = new InitialStudentsToCourses(mockStudentDao);
    }


    @Test
    void generate_returnAtLeast210Connections_whenDataIsRight() {

        studentsToCourses.generate();

        verify(mockStudentDao, atLeast(210)).addCourseToStudent(any(Integer.class), any(Integer.class));
    }

}