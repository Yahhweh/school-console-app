package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    private final DBConnection DBConnection;

    public StudentDao(DBConnection DBConnection) {
        this.DBConnection = DBConnection;
    }

    public void save(Student student) {
        String sql = "Insert into students (group_id, first_name, last_name) values (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, student.getGroupId(), Types.INTEGER);
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to save student", e);
        }
    }

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "select * from students";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

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

    public void addCourseToStudent(int studentId, int courseId) {
        String sql = "Insert into student_courses(student_id, course_id) VALUES(?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, studentId, Types.INTEGER);
            statement.setObject(2, courseId, Types.INTEGER);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public void addIdGroups(int student_id, int group_id) {
        String sql = "UPDATE students set group_id = ? where(student_id = ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, group_id, Types.INTEGER);
            statement.setObject(2, student_id, Types.INTEGER);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public void deleteById(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Info: Student with ID " + studentId + " not found.");
            }

        } catch (SQLException e) {
            throw new DaoException("Failed to delete student with ID: " + studentId, e);
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        String sql = "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to remove student from course", e);
        }
    }

    public List<Student> findStudentsByCourseName(String courseName) {
        List<Student> students = new ArrayList<>();
        String sql = """
            SELECT s.student_id, s.group_id, s.first_name, s.last_name
            FROM students s
            JOIN student_courses sc ON s.student_id = sc.student_id
            JOIN courses c ON sc.course_id = c.course_id
            WHERE c.course_name = ?
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

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

    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        List<Group> groups = new ArrayList<>();

        String sql = """
        SELECT g.group_id, g.group_name
        FROM groups g
        LEFT JOIN students s ON g.group_id = s.group_id
        GROUP BY g.group_id, g.group_name
        HAVING COUNT(s.student_id) <= ?
    """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, count);

            try (ResultSet resultSet = st.executeQuery()) {
                while (resultSet.next()) {
                    groups.add(new Group(
                        resultSet.getInt("group_id"),
                        resultSet.getString("group_name")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find groups with student count constraint", e);
        }
        return groups;
    }
}