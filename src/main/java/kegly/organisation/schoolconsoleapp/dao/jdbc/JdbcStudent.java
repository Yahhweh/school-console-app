package kegly.organisation.schoolconsoleapp.dao.jdbc;

import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.dao.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcStudent implements StudentDao {

    private static final String SAVE_SQL                       = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String FIND_ALL_SQL                   = "SELECT * FROM students";
    private static final String UPDATE_SQL                     = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String DELETE_BY_ID_SQL               = "DELETE FROM students WHERE student_id = ?";

    private static final String REMOVE_STUDENT_FROM_COURSE_SQL = "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?";
    private static final String ADD_COURSE_TO_STUDENT_SQL      = "INSERT INTO student_courses(student_id, course_id) VALUES(?, ?)";

    private static final String FIND_BY_COURSE_NAME_SQL = """
        SELECT s.student_id, s.group_id, s.first_name, s.last_name
        FROM students s
        JOIN student_courses sc ON s.student_id = sc.student_id
        JOIN courses c ON sc.course_id = c.course_id
        WHERE c.course_name = ?
        """;

    private final DBConnection DBConnection;

    public JdbcStudent(DBConnection DBConnection) {
        this.DBConnection = DBConnection;
    }

    @Override
    public void save(Student student) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {

            statement.setObject(1, student.getGroupId(), Types.INTEGER);
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to save student", e);
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Integer studentId = rs.getObject("student_id", Integer.class);
                Integer groupId = rs.getObject("group_id", Integer.class);
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                students.add(new Student(studentId, groupId, firstName, lastName));
            }
            return students;

        } catch (SQLException e) {
            throw new DaoException("Failed to find all students", e);
        }
    }

    @Override
    public void addCourseToStudent(int studentId, int courseId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_COURSE_TO_STUDENT_SQL)) {

            statement.setObject(1, studentId, Types.INTEGER);
            statement.setObject(2, courseId, Types.INTEGER);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public void deleteById(int studentId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {

            statement.setInt(1, studentId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Info: Student with ID " + studentId + " not found.");
            }

        } catch (SQLException e) {
            throw new DaoException("Failed to delete student with ID: " + studentId, e);
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(REMOVE_STUDENT_FROM_COURSE_SQL)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to remove student from course", e);
        }
    }

    @Override
    public List<Student> findByCourseName(String courseName) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_COURSE_NAME_SQL)) {

            statement.setString(1, courseName);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getObject("group_id", Integer.class),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                    ));
                }
            }
            return students;

        } catch (SQLException e) {
            throw new DaoException("Failed to find students by course name", e);
        }
    }

    @Override
    public void update(Student student) {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setObject(1, student.getGroupId(), Types.INTEGER);
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.setObject(4, student.getId(), Types.INTEGER);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


}