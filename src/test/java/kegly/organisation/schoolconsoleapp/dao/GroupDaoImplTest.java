package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoImplTest {

    private DBConnection dBConnection;
    private GroupDaoImpl groupDaoImpl;
    private static final String dropAll = "DROP ALL OBJECTS";

    @BeforeEach
    void setup() {
        dBConnection = new DBConnection(){
            @Override
            public Connection getConnection(){
                return super.getTestConnection();
            }
        };
        groupDaoImpl = new GroupDaoImpl(dBConnection);
        final String initialSql = "schema.sql";

        try (Connection conn = dBConnection.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute(dropAll);
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





