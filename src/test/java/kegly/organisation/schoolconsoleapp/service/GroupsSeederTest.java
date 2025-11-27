package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class InitialGroupsTest {

    private GroupDaoImpl mockGroupDaoImpl;
    private InitialGroups initialGroups;

    @BeforeEach
    void setup() {
        mockGroupDaoImpl = mock(GroupDaoImpl.class);
        initialGroups = new InitialGroups(mockGroupDaoImpl);
    }

    @Test
    void generate_shouldCreateExactAmountOfGroupsWithCorrectFormat() {

        initialGroups.generate();

        verify(mockGroupDaoImpl, times(10)).save(any(Group.class));
    }
}