package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoImplTest {

    private DBConnection DBConnection;
    private GroupDaoImpl groupDaoImpl;

    @BeforeEach
    void setup() {
        DBConnection = new DBConnection();
        groupDaoImpl = new GroupDaoImpl();
        final String initialSql = "schema.sql";

        try (Connection conn = DBConnection.getConnection()) {
            SchemaLoader initializer = new SchemaLoader();
            initializer.runScript(conn, initialSql);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Test
    void getGroups_returnGroups_whenRightConnection() {
        List<Group> expected = List.of();

        List<Group> result = groupDaoImpl.findAll();

        assertEquals(expected, result);

    }

    @Test
    void addGroup_addGroupToSql() {
        String testGroupName = "TEST-" + System.currentTimeMillis();
        Group newGroup = new Group(testGroupName);

        List<Group> expected = List.of(new Group(1, testGroupName));

        groupDaoImpl.save(newGroup);

        List<Group> result = groupDaoImpl.findAll();

        assertEquals(expected,result);


    }
    }





