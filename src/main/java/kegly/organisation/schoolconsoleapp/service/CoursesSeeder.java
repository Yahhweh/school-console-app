package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Course;

import java.util.List;

public class InitialCourses implements InitialData {

    CourseDaoImpl courseDaoImpl;

    public InitialCourses(CourseDaoImpl courseDaoImpl) {
        this.courseDaoImpl = courseDaoImpl;
    }

    @Override
    public void generate() {
        List<String> SUBJECTS = List.of(
            "Mathematics",
            "Physics",
            "Chemistry",
            "Biology",
            "Geography",
            "History",
            "Information Technologies",
            "Literature",
            "English Language",
            "Computer Graphics"
        );

        for (int i = 0; i < SUBJECTS.size(); i++) {
            courseDaoImpl.save(new Course(SUBJECTS.get(i), generateDescription(SUBJECTS.get(i))));
        }
    }

    private String generateDescription(String subject) {
        return "It is " + subject + ". You will study this subject the whole year";
    }
}
