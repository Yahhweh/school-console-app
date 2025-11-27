package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Course;

import java.util.List;

public class CoursesSeeder implements Seeder {

    private CourseDaoImpl courseDaoImpl;

    public CoursesSeeder(CourseDaoImpl courseDaoImpl) {
        this.courseDaoImpl = courseDaoImpl;
    }

    @Override
    public void generate(int amount ) {
        List<String> subjects = List.of(
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

        for (int i = 0; i < amount; i++) {
            courseDaoImpl.save(new Course(subjects.get(i), generateDescription(subjects.get(i))));
        }
    }

    private String generateDescription(String subject) {
        return "It is " + subject + ". You will study this subject the whole year";
    }
}
