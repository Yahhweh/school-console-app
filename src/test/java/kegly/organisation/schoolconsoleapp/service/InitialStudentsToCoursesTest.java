package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.StudentDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class InitialStudentsToCoursesTest {

    StudentDaoImpl mockStudentDaoImpl;
    InitialStudentsToCourses studentsToCourses;

    @BeforeEach
    void setup() {
        mockStudentDaoImpl = mock(StudentDaoImpl.class);
        studentsToCourses = new InitialStudentsToCourses(mockStudentDaoImpl);
    }


    @Test
    void generate_returnAtLeast210Connections_whenDataIsRight() {

        studentsToCourses.generate();

        verify(mockStudentDaoImpl, atLeast(210)).addCourseToStudent(any(Integer.class), any(Integer.class));
    }

}