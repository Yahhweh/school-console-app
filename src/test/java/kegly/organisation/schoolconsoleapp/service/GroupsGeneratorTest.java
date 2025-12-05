package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroupDao;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupsGeneratorTest {

    @Mock
    private JdbcGroupDao mockJdbcGroupDao;
    @InjectMocks
    private GroupsGenerator groupsGenerator;
    private static final int groupsAmount = 10;

    @Test
    void generate_shouldCreateExactAmountOfGroupsWithCorrectFormat() {

        groupsGenerator.generate(groupsAmount);

        verify(mockJdbcGroupDao, times(10)).save(any(Group.class));
    }

    @Test
    void generate_shouldCreateNothing() {

        groupsGenerator.generate(0);
        verify(mockJdbcGroupDao, times(0)).save(any(Group.class));
    }


}