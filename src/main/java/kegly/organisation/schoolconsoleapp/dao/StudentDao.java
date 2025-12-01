package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.List;

public interface StudentDao {
     void save(Student student);

     List<Student> findAll();

     void addCourseToStudent(int studentId, int courseId);

     void deleteById(int studentId);

     void removeStudentFromCourse(int studentId, int courseId);

     List<Student> findStudentsByCourseName(String courseName);

    void update(Student student);
}
