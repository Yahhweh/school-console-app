package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroupDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudentDao;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class StudentsGeneratorTest {

    StudentDao mockStudent;
    GroupDao mockGroup;
    StudentsGenerator studentsGenerator;
    private static final int studentsAmount = 200;


    @BeforeEach
    void setup() {
        mockStudent = mock(JdbcStudentDao.class);
        mockGroup = mock(JdbcGroupDao.class);
        studentsGenerator = new StudentsGenerator(mockStudent, mockGroup);
    }

    @Test
    public void generate_shouldSaveStudents() {
        studentsGenerator.generate(studentsAmount);
        verify(mockStudent, times(200)).save(any(Student.class));
    }
}