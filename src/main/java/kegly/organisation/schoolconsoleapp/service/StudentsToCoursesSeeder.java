package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentsToCoursesSeeder implements Seeder {

    private final StudentDaoImpl studentDaoImpl;

    public StudentsToCoursesSeeder(StudentDaoImpl studentDaoImpl) {
        this.studentDaoImpl = studentDaoImpl;
    }

    @Override
    public void generate(int amount) {
        int maxCoursesPerStudent = 3;
        int minCoursesPerStudent = 1;
        Random random = new Random();
        List<Student> students = studentDaoImpl.findAll();


        for (int i = 0; i < amount; i++) {
            int quantityCourses = random.nextInt(maxCoursesPerStudent - minCoursesPerStudent + 1) + minCoursesPerStudent;

            //fix it later
            assignCourses(i+1, quantityCourses);
        }
    }

    private void assignCourses(int studentId, int quantityCourses) {
        List<Integer> availableCourseIds = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        Random random = new Random();

        for (int j = 0; j < quantityCourses; j++) {
            if (availableCourseIds.isEmpty()) {
                break;
            }

            int randomIndex = random.nextInt(availableCourseIds.size());

            Integer courseId = availableCourseIds.get(randomIndex);

            availableCourseIds.remove(randomIndex);

            studentDaoImpl.addCourseToStudent(studentId, courseId);
        }
    }
}