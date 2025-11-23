package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Course {

    private Integer course_id;
    private String course_name;
    private String course_description;

    public Course(String course_name, String course_description) {
        this.course_name = course_name;
        this.course_description = course_description;
    }
}
