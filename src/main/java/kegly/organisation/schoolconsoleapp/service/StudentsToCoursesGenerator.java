package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.entity.Course;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StudentsToCoursesGenerator implements DataGenerator {

    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final int minCoursesPerStudent;
    private final int maxCoursesPerStudent;
    private final Random random;

    public StudentsToCoursesGenerator(StudentDao studentDao,
                                      CourseDao courseDao,
                                      int minCoursesPerStudent,
                                      int maxCoursesPerStudent) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.minCoursesPerStudent = minCoursesPerStudent;
        this.maxCoursesPerStudent = maxCoursesPerStudent;
        this.random = new Random();
    }

    @Override
    public void generate(int amount) {
        List<Student> students = studentDao.findAll();
        List<Course> allCourses = courseDao.findAll();

        if (students.isEmpty() || allCourses.isEmpty()) {
            System.out.println("Seeding aborted: No students or courses found in database.");
            return;
        }

        List<Integer> allCourseIds = allCourses.stream()
            .map(Course::getId)
            .collect(Collectors.toList());

        int limit = Math.min(amount, students.size());

        for (int i = 0; i < limit; i++) {
            Student student = students.get(i);
            int quantityCourses = random.nextInt(maxCoursesPerStudent - minCoursesPerStudent + 1) + minCoursesPerStudent;

            assignCourses(student.getId(), quantityCourses, allCourseIds);
        }
    }

    private void assignCourses(int studentId, int quantityCourses, List<Integer> sourceCourseIds) {
        List<Integer> availableIds = new ArrayList<>(sourceCourseIds);

        for (int j = 0; j < quantityCourses; j++) {
            if (availableIds.isEmpty()) {
                break;
            }

            int randomIndex = random.nextInt(availableIds.size());
            Integer courseId = availableIds.get(randomIndex);

            availableIds.remove(randomIndex);

            studentDao.addCourseToStudent(studentId, courseId);
        }
    }
}