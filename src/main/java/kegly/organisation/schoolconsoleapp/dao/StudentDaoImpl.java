package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.DBConnection;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements  StudentDao{

    private final DBConnection DBConnection;
    private static final String saveSql = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String findAllSql ="SELECT * FROM students";
    private static final String addCourseToStudentSql = "Insert INTO student_courses(student_id, course_id) VALUES(?, ?)";
    private static final String addIdGroupsSql = "UPDATE students SET group_id = ? WHERE(student_id = ?)";
    private static final String deleteByIdSql = "DELETE FROM students WHERE student_id = ?";
    private static final String removeStudentFromCourseSql = "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?";
    private static final String findStudentsByCourseNameSql = """
    SELECT s.student_id, s.group_id, s.first_name, s.last_name
    FROM students s
    JOIN student_courses sc ON s.student_id = sc.student_id
    JOIN courses c ON sc.course_id = c.course_id
    WHERE c.course_name = ?
""";
    private static final String findGroupsWithLessOrEqualStudentsSql = """
    SELECT g.group_id, g.group_name
    FROM groups g
    LEFT JOIN students s ON g.group_id = s.group_id
    GROUP BY g.group_id, g.group_name
    HAVING COUNT(s.student_id) <= ?
""";

    public StudentDaoImpl(DBConnection DBConnection) {
        this.DBConnection = DBConnection;
    }

    @Override
    public void save(Student student) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(saveSql)) {

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
             PreparedStatement statement = connection.prepareStatement(findAllSql);
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
             PreparedStatement statement = connection.prepareStatement(addCourseToStudentSql)) {

            statement.setObject(1, studentId, Types.INTEGER);
            statement.setObject(2, courseId, Types.INTEGER);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public void addIdGroups(int student_id, int group_id) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(addIdGroupsSql)) {
            statement.setObject(1, group_id, Types.INTEGER);
            statement.setObject(2, student_id, Types.INTEGER);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public void deleteById(int studentId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteByIdSql)) {

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
             PreparedStatement statement = connection.prepareStatement(removeStudentFromCourseSql)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to remove student from course", e);
        }
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(findStudentsByCourseNameSql)) {

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
    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(findGroupsWithLessOrEqualStudentsSql)) {

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