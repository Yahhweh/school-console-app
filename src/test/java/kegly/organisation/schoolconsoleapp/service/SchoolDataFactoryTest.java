package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
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
class SchoolDataFactoryTest {

    @Mock
    private StudentDao studentDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private CourseDao courseDao;
    @Mock
    private ConnectionClass connectionClass;

    private SchoolDataFactory schoolDataFactory;

    @BeforeEach
    void setUp() {
        schoolDataFactory = new SchoolDataFactory();
        injectMock("studentDao", studentDao);
        injectMock("groupDao", groupDao);
        injectMock("courseDao", courseDao);
        injectMock("connectionClass", connectionClass);
    }

    private void injectMock(String fieldName, Object mock) {
        try {
            Field field = SchoolDataFactory.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(schoolDataFactory, mock);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock for field: " + fieldName, e);
        }
    }

    @Test
    void findGroupsWithLessOrEqualStudents_returnGroupsList_whenCountIsProvided() {
        int count = 15;
        List<Group> expected = Collections.singletonList(new Group(1, "Group A"));
        when(groupDao.findGroupsWithLessOrEqualStudents(count)).thenReturn(expected);

        List<Group> result = schoolDataFactory.findGroupsWithLessOrEqualStudents(count);

        verify(groupDao).findGroupsWithLessOrEqualStudents(count);
        assertEquals(expected, result);
    }

    @Test
    void findStudentsByCourseName_returnStudentList_whenCourseNameIsRight() {
        String courseName = "Math";
        List<Student> expected = Collections.singletonList(new Student(1, 1, "John", "Doe"));
        when(studentDao.findStudentsByCourseName(courseName)).thenReturn(expected);

        List<Student> result = schoolDataFactory.findStudentsByCourseName(courseName);

        verify(studentDao).findStudentsByCourseName(courseName);
        assertEquals(expected, result);
    }

    @Test
    void addNewStudent_callDaoSave_whenStudentDataIsProvided() {
        String firstName = "Alice";
        String lastName = "Wonderland";
        Integer groupId = 10;

        schoolDataFactory.addNewStudent(firstName, lastName, groupId);

        verify(studentDao).save(any(Student.class));
    }

    @Test
    void deleteStudentById_callDaoDelete_whenIdIsProvided() {
        int studentId = 5;

        schoolDataFactory.deleteStudentById(studentId);

        verify(studentDao).deleteById(studentId);
    }

    @Test
    void addStudentToCourse_callDaoAdd_whenIdsAreRight() {
        int studentId = 1;
        int courseId = 2;

        schoolDataFactory.addStudentToCourse(studentId, courseId);

        verify(studentDao).addCourseToStudent(studentId, courseId);
    }

    @Test
    void removeStudentFromCourse_callDaoRemove_whenIdsAreRight() {
        int studentId = 1;
        int courseId = 2;

        schoolDataFactory.removeStudentFromCourse(studentId, courseId);

        verify(studentDao).removeStudentFromCourse(studentId, courseId);
    }
}