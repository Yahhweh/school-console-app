package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.List;

public interface StudentDao {
    void save(Student student);

    List<Student> findAll();

    void addCourseToStudent(int studentId, int courseId);

    void deleteById(int studentId);

    void removeFromCourse(int studentId, int courseId);

    List<Student> findByCourseName(String courseName);

    void update(Student student);
}
