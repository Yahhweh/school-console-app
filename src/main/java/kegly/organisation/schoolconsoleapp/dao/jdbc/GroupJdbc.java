package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupJdbc implements GroupDao {

    private final DBConnection dBConnection;

    private static final String FIND_ALL_SQL= "SELECT * FROM groups";
    private static final String SAVE_SQL= "INSERT INTO groups(group_name) VALUES(?)";

    private static final String findGroupsWithLessOrEqualStudentsSql = """
    SELECT g.group_id, g.group_name
    FROM groups g
    LEFT JOIN students s ON g.group_id = s.group_id
    GROUP BY g.group_id, g.group_name
    HAVING COUNT(s.student_id) <= ?
""";

    public GroupJdbc(DBConnection dbConnection) {
        this.dBConnection = dbConnection;
    }

    @Override
    public List<Group> findAll() {
        List<Group> result = new ArrayList<>();

        try (Connection connection = dBConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Integer groupId = rs.getObject("group_id", Integer.class);
                String groupName = rs.getString("group_name");

                result.add(new Group(groupId, groupName));
            }
            return result;

        } catch (SQLException exception) {
            throw new DaoException(exception.toString());
        }
    }

    @Override
    public void save(Group group) {
        try (Connection connection = dBConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(SAVE_SQL)) {

            st.setString(1, group.getGroupName());
            st.executeUpdate();

        } catch (SQLException exception) {
            throw new DaoException(exception.getMessage());
        }
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = dBConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(findGroupsWithLessOrEqualStudentsSql)) {

            st.setInt(1, count);

            try (ResultSet resultSet = st.executeQuery()) {
                while (resultSet.next()) {
                    groups.add(new Group(
                        resultSet.getInt("group_id"),
                        resultSet.getString("group_name")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find groups with student count constraint", e);
        }
        return groups;
    }
}