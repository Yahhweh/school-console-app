package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.DatabaseInitializer;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoTest {

    private DBConnection DBConnection;
    private GroupDao groupDao;

    @BeforeEach
    void setup() {
        DBConnection = new DBConnection();
        groupDao = new GroupDao();

        try (Connection conn = DBConnection.getConnection()) {
            DatabaseInitializer initializer = new DatabaseInitializer();
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

        List<Group> expected = List.of(new Group(1, testGroupName));

        groupDao.save(newGroup);

        List<Group> result = groupDao.findAll();

        assertEquals(expected,result);


    }
    }





