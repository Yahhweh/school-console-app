package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.GroupDaoImpl;
import kegly.organisation.schoolconsoleapp.dao.StudentDaoImpl;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class InitialStudents implements InitialData {

    private final StudentDaoImpl studentDaoImpl;
    private final GroupDaoImpl groupDaoImpl;

    public InitialStudents(StudentDaoImpl studentDaoImpl, GroupDaoImpl groupDaoImpl) {
        this.studentDaoImpl = studentDaoImpl;
        this.groupDaoImpl = groupDaoImpl;
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

            studentDaoImpl.save(new Student(null, firstName, lastName));
        }
    }

    public void assignRandomGroups() {
        List<Group> groups = groupDaoImpl.findAll();

        List<Student> allStudents = studentDaoImpl.findAll();
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

                studentDaoImpl.addIdGroups(student.getStudentId(), group.getGroup_id());
            }
        }
    }
}