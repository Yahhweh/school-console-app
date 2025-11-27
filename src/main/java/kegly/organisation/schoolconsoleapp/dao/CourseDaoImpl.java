package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements  CourseDao {

    private final DBConnection DBConnection;

    private static final String findAllSql = "SELECT * FROM courses";
    private static final String saveSql = "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";
    private static final String findCoursesByStudentIdSql = "Select course_id from student_courses where(studentId = ?)";

    public CourseDaoImpl(DBConnection DBConnection) {
        this.DBConnection = DBConnection;
    }

    @Override
    public List<Course> findAll() {
        try (Connection connection = DBConnection.getConnection()) {

            List<Course> result = new ArrayList<>();

            PreparedStatement st = connection.prepareStatement(findAllSql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Integer course_id = rs.getObject("course_id", Integer.class);
                String course_name = rs.getString("course_name");
                String course_description = rs.getString("course_description");
                result.add(new Course(course_id, course_name, course_description));
            }
            return result;

        } catch (SQLException exeption) {
            throw new DaoException(exeption.toString());
        }
    }

    @Override
    public void save(Course course) {
        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement st = connection.prepareStatement(saveSql, PreparedStatement.RETURN_GENERATED_KEYS);

            st.setString(1, course.getCourseName());
            st.setString(2, course.getCourseDescription());

            st.executeUpdate();

            try (ResultSet resultSet = st.getGeneratedKeys()) {
                if (resultSet.next()) {
                    course.setCourseId(resultSet.getInt(1));
                } else {
                    throw new DaoException("Didnt found id");
                }
            }

        } catch (SQLException exception) {
            throw new DaoException(exception.getMessage());
        }
    }

    @Override
    public List<Course> findCoursesByStudentId(int studentId) {
        List<Course> result = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(findCoursesByStudentIdSql);
            while (rs.next()) {
                Integer courseId = rs.getObject("course_id", Integer.class);
                String courseName = rs.getString("course_name");
                String courseDescription = rs.getString("course_description");
                result.add(new Course(courseId, courseName, courseDescription));
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }

    }
}