package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Student {

    int student_id;
    int group_id;
    String first_name;
    String last_name;

    public Student(int student_id, int group_id, String first_name, String last_name) {
        this.student_id = student_id;
        this.group_id = group_id;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
