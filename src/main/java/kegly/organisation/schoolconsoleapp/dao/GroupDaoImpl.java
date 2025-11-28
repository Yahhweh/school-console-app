package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl implements GroupDao {

    private final DBConnection dBConnection;

    private static final String findAllSql = "SELECT * FROM groups";
    private static final String saveSql = "INSERT INTO groups(group_name) VALUES(?)";

    public GroupDaoImpl(DBConnection dbConnection) {
        this.dBConnection = dbConnection;
    }

    @Override
    public List<Group> findAll() {
        List<Group> result = new ArrayList<>();

        try (Connection connection = dBConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(findAllSql);
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
             PreparedStatement st = connection.prepareStatement(saveSql)) {

            st.setString(1, group.getGroupName());
            st.executeUpdate();

        } catch (SQLException exception) {
            throw new DaoException(exception.getMessage());
        }
    }
}