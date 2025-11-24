package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.db.ConnectionClass;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentDaoTest {

    @Mock private ConnectionClass connectionClass;
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private Statement statement;
    @Mock private ResultSet resultSet;

    private StudentDao studentDao;

    @BeforeEach
    void setUp() throws DaoException {
        studentDao = new StudentDao(connectionClass);
        lenient().when(connectionClass.getConnection()).thenReturn(connection);
    }

    @Test
    void save_ShouldExecuteInsert() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Student student = new Student(1, "John", "Doe");
        studentDao.save(student);

        verify(preparedStatement).setObject(1, 1, java.sql.Types.INTEGER);
        verify(preparedStatement).setString(2, "John");
        verify(preparedStatement).setString(3, "Doe");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void findAll_ShouldReturnListOfStudents() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getObject("student_id", Integer.class)).thenReturn(1);
        when(resultSet.getObject("group_id", Integer.class)).thenReturn(10);
        when(resultSet.getString("first_name")).thenReturn("Alice");
        when(resultSet.getString("last_name")).thenReturn("Smith");

        List<Student> result = studentDao.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
    }
}