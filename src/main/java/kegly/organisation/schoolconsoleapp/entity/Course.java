package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Course {

    private Integer id;
    private String name;
    private String description;

    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
