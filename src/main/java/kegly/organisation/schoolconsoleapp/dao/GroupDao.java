package kegly.organisation.schoolconsoleapp.dao;

import kegly.organisation.schoolconsoleapp.entity.Group;

import java.util.List;

public interface GroupDao {
    List<Group> findAll();

    void save(Group group);

    List<Group> findWithLessOrEqualStudents(int count);
}
