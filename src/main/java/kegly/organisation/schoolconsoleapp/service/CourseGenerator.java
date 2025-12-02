package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourse;
import kegly.organisation.schoolconsoleapp.entity.Course;

import java.util.List;

public class CourseGenerator implements Seeder {

    private static final List<String> subjects = List.of(
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
    private CourseDao jdbcCourse;

    public CourseGenerator(JdbcCourse jdbcCourse) {
        this.jdbcCourse = jdbcCourse;
    }

    @Override
    public void generate(int amount ) {
        for (int i = 0; i < amount; i++) {
            jdbcCourse.save(new Course(subjects.get(i), generateDescription(subjects.get(i))));
        }
    }

    private String generateDescription(String subject) {
        return "It is " + subject + ". You will study this subject the whole year";
    }
}
