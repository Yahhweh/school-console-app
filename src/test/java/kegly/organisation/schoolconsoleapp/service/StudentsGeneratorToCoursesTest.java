package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class StudentsGeneratorToCoursesTest {

    private StudentDao mockStudentDao;
    private CourseDao mockCourseDao;
    private StudentsToCoursesGenerator studentsToCoursesGenerator;

    private static final int STUDENTS_AMOUNT = 200;
    private static final int COURSES_AMOUNT = 10;
    private static final int MIN_COURSES = 1;
    private static final int MAX_COURSES = 3;

    @BeforeEach
    void setup() {
        mockStudentDao = mock(StudentDao.class);
        mockCourseDao = mock(CourseDao.class);

        studentsToCoursesGenerator = new StudentsToCoursesGenerator(
            mockStudentDao,
            mockCourseDao,
            MIN_COURSES,
            MAX_COURSES
        );
    }

    @Test
    void generate_returnAtLeast200Connections_whenDataIsRight() {
        List<Student> mockStudents = createMockStudents(STUDENTS_AMOUNT);
        List<Course> mockCourses = createMockCourses(COURSES_AMOUNT);

        when(mockStudentDao.findAll()).thenReturn(mockStudents);
        when(mockCourseDao.findAll()).thenReturn(mockCourses);

        studentsToCoursesGenerator.generate(STUDENTS_AMOUNT);
        verify(mockStudentDao, atLeast(STUDENTS_AMOUNT * MIN_COURSES))
            .addCourseToStudent(anyInt(), anyInt());

        verify(mockStudentDao, atMost(STUDENTS_AMOUNT * MAX_COURSES))
            .addCourseToStudent(anyInt(), anyInt());
    }

    private List<Student> createMockStudents(int count) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Student s = mock(Student.class);
            when(s.getId()).thenReturn(i + 1);
            students.add(s);
        }
        return students;
    }

    private List<Course> createMockCourses(int count) {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Course c = mock(Course.class);
            when(c.getId()).thenReturn(i + 1);
            courses.add(c);
        }
        return courses;
    }
}