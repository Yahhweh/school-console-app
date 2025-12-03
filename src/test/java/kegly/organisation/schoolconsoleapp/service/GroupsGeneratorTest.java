package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroupDao;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GroupsGeneratorTest {

    private JdbcGroupDao mockJdbcGroupDao;
    private GroupsGenerator groupsGenerator;
    private static final int groupsAmount = 10;
    @BeforeEach
    void setup() {
        mockJdbcGroupDao = mock(JdbcGroupDao.class);
        groupsGenerator = new GroupsGenerator(mockJdbcGroupDao);
    }

    @Test
    void generate_shouldCreateExactAmountOfGroupsWithCorrectFormat() {

        groupsGenerator.generate(groupsAmount);

        verify(mockJdbcGroupDao, times(10)).save(any(Group.class));
    }
}