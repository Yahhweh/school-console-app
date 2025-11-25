package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class InitialGroupsTest {

    private GroupDao mockGroupDao;
    private InitialGroups initialGroups;

    @BeforeEach
    void setup() {
        mockGroupDao = mock(GroupDao.class);
        initialGroups = new InitialGroups(mockGroupDao);
    }

    @Test
    void generate_shouldCreateExactAmountOfGroupsWithCorrectFormat() {

        initialGroups.generate();

        verify(mockGroupDao, times(10)).save(any(Group.class));
    }
}