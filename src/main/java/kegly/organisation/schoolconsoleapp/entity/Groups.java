package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Groups {

    int group_id;
    String group_name;

    public Groups(int group_id, String group_name) {
        this.group_id = group_id;
        this.group_name = group_name;
    }
}
