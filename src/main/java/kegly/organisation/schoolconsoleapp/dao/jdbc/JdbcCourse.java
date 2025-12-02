package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.dao.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcCourse implements CourseDao {

    private final static String FIND_COURSES_BY_STUDENT_ID_SQL = "SELECT course_id FROM student_courses WHERE (studentId = ?)";
    private final static String FIND_SQL = "SELECT * FROM courses";
    private final static String SAVE_SQL = "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";

    private final DBConnection dBConnection;

    public JdbcCourse(DBConnection DBConnection) {
        this.dBConnection = DBConnection;
    }

    @Override
    public List<Course> findAll() {

        try (Connection connection = dBConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(FIND_SQL)) {

            List<Course> result = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
            return result;

        } catch (SQLException exception) {
            throw new DaoException(exception.toString());
        }
    }

    @Override
    public void save(Course course) {
        try (Connection connection = dBConnection.getConnection()) {

            PreparedStatement st = connection.prepareStatement(SAVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);

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
    public List<Course> findCoursesByStudentId(int studentId) {
        try (Connection connection = dBConnection.getConnection();) {

            PreparedStatement statement = connection.prepareStatement(FIND_COURSES_BY_STUDENT_ID_SQL);
            List<Course> result = createResultSet(statement);
            return result;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    private List<Course> createResultSet(PreparedStatement statement) {
        List<Course> result = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
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