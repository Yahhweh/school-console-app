package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class InitialStudents implements InitialData {

    private final StudentDao studentDao;
    private final GroupDao groupDao;

    public InitialStudents(StudentDao studentDao, GroupDao groupDao) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
    }

    private static final List<String> FIRST_NAMES = List.of(
        "Liam", "Noah", "Oliver", "Elijah", "James",
        "William", "Benjamin", "Lucas", "Henry", "Alexander",
        "Mason", "Michael", "Ethan", "Daniel", "Jacob",
        "Logan", "Jackson", "Levi", "Sebastian", "Mateo"
    );

    private static final List<String> LAST_NAMES = List.of(
        "Smith", "Johnson", "Williams", "Brown", "Jones",
        "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
        "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin"
    );


    @Override
    public void generate() {
        int AMOUNT_STUDENTS = 200;
        Random random = new Random();

        for (int i = 0; i < AMOUNT_STUDENTS; i++) {
            String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));

            studentDao.save(new Student(null, firstName, lastName));
        }
    }

    public void assignRandomGroups() {
        List<Group> groups = groupDao.findAll();

        List<Student> allStudents = studentDao.findAll();
        Queue<Student> studentQueue = new ArrayDeque<>(allStudents);

        Random random = new Random();
        int minStudents = 10;
        int maxStudents = 30;

        for (Group group : groups) {
            if (studentQueue.isEmpty()) {
                break;
            }

            int count = random.nextInt(maxStudents - minStudents + 1) + minStudents;

            for (int i = 0; i < count; i++) {
                if (studentQueue.isEmpty()) {
                    break;
                }

                Student student = studentQueue.poll();

                studentDao.addIdGroups(student.getStudentId(), group.getGroup_id());
            }
        }
    }
}