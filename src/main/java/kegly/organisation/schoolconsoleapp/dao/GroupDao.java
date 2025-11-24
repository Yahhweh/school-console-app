


package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {

    private static final ConnectionClass connectionClass = new ConnectionClass();

    public List<Group> findAll() {
        String sql = "SELECT * FROM groups";
        try (Connection connection = connectionClass.getConnection()) {

            List<Group> result = new ArrayList<>();

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Integer groupId = rs.getObject("group_id", Integer.class);
                String groupName = rs.getString("group_name");

                result.add(new Group(groupId, groupName));
            }

            return result;

        } catch (SQLException exeption) {
            throw new DaoException(exeption.toString());
        }
    }

    public void save(Group group) {
        String sql = "Insert into groups(group_name) values(?)";
        try (Connection connection = connectionClass.getConnection()) {

            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, group.getGroup_name());

            st.executeUpdate();

        } catch (SQLException exeption) {
            throw new DaoException(exeption.getMessage());
        }

    }

    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = connectionClass.getConnection()) {

            String sql = """
            Select g.group_id, g.group_name
            From groups g
            Left Join students s On g.group_id = s.group_id
            Group by g.group_id, g.group_name
            Having Count(s.student_id) <= ?
            """;

            PreparedStatement st = connection.prepareStatement(sql);

            st.setInt(1, count);

            try (ResultSet resultSet = st.executeQuery()) {
                while (resultSet.next()) {
                    groups.add(new Group(
                        resultSet.getString("group_name")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return groups;
    }
}
