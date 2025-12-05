package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentsGeneratorTest {

    @Mock
    StudentDao mockStudent;
    @Mock
    GroupDao mockGroup;
    @InjectMocks
    StudentsGenerator studentsGenerator;
    private static final int studentsAmount = 200;

    @Test
    public void generate_shouldSaveStudents() {
        studentsGenerator.generate(studentsAmount);
        verify(mockStudent, times(200)).save(any(Student.class));
    }

    @Test
    public void generate_shouldSaveNothing() {
        studentsGenerator.generate(0);
        verify(mockStudent, times(0)).save(any(Student.class));
    }

    @Test
    void assignRandomGroups_shouldDoNothing_whenNoGroups() {
        when(mockGroup.findAll()).thenReturn(Collections.emptyList());
        when(mockStudent.findAll()).thenReturn(new ArrayList<>(createStudents(10)));

        studentsGenerator.assignRandomGroups(1, 10);

        verify(mockStudent, never()).update(any());
    }

    @Test
    void assignRandomGroups_shouldDoNothing_whenNoStudents() {
        when(mockGroup.findAll()).thenReturn(createGroups(5));
        when(mockStudent.findAll()).thenReturn(new ArrayList<>());

        studentsGenerator.assignRandomGroups(1, 10);

        verify(mockStudent, never()).update(any());
    }


    private List<Student> createStudents(int count) {
        return IntStream.rangeClosed(1, count)
            .mapToObj(i -> new Student(i, null, "Student" + 1, "LastStudent" + 1))
            .collect(Collectors.toList());
    }

    private List<Group> createGroups(int count) {
        return IntStream.rangeClosed(1, count)
            .mapToObj(i -> new Group("Group" + 1))
            .collect(Collectors.toList());
    }
}