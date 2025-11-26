package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl implements GroupDao{

    private static final DBConnection DB_CONNECTION = new DBConnection();

    private static final String findAllSql = "SELECT * FROM groups";
    private static final String saveSql = "INSERT INTO groups(group_name) VALUES(?)";

    public List<Group> findAll() {
        try (Connection connection = DB_CONNECTION.getConnection()) {

            List<Group> result = new ArrayList<>();

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(findAllSql);

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
        try (Connection connection = DB_CONNECTION.getConnection()) {

            PreparedStatement st = connection.prepareStatement(saveSql);

            st.setString(1, group.getGroupName());

            st.executeUpdate();

        } catch (SQLException exeption) {
            throw new DaoException(exeption.getMessage());
        }

    }
}
