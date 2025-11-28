package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;
import kegly.organisation.schoolconsoleapp.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface StudentDao {
     void save(Student student);

     List<Student> findAll();

     void addCourseToStudent(int studentId, int courseId);

     void deleteById(int studentId);

     void removeStudentFromCourse(int studentId, int courseId);

     List<Student> findStudentsByCourseName(String courseName);

     List<Group> findGroupsWithLessOrEqualStudents(int count);

     void update(Student student);
}
