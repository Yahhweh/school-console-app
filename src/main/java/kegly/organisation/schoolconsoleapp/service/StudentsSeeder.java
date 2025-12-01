package kegly.organisation.schoolconsoleapp.service;

import kegly.organisation.schoolconsoleapp.dao.jdbc.GroupJdbc;
import kegly.organisation.schoolconsoleapp.dao.jdbc.StudentJdbc;
import kegly.organisation.schoolconsoleapp.entity.Group;
import kegly.organisation.schoolconsoleapp.entity.Student;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class StudentsSeeder implements Seeder {

    private final StudentJdbc studentJdbc;
    private final GroupJdbc groupJdbc;
    private final Random random = new Random();

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

    public StudentsSeeder(StudentJdbc studentJdbc, GroupJdbc groupJdbc) {
        this.studentJdbc = studentJdbc;
        this.groupJdbc = groupJdbc;
    }

    @Override
    public void generate(int amount) {
        for (int i = 0; i < amount; i++) {
            String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
            String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));

            studentJdbc.save(new Student(null, null, firstName, lastName));
        }
    }

    public void assignRandomGroups(int minStudents, int maxStudents) {
        List<Group> groups = groupJdbc.findAll();
        List<Student> allStudents = studentJdbc.findAll();

        Queue<Student> studentQueue = new ArrayDeque<>(allStudents);

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

                if (student != null) {
                    studentJdbc.update(student);
                }
            }
        }
    }
}