package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoTest extends AbstractDaoTest {

    private static final String SQL_DATA = "datasets/group_dao.sql";
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
    void findWithLessOrEqualStudents_ReturnsMatchingGroups() throws SQLException {
        SchemaLoader schemaLoader = new SchemaLoader();
        schemaLoader.runScript(connectionProvider.getConnection(), SQL_DATA);

        List<Group> result = groupDao.findWithLessOrEqualStudents(1);

        assertEquals(1, result.size());
        assertEquals("Group B", result.get(0).getName());
        assertEquals(200, result.get(0).getId());
    }
}