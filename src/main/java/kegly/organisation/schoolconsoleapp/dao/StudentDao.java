package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface StudentDao {
    public void save(Student student);

    public List<Student> findAll();

    public void addCourseToStudent(int studentId, int courseId);

    public void addIdGroups(int student_id, int group_id);

    public void deleteById(int studentId);

    public void removeStudentFromCourse(int studentId, int courseId);

    public List<Student> findStudentsByCourseName(String courseName);

    public List<Group> findGroupsWithLessOrEqualStudents(int count);
}
