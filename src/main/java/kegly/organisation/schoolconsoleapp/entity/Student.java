package kegly.organisation.schoolconsoleapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {

    private Integer studentId;
    private Integer groupId;
    private String firstName;
    private String lastName;

    public Student(Integer groupId, String firstName, String lastName) {
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}