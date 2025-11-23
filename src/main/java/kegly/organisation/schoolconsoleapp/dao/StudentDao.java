package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    private final ConnectionClass connectionClass;

    public StudentDao(ConnectionClass connectionClass) {
        this.connectionClass = connectionClass;
    }

    public void save(Student student) {
        String sql = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";

        try (Connection connection = connectionClass.getConnection();
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
        String sql = "SELECT * FROM students";

        try (Connection connection = connectionClass.getConnection();
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
}