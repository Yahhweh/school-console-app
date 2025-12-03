package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.entity.Course;

import java.util.List;

public interface CourseDao {
    List<Course> findAll();

    void save(Course course);

    List<Course> findByStudentId(int studentId);
}
