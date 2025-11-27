package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GroupsSeederTest {

    private GroupDaoImpl mockGroupDaoImpl;
    private GroupsSeeder groupsSeeder;
    private static final int groupsAmount = 10;
    @BeforeEach
    void setup() {
        mockGroupDaoImpl = mock(GroupDaoImpl.class);
        groupsSeeder = new GroupsSeeder(mockGroupDaoImpl);
    }

    @Test
    void generate_shouldCreateExactAmountOfGroupsWithCorrectFormat() {

        groupsSeeder.generate(groupsAmount);

        verify(mockGroupDaoImpl, times(10)).save(any(Group.class));
    }
}