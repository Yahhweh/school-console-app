package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroup;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudent;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class StudentsGeneratorTest {

    JdbcStudent mockStudent;
    JdbcGroup mockGroup;
    StudentsGenerator studentsGenerator;
    private static final int studentsAmount = 200;


    @BeforeEach
    void setup() {
        mockStudent = mock(JdbcStudent.class);
        mockGroup = mock(JdbcGroup.class);
        studentsGenerator = new StudentsGenerator(mockStudent, mockGroup);
    }

    @Test
    public void randomCourses() {
        studentsGenerator.generate(studentsAmount);
        verify(mockStudent, times(200)).save(any(Student.class));
    }
}