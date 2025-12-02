package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroup;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GroupsGeneratorTest {

    private JdbcGroup mockJdbcGroup;
    private GroupsGenerator groupsGenerator;
    private static final int groupsAmount = 10;
    @BeforeEach
    void setup() {
        mockJdbcGroup = mock(JdbcGroup.class);
        groupsGenerator = new GroupsGenerator(mockJdbcGroup);
    }

    @Test
    void generate_shouldCreateExactAmountOfGroupsWithCorrectFormat() {

        groupsGenerator.generate(groupsAmount);

        verify(mockJdbcGroup, times(10)).save(any(Group.class));
    }
}