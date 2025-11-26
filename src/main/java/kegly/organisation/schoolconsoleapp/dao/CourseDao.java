package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.entity.Course;

import java.util.List;

public interface CourseDao {
    public List<Course> findAll();
    public void save(Course course);
}
