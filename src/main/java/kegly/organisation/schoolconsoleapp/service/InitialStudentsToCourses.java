package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.StudentDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InitialStudentsToCourses implements InitialData {

    private final StudentDao studentDao;

    public InitialStudentsToCourses(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public void generate() {
        int maxCoursesPerStudent = 3;
        int minCoursesPerStudent = 1;
        Random random = new Random();

        int studentQuantity = 200;

        for (int i = 0; i < studentQuantity; i++) {
            int quantityCourses = random.nextInt(maxCoursesPerStudent - minCoursesPerStudent + 1) + minCoursesPerStudent;

            int studentId = i + 1;

            assignCourses(studentId, quantityCourses);
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

            studentDao.addCourseToStudent(studentId, courseId);
        }
    }
}