package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
import kegly.organisation.schoolconsoleapp.db.DatabaseInitializer;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

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