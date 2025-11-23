package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Courses {

    int course_id;
    String course_name;
    String course_description;

    public Courses(int course_id, String course_name, String course_description) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_description = course_description;
    }
}
