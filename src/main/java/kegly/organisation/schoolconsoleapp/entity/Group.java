package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Group {
    private Integer group_id;
    private String group_name;

    public Group(String groupName) {
        this.group_name = groupName;
    }

    public Group(Integer groupId, String groupName) {
        this.group_id = groupId;
        this.group_name = groupName;
    }
}