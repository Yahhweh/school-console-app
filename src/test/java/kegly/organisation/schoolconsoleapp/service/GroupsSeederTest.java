package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.GroupJdbc;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GroupsSeederTest {

    private GroupJdbc mockGroupJdbc;
    private GroupsSeeder groupsSeeder;
    private static final int groupsAmount = 10;
    @BeforeEach
    void setup() {
        mockGroupJdbc = mock(GroupJdbc.class);
        groupsSeeder = new GroupsSeeder(mockGroupJdbc);
    }

    @Test
    void generate_shouldCreateExactAmountOfGroupsWithCorrectFormat() {

        groupsSeeder.generate(groupsAmount);

        verify(mockGroupJdbc, times(10)).save(any(Group.class));
    }
}