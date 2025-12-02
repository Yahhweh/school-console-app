package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Group {
    private Integer id;
    private String name;

    public Group(String name) {
        this.name = name;
    }

    public Group(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}