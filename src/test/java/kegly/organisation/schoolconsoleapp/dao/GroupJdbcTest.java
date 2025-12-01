package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.dao.jdbc.GroupJdbc;
import kegly.organisation.schoolconsoleapp.dao.jdbc.StudentJdbc;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupJdbcTest {

    private DBConnection dBConnection;
    private GroupJdbc groupJdbc;
    private StudentJdbc studentJdbc;
    private static final String dropAll = "DROP ALL OBJECTS";

    @BeforeEach
    void setup() {
        dBConnection = new DBConnection(){
            @Override
            public Connection getConnection(){
                return super.getTestConnection();
            }
        };
        groupJdbc = new GroupJdbc(dBConnection);
        studentJdbc = new StudentJdbc(dBConnection);
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

        List<Group> result = groupJdbc.findAll();

        assertEquals(expected, result);

    }

    @Test
    void addGroup_addGroupToSql() {
        String testGroupName = "TEST-" + System.currentTimeMillis();
        Group newGroup = new Group(testGroupName);

        List<Group> expected = List.of(new Group(1, testGroupName));

        groupJdbc.save(newGroup);

        List<Group> result = groupJdbc.findAll();

        assertEquals(expected,result);
    }

    @Test
    void findGroupsWithLessOrEqualStudents_returnsFilteredGroups_whenTwoStudents() {
        try (Connection connection = dBConnection.getConnection();
             PreparedStatement st1 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (100, 'Group A')");
             PreparedStatement st2 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (200, 'Group B')")) {

            st1.executeUpdate();
            st2.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }

        Student s1 = new Student(1, 100, "Student", "One");
        Student s2 = new Student(2, 100, "Student", "Two");
        Student s3 = new Student(3, 200, "Student", "Three");

        studentJdbc.save(s1);
        studentJdbc.save(s2);
        studentJdbc .save(s3);

        List<Group> result = groupJdbc.findGroupsWithLessOrEqualStudents(1);

        assertEquals(1, result.size());
        assertEquals("Group B", result.get(0).getGroupName());
    }
    }





