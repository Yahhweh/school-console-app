package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionProvider;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.dao.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcGroupDao implements GroupDao {

    private static final String FIND_ALL_SQL = "SELECT * FROM groups";
    private static final String SAVE_SQL     = "INSERT INTO groups(group_name) VALUES(?)";

    private static final String FIND_WITH_LESS_OR_EQUAL_STUDENTS_SQL = """
        SELECT g.group_id, g.group_name
        FROM groups g
        LEFT JOIN students s ON g.group_id = s.group_id
        GROUP BY g.group_id, g.group_name
        HAVING COUNT(s.student_id) <= ?
        """;

    private final ConnectionProvider dBConnectionProvider;

    public JdbcGroupDao(ConnectionProvider dBConnectionProvider) {
        this.dBConnectionProvider = dBConnectionProvider;
    }

    @Override
    public List<Group> findAll() {
        List<Group> result = new ArrayList<>();

        try (Connection connection = dBConnectionProvider.getConnection();
             PreparedStatement st = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;

        } catch (SQLException exception) {
            throw new DaoException(exception.toString());
        }
    }

    @Override
    public void save(Group group) {
        try (Connection connection = dBConnectionProvider.getConnection();
             PreparedStatement st = connection.prepareStatement(SAVE_SQL)) {

            st.setString(1, group.getName());
            st.executeUpdate();

        } catch (SQLException exception) {
            throw new DaoException(exception.getMessage());
        }
    }

    @Override
    public List<Group> findWithLessOrEqualStudents(int count) {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = dBConnectionProvider.getConnection();
             PreparedStatement st = connection.prepareStatement(FIND_WITH_LESS_OR_EQUAL_STUDENTS_SQL)) {

            st.setInt(1, count);

            try (ResultSet resultSet = st.executeQuery()) {
                while (resultSet.next()) {
                    groups.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find groups with student count constraint", e);
        }
        return groups;
    }

    private Group mapRow(ResultSet rs) throws SQLException {
        Integer groupId = rs.getObject("group_id", Integer.class);
        String groupName = rs.getString("group_name");
        return new Group(groupId, groupName);
    }
}