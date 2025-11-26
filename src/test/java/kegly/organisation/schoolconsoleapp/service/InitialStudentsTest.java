package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDaoImpl;
import kegly.organisation.schoolconsoleapp.dao.StudentDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class InitialStudentsTest {

    StudentDaoImpl mockStudent;
    GroupDaoImpl mockGroup;
    InitialStudents initialStudents;

    @BeforeEach
    void setup() {
        mockStudent = mock(StudentDaoImpl.class);
        mockGroup = mock(GroupDaoImpl.class);
        initialStudents = new InitialStudents(mockStudent, mockGroup);
    }

    @Test
    public void randomCourses() {
        initialStudents.generate();
        verify(mockStudent, times(200)).save(any(Student.class));
    }
}