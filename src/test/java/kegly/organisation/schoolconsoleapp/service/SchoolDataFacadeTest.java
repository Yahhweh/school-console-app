package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourseDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroupDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudentDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionProvider;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolDataFacadeTest {

    @Mock
    private JdbcStudentDao jdbcStudentDao;
    @Mock
    private JdbcGroupDao jdbcGroupDao;
    @Mock
    private JdbcCourseDao jdbcCourseDao;
    @Mock
    private ConnectionProvider connectionProvider;

    private SchoolDataFacade schoolDataFacade;

    @BeforeEach
    void setUp() {
        schoolDataFacade = new SchoolDataFacade();
        injectMock("jdbcStudentDao", jdbcStudentDao);
        injectMock("jdbcGroupDao", jdbcGroupDao);
        injectMock("jdbcCourseDao", jdbcCourseDao);
        injectMock("connectionProvider", connectionProvider);
    }

    private void injectMock(String fieldName, Object mock) {
        try {
            Field field = SchoolDataFacade.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(schoolDataFacade, mock);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock for field: " + fieldName, e);
        }
    }

    @Test
    void findGroupsWithLessOrEqualStudents_returnGroupsList_whenCountIsProvided() {
        int count = 15;
        List<Group> expected = Collections.singletonList(new Group(1, "Group A"));
        when(jdbcGroupDao.findWithLessOrEqualStudents(count)).thenReturn(expected);

        List<Group> result = schoolDataFacade.findGroupsWithLessOrEqualStudents(count);

        verify(jdbcGroupDao).findWithLessOrEqualStudents(count);
        assertEquals(expected, result);
    }

    @Test
    void findStudentsByCourseName_returnStudentList_whenCourseNameIsRight() {
        String courseName = "Math";
        List<Student> expected = Collections.singletonList(new Student(1, 1, "John", "Doe"));
        when(jdbcStudentDao.findByCourseName(courseName)).thenReturn(expected);

        List<Student> result = schoolDataFacade.findByCourseName(courseName);

        verify(jdbcStudentDao).findByCourseName(courseName);
        assertEquals(expected, result);
    }

    @Test
    void addNewStudent_callDaoSave_whenStudentDataIsProvided() {
        String firstName = "Alice";
        String lastName = "Wonderland";
        Integer groupId = 10;

        schoolDataFacade.addNewStudent(firstName, lastName, groupId);

        verify(jdbcStudentDao).save(any(Student.class));
    }

    @Test
    void deleteStudentById_callDaoDelete_whenIdIsProvided() {
        int studentId = 5;

        schoolDataFacade.deleteStudentById(studentId);

        verify(jdbcStudentDao).deleteById(studentId);
    }

    @Test
    void addStudentToCourse_callDaoAdd_whenIdsAreRight() {
        int studentId = 1;
        int courseId = 2;

        schoolDataFacade.addStudentToCourse(studentId, courseId);

        verify(jdbcStudentDao).addCourseToStudent(studentId, courseId);
    }

    @Test
    void removeStudentFromCourse_callDaoRemove_whenIdsAreRight() {
        int studentId = 1;
        int courseId = 2;

        schoolDataFacade.removeStudentFromCourse(studentId, courseId);

        verify(jdbcStudentDao).removeFromCourse(studentId, courseId);
    }
}