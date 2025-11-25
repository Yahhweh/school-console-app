package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    private final DBConnection DBConnection;

    public CourseDao(DBConnection DBConnection) {
        this.DBConnection = DBConnection;
    }

    public List<Course> findAll() {
        String sql = "SELECT * FROM courses";
        try (Connection connection = DBConnection.getConnection()) {

            List<Course> result = new ArrayList<>();

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String course_name = rs.getString("course_name");
                String course_description = rs.getString("course_description");
                result.add(new Course(course_name, course_description));
            }
            return result;

        } catch (SQLException exeption) {
            throw new DaoException(exeption.toString());
        }
    }

    public void save(Course course) {
        String sql = "Insert into courses(course_name, course_description) values(?, ?)";
        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, course.getCourse_name());
            st.setString(2, course.getCourse_description());

            st.executeUpdate();

        } catch (SQLException exeption) {
            throw new DaoException(exeption.getMessage());
        }

    }
}