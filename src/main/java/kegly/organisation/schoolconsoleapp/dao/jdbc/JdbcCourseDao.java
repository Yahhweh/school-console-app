package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.DaoException;
import kegly.organisation.schoolconsoleapp.db.ConnectionProvider;
import kegly.organisation.schoolconsoleapp.entity.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcCourseDao implements CourseDao {
    private static final String FIND_COURSES_BY_STUDENT_ID_SQL =
        "SELECT c.course_id, c.course_name, c.course_description " +
            "FROM courses c " +
            "JOIN student_courses sc ON c.course_id = sc.course_id " +
            "WHERE sc.student_id = ?";

    private final static String FIND_SQL = "SELECT * FROM courses";
    private final static String SAVE_SQL = "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";

    private final ConnectionProvider dbConnectionProvider;

    public JdbcCourseDao(ConnectionProvider ConnectionProvider) {
        this.dbConnectionProvider = ConnectionProvider;
    }

    @Override
    public List<Course> findAll() {

        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement st = connection.prepareStatement(FIND_SQL)) {

            List<Course> result = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
            return result;

        } catch (SQLException exception) {
            throw new DaoException("Failed to find all courses", exception);
        }
    }

    @Override
    public void save(Course course) {
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement st = connection.prepareStatement(SAVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            st.setString(1, course.getName());
            st.setString(2, course.getDescription());

            st.executeUpdate();

            try (ResultSet resultSet = st.getGeneratedKeys()) {
                if (resultSet.next()) {
                    course.setId(resultSet.getInt(1));
                } else {
                    throw new DaoException("Didnt found id");
                }
            }

        } catch (SQLException exception) {
            throw new DaoException(exception.getMessage());
        }
    }

    @Override
    public List<Course> findByStudentId(int studentId) {
        try (Connection connection = dbConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_COURSES_BY_STUDENT_ID_SQL)) {

            statement.setInt(1, studentId);
            return parseMapRow(statement);

            }catch (SQLException e) {
            throw new DaoException("Failed to find courses for student ID: " + studentId, e);
        }
    }

    private List<Course> parseMapRow(PreparedStatement statement) {
        List<Course> result = new ArrayList<>();
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Data access error", e);
        }
        return result;
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        Integer courseId = rs.getObject("course_id", Integer.class);
        String courseName = rs.getString("course_name");
        String courseDescription = rs.getString("course_description");
        return new Course(courseId, courseName, courseDescription);
    }
}