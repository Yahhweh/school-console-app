package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroupDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudentDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionProvider;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcGroupDaoTest {

    private ConnectionProvider connectionProvider;
    private JdbcGroupDao jdbcGroupDao;
    private JdbcStudentDao jdbcStudentDao;
    private static final String dropAll = "DROP ALL OBJECTS";

    @BeforeEach
    void setup() throws IOException {
        connectionProvider = new ConnectionProvider("testApplication.properties");
        jdbcGroupDao = new JdbcGroupDao(connectionProvider);
        jdbcStudentDao = new JdbcStudentDao(connectionProvider);

        final String initialSql = "schema.sql";

        try (Connection conn = connectionProvider.getConnection();
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

        List<Group> result = jdbcGroupDao.findAll();

        assertEquals(expected, result);

    }

    @Test
    void addGroup_addGroupToSql() {
        String testGroupName = "TEST-" + System.currentTimeMillis();
        Group newGroup = new Group(testGroupName);

        List<Group> expected = List.of(new Group(1, testGroupName));

        jdbcGroupDao.save(newGroup);

        List<Group> result = jdbcGroupDao.findAll();

        assertEquals(expected,result);
    }

    @Test
    void findWithLessOrEqualStudents_returnsFiltered_whenTwoStudents() {
        try (Connection connection = connectionProvider.getConnection();
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

        jdbcStudentDao.save(s1);
        jdbcStudentDao.save(s2);
        jdbcStudentDao.save(s3);

        List<Group> result = jdbcGroupDao.findWithLessOrEqualStudents(1);

        assertEquals(1, result.size());
        assertEquals("Group B", result.get(0).getName());
    }
    }





