package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Course {

    private Integer courseId;
    private String courseName;
    private String courseDescription;

    public Course(String courseName, String courseDescription) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }
}
