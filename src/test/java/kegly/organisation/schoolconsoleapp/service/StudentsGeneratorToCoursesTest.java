package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentsGeneratorToCoursesTest {

    @Mock
    private StudentDao mockStudentDao;
    @Mock
    private CourseDao mockCourseDao;
    private StudentsToCoursesGenerator studentsToCoursesGenerator;

    private static final int STUDENTS_AMOUNT = 200;
    private static final int COURSES_AMOUNT = 10;
    private static final int MIN_COURSES = 1;
    private static final int MAX_COURSES = 3;

    @BeforeEach
    void setup() {

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

    @Test
    void generate_stopsAtLimit_whenAmountIsLessThanTotalStudents() {
        int totalStudentsInDb = 20;
        int requestedGenerationAmount = 5;

        List<Student> students = createRealStudents(totalStudentsInDb);
        List<Course> courses = createRealCourses(COURSES_AMOUNT);

        when(mockStudentDao.findAll()).thenReturn(students);
        when(mockCourseDao.findAll()).thenReturn(courses);

        studentsToCoursesGenerator.generate(requestedGenerationAmount);

        ArgumentCaptor<Integer> studentIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockStudentDao, atLeastOnce()).addCourseToStudent(studentIdCaptor.capture(), anyInt());

        long uniqueStudentsProcessed = studentIdCaptor.getAllValues().stream()
            .distinct()
            .count();

        assertEquals(requestedGenerationAmount, uniqueStudentsProcessed);
    }

    @Test
    void generate_doesNothing_whenNoCourses() {
        when(mockStudentDao.findAll()).thenReturn(createRealStudents(5));
        when(mockCourseDao.findAll()).thenReturn(Collections.emptyList());

        studentsToCoursesGenerator.generate(10);

        verify(mockStudentDao, never()).addCourseToStudent(anyInt(), anyInt());
    }

    @Test
    void generate_doesNothing_whenNoStudents() {
        when(mockStudentDao.findAll()).thenReturn(Collections.emptyList());
        when(mockCourseDao.findAll()).thenReturn(createRealCourses(5));

        studentsToCoursesGenerator.generate(10);

        verify(mockStudentDao, never()).addCourseToStudent(anyInt(), anyInt());
    }

    private List<Student> createRealStudents(int count) {
        return IntStream.rangeClosed(1, count)
            .mapToObj(i -> {
                Student s = new Student(1, "Student", "Number" + i);
                s.setId(i);
                return s;
            })
            .collect(Collectors.toList());
    }

    private List<Course> createRealCourses(int count) {
        return IntStream.rangeClosed(1, count)
            .mapToObj(i -> {
                Course c = new Course(i, "Course" + i, "Desc");
                c.setId(i);
                return c;
            })
            .collect(Collectors.toList());
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
