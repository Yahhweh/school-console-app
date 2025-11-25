


package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {

    private static final DBConnection DB_CONNECTION = new DBConnection();

    public List<Group> findAll() {
        String sql = "SELECT * FROM groups";
        try (Connection connection = DB_CONNECTION.getConnection()) {

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
        try (Connection connection = DB_CONNECTION.getConnection()) {

            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, group.getGroup_name());

            st.executeUpdate();

        } catch (SQLException exeption) {
            throw new DaoException(exeption.getMessage());
        }

    }
}
