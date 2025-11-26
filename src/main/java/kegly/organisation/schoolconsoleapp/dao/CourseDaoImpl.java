package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements  CourseDao{

    private final DBConnection DBConnection;

    private static final String findAllSql = "SELECT * FROM courses";
    private static final String saveSql  = "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";

    public CourseDaoImpl(DBConnection DBConnection) {
        this.DBConnection = DBConnection;
    }

    public List<Course> findAll() {
        try (Connection connection = DBConnection.getConnection()) {

            List<Course> result = new ArrayList<>();

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery(findAllSql);

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
        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement st = connection.prepareStatement(saveSql);

            st.setString(1, course.getCourseName());
            st.setString(2, course.getCourseDescription());

            st.executeUpdate();

        } catch (SQLException exeption) {
            throw new DaoException(exeption.getMessage());
        }

    }
}