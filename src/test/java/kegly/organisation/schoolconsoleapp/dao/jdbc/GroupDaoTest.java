package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.DaoException;
import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoTest extends AbstractDaoTest {

    private GroupDao groupDao;

    @BeforeEach
    void setUp() {
        this.groupDao = new JdbcGroupDao(super.connectionProvider);
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoGroupsExist() {
        List<Group> expected = List.of();

        List<Group> result = groupDao.findAll();

        assertEquals(expected, result);
    }

    @Test
    void save_PersistsGroup_WhenGroupIsNew() {
        String testGroupName = "TEST-" + System.currentTimeMillis();
        Group newGroup = new Group(testGroupName);

        List<Group> expected = List.of(new Group(1, testGroupName));

        groupDao.save(newGroup);

        List<Group> result = groupDao.findAll();

        assertEquals(expected, result);
    }

    @Test
    void findWithLessOrEqualStudents_ReturnsMatchingGroups_WhenStudentCountIsBelowThreshold() {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement st1 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (100, 'Group A')");
             PreparedStatement st2 = connection.prepareStatement("INSERT INTO groups (group_id, group_name) VALUES (200, 'Group B')");
             PreparedStatement st4 = connection.prepareStatement("INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (2, 100, 'Student', 'Two')");
             PreparedStatement st3 = connection.prepareStatement("INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (1, 100, 'Student', 'One')");
             PreparedStatement st5 = connection.prepareStatement("INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (3, 200, 'Student', 'Three')")) {

            st1.executeUpdate();
            st2.executeUpdate();
            st3.executeUpdate();
            st4.executeUpdate();
            st5.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }

        List<Group> result = groupDao.findWithLessOrEqualStudents(1);

        assertEquals(1, result.size());
        assertEquals("Group B", result.get(0).getName());
    }
}