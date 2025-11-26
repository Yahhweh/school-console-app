package kegly.organisation.schoolconsoleapp.entity;

import lombok.Data;

@Data
public class Group {
    private Integer groupId;
    private String groupName;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(Integer groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}