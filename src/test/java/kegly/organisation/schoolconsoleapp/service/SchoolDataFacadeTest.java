package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDaoImpl;
import kegly.organisation.schoolconsoleapp.dao.GroupDaoImpl;
import kegly.organisation.schoolconsoleapp.dao.StudentDaoImpl;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
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
    private StudentDaoImpl studentDaoImpl;
    @Mock
    private GroupDaoImpl groupDaoImpl;
    @Mock
    private CourseDaoImpl courseDaoImpl;
    @Mock
    private DBConnection DBConnection;

    private SchoolDataFacade schoolDataFacade;

    @BeforeEach
    void setUp() {
        schoolDataFacade = new SchoolDataFacade();
        injectMock("studentDao", studentDaoImpl);
        injectMock("groupDao", groupDaoImpl);
        injectMock("courseDao", courseDaoImpl);
        injectMock("connectionClass", DBConnection);
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
        when(studentDaoImpl.findGroupsWithLessOrEqualStudents(count)).thenReturn(expected);

        List<Group> result = schoolDataFacade.findGroupsWithLessOrEqualStudents(count);

        verify(studentDaoImpl).findGroupsWithLessOrEqualStudents(count);
        assertEquals(expected, result);
    }

    @Test
    void findStudentsByCourseName_returnStudentList_whenCourseNameIsRight() {
        String courseName = "Math";
        List<Student> expected = Collections.singletonList(new Student(1, 1, "John", "Doe"));
        when(studentDaoImpl.findStudentsByCourseName(courseName)).thenReturn(expected);

        List<Student> result = schoolDataFacade.findStudentsByCourseName(courseName);

        verify(studentDaoImpl).findStudentsByCourseName(courseName);
        assertEquals(expected, result);
    }

    @Test
    void addNewStudent_callDaoSave_whenStudentDataIsProvided() {
        String firstName = "Alice";
        String lastName = "Wonderland";
        Integer groupId = 10;

        schoolDataFacade.addNewStudent(firstName, lastName, groupId);

        verify(studentDaoImpl).save(any(Student.class));
    }

    @Test
    void deleteStudentById_callDaoDelete_whenIdIsProvided() {
        int studentId = 5;

        schoolDataFacade.deleteStudentById(studentId);

        verify(studentDaoImpl).deleteById(studentId);
    }

    @Test
    void addStudentToCourse_callDaoAdd_whenIdsAreRight() {
        int studentId = 1;
        int courseId = 2;

        schoolDataFacade.addStudentToCourse(studentId, courseId);

        verify(studentDaoImpl).addCourseToStudent(studentId, courseId);
    }

    @Test
    void removeStudentFromCourse_callDaoRemove_whenIdsAreRight() {
        int studentId = 1;
        int courseId = 2;

        schoolDataFacade.removeStudentFromCourse(studentId, courseId);

        verify(studentDaoImpl).removeStudentFromCourse(studentId, courseId);
    }
}