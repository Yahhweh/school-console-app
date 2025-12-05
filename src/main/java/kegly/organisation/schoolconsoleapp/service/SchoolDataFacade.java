package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.List;

public class SchoolDataFacade {

    private static final int COURSE_AMOUNT = 10;
    private static final int GROUPS_AMOUNT = 10;
    private static final int studentsAmount = 200;
    private static final int MIN_COURSES_PER_STUDENT = 1;
    private static final int MAX_COURSES_PER_STUDENT = 3;
    private final StudentDao studentDao;
    private final GroupDao groupDao;
    private final CourseDao courseDao;

    public SchoolDataFacade(StudentDao studentDao, GroupDao groupDao, CourseDao courseDao) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
    }

    public void initializeDatabase() {
        System.out.println("Initializing database schema");

        System.out.println("Generating data");
        new CourseGenerator(courseDao).generate(COURSE_AMOUNT);
        new GroupsGenerator(groupDao).generate(GROUPS_AMOUNT);
        StudentsGenerator studentsGenerator = new StudentsGenerator(studentDao, groupDao);
        studentsGenerator.generate(studentsAmount);
        studentsGenerator.assignRandomGroups(10, 30);

        new StudentsToCoursesGenerator(
            studentDao,
            courseDao,
            MIN_COURSES_PER_STUDENT,
            MAX_COURSES_PER_STUDENT
        ).generate(studentsAmount);

        System.out.println("Data initialization completed.\n");

    }

    public List<Group> findGroupsWithLessOrEqualStudents(int count) {
        return groupDao.findWithLessOrEqualStudents(count);
    }

    public List<Student> findByCourseName(String courseName) {
        return studentDao.findByCourseName(courseName);
    }

    public void addNewStudent(String firstName, String lastName, Integer groupId) {
        studentDao.save(new Student(groupId, firstName, lastName));
    }

    public void deleteStudentById(int studentId) {
        studentDao.deleteById(studentId);
    }

    public void addStudentToCourse(int studentId, int courseId) {
        studentDao.addCourseToStudent(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        studentDao.removeFromCourse(studentId, courseId);
    }
}