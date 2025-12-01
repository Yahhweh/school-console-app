package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseJdbc implements CourseDao {

    final String FIND_COURSES_BY_STUDENT_ID_SQL = "SELECT course_id FROM student_courses WHERE (studentId = ?)";
    final String FIND_SQL = "SELECT * FROM courses";
    final String SAVE_SQL = "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";

    private final DBConnection dBConnection;

    public CourseJdbc(DBConnection DBConnection) {
        this.dBConnection = DBConnection;
    }

    @Override
    public List<Course> findAll() {

        try (Connection connection = dBConnection.getConnection()) {

            List<Course> result = new ArrayList<>();

            PreparedStatement st = connection.prepareStatement(FIND_SQL);

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
        try (Connection connection = dBConnection.getConnection()) {

            PreparedStatement st = connection.prepareStatement(SAVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);

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
                Integer courseId = rs.getObject("course_id", Integer.class);
                String courseName = rs.getString("course_name");
                String courseDescription = rs.getString("course_description");
                result.add(new Course(courseId, courseName, courseDescription));
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return result;
    }
}