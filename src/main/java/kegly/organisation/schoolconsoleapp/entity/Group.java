package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Group {

    private Integer group_id;
    private String group_name;

    public Group(String group_name) {
        this.group_name = group_name;
    }
}
