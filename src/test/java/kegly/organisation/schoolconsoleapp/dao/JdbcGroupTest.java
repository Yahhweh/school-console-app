package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroup;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudent;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcGroupTest {

    private DBConnection dbConnection;
    private JdbcGroup jdbcGroup;
    private JdbcStudent jdbcStudent;
    private static final String dropAll = "DROP ALL OBJECTS";

    @BeforeEach
    void setup() {
        dbConnection = new DBConnection("testApplication.properties");
        jdbcGroup = new JdbcGroup(dbConnection);
        jdbcStudent = new JdbcStudent(dbConnection);

        final String initialSql = "schema.sql";

        try (Connection conn = dbConnection.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute("DROP ALL OBJECTS");

            SchemaLoader initializer = new SchemaLoader();
            initializer.runScript(conn, initialSql);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Test
    void getGroups_returnGroups_whenRightConnection() {
        List<Group> expected = List.of();

        List<Group> result = jdbcGroup.findAll();

        assertEquals(expected, result);

    }

    @Test
    void addGroup_addGroupToSql() {
        String testGroupName = "TEST-" + System.currentTimeMillis();
        Group newGroup = new Group(testGroupName);

        List<Group> expected = List.of(new Group(1, testGroupName));

        jdbcGroup.save(newGroup);

        List<Group> result = jdbcGroup.findAll();

        assertEquals(expected,result);
    }

    @Test
    void findGroupsWithLessOrEqualStudents_returnsFilteredGroups_whenTwoStudents() {
        try (Connection connection = dbConnection.getConnection();
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

        jdbcStudent.save(s1);
        jdbcStudent.save(s2);
        jdbcStudent.save(s3);

        List<Group> result = jdbcGroup.findGroupsWithLessOrEqualStudents(1);

        assertEquals(1, result.size());
        assertEquals("Group B", result.get(0).getName());
    }
    }





