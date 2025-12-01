package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.GroupJdbc;
import kegly.organisation.schoolconsoleapp.dao.jdbc.StudentJdbc;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class StudentsSeederTest {

    StudentJdbc mockStudent;
    GroupJdbc mockGroup;
    StudentsSeeder studentsSeeder;
    private static final int studentsAmount = 200;


    @BeforeEach
    void setup() {
        mockStudent = mock(StudentJdbc.class);
        mockGroup = mock(GroupJdbc.class);
        studentsSeeder = new StudentsSeeder(mockStudent, mockGroup);
    }

    @Test
    public void randomCourses() {
        studentsSeeder.generate(studentsAmount);
        verify(mockStudent, times(200)).save(any(Student.class));
    }
}