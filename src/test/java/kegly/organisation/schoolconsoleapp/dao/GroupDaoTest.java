package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
import kegly.organisation.schoolconsoleapp.db.DatabaseInitializer;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoTest {

    private ConnectionClass connectionClass;
    private GroupDao groupDao;

    @BeforeEach
    void setup() {
        connectionClass = new ConnectionClass("ds-connection.properties");
        groupDao = new GroupDao(connectionClass);

        try (Connection conn = connectionClass.getConnection()) {
            DatabaseInitializer initializer = new DatabaseInitializer("schema.sql");
            initializer.runScript(conn);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Test
    void getGroups_returnGroups_whenRightConnection() {
        List<Group> expected = List.of();

        List<Group> result = groupDao.findAll();

        assertEquals(expected, result);

    }

    @Test
    void addGroup_addGroupToSql() {
        String testGroupName = "TEST-" + System.currentTimeMillis();
        Group newGroup = new Group(testGroupName);

        List<Group> expected = List.of(new Group(testGroupName));

        groupDao.save(newGroup);

        List<Group> result = groupDao.findAll();

        assertEquals(expected,result);


    }

    @Test
    void findGroupsWithLessOrEqualStudents_returnEmptyGroup_whenLimitIsZero() {
        String uniqueName = "Test-Empty-" + System.currentTimeMillis();
        Group emptyGroup = new Group(uniqueName);

        List<Group> expected = List.of(emptyGroup);

        groupDao.save(emptyGroup);

        List<Group> result = groupDao.findGroupsWithLessOrEqualStudents(1);


        assertEquals(expected, result);

    }

}





