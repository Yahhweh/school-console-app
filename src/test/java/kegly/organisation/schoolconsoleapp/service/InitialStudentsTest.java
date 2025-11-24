package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class InitialStudentsTest {

    StudentDao mockStudent;
    GroupDao mockGroup;
    InitialStudents initialStudents;

    @BeforeEach
    void setup() {
        mockStudent = mock(StudentDao.class);
        mockGroup = mock(GroupDao.class);
        initialStudents = new InitialStudents(mockStudent, mockGroup);
    }

    @Test
    public void randomCourses() {
        initialStudents.generate();
        verify(mockStudent, times(200)).save(any(Student.class));
    }
}