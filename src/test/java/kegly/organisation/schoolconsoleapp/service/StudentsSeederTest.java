package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDaoImpl;
import kegly.organisation.schoolconsoleapp.dao.StudentDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class StudentsSeederTest {

    StudentDaoImpl mockStudent;
    GroupDaoImpl mockGroup;
    StudentsSeeder studentsSeeder;
    private static final int studentsAmount = 200;


    @BeforeEach
    void setup() {
        mockStudent = mock(StudentDaoImpl.class);
        mockGroup = mock(GroupDaoImpl.class);
        studentsSeeder = new StudentsSeeder(mockStudent, mockGroup);
    }

    @Test
    public void randomCourses() {
        studentsSeeder.generate(studentsAmount);
        verify(mockStudent, times(200)).save(any(Student.class));
    }
}