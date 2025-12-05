package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourseDao;
import kegly.organisation.schoolconsoleapp.entity.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseGeneratorTest {

    @Mock
    private JdbcCourseDao mockJdbcCourseDao;
    @InjectMocks
    private CourseGenerator courseGenerator;
    private static final int coursesAmount = 10;

    @Test
    void generate_shouldCreateExactAmountOfCourses_whenCorrectData() {

        courseGenerator.generate(coursesAmount);

        verify(mockJdbcCourseDao, times(10)).save(any(Course.class));
    }

    @Test
    void generate_shouldDoNothing_whenAmountIsZero() {
        courseGenerator.generate(0);

        verify(mockJdbcCourseDao, never()).save(any());
    }

    @Test
    void generate_shouldThrowException_whenAmountExceedsAvailableSubjects() {
        int amount = 11;

        assertThrows(IndexOutOfBoundsException.class, () -> {
            courseGenerator.generate(amount);
        });
    }
}
